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

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.DecisionCell;
import org.ipsquad.apes.adapters.FinalPointCell;
import org.ipsquad.apes.adapters.InitialPointCell;
import org.ipsquad.apes.adapters.NoteCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SynchroCell;
import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.EdgeView;
import org.jgraph.graph.VertexView;

/**
 * Create a JGraph for an ActivityDiagram
 *
 * @version $Revision: 1.5 $
 */
public class ActivityJGraph extends JGraph
{
	public ActivityJGraph(SpemGraphAdapter model)
	{
		super(model);
		getActionMap().put("copy",Context.getInstance().getAction("Copy"));
		getActionMap().put("paste",Context.getInstance().getAction("Paste"));
	}

	protected VertexView createVertexView(Object v, CellMapper cm) 
	{
		// Return the appropriate view
		if(v instanceof ActivityCell)
		{
			return new ActivityView(v, this, cm);
		}
		else if(v instanceof DecisionCell)
		{
			return new DecisionView(v, this, cm);
		}
		else if(v instanceof InitialPointCell)
		{
			return new InitialPointView(v, this, cm);
		}
		else if(v instanceof FinalPointCell)
		{
			return new FinalPointView(v, this, cm);
		}
		else if(v instanceof SynchroCell)
		{
			return new SynchroView(v, this, cm);
		}
		else if(v instanceof NoteCell)
		{
			return new NoteView(v, this, cm);
		}
		// Else Call Superclass
		return super.createVertexView(v, cm);
	}

	
	protected EdgeView createEdgeView(Object e, CellMapper cm) 
	{
		return new TransitionEdgeView(e, this, cm);
	}

	public Dimension getPreferredSize()
	{
		Dimension ret = super.getPreferredSize();
		ret.height+=50;
		ret.width+=50;
		return ret;
	}
};
