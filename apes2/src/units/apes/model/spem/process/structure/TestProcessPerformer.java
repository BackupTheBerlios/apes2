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


package apes.model.spem.process.structure;

import junit.framework.TestCase;

import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestProcessPerformer extends TestCase
{
	ProcessPerformer proPerf = new ProcessPerformer("process_perf");
	ProcessPerformer proPerf2 = new ProcessPerformer("process_perf");
	WorkDefinition wd = new WorkDefinition("définition_de_travail");
	WorkProduct wp = new WorkProduct("produit_de_travail");
	WorkDefinition wd1 = new WorkDefinition("définition_de_travail1");
	WorkDefinition wd2 = new WorkDefinition("définition_de_travail2");
	WorkProduct wp1 = new WorkProduct("produit_de_travail1");
	WorkProduct wp2 = new WorkProduct("produit_de_travail2");
	Activity activ = new Activity("activité");
	
	public void testProcessPerformer() { 
		assertTrue(proPerf.getName()=="process_perf");
	}
	
	public void testaddFeature() { 
		assertTrue(proPerf.addFeature(wd));
		assertFalse(proPerf.addFeature(wd));
	}
	
	public void testremoveFeature() { 
		assertFalse(proPerf.removeFeature(wd));
		proPerf.addFeature(wd);
		assertTrue(proPerf.removeFeature(wd));
	}
	
	public void testcontainsFeature() { 
		assertFalse(proPerf.containsFeature(wd));
		proPerf.addFeature(wd);
		assertTrue(proPerf.containsFeature(wd));
	}
	
	public void testaddResponsibility() { 
		assertTrue(proPerf.addResponsibility(wp));
		assertFalse(proPerf.containsFeature(wd));
	}
	
	public void testremoveResponsibility() { 
		assertFalse(proPerf.removeResponsibility(wp));
		proPerf.addResponsibility(wp);
		assertTrue(proPerf.removeResponsibility(wp));
	}
	
	public void testcontainsResponsibility() { 
		assertFalse(proPerf.containsResponsibility(wp));
		proPerf.addResponsibility(wp);
		assertTrue(proPerf.containsResponsibility(wp));
	}
	
	public void testgetFeatureCount() { 
		proPerf.addFeature(wd);
		assertFalse(proPerf.getFeatureCount()==0);
		assertTrue(proPerf.getFeatureCount()==1);
		proPerf.addFeature(wd2);
		assertFalse(proPerf.getFeatureCount()==0);
		assertTrue(proPerf.getFeatureCount()==2);
	}
	
	public void testgetResponsibilityCount() { 
		
		proPerf.addResponsibility(wp1);
		proPerf.addResponsibility(wp1);
		assertFalse(proPerf.getResponsibilityCount()==0);
		assertTrue(proPerf.getResponsibilityCount()==1);
		proPerf.addResponsibility(wp2);
		assertFalse(proPerf.getResponsibilityCount()==0);
		assertTrue(proPerf.getResponsibilityCount()==2);
	}
	
	public void testgetFeature() { 
		proPerf.addFeature(wd1);
		proPerf.addFeature(wd2);
		assertFalse(proPerf.getFeature(0)==wd2);
		assertTrue(proPerf.getFeature(0)==wd1);
		assertTrue(proPerf.getFeature(1)==wd2);
	}
	
	public void testgetResponsibility() { 
		proPerf.addResponsibility(wp1);
		proPerf.addResponsibility(wp2);
		assertFalse(proPerf.getResponsibility(0)==wp2);
		assertTrue(proPerf.getResponsibility(0)==wp1);
		assertTrue(proPerf.getResponsibility(1)==wp2);
	}
	
	public void testequals() { 
		assertFalse(proPerf.equals(activ));
		assertTrue(proPerf.equals(proPerf2));
	}
	
}
