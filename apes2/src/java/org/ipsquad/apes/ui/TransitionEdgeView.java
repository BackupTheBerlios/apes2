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

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Point;

import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.EdgeRenderer;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.GraphConstants;

/**
 * 
 * 
 * @version $revision$
 */
public class TransitionEdgeView extends EdgeView
{
	public EdgeRenderer getEdgeRenderer()
	{
		return renderer;
	}

	public TransitionEdgeView(Object cell, JGraph graph, CellMapper mapper) 
	{	
		super(cell, graph, mapper);
		renderer = new ApesEdgeRenderer();
	}

	public CellViewRenderer getRenderer()
	{
		return renderer;
	}

	public GraphCellEditor getEditor()
	{
		return new ApesGraphCellEditor((GraphCell)getCell());
	}

	static class ApesEdgeRenderer extends EdgeRenderer	
	{
		protected void paintLabel(Graphics g, String label)
		{
			Point p = getLabelPosition(view);
			if (p != null && label != null && label.length() > 0) 
			{
				int sw = metrics.stringWidth(label);
				int sh = metrics.getHeight();
				g.setColor(GraphConstants.getBackground(view.getAllAttributes()));
				if( !g.getColor().equals(Color.WHITE) )
				{
					g.fillRect(p.x - sw / 2 - 1, p.y - sh / 2 - 1, sw + 2, sh + 2);
				}
				if (borderColor != null) 
				{
					g.setColor(borderColor);
					g.drawRect(p.x - sw / 2 - 1, p.y - sh / 2 - 1, sw + 2, sh + 2);
				}
				g.setColor(GraphConstants.getForeground(view.getAllAttributes()));
				g.setFont(GraphConstants.getFont(view.getAllAttributes()));
				g.drawString(label, p.x - sw / 2, p.y + sh / 4);
			}
		}
	}
}
