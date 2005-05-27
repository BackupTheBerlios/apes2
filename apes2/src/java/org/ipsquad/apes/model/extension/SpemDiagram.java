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

import org.ipsquad.apes.model.spem.core.ModelElement;

/**
 * Base class for diagrams
 *
 * @version $Revision: 1.7 $
 */
public abstract class SpemDiagram extends ModelElement
{

	public SpemDiagram()
	{

	}

	public SpemDiagram(String name)
	{
		super(name);
	}

	public abstract boolean addModelElement(ModelElement me);

	public abstract boolean removeModelElement(ModelElement e);
	
	public abstract boolean canAddModelElement(ModelElement me);

	public abstract boolean containsModelElement(ModelElement e);

	public abstract ModelElement getModelElement(int i);

	public abstract int modelElementCount();

	/**
	 * Creates a link beetween source and target.
	 *  
	 * @param source 
	 * @param target 
	 * @param extras can be used to specify extra informations 
	 * 	such as the type of the link to create 
	 * 	when there are multiple choices
	 * @return true if the link is created, false otherwise
	 */
	public abstract boolean createLinkModelElements(ModelElement source, ModelElement target, Object extras);

	/**
	 * Removes a link beetween source and target.
	 *  
	 * @param source 
	 * @param target 
	 * @param extras can be used to specify extra informations
	 * 	such as the type of the link to remove
	 * 	when there are multiple choices
	 * @return true if the link is created, false otherwise
	 */
	public abstract boolean removeLinkModelElements(ModelElement source, ModelElement target, Object extras);

	/**
	 * Tests if source and target can be linked
	 * 
	 * @param source 
	 * @param target
	 * @param extras can be used to specify extra informations 
	 * 	such as the type of the link to create 
	 * 	when there are multiple choices
	 * @return true if source and target can be linked
	 */
	public abstract boolean areLinkableModelElements(ModelElement source, ModelElement target, Object extras);

	/**
	 * Tests if source and target are linked
	 * 
	 * @param source 
	 * @param target
	 * @param extras can be used to specify extra informations
	 * 	such as the type of the link to search 
	 * 	when there are multiple choices
	 * @return true if source and target are linked
	 */
	public abstract boolean existsLinkModelElements(ModelElement source, ModelElement target, Object extras);
}
