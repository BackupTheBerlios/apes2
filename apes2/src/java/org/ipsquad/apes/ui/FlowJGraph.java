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

import java.awt.Dimension;
import java.util.Map;

import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.adapters.WorkProductStateCell;
import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.Edge;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

/**
 * Create a JGraph for a FlowDiagram
 *
 * @version $Revision: 1.1 $
 */
public class FlowJGraph extends JGraph
{
	public FlowJGraph(SpemGraphAdapter model)
	{
		super(model);
	}

	protected VertexView createVertexView(Object v, CellMapper cm) 
	{
		// Return the appropriate view
		if(v instanceof ProcessRoleCell)
		{
    			return new ProcessRoleView(v, this, cm);
		}
		if(v instanceof ActivityCell)
		{
			return new ActivityView(v, this, cm);
		}
		if(v instanceof WorkProductCell)
		{
			return new WorkProductView(v, this, cm);
		}
		if(v instanceof WorkProductStateCell)
		{
			return new WorkProductStateView(v, this, cm);
		}
		// Else Call Superclass
		return super.createVertexView(v, cm);
	}

	protected EdgeView createEdgeView(Object e, CellMapper cm)
	{
		EdgeView ev = super.createEdgeView((Object)e, cm);
		ev.update();
		
		if(e instanceof Edge && GraphConstants.getPoints(((Edge)e).getAttributes())==null)
		{
			Map evAttr = GraphConstants.cloneMap(ev.getAttributes());
			GraphConstants.setPoints(((Edge)e).getAttributes(), GraphConstants.getPoints(evAttr));
		}
		
		return ev;
	}

	public Dimension getPreferredSize()
	{
		Dimension ret = super.getPreferredSize();
		ret.height+=50;
		ret.width+=50;
		return ret;
	}
};
