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

import java.util.Collection;
import java.util.Iterator;
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
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEditSupport;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.Identity;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.frontend.event.ApesModelEvent;
import org.ipsquad.apes.model.frontend.event.ApesModelListener;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.IconManager;

/**
 * This adapter allows to display a process in a JTree
 *
 * @version $Revision: 1.16 $
 */
public class SpemTreeAdapter extends UndoableEditSupport implements TreeModel, ApesModelListener
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

	public boolean shouldGoInTree(Object o)
	{
	    return ApesMediator.getInstance().shouldGoInTree(o);
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
			if(Debug.enabled) Debug.print(Debug.ADAPTER, "associateIcon : Something unknown in the SpemTreeAdapter");
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
			if(Debug.enabled) Debug.print(Debug.ADAPTER, "associateMenu : Something unknown in the SpemTreeAdapter");
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
			if(Debug.enabled) Debug.print(Debug.ADAPTER, "elementAction : Something unknown in the SpemTreeAdapter");
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
	 * Adds nodes in the tree
	 * 
	 * @param nodes the nodes to insert
	 * @param parents the parents of each nodes
	 */
	public void insert( Object[] nodes, Object[] parentNodes )
	{
		if(Debug.enabled) Debug.print(Debug.ADAPTER, "(A) -> SpemTreeAdapter::insert ");
		
		if(nodes == null || parentNodes == null || nodes.length != parentNodes.length)
		    return;
		
		Map matching = ApesGraphConstants.createMap();
		Map parentMatching = ApesGraphConstants.createMap();
		
        Object[] objects = new Object[nodes.length];
        Object[] parents = new Object[nodes.length];
        
	    for ( int i = 0; i < objects.length; i++ )
        {
            objects[i] = ((ApesTreeNode)nodes[i]).getUserObject();
            parents[i] = ((ApesTreeNode)parentNodes[i]).getUserObject();
            matching.put(objects[i], nodes[i]);
            parentMatching.put(parents[i], parentNodes[i]);
        }
	    Map saved = ApesGraphConstants.createMap();
	    ApesGraphConstants.setInsertApesTreeNodes(saved, matching );
		ApesGraphConstants.setInsertParentApesTreeNodes(saved, parentMatching);
		
		ApesMediator.getInstance().insertInModel(objects, parents, saved);
	}
	
	/**
	 * Removes nodes from the tree
	 * 
	 * @param nodes
	 */
	public void remove( Object[] nodes )
	{
	    if(Debug.enabled) Debug.print(Debug.ADAPTER, "(A) -> SpemTreeAdapter::remove ");
		
	    Map matching = ApesGraphConstants.createMap();
        Object[] objects = new Object[nodes.length];
	    for ( int i = 0; i < objects.length; i++ )
        {
            objects[i] = ((ApesTreeNode)nodes[i]).getUserObject();
            matching.put(objects[i], nodes[i]);
        }
	    Map saved = ApesGraphConstants.createMap();
	    ApesGraphConstants.setRemoveApesTreeNodes(saved, matching);
        
	    ApesMediator.getInstance().removeFromModel(objects, saved);
	}
	
	/**
	 * Moves the nodes to the new parents
	 * 
	 * @param nodes
	 * @param newParents
	 */
	public void move( Object[] nodes, Object[] newParents)
	{
		if(Debug.enabled) Debug.print(Debug.ADAPTER, "(A) -> SpemTreeAdapter::move ");
		
		if(nodes == null || newParents == null)
			return;
	
		Map matching = ApesGraphConstants.createMap();
		Map parentMatching = ApesGraphConstants.createMap();
		Map moves = ApesGraphConstants.createMap();
		
	    for ( int i = 0; i < nodes.length; i++ )
        {
            Object object = ((ApesTreeNode)nodes[i]).getUserObject();
            Object parent = ((ApesTreeNode)newParents[i]).getUserObject();
            
            moves.put(object, parent);
            matching.put(object, nodes[i]);
            parentMatching.put(parent, newParents[i]);
        }
	    
	    Map saved = ApesGraphConstants.createMap();
	    ApesGraphConstants.setInsertApesTreeNodes(saved, matching );
		ApesGraphConstants.setInsertParentApesTreeNodes(saved, parentMatching);
		
		ApesMediator.getInstance().move(null, moves, saved);
	}

	/**
	 * Called when the value associated to a node has changed
	 *
	 * @param path the path to the modified node
	 * @param newValue the new value associated to the node
	 */
	public void valueForPathChanged(TreePath path, Object newValue)
	{
		if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemTreeAdapter) -> valueForPathChanged");
        
		if( newValue instanceof String && path.getLastPathComponent() != mRoot 
				&& ! newValue.equals(path.getLastPathComponent().toString()))
		{
			ApesTreeNode node = (ApesTreeNode) path.getLastPathComponent();
			Map attr = ApesGraphConstants.createMap();
			attr.put(node.getUserObject(), node);
			Map change = ApesGraphConstants.createMap();
			change.put(node.getUserObject(), newValue);
			ApesMediator.getInstance().change(null, change, attr );
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

    public void modelChanged( ApesModelEvent e )
    {
        if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemTreeAdapter) -> modelChanged");
        
        Object source = e.getChange().getSource();
        Object[] inserted = e.getChange().getInserted();
        Map changed = e.getChange().getChanged();
	    Object[] removed = e.getChange().getRemoved();
        Object[] parents = e.getChange().getParents();
        Collection moved = e.getChange().getMoved();
        Map extras = e.getChange().getExtras();
        
        handleInsert(source, inserted, parents, extras);
        handleRemove(source, removed, extras);
        handleMove(source, e.getChange(), extras);
        handleChange(source, changed, extras);
    }
    
    protected void handleInsert(Object source, Object[] elements, Object[] parents, Map attr)
    {
        if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemTreeAdapter) -> handleInsert");
        
        if(elements == null || parents == null || elements.length != parents.length) return;
        
        NodeEdit postEdit = null;
       
        for ( int i = 0; i < elements.length; i++ )
        {
        	ApesTreeNode node = getNodeByUserObject(elements[i], attr, true);
            if( node != null && node.getParent() == null )
            {            	
            	ApesTreeNode parent = getParentNodeByUserObject(parents[i],attr);
                if( parent == null )
                {
                    parent = mRoot;
                }
                NodeEdit edit = createInsertNodeEdit(parent, node);
                if(edit != null)
                {
                	if(postEdit == null)
                	{
                		postEdit = edit;
                	}
                	else
                	{
                		edit.end();
                		postEdit.addEdit(edit);
                	}
                	if(Debug.enabled) Debug.print(Debug.ADAPTER, "\t(SpemTreeAdapter) -> inserted "+edit);
                	edit.execute();
                }
            }
        }
        
        if(postEdit != null)
        {
        	postEdit.end();
        	postEdit(postEdit);
        }
    }
    
    protected void handleRemove(Object source, Object[] elements, Map attr)
    {
        if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemTreeAdapter) -> handleRemoved");
        
        if(elements == null || source != null) return;
        
        //Vector edits = new Vector();
        NodeEdit postEdit = null;
        
        for ( int i = 0; i < elements.length; i++ )
        {
            ApesTreeNode node = getNodeByUserObject(elements[i], attr, false);
            if( node != null )
            { 
                //update reference to the node
                if(node.getParent() == null && node instanceof Identity)
                {
                    node = findWithID(((Identity)node).getID());
                }
                ApesTreeNode parent = (ApesTreeNode)node.getParent();
                if(parent != null)
                {
                    NodeEdit edit = createRemoveNodeEdit(parent, node);
                    if(edit != null)
                    {
                    	if(postEdit == null)
                    	{
                    		postEdit = edit;
                    	}
                    	else
                    	{
                    		edit.end();
                    		postEdit.addEdit(edit);
                    	}
                    	if(Debug.enabled) Debug.print(Debug.ADAPTER, "\t(SpemTreeAdapter) -> removed "+edit);
                    	edit.execute();
                    }
                }
                else
                {
                	if(Debug.enabled) Debug.print(Debug.ADAPTER, "**WARNING** (SpemTreeAdapter) -> remove : parent is null!");
                }
            }
        }
        
        if(postEdit != null)
        {
        	postEdit.end();
        	postEdit(postEdit);
        }
    }
    
    protected Map handleMove(Object source, ApesModelEvent.ApesModelChange change, Map attr)
    {
    	if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemTreeAdapter) -> handleMoved");
      
    	if(change.getMoved() == null) return null;
    	
    	Iterator it = change.getMoved().iterator();
    	while(it.hasNext())
    	{
    		Object userObject = it.next();
    		ApesTreeNode node = getNodeByUserObject(userObject, attr, false);
    	    ApesTreeNode oldParent = getParentNodeByUserObject(change.getOldParent(userObject),attr);
    	    ApesTreeNode newParent = getParentNodeByUserObject(change.getNewParent(userObject),attr);
    	    
            if( node != null && oldParent != null && newParent != null)
            { 
            	NodeEdit edit = createMoveNodeEdit(oldParent, newParent, node);
            	if(edit != null)
            	{
            		edit.end();
            		edit.execute();
            		postEdit(edit);
            	}
            }
    	}
    	return null;
    }
    
    protected void handleChange(Object source, Map changed, Map extras)
    {
    	if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemTreeAdapter) -> handleChange");
        
    	if(changed != null)
    	{
			Iterator it = changed.entrySet().iterator();
    		while(it.hasNext())
    		{
        		Map.Entry entry = (Map.Entry)it.next();
    			ApesTreeNode node = getNodeByUserObject(entry.getKey(), extras, false);
    			if(node != null && entry.getValue() != null && entry.getValue() instanceof String)
    			{
        			NodeChandeAttributes edit = createChangeNodeEdit(node, null);	
					if(edit != null)
					{
						//edit.change();
						fireTreeNodesChanged( this, ((ApesTreeNode)node.getParent()).getPath(), new int[]{ node.getParent().getIndex(node) }, new Object[]{ node } );
						postEdit(edit);
					}
    			}
    		}
    	}
    }
    
    protected ApesTreeNode getNodeByUserObject(Object userObject, Map attr, boolean create)
    {
        ApesTreeNode node = null;
        if(attr != null && attr.containsKey(ApesGraphConstants.INSERT_APES_TREE_NODE))
        {
           node = (ApesTreeNode)ApesGraphConstants.getInsertApesTreeNodes(attr).get(userObject);
        }
        if(node == null && userObject instanceof Identity)
        {
          node = findWithID(((Identity)userObject).getID());
        }
        if( node == null && create && shouldGoInTree(userObject))
        {
            node = new ApesTreeNode(userObject,null);
        }
        return node;    
     }
    
    protected ApesTreeNode getParentNodeByUserObject(Object parent, Map attr)
    {
        ApesTreeNode node = null;
        if(attr != null && attr.containsKey(ApesGraphConstants.INSERT_PARENT_APES_TREE_NODE))
        {
            node = (ApesTreeNode)ApesGraphConstants.getInsertParentApesTreeNodes(attr).get(parent);
        }
        if(node == null && parent instanceof Identity)
        {
            node = findWithID(((Identity)parent).getID());
        }
        return node;    
    }
    	
	protected NodeEdit createInsertNodeEdit(ApesTreeNode insertedParent, ApesTreeNode insertedElement)
	{
		return createNodeEdit(insertedParent, insertedElement, null, null, null, null, null);
	}
	
	protected NodeEdit createRemoveNodeEdit(ApesTreeNode removedParent, ApesTreeNode removedElement)
	{
		return createNodeEdit(null, null, removedParent, removedElement, null, null, null);
	}
	
	protected NodeEdit createMoveNodeEdit(ApesTreeNode oldParent, ApesTreeNode newParent, ApesTreeNode movedElement)
	{
		return createNodeEdit(null, null, null, null, oldParent, newParent, movedElement);
	}
	
	protected NodeChandeAttributes createChangeNodeEdit(ApesTreeNode node, Map changed)
	{
		return new NodeChandeAttributes(node, changed);
	}
	
	protected NodeEdit createNodeEdit(ApesTreeNode insertedParent, ApesTreeNode insertedElement,
			ApesTreeNode removedParent, ApesTreeNode removedElement,
			ApesTreeNode oldParent, ApesTreeNode newParent, ApesTreeNode movedElement )
	{
		return new NodeEdit(insertedParent, insertedElement, 
				removedParent, removedElement, oldParent, newParent, movedElement);
	}
	
	public class NodeEdit extends CompoundEdit
	{
		ApesTreeNode mInsertElement, mRemoveElement, mMoveElement;
		ApesTreeNode mInsertParent, mRemoveParent, mMoveOldParent, mMoveNewParent;
		
		public NodeEdit(ApesTreeNode insertedParent, ApesTreeNode insertedElement,
				ApesTreeNode removedParent, ApesTreeNode removedElement,
				ApesTreeNode movedOldParent, ApesTreeNode movedNewParent, ApesTreeNode movedElement)
		{
			mInsertElement = insertedElement;
			mInsertParent = insertedParent;
			mRemoveElement = removedElement;
			mRemoveParent = removedParent;
			mMoveElement = movedElement;
			mMoveNewParent = movedNewParent;
			mMoveOldParent = movedOldParent;
		}
		
		public boolean  isSignificant() 
		{
			return true;
		}
		
		public void redo() throws CannotRedoException
		{
			super.redo();
			execute();
		}
		
		public void undo() throws CannotUndoException
		{
			super.undo();
			execute();
		}
		
		public void execute()
		{
        	ApesTreeNode insertedParent = mInsertParent;
        	ApesTreeNode insertedElement = mInsertElement;
			
        	ApesTreeNode removedParent = mRemoveParent;
        	ApesTreeNode removedElement = mRemoveElement;
			
        	if(insertedElement != null)
			{
				insertedParent.add(insertedElement);
				fireTreeNodesInserted(SpemTreeAdapter.this, insertedParent.getPath(), new int[]{insertedParent.getIndex(insertedElement)}, new Object[]{insertedElement});			
			}
			mRemoveElement = insertedElement;
			mRemoveParent = insertedParent;
			
			if(removedElement != null)
			{
				int[] indices = new int[]{ removedParent.getIndex(removedElement) };
				removedParent.remove(removedElement);
				fireTreeNodesRemoved(this, removedParent.getPath(), indices, new Object[]{removedElement} );
			}
			mInsertElement = removedElement;
			mInsertParent = removedParent;
			
			ApesTreeNode movedOldParent = mMoveOldParent;
        	ApesTreeNode movedNewParent = mMoveNewParent;
        	
			if(mMoveElement != null)
			{
				int[] indices =  new int[]{ movedOldParent.getIndex(mMoveElement) };
				movedOldParent.remove(mMoveElement);
				fireTreeNodesRemoved(this, movedOldParent.getPath(), indices, new Object[]{ mMoveElement } );
				movedNewParent.add(mMoveElement);
				fireTreeNodesInserted(this, movedNewParent.getPath(), new int[]{ movedNewParent.getIndex(mMoveElement) }, new Object[]{ mMoveElement } );
			}
			mMoveOldParent = movedNewParent;
			mMoveNewParent = movedOldParent;
		}
		
		public String toString()
		{
			String result = "NodeEdit[ insert/"+mInsertElement+" insertParent/"+mInsertParent
				+" remove/"+mRemoveElement+" removeParent/"+mRemoveParent+"]";
			return result;
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
