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
		

		
		assertFalse(ad.createLinkModelElements(a,a2));
		assertFalse(ad.createLinkModelElements(a,d));
		assertFalse(ad.createLinkModelElements(a,d2));

		assertFalse(ad.createLinkModelElements(a,s));
		assertFalse(ad.createLinkModelElements(a,s2));

		assertFalse(ad.createLinkModelElements(a,ip));
		assertFalse(ad.createLinkModelElements(a,fp));
		assertFalse(ad.createLinkModelElements(a,fp2));

		assertFalse(ad.createLinkModelElements(a2,a));
		assertFalse(ad.createLinkModelElements(a2,d));
		assertFalse(ad.createLinkModelElements(a2,d2));

		assertFalse(ad.createLinkModelElements(a2,s));
		assertFalse(ad.createLinkModelElements(a2,s2));

		assertFalse(ad.createLinkModelElements(a2,ip));
		assertFalse(ad.createLinkModelElements(a2,fp));
		assertFalse(ad.createLinkModelElements(a2,fp2));

		assertFalse(ad.createLinkModelElements(d,a));
		assertFalse(ad.createLinkModelElements(d,a2));
		assertFalse(ad.createLinkModelElements(d,d2));

		assertFalse(ad.createLinkModelElements(d,s));
		assertFalse(ad.createLinkModelElements(d,s2));

		assertFalse(ad.createLinkModelElements(d,ip));
		assertFalse(ad.createLinkModelElements(d,fp));
		assertFalse(ad.createLinkModelElements(d,fp2));

		assertFalse(ad.createLinkModelElements(d2,a));
		assertFalse(ad.createLinkModelElements(d2,d));
		assertFalse(ad.createLinkModelElements(d2,a2));

		assertFalse(ad.createLinkModelElements(d2,s));
		assertFalse(ad.createLinkModelElements(d2,s2));
		assertFalse(ad.createLinkModelElements(s,a));
		assertFalse(ad.createLinkModelElements(s,a2));
		assertFalse(ad.createLinkModelElements(s,d));
		assertFalse(ad.createLinkModelElements(s,d2));
		assertFalse(ad.createLinkModelElements(s,s2));
		assertFalse(ad.createLinkModelElements(s2,a));
		assertFalse(ad.createLinkModelElements(s2,a2));
		assertFalse(ad.createLinkModelElements(s2,d));
		assertFalse(ad.createLinkModelElements(s2,d2));
		assertFalse(ad.createLinkModelElements(s2,s));
		

		assertFalse(ad.createLinkModelElements(d2,ip));
		assertFalse(ad.createLinkModelElements(d2,fp));
		assertFalse(ad.createLinkModelElements(d2,fp2));
		assertFalse(ad.createLinkModelElements(ip,a));
		assertFalse(ad.createLinkModelElements(ip,a2));
		assertFalse(ad.createLinkModelElements(ip,d));
		assertFalse(ad.createLinkModelElements(ip,d2));
		assertFalse(ad.createLinkModelElements(ip,fp));
		assertFalse(ad.createLinkModelElements(ip,fp2));
		assertFalse(ad.createLinkModelElements(fp,a));
		assertFalse(ad.createLinkModelElements(fp,a2));
		assertFalse(ad.createLinkModelElements(fp,d));
		assertFalse(ad.createLinkModelElements(fp,d2));
		assertFalse(ad.createLinkModelElements(fp,ip));
		assertFalse(ad.createLinkModelElements(fp,fp2));
		assertFalse(ad.createLinkModelElements(fp2,a));
		assertFalse(ad.createLinkModelElements(fp2,a2));
		assertFalse(ad.createLinkModelElements(fp2,d));
		assertFalse(ad.createLinkModelElements(fp2,d2));
		assertFalse(ad.createLinkModelElements(fp2,fp));
		assertFalse(ad.createLinkModelElements(fp2,ip));
	
		

		ad.addModelElement(a);
		assertFalse(ad.createLinkModelElements(a,a2));
		assertFalse(ad.createLinkModelElements(a,d));
		assertFalse(ad.createLinkModelElements(a,d2));

		assertFalse(ad.createLinkModelElements(a,s));
		assertFalse(ad.createLinkModelElements(a,s2));

		assertFalse(ad.createLinkModelElements(a,ip));
		assertFalse(ad.createLinkModelElements(a,fp));
		assertFalse(ad.createLinkModelElements(a,fp2));

		assertFalse(ad.createLinkModelElements(a2,a));
		assertFalse(ad.createLinkModelElements(d,a));
		assertFalse(ad.createLinkModelElements(d2,a));

		assertFalse(ad.createLinkModelElements(s,a));
		assertFalse(ad.createLinkModelElements(s2,a));

		assertFalse(ad.createLinkModelElements(ip,a));
		assertFalse(ad.createLinkModelElements(fp,a));
		assertFalse(ad.createLinkModelElements(fp2,a));

		
		ad.addModelElement(a2);
		assertTrue(ad.createLinkModelElements(a,a2));
		assertTrue(ad.createLinkModelElements(a2,a));
		
		assertFalse(ad.createLinkModelElements(a,a2));
		assertFalse(ad.createLinkModelElements(a2,a));
	
		ad.addModelElement(d);
		ad.addModelElement(d2);
		assertFalse(ad.createLinkModelElements(a,d));
		assertFalse(ad.createLinkModelElements(a2,d));
		assertFalse(ad.createLinkModelElements(a,d2));
		assertFalse(ad.createLinkModelElements(a2,d2));
		assertFalse(ad.createLinkModelElements(d,d2));
		assertFalse(ad.createLinkModelElements(d2,d));
		
		ad.addModelElement(s);
		ad.addModelElement(s2);
		assertFalse(ad.createLinkModelElements(a,s));
		assertFalse(ad.createLinkModelElements(a2,s));
		assertFalse(ad.createLinkModelElements(a,s2));
		assertFalse(ad.createLinkModelElements(a2,s2));
		assertTrue(ad.createLinkModelElements(s,s2));
		assertTrue(ad.createLinkModelElements(s2,s));
			
		ad.addModelElement(a3);
		ad.addModelElement(a4);
		ad.addModelElement(a5);
		assertTrue(ad.createLinkModelElements(a3,d));
		assertFalse(ad.createLinkModelElements(a3,d));			
		assertFalse(ad.createLinkModelElements(a3,d2));
		assertFalse(ad.createLinkModelElements(a3,s));
//		assertFalse(ad.createLinkModelElements(a4,d));
		assertTrue(ad.createLinkModelElements(a4,d));
		assertFalse(ad.createLinkModelElements(a3,a4));

		assertTrue(ad.createLinkModelElements(d,a4));
		assertTrue(ad.createLinkModelElements(d,a5));
		assertFalse(ad.createLinkModelElements(d,a4));
		assertFalse(ad.createLinkModelElements(d,a5));
		
		ad.addModelElement(ip);
		assertTrue(ad.createLinkModelElements(ip,a));
		assertFalse(ad.createLinkModelElements(ip,d));
		assertFalse(ad.createLinkModelElements(ip,a2));
		assertFalse(ad.createLinkModelElements(ip,fp));
		assertFalse(ad.createLinkModelElements(a,ip));
		assertFalse(ad.createLinkModelElements(d,ip));
		assertFalse(ad.createLinkModelElements(fp,ip));
		
		ad.addModelElement(fp);
		ad.addModelElement(fp2);
		assertFalse(ad.createLinkModelElements(fp,a));
		assertFalse(ad.createLinkModelElements(fp,d));
		assertFalse(ad.createLinkModelElements(fp,ip));
		assertFalse(ad.createLinkModelElements(fp,fp2));
		assertFalse(ad.createLinkModelElements(fp,a2));
		assertFalse(ad.createLinkModelElements(fp,d2));
		assertFalse(ad.createLinkModelElements(ip,fp));
		assertFalse(ad.createLinkModelElements(a,fp));
		assertTrue(ad.createLinkModelElements(d,fp));
		assertFalse(ad.createLinkModelElements(a2,fp));
		assertTrue(ad.createLinkModelElements(d2,fp));
		assertFalse(ad.createLinkModelElements(fp2,fp));
		
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

		
		assertFalse(ad.removeLinkModelElements(a,a2));
		assertFalse(ad.removeLinkModelElements(a,a2));
		assertFalse(ad.removeLinkModelElements(a,d));
		assertFalse(ad.removeLinkModelElements(a,d2));
		assertFalse(ad.removeLinkModelElements(a,s));
		assertFalse(ad.removeLinkModelElements(a,s2));
		assertFalse(ad.removeLinkModelElements(a2,a));
		assertFalse(ad.removeLinkModelElements(a2,a2));
		assertFalse(ad.removeLinkModelElements(a2,d));
		assertFalse(ad.removeLinkModelElements(a2,d2));
		assertFalse(ad.removeLinkModelElements(a2,s));
		assertFalse(ad.removeLinkModelElements(a2,s2));
		assertFalse(ad.removeLinkModelElements(d,a));
		assertFalse(ad.removeLinkModelElements(d,a2));
		assertFalse(ad.removeLinkModelElements(d,d));
		assertFalse(ad.removeLinkModelElements(d,d2));
		assertFalse(ad.removeLinkModelElements(d,s));
		assertFalse(ad.removeLinkModelElements(d,s2));
		assertFalse(ad.removeLinkModelElements(d2,a));
		assertFalse(ad.removeLinkModelElements(d2,a2));
		assertFalse(ad.removeLinkModelElements(d2,d));
		assertFalse(ad.removeLinkModelElements(d2,d2));

		assertFalse(ad.removeLinkModelElements(d2,s));
		assertFalse(ad.removeLinkModelElements(d2,s2));

		assertFalse(ad.removeLinkModelElements(ip,a));
		assertFalse(ad.removeLinkModelElements(ip,fp));
		assertFalse(ad.removeLinkModelElements(ip,d));
		assertFalse(ad.removeLinkModelElements(a,ip));
		assertFalse(ad.removeLinkModelElements(fp,ip));
		assertFalse(ad.removeLinkModelElements(d,ip));
		assertFalse(ad.removeLinkModelElements(fp,a));
		assertFalse(ad.removeLinkModelElements(fp,ip));
		assertFalse(ad.removeLinkModelElements(fp,d));
		assertFalse(ad.removeLinkModelElements(fp,fp2));
		assertFalse(ad.removeLinkModelElements(fp2,fp));
		assertFalse(ad.removeLinkModelElements(a,fp));
		assertFalse(ad.removeLinkModelElements(d,fp));
		assertFalse(ad.removeLinkModelElements(ip,fp));
		


		ad.addModelElement(a);
		ad.addModelElement(a2);
		ad.addModelElement(d);
		ad.addModelElement(d2);

		ad.addModelElement(s);
		ad.addModelElement(s2);

		ad.addModelElement(ip);
		ad.addModelElement(fp);
		ad.addModelElement(fp2);
		

		ad.createLinkModelElements(a,a2);
		assertFalse(ad.removeLinkModelElements(a2,a));
		assertTrue(ad.removeLinkModelElements(a,a2));
		
		ad.createLinkModelElements(a2,a);
		assertFalse(ad.removeLinkModelElements(a,a2));
		assertTrue(ad.removeLinkModelElements(a2,a));

		ad.createLinkModelElements(d,a);
		assertFalse(ad.removeLinkModelElements(a,d));
		assertTrue(ad.removeLinkModelElements(d,a));

		ad.createLinkModelElements(a2,d);
		assertFalse(ad.removeLinkModelElements(d,a2));
		assertTrue(ad.removeLinkModelElements(a2,d));
		
		ad.createLinkModelElements(d,d2);
		assertFalse(ad.removeLinkModelElements(d2,d));
		assertFalse(ad.removeLinkModelElements(d,d2));

		
		ad.createLinkModelElements(s,s2);
		assertFalse(ad.removeLinkModelElements(s2,s));
		assertTrue(ad.removeLinkModelElements(s,s2));

		
		ad.createLinkModelElements(a,ip);
		assertFalse(ad.removeLinkModelElements(ip,a));
		assertFalse(ad.removeLinkModelElements(a,ip));
		
		ad.createLinkModelElements(d,ip);
		assertFalse(ad.removeLinkModelElements(ip,d));
		assertFalse(ad.removeLinkModelElements(d,ip));
		
		ad.createLinkModelElements(ip,fp);
		assertFalse(ad.removeLinkModelElements(ip,fp));
		assertFalse(ad.removeLinkModelElements(fp,ip));
		
		ad.createLinkModelElements(ip,a);
		assertFalse(ad.removeLinkModelElements(a,ip));
		assertTrue(ad.removeLinkModelElements(ip,a));
		
		ad.createLinkModelElements(ip,d);
		assertFalse(ad.removeLinkModelElements(d,ip));
		assertTrue(ad.removeLinkModelElements(ip,d));
		
		ad.createLinkModelElements(a,fp);
		assertFalse(ad.removeLinkModelElements(fp,a));
		assertTrue(ad.removeLinkModelElements(a,fp));
		
		ad.createLinkModelElements(d,fp);
		assertFalse(ad.removeLinkModelElements(fp,d));
		assertTrue(ad.removeLinkModelElements(d,fp));
		
		ad.createLinkModelElements(fp,a);
		assertFalse(ad.removeLinkModelElements(fp,a));
		assertFalse(ad.removeLinkModelElements(a,fp));
		
		ad.createLinkModelElements(fp,d);
		assertFalse(ad.removeLinkModelElements(fp,d));
		assertFalse(ad.removeLinkModelElements(d,fp));
		
		ad.createLinkModelElements(fp,ip);
		assertFalse(ad.removeLinkModelElements(ip,fp));
		assertFalse(ad.removeLinkModelElements(fp,ip));
		
		ad.createLinkModelElements(fp,fp2);
		assertFalse(ad.removeLinkModelElements(fp,fp2));
		assertFalse(ad.removeLinkModelElements(fp2,fp));
		

	}
	
	public void testAreLinkableActivityActivity()
	{
		ActivityDiagram ad = new ActivityDiagram();
		Activity a = createActivity();
		Activity a2 = createActivity();
		Activity a3 = createActivity();
		ActivityDiagram.Decision d = createDecision();
		ActivityDiagram.Synchro s= createSynchro();
		
		assertFalse(ad.areLinkableModelElements(a,a2));
		assertFalse(ad.areLinkableModelElements(a2,a));
		
		ad.addModelElement(a);
		
		assertFalse(ad.areLinkableModelElements(a,a2));
		assertFalse(ad.areLinkableModelElements(a2,a));
		
		ad.addModelElement(a2);	
		
		assertTrue(ad.areLinkableModelElements(a,a2));
		assertTrue(ad.areLinkableModelElements(a2,a));
		
		ad.createLinkModelElements(a,a2);
		assertFalse(ad.areLinkableModelElements(a,a2));
		
		ad.addModelElement(a3);
		assertFalse(ad.areLinkableActivityActivity(a,a3));
		
		ad.addModelElement(d);
		ad.createLinkModelElements(a2,d);
		assertFalse(ad.areLinkableActivityActivity(a2,a3));
		
		ad.addModelElement(s);
		ad.createLinkModelElements(a2,s);
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
		
		ad.createLinkModelElements(a,d);
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
		
		ad.createLinkModelElements(d,a);
		assertFalse(ad.areLinkableDecisionActivity(d,a));
		assertTrue(ad.areLinkableDecisionActivity(d,a2));
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
		
		ad.createLinkModelElements(ip,a);
		assertFalse(ad.areLinkableInitialPointModelElement(ip,a));
		assertFalse(ad.areLinkableInitialPointModelElement(ip,d));
		
		ad.createLinkModelElements(ip,fp);
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
		
		ad.createLinkModelElements(ip,d);
		assertFalse(ad.areLinkableInitialPointDecision(ip,d));
		assertFalse(ad.areLinkableInitialPointModelElement(ip,a));
		
		ad.removeLinkModelElements(ip,d);
		ad.createLinkModelElements(a,d);
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
		
		ad.createLinkModelElements(s,d);
		assertFalse(ad.areLinkableSynchroDecision(s,d));
		
		ad.removeLinkModelElements(s,d);
		ad.createLinkModelElements(a,d);
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
		
		ad.createLinkModelElements(a,fp);
		assertFalse(ad.areLinkableModelElementFinalPoint(a,fp));
		assertTrue(ad.areLinkableModelElementFinalPoint(d,fp));
		
		
		ad.createLinkModelElements(ip,fp);
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
		

		assertFalse(ad.areLinkableModelElements(a,a2));
		assertFalse(ad.areLinkableModelElements(a2,a));
		
		ad.addModelElement(a);
		
		assertFalse(ad.areLinkableModelElements(a,a2));
		assertFalse(ad.areLinkableModelElements(a2,a));
		
		ad.addModelElement(a2);	
		
		assertTrue(ad.areLinkableModelElements(a,a2));
		assertTrue(ad.areLinkableModelElements(a2,a));
		
		ad.createLinkModelElements(a,a2);
		assertFalse(ad.areLinkableModelElements(a,a2));
		
		ad.addModelElement(d);
		ad.addModelElement(d2);
		
		assertFalse(ad.areLinkableModelElements(d,d2));
		assertFalse(ad.areLinkableModelElements(d2,d));
		assertFalse(ad.areLinkableModelElements(a,d));
		assertTrue(ad.areLinkableModelElements(d,a));
		
		ad.createLinkModelElements(a2,d);
		assertFalse(ad.areLinkableModelElements(a2,d));
		assertFalse(ad.areLinkableModelElements(a3,d));
		
		ad.addModelElement(a3);
		assertTrue(ad.areLinkableModelElements(d,a3));
		
		ad.createLinkModelElements(d,a3);
		assertTrue(ad.areLinkableModelElements(d,a));

		
		ad.addModelElement(s);
		assertTrue(ad.areLinkableModelElements(s,a3));
		assertTrue(ad.areLinkableModelElements(s,a));

		
		ad.addModelElement(ip);
		assertFalse(ad.areLinkableModelElements(a,ip));
		assertFalse(ad.areLinkableModelElements(d,ip));
		assertTrue(ad.areLinkableModelElements(ip,a));
		ad.createLinkModelElements(ip,a);
		assertFalse(ad.areLinkableModelElements(ip,a));
		assertFalse(ad.areLinkableModelElements(ip,a2));
		assertFalse(ad.areLinkableModelElements(ip,d));
		
		ad.addModelElement(fp);
		ad.addModelElement(fp2);
		assertFalse(ad.areLinkableModelElements(fp,a));
		assertFalse(ad.areLinkableModelElements(fp,d));
		assertFalse(ad.areLinkableModelElements(fp,ip));
		assertFalse(ad.areLinkableModelElements(fp,fp2));
		assertFalse(ad.areLinkableModelElements(ip,fp));
		assertFalse(ad.areLinkableModelElements(a,fp));
		ad.createLinkModelElements(a,fp);
		assertFalse(ad.areLinkableModelElements(a,fp));
		assertFalse(ad.areLinkableModelElements(a2,fp));
		assertTrue(ad.areLinkableModelElements(d,fp));
		ad.createLinkModelElements(d,fp);
		assertFalse(ad.areLinkableModelElements(d,fp));
		assertTrue(ad.areLinkableModelElements(d2,fp));

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
		
		assertFalse(ad.existsLinkModelElements(a,a2));
		assertFalse(ad.existsLinkModelElements(a2,a));
		assertFalse(ad.existsLinkModelElements(d,a));
		assertFalse(ad.existsLinkModelElements(a,d));

		assertFalse(ad.existsLinkModelElements(a,s));
		assertFalse(ad.existsLinkModelElements(s,a));

		assertFalse(ad.existsLinkModelElements(ip,a));
		assertFalse(ad.existsLinkModelElements(ip,d));
		assertFalse(ad.existsLinkModelElements(a,fp));
		assertFalse(ad.existsLinkModelElements(d,fp));

		
		ad.createLinkModelElements(a,a2);
		assertTrue(ad.existsLinkModelElements(a,a2));
		assertFalse(ad.existsLinkModelElements(a2,a));
		
		ad.removeLinkModelElements(a,a2);
		assertFalse(ad.existsLinkModelElements(a,a2));
		
		ad.createLinkModelElements(a,d);
		ad.createLinkModelElements(d,a2);
		assertTrue(ad.existsLinkModelElements(a,d));
		assertFalse(ad.existsLinkModelElements(d,a));
		assertTrue(ad.existsLinkModelElements(d,a2));
		assertFalse(ad.existsLinkModelElements(a2,d));

		
		ad.createLinkModelElements(a,s);
		ad.createLinkModelElements(s,a2);
		assertFalse(ad.existsLinkModelElements(a,s));
		assertFalse(ad.existsLinkModelElements(s,a));
		assertTrue(ad.existsLinkModelElements(s,a2));
		assertFalse(ad.existsLinkModelElements(a2,s));

		
		ad.createLinkModelElements(ip,a);
		assertFalse(ad.existsLinkModelElements(ip,a));
		assertFalse(ad.existsLinkModelElements(a,ip));
		
		ad.createLinkModelElements(ip,d);
		assertFalse(ad.existsLinkModelElements(ip,d));
		assertFalse(ad.existsLinkModelElements(d,ip));
		
		ad.createLinkModelElements(a,fp);
		assertFalse(ad.existsLinkModelElements(a,fp));
		assertFalse(ad.existsLinkModelElements(fp,a));
		
		ad.createLinkModelElements(d,fp);
		assertFalse(ad.existsLinkModelElements(d,fp));
		assertFalse(ad.existsLinkModelElements(fp,d));	

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
		assertTrue(ad.createLinkModelElements(a,a2));
		
		assertFalse(ad.setLinkLabel(a2,a,"false test"));
		assertTrue(ad.setLinkLabel(a,a2,"true test"));
		
		ad.removeLinkModelElements(a,a2);
		ad.createLinkModelElements(a,d);
		ad.createLinkModelElements(d,a2);
		ad.createLinkModelElements(a,s);
		ad.createLinkModelElements(s,a2);
		
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
		ad.createLinkModelElements(a,a2);
		
		assertTrue(ad.getLinkLabel(a,a2)=="");
		assertTrue(ad.getLinkLabel(a2,a)=="");
		ad.setLinkLabel(a,a2,"true test");
		
		assertTrue(ad.getLinkLabel(a,a2)=="true test");
		assertTrue(ad.getLinkLabel(a2,a)=="");
		
		ad.removeLinkModelElements(a,a2);
		ad.createLinkModelElements(a,d);
		ad.createLinkModelElements(d,a2);
		ad.createLinkModelElements(s,a2);
		
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
