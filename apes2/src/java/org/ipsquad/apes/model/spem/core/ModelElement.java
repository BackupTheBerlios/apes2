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


package org.ipsquad.apes.model.spem.core;

import java.util.Vector;

import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;

/**
 * Base class for the elements of the model
 *
 * @version $Revision: 1.1 $
 */
public abstract class ModelElement extends Element
{
	private IPackage mParent;
	private Vector mBehavior = new Vector();
	
	//private Vector mPresentation = new Vector();

	public ModelElement(String name)
	{
		super(name);
	}

	public ModelElement()
	{
		
	}

	/**
	 * Add a state machine to the model element
	 *
	 * @param sm the state machine to associate
	 * @return true if the element can be added, false otherwise
	 */
	public boolean addBehavior( StateMachine sm )
	{
		if( !mBehavior.contains(sm) )
		{
			mBehavior.add(sm);
			sm.setContext(this);
			return true;
		}
		return false;
	}
	
	/**
	 * Remove a state machine from the model element
	 *
	 * @param sm the state machine to remove
	 * @return true if the element can be removed, false otherwise
	 */
	public boolean removeBehavior( StateMachine sm )
	{
		if( mBehavior.contains(sm) )
		{
			mBehavior.remove(sm);
			sm.setContext(null);
			return true;
		}
		return false;
	}
	
	/**
	 * Check if a state machine is associated to the model element
	 *
	 * @param sm the state machine to test
	 * @return true if the element is associated, false otherwise
	 */
	public boolean containsBehavior(StateMachine sm)
	 {
		return mBehavior.contains(sm);
	 }



	/**
	 * Get a state machine by giving its index
	 *
	 * @param i the index of the state machine to retrieve
	 * @return the state machine or null if the index is invalid
	 */
	public StateMachine getBehavior(int i)
	 {
		if(i<0 || i>=mBehavior.size())
		{
			return null;
		}
	 
		return (StateMachine) mBehavior.get(i);
	 }
	
	/**
	 * Get the number of state machines associated to the model element
	 *
	 * @return the number of state machines
	 */
	public int behaviorCount()
	 {
		return mBehavior.size();
	 }
	
	/**
	 * Add a presentation element to the model element
	 *
	 * @param e the presentation element to associate
	 * @return true if the element can be added, false otherwise
	 */
	/*public boolean addPresentation(PresentationElement e)
	{
		if(!containsPresentation(e) && e.getSubject()==null)
		{
			mPresentation.add(e);
			e.setSubject(this);
			return true;
		}

		return false;
	}*/



	/**
	 * Remove a presentation element from the model element
	 *
	 * @param e the presentation element to remove
	 * @return true if the element can be removed, false otherwise
	 */
	/*public boolean removePresentation(PresentationElement e)
	{
		if(containsPresentation(e))
		{
			mPresentation.remove(e);
			e.setSubject(null);
			return true;
		}

		return false;
	}*/

	
	
	/**
	 * Check if a presentation element is associated to the model element
	 *
	 * @param e the presentation element to test
	 * @return true if the element is associated, false otherwise
	 */
	/*public boolean containsPresentation(PresentationElement e)
	{
		return mPresentation.contains(e);
	}*/



	/**
	 * Get a presentation element by giving its index
	 *
	 * @param i the index of the presentation element to retrieve
	 * @return the presentation element or null if the index is invalid
	 */
	/*public PresentationElement getPresentation(int i)
	{
		if(i<0 || i>=mPresentation.size())
		{
			return null;
		}
		
		return (PresentationElement) mPresentation.get(i);
	}*/



	/**
	 * Get the number of presentation elements associated to the model element
	 *
	 * @return the number of presentation elements
	 */
	/*public int presentationCount()
	{
		return mPresentation.size();
	}*/



	/**
	 * Set the containing package of the model element
	 *
	 * @param parent the package
	 */
	public void setParent(IPackage parent)
	{
		mParent = parent;
	}
	
	/**
	 * Get the containing package of the model element
	 *
	 * @return the package containing this model element
	 */
	public IPackage getParent()
	{
		return mParent;
	}
	
	public Object clone()
	{
		ModelElement me = (ModelElement) super.clone();
		me.mBehavior=new Vector();
		//me.mPresentation = (Vector) mPresentation.clone();
		return me;
	}
}
