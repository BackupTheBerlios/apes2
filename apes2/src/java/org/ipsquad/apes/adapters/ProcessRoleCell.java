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


package org.ipsquad.apes.adapters;

import org.ipsquad.apes.model.spem.process.structure.ProcessRole;

/**
 * @version $Revision: 1.3 $
 */
public class ProcessRoleCell extends ApesGraphCell
{
	public ProcessRoleCell()
	{
		super(new ProcessRole(""));
	}
	
	public ProcessRoleCell(ProcessRole pr)
	{
		super(pr);
	}
	
	/*protected void init()
	{
		add(new DefaultPort());

		Map map = GraphConstants.createMap();
		GraphConstants.setSize(map, new Dimension(50,50));
		GraphConstants.setSizeable(map, false);
		GraphConstants.setLineWidth(map, 2);
		GraphConstants.setBorderColor(map, Color.black);
		GraphConstants.setBackground(map, Color.white);
		colorStyle(map,"RoleTitle");
		GraphConstants.setOpaque(map, true);
		changeAttributes(map);
	}*/
}

