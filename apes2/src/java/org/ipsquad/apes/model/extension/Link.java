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

package org.ipsquad.apes.model.extension;

import java.io.Serializable;

/**
 * Class for represent a link in a diagram
 *
 * @version $Revision: 1.6 $
 */
public class Link implements Serializable, Cloneable
{
	private Object mSource;
	private Object mTarget;
	private String mLabel = "";
	private Object mExtras = null;
	
	public Link()
	{   
	}
	
	public Link(Object source, Object target)
	{
		mSource = source;
		mTarget = target;
	}
	
	public Link(Object source, Object target, String label)
	{
		this(source, target);
		mLabel = label;
	}
	
	public Link(Object source, Object target, String label, Object extras)
	{
		this(source, target);
		mLabel = label;
		mExtras = extras;
	}
	
	public Object getSource()
	{
		return mSource;
	}
	
	public Object getTarget()
	{
		return mTarget;
	}
	
	public String getLabel()
	{
	    return mLabel;
	}
	
	public Object getExtras()
	{
		return mExtras;
	}
	
	public void setSource(Object source)
	{
		mSource = source;
	}
	
	public void setTarget(Object target)
	{
		mTarget = target;
	}
	
	public void setLabel(String label)
	{
	    mLabel = label;
	}
	
	public void setExtras(Object extras)
	{
		mExtras = extras;
	}
	
	public Object clone()
	{
		return new Link( mSource, mTarget, mLabel, mExtras);
	}
	
	public String toString()
	{
	    return "link("+mSource+" - "+mTarget+", "+mExtras+")";
	}
	
	public boolean equals(Object o)
	{
		if(o != null && o instanceof Link)
		{
			return mSource == ((Link)o).getSource() && mTarget == ((Link)o).getTarget()
			&& ((mExtras != null && mExtras.equals(((Link)o).mExtras)) || (mExtras == null && ((Link)o).mExtras == null));
		}
		return false;
	}
	
	public int hashCode()
	{
		return (mSource != null?mSource.hashCode():0)
		        +(mTarget != null?mTarget.hashCode():0);
	}
	
	/*public void visit(ModelVisitor visitor)
	{
	    visitor.visitLink(this);
	}*/
}
