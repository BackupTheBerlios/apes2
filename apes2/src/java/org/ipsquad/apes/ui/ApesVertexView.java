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
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.geom.Rectangle2D;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.ui.actions.ChangeColorAction;
import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphCellEditor;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

/**
 *
 * @version $Revision: 1.1 $
 */
public class ApesVertexView extends VertexView
{
	public ApesVertexView(Object cell, JGraph graph, CellMapper cm)
	{
		super(cell, graph, cm);
		GraphConstants.setVerticalAlignment(attributes, 3); // 3 => BOTTOM
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
	
	/*public CellViewRenderer getRenderer()
	{
		return new ApesVertexRenderer();
	}*/

	public GraphCellEditor getEditor()
	{
		return new ApesGraphCellEditor();
	}

	/*private class ApesVertexRenderer extends VertexRenderer
	{
		public Component getRendererComponent(JGraph graph, CellView view, boolean sel, 
											  boolean focus, boolean preview)
		{
			Component c = super.getRendererComponent(graph, view, sel, focus, preview);
			System.out.println("GETCELLLLL");
			if( view.getCell() instanceof DefaultGraphCell )
			{
				DefaultGraphCell node = (DefaultGraphCell)view.getCell();
				Color fc = GraphConstants.getForeground(node.getAttributes());
				Color bc = GraphConstants.getBackground(node.getAttributes());
				System.out.println("getCell "+node+" "+bc);
				Font font = GraphConstants.getFont(node.getAttributes());
				
				setBackground(bc);
				setForeground(fc);
				setFont(font);
			}
			
			return c;
		}
	}*/
	
	private class ApesGraphCellEditor extends DefaultGraphCellEditor implements ChangeColorAction.ColorChangeListener, CellEditorListener
	{
		private Color mInitForeground = null;
		private Color mCurrentForeground = null;
		
		private Color mInitBackground = null;
		private Color mCurrentBackground = null;
		
		private Font mInitFont = null;
		private Font mCurrentFont = null;
		
		private ChangeColorAction mActionForeground = (ChangeColorAction)Context.getInstance().getAction("ChangeForeground");
		private ChangeColorAction mActionBackground = (ChangeColorAction)Context.getInstance().getAction("ChangeBackground");
		
		public ApesGraphCellEditor()
		{
			super();
			mActionForeground.addChangeColorListener(this);	
			mActionBackground.addChangeColorListener(this);	
			
			mInitForeground = GraphConstants.getForeground(((DefaultGraphCell)cell).getAttributes());
			mInitBackground = GraphConstants.getBackground(((DefaultGraphCell)cell).getAttributes());
			mInitFont = GraphConstants.getFont(((DefaultGraphCell)cell).getAttributes());
			
			mActionForeground.setEnabled(true);
			mActionBackground.setEnabled(true);
			addCellEditorListener(this);
		}
		
		public Component getGraphCellEditorComponent(
			JGraph graph,
			Object value,
			boolean isSelected)
		{
			Component c = super.getGraphCellEditorComponent(graph, cell, isSelected);
			
			if( editingComponent instanceof JTextField && value instanceof DefaultGraphCell )
			{
				Color fc = mCurrentForeground == null ? mInitForeground : mCurrentForeground;
				Color bc = mCurrentBackground == null ? mInitBackground : mCurrentBackground;
				Font font = mCurrentFont == null ? mInitFont : mCurrentFont;
				
				mActionForeground.setColor(fc);
				mActionBackground.setColor(bc);
				
				((JTextField)editingComponent).setForeground(fc);
				((JTextField)editingComponent).setSelectedTextColor(fc);
				((JTextField)editingComponent).setBackground(bc);
				((JTextField)editingComponent).setFont( font );
				((JTextField)editingComponent).selectAll();
			}
			
			return c;
		}

		public void colorChanged(Color c)
		{
			if(editingComponent instanceof JTextField)
			{
				if( c == mActionForeground.getColor() )
				{	
					mCurrentForeground = c;
				}
				else
				{
					mCurrentBackground = c;
				}
				
				getGraphCellEditorComponent(graph,cell,true);
			}
		}

		public void editingCanceled(ChangeEvent e)
		{
			mCurrentForeground = null;
			mCurrentBackground = null;
			mCurrentFont = null;
			
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
		}

		public void editingStopped(ChangeEvent e)
		{
			DefaultGraphCell cell = (DefaultGraphCell)getCell();
			Map map = GraphConstants.cloneMap(cell.getAttributes());
			boolean hasChanged = false;
			
			if( mCurrentForeground != null && mInitForeground != mCurrentForeground )
			{
				GraphConstants.setForeground(map,mCurrentForeground);
				hasChanged = true;
			}
			if( mCurrentBackground != null &&  mInitBackground != mCurrentBackground )
			{
				GraphConstants.setBackground(map,mCurrentBackground);
				hasChanged = true;
			}
			if(  mCurrentFont != null && mInitFont != mCurrentFont )
			{
				GraphConstants.setFont(map,mCurrentFont);
				hasChanged = true;
			}
			
			if( hasChanged )
			{	
				Map edit = new HashMap();
				edit.put(cell,map);
				getModel().edit(edit,null,null,null);
			}
			
			mCurrentForeground = null;
			mCurrentBackground = null;
			mCurrentFont = null;
			
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
		}
	}
}
