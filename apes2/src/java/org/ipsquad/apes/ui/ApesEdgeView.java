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
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.event.MouseEvent;

import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.jgraph.JGraph;
import org.jgraph.graph.CellHandle;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.GraphContext;
import org.jgraph.graph.PortView;

/**
 * 
 * @version $revision$
 */
public class ApesEdgeView extends EdgeView
{
	//static boolean mIsMove = true;
	
	public ApesEdgeView(Object cell, JGraph graph, CellMapper mapper) 
	{	
		super(cell, graph, mapper);
	}	
	
	public CellHandle getHandle(GraphContext context)
	{
		return new ApesEdgeHandle(this, context);
	}

	/*public static void setIsMove( boolean isMove )
	{
		mIsMove = isMove;
	}*/
	
	public class ApesEdgeHandle extends EdgeView.EdgeHandle
	{
		private PortView mPort = null, mFirstPort = null;
		private Point mStart, mCurrent;
		
		public ApesEdgeHandle(EdgeView edge, GraphContext cxt)
		{
			super(edge, cxt);
		}
		
		public void mousePressed(MouseEvent event)
		{
			//if( mIsMove )
			//{
				int index = indexOfPoint(event.getPoint());
				
				if( index != -1 )
				{
					if( index == 0 )
					{
						mFirstPort = (PortView)getTarget();
						mStart = edge.getPoint(r.length-1);
					}
					else
					{
						mFirstPort = (PortView)getSource();
						mStart = edge.getPoint(0);
					}
					event.consume();			
				}
			//}
			//else
			//{
				super.mousePressed(event);
			//}
		}
		
		public void mouseReleased(MouseEvent e)
		{
			//if( mIsMove )
			//{	
				if( e!=null && !e.isConsumed() && mPort!=null && mFirstPort!=null && mFirstPort!=mPort)
				{
					graph.clearSelection();
				
					if( mFirstPort != null && mPort != null )
					{
						((SpemGraphAdapter)graph.getModel()).moveEdge( (DefaultEdge)getCell(),(ApesGraphCell)mPort.getParentView().getCell(), mFirstPort == getTarget() );
					}
				
					graph.repaint();
				}
				else
				{
					graph.repaint();
				}
			
				//e.consume();
			
				mFirstPort = mPort = null;
				mStart = mCurrent = null;
			//}
			//else
			//{
				super.mouseReleased(e);
			//}
		}
		
		public void mouseDragged(MouseEvent e)
		{
			//if( mIsMove )
			//{
				if( mStart!=null && !e.isConsumed())
				{
					Graphics g = graph.getGraphics();
				
					paintConnector(Color.black, graph.getBackground(), g);
				
					mPort = getTargetPortAt(e.getPoint());
					
					if(mPort != null)
					{
						mCurrent = graph.toScreen(mPort.getLocation(null));
					}
					else
					{
						mCurrent = graph.snap(e.getPoint());
					}
					
					paintConnector(graph.getBackground(), Color.black, g);
				
					e.consume();
				}
			//}
			//else
			//{
				super.mouseDragged(e);
			//}
		}
		
		public void mouseMoved(MouseEvent e)
		{
			//if( mIsMove )
			//{
			
				if( e != null && indexOfPoint(e.getPoint()) != -1 )
				{
					graph.setCursor(new Cursor(Cursor.HAND_CURSOR));
			//		e.consume();
				}
			//}
			//else
			//{
				super.mouseMoved(e);
			//}
		}
		
		private void paintConnector(Color fg, Color bg, Graphics g)
		{
			g.setColor(fg);
			g.setXORMode(bg);
			if( mPort != null )
			{	
				paintPort(graph.getGraphics(), mPort);
			}
			
			if(mFirstPort!=null && mStart!=null && mCurrent!=null)
			{
				g.drawLine(mStart.x, mStart.y, mCurrent.x, mCurrent.y);
			}
		}
		
		private PortView getTargetPortAt(Point point)
		{
			Object cell = graph.getFirstCellForLocation(point.x, point.y);
			
			for(int i=0; i<graph.getModel().getChildCount(cell); i++)
			{
				Object tmp = graph.getModel().getChild(cell, i);
				
				tmp = graph.getGraphLayoutCache().getMapping(tmp, false);
				
				if(tmp instanceof PortView)
				{
					PortView portView = (PortView)tmp;
					if( portView.getCell() != ((DefaultEdge)getCell()).getSource() 
							&& portView.getCell() != ((DefaultEdge)getCell()).getTarget())
					{	
						return (PortView)tmp;
					}
				}
			}

			return null;
		}
		
		private int indexOfPoint( Point p )
		{
			boolean bendable = graph.isBendable() && GraphConstants.isBendable(edge.getAllAttributes());
			boolean disconnectable = graph.isDisconnectable()	&& GraphConstants.isDisconnectable(orig.getAllAttributes());
			int x = p.x;
			int y = p.y;
			// Detect hit on control point
			int index = 0;
			while( index < r.length && !r[index].contains(x, y) ) 
			{
				index++;
			}
			return (index == 0 || index == r.length-1)? index : -1;
		}
	}
}
