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

package org.ipsquad.apes;

import java.awt.Toolkit;
import java.awt.datatransfer.Clipboard;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Vector;

import org.ipsquad.apes.adapters.ApesTransferable;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.ui.GraphFrame;
import org.jgraph.JGraph;

/**
 *
 * @version $Revision: 1.6 $
 */
public class ApesClipboardManager
{
	private static ArrayList mGraphCellsList ;
	
	public static  Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	
	public static void copy() 
	{
		Project p = Context.getInstance().getProject();
		JGraph graph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Object [] listCells = graph.getSelectionCells() ; 
		ApesTransferable transfer = new ApesTransferable(p, listCells) ;
		
		cb.setContents(transfer, null) ;
	}
	
	public static void cut() 
	{
		Project p = Context.getInstance().getProject();
		JGraph graph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Object [] listCells = graph.getSelectionCells() ; 
		ApesTransferable transfer = new ApesTransferable(p, listCells) ;
		
		cb.setContents(transfer, null) ;
		graph.getGraphLayoutCache().remove(listCells);
	}
	
	public static void paste()
	{
		JGraph graph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Transferable t = cb.getContents(mGraphCellsList);
		SpemGraphAdapter adapter = (SpemGraphAdapter)graph.getModel();
		int projectHashCode;
		Vector cells = null;
		boolean cloneUserObject = false;
		
		// Make sure the clipboard is not empty.
		if (t == null)
		{
			System.out.println("The clipboard is empty.");
			return;
		}
		try 
		{
			cells = (Vector)t.getTransferData(ApesTransferable.mArrayFlavor);
			projectHashCode = ((Integer)cells.remove(0)).intValue();
			
			if( Context.getInstance().getProject().hashCode() != projectHashCode )
			{
				cloneUserObject = true;
			}
			
			adapter.insertCells( cells, cloneUserObject );
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
