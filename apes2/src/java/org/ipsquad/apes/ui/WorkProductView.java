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
 * Display a WorkProductState cell
 *
 * @version $Revision: 1.5 $
 */
 
public class WorkProductView extends ApesVertexView
{
	static WorkProductRenderer renderer = new WorkProductRenderer();
	// Constructor for Superclass
	
	public WorkProductView(Object cell, JGraph graph, CellMapper cm)
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
		
		x[0]= r.x + r.width/2 - 20;
		x[1]= r.x + r.width/2 + 15;
		x[2]= r.x + r.width/2 + 20;
		x[3]= r.x + r.width/2 + 20;
		x[4]= r.x + r.width/2 - 20;
		y[0]= r.y;
		y[1]= r.y;
		y[2]= r.y + 5;
		y[3]= r.y + r.height - 16;
		y[4]= r.y + r.height - 16;
		
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

	// Define the Renderer for a WorkProductView
	static class WorkProductRenderer extends ApesVertexView.ApesVertexRenderer	
	{
		public void paint(Graphics g)
		{
			IconManager im = IconManager.getInstance();
			int b = borderWidth;
			Graphics2D g2 = (Graphics2D) g;
			Dimension d = getSize();
			boolean tmp = selected;
			Color c;
			int x[]=new int[5];
			int y[]=new int[5];
			
			x[0]= d.width/2 - 20;
			x[1]= d.width/2 + 10;
			x[2]= d.width/2 + 20;
			x[3]= d.width/2 + 20;
			x[4]= d.width/2 - 20;
			y[0]= b - 1;
			y[1]= b - 1;
			y[2]= b - 1 + 10;
			y[3]= d.height - b - 16;
			y[4]= d.height - b - 16;
			
			if (super.isOpaque()) 
			{
				c = new Color(223,146,126);
				g.setColor(c);
				g.fillPolygon(x,y,5);
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
				g.drawPolygon(x,y,5);
				g.drawLine(x[1],y[1],x[1],y[2]);
				g.drawLine(x[1],y[2],x[2],y[2]);
				
				g2.setStroke(new BasicStroke(1));
				
				for(int i=5; i<15; i+=5)
				{
					g.drawLine(d.width/2-15,i,d.width/2+5,i);
				}

				
				for(int i=15; i<50; i+=5)
				{
					g.drawLine(d.width/2-15,i,d.width/2+15,i);
				}
				
				g2.setStroke(new BasicStroke(b));
			}
			if (selected) 
			{
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawPolygon(x,y,5);
				g.drawLine(x[1],y[1],x[1],y[2]);
				g.drawLine(x[1],y[2],x[2],y[2]);
			}
		}
	}
}
