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


package org.ipsquad.apes.model.spem.process.structure;

import java.util.Vector;

import org.ipsquad.apes.model.spem.SpemVisitor;

/**
 * @version $Revision: 1.1 $
 */
public class Activity extends WorkDefinition
{
	private Vector mInput = new Vector();
	private Vector mOutput = new Vector();
	
	public Activity()
	{
	
	}
	
	public Activity(String name)
	{
		super(name);
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
};
