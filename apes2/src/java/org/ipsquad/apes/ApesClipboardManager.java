/*
 * Created on 8 févr. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.ipsquad.apes;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.ApesTransferable;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.ui.GraphFrame;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultPort;

/**
 * @author go
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class ApesClipboardManager
{
	private static ArrayList mGraphCellsList ;
	
	public static  Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	
	public static void copy() 
	{
		Object [] listCells = (((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph().getSelectionCells()) ; 
		ApesTransferable t = new ApesTransferable (listCells) ;
		cb.setContents(t, null) ;
	}
	
	
	public static void paste()
	{
		SpemGraphAdapter adapter ;
		ApesGraphCell cell;
		DefaultEdge edge ;
		
		Transferable t = cb.getContents(mGraphCellsList);
		// Make sure the clipboard is not empty.
		if (t == null)
		{
			System.out.println("The clipboard is empty.");
			return;
		}
		try 
		{
			mGraphCellsList = (ArrayList)t.getTransferData(ApesTransferable.arrayFlavor);		
			adapter = (SpemGraphAdapter)(((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraphModel());
			
			for(int i = 0 ; i < mGraphCellsList.size(); i++)
			{
				if (mGraphCellsList.get(i) instanceof ApesGraphCell)
				{
					cell = (ApesGraphCell)mGraphCellsList.get(i);
					String name = cell.toString();
					cell.setUserObject(((Element)cell.getUserObject()).clone());
					Map attr = cell.getAttributes();
					Map view = new HashMap();
					view.put("Attributes", attr);
					adapter.insertCell(cell, view);
					
					if( !cell.toString().equals(name) )
					{	
						adapter.change(cell.getUserObject(),name,null);
					}
				}
				if (mGraphCellsList.get(i) instanceof DefaultEdge)
				{				
					edge = (DefaultEdge)mGraphCellsList.get(i) ;
					Map attr = edge.getAttributes() ;
					Map view = new HashMap();
					view.put("Attributes", attr);
					DefaultPort sourcePort = (DefaultPort) edge.getSource();
					DefaultPort targetPort = (DefaultPort) edge.getTarget();
					ApesGraphCell target = (ApesGraphCell) targetPort.getParent();
					ApesGraphCell source = (ApesGraphCell) sourcePort.getParent();
					adapter.insertEdge(source, target, view);
				}
			}
		} 
		catch (IOException e) 
		{
			System.out.println("Données non disponible");
		} 
		catch (UnsupportedFlavorException e) {
			System.out.println("DataFlower de mauvais type");
		} 
	}

}
