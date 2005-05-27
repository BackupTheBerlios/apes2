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

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestActivityDiagram extends TestCase
{	
	private Activity createActivity()
	{
		return new Activity();
	}
	
	private ProcessRole createProcessRole()
	{
		return new ProcessRole();
	}
	
	private WorkProduct createWorkProduct()
	{
		return new WorkProduct();
	}
	
	private ActivityDiagram.Decision createDecision()
	{
		return new ActivityDiagram.Decision();
	}
	

	private ActivityDiagram.Synchro createSynchro()
	{
		return new ActivityDiagram.Synchro();
	}
	

	private ActivityDiagram.InitialPoint createInitialPoint()
	{
		return new ActivityDiagram.InitialPoint();
	}
	
	private ActivityDiagram.FinalPoint createFinalPoint()
	{
		return new ActivityDiagram.FinalPoint();
	}
	
	public void testAddModelElement()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ProcessRole pr = createProcessRole();
		Activity a = createActivity();
		WorkProduct wp = createWorkProduct();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Synchro s=createSynchro();
		ActivityDiagram.InitialPoint ip = createInitialPoint();
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		

		
		assertTrue(ad.modelElementCount()==0);
		assertFalse(ad.addModelElement(pr));
		assertFalse(ad.addModelElement(wp));
		assertTrue(ad.modelElementCount()==0);
		assertTrue(ad.addModelElement(a));
		assertTrue(ad.modelElementCount()==1);
		assertTrue(ad.containsModelElement(a));
		assertTrue(ad.addModelElement(d));
		assertTrue(ad.modelElementCount()==2);
		assertTrue(ad.containsModelElement(d));

		assertTrue(ad.addModelElement(s));
		assertTrue(ad.modelElementCount()==3);
		assertTrue(ad.containsModelElement(s));

		assertTrue(ad.addModelElement(ip));
		assertTrue(ad.modelElementCount()==4);
		assertTrue(ad.containsModelElement(ip));
		assertFalse(ad.addModelElement(ip));
		assertTrue(ad.addModelElement(fp));
		assertTrue(ad.modelElementCount()==5);
		assertTrue(ad.containsModelElement(fp));
		

		
		for(int i=1; i<100; i++)
		{
			a = createActivity();
			assertTrue(ad.modelElementCount()==i*4+1);
			ad.addModelElement(a);
			d = createDecision();
			assertTrue(ad.modelElementCount()==i*4+2);	
			ad.addModelElement(d);

			s = createSynchro();
			assertTrue(ad.modelElementCount()==i*4+3);	
			ad.addModelElement(s);

			fp = createFinalPoint();
			assertTrue(ad.modelElementCount()==i*4+4);
			ad.addModelElement(fp);
			
		}
	}
		
	public void testAddActivityInTwoDiagram()
	{
		ActivityDiagram ad1 = new ActivityDiagram();
		ActivityDiagram ad2 = new ActivityDiagram();
		Activity a = createActivity();
		
		assertTrue(ad1.modelElementCount()==0);
		assertTrue(ad2.modelElementCount()==0);
		
		assertTrue(ad1.addModelElement(a));
		assertTrue(ad2.addModelElement(a));
		
		assertTrue(ad1.modelElementCount()==1);
		assertTrue(ad2.modelElementCount()==1);
	}

	public void testAddActivityInTwoClonedDiagram()
	{
		ActivityDiagram ad1 = new ActivityDiagram();
		ActivityDiagram ad2 = (ActivityDiagram)ad1.clone();
		Activity a = createActivity();
		
		assertTrue(ad1.modelElementCount()==0);
		assertTrue(ad2.modelElementCount()==0);
		
		assertTrue(ad1.addModelElement(a));
		assertTrue(ad2.addModelElement(a));
		
		assertTrue(ad1.modelElementCount()==1);
		assertTrue(ad2.modelElementCount()==1);
	}

	public void testRemoveModelElement()
	{
		Activity a = createActivity();
		Activity a2 = createActivity();
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();

		ActivityDiagram.Synchro s = createSynchro();
		ActivityDiagram.Synchro s2 = createSynchro();
		

		ActivityDiagram.InitialPoint ip = createInitialPoint();
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		ActivityDiagram.FinalPoint fp2 = createFinalPoint();


		ad.addModelElement(a);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(a2);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(d);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(a);
		assertTrue(ad.modelElementCount()==0);
		
		ad.addModelElement(d);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(d);
		assertTrue(ad.modelElementCount()==0);
		

		ad.addModelElement(s);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(s);
		assertTrue(ad.modelElementCount()==0);

		ad.addModelElement(ip);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(ip);
		assertTrue(ad.modelElementCount()==0);
		
		ad.addModelElement(fp);
		assertTrue(ad.modelElementCount()==1);
		
		ad.removeModelElement(fp);
		assertTrue(ad.modelElementCount()==0);
		

		for(int i=0; i<100; i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			fp = createFinalPoint();
			ad.addModelElement(fp);
		}	

		
		ad.addModelElement(a2);
		ad.addModelElement(d2);

		ad.addModelElement(s2);

		ad.addModelElement(fp2);

		
		for(int i=0; i<100; i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			fp = createFinalPoint();
			ad.addModelElement(fp);

		}
		
		assertTrue(ad.modelElementCount()==804);
		ad.removeModelElement(a2);
		ad.removeModelElement(d2);

		ad.removeModelElement(s2);
		assertTrue(ad.modelElementCount()==801);

		ad.removeModelElement(fp2);
		assertTrue(ad.modelElementCount()==800);

	}
	
	public void testContainsModelElement()
	{
		Activity a;
		Activity a2 = createActivity();

		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();		
		ActivityDiagram.Synchro s = createSynchro();
		ActivityDiagram.Synchro s2 = createSynchro();	

		ActivityDiagram.FinalPoint fp;		
		ActivityDiagram.InitialPoint ip2 = createInitialPoint();
		ActivityDiagram.FinalPoint fp2 = createFinalPoint();

		ActivityDiagram ad = new ActivityDiagram();
		
		for(int i=0; i<100; i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			fp = createFinalPoint();
			ad.addModelElement(fp);
		}	

		
		assertFalse(ad.containsModelElement(a2));
		ad.addModelElement(a2);
		assertTrue(ad.containsModelElement(a2));

		assertFalse(ad.containsModelElement(d2));
		ad.addModelElement(d2);
		assertTrue(ad.containsModelElement(d2));
		

		assertFalse(ad.containsModelElement(s2));
		ad.addModelElement(s2);
		assertTrue(ad.containsModelElement(s2));

		assertFalse(ad.containsModelElement(ip2));
		ad.addModelElement(ip2);
		assertTrue(ad.containsModelElement(ip2));
		
		assertFalse(ad.containsModelElement(fp2));
		ad.addModelElement(fp2);
		assertTrue(ad.containsModelElement(fp2));

		for(int i=0; i<100; i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			fp = createFinalPoint();
			ad.addModelElement(fp);

		}
		
		assertTrue(ad.containsModelElement(a2));
		assertTrue(ad.containsModelElement(d2));

		assertTrue(ad.containsModelElement(s2));

		assertTrue(ad.containsModelElement(ip2));
		assertTrue(ad.containsModelElement(fp2));

	}
	
	public void testGetModelElement()
	{
		Activity a;
		Activity a2 = createActivity();
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d;
		ActivityDiagram.Decision d2 = createDecision();

		ActivityDiagram.Synchro s;
		ActivityDiagram.Synchro s2 = createSynchro();

		ActivityDiagram.InitialPoint ip2 = createInitialPoint();
		ActivityDiagram.FinalPoint fp;
		ActivityDiagram.FinalPoint fp2 = createFinalPoint();

		
		for(int i=0; i<100; i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			fp = createFinalPoint();
			ad.addModelElement(fp);

		}
		
		ad.addModelElement(a2);
		ad.addModelElement(d2);

		ad.addModelElement(s2);
		

		ad.addModelElement(ip2);
		ad.addModelElement(fp2);


		for(int i=0; i<100; i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			fp = createFinalPoint();
			ad.addModelElement(fp);

		}
		

		assertEquals(ad.getModelElement(400), a2);
		assertEquals(ad.getModelElement(401), d2);
		assertEquals(ad.getModelElement(402), s2);

		assertEquals(ad.getModelElement(403), ip2);
		assertEquals(ad.getModelElement(404), fp2);

		assertEquals(ad.getModelElement(-1), null);

		assertEquals(ad.getModelElement(805), null);

		assertEquals(ad.getModelElement(806), null);

	}
	
	public void testModelElementCount()
	{
		Activity a = createActivity();
		ActivityDiagram.Decision d = createDecision();

		ActivityDiagram.Synchro s = createSynchro();

		ActivityDiagram.InitialPoint ip = createInitialPoint();
		ActivityDiagram.FinalPoint fp = createFinalPoint();

		ActivityDiagram ad = new ActivityDiagram();
		
		assertEquals(ad.modelElementCount(),0);
		
		ad.addModelElement(a);
		assertEquals(ad.modelElementCount(),1);
		
		ad.addModelElement(a);
		assertEquals(ad.modelElementCount(),1);
		
		ad.addModelElement(d);
		assertEquals(ad.modelElementCount(),2);
		
		ad.addModelElement(d);
		assertEquals(ad.modelElementCount(),2);
		

		ad.addModelElement(s);
		assertEquals(ad.modelElementCount(),3);
		
		ad.addModelElement(s);
		assertEquals(ad.modelElementCount(),3);

		ad.addModelElement(ip);
		assertEquals(ad.modelElementCount(),4);
		
		ad.addModelElement(ip);
		assertEquals(ad.modelElementCount(),4);
		
		ad.addModelElement(fp);
		assertEquals(ad.modelElementCount(),5);
		
		ad.addModelElement(fp);
		assertEquals(ad.modelElementCount(),5);
		
		ad.removeModelElement(a);
		assertEquals(ad.modelElementCount(),4);	

		ad.removeModelElement(a);

		assertEquals(ad.modelElementCount(),4);
		
		ad.removeModelElement(d);
		assertEquals(ad.modelElementCount(),3);
		
		ad.removeModelElement(d);
		assertEquals(ad.modelElementCount(),3);
		
		ad.removeModelElement(ip);
		assertEquals(ad.modelElementCount(),2);

		

		ad.removeModelElement(a);
		assertEquals(ad.modelElementCount(),2);
		
		ad.removeModelElement(d);

		ad.removeModelElement(ip);

		assertEquals(ad.modelElementCount(),2);
		

		ad.removeModelElement(d);
		assertEquals(ad.modelElementCount(),2);
		
		ad.removeModelElement(s);

		ad.removeModelElement(fp);

		assertEquals(ad.modelElementCount(),0);
		
		
		
		for(int i=0;i<100;i++)
		{
			a = createActivity();
			ad.addModelElement(a);
			d = createDecision();
			ad.addModelElement(d);

			s = createSynchro();
			ad.addModelElement(s);

			ip = createInitialPoint();
			ad.addModelElement(ip);
			fp = createFinalPoint();
			ad.addModelElement(fp);

		}

		assertEquals(ad.modelElementCount(),401);


	}
	
	public void testCreateLinkModelElements()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		Activity a3 = createActivity();
		Activity a4 = createActivity();
		Activity a5 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();

		ActivityDiagram.Synchro s = createSynchro();
		ActivityDiagram.Synchro s2 = createSynchro();

		ActivityDiagram.InitialPoint ip = createInitialPoint();
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		ActivityDiagram.FinalPoint fp2 = createFinalPoint();
		

		
		assertFalse(ad.createLinkModelElements(a,a2,null));
		assertFalse(ad.createLinkModelElements(a,d,null));
		assertFalse(ad.createLinkModelElements(a,d2,null));

		assertFalse(ad.createLinkModelElements(a,s,null));
		assertFalse(ad.createLinkModelElements(a,s2,null));

		assertFalse(ad.createLinkModelElements(a,ip,null));
		assertFalse(ad.createLinkModelElements(a,fp,null));
		assertFalse(ad.createLinkModelElements(a,fp2,null));

		assertFalse(ad.createLinkModelElements(a2,a,null));
		assertFalse(ad.createLinkModelElements(a2,d,null));
		assertFalse(ad.createLinkModelElements(a2,d2,null));

		assertFalse(ad.createLinkModelElements(a2,s,null));
		assertFalse(ad.createLinkModelElements(a2,s2,null));

		assertFalse(ad.createLinkModelElements(a2,ip,null));
		assertFalse(ad.createLinkModelElements(a2,fp,null));
		assertFalse(ad.createLinkModelElements(a2,fp2,null));

		assertFalse(ad.createLinkModelElements(d,a,null));
		assertFalse(ad.createLinkModelElements(d,a2,null));
		assertFalse(ad.createLinkModelElements(d,d2,null));

		assertFalse(ad.createLinkModelElements(d,s,null));
		assertFalse(ad.createLinkModelElements(d,s2,null));

		assertFalse(ad.createLinkModelElements(d,ip,null));
		assertFalse(ad.createLinkModelElements(d,fp,null));
		assertFalse(ad.createLinkModelElements(d,fp2,null));

		assertFalse(ad.createLinkModelElements(d2,a,null));
		assertFalse(ad.createLinkModelElements(d2,d,null));
		assertFalse(ad.createLinkModelElements(d2,a2,null));

		assertFalse(ad.createLinkModelElements(d2,s,null));
		assertFalse(ad.createLinkModelElements(d2,s2,null));
		assertFalse(ad.createLinkModelElements(s,a,null));
		assertFalse(ad.createLinkModelElements(s,a2,null));
		assertFalse(ad.createLinkModelElements(s,d,null));
		assertFalse(ad.createLinkModelElements(s,d2,null));
		assertFalse(ad.createLinkModelElements(s,s2,null));
		assertFalse(ad.createLinkModelElements(s2,a,null));
		assertFalse(ad.createLinkModelElements(s2,a2,null));
		assertFalse(ad.createLinkModelElements(s2,d,null));
		assertFalse(ad.createLinkModelElements(s2,d2,null));
		assertFalse(ad.createLinkModelElements(s2,s,null));
		

		assertFalse(ad.createLinkModelElements(d2,ip,null));
		assertFalse(ad.createLinkModelElements(d2,fp,null));
		assertFalse(ad.createLinkModelElements(d2,fp2,null));
		assertFalse(ad.createLinkModelElements(ip,a,null));
		assertFalse(ad.createLinkModelElements(ip,a2,null));
		assertFalse(ad.createLinkModelElements(ip,d,null));
		assertFalse(ad.createLinkModelElements(ip,d2,null));
		assertFalse(ad.createLinkModelElements(ip,fp,null));
		assertFalse(ad.createLinkModelElements(ip,fp2,null));
		assertFalse(ad.createLinkModelElements(fp,a,null));
		assertFalse(ad.createLinkModelElements(fp,a2,null));
		assertFalse(ad.createLinkModelElements(fp,d,null));
		assertFalse(ad.createLinkModelElements(fp,d2,null));
		assertFalse(ad.createLinkModelElements(fp,ip,null));
		assertFalse(ad.createLinkModelElements(fp,fp2,null));
		assertFalse(ad.createLinkModelElements(fp2,a,null));
		assertFalse(ad.createLinkModelElements(fp2,a2,null));
		assertFalse(ad.createLinkModelElements(fp2,d,null));
		assertFalse(ad.createLinkModelElements(fp2,d2,null));
		assertFalse(ad.createLinkModelElements(fp2,fp,null));
		assertFalse(ad.createLinkModelElements(fp2,ip,null));
	
		

		ad.addModelElement(a);
		assertFalse(ad.createLinkModelElements(a,a2,null));
		assertFalse(ad.createLinkModelElements(a,d,null));
		assertFalse(ad.createLinkModelElements(a,d2,null));

		assertFalse(ad.createLinkModelElements(a,s,null));
		assertFalse(ad.createLinkModelElements(a,s2,null));

		assertFalse(ad.createLinkModelElements(a,ip,null));
		assertFalse(ad.createLinkModelElements(a,fp,null));
		assertFalse(ad.createLinkModelElements(a,fp2,null));

		assertFalse(ad.createLinkModelElements(a2,a,null));
		assertFalse(ad.createLinkModelElements(d,a,null));
		assertFalse(ad.createLinkModelElements(d2,a,null));

		assertFalse(ad.createLinkModelElements(s,a,null));
		assertFalse(ad.createLinkModelElements(s2,a,null));

		assertFalse(ad.createLinkModelElements(ip,a,null));
		assertFalse(ad.createLinkModelElements(fp,a,null));
		assertFalse(ad.createLinkModelElements(fp2,a,null));

		
		ad.addModelElement(a2);
		assertTrue(ad.createLinkModelElements(a,a2,null));
		assertTrue(ad.createLinkModelElements(a2,a,null));
		
		assertFalse(ad.createLinkModelElements(a,a2,null));
		assertFalse(ad.createLinkModelElements(a2,a,null));
	
		ad.addModelElement(d);
		ad.addModelElement(d2);
		assertFalse(ad.createLinkModelElements(a,d,null));
		assertFalse(ad.createLinkModelElements(a2,d,null));
		assertFalse(ad.createLinkModelElements(a,d2,null));
		assertFalse(ad.createLinkModelElements(a2,d2,null));
		assertTrue(ad.createLinkModelElements(d,d2,null));
		assertTrue(ad.createLinkModelElements(d2,d,null));
		
		ad.addModelElement(s);
		ad.addModelElement(s2);
		assertFalse(ad.createLinkModelElements(a,s,null));
		assertFalse(ad.createLinkModelElements(a2,s,null));
		assertFalse(ad.createLinkModelElements(a,s2,null));
		assertFalse(ad.createLinkModelElements(a2,s2,null));
		assertTrue(ad.createLinkModelElements(s,s2,null));
		assertTrue(ad.createLinkModelElements(s2,s,null));
			
		ad.addModelElement(a3);
		ad.addModelElement(a4);
		ad.addModelElement(a5);
		assertTrue(ad.createLinkModelElements(a3,d,null));
		assertFalse(ad.createLinkModelElements(a3,d,null));			
		assertFalse(ad.createLinkModelElements(a3,d2,null));
		assertFalse(ad.createLinkModelElements(a3,s,null));
//		assertFalse(ad.createLinkModelElements(a4,d));
		assertTrue(ad.createLinkModelElements(a4,d,null));
		assertFalse(ad.createLinkModelElements(a3,a4,null));

		assertTrue(ad.createLinkModelElements(d,a4,null));
		assertTrue(ad.createLinkModelElements(d,a5,null));
		assertFalse(ad.createLinkModelElements(d,a4,null));
		assertFalse(ad.createLinkModelElements(d,a5,null));
		
		ad.addModelElement(ip);
		assertTrue(ad.createLinkModelElements(ip,a,null));
		assertFalse(ad.createLinkModelElements(ip,d,null));
		assertFalse(ad.createLinkModelElements(ip,a2,null));
		assertFalse(ad.createLinkModelElements(ip,fp,null));
		assertFalse(ad.createLinkModelElements(a,ip,null));
		assertFalse(ad.createLinkModelElements(d,ip,null));
		assertFalse(ad.createLinkModelElements(fp,ip,null));
		
		ad.addModelElement(fp);
		ad.addModelElement(fp2);
		assertFalse(ad.createLinkModelElements(fp,a,null));
		assertFalse(ad.createLinkModelElements(fp,d,null));
		assertFalse(ad.createLinkModelElements(fp,ip,null));
		assertFalse(ad.createLinkModelElements(fp,fp2,null));
		assertFalse(ad.createLinkModelElements(fp,a2,null));
		assertFalse(ad.createLinkModelElements(fp,d2,null));
		assertFalse(ad.createLinkModelElements(ip,fp,null));
		assertFalse(ad.createLinkModelElements(a,fp,null));
		assertTrue(ad.createLinkModelElements(d,fp,null));
		assertFalse(ad.createLinkModelElements(a2,fp,null));
		assertTrue(ad.createLinkModelElements(d2,fp,null));
		assertFalse(ad.createLinkModelElements(fp2,fp,null));
		
}

	public void testRemoveLinkModelElements()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();

		ActivityDiagram.Synchro s = createSynchro();
		ActivityDiagram.Synchro s2 = createSynchro();

		ActivityDiagram.InitialPoint ip = createInitialPoint();;
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		ActivityDiagram.FinalPoint fp2 = createFinalPoint();

		
		assertFalse(ad.removeLinkModelElements(a,a2,null));
		assertFalse(ad.removeLinkModelElements(a,a2,null));
		assertFalse(ad.removeLinkModelElements(a,d,null));
		assertFalse(ad.removeLinkModelElements(a,d2,null));
		assertFalse(ad.removeLinkModelElements(a,s,null));
		assertFalse(ad.removeLinkModelElements(a,s2,null));
		assertFalse(ad.removeLinkModelElements(a2,a,null));
		assertFalse(ad.removeLinkModelElements(a2,a2,null));
		assertFalse(ad.removeLinkModelElements(a2,d,null));
		assertFalse(ad.removeLinkModelElements(a2,d2,null));
		assertFalse(ad.removeLinkModelElements(a2,s,null));
		assertFalse(ad.removeLinkModelElements(a2,s2,null));
		assertFalse(ad.removeLinkModelElements(d,a,null));
		assertFalse(ad.removeLinkModelElements(d,a2,null));
		assertFalse(ad.removeLinkModelElements(d,d,null));
		assertFalse(ad.removeLinkModelElements(d,d2,null));
		assertFalse(ad.removeLinkModelElements(d,s,null));
		assertFalse(ad.removeLinkModelElements(d,s2,null));
		assertFalse(ad.removeLinkModelElements(d2,a,null));
		assertFalse(ad.removeLinkModelElements(d2,a2,null));
		assertFalse(ad.removeLinkModelElements(d2,d,null));
		assertFalse(ad.removeLinkModelElements(d2,d2,null));

		assertFalse(ad.removeLinkModelElements(d2,s,null));
		assertFalse(ad.removeLinkModelElements(d2,s2,null));

		assertFalse(ad.removeLinkModelElements(ip,a,null));
		assertFalse(ad.removeLinkModelElements(ip,fp,null));
		assertFalse(ad.removeLinkModelElements(ip,d,null));
		assertFalse(ad.removeLinkModelElements(a,ip,null));
		assertFalse(ad.removeLinkModelElements(fp,ip,null));
		assertFalse(ad.removeLinkModelElements(d,ip,null));
		assertFalse(ad.removeLinkModelElements(fp,a,null));
		assertFalse(ad.removeLinkModelElements(fp,ip,null));
		assertFalse(ad.removeLinkModelElements(fp,d,null));
		assertFalse(ad.removeLinkModelElements(fp,fp2,null));
		assertFalse(ad.removeLinkModelElements(fp2,fp,null));
		assertFalse(ad.removeLinkModelElements(a,fp,null));
		assertFalse(ad.removeLinkModelElements(d,fp,null));
		assertFalse(ad.removeLinkModelElements(ip,fp,null));
		


		ad.addModelElement(a);
		ad.addModelElement(a2);
		ad.addModelElement(d);
		ad.addModelElement(d2);

		ad.addModelElement(s);
		ad.addModelElement(s2);

		ad.addModelElement(ip);
		ad.addModelElement(fp);
		ad.addModelElement(fp2);
		

		ad.createLinkModelElements(a,a2,null);
		assertFalse(ad.removeLinkModelElements(a2,a,null));
		assertTrue(ad.removeLinkModelElements(a,a2,null));
		
		ad.createLinkModelElements(a2,a,null);
		assertFalse(ad.removeLinkModelElements(a,a2,null));
		assertTrue(ad.removeLinkModelElements(a2,a,null));

		ad.createLinkModelElements(d,a,null);
		assertFalse(ad.removeLinkModelElements(a,d,null));
		assertTrue(ad.removeLinkModelElements(d,a,null));

		ad.createLinkModelElements(a2,d,null);
		assertFalse(ad.removeLinkModelElements(d,a2,null));
		assertTrue(ad.removeLinkModelElements(a2,d,null));
		
		ad.createLinkModelElements(d,d2,null);
		assertFalse(ad.removeLinkModelElements(d2,d,null));
		assertTrue(ad.removeLinkModelElements(d,d2,null));

		
		ad.createLinkModelElements(s,s2,null);
		assertFalse(ad.removeLinkModelElements(s2,s,null));
		assertTrue(ad.removeLinkModelElements(s,s2,null));

		
		ad.createLinkModelElements(a,ip,null);
		assertFalse(ad.removeLinkModelElements(ip,a,null));
		assertFalse(ad.removeLinkModelElements(a,ip,null));
		
		ad.createLinkModelElements(d,ip,null);
		assertFalse(ad.removeLinkModelElements(ip,d,null));
		assertFalse(ad.removeLinkModelElements(d,ip,null));
		
		ad.createLinkModelElements(ip,fp,null);
		assertFalse(ad.removeLinkModelElements(ip,fp,null));
		assertFalse(ad.removeLinkModelElements(fp,ip,null));
		
		ad.createLinkModelElements(ip,a,null);
		assertFalse(ad.removeLinkModelElements(a,ip,null));
		assertTrue(ad.removeLinkModelElements(ip,a,null));
		
		ad.createLinkModelElements(ip,d,null);
		assertFalse(ad.removeLinkModelElements(d,ip,null));
		assertTrue(ad.removeLinkModelElements(ip,d,null));
		
		ad.createLinkModelElements(a,fp,null);
		assertFalse(ad.removeLinkModelElements(fp,a,null));
		assertTrue(ad.removeLinkModelElements(a,fp,null));
		
		ad.createLinkModelElements(d,fp,null);
		assertFalse(ad.removeLinkModelElements(fp,d,null));
		assertTrue(ad.removeLinkModelElements(d,fp,null));
		
		ad.createLinkModelElements(fp,a,null);
		assertFalse(ad.removeLinkModelElements(fp,a,null));
		assertFalse(ad.removeLinkModelElements(a,fp,null));
		
		ad.createLinkModelElements(fp,d,null);
		assertFalse(ad.removeLinkModelElements(fp,d,null));
		assertFalse(ad.removeLinkModelElements(d,fp,null));
		
		ad.createLinkModelElements(fp,ip,null);
		assertFalse(ad.removeLinkModelElements(ip,fp,null));
		assertFalse(ad.removeLinkModelElements(fp,ip,null));
		
		ad.createLinkModelElements(fp,fp2,null);
		assertFalse(ad.removeLinkModelElements(fp,fp2,null));
		assertFalse(ad.removeLinkModelElements(fp2,fp,null));
		

	}
	
	public void testAreLinkableActivityActivity()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		Activity a3 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Synchro s= createSynchro();
		
		assertFalse(ad.areLinkableModelElements(a,a2,null));
		assertFalse(ad.areLinkableModelElements(a2,a,null));
		
		ad.addModelElement(a);
		
		assertFalse(ad.areLinkableModelElements(a,a2,null));
		assertFalse(ad.areLinkableModelElements(a2,a,null));
		
		ad.addModelElement(a2);	
		
		assertTrue(ad.areLinkableModelElements(a,a2,null));
		assertTrue(ad.areLinkableModelElements(a2,a,null));
		
		ad.createLinkModelElements(a,a2,null);
		assertFalse(ad.areLinkableModelElements(a,a2,null));
		
		ad.addModelElement(a3);
		assertFalse(ad.areLinkableActivityActivity(a,a3));
		
		ad.addModelElement(d);
		ad.createLinkModelElements(a2,d,null);
		assertFalse(ad.areLinkableActivityActivity(a2,a3));
		
		ad.addModelElement(s);
		ad.createLinkModelElements(a2,s,null);
		assertFalse(ad.areLinkableActivityActivity(a2,a3));
	}
	
	public void testAreLinkableActivityDecision()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();
		Activity  a = createActivity();
		Activity  a2 = createActivity();
		
		ad.addModelElement(a);
		ad.addModelElement(a2);	
		ad.addModelElement(d);
		ad.addModelElement(d2);
		
		
		assertTrue(ad.areLinkableActivityDecision(a,d));
		assertTrue(ad.areLinkableActivityDecision(a,d2));
		
		ad.createLinkModelElements(a,d,null);
		assertFalse(ad.areLinkableActivityDecision(a,d));
		assertTrue(ad.areLinkableActivityDecision(a2,d));
		//assertFalse(ad.areLinkableActivityDecision(a2,d));
		assertFalse(ad.areLinkableActivityDecision(a,d2));
	}
	
	public void testAreLinkableDecisionActivity()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();
		Activity  a = createActivity();
		Activity  a2 = createActivity();
		
		ad.addModelElement(d);
		ad.addModelElement(a2);	
		ad.addModelElement(a);
		ad.addModelElement(d2);
		
		assertTrue(ad.areLinkableDecisionActivity(d,a));
		assertTrue(ad.areLinkableDecisionActivity(d,a2));
		
		ad.createLinkModelElements(d,a,null);
		assertFalse(ad.areLinkableDecisionActivity(d,a));
		assertTrue(ad.areLinkableDecisionActivity(d,a2));
	}
	
	public void testAreLinkableDecisionDecision()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();
		
		ad.addModelElement(d);
		ad.addModelElement(d2);
		
		assertTrue(ad.areLinkableDecisionDecision(d,d2));
		
		ad.createLinkModelElements(d,d2,null);
		assertFalse(ad.areLinkableDecisionDecision(d,d2));
	}
	
	public void testAreLinkableInitialPointModelElement()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		Activity  a = createActivity();
		ActivityDiagram.InitialPoint ip = createInitialPoint();;
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		
		
		
		ad.addModelElement(a);
		ad.addModelElement(ip);	
		ad.addModelElement(d);
		
		assertTrue(ad.areLinkableInitialPointModelElement(ip,a));
		assertTrue(ad.areLinkableInitialPointModelElement(ip,d));
		
		ad.createLinkModelElements(ip,a,null);
		assertFalse(ad.areLinkableInitialPointModelElement(ip,a));
		assertFalse(ad.areLinkableInitialPointModelElement(ip,d));
		
		ad.createLinkModelElements(ip,fp,null);
		assertFalse(ad.areLinkableInitialPointModelElement(ip,fp));
		
	}
	
	public void testAreLinkableInitialPointDecision()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		Activity  a = createActivity();
		ActivityDiagram.InitialPoint ip = createInitialPoint();;
				
		ad.addModelElement(d);
		ad.addModelElement(ip);	
		ad.addModelElement(a);
		
		assertTrue(ad.areLinkableInitialPointModelElement(ip,a));
		assertTrue(ad.areLinkableInitialPointDecision(ip,d));
		
		ad.createLinkModelElements(ip,d,null);
		assertFalse(ad.areLinkableInitialPointDecision(ip,d));
		assertFalse(ad.areLinkableInitialPointModelElement(ip,a));
		
		ad.removeLinkModelElements(ip,d,null);
		ad.createLinkModelElements(a,d,null);
		assertFalse(ad.areLinkableInitialPointDecision(ip,d));
	}

	public void testAreLinkableSynchroDecision()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		Activity  a = createActivity();
		ActivityDiagram.Synchro s = createSynchro();
				
		ad.addModelElement(d);
		ad.addModelElement(s);	
		ad.addModelElement(a);
		
		assertTrue(ad.areLinkableSynchroModelElement(s,a));
		assertTrue(ad.areLinkableSynchroDecision(s,d));
		
		ad.createLinkModelElements(s,d,null);
		assertFalse(ad.areLinkableSynchroDecision(s,d));
		
		ad.removeLinkModelElements(s,d,null);
		ad.createLinkModelElements(a,d,null);
		assertFalse(ad.areLinkableSynchroDecision(s,d));
	}
	
	public void testAreLinkableModelElementFinalPoint()
	{
		ActivityDiagram ad = new ActivityDiagram();
		ActivityDiagram.Decision d = createDecision();
		Activity  a = createActivity();
		ActivityDiagram.InitialPoint ip = createInitialPoint();;
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		
		
		
		assertTrue(ad.areLinkableModelElementFinalPoint(a,fp));
		
		ad.addModelElement(a);
		assertTrue(ad.areLinkableModelElementFinalPoint(a,fp));
		
		
		ad.addModelElement(fp);	
		ad.addModelElement(d);
		
		assertTrue(ad.areLinkableModelElementFinalPoint(a,fp));
		assertTrue(ad.areLinkableModelElementFinalPoint(d,fp));
		
		ad.createLinkModelElements(a,fp,null);
		assertFalse(ad.areLinkableModelElementFinalPoint(a,fp));
		assertTrue(ad.areLinkableModelElementFinalPoint(d,fp));
		
		
		ad.createLinkModelElements(ip,fp,null);
		assertTrue(ad.areLinkableModelElementFinalPoint(ip,fp));
		
	}
	

	public void testAreLinkableModelElements()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		Activity a3 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Decision d2 = createDecision();

		ActivityDiagram.Synchro s = createSynchro();

		ActivityDiagram.InitialPoint ip = createInitialPoint();
		ActivityDiagram.FinalPoint fp = createFinalPoint();
		ActivityDiagram.FinalPoint fp2 = createFinalPoint();
		

		assertFalse(ad.areLinkableModelElements(a,a2,null));
		assertFalse(ad.areLinkableModelElements(a2,a,null));
		
		ad.addModelElement(a);
		
		assertFalse(ad.areLinkableModelElements(a,a2,null));
		assertFalse(ad.areLinkableModelElements(a2,a,null));
		
		ad.addModelElement(a2);	
		
		assertTrue(ad.areLinkableModelElements(a,a2,null));
		assertTrue(ad.areLinkableModelElements(a2,a,null));
		
		ad.createLinkModelElements(a,a2,null);
		assertFalse(ad.areLinkableModelElements(a,a2,null));
		
		ad.addModelElement(d);
		ad.addModelElement(d2);
		
		assertTrue(ad.areLinkableModelElements(d,d2,null));
		assertTrue(ad.areLinkableModelElements(d2,d,null));
		assertFalse(ad.areLinkableModelElements(a,d,null));
		assertTrue(ad.areLinkableModelElements(d,a,null));
		
		ad.createLinkModelElements(a2,d,null);
		assertFalse(ad.areLinkableModelElements(a2,d,null));
		assertFalse(ad.areLinkableModelElements(a3,d,null));
		
		ad.addModelElement(a3);
		assertTrue(ad.areLinkableModelElements(d,a3,null));
		
		ad.createLinkModelElements(d,a3,null);
		assertTrue(ad.areLinkableModelElements(d,a,null));

		
		ad.addModelElement(s);
		assertTrue(ad.areLinkableModelElements(s,a3,null));
		assertTrue(ad.areLinkableModelElements(s,a,null));

		
		ad.addModelElement(ip);
		assertFalse(ad.areLinkableModelElements(a,ip,null));
		assertFalse(ad.areLinkableModelElements(d,ip,null));
		assertTrue(ad.areLinkableModelElements(ip,a,null));
		ad.createLinkModelElements(ip,a,null);
		assertFalse(ad.areLinkableModelElements(ip,a,null));
		assertFalse(ad.areLinkableModelElements(ip,a2,null));
		assertFalse(ad.areLinkableModelElements(ip,d,null));
		
		ad.addModelElement(fp);
		ad.addModelElement(fp2);
		assertFalse(ad.areLinkableModelElements(fp,a,null));
		assertFalse(ad.areLinkableModelElements(fp,d,null));
		assertFalse(ad.areLinkableModelElements(fp,ip,null));
		assertFalse(ad.areLinkableModelElements(fp,fp2,null));
		assertFalse(ad.areLinkableModelElements(ip,fp,null));
		assertFalse(ad.areLinkableModelElements(a,fp,null));
		ad.createLinkModelElements(a,fp,null);
		assertFalse(ad.areLinkableModelElements(a,fp,null));
		assertFalse(ad.areLinkableModelElements(a2,fp,null));
		assertTrue(ad.areLinkableModelElements(d,fp,null));
		ad.createLinkModelElements(d,fp,null);
		assertFalse(ad.areLinkableModelElements(d,fp,null));
		assertTrue(ad.areLinkableModelElements(d2,fp,null));

	}
	
	public void testExistsLinkModelElements()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		ActivityDiagram.Decision d = createDecision();

		ActivityDiagram.Synchro s = createSynchro();

		ActivityDiagram.InitialPoint ip = createInitialPoint();
		ActivityDiagram.FinalPoint fp = createFinalPoint();

		
		ad.addModelElement(a);	
		ad.addModelElement(a2);
		ad.addModelElement(d);
		ad.addModelElement(s);
		
		assertFalse(ad.existsLinkModelElements(a,a2,null));
		assertFalse(ad.existsLinkModelElements(a2,a,null));
		assertFalse(ad.existsLinkModelElements(d,a,null));
		assertFalse(ad.existsLinkModelElements(a,d,null));

		assertFalse(ad.existsLinkModelElements(a,s,null));
		assertFalse(ad.existsLinkModelElements(s,a,null));

		assertFalse(ad.existsLinkModelElements(ip,a,null));
		assertFalse(ad.existsLinkModelElements(ip,d,null));
		assertFalse(ad.existsLinkModelElements(a,fp,null));
		assertFalse(ad.existsLinkModelElements(d,fp,null));

		
		ad.createLinkModelElements(a,a2,null);
		assertTrue(ad.existsLinkModelElements(a,a2,null));
		assertFalse(ad.existsLinkModelElements(a2,a,null));
		
		ad.removeLinkModelElements(a,a2,null);
		assertFalse(ad.existsLinkModelElements(a,a2,null));
		
		ad.createLinkModelElements(a,d,null);
		ad.createLinkModelElements(d,a2,null);
		assertTrue(ad.existsLinkModelElements(a,d,null));
		assertFalse(ad.existsLinkModelElements(d,a,null));
		assertTrue(ad.existsLinkModelElements(d,a2,null));
		assertFalse(ad.existsLinkModelElements(a2,d,null));

		
		ad.createLinkModelElements(a,s,null);
		ad.createLinkModelElements(s,a2,null);
		assertFalse(ad.existsLinkModelElements(a,s,null));
		assertFalse(ad.existsLinkModelElements(s,a,null));
		assertTrue(ad.existsLinkModelElements(s,a2,null));
		assertFalse(ad.existsLinkModelElements(a2,s,null));

		
		ad.createLinkModelElements(ip,a,null);
		assertFalse(ad.existsLinkModelElements(ip,a,null));
		assertFalse(ad.existsLinkModelElements(a,ip,null));
		
		ad.createLinkModelElements(ip,d,null);
		assertFalse(ad.existsLinkModelElements(ip,d,null));
		assertFalse(ad.existsLinkModelElements(d,ip,null));
		
		ad.createLinkModelElements(a,fp,null);
		assertFalse(ad.existsLinkModelElements(a,fp,null));
		assertFalse(ad.existsLinkModelElements(fp,a,null));
		
		ad.createLinkModelElements(d,fp,null);
		assertFalse(ad.existsLinkModelElements(d,fp,null));
		assertFalse(ad.existsLinkModelElements(fp,d,null));	

	}
	
	public void testSetLinkLabel()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Synchro s = createSynchro();
		
		ad.addModelElement(a);	
		ad.addModelElement(a2);
		ad.addModelElement(d);
		ad.addModelElement(s);
		assertTrue(ad.createLinkModelElements(a,a2,null));
		
		assertFalse(ad.setLinkLabel(a2,a,"false test"));
		assertTrue(ad.setLinkLabel(a,a2,"true test"));
		
		ad.removeLinkModelElements(a,a2,null);
		ad.createLinkModelElements(a,d,null);
		ad.createLinkModelElements(d,a2,null);
		ad.createLinkModelElements(a,s,null);
		ad.createLinkModelElements(s,a2,null);
		
		assertFalse(ad.setLinkLabel(d,a,"false test"));
		assertTrue(ad.setLinkLabel(a,d,"true test"));
		assertFalse(ad.setLinkLabel(a2,d,"false test"));
		assertTrue(ad.setLinkLabel(d,a2,"true test"));
		assertFalse(ad.setLinkLabel(a2,s,"false test"));
		assertTrue(ad.setLinkLabel(s,a2,"true test"));
	}
	
	public void testGetLinkLabel()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Synchro s = createSynchro();
		
		ad.addModelElement(a);	
		ad.addModelElement(a2);
		ad.addModelElement(d);
		ad.addModelElement(s);
		ad.createLinkModelElements(a,a2,null);
		
		assertTrue(ad.getLinkLabel(a,a2)=="");
		assertTrue(ad.getLinkLabel(a2,a)=="");
		ad.setLinkLabel(a,a2,"true test");
		
		assertTrue(ad.getLinkLabel(a,a2)=="true test");
		assertTrue(ad.getLinkLabel(a2,a)=="");
		
		ad.removeLinkModelElements(a,a2,null);
		ad.createLinkModelElements(a,d,null);
		ad.createLinkModelElements(d,a2,null);
		ad.createLinkModelElements(s,a2,null);
		
		assertTrue(ad.getLinkLabel(a,d)=="");
		assertTrue(ad.getLinkLabel(a,s)=="");
		assertTrue(ad.getLinkLabel(d,a2)=="");
		assertTrue(ad.getLinkLabel(s,a2)=="");
		ad.setLinkLabel(a,d,"true test");
		assertTrue(ad.getLinkLabel(a,d)=="true test");
		ad.setLinkLabel(d,a2,"true test");
		assertTrue(ad.getLinkLabel(d,a2)=="true test");
		ad.setLinkLabel(s,a2,"true test");
		assertTrue(ad.getLinkLabel(s,a2)=="true test");
	}
}
