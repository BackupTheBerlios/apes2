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

package apes.adapters;

import java.util.Iterator;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.ipsquad.utils.ConfigManager;
import org.jgraph.graph.GraphModel;

public class TestFlowGraphAdapter extends TestCase
{
	static SpemTreeAdapter model;
	static FlowDiagram diagram1;
	static FlowGraphAdapter adapter1;
	static WorkProductCell wc1;
	static WorkProductCell wc2;
	static ActivityCell ac1;
	static ActivityCell ac2;
	static ProcessRoleCell pc1;
	static ProcessRoleCell pc2;	
	
	//public TestSpemGraphAdapter()
	static {
		ConfigManager.init(ApesMain.createDefaultProperties());
		
		Context context = Context.getInstance();
		
		model = new SpemTreeAdapter();
		diagram1 = new FlowDiagram();
		adapter1 = new FlowGraphAdapter(diagram1);
		
		context.setTopLevelFrame(new MainFrameInterface()
		{
			private JTree tree = new JTree(model);
			
			public JTree getTree() { return tree; }
			public JDesktopPane getDesktop() { return new JDesktopPane(); }
			public void setToolPalette(ToolPalette toolPalette) { fail(); }
			public ToolPalette getToolPalette() { fail(); return null; }
			public void openDiagram(GraphModel model) { fail(); }
			public void deleteDiagram(GraphModel model) { fail(); }
			public void setDefaultToolPalette() { fail(); }
			public void setFilePath(String filePath) { }
			public GraphFrame getGraphFrame(GraphModel model) { fail(); return null; }
			
		});

		context.setProject(new Project());
		ApesMediator.getInstance().addApesModelListener( adapter1 );
		ApesMediator.getInstance().registerDiagram( diagram1 );
		
		context.getProject().getProcess().getComponent().addModelElement(diagram1);
	}	
	
	public void testInsertCell()
	{
		/*ApesMediator.getInstance().addApesModelListener( adapter1 );
		
		adapter1.insert(new Object[]{new DefaultGraphCell()}, null, null, null, null);
		assertEquals(adapter1.getRootCount(), 0);
		adapter1.insert(new Object[]{new WorkDefinitionCell()}, null, null, null, null);
		assertEquals(adapter1.getRootCount(), 0);
		
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==0);
		
		//insert one activity to adapter1
		ac1 = (ActivityCell)adapter1.associateGraphCell(new Activity());
		Map attr = ac1.getAttributes();
		Hashtable attributes = new Hashtable();
		attributes.put("Attributes", attr);

		adapter1.insert(new Object[]{ac1},attributes, null, null, null);
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==1);
		//insert one activity to adapter1
		ac2 = (ActivityCell)adapter1.associateGraphCell(new Activity());
		attr = ac2.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);

		adapter1.insertCell(ac2,attributes);
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==2);
		//insert one WorkProduct to adapter1
		WorkProduct w = new WorkProduct();
		wc1 = (WorkProductCell)adapter1.associateGraphCell(w);
		attr = wc1.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);

		adapter1.insertCell(wc1,attributes);
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==3);
		//insert one WorkProduct to adapter1
		w = new WorkProduct();
		wc2 = (WorkProductCell)adapter1.associateGraphCell(w);
		attr = wc2.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);

		adapter1.insertCell(wc2,attributes);
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==4);
		//insert one WorkProduct to adapter1
		ProcessRole p = new ProcessRole();
		pc1 = (ProcessRoleCell)adapter1.associateGraphCell(p);
		attr = pc1.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);

		adapter1.insertCell(pc1,attributes);
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==5);
		//insert one WorkProduct to adapter1
		p = new ProcessRole();
		pc2 = (ProcessRoleCell)adapter1.associateGraphCell(p);
		attr = pc2.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);

		adapter1.insertCell(pc2,attributes);
		assertTrue(FlowGraphAdapter.getRoots(adapter1).length==6);*/
	}
	
	public void testFindCellsByUserObject()
	{
		/*Vector v = adapter1.findCellsByUserObject(
			new Object[]{ac1.getUserObject(),ac2.getUserObject()
					,wc1.getUserObject(),wc2.getUserObject()
					,pc1.getUserObject(),pc2.getUserObject()});
		assertEquals(v.size(), 6);
		
		//on reaffecte les cells correspondantes
		Vector result = new Vector();
		for( int i = 0; i < v.size(); i++ )
		{
			Object userObject = ((DefaultGraphCell)v.get(i)).getUserObject(); 
			if( userObject == ac1.getUserObject() )
			{
				ac1 = (ActivityCell)v.get(i);
				result.add(ac1);
			}
			if( userObject == ac2.getUserObject() )
			{
				ac2 = (ActivityCell)v.get(i);
				result.add(ac2);
			}
			if( userObject == wc1.getUserObject() )
			{
				wc1 = (WorkProductCell)v.get(i);
				result.add(wc1);
			}
			if( userObject == wc2.getUserObject() )
			{
				wc2 = (WorkProductCell)v.get(i);
				result.add(wc2);
			}
			if( userObject == pc1.getUserObject() )
			{
				pc1 = (ProcessRoleCell)v.get(i);
				result.add(pc1);
			}
			if( userObject == pc2.getUserObject() )
			{
				pc2 = (ProcessRoleCell)v.get(i);
				result.add(pc2);
			}
		}
		
		assertEquals(result.size(), 6);
		assertTrue(result.contains(ac1));
		assertTrue(result.contains(ac2));
		assertTrue(result.contains(wc1));
		assertTrue(result.contains(wc2));
		assertTrue(result.contains(pc1));
		assertTrue(result.contains(pc2));*/
	}
	
	public void testInsertEdge()
	{
		/*//---------------- wrong tests --------------------------
		//link activity <-> same activity
		adapter1.insertEdge(ac1,ac1,null);
		assertFalse(((Port)ac1.getChildAt(0)).edges().hasNext());
		//link product <-> same product
		adapter1.insertEdge(wc1,wc1,null);
		assertFalse(((Port)wc1.getChildAt(0)).edges().hasNext());
		//link role <-> same role
		adapter1.insertEdge(pc1,pc1,null);
		assertFalse(((Port)pc1.getChildAt(0)).edges().hasNext());
		
		//link activity <-> activity
		adapter1.insertEdge(ac1,ac2,null);
		assertFalse(((Port)ac1.getChildAt(0)).edges().hasNext());
		assertFalse(((Port)ac2.getChildAt(0)).edges().hasNext());
		//link product <-> product
		adapter1.insertEdge(wc1,wc2,null);
		assertFalse(((Port)wc1.getChildAt(0)).edges().hasNext());
		assertFalse(((Port)wc2.getChildAt(0)).edges().hasNext());
		//link role <-> role
		adapter1.insertEdge(pc1,pc2,null);
		assertFalse(((Port)pc1.getChildAt(0)).edges().hasNext());
		assertFalse(((Port)pc2.getChildAt(0)).edges().hasNext());
		
		//activity -> role
		adapter1.insertEdge(ac1,pc1,null);
		assertFalse(((Port)ac1.getChildAt(0)).edges().hasNext());
		assertFalse(((Port)pc1.getChildAt(0)).edges().hasNext());
		//product -> role
		adapter1.insertEdge(wc1,pc1,null);
		assertFalse(((Port)wc1.getChildAt(0)).edges().hasNext());
		assertFalse(((Port)pc1.getChildAt(0)).edges().hasNext());
		
		//---------------- true tests --------------------------
		//activity1 -> product1
		adapter1.insertEdge(ac1,wc1,null);
		Port source = (Port)ac1.getChildAt(0);
		Port target = (Port)wc1.getChildAt(0);
		
		Iterator edges = source.edges();
		assertTrue(edges.hasNext());
		assertTrue(((Edge)edges.next()).getTarget()==target);
		assertFalse(edges.hasNext());
		
		edges = target.edges();
		assertTrue(edges.hasNext());
		assertTrue(((Edge)edges.next()).getSource()==source);
		assertFalse(edges.hasNext());
		
		//activity1 -> product2
		adapter1.insertEdge(ac1,wc2,null);
		source = (Port)ac1.getChildAt(0);
		target = (Port)wc2.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edgesCount(edges)==2);
		
		edges = target.edges();
		assertTrue(edges.hasNext());
		assertTrue(((Edge)edges.next()).getSource()==source);
		assertFalse(edges.hasNext());
		
		//activity2 -> product1
		adapter1.insertEdge(ac2,wc1,null);
		source = (Port)ac2.getChildAt(0);
		target = (Port)wc1.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edges.hasNext());
		assertTrue(((Edge)edges.next()).getTarget()==target);
		assertFalse(edges.hasNext());
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==2);
		
		//activity2 -> product2
		adapter1.insertEdge(ac2,wc2,null);
		source = (Port)ac2.getChildAt(0);
		target = (Port)wc2.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edgesCount(edges)==2);
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==2);
		
		//product1 -> activity1
		adapter1.insertEdge(wc1,ac1,null);
		source = (Port)wc1.getChildAt(0);
		target = (Port)ac1.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edgesCount(edges)==3);
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==3);
		
		//product1 -> activity2
		adapter1.insertEdge(wc1,ac2,null);
		source = (Port)wc1.getChildAt(0);
		target = (Port)ac2.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edgesCount(edges)==4);
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==3);
		
		//product2 -> activity1
		adapter1.insertEdge(wc2,ac1,null);
		source = (Port)wc2.getChildAt(0);
		target = (Port)ac1.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edgesCount(edges)==3);
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==4);

		//product2 -> activity2
		adapter1.insertEdge(wc2,ac2,null);
		source = (Port)wc2.getChildAt(0);
		target = (Port)ac2.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edgesCount(edges)==4);
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==4);
		
		//role1 -> acivity1
		adapter1.insertEdge(pc1,ac1,null);
		source = (Port)pc1.getChildAt(0);
		target = (Port)ac1.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edges.hasNext());
		assertTrue(((Edge)edges.next()).getTarget()==target);
		assertFalse(edges.hasNext());
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==5);
		
		//role2 -> activity2
		adapter1.insertEdge(pc2,ac2,null);
		source = (Port)pc2.getChildAt(0);
		target = (Port)ac2.getChildAt(0);
		
		edges = source.edges();
		assertTrue(edges.hasNext());
		assertTrue(((Edge)edges.next()).getTarget()==target);
		assertFalse(edges.hasNext());
		
		edges = target.edges();
		assertTrue(edgesCount(edges)==5);
		
		//---------------- wrong tests --------------------------
		//relink activity -> product
		adapter1.insertEdge(ac1,wc1,null);
		source = (Port)ac1.getChildAt(0);
		target = (Port)wc1.getChildAt(0);
		edges = source.edges();
		assertTrue(edgesCount(edges)==5);
		edges = target.edges();
		assertTrue(edgesCount(edges)==4);
		//relink product -> activity
		adapter1.insertEdge(wc1,ac1,null);
		source = (Port)wc1.getChildAt(0);
		target = (Port)ac1.getChildAt(0);
		edges = source.edges();
		assertTrue(edgesCount(edges)==4);
		edges = target.edges();
		assertTrue(edgesCount(edges)==5);
		//relink role -> activity
		adapter1.insertEdge(pc1,ac1,null);
		source = (Port)pc1.getChildAt(0);
		target = (Port)ac1.getChildAt(0);
		edges = source.edges();
		assertTrue(edgesCount(edges)==1);
		edges = target.edges();
		assertTrue(edgesCount(edges)==5);
		//link other role -> activity
		adapter1.insertEdge(pc2,ac1,null);
		source = (Port)pc2.getChildAt(0);
		target = (Port)ac1.getChildAt(0);
		edges = source.edges();
		assertTrue(edgesCount(edges)==1);
		edges = target.edges();
		assertTrue(edgesCount(edges)==5);*/
	}

	public void testMoveAndRemove()
	{
		/*//remove first edge of ac1 -> wc1 and ac2 -> wc2
		DefaultEdge edge1 = null,
					edge2 = null;
		Port source1 = (Port)ac1.getFirstChild();
		Port target1 = (Port)wc1.getFirstChild();
		Port source2 = (Port)ac2.getFirstChild();
		Port target2 = (Port)wc2.getFirstChild();
		
		Iterator edges = source1.edges();
		while( edge1 == null )
		{
			DefaultEdge e = (DefaultEdge)edges.next();
			if(e.getTarget() == target1)
			{
				edge1 = e; 
			}
		}
		edges = source2.edges();
		while( edge2 == null )
		{
			DefaultEdge e = (DefaultEdge)edges.next();
			if(e.getTarget() == target2)
			{
				edge2 = e; 
			}
		}
		
		adapter1.remove(new Object[]{edge1,edge2});
		
		edges = source1.edges();
		assertTrue(edgesCount(edges)==4);
		edges = source2.edges();
		assertTrue(edgesCount(edges)==4);
		edges = target1.edges();
		assertTrue(edgesCount(edges)==3);
		edges = target2.edges();
		assertTrue(edgesCount(edges)==3);
		
		//move link ac1 -> wc2 to ac2 -> wc2
		edges = source1.edges();
		edge1 = null;
		while( edge1 == null )
		{
			DefaultEdge e = (DefaultEdge)edges.next();
			if(e.getTarget() == target2)
			{
				edge1 = e; 
			}
		}
		
		adapter1.moveEdge(edge1,ac2,true);
		
		edges = source1.edges();
		assertTrue(edgesCount(edges)==3);
		edges = source2.edges();
		assertTrue(edgesCount(edges)==5);
		edges = target2.edges();
		assertTrue(edgesCount(edges)==3);
		
		//remove ac2
		adapter1.remove(new Object[]{ac2});
		edges = target1.edges();
		assertTrue(edgesCount(edges)==1);
		edges = target2.edges();
		assertTrue(edgesCount(edges)==1);
		assertFalse(adapter1.contains(ac2));
		assertFalse(adapter1.contains(source2));*/
	}
	
	private int edgesCount( Iterator it )
	{
		int i=0;
		while(it.hasNext())
		{
			i++;
			it.next();
		}
		return i;
	}
}
