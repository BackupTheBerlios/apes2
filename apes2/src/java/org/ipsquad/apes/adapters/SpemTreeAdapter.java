/*
 * APES is a Process Engineering Software
 * Copyright (C) 2003-2004 IPSquad
 * team@ipsquad.tuxfamily.org
 *
 *
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA  02111-1307, USA.
 */


package org.ipsquad.apes.adapters;

import java.util.HashMap;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.event.EventListenerList;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;
import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.UndoableEditSupport;

import org.ipsquad.apes.Identity;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.frontend.ChangeEvent;
import org.ipsquad.apes.model.frontend.Event;
import org.ipsquad.apes.model.frontend.InsertEvent;
import org.ipsquad.apes.model.frontend.MoveEvent;
import org.ipsquad.apes.model.frontend.RemoveEvent;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.IconManager;

/**
 * This adapter allows to display a process in a JTree
 *
 * @version $Revision: 1.11 $
 */
public class SpemTreeAdapter extends UndoableEditSupport implements TreeModel, ApesMediator.Listener
{
	private EventListenerList mListenerList = new EventListenerList();
	private ApesTreeNode mRoot = new ApesTreeNode(new ApesProcess("fake"), false);
	
	public SpemTreeAdapter( )
	{
		super();
	}
		
	/**
	 * Set the root of this tree model
	 *
	 * @param root the new root
	 */
	public void setRoot(ApesProcess root)
	{
		if( mRoot == null || mRoot.getUserObject() != root )
		{	
			mRoot = new ApesTreeNode(root, false);
			Object[] path = {mRoot};
			fireTreeStructureChanged(this, path, null, null);
		}
		//mRoot.addModelElement(new ProcessComponent("Component"));
		//fireTreeNodesInserted(this,path,new int[]{0},new Object[]{mRoot.getComponent()});
	}

	/**
	 * Set the root of this tree model
	 * 
	 * @param root the new root
	 */
	public void setRoot(ApesTreeNode root)
	{
		mRoot = root;
		fireTreeStructureChanged(this,new Object[]{mRoot},null,null);
	}
	
	/**
	 * Get the root of the tree model
	 *
	 * @return the root
	 */
	public Object getRoot()
	{
		return mRoot;
	}

	/**
	 * Get a child of a tree node by giving its index
	 *
	 * @param parent the node
	 * @param index the index of the child
	 * @return the chil or null if it does not exists
	 */
	public Object getChild(Object parent, int index)
	{	
		if( !(parent instanceof ApesTreeNode) )
		{
			return null;
		}
		TreeNode node = (TreeNode)parent;
		
		if( index < 0 || index >= node.getChildCount() )
		{
			return null;
		}
		
		return ((TreeNode)parent).getChildAt(index);
	}

	/**
	 * Get the number of child of a node
	 *
	 * @param parent the node
	 * @return the number of child
	 */
	public int getChildCount(Object parent)
	{
		if( parent instanceof TreeNode )
		{
			return ((TreeNode)parent).getChildCount();
		}
		return -1;
	}

	/**
	 * Check if a node is a leaf of the tree
	 *
	 * @param node the node
	 * @return true if the node is a leaf, false otherwise
	 */
	public boolean isLeaf(Object node)
	{
		return ((TreeNode)node).isLeaf();
	}

	/**
	 * Called when the value associated to a node has changed
	 *
	 * @param path the path to the modified node
	 * @param newValue the new value associated to the node
	 */
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		if( newValue instanceof String && path.getLastPathComponent() != mRoot 
				&& ! newValue.equals(path.getLastPathComponent().toString()))
		{
			change( (ApesTreeNode)path.getLastPathComponent(), (String)newValue );
		}
		
		if( newValue instanceof Map )
		{	
			ApesTreeNode node = (ApesTreeNode)path.getLastPathComponent();
			ApesTreeNode parent = (ApesTreeNode)node.getParent();
			Map undo = node.changeAttributes((Map)newValue);
			NodeChandeAttributes edit = new NodeChandeAttributes(node, undo);
			postEdit(edit);
			fireTreeNodesChanged(this,parent.getPath(),new int[]{parent.getIndex(node)},new Object[]{node});
		}
	}

	/**
	 * Find the index of a child in a node
	 *
	 * @param parent the node
	 * @param child the child to evaluate
	 * @return the index of the child or -1 if child is not in parent
	 */
	public int getIndexOfChild(Object parent, Object child)
	{
		if( parent instanceof TreeNode && child instanceof TreeNode )
		{	
			return ((TreeNode)parent).getIndex((TreeNode)child);
		}
		return -1;
	}

	/**
	 * Move an element into another
	 *
	 * @param element the element to move
	 * @param container the element which will contain the moved element
	 * @return true if the operation is allowed
	 */
	public void moveInto(Object element, Object container)
	{
		move( element, container );
	}	

	public TreeNode[] getPathToRoot(ApesTreeNode node)
	{
		return node.getPath();
	}
	
	/**
	 * Associate an icon to a node
	 *
	 * @param node the node to consider
	 * @return an icon representing the node
	 */
	public Icon associateIcon(Object node)
	{
		IconManager im = IconManager.getInstance();
		
		if(node instanceof ApesTreeNode)
		{
			Element e = (Element) ((ApesTreeNode)node).getUserObject();
			IconAssociater ia = new IconAssociater(im);

			e.visit(ia);

			return ia.getIcon();
		}
		else
		{
			if(Debug.enabled) Debug.print("associateIcon : Something unknown in the SpemTreeAdapter");
			return im.getIcon("icons/TreeUnknown.gif");
		}
	
	}

	/**
	 * Associate a menu to a node
	 *
	 * @param node the node to consider
	 * @return a popup menu for the operations on this node
	 */
	public JPopupMenu associateMenu(Object node)
	{
		if(node instanceof ApesTreeNode)
		{
			Element e = (Element)((ApesTreeNode)node).getUserObject();
			
			PopupMenuAssociater ma = new PopupMenuAssociater();

			e.visit(ma);

			return ma.getMenu();
		}
		else
		{
			if(Debug.enabled) Debug.print("associateMenu : Something unknown in the SpemTreeAdapter");
			return null;
		}
	
	}
	
	/**
	 * Execute an action associated to a node
	 *
	 * @param node the node to consider
	 */
	public void elementAction(Object node)
	{
		if(node instanceof ApesTreeNode)
		{
			Element e = (Element) ((ApesTreeNode)node).getUserObject();
			//ActionEdit edit = new ActionEdit(e);
			ActionAssociater aa = new ActionAssociater();
			e.visit(aa);
			//postEdit(edit);
		}
		else
		{
			if(Debug.enabled) Debug.print("elementAction : Something unknown in the SpemTreeAdapter");
		}
	}

	/**
	 * Find an element by its id
	 * 
	 * @param id the id of the element to find
	 * @return the corresponding node or null
	 */
	public ApesTreeNode findWithID(int id)
	{
		return findWithID(id, mRoot);
	}
	
	/**
	 * Recursive search by the id of an element
	 * 
	 * @param id the id of the element to find
	 * @param current the current node
	 * @return the corresponding node or null
	 */
	private ApesTreeNode findWithID(int id, ApesTreeNode current)
	{
		if( current instanceof Identity && ((Identity)current).getID() == id )
		{
			return current;
		}
		
		for(int i=0; i<getChildCount(current); i++)
		{
			ApesTreeNode node = (ApesTreeNode)getChild(current, i);
			
			node = findWithID(id, node);
			
			if(node!=null)
			{
				return node;
			}
		}
		
		return null;
	}

	/**
	 * Add a listener to this tree model
	 *
	 * @param l the listener to add
	 */
	public void addTreeModelListener(TreeModelListener l)
	{
		mListenerList.remove(TreeModelListener.class, l);
		mListenerList.add(TreeModelListener.class, l);
	}

	/**
	 * Remove a listener from this tree model
	 *
	 * @param l the listener to add
	 */
	public void removeTreeModelListener(TreeModelListener l)
	{
		mListenerList.remove(TreeModelListener.class, l);
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesChanged(Object source, Object[] path, int[] childIndices, Object[] children)
	{
		Object[] listeners = mListenerList.getListenerList();
		TreeModelEvent e = null;
		
		for(int i=listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==TreeModelListener.class)
			{
				if(e==null)
				{
					try{
					e=new TreeModelEvent(source, path, childIndices, children);
					}catch(Throwable t){}
				}
				((TreeModelListener)listeners[i+1]).treeNodesChanged(e);
			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesInserted(Object source, Object[] path, int[] childIndices, Object[] children)
	{
		Object[] listeners = mListenerList.getListenerList();
		TreeModelEvent e = null;
		
		for(int i=listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==TreeModelListener.class)
			{
				if(e==null)
				{
					try{
						e=new TreeModelEvent(source, path, childIndices, children);
					}catch(Throwable t){}
				}
				((TreeModelListener)listeners[i+1]).treeNodesInserted(e);
			}
		}
	}


	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeNodesRemoved(Object source, Object[] path, int[] childIndices, Object[] children)
	{
		Object[] listeners = mListenerList.getListenerList();
		TreeModelEvent e = null;
		
		for(int i=listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==TreeModelListener.class)
			{
				if(e==null)
				{
					e=new TreeModelEvent(source, path, childIndices, children);
				}
				((TreeModelListener)listeners[i+1]).treeNodesRemoved(e);

			}
		}
	}

	/**
	 * Notify all listeners that have registered interest for
	 * notification on this event type.  The event instance
	 * is lazily created using the parameters passed into
	 * the fire method.
	 * @see EventListenerList
	 */
	protected void fireTreeStructureChanged(Object source, Object[] path, int[] childIndices, Object[] children)
	{
		Object[] listeners = mListenerList.getListenerList();
		TreeModelEvent e = null;
		
		for(int i=listeners.length-2; i>=0; i-=2)
		{
			if (listeners[i]==TreeModelListener.class)
			{
				if(e==null)
				{
					e=new TreeModelEvent(source, path, childIndices, children);
				}
				((TreeModelListener)listeners[i+1]).treeStructureChanged(e);
			}
		}
	}

	/**
	 * Change the parent of a node
	 * 
	 * @param object the child
	 * @param container the newParent
	 */
	public void move( Object object, Object container )
	{
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::move "+object+" "+container);
		
		if( object instanceof ApesTreeNode && container instanceof ApesTreeNode )
		{	
			ApesTreeNode node = (ApesTreeNode)object;
			ApesTreeNode parent = (ApesTreeNode)container;
			
			Map attr = new HashMap();
			attr.put("oldParent",node.getParent());
			attr.put("newParent",container);
			attr.put(node.getUserObject(),node);
			
			ApesMediator.getInstance().update( 
					ApesMediator.getInstance().createMoveCommand( ((ApesTreeNode)node).getUserObject(), ((ApesTreeNode)container).getUserObject(), attr ) );
		}
	}
	
	/**
	 * Add a node to another
	 * 
	 * @param objectToAdd the added node
	 * @param parent the parent
	 */
	public void insert( ApesTreeNode objectToAdd, ApesTreeNode parent )
	{
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::insert "+objectToAdd+" "+parent);
		
		Map attr = new HashMap();
		attr.put(objectToAdd.getUserObject(), objectToAdd );
		attr.put("parent",parent);
		ApesMediator.getInstance().update( 
				ApesMediator.getInstance().createInsertCommand( objectToAdd.getUserObject(), parent.getUserObject(), attr ) );
	}
	
	/**
	 * Remove the specified node
	 * 
	 * @param nodeToRemove the node to remove
	 */
	public void remove( ApesTreeNode nodeToRemove )
	{
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::remove "+nodeToRemove);
		
		Map attr = new HashMap();
		attr.put(nodeToRemove.getUserObject(), nodeToRemove);
		
		ApesMediator.getInstance().update( 
			ApesMediator.getInstance().createRemoveCommand( new Object[]{ nodeToRemove.getUserObject() }, attr ) );
	}
	
	/**
	 * Change the name of a node
	 * 
	 * @param nodeToChange the node to change
	 * @param newValue the new name
	 */
	public void change( ApesTreeNode nodeToChange, String newValue )
	{		
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::change "+nodeToChange+" "+newValue);
		
		Map attr = new HashMap();
		attr.put(nodeToChange.getUserObject(),nodeToChange);
		ApesMediator.getInstance().update( 
				ApesMediator.getInstance().createChangeCommand( nodeToChange.getUserObject(), newValue, attr ) );
	}
	
	/**
	 * Invoked after an element has changed in some way.
	 */
	public void updated( Event e )
	{
		if( e instanceof InsertEvent )
		{
			inserted( (InsertEvent)e );
		}
		else if( e instanceof RemoveEvent )
		{
			removed( (RemoveEvent)e );
		}
		else if( e instanceof ChangeEvent )
		{
			changed( (ChangeEvent)e );
		}
		else if( e instanceof MoveEvent )
		{
			moved( (MoveEvent)e );
		}
	}
	
	/**
	 * A new element has been inserted in the model.
	 * 
	 * @param event the event corresponding to the change
	 */
	protected void inserted( InsertEvent event )
	{
		//System.out.println("tree insert "+event);
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::inserted "+event);
		
		// if the element already exist, do nothing
		if( !event.isAlreadyExistInModel() && event.getParent() != null )
		{
			ApesTreeNode  node = null;
			ApesTreeNode parent = null;
			
			// try to retrieve the attributes of the inserted element
			if( event.getAttributes() != null && event.getAttributes().containsKey(event.getInserted()) )
			{
				node = (ApesTreeNode)event.getAttributes().get(event.getInserted());
			}
			else
			{	
				//node = findNodeByUserObject( (ApesTreeNode)getRoot(), event.getInserted());
				node = (ApesTreeNode)findWithID(((Identity)event.getInserted()).getID());
			}
			// try to retrieve the attributes of the parent			
			if( event.getAttributes() != null && event.getAttributes().containsKey("parent") )
			{
				parent = (ApesTreeNode)event.getAttributes().get("parent");
			}
			else
			{	
				//parent = findNodeByUserObject((ApesTreeNode)getRoot(), event.getParent());
				parent = (ApesTreeNode)findWithID(((Identity)event.getParent()).getID());
			}
			// if the node doesn't exist, create it
			if( node == null )
			{
				node = new ApesTreeNode( event.getInserted(), true );
			}
			
			if( parent != null )
			{	
				if( node.getParent() == null )
				{
					parent.add(node);
				}
				// insert the element and create undo edit
				fireTreeNodesInserted( this, parent.getPath(), new int[]{ parent.getIndex(node) }, new Object[]{ node });
				
				NodeInsertedEdit edit = new NodeInsertedEdit( this, node, parent );
				postEdit( edit );
			}
		}
	}
	
	/**
	 * A new element has been removed in the model.
	 * 
	 * @param event the event corresponding to the change
	 */
	protected void removed( RemoveEvent event )
	{
		//System.out.println("tree remove "+event);
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::removed "+event);
		
		//if the remove concerns a diagram, do nothing
		if( event.getDiagram() == null )
		{
			//Only remove the parent of the removed elements
			Element element = (Element)event.getElements()[event.getElements().length-1];
			ApesTreeNode node = null;
			ApesTreeNode parent = null;
			
			//try to retrieve the attributes of the removed element
			if( event.getAttributes() != null && event.getAttributes().containsKey(element) )
			{
				node = (ApesTreeNode) event.getAttributes().get(element);
				parent = (ApesTreeNode)node.getParent();
			}
			else
			{
				node = (ApesTreeNode)findWithID(element.getID());
				parent = (ApesTreeNode)findWithID(((Identity)event.getParents().get(element)).getID());
			}
			
			if( node != null && parent != null )
			{	
				int[] indices = new int[]{ node.getParent().getIndex(node) };
				
				parent.remove(node);
				
				fireTreeNodesRemoved(this, parent.getPath(), indices, new Object[]{node} );
			
				NodeRemovedEdit edit = new NodeRemovedEdit( this, node, parent );
				postEdit( edit );
			}
		}
	}
	
	/**
	 * A new element has been changed in the model.
	 * 
	 * @param event  the event corresponding to the change
	 */
	protected void changed( ChangeEvent event )
	{
		//System.out.println("tree edit : "+e);
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::changed "+event);
		
		if( event.getAttributes() != null && event.getAttributes().containsKey(event.getElement()))
		{
			ApesTreeNode node = null;
			
			if( event.getElement() instanceof ApesTreeNode )
			{	
				node = (ApesTreeNode)event.getAttributes().get(event.getElement());
			}
			else
			{
				//node = findNodeByUserObject(mRoot, e.getElement());
				node = (ApesTreeNode)findWithID(((Identity)event.getElement()).getID());
			}
			
			if( node != null )
			{	
				ApesTreeNode parent = (ApesTreeNode)node.getParent();
			
				if(parent != null)
				{
					fireTreeNodesChanged( this, parent.getPath(), new int[]{ parent.getIndex(node) }, new Object[]{ node } );
				}
				NodeChangedEdit edit = new NodeChangedEdit( node );
				
				postEdit( edit );
			}
		}
	}
	
	/**
	 * A new element has been moved in the model.
	 * 
	 * @param event  the event corresponding to the change
	 */
	protected void moved( MoveEvent event )
	{
		//System.out.println("tree move "+e.getAttributes() );
		if(Debug.enabled) Debug.print("(A) -> SpemTreeAdapter::moved "+event);
		
		if( event.getAttributes() != null 
				&& event.getAttributes().containsKey("oldParent")
				&& event.getAttributes().containsKey("newParent")
				&& event.getAttributes().containsKey(event.getElement())
		)
		{	
			ApesTreeNode oldParent =  (ApesTreeNode)event.getAttributes().get("oldParent");
			ApesTreeNode newParent =  (ApesTreeNode)event.getAttributes().get("newParent");
			ApesTreeNode node =  (ApesTreeNode)event.getAttributes().get(event.getElement());
			
			int[] indices = new int[]{oldParent.getIndex(node)};
									
			oldParent.remove(node);
			newParent.add(node);
			
			fireTreeNodesRemoved(this, oldParent.getPath(), indices, new Object[]{ node } );
			fireTreeNodesInserted(this, newParent.getPath(), new int[]{ newParent.getIndex(node) }, new Object[]{ node } );
		
			NodeMovedEdit edit = new NodeMovedEdit( this, node, oldParent, newParent );
			postEdit( edit );
		}
	}
	
	/**
	 * An object representing an inserting that has been done, and that can be undone and redone.
	 */
	private class NodeInsertedEdit extends AbstractUndoableEdit
	{
		private SpemTreeAdapter mSource;
		private ApesTreeNode mParent;
		private ApesTreeNode mNode;
		
		public NodeInsertedEdit( SpemTreeAdapter source, ApesTreeNode node, ApesTreeNode parent )
		{
			mSource = source;
			mNode = node;
			mParent = parent;
		}

		public void undo()
		{
			super.undo();
			int[] indices = new int[]{ mParent.getIndex(mNode) };
			
			mParent.remove(mNode);
			fireTreeNodesRemoved( mSource, mParent.getPath() , indices, new Object[]{ mNode });
		}

		public void redo()
		{
			super.redo();
			
			mParent.add(mNode);
			fireTreeNodesInserted( mSource, mParent.getPath(), new int[]{ mParent.getIndex(mNode) }, new Object[]{ mNode }); 
		}
	}
	
	/**
	 * An object representing a removing that has been done, and that can be undone and redone.
	 */
	private class NodeRemovedEdit extends AbstractUndoableEdit
	{
		private SpemTreeAdapter mSource;
		private ApesTreeNode mNode;
		private ApesTreeNode mOldParent;
		
		public NodeRemovedEdit( SpemTreeAdapter source, ApesTreeNode node, ApesTreeNode oldParent )
		{
			mSource = source;
			mNode = node;
			mOldParent = oldParent;
		}

		public void undo()
		{
			super.undo();
			mOldParent.add(mNode);
			fireTreeNodesInserted( mSource, mOldParent.getPath() , new int[]{ mOldParent.getIndex(mNode) }, new Object[]{ mNode });
		}

		public void redo()
		{
			super.redo();
			int[] indices = new int[]{ mOldParent.getIndex(mNode) };
			mOldParent.remove(mNode);
			fireTreeNodesRemoved( mSource, mOldParent.getPath(), indices, new Object[]{ mNode });
		}
	}

	 /**
	 * An object representing a moving that has been done, and that can be undone and redone.
	 */
	public class NodeMovedEdit extends AbstractUndoableEdit
	{
		private SpemTreeAdapter mSource;
		private ApesTreeNode mNode;
		private ApesTreeNode mOldParent;
		private ApesTreeNode mNewParent;
		
		public NodeMovedEdit( SpemTreeAdapter source, ApesTreeNode node, ApesTreeNode oldParent, ApesTreeNode newParent )
		{
			mSource = source;
			mNode = node;
			mOldParent = oldParent;
			mNewParent = newParent;
		}
		
		private void move( ApesTreeNode newParent )
		{
			ApesTreeNode parent = (ApesTreeNode)mNode.getParent();
			int[] indices = new int[]{ parent.getIndex(mNode) };
			parent.remove(mNode);
			newParent.add(mNode);
			fireTreeNodesRemoved( mSource, parent.getPath(), indices, new Object[]{ mNode } );
			fireTreeNodesInserted( mSource, newParent.getPath(), new int[]{ newParent.getIndex(mNode) }, new Object[]{ mNode } );
		}
		
		public void undo() throws CannotUndoException
		{
			super.undo();
			move(mOldParent);
		}

		public void redo() throws CannotRedoException
		{
			super.redo();
			move(mNewParent);
		}
	}
	
	/**
	 * An object representing a changing that has been done, and that can be undone and redone.
	 */
	public class NodeChangedEdit extends AbstractUndoableEdit
	{
		private TreePath mPath;
		private ApesTreeNode mNode;
		
		public NodeChangedEdit(ApesTreeNode node )
		{
			mNode = node;
		}
		
		private void change()
		{
			ApesTreeNode parent = (ApesTreeNode)mNode.getParent();
			fireTreeNodesChanged(this, parent.getPath(), new int[]{ parent.getIndex(mNode) }, new Object[]{ mNode } );
		}
		
		public void undo() throws CannotUndoException
		{
			super.undo();
			change();
		}
		
		public void redo() throws CannotRedoException
		{
			super.redo();
			change();
		}
	}
	
	/**
	 * An object representing an attributes changing that has been done, and that can be undone and redone.
	 */
	public class NodeChandeAttributes extends AbstractUndoableEdit
	{
		private ApesTreeNode mNode;
		private Map mAttributes;
		
		public NodeChandeAttributes(ApesTreeNode node, Map undo)
		{
			mNode = node;
			mAttributes = undo;
		}
		
		public void change()
		{
			ApesTreeNode parent = (ApesTreeNode)mNode.getParent();
			mAttributes = mNode.changeAttributes(mAttributes);
			fireTreeNodesChanged(this,parent.getPath(),new int[]{parent.getIndex(mNode)},new Object[]{mNode});
		}
		
		public void undo()
		{
			super.undo();
			change();
		}
		
		public void redo()
		{
			super.redo();
			change();
		}
	}
}
