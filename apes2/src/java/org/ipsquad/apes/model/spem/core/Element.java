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


package org.ipsquad.apes.model.spem.core;

import java.io.Serializable;

import org.ipsquad.apes.Identity;
import org.ipsquad.apes.model.spem.SpemVisitor;

/**
 * Base class for all the SPEM elements
 *
 * Specify an abstract method to allow visitors
 * See the visitor design pattern for more informations
 *
 * @version $Revision: 1.6 $
 */
public abstract class Element implements Serializable, Cloneable, Identity
{
	private static int msNoNameCounter = 0;
	private static int mNoID = 0;

	private String mName;
	private int mID;
	
	/**
	 * Create a new element
	 *
	 * @param name the new element name
	 */
	public Element(String name)
	{
		mName = name;
		mID=mNoID++;
	}


	/**
	 * Create a new element with a default name
	 */
	public Element()
	{
		this("noname"+msNoNameCounter++);
		mID=mNoID++;
	}

	public void resetName()
	{
		setName("noname"+msNoNameCounter++);
	}

	/**
	 * Allow the elements to be visited
	 *
	 * @param visitor the visitor which need an access
	 */
	public abstract void visit(SpemVisitor visitor);

	
	/**
	 * Get the name of the element
	 *
	 * @return the element name
	 */
	public String getName()
	{
		return mName;
	}


	/**
	 * Set the name of the element
	 *
	 * @param name the element name
	 */
	public void setName(String name)
	{
		mName = name;
	}

	/**
	 * Get the unique identifier of this element
	 *
	 * @return the ID of the element
	 */
	public int getID()
	{
		return mID;
	}

	private void setID()
	{
		mID=mNoID++;
	}

	/**
	 * Gives a string representation of the element
	 *
	 * @return string representation
	 */
	public String toString()
	{
		return mName;
	}

	/**
	 * Returns a copy of the object.
	 * Only the name is altered, the clone will have a name of the type 'noname*' 
	 *
	 * @return the copy of the element
	 */
	public Object clone()
	{
		Element e = null;
		try
		{
			e = (Element) super.clone();
		}
		catch(CloneNotSupportedException ex)
		{
			ex.printStackTrace();
		}
		
		e.setName("noname"+msNoNameCounter++);
		e.setID();
		return e;
	}
	
	/**
	 * Warning : this method is only use by other software like iepp
	 *
	 */
	public static void resetID()
	{
		mNoID = 0;
	}
	
	/**
	 * Warning : this method must only be used by the load project process
	 *
	 */
	public static void setNoNameCounter(int counter)
	{
		msNoNameCounter = counter;
	}

	/**
	 * Warning : this method must only be used by the load project process
	 *
	 */
	public static void setNoID(int counter)
	{
		mNoID = counter;
	}
	
	/**
	 * Warning : this method should only be used by the load project process
	 *
	 * Used when an older version of a component is loaded.
	 * This allowed to initialize new attributes which are not present 
	 * in the older version.
	 * 
	 * This allows to add new attributes in the model layer without breaking 
	 * compatibility with older versions.
	 */
	public void updateToCurrentVersion() { }
};
