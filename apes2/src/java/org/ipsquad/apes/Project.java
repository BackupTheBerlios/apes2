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

import java.io.Serializable;
import java.util.HashMap;
import java.util.Iterator;

import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ContextGraphAdapter;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ResponsabilityGraphAdapter;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.adapters.WorkDefinitionGraphAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.utils.ConfigManager;

/**
 * Application Project
 *
 * This class represent a project in the application
 *
 * @version $Revision: 1.12 $
 */
public class Project implements Serializable
{
	//private ProcessComponent mProcess = new ProcessComponent();
	private ApesProcess mProcess = new ApesProcess(ConfigManager.getInstance().getProperty("Project")!=null?ConfigManager.getInstance().getProperty("Project"):"");
	private HashMap mDiagramMap = new HashMap();
	
	/**
	 * Retrieve the process associated to this project
	 *
	 * @return the process
	 */
	public ApesProcess getProcess()
	{
		return mProcess;
	}
	
	/**
	 * functions use to save the project
	 * @param p
	 */
	public void setProcess( ApesProcess p )
	{
		mProcess = p;
	}
	
	public HashMap getDiagramMap()
	{
		return mDiagramMap;
	}
	
	public void setDiagramMap( HashMap diagramMap )
	{
		mDiagramMap = diagramMap;
	}
	
	/**
	 * Retrieve graph model associated to the given diagram in the model
	 * @return the graph model
	 */
	public SpemGraphAdapter getGraphModel(SpemDiagram diagram)
	{
		if(mDiagramMap.get(diagram)==null)
		{
			if(diagram instanceof FlowDiagram)
			{
				FlowGraphAdapter adapt = new FlowGraphAdapter((FlowDiagram)diagram);
				adapt.addUndoableEditListener(Context.getInstance().getUndoManager());
				mDiagramMap.put(diagram, adapt);
				ApesMediator.getInstance().registerDiagram(diagram);
				ApesMediator.getInstance().addApesModelListener( adapt );
				//Context.getInstance().getTopLevelFrame().getTree().getModel().addTreeModelListener(adapt);
			}
			else if(diagram instanceof ResponsabilityDiagram)
			{
				ResponsabilityGraphAdapter adapt = new ResponsabilityGraphAdapter((ResponsabilityDiagram)diagram);
				adapt.addUndoableEditListener(Context.getInstance().getUndoManager());
				mDiagramMap.put(diagram, adapt);
				ApesMediator.getInstance().registerDiagram(diagram);
				ApesMediator.getInstance().addApesModelListener( adapt );
			}
			else if(diagram instanceof ContextDiagram)
			{
				ContextGraphAdapter adapt = new ContextGraphAdapter((ContextDiagram)diagram);
				adapt.addUndoableEditListener(Context.getInstance().getUndoManager());
				mDiagramMap.put(diagram, adapt);
				ApesMediator.getInstance().registerDiagram(diagram);
				ApesMediator.getInstance().addApesModelListener( adapt );
			}
			else if(diagram instanceof ActivityDiagram)
			{
				ActivityGraphAdapter adapt = new ActivityGraphAdapter((ActivityDiagram)diagram);
				adapt.addUndoableEditListener(Context.getInstance().getUndoManager());
				mDiagramMap.put(diagram, adapt);
				ApesMediator.getInstance().registerDiagram(diagram);
				ApesMediator.getInstance().addApesModelListener( adapt );
				//Context.getInstance().getTopLevelFrame().getTree().getModel().addTreeModelListener(adapt);
			}
			else if(diagram instanceof WorkDefinitionDiagram)
			{
				WorkDefinitionGraphAdapter adapt = new WorkDefinitionGraphAdapter((WorkDefinitionDiagram)diagram);
				adapt.addUndoableEditListener(Context.getInstance().getUndoManager());
				mDiagramMap.put(diagram, adapt);
				ApesMediator.getInstance().registerDiagram(diagram);
				ApesMediator.getInstance().addApesModelListener( adapt );
				//Context.getInstance().getTopLevelFrame().getTree().getModel().addTreeModelListener(adapt);
			}
		}
		
		return (SpemGraphAdapter) mDiagramMap.get(diagram);
	}
	
	/**
	 * Insert a diagram in the project and force the corresponding adapter
	 *
	 * @param diagram
	 * @param adapter
	 */
	public void setGraphModel(SpemDiagram diagram, SpemGraphAdapter adapter)
	{
		adapter.addUndoableEditListener(Context.getInstance().getUndoManager());
		mDiagramMap.put(diagram, adapter);
		ApesMediator.getInstance().addApesModelListener( adapter );
		//ApesMediator.getInstance().addDiagram( diagram );
		//Context.getInstance().getTopLevelFrame().getTree().getModel().addTreeModelListener(adapter);
	}

	public void removeGraphModel(SpemDiagram diagram)
	{
		SpemGraphAdapter adapt = (SpemGraphAdapter) mDiagramMap.remove(diagram);
		//ApesMediator.getInstance().removeDiagram( diagram );
		
		if(adapt!=null)
		{
			Context.getInstance().getTopLevelFrame().deleteDiagram(adapt);
			ApesMediator.getInstance().removeApesModelListener( adapt );
			//Context.getInstance().getTopLevelFrame().getTree().getModel().removeTreeModelListener(adapt);
			adapt.destroy();
			adapt.removeUndoableEditListener(Context.getInstance().getUndoManager());
		}
	}
	
	public void clearListeners()
	{
		Iterator it = mDiagramMap.values().iterator();
		
		while(it.hasNext())
		{
			SpemGraphAdapter adapt = (SpemGraphAdapter) it.next();
			//Context.getInstance().getTopLevelFrame().getTree().getModel().removeTreeModelListener(adapt);
			ApesMediator.getInstance().removeApesModelListener( adapt );
			adapt.removeUndoableEditListener(Context.getInstance().getUndoManager());
		}
		SpemTreeAdapter treeAdapt = (SpemTreeAdapter)Context.getInstance().getTopLevelFrame().getTree().getModel();
		treeAdapt.removeUndoableEditListener(Context.getInstance().getUndoManager());
		ApesMediator.getInstance().removeApesModelListener( treeAdapt );
		ApesMediator.getInstance().removeUndoableEditListener(Context.getInstance().getUndoManager());
	}
	
	public void setListeners()
	{
		Iterator it = mDiagramMap.values().iterator();
		
		while(it.hasNext())
		{
			SpemGraphAdapter adapt = (SpemGraphAdapter) it.next();
			ApesMediator.getInstance().addApesModelListener( adapt );
			//Context.getInstance().getTopLevelFrame().getTree().getModel().addTreeModelListener(adapt);
			adapt.addUndoableEditListener(Context.getInstance().getUndoManager());
		}
		SpemTreeAdapter treeAdapt = (SpemTreeAdapter)Context.getInstance().getTopLevelFrame().getTree().getModel();
		treeAdapt.addUndoableEditListener(Context.getInstance().getUndoManager());
		ApesMediator.getInstance().addApesModelListener( treeAdapt );
		ApesMediator.getInstance().addUndoableEditListener(Context.getInstance().getUndoManager());
	}
	
	/*public void initApesMediator()
	{
		Vector diagrams = new Vector();
		Iterator it = mDiagramMap.keySet().iterator();
		
		while(it.hasNext())
		{
			SpemDiagram diagram = (SpemDiagram)it.next();
			diagrams.add( diagram );
		}
		
		ApesMediator.getInstance().setDiagram( diagrams );
	}*/
}
