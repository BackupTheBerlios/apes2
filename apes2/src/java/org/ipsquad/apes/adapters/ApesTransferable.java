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
import java.util.Arrays;
import java.util.Vector;

import org.ipsquad.apes.Project;


/**
 * 
 * 
 * @version $revision$
 */
public class ApesTransferable implements Transferable 
{
	public static DataFlavor mArrayFlavor ;
	private Vector mElements;
	
	private DataFlavor[] mFlavors = {mArrayFlavor};
	
	static 
	{
		try
		{
			mArrayFlavor = new DataFlavor(Class.forName("[Lorg.ipsquad.apes.adapters.ApesGraphCell;"), "Tableau de String");
		}
		catch (ClassNotFoundException e) 
		{ 
		}
	}
	
	public ApesTransferable( Project p, Object[] list )
	{
		mElements = new Vector(Arrays.asList(list));
		mElements.add(0, new Integer(p.hashCode()));
	}
	
	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferDataFlavors()
	 */
	public DataFlavor[] getTransferDataFlavors()
	{
		return mFlavors; 
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#isDataFlavorSupported(java.awt.datatransfer.DataFlavor)
	 */
	public boolean isDataFlavorSupported(DataFlavor flavor) 
	{ 
		return flavor.equals(mArrayFlavor); 
	}

	/* (non-Javadoc)
	 * @see java.awt.datatransfer.Transferable#getTransferData(java.awt.datatransfer.DataFlavor)
	 */
	public Object getTransferData(DataFlavor flavor) throws UnsupportedFlavorException, IOException 
	{
		if (flavor.equals(mArrayFlavor))
		{
			return mElements;
		}
		else
		{
			throw new UnsupportedFlavorException(flavor);
		}
	}

}
