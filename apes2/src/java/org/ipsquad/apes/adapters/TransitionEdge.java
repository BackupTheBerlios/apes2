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


package org.ipsquad.apes.adapters;

import java.awt.Color;
import java.util.Map;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.ui.ColorFontPanel;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

/**
 * This is a convenient class which correct problems in the clone method
 *
 * @version $Revision: 1.1 $
 */
public class TransitionEdge extends DefaultEdge
{
	public TransitionEdge()
	{
		super( new ActivityDiagram.Transition() );
		init();
	}
	
	public TransitionEdge(Object userObject)
	{
		super( userObject );
		init();
	}
	
	public void init()
	{
		Map map = GraphConstants.createMap();
		int arrow = GraphConstants.ARROW_CLASSIC;
		GraphConstants.setLineEnd(map , arrow);
		GraphConstants.setEndFill(map, true);
		GraphConstants.setForeground(map, ColorFontPanel.getColor("GuardTitle"));
		GraphConstants.setBackground(map, Color.white);
		
		changeAttributes(map);	
	}
	
	public void setAttributes(Map change)
	{
		super.setAttributes(change);
		
		String guard = (String)GraphConstants.getValue(attributes);
		
		((ActivityDiagram.Transition) userObject).setLabel( guard );
	}
	
	public Map changeAttributes(Map change)
	{
		Map undo = GraphConstants.applyMap(change, attributes);
		
		if( GraphConstants.getValue(attributes) instanceof String)
		{
			String guard = (String)GraphConstants.getValue(attributes);
			if( userObject instanceof ActivityDiagram.Transition)
				((ActivityDiagram.Transition)userObject).setLabel( guard );
		}
		
		return undo;
	}
	
	public Object clone()
	{
		TransitionEdge t = (TransitionEdge) super.clone();
		t.userObject = ((ActivityDiagram.Transition)userObject).clone();
		t.init();
		return t;
	}
}

