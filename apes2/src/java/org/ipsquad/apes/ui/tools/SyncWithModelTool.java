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
import java.util.Map;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.FinalPointCell;
import org.ipsquad.apes.adapters.InitialPointCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.utils.ErrorManager;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * This tool allows to create cells in the graph It use the prototype design
 * pattern to clone cells
 * 
 * @version $Revision: 1.2 $
 */

public class SyncWithModelTool extends Tool
{
    private JGraph mGraph;

    private FinalPointCell mFinalPointCell;
    private InitialPointCell mInitialPointCell;


    private int mPosX;
    private int mPosY;
    
    /**
     * 
     */
    public SyncWithModelTool()
    {
        mFinalPointCell = new FinalPointCell();
        mInitialPointCell = new InitialPointCell();
    }
    
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
        mPosX = 0;
        
        mGraph.clearSelection();
        mPosY=mGraph.getBounds().height;
        boolean hasFinalPoint = false;
        
        ActivityGraphAdapter gModel = (ActivityGraphAdapter)mGraph.getModel();
        ActivityDiagram diagram = (ActivityDiagram)gModel.getDiagram();
        
        ApesWorkDefinition wd = (ApesWorkDefinition)diagram.getParent();
        
        
                
        
        int i=0;
        for(i=0; i<wd.subWorkCount();i++)
        {
            
            if(!diagram.containsModelElement(wd.getSubWork(i)))
            {
                addElement(wd.getSubWork(i));
            }
        }
       
        if(!diagram.haveInitialPoint())
        {
            addElement(mInitialPointCell);
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
            addElement(mFinalPointCell);
        }
        
        fireToolFinished();
    }

    public void uninstall(JGraph graph)
    {
        mGraph = null;
        graph.setMoveable(true);
        graph.setSizeable(true);
    }

    
    private void addElement(DefaultGraphCell cell)
    {
        Point pt = new Point(mPosX, mPosY);
        
        ApesGraphCell vertex = (ApesGraphCell) cell.clone();
        Map attr = vertex.getAttributes();

        GraphConstants.setBounds(attr, new Rectangle(pt, GraphConstants.getSize(attr)));

        Map attributes = ApesGraphConstants.createMap();
        attributes.put(vertex, attr);
        mPosX += GraphConstants.getSize(attr).width + 50;

        Object[] arg = new Object[] { vertex };

        ((SpemGraphAdapter) mGraph.getModel()).insert(arg, attributes, null, null, null);
    }
    
    private void addElement(ModelElement element)
    {
        Point pt = new Point(mPosX, mPosY);
        
         
 		ApesGraphCell vertex = ((SpemGraphAdapter)mGraph.getModel()).associateGraphCell(element);
 		
 		if(vertex==null)
 		{
 			ErrorManager.getInstance().printKey("errorElementForbidden");
 			return;
 		}
 		
 		Map attr = vertex.getAttributes();
 		
 		GraphConstants.setBounds(attr, new Rectangle(pt, GraphConstants.getSize(attr)));
 		mPosX += GraphConstants.getSize(attr).width + 50;
 			
 		Map view = ApesGraphConstants.createMap();
 		ApesGraphConstants.setAttributes(view, attr);
 		mGraph.getModel().insert(new Object[]{vertex}, view, null, null, null);
    } 
}