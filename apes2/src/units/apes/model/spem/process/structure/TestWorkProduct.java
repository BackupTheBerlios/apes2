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

import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;

import junit.framework.TestCase;

public class TestWorkProduct extends TestCase
{
	WorkProduct workprod = new WorkProduct("produit_de_travail");
	WorkDefinition workdef = new WorkDefinition("définition_de_travail");
	WorkDefinition workdef2 = new WorkDefinition("définition_de_travail2");
	ProcessPerformer properf = new ProcessPerformer("proc_perf");
	
	
	public void testWorkProduct() {
		assertTrue(workprod.getName()=="produit_de_travail");	
	}
	
	public void testgetReferences() {
		workprod.setReferences(1);
		assertTrue(workprod.getReferences()==1);	
	}
	
	public void testgetResponsible() {
		workprod.setResponsible(properf);
		assertTrue(workprod.getResponsible()==properf);	
	}
	
	public void testaddInputWorkDefinition() {
		assertTrue(workprod.addInputWorkDefinition(workdef));	
		workprod.addInputWorkDefinition(workdef);
		assertFalse(workprod.addInputWorkDefinition(workdef));		
	}
	
	public void testremoveInputWorkDefinition() {
		assertFalse(workprod.removeInputWorkDefinition(workdef));	
		workprod.addInputWorkDefinition(workdef);
		assertTrue(workprod.removeInputWorkDefinition(workdef));		
	}
	
	public void testcontainsInputWorkDefinition() {
		assertFalse(workprod.containsInputWorkDefinition(workdef));	
		workprod.addInputWorkDefinition(workdef);
		assertTrue(workprod.containsInputWorkDefinition(workdef));		
	}
	
	public void testaddOutputWorkDefinition() {
		assertTrue(workprod.addOutputWorkDefinition(workdef));	
		workprod.addOutputWorkDefinition(workdef);
		assertFalse(workprod.addOutputWorkDefinition(workdef));		
	}
	
	public void testremoveOutputWorkDefinition() {
		assertFalse(workprod.removeOutputWorkDefinition(workdef));	
		workprod.addOutputWorkDefinition(workdef);
		assertTrue(workprod.removeOutputWorkDefinition(workdef));		
	}
	
	public void testcontainsOutputWorkDefinition() {
		assertFalse(workprod.containsOutputWorkDefinition(workdef));	
		workprod.addOutputWorkDefinition(workdef);
		assertTrue(workprod.containsOutputWorkDefinition(workdef));		
	}
	
	public void testgetInputCount() {
		assertTrue(workprod.getInputCount()==0);	
		workprod.addInputWorkDefinition(workdef);
		workprod.addInputWorkDefinition(workdef2);
		assertTrue(workprod.getInputCount()==2);	
		assertFalse(workprod.getInputCount()==1);	
	}
	
	public void testgetOutputCount() {
		assertTrue(workprod.getOutputCount()==0);	
		workprod.addOutputWorkDefinition(workdef);
		workprod.addOutputWorkDefinition(workdef2);
		assertTrue(workprod.getOutputCount()==2);	
		assertFalse(workprod.getOutputCount()==1);	
	}
	
	public void testgetInput() {
		workprod.addInputWorkDefinition(workdef);
		assertTrue(workprod.getInput(0)==workdef);
		workprod.addInputWorkDefinition(workdef2);
		assertFalse(workprod.getInput(1)==workdef);
		assertTrue(workprod.getInput(1)==workdef2);
	}
	
	public void testgetOutput() {
		workprod.addOutputWorkDefinition(workdef);
		assertTrue(workprod.getOutput(0)==workdef);
		workprod.addOutputWorkDefinition(workdef2);
		assertFalse(workprod.getOutput(1)==workdef);
		assertTrue(workprod.getOutput(1)==workdef2);
	}
	
	public void testaddModelElement() {
		StateMachine stm = new StateMachine("stm");
		assertTrue(workprod.addModelElement(stm));
		assertFalse(workprod.addModelElement(workdef));
	}
	
	public void testremoveModelElement() {
		StateMachine stm = new StateMachine("stm");
		assertFalse(workprod.removeModelElement(workdef));
		workprod.addModelElement(stm);
		assertFalse(workprod.removeModelElement(workdef));
		assertTrue(workprod.removeModelElement(stm));
	}
	
	public void testcontainsModelElement() {
		StateMachine stm = new StateMachine("stm");
		assertFalse(workprod.containsModelElement(workdef));
		workprod.addModelElement(stm);
		assertTrue(workprod.containsModelElement(stm));
	}
	
	public void testgetModelElement() {
		StateMachine stm = new StateMachine("stm");
		StateMachine stm2 = new StateMachine("stm2");
		workprod.addModelElement(stm);
		assertFalse(workprod.getModelElement(1)==stm);
		assertTrue(workprod.getModelElement(0)==stm);
		workprod.addModelElement(stm2);
		assertTrue(workprod.getModelElement(1)==stm2);
		assertFalse(workprod.getModelElement(1)==stm);
	}
	
	public void testmodelElementCount() {
		StateMachine stm = new StateMachine("stm");
		StateMachine stm2 = new StateMachine("stm2");
		workprod.addModelElement(stm);
		workprod.addModelElement(stm2);
		assertTrue(workprod.modelElementCount()==2);
		assertFalse(workprod.modelElementCount()==1);
	}
	
}
