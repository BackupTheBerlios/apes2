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

package org.ipsquad.apes.adapters;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * 
 * 
 * @version $revision$
 */
public class ApesTransferable implements Transferable 
{
	public static DataFlavor arrayFlavor ;
	static ActivityGraphAdapter adapter ;
	private ArrayList listCell = new ArrayList() ;
	
	static {
		try
		{
			arrayFlavor = new DataFlavor(Class.forName("[Lorg.ipsquad.apes.adapters.ApesGraphCell;"), "Tableau de String");
		}
		catch (ClassNotFoundException e) 
		{ 
		}
	}
	
	public ApesTransferable (Object[] list)
	{
		//listCell = new List(ApesGraphCell);
		System.out.println(list.length);
		for(int i = 0; i < list.length; i++)
		{
			listCell.add(list[i]); 
		}
	}
	
	DataFlavor[] flavors = {arrayFlavor};
	
	
	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors()
	{
		return flavors; 
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) 
	{ 
		return flavor.equals(arrayFlavor); 
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		if (flavor.equals(arrayFlavor)) 
			return listCell;
		else 
			throw new UnsupportedFlavorException(flavor);
	}

}
