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

import javax.swing.JDesktopPane;
import javax.swing.JTree;
import javax.swing.undo.CannotUndoException;

import junit.framework.TestCase;

import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.DecisionCell;
import org.ipsquad.apes.adapters.FinalPointCell;
import org.ipsquad.apes.adapters.InitialPointCell;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.adapters.SynchroCell;
import org.ipsquad.apes.adapters.TransitionEdge;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.ipsquad.utils.ConfigManager;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;

public class TestActivityGraphAdapter extends TestCase
{
	static SpemTreeAdapter model;
	static ActivityDiagram diagram1;
	static ActivityGraphAdapter adapter1;
	static ActivityDiagram diagram2;
	static ActivityGraphAdapter adapter2;
	static ApesGraphCell ac;
	static Context context;
	static ApesWorkDefinition wd1;
	static ApesWorkDefinition wd2;
	static ApesMediator mediator;
	static ProcessComponent root;
	
	//public TestSpemGraphAdapter()
	protected void setUp()
	{
		ConfigManager.init(ApesMain.createDefaultProperties());
		
		context = Context.getInstance();
		model = new SpemTreeAdapter();
		wd1 = new ApesWorkDefinition();
		wd2 = new ApesWorkDefinition();
		mediator = ApesMediator.getInstance();
		
		context.setTopLevelFrame(new MainFrameInterface()
		{
			private JTree tree = new JTree(model);
			
			public JTree getTree() { return tree; }
			public JDesktopPane getDesktop() { return new JDesktopPane(); }
			public void setToolPalette(ToolPalette toolPalette) { fail(); }
			public ToolPalette getToolPalette() { fail(); return null; }
			public void openDiagram(GraphModel model) { fail(); }
			public void deleteDiagram(GraphModel model) { }
			public void setDefaultToolPalette() { fail(); }
			public void setFilePath(String filePath) { }
			public GraphFrame getGraphFrame(GraphModel model) { fail(); return null; }
			
		});
	
		context.setProject(new Project());

		root = context.getProject().getProcess().getComponent();
		wd1 = new ApesWorkDefinition();
		wd2 = new ApesWorkDefinition();
		mediator.insertInModel(new Object[]{wd1,wd2}, new Object[]{root, root}, null);
		diagram1 = wd1.getActivityDiagram();
		diagram2 = wd2.getActivityDiagram();
		adapter1 = (ActivityGraphAdapter)Context.getInstance().getProject().getGraphModel(diagram1);
		adapter2 = (ActivityGraphAdapter)Context.getInstance().getProject().getGraphModel(diagram2);

		assertEquals(0, adapter1.getRootCount());
		assertEquals(0, adapter2.getRootCount());	
	}
	
	protected void tearDown()
	{
		mediator.removeFromModel(new Object[]{root}, null);
		mediator.clearAll();
	}

	public void testInsert()
	{
		/*
		 * insert cells in adapter1
		 */
		ActivityDiagram.InitialPoint initialPoint = new ActivityDiagram.InitialPoint();
		ActivityDiagram.FinalPoint finalPoint = new ActivityDiagram.FinalPoint();
		ActivityDiagram.Synchro firstSynchro = new ActivityDiagram.Synchro();
		ActivityDiagram.Synchro lastSynchro = new ActivityDiagram.Synchro();
		ActivityDiagram.Decision decision = new ActivityDiagram.Decision();
		Activity activity1 = new Activity();
		Activity activity2 = new Activity();
		Activity activity3 = new Activity();
		Activity activity4 = new Activity();

		InitialPointCell initialPointCell = new InitialPointCell(initialPoint);
		FinalPointCell finalPointCell = new FinalPointCell(finalPoint);
		SynchroCell firstSynchroCell = new SynchroCell(firstSynchro); 
		SynchroCell lastSynchroCell = new SynchroCell(lastSynchro); 
		DecisionCell decisionCell = new DecisionCell(decision);
		ActivityCell activityCell1 = new ActivityCell(activity1);
		ActivityCell activityCell2 = new ActivityCell(activity2);
		ActivityCell activityCell3 = new ActivityCell(activity3);
		ActivityCell activityCell4 = new ActivityCell(activity4);
		
		adapter1.insert(new Object[]{initialPointCell, finalPointCell, firstSynchroCell,
						lastSynchroCell, decisionCell, activityCell1, activityCell2,
						activityCell3, activityCell4}, null, null, null, null);
		
		assertEquals(9, diagram1.modelElementCount());
		assertTrue(diagram1.containsModelElement(initialPoint));
		assertTrue(diagram1.containsModelElement(finalPoint));
		assertTrue(diagram1.containsModelElement(firstSynchro));
		assertTrue(diagram1.containsModelElement(lastSynchro));
		assertTrue(diagram1.containsModelElement(decision));
		assertTrue(diagram1.containsModelElement(activity1));
		assertTrue(diagram1.containsModelElement(activity2));
		assertTrue(diagram1.containsModelElement(activity3));
		assertTrue(diagram1.containsModelElement(activity4));
		
		assertEquals(9, adapter1.getRootCount());
		assertTrue(adapter1.contains(initialPointCell));
		assertTrue(adapter1.contains(finalPointCell));
		assertTrue(adapter1.contains(firstSynchroCell));
		assertTrue(adapter1.contains(lastSynchroCell));
		assertTrue(adapter1.contains(decisionCell));
		assertTrue(adapter1.contains(activityCell1));
		assertTrue(adapter1.contains(activityCell2));
		assertTrue(adapter1.contains(activityCell3));
		assertTrue(adapter1.contains(activityCell4));
		
		/*
		 * try insert same activities in adapter2
		 */
		adapter2.insert(new Object[]{activityCell1, activityCell2, activityCell3, activityCell4}, null, null, null, null);
		
		assertEquals(0, diagram2.modelElementCount());
		assertEquals(0, adapter2.getRootCount());
		
		/*
		 * insert links beetween elements
		 */
		ActivityDiagram.Transition t_initialPoint_firstSynchro = new ActivityDiagram.Transition(initialPoint, firstSynchro, "");
		ActivityDiagram.Transition t_firstSynchro_activity1 = new ActivityDiagram.Transition(firstSynchro, activity1, "");
		ActivityDiagram.Transition t_firstSynchro_activity2 = new ActivityDiagram.Transition(firstSynchro, activity2, "");
		ActivityDiagram.Transition t_activity1_lastSynchro = new ActivityDiagram.Transition(activity1, lastSynchro, "");
		ActivityDiagram.Transition t_activity2_lastSynchro = new ActivityDiagram.Transition(activity2, lastSynchro, "");
		ActivityDiagram.Transition t_lastSynchro_decision = new ActivityDiagram.Transition(lastSynchro, decision, "");
		ActivityDiagram.Transition t_decision_activity3 = new ActivityDiagram.Transition(decision, activity3, "");
		ActivityDiagram.Transition t_decision_activity4 = new ActivityDiagram.Transition(decision, activity4, "");
		ActivityDiagram.Transition t_activity3_finalPoint = new ActivityDiagram.Transition(activity3, finalPoint, "");
		ActivityDiagram.Transition t_activity4_finalPoint = new ActivityDiagram.Transition(activity4, finalPoint, "");
		
		TransitionEdge t1 = new TransitionEdge(t_initialPoint_firstSynchro);
		t1.setSource(initialPointCell.getChildAt(0));
		t1.setTarget(firstSynchroCell.getChildAt(0));
		TransitionEdge t2 = new TransitionEdge(t_firstSynchro_activity1);
		t2.setSource(firstSynchroCell.getChildAt(0));
		t2.setTarget(activityCell1.getChildAt(0));
		TransitionEdge t3 = new TransitionEdge(t_firstSynchro_activity2);
		t3.setSource(firstSynchroCell.getChildAt(0));
		t3.setTarget(activityCell2.getChildAt(0));
		TransitionEdge t4 = new TransitionEdge(t_activity1_lastSynchro);
		t4.setSource(activityCell1.getChildAt(0));
		t4.setTarget(lastSynchroCell.getChildAt(0));
		TransitionEdge t5 = new TransitionEdge(t_activity2_lastSynchro);
		t5.setSource(activityCell2.getChildAt(0));
		t5.setTarget(lastSynchroCell.getChildAt(0));
		TransitionEdge t6 = new TransitionEdge(t_lastSynchro_decision);
		t6.setSource(lastSynchroCell.getChildAt(0));
		t6.setTarget(decisionCell.getChildAt(0));
		TransitionEdge t7 = new TransitionEdge(t_decision_activity3);
		t7.setSource(decisionCell.getChildAt(0));
		t7.setTarget(activityCell3.getChildAt(0));
		TransitionEdge t8 = new TransitionEdge(t_decision_activity4);
		t8.setSource(decisionCell.getChildAt(0));
		t8.setTarget(activityCell4.getChildAt(0));
		TransitionEdge t9 = new TransitionEdge(t_activity3_finalPoint);
		t9.setSource(activityCell3.getChildAt(0));
		t9.setTarget(finalPointCell.getChildAt(0));
		TransitionEdge t10 = new TransitionEdge(t_activity4_finalPoint);
		t10.setSource(activityCell4.getChildAt(0));
		t10.setTarget(finalPointCell.getChildAt(0));
		
		adapter1.insert(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10}, null, null, null, null);
		
		assertTrue(diagram1.existsLinkModelElements(initialPoint, firstSynchro));
		assertTrue(diagram1.existsLinkModelElements(firstSynchro, activity1));
		assertTrue(diagram1.existsLinkModelElements(firstSynchro, activity2));
		assertTrue(diagram1.existsLinkModelElements(activity1, lastSynchro));
		assertTrue(diagram1.existsLinkModelElements(activity2, lastSynchro));
		assertTrue(diagram1.existsLinkModelElements(lastSynchro, decision));
		assertTrue(diagram1.existsLinkModelElements(decision, activity3));
		assertTrue(diagram1.existsLinkModelElements(decision, activity4));
		assertTrue(diagram1.existsLinkModelElements(activity3, finalPoint));
		assertTrue(diagram1.existsLinkModelElements(activity4, finalPoint));
		assertEquals(19, adapter1.getRootCount());
		assertTrue(adapter1.contains(t1));
		assertTrue(adapter1.contains(t2));
		assertTrue(adapter1.contains(t3));
		assertTrue(adapter1.contains(t4));
		assertTrue(adapter1.contains(t5));
		assertTrue(adapter1.contains(t6));
		assertTrue(adapter1.contains(t7));
		assertTrue(adapter1.contains(t8));
		assertTrue(adapter1.contains(t9));
		assertTrue(adapter1.contains(t10));
		
		/*
		 * try insert bad links
		 */
		falseTest(finalPointCell, initialPointCell);
		falseTest(finalPointCell, activityCell1);
		falseTest(finalPointCell, firstSynchroCell);
		falseTest(finalPointCell, decisionCell);
				
		falseTest(activityCell2, decisionCell);
		falseTest(firstSynchroCell, decisionCell);
	}
	
	public void falseTest(DefaultGraphCell cell1, DefaultGraphCell cell2)
	{
		ActivityDiagram.Transition falseTest = new ActivityDiagram.Transition((ModelElement)cell1.getUserObject(), (ModelElement)cell2.getUserObject(), "");
		TransitionEdge t_falseTest = new TransitionEdge(falseTest);
		t_falseTest.setSource(cell1.getChildAt(0));
		t_falseTest.setTarget(cell2.getChildAt(0));
		adapter1.insert(new Object[]{t_falseTest}, null, null, null, null);
		
		assertFalse(diagram1.existsLinkModelElements((ModelElement)cell1.getUserObject(), (ModelElement)cell2.getUserObject()));
		assertFalse(adapter1.contains(t_falseTest));		
	}
	
	public void testRemove()
	{
		/*
		 * insert cells in adapter1
		 */
		ActivityDiagram.InitialPoint initialPoint = new ActivityDiagram.InitialPoint();
		ActivityDiagram.FinalPoint finalPoint = new ActivityDiagram.FinalPoint();
		ActivityDiagram.Synchro firstSynchro = new ActivityDiagram.Synchro();
		ActivityDiagram.Synchro lastSynchro = new ActivityDiagram.Synchro();
		ActivityDiagram.Decision decision = new ActivityDiagram.Decision();
		Activity activity1 = new Activity();
		Activity activity2 = new Activity();
		Activity activity3 = new Activity();
		Activity activity4 = new Activity();

		InitialPointCell initialPointCell = new InitialPointCell(initialPoint);
		FinalPointCell finalPointCell = new FinalPointCell(finalPoint);
		SynchroCell firstSynchroCell = new SynchroCell(firstSynchro); 
		SynchroCell lastSynchroCell = new SynchroCell(lastSynchro); 
		DecisionCell decisionCell = new DecisionCell(decision);
		ActivityCell activityCell1 = new ActivityCell(activity1);
		ActivityCell activityCell2 = new ActivityCell(activity2);
		ActivityCell activityCell3 = new ActivityCell(activity3);
		ActivityCell activityCell4 = new ActivityCell(activity4);
		
		adapter1.insert(new Object[]{initialPointCell, finalPointCell, firstSynchroCell,
						lastSynchroCell, decisionCell, activityCell1, activityCell2,
						activityCell3, activityCell4}, null, null, null, null);
		
		/*
		 * insert links beetween elements
		 */
		ActivityDiagram.Transition t_initialPoint_firstSynchro = new ActivityDiagram.Transition(initialPoint, firstSynchro, "");
		ActivityDiagram.Transition t_firstSynchro_activity1 = new ActivityDiagram.Transition(firstSynchro, activity1, "");
		ActivityDiagram.Transition t_firstSynchro_activity2 = new ActivityDiagram.Transition(firstSynchro, activity2, "");
		ActivityDiagram.Transition t_activity1_lastSynchro = new ActivityDiagram.Transition(activity1, lastSynchro, "");
		ActivityDiagram.Transition t_activity2_lastSynchro = new ActivityDiagram.Transition(activity2, lastSynchro, "");
		ActivityDiagram.Transition t_lastSynchro_decision = new ActivityDiagram.Transition(lastSynchro, decision, "");
		ActivityDiagram.Transition t_decision_activity3 = new ActivityDiagram.Transition(decision, activity3, "");
		ActivityDiagram.Transition t_decision_activity4 = new ActivityDiagram.Transition(decision, activity4, "");
		ActivityDiagram.Transition t_activity3_finalPoint = new ActivityDiagram.Transition(activity3, finalPoint, "");
		ActivityDiagram.Transition t_activity4_finalPoint = new ActivityDiagram.Transition(activity4, finalPoint, "");
		
		TransitionEdge t1 = new TransitionEdge(t_initialPoint_firstSynchro);
		t1.setSource(initialPointCell.getChildAt(0));
		t1.setTarget(firstSynchroCell.getChildAt(0));
		TransitionEdge t2 = new TransitionEdge(t_firstSynchro_activity1);
		t2.setSource(firstSynchroCell.getChildAt(0));
		t2.setTarget(activityCell1.getChildAt(0));
		TransitionEdge t3 = new TransitionEdge(t_firstSynchro_activity2);
		t3.setSource(firstSynchroCell.getChildAt(0));
		t3.setTarget(activityCell2.getChildAt(0));
		TransitionEdge t4 = new TransitionEdge(t_activity1_lastSynchro);
		t4.setSource(activityCell1.getChildAt(0));
		t4.setTarget(lastSynchroCell.getChildAt(0));
		TransitionEdge t5 = new TransitionEdge(t_activity2_lastSynchro);
		t5.setSource(activityCell2.getChildAt(0));
		t5.setTarget(lastSynchroCell.getChildAt(0));
		TransitionEdge t6 = new TransitionEdge(t_lastSynchro_decision);
		t6.setSource(lastSynchroCell.getChildAt(0));
		t6.setTarget(decisionCell.getChildAt(0));
		TransitionEdge t7 = new TransitionEdge(t_decision_activity3);
		t7.setSource(decisionCell.getChildAt(0));
		t7.setTarget(activityCell3.getChildAt(0));
		TransitionEdge t8 = new TransitionEdge(t_decision_activity4);
		t8.setSource(decisionCell.getChildAt(0));
		t8.setTarget(activityCell4.getChildAt(0));
		TransitionEdge t9 = new TransitionEdge(t_activity3_finalPoint);
		t9.setSource(activityCell3.getChildAt(0));
		t9.setTarget(finalPointCell.getChildAt(0));
		TransitionEdge t10 = new TransitionEdge(t_activity4_finalPoint);
		t10.setSource(activityCell4.getChildAt(0));
		t10.setTarget(finalPointCell.getChildAt(0));
		
		adapter1.insert(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10}, null, null, null, null);

		/*
		 * Remove lastSynchro/activity1/finalPoint
		 */
		adapter1.remove(new Object[]{lastSynchroCell, activityCell1, finalPointCell});
		
		assertTrue(diagram1.containsModelElement(initialPoint));
		assertFalse(diagram1.containsModelElement(finalPoint));
		assertTrue(diagram1.containsModelElement(firstSynchro));
		assertFalse(diagram1.containsModelElement(lastSynchro));
		assertTrue(diagram1.containsModelElement(decision));
		assertFalse(diagram1.containsModelElement(activity1));
		assertTrue(diagram1.containsModelElement(activity2));
		assertTrue(diagram1.containsModelElement(activity3));
		assertTrue(diagram1.containsModelElement(activity4));
		assertTrue(diagram1.existsLinkModelElements(initialPoint, firstSynchro));
		assertFalse(diagram1.existsLinkModelElements(firstSynchro, activity1));
		assertTrue(diagram1.existsLinkModelElements(firstSynchro, activity2));
		assertFalse(diagram1.existsLinkModelElements(activity1, lastSynchro));
		assertFalse(diagram1.existsLinkModelElements(activity2, lastSynchro));
		assertFalse(diagram1.existsLinkModelElements(lastSynchro, decision));
		assertTrue(diagram1.existsLinkModelElements(decision, activity3));
		assertTrue(diagram1.existsLinkModelElements(decision, activity4));
		assertFalse(diagram1.existsLinkModelElements(activity3, finalPoint));
		assertFalse(diagram1.existsLinkModelElements(activity4, finalPoint));
		
		assertEquals(10, adapter1.getRootCount());
		assertTrue(adapter1.contains(initialPointCell));
		assertFalse(adapter1.contains(finalPointCell));
		assertTrue(adapter1.contains(firstSynchroCell));
		assertFalse(adapter1.contains(lastSynchroCell));
		assertTrue(adapter1.contains(decisionCell));
		assertFalse(adapter1.contains(activityCell1));
		assertTrue(adapter1.contains(activityCell2));
		assertTrue(adapter1.contains(activityCell3));
		assertTrue(adapter1.contains(activityCell4));
		assertTrue(adapter1.contains(t1));
		assertFalse(adapter1.contains(t2));
		assertTrue(adapter1.contains(t3));
		assertFalse(adapter1.contains(t4));
		assertFalse(adapter1.contains(t5));
		assertFalse(adapter1.contains(t6));
		assertTrue(adapter1.contains(t7));
		assertTrue(adapter1.contains(t8));
		assertFalse(adapter1.contains(t9));
		assertFalse(adapter1.contains(t10));
		
		/*
		 * remove all elements
		 */
		adapter1.remove(new Object[]{initialPointCell, firstSynchroCell, activityCell2, decisionCell, activityCell3, activityCell4, t1, t3, t7, t8});
		
		assertEquals(0, diagram1.modelElementCount());
		assertEquals(0, diagram1.getTransitionCount());
		assertEquals(0, adapter1.getRootCount());
	}
	
	public void testUndoRedo()
	{
		GraphUndoManager undoManager = context.getUndoManager();
		adapter1.addUndoableEditListener(undoManager);
		
		ActivityDiagram.InitialPoint initialPoint = new ActivityDiagram.InitialPoint();
		ActivityDiagram.FinalPoint finalPoint = new ActivityDiagram.FinalPoint();
		ActivityDiagram.Synchro firstSynchro = new ActivityDiagram.Synchro();
		ActivityDiagram.Synchro lastSynchro = new ActivityDiagram.Synchro();
		ActivityDiagram.Decision decision = new ActivityDiagram.Decision();
		Activity activity1 = new Activity();
		Activity activity2 = new Activity();
		Activity activity3 = new Activity();
		Activity activity4 = new Activity();

		InitialPointCell initialPointCell = new InitialPointCell(initialPoint);
		FinalPointCell finalPointCell = new FinalPointCell(finalPoint);
		SynchroCell firstSynchroCell = new SynchroCell(firstSynchro); 
		SynchroCell lastSynchroCell = new SynchroCell(lastSynchro); 
		DecisionCell decisionCell = new DecisionCell(decision);
		ActivityCell activityCell1 = new ActivityCell(activity1);
		ActivityCell activityCell2 = new ActivityCell(activity2);
		ActivityCell activityCell3 = new ActivityCell(activity3);
		ActivityCell activityCell4 = new ActivityCell(activity4);
		
		adapter1.insert(new Object[]{initialPointCell, finalPointCell, firstSynchroCell,
						lastSynchroCell, decisionCell, activityCell1, activityCell2,
						activityCell3, activityCell4}, null, null, null, null);
		
		ActivityDiagram.Transition t_initialPoint_firstSynchro = new ActivityDiagram.Transition(initialPoint, firstSynchro, "");
		ActivityDiagram.Transition t_firstSynchro_activity1 = new ActivityDiagram.Transition(firstSynchro, activity1, "");
		ActivityDiagram.Transition t_firstSynchro_activity2 = new ActivityDiagram.Transition(firstSynchro, activity2, "");
		ActivityDiagram.Transition t_activity1_lastSynchro = new ActivityDiagram.Transition(activity1, lastSynchro, "");
		ActivityDiagram.Transition t_activity2_lastSynchro = new ActivityDiagram.Transition(activity2, lastSynchro, "");
		ActivityDiagram.Transition t_lastSynchro_decision = new ActivityDiagram.Transition(lastSynchro, decision, "");
		ActivityDiagram.Transition t_decision_activity3 = new ActivityDiagram.Transition(decision, activity3, "");
		ActivityDiagram.Transition t_decision_activity4 = new ActivityDiagram.Transition(decision, activity4, "");
		ActivityDiagram.Transition t_activity3_finalPoint = new ActivityDiagram.Transition(activity3, finalPoint, "");
		ActivityDiagram.Transition t_activity4_finalPoint = new ActivityDiagram.Transition(activity4, finalPoint, "");
		
		TransitionEdge t1 = new TransitionEdge(t_initialPoint_firstSynchro);
		t1.setSource(initialPointCell.getChildAt(0));
		t1.setTarget(firstSynchroCell.getChildAt(0));
		TransitionEdge t2 = new TransitionEdge(t_firstSynchro_activity1);
		t2.setSource(firstSynchroCell.getChildAt(0));
		t2.setTarget(activityCell1.getChildAt(0));
		TransitionEdge t3 = new TransitionEdge(t_firstSynchro_activity2);
		t3.setSource(firstSynchroCell.getChildAt(0));
		t3.setTarget(activityCell2.getChildAt(0));
		TransitionEdge t4 = new TransitionEdge(t_activity1_lastSynchro);
		t4.setSource(activityCell1.getChildAt(0));
		t4.setTarget(lastSynchroCell.getChildAt(0));
		TransitionEdge t5 = new TransitionEdge(t_activity2_lastSynchro);
		t5.setSource(activityCell2.getChildAt(0));
		t5.setTarget(lastSynchroCell.getChildAt(0));
		TransitionEdge t6 = new TransitionEdge(t_lastSynchro_decision);
		t6.setSource(lastSynchroCell.getChildAt(0));
		t6.setTarget(decisionCell.getChildAt(0));
		TransitionEdge t7 = new TransitionEdge(t_decision_activity3);
		t7.setSource(decisionCell.getChildAt(0));
		t7.setTarget(activityCell3.getChildAt(0));
		TransitionEdge t8 = new TransitionEdge(t_decision_activity4);
		t8.setSource(decisionCell.getChildAt(0));
		t8.setTarget(activityCell4.getChildAt(0));
		TransitionEdge t9 = new TransitionEdge(t_activity3_finalPoint);
		t9.setSource(activityCell3.getChildAt(0));
		t9.setTarget(finalPointCell.getChildAt(0));
		TransitionEdge t10 = new TransitionEdge(t_activity4_finalPoint);
		t10.setSource(activityCell4.getChildAt(0));
		t10.setTarget(finalPointCell.getChildAt(0));
		
		adapter1.insert(new Object[]{t1, t2, t3, t4, t5, t6, t7, t8, t9, t10}, null, null, null, null);
		adapter1.remove(new Object[]{lastSynchroCell, activityCell1, finalPointCell});
		adapter1.remove(new Object[]{initialPointCell, firstSynchroCell, activityCell2, decisionCell, activityCell3, activityCell4, t1, t3, t7, t8});
		
		
		//FIXME : problem with an undoableEdit of DefaultGraphModel...
		try
		{
			undoManager.undo(null);
		}
		catch(CannotUndoException e)
		{
			e.printStackTrace();
		}
		
		assertTrue(adapter1.contains(initialPointCell));
		assertTrue(adapter1.contains(firstSynchroCell));
		assertTrue(adapter1.contains(activityCell2));
		assertTrue(adapter1.contains(decisionCell));
		assertTrue(adapter1.contains(activityCell3));
		assertTrue(adapter1.contains(activityCell4));
		assertTrue(adapter1.contains(t1));
		assertTrue(adapter1.contains(t3));
		assertTrue(adapter1.contains(t7));
		assertTrue(adapter1.contains(t8));
	}

}
