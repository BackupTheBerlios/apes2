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

package apes.model.spem.frontend;

import java.util.HashMap;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.frontend.ChangeEvent;
import org.ipsquad.apes.model.frontend.Event;
import org.ipsquad.apes.model.frontend.InsertEvent;
import org.ipsquad.apes.model.frontend.MoveEvent;
import org.ipsquad.apes.model.frontend.RemoveEvent;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.ipsquad.utils.ConfigManager;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphModel;

/**
 * 
 * @version $Revision: 1.8 $ 
 */
public class TestApesMediator extends TestCase
{
	static ApesMediator mediator;
	static ProcessComponent root;
	static FlowDiagram flow;
	static ActivityDiagram act;
	static ResponsabilityDiagram resp;
	static Map attr;
	
	static class TestListener implements ApesMediator.Listener
	{
		public Event event = null;
		public int count = 0;
		
		public void updated( Event e )
		{
			event = e;
			count++;
		}
	}
	
	static TestListener listener;
	static SpemTreeAdapter model;
	
	static
	{
		Context context = Context.getInstance();
		ConfigManager.init(null);
		
		model = new SpemTreeAdapter();
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
			public void setFilePath(String filePath) { }//fail(); }
			public GraphFrame getGraphFrame(GraphModel model) { fail(); return null; }
		});
		
		ConfigManager.init(ApesMain.createDefaultProperties());
		
		mediator = ApesMediator.getInstance();
		context.setProject(new Project());		
		listener = new TestListener();
		mediator.addApesMediatorListener( listener );
		
		root = context.getProject().getProcess().getComponent();
		flow = new FlowDiagram();
		act = new ActivityDiagram();
		resp = new ResponsabilityDiagram();
		
		context.getProject().setGraphModel( flow, new FlowGraphAdapter(flow) );
		context.getProject().setGraphModel( act, new FlowGraphAdapter(act) );
		context.getProject().setGraphModel( resp, new FlowGraphAdapter(resp) );
		
		attr = new HashMap();
		attr.put("test","test");
	}
	
	public void testUpdate()
	{
		mediator.addApesMediatorListener( listener );
		
		InsertEvent i_e = null;
		MoveEvent m_e = null;
		ChangeEvent c_e = null;
		RemoveEvent r_e = null;
		
		// test insertCommand in the tree
		SPackage p = new SPackage();
		ProcessRole r = new ProcessRole();
		WorkProduct w = new WorkProduct();
		ApesWorkDefinition aw1 = new ApesWorkDefinition();
		ApesWorkDefinition aw2 = new ApesWorkDefinition();
		Activity a1 = new Activity();
		Activity a2 = new Activity();
		Activity a3 = new Activity();
		
		mediator.update( mediator.createInsertCommand( r, root, attr ) );
		assertTrue( listener.event != null );
		assertTrue( listener.event instanceof InsertEvent );
		i_e = (InsertEvent)listener.event;
		assertTrue(i_e.getAttributes().size() == 1);
		assertTrue(i_e.getAttributes().get("test") == "test" );
		assertEquals(i_e.getDiagram(), null);
		assertEquals(i_e.getInserted(), r);
		assertEquals(i_e.getParent(), root);
		assertFalse(i_e.isAlreadyExistInModel());
		listener.event = null;
		listener.count = 0;
		
		mediator.update( mediator.createInsertCommand( w, root, attr ) );//count == 1
		mediator.update( mediator.createInsertCommand( p, root, attr ) );//count == 2
		mediator.update( mediator.createInsertCommand( aw1, root, attr ) );//count == 5
		mediator.update( mediator.createInsertCommand( aw2, root, attr ) );//count == 8
		mediator.update( mediator.createInsertCommand( a1, aw1, attr ) );//count == 9
		mediator.update( mediator.createInsertCommand( a2, aw1,attr ) );//count == 10
		mediator.update( mediator.createInsertCommand( a3, aw2,attr ) );//count == 11
		mediator.update( mediator.createInsertCommand( flow, aw1, attr ) );//count == 11
		mediator.update( mediator.createInsertCommand( act, aw1, attr ) );//count == 11
		mediator.update( mediator.createInsertCommand( resp, root, attr ) );//count == 12

		assertEquals(listener.count, 12);
		
		act = aw1.getActivityDiagram();
		flow = aw1.getFlowDiagram();
		
		listener.event = null;
		listener.count = 0;
		
		mediator.update( mediator.createInsertCommand( a1, root, attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createInsertCommand( flow, root, attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createInsertCommand( act, root, attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createInsertCommand( r, root, attr ) );
		assertTrue( listener.event== null );
		mediator.update( mediator.createInsertCommand( a1, aw2, attr ) );
		assertTrue( listener.event== null );
		
		//test insertCommand in graph
		Map test = new HashMap();
		test.put("Attributes",new HashMap());
		
		mediator.update( mediator.createInsertCommandToSpemDiagram( flow, w, test ) );
		
		assertTrue( listener.event != null );
		assertTrue( listener.event instanceof InsertEvent );
		i_e = (InsertEvent)listener.event;
		assertEquals(i_e.getAttributes().size(), 1);
		assertNotNull(i_e.getAttributes().get("Attributes"));
		assertTrue(i_e.getDiagram()==flow);
		assertEquals(i_e.getInserted(), w);
		assertEquals(i_e.getParent(), root);
		assertTrue(i_e.isAlreadyExistInModel());
		
		listener.event = null;
		listener.count = 0;
		
		mediator.update( mediator.createInsertCommandToSpemDiagram( flow, a1, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram( flow, r, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram( resp, w, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram( resp, r, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram( act, a1, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram( act, a2, test ) );
		
		test.put("firstPort", new DefaultPort());
		test.put("endPort", new DefaultPort());
		
		mediator.update( mediator.createInsertCommandToSpemDiagram(flow, r, a1, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram(flow, a1, w, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram(resp, r, w, test ) );
		mediator.update( mediator.createInsertCommandToSpemDiagram( act, a1, a2, test ) );
		
		assertEquals(listener.count, 10);

		listener.event = null;
		listener.count = 0;
		
		mediator.update( mediator.createInsertCommand( act, a3, attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createInsertCommand( flow, a3, attr ) );
		assertTrue( listener.event == null );
		
		
		// test moveComand
		mediator.update( mediator.createMoveCommand( w, p, attr ) );
		
		assertTrue( listener.event != null );
		assertTrue( listener.event instanceof MoveEvent );
		m_e = (MoveEvent)listener.event;
		assertTrue(m_e.getAttributes().size() == 1);
		assertTrue(m_e.getAttributes().get("test") == "test" );
		assertEquals(m_e.getElement(), w);
		assertEquals(m_e.getNewParent(), p);
		assertEquals(m_e.getOldParent(), root);

		listener.event = null;
		listener.count = 0;
		
		mediator.update( mediator.createMoveCommand( flow, root, attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createMoveCommand( act, root, attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createMoveCommand( resp, aw1, attr ) );
		assertTrue( listener.event == null );
		
		//test change

		mediator.update( mediator.createChangeCommand( w, "WorkProduct0", attr ) );
		assertTrue( listener.event != null );
		assertTrue( listener.event instanceof ChangeEvent );
		c_e = (ChangeEvent)listener.event;
		assertTrue(c_e.getAttributes().size() == 1);
		assertTrue(c_e.getAttributes().get("test") == "test" );
		assertEquals(c_e.getElement(), w);
		assertEquals(c_e.getNewValue(), "WorkProduct0");

		listener.event = null;
		listener.count = 0;
		
		mediator.update( mediator.createChangeCommand( w, "", attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createChangeCommand( w, "WorkProduct0", attr ) );
		assertTrue( listener.event == null );
		mediator.update( mediator.createChangeCommand( w, null, attr ) );
		assertTrue( listener.event == null );
		
		//test remove from graph
		mediator.update( mediator.createRemoveCommand( flow, new Object[]{ a1, r, w }, new Object[]{ r, a1 }, new Object[]{ a1, w }, attr ) );
		
		assertTrue( listener.event != null );
		r_e = (RemoveEvent) listener.event;
		assertTrue(r_e.getDiagram()==flow);
		assertEquals(r_e.getElements().length, 3);
		assertTrue( r_e.getElements()[0] == a1 || r_e.getElements()[0] == r || r_e.getElements()[0] == w);
		assertTrue( r_e.getElements()[1] == a1 || r_e.getElements()[1] == r || r_e.getElements()[1] == w);
		assertTrue( r_e.getElements()[2] == a1 || r_e.getElements()[2] == r || r_e.getElements()[2] == w);
		assertTrue( r_e.getElements()[0] != r_e.getElements()[1] );
		assertTrue( r_e.getElements()[0] != r_e.getElements()[2] );
		assertTrue( r_e.getElements()[1] != r_e.getElements()[0] );
		assertTrue( r_e.getElements()[1] != r_e.getElements()[2] );
		assertTrue( r_e.getElements()[2] != r_e.getElements()[0] );
		assertTrue( r_e.getElements()[2] != r_e.getElements()[1] );
		assertEquals(r_e.getIndices(), null);
		assertEquals(r_e.getParents(), null);
		assertEquals(r_e.getSources().length, 2);
		assertTrue(r_e.getSources()[0]==r || r_e.getSources()[0] == a1);
		assertTrue(r_e.getSources()[1]==r || r_e.getSources()[1] == a1);
		assertTrue(r_e.getSources()[0] != r_e.getSources()[1] );
		assertEquals(r_e.getTargets().length, 2);
		assertTrue(r_e.getTargets()[0]==a1 || r_e.getTargets()[0] == w);
		assertTrue(r_e.getTargets()[1]==a1 || r_e.getTargets()[1] == w);
		assertTrue(r_e.getTargets()[0] != r_e.getTargets()[1] );
		assertTrue(r_e.getAttributes().size() == 1);
		assertTrue(r_e.getAttributes().get("test") == "test" );
		
		listener.event = null;
		listener.count = 0;

		//test remove from tree

	}

}
