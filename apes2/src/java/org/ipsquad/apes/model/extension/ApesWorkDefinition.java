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

import java.util.Collection;
import java.util.Iterator;

import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;

/**
 * WorkDefinition which contains an activity diagram and a flow diagram 
 *
 * @version $Revision: 1.7 $
 */
public class ApesWorkDefinition extends WorkDefinition implements IPackage
{
	private FlowDiagram mFlowDiagram = null;
	private ActivityDiagram mActivityDiagram = null;

	public ApesWorkDefinition()
	{
		super();
	}

	public ApesWorkDefinition( String name )
	{
		super( name );
	}

	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitWorkDefinition(this);
	}
	
	public boolean addSubWork(Activity a)
	{
		if( super.addSubWork(a) )
		{
			a.setParent( this );
			return true;
		}
		return false;
	}

	public boolean removeSubWork(Activity a)
	{
		if( super.removeSubWork(a) )
		{
			a.setParent( null );
			return true;
		}
		return false;
	}

	public boolean canAddFlowDiagram()
	{
		return mFlowDiagram == null;
	}
	
	public boolean addFlowDiagram( FlowDiagram diagram )
	{
		if( canAddFlowDiagram() )
		{
			mFlowDiagram = diagram;
			mFlowDiagram.setParent( this );
			return true;
		}
		return false;
	}

	public boolean removeFlowDiagram()
	{
		if( mFlowDiagram != null )
		{
			mFlowDiagram.setParent( null );
			mFlowDiagram = null;
			return true;
		}
		return false;
	}
	
	public boolean canAddActivityDiagram()
	{
		return mActivityDiagram == null;
	}

	public boolean addActivityDiagram( ActivityDiagram diagram )
	{
		if( canAddActivityDiagram() )
		{
			mActivityDiagram = diagram;
			mActivityDiagram.setParent( this );
			return true;
		}
		return false;
	}
	
	public boolean removeActivityDiagram()
	{
		if( mActivityDiagram != null )
		{
			mActivityDiagram.setParent( null );
			mActivityDiagram = null;
			return true;
		}
		return false;
	}

	public ActivityDiagram getActivityDiagram()
	{
		return mActivityDiagram;
	}
	
	public FlowDiagram getFlowDiagram()
	{
		return mFlowDiagram;
	}

	public boolean addModelElement(ModelElement e) 
	{
		if( e instanceof Activity )
		{
			Activity a = (Activity)e;
			if(addSubWork(a))
			{
				if(a.getOwner() == null && getOwner() != null)
				{
					if(getOwner() != null && !getOwner().containsFeature(a))
					{
						getOwner().addFeature(a);
					}
					a.setOwner(getOwner());
				}
				return true;
			}
			return false;
		}
		if( e instanceof FlowDiagram )
		{
			return addFlowDiagram( (FlowDiagram) e ); 
		}
		if( e instanceof ActivityDiagram )
		{
			return addActivityDiagram( (ActivityDiagram) e );
		}
		
		return false;
	}

	public boolean removeModelElement(ModelElement e)
	{
		if( e instanceof Activity )
		{
			Activity a = (Activity)e;
			Collection links = ApesMediator.getInstance().getLinks(mFlowDiagram, getOwner());
			if(getOwner() != null && !contains(links, a))
			{
				if(getOwner() != null && getOwner().containsFeature(a))
				{
					getOwner().removeFeature(a);
				}
				a.setOwner(null);
			}
			return removeSubWork( (Activity) e );
		}
		if( e instanceof FlowDiagram && mFlowDiagram == (FlowDiagram) e )
		{
			return removeFlowDiagram( ); 
		}
		if( e instanceof ActivityDiagram && mActivityDiagram == (ActivityDiagram) e )
		{
			return removeActivityDiagram( );
		}
		
		return false;
	}

	public boolean containsModelElement(ModelElement e) 
	{
		if( e instanceof Activity )
		{
			return containsSubWork( (Activity) e );
		}
		if( e instanceof FlowDiagram )
		{
			return mFlowDiagram == (FlowDiagram) e;
		}
		if( e instanceof ActivityDiagram )
		{
			return mActivityDiagram == (ActivityDiagram) e;
		}
		return false;
	}

	public ModelElement getModelElement(int i)
	{
		ModelElement e = getSubWork( i );
		
		if( e != null )
		{
			return e;
		}

		if( subWorkCount() == i )
		{
			if( mFlowDiagram != null )
			{
				return mFlowDiagram;
			}
			
			return mActivityDiagram;
		}

		if( subWorkCount()+1 == i )
		{
			return mActivityDiagram;
		}

		return null;
	}

	public int modelElementCount() 
	{
		int count = subWorkCount();

		if( mFlowDiagram != null )
		{
			count++;
		}
		
		if( mActivityDiagram != null )
		{
			count++;
		}

		return count;
	}
	
	public boolean setOwner(ProcessPerformer owner)
	{
		ProcessPerformer oldOwner = getOwner();
		if(oldOwner != owner && super.setOwner(owner))
		{
			if(owner != null && !owner.containsFeature(this))
				owner.addFeature(this);
			
			Collection links = ApesMediator.getInstance().getLinks(mFlowDiagram, oldOwner);
			for(int i = 0; i < subWorkCount(); i++)
			{
				WorkDefinition wd = getSubWork(i);	
				if(wd.getOwner() == oldOwner && !contains(links, wd))
				{
					if(owner != null && !owner.containsFeature(wd))
						owner.addFeature(wd);
					wd.setOwner(owner);
				}
			}
			return true;
		}
		return false;
	}
	
	protected boolean contains(Collection links, Object o)
	{
		for ( Iterator iter = links.iterator(); iter.hasNext(); )
		{
			Link link = (Link) iter.next();
			if(link.getSource() == o || link.getTarget() == o)
			{
				return true;
			}
		}
		return false;
	}
	
	public Object clone()
	{
		ApesWorkDefinition aw = (ApesWorkDefinition)super.clone();
		aw.mFlowDiagram = null;
		aw.mActivityDiagram = null;
		
		return aw;
	}
}
