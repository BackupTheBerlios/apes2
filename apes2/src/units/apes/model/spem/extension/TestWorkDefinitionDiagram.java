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
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;

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
	
	private StateMachine createWorkProductState()
	{
		return new StateMachine();
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
		StateMachine sm = createWorkProductState();
		
		assertTrue(wdd.modelElementCount()==0);
		
		wdd.addWorkProduct(wp);
		
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.addWorkProduct(wp);
		assertTrue(wdd.modelElementCount()==1);
		
		//
		//wp.addBehavior(sm);
		//wdd.addWorkProductState(sm);
		//assertTrue(wdd.modelElementCount()==1);
		//
		
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
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
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
		
		
		wdd.addWorkProductState(sm);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(sm2);
		assertTrue(wdd.modelElementCount()==1);
		
		wdd.removeModelElement(sm);
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
		StateMachine sm;
		StateMachine sm2 = createWorkProductState();
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
		
		
		for(int i=0; i<100; i++)
		{
			sm = createWorkProductState();
			wdd.addWorkProductState(sm);
		}
		
		assertFalse(wdd.containsModelElement(sm2));
		wdd.addWorkProductState(sm2);
		assertTrue(wdd.containsModelElement(sm2));
		
		for(int i=0; i<100; i++)
		{
			sm = createWorkProductState();
			wdd.addWorkProductState(sm);
		}
		
		assertTrue(wdd.containsModelElement(sm2));
		
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
		StateMachine sm = createWorkProductState();
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
		
		wdd.addWorkProductState(sm);
		assertEquals(wdd.modelElementCount(),4);
		
		wdd.addWorkProductState(sm);
		assertEquals(wdd.modelElementCount(),4);
		
		wdd.removeModelElement(pr);
		assertEquals(wdd.modelElementCount(),3);
		
		wdd.removeModelElement(pr);
		assertEquals(wdd.modelElementCount(),3);
		
		for(int i=0;i<100;i++)
		{
			pr = createProcessRole();
			wdd.addProcessRole(pr);
		}
		assertEquals(wdd.modelElementCount(),103);
	}
	
	public void testCreateLinkProcessRoleWorkDefinition()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		ProcessRole pr3 = createProcessRole();
		ProcessRole pr4 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		ApesWorkDefinition awd = new ApesWorkDefinition();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		FlowDiagram fd = new FlowDiagram();
		Activity a1 = new Activity();
		Activity a2 = new Activity();
		Activity a3 = new Activity();
		
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addProcessRole(pr);
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr2,wd));
		assertTrue(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.createLinkProcessRoleWorkDefinition(pr,wd2));
		
		assertFalse(wdd.createLinkProcessRoleWorkDefinition(pr,wd));
		
		awd.addFlowDiagram(fd);
		awd.addSubWork(a1);
		awd.addSubWork(a2);
		awd.addSubWork(a3);
		
		fd.addActivity(a1);
		fd.addActivity(a2);
		fd.addActivity(a3);
		fd.addProcessRole(pr3);
		fd.addProcessRole(pr4);
		fd.createLinkModelElements(pr4,a2);
		
		/* wdd : pr  -> wd
		 * wdd : pr  -> wd2
		 * wdd : pr3 -> awd
		 * fd  : pr4 -> a2
		 */
		wdd.addWorkDefinition(awd);
		wdd.addProcessRole(pr3);
		assertTrue(wdd.createLinkProcessRoleWorkDefinition(pr3,awd));
		assertTrue(a1.getOwner().equals(pr3));
		assertTrue(a2.getOwner().equals(pr4));
		assertTrue(a3.getOwner().equals(pr3));
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
	
	
	public void testCreateLinkWorkDefinitionWorkProductState()
	{
		WorkDefinition wd = createWorkDefinition();
		
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.createLinkWorkDefinitionWorkProductState(wd,sm));
		
		wdd.addWorkProductState(sm);
		assertFalse(wdd.createLinkWorkDefinitionWorkProductState(wd,sm));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.createLinkWorkDefinitionWorkProductState(wd,sm2));
		assertFalse(wdd.createLinkWorkDefinitionWorkProductState(wd,sm));
		
		WorkProduct wp = createWorkProduct();
		sm.setContext(wp);
		assertTrue(wdd.createLinkWorkDefinitionWorkProductState(wd,sm));
		
		
		assertEquals(wdd.getTransitionCount(),2);
		
		
		assertFalse(wdd.createLinkWorkDefinitionWorkProductState(wd,sm));
		
		WorkDefinition wd2 = createWorkDefinition();
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.createLinkWorkDefinitionWorkProductState(wd2,sm));
		
		
		assertEquals(wdd.getTransitionCount(),4);
		
		WorkDefinition wd3 = createWorkDefinition();
		wdd.addWorkDefinition(wd3);
		wdd.createLinkWorkProductStateWorkDefinition(sm,wd3);
		
		
		assertFalse(wdd.createLinkWorkDefinitionWorkProductState(wd3,sm));
		
	}
	
	public void testCreateLinkWorkProductStateWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.createLinkWorkProductStateWorkDefinition(sm,wd));
		
		wdd.addWorkProductState(sm);
		assertFalse(wdd.createLinkWorkProductStateWorkDefinition(sm,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.createLinkWorkProductStateWorkDefinition(sm2,wd));
		assertFalse(wdd.createLinkWorkProductStateWorkDefinition(sm,wd));
		
		WorkProduct wp = createWorkProduct();
		sm.setContext(wp);
		assertTrue(wdd.createLinkWorkProductStateWorkDefinition(sm,wd));
		
		assertEquals(wdd.getTransitionCount(),2);
		
		assertFalse(wdd.createLinkWorkProductStateWorkDefinition(sm,wd));
		
		WorkDefinition wd2 = createWorkDefinition();
		wdd.addWorkDefinition(wd2);
		assertTrue(wdd.createLinkWorkProductStateWorkDefinition(sm,wd2));
		
		assertEquals(wdd.getTransitionCount(),4);
		
		WorkDefinition wd3 = createWorkDefinition();
		wdd.addWorkDefinition(wd3);
		wdd.createLinkWorkDefinitionWorkProductState(wd3,sm);
		
		assertTrue(wdd.existsLinkWorkDefinitionWorkProductState(wd3,sm));
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd3));
		assertFalse(wdd.createLinkWorkProductStateWorkDefinition(sm,wd3));
	}
	
	public void testRemoveLinkProcessRoleWorkDefinition()
	{
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		ProcessRole pr3 = createProcessRole();
		ProcessRole pr4 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ApesWorkDefinition awd = new ApesWorkDefinition();
		FlowDiagram fd = new FlowDiagram();
		Activity a1 = new Activity();
		Activity a2 = new Activity();
		Activity a3 = new Activity();
		
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		
		wdd.addProcessRole(pr);
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr2,wd));
		
		wdd.createLinkProcessRoleWorkDefinition(pr,wd);
		
		assertTrue(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr,wd));
		
		awd.addFlowDiagram(fd);
		awd.addSubWork(a1);
		awd.addSubWork(a2);
		awd.addSubWork(a3);
		
		fd.addActivity(a1);
		fd.addActivity(a2);
		fd.addActivity(a3);
		fd.addProcessRole(pr3);
		fd.addProcessRole(pr4);
		fd.createLinkModelElements(pr4,a2);
		

		wdd.addWorkDefinition(awd);
		wdd.addProcessRole(pr3);
		assertFalse(wdd.removeLinkProcessRoleWorkDefinition(pr3,awd));
		wdd.createLinkProcessRoleWorkDefinition(pr3,awd);
		assertTrue(a1.getOwner().equals(pr3));
		assertTrue(a2.getOwner().equals(pr4));
		assertTrue(a3.getOwner().equals(pr3));
		
		assertTrue(wdd.removeLinkProcessRoleWorkDefinition(pr3,awd));
		assertTrue(a1.getOwner() == null);
		assertTrue(a2.getOwner().equals(pr4));
		assertTrue(a3.getOwner() == null);
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
	
	
	public void testRemoveLinkWorkDefinitionWorkProductState()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		StateMachine sm = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.removeLinkWorkDefinitionWorkProductState(wd,sm));
		
		sm.setContext(wp);
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.removeLinkWorkDefinitionWorkProductState(wd,sm));
		wdd.addWorkProductState(sm);
		assertFalse(wdd.removeLinkWorkDefinitionWorkProductState(wd,sm));
		assertFalse(wdd.removeLinkWorkDefinitionWorkProductState(wd2,sm));
		
		
		wdd.createLinkWorkDefinitionWorkProductState(wd,sm);
		
		assertEquals(wdd.getTransitionCount(),2);

		assertTrue(wdd.removeLinkWorkDefinitionWorkProductState(wd,sm));
		
		assertEquals(wdd.getTransitionCount(),0);

		assertFalse(wdd.removeLinkWorkDefinitionWorkProductState(wd,sm));
	}
	
	public void testRemoveLinkWorkProductStateWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkDefinition wd2 = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		StateMachine sm = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.removeLinkWorkProductStateWorkDefinition(sm,wd));
		
		sm.setContext(wp);
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.removeLinkWorkProductStateWorkDefinition(sm,wd));
		wdd.addWorkProductState(sm);
		assertFalse(wdd.removeLinkWorkProductStateWorkDefinition(sm,wd));
		assertFalse(wdd.removeLinkWorkProductStateWorkDefinition(sm,wd2));
		
		
		wdd.createLinkWorkProductStateWorkDefinition(sm,wd);
		
		assertEquals(wdd.getTransitionCount(),2);

		assertTrue(wdd.removeLinkWorkProductStateWorkDefinition(sm,wd));
		
		assertEquals(wdd.getTransitionCount(),0);

		assertFalse(wdd.removeLinkWorkProductStateWorkDefinition(sm,wd));
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
	
	public void testAreLinkableWorkDefinitionWorkProductState()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm));
		
		sm.setContext(wp);
		sm2.setContext(wp2);
		wdd.addWorkProductState(sm);
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm2));
		assertTrue(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm));
		
		wdd.createLinkWorkDefinitionWorkProductState(wd,sm);
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm));
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd));
	}
	
	public void testAreLinkableWorkProductStateWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd));
		
		sm.setContext(wp);
		sm2.setContext(wp2);
		wdd.addWorkProductState(sm);
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm2,wd));
		assertTrue(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd));
		
		wdd.createLinkWorkProductStateWorkDefinition(sm,wd);
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd));
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm));
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
	
	public void testExistsLinkWorkDefinitionWorkProductState()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.existsLinkWorkDefinitionWorkProductState(wd,sm));
		
		sm.setContext(wp);
		sm2.setContext(wp2);
		wdd.addWorkProductState(sm);
		assertFalse(wdd.existsLinkWorkDefinitionWorkProductState(wd,sm));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.existsLinkWorkDefinitionWorkProductState(wd,sm));
		
		wdd.createLinkWorkDefinitionWorkProductState(wd,sm);
		assertTrue(wdd.existsLinkWorkDefinitionWorkProductState(wd,sm));
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm2));
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm,wd));
		
		wdd.removeLinkWorkDefinitionWorkProductState(wd,sm);
		assertFalse(wdd.existsLinkWorkDefinitionWorkProductState(wd,sm));
	}
	
	public void testExistsLinkWorkProductStateWorkDefinition()
	{
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		StateMachine sm2 = createWorkProductState();
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		
		assertFalse(wdd.existsLinkWorkProductStateWorkDefinition(sm,wd));
		
		sm.setContext(wp);
		sm2.setContext(wp2);
		wdd.addWorkProductState(sm);
		assertFalse(wdd.existsLinkWorkProductStateWorkDefinition(sm,wd));
		
		wdd.addWorkDefinition(wd);
		assertFalse(wdd.existsLinkWorkProductStateWorkDefinition(sm,wd));
		
		wdd.createLinkWorkProductStateWorkDefinition(sm,wd);
		assertTrue(wdd.existsLinkWorkProductStateWorkDefinition(sm,wd));
		assertFalse(wdd.areLinkableWorkProductStateWorkDefinition(sm2,wd));
		assertFalse(wdd.areLinkableWorkDefinitionWorkProductState(wd,sm));
		
		wdd.removeLinkWorkProductStateWorkDefinition(sm,wd);
		assertFalse(wdd.existsLinkWorkProductStateWorkDefinition(sm,wd));
		
	}
	
	public void testAddModelElement()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		StateMachine sm = createWorkProductState();
		
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
		
		assertTrue(wdd.modelElementCount()==3);
		assertTrue(wdd.addModelElement(sm));
		assertTrue(wdd.modelElementCount()==4);
		assertTrue(wdd.containsModelElement(sm));	
	}
	
	public void testCreateLinkModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		ProcessRole pr2 = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		
		sm.setContext(wp2);
		assertFalse(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(pr,wp));
		assertFalse(wdd.createLinkModelElements(pr,sm));
		assertFalse(wdd.createLinkModelElements(wp,wd));
		assertFalse(wdd.createLinkModelElements(wp,sm));
		assertFalse(wdd.createLinkModelElements(wd,wp));
		assertFalse(wdd.createLinkModelElements(sm,wp));
		assertFalse(wdd.createLinkModelElements(sm,wd));
		assertFalse(wdd.createLinkModelElements(wd,sm));
		
		
		wdd.addModelElement(wd);
		assertFalse(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(wp,wd));
		assertFalse(wdd.createLinkModelElements(sm,wd));
		assertFalse(wdd.createLinkModelElements(wd,pr));
		assertFalse(wdd.createLinkModelElements(wd,wp));
		assertFalse(wdd.createLinkModelElements(wd,sm));
		
		wdd.addModelElement(wp);
		assertTrue(wdd.createLinkModelElements(wp,wd));
		assertFalse(wdd.createLinkModelElements(wp,wd));
		assertTrue(wdd.createLinkModelElements(wd,wp));
		assertFalse(wdd.createLinkModelElements(wd,wp));
		assertFalse(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(sm,wd));
		assertFalse(wdd.createLinkModelElements(wd,sm));
		
		wdd.addModelElement(pr);
		assertTrue(wdd.createLinkModelElements(pr,wd));
		assertFalse(wdd.createLinkModelElements(wd,pr));
		assertFalse(wdd.createLinkModelElements(pr,wp));
		assertFalse(wdd.createLinkModelElements(wp,pr));
		assertFalse(wdd.createLinkModelElements(pr,sm));
		assertFalse(wdd.createLinkModelElements(sm,pr));
		
		wdd.addModelElement(sm);
		assertTrue(wdd.createLinkModelElements(sm,wd));
		assertFalse(wdd.createLinkModelElements(wd,sm));
		assertFalse(wdd.createLinkModelElements(sm,wd));
		assertFalse(wdd.createLinkModelElements(sm,wp));
		assertFalse(wdd.createLinkModelElements(wp,sm));
		assertFalse(wdd.createLinkModelElements(sm,pr));	
		assertFalse(wdd.createLinkModelElements(pr,sm));
		
		assertFalse(wdd.createLinkModelElements(wd,wd));
		assertFalse(wdd.createLinkModelElements(pr,pr));
		assertFalse(wdd.createLinkModelElements(wp,wp));
		assertFalse(wdd.createLinkModelElements(sm,sm));
		
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
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		
		sm.setContext(wp2);
		assertFalse(wdd.removeLinkModelElements(pr,wd));
		assertFalse(wdd.removeLinkModelElements(pr,wp));
		assertFalse(wdd.removeLinkModelElements(pr,sm));
		assertFalse(wdd.removeLinkModelElements(wp,wd));
		assertFalse(wdd.removeLinkModelElements(sm,wd));
		assertFalse(wdd.removeLinkModelElements(wd,wp));
		assertFalse(wdd.removeLinkModelElements(wd,sm));
		
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
		
		wdd.addModelElement(sm);
		
		assertFalse(wdd.removeLinkModelElements(wd,sm));	
		wdd.createLinkModelElements(wd,sm);
		assertTrue(wdd.removeLinkModelElements(wd,sm));
		assertFalse(wdd.removeLinkModelElements(wd,sm));
		
		assertFalse(wdd.removeLinkModelElements(sm,wd));	
		wdd.createLinkModelElements(sm,wd);
		assertTrue(wdd.removeLinkModelElements(sm,wd));
		assertFalse(wdd.removeLinkModelElements(sm,wd));
		
		wdd.addModelElement(pr);
		
		assertFalse(wdd.removeLinkModelElements(pr,wd));
		wdd.createLinkModelElements(pr,wd);
		assertTrue(wdd.removeLinkModelElements(pr,wd));
		assertFalse(wdd.removeLinkModelElements(pr,wd));
		
		assertFalse(wdd.removeLinkModelElements(pr,wp));
		assertFalse(wdd.removeLinkModelElements(pr,sm));
	}
	
	public void testAreLinkableModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		
		sm.setContext(wp2);
		assertFalse(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,wp));
		assertFalse(wdd.areLinkableModelElements(pr,sm));
		assertFalse(wdd.areLinkableModelElements(wp,wd));
		assertFalse(wdd.areLinkableModelElements(sm,wd));
		assertFalse(wdd.areLinkableModelElements(wd,wp));
		assertFalse(wdd.areLinkableModelElements(wd,sm));
		
		wdd.addModelElement(wd);	
		wdd.addModelElement(wp);
		
		assertFalse(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,wp));
		assertTrue(wdd.areLinkableModelElements(wp,wd));
		assertTrue(wdd.areLinkableModelElements(wd,wp));
		
		wdd.addModelElement(sm);
		
		assertFalse(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,sm));
		assertTrue(wdd.areLinkableModelElements(sm,wd));
		assertTrue(wdd.areLinkableModelElements(wd,sm));
		
		wdd.addModelElement(pr);	
		
		assertTrue(wdd.areLinkableModelElements(pr,wd));
		assertFalse(wdd.areLinkableModelElements(pr,wp));
		assertFalse(wdd.areLinkableModelElements(pr,sm));
		assertFalse(wdd.areLinkableModelElements(wd,pr));
		assertFalse(wdd.areLinkableModelElements(wp,pr));
		assertFalse(wdd.areLinkableModelElements(sm,pr));
		
		wdd.createLinkModelElements(pr,wd);
		assertFalse(wdd.areLinkableModelElements(pr,wd));
	}
	
	public void testExistsLinkModelElements()
	{
		WorkDefinitionDiagram wdd = new WorkDefinitionDiagram();
		ProcessRole pr = createProcessRole();
		WorkDefinition wd = createWorkDefinition();
		WorkProduct wp = createWorkProduct();
		WorkProduct wp2 = createWorkProduct();
		StateMachine sm = createWorkProductState();
		
		sm.setContext(wp2);
		wdd.addModelElement(wd);	
		wdd.addModelElement(wp);
		wdd.addModelElement(pr);
		wdd.addModelElement(sm);
		
		assertFalse(wdd.existsLinkModelElements(pr,wd));
		assertFalse(wdd.existsLinkModelElements(pr,wp));
		assertFalse(wdd.existsLinkModelElements(pr,sm));
		assertFalse(wdd.existsLinkModelElements(wp,wd));
		assertFalse(wdd.existsLinkModelElements(wd,wp));
		assertFalse(wdd.existsLinkModelElements(sm,wd));
		assertFalse(wdd.existsLinkModelElements(wd,sm));
		
		assertFalse(wdd.existsLinkModelElements(pr,pr));
		assertFalse(wdd.existsLinkModelElements(wd,wd));
		assertFalse(wdd.existsLinkModelElements(wp,wp));
		assertFalse(wdd.existsLinkModelElements(sm,sm));
		
		wdd.createLinkModelElements(pr,wd);
		assertTrue(wdd.existsLinkModelElements(pr,wd));
		wdd.removeLinkModelElements(pr,wd);
		assertFalse(wdd.existsLinkModelElements(pr,wd));
		
		wdd.createLinkModelElements(pr,wp);
		assertFalse(wdd.existsLinkModelElements(pr,wp));
		
		wdd.createLinkModelElements(pr,sm);
		assertFalse(wdd.existsLinkModelElements(pr,sm));
		
		wdd.createLinkModelElements(wp,wd);
		assertTrue(wdd.existsLinkModelElements(wp,wd));
		wdd.removeLinkModelElements(wp,wd);
		assertFalse(wdd.existsLinkModelElements(wp,wd));
		
		wdd.createLinkModelElements(sm,wd);
		assertTrue(wdd.existsLinkModelElements(sm,wd));
		wdd.createLinkModelElements(wd,sm);
		assertFalse(wdd.existsLinkModelElements(wd,sm));
		wdd.removeLinkModelElements(sm,wd);
		assertFalse(wdd.existsLinkModelElements(sm,wd));
		
		wdd.createLinkModelElements(wd,wp);
		assertTrue(wdd.existsLinkModelElements(wd,wp));
		wdd.removeLinkModelElements(wd,wp);
		assertFalse(wdd.existsLinkModelElements(wd,wp));
		
		wdd.createLinkModelElements(wd,sm);
		assertTrue(wdd.existsLinkModelElements(wd,sm));
		wdd.createLinkModelElements(wd,sm);
		assertFalse(wdd.existsLinkModelElements(sm,wd));
		wdd.removeLinkModelElements(wd,sm);
		assertFalse(wdd.existsLinkModelElements(wd,sm));
	}
	
}
