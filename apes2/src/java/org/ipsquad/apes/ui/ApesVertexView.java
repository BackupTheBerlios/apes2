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

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.Map;

import org.ipsquad.apes.ApesGraphConstants;
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
 * @version $Revision: 1.13 $
 */
public class ApesVertexView extends VertexView
{
	protected int mMinimumWidth = 50;
	protected int mMinimumHeight = 50;
	
	public ApesVertexView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
		GraphConstants.setVerticalAlignment(attributes, 3); // 3 => BOTTOM		
	}

	protected void init()
	{
		Rectangle r = ApesGraphConstants.getBounds(allAttributes);
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
		return new Dimension(Math.max(calculateLabelLength(),mMinimumWidth), mMinimumHeight);
	}
	
	protected int calculateLabelLength()
	{
		Object user_object = ((ApesGraphCell)getCell()).getUserObject();
		Graphics2D g = (Graphics2D)getGraph().getGraphics();
		Font f = ApesGraphConstants.getFont(getAllAttributes());
		
		if(g!=null)
		{
			Rectangle2D rect = f.getStringBounds(user_object.toString(), g.getFontRenderContext());
			return (int)rect.getWidth()+10;
		}
		else
		{
			//if g is not available, just return width of the cell
			//else the alignement is lost when a file is loaded    
			
			//return 10+user_object.toString().length()*10;
			return ApesGraphConstants.getBounds(allAttributes).width;
		}
	}

	public Rectangle getBounds()
	{
		Rectangle rect = super.getBounds();
		
		if(rect != null)
		{	
			Dimension newSize = calculateSize();
			
			//the new size is different if the name of the element have changed
			//we must center the cell to not modify potential previous alignements
			if(!rect.getSize().equals(newSize))
			{
				double centerX = rect.getCenterX();
				
				rect.setSize(newSize);
				rect.setLocation((int)centerX - newSize.width / 2, rect.y);
				
				//update the bounds and the size of the cell to center it
				ApesGraphConstants.setBounds(((ApesGraphCell)cell).getAttributes(), rect);
				ApesGraphConstants.setSize(((ApesGraphCell)cell).getAttributes(), rect.getSize());
			}
			
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
		protected CellView mCurrentView = null;
		
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
