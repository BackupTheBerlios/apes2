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
import org.ipsquad.apes.model.spem.core.ModelElement;

/**
 * @version $Revision: 1.4 $
 */
public class WorkDefinition extends ModelElement
{
	private Vector mSubWork = new Vector();
	private Vector mParentWork = new Vector();
	private ProcessPerformer mOwner;
	
	
	public WorkDefinition()
	{
	
	}
	
	public WorkDefinition(String name)
	{
		super(name);
	}
	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitWorkDefinition(this);
	}	
	
	public ProcessPerformer getOwner()
	{
		return mOwner;
	}
	
	public boolean setOwner(ProcessPerformer owner)
	{
		if(owner==null)
		{
			mOwner = owner;
			return true;
		}
		
		if(mOwner==null)
		{
			mOwner = owner;
			return true;
		}
		
		return false;
	}

	public boolean addSubWork( Activity a )
	{
		if( ! mSubWork.contains( a ) )
		{
			mSubWork.add( a );
			a.addParentWork( this );
			return true;
		}
		return false;
	}
	
	public boolean removeSubWork( Activity a )
	{
		if( mSubWork.remove( a ) )
		{
			mSubWork.remove( a );
			a.removeParentWork( this );
			
			return true;
		}
		
		return false;
	}
	
	public boolean containsSubWork( Activity a )
	{
		return mSubWork.contains( a );
	}
	
	public Activity getSubWork( int i )
	{
		if( i < 0 || i >= mSubWork.size() )
		{
			return null;
		}
		
		return (Activity) mSubWork.get( i );
	}
	
	public int subWorkCount()
	{
		return mSubWork.size();
	}
	
	public boolean addParentWork( WorkDefinition w )
	{
		if( ! mParentWork.contains( w ) )
		{
			mParentWork.add( w );
			return true;
		}
		return false;
	}
	
	public boolean removeParentWork( WorkDefinition w )
	{
		return mParentWork.remove( w );
	}
	
	public boolean containsParentWork( WorkDefinition w )
	{
		return mParentWork.contains( w );
	}
	
	public WorkDefinition getParentWork( int i )
	{
		if( i < 0 || i >= mParentWork.size() )
		{
			return null;
		}
		
		return (WorkDefinition) mParentWork.get( i );
	}
	
	public int parentWorkCount()
	{
		return mParentWork.size();
	}

	public Object clone()
	{
		WorkDefinition wd = (WorkDefinition)super.clone();
		
		wd.mSubWork = new Vector();
		wd.mParentWork = new Vector();
		wd.mOwner = null;
		
		return wd;
	}
	
}
