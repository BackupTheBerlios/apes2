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


package org.ipsquad.apes.processing;

import org.ipsquad.apes.model.spem.DefaultSpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;

/**
 *
 * @version $Revision: 1.1 $
 */
public class ModelElementCountVisitor extends DefaultSpemVisitor implements RoutedSpemVisitor
{
	private int modelElementCount;	// count all the elements

	public ModelElementCountVisitor()
	{
		modelElementCount=0;
	}


	/**
	 * Called when the visited element is a model element
	 *
	 * @param e the visited model element
	 */
	public void visitModelElement(ModelElement e){ modelElementCount++; }

	
	/**
	 * Get the total of model elements
	 *
	 * @return the total of model elements
	 */
	public int getModelElementCount()
	{		
		return modelElementCount;
	}

	public void routingBegin() { }

	public void routingEnd() { }

};
