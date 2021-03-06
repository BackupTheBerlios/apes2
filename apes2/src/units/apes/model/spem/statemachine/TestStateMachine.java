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

package apes.model.spem.statemachine;

import junit.framework.TestCase;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;


public class TestStateMachine extends TestCase
{
	private StateMachine createStateMachine()
	{
		return new StateMachine()
		{
			public void visit(SpemVisitor visitor) {}
		};	
	}
	
	private StateMachine createStateMachine(String name)
	{
		return new StateMachine(name)
		{
			public void visit(SpemVisitor visitor) {}
		};	
	}
	
	private ModelElement createModelElement()
	{
		return new ModelElement()
		{
			public void visit(SpemVisitor visitor) {}
		};	
	}
	
	public void testWithName()
	{
		StateMachine sm = createStateMachine("testWithName");
		
		assertEquals("testWithName", sm.getName());
	}
	
	public void testNoName()
	{
		StateMachine sm = createStateMachine();
		String name = sm.getName();
		
		assertEquals("noname", name.substring(0,6));
		
		int init = Integer.parseInt(name.substring(6,name.length()));
		
		for(int i=0; i<100; i++)
		{
			sm = createStateMachine();
			name = sm.getName();
			assertEquals("noname", name.substring(0,6));
			
			int cnt = Integer.parseInt(name.substring(6,name.length()));
			assertTrue(cnt == (i+init+1) );
		}
	}
	
	
	public void testContext()
	{
		StateMachine sm = createStateMachine();
		ModelElement context = createModelElement();
		sm.setContext(context);
		assertTrue(sm.getContext().equals(context));
		ModelElement newContext = createModelElement();
		sm.setContext(newContext);
		assertTrue(sm.getContext().equals(newContext));
	}
	
	
	
}
