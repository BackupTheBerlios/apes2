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


package org.ipsquad.apes.ui;

import java.awt.BorderLayout;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JInternalFrame;
import javax.swing.JScrollPane;
import javax.swing.event.InternalFrameEvent;
import javax.swing.event.InternalFrameListener;
import javax.swing.undo.AbstractUndoableEdit;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.frontend.ChangeEvent;
import org.ipsquad.apes.model.frontend.Event;
import org.ipsquad.utils.ErrorManager;
import org.jgraph.JGraph;
import org.jgraph.event.GraphModelEvent;
import org.jgraph.event.GraphModelListener;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

/**
 * Base class for the graph editing internal frames
 *
 * @version $Revision: 1.4 $
 */
public class GraphFrame extends JInternalFrame implements InternalFrameListener, GraphModelListener, DropTargetListener, ApesMediator.Listener
{

	protected JGraph mGraph;
	protected ToolPalette mToolPalette;
	
	public DefaultEdge getDefaultEdge()
	{
		return new DefaultEdge();
	}
	
	public GraphFrame(SpemGraphAdapter model)
	{
		super(model.getName(),true,true,true,true);
		
		model.addGraphModelListener(this);
		ApesMediator.getInstance().addApesMediatorListener(this);
		mToolPalette = createToolPalette();
		mGraph = createGraph(model);
		
		new DropTarget(mGraph, DnDConstants.ACTION_COPY_OR_MOVE, this, true);
		
		// Update the content
		getContentPane().setLayout(new BorderLayout());
		getContentPane().add(new JScrollPane(mGraph));
		
		setDefaultCloseOperation(HIDE_ON_CLOSE);
		addInternalFrameListener(this);
		
		mGraph.setDisconnectable(false);
	}
	
	public JGraph getGraph()
	{
		return mGraph;
	}
	
	public SpemGraphAdapter getGraphModel()
	{
		return (SpemGraphAdapter)mGraph.getModel();
	}
	
	public ToolPalette createToolPalette()
	{
		return new ToolPalette();
	}
	
	public JGraph createGraph(SpemGraphAdapter model)
	{
		return new JGraph(model);
	}
	
	private void installToolPalette()
	{
		Context context = Context.getInstance();
	
		if(context.getTopLevelFrame().getToolPalette()!=null)
		{
			context.getTopLevelFrame().getToolPalette().uninstall();
		}
		context.getTopLevelFrame().setToolPalette(mToolPalette);
		mToolPalette.install(mGraph);
	}
	
	private void uninstallToolPalette()
	{
		Context context = Context.getInstance();
	
		if(context.getTopLevelFrame().getToolPalette()!=null)
		{
			context.getTopLevelFrame().getToolPalette().uninstall();
		}
		context.getTopLevelFrame().setDefaultToolPalette();
	}
	
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
			userObject = ((SpemGraphAdapter)mGraph.getModel()).findWithID(((Integer)transferable.getTransferData(flavors[0])).intValue());
			
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
		
		mGraph.clearSelection();
		Point pt = mGraph.fromScreen(dtde.getLocation());

		ApesGraphCell vertex = ((SpemGraphAdapter)mGraph.getModel()).associateGraphCell(userObject);
		
		if(vertex==null)
		{
			ErrorManager.getInstance().printKey("errorElementForbidden");
			dtde.dropComplete(false);
			return;
		}
		
		Map attr = vertex.getAttributes();

		GraphConstants.setBounds(attr, new Rectangle(pt, GraphConstants.getSize(attr)));

		Map view = new HashMap();
		view.put("Attributes", attr);
		
		((SpemGraphAdapter)mGraph.getModel()).insertCell( vertex, view);
		
		//Dimension size = new Dimension(25,25);
		//Map map = GraphConstants.createMap();
		//GraphConstants.setSize(map, size);
		//GraphConstants.setBorderColor(map, Color.black);
		//GraphConstants.setBackground(map, Color.white);
		//GraphConstants.setOpaque(map, true);
		//vertex.setAttributes(map);

		//CHANGE BY PTIYO FOR JGRAPH3
		//vertex.setAttributes(attr);
		/*vertex.changeAttributes(attr);
		
		Hashtable attributes = new Hashtable();
		attributes.put(vertex, attr);

		Object[] arg = new Object[]{vertex};

		mGraph.getModel().insert(arg, attributes, null, null, null);
		
		if(mGraph.getModel().contains(vertex))
		{
			try
			{
				setSelected(true);
			}
			catch(Exception e) { }
			
			mGraph.setSelectionCell(vertex);
			dtde.dropComplete(true);
		}
		else
		{
			dtde.dropComplete(false);
		}*/
	}
	
	public void graphChanged(GraphModelEvent e)
	{
		mGraph.repaint();
		setTitle( ((SpemGraphAdapter)mGraph.getModel()).getName() );
	}
	
	public void dispose()
	{
		ApesMediator.getInstance().removeApesMediatorListener(this);
		getGraphModel().removeGraphModelListener(this);
		super.dispose();
	}
	
	public void internalFrameActivated(InternalFrameEvent e)
	{
		installToolPalette();
		Context.getInstance().getAction("Remove").setEnabled(true);
		Context.getInstance().getAction("Bigger").setEnabled(true);
		Context.getInstance().getAction("Smaller").setEnabled(true);
		
		Context.getInstance().getAction("AlignH").setEnabled(true);
		Context.getInstance().getAction("AlignV").setEnabled(true);
	}

	public void internalFrameDeactivated(InternalFrameEvent e)
	{
		uninstallToolPalette();
		mGraph.clearSelection();
		Context.getInstance().getAction("Remove").setEnabled(false);
		Context.getInstance().getAction("Bigger").setEnabled(false);
		Context.getInstance().getAction("Smaller").setEnabled(false);
		
		Context.getInstance().getAction("AlignH").setEnabled(false);
		Context.getInstance().getAction("AlignV").setEnabled(false);
	}

	public void internalFrameOpened(InternalFrameEvent e) { }
	public void internalFrameClosed(InternalFrameEvent e) {	}
	public void internalFrameClosing(InternalFrameEvent e) { }
	public void internalFrameIconified(InternalFrameEvent e) { }
	public void internalFrameDeiconified(InternalFrameEvent e) { }

	public void updated(Event e)
	{
		if( e instanceof ChangeEvent && ((ChangeEvent)e).getElement() == getGraphModel().getDiagram() )
		{
			graphChanged( null );
			Context.getInstance().getUndoManager().addEdit( new AbstractUndoableEdit()
					{
				public void undo() { graphChanged(null); }
				public void redo() { graphChanged(null); }
			});
		}
	}

}
