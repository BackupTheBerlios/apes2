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

import java.util.Map;

import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.GraphConstants;

/**
 * @version $Revision: 1.1 $
 */
public class NoteEdge extends DefaultEdge
{
	public NoteEdge()
	{
		super();
		init();
	}
	
	public void init()
	{
		Map map = GraphConstants.createMap();
		GraphConstants.setDashPattern(map, new float[] { 3, 3 });
		GraphConstants.setEditable(map, false);
		GraphConstants.setLineEnd(map,GraphConstants.ARROW_NONE);
		
		changeAttributes(map);	
	}
	
	public Object clone()
	{
		NoteEdge t = (NoteEdge) super.clone();
		t.init();
		return t;
	}
}
