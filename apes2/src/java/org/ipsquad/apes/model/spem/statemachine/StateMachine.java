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
package org.ipsquad.apes.model.spem.statemachine;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;

/**
 * An incomplete implementation of the StateMachine 
 *
 * @version $Revision: 1.6 $
 */
public class StateMachine extends ModelElement
{
	private ModelElement mContext = null;
	
	//private State mTop;
	//private Vector mTransitions;
	
	public StateMachine( )
	{
		super();
	}
	
	public StateMachine(String name)
	{
		super(name);
	}
	
	public void setContext( ModelElement context )
	{
		mContext = context;
	}

	public ModelElement getContext()
	{
		return mContext;
	}
	
	/*public String getName()
	{
		//return '('+super.getName()+')';
		return super.getName();
	}*/
	
	public String toString()
	{
		return getName();
	}
	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitStateMachine(this);
	}
	
	public Object clone()
	{
		StateMachine sm = (StateMachine)super.clone();
		sm.mContext = null;
		return sm;
	}
}
