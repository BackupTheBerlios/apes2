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

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.jgraph.graph.GraphConstants;

/**
 * @version $Revision: 1.3 $
 */
public class InitialPointCell extends ApesGraphCell
{
	public InitialPointCell()
	{
		super(new ActivityDiagram.InitialPoint());
		GraphConstants.setEditable(attributes, false);
	}
	
	public InitialPointCell(ActivityDiagram.InitialPoint ip)
	{
		super(ip);
		GraphConstants.setEditable(attributes, false);
	}
}
