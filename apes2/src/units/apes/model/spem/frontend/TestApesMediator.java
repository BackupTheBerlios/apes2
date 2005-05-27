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

import java.util.Collection;
import java.util.Iterator;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.ipsquad.utils.ConfigManager;
import org.jgraph.graph.GraphModel;

/**
 * 
 * @version $Revision: 1.11 $ 
 */
public class TestApesMediator extends TestCase
{
	static ApesMediator mediator;
	static ProcessComponent root;
	static FlowDiagram flow;
	static ActivityDiagram act;
	static ResponsabilityDiagram resp;
	static Map attr;
	
	/*static class TestListener implements ApesModelListener
	{
		public ApesModelEvent event = null;
		public int count = 0;
		
		public void modelChanged( ApesModelEvent e )
		{
			event = e;
			count++;
		}
	}*/
	
	//static TestListener listener;
	static SpemTreeAdapter model;
	
	static Context context = Context.getInstance();
	
	static WorkProduct required = null;
	static WorkProduct provided = null;
	static WorkDefinition wd = null;
	static WorkDefinitionDiagram diagram = null;
	static Link required_wd = null;
	static Link wd_provided = null;
	static Link role_wd = null;
	static ProcessRole role = null;
	
	static
	{
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
			public void deleteDiagram(GraphModel model) { }//fail(); }
			public void setDefaultToolPalette() { fail(); }
			public void setFilePath(String filePath) { }//fail(); }
			public GraphFrame getGraphFrame(GraphModel model) { fail(); return null; }
		});
		
		ConfigManager.init(ApesMain.createDefaultProperties());
		
		mediator = ApesMediator.getInstance();
		//listener = new TestListener();
		//mediator.addApesModelListener( listener );
	}
	
	protected void setUp()
	{
		context.setProject(new Project());		
		root = context.getProject().getProcess().getComponent();
	
		diagram = new WorkDefinitionDiagram("TestDiagram");
		
		required = new WorkProduct("required");
		provided = new WorkProduct("provided");
		role = new ProcessRole("role");
		wd = new WorkDefinition("wd");
	}
		
	protected void tearDown()
	{
		mediator.removeFromModel(new Object[]{root.getParent()}, null);
		mediator.clearAll();
	}
	
	public void testSetSource()
	{
		Link link = new Link();
		Object source = new Object();
		
		mediator.setSource(link, source);
		assertEquals(source, link.getSource());
		
		mediator.setSource(link, null);
		assertNull(link.getSource());
	}
	
	public void testSetTarget()
	{
		Link link = new Link();
		Object target = new Object();
		
		mediator.setTarget(link, target);
		assertEquals(target, link.getTarget());
		
		mediator.setTarget(link, null);
		assertNull(link.getTarget());
	}
	
	public void testShouldGoInTree()
	{
		assertFalse(mediator.shouldGoInTree(null));
		assertFalse(mediator.shouldGoInTree(new Object()));
		assertFalse(mediator.shouldGoInTree(new Link()));
		
		assertTrue(mediator.shouldGoInTree(new Activity()));
		assertTrue(mediator.shouldGoInTree(new ApesProcess()));
		assertTrue(mediator.shouldGoInTree(new ApesWorkDefinition()));
		assertTrue(mediator.shouldGoInTree(new ProcessComponent()));
		assertTrue(mediator.shouldGoInTree(new ProcessRole()));
		assertTrue(mediator.shouldGoInTree(new SPackage()));
		assertTrue(mediator.shouldGoInTree(new StateMachine()));
		assertTrue(mediator.shouldGoInTree(new WorkDefinition()));
		assertTrue(mediator.shouldGoInTree(new WorkProduct()));
		assertTrue(mediator.shouldGoInTree(new ActivityDiagram()));
		assertTrue(mediator.shouldGoInTree(new ContextDiagram()));
		assertTrue(mediator.shouldGoInTree(new FlowDiagram()));
		assertTrue(mediator.shouldGoInTree(new WorkDefinitionDiagram()));
		assertTrue(mediator.shouldGoInTree(new ResponsabilityDiagram()));
		assertTrue(mediator.shouldGoInTree(new ApesProcess.RequiredInterface("r")));
		assertTrue(mediator.shouldGoInTree(new ApesProcess.ProvidedInterface("p")));
	}
	
	public void testGetLinks()
	{
		ProcessComponent component = new ProcessComponent("component");
		WorkDefinitionDiagram diagram = new WorkDefinitionDiagram("TestDiagram");
		
		mediator.insertInModel(new Object[]{diagram}, new Object[]{component}, null);
		
		/*WorkProduct required = new WorkProduct("required");
		WorkProduct provided = new WorkProduct("provided");
		WorkDefinition wd = new WorkDefinition("wd");
		ProcessRole role = new ProcessRole("role");*/

		mediator.insertIn(diagram, new Object[]{required, provided, wd, role}, null);
		
		Link required_wd = new Link(required, wd);
		Link wd_provided = new Link(wd, provided);
		Link role_wd = new Link(role, wd);
		
		mediator.insertIn(diagram, new Object[]{required_wd, wd_provided, role_wd}, null);
		
		Collection links = mediator.getLinks(diagram, new Object[]{required, provided, wd, role, required_wd, wd_provided, role_wd});
		assertEquals(3, links.size());
		assertTrue(links.contains(required_wd));
		assertTrue(links.contains(wd_provided));
		assertTrue(links.contains(role_wd));
		
		links = mediator.getLinks(diagram, new Object[]{required, provided, wd, role, required_wd, wd_provided});
		assertEquals(3, links.size());
		assertTrue(links.contains(required_wd));
		assertTrue(links.contains(wd_provided));
		assertTrue(links.contains(role_wd));

		links = mediator.getLinks(diagram, new Object[]{required, provided, wd, role, required_wd});
		assertEquals(3, links.size());
		assertTrue(links.contains(required_wd));
		assertTrue(links.contains(wd_provided));
		assertTrue(links.contains(role_wd));
		
		links = mediator.getLinks(diagram, new Object[]{required, provided, wd, role});
		assertEquals(3, links.size());
		assertTrue(links.contains(required_wd));
		assertTrue(links.contains(wd_provided));
		assertTrue(links.contains(role_wd));
		
		links = mediator.getLinks(diagram, new Object[]{role});
		assertEquals(1, links.size());
		assertTrue(links.contains(role_wd));
		
		links = mediator.getLinks(diagram, new Object[]{wd});
		assertEquals(3, links.size());
		assertTrue(links.contains(required_wd));
		assertTrue(links.contains(wd_provided));
		assertTrue(links.contains(role_wd));
		
		links = mediator.getLinks(diagram, new Object[]{provided});
		assertEquals(1, links.size());
		assertTrue(links.contains(wd_provided));

		links = mediator.getLinks(diagram, new Object[]{required});
		assertEquals(1, links.size());
		assertTrue(links.contains(required_wd));

		links = mediator.getLinks(diagram, new Object[]{required, provided, role});
		assertEquals(3, links.size());
		assertTrue(links.contains(required_wd));
		assertTrue(links.contains(wd_provided));
		assertTrue(links.contains(role_wd));
		
		links = mediator.getLinks(diagram, new Object[]{null});
		assertEquals(0, links.size());
		
		links = mediator.getLinks(diagram, null);
		assertNull(links);	
	}
	
	public void testGetAllElements()
	{
		mediator.insertInModel(new Object[]{diagram}, new Object[]{root}, null);
		
		/*required = new WorkProduct("required");
		provided = new WorkProduct("provided");
		wd = new WorkDefinition("wd");
		role = new ProcessRole("role");*/

		mediator.insertIn(diagram, new Object[]{required, provided, wd, role}, null);
		Collection coll = mediator.getAllElements(diagram);
		assertEquals(4, coll.size());
		assertTrue(coll.contains(required));
		assertTrue(coll.contains(provided));
		assertTrue(coll.contains(wd));
		assertTrue(coll.contains(role));
		
		required_wd = new Link(required, wd);
		wd_provided = new Link(wd, provided);
		role_wd = new Link(role, wd);
		
		mediator.insertIn(diagram, new Object[]{required_wd, wd_provided, role_wd}, null);		
		coll = mediator.getAllElements(diagram);
		assertEquals(7, coll.size());
		assertTrue(coll.contains(required));
		assertTrue(coll.contains(provided));
		assertTrue(coll.contains(wd));
		assertTrue(coll.contains(role));
		assertTrue(coll.contains(required_wd));
		assertTrue(coll.contains(wd_provided));
		assertTrue(coll.contains(role_wd));
	}
	
	public void testGetDescendants()
	{
		SPackage top = new SPackage();
		SPackage _1_1 = new SPackage();
		SPackage _1_1_1 = new SPackage();
		SPackage _1_2 = new SPackage();
		SPackage _1_2_1 = new SPackage();
		SPackage _1_2_2 = new SPackage();
		
		top.addModelElement(_1_1);
		_1_1.addModelElement(_1_1_1);
		top.addModelElement(_1_2);
		_1_2.addModelElement(_1_2_1);
		_1_2.addModelElement(_1_2_2);
		
		Collection descendants = mediator.getDescendants(new Object[]{top});
		assertEquals(6, descendants.size());
		assertTrue(descendants.contains(top));
		assertTrue(descendants.contains(_1_1));
		assertTrue(descendants.contains(_1_1_1));
		assertTrue(descendants.contains(_1_2));
		assertTrue(descendants.contains(_1_2_1));
		assertTrue(descendants.contains(_1_2_2));
		
		descendants = mediator.getDescendants(new Object[]{top,_1_2_2});
		assertEquals(6, descendants.size());
		assertTrue(descendants.contains(top));
		assertTrue(descendants.contains(_1_1));
		assertTrue(descendants.contains(_1_1_1));
		assertTrue(descendants.contains(_1_2));
		assertTrue(descendants.contains(_1_2_1));
		assertTrue(descendants.contains(_1_2_2));
		
		descendants = mediator.getDescendants(new Object[]{_1_1});
		assertEquals(2, descendants.size());
		assertTrue(descendants.contains(_1_1));
		assertTrue(descendants.contains(_1_1_1));
		
		descendants = mediator.getDescendants(new Object[]{_1_1, _1_2});
		assertEquals(5, descendants.size());
		assertTrue(descendants.contains(_1_1));
		assertTrue(descendants.contains(_1_1_1));
		assertTrue(descendants.contains(_1_2));
		assertTrue(descendants.contains(_1_2_1));
		assertTrue(descendants.contains(_1_2_2));
		
		assertNull(mediator.getDescendants(null));
		assertEquals(0, mediator.getDescendants(new Object[]{null}).size());
	}
	
	public void testGetParents()
	{
		SPackage top = new SPackage();
		SPackage _1_1 = new SPackage();
		SPackage _1_1_1 = new SPackage();
		SPackage _1_2 = new SPackage();
		SPackage _1_2_1 = new SPackage();
		SPackage _1_2_2 = new SPackage();
		
		top.addModelElement(_1_1);
		_1_1.addModelElement(_1_1_1);
		top.addModelElement(_1_2);
		_1_2.addModelElement(_1_2_1);
		_1_2.addModelElement(_1_2_2);
		
		Object[] parents = mediator.getParents(new Object[]{top, _1_1, _1_1_1, _1_2, _1_2_1, _1_2_2});
		assertEquals(6, parents.length);
		assertEquals(null, parents[0]);
		assertEquals(top, parents[1]);
		assertEquals(_1_1, parents[2]);
		assertEquals(top, parents[3]);
		assertEquals(_1_2, parents[4]);
		assertEquals(_1_2, parents[5]);
		
		assertNull(mediator.getParents(null));
		assertEquals(1, mediator.getParents(new Object[]{null}).length);
	}
	
	public void testInsertInModel()
	{
		/*
		 * Standard inserted tests
		 */
		SPackage p = new SPackage();
		
		assertFalse(root.containsModelElement(p));
		mediator.insertInModel(new Object[]{p}, new Object[]{root}, null);
		assertTrue(root.containsModelElement(p));
		
		SPackage p1 = new SPackage();
		SPackage p2 = new SPackage();
		SPackage p3 = new SPackage();
		
		assertFalse(p.containsModelElement(p1));
		assertFalse(p.containsModelElement(p2));
		assertFalse(p.containsModelElement(p3));
		mediator.insertInModel(new Object[]{p1,p2,p3}, new Object[]{p,p,p}, null);
		assertTrue(p.containsModelElement(p1));
		assertTrue(p.containsModelElement(p2));
		assertTrue(p.containsModelElement(p3));
		
		/*
		 * ApesWorkDefinition inserted tests
		 */
		ApesWorkDefinition wd = new ApesWorkDefinition();
		assertFalse(p.containsModelElement(wd));
		mediator.insertInModel(new Object[]{wd}, new Object[]{p}, null);
		assertTrue(p.containsModelElement(wd));
		assertEquals(2, wd.modelElementCount());
		assertTrue(wd.getModelElement(0) instanceof FlowDiagram && wd.getModelElement(1) instanceof ActivityDiagram);

		Activity a = new Activity();
		
		assertFalse(root.containsModelElement(a));
		mediator.insertInModel(new Object[]{a}, new Object[]{root}, null);
		assertFalse(root.containsModelElement(a));
		
		assertFalse(wd.containsModelElement(a));
		mediator.insertInModel(new Object[]{a}, new Object[]{wd}, null);
		assertTrue(wd.containsModelElement(a));
		
		/*
		 * StateMachine inserted tests
		 */
		WorkProduct w = new WorkProduct();
		WorkProduct w1 = new WorkProduct();
		
		assertFalse(root.containsModelElement(w));
		assertFalse(root.containsModelElement(w1));
		mediator.insertInModel(new Object[]{w,w1}, new Object[]{root,root},null);
		assertTrue(root.containsModelElement(w));
		assertTrue(root.containsModelElement(w1));
		
		StateMachine sm = new StateMachine();
		StateMachine sm1 = new StateMachine();
		
		assertFalse(root.containsModelElement(sm));
		assertFalse(root.containsModelElement(sm1));
		mediator.insertInModel(new Object[]{sm, sm1}, new Object[]{root, root},null);
		assertFalse(root.containsModelElement(sm));
		assertFalse(root.containsModelElement(sm1));
		
		assertFalse(w.containsModelElement(sm));
		assertFalse(w.containsModelElement(sm1));
		mediator.insertInModel(new Object[]{sm, sm1}, new Object[]{w, w},null);
		assertTrue(w.containsModelElement(sm));
		assertTrue(w.containsModelElement(sm1));
		
		assertFalse(w1.containsModelElement(sm));
		assertFalse(w1.containsModelElement(sm1));
		mediator.insertInModel(new Object[]{sm, sm1}, new Object[]{w1, w1},null);
		assertFalse(w1.containsModelElement(sm));
		assertFalse(w1.containsModelElement(sm1));
	}
	
	public void testInsertIn()
	{
		/*
		 * Standard insert tests
		 */
		ProcessComponent component = new ProcessComponent("component");
		WorkDefinitionDiagram diagram = new WorkDefinitionDiagram("TestDiagram");
		
		mediator.insertInModel(new Object[]{diagram}, new Object[]{component}, null);
		
		//elements
		/*WorkProduct required = new WorkProduct("required");
		WorkProduct provided = new WorkProduct("provided");
		ApesWorkDefinition wd = new ApesWorkDefinition("wd");
		ProcessRole role = new ProcessRole("role");*/

		ApesWorkDefinition awd = new ApesWorkDefinition("awd");
		
		mediator.insertInModel(new Object[]{root}, new Object[]{diagram}, null);
		
		assertEquals(0, diagram.modelElementCount());
		mediator.insertIn(diagram, new Object[]{required, provided, awd, role}, null);
		assertEquals(4, diagram.modelElementCount());
		assertTrue(diagram.containsModelElement(required));
		assertTrue(diagram.containsModelElement(provided));
		assertTrue(diagram.containsModelElement(awd));
		assertTrue(diagram.containsModelElement(role));
		
		//links
		Link required_wd = new Link(required, awd);
		Link wd_provided = new Link(awd, provided);
		Link role_wd = new Link(role, awd);
		
		mediator.insertIn(diagram, new Object[]{required_wd, wd_provided, role_wd}, null);
		assertEquals(4, diagram.modelElementCount());
		assertTrue(diagram.existsLinkModelElements(required, awd,null));
		assertTrue(diagram.existsLinkModelElements(awd, provided,null));
		assertTrue(diagram.existsLinkModelElements(role, awd,null));
		
		/*
		 * WorkProductRef tests 
		 */
		ContextDiagram cd = (ContextDiagram) root.getModelElement(0);
		
		mediator.insertIn(cd, new Object[]{required, provided, role, awd}, null);
		assertEquals(3, cd.modelElementCount());
		assertTrue(cd.containsModelElement(required));
		assertTrue(cd.containsModelElement(provided));
		
		Link required_component = new Link(required, root);
		Link component_provided = new Link(root, provided);
		
		assertEquals(0, Context.getInstance().getProject().getProcess().getProvidedInterface().modelElementCount());
		assertEquals(0, Context.getInstance().getProject().getProcess().getRequiredInterface().modelElementCount());
		mediator.insertIn(cd, new Object[]{required_component, component_provided}, null);
		assertTrue(cd.existsLinkModelElements(required,root,null));
		assertTrue(cd.existsLinkModelElements(root, provided,null));
		assertEquals(1, Context.getInstance().getProject().getProcess().getProvidedInterface().modelElementCount());
		assertEquals(1, Context.getInstance().getProject().getProcess().getRequiredInterface().modelElementCount());
		
		/*
		 * ProcessRole tests
		 */
		Activity a1 = new Activity();
		Activity a2 = new Activity();
		Activity a3 = new Activity();
		
		assertEquals(0, awd.getFlowDiagram().modelElementCount());
		mediator.insertIn(awd.getFlowDiagram(), new Object[]{a1,a2,a3}, null);
		assertEquals(3, awd.getFlowDiagram().modelElementCount());
		assertEquals(role, a1.getOwner());
		assertEquals(role, a2.getOwner());
		assertEquals(role, a3.getOwner());
		
		ProcessRole role1 = new ProcessRole("role1");
		mediator.insertIn(awd.getFlowDiagram(), new Object[]{role, role1}, null);
		assertEquals(5, awd.getFlowDiagram().modelElementCount());
		assertTrue(awd.getFlowDiagram().containsModelElement(role));
		assertTrue(awd.getFlowDiagram().containsModelElement(role1));
		
		Link role_a2 = new Link(role, a2,"",new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		Link role1_a1 = new Link(role1, a1,"",new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		mediator.insertIn(awd.getFlowDiagram(), new Object[]{role1_a1, role_a2}, null);
		assertEquals(5, awd.getFlowDiagram().modelElementCount());
		assertTrue(awd.getFlowDiagram().existsLinkModelElements(role,a2,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(awd.getFlowDiagram().existsLinkModelElements(role1,a1,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(awd.getFlowDiagram().existsLinkModelElements(role,a2,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(awd.getFlowDiagram().existsLinkModelElements(role1,a1,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		assertEquals(role1, a1.getOwner());
		assertEquals(role, a2.getOwner());
		assertEquals(role, a3.getOwner());
		
		Link role_a2_assistant = new Link(role, a2,"",new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		Link role_a1_assistant = new Link(role, a1,"",new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
		mediator.insertIn(awd.getFlowDiagram(), new Object[]{role_a2_assistant, role_a1_assistant}, null);
		assertEquals(5, awd.getFlowDiagram().modelElementCount());
		assertTrue(awd.getFlowDiagram().existsLinkModelElements(role,a2,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertFalse(awd.getFlowDiagram().existsLinkModelElements(role,a2,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		assertFalse(awd.getFlowDiagram().existsLinkModelElements(role,a1,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertTrue(awd.getFlowDiagram().existsLinkModelElements(role,a1,
				new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)));
		
		/*
		 * StateMachine tests
		 */
		ApesWorkDefinition wd1 = new ApesWorkDefinition();
		WorkProduct w = new WorkProduct();
		WorkProduct w1 = new WorkProduct();
		mediator.insertInModel(new Object[]{w,w1}, new Object[]{root,root},null);

		StateMachine sm = new StateMachine();
		StateMachine sm1 = new StateMachine();		
		StateMachine sm2 = new StateMachine();
		StateMachine sm3 = new StateMachine();		
		mediator.insertInModel(new Object[]{sm, sm1,sm2,sm3}, new Object[]{w, w, w1, w1},null);
		
		assertFalse(diagram.containsModelElement(w));
		assertFalse(diagram.containsModelElement(w1));
		assertFalse(diagram.containsModelElement(sm2));
		assertFalse(diagram.containsModelElement(sm3));
		assertFalse(diagram.containsModelElement(wd1));
		mediator.insertIn(diagram, new Object[]{wd1, w, w1, sm2, sm3}, null);
		assertTrue(diagram.containsModelElement(w));
		assertTrue(diagram.containsModelElement(w1));
		assertTrue(diagram.containsModelElement(sm2));
		assertTrue(diagram.containsModelElement(sm3));
		assertTrue(diagram.containsModelElement(wd1));
		
		Link w_wd1 = new Link(w, wd1);
		Link sm1_wd1 = new Link(sm1, wd1);
		Link wd1_sm2 = new Link(wd1, sm2);
		Link w1_wd1 = new Link(w1, wd1);
		Link sm3_wd1 = new Link(sm3, wd1);
		
		assertFalse(diagram.existsLinkModelElements(w, wd1,null));
		assertFalse(diagram.existsLinkModelElements(sm3, wd1,null));
		assertFalse(diagram.existsLinkModelElements(w1,wd1,null));
		mediator.insertIn(diagram, new Object[]{w_wd1, sm3_wd1},null);
		assertTrue(diagram.existsLinkModelElements(w, wd1,null));
		assertTrue(diagram.existsLinkModelElements(sm3, wd1,null));
		assertTrue(diagram.existsLinkModelElements(w1,wd1,null));
		
		assertFalse(diagram.existsLinkModelElements(sm1,wd1,null));
		mediator.insertIn(diagram, new Object[]{sm1_wd1}, null);
		assertFalse(diagram.existsLinkModelElements(sm1,wd1,null));
		
		SpemGraphAdapter adapter = Context.getInstance().getProject().getGraphModel(diagram);
		
		mediator.insertIn(diagram, new Object[]{w1_wd1}, null);
		assertEquals(0, adapter.getLinks(w1).size());
		
		Collection links = adapter.getLinks(wd1);
		assertEquals(2, links.size());
		Iterator it = links.iterator();
		Link link = (Link)it.next();
		assertTrue((link.getSource() == sm3 && link.getTarget() == wd1)
				|| (link.getSource() == w && link.getTarget() == wd1));
		link = (Link)it.next();
		assertTrue((link.getSource() == sm3 && link.getTarget() == wd1)
				|| (link.getSource() == w && link.getTarget() == wd1));
		assertFalse(it.hasNext());
	}
	
	public void testRemoveFromModel()
	{
		/*
		 * direct removed tests
		 */
		WorkDefinitionDiagram diagram = new WorkDefinitionDiagram("TestDiagram");
		
		mediator.insertInModel(new Object[]{diagram}, new Object[]{root}, null);
		
		//elements
		WorkProduct required = new WorkProduct("required");
		WorkProduct provided = new WorkProduct("provided");
		ApesWorkDefinition wd = new ApesWorkDefinition("wd");
		ProcessRole role = new ProcessRole("role");
		mediator.insertInModel(new Object[]{root}, new Object[]{diagram}, null);
		mediator.insertIn(diagram, new Object[]{required, provided, wd, role}, null);

		Link required_wd = new Link(required, wd);
		Link wd_provided = new Link(wd, provided);
		Link role_wd = new Link(role, wd);
		mediator.insertIn(diagram, new Object[]{required_wd, wd_provided, role_wd}, null);

		ContextDiagram cd = (ContextDiagram) root.getModelElement(0);
		mediator.insertIn(cd, new Object[]{required, provided, role, wd}, null);
		
		Link required_component = new Link(required, root);
		Link component_provided = new Link(root, provided);		
		mediator.insertIn(cd, new Object[]{required_component, component_provided}, null);
		
		//tests if all links linked with a removed element are deleted
		assertEquals(root, required.getParent());
		assertTrue(root.containsModelElement(required));
		mediator.removeFromModel(new Object[]{required},null);
		assertFalse(root.containsModelElement(required));
		assertFalse(cd.existsLinkModelElements(required, root,null));
		
		/*
		 * undirect removed tests
		 */
		SPackage p1 = new SPackage();
		SPackage p11 = new SPackage();
		SPackage p111 = new SPackage();
		SPackage p12 = new SPackage();
		ResponsabilityDiagram rd = new ResponsabilityDiagram();				

		mediator.insertInModel(new Object[]{p1}, new Object[]{root},null);
		mediator.insertInModel(new Object[]{p11,p12,rd}, new Object[]{p1,p1,p1},null);
		mediator.insertInModel(new Object[]{p111}, new Object[]{p11},null);

		ProcessRole role1 = new ProcessRole();
		WorkProduct wp1 = new WorkProduct();
		WorkProduct wp2 = new WorkProduct();
		
		mediator.insertInModel(new Object[]{role1}, new Object[]{p111}, null);
		mediator.insertInModel(new Object[]{wp1,wp2}, new Object[]{p12, p12}, null);
		mediator.insertIn(rd, new Object[]{role1,wp1,wp2},null);
		
		Link role1_wp1 = new Link(role1, wp1);
		Link role1_wp2 = new Link(role1, wp2);
		
		mediator.insertIn(rd, new Object[]{role1_wp1,role1_wp2},null);
		
		assertTrue(rd.existsLinkProcessRoleWorkProduct(role1,wp1));
		assertTrue(rd.existsLinkProcessRoleWorkProduct(role1,wp2));
		mediator.removeFromModel(new Object[]{p11}, null);
		assertFalse(p1.containsModelElement(p11));
		assertFalse(rd.containsModelElement(role1));
		assertFalse(rd.existsLinkProcessRoleWorkProduct(role1,wp1));
		assertFalse(rd.existsLinkProcessRoleWorkProduct(role1,wp2));
	}

	public void testRemoveFrom()
	{
		/*
		 * Standard tests 
		 */
		WorkDefinitionDiagram diagram = new WorkDefinitionDiagram("TestDiagram");
		
		mediator.insertInModel(new Object[]{diagram}, new Object[]{root}, null);
		
		WorkProduct required = new WorkProduct("required");
		WorkProduct provided = new WorkProduct("provided");
		ApesWorkDefinition wd = new ApesWorkDefinition("wd");
		ProcessRole role = new ProcessRole("role");
		mediator.insertInModel(new Object[]{root}, new Object[]{diagram}, null);
		mediator.insertIn(diagram, new Object[]{required, provided, wd, role}, null);

		Link required_wd = new Link(required, wd);
		Link wd_provided = new Link(wd, provided);
		Link role_wd = new Link(role, wd);
		mediator.insertIn(diagram, new Object[]{required_wd, wd_provided, role_wd}, null);

		mediator.removeFrom(diagram, new Object[]{wd}, null);
		assertFalse(diagram.containsModelElement(wd));
		assertFalse(diagram.existsLinkModelElements(required, wd,null));
		assertFalse(diagram.existsLinkModelElements(wd, provided,null));
		assertFalse(diagram.existsLinkModelElements(role, wd,null));
	
		/*
		 * WorkProductRef tests 
		 */
		ContextDiagram cd = (ContextDiagram) root.getModelElement(0);		
		mediator.insertIn(cd, new Object[]{required, provided, role, wd}, null);
		Link required_component = new Link(required, root);
		Link component_provided = new Link(root, provided);
		mediator.insertIn(cd, new Object[]{required_component, component_provided}, null);

		mediator.removeFrom(cd, new Object[]{required, component_provided}, null);
		assertFalse(cd.containsModelElement(required));
		assertFalse(cd.existsLinkModelElements(required,root,null));
		assertFalse(cd.existsLinkModelElements(root,provided,null));
		
		/*
		 * ProcessRole tests
		 */
		SpemGraphAdapter diagram_adapter = Context.getInstance().getProject().getGraphModel(diagram);
		SpemGraphAdapter flow_adapter = Context.getInstance().getProject().getGraphModel(wd.getFlowDiagram());
		diagram.setName("FlowDiagram");
		
		mediator.insertIn(diagram, new Object[]{wd}, null);
		mediator.insertIn(diagram, new Object[]{required_wd, wd_provided, role_wd}, null);
		Activity a1 = new Activity("a1");
		Activity a2 = new Activity("a2");
		Activity a3 = new Activity("a3");
		mediator.insertIn(wd.getFlowDiagram(), new Object[]{a1,a2,a3}, null);		
		ProcessRole role1 = new ProcessRole("role1");
		mediator.insertIn(wd.getFlowDiagram(), new Object[]{role, role1}, null);
		Link role1_a1 = new Link(role1, a1,"",new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		mediator.insertIn(wd.getFlowDiagram(), new Object[]{role1_a1}, null);
		assertEquals(6, flow_adapter.getRootCount());
		
		assertEquals(7, diagram_adapter.getRootCount());
		mediator.removeFrom(diagram, new Object[]{role},null);
		assertEquals(5, diagram_adapter.getRootCount());
		assertFalse(diagram.containsModelElement(role));
		assertFalse(diagram.existsLinkModelElements(role, wd,null));
		assertNull(wd.getOwner());
		assertNull(a3.getOwner());
		assertEquals(role1, a1.getOwner());
		assertNull(a2.getOwner());
		
		/*
		 * StateMachine tests
		 */
		ApesWorkDefinition wd1 = new ApesWorkDefinition();
		WorkProduct w = new WorkProduct();
		WorkProduct w1 = new WorkProduct();
		mediator.insertInModel(new Object[]{w,w1}, new Object[]{root,root},null);

		StateMachine sm = new StateMachine();
		StateMachine sm1 = new StateMachine();		
		StateMachine sm2 = new StateMachine();
		StateMachine sm3 = new StateMachine();		
		mediator.insertInModel(new Object[]{sm, sm1,sm2,sm3}, new Object[]{w, w, w1, w1},null);
		mediator.insertIn(diagram, new Object[]{wd1, w, w1, sm2, sm3}, null);
		
		Link w_wd1 = new Link(w, wd1);
		Link sm1_wd1 = new Link(sm1, wd1);
		Link wd1_sm2 = new Link(wd1, sm2);
		Link w1_wd1 = new Link(w1, wd1);
		Link sm3_wd1 = new Link(sm3, wd1);		
		mediator.insertIn(diagram, new Object[]{w_wd1, sm3_wd1},null);

		assertTrue(diagram.containsModelElement(sm3));
		assertTrue(diagram.existsLinkModelElements(w1, wd1,null));
		assertTrue(diagram.existsLinkModelElements(sm3, wd1,null));
		mediator.removeFrom(diagram, new Object[]{sm3}, null);
		assertFalse(diagram.containsModelElement(sm3));
		assertFalse(diagram.existsLinkModelElements(w1, wd1,null));
		assertFalse(diagram.existsLinkModelElements(sm3, wd1,null));		
	}
	
	public void testMove()
	{
		/*
		 * Activity moved tests
		 */
		SPackage p = new SPackage("p");
		mediator.insertInModel(new Object[]{p}, new Object[]{root}, null);
		ApesWorkDefinition wd1 = new ApesWorkDefinition("wd1");
		ApesWorkDefinition wd2 = new ApesWorkDefinition("wd2");
		mediator.insertInModel(new Object[]{wd1, wd2}, new Object[]{p,p}, null);
		assertTrue(p.containsModelElement(wd1));
		assertTrue(p.containsModelElement(wd2));
		assertEquals(2, p.modelElementCount());
		
		Activity a = new Activity("a");
		Activity a1 = new Activity("a1");
		Activity a2 = new Activity("a2");
		
		ProcessRole r = new ProcessRole("r");
		ProcessRole r1 = new ProcessRole("r1");
		ProcessRole r2 = new ProcessRole("r2");
		WorkProduct wp = new WorkProduct("wp");
		
		mediator.insertInModel(new Object[]{r,r1,r2,wp,a1,a2}, new Object[]{p,p,p,p,wd1,wd1}, null);
		assertTrue(p.containsModelElement(r));
		assertTrue(p.containsModelElement(r1));
		assertTrue(p.containsModelElement(r2));
		assertTrue(p.containsModelElement(wp));
		assertTrue(wd1.containsModelElement(a1));
		assertTrue(wd1.containsModelElement(a2));
		
		mediator.insertIn(wd1.getFlowDiagram(), new Object[]{a2,wp,r}, null);
		assertTrue(wd1.getFlowDiagram().containsModelElement(a2));
		assertTrue(wd1.getFlowDiagram().containsModelElement(wp));
		assertTrue(wd1.getFlowDiagram().containsModelElement(r));
		
		Link r_a2 = new Link(r,a2,"",new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		Link wp_a2 = new Link(wp,a2);
		mediator.insertIn(wd1.getFlowDiagram(), new Object[]{r_a2, wp_a2}, null);
		assertEquals(a2.getOwner(), r);
		assertEquals(a2.getInputCount(), 1);
		assertEquals(a2.getInput(0), wp);
		
		Map moves = ApesGraphConstants.createMap();
		moves.put(a1, wd2);
		moves.put(a2, wd2);
		mediator.move(null, moves, null);
		assertEquals(wd2, a1.getParent());
		assertEquals(wd2, a2.getParent());
		assertNull(a1.getOwner());
		assertNull(a2.getOwner());
		assertEquals(a1.getInputCount(),0);
		assertEquals(a1.getOutputCount(),0);
		assertEquals(a2.getInputCount(),0);
		assertEquals(a2.getOutputCount(),0);
		assertFalse(wd1.getFlowDiagram().containsModelElement(a1));
		assertFalse(wd1.getFlowDiagram().containsModelElement(a2));
		
		moves.put(a1, wd1);
		moves.put(a2, wd1);
		mediator.move(null, moves, null);
		assertEquals(wd1, a1.getParent());
		assertEquals(wd1, a2.getParent());
		assertNull(a1.getOwner());
		assertNull(a2.getOwner());
		assertEquals(a1.getInputCount(),0);
		assertEquals(a1.getOutputCount(),0);
		assertEquals(a2.getInputCount(),0);
		assertEquals(a2.getOutputCount(),0);
		
		wd2.setOwner(r1);
		r1.addFeature(wd2);
		mediator.insertIn(wd1.getFlowDiagram(), new Object[]{a2},null);
		mediator.insertIn(wd1.getFlowDiagram(), new Object[]{r_a2, wp_a2},null);
		moves.put(a1, wd2);
		moves.put(a2, wd2);
		mediator.move(null, moves, null);
		assertEquals(wd2, a1.getParent());
		assertEquals(wd2, a2.getParent());
		assertEquals(a1.getOwner(),r1);
		assertEquals(a2.getOwner(),r1);
		assertEquals(a1.getInputCount(),0);
		assertEquals(a1.getOutputCount(),0);
		assertEquals(a2.getInputCount(),0);
		assertEquals(a2.getOutputCount(),0);
		
		moves.put(a1, wd1);
		moves.put(a2, wd1);
		mediator.move(null, moves, null);
		assertEquals(wd1, a1.getParent());
		assertEquals(wd1, a2.getParent());
		assertNull(a1.getOwner());
		assertNull(a2.getOwner());
		assertEquals(a1.getInputCount(),0);
		assertEquals(a1.getOutputCount(),0);
		assertEquals(a2.getInputCount(),0);
		assertEquals(a2.getOutputCount(),0);
	
		wd1.setOwner(r2);
		r2.addFeature(wd);
		mediator.insertIn(wd1.getFlowDiagram(), new Object[]{a2},null);
		mediator.insertIn(wd1.getFlowDiagram(), new Object[]{r_a2, wp_a2},null);
		moves.put(a1, wd2);
		moves.put(a2, wd2);
		mediator.move(null, moves, null);
		assertEquals(wd2, a1.getParent());
		assertEquals(wd2, a2.getParent());
		assertEquals(a1.getOwner(),r1);
		assertEquals(a2.getOwner(),r1);
		assertEquals(a1.getInputCount(),0);
		assertEquals(a1.getOutputCount(),0);
		assertEquals(a2.getInputCount(),0);
		assertEquals(a2.getOutputCount(),0);
	}
	
	public void testUndoRedo()
	{
		/*
		 * WorkProductRef tests 
		 */
		ContextDiagram cd = (ContextDiagram) root.getModelElement(0);
		
		mediator.insertIn(cd, new Object[]{required, provided, role, wd}, null);
		
		assertEquals(3, cd.modelElementCount());
		assertTrue(cd.containsModelElement(required));
		assertTrue(cd.containsModelElement(provided));
		
		Link required_component = new Link(required, root);
		Link component_provided = new Link(root, provided);
		
		assertEquals(0, Context.getInstance().getProject().getProcess().getProvidedInterface().modelElementCount());
		assertEquals(0, Context.getInstance().getProject().getProcess().getRequiredInterface().modelElementCount());
		mediator.insertIn(cd, new Object[]{required_component, component_provided}, null);
		assertTrue(cd.existsLinkModelElements(required,root,null));
		assertTrue(cd.existsLinkModelElements(root, provided,null));
		assertEquals(1, Context.getInstance().getProject().getProcess().getProvidedInterface().modelElementCount());
		assertEquals(1, Context.getInstance().getProject().getProcess().getRequiredInterface().modelElementCount());
		
		context.getUndoManager().undo();
		assertEquals(0, Context.getInstance().getProject().getProcess().getProvidedInterface().modelElementCount());
		assertEquals(0, Context.getInstance().getProject().getProcess().getRequiredInterface().modelElementCount());
		
		context.getUndoManager().redo();
		assertEquals(1, Context.getInstance().getProject().getProcess().getProvidedInterface().modelElementCount());
		assertEquals(1, Context.getInstance().getProject().getProcess().getRequiredInterface().modelElementCount());

		/*
		 * diagram
		 */
		SPackage p = new SPackage();
		mediator.insertInModel(new Object[]{p}, new Object[]{root}, null);
		SPackage p1 = new SPackage();
		mediator.insertInModel(new Object[]{p1}, new Object[]{p}, null);
		ApesWorkDefinition wd = new ApesWorkDefinition();
		mediator.insertInModel(new Object[]{wd}, new Object[]{p1}, null);
		FlowDiagram fd = wd.getFlowDiagram();
		
		Activity a = new Activity();
		WorkProduct required = new WorkProduct("required");
		WorkProduct provided = new WorkProduct("provided");
		ProcessRole role = new ProcessRole("role");
		mediator.insertInModel(new Object[]{required, provided}, new Object[]{root, root}, null);
		mediator.insertIn(fd, new Object[]{required, provided, role, a}, null);

		Link required_a = new Link(required, a);
		Link a_provided = new Link(a, provided);
		Link role_a = new Link(role, a, "", new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));
		mediator.insertIn(fd, new Object[]{required_a, a_provided, role_a}, null);
		assertTrue(fd.existsLinkModelElements(role, a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		assertEquals(role, a.getOwner());
		
		mediator.removeFromModel(new Object[]{p}, null);
		assertFalse(root.containsModelElement(p));
		assertEquals(0, p.modelElementCount());
		assertEquals(0, p1.modelElementCount());
		assertEquals(0, wd.modelElementCount());
		assertEquals(0, fd.modelElementCount());
		assertNull(a.getOwner());
		assertEquals(0, a.getInputCount());
		assertEquals(0, a.getOutputCount());
		assertEquals(0, role.getResponsibilityCount());
		assertEquals(0, required.getInputCount());
		assertEquals(0, required.getOutputCount());
		assertEquals(0, provided.getInputCount());
		assertEquals(0, provided.getOutputCount());
		assertFalse(fd.existsLinkModelElements(required, a,null));
		assertFalse(fd.existsLinkModelElements(a, provided,null));
		assertFalse(fd.existsLinkModelElements(role, a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		
		context.getUndoManager().undo(null);
		assertTrue(root.containsModelElement(p));
		assertTrue(p.containsModelElement(p1));
		assertTrue(p1.containsModelElement(wd));
		assertTrue(wd.containsModelElement(fd));
		assertTrue(fd.containsModelElement(a));
		assertTrue(fd.containsModelElement(role));
		assertTrue(fd.containsModelElement(required));
		assertTrue(fd.containsModelElement(provided));
		assertEquals(role, a.getOwner());
		assertEquals(1, a.getInputCount());
		assertEquals(required, a.getInput(0));
		assertEquals(1, a.getOutputCount());
		assertEquals(provided, a.getOutput(0));
		assertEquals(1, role.getFeatureCount());
		assertEquals(a, role.getFeature(0));
		assertEquals(0, required.getInputCount());
		assertEquals(1, required.getOutputCount());
		assertEquals(a, required.getOutput(0));
		assertEquals(1, provided.getInputCount());
		assertEquals(a, provided.getInput(0));
		assertEquals(0, provided.getOutputCount());
		assertTrue(fd.existsLinkModelElements(required, a,null));
		assertTrue(fd.existsLinkModelElements(a, provided,null));
		assertTrue(fd.existsLinkModelElements(role, a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));
		
		context.getUndoManager().undo(null);
		assertNull(a.getOwner());
		assertEquals(0, a.getInputCount());
		assertEquals(0, a.getOutputCount());
		assertEquals(0, role.getResponsibilityCount());
		assertEquals(0, required.getInputCount());
		assertEquals(0, required.getOutputCount());
		assertEquals(0, provided.getInputCount());
		assertEquals(0, provided.getOutputCount());
		assertFalse(fd.existsLinkModelElements(required, a,null));
		assertFalse(fd.existsLinkModelElements(a, provided,null));
		assertFalse(fd.existsLinkModelElements(role, a,
				new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)));			
	}
}
