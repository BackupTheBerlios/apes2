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

package org.ipsquad.apes.ui.tools;

import java.awt.Point;
import java.awt.Rectangle;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.utils.ErrorManager;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;

/**
 * This tool allows to create cells in the graph It use the prototype design
 * pattern to clone cells
 * 
 * @version $Revision: 1.3 $
 */

public class SyncWithModelTool extends Tool
{
    private JGraph mGraph;

    private int mPosX;
    private int mPosY;
    
    public boolean isStable()
    {
        return false;
    }

    public void install(JGraph graph)
    {
        mGraph = graph;
        graph.setMoveable(false);
        graph.setSizeable(false);
        fireToolStarted();
        mPosX = 150;
        
        mGraph.clearSelection();
        //mPosY=mGraph.getBounds().height;
        mPosY=100;
        boolean hasFinalPoint = false;
        
        ActivityGraphAdapter gModel = (ActivityGraphAdapter)mGraph.getModel();
        ActivityDiagram diagram = (ActivityDiagram)gModel.getDiagram();
        
        ApesWorkDefinition wd = (ApesWorkDefinition)diagram.getParent();
       
        Vector toInsert = new Vector();
        int i=0;
        for(i=0; i<wd.subWorkCount();i++)
        {
            if(!diagram.containsModelElement(wd.getSubWork(i)))
            {
            	toInsert.add(getCell(wd.getSubWork(i))); 
            }
        }
       
        if(!diagram.haveInitialPoint())
        {
        	int posY=mPosY;
        	mPosY=10;
            toInsert.add(getCell(new ActivityDiagram.InitialPoint()));
            mPosY=posY;
        }   
        
        for(i=0; i<diagram.modelElementCount(); i++)
        {
            if(diagram.getModelElement(i) instanceof FinalPoint)
            {
                hasFinalPoint = true;
                break;
            }
        }
        
        if(!hasFinalPoint)
        {
        	toInsert.add(getCell(new ActivityDiagram.FinalPoint()));
        }
        
        insertCells(toInsert);
        fireToolFinished();
    }

    public void uninstall(JGraph graph)
    {
        mGraph = null;
        graph.setMoveable(true);
        graph.setSizeable(true);
    }

    
    private void insertCells(Vector cells)
    {
    	Map view = ApesGraphConstants.createMap();
    	
    	for (Iterator it = cells.iterator(); it.hasNext();) 
    	{
			DefaultGraphCell cell = (DefaultGraphCell) it.next();
			if(it == null)
			{
				it.remove();
			}
			else
			{
				view.put(cell, cell.getAttributes());
			}
		}
    	
        ((SpemGraphAdapter) mGraph.getModel()).insert(cells.toArray(), view, null, null, null);
    }
    
    private DefaultGraphCell getCell(ModelElement me)
    {
    	Point pt = new Point(mPosX, mPosY);
        
         
 		ApesGraphCell vertex = ((SpemGraphAdapter)mGraph.getModel()).associateGraphCell(me);
 		
 		if(vertex==null)
 		{
 			ErrorManager.getInstance().printKey("errorElementForbidden");
 			return null;
 		}
 		
 		ApesGraphConstants.setBounds(vertex.getAttributes(), new Rectangle(pt, ApesGraphConstants.getSize(vertex.getAttributes())));
 		mPosY += ApesGraphConstants.getSize(vertex.getAttributes()).height + 30;
 		
 		return vertex;
    }    
}