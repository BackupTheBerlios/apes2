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
		ProcessRole pr_performer1 = createProcessRole();
		ProcessRole pr_performer2 = createProcessRole();
		ProcessRole pr_assistant1 = createProcessRole();
		ProcessRole pr_assistant2 = createProcessRole();
		
		Activity a = createActivity();
		Activity a2 = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		/* Try to link elements which are not in the diagram */
		assertFalse(cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		/* Add an assistant link : pr_assistant1 -> a */
		cd.addProcessRole(pr_assistant1);
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addActivity(a);
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertTrue(cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addProcessRole(pr_assistant2);
		
		/* Add a performer link : pr_performer1 -> a */
		cd.addProcessRole(pr_performer1);
		assertFalse(cd.createLinkProcessRoleActivity(pr_performer2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		
		cd.addActivity(a2);
		assertTrue(cd.createLinkProcessRoleActivity(pr_performer1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		
		cd.addProcessRole(pr_performer2);
		assertFalse(cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_performer2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
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
		ProcessRole pr_performer1 = createProcessRole();
		ProcessRole pr_performer2 = createProcessRole();
		ProcessRole pr_assistant1 = createProcessRole();
		ProcessRole pr_assistant2 = createProcessRole();
		Activity a = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.removeLinkProcessRoleActivity(pr_performer1,a));
		
		cd.addProcessRole(pr_performer1);
		cd.addProcessRole(pr_performer2);
		cd.addProcessRole(pr_assistant1);
		cd.addProcessRole(pr_assistant2);
		assertFalse(cd.removeLinkProcessRoleActivity(pr_performer1,a));
		cd.addActivity(a);
		assertFalse(cd.removeLinkProcessRoleActivity(pr_performer1,a));
		assertFalse(cd.removeLinkProcessRoleActivity(pr_performer2,a));
		
		cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		cd.createLinkProcessRoleActivity(pr_assistant2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		
		assertTrue(cd.removeLinkProcessRoleActivity(pr_assistant1,a));
		assertTrue(cd.removeLinkProcessRoleActivity(pr_performer1,a));
		assertTrue(cd.removeLinkProcessRoleActivity(pr_assistant2,a));
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
		ProcessRole pr_performer1 = createProcessRole();
		ProcessRole pr_performer2 = createProcessRole();
		ProcessRole pr_assistant1 = createProcessRole();
		ProcessRole pr_assistant2 = createProcessRole();
		Activity a = createActivity();
		Activity a2 = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addProcessRole(pr_performer1);
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addActivity(a);
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer2,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		
		cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addActivity(a2);
		assertTrue(cd.areLinkableProcessRoleActivity(pr_performer1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_performer1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addProcessRole(pr_assistant1);
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(cd.areLinkableProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.createLinkProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableProcessRoleActivity(pr_assistant1,a2,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
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
		ProcessRole pr_performer1 = createProcessRole();
		ProcessRole pr_assistant1 = createProcessRole();
		
		Activity a = createActivity();
		FlowDiagram cd = new FlowDiagram();
		
		assertFalse(cd.existsLinkProcessRoleActivity(pr_performer1,a, 
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.existsLinkProcessRoleActivity(pr_assistant1,a, 
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addProcessRole(pr_performer1);
		cd.addProcessRole(pr_assistant1);
		assertFalse(cd.existsLinkProcessRoleActivity(pr_performer1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.addActivity(a);
		assertFalse(cd.existsLinkProcessRoleActivity(pr_performer1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.createLinkProcessRoleActivity(pr_assistant1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		assertTrue(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.existsLinkProcessRoleActivity(pr_performer1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		cd.createLinkProcessRoleActivity(pr_performer1,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		assertTrue(cd.existsLinkProcessRoleActivity(pr_performer1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.existsLinkProcessRoleActivity(pr_performer1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertTrue(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertTrue(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		
		cd.removeLinkProcessRoleActivity(pr_performer1,a);
		assertFalse(cd.existsLinkProcessRoleActivity(pr_performer1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));

		cd.removeLinkProcessRoleActivity(pr_assistant1,a);
		assertFalse(cd.existsLinkProcessRoleActivity(pr_assistant1,a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE | FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
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
		
		assertFalse(cd.createLinkModelElements(pr,a,null));
		assertFalse(cd.createLinkModelElements(pr,wp,null));
		assertFalse(cd.createLinkModelElements(wp,a,null));
		assertFalse(cd.createLinkModelElements(a,wp,null));
		
		cd.addModelElement(a);
		assertFalse(cd.createLinkModelElements(pr,a,null));
		assertFalse(cd.createLinkModelElements(wp,a,null));
		
		cd.addModelElement(wp);
		assertTrue(cd.createLinkModelElements(wp,a,null));
		assertFalse(cd.createLinkModelElements(wp,a,null));
		assertTrue(cd.createLinkModelElements(a,wp,null));
		assertFalse(cd.createLinkModelElements(a,wp,null));
		assertFalse(cd.createLinkModelElements(pr,a,null));
		
		cd.addModelElement(pr);
		assertTrue(cd.createLinkModelElements(pr,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.createLinkModelElements(a,pr,null));
		assertFalse(cd.createLinkModelElements(pr,wp,null));
		assertFalse(cd.createLinkModelElements(wp,pr,null));
		
		assertFalse(cd.createLinkModelElements(a,a,null));
		assertFalse(cd.createLinkModelElements(pr,pr,null));
		assertFalse(cd.createLinkModelElements(wp,wp,null));
		
		cd.addModelElement(pr2);
		assertFalse(cd.createLinkModelElements(pr2,wp,null));
	}
		
	public void testRemoveLinkModelElements()
	{
		FlowDiagram cd = new FlowDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		
		assertFalse(cd.removeLinkModelElements(pr,a,null));
		assertFalse(cd.removeLinkModelElements(pr,wp,null));
		assertFalse(cd.removeLinkModelElements(wp,a,null));
		assertFalse(cd.removeLinkModelElements(a,wp,null));
		
		cd.addModelElement(a);	
		cd.addModelElement(wp);
		
		assertFalse(cd.removeLinkModelElements(a,wp,null));	
		cd.createLinkModelElements(a,wp,null);
		assertTrue(cd.removeLinkModelElements(a,wp,null));
		assertFalse(cd.removeLinkModelElements(a,wp,null));
		
		assertFalse(cd.removeLinkModelElements(wp,a,null));	
		cd.createLinkModelElements(wp,a,null);
		assertTrue(cd.removeLinkModelElements(wp,a,null));
		assertFalse(cd.removeLinkModelElements(wp,a,null));	
		
		cd.addModelElement(pr);
		
		assertFalse(cd.removeLinkModelElements(pr,a,null));
		cd.createLinkModelElements(pr,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		assertTrue(cd.removeLinkModelElements(pr,a,null));
		assertFalse(cd.removeLinkModelElements(pr,a,null));
		
		assertFalse(cd.removeLinkModelElements(pr,wp,null));
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
		
		assertFalse(cd.areLinkableModelElements(pr,a,null));
		assertFalse(cd.areLinkableModelElements(pr,wp,null));
		assertFalse(cd.areLinkableModelElements(wp,a,null));
		assertFalse(cd.areLinkableModelElements(a,wp,null));
		
		cd.addModelElement(a);	
		cd.addModelElement(wp);
		
		assertFalse(cd.areLinkableModelElements(pr,a,null));
		assertFalse(cd.areLinkableModelElements(pr,wp,null));
		assertTrue(cd.areLinkableModelElements(wp,a,null));
		assertTrue(cd.areLinkableModelElements(a,wp,null));
		
		cd.addModelElement(pr);	
		
		assertTrue(cd.areLinkableModelElements(pr,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(cd.areLinkableModelElements(pr,wp,null));
		assertFalse(cd.areLinkableModelElements(a,pr,null));
		assertFalse(cd.areLinkableModelElements(wp,pr,null));
		
		cd.createLinkModelElements(pr,a,null);
		assertFalse(cd.areLinkableModelElements(pr,a,null));
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
		
		assertFalse(cd.existsLinkModelElements(pr,a,null));
		assertFalse(cd.existsLinkModelElements(pr,wp,null));
		assertFalse(cd.existsLinkModelElements(wp,a,null));
		assertFalse(cd.existsLinkModelElements(a,wp,null));
		
		assertFalse(cd.existsLinkModelElements(pr,pr,null));
		assertFalse(cd.existsLinkModelElements(a,a,null));
		assertFalse(cd.existsLinkModelElements(wp,wp,null));
		
		cd.createLinkModelElements(pr,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		assertTrue(cd.existsLinkModelElements(pr,a,new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		cd.removeLinkModelElements(pr,a,null);
		assertFalse(cd.existsLinkModelElements(pr,a,null));
		
		cd.createLinkModelElements(pr,wp,null);
		assertFalse(cd.existsLinkModelElements(pr,wp,null));
		/*cd.removeLinkModelElements(pr,wp);
		assertFalse(cd.existsLinkModelElements(pr,wp));*/
		
		cd.createLinkModelElements(wp,a,null);
		assertTrue(cd.existsLinkModelElements(wp,a,null));
		cd.removeLinkModelElements(wp,a,null);
		assertFalse(cd.existsLinkModelElements(wp,a,null));
		
		cd.createLinkModelElements(a,wp,null);
		assertTrue(cd.existsLinkModelElements(a,wp,null));
		cd.removeLinkModelElements(a,wp,null);
		assertFalse(cd.existsLinkModelElements(a,wp,null));
	}
}
