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

import javax.swing.text.DefaultStyledDocument;

import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.NoteCell;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.ui.tools.CellTool;
import org.ipsquad.apes.ui.tools.EdgeTool;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * Flow graph editing internal frame
 *
 * @version $Revision: 1.6 $
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

		
		DefaultGraphCell vertex = new ActivityCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteActivity.gif", "paletteActivity");

		
		vertex = new WorkProductCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteWorkProduct.gif", "paletteWorkProduct");


		vertex = new ProcessRoleCell();
		palette.addToolButton(new CellTool(vertex), "icons/PaletteRole.gif", "paletteRole");

		vertex = new NoteCell(new DefaultStyledDocument());
		palette.addToolButton(new CellTool(vertex), "icons/PaletteNotes.gif","paletteNote");
		
		
		return palette;
	}
	
	public JGraph createGraph(SpemGraphAdapter model)
	{
		return new FlowJGraph(model);
	}
	
}
