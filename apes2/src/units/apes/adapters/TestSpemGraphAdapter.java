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

import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphModel;
import org.jgraph.graph.GraphUndoManager;
import org.jgraph.graph.Port;

public class TestSpemGraphAdapter extends TestCase
{
	static SpemTreeAdapter model;
	static SPackage parentParent, parent;
	static FlowDiagram diagram;
	static SpemGraphAdapter adapter;
	static ApesGraphCell vertex1, vertex2, vertex3;
	static DefaultEdge edge1, edge2;
	static Port source1, source2, target1, target2;
	static ApesMediator mediator;
	
	//public TestSpemGraphAdapter()
	static {
		Context context = Context.getInstance();
		
		model = new SpemTreeAdapter();
		diagram = new FlowDiagram();
		parentParent = new SPackage();
		parent = new SPackage();
		
		parentParent.addModelElement( parent );
		parent.addModelElement( diagram );
		
		adapter = new SpemGraphAdapter(diagram)
		{
			{
				mBuilder = new Builder(){
					public Object create( Object o )
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
		
		/*adapter.setBuilder( adapter.new Builder(){
			public Object create( Object o )
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
		} );*/
		
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
		
		context.setProject(new Project());		
		
		mediator = ApesMediator.getInstance();
		mediator.addApesMediatorListener( adapter );
		ApesMediator.getInstance().registerDiagram( diagram );
		
		context.getProject().getProcess().getComponent().addModelElement( diagram );
		
		//model.addModelElementToPackage(diagram, (SPackage)model.getRoot());
	}
	
	
	public void testInsert()
	{
		mediator.addApesMediatorListener( adapter );
		
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==0);
		
		vertex1 = adapter.associateGraphCell(new Activity());
		Map attr = vertex1.getAttributes();
		Hashtable attributes = new Hashtable();
		attributes.put("Attributes", attr);
		
		adapter.insertCell( vertex1, attributes );
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==1);
		
		vertex2 = adapter.associateGraphCell(new ProcessRole());
		attr = vertex2.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);
		
		adapter.insertCell( vertex2, attributes );
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==2);

		vertex3 = adapter.associateGraphCell(new WorkProduct());
		attr = vertex3.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);
		
		adapter.insertCell( vertex3, attributes );
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==3);

		ConnectionSet cs = new ConnectionSet();
		edge1 = new DefaultEdge();
		source1 = (Port)vertex2.getFirstChild();
		target1 = (Port)vertex1.getFirstChild();
		edge1.setSource(source1);
		edge1.setTarget(target1);
		cs.connect(edge1, source1, target1);

		Map view = new HashMap();
		view.put( "firstPort", source1);
		view.put( "endPort", target1);

		attr = edge1.getAttributes();
		
		view.put("Attributes", attr);
		
		adapter.insertEdge( vertex2, vertex1, view);
		
		Object lastInsert =SpemGraphAdapter.getRoots(adapter)[SpemGraphAdapter.getRoots(adapter).length-1]; 
		assertTrue( lastInsert instanceof DefaultEdge );
		assertTrue( ((DefaultEdge) lastInsert).getSource() == source1 );
		assertTrue( ((DefaultEdge) lastInsert).getTarget() == target1 );
		Iterator it_s1 = source1.edges();
		assertTrue( it_s1.hasNext() );
		it_s1.next();
		assertFalse( it_s1.hasNext() );
		Iterator it_t1 = target1.edges();
		assertTrue( it_t1.hasNext() );
		it_t1.next();
		assertFalse( it_t1.hasNext() );
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex2.getUserObject(), (ModelElement) vertex1.getUserObject()));
		edge1 = (DefaultEdge) lastInsert;
		
		cs = new ConnectionSet();
		edge2 = new DefaultEdge();
		source2 = (Port)vertex1.getFirstChild();
		target2 = (Port)vertex3.getFirstChild();
		edge2.setSource(source2);
		edge2.setTarget(target2);
		cs.connect(edge2, source2, target2);

		view = new HashMap();
		view.put( "firstPort", source2);
		view.put( "endPort", target2);

		attr = edge2.getAttributes();
		
		view.put("Attributes", attr);
		
		adapter.insertEdge( vertex1, vertex3, view );
		lastInsert =SpemGraphAdapter.getRoots(adapter)[SpemGraphAdapter.getRoots(adapter).length-1]; 
		assertTrue( lastInsert instanceof DefaultEdge );
		assertTrue( ((DefaultEdge) lastInsert).getSource() == source2 );
		assertTrue( ((DefaultEdge) lastInsert).getTarget() == target2 );
		Iterator it_s2 = source2.edges();
		assertTrue( it_s2.hasNext() );
		it_s2.next();
		assertTrue( it_s2.hasNext() );
		it_s2.next();
		assertFalse( it_s2.hasNext() );
		Iterator it_t2 = target2.edges();
		assertTrue( it_t2.hasNext() );
		it_t2.next();
		assertFalse( it_t2.hasNext() );
		assertTrue( diagram.existsLinkModelElements( (ModelElement) vertex1.getUserObject(), (ModelElement) vertex3.getUserObject()));
		edge2 = (DefaultEdge) lastInsert;
	}

	public void testRecursivelyFindEdges()
	{
		Vector sources = new Vector();
		Vector targets = new Vector();
		
		adapter.recursivelyFindEdges(new Object[]{vertex1}, sources, targets );
		assertTrue( sources.size() == 2 );
		assertTrue( targets.size() == 2 );
		
		sources.clear();
		targets.clear();
		adapter.recursivelyFindEdges(new Object[]{vertex2}, sources, targets );
		assertTrue(sources.size()==1);
		assertTrue(targets.size()==1);
		
		sources.clear();
		targets.clear();
		adapter.recursivelyFindEdges(new Object[]{vertex3}, sources, targets);
		assertTrue(sources.size()==1);
		assertTrue(targets.size()==1);
		
		sources.clear();
		targets.clear();
		adapter.recursivelyFindEdges(new Object[]{vertex2, vertex3}, sources, targets);
		assertTrue(sources.size()==2);
		assertTrue(targets.size()==2);
		
		sources.clear();
		targets.clear();
		adapter.recursivelyFindEdges(new Object[]{vertex1, vertex2, vertex3}, sources, targets);
		assertTrue(sources.size()==2);
		assertTrue(targets.size()==2);
		
		sources.clear();
		targets.clear();
		adapter.recursivelyFindEdges(new Object[]{vertex3, edge1}, sources, targets);
		assertTrue(sources.size()==2);
		assertTrue(targets.size()==2);
	}
	
	public void testAddRelated()
	{
		Vector related = new Vector();
		
		related.clear();
		adapter.addRelated(new Object[]{vertex1}, related);
		assertTrue(related.contains(vertex1));
		assertTrue(related.contains(target1));
		assertTrue(related.contains(edge1));
		assertTrue(related.contains(source2));
		assertTrue(related.contains(edge2));
		assertTrue(target1==source2);
		assertTrue(related.size()==4);

		related.clear();
		adapter.addRelated(new Object[]{vertex2}, related);
		assertTrue(related.contains(vertex2));
		assertTrue(related.contains(source1));
		assertTrue(related.contains(edge1));
		assertTrue(related.size()==3);

		related.clear();
		adapter.addRelated(new Object[]{vertex3}, related);
		assertTrue(related.contains(vertex3));
		assertTrue(related.contains(target2));
		assertTrue(related.contains(edge2));
		assertTrue(related.size()==3);
		
		related.clear();
		adapter.addRelated(new Object[]{vertex2, vertex3}, related);
		assertTrue(related.contains(vertex3));
		assertTrue(related.contains(target2));
		assertTrue(related.contains(edge2));
		assertTrue(related.contains(vertex2));
		assertTrue(related.contains(source1));
		assertTrue(related.contains(edge1));
		assertTrue(related.size()==6);

		related.clear();
		adapter.addRelated(new Object[]{vertex1, vertex2, vertex3}, related);
		assertTrue(related.contains(vertex1));
		assertTrue(related.contains(target1));
		assertTrue(related.contains(edge1));
		assertTrue(related.contains(source2));
		assertTrue(related.contains(edge2));
		assertTrue(target1==source2);
		assertTrue(related.contains(vertex2));
		assertTrue(related.contains(source1));
		assertTrue(related.contains(vertex3));
		assertTrue(related.contains(target2));
		assertTrue(related.size()==8);

		related.clear();
		adapter.addRelated(new Object[]{vertex3, edge1}, related);
		assertTrue(related.contains(vertex3));
		assertTrue(related.contains(target2));
		assertTrue(related.contains(edge2));
		assertTrue(related.contains(edge1));
		assertTrue(related.size()==4);
	}

	public void testRemove()
	{
		adapter.remove(new Object[]{edge1});
		assertFalse(adapter.contains(edge1));

		adapter.remove(new Object[]{vertex2});
		assertFalse(adapter.contains(vertex2));

		adapter.remove(new Object[]{vertex3});
		assertFalse(adapter.contains(vertex3));
		assertFalse(adapter.contains(edge2));
		
		adapter.remove(new Object[]{vertex1});
		assertFalse(adapter.contains(vertex1));
		assertFalse(adapter.contains(edge2));
	}
	
	public void testDestroy()
	{
		testInsert();
		
		Vector sources = new Vector();
		Vector targets = new Vector();
		
		adapter.recursivelyFindEdges(SpemGraphAdapter.getRoots(adapter), sources, targets );
		assertTrue(sources.size()!=0);
		assertTrue(targets.size()!=0);
		
		adapter.destroy();
		
		sources.clear();
		targets.clear();
		
		adapter.recursivelyFindEdges(SpemGraphAdapter.getRoots(adapter), sources, targets );
		
		for( int i = 0; i < sources.size(); i++ )
		{
			assertFalse( diagram.existsLinkModelElements( (ModelElement) sources.get(i), (ModelElement)targets.get(i)));
		}
	}

	public void testUndoRedo()
	{
		adapter.remove(SpemGraphAdapter.getRoots(adapter));
		GraphUndoManager undoManager = Context.getInstance().getUndoManager();
		adapter.addUndoableEditListener(undoManager);
		
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==0);
		
		vertex1 = adapter.associateGraphCell(new Activity());
		Map attr = vertex1.getAttributes();
		Hashtable attributes = new Hashtable();
		attributes.put("Attributes", attr);
		
		adapter.insertCell( vertex1, attributes );
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==1);
		
		vertex2 = adapter.associateGraphCell(new ProcessRole());
		attr = vertex2.getAttributes();
		attributes = new Hashtable();
		attributes.put("Attributes", attr);
		
		adapter.insertCell( vertex2, attributes );
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==2);
		
		ConnectionSet cs = new ConnectionSet();
		edge1 = new DefaultEdge();
		source1 = (Port)vertex2.getFirstChild();
		target1 = (Port)vertex1.getFirstChild();
		edge1.setSource(source1);
		edge1.setTarget(target1);
		cs.connect(edge1, source1, target1);

		Map view = new HashMap();
		view.put( "firstPort", source1);
		view.put( "endPort", target1);

		attr = edge1.getAttributes();
		
		view.put("Attributes", attr);
		
		adapter.insertEdge( vertex2, vertex1, view);
		
		Object lastInsert =SpemGraphAdapter.getRoots(adapter)[SpemGraphAdapter.getRoots(adapter).length-1]; 
		assertTrue( lastInsert instanceof DefaultEdge );
		assertTrue( ((DefaultEdge) lastInsert).getSource() == source1 );
		assertTrue( ((DefaultEdge) lastInsert).getTarget() == target1 );
		Iterator it_s1 = source1.edges();
		assertTrue( it_s1.hasNext() );
		it_s1.next();
		assertFalse( it_s1.hasNext() );
		Iterator it_t1 = target1.edges();
		assertTrue( it_t1.hasNext() );
		it_t1.next();
		assertFalse( it_t1.hasNext() );
		
		edge1 = (DefaultEdge) lastInsert;
		
		
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==3);
		adapter.remove(new Object[]{vertex2,edge1});
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==1);
		adapter.remove(new Object[]{vertex1});
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==0);
		
		
		undoManager.undo(null);
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==1);
		undoManager.undo(null);
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==3);
		assertTrue(edge1.getSource()==source1);
		assertTrue(source1.edges().next()==edge1);
		assertTrue(edge1.getTarget()==target1);
		assertTrue(target1.edges().next()==edge1);
		undoManager.redo(null);
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==1);
		undoManager.redo(null);
		assertTrue(SpemGraphAdapter.getRoots(adapter).length==0);
		
	}
}
