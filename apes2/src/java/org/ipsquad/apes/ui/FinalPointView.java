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

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Line2D;

import org.ipsquad.utils.IconManager;
import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;


/**
 * Display a Decision cell
 *
 * @version $Revision: 1.6 $
 */
 
public class FinalPointView extends ApesVertexView
{
	static FinalPointRenderer renderer = new FinalPointRenderer();
	// Constructor for Superclass
	
	public FinalPointView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
	}
	
	
	protected Dimension calculateSize()
	{
		return new Dimension(25, 25);
	}
	
	
	/**
	 * Returns the intersection of the bounding rectangle and the
	 * straight line between the source and the specified point p.
	 * The specified point is expected not to intersect the bounds.
	 */
	public Point getPerimeterPoint(Point source, Point p) 
	{
		Line2D.Double line1 = new Line2D.Double(p,getCenterPoint());
		Rectangle rContour = getBounds();
		
		if (line1.intersects(rContour) == true)
		{
			// Compute relative bounds
			int a = (rContour.width) / 2 +1;
			int b = (rContour.height) / 2 +1;

			// Get center
			int xCenter = rContour.x + a;
			int yCenter = rContour.y + b;

			// Compute angle
			int dx = p.x - xCenter;
			int dy = p.y - yCenter;
			double t = Math.atan2(dy, dx);

			// Compute Perimeter Point
			int xout = xCenter + (int) (a * Math.cos(t));
			int yout = yCenter + (int) (b * Math.sin(t));

			// Return perimeter point
			return new Point(xout, yout);
		}
				
		return getCenterPoint();
	}
	
	// Returns the Renderer for this View
	public CellViewRenderer getRenderer()
	{
		return renderer;
	}

	// Define the Renderer for a ActivityView
	static class FinalPointRenderer extends VertexRenderer
	{
		public void paint(Graphics g)
		{
			IconManager im = IconManager.getInstance();
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			Dimension d = getSize();
			boolean tmp = selected;
			Color c;
			
			if(super.isOpaque()) 
			{
				c = new Color(0,0,0);
				g.setColor(c);
		
				g.drawOval(1,1,d.width-2,d.height-2);
				g.fillOval(6,6,d.width-12,d.height-12);
			}
			try 
			{
				setBorder(null);
				setOpaque(false);
				selected = false;
				super.paint(g);
			} 
			finally 
			{
				selected = tmp;
			}
			
			if (bordercolor != null) 
			{
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawOval(1,1,d.width-2,d.height-2);
			}
			if (selected) 
			{
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawOval(1,1,d.width-2,d.height-2);
			}
		}
	}
}
