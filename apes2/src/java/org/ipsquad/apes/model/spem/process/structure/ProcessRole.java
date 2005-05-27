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
public class ProcessRole extends ProcessPerformer
{
	private Vector mParticipation = new Vector();
	
	public ProcessRole()
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++ProcessRole");
	}

	public ProcessRole(String name)
	{
		super(name);
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++ProcessRole::"+name);
	}

	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitRole(this);
	}
	
	/**
	 * Adds an activity where this role takes part
	 *
	 * @param a the activity
	 * @return true if the activity can be added, false otherwise
	 */
	public boolean addParticipation(Activity a)
	{
		if(!containsParticipation(a))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ProcessPerformer("+getName()+")::addParticipation "+a);
			mParticipation.add(a);
			return true;
		}
		return false;
	}
	
	
	/**
	 * Removes an activity where this role takes part
	 *
	 * @param a an activity
	 * @return true if the activity can be removed, false otherwise
	 */
	public boolean removeParticipation(Activity a)
	{
		if(containsParticipation(a))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ProcessPerformer("+getName()+")::removeParticipation "+a);
			mParticipation.remove(a);
			return true;
		}

		return false;
	}
	
	/**
	 * Check if an activity is a participation of this ProcessRole
	 *
	 * @param a the Activity to test
	 * @return true if the Activity is a participation of this ProcessRole, false otherwise
	 */
	public boolean containsParticipation(Activity a)
	{
		return mParticipation.contains(a);
	}
	
	public Object clone()
	{
		ProcessRole r = (ProcessRole)super.clone();
		
		r.mParticipation = new Vector();
		
		return r;
	}

	public int getParticipationCount()
	{
		return mParticipation.size();
	}

	public Activity getParticipation(int i)
	{
		return (Activity)mParticipation.get(i);
	}
	
	public boolean equals(Object o)
	{
		if( o instanceof ProcessRole )
		{	
			ProcessRole r = (ProcessRole)o;
			return mParticipation.equals(r.mParticipation)
				&& super.equals(o);
		}
		return false;
	}
	
	public void updateToCurrentVersion()
	{
		super.updateToCurrentVersion();
		
		if(mParticipation == null)
		{
			mParticipation = new Vector();
		}
	}
}
