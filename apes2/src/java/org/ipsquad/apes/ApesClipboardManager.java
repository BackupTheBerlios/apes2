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

package org.ipsquad.apes;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.ApesTransferable;
import org.ipsquad.apes.adapters.NoteEdge;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.ErrorManager;
import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;

/**
 *
 * @version $Revision: 1.19 $
 */
public class ApesClipboardManager
{
	private static ArrayList mGraphCellsList ;
	
	public static  Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	
	public static void copy() 
	{
		Project p = Context.getInstance().getProject();
		if( Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame() == null )
		{
			return;
		}
		
		JGraph graph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Object [] listCells = graph.getSelectionCells() ;
		
		Vector cells = new Vector();
		Vector edges = new Vector();
		for (int i = 0; i < listCells.length; i++) {
			if(listCells[i] instanceof ApesGraphCell)
			{
				cells.add(listCells[i]);
			}
			else if(listCells[i] instanceof DefaultEdge)
			{
				edges.add(listCells[i]);
			}
		}

		ApesTransferable transfer = new ApesTransferable(p, cells, edges) ;
		cb.setContents(transfer, null) ;
	}
	
	public static void cut() 
	{
		Project p = Context.getInstance().getProject();
		
		if( Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame() == null )
		{
			return;
		}
		
		JGraph graph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Object [] listCells = graph.getSelectionCells() ;
		
		Vector cells = new Vector();
		Vector edges = new Vector();
		for (int i = 0; i < listCells.length; i++) {
			if(listCells[i] instanceof ApesGraphCell)
			{
				cells.add(listCells[i]);
			}
			else if(listCells[i] instanceof DefaultEdge)
			{
				DefaultEdge edge = (DefaultEdge)listCells[i];
				Map edgeMap = new HashMap();
				edgeMap.put("EDGE", edge);
				edgeMap.put("SOURCE", ((DefaultPort)edge.getSource()).getParent());
				edgeMap.put("TARGET", ((DefaultPort)edge.getTarget()).getParent());
				edges.add(edgeMap);
			}
		}

		ApesTransferable transfer = new ApesTransferable(p, cells, edges) ;
		cb.setContents(transfer, null) ;
		SpemGraphAdapter adapter = (SpemGraphAdapter)graph.getModel();		
		adapter.remove(listCells);
	}
	
	public static void paste()
	{
		if( Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame() == null )
		{
			return;
		}
		
		JGraph graph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Transferable t = cb.getContents(mGraphCellsList);
		SpemGraphAdapter adapter = (SpemGraphAdapter)graph.getModel();
		int projectHashCode;
		
		// Make sure the clipboard is not empty.
		if (t == null)
		{
			if(Debug.enabled) Debug.print(Debug.VIEW, "(ApesClipboardManager) -> paste No available data");
			return;
		}
		try 
		{
			Map transfer  = (Map)t.getTransferData(ApesTransferable.mArrayFlavor);			
			projectHashCode = ((Integer)transfer.remove("HASHCODE")).intValue();
			
			pasteCells(adapter, (Vector)transfer.get("CELLS"), projectHashCode);
			pasteEdges(adapter, (Vector)transfer.get("EDGES"), projectHashCode);
		} 
		catch (IOException e) 
		{
			if(Debug.enabled) Debug.print(Debug.VIEW, "(ApesClipboardManager) -> paste No available data");
		} 
		catch (UnsupportedFlavorException e) 
		{
			if(Debug.enabled) Debug.print(Debug.VIEW, "(ApesClipboardManager) -> paste Bad flavor type");
		}
	}
	
	protected static void pasteCells(SpemGraphAdapter adapter, Vector cells, int projectHashCode)
	{
		Vector edges = new Vector();
		Map attributes = ApesGraphConstants.createMap();

		for (Iterator it = cells.iterator(); it.hasNext();) 
		{
			Object tmp = (Object) it.next();
	
			if( tmp instanceof ApesGraphCell )
			{
			    ApesGraphCell cell = (ApesGraphCell)tmp;
			    Object userObject = cell.getUserObject();
		    	
			    //if the cell doesn't have a port add it now 
			    if(cell.getChildCount() == 0)
			    {
			    	cell.add(new DefaultPort());
			    }
			    
			    //Element of the model
			    if(userObject instanceof Element)
			    {
			    	if(!setUserObject(adapter, cell, (Element)userObject, projectHashCode))
			    	{
			    		it.remove();
			    	}
			    }
			    //String (NoteCell)
			    else if(userObject instanceof String)
			    {
			    	cell.setUserObject(new String((String)userObject));
			    }
			  
			    if(Context.getInstance().hashCode() != projectHashCode && userObject instanceof StateMachine)
			    {
			    	StateMachine sm = (StateMachine)userObject;
			    	String parentName = sm.getContext().getName();
			    	IPackage parent = Context.getInstance().getProject().getProcess().getComponent();	
					for (int i = 0; i < parent.modelElementCount(); i++) 
					{
						if(parent.getModelElement(i).getName().equals(parentName))
						{
							ErrorManager.getInstance().printKey("errorDuplicatedName");
							it.remove();
						}
					}
					WorkProduct wp = new WorkProduct(parentName);
					ApesMediator.getInstance().insertInModel(new Object[]{wp}, new Object[]{parent}, null);					
					ApesMediator.getInstance().insertInModel(new Object[]{cell.getUserObject()}, new Object[]{wp}, null);
			    }
			    
			    Map attr = cell.getAttributes();
				attributes.put(cell, attr);
			}
		}
					
		if(cells.size() > 0)
		{
			adapter.insert(cells.toArray(), attributes, null, null, null);
		}
	}

	private static boolean setUserObject(SpemGraphAdapter adapter, ApesGraphCell cell, Element element, int projectHashCode) 
	{
		//Copy from one instance of Apes to another
		if( Context.getInstance().getProject().hashCode() != projectHashCode )
		{
			Element tmpElement = (Element)element.clone();
			tmpElement.setName(element.getName());
			
			//Check if there isn't any other element with the same name
			IPackage parent = null;
			if(!(tmpElement instanceof Activity))
			{
				parent = Context.getInstance().getProject().getProcess().getComponent();	
			}	
			else
			{
				parent = adapter.getDiagram().getParent();
			}
			
			for (int i = 0; i < parent.modelElementCount(); i++) 
			{
				if(parent.getModelElement(i).getName().equals(tmpElement.getName()))
				{
					ErrorManager.getInstance().printKey("errorDuplicatedName");
					return false;
				}
			}
			
			cell.setUserObject(tmpElement);
		}
		//Copy to the same instance
		else
		{
			Object userObject = ApesMediator.getInstance().findByID(element.getID());
			if( userObject != null )
			{
				cell.setUserObject(userObject);
			}
		}
			
		return true;
	}
	

	protected static void pasteEdges(SpemGraphAdapter adapter, Vector edges, int projectHashCode)
	{
		Vector edgesToInsert = new Vector();
		Map attributes = ApesGraphConstants.createMap();
		ConnectionSet cs = new ConnectionSet();
			
		for (Iterator it = edges.iterator(); it.hasNext();) 
		{
			Object tmp = (Object) it.next();
			//Only try to copy the note edges
			//Other links will not be copy
			if(tmp instanceof NoteEdge)
			{
				NoteEdge edge = (NoteEdge)tmp;
				edgesToInsert.add(edge);
				cs.connect(edge, edge.getSource(), edge.getTarget());
				attributes.put(edge, edge.getAttributes());
			}
			else if(tmp instanceof DefaultEdge)
			{
				DefaultEdge edge = (DefaultEdge)tmp;
				edgesToInsert.add(edge);
				cs.connect(edge, edge.getSource(), edge.getTarget());
				edge.setSource(edge.getSource());
				edge.setTarget(edge.getTarget());
				attributes.put(edge, edge.getAttributes());
			}
			else if(tmp instanceof Map)
			{
				Map edgeMap = (Map)tmp;
				DefaultEdge edge = (DefaultEdge)edgeMap.get("EDGE");
				ApesGraphCell source = (ApesGraphCell)edgeMap.get("SOURCE");
				ApesGraphCell target = (ApesGraphCell)edgeMap.get("TARGET");
				
				if(source != null && target != null)
				{
					edgesToInsert.add(edge);
					cs.connect(edge, source.getChildAt(0), target.getChildAt(0));
					attributes.put(edge, edge.getAttributes());
					if(!(edge instanceof NoteEdge))
					{
						edge.setSource(source.getChildAt(0));
						edge.setTarget(target.getChildAt(0));
					}
				}
			}
		}
		
		if(edgesToInsert.size() > 0)
		{
			adapter.insert(edgesToInsert.toArray(), attributes, cs, null, null);
		}	
	}
}
