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


package org.ipsquad.apes.ui;

import java.util.Map;

import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.DecisionCell;
import org.ipsquad.apes.adapters.FinalPointCell;
import org.ipsquad.apes.adapters.InitialPointCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SynchroCell;
import org.ipsquad.apes.adapters.TransitionEdge;
import org.ipsquad.apes.ui.tools.CellTool;
import org.ipsquad.apes.ui.tools.EdgeTool;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

/**
 * Activity graph editing internal frame
 *
 * @version $Revision: 1.1 $
 */
public class ActivityGraphFrame extends GraphFrame
{
	public ActivityGraphFrame(SpemGraphAdapter model)
	{
		super(model);
	}

	public ToolPalette createToolPalette()
	{
		ToolPalette palette = super.createToolPalette();

		DefaultEdge edge = new TransitionEdge();
		Map map = GraphConstants.createMap();
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(map , arrow);
		GraphConstants.setEndFill(map, true);
		//edge.setAttributes(map);
		edge.changeAttributes(map);
		
		palette.addToolButton(new EdgeTool(edge), "icons/PaletteTransition.gif", "paletteTransition");
		//palette.addToolButton(new EdgeTool(getDefaultEdge()), "icons/PaletteTransition.gif", "paletteTransition");
		
		palette.addSeparator();

		
		ApesGraphCell vertex = new ActivityCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteActivity.gif", "paletteActivity");
		
		vertex = new DecisionCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteDecision.gif", "paletteDecision");

		vertex = new InitialPointCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteInitialPoint.gif", "paletteInitialPoint");
		
		vertex = new FinalPointCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteFinalPoint.gif","paletteFinalPoint");

		vertex = new SynchroCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteSynchro.gif","paletteSynchro");
		return palette;
	}
	
	public JGraph createGraph(SpemGraphAdapter model)
	{
		return new ActivityJGraph(model);
	}
	
}
