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

/**
 * Elements used to document model elements
 *
 * @version $Revision: 1.2 $
 */
public abstract class PresentationElement extends Element
{
	private ModelElement mSubject;
	
	public PresentationElement(String name)
	{
		super(name);
	}
	
	public PresentationElement()
	{
		
	}
	
	/**
	 * Get the subject of this presentation element
	 *
	 * @return the documented model element
	 */
	public ModelElement getSubject()
	{
		return mSubject;
	}
	

	/**
	 * Set the subject of this presentation element
	 *
	 * @param subject the model element to document
	 */
	public void setSubject(ModelElement subject)
	{
		mSubject = subject;
	}
	
	public Object clone()
	{
		PresentationElement pe = (PresentationElement)super.clone();
		
		pe.mSubject = null;
		
		return pe;
	}
}
