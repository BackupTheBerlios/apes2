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


package org.ipsquad.apes.model.spem.modelmanagement;

import java.util.Vector;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;

/**
 * Package of the model
 *
 * @version $Revision: 1.1 $
 */
public class SPackage extends ModelElement implements IPackage
{
	private Vector mOwnedElement = new Vector();

	public SPackage(String name)
	{
		super(name);
	}

	public SPackage()
	{

	}

	public void visit(SpemVisitor visitor)
	{
		visitor.visitPackage(this);
	}

	
	/**
	 * Add a model element to this package
	 *
	 * @param e the model element to add
	 * @return true if the model element can be added, false otherwise
	 */
	public boolean addModelElement(ModelElement e)
	{
		if(!containsModelElement(e) && e.getParent()==null)
		{
			mOwnedElement.add(e);
			e.setParent(this);
			return true;
		}

		return false;
	}



	/**
	 * Remove a model element from this package
	 *
	 * @param e the model element to remove
	 * @return true if the model element can be removed, false otherwise
	 */
	public boolean removeModelElement(ModelElement e)
	{
		if(containsModelElement(e))
		{
			mOwnedElement.remove(e);
			e.setParent(null);
			return true;
		}

		return false;
	}


	/**
	 * Check if a model element is in this package
	 *
	 * @param e the model element to test
	 * @return true if the model element is in this package, false otherwise
	 */
	public boolean containsModelElement(ModelElement e)
	{
		return mOwnedElement.contains(e);
	}


	/**
	 * Get a model element by giving its index
	 *
	 * @param i the model element index
	 * @return the model element or null if the index is invalid
	 */
	public ModelElement getModelElement(int i)
	{
		if(i<0 || i>=mOwnedElement.size())
		{
			return null;
		}
		
		return (ModelElement) mOwnedElement.get(i);
	}


	/**
	 * Get the number of model elements in this package
	 *
	 * @return the number of model elements contained in this package
	 */
	public int modelElementCount()
	{
		return mOwnedElement.size();
	}

	
	public Object clone()
	{
		SPackage p = (SPackage)super.clone();
		
		p.mOwnedElement = new Vector();
		
		return p;
	}
}
