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

import java.util.Map;


/**
 * Encapsulates information describing moving into the model, and is used to notify ApesMediator listeners of the change. 
 * 
 *  @version $Revision: 1.1 $
 */
public class MoveEvent extends Event
{
	private Object mElement;
	private Object mOldParent;
	private int mOldIndex;
	private Object mNewParent;
	private int mNewIndex;
	
	public String toString()
	{
		return "Element "+mElement+" OldParent "+mOldParent+" OldIndex "+mOldIndex
		+" NewParent "+mNewParent+" NewIndex "+mNewIndex;
	}
	
	protected MoveEvent( Object element, Object oldParent, int oldIndex, Object newParent, int newIndex, Map attr ) 
	{
		super( attr );
		mElement = element;
		mOldParent = oldParent;
		mOldIndex = oldIndex;
		mNewParent = newParent;
		mNewIndex = newIndex;
	}
	
	public Object getElement() { return mElement; }
	public int getNewIndex() { return mNewIndex; }
	public Object getNewParent() { return mNewParent; }
	public int getOldIndex() { return mOldIndex; }
	public Object getOldParent() { return mOldParent; }
}