/*
 * APES is a Process Engineering Software
 * Copyright (C) 2002-2003 IPSquad
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


package apes.adapters;

import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;

import junit.framework.TestCase;

import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

public class TestSpemTreeAdapter extends TestCase
{
	SpemTreeAdapter adapt;
	/*ApesProcess proc;
	ProcessComponent component;
	SPackage p1;
	SPackage p2;
	SPackage p3;
	ApesWorkDefinition w1;
	Activity act;
	ProcessRole role;
	WorkProduct prod;
	ContextDiagram context;*/
	ApesTreeNode node1;
	ApesTreeNode node11 = new ApesTreeNode(new Activity("node11"),false);
	ApesTreeNode node12 = new ApesTreeNode(new SPackage("node12"),true);
	ApesTreeNode node21 = new ApesTreeNode(new ProcessRole("node21"),false);
	ApesTreeNode node22 = new ApesTreeNode(new WorkProduct("node22"),false);
	
	public void setUp()
	{
		ApesMediator.getInstance().clearListeners();
		
		adapt = new SpemTreeAdapter();
		adapt.setRoot(new ApesProcess("node1"));
		node1 = (ApesTreeNode)adapt.getRoot();
		
		//remove the context diagram for the test
		
		node1.add(node11);
		node1.add(node12);
		node12.add(node21);
		node12.add(node22);
		//adapt.setRoot(createProcess());
	}
	
	/*private ApesProcess createProcess()
	{
		proc = new ApesProcess("proc");
		component = new ProcessComponent("component");
		proc.addModelElement(component);
		context = new ContextDiagram("context");
		component.addModelElement(context);
		
		//component = new ProcessComponent("component");
		p1 = new SPackage("p1");
		p2 = new SPackage("p2");
		p3 = new SPackage("p3");
		w1 = new ApesWorkDefinition("w1");
		act = new Activity("act");
		role = new ProcessRole("role");
		prod = new WorkProduct("prod");
		
		//proc.addModelElement(component);
		component.addModelElement(p1);
		component.addModelElement(p2);
		component.addModelElement(p3);
		
		w1.addModelElement(act);
		p1.addModelElement(w1);
		p2.addModelElement(role);
		p3.addModelElement(prod);
		
		return proc;
	}*/
	
	
	public void testGetChild()
	{
		assertTrue(adapt.getChild(new Object(), -1) == null);
		assertTrue(adapt.getChild(new Object(), 0) == null);
		assertTrue(adapt.getChild(new Object(), 1) == null);
		
		assertTrue(adapt.getChild(node1, -1) == null);
		assertTrue(adapt.getChild(node1, 0) == node11);
		assertTrue(adapt.getChild(node1, 1) == node12);
		assertTrue(adapt.getChild(node1, 2) == null);
		
		assertTrue(adapt.getChild(node11, -1) == null);
		assertTrue(adapt.getChild(node11, 0) ==null);
		
		assertTrue(adapt.getChild(node12, -1) == null);
		assertTrue(adapt.getChild(node12, 0) == node21);
		assertTrue(adapt.getChild(node12, 1) == node22);
		assertTrue(adapt.getChild(node12, 2) == null);
		
		assertTrue(adapt.getChild(node21, -1) == null);
		assertTrue(adapt.getChild(node22, 0) == null);
	}
	
	public void testGetChildCount()
	{
		assertTrue(adapt.getChildCount(new Object()) == -1);
		assertEquals(adapt.getChildCount(node1), 2);
		assertEquals(adapt.getChildCount(node11), 0);
		assertEquals(adapt.getChildCount(node12), 2);
		assertEquals(adapt.getChildCount(node21), 0);
		assertEquals(adapt.getChildCount(node22), 0);
	}
	
	public void testIsLeaf()
	{
		assertFalse(adapt.isLeaf(node1));
		assertTrue(adapt.isLeaf(node11));
		assertFalse(adapt.isLeaf(node12));
		assertTrue(adapt.isLeaf(node21));
		assertTrue(adapt.isLeaf(node22));
	}
	
	public void testValueForPathChanged()
	{
		//Can not rename the tree root
		assertTrue(node1.getName().equals("node1"));
		adapt.valueForPathChanged(new TreePath(new Object[]{node1}), "Node1");
		assertTrue(node1.getName().equals("node1"));


		assertTrue(node11.getName().equals("node11"));
		adapt.valueForPathChanged(new TreePath(new Object[]{node1, node11}), "Node11");
		assertTrue(node11.getName().equals("Node11"));

		assertTrue(node12.getName().equals("node12"));
		adapt.valueForPathChanged(new TreePath(new Object[]{node1, node12 }), "Node12");
		assertTrue(node12.getName().equals("Node12"));

		assertTrue(node21.getName().equals("node21"));
		adapt.valueForPathChanged(new TreePath(new Object[]{node1, node12, node21}), "Node21");
		assertTrue(node21.getName().equals("Node21"));

		assertTrue(node22.getName().equals("node22"));
		adapt.valueForPathChanged(new TreePath(new Object[]{node1, node12, node22}), "Node22");
		assertTrue(node22.getName().equals("Node22"));
	}
	
	public void testGetIndexOfChild()
	{
		assertEquals(adapt.getIndexOfChild(node1, node11), 0);
		assertEquals(adapt.getIndexOfChild(node1, node12), 1);
		assertEquals(adapt.getIndexOfChild(node12, node21), 0);
		assertEquals(adapt.getIndexOfChild(node12, node22), 1);
		
		assertEquals(adapt.getIndexOfChild(node1, node21), -1);
		assertEquals(adapt.getIndexOfChild(node1, node22), -1);
		assertEquals(adapt.getIndexOfChild(node1, node1), -1);
		assertEquals(adapt.getIndexOfChild(node11, node1), -1);
		assertEquals(adapt.getIndexOfChild(node11, node12), -1);
		assertEquals(adapt.getIndexOfChild(node11, node21), -1);
		assertEquals(adapt.getIndexOfChild(node11, node22), -1);
		assertEquals(adapt.getIndexOfChild(node12, node1), -1);
		assertEquals(adapt.getIndexOfChild(node12, node12), -1);
		assertEquals(adapt.getIndexOfChild(node21, node1), -1);
		assertEquals(adapt.getIndexOfChild(node21, node11), -1);
		assertEquals(adapt.getIndexOfChild(node21, node12), -1);
		assertEquals(adapt.getIndexOfChild(node21, node22), -1);
		assertEquals(adapt.getIndexOfChild(node21, node21), -1);
		assertEquals(adapt.getIndexOfChild(node22, node1), -1);
		assertEquals(adapt.getIndexOfChild(node22, node11), -1);
		assertEquals(adapt.getIndexOfChild(node22, node12), -1);
		assertEquals(adapt.getIndexOfChild(node22, node21), -1);
		assertEquals(adapt.getIndexOfChild(node22, node22), -1);
		
		assertEquals(adapt.getIndexOfChild(node1, new Object()), -1);
	}
	
	public void testGetPathToRoot()
	{
		assertTrue(arraysEquals(adapt.getPathToRoot(node1), 
				new TreeNode[]{node1}));
		assertTrue(arraysEquals(adapt.getPathToRoot(node11), 
				new TreeNode[]{node1, node11}));
		assertTrue(arraysEquals(adapt.getPathToRoot(node12), 
				new TreeNode[]{node1, node12}));
		assertTrue(arraysEquals(adapt.getPathToRoot(node21), 
				new TreeNode[]{node1, node12, node21}));
		assertTrue(arraysEquals(adapt.getPathToRoot(node22), 
				new TreeNode[]{node1, node12, node22}));
		
		ApesTreeNode test = new ApesTreeNode(null, false);
		assertTrue(arraysEquals(adapt.getPathToRoot(test), new ApesTreeNode[]{test}));
	}
	
	private boolean arraysEquals(Object[] array1, Object[] array2)
	{
		if(array1.length!=array2.length) return false;

		for(int i=0; i<array1.length; i++)
		{
			if(!array1[i].equals(array2[i]))
			{
				return false;
			}
		}

		return true;
	}

}
