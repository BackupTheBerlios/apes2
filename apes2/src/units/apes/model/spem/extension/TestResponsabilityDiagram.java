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

import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestResponsabilityDiagram extends TestCase
{
	private ProcessRole createProcessRole()
	{
		return new ProcessRole();
	}
	
	private WorkProduct createWorkProduct()
	{
		return new WorkProduct();
	}
	
	public void testAddModelElement()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(rd.modelElementCount()==0);
		assertTrue(rd.addModelElement(pr));
		assertTrue(rd.modelElementCount()==1);
		assertTrue(rd.containsModelElement(pr));
		
		assertTrue(rd.modelElementCount()==1);
		assertTrue(rd.addModelElement(wp));
		assertTrue(rd.modelElementCount()==2);
		assertTrue(rd.containsModelElement(wp));	
	}
	
	public void testAddProcessRole()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		
		assertTrue(rd.modelElementCount()==0);
		
		rd.addProcessRole(pr);
		
		assertTrue(rd.modelElementCount()==1);
		
		rd.addProcessRole(pr);
		assertTrue(rd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			pr = createProcessRole();
			assertTrue(rd.modelElementCount()==i);
			rd.addProcessRole(pr);
		}
	}
	
	public void testAddWorkProduct()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(rd.modelElementCount()==0);
		
		rd.addWorkProduct(wp);
		
		assertTrue(rd.modelElementCount()==1);
		
		rd.addWorkProduct(wp);
		assertTrue(rd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			wp = createWorkProduct();
			assertTrue(rd.modelElementCount()==i);
			rd.addWorkProduct(wp);
		}
	}
	
	public void testRemoveModelElement()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		
		rd.addProcessRole(pr);
		assertTrue(rd.modelElementCount()==1);
		
		rd.removeModelElement(pr2);
		assertTrue(rd.modelElementCount()==1);
		
		rd.removeModelElement(pr);
		assertTrue(rd.modelElementCount()==0);
		
		rd.addWorkProduct(wp);
		assertTrue(rd.modelElementCount()==1);
		
		rd.removeModelElement(wp2);
		assertTrue(rd.modelElementCount()==1);
		
		rd.removeModelElement(wp);
		assertTrue(rd.modelElementCount()==0);
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			rd.addProcessRole(pr);
		}
		
		rd.addProcessRole(pr2);
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			rd.addWorkProduct(wp);
		}
		
		assertTrue(rd.modelElementCount()==201);
		rd.removeModelElement(pr2);
		assertTrue(rd.modelElementCount()==200);
	}
	
	public void testContainsModelElement()
	{
		ProcessRole pr;
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp;
		WorkProduct wp2 = createWorkProduct();
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			rd.addProcessRole(pr);
		}
		
		assertFalse(rd.containsModelElement(pr2));
		rd.addProcessRole(pr2);
		assertTrue(rd.containsModelElement(pr2));
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			rd.addProcessRole(pr);
		}
		
		assertTrue(rd.containsModelElement(pr2));	
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			rd.addWorkProduct(wp);
		}
		
		assertFalse(rd.containsModelElement(wp2));
		rd.addWorkProduct(wp2);
		assertTrue(rd.containsModelElement(wp2));
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			rd.addWorkProduct(wp);
		}
		
		assertTrue(rd.containsModelElement(wp2));
	}
	public void testGetModelElement()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr;
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp;
		WorkProduct wp2 = createWorkProduct();
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			rd.addModelElement(pr);
			wp = createWorkProduct();
			rd.addModelElement(wp);
		}
		
		rd.addModelElement(pr2);
		rd.addModelElement(wp2);

		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			rd.addModelElement(pr);
			
			wp = createWorkProduct();
			rd.addModelElement(wp);
		}
		
		assertEquals(rd.getModelElement(200), pr2);
		assertEquals(rd.getModelElement(201), wp2);
		
		assertEquals(rd.getModelElement(-1), null);

		assertEquals(rd.getModelElement(403), null);

		assertEquals(rd.getModelElement(404), null);

	}
	
	public void testModelElementCount()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		assertEquals(rd.modelElementCount(),0);
		
		rd.addProcessRole(pr);
		assertEquals(rd.modelElementCount(),1);
		
		rd.addProcessRole(pr);
		assertEquals(rd.modelElementCount(),1);
		
		rd.addWorkProduct(wp);
		assertEquals(rd.modelElementCount(),2);
		
		rd.addWorkProduct(wp);
		assertEquals(rd.modelElementCount(),2);
		
		rd.removeModelElement(pr);
		assertEquals(rd.modelElementCount(),1);
		
		rd.removeModelElement(pr);
		assertEquals(rd.modelElementCount(),1);
		
		rd.removeModelElement(wp);
		assertEquals(rd.modelElementCount(),0);
		
		for(int i=0;i<100;i++)
		{
			pr = createProcessRole();
			rd.addProcessRole(pr);
		}
		assertEquals(rd.modelElementCount(),100);
	}
	
	public void testCreateLinkModelElements()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		
		assertFalse(rd.createLinkModelElements(pr,wp));
		assertFalse(rd.createLinkModelElements(wp,pr));
		
		rd.addModelElement(pr);
		assertFalse(rd.createLinkModelElements(pr,wp));
		assertFalse(rd.createLinkModelElements(wp,pr));
		
		rd.addModelElement(wp);
		assertTrue(rd.createLinkModelElements(pr,wp));
		assertFalse(rd.createLinkModelElements(wp,pr));
		assertFalse(rd.createLinkModelElements(pr,wp));
		
		assertFalse(rd.createLinkModelElements(pr,pr));
		assertFalse(rd.createLinkModelElements(wp,wp));
		
		rd.addModelElement(pr2);
		assertFalse(rd.createLinkModelElements(pr2,wp));
	}
	
	public void testCreateLinkProcessRoleWorkProduct()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		
		assertFalse(rd.createLinkProcessRoleWorkProduct(pr,wp));
		
		rd.addProcessRole(pr);
		assertFalse(rd.createLinkProcessRoleWorkProduct(pr,wp));
		
		rd.addWorkProduct(wp);
		assertFalse(rd.createLinkProcessRoleWorkProduct(pr2,wp));
		assertTrue(rd.createLinkProcessRoleWorkProduct(pr,wp));
		
		rd.addWorkProduct(wp2);
		assertTrue(rd.createLinkProcessRoleWorkProduct(pr,wp2));
		
		assertFalse(rd.createLinkProcessRoleWorkProduct(pr,wp));
	}

	public void testRemoveLinkModelElements()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(rd.removeLinkModelElements(pr,wp));
		assertFalse(rd.removeLinkModelElements(wp,pr));
		
		rd.addModelElement(pr);	
		rd.addModelElement(wp);
		
		assertFalse(rd.removeLinkModelElements(pr,wp));	
		rd.createLinkModelElements(pr,wp);
		assertTrue(rd.removeLinkModelElements(pr,wp));
		assertFalse(rd.removeLinkModelElements(pr,wp));
	}	
	
	public void testRemoveLinkProcessRoleWorkProduct()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(rd.removeLinkProcessRoleWorkProduct(pr,wp));
		
		rd.addProcessRole(pr);
		assertFalse(rd.removeLinkProcessRoleWorkProduct(pr,wp));
		rd.addWorkProduct(wp);
		assertFalse(rd.removeLinkProcessRoleWorkProduct(pr,wp));
		
		rd.createLinkProcessRoleWorkProduct(pr,wp);
		
		assertTrue(rd.removeLinkProcessRoleWorkProduct(pr,wp));
		assertFalse(rd.removeLinkProcessRoleWorkProduct(pr,wp));
	}
	
	public void testAreLinkableModelElements()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(rd.areLinkableModelElements(pr,wp));
		assertFalse(rd.areLinkableModelElements(wp,pr));
		
		rd.addModelElement(pr);	
		rd.addModelElement(wp);
	
		assertFalse(rd.areLinkableModelElements(wp,pr));
		assertTrue(rd.areLinkableModelElements(pr,wp));
	
		rd.createLinkModelElements(pr,wp);
		assertFalse(rd.areLinkableModelElements(pr,wp));
	}
	
	public void testAreLinkableProcessRoleWorkProduct()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		
		assertFalse(rd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		rd.addProcessRole(pr);
		assertFalse(rd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		rd.addWorkProduct(wp);
		assertFalse(rd.areLinkableProcessRoleWorkProduct(pr2,wp));
		assertTrue(rd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		rd.createLinkProcessRoleWorkProduct(pr,wp);
		assertFalse(rd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		rd.addWorkProduct(wp2);
		assertTrue(rd.areLinkableProcessRoleWorkProduct(pr,wp2));
		
		rd.addProcessRole(pr2);
		assertTrue(rd.areLinkableProcessRoleWorkProduct(pr2,wp2));
	}
	
	public void testExistsLinkModelElements()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		rd.addModelElement(pr);	
		rd.addModelElement(wp);
		
		assertFalse(rd.existsLinkModelElements(pr,wp));
		assertFalse(rd.existsLinkModelElements(wp,pr));
	
		assertFalse(rd.existsLinkModelElements(pr,pr));
		assertFalse(rd.existsLinkModelElements(wp,wp));
		rd.createLinkModelElements(pr,wp);
		assertTrue(rd.existsLinkModelElements(pr,wp));
	}
	
	public void testExistsLinkProcessRoleWorkProduct()
	{
		ResponsabilityDiagram rd = new ResponsabilityDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(rd.existsLinkProcessRoleWorkProduct(pr,wp));
		
		rd.addProcessRole(pr);
		assertFalse(rd.existsLinkProcessRoleWorkProduct(pr,wp));
		
		rd.addWorkProduct(wp);
		assertFalse(rd.existsLinkProcessRoleWorkProduct(pr,wp));
		
		rd.createLinkProcessRoleWorkProduct(pr,wp);
		assertTrue(rd.existsLinkProcessRoleWorkProduct(pr,wp));
		assertFalse(rd.existsLinkProcessRoleWorkProduct(pr2,wp));
		
		rd.removeLinkProcessRoleWorkProduct(pr,wp);
		assertFalse(rd.existsLinkProcessRoleWorkProduct(pr,wp));
	}
}
