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

/**
 *
 * @version $Revision: 1.5 $
 */
public class ApesClipboardManager
{
	private static ArrayList mGraphCellsList ;
	
	public static  Clipboard cb = Toolkit.getDefaultToolkit().getSystemClipboard(); 
	//private static Object [] listCells ;
	
	public static void copy() 
	{
		Object [] listCells = (((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph().getSelectionCells()) ; 
		ApesTransferable transfer = new ApesTransferable (listCells) ;
		cb.setContents(transfer, null) ;
	}
	
	public static void cut() 
	{
		Object [] listCells = (((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph().getSelectionCells()) ; 
		ApesTransferable transfer = new ApesTransferable (listCells) ;
		cb.setContents(transfer, null) ;
		((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph().getModel().remove(listCells) ;
	}
	
	public static void paste()
	{
		SpemGraphAdapter adapter ;
		Vector cells = new Vector(),
					edges = new Vector();
		Transferable t = cb.getContents(mGraphCellsList);
		// Make sure the clipboard is not empty.
		if (t == null)
		{
			System.out.println("The clipboard is empty.");
			return;
		}
		try 
		{
			adapter = (SpemGraphAdapter)(((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraphModel());
			adapter.insert((ArrayList)t.getTransferData(ApesTransferable.arrayFlavor));
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
