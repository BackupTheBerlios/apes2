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
package apes.model.spem.core;

import junit.framework.TestCase;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;

/**
 * 
 * @version $Revision: 1.2 $
 */
public class TestModelElement extends TestCase
{
	private StateMachine createStateMachine()
	{
		return new StateMachine();
	}
	
	private ModelElement createModelElement()
	{
		return new ModelElement()
		{
			public void visit(SpemVisitor visitor) {}
		};
	}
	
	public void testAddModelElement()
	{
		ModelElement me = createModelElement();
		StateMachine sm = createStateMachine();
		
		assertTrue(me.behaviorCount()==0);
		assertTrue(sm.getParent()==null);
		
		me.addBehavior(sm);
		
		assertTrue(me.behaviorCount()==1);
		assertTrue(sm.getContext()==me);
		
		me.addBehavior(sm);
		assertTrue(me.behaviorCount()==1);
		assertTrue(sm.getContext()==me);
		
		for(int i=1; i<100; i++)
		{
			sm = createStateMachine();
			assertTrue(me.behaviorCount()==i);
			me.addBehavior(sm);
		}
		
	}

	public void testRemoveModelElement()
	{
		StateMachine sm1 = createStateMachine();
		StateMachine sm2 = createStateMachine();
		ModelElement me = createModelElement();
		
		me.addBehavior(sm1);
		assertTrue(me.behaviorCount()==1);
		assertTrue(sm1.getContext()==me);
		
		me.removeBehavior(sm2);
		assertTrue(me.behaviorCount()==1);
		
		me.removeBehavior(sm1);
		assertTrue(me.behaviorCount()==0);
		assertTrue(sm1.getContext()==null);
		
		for(int i=0; i<100; i++)
		{
			sm1 = createStateMachine();
			me.addBehavior(sm1);
		}
		
		me.addBehavior(sm2);
		
		for(int i=0; i<100; i++)
		{
			sm1 = createStateMachine();
			me.addBehavior(sm1);
		}
		
		assertTrue(me.behaviorCount()==201);
		me.removeBehavior(sm2);
		assertTrue(me.behaviorCount()==200);
	}
	
	public void testContainsModelElement()
	{
		StateMachine sm1;
		StateMachine sm2 = createStateMachine();
		ModelElement me = createModelElement();
		
		for(int i=0; i<100; i++)
		{
			sm1 = createStateMachine();
			me.addBehavior(sm1);
		}
		
		assertFalse(me.containsBehavior(sm2));
		me.addBehavior(sm2);
		assertTrue(me.containsBehavior(sm2));
		
		for(int i=0; i<100; i++)
		{
			sm1 = createStateMachine();
			me.addBehavior(sm1);
		}
		
		assertTrue(me.containsBehavior(sm2));
	}

	public void testGetModelElement()
	{
		StateMachine sm1;
		StateMachine sm2 = createStateMachine();
		ModelElement me = createModelElement();
		
		for(int i=0; i<100; i++)
		{
			sm1 = createStateMachine();
			me.addBehavior(sm1);
		}
		
		me.addBehavior(sm2);
		
		for(int i=0; i<100; i++)
		{
			sm1 = createStateMachine();
			me.addBehavior(sm1);
		}
		
		assertEquals(me.getBehavior(100), sm2);
		assertEquals(me.getBehavior(-1), null);
		assertEquals(me.getBehavior(201), null);
	}
}
