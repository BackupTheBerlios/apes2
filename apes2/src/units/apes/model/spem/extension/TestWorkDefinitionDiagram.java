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

import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestWorkDefinitionDiagram extends TestCase
{
	private ProcessRole createProcessRole()
	{
		return new ProcessRole();
	}
	
	private WorkDefinition createWorkDefinition()
	{
		return new WorkDefinition();
	}
	
	private WorkProduct createWorkProduct()
	{
		return new WorkProduct();
	}
	
	public void testAddProcessRole()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		
		assertTrue(wdd.modelElementCount()==0);
		
		wdd.addProcessRole(pr);
		
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.addProcessRole(pr);
		assertTrue(wdd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			pr = createProcessRole();
			assertTrue(wdd.modelElementCount()==i);
			wdd.addProcessRole(pr);
		}
	}
	
	public void testAddWorkDefinition()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		WorkDefinition wd = createWorkDefinition();
		
		assertTrue(wdd.modelElementCount()==0);
		
		wdd.addWorkDefinition(wd);
		
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.addWorkDefinition(wd);
		assertTrue(wdd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			wd = createWorkDefinition();
			assertTrue(wdd.modelElementCount()==i);
			wdd.addWorkDefinition(wd);
		}
	}
	
	public void testAddWorkProduct()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(wdd.modelElementCount()==0);
		
		wdd.addWorkProduct(wp);
		
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.addWorkProduct(wp);
		assertTrue(wdd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			wp = createWorkProduct();
			assertTrue(wdd.modelElementCount()==i);
			wdd.addWorkProduct(wp);
		}
	}
	
	public void testRemoveModelElement()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		wdd.addProcessRole(pr);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(pr2);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(pr);
		assertTrue(wdd.modelElementCount()==0);
		
		
		wdd.addWorkDefinition(wd);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(wd2);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(wd);
		assertTrue(wdd.modelElementCount()==0);
		
		
		wdd.addWorkProduct(wp);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(wp2);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(wp);
		assertTrue(wdd.modelElementCount()==0);
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		
		wdd.addProcessRole(pr2);
		
		for(int i=0; i<100; i++)
		{
			wd = createWorkDefinition();
			wdd.addWorkDefinition(wd);
		}
		
		assertTrue(wdd.modelElementCount()==201);
		wdd.removeModelElement(pr2);
		assertTrue(wdd.modelElementCount()==200);
	}
	
	public void testContainsModelElement()
	{
		ProcessRole pr;
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd;
		WorkDefinition wd2 = createWorkDefinition();
		WorkProduct wp;
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		
		assertFalse(wdd.containsModelElement(pr2));
		wdd.addProcessRole(pr2);
		assertTrue(wdd.containsModelElement(pr2));
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		
		assertTrue(wdd.containsModelElement(pr2));
		
		
		for(int i=0; i<100; i++)
		{
			wd = createWorkDefinition();
			wdd.addWorkDefinition(wd);
		}
		
		assertFalse(wdd.containsModelElement(wd2));
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.containsModelElement(wd2));
		
		for(int i=0; i<100; i++)
		{
			wd = createWorkDefinition();
			wdd.addWorkDefinition(wd);
		}
		
		assertTrue(wdd.containsModelElement(wd2));
		
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			wdd.addWorkProduct(wp);
		}
		
		assertFalse(wdd.containsModelElement(wp2));
		wdd.addWorkProduct(wp2);
		assertTrue(wdd.containsModelElement(wp2));
		
		for(int i=0; i<100; i++)
		{
			wp = createWorkProduct();
			wdd.addWorkProduct(wp);
		}
		
		assertTrue(wdd.containsModelElement(wp2));
	}
	
	
	public void testGetModelElement()
	{
		ProcessRole pr;
		ProcessRole pr2 = createProcessRole();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		
		wdd.addProcessRole(pr2);
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		
		assertEquals(wdd.getModelElement(100), pr2);
		assertEquals(wdd.getModelElement(-1), null);
		assertEquals(wdd.getModelElement(201), null);
	}
	
	public void testModelElementCount()
	{
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertEquals(wdd.modelElementCount(),0);
		
		wdd.addProcessRole(pr);
		assertEquals(wdd.modelElementCount(),1);
		
		wdd.addProcessRole(pr);
		assertEquals(wdd.modelElementCount(),1);
		
		wdd.addWorkDefinition(wd);
		assertEquals(wdd.modelElementCount(),2);
		
		wdd.addWorkDefinition(wd);
		assertEquals(wdd.modelElementCount(),2);
		
		wdd.addWorkProduct(wp);
		assertEquals(wdd.modelElementCount(),3);
		
		wdd.addWorkProduct(wp);
		assertEquals(wdd.modelElementCount(),3);
		
		wdd.removeModelElement(pr);
		assertEquals(wdd.modelElementCount(),2);
		
		wdd.removeModelElement(pr);
		assertEquals(wdd.modelElementCount(),2);
		
		for(int i=0;i<100;i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		assertEquals(wdd.modelElementCount(),102);
	}
	
	public void testCreateLinkProcessRoleWorkDefinition()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addProcessRole(pr);
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr2,wd));
		assertTrue(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.createLinkProcessRoleWorkDefinition(pr,wd2));
		
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
	}
	
	public void testCreateLinkWorkProductWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.createLinkWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkProduct(wp);
		assertFalse(wdd.createLinkWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.createLinkWorkProductWorkDefinition(wp2,wd));
		assertTrue(wdd.createLinkWorkProductWorkDefinition(wp,wd));
		
		assertEquals(wdd.getTransitionCount(),1);

		assertFalse(wdd.createLinkWorkProductWorkDefinition(wp,wd));

		wdd.addWorkProduct(wp2);
		assertTrue(wdd.createLinkWorkProductWorkDefinition(wp2,wd));

		assertEquals(wdd.getTransitionCount(),2);
	}
	
	public void testCreateLinkWorkDefinitionWorkProduct()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.createLinkWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkProduct(wp);
		assertFalse(wdd.createLinkWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.createLinkWorkDefinitionWorkProduct(wd,wp2));
		assertTrue(wdd.createLinkWorkDefinitionWorkProduct(wd,wp));
		
		assertEquals(wdd.getTransitionCount(),1);

		assertFalse(wdd.createLinkWorkDefinitionWorkProduct(wd,wp));
		
		WorkDefinition wd2 = createWorkDefinition();
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.createLinkWorkDefinitionWorkProduct(wd2,wp));
		
		assertEquals(wdd.getTransitionCount(),2);
		
	}
	
	public void testRemoveLinkProcessRoleWorkDefinition()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addProcessRole(pr);
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr2,wd));
		
		wdd.createLinkProcessRoleWorkDefinition(pr,wd);
		
		assertTrue(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
	}
	
	public void testRemoveLinkWorkProductWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.removeLinkWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.removeLinkWorkProductWorkDefinition(wp,wd));
		wdd.addWorkProduct(wp);
		assertFalse(wdd.removeLinkWorkProductWorkDefinition(wp,wd));
		assertFalse(wdd.removeLinkWorkProductWorkDefinition(wp,wd2));
		
		wdd.createLinkWorkProductWorkDefinition(wp,wd);
		
		assertEquals(wdd.getTransitionCount(),1);
		
		assertTrue(wdd.removeLinkWorkProductWorkDefinition(wp,wd));

		assertEquals(wdd.getTransitionCount(),0);

		assertFalse(wdd.removeLinkWorkProductWorkDefinition(wp,wd));
	}
	
	public void testRemoveLinkWorkDefinitionWorkProduct()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.removeLinkWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.removeLinkWorkDefinitionWorkProduct(wd,wp));
		wdd.addWorkProduct(wp);
		assertFalse(wdd.removeLinkWorkDefinitionWorkProduct(wd,wp));
		assertFalse(wdd.removeLinkWorkDefinitionWorkProduct(wd2,wp));
		
		wdd.createLinkWorkDefinitionWorkProduct(wd,wp);
		
		assertEquals(wdd.getTransitionCount(),1);

		assertTrue(wdd.removeLinkWorkDefinitionWorkProduct(wd,wp));
		
		assertEquals(wdd.getTransitionCount(),0);

		assertFalse(wdd.removeLinkWorkDefinitionWorkProduct(wd,wp));
	}
	
	public void testAreLinkableProcessRoleWorkDefinition()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.areLinkableProcessRoleWorkDefinition(pr,wd));
		
		wdd.addProcessRole(pr);
		assertFalse(wdd.areLinkableProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.areLinkableProcessRoleWorkDefinition(pr2,wd));
		assertTrue(wdd.areLinkableProcessRoleWorkDefinition(pr,wd));
		
		wdd.createLinkProcessRoleWorkDefinition(pr,wd);
		assertFalse(wdd.areLinkableProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.areLinkableProcessRoleWorkDefinition(pr,wd2));
	}
	
	public void testAreLinkableWorkProductWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.areLinkableWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkProduct(wp);
		assertFalse(wdd.areLinkableWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.areLinkableWorkProductWorkDefinition(wp2,wd));
		assertTrue(wdd.areLinkableWorkProductWorkDefinition(wp,wd));
		
		wdd.createLinkWorkProductWorkDefinition(wp,wd);
		assertFalse(wdd.areLinkableWorkProductWorkDefinition(wp,wd));
	}
	
	public void testAreLinkableWorkDefinitionWorkProduct()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.areLinkableWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkProduct(wp);
		assertFalse(wdd.areLinkableWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.areLinkableWorkDefinitionWorkProduct(wd,wp2));
		assertTrue(wdd.areLinkableWorkDefinitionWorkProduct(wd,wp));
		
		wdd.createLinkWorkDefinitionWorkProduct(wd,wp);
		assertFalse(wdd.areLinkableWorkDefinitionWorkProduct(wd,wp));
	}
	
	public void testExistsLinkProcessRoleWorkDefinition()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.existsLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addProcessRole(pr);
		assertFalse(wdd.existsLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.existsLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.createLinkProcessRoleWorkDefinition(pr,wd);
		assertTrue(wdd.existsLinkProcessRoleWorkDefinition(pr,wd));
		assertFalse(wdd.existsLinkProcessRoleWorkDefinition(pr2,wd));
		
		wdd.removeLinkProcessRoleWorkDefinition(pr,wd);
		assertFalse(wdd.existsLinkProcessRoleWorkDefinition(pr,wd));
	}
	
	public void testExistsLinkWorkProductWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.existsLinkWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkProduct(wp);
		assertFalse(wdd.existsLinkWorkProductWorkDefinition(wp,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.existsLinkWorkProductWorkDefinition(wp,wd));
		
		wdd.createLinkWorkProductWorkDefinition(wp,wd);
		assertTrue(wdd.existsLinkWorkProductWorkDefinition(wp,wd));
		assertFalse(wdd.areLinkableWorkProductWorkDefinition(wp2,wd));
		
		wdd.removeLinkWorkProductWorkDefinition(wp,wd);
		assertFalse(wdd.existsLinkWorkProductWorkDefinition(wp,wd));
	}
	
	public void testExistsLinkWorkDefinitionWorkProduct()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.existsLinkWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkProduct(wp);
		assertFalse(wdd.existsLinkWorkDefinitionWorkProduct(wd,wp));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.existsLinkWorkDefinitionWorkProduct(wd,wp));
		
		wdd.createLinkWorkDefinitionWorkProduct(wd,wp);
		assertTrue(wdd.existsLinkWorkDefinitionWorkProduct(wd,wp));
		assertFalse(wdd.areLinkableWorkDefinitionWorkProduct(wd,wp2));
		
		wdd.removeLinkWorkDefinitionWorkProduct(wd,wp);
		assertFalse(wdd.existsLinkWorkDefinitionWorkProduct(wd,wp));
	}
	
	public void testAddModelElement()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(wdd.modelElementCount()==0);
		assertTrue(wdd.addModelElement(pr));
		assertTrue(wdd.modelElementCount()==1);
		assertTrue(wdd.containsModelElement(pr));
		
		assertTrue(wdd.modelElementCount()==1);
		assertTrue(wdd.addModelElement(wd));
		assertTrue(wdd.modelElementCount()==2);
		assertTrue(wdd.containsModelElement(wd));
		
		assertTrue(wdd.modelElementCount()==2);
		assertTrue(wdd.addModelElement(wp));
		assertTrue(wdd.modelElementCount()==3);
		assertTrue(wdd.containsModelElement(wp));	
	}
	
	public void testCreateLinkModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(pr,wp));
		assertFalse(wdd.createLinkModelElements(wp,wd));
		assertFalse(wdd.createLinkModelElements(wd,wp));
		
		wdd.addModelElement(wd);
		assertFalse(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(wp,wd));
		
		wdd.addModelElement(wp);
		assertTrue(wdd.createLinkModelElements(wp,wd));
		assertFalse(wdd.createLinkModelElements(wp,wd));
		assertTrue(wdd.createLinkModelElements(wd,wp));
		assertFalse(wdd.createLinkModelElements(wd,wp));
		assertFalse(wdd.createLinkModelElements(pr,wd));
		
		wdd.addModelElement(pr);
		assertTrue(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(wd,pr));
		assertFalse(wdd.createLinkModelElements(pr,wp));
		assertFalse(wdd.createLinkModelElements(wp,pr));
		
		assertFalse(wdd.createLinkModelElements(wd,wd));
		assertFalse(wdd.createLinkModelElements(pr,pr));
		assertFalse(wdd.createLinkModelElements(wp,wp));
		
		wdd.addModelElement(pr2);
		assertFalse(wdd.createLinkModelElements(pr2,wp));
	}
	
	public void testRemoveLinkModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(wdd.removeLinkModelElements(pr,wd));
		assertFalse(wdd.removeLinkModelElements(pr,wp));
		assertFalse(wdd.removeLinkModelElements(wp,wd));
		assertFalse(wdd.removeLinkModelElements(wd,wp));
		
		wdd.addModelElement(wd);	
		wdd.addModelElement(wp);
		
		assertFalse(wdd.removeLinkModelElements(wd,wp));	
		wdd.createLinkModelElements(wd,wp);
		assertTrue(wdd.removeLinkModelElements(wd,wp));
		assertFalse(wdd.removeLinkModelElements(wd,wp));
		
		assertFalse(wdd.removeLinkModelElements(wp,wd));	
		wdd.createLinkModelElements(wp,wd);
		assertTrue(wdd.removeLinkModelElements(wp,wd));
		assertFalse(wdd.removeLinkModelElements(wp,wd));	
		
		wdd.addModelElement(pr);
		
		assertFalse(wdd.removeLinkModelElements(pr,wd));
		wdd.createLinkModelElements(pr,wd);
		assertTrue(wdd.removeLinkModelElements(pr,wd));
		assertFalse(wdd.removeLinkModelElements(pr,wd));
		
		assertFalse(wdd.removeLinkModelElements(pr,wp));
	}
	
	public void testAreLinkableModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,wp));
		assertFalse(wdd.areLinkableModelElements(wp,wd));
		assertFalse(wdd.areLinkableModelElements(wd,wp));
		
		wdd.addModelElement(wd);	
		wdd.addModelElement(wp);
		
		assertFalse(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,wp));
		assertTrue(wdd.areLinkableModelElements(wp,wd));
		assertTrue(wdd.areLinkableModelElements(wd,wp));
		
		wdd.addModelElement(pr);	
		
		assertTrue(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,wp));
		assertFalse(wdd.areLinkableModelElements(wd,pr));
		assertFalse(wdd.areLinkableModelElements(wp,pr));
		
		wdd.createLinkModelElements(pr,wd);
		assertFalse(wdd.areLinkableModelElements(pr,wd));
	}
	
	public void testExistsLinkModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		
		wdd.addModelElement(wd);	
		wdd.addModelElement(wp);
		wdd.addModelElement(pr);	
		
		assertFalse(wdd.existsLinkModelElements(pr,wd));
		assertFalse(wdd.existsLinkModelElements(pr,wp));
		assertFalse(wdd.existsLinkModelElements(wp,wd));
		assertFalse(wdd.existsLinkModelElements(wd,wp));
		
		assertFalse(wdd.existsLinkModelElements(pr,pr));
		assertFalse(wdd.existsLinkModelElements(wd,wd));
		assertFalse(wdd.existsLinkModelElements(wp,wp));
		
		wdd.createLinkModelElements(pr,wd);
		assertTrue(wdd.existsLinkModelElements(pr,wd));
		wdd.removeLinkModelElements(pr,wd);
		assertFalse(wdd.existsLinkModelElements(pr,wd));
		
		wdd.createLinkModelElements(pr,wp);
		assertFalse(wdd.existsLinkModelElements(pr,wp));
		
		wdd.createLinkModelElements(wp,wd);
		assertTrue(wdd.existsLinkModelElements(wp,wd));
		wdd.removeLinkModelElements(wp,wd);
		assertFalse(wdd.existsLinkModelElements(wp,wd));
		
		wdd.createLinkModelElements(wd,wp);
		assertTrue(wdd.existsLinkModelElements(wd,wp));
		wdd.removeLinkModelElements(wd,wp);
		assertFalse(wdd.existsLinkModelElements(wd,wp));
	}
}
