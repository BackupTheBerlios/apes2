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

package org.ipsquad.apes.ui.tools;


import java.awt.Point;
import java.awt.Rectangle;
import java.awt.event.MouseEvent;
import java.util.Map;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.jgraph.JGraph;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * This tool allows to create cells in the graph
 * It use the prototype design pattern to clone cells
 *
 * @version $Revision: 1.10 $
 */
public class CellTool extends Tool
{
	private JGraph mGraph;
	private DefaultGraphCell mPrototype;
	private CellHandler mHandler = new CellHandler();

	/**
	 * Build a new CellTool
	 *
	 * @param prototype the cell to clone
	 */
	public CellTool(DefaultGraphCell prototype)
	{
		mPrototype = prototype;
	}

	public void install(JGraph graph)
	{
		mGraph = graph;
		graph.setMarqueeHandler(mHandler);
		graph.setMoveable(false);
		graph.setSizeable(false);
	}

	public void uninstall(JGraph graph)
	{
		mGraph = null;
		graph.setMoveable(true);
		graph.setSizeable(true);
	}

	public boolean isStable()
	{
		return false;
	}
	
	private class CellHandler extends BasicMarqueeHandler
	{
		public boolean isForceMarqueeEvent(MouseEvent e)
		{
			return false;
		}


		public void mousePressed(MouseEvent e)
		{
			fireToolStarted();

			mGraph.clearSelection();
			Point pt = mGraph.fromScreen(e.getPoint());

			ApesGraphCell vertex = (ApesGraphCell) mPrototype.clone();
			Map attr = vertex.getAttributes();

			GraphConstants.setBounds(attr, new Rectangle(pt, GraphConstants.getSize(attr)));

			Map attributes = ApesGraphConstants.createMap();
			attributes.put(vertex, attr);

			Object[] arg = new Object[]{vertex};

			((SpemGraphAdapter)mGraph.getModel()).insert(arg, attributes, null, null, null);
			
			if(mGraph.getModel().contains(vertex))
			{
				mGraph.setSelectionCell(vertex);
				
				// Makes user set a new name
				mGraph.startEditingAtCell(vertex);
			}
		}

		public void mouseReleased(MouseEvent e)
		{
			fireToolFinished();
		}

		public void mouseDragged(MouseEvent e) {}
		public void mouseMoved(MouseEvent e) {}
	}

}
