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
package org.ipsquad.apes.model.frontend;

import java.io.Serializable;
import java.util.Map;

/**
 * Encapsulates information describing an update into the model, and is used to notify ApesMediator listeners of the change. 
 * 
 * @version $Revision: 1.1 $
 */
public abstract class Event implements Serializable
{
	private Map mAttributes;
	
	protected Event( Map attr )
	{
		mAttributes = attr;
	}
	
	/**
	 * 
	 * @return a map containing informations passing by the object which calls the ApesMediator.
	 *  
	 */
	public Map getAttributes() { return mAttributes; }
}