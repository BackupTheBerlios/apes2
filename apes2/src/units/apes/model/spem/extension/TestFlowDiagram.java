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

import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestFlowDiagram extends TestCase
{
	private ProcessRole createProcessRole()
	{
		return new ProcessRole();
	}
	
	private Activity createActivity()
	{
		return new Activity();
	}
	
	private WorkProduct createWorkProduct()
	{
		return new WorkProduct();
	}
	
	public void testAddProcessRole()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		
		assertTrue(cd.modelElementCount()==0);
		
		cd.addProcessRole(pr);
		
		assertTrue(cd.modelElementCount()==1);
		
		cd.addProcessRole(pr);
		assertTrue(cd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			pr = createProcessRole();
			assertTrue(cd.modelElementCount()==i);
			cd.addProcessRole(pr);
		}
	}
	
	public void testAddActivity()
	{
		FlowDiagram cd = new FlowDiagram();
		Activity a = createActivity();
		
		assertTrue(cd.modelElementCount()==0);
		
		cd.addActivity(a);
		
		assertTrue(cd.modelElementCount()==1);
		
		cd.addActivity(a);
		assertTrue(cd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			a = createActivity();
			assertTrue(cd.modelElementCount()==i);
			cd.addActivity(a);
		}
	}
	
	public void testAddWorkProduct()
	{
		FlowDiagram cd = new FlowDiagram();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(cd.modelElementCount()==0);
		
		cd.addWorkProduct(wp);
		
		assertTrue(cd.modelElementCount()==1);
		
		cd.addWorkProduct(wp);
		assertTrue(cd.modelElementCount()==1);
		
		for(int i=1; i<100; i++)
		{
			wp = createWorkProduct();
			assertTrue(cd.modelElementCount()==i);
			cd.addWorkProduct(wp);
		}
	}
	
	public void testRemoveModelElement()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		Activity a2 = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		cd.addProcessRole(pr);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(pr2);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(pr);
		assertTrue(cd.modelElementCount()==0);
		
		
		cd.addActivity(a);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(a2);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(a);
		assertTrue(cd.modelElementCount()==0);
		
		
		cd.addWorkProduct(wp);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(wp2);
		assertTrue(cd.modelElementCount()==1);
		
		cd.removeModelElement(wp);
		assertTrue(cd.modelElementCount()==0);
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			cd.addProcessRole(pr);
		}
		
		cd.addProcessRole(pr2);
		
		for(int i=0; i<100; i++)
		{
			a = createActivity();
			cd.addActivity(a);
		}
		
		assertTrue(cd.modelElementCount()==201);
		cd.removeModelElement(pr2);
		assertTrue(cd.modelElementCount()==200);
	}
	
	public void testContainsModelElement()
	{
		ProcessRole pr;
		ProcessRole pr2 = createProcessRole();
		Activity a;
		Activity a2 = createActivity();
		WorkProduct wp;
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			cd.addProcessRole(pr);
		}
		
		assertFalse(cd.containsModelElement(pr2));
		cd.addProcessRole(pr2);
		assertTrue(cd.containsModelElement(pr2));
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			cd.addProcessRole(pr);
		}
		
		assertTrue(cd.containsModelElement(pr2));
		
		
		for(int i=0; i<100; i++)
		{
			a = createActivity();
			cd.addActivity(a);
		}
		
		assertFalse(cd.containsModelElement(a2));
		cd.addActivity(a2);
		assertTrue(cd.containsModelElement(a2));
		
		for(int i=0; i<100; i++)
		{
			a = createActivity();
			cd.addActivity(a);
		}
		
		assertTrue(cd.containsModelElement(a2));
		
		
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
		ProcessRole pr;
		ProcessRole pr2 = createProcessRole();
		FlowDiagram cd = new FlowDiagram();
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			cd.addProcessRole(pr);
		}
		
		cd.addProcessRole(pr2);
		
		for(int i=0; i<100; i++)
		{
			pr = createProcessRole();
			cd.addProcessRole(pr);
		}
		
		assertEquals(cd.getModelElement(100), pr2);
		assertEquals(cd.getModelElement(-1), null);
		assertEquals(cd.getModelElement(201), null);
	}
	
	public void testModelElementCount()
	{
		ProcessRole pr = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertEquals(cd.modelElementCount(),0);
		
		cd.addProcessRole(pr);
		assertEquals(cd.modelElementCount(),1);
		
		cd.addProcessRole(pr);
		assertEquals(cd.modelElementCount(),1);
		
		cd.addActivity(a);
		assertEquals(cd.modelElementCount(),2);
		
		cd.addActivity(a);
		assertEquals(cd.modelElementCount(),2);
		
		cd.addWorkProduct(wp);
		assertEquals(cd.modelElementCount(),3);
		
		cd.addWorkProduct(wp);
		assertEquals(cd.modelElementCount(),3);
		
		cd.removeModelElement(pr);
		assertEquals(cd.modelElementCount(),2);
		
		cd.removeModelElement(pr);
		assertEquals(cd.modelElementCount(),2);
		
		for(int i=0;i<100;i++)
		{
			pr = createProcessRole();
			cd.addProcessRole(pr);
		}
		assertEquals(cd.modelElementCount(),102);
	}
	
	public void testCreateLinkProcessRoleActivity()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		Activity a2 = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.createLinkProcessRoleActivity(pr,a));
		
		cd.addProcessRole(pr);
		assertFalse(cd.createLinkProcessRoleActivity(pr,a));
		
		cd.addActivity(a);
		assertFalse(cd.createLinkProcessRoleActivity(pr2,a));
		assertTrue(cd.createLinkProcessRoleActivity(pr,a));
		
		cd.addActivity(a2);
		assertTrue(cd.createLinkProcessRoleActivity(pr,a2));
		
		assertFalse(cd.createLinkProcessRoleActivity(pr,a));
	}
	
	public void testCreateLinkWorkProductActivityInput()
	{
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.createLinkWorkProductActivityInput(wp,a));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.createLinkWorkProductActivityInput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.createLinkWorkProductActivityInput(wp2,a));
		assertTrue(cd.createLinkWorkProductActivityInput(wp,a));
		
		assertEquals(a.getInputCount(),1);
		assertEquals(wp.getOutputCount(),1);

		assertFalse(cd.createLinkWorkProductActivityInput(wp,a));

		cd.addWorkProduct(wp2);
		assertTrue(cd.createLinkWorkProductActivityInput(wp2,a));

		assertEquals(a.getInputCount(),2);
		assertEquals(wp.getOutputCount(),1);
		assertEquals(wp2.getOutputCount(),1);
	}
	
	public void testCreateLinkWorkProductActivityOutput()
	{
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.createLinkWorkProductActivityOutput(wp,a));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.createLinkWorkProductActivityOutput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.createLinkWorkProductActivityOutput(wp2,a));
		assertTrue(cd.createLinkWorkProductActivityOutput(wp,a));
		
		assertEquals(wp.getInputCount(),1);
		assertEquals(a.getOutputCount(),1);

		assertFalse(cd.createLinkWorkProductActivityOutput(wp,a));
		
		Activity a2 = createActivity();
		cd.addActivity(a2);
		assertTrue(cd.createLinkWorkProductActivityOutput(wp,a2));
		
		assertEquals(wp.getInputCount(),2);
		assertEquals(a2.getOutputCount(),1);
		assertEquals(a.getOutputCount(),1);
	}
	
	public void testRemoveLinkProcessRoleActivity()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.removeLinkProcessRoleActivity(pr,a));
		
		cd.addProcessRole(pr);
		assertFalse(cd.removeLinkProcessRoleActivity(pr,a));
		cd.addActivity(a);
		assertFalse(cd.removeLinkProcessRoleActivity(pr,a));
		assertFalse(cd.removeLinkProcessRoleActivity(pr2,a));
		
		cd.createLinkProcessRoleActivity(pr,a);
		
		assertTrue(cd.removeLinkProcessRoleActivity(pr,a));
		assertFalse(cd.removeLinkProcessRoleActivity(pr,a));
	}
	
	/*public void testRemoveLinkProcessRoleWorkProduct()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		ClassDiagram cd = new ClassDiagram();
		
		assertFalse(cd.removeLinkProcessRoleWorkProduct(pr,wp));
		
		cd.addProcessRole(pr);
		assertFalse(cd.removeLinkProcessRoleWorkProduct(pr,wp));
		cd.addWorkProduct(wp);
		assertFalse(cd.removeLinkProcessRoleWorkProduct(pr,wp));
		assertFalse(cd.removeLinkProcessRoleWorkProduct(pr2,wp));
		
		cd.createLinkProcessRoleWorkProduct(pr,wp);
		
		assertTrue(cd.removeLinkProcessRoleWorkProduct(pr,wp));
		assertFalse(cd.removeLinkProcessRoleWorkProduct(pr,wp));
	}*/
	
	public void testRemoveLinkWorkProductActivityInput()
	{
		Activity a = createActivity();
		Activity a2 = createActivity();
		WorkProduct wp = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.removeLinkWorkProductActivityInput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.removeLinkWorkProductActivityInput(wp,a));
		cd.addWorkProduct(wp);
		assertFalse(cd.removeLinkWorkProductActivityInput(wp,a));
		assertFalse(cd.removeLinkWorkProductActivityInput(wp,a2));
		
		cd.createLinkWorkProductActivityInput(wp,a);
		
		assertEquals(a.getInputCount(),1);
		assertEquals(wp.getOutputCount(),1);

		assertTrue(cd.removeLinkWorkProductActivityInput(wp,a));

		assertEquals(a.getInputCount(),0);
		assertEquals(wp.getOutputCount(),0);

		assertFalse(cd.removeLinkWorkProductActivityInput(wp,a));
	}
	
	public void testRemoveLinkWorkProductActivityOutput()
	{
		Activity a = createActivity();
		Activity a2 = createActivity();
		WorkProduct wp = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.removeLinkWorkProductActivityOutput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.removeLinkWorkProductActivityOutput(wp,a));
		cd.addWorkProduct(wp);
		assertFalse(cd.removeLinkWorkProductActivityOutput(wp,a));
		assertFalse(cd.removeLinkWorkProductActivityOutput(wp,a2));
		
		cd.createLinkWorkProductActivityOutput(wp,a);
		
		assertEquals(wp.getInputCount(),1);
		assertEquals(a.getOutputCount(),1);

		assertTrue(cd.removeLinkWorkProductActivityOutput(wp,a));

		assertEquals(wp.getInputCount(),0);
		assertEquals(a.getOutputCount(),0);

		assertFalse(cd.removeLinkWorkProductActivityOutput(wp,a));
	}
	
	public void testAreLinkableProcessRoleActivity()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		Activity a2 = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.areLinkableProcessRoleActivity(pr,a));
		
		cd.addProcessRole(pr);
		assertFalse(cd.areLinkableProcessRoleActivity(pr,a));
		
		cd.addActivity(a);
		assertFalse(cd.areLinkableProcessRoleActivity(pr2,a));
		assertTrue(cd.areLinkableProcessRoleActivity(pr,a));
		
		cd.createLinkProcessRoleActivity(pr,a);
		assertFalse(cd.areLinkableProcessRoleActivity(pr,a));
		
		cd.addActivity(a2);
		assertTrue(cd.areLinkableProcessRoleActivity(pr,a2));
	}
	
	/*public void testAreLinkableProcessRoleWorkProduct()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ClassDiagram cd = new ClassDiagram();
		
		assertFalse(cd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		cd.addProcessRole(pr);
		assertFalse(cd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.areLinkableProcessRoleWorkProduct(pr2,wp));
		assertTrue(cd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		cd.createLinkProcessRoleWorkProduct(pr,wp);
		assertFalse(cd.areLinkableProcessRoleWorkProduct(pr,wp));
		
		cd.addWorkProduct(wp2);
		assertTrue(cd.areLinkableProcessRoleWorkProduct(pr,wp2));
	}*/
	
	public void testAreLinkableWorkProductActivityInput()
	{
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.areLinkableWorkProductActivityInput(wp,a));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.areLinkableWorkProductActivityInput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.areLinkableWorkProductActivityInput(wp2,a));
		assertTrue(cd.areLinkableWorkProductActivityInput(wp,a));
		
		cd.createLinkWorkProductActivityInput(wp,a);
		assertFalse(cd.areLinkableWorkProductActivityInput(wp,a));
	}
	
	public void testAreLinkableWorkProductActivityOutput()
	{
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.areLinkableWorkProductActivityOutput(wp,a));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.areLinkableWorkProductActivityOutput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.areLinkableWorkProductActivityOutput(wp2,a));
		assertTrue(cd.areLinkableWorkProductActivityOutput(wp,a));
		
		cd.createLinkWorkProductActivityOutput(wp,a);
		assertFalse(cd.areLinkableWorkProductActivityOutput(wp,a));
	}
	
	public void testExistsLinkProcessRoleActivity()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.existsLinkProcessRoleActivity(pr,a));
		
		cd.addProcessRole(pr);
		assertFalse(cd.existsLinkProcessRoleActivity(pr,a));
		
		cd.addActivity(a);
		assertFalse(cd.existsLinkProcessRoleActivity(pr,a));
		
		cd.createLinkProcessRoleActivity(pr,a);
		assertTrue(cd.existsLinkProcessRoleActivity(pr,a));
		assertFalse(cd.existsLinkProcessRoleActivity(pr2,a));
		
		cd.removeLinkProcessRoleActivity(pr,a);
		assertFalse(cd.existsLinkProcessRoleActivity(pr,a));
	}
	
	/*public void testExistsLinkProcessRoleWorkProduct()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		ClassDiagram cd = new ClassDiagram();
		
		assertFalse(cd.existsLinkProcessRoleWorkProduct(pr,wp));
		
		cd.addProcessRole(pr);
		assertFalse(cd.existsLinkProcessRoleWorkProduct(pr,wp));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.existsLinkProcessRoleWorkProduct(pr,wp));
		
		cd.createLinkProcessRoleWorkProduct(pr,wp);
		assertTrue(cd.existsLinkProcessRoleWorkProduct(pr,wp));
		assertFalse(cd.existsLinkProcessRoleWorkProduct(pr2,wp));
		
		cd.removeLinkProcessRoleWorkProduct(pr,wp);
		assertFalse(cd.existsLinkProcessRoleWorkProduct(pr,wp));
	}*/
	
	public void testExistsLinkWorkProductActivityInput()
	{
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.existsLinkWorkProductActivityInput(wp,a));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.existsLinkWorkProductActivityInput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.existsLinkWorkProductActivityInput(wp,a));
		
		cd.createLinkWorkProductActivityInput(wp,a);
		assertTrue(cd.existsLinkWorkProductActivityInput(wp,a));
		assertFalse(cd.areLinkableWorkProductActivityInput(wp2,a));
		
		cd.removeLinkWorkProductActivityInput(wp,a);
		assertFalse(cd.existsLinkWorkProductActivityInput(wp,a));
	}
	
	public void testExistsLinkWorkProductActivityOutput()
	{
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.existsLinkWorkProductActivityOutput(wp,a));
		
		cd.addWorkProduct(wp);
		assertFalse(cd.existsLinkWorkProductActivityOutput(wp,a));
		
		cd.addActivity(a);
		assertFalse(cd.existsLinkWorkProductActivityOutput(wp,a));
		
		cd.createLinkWorkProductActivityOutput(wp,a);
		assertTrue(cd.existsLinkWorkProductActivityOutput(wp,a));
		assertFalse(cd.areLinkableWorkProductActivityOutput(wp2,a));
		
		cd.removeLinkWorkProductActivityOutput(wp,a);
		assertFalse(cd.existsLinkWorkProductActivityOutput(wp,a));
	}
	
	public void testAddModelElement()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		
		assertTrue(cd.modelElementCount()==0);
		assertTrue(cd.addModelElement(pr));
		assertTrue(cd.modelElementCount()==1);
		assertTrue(cd.containsModelElement(pr));
		
		assertTrue(cd.modelElementCount()==1);
		assertTrue(cd.addModelElement(a));
		assertTrue(cd.modelElementCount()==2);
		assertTrue(cd.containsModelElement(a));
		
		assertTrue(cd.modelElementCount()==2);
		assertTrue(cd.addModelElement(wp));
		assertTrue(cd.modelElementCount()==3);
		assertTrue(cd.containsModelElement(wp));	
	}
	
	public void testCreateLinkModelElements()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(cd.createLinkModelElements(pr,a));
		assertFalse(cd.createLinkModelElements(pr,wp));
		assertFalse(cd.createLinkModelElements(wp,a));
		assertFalse(cd.createLinkModelElements(a,wp));
		
		cd.addModelElement(a);
		assertFalse(cd.createLinkModelElements(pr,a));
		assertFalse(cd.createLinkModelElements(wp,a));
		
		cd.addModelElement(wp);
		assertTrue(cd.createLinkModelElements(wp,a));
		assertFalse(cd.createLinkModelElements(wp,a));
		assertTrue(cd.createLinkModelElements(a,wp));
		assertFalse(cd.createLinkModelElements(a,wp));
		assertFalse(cd.createLinkModelElements(pr,a));
		
		cd.addModelElement(pr);
		assertTrue(cd.createLinkModelElements(pr,a));
		assertFalse(cd.createLinkModelElements(a,pr));
		assertFalse(cd.createLinkModelElements(pr,wp));
		assertFalse(cd.createLinkModelElements(wp,pr));
		
		assertFalse(cd.createLinkModelElements(a,a));
		assertFalse(cd.createLinkModelElements(pr,pr));
		assertFalse(cd.createLinkModelElements(wp,wp));
		
		cd.addModelElement(pr2);
		assertFalse(cd.createLinkModelElements(pr2,wp));
	}
		
	public void testRemoveLinkModelElements()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(cd.removeLinkModelElements(pr,a));
		assertFalse(cd.removeLinkModelElements(pr,wp));
		assertFalse(cd.removeLinkModelElements(wp,a));
		assertFalse(cd.removeLinkModelElements(a,wp));
		
		cd.addModelElement(a);	
		cd.addModelElement(wp);
		
		assertFalse(cd.removeLinkModelElements(a,wp));	
		cd.createLinkModelElements(a,wp);
		assertTrue(cd.removeLinkModelElements(a,wp));
		assertFalse(cd.removeLinkModelElements(a,wp));
		
		assertFalse(cd.removeLinkModelElements(wp,a));	
		cd.createLinkModelElements(wp,a);
		assertTrue(cd.removeLinkModelElements(wp,a));
		assertFalse(cd.removeLinkModelElements(wp,a));	
		
		cd.addModelElement(pr);
		
		assertFalse(cd.removeLinkModelElements(pr,a));
		cd.createLinkModelElements(pr,a);
		assertTrue(cd.removeLinkModelElements(pr,a));
		assertFalse(cd.removeLinkModelElements(pr,a));
		
		assertFalse(cd.removeLinkModelElements(pr,wp));
		/*cd.createLinkModelElements(pr,wp);
		assertTrue(cd.removeLinkModelElements(pr,wp));
		assertFalse(cd.removeLinkModelElements(pr,wp));*/
	}
	
	public void testAreLinkableModelElements()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(cd.areLinkableModelElements(pr,a));
		assertFalse(cd.areLinkableModelElements(pr,wp));
		assertFalse(cd.areLinkableModelElements(wp,a));
		assertFalse(cd.areLinkableModelElements(a,wp));
		
		cd.addModelElement(a);	
		cd.addModelElement(wp);
		
		assertFalse(cd.areLinkableModelElements(pr,a));
		assertFalse(cd.areLinkableModelElements(pr,wp));
		assertTrue(cd.areLinkableModelElements(wp,a));
		assertTrue(cd.areLinkableModelElements(a,wp));
		
		cd.addModelElement(pr);	
		
		assertTrue(cd.areLinkableModelElements(pr,a));
		assertFalse(cd.areLinkableModelElements(pr,wp));
		assertFalse(cd.areLinkableModelElements(a,pr));
		assertFalse(cd.areLinkableModelElements(wp,pr));
		
		cd.createLinkModelElements(pr,a);
		assertFalse(cd.areLinkableModelElements(pr,a));
	}
	
	public void testExistsLinkModelElements()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		
		cd.addModelElement(a);	
		cd.addModelElement(wp);
		cd.addModelElement(pr);	
		
		assertFalse(cd.existsLinkModelElements(pr,a));
		assertFalse(cd.existsLinkModelElements(pr,wp));
		assertFalse(cd.existsLinkModelElements(wp,a));
		assertFalse(cd.existsLinkModelElements(a,wp));
		
		assertFalse(cd.existsLinkModelElements(pr,pr));
		assertFalse(cd.existsLinkModelElements(a,a));
		assertFalse(cd.existsLinkModelElements(wp,wp));
		
		cd.createLinkModelElements(pr,a);
		assertTrue(cd.existsLinkModelElements(pr,a));
		cd.removeLinkModelElements(pr,a);
		assertFalse(cd.existsLinkModelElements(pr,a));
		
		cd.createLinkModelElements(pr,wp);
		assertFalse(cd.existsLinkModelElements(pr,wp));
		/*cd.removeLinkModelElements(pr,wp);
		assertFalse(cd.existsLinkModelElements(pr,wp));*/
		
		cd.createLinkModelElements(wp,a);
		assertTrue(cd.existsLinkModelElements(wp,a));
		cd.removeLinkModelElements(wp,a);
		assertFalse(cd.existsLinkModelElements(wp,a));
		
		cd.createLinkModelElements(a,wp);
		assertTrue(cd.existsLinkModelElements(a,wp));
		cd.removeLinkModelElements(a,wp);
		assertFalse(cd.existsLinkModelElements(a,wp));
	}
}
