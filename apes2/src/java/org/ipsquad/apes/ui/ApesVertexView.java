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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.ipsquad.apes.adapters.ApesGraphCell;
import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.CellView;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexRenderer;
import org.jgraph.graph.VertexView;

/**
 *
 * @version $Revision: 1.7 $
 */
public class ApesVertexView extends VertexView
{
	public ApesVertexView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
		GraphConstants.setVerticalAlignment(attributes, 3); // 3 => BOTTOM
		
		Rectangle r = GraphConstants.getBounds(allAttributes);
		if( r != null && r.getSize() != calculateSize() && cell instanceof DefaultGraphCell )
		{
			Map apply = GraphConstants.createMap();
			r.setSize(calculateSize());
			GraphConstants.setBounds(apply,r);
			((DefaultGraphCell)cell).changeAttributes(apply);
		}
	}

	protected Dimension calculateSize()
	{
		return new Dimension(calculateLabelLength(), 50);
	}
	
	protected int calculateLabelLength()
	{
		Object user_object = ((ApesGraphCell)getCell()).getUserObject();
		Graphics2D g = (Graphics2D)getGraph().getGraphics();
		Font f = GraphConstants.getFont(getAllAttributes());
		
		if(g!=null)
		{
			Rectangle2D rect = f.getStringBounds(user_object.toString(), g.getFontRenderContext());
			return (int)rect.getWidth()+10;
		}
		else
		{
			return 10+user_object.toString().length()*10;
		}
	}
	
	public Map getAttributes()
	{
		Dimension d = calculateSize();
		GraphConstants.setSize(attributes, d);
		GraphConstants.setSize(((ApesGraphCell)cell).getAttributes(), d);

		if(GraphConstants.getBounds(attributes)!=null)
		{
			Rectangle r = GraphConstants.getBounds(attributes);
			Point p = r.getLocation();
			
			p.x-= (d.width - r.width)/2;
			
			GraphConstants.setBounds(attributes, new Rectangle(p, d));
		}

		update();
		
		return super.getAttributes();
	}
	
	public Rectangle getBounds()
	{
		Rectangle rect = super.getBounds();
		
		if(rect != null)
		{	
			rect.setSize(calculateSize());
			return rect;
		}
	
		return null;
	}

	public GraphCellEditor getEditor()
	{
		return new ApesGraphCellEditor((GraphCell)cell);
	}

	protected static class ApesVertexRenderer extends VertexRenderer
	{
		private CellView mCurrentView = null;
		
		public Component getRendererComponent(JGraph graph, CellView view, boolean sel, 
											  boolean focus, boolean preview)
		{
			mCurrentView = view;
			return super.getRendererComponent(graph, view, sel, focus, preview);
		}
			
		public void paint(Graphics g) 
		{
			if(mCurrentView != null && mCurrentView.getCell().toString().equals(getText()))
			{
				Graphics2D g2 = (Graphics2D) g;
				Dimension d = getSize();
				int b = borderWidth;
				String text = getText();
				Font f = GraphConstants.getFont(mCurrentView.getAllAttributes());
				Rectangle2D rect = f.getStringBounds(getText(), g2.getFontRenderContext());
					
				g.setColor(GraphConstants.getBackground(mCurrentView.getAllAttributes()));
				g.fillRect((int)rect.getX()+b,(int)(rect.getY()+d.getHeight()-b),(int)d.getWidth()-b,(int)d.getHeight()-b);
			}	
			super.paint(g);
		}
	}
}
