/*
 * APES is a Process Engineering Software
 * Copyright (C) 2004 IPSquad
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

import java.awt.Font;
import java.util.Map;

import javax.swing.tree.DefaultMutableTreeNode;

import org.ipsquad.apes.Identity;
import org.ipsquad.apes.model.spem.core.Element;
import org.jgraph.graph.GraphConstants;

/**
 * 
 *
 * @version $Revision
 *
 */
public class ApesTreeNode extends DefaultMutableTreeNode implements Identity
{
	private Map mAttributes = GraphConstants.createMap();
	
	public ApesTreeNode(Object userObject, boolean isLeaf)
	{
		super(userObject);
		init();
		
	}
	public void init()
	{
		if( userObject instanceof Element )
		{	
			ColorAssociater c = new ColorAssociater();
			((Element)userObject).visit(c);
			GraphConstants.setForeground(mAttributes,c.getForeground());
			GraphConstants.setBackground(mAttributes,c.getBackground());
			GraphConstants.setFont(mAttributes,new Font(GraphConstants.EDITABLE,c.getFont(),13));
		}
	}

	public ApesTreeNode(Object userObject, ApesTreeNode parent)
	{
		super(userObject);
		init();
		setParent(parent);
	}
	
	public Map getAttributes()
	{
		return mAttributes;
	}
	
	public Map changeAttributes(Map change)
	{
		Element oldUserObject = (Element)userObject;	
		Map undo = GraphConstants.applyMap(change, getAttributes());
		
		return undo;
	}
	
	public int getID()
	{
		if( getUserObject() instanceof Identity )
		{
			return ((Identity)getUserObject()).getID();
		}
		return -1;
	}
	
	public String getName()
	{
		return getUserObject().toString();
	}
	
	public String toString()
	{
		return getName();
	}
	
	public Object clone()
	{
		Object o = ((Element)userObject).clone();
		ApesTreeNode node = (ApesTreeNode) super.clone();
		node.setUserObject(o);
		node.init();
		
		return node;
	}
}
