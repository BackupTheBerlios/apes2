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


package org.ipsquad.apes.adapters;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.undo.UndoableEdit;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.Identity;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.extension.ActivityDiagram.Decision;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.InitialPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.Synchro;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.frontend.event.ApesModelEvent;
import org.ipsquad.apes.model.frontend.event.ApesModelListener;
import org.ipsquad.apes.model.spem.ModelVisitor;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.basic.Guidance;
import org.ipsquad.apes.model.spem.basic.GuidanceKind;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.components.SProcess;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.utils.Debug;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultGraphModel;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.ParentMap;
import org.jgraph.graph.Port;

/**
 * This adapter allows to display a spem diagram in a JGraph
 *
 * @version $Revision: 1.34 $
 */
public abstract class SpemGraphAdapter extends DefaultGraphModel implements ApesModelListener
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
		//Vector sources = new Vector();
		//Vector targets = new Vector();
		
		//recursivelyFindEdges(getRoots(this), sources, targets );
		
		/*if( ! sources.isEmpty() )
		{
			//super.remove( to_remove.toArray());
			ApesMediator.getInstance().removeDiagramLinksAfterDeleted( mDiagram, sources.toArray(), targets.toArray() );
		}*/
	}
	
	/**
	 * Find an element by its id
	 * 
	 * @param id the id of the element to find
	 * @return the corresponding cell or null
	 */
	public DefaultGraphCell findWithID(int id)
	{
		for(int i = 0; i < roots.size(); i++)
		{
		    Object current = roots.get(i);
		    if( current instanceof Identity && ((Identity)current).getID() == id )
			{
		    	return (DefaultGraphCell)current;
			}
		}
	    return null;
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

	public Collection getLinksFrom(Object userObject)
	{
		Set result = new HashSet();
		Set set = getEdges(this, new Object[]{getCellByUserObject(userObject, null, false)});
		for ( Iterator iter = set.iterator(); iter.hasNext(); )
		{
			DefaultEdge edge = (DefaultEdge) iter.next();
			Object source = ((DefaultGraphCell)getParent(edge.getSource())).getUserObject();
			if(source == userObject)
			{
				Object target = ((DefaultGraphCell)getParent(edge.getTarget())).getUserObject();
				result.add(new Link(source, target));
			}
		}
		return result;
	}
	
	public Collection getLinksTo(Object userObject)
	{
		Set result = new HashSet();
		Set set = getEdges(this, new Object[]{getCellByUserObject(userObject, null, false)});
		for ( Iterator iter = set.iterator(); iter.hasNext(); )
		{
			DefaultEdge edge = (DefaultEdge) iter.next();
			Object target = ((DefaultGraphCell)getParent(edge.getTarget())).getUserObject();
			if(target == userObject)
			{
				Object source = ((DefaultGraphCell)getParent(edge.getSource())).getUserObject();
				result.add(new Link(source, target));
			}
		}
		return result;
	}

	public Collection getLinks(Object userObject)
	{
		DefaultGraphCell cell = getCellByUserObject(userObject, null, false);
		if(cell != null && cell instanceof ApesGraphCell)
		{
			Set result = new HashSet();
			for ( Iterator iter = ((Port)cell.getChildAt(0)).edges(); iter.hasNext(); )
			{
				DefaultEdge edge = (DefaultEdge) iter.next();
				Object source = ((DefaultGraphCell)getParent(edge.getSource())).getUserObject();
				Object target = ((DefaultGraphCell)getParent(edge.getTarget())).getUserObject();
				result.add(new Link(source, target));
			}
			return result;
		}
		return null;
	}

	public void insert( Object[] cells, Map attributes, ConnectionSet cs,
            ParentMap parentMap, UndoableEdit[] edits )
    {
	    if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemGraphAdapter/"+getName()+") -> insert");
	    
	    if(cells == null) return;
	    
	    Vector notes = new Vector();
	    Vector noteEdges = new Vector();
	    Vector elements = new Vector();	    
	    Map matching = ApesGraphConstants.createMap();
	    
	    for ( int i = 0; i < cells.length; i++ )
        {
	    	if(cells[i] instanceof NoteCell || cells[i] instanceof ProcessComponentCell)
	    	{
	    		notes.add(cells[i]);
	    	}
	    	else if(cells[i] instanceof ApesGraphCell)
	    	{
	    		Object userObject = ((DefaultGraphCell)cells[i]).getUserObject();
	    		if(userObject != null)
	    		{
	    			matching.put(userObject, cells[i]);
	    			elements.add(userObject);
	    		}
	    	}
	    	else if(cells[i] instanceof NoteEdge && cs != null)
	    	{
	    		NoteEdge edge = (NoteEdge)cells[i];
	    		//to add a note edge in the diagram we doesn't pass through the ApesMediator
	    		//thus we must check if added the note edge is allowed here 
	    		int found = 0;
	    		Iterator it = cs.connections();
	    		while (it.hasNext()) 
	    		{
					ConnectionSet.Connection connection = (ConnectionSet.Connection) it.next();
					if(connection.getEdge() == edge && contains(getParent(connection.getPort())))
					{
						++found;						
					}
				}
	    		
	    		if(found == 2)
	    		{
	    			noteEdges.add(edge);
	    		}
	    	}
	    	else if(cells[i] instanceof DefaultEdge)
	    	{
	    		DefaultEdge edge = (DefaultEdge)cells[i];
	    		ApesGraphCell source = (ApesGraphCell)getParent(edge.getSource());
	    		ApesGraphCell target = (ApesGraphCell)getParent(edge.getTarget());
	    		Link link = new Link(source.getUserObject(), target.getUserObject());
        		matching.put(link, edge);
	    		elements.add(link);
	    	}
        }
	    
	    Context.getInstance().getUndoManager().save();
	    
	    insertElements( elements, matching, attributes );
	    insertNotes( notes, noteEdges, attributes, cs  );
	    
	    Vector undoableEdits = Context.getInstance().getUndoManager().restore();
        if(undoableEdits != null)
        {
        	for ( Iterator it = undoableEdits.iterator(); it.hasNext(); )
			{
				postEdit((UndoableEdit)it.next());
			}
        }
    }
	
    protected void insertElements( Vector elements, Map matching, Map attributes )
	{
		if(elements.size() > 0)
	    {
		    Map saved = ApesGraphConstants.createMap();
		    if(attributes!=null) ApesGraphConstants.setAttributes(saved, attributes);
	    	ApesGraphConstants.setCells(saved, matching);
	    	ApesMediator.getInstance().insertIn(mDiagram, elements.toArray(), saved);
	    }
	}

	protected void insertNotes( Vector notes, Vector links, Map attributes, ConnectionSet cs )
	{
		if(notes.size()>0 || links.size()>0)
	    {
			notes.addAll(links);
	    	super.insert(notes.toArray(), attributes, cs, null, null);
	    }
	}

	public void remove( Object[] cells )
    {
    	if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemGraphAdapter/"+getName()+") -> remove");
	    
    	Vector notes = new Vector();
    	Vector noteEdges = new Vector();
    	Vector elements = new Vector();
    	Map matching = ApesGraphConstants.createMap();
    	
        for ( int i = 0; i < cells.length; i++ )
        {
        	if(cells[i] instanceof NoteCell)
        	{
        		notes.add(cells[i]);
        	}
        	else if(cells[i] instanceof ApesGraphCell)
        	{
        		Object userObject = ((DefaultGraphCell)cells[i]).getUserObject();
        		if(userObject != null)
        		{
        			matching.put(userObject, cells[i]);
        			elements.add(userObject);
        		}
        	}
        	else if(cells[i] instanceof NoteEdge)
        	{
        		noteEdges.add(cells[i]);
        	}
        	else if(cells[i] instanceof DefaultEdge)
        	{
        		DefaultEdge edge = (DefaultEdge)cells[i];
        		ApesGraphCell source = (ApesGraphCell)getParent(edge.getSource());
	    		ApesGraphCell target = (ApesGraphCell)getParent(edge.getTarget());
	    		Link link = new Link(source.getUserObject(), target.getUserObject());
	    		matching.put(link, edge);
	    		elements.add(link);
        	}
        }
        
        Context.getInstance().getUndoManager().save();
        
        removeNotes( notes, noteEdges );
        removeElements( elements, matching );
        
        Vector undoableEdits = Context.getInstance().getUndoManager().restore();
        if(undoableEdits != null)
        {
        	for ( Iterator it = undoableEdits.iterator(); it.hasNext(); )
			{
				postEdit((UndoableEdit)it.next());
			}
        }
    }

	protected void removeElements( Vector elements, Map matching )
	{
		if(elements.size()>0)
        {
        	Map saved = ApesGraphConstants.createMap();
        	ApesGraphConstants.setCells(saved, matching);
        	ApesMediator.getInstance().removeFrom(mDiagram, elements.toArray(), saved);
        }
	}

	protected void removeNotes( Vector notes, Vector links )
	{
		if(notes.size()>0 || links.size()>0)
        {
			Set elements = new HashSet(links);
			for ( Iterator it = notes.iterator(); it.hasNext(); )
			{
				NoteCell note = (NoteCell) it.next();
				elements.add(note);
				elements.addAll(note.getChildren());
				elements.addAll(getEdges(this, note.getChildren().toArray()));
			}
        	super.remove(elements.toArray());
        }
	}

	public void edit(
    		Map attributes,
    		ConnectionSet cs,
    		ParentMap pm,
    		UndoableEdit[] edits) 
    {
	    if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemGraphAdater/"+getName()+" -> edit( attr "+attributes+" cs "+cs+" pm "+pm+" "+edits+")");
    	
	    if(attributes != null)
    	{
    		Map changes = new HashMap();
    		Map attr = new HashMap();
    		
    		Iterator it = attributes.entrySet().iterator();
    		while (it.hasNext())
    		{
    			Map.Entry entry = (Map.Entry) it.next();
    			DefaultGraphCell cell = (DefaultGraphCell) entry.getKey();
    		    
    			if(cell != null && cell instanceof ApesGraphCell && !(cell instanceof NoteCell))
    			{	
    				Map map = (Map) entry.getValue();
    				
    				if( ApesGraphConstants.getValue(cell.getAttributes()) != null && map.size() == 1 && GraphConstants.getValue(map) instanceof String)
    				{
    					if(! ApesGraphConstants.getValue(map).equals(ApesGraphConstants.getValue(cell.getAttributes()).toString()))
    					{
    						attr.put(cell.getUserObject(), cell);
    						changes.put(cell.getUserObject(), ApesGraphConstants.getValue(map));
    					}
						it.remove();
    				}
    			}    			
    		}
    		
    		if(!changes.isEmpty())
    		{
    			ApesMediator.getInstance().change(mDiagram, changes, attr);
    			return;
    		}
    	}
    	
    	if(!attributes.isEmpty())
    	{
    		super.edit(attributes, cs, pm, edits);
    	}
    }
    
	/**
	 * Move the begin or end extremity, indicating by isSource, of an edge to the new cell 
	 * 
	 * @param edge the edge to move
	 * @param newCell the new cell to connecting
	 * @param isSource true if the concerned end is the source of the edge, false otherwise 
	 */
	public void moveEdge( DefaultEdge edge, ApesGraphCell newCell, boolean isSource )
	{
	    if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemGraphAdater/"+getName()+" -> moveEdge( edge "+edge+" cell "+newCell+" isSource "+isSource+")");
	    
	    if(edge != null && contains(edge))
	    {
	        ApesGraphCell source = isSource? newCell : (ApesGraphCell)getParent(edge.getSource());
            ApesGraphCell target = isSource?(ApesGraphCell)getParent(edge.getTarget()) : newCell;
            ApesGraphCell oldSource = (ApesGraphCell)getParent(edge.getSource());
    		ApesGraphCell oldTarget = (ApesGraphCell)getParent(edge.getTarget());
    		
    		boolean oldEdgeHaveNoteCell = oldSource instanceof NoteCell || oldTarget instanceof NoteCell;     
   	        boolean newEdgeHaveNoteCell = source instanceof NoteCell || target instanceof NoteCell;

   	        //Moves a link from two elements to two elements
   	        if( !oldEdgeHaveNoteCell && !newEdgeHaveNoteCell)
			{
   	            Map move = ApesGraphConstants.createMap();
   	            Link oldLink = new Link(oldSource.getUserObject(), oldTarget.getUserObject());
   	            Link newLink = new Link(source.getUserObject(), target.getUserObject());
   	            
   	            move.put(oldLink, newLink);
 
   	            ApesMediator.getInstance().move(mDiagram, move, null);
			}   		
   	        
   	        //Moves a link from a note cell and an element to another note cell and an element
			if(oldEdgeHaveNoteCell && newEdgeHaveNoteCell)
			{
			    Context.getInstance().getUndoManager().save();
	   			
			    super.remove(new Object[]{edge});
	   			
			    DefaultEdge newEdge = new NoteEdge();
       		    newEdge.setSource(source.getChildAt(0));
       		    newEdge.setTarget(target.getChildAt(0));
       		    
       		    ConnectionSet cs = new ConnectionSet();
       		    cs.connect(edge, source.getChildAt(0), target.getChildAt(0));
        		Map attr = ApesGraphConstants.createMap();
        		attr.put(newEdge, newEdge.getAttributes());
            			     		
        		super.insert(new Object[]{newEdge}, attr, cs, null, null);
			}
			
						
	    }
	}
	
	public void modelChanged(ApesModelEvent e)
	{
	    if(Debug.enabled) Debug.print(Debug.ADAPTER, "(SpemGraphAdapter/"+getName()+") -> modelChanged");
	    
        Object source = e.getChange().getSource();
        Object[] inserted = e.getChange().getInserted();
        Map changed = e.getChange().getChanged();
	    Object[] removed = e.getChange().getRemoved();
        Object[] parents = e.getChange().getParents();
        Map extras = e.getChange().getExtras();
        
        handleInsert(source, inserted, extras);
        handleRemove(source, removed, extras);
        handleChange(changed, extras);
	}
	
	protected Object[] handleInsert(Object source, Object[] elements, Map extras)
	{
	    if(source == mDiagram)
	    {   
	    	if(elements != null)
	        {
	    		if(Debug.enabled) Debug.print(Debug.ADAPTER, "\t(SpemGraphAdapter/"+getName()+") -> handleInsert");
	    	    
	    		Object[] cells = getCellsByUserObject(sortUserObjects(elements, false), extras, true);
	    		ConnectionSet cs = getConnectionSet(cells);
	            Map attributes = getAttributes(cells);
	            
	            super.insert(cells,attributes,cs,null,null);
	        }
	    }
	    return null;
	}
	
	public Object[] sortUserObjects(Object[] elements, boolean linkBefore)
	{
		if(elements == null) return null;
		
		ArrayList result = new ArrayList(elements.length);
		for ( int i = 0; i < elements.length; i++ )
		{
			if(elements[i] instanceof Link)
			{
				result.add( linkBefore? 0 : result.size(), elements[i]); 
			}
			else
			{
				result.add( linkBefore? result.size() : 0, elements[i]);
			}
		}
		return result.toArray();
	}
		
	protected Object[] handleRemove(Object source, Object[] elements, Map attr)
	{
		if(source == null || source == mDiagram)
	    {
	        if(elements == null) return null;
	        
	        if(Debug.enabled) Debug.print(Debug.ADAPTER, "\t(SpemGraphAdapter/"+getName()+") -> handleRemove "+elements.length);
    	    
	    	Set removed = new HashSet();
	    	
	    	for ( int i = elements.length-1; i >= 0; i-- )
	        {
	    		DefaultGraphCell cell = getCellByUserObject(elements[i], attr, false);
	    		
	            if( cell != null )
	            { 
	                if(!roots.contains(cell) && cell instanceof Identity)
	                {
	                    cell = findWithID(((Identity)cell).getID());
	                }
	                if(cell != null && !removed.contains(cell))
	                {
	                	removed.add(cell);
	                	removed.addAll(cell.getChildren());
	                	removed.addAll(getEdges(this,cell.getChildren().toArray()));
	                }
	        	}
	        }
	        super.remove(removed.toArray());
	    }	    
	    return null;
	}
	
	protected Map handleChange(Map changed, Map extras)
	{
		if(changed == null) 
			return null;
		
		if(Debug.enabled) Debug.print(Debug.ADAPTER, "\t(SpemGraphAdapter/"+getName()+") -> handleChange "+changed);
		
		Map change = new HashMap();
		Object[] cells = getCellsByUserObject(changed.keySet().toArray(), extras, false);
		for ( int i = 0; i < cells.length; i++ )
		{
			Object tmp = cells[i];
			if(tmp instanceof ApesGraphCell && contains(tmp))
			{
				ApesGraphCell cell = (ApesGraphCell) tmp;
				Map oldAttr = ApesGraphConstants.createMap();
				Map newAttr = ApesGraphConstants.createMap();
				ApesGraphConstants.setValue(oldAttr, cell.getUserObject().toString());
				ApesGraphConstants.setValue(newAttr, changed.get(cell.getUserObject()));
				cell.changeAttributes(newAttr);
				
				change.put(cell, oldAttr);
			}
		}
		if(!change.isEmpty())
		{
			super.edit(change, null, null, null);
		}
		
		return null;
	}
	
	/**
	 * Retrieve cells by their userObject. Search in the attr's map then in the model and finally create the cells if they don't exist and if create is true.
	 * 
	 * @param userObjects
	 * @param attr a map used to store attributes
	 * @param create true if the cells must be create if they don't exist
	 * @return an array of cells
	 */
	public Object[] getCellsByUserObject(Object[] userObjects, Map attr, boolean create)
    {
		Vector cells = new Vector();
	    for ( int i = 0; i < userObjects.length; i++ )
        {
            Object tmp = getCellByUserObject(userObjects[i],attr,true);
    	    if(tmp!=null)
            {
                cells.add(tmp);
            }
        }
	    return cells.toArray();
    }
	
	/**
	 * Retrieve a cell by its userObject. Search in the attr's map then in the model and finally create the cell if she doesn't exist and if create is true.
	 * 
	 * @param userObject
	 * @param attr a map used to store attributes
	 * @param create true if the cells must be create if they don't exist
	 * @return a cell containing the userObject
	 */
	public DefaultGraphCell getCellByUserObject(Object userObject, Map attr, boolean create)
    {
		DefaultGraphCell cell = null;
		//try to find the cell in attr's map
		if(attr != null && attr.containsKey(ApesGraphConstants.GRAPH_CELLS))
		{
			cell = (DefaultGraphCell)ApesGraphConstants.getCells(attr).get(userObject);
		}
		//try to retrieve the cell in the graph
		if(cell == null)
		{
			if(userObject instanceof Identity)
			{
				cell = findWithID(((Identity)userObject).getID());          
			}
			else if(userObject instanceof Link)
			{
				Link link = (Link)userObject;
				cell = getEdge(link);
			}
		}
		//try to create the cell
		if( cell == null && create )
		{
			if(mBuilder.shouldGoInGraph(userObject))
			{
				cell = mBuilder.create(userObject);				
			}
		}
		return cell;
	}
	
	/**
	 * Retrieve an edge by a link
	 * 
	 * @param link 
	 * @return the edge representing the link passed in parameter
	 */
	public DefaultEdge getEdge(Link link)
	{
		DefaultGraphCell source = getCellByUserObject(link.getSource(), null, false);
		DefaultGraphCell target = getCellByUserObject(link.getTarget(), null, false);
		
		if(source != null && source.getChildCount() == 1 && target != null && target.getChildCount() == 1)
		{
			Iterator it = edges(source.getChildAt(0));
			while(it.hasNext() )
			{
				DefaultEdge edge = (DefaultEdge) it.next();
				if(edge.getTarget() == target.getChildAt(0))
				{
					return edge;
				}
			}
		}
		return null;
	}
	
	protected ConnectionSet getConnectionSet(Object[] cells)
	{
	    ConnectionSet cs = new ConnectionSet(); 
	    for ( int i = 0; i < cells.length; i++ )
        {
            if(cells[i] instanceof DefaultEdge)
            {
            	DefaultEdge edge = (DefaultEdge)cells[i];
            	cs.connect(edge, edge.getSource(), edge.getTarget());
                //set source and target to null to delegate the creation of the connection to the DefaultGraphModel
                edge.setSource(null);
                edge.setTarget(null);
            }
        }
	    return cs.size()>0?cs:null;
	}
	
	/**
	 * Return a map containing cells and their attributes in value
	 * @param cells
	 * @return a map containing cells and their attributes in value
	 */
	public Map getAttributes(Object[] cells)
	{
	    Map attr = ApesGraphConstants.createMap();
	    for ( int i = 0; i < cells.length; i++ )
        {
            attr.put(cells[i], getAttributes(cells[i]));
        }
	    return attr;
	}
	
	/**
	 * The bulider create the cell corresponding to an element of the model.
	 * He implements the ModelVisitor.
	 */
	public abstract class Builder implements ModelVisitor, Serializable 
	{
		protected DefaultGraphCell mCreated = null;
		
		/**
		 * Create the cell corresponding to the given object
		 * @param o an object of the model to encapsulate in a cell
		 * @return the coresponding cell
		 */
		public abstract DefaultGraphCell create( Object o );
		
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
		
		public void visitTransition(ActivityDiagram.Transition transition) { mCreated = null; }
		
		protected Map getDefaultEdgeAttributes()
		{
		    Map map = ApesGraphConstants.createMap();
		    int arrow = ApesGraphConstants.ARROW_CLASSIC;
		    ApesGraphConstants.setLineEnd(map , arrow);
		    ApesGraphConstants.setDashPattern(map, new float[] { 3, 3 });
		    ApesGraphConstants.setEndFill(map, true);
		    ApesGraphConstants.setEditable(map, false);
		    return map;
		}
	}

}


