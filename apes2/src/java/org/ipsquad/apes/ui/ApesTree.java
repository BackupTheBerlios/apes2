/*
 * APES is a Process Engineering Software
 * Copyright (C) 2002-2003 IPSquad
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


package org.ipsquad.apes.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.awt.Point;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DragGestureEvent;
import java.awt.dnd.DragGestureListener;
import java.awt.dnd.DragSource;
import java.awt.dnd.DragSourceDragEvent;
import java.awt.dnd.DragSourceDropEvent;
import java.awt.dnd.DragSourceEvent;
import java.awt.dnd.DragSourceListener;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.InputEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.IOException;
import java.util.EventObject;
import java.util.Map;

import javax.swing.Icon;
import javax.swing.JPopupMenu;
import javax.swing.JTextField;
import javax.swing.JTree;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;
import javax.swing.tree.TreeSelectionModel;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Identity;
import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.ui.actions.ChangeColorAction;
import org.jgraph.graph.GraphConstants;

/**
 * Application tree view
 *
 * @version $Revision: 1.1 $
 */
public class ApesTree extends JTree implements DragGestureListener, DragSourceListener, DropTargetListener, TreeModelListener
{
	DragSource mDragSource;
	ChangeColorAction mCa = (ChangeColorAction)Context.getInstance().getAction("ChangeColor");
	
	public ApesTree()
	{
		super(new SpemTreeAdapter());
		setEditable(true);
		getSelectionModel().setSelectionMode(TreeSelectionModel.SINGLE_TREE_SELECTION);
		ApesTreeCellRenderer renderer = new ApesTreeCellRenderer();
		setCellRenderer(renderer);
		setCellEditor(new ApesTreeCellEditor(this, renderer));
		setToggleClickCount(0);
		
		
		mDragSource = DragSource.getDefaultDragSource();
		mDragSource.createDefaultDragGestureRecognizer(this, DnDConstants.ACTION_COPY_OR_MOVE, this);
		new DropTarget(this, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
		setExpandsSelectedPaths(true);
		
		final ApesTree local_tree = this;
		MouseListener ml = new MouseAdapter()
		{
			public void mousePressed(MouseEvent e)
			{
				int selRow = getRowForLocation(e.getX(), e.getY());
				TreePath selPath = getPathForLocation(e.getX(), e.getY());
				
				// the modifiers test is needed in order to make it work on OSes that don't correctly set the isPopupTrigger flag (swing sux0r)
				if(selRow != -1 && (e.isPopupTrigger() || (e.getModifiers() & InputEvent.BUTTON3_MASK)!=0) )
				{
					JPopupMenu popup = associateMenu(selPath.getLastPathComponent());
					
					if(popup!=null)
					{
						e.consume();
						setSelectionPath(selPath);
						popup.show(local_tree, e.getX(), e.getY());
					}
				}
				else if(selRow != -1 && e.getClickCount()==2)
				{
					e.consume();
					((SpemTreeAdapter)getModel()).elementAction(selPath.getLastPathComponent());
				}
				else if(selRow != -1 && e.getClickCount()==3)
				{
					e.consume();
					startEditingAtPath(selPath);
				}
			}
		};
		
		addMouseListener(ml);
		
		if(getModel()!=null)
		{
			getModel().addTreeModelListener(this);
		}
	}
	
	public void setModel(TreeModel newModel)
	{
		if(getModel()!=null)
		{
			getModel().removeTreeModelListener(this);
		}
		
		super.setModel(newModel);
		
		if(getModel()!=null)
		{
			getModel().addTreeModelListener(this);
		}
	}

	public boolean isPathEditable(TreePath path)
	{
		if(path.getLastPathComponent() == getModel().getRoot())
		{
			return false;
		}
		
		return super.isPathEditable(path);
	}
	
	private Icon associateIcon(Object value)
	{
		return ((SpemTreeAdapter)getModel()).associateIcon(value);
	}
	
	private JPopupMenu associateMenu(Object value)
	{
		return ((SpemTreeAdapter)getModel()).associateMenu(value);
	}


	public void dragGestureRecognized(DragGestureEvent dge)
	{
		if(dge.getTriggerEvent().isConsumed()) return;
		
		ApesTreeNode node = (ApesTreeNode)getLastSelectedPathComponent();
		
		if(node!=null)
		{
			dge.getTriggerEvent().consume();
			mDragSource.startDrag(dge, DragSource.DefaultMoveDrop, new ApesTransferable(new Integer(((Identity)node.getUserObject()).getID())), this);
		}
	}

	public void dragEnter(DragSourceDragEvent dsde) { cancelEditing(); }
	
	public void dragOver(DragSourceDragEvent dsde) { cancelEditing(); }
	
	public void dropActionChanged(DragSourceDragEvent dsde) { cancelEditing(); }
	public void dragExit(DragSourceEvent dse) { cancelEditing(); }
	public void dragDropEnd(DragSourceDropEvent dsde) { cancelEditing(); }


	public void dragEnter(DropTargetDragEvent dtde) { }
	public void dragOver(DropTargetDragEvent dtde) { }
	public void dropActionChanged(DropTargetDragEvent dtde) { }
	public void dragExit(DropTargetEvent dte) { }
	public void drop(DropTargetDropEvent dtde)
	{
		Transferable transferable = dtde.getTransferable();
		DataFlavor[] flavors = transferable.getTransferDataFlavors();
		Object userObject;
		
		try
		{
			userObject = ((SpemTreeAdapter)getModel()).findWithID(((Integer)transferable.getTransferData(flavors[0])).intValue());
		}
		catch(Exception e)
		{
			e.printStackTrace();
			dtde.dropComplete(false);
			return;
		}
		
		if(userObject==null)
		{
			dtde.dropComplete(false);
			return;
		}

		Point p = dtde.getLocation();
		int selRow = getRowForLocation((int)p.getX(), (int)p.getY());
		TreePath selPath = getPathForLocation((int)p.getX(), (int)p.getY());

		if(selRow != -1)
		{
			for(int i=0; i<selPath.getPathCount(); i++)
			{
				if(userObject==selPath.getPathComponent(i))
				{
					dtde.dropComplete(false);
					return;
				}
			}
			
			SpemTreeAdapter model = (SpemTreeAdapter)getModel();
			
			model.moveInto(userObject, selPath.getLastPathComponent());

			dtde.dropComplete(true);
			return;
		}

		dtde.dropComplete(false);
		return;
	}

	public void treeNodesChanged(TreeModelEvent e) 
	{
		updateUI(); 
	}
	
	public void treeNodesRemoved(TreeModelEvent e) { }
	
	public void treeNodesInserted(TreeModelEvent e) { }
	
	public void treeStructureChanged(TreeModelEvent e) { }

	private class ApesTreeCellRenderer extends DefaultTreeCellRenderer
	{
		public Component getTreeCellRendererComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                              boolean leaf, int row, boolean hasFocus)
		{
			Component c = super.getTreeCellRendererComponent(tree, value, sel, expanded, leaf, row, hasFocus);
			
			if( value instanceof ApesTreeNode )
			{
				ApesTreeNode node = (ApesTreeNode)value;
				Color fc = GraphConstants.getForeground(node.getAttributes());
				Color bc = GraphConstants.getBackground(node.getAttributes());
				Font font = GraphConstants.getFont(node.getAttributes());
				setBackgroundNonSelectionColor(bc);
				setForeground(fc);
				setIcon(associateIcon(node));
				setFont(font);
			}
			
			return c;
		}
		
	}

	private class ApesTreeCellEditor extends DefaultTreeCellEditor implements ChangeColorAction.ColorChangeListener, CellEditorListener
	{
		private ApesTreeNode mCurrentNode = null;
		
		private Color mInitForeground = null;
		private Color mCurrentForeground = null;
		
		private Color mInitBackground = null;
		private Color mCurrentBackground = null;
		
		private Font mInitFont = null;
		private Font mCurrentFont = null;
		
		private ChangeColorAction mActionForeground = (ChangeColorAction)Context.getInstance().getAction("ChangeForeground");
		private ChangeColorAction mActionBackground = (ChangeColorAction)Context.getInstance().getAction("ChangeBackground");
		
		public ApesTreeCellEditor(JTree tree, DefaultTreeCellRenderer renderer)
		{
			super(tree, renderer);
			mActionForeground.addChangeColorListener(this);
			mActionBackground.addChangeColorListener(this);
			addCellEditorListener(this);
		}
		
		public Component getTreeCellEditorComponent(JTree tree, Object value, boolean sel, boolean expanded,
                                                              boolean leaf, int row)
		{
			Component c = super.getTreeCellEditorComponent(tree, value, sel, expanded, leaf, row);
			editingIcon = associateIcon(value);
			
			if( editingComponent instanceof JTextField && value instanceof ApesTreeNode )
			{
				Color fc = mCurrentForeground == null ? mInitForeground : mCurrentForeground;
				Color bc = mCurrentBackground == null ? mInitBackground : mCurrentBackground;
				Font font = mCurrentFont == null ? mInitFont : mCurrentFont;
				
				mActionForeground.setColor(fc);
				mActionBackground.setColor(bc);
				
				((JTextField)editingComponent).setForeground(fc);
				((JTextField)editingComponent).setSelectedTextColor(fc);
				((JTextField)editingComponent).setBackground(bc);
				((JTextField)editingComponent).setFont( font );
				((JTextField)editingComponent).selectAll();
			}
			
			return c;
		}

		public void colorChanged(Color c)
		{
			TreePath path = getEditingPath();
			if(path != null)
			{
				if( c == mActionForeground.getColor() )
				{
					mCurrentForeground = c;	
				}
				else
				{
					mCurrentBackground = c;
				}
				
				ApesTreeNode node = (ApesTreeNode)path.getLastPathComponent();
				getCellEditor().getTreeCellEditorComponent(ApesTree.this,node,true,true,getModel().isLeaf(node),getModel().getIndexOfChild(node.getParent(),node));
			}
		}

		protected boolean canEditImmediately(EventObject event)
		{
			boolean canEdit = super.canEditImmediately(event);
			
			if( canEdit )
			{
				mActionForeground.setEnabled(true);
				mActionBackground.setEnabled(true);
				
				ApesTreeNode node = (ApesTreeNode)tree.getSelectionPath().getLastPathComponent();
				
				if( node != null )
				{
					mCurrentNode = node;
					mInitForeground = GraphConstants.getForeground(node.getAttributes());
					mInitBackground = GraphConstants.getBackground(node.getAttributes());
					mInitFont = GraphConstants.getFont(node.getAttributes());	
				}
			}
			return canEdit;
		}

		public void editingCanceled(ChangeEvent e)
		{
			mCurrentNode = null;
			mCurrentForeground = null;
			mCurrentBackground = null;
			mCurrentFont = null;
			
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
		}

		public void editingStopped(ChangeEvent e)
		{
			if( mCurrentNode != null )
			{
				ApesTreeNode node = (ApesTreeNode)mCurrentNode;
				Map map = GraphConstants.cloneMap(node.getAttributes());
				boolean hasChanged = false;
			
				if( mCurrentForeground != null && mInitForeground != mCurrentForeground )
				{
					GraphConstants.setForeground(map,mCurrentForeground);
					hasChanged = true;
				}
				if( mCurrentBackground != null &&  mInitBackground != mCurrentBackground )
				{
					GraphConstants.setBackground(map,mCurrentBackground);
					hasChanged = true;
				}
				if(  mCurrentFont != null && mInitFont != mCurrentFont )
				{
					GraphConstants.setFont(map,mCurrentFont);
					hasChanged = true;
				}
			
				if( hasChanged )
				{	
					getModel().valueForPathChanged(new TreePath(node.getPath()), map);
				}
			
				mCurrentForeground = null;
				mCurrentBackground = null;
				mCurrentFont = null;
				
				mActionForeground.setEnabled(false);
				mActionBackground.setEnabled(false);
			}
		}
	}


	private class ApesTransferable implements Transferable
	{
		Object mPayLoad;
		DataFlavor mFlavor;

		public ApesTransferable(Object payload)
		{
			mPayLoad = payload;
			mFlavor = new DataFlavor(payload.getClass(), payload.getClass().toString());
		}

		public DataFlavor[] getTransferDataFlavors()
		{
			return new DataFlavor[]{mFlavor};
		}

		public boolean isDataFlavorSupported(DataFlavor flavor)
		{
			return mFlavor.equals(flavor);
		}

		public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException
		{
			if(!isDataFlavorSupported(flavor))
			{
				throw new UnsupportedFlavorException(flavor);
			}

			return mPayLoad;
		}
	}
}
