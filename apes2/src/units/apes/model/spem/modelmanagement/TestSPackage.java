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

import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

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
		ProcessRole pr = new ProcessRole();
		WorkDefinition wd = new WorkDefinition();
		WorkProduct wp = new WorkProduct();
		ResponsabilityDiagram rp = new ResponsabilityDiagram();
		
		ModelElement me = createModelElement();
		Activity a = new Activity();
		
		SPackage p = new SPackage();
		assertEquals(0, p.modelElementCount());
		
		wrongAddTest(p, a);
		wrongAddTest(p, me);
		
		rightAddTest(p, pr);
		rightAddTest(p, wd);
		rightAddTest(p, wp);
		rightAddTest(p, rp);
				
		SPackage p1 = new SPackage();
		assertEquals(0, p1.modelElementCount());
		
		wrongAddTest(p1, pr);
		wrongAddTest(p1, wd);
		wrongAddTest(p1, wp);
		wrongAddTest(p1, rp);
				
		for(int i=0; i<100; i++)
		{
			pr = new ProcessRole();
			assertEquals(i, p1.modelElementCount());
			p1.addModelElement(pr);
		}
		
	}

	public void wrongAddTest(SPackage p, ModelElement me)
	{
		Object parent = me.getParent();
		int count = p.modelElementCount();
		if(p.addModelElement(me))
			fail();
		assertEquals(count, p.modelElementCount());
		assertEquals(parent, me.getParent());
	}

	public void rightAddTest(SPackage p, ModelElement me)
	{
		int count = p.modelElementCount();
		if(!p.addModelElement(me))
			fail();
		assertEquals(count+1, p.modelElementCount());
		assertEquals(p, me.getParent());
	}

	public void testRemoveModelElement()
	{
		ProcessRole pr = new ProcessRole();
		WorkDefinition wd = new WorkDefinition();
		WorkProduct wp = new WorkProduct();
		ResponsabilityDiagram rp = new ResponsabilityDiagram();
		
		SPackage p = new SPackage();
		
		p.addModelElement(pr);
		assertTrue(p.modelElementCount()==1);
		assertTrue(pr.getParent()==p);
		
		p.removeModelElement(wd);
		assertTrue(p.modelElementCount()==1);
		
		p.removeModelElement(pr);
		assertTrue(p.modelElementCount()==0);
		assertTrue(pr.getParent()==null);
		
		for(int i=0; i<100; i++)
		{
			wd = new WorkDefinition();
			p.addModelElement(wd);
		}
		
		p.addModelElement(wp);
		
		for(int i=0; i<100; i++)
		{
			rp = new ResponsabilityDiagram();
			p.addModelElement(rp);
		}
		
		assertTrue(p.modelElementCount()==201);
		p.removeModelElement(wp);
		assertTrue(p.modelElementCount()==200);
	}
	
	public void testContainsModelElement()
	{
		ProcessRole pr = null;
		ModelElement me2 = createModelElement();
		SPackage p = new SPackage();
		
		for(int i=0; i<100; i++)
		{
			pr = new ProcessRole();
			p.addModelElement(pr);
		}
		
		assertFalse(p.containsModelElement(me2));
		assertTrue(p.containsModelElement(pr));
	}

	public void testGetModelElement()
	{
		ProcessRole pr = null;
		WorkDefinition wd = new WorkDefinition();
		WorkProduct wp = new WorkProduct();
		ResponsabilityDiagram rp = new ResponsabilityDiagram();
		
		SPackage p = new SPackage();
		
		for(int i=0; i<100; i++)
		{
			pr = new ProcessRole();
			p.addModelElement(pr);
		}
		
		p.addModelElement(wd);
		
		for(int i=0; i<100; i++)
		{
			pr = new ProcessRole();
			p.addModelElement(pr);
		}
		
		assertEquals(wd, p.getModelElement(100));
		assertNull(p.getModelElement(-1));
		assertEquals(pr, p.getModelElement(200));
		assertNull(p.getModelElement(201));
	}
}
