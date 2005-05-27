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


package org.ipsquad.apes.ui;

import java.util.Map;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.ContextEdge;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.NoteCell;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.ui.tools.CellTool;
import org.ipsquad.apes.ui.tools.EdgeTool;
import org.jgraph.JGraph;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;

/**
 * Flow graph editing internal frame
 *
 * @version $Revision: 1.13 $
 */
public class FlowGraphFrame extends GraphFrame
{
	public FlowGraphFrame(SpemGraphAdapter model)
	{
		super(model);
	}

	public ToolPalette createToolPalette()
	{
		ToolPalette palette = super.createToolPalette();

		DefaultEdge edge = new ContextEdge();
		
		palette.addToolButton(new FlowEdgeTool(edge), "icons/PaletteLink.gif", "paletteLink");
		//palette.addToolButton(new EdgeTool(edge), "icons/PaletteLink.gif", "paletteLink");
		
		palette.addSeparator();

		
		DefaultGraphCell vertex = new ActivityCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteActivity.gif", "paletteActivity");

		
		vertex = new WorkProductCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteWorkProduct.gif", "paletteWorkProduct");


		vertex = new ProcessRoleCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteRole.gif", "paletteRole");

		vertex = new NoteCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteNotes.gif","paletteNote");
		
		return palette;
	}
	
	public JGraph createGraph(SpemGraphAdapter model)
	{
		return new FlowJGraph(model);
	}
	
	protected Link createLink(ApesGraphCell source, ApesGraphCell target)
	{
		return new Link(source.getUserObject(), target.getUserObject());
	}
	
	private class FlowEdgeTool extends EdgeTool
	{
		boolean mPersist = false;
		
		public FlowEdgeTool(DefaultEdge edge)
		{
			super(edge);
			mHandler = new FlowEdgeHandler();
		}
		
		public void uninstall(JGraph graph)
		{
			//update persistant variable and edge type
			mPersist = false;
			((FlowGraphAdapter)mGraph.getModel()).setRoleActivityLinkType(FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE);
			super.uninstall(graph);
		}

		private class FlowEdgeHandler extends EdgeTool.EdgeHandler
		{
			protected void createEdge(DefaultEdge edge, ConnectionSet cs, Map attributes) 
			{
				Object[] arg = new Object[]{edge};
				
				//try to update the edge type
				//if is not possible, ask the user 
				if(!((FlowGraphAdapter)mGraph.getModel()).updateType(edge))
				{
					ApesFrame parent = (ApesFrame)Context.getInstance().getTopLevelFrame();
					SelectEdgeTypeDialog dialog = new SelectEdgeTypeDialog(parent);
					dialog.pack();
					dialog.setVisible(true);
					
					//if the selected type is undefined, the cancel button has been pressed
					if(dialog.getSelectedType() == FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE)
					{
						return;
					}
					//update persistant variable if assistant has been selected
					else if(dialog.getSelectedType() == FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)
					{
						mPersist = dialog.getIsPersistant();
					}
					
					//update the type of the edge
					((FlowGraphAdapter)mGraph.getModel()).setRoleActivityLinkType(dialog.getSelectedType());
				}
				
				//modify the edge appearance if necessary
				if(((FlowGraphAdapter)mGraph.getModel()).getRoleActivityLinkType() == FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)
				{
					edge.getAttributes().remove(ApesGraphConstants.DASHPATTERN);											
				}
				
				//create the edge
				super.createEdge(edge, cs, attributes);
				
				//if the edge type is not persistant reset it
				if(!mPersist)
				{
					((FlowGraphAdapter)mGraph.getModel()).setRoleActivityLinkType(FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE);
				}
			}
		}
	}
}
