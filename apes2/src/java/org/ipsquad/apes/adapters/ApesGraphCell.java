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
import java.awt.Dimension;
import java.awt.Font;
import java.util.Map;

import org.ipsquad.apes.Identity;
import org.ipsquad.apes.model.spem.core.Element;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

/**
 * This is a convenient class which correct problems in the clone method
 *
 * @version $Revision: 1.2 $
 */
public abstract class ApesGraphCell extends DefaultGraphCell implements Identity
{
/*	public ApesGraphCell()
	{
		init();
	}*/

	public ApesGraphCell(Object userObject)
	{
		super(userObject);
		init();
	}
	
	protected void init()
	{
		add(new DefaultPort());

		Map map = GraphConstants.createMap();
		GraphConstants.setSize(map, new Dimension(50,50));
		GraphConstants.setSizeable(map, false);
		GraphConstants.setLineWidth(map, 2);
		GraphConstants.setBorderColor(map, Color.black);
		
		ColorAssociater ca = new ColorAssociater();
		((Element)userObject).visit(ca);

		GraphConstants.setForeground(map, ca.getForeground());
		GraphConstants.setBackground(map, ca.getBackground());
		GraphConstants.setFont(map,new Font((String)map.get(GraphConstants.EDITABLE),ca.getFont(),13));
		 
		GraphConstants.setOpaque(map, true);
		changeAttributes(map);
		//JOptionPane.showMessageDialog((ApesFrame)Context.getInstance().getTopLevelFrame(),"apes1");
	}
	
	public Map changeAttributes(Map change)
	{
		Element oldUserObject = (Element)userObject;	
		Map undo = GraphConstants.applyMap(change, attributes);
		
		Object tmp = GraphConstants.getValue(attributes);
		
		return undo;
	}
	
	public int getID()
	{
		if(userObject instanceof Identity )
		{	
			return ((Identity)userObject).getID();
		}
		return -1;
	}
	
	public String toString()
	{	
		return ((Element)userObject).getName();
	}
	
	public Object clone()
	{
		Object o = ((Element)userObject).clone();
		ApesGraphCell c = (ApesGraphCell) super.clone();
		c.setUserObject(o);
		c.init();
		
		return c;
	}
};

