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
 * Display a WorkDefinition cell
 *
 * @version $$
 */

public class WorkDefinitionView extends ApesVertexView
{
	static WorkDefinitionRenderer renderer = new WorkDefinitionRenderer();
	// Constructor for Superclass
	
	public WorkDefinitionView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
	}



	protected Dimension calculateSize()
	{
		int width=calculateLabelLength();
		if(width<45)
			width=45;
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
		int x[]=new int[5];
		int y[]=new int[5];	
		
		x[0]= r.x ;
		x[1]= r.x + r.width;
		x[2]= r.x + r.width;
		x[3]= r.x;
		x[4]= r.x;
		y[0]= r.y;
		y[1]= r.y;
		y[2]= r.y + r.height-16;
		y[3]= r.y + r.height-16;
		y[4]= r.y;
		
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
		
		line2 = new Line2D.Double((double) x[3], (double) y[3], (double) x[4], (double) y[4]);
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
		
		line2 = new Line2D.Double((double) x[4], (double) y[4], (double) x[0], (double) y[0]);
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

	// Define the Renderer for a WorkDefinitionView
	static class WorkDefinitionRenderer extends ApesVertexView.ApesVertexRenderer	
	{
		public void paint(Graphics g)
		{
			IconManager im = IconManager.getInstance();
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			Dimension d = getSize();
			boolean tmp = selected;
			Color c;
			int x1[]=new int[5];
			int y1[]=new int[5];
			int x2[]=new int[5];
			int y2[]=new int[5];
			
			
			x1[0]= d.width/2-26; //5
			x1[1]= d.width/2-6;
			x1[2]= d.width/2+4;
			x1[3]= d.width/2-6;
			x1[4]= d.width/2-26;
			y1[0]= 5;
			y1[1]= 5;
			y1[2]= 15;
			y1[3]= 25;
			y1[4]= 25;
			
			x2[0]= d.width/2-4;
			x2[1]= d.width/2+16;
			x2[2]= d.width/2+26;
			x2[3]= d.width/2+16;
			x2[4]= d.width/2-4;
			y2[0]= 31;
			y2[1]= 31;
			y2[2]= 41;
			y2[3]= 51;
			y2[4]= 51;
			
			
			if (super.isOpaque()) 
			{
				c = new Color(255,252,43);
				g.setColor(c);
				g.fillPolygon(x1,y1,5);
				g.fillPolygon(x2,y2,5);
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawLine(d.width/2+4,15,d.width/2+19,15);
				g.drawArc(d.width/2+14,15,13,13,-90,180);
				g.drawLine(d.width/2+19,28,d.width/2-19,28);
				g.drawArc(d.width/2-26,28,13,13,90,180);
				g.drawLine(d.width/2-19,41,d.width/2-4,41);
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
				g2.setStroke(new BasicStroke(1));	
				g.drawLine(d.width/2-30,1,d.width/2+29,1);
				g.drawLine(d.width/2+29,1,d.width/2+29,54);
				g.drawLine(d.width/2+29,54,d.width/2-30,54);
				g.drawLine(d.width/2-30,54,d.width/2-30,1);
				
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawPolygon(x1,y1,5);
				g.drawPolygon(x2,y2,5);
			}
			if (selected) 
			{
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawPolygon(x1,y1,5);
				g.drawPolygon(x2,y2,5);

				g.drawLine(d.width/2+4,15,d.width/2+19,15);
				g.drawArc(d.width/2+14,15,13,13,-90,180);
				g.drawLine(d.width/2+19,28,d.width/2-19,28);
				g.drawArc(d.width/2-26,28,13,13,90,180);
				g.drawLine(d.width/2-19,41,d.width/2-4,41);
			}
		}
	}
}
