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


package org.ipsquad.apes.model.spem.process.structure;

import java.util.Vector;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.utils.Debug;

/**
 * @version $Revision: 1.5 $
 */
public class Activity extends WorkDefinition
{
	private Vector mInput = new Vector();
	private Vector mOutput = new Vector();
	private Vector mAssistant = new Vector();
	
	public Activity()
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++Activity");
	}
	
	public Activity(String name)
	{
		super(name);
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++Activity::"+name);
	}
	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitActivity(this);
	}
	
	
	
	/**
	 * Add a work product in input of the activity
	 *
	 * @param w the Work Product to associate
	 * @return true if the Work Product can be added, false otherwise
	 */
	public boolean addInputWorkProduct(WorkProduct w)
	{
		if(!containsInputWorkProduct(w))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> Activity("+getName()+")::addInputWorkProduct "+w);
			mInput.add(w);
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * Remove a Work Product in input of this activity 
	 *
	 * @param w the Work Product to remove
	 * @return true if the Work Product can be removed, false otherwise
	 */
	public boolean removeInputWorkProduct(WorkProduct w)
	{
		if(containsInputWorkProduct(w))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> Activity("+getName()+")::removeInputWorkProduct "+w);
			mInput.remove(w);
			return true;
		}
		return false;
	}
	
	
	
	/**
	 * Check if a Work Product is in input of this activity
	 *
	 * @param w the Work Product to test
	 * @return true if the Work Product is in input of this activity, false otherwise
	 */
	public boolean containsInputWorkProduct(WorkProduct w)
	{
		return mInput.contains(w);
	}
			
	
	
	/**
	 * Add a work product in output of the activity
	 *
	 * @param w the Work Product to associate
	 * @return true if the Work Product can be added, false otherwise
	 */
	public boolean addOutputWorkProduct(WorkProduct w)
	{
		if(!containsOutputWorkProduct(w))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> Activity("+getName()+")::addOutputWorkProduct "+w);
			mOutput.add(w);
			return true;
		}
		
		return false;
	}
	
	
	
	/**
	 * Remove a Work Product in output of this activity 
	 *
	 * @param w the Work Product to remove
	 * @return true if the Work Product can be removed, false otherwise
	 */
	public boolean removeOutputWorkProduct(WorkProduct w)
	{
		if(containsOutputWorkProduct(w))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> Activity("+getName()+")::removeOutputWorkProduct "+w);
			mOutput.remove(w);
			return true;
		}
		return false;
	}

	
	
	/**
	 * Check if a Work Product is in outut of this activity
	 *
	 * @param w the Work Product to test
	 * @return true if the Work Product is in outut of this activity, false otherwise
	 */
	public boolean containsOutputWorkProduct(WorkProduct w)
	{
		return mOutput.contains(w);
	}
	
	public Object clone()
	{
		Activity a = (Activity)super.clone();
		
		a.mInput = new Vector();
		a.mOutput = new Vector();
		a.mAssistant = new Vector();
		
		return a;
	}

	public int getInputCount()
	{
		return mInput.size();
	}
	
	public int getOutputCount()
	{
		return mOutput.size();
	}

	public WorkProduct getInput(int i)
	{
		return (WorkProduct)mInput.get(i);
	}
	
	public WorkProduct getOutput(int i)
	{
		return (WorkProduct)mOutput.get(i);
	}
	
	/**
	 * Adds an assistant
	 *
	 * @param r the assistant
	 * @return true if the assistant can be added, false otherwise
	 */
	public boolean addAssistant(ProcessRole r)
	{
		if(!containsAssistant(r))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> Activity("+getName()+")::addAssistant "+r);
			mAssistant.add(r);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Removes an assistant
	 *
	 * @param r the assistant
	 * @return true if the assistant can be removed, false otherwise
	 */
	public boolean removeAssistant(ProcessRole r)
	{
		if(containsAssistant(r))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> Acivity("+getName()+")::removeAssistant "+r);
			mAssistant.remove(r);
			return true;
		}

		return false;
	}
	
	/**
	 * Check if a role is an assistant of this activity
	 *
	 * @param r the role to test
	 * @return true if the role is an assistant of this activity, false otherwise
	 */
	public boolean containsAssistant(ProcessRole r)
	{
		return mAssistant.contains(r);
	}
	
	public int getAssistantCount()
	{
		return mAssistant.size();
	}

	public ProcessRole getAssistant(int i)
	{
		return (ProcessRole)mAssistant.get(i);
	}
	
	public void updateToCurrentVersion()
	{
		super.updateToCurrentVersion();
		
		if(mAssistant == null)
		{
			mAssistant = new Vector();
		}
	}
};
