/*
 * Created on 6 févr. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.ipsquad.apes.adapters;

import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.io.IOException;
import java.util.ArrayList;


/**
 * @author go
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
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
