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

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;


/**
 * Encapsulates information describing inserting to the model, and is used to notify ApesMediator listeners of the change.
 *  
 * @version $Revision: 1.1 $ 
 */
public class InsertEvent extends Event
{
	private SpemDiagram mDiagram = null;
	private Object mInserted = null;
	private Object mSource = null;
	private Object mTarget = null;
	private IPackage mParent = null;
	private boolean mIsAlreadyExistInModel = false;
	
	public String toString()
	{
		return "Inserted "+mInserted+" Parent "+mParent+" isAlreadyExist "+mIsAlreadyExistInModel
		+" diagram "+mDiagram;
	}
		
	protected InsertEvent( SpemDiagram diagram, Object inserted, IPackage parent, boolean isAlreadyExist, Map attr )
	{
		super(attr);
		mDiagram = diagram;
		mInserted = inserted;
		if( inserted instanceof ActivityDiagram.Transition )
		{
			mSource = ((ActivityDiagram.Transition)inserted).getInputModelElement();
			mTarget = ((ActivityDiagram.Transition)inserted).getOutputModelElement();
		}
		mParent = parent;
		mIsAlreadyExistInModel = isAlreadyExist;
	}
	
	protected InsertEvent(  SpemDiagram diagram, Map linkInserted, Map attr )
	{
		super(attr);
		if( linkInserted != null )
		{
			Map.Entry entry = (Map.Entry)((Map)linkInserted).entrySet().iterator().next();
			mSource = entry.getKey();
			mTarget = entry.getValue();
		}
		mIsAlreadyExistInModel = true;
		mDiagram = diagram;
	}
	
	protected InsertEvent( Object inserted, IPackage parent, Map attr )
	{
		super(attr);
		mInserted = inserted;
		mParent = parent;
	}
	/**
	 * 
	 * @return the diagram where the element was inserted, null otherwise
	 */
	public SpemDiagram getDiagram() { return mDiagram; }
	/**
	 * 
	 * @return the inserted element
	 */
	public Object getInserted() { return mInserted; }
	public Object getSource() { return mSource; }
	public Object getTarget() { return mTarget; }
	/**
	 * 
	 * @return the parent of the inserted element
	 */
	public IPackage getParent() { return mParent; }
	/**
	 * 
	 * @return true if the element was only inserted in a diagram, false otherwise
	 */
	public boolean isAlreadyExistInModel() { return mIsAlreadyExistInModel; }
}