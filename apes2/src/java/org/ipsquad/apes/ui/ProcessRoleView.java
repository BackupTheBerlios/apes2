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


/**
 * Display a Process Role cell
 *
 * @version $Revision: 1.6 $
 */
 
public class ProcessRoleView extends ApesVertexView
{
	static ProcessRoleRenderer renderer = new ProcessRoleRenderer();
	// Constructor for Superclass
	
	public ProcessRoleView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
	}



	protected Dimension calculateSize()
	{
		int width=calculateLabelLength();
		if(width<55)
			width=55;
		return new Dimension(width, 70);
	}
	
	/**
	 * Returns the intersection of the bounding rectangle and the
	 * straight line between the source and the specified point p.
	 * The specified point is expected not to intersect the bounds.
	 */
	public Point getPerimeterPoint(Point source, Point p) 
	{
		Line2D.Double line1 = new Line2D.Double(p,getCenterPoint());
		Line2D.Double line2;
		Rectangle r = getBounds();
		Rectangle rContour = new Rectangle(r.x + (r.width)/2, r.y, 50/3, (r.height)/3);
		int x[]=new int[4];
		int y[]=new int[4];	
		
		x[0]= r.x + r.width/2 - 50/6;
		x[1]= r.x + r.width/2 + 25;
		x[2]= r.x + r.width/2 + 50/6;
		x[3]= r.x + r.width/2 - 25;
		y[0]= r.y + (r.height)/3;
		y[1]= r.y + (r.height)/3;
		y[2]= r.y + r.height - 16;
		y[3]= r.y + r.height - 16;
		
		
		if (line1.intersects(rContour) == true)
		{
			// Compute relative bounds
			int a = (rContour.width + 1) / 2;
			int b = (rContour.height + 1) / 2;

			// Get center
			int xCenter = rContour.x + a;
			int yCenter = rContour.y + b;

			// Compute angle
			int dx = p.x - xCenter;
			int dy = p.y - yCenter;
			double t = Math.atan2(dy, dx);

			// Compute Perimeter Point
			int xout = xCenter + (int) (a * Math.cos(t)) - 1;
			int yout = yCenter + (int) (b * Math.sin(t)) - 1;

			// Return perimeter point
			return new Point(xout, yout);
		}
		
		line2 = new Line2D.Double((double) x[0], (double) y[0], (double) x[1], (double) y[1]);
		
		if (line1.intersectsLine(line2) == true)
		{
			double a1 = line1.getY2() - line1.getY1() ; 
			double b1 = line1.getX1() - line1.getX2() ; 
			double c1 = line1.getY1() * line1.getX2() - line1.getX1() * line1.getY2() ;
			double a2 = line2.getY2() - line2.getY1() ; 
			double b2 = line2.getX1() - line2.getX2() ; 
			double c2 = line2.getY1() * line2.getX2() - line2.getX1() * line2.getY2() ;
			double det = a1 * b2 - a2 * b1 ;
			
			double xout = (c2 * b1 - c1 * b2) / det ;
			double yout = (a2 * c1 - a1 * c2) / det ;
			return new Point((int)xout, (int)yout);
		}
		
		line2 = new Line2D.Double((double) x[1], (double) y[1], (double) x[2], (double) y[2]);
		if (line1.intersectsLine(line2) == true)
		{
			double a1 = line1.getY2() - line1.getY1() ; 
			double b1 = line1.getX1() - line1.getX2() ; 
			double c1 = line1.getY1() * line1.getX2() - line1.getX1() * line1.getY2() ;
			double a2 = line2.getY2() - line2.getY1() ; 
			double b2 = line2.getX1() - line2.getX2() ; 
			double c2 = line2.getY1() * line2.getX2() - line2.getX1() * line2.getY2() ;
			double det = a1 * b2 - a2 * b1 ;
			
			double xout = (c2 * b1 - c1 * b2) / det ;
			double yout = (a2 * c1 - a1 * c2) / det ;
			return new Point((int)xout, (int)yout);
		}
		
		line2 = new Line2D.Double((double) x[2], (double) y[2], (double) x[3], (double) y[3]);
		if (line1.intersectsLine(line2) == true)
		{
			double a1 = line1.getY2() - line1.getY1() ; 
			double b1 = line1.getX1() - line1.getX2() ; 
			double c1 = line1.getY1() * line1.getX2() - line1.getX1() * line1.getY2() ;
			double a2 = line2.getY2() - line2.getY1() ; 
			double b2 = line2.getX1() - line2.getX2() ; 
			double c2 = line2.getY1() * line2.getX2() - line2.getX1() * line2.getY2() ;
			double det = a1 * b2 - a2 * b1 ;
			
			double xout = (c2 * b1 - c1 * b2) / det ;
			double yout = (a2 * c1 - a1 * c2) / det ;
			return new Point((int)xout, (int)yout);
		}
		
		line2 = new Line2D.Double((double) x[3], (double) y[3], (double) x[0], (double) y[0]);
		if (line1.intersectsLine(line2) == true)
		{
			double a1 = line1.getY2() - line1.getY1() ; 
			double b1 = line1.getX1() - line1.getX2() ; 
			double c1 = line1.getY1() * line1.getX2() - line1.getX1() * line1.getY2() ;
			double a2 = line2.getY2() - line2.getY1() ; 
			double b2 = line2.getX1() - line2.getX2() ; 
			double c2 = line2.getY1() * line2.getX2() - line2.getX1() * line2.getY2() ;
			double det = a1 * b2 - a2 * b1 ;
			
			double xout = (c2 * b1 - c1 * b2) / det ;
			double yout = (a2 * c1 - a1 * c2) / det ;
			return new Point((int)xout, (int)yout);
		}
		return getCenterPoint();
	}
	
	// Returns the Renderer for this View
	public CellViewRenderer getRenderer()
	{
  		return renderer;
	}

	// Define the Renderer for a ProcessRoleView
	static class ProcessRoleRenderer extends ApesVertexView.ApesVertexRenderer
	{
		public void paint(Graphics g)
		{
			IconManager im = IconManager.getInstance();
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			Dimension d = getSize();
			boolean tmp = selected;
			Color c;
			int x[]=new int[4];
			int y[]=new int[4];
			
			x[0]= d.width/2 - 50/6;
			x[1]= d.width/2 + 25;
			x[2]= d.width/2 + 50/6;
			x[3]= d.width/2 - 25;
			y[0]= b - 1 + (d.height - b)/3;
			y[1]= b - 1 + (d.height - b)/3;
			y[2]= d.height - b - 16;
			y[3]= d.height - b - 16;
			
			if (super.isOpaque()) 
			{
				c = new Color(245,197,146);
				g.setColor(c);
				g.fillOval(b - 1 + d.width/2, b - 1, 50/3, (d.height - b)/3);
				g.fillPolygon(x,y,4);
				
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
				g.drawOval(b - 1 + d.width/2, b - 1, 50/3, (d.height - b)/3);
				g.drawPolygon(x,y,4);
			}
			if (selected) 
			{
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawOval(b - 1 + d.width/2, b - 1, 50/3, (d.height - b)/3);
				g.drawPolygon(x,y,4);
			}
		}
	}
}
