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

package org.ipsquad.apes.ui.tools;

import java.awt.Color;
import java.awt.Cursor;
import java.awt.Graphics;
import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Map;

import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.ui.ApesEdgeView;
import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.PortView;

/**
 * This tool allows to create edges in the graph
 * It use the prototype design pattern to clone edges
 *
 * @version $Revision: 1.3 $
 */
public class EdgeTool extends Tool
{
	private JGraph mGraph;
	private DefaultEdge mPrototype;
	private EdgeHandler mHandler = new EdgeHandler();
	private boolean mStable = true;
	
	/**
	 * Build a new EdgeTool
	 *
	 * @param prototype the edge to clone
	 */
	public EdgeTool(DefaultEdge prototype)
	{
		mPrototype = prototype;
	}

	public void install(JGraph graph)
	{
		mGraph = graph;
		graph.setMarqueeHandler(mHandler);
		graph.setMoveable(false);
		graph.setSizeable(false);
		graph.setPortsVisible(true);
		ApesEdgeView.setIsMove(false);
	}

	public void uninstall(JGraph graph)
	{
		mGraph = null;
		graph.setMoveable(true);
		graph.setSizeable(true);
		graph.setPortsVisible(false);
		ApesEdgeView.setIsMove(true);
	}

	public boolean isStable()
	{
		boolean oldStable = mStable;
		if( !mStable )
		{
			mStable = true;
		}

		return oldStable;
	}

	private class EdgeHandler extends BasicMarqueeHandler
	{
		private PortView mPort, mFirstPort;
		private Point mStart, mCurrent;
		
		public boolean isForceMarqueeEvent(MouseEvent e)
		{
			mPort = getSourcePortAt(e.getPoint());
			if(mPort != null && mGraph.isPortsVisible())
				return true;
			return false;
		}


		public void mousePressed(MouseEvent e)
		{
			if(mPort!=null && !e.isConsumed() && mGraph.isPortsVisible())
			{
				fireToolStarted();
				mStart = mGraph.toScreen(mPort.getLocation(null));
				mFirstPort = mPort;
				e.consume();			
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			if(e!=null && !e.isConsumed() && mPort!=null && mFirstPort!=null && mFirstPort!=mPort)
			{
				mGraph.clearSelection();
				
				if( mFirstPort.getParentView() != null && mPort.getParentView() != null )
				{
					DefaultEdge edge = (DefaultEdge) mPrototype.clone();
					
					Map view = new HashMap();
					view.put( "firstPort",mFirstPort.getCell());
					view.put( "endPort",mPort.getCell());
					Map attr = mPrototype.getAttributes();
					//attr.put(edge, attr);

					view.put("Attributes", attr);
					
					((SpemGraphAdapter)mGraph.getModel()).insertEdge( (ApesGraphCell)mFirstPort.getParentView().getCell(), (ApesGraphCell)mPort.getParentView().getCell(), view);
				}
				
				e.consume();
				
				mGraph.repaint();
			}
			else
			{
				//mGraph.repaint();
				mStable = false;
			}
			
			mFirstPort = mPort = null;
			mStart = mCurrent = null;

			fireToolFinished();
		}

		public void mouseDragged(MouseEvent e)
		{
			if(mStart!=null && !e.isConsumed())
			{
				Graphics g = mGraph.getGraphics();
				
				paintConnector(Color.black, mGraph.getBackground(), g);
				
				mPort = getTargetPortAt(e.getPoint());
				
				if(mPort!=null)
				{
					mCurrent = mGraph.toScreen(mPort.getLocation(null));
				}
				else
				{
					mCurrent = mGraph.snap(e.getPoint());
				}
				
				paintConnector(mGraph.getBackground(), Color.black, g);
				
				e.consume();
			}

		}
		
		public void mouseMoved(MouseEvent e)
		{
			if(e!=null && getSourcePortAt(e.getPoint())!=null && !e.isConsumed() && mGraph.isPortsVisible())
			{
				mGraph.setCursor(new Cursor(Cursor.HAND_CURSOR));
				e.consume();
			}
		}


		private PortView getSourcePortAt(Point point)
		{
			if(point == null || mGraph == null)
			{
				return null;
			}
			
			Point tmp = mGraph.fromScreen(new Point(point));
			
			
			return (PortView)mGraph.getPortViewAt(tmp.x, tmp.y);
		}

		private PortView getTargetPortAt(Point point)
		{
			Object cell = mGraph.getFirstCellForLocation(point.x, point.y);
			
			for(int i=0; i<mGraph.getModel().getChildCount(cell); i++)
			{
				Object tmp = mGraph.getModel().getChild(cell, i);
				
				tmp = mGraph.getGraphLayoutCache().getMapping(tmp, false);
				
				if(tmp instanceof PortView && tmp!=mFirstPort)
				{
					return (PortView) tmp;
				}
			}

			return getSourcePortAt(point);
		}

		
		private void paintConnector(Color fg, Color bg, Graphics g)
		{
			g.setColor(fg);
			g.setXORMode(bg);
			paintPort(mGraph.getGraphics());
			
			if(mFirstPort!=null && mStart!=null && mCurrent!=null)
			{
				g.drawLine(mStart.x, mStart.y, mCurrent.x, mCurrent.y);
			}
		}
		
		private void paintPort(Graphics g)
		{
			if(mPort!=null)
			{
				boolean o = (GraphConstants.getOffset(mPort.getAttributes()) != null);
				Rectangle r = (o) ? mPort.getBounds() : mPort.getParentView().getBounds();
				r = mGraph.toScreen(new Rectangle(r));
				r.setBounds(r.x-3, r.y-3, r.width+6, r.height+6);
				mGraph.getUI().paintCell(g, mPort, r, true);
			}
		}
	}
}
