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

import java.awt.event.MouseEvent;

import org.jgraph.JGraph;
import org.jgraph.event.GraphSelectionEvent;
import org.jgraph.event.GraphSelectionListener;
import org.jgraph.graph.BasicMarqueeHandler;
import org.jgraph.graph.CellView;

/**
 * The default graph manipulating tool
 * Allows to move, resize, select and edit portion of the graph
 * You can use the marquee handler.
 * 
 * @version $Revision: 1.5 $
 */
public class DefaultTool extends Tool
{
	private JGraph mGraph;
	private DefaultHandler mHandler = new DefaultHandler();

	public void install(JGraph graph)
	{
		if(graph!=mGraph && mGraph==null)
		{
			mGraph = graph;
			graph.setMarqueeHandler(mHandler);
			graph.addGraphSelectionListener(mHandler);
		}
	}

	public void uninstall(JGraph graph)
	{
		if(graph==mGraph && mGraph!=null)
		{
			mGraph=null;
			graph.removeGraphSelectionListener(mHandler);
		}
	}


	public boolean isStable()
	{
		return true;
	}
	
	
	class DefaultHandler extends BasicMarqueeHandler implements GraphSelectionListener
	{
		public boolean isForceMarqueeEvent(MouseEvent e)
		{
			return super.isForceMarqueeEvent(e);
		}

		public void mouseDragged(MouseEvent e)
		{
			super.mouseDragged(e);
		}

		public void mouseMoved(MouseEvent e)
		{
			super.mouseMoved(e);
		}

		public void mousePressed(MouseEvent e)
		{
			fireToolStarted(); 
			super.mousePressed(e);
		}

		public void mouseReleased(MouseEvent e)
		{
			super.mouseReleased(e);
			fireToolFinished();
		}
		
		public void valueChanged(GraphSelectionEvent e)
		{
			Object[] selected = e.getCells();
			
			if(selected.length==1
			&& !e.isAddedCell(selected[0])
			&& mGraph.getSelectionModel().isCellSelected(selected[0])
			&& mGraph.getModel().contains(selected[0]))
			{
				CellView cv = mGraph.getGraphLayoutCache().getMapping(selected[0], false);
				Object[] intersected = mGraph.getGraphLayoutCache().getRoots(cv.getBounds());
				intersected = mGraph.getGraphLayoutCache().order(intersected);

				if(intersected[0]!=cv)
				{
					Object[] arg = new Object[]{selected[0]};
					mGraph.getGraphLayoutCache().toFront(mGraph.getGraphLayoutCache().getMapping(arg));
				}
			}
		}
	}

}
