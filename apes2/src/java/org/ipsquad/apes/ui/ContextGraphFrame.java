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

import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.ui.tools.CellTool;
import org.ipsquad.apes.ui.tools.EdgeTool;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

/**
 * Responsability graph editing internal frame
 *
 * @version $Revision: 1.1 $
 */
public class ContextGraphFrame extends GraphFrame {
	
	public ContextGraphFrame( SpemGraphAdapter model )
	{
		super(model);
	}
	
	public ToolPalette createToolPalette()
	{
		ToolPalette palette = super.createToolPalette();

		DefaultEdge edge = new DefaultEdge();
		Map map = GraphConstants.createMap();
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(map , arrow);
		GraphConstants.setDashPattern(map, new float[] { 3, 3 });
		GraphConstants.setEndFill(map, true);
		GraphConstants.setEditable(map, false);
		//edge.setAttributes(map);
		edge.changeAttributes(map);
		
		palette.addToolButton(new EdgeTool(edge), "icons/PaletteLink.gif", "paletteLink");
		//palette.addToolButton(new EdgeTool(edge), "icons/PaletteLink.gif", "paletteLink");
		
		palette.addSeparator();

		ApesGraphCell vertex = new WorkProductCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteWorkProduct.gif", "paletteWorkProduct");

		return palette;
	}
	
	public JGraph createGraph(SpemGraphAdapter model)
	{
		return new ContextJGraph(model);
	}
}
