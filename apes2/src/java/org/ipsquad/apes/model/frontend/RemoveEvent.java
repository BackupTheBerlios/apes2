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
package org.ipsquad.apes.model.frontend;

import java.util.Map;

import org.ipsquad.apes.model.extension.SpemDiagram;


/**
 * Encapsulates information describing removing from the model, and is used to notify ApesMediator listeners of the change. 
 * 
 * @version $Revision: 1.2 $
 */
public class RemoveEvent extends Event
{
	private SpemDiagram mDiagram = null;
	private Object[] mElements;
	private Object[] mSources, mTargets;
	private Map mParents = null;
	private Map mIndices = null;
	
	public String toString()
	{
		String result="elements ";
		for( int i=0; i<mElements.length; i++)
			result+=mElements[i]+" ";
		result+=" Diagram "+mDiagram+" parent "+mParents+" indices "+mIndices;
		return result;
	}
	
	protected RemoveEvent( Object[] elements, Object[] sources, Object[] targets, Map parents, Map indices, Map attr )  
	{
		super(attr);
		mElements = elements;
		mSources = sources;
		mTargets = targets;
		mParents = parents;
		mIndices = indices;
	}
	
	protected RemoveEvent( SpemDiagram diagram, Object[] elements, Object[] sources, Object[] targets, Map attr )  
	{
		super(attr);
		mDiagram = diagram;
		mElements = elements;
		mSources = sources;
		mTargets = targets;
	}
	/**
	 * 
	 * @return the diagram where the elements was removed, null otherwise 
	 */
	public SpemDiagram getDiagram() { return mDiagram; }
	/**
	 * 
	 * @return the removed elements
	 */
	public Object[] getElements() { return mElements; }
	/**
	 * 
	 * @return the removed links
	 */
	public Object[] getSources() { return mSources; }
	public Object[] getTargets() { return mTargets; }
	/**
	 * 
	 * @return a map associating element removed with its old index in the model
	 */
	public Map getIndices() { return mIndices; }
	/**
	 * 
	 * @return
	 */
	public Map getParents() { return mParents; }
}
