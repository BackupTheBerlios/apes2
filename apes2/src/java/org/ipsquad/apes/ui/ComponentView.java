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
 * Display a Component cell
 *
 * @version $Revision: 1.9 $
 */
 
public class ComponentView extends ApesVertexView
{
	static ComponentViewRenderer renderer = new ComponentViewRenderer();
	// Constructor for Superclass
	
	public ComponentView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
	}



	protected Dimension calculateSize()
	{
		int width=calculateLabelLength();
		if(width<100)
			width=100;
		return new Dimension(width, 100);
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
		
		x[0]= r.x+r.width/2-50; //+ r.width/2 - 20;
		x[1]= r.x+r.width/2+50;//+ r.width/2 + 15;
		x[2]= r.x+r.width/2+50;//+ r.width/2 + 20;
		x[3]= r.x+r.width/2-50;//+ r.width/2 + 20;
		x[4]= r.x+r.width/2-50 ;//+ r.width/2 - 20;
		y[0]= r.y;
		y[1]= r.y;
		y[2]= r.y+r.height-15;//+ 5;
		y[3]= r.y+r.height-15;//+ r.height - 16;
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

	// Define the Renderer for a ComponentView
	static class ComponentViewRenderer extends ApesVertexView.ApesVertexRenderer
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
			int x2[]=new int[20];
			int y2[]=new int[20];
			int x3[]=new int[5];
			int y3[]=new int[5];
			
			x1[0]= d.width/2 + 5;
			x1[1]= d.width/2 + 35;
			x1[2]= d.width/2 + 45;
			x1[3]= d.width/2 + 45;
			x1[4]= d.width/2 +5;
			y1[0]= d.height/2-45;
			y1[1]= d.height/2-45;
			y1[2]= d.height/2-35;
			y1[3]= d.height + b - 46;
			y1[4]= d.height + b - 46;
			
			x2[0]= (d.width)/2-19;
			x2[1]= (d.width)/2-19 ;
			x2[2]= (d.width)/2-4;
			x2[3]= (d.width)/2-4;
			x2[4]= (d.width)/2-19;
			x2[5]= (d.width)/2-19;
			x2[6]= (d.width)/2-4;
			x2[7]= (d.width)/2-4;
			x2[8]= (d.width)/2-9;
			x2[9]= (d.width)/2-9;
			x2[10]= (d.width)/2-39;
			x2[11]= (d.width)/2-39;
			x2[12]= (d.width)/2-44;
			x2[13]= (d.width)/2-44;
			x2[14]= (d.width)/2-29;
			x2[15]= (d.width)/2-29;
			x2[16]= (d.width)/2-44;
			x2[17]= (d.width)/2-44;
			x2[18]= (d.width)/2-29;
			x2[19]= (d.width)/2-29;
		
			y2[0]=25;
			y2[1]= 30;
			y2[2]= 30;
			y2[3]= 35;
			y2[4]= 35;
			y2[5]= 45;	
			y2[6]= 45;
			y2[7]= 65;
			y2[8]= 65;
			y2[9]= 50;
			y2[10]= 50;	
			y2[11]= 65;
			y2[12]= 65;
			y2[13]= 45;
			y2[14]= 45;
			y2[15]= 35;	
			y2[16]= 35;
			y2[17]= 30;
			y2[18]= 30;
			y2[19]= 25;	
		
			x3[0]= (d.width)/2-25;
			x3[1]= (d.width)/2+30;
			x3[2]= (d.width)/2+45;
			x3[3]= (d.width)/2+30;
			x3[4]= (d.width)/2-25;
			y3[0]=50;
			y3[1]= 50;
			y3[2]= 66;
			y3[3]=82;
			y3[4]= 82;


			//Draws the Component
			if (super.isOpaque()) 
			{
				
				c = new Color(223,146,126);
				g.setColor(c);
				g.fillPolygon(x1,y1,5);
				
				
				g.setColor(bordercolor);
				
				for(int i=5+5; i<15+5; i+=5)
				{
					g.drawLine(d.width/2+10,i,d.width/2+30,i);
				}

				
				for(int i=15+5; i<55; i+=5)
				{
					g.drawLine(d.width/2+10,i,d.width/2+40,i);
				}
				
					
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawPolygon(x1,y1,5);
				g.drawLine(d.width/2 + 35,b+3,d.width/2 + 35,b+13);
				g.drawLine(d.width/2 + 35,b+13,d.width/2 + 45,b+13);
				
				c = new Color(0,0,255);
				g.setColor(c);
				g.fillOval(d.width/2-35, 5, 20,20);
				g.fillPolygon(x2,y2,20);
				
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawOval(d.width/2-35,5,20,20);
				g.drawPolygon(x2,y2,20);		
				
				c = new Color(255,252,43);
				g.setColor(c);
				g.fillPolygon(x3,y3,5);		
				
				g.setColor(bordercolor);
				
				g2.setStroke(new BasicStroke(b));
				g.drawPolygon(x3,y3,5);
					
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
				/*g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawPolygon(x,y,5);
				
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(b));
				g.drawPolygon(x2,y2,5);*/
				g.setColor(bordercolor);
				g2.setStroke(new BasicStroke(1));
				g.drawLine(d.width/2-50,d.height/2-50,d.width/2+49,d.height/2-50);
				g.drawLine(d.width/2+49,d.height/2-50,d.width/2+49,d.height/2+35);
				g.drawLine(d.width/2+49,d.height/2+35,d.width/2-50,d.height/2+35);
				g.drawLine(d.width/2-50,d.height/2+35,d.width/2-50,d.height/2-50);
			}
			if (selected) 
			{
				/*g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawPolygon(x,y,5);
				
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawPolygon(x2,y2,5);*/
				g2.setStroke(GraphConstants.SELECTION_STROKE);
				g.setColor(graph.getHighlightColor());
				g.drawLine(d.width/2-50,d.height/2-50,d.width/2+49,d.height/2-50);
				g.drawLine(d.width/2+49,d.height/2-50,d.width/2+49,d.height/2+35);
				g.drawLine(d.width/2+49,d.height/2+35,d.width/2-50,d.height/2+35);
				g.drawLine(d.width/2-50,d.height/2+35,d.width/2-50,d.height/2-50);
				
				
			}
		
		}
	}
}
