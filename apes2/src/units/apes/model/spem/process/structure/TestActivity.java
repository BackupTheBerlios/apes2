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

import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

import junit.framework.TestCase;

public class TestActivity extends TestCase
{
	Activity activ = new Activity("activité");
	WorkProduct wp = new WorkProduct("produit_de_travail");
	WorkProduct wp1 = new WorkProduct("produit_de_travail1");
	WorkProduct wp2 = new WorkProduct("produit_de_travail2");
	
	public void testActivity() { 
		assertTrue(activ.getName()=="activité");
	}
	
	public void testaddInputWorkProduct() { 
		activ.addInputWorkProduct(wp);		
		assertFalse(activ.addInputWorkProduct(wp));
	}
	
	public void testremoveInputWorkProduct() { 
		assertFalse(activ.removeInputWorkProduct(wp));
		activ.addInputWorkProduct(wp);		
		assertTrue(activ.removeInputWorkProduct(wp));
	}
	
	public void testcontainsInputWorkProduct() { 
		assertFalse(activ.containsInputWorkProduct(wp));
		activ.addInputWorkProduct(wp);		
		assertTrue(activ.containsInputWorkProduct(wp));
	}
	
	public void testaddOutputWorkProduct() { 
		assertTrue(activ.addOutputWorkProduct(wp1));
		assertFalse(activ.addOutputWorkProduct(wp1));
	}
	
	public void testremoveOutputWorkProduct() { 
		assertFalse(activ.removeOutputWorkProduct(wp));
		activ.addOutputWorkProduct(wp);
		assertTrue(activ.removeOutputWorkProduct(wp));
	}
	
	public void testcontainsOutputWorkProduct() { 
		assertFalse(activ.containsOutputWorkProduct(wp));
		activ.addOutputWorkProduct(wp);
		assertTrue(activ.containsOutputWorkProduct(wp));
	}
	
	public void testgetInputCount() { 
		activ.addInputWorkProduct(wp1);
		activ.addInputWorkProduct(wp2);
		assertFalse(activ.getInputCount()==5);
		assertTrue(activ.getInputCount()==2);
	}
	
	public void testgetOutputCount() { 
		activ.addOutputWorkProduct(wp1);
		activ.addOutputWorkProduct(wp2);
		assertFalse(activ.getOutputCount()==5);
		assertTrue(activ.getOutputCount()==2);
	}
	
	public void testgetInput() { 
		activ.addInputWorkProduct(wp1);
		activ.addInputWorkProduct(wp2);
		assertFalse(activ.getInput(0)==wp2);
		assertTrue(activ.getInput(1)==wp2);
		assertFalse(activ.getInput(1)==wp1);
		assertTrue(activ.getInput(0)==wp1);
	}
	
	public void testgetOutput() { 
		activ.addOutputWorkProduct(wp1);
		activ.addOutputWorkProduct(wp2);
		assertFalse(activ.getOutput(0)==wp2);
		assertTrue(activ.getOutput(1)==wp2);
		assertFalse(activ.getOutput(1)==wp1);
		assertTrue(activ.getOutput(0)==wp1);
	}
	
}
