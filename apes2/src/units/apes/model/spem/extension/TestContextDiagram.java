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


package apes.model.spem.extension;

import junit.framework.TestCase;

import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestContextDiagram extends TestCase
{
	private WorkProduct createWorkProduct()
	{
		return new WorkProduct();
	}
	
	public void testAddModelElement()
	{
		WorkProduct wp = createWorkProduct();
		ProcessComponent pc = new ProcessComponent();
		ContextDiagram cd = new ContextDiagram();
		
		assertTrue(cd.modelElementCount()==1);
		assertTrue(cd.addModelElement(wp));
		assertTrue(cd.modelElementCount()==2);
		assertTrue(cd.containsModelElement(wp));
		
		assertTrue(cd.containsModelElement(pc));	
	}
	
	public void testAddWorkProduct()
	{
		ContextDiagram cd = new ContextDiagram();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(cd.modelElementCount()==1);
		
		cd.addWorkProduct(wp);
		
		assertTrue(cd.modelElementCount()==2);
		
		cd.addWorkProduct(wp);
		assertTrue(cd.modelElementCount()==2);
		
		for(int i=1; i<100; i++)
		{
			wp = createWorkProduct();
			assertTrue(cd.modelElementCount()==i+1);
			cd.addWorkProduct(wp);
		}
	}
	
	public void testRemoveModelElement()
	{
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ProcessComponent pc = new ProcessComponent();
		ContextDiagram cd = new ContextDiagram();
		
		cd.addWorkProduct(wp);
		assertTrue(cd.modelElementCount()==2);
		
		cd.removeModelElement(wp2);
		assertTrue(cd.modelElementCount()==2);
		
		cd.removeModelElement(wp);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(pc);
		assertFalse(cd.modelElementCount()==0);
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			cd.addWorkProduct(wp);
		}
		
		assertTrue(cd.modelElementCount()==101);
		cd.removeModelElement(wp2);
		assertTrue(cd.modelElementCount()==101);
	}
	
	public void testContainsModelElement()
	{
		WorkProduct wp;
		WorkProduct wp2 = createWorkProduct();
		ContextDiagram cd = new ContextDiagram();
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			cd.addWorkProduct(wp);
		}
		
		assertFalse(cd.containsModelElement(wp2));
		cd.addWorkProduct(wp2);
		assertTrue(cd.containsModelElement(wp2));
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			cd.addWorkProduct(wp);
		}
		
		assertTrue(cd.containsModelElement(wp2));
	}
	
	public void testGetModelElement()
	{
		WorkProduct wp;
		WorkProduct wp2 = createWorkProduct();
		ContextDiagram cd = new ContextDiagram();
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			cd.addWorkProduct(wp);
		}
		
		cd.addWorkProduct(wp2);
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			cd.addWorkProduct(wp);
		}
		
		assertEquals(cd.getModelElement(101), wp2);
		assertEquals(cd.getModelElement(-1), null);
		assertEquals(cd.getModelElement(202), null);
	}
	
	public void testModelElementCount()
	{
		WorkProduct wp = createWorkProduct();
		ContextDiagram cd = new ContextDiagram();
		
		assertEquals(cd.modelElementCount(),1);
		
		cd.addWorkProduct(wp);
		assertEquals(cd.modelElementCount(),2);
		
		cd.addWorkProduct(wp);
		assertEquals(cd.modelElementCount(),2);
		
		cd.removeModelElement(wp);
		assertEquals(cd.modelElementCount(),1);
		
		cd.removeModelElement(wp);
		assertEquals(cd.modelElementCount(),1);
		
		for(int i=0;i<100;i++)
		{
			wp = createWorkProduct();
			cd.addWorkProduct(wp);
		}
		assertEquals(cd.modelElementCount(),101);
	}
	
	public void testCreateLinkModelElements()
	{
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ProcessComponent pc = new ProcessComponent();
		ContextDiagram cd = new ContextDiagram();
		
		assertFalse(cd.createLinkModelElements(wp,pc));
		
		cd.addWorkProduct(wp);
		assertTrue(cd.createLinkModelElements(wp,pc));
		
		cd.addWorkProduct(wp2);
		assertFalse(cd.createLinkModelElements(wp,wp2));
		assertTrue(cd.createLinkModelElements(pc,wp2));		
	}

	
	public void testRemoveLinkModelElements()
	{
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ProcessComponent pc = new ProcessComponent();
		ContextDiagram cd = new ContextDiagram();
		
		assertFalse(cd.removeLinkModelElements(wp,pc));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.removeLinkModelElements(wp,pc));
		
		cd.addWorkProduct(wp2);
		cd.createLinkModelElements(wp,pc);
		cd.createLinkModelElements(pc,wp2);
		
		assertTrue(cd.removeLinkModelElements(wp,pc));
		assertFalse(cd.removeLinkModelElements(pc,wp));
		assertTrue(cd.removeLinkModelElements(pc,wp2));
	}
	
	
	public void testAreLinkableModelElements()
	{
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ProcessComponent pc = new ProcessComponent();
		ContextDiagram cd = new ContextDiagram();
		
		assertFalse(cd.areLinkableModelElements(wp,wp2));
		assertFalse(cd.areLinkableModelElements(wp,pc));
		
		cd.addWorkProduct(wp);
		assertTrue(cd.areLinkableModelElements(wp,pc));
		
		cd.addWorkProduct(wp2);
		assertTrue(cd.areLinkableModelElements(pc,wp2));
		
		cd.createLinkModelElements(wp,pc);
		assertFalse(cd.areLinkableModelElements(wp,pc));
		assertFalse(cd.areLinkableModelElements(pc,wp));
	}
	
	public void testExistsLinkModelElements()
	{
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ProcessComponent pc = new ProcessComponent();
		ContextDiagram cd = new ContextDiagram();
		
		assertFalse(cd.existsLinkModelElements(wp,pc));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.existsLinkModelElements(wp,pc));
		
		cd.createLinkModelElements(wp,pc);
		assertTrue(cd.existsLinkModelElements(wp,pc));
		
		cd.addWorkProduct(wp2);
		assertFalse(cd.existsLinkModelElements(pc,wp2));
		cd.createLinkModelElements(pc,wp2);
		assertTrue(cd.existsLinkModelElements(pc,wp2));
		
		cd.removeLinkModelElements(wp,pc);
		assertFalse(cd.existsLinkModelElements(wp,pc));
	}
}
