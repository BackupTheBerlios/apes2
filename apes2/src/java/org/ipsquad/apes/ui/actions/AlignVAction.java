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

package org.ipsquad.apes.ui.actions;

import java.awt.Event;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.util.HashMap;
import java.util.Map;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.NoteCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.ui.GraphFrame;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;


/**
 * Align the centers of selected objects vertically
 *
 * @version $Revision: 1.7 $
 */
public class AlignVAction extends ApesAction
{
	
	public AlignVAction()
	{
		super("alignV", "icons/AlignV.gif", 'V', Event.CTRL_MASK);
	}

	public void actionPerformed(ActionEvent e)
	{
		Object [] listCells = (((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph().getSelectionCells()) ; 
		if(listCells.length>1)
		{	
			alignV(listCells);
		}
	}
	
	//Perform vertical alignment
	public void alignV(Object [] listCells)
	{
		DefaultGraphCell c;
		Rectangle newRect, oldRect = null;
		JGraph mGraph = ((GraphFrame)Context.getInstance().getTopLevelFrame().getDesktop().getSelectedFrame()).getGraph();
		Map attr, myMap = new HashMap();
		double oldY,newY;
		oldY=0;
		newY=0;
		
		for( int i = 0; i < listCells.length; i++)
		{
			if(listCells[i] instanceof ApesGraphCell || listCells[i] instanceof NoteCell )
			{	
				c=(DefaultGraphCell)listCells[i];
				
				if( oldRect == null )
				{
					oldRect = GraphConstants.getBounds(c.getAttributes());
					oldY=oldRect.getCenterY();
				}
				else
				{	
					attr = GraphConstants.createMap();
					newRect=new Rectangle(GraphConstants.getBounds(c.getAttributes())); 
					newY=newRect.getCenterY();
					newY=oldY-newRect.getHeight()/2;
					if(newY<0)
						newY=0;
					newRect.setRect(newRect.getX(),newY,newRect.getWidth(),newRect.getHeight()); 
					GraphConstants.setBounds(attr,newRect); 
					myMap.put(c, attr); 
				}
			}
		}
		((SpemGraphAdapter)mGraph.getModel()).edit(myMap,null,null,null);
	}

}
