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
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.NoteCell;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.ipsquad.utils.ConfigManager;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

public class TestSpemGraphAdapter extends TestCase
{
	static Context context = Context.getInstance();
	
	static ProcessComponent root;
	static SpemTreeAdapter model;
	static IPackage parentParent, parent;
	static SpemDiagram diagram;
	static SpemGraphAdapter adapter;
	static ApesGraphCell vertex1, vertex2, vertex3;
	static DefaultEdge edge1, edge2;
	static Port source1, source2, target1, target2;
	static ApesMediator mediator;
	
	//public TestSpemGraphAdapter()
	static {
		ConfigManager.init(ApesMain.createDefaultProperties());
		
		context = Context.getInstance();
		
		model = new SpemTreeAdapter();

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
			public void setFilePath(String filePath) { }//fail(); }
			public GraphFrame getGraphFrame(GraphModel model) { fail(); return null; }
		});
		
		context.setProject(new Project());		
		
		mediator = ApesMediator.getInstance();
	}
	
	
	protected void setUp()
	{
		context.setProject(new Project());		
		root = context.getProject().getProcess().getComponent();
		
		diagram = new FlowDiagram("FlowDiagram");
		parentParent = new SPackage();
		parent = new ApesWorkDefinition();
		
		parentParent.addModelElement( (ModelElement)parent );
		parent.addModelElement( diagram );
		
		
		adapter = new SpemGraphAdapter(diagram)
		{
			{
				mBuilder = new Builder(){
					public DefaultGraphCell create( Object o )
					{
						if( o instanceof Element )
						{
							((Element)o).visit( this );
							return mCreated;
						}
						return null;
					}
					
					public void visitProduct(WorkProduct product) 
					{
						mCreated = new WorkProductCell( product );
					}

					public void visitRole(ProcessRole role) 
					{
						mCreated = new ProcessRoleCell( role );
					}

					public void visitActivity(Activity activity) 
					{
						mCreated = new ActivityCell( activity );
					}

					public boolean shouldGoInGraph(Object o)
					{
						if( o instanceof Activity || o instanceof WorkProduct || o instanceof ProcessRole)
						{	
							return true;
						}
						return false;
					}
				};
			}
			
			public ApesGraphCell associateGraphCell(Object o)
			{
				if(o instanceof Activity)
				{
					return new ActivityCell((Activity)o);
				}
				else if(o instanceof ProcessRole)
				{
					return new ProcessRoleCell((ProcessRole)o);
				}
				else if(o instanceof WorkProduct)
				{
					return new WorkProductCell((WorkProduct)o);
				}

				return null;
			}

			protected boolean shouldGoInTree(Object o)
			{
				return true;
			}
			
			protected void postLinkCreation(Edge edge, ApesGraphCell source, ApesGraphCell target)
			{

			}
		};
		
		mediator.addApesModelListener( adapter );
		ApesMediator.getInstance().registerDiagram( diagram );
		
		context.getProject().getProcess().getComponent().addModelElement( diagram );
	}
		
	protected void tearDown()
	{
		mediator.removeFromModel(new Object[]{root}, null);
		mediator.clearAll();
	}

	public void testInsert()
	{
		assertEquals(0, adapter.getRootCount());
		
		/*
		 * insert an activity cell
		 */
		vertex1 = adapter.associateGraphCell(new Activity());
		Map attr = ApesGraphConstants.createMap();
		attr.put(vertex1, vertex1.getAttributes());	
		
		adapter.insert( new Object[]{vertex1}, attr, null, null, null );
		assertEquals(1, SpemGraphAdapter.getRoots(adapter).length);
		assertEquals(vertex1, SpemGraphAdapter.getRoots(adapter)[0]);
		
		/*
		 * insert an activity directly by the mediator
		 */
		Activity a = new Activity();
		mediator.insertIn(adapter.getDiagram(), new Object[]{a}, null);
		Object[] cells = SpemGraphAdapter.getRoots(adapter);
		assertEquals(2, cells.length);
		vertex1 = null;
		if(((DefaultGraphCell)cells[0]).getUserObject() == a)
			vertex1 = (ApesGraphCell)cells[0];
		else if(((DefaultGraphCell)cells[1]).getUserObject() == a)
			vertex1 = (ApesGraphCell)cells[1];
		assertNotNull(vertex1);
		
		/*
		 * insert a process role cell
		 */
		vertex2 = adapter.associateGraphCell(new ProcessRole());
		attr.clear();
		attr.put(vertex2, vertex2.getAttributes());
		
		assertFalse(adapter.contains(vertex2));
		adapter.insert( new Object[]{vertex2}, attr, null, null, null );
		assertEquals(3, adapter.getRootCount());
		assertTrue(adapter.contains(vertex2));
		
		/*
		 * insert a work product cell
		 */
		vertex3 = adapter.associateGraphCell(new WorkProduct());
		attr.clear();
		attr.put(vertex3, vertex3.getAttributes());
		
		assertFalse(adapter.contains(vertex3));
		adapter.insert( new Object[]{vertex3}, attr, null, null, null );
		assertEquals(4, adapter.getRootCount());
		assertTrue(adapter.contains(vertex3));
		
		/*
		 * insert a link beetween vertex2 and vertex1
		 */
		ConnectionSet cs = new ConnectionSet();
		DefaultEdge edge1 = new DefaultEdge();
		source1 = (Port)vertex2.getFirstChild();
		target1 = (Port)vertex1.getFirstChild();
		edge1.setSource(source1);
		edge1.setTarget(target1);
		cs.connect(edge1, source1, target1);

		attr.clear();
		attr.put(edge1, edge1.getAttributes());
		
		adapter.insert( new Object[]{edge1}, attr, cs, null, null);
		
		assertEquals(5, adapter.getRootCount());

		assertTrue(adapter.contains(edge1));
		Iterator it = source1.edges();
		assertEquals(edge1, it.next());
		assertFalse(it.hasNext());
		it = target1.edges();
		assertEquals(edge1, it.next());
		assertFalse(it.hasNext());
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));
		
		/*
		 * insert a link beetween vertex1 and vertex3
		 */
		cs = new ConnectionSet();
		DefaultEdge edge2 = new DefaultEdge();
		source1 = (Port)vertex1.getFirstChild();
		target1 = (Port)vertex3.getFirstChild();
		edge2.setSource(source1);
		edge2.setTarget(target1);
		cs.connect(edge2, source1, target1);

		attr.clear();
		attr.put(edge1, edge1.getAttributes());
		
		adapter.insert( new Object[]{edge2}, attr, cs, null, null);
		
		assertEquals(6, adapter.getRootCount());

		assertTrue(adapter.contains(edge2));
		it = source1.edges();
		Object tmp = it.next();
		assertTrue(edge2 == tmp || edge1 == tmp);
		tmp = it.next();
		assertTrue(edge2 == tmp || edge1 == tmp);
		assertFalse(it.hasNext());
		it = target1.edges();
		assertEquals(edge2, it.next());
		assertFalse(it.hasNext());
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex1.getUserObject(), (ModelElement) vertex3.getUserObject()));
	}

	public void testRemove()
	{
		assertEquals(0, adapter.getRootCount());
		
		vertex1 = adapter.associateGraphCell(new Activity());
		Map attr = ApesGraphConstants.createMap();
		attr.put(vertex1, vertex1.getAttributes());	
		adapter.insert( new Object[]{vertex1}, attr, null, null, null );
		
		Activity a = new Activity();
		mediator.insertIn(adapter.getDiagram(), new Object[]{a}, null);
		Object[] cells = SpemGraphAdapter.getRoots(adapter);
		vertex2 = adapter.associateGraphCell(new ProcessRole());
		attr.clear();
		attr.put(vertex2, vertex2.getAttributes());
		adapter.insert( new Object[]{vertex2}, attr, null, null, null );
		
		vertex3 = adapter.associateGraphCell(new WorkProduct());
		attr.clear();
		attr.put(vertex3, vertex3.getAttributes());
		adapter.insert( new Object[]{vertex3}, attr, null, null, null );
		
		ConnectionSet cs = new ConnectionSet();
		DefaultEdge edge1 = new DefaultEdge();
		source1 = (Port)vertex2.getFirstChild();
		target1 = (Port)vertex1.getFirstChild();
		edge1.setSource(source1);
		edge1.setTarget(target1);
		cs.connect(edge1, source1, target1);
		attr.clear();
		attr.put(edge1, edge1.getAttributes());		
		adapter.insert( new Object[]{edge1}, attr, cs, null, null);
		
		cs = new ConnectionSet();
		DefaultEdge edge2 = new DefaultEdge();
		source1 = (Port)vertex1.getFirstChild();
		target1 = (Port)vertex3.getFirstChild();
		edge2.setSource(source1);
		edge2.setTarget(target1);
		cs.connect(edge2, source1, target1);
		attr.clear();
		attr.put(edge1, edge1.getAttributes());		
		adapter.insert( new Object[]{edge2}, attr, cs, null, null);
		
		//Remove
		assertTrue(adapter.contains(edge1));
		adapter.remove(new Object[]{edge1});
		assertFalse(adapter.contains(edge1));
		Iterator it = source1.edges();
		assertNotSame(edge1, it.next());
		assertFalse(it.hasNext());
		it = target1.edges();
		assertNotSame(edge1, it.next());
		assertFalse(it.hasNext());
		assertFalse( diagram.existsLinkModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));
		assertTrue( diagram.areLinkableModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));
		
		adapter.remove(new Object[]{vertex2});
		assertFalse(adapter.contains(vertex2));
		
		
		adapter.remove(new Object[]{vertex3});
		assertFalse(adapter.contains(vertex3));
		assertFalse(adapter.contains(edge2));
		
		adapter.remove(new Object[]{vertex1});
		assertFalse(adapter.contains(vertex1));
		assertFalse(adapter.contains(edge2));
	}
	
	public void testRename()
	{
		assertEquals(0, adapter.getRootCount());
		
		vertex1 = adapter.associateGraphCell(new Activity());
		Map attr = ApesGraphConstants.createMap();
		attr.put(vertex1, vertex1.getAttributes());	
		adapter.insert( new Object[]{vertex1}, attr, null, null, null );
		
		double centerX = ApesGraphConstants.getBounds(vertex1.getAttributes()).getCenterX();
		
		Map newName = ApesGraphConstants.createMap();
		ApesGraphConstants.setValue(newName, "a name more longer than the width of the vertex...");
		Map edit = ApesGraphConstants.createMap();
		edit.put(vertex1, newName);
		adapter.edit(edit, null, null, null);
		assertEquals(centerX, ApesGraphConstants.getBounds(vertex1.getAttributes()).getCenterX());
		assertEquals("a name more longer than the width of the vertex...", vertex1.getUserObject().toString());
	}
	
	public void testUndoRedo()
	{
		adapter.remove(SpemGraphAdapter.getRoots(adapter));
		GraphUndoManager undoManager = Context.getInstance().getUndoManager();
		adapter.addUndoableEditListener(undoManager);
		
		//mediator.removeFrom(adapter.getDiagram(), new Object[]{((DefaultGraphCell)adapter.getRootAt(0)).getUserObject()}, null);
		assertEquals(0, adapter.getRootCount());
		//mediator.addApesModelListener( adapter );
		//assertEquals(0, adapter.getRootCount());
		
		/*
		 * insert an activity cell and a process role cell
		 */
		vertex1 = adapter.associateGraphCell(new Activity());
		Map attr = ApesGraphConstants.createMap();
		attr.put(vertex1, vertex1.getAttributes());	
		vertex2 = adapter.associateGraphCell(new ProcessRole());
		attr.put(vertex2, vertex2.getAttributes());
		
		assertFalse(adapter.contains(vertex1));
		assertFalse(adapter.contains(vertex2));
		adapter.insert( new Object[]{vertex1, vertex2}, attr, null, null, null );
		assertEquals(2, SpemGraphAdapter.getRoots(adapter).length);
		assertTrue(adapter.contains(vertex1));
		assertTrue(adapter.contains(vertex2));
		
		/*
		 * insert a work product cell
		 */
		vertex3 = adapter.associateGraphCell(new WorkProduct());
		attr.clear();
		attr.put(vertex3, vertex3.getAttributes());
		
		assertFalse(adapter.contains(vertex3));
		adapter.insert( new Object[]{vertex3}, attr, null, null, null );
		assertEquals(3, adapter.getRootCount());
		assertTrue(adapter.contains(vertex3));
		
		/*
		 * insert a link beetween vertex2 and vertex1
		 */
		ConnectionSet cs = new ConnectionSet();
		DefaultEdge edge1 = new DefaultEdge();
		source1 = (Port)vertex2.getFirstChild();
		target1 = (Port)vertex1.getFirstChild();
		edge1.setSource(source1);
		edge1.setTarget(target1);
		cs.connect(edge1, source1, target1);

		attr.clear();
		attr.put(edge1, edge1.getAttributes());
		
		adapter.insert( new Object[]{edge1}, attr, cs, null, null);
		
		assertEquals(4, adapter.getRootCount());

		assertTrue(adapter.contains(edge1));
		Iterator it = source1.edges();
		assertEquals(edge1, it.next());
		assertFalse(it.hasNext());
		it = target1.edges();
		assertEquals(edge1, it.next());
		assertFalse(it.hasNext());
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));
		
		/*
		 * insert a link beetween vertex1 and vertex3
		 */
		cs = new ConnectionSet();
		DefaultEdge edge2 = new DefaultEdge();
		Port source2 = (Port)vertex1.getFirstChild();
		Port target2 = (Port)vertex3.getFirstChild();
		edge2.setSource(source2);
		edge2.setTarget(target2);
		cs.connect(edge2, source2, target2);

		attr.clear();
		attr.put(edge2, edge2.getAttributes());
		
		adapter.insert( new Object[]{edge2}, attr, cs, null, null);
		
		assertEquals(5, adapter.getRootCount());

		assertTrue(adapter.contains(edge2));
		it = source2.edges();
		Object tmp = it.next();
		assertTrue(edge2 == tmp || edge1 == tmp);
		tmp = it.next();
		assertTrue(edge2 == tmp || edge1 == tmp);
		assertFalse(it.hasNext());
		it = target2.edges();
		assertEquals(edge2, it.next());
		assertFalse(it.hasNext());
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex1.getUserObject(), (ModelElement) vertex3.getUserObject()));
		
		/*
		 * Remove elements
		 */
		assertEquals(5, adapter.getRootCount());
		adapter.remove(new Object[]{vertex1});
		assertEquals(2, adapter.getRootCount());
		assertTrue(adapter.contains(vertex2));
		assertTrue(adapter.contains(vertex3));
		assertFalse(adapter.contains(vertex1));
		assertFalse(adapter.contains(edge1));
		assertFalse(adapter.contains(edge2));
		adapter.remove(new Object[]{vertex2,vertex3});
		assertEquals(0, adapter.getRootCount());
		
		undoRedo( undoManager, edge1, edge2, source2, target2 );
		
		for(int i = 0; i < 10; i++)
		{
			while(undoManager.canUndo())
			{
				undoManager.undo();
			}
			while(undoManager.canRedo())
			{
				undoManager.redo();
			}
		}
		
		undoRedo( undoManager, edge1, edge2, source2, target2 );
	}
	
	protected void undoRedo( GraphUndoManager undoManager, DefaultEdge edge1, DefaultEdge edge2, Port source2, Port target2 )
	{
		Iterator it;
		Object tmp;
		/*
		 * undo
		 */
		undoManager.undo(null);
		assertEquals(2, adapter.getRootCount());
		assertTrue(adapter.contains(vertex2));
		assertTrue(adapter.contains(vertex3));
		assertTrue(diagram.containsModelElement((ModelElement)vertex2.getUserObject()));
		assertTrue(diagram.containsModelElement((ModelElement)vertex3.getUserObject()));
		
		undoManager.undo(null);
		assertEquals(5, adapter.getRootCount());
		assertTrue(adapter.contains(vertex1));
		assertTrue(adapter.contains(edge1));
		assertTrue(adapter.contains(edge2));
		assertTrue(diagram.containsModelElement((ModelElement)vertex1.getUserObject()));
		assertTrue(diagram.existsLinkModelElements((ModelElement)vertex2.getUserObject(),(ModelElement)vertex1.getUserObject()));
		assertTrue(diagram.existsLinkModelElements((ModelElement)vertex1.getUserObject(),(ModelElement)vertex3.getUserObject()));
		assertEquals(source1, edge1.getSource());
		it = source1.edges();
		tmp = it.next();
		assertEquals(edge1, tmp);
		assertFalse(it.hasNext());
		assertEquals(target1, edge1.getTarget());
		it = target1.edges();
		tmp = it.next();
		assertTrue(edge1 == tmp || edge2 == tmp);
		tmp = it.next();
		assertTrue(edge1 == tmp || edge2 == tmp);
		assertFalse(it.hasNext());
		
		undoManager.undo(null);
		assertEquals(4, adapter.getRootCount());
		assertFalse(adapter.contains(edge2));
		it = source2.edges();
		tmp = it.next();
		assertNotSame(edge2, tmp);
		assertFalse(it.hasNext());
		assertNotSame(edge2, tmp);
		assertFalse(it.hasNext());
		it = target2.edges();
		assertFalse(it.hasNext());
		assertFalse( diagram.existsLinkModelElements( (ModelElement) vertex1.getUserObject(), (ModelElement) vertex3.getUserObject()));

		undoManager.undo(null);
		assertEquals(3, adapter.getRootCount());
		assertFalse(adapter.contains(edge1));
		it = source1.edges();
		assertFalse(it.hasNext());
		it = target1.edges();
		assertFalse(it.hasNext());
		assertFalse( diagram.existsLinkModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));

		undoManager.undo(null);
		assertEquals(2, adapter.getRootCount());
		assertFalse(adapter.contains(vertex3));
		
		undoManager.undo(null);
		assertEquals(0, SpemGraphAdapter.getRoots(adapter).length);
		assertFalse(adapter.contains(vertex1));
		assertFalse(adapter.contains(vertex2));
		
		assertFalse(undoManager.canUndo());
		
		/*
		 * redo
		 */
		undoManager.redo(null);
		assertEquals(2, SpemGraphAdapter.getRoots(adapter).length);
		assertTrue(adapter.contains(vertex1));
		assertTrue(adapter.contains(vertex2));
		
		undoManager.redo(null);
		assertEquals(3, adapter.getRootCount());
		assertTrue(adapter.contains(vertex3));
		
		undoManager.redo(null);
		assertEquals(4, adapter.getRootCount());
		assertTrue(adapter.contains(edge1));
		it = source1.edges();
		assertEquals(edge1, it.next());
		assertFalse(it.hasNext());
		it = target1.edges();
		assertEquals(edge1, it.next());
		assertFalse(it.hasNext());
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));
		
		undoManager.redo(null);
		assertEquals(5, adapter.getRootCount());
		assertTrue(adapter.contains(vertex1));
		assertTrue(adapter.contains(edge1));
		assertTrue(adapter.contains(edge2));
		assertTrue(diagram.containsModelElement((ModelElement)vertex1.getUserObject()));
		assertTrue(diagram.existsLinkModelElements((ModelElement)vertex2.getUserObject(),(ModelElement)vertex1.getUserObject()));
		assertTrue(diagram.existsLinkModelElements((ModelElement)vertex1.getUserObject(),(ModelElement)vertex3.getUserObject()));
		assertEquals(source1, edge1.getSource());
		it = source1.edges();
		tmp = it.next();
		assertEquals(edge1, tmp);
		assertFalse(it.hasNext());
		assertEquals(target1, edge1.getTarget());
		it = target1.edges();
		tmp = it.next();
		assertTrue(edge1 == tmp || edge2 == tmp);
		tmp = it.next();
		assertTrue(edge1 == tmp || edge2 == tmp);
		assertFalse(it.hasNext());
		
		undoManager.redo(null);
		assertEquals(2, adapter.getRootCount());
		assertTrue(adapter.contains(vertex2));
		assertTrue(adapter.contains(vertex3));
		assertTrue(diagram.containsModelElement((ModelElement)vertex2.getUserObject()));
		assertTrue(diagram.containsModelElement((ModelElement)vertex3.getUserObject()));
		
		undoManager.redo(null);
		assertEquals(0, adapter.getRootCount());
		
		assertFalse(undoManager.canRedo());
	}
	
	public void testNoteCell()
	{
	    assertEquals(0, adapter.getRootCount());
		
		/*
		 * insert a note cell
		 */
	    NoteCell cell = new NoteCell();
		Map attr = ApesGraphConstants.createMap();
		attr.put(cell, cell.getAttributes());	
		
		adapter.insert( new Object[]{cell}, attr, null, null, null );
		assertEquals(1, SpemGraphAdapter.getRoots(adapter).length);
		assertEquals(cell, SpemGraphAdapter.getRoots(adapter)[0]);
		
		/*
		 * adds some text
		 */
		Map text = ApesGraphConstants.createMap();
		ApesGraphConstants.setValue(text, "A short test...");
		attr.clear();
		attr.put(cell, text);
		
		adapter.edit(attr, null, null, null);
		assertEquals("A short test...", ApesGraphConstants.getValue(cell.getAttributes()));
	}
	
	public void testDuplicateNames()
	{
		assertEquals(0, adapter.getRootCount());
		
		/*
		 * insert two activity cells
		 */
		vertex1 = adapter.associateGraphCell(new Activity("a1"));
		vertex2 = adapter.associateGraphCell(new Activity("a2"));
		Map attr = ApesGraphConstants.createMap();
		attr.put(vertex1, vertex1.getAttributes());	
		attr.put(vertex2, vertex2.getAttributes());	
		
		adapter.insert( new Object[]{vertex1, vertex2}, attr, null, null, null );
		assertEquals(2, SpemGraphAdapter.getRoots(adapter).length);
		assertTrue(adapter.contains(vertex1));
		assertTrue(adapter.contains(vertex2));
		
		/*
		 * Try to change the name of a2 to a1
		 */
		Map name = ApesGraphConstants.createMap();
		ApesGraphConstants.setValue(name, "a1");
		attr.clear();
		attr.put(vertex2, name);
		assertEquals("a2", vertex2.toString());
	}
}
