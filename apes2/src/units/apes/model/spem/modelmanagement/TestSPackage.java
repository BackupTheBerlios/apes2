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


package apes.model.spem.modelmanagement;

import junit.framework.TestCase;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;

public class TestSPackage extends TestCase
{
	private ModelElement createModelElement()
	{
		return new ModelElement()
		{
			public void visit(SpemVisitor visitor) {}
		};
	}
	
	public void testAddModelElement()
	{
		SPackage p = new SPackage();
		ModelElement me = createModelElement();
		
		assertTrue(p.modelElementCount()==0);
		assertTrue(me.getParent()==null);
		
		p.addModelElement(me);
		
		assertTrue(p.modelElementCount()==1);
		assertTrue(me.getParent()==p);
		
		p.addModelElement(me);
		assertTrue(p.modelElementCount()==1);
		assertTrue(me.getParent()==p);
		
		for(int i=1; i<100; i++)
		{
			me = createModelElement();
			assertTrue(p.modelElementCount()==i);
			p.addModelElement(me);
		}
		
	}

	public void testRemoveModelElement()
	{
		ModelElement me1 = createModelElement();
		ModelElement me2 = createModelElement();
		SPackage p = new SPackage();
		
		p.addModelElement(me1);
		assertTrue(p.modelElementCount()==1);
		assertTrue(me1.getParent()==p);
		
		p.removeModelElement(me2);
		assertTrue(p.modelElementCount()==1);
		
		p.removeModelElement(me1);
		assertTrue(p.modelElementCount()==0);
		assertTrue(me1.getParent()==null);
		
		for(int i=0; i<100; i++)
		{
			me1 = createModelElement();
			p.addModelElement(me1);
		}
		
		p.addModelElement(me2);
		
		for(int i=0; i<100; i++)
		{
			me1 = createModelElement();
			p.addModelElement(me1);
		}
		
		assertTrue(p.modelElementCount()==201);
		p.removeModelElement(me2);
		assertTrue(p.modelElementCount()==200);
	}
	
	public void testContainsModelElement()
	{
		ModelElement me1;
		ModelElement me2 = createModelElement();
		SPackage p = new SPackage();
		
		for(int i=0; i<100; i++)
		{
			me1 = createModelElement();
			p.addModelElement(me1);
		}
		
		assertFalse(p.containsModelElement(me2));
		p.addModelElement(me2);
		assertTrue(p.containsModelElement(me2));
		
		for(int i=0; i<100; i++)
		{
			me1 = createModelElement();
			p.addModelElement(me1);
		}
		
		assertTrue(p.containsModelElement(me2));
	}

	public void testGetModelElement()
	{
		ModelElement me1;
		ModelElement me2 = createModelElement();
		SPackage p = new SPackage();
		
		for(int i=0; i<100; i++)
		{
			me1 = createModelElement();
			p.addModelElement(me1);
		}
		
		p.addModelElement(me2);
		
		for(int i=0; i<100; i++)
		{
			me1 = createModelElement();
			p.addModelElement(me1);
		}
		
		assertEquals(p.getModelElement(100), me2);
		assertEquals(p.getModelElement(-1), null);
		assertEquals(p.getModelElement(201), null);
	}
}
