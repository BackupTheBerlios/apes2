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


package org.ipsquad.apes.adapters;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Vector;

import javax.swing.undo.UndoableEdit;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.extension.ActivityDiagram.Decision;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.InitialPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.Synchro;
import org.ipsquad.apes.model.extension.ActivityDiagram.Transition;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.frontend.ChangeEvent;
import org.ipsquad.apes.model.frontend.Event;
import org.ipsquad.apes.model.frontend.InsertEvent;
import org.ipsquad.apes.model.frontend.RemoveEvent;
import org.ipsquad.apes.model.spem.ModelVisitor;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.basic.Guidance;
import org.ipsquad.apes.model.spem.basic.GuidanceKind;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.components.SProcess;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.Edge;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;

/**
 * This adapter allows to display a spem diagram in a JGraph
 *
 * @version $Revision: 1.18 $
 */
public abstract class SpemGraphAdapter extends DefaultGraphModel implements ApesMediator.Listener
{
	protected SpemDiagram mDiagram;
	protected Builder mBuilder;
	
	/**
	 * Creates a graph adapter and initializes it with the specified diagram
	 * @param diagram a diagram that constitutes the diagram's data
	 */
	public SpemGraphAdapter(SpemDiagram diagram )
	{
		super();
		mDiagram = diagram;
	}
	
	/**
	 * Return the name of the diagram
	 * @return the diagram name
	 */
	public String getName()
	{
		return mDiagram.getName();
	}
	
	/**
	 * Change the name of the diagram
	 * @param name the new diagram name
	 */
	public void setName(String name)
	{
		mDiagram.setName(name);
	}
	
	/**
	 * Return the diagram id
	 * @return the diagram id
	 */
	public int getID()
	{
		return mDiagram.getID();
	}
	
	/**
	 * Return the diagram of this adapter
	 * @return the diagram store in this adapter
	 */
	public SpemDiagram getDiagram()
	{
		return mDiagram;
	}
	
	/**
	 * Call when a diagram was remove from the project
	 */
	public void destroy()
	{
		Vector sources = new Vector();
		Vector targets = new Vector();
		
		recursivelyFindEdges(getRoots(this), sources, targets );
		
		if( ! sources.isEmpty() )
		{
			//super.remove( to_remove.toArray());
			ApesMediator.getInstance().removeDiagramLinksAfterDeleted( mDiagram, sources.toArray(), targets.toArray() );
		}
	}
	
	/**
	 * Retrieve an element of the graph by its id
	 * @param id the id of the element
	 * @return the corresponding element 
	 */
	public Object findWithID( int id )
	{
		return ApesMediator.getInstance().findByID(id);
	}
	
	/**
	 * Return the ApesGraphCell corresponding to the object
	 * @param o the object to encapsulate
	 * @return the corrsponding ApesGraphCell
	 */
	public ApesGraphCell associateGraphCell(Object o)
	{
		Object cell = mBuilder.create( o );
		
		if( cell instanceof ApesGraphCell )
		{
			return (ApesGraphCell) cell;
		}
		
		return null;
	}
	
	/**
	 * Find all edges linked to the cells
	 * @param cells
	 * @param sources the sources of the found edges
	 * @param targets the targets of the found edges
	 */
	public void recursivelyFindEdges(Object[] cells, Vector sources, Vector targets )
	{
		recursivelyFindEdges( cells, sources, targets, new Vector() );
	}
	
	private void recursivelyFindEdges(Object[] cells, Vector sources, Vector targets, Vector checkDuplicate )
	{
		for(int i=0; i<cells.length; i++)
		{
			if(cells[i] instanceof Edge)
			{
				Edge e = (Edge) cells[i];
				Object source = ((ApesGraphCell)getParent( e.getSource() )).getUserObject();
				Object target = ((ApesGraphCell)getParent( e.getTarget() )).getUserObject();
				
				if( !checkDuplicate.contains(source.toString()+target.toString()) )
				{
					sources.add( source );
					targets.add( target );
					checkDuplicate.add(source.toString()+target.toString());
				}
			}
			else if(cells[i] instanceof ApesGraphCell)
			{
				ApesGraphCell vertex = (ApesGraphCell)cells[i];
				List children = vertex.getChildren();
				recursivelyFindEdges(children.toArray(), sources, targets, checkDuplicate );
			}
			else if(cells[i] instanceof Port)
			{
				Iterator it = ((Port)cells[i]).edges();
				while(it.hasNext())
				{
					Edge e = (Edge)it.next();
					if( e != null )
					{
						Object source = ((ApesGraphCell)getParent( e.getSource() )).getUserObject();
						Object target = ((ApesGraphCell)getParent( e.getTarget() )).getUserObject();
						if( !checkDuplicate.contains(source.toString()+target.toString()) )
						{
							sources.add( source );
							targets.add( target );
							checkDuplicate.add(source.toString()+target.toString());
						}
					}
				}
			}
			
		}
	}

	public void remove(Object[] roots)
	{
		//System.out.println("SpemGraphAdapter::remove");
		
		Vector cells_to_remove = new Vector();
		Vector sources_to_remove = new Vector();
		Vector targets_to_remove = new Vector();
		Map attr = new HashMap();
		Vector edges_to_remove = new Vector();
		
		Vector filtered_roots = new Vector();
		addRelated( roots, filtered_roots );
		
		for(int  i = 0; i < filtered_roots.size(); i++ )
		{		
			if( filtered_roots.get(i) instanceof ApesGraphCell )
			{
				cells_to_remove.add( ((ApesGraphCell)filtered_roots.get(i)).getUserObject());
			}
			else if ( filtered_roots.get(i) instanceof TransitionEdge )
			{
				TransitionEdge te = (TransitionEdge) filtered_roots.get(i);
				ActivityDiagram.Transition t = (ActivityDiagram.Transition)te.getUserObject(); 
				sources_to_remove.add( t.getInputModelElement() );
				targets_to_remove.add( t.getOutputModelElement() );
				edges_to_remove.add( te );
			}
			else if( filtered_roots.get(i) instanceof DefaultEdge )
			{
				DefaultGraphCell source = (DefaultGraphCell) getParent(((DefaultEdge)filtered_roots.get(i)).getSource());
				DefaultGraphCell target = (DefaultGraphCell) getParent(((DefaultEdge)filtered_roots.get(i)).getTarget());
				sources_to_remove.add( source.getUserObject() );
				targets_to_remove.add( target.getUserObject() );
				edges_to_remove.add( filtered_roots.get(i) );
			}
		}

		if( ! edges_to_remove.isEmpty() )
		{
			attr.put("Links", edges_to_remove.toArray() );
		}
		
		remove( cells_to_remove.toArray(), sources_to_remove.toArray(), targets_to_remove.toArray(), attr );
	}

	public void edit(
		Map attributes,
		ConnectionSet cs,
		ParentMap pm,
		UndoableEdit[] edits) 
	{
		//System.out.println("SpemGraphAdater::edit attr "+attributes+" cs "+cs+" pm "+pm+" "+edits);
		if(attributes != null)
		{
			Iterator it = attributes.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry entry = (Map.Entry) it.next();
				DefaultGraphCell cell = (DefaultGraphCell) entry.getKey();
				
				if(cell instanceof ApesGraphCell || cell instanceof TransitionEdge )
				{	
					Map map = (Map) entry.getValue();
				
					if( GraphConstants.getValue(cell.getAttributes()) != null && map.size() == 1 && GraphConstants.getValue(map) instanceof String 
							&& ! GraphConstants.getValue(map).equals(GraphConstants.getValue(cell.getAttributes()).toString()))
					{
						Map attr = new HashMap();
						attr.put(cell.getUserObject(), cell);
						change( cell.getUserObject(), (String)GraphConstants.getValue(map), attr);
						return;
					}
				}
			}
		}
		super.edit(attributes, cs, pm, edits);
	}

	/**
	 * Retrieve all elements linked directly or indirectly to the cells
	 * @param cells
	 * @param related
	 */
	public void addRelated(Object[] cells, Vector related)
	{
		for(int i=0; i<cells.length; i++)
		{
			if(!related.contains(cells[i]) && cells[i]!=null)
			{
				related.add(cells[i]);
			}

			if(cells[i] instanceof ApesGraphCell)
			{
				ApesGraphCell vertex = (ApesGraphCell)cells[i];    
				List children = vertex.getChildren();
				addRelated(children.toArray(), related);
			}
			else if(cells[i] instanceof Port)
			{
				Iterator it = ((Port)cells[i]).edges();     
				while(it.hasNext())
				{
					Object o = it.next();                    
					
					if(!related.contains(o) && o!=null)
					{
						related.add(o);
					}
				}
			}
		}												
	}

	/**
	 * Insert a list of elements in this diagram
	 * 
	 * @param toInserts the elements to add
	 * @param cloneUserObject true if the userObject of the cells must be clone, false otherwise
	 */
	public void insertCells( Vector toInserts, boolean cloneUserObject )
	{
		ApesGraphCell cell;
		DefaultEdge edge;
		Vector commands = new Vector();
		
		for( int i = 0; i < toInserts.size(); i++ )
		{
			if (toInserts.get(i) instanceof ApesGraphCell)
			{
				addInsertCellToCommands( (ApesGraphCell)toInserts.get(i), cloneUserObject, commands );
			}
			if (toInserts.get(i) instanceof DefaultEdge)
			{				
				addInsertEdgeToCommands( (DefaultEdge)toInserts.get(i), commands ) ;
			}
		}
		
		if(commands.size() > 0)
		{	
			ApesMediator.getInstance().execute(commands);
		}
	}
	
	/**
	 * Add the insert cell command to the list
	 * @param cell the cell to add
	 * @param commands the list of commands
	 */
	protected void addInsertCellToCommands( ApesGraphCell cell, boolean cloneUserObject, Vector commands )
	{
		String name = cell.toString();
		
		if(cloneUserObject)
		{	
			cell.setUserObject(((Element)cell.getUserObject()).clone());
		}
		
		Map attr = cell.getAttributes();
		Map view = new HashMap();
		view.put("Attributes", attr);
		
		commands.add(
				ApesMediator.getInstance().createInsertCommandToSpemDiagram(mDiagram, cell.getUserObject(), view ));
		
		// try to give the same name to the new object
		if( cloneUserObject && !cell.toString().equals(name) )
		{	
			commands.add(
					ApesMediator.getInstance().createChangeCommand(cell.getUserObject(),name,null));
		}
	}
	
	/**
	 * Add the insert edge command to the list
	 * @param edge the edge to add
	 * @param commands the list of commands
	 */
	protected void addInsertEdgeToCommands( DefaultEdge edge, Vector commands )
	{
		Map attr = edge.getAttributes() ;
		Map view = new HashMap();
		view.put("Attributes", attr);

		DefaultPort sourcePort = (DefaultPort) edge.getSource();
		DefaultPort targetPort = (DefaultPort) edge.getTarget();
		
		ApesGraphCell target = (ApesGraphCell) targetPort.getParent();
		ApesGraphCell source = (ApesGraphCell) sourcePort.getParent();
		
		if( target != null && source != null )
		{	
			commands.add(
				ApesMediator.getInstance().createInsertCommandToSpemDiagram(mDiagram,source.getUserObject(), target.getUserObject(), view) );
		}
	}
	
	/**
	 * Insert a cell in the diagram. If the cell is an ApesGraphCell, calls the ApesMediator
	 * @param vertex the cell to insert
	 * @param attr Attributes to send to the ApesMediator. This attribute is not modified.
	 */
	public void insertCell( DefaultGraphCell vertex, Map attr )
	{
		//System.out.println("Graph::tryInsertCell "+vertex.getUserObject());
		if( vertex instanceof ApesGraphCell )
		{	
			ApesMediator.getInstance().update( 
				ApesMediator.getInstance().createInsertCommandToSpemDiagram( mDiagram, vertex.getUserObject(), attr ) );
		}
		else if( vertex instanceof NoteCell )
		{
			NoteCell cell = new NoteCell();
			// Construct a Map from cells to Maps (for insert)
			Hashtable attributes = new Hashtable();
			// Associate the Vertex with its Attributes
			attributes.put(cell, vertex.getAttributes());
			// Insert the Vertex and its Attributes
			super.insert(new Object[]{cell}, attributes, null, null, null);
			
			
		}
	}
	
	/**
	 * Insert an edge to the diagram
	 * @param source the source of the edge
	 * @param target the target of the edge
	 * @param attr Attributes to send to the ApesMediator. This attribute is not modified.
	 */
	public void insertEdge( DefaultGraphCell source, DefaultGraphCell target, Map attr )
	{
		//System.out.println("Graph::tryInsertEdge");
		if( source instanceof ApesGraphCell && target instanceof ApesGraphCell )
		{	
			ApesMediator.getInstance().update( 
				ApesMediator.getInstance().createInsertCommandToSpemDiagram( mDiagram, source.getUserObject(), target.getUserObject(), attr ) );
		}
	}
	
	/**
	 * Move the end, indicating by isSource, of an edge to the new cell 
	 * @param edge the edge to move
	 * @param newCell the new cell to connecting
	 * @param isSource true if the concerned end is the source of the edge, false otherwise 
	 */
	public void moveEdge( DefaultEdge edge, ApesGraphCell newCell, boolean isSource )
	{
		//System.out.println("Graph::moveEdge "+edge+" "+newCell+" "+isSource);
		Vector commands = new Vector();
		Map attr = new HashMap();
		Object source = ((ApesGraphCell)((DefaultPort)edge.getSource()).getParent()).getUserObject();
		Object target = ((ApesGraphCell)((DefaultPort)edge.getTarget()).getParent()).getUserObject();
		
		attr.put("Links", new Object[]{edge} );
		commands.add( ApesMediator.getInstance().createRemoveCommand( mDiagram, null, new Object[]{ source }, new Object[]{ target }, attr ) );
		
		if( isSource )
		{
			source = newCell.getUserObject();
		}
		else
		{
			target = newCell.getUserObject();
		}
		attr = new HashMap();
		attr.put("Attributes",edge.getAttributes());
		commands.add( ApesMediator.getInstance().createInsertCommandToSpemDiagram( mDiagram, source, target, attr));
		
		ApesMediator.getInstance().execute(commands);
	}
	
	/**
	 * Remove elements from the model
	 * @param cells the cells to remove
	 * @param sources the sources of the edges to remove
	 * @param targets the targets of the edges to remove
	 * @param attr Attributes to send to the ApesMediator. This attribute is not modified.
	 */
	public void remove( Object[] cells, Object[] sources, Object[] targets, Map attr )
	{
		//System.out.println("Graph::tryRemove");
		ApesMediator.getInstance().update( 
				ApesMediator.getInstance().createRemoveCommand( mDiagram, cells, sources, targets, attr ) );
	}
	
	/**
	 * Change the name of an element
	 * @param element the concerned element 
	 * @param newName the new name
	 * @param attr Attributes to send to the ApesMediator. This attribute is not modified.
	 */
	public void change( Object element, String newName, Map attr )
	{
		ApesMediator.getInstance().update( 
				ApesMediator.getInstance().createChangeCommand( element, newName, attr ) );
	}
	
	public void updated( Event e )
	{
		if( e instanceof InsertEvent )
		{
			inserted( (InsertEvent)e );
		}
		else if( e instanceof RemoveEvent )
		{
			removed( (RemoveEvent)e );
		}
		else if( e instanceof ChangeEvent )
		{
			changed( (ChangeEvent)e );
		}
	}
	
	/**
	 * Insert the element define by this event
	 * @param e the received event
	 */
	protected void inserted( InsertEvent e ) 
	{ 
		if( e.getDiagram() != mDiagram || (e.getInserted() != null && ! mBuilder.shouldGoInGraph(e.getInserted())) )
		{
			return;
		}
		
		//System.out.println("Graph : insert "+e);
		DefaultEdge edge = null;
		
		if( e.getInserted() != null )
		{
			Object objectToAdd = mBuilder.create( e.getInserted() );
			
			if( objectToAdd != null )
			{
				if( objectToAdd instanceof ApesGraphCell )
				{	
					Map apply = null;
					
					if( e.getAttributes() != null && e.getAttributes().containsKey("Attributes") )
					{
						Map attr = (Map) e.getAttributes().get("Attributes");
						apply =  new HashMap();
						apply.put( objectToAdd, attr );
					}
					super.insert( new Object[]{ objectToAdd }, apply, null, null, null );
				}
				else
				{
					edge = (DefaultEdge)objectToAdd;
				}
			}
		}
		else
		{
			edge = new DefaultEdge();
		}
		
		if( edge != null )
		{
			Port firstPort = null, port = null;
			Map apply = new HashMap();

			if( e.getAttributes() != null && e.getAttributes().containsKey("Attributes") )
			{
				Map attr = (Map) e.getAttributes().get("Attributes");
				apply = new HashMap();
				apply.put( edge, attr );
			}
			
			if( e.getAttributes() != null && e.getAttributes().containsKey("firstPort") 
					&& e.getAttributes().containsKey("endPort") )
			{	
				firstPort = (Port) e.getAttributes().get("firstPort");
				port = (Port)e.getAttributes().get("endPort");
			}
			else
			{
				Vector cells = findCellsByUserObject( new Object[]{ e.getSource() } );
				firstPort = (Port) ((ApesGraphCell)cells.get(0)).getChildAt(0);
				cells = findCellsByUserObject( new Object[]{ e.getTarget() } );
				port = (Port) ((ApesGraphCell)cells.get(0)).getChildAt(0);
			}

			ConnectionSet cs = new ConnectionSet();
			edge.setSource( firstPort);
			edge.setTarget( port );
			firstPort.addEdge( edge );
			port.addEdge( edge );
			cs.connect(edge, firstPort, port );
			
			
			super.insert(new Object[]{ edge },apply,cs, null,null);
		}
	}
	
	/**
	 * Remove the elements concerned by this event
	 * @param e the received event
	 */
	protected void removed( RemoveEvent e )
	{
		//System.out.println("Graph::remove "+mDiagram+" "+e);
		
		if( e.getDiagram() == mDiagram || e.getDiagram() == null )
		{
			Vector to_remove = findCellsByUserObject( e.getElements() );
			
			if( e.getAttributes() != null )
			{
				Object[] edges = (Object[])e.getAttributes().get("Links");
				if( edges != null )
				{
					for( int j = 0; j < edges.length; j++ )
					{
						to_remove.add(edges[j]);
					}
				}
			}
			
			if( to_remove.size() > 0 )
			{
				Vector related = new Vector();
				addRelated( to_remove.toArray(), related );
				super.remove( related.toArray() );
			}
		}
	}
	
	/**
	 * Change the attributes of the element concerned by this event
	 * @param e the received event
	 */
	protected void changed( ChangeEvent e )
	{
		//System.out.println("Graph::edit "+e);
		
		if( !mBuilder.shouldGoInGraph(e.getElement()) )
		{
			return;
		}
		
		Map attr = new HashMap();
		DefaultGraphCell cell = null;
		
		if( e.getAttributes() != null 
				&& e.getAttributes().get(e.getElement()) != null 
				&& e.getElement() instanceof DefaultGraphCell )
		{
			cell = (DefaultGraphCell)e.getAttributes().get(e.getElement());
		}
		else
		{		
			Vector cells = findCellsByUserObject( new Object[]{ e.getElement() } );
			
			if( cells.size() == 1 )
			{
				cell = (DefaultGraphCell)cells.get(0);
			}
		}
		
		if( cell != null )
		{
			Map new_val = GraphConstants.createMap();
			GraphConstants.setValue(new_val, cell.toString());
			attr.put( cell, new_val );
			
			super.edit( attr, null, null, null);
		}
	}
	
	protected Vector findCellsByUserObject( Object[] userObjects )
	{
		Vector cells = new Vector();
		Vector v_user_objets = new Vector();
		int i;
		for( i = 0; i < userObjects.length; i++ )
		{
			v_user_objets.add(userObjects[i]);
		}
		
		for( i = 0; i < getRootCount(); i++ )
		{
			Object root = getRootAt(i);
			if( ( root instanceof ApesGraphCell 
					|| root instanceof TransitionEdge )
					&& v_user_objets.contains(((DefaultGraphCell)root).getUserObject()))
			{
				cells.add(root);
			}
		}
		
		return cells;
	}
	
	protected ConnectionSet handleConnectionSet(ConnectionSet cs)
	{
		if(cs==null) return super.handleConnectionSet(cs);

		ConnectionSet newConnections = new ConnectionSet();

		Iterator it = cs.connections();
		Vector alreadyProcessed = new Vector();
		while(it.hasNext())
		{
			ConnectionSet.Connection c = (ConnectionSet.Connection) it.next();

			if(!c.isSource())
			{
				Port sourcePort = (Port) ((Edge)c.getEdge()).getSource();
				Port targetPort = (Port) ((Edge)c.getEdge()).getTarget();

				DefaultGraphCell source = (DefaultGraphCell)getParent(sourcePort);
				DefaultGraphCell target = (DefaultGraphCell)getParent(targetPort);
				
				sourcePort.addEdge(c.getEdge());
				targetPort.addEdge(c.getEdge());
			}
		}

		return super.handleConnectionSet(newConnections);
	}
	
	protected Map handleAttributes(Map propertyMap)
	{
		if(propertyMap != null)
		{
			Hashtable undo = new Hashtable();
			Iterator it = propertyMap.entrySet().iterator();
			while (it.hasNext())
			{
				Map.Entry entry = (Map.Entry) it.next();
				
				if(entry.getKey() instanceof DefaultGraphCell)
				{
					DefaultGraphCell cell = (DefaultGraphCell) entry.getKey();
					Map clonedMap = GraphConstants.cloneMap(getAttributes(cell));
					if(GraphConstants.getValue(clonedMap)==null)
					{
						GraphConstants.setValue(clonedMap, "");
					}
					else
					{
						if( cell instanceof ApesGraphCell )
						{
							GraphConstants.setValue(clonedMap, cell.getUserObject());
						}
						else
						{
							GraphConstants.setValue(clonedMap, GraphConstants.getValue(clonedMap).toString());
						}
					}
					undo.put(cell, clonedMap);
	
					Map map = (Map) entry.getValue();
					cell.changeAttributes(map);
				}
			
			}
			return undo;
		}
		return null;
	}
	
	/**
	 * The bulider create the cell corresponding to an element of the model.
	 * He implements the ModelVisitor.
	 */
	public abstract class Builder implements ModelVisitor, Serializable 
	{
		protected Object mCreated = null;
		
		/**
		 * Create the cell corresponding to the given object
		 * @param o an object of the model to encapsulate in a cell
		 * @return the coresponding cell
		 */
		public abstract Object create( Object o );
		
		public abstract boolean shouldGoInGraph(Object o);
		
		public void visitApesProcess(ApesProcess p){ mCreated = null; }
		public void visitWorkProductRef(WorkProductRef ref){ mCreated = null; }
		public void visitPackage(IPackage pack) { mCreated = null; }
		public void visitProcessComponent(ProcessComponent component) { mCreated = null; }
		public void visitComponentInterface(ApesProcess.Interface i) { mCreated = null; }
		public void visitProvidedInterface(ApesProcess.ProvidedInterface pi){ mCreated = null; };
		public void visitRequiredInterface(ApesProcess.RequiredInterface ri){ mCreated = null; };
		public void visitProcess(SProcess process) { mCreated = null; }
		public void visitWorkDefinition(WorkDefinition work) { mCreated = null; }
		public void visitProcessPerformer(ProcessPerformer performer) { mCreated = null; }
		public void visitExternalDescription(ExternalDescription description) { mCreated = null; }
		public void visitGuidance(Guidance guidance) { mCreated = null; }
		public void visitGuidanceKind(GuidanceKind kind) { mCreated = null; }
		public void visitFlowDiagram(FlowDiagram diagram) { mCreated = null; }
		public void visitResponsabilityDiagram(ResponsabilityDiagram diagram) { mCreated = null; }
		public void visitContextDiagram(ContextDiagram diagram) { mCreated = null; }
		public void visitActivityDiagram(ActivityDiagram diagram) { mCreated = null; }	
		public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram) { mCreated = null; }
		public void visitStateMachine(StateMachine sm) { mCreated = null; }
		
		public void visitDecision( Decision decision ) { mCreated = null; }
		public void visitFinalPoint(FinalPoint finalPoint ) { mCreated = null; }
		public void visitInitialPoint(InitialPoint initialPoint) { mCreated = null; }
		public void visitSynchro(Synchro synchro) { mCreated = null; }
		public void visitTransition(Transition transition) { mCreated = null; }
	}
}


