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
package org.ipsquad.apes.model.frontend;

import java.awt.Rectangle;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

import javax.swing.event.EventListenerList;
import javax.swing.undo.CannotRedoException;
import javax.swing.undo.CannotUndoException;
import javax.swing.undo.CompoundEdit;
import javax.swing.undo.UndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import org.ipsquad.apes.ApesGraphConstants;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.Identity;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.frontend.event.ApesModelEvent;
import org.ipsquad.apes.model.frontend.event.ApesModelListener;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;

/**
 * 
 * @version $Revision: 1.36 $
 */
public class ApesMediator extends UndoableEditSupport implements Serializable
{
	private static final ApesMediator mInstance = new ApesMediator();
	
	private ResourceManager mResource = ResourceManager.getInstance();
	private ConfigManager mConfig = ConfigManager.getInstance();
	
	protected transient EventListenerList mListenerList =
		new EventListenerList();

	private Vector mListeners = new Vector();
	private Vector mDiagrams = new Vector();
	
	/**
	 *allow to store the edits in place to add them to the undo manager
	 *@see private method postEdit and restorePostEdits for more information 
	 */
	private boolean mStorePostEdits = false;
	private Vector mPostEdits = new Vector();
	
	private ApesMediator() { }
	
	/**
	 * Return the unique instance of the ApesMediator
	 * 
	 * @return the unique instance of the ApesMediator
	 */
	public static ApesMediator getInstance()
	{
		return mInstance;
	}
	
	/**
	 * Adds a listener for the ApesModelEvent posted after the model changes
	 * 
	 * @param l
	 */
	public void addApesModelListener(ApesModelListener l) 
	{
		mListenerList.add(ApesModelListener.class, l);
	}
	
	/**
	 * Removes a listener previously added with addApesModelListener()
	 * 
	 * @param l
	 */
	public void removeApesModelListener(ApesModelListener l) 
	{
		mListenerList.remove(ApesModelListener.class, l);
	}
	
	/**
	 * Register a diagram which has been created
	 * @param diagram
	 */
	public void registerDiagram( SpemDiagram diagram )
	{
		if( !mDiagrams.contains(diagram) )
		{
			mDiagrams.add(diagram);
		}
	}
	
	/**
	 * Set a new process
	 * 
	 * @param ap the process
	 */
	public void setProcess(ApesProcess ap)
	{
		if( ap.getComponent() != null || ap.getProvidedInterface() != null || ap.getRequiredInterface() != null )
		{
			// if there is no component, create the interfaces then create a new component
			if(ap.getComponent() == null)
			{
			    String name = mConfig.getProperty("Component");
				ProcessComponent pc = new ProcessComponent(name!=null?name:"Component");
				name = mConfig.getProperty("ContextDiagram");
				ContextDiagram cd = new ContextDiagram(name!=null?name:"ContextDiagram");
				name = mConfig.getProperty("WorkDefinitionDiagram");
				WorkDefinitionDiagram wd = new WorkDefinitionDiagram(name!=null?name:"WorkDefinitionDiagram");
				
				insertInModel(new Object[]{pc}, new Object[]{ap}, null);
				insertInModel(new Object[]{cd,wd}, new Object[]{pc,pc}, null);		
				
				loadProvidedInterface(ap);
				loadRequiredInterface(ap);
			}
			else
			{
				registerAllDiagrams(ap.getComponent());
			}
		}
		else
		{
			initNewProcess(ap);
		}
	}

	private void registerAllDiagrams(IPackage p)
	{
		for( int i = 0; i < p.modelElementCount(); i++ )
		{
			if( p.getModelElement(i) instanceof SpemDiagram )
			{
				mDiagrams.add(p.getModelElement(i));
			}
			if( p.getModelElement(i) instanceof IPackage )
			{
				registerAllDiagrams((IPackage)p.getModelElement(i));
			}
		}
	}
	
	/**
	 * Create or load the provided interface
	 * 
	 * @param ap
	 */
	private void loadProvidedInterface(ApesProcess ap)
	{
		if( ap.getProvidedInterface() == null )
		{	
		    ModelElement me = new ApesProcess.ProvidedInterface(mConfig.getProperty("Provided"));
			ap.addModelElement(me);
			insertInModel(new Object[]{me}, new Object[]{ap}, null);
			
		}
		else
		{
		    loadInterface(ap, ap.getProvidedInterface());
		}
	}
	
	/**
	 * Create or load the required interface
	 * 
	 * @param ap
	 */
	private void loadRequiredInterface(ApesProcess ap)
	{
		WorkProductRef ref = null;
		ModelElement me = null;
		ProcessComponent c = ap.getComponent();
		
		if( ap.getRequiredInterface() == null )
		{	
			me = new ApesProcess.RequiredInterface(mConfig.getProperty("Required"));
			ap.addModelElement(me);
			insertInModel(new Object[]{me}, new Object[]{ap}, null);
		}
		else
		{
			loadInterface(ap, ap.getRequiredInterface());
		}
	}
	
	/**
	 * Load the content of an interface
	 * 
     * @param ap
     * @param in
     */
    private void loadInterface(ApesProcess ap, ApesProcess.Interface in) 
    {
        ProcessComponent c = ap.getComponent();
        WorkProductRef ref = null;
        
        ap.removeModelElement(in);
        insertInModel(new Object[]{in}, new Object[]{ap}, null);
        
        SpemDiagram diag = (SpemDiagram)ap.getComponent().getModelElement(0);
        SpemGraphAdapter adapt = Context.getInstance().getProject().getGraphModel(diag);
        
        Map link = new HashMap();
        
        int i = in.modelElementCount()-1;
        int index = 0;
        while( i >= 0 )
        {
        	ref = (WorkProductRef)in.getModelElement(index);
        	in.removeModelElement(ref);
        	
        	WorkProductCell cell = new WorkProductCell(ref.getReference());
        	Map apply = ApesGraphConstants.createMap();
        	Map attr = cell.getAttributes();
        	Rectangle bounds = new Rectangle(in instanceof ApesProcess.ProvidedInterface?350:50,i*75+10,50,50);
        	ApesGraphConstants.setBounds(attr,bounds);
        	apply.put(cell, attr);
        	
        	adapt.insert(new Object[]{cell}, apply, null, null, null);
        	if(in instanceof ApesProcess.ProvidedInterface)
        	{
        	    insertIn(diag, new Object[]{new Link(c,ref.getReference())}, null);
        	}
        	else
        	{
        	    insertIn(diag, new Object[]{new Link(ref.getReference(),c)}, null);       	    
        	}
        	--i;
        }
    }

    /**
	 * Create a new process
	 * @param ap
	 */
	private void initNewProcess( ApesProcess ap )
	{
		String name = mConfig.getProperty("Component");
		ProcessComponent pc = new ProcessComponent(name!=null?name:"Component");
		name = mConfig.getProperty("ContextDiagram");
		ContextDiagram cd = new ContextDiagram(name!=null?name:"ContextDiagram");
		name = mConfig.getProperty("WorkDefinitionDiagram");
		WorkDefinitionDiagram wd = new WorkDefinitionDiagram(name!=null?name:"WorkDefinitionDiagram");
		name = mConfig.getProperty("Provided");
		ApesProcess.ProvidedInterface api = new ApesProcess.ProvidedInterface(name!=null?name:"Provided");
		name = mConfig.getProperty("Required");
		ApesProcess.RequiredInterface ari = new ApesProcess.RequiredInterface(name!=null?name:"Required");
		
		insertInModel(new Object[]{pc}, new Object[]{ap}, null);
		insertInModel(new Object[]{cd,wd}, new Object[]{pc,pc}, null);		
		insertInModel(new Object[]{api,ari}, new Object[]{ap,ap}, null);
	}
		
	/**
	 * Clear all current ApesMediator listeners
	 */
	public void clearListeners()
	{
		mListenerList = new EventListenerList();
	}
	
	/**
	 * Clear all listeners and all registered diagrams
	 */
	public void clearAll()
	{
		mListeners.clear();
		mDiagrams.clear();
	}
	
	/**
	 * Sets the source of a link
	 * 
	 * @param link the link to set the source
	 * @param source the source
	 */
	public void setSource(Object link, Object source)
	{
	    if(link instanceof Link)
	    {
	        ((Link)link).setSource(source);
	    }
	}

	/**
	 * Sets the target of a link
	 * 
	 * @param link the link to set the target
	 * @param source the target
	 */
	public void setTarget(Object link, Object target)
	{
	    if(link instanceof Link)
	    {
	        ((Link)link).setTarget(target);
	    }
	}

	/**
	 * Returns true if the object passed in parameter should go in the tree view
	 * 
	 * @param o an object of the model
	 * @return true if the object should go in the tree view or null
	 */
	public boolean shouldGoInTree(Object o)
	{
		if(o instanceof Activity || o instanceof ApesProcess || o instanceof ApesWorkDefinition
			|| o instanceof ProcessComponent || o instanceof ProcessRole || o instanceof StateMachine 
			|| o instanceof WorkDefinition || o instanceof WorkProduct || o instanceof WorkProductRef 
			|| o instanceof SPackage || o instanceof ActivityDiagram || o instanceof ContextDiagram 
			|| o instanceof FlowDiagram || o instanceof WorkDefinitionDiagram || o instanceof ResponsabilityDiagram
			|| o instanceof ApesProcess.ProvidedInterface || o instanceof ApesProcess.RequiredInterface)
		{
	        return true;
	    }
	    return false;
	}
	
	/**
	 * Find an element by its id
	 * 
	 * @param id the id of the element to find
	 * @return the corresponding object or null
	 */
	public Object findByID( int id )
	{
		ApesProcess ap = Context.getInstance().getProject().getProcess();
		return findByID( id, ap );
	}
	
	/**
	 * Recursive search by the id of an element
	 * 
	 * @param id the id of the element to find
	 * @param element the current element
	 * @return the corresponding object or null
	 */
	private Identity findByID( int id, Element element )
	{
		if( element.getID() == id )
		{
			return element;
		}
		
		if( element instanceof IPackage)
		{
			IPackage p = (IPackage)element;
			for( int i = 0; i < p.modelElementCount(); i++ )
			{
				Identity o = findByID( id, p.getModelElement(i) );
				
				if( o != null && o.getID() == id )
				{
					return o;
				}
			}
		}
		
		return null;
	}

	/**
	 * Gets all links in a diagram linked to at least one element of the array passed in parameter
	 * 
	 * @param diagram the diagram
	 * @param elements the elements
	 * @return a collection of Link
	 */
	public Collection getLinks(SpemDiagram diagram, Object[] elements)
	{
		if(elements == null)
			return null;
		
	    Set set = new HashSet();
	    for ( int i = 0; i < elements.length; i++ )
        {
	        if(elements[i] != null)
	        {
	        	if(!(elements[i] instanceof Link))
	        	{
	        		set.addAll(getLinks( diagram, elements[i] ));
	        	}
	            else set.add(elements[i]);
	        }	        
        }
	    return set;
	}
	
	/**
	 * Gets all links in a diagram linked to with the element passed in parameter
	 * 
	 * @param diagram the diagram
	 * @param elements the element
	 * @return a collection of Link
	 */
	public Collection getLinks( SpemDiagram diagram, Object element )
    {
	    Set set = new HashSet();
        if(element instanceof ModelElement)
        {
            ModelElement me = (ModelElement) element;
            if(diagram.containsModelElement(me))
            {
            	Collection links = ((SpemGraphAdapter)Context.getInstance().getProject().getGraphModel(diagram)).getLinks(me); 
                if(links != null)
                {
                	set.addAll(links);
                }
            }
        }
        else if( element instanceof Link )
        {
        	set.add(element);
        }
        return set;
    }

	/**
	 * Gets all elements of a diagram
	 * 
	 * @param diagram the diagram
	 * @return a collection containing all elements in the diagram
	 */
	public Collection getAllElements(SpemDiagram diagram)
	{
		if(diagram == null)
			return null;
		
		Collection set = new HashSet();
		for ( int i = 0; i < diagram.modelElementCount(); i++ )
		{
			ModelElement me = diagram.getModelElement(i);
			set.addAll(getLinks(diagram, me));
			set.add(me);
		}
		return set;
	}
	
	/**
	 * Gets all descendants of elements 
	 * @param elements
	 * @return a collection containing all descendants of the elements
	 */
	public Collection getDescendants(Object[] elements)
	{
		if(elements == null)
			return null;
		
        Set set = new HashSet();
        //get all parts of the tree
        findRecursiveDependanciesInModel(elements, set);        
	    return set;
	}
	
	/**
	 * Gets an array containing the parents of each elements
	 * 
	 * @param elements
	 * @return an array containing the parents of each elements
	 */
    public Object[] getParents( Object[] elements )
    {
    	if(elements == null)
    		return null;
    	
        Object[] parents = new Object[elements.length];
        for ( int i = 0; i < elements.length; i++ )
        {
            if(elements != null && elements[i] instanceof ModelElement)
            {
                parents[i] = ((ModelElement)elements[i]).getParent();
            }
        }
        return parents;
    }
    
    private void findRecursiveDependanciesInModel(Object[] elements, Collection c)
    {
    	for( int i = 0; i < elements.length; i++ )
        {
    		if(elements[i] != null)
    		{
    			if(elements[i] instanceof IPackage)
    			{
    				IPackage p = (IPackage)elements[i];
    				Object[] tmpElements = new Object[p.modelElementCount()];
    				for(int j = 0; j < p.modelElementCount(); j++)
    				{
    					tmpElements[j] = p.getModelElement(j);
    				}
    				findRecursiveDependanciesInModel(tmpElements,c);
    			}
    			if(elements[i] instanceof ModelElement)
    			{
    				ModelElement me = (ModelElement)elements[i];
    				Object[] tmpElements = new Object[me.behaviorCount()];
    				for(int j = 0; j < me.behaviorCount(); j++)
    				{
    					tmpElements[j] = me.getBehavior(j);
    				}
    				findRecursiveDependanciesInModel(tmpElements,c);
    			}
    			c.add(elements[i]);
    		}
        }        
    }
    
    /**
     * Gets an array containing the parents of each elements from a diagram
     * 
     * @param diagram
     * @param elements
     * @return an array containing the parents of each elements from a diagram
     */
    protected Object[] getParentsFromDiagram( SpemDiagram diagram, Object[] elements)
	{
	    Vector parents = new Vector();
	    IPackage parent = diagram.getParent();
        if(parent != null)
        {       
            IPackage parentParent = parent.getParent(); 
            if(diagram instanceof ContextDiagram
               || diagram instanceof ResponsabilityDiagram
               || diagram instanceof WorkDefinitionDiagram)
            {
                for(int i = 0; i < elements.length; i++)
                {
                    if(elements[i] instanceof ModelElement)
                    {
                    	ModelElement me = (ModelElement)elements[i];
						parents.add(me.getParent()==null?parent:me.getParent());						
                    }
                    else
                    {
                        parents.add(null);
                    }
                }
            }
            else if( parentParent != null 
               && (diagram instanceof ActivityDiagram
                       || diagram instanceof FlowDiagram))
            {
                for(int i = 0; i < elements.length; i++)
                {
                	if(elements[i] instanceof ModelElement)
                	{
                		ModelElement me = (ModelElement)elements[i];
                		if(me instanceof Activity)
                		{
                			if(me.getParent() == null || me.getParent() == parent)
                			{
                				parents.add(parent);
                			}
                			else parents.add(null);
                		}
                		else
                		{
                            parents.add(me.getParent()==null?parentParent:me.getParent());                			
                		}
                	}
                    else
                    {
                        parents.add(null);
                    }
                }
            }
        } 
        return parents.size()==elements.length?parents.toArray():null;
	}
    
    /**
     * Returns the reference of a work product
     *  
     * @param w the work product
     * @return the reference of a work product
     */
    public WorkProductRef getWorkProductRef(WorkProduct w)
    {
        ApesProcess.Interface in = Context.getInstance().getProject().getProcess().getProvidedInterface();
        WorkProductRef ref = getWorkProductRef(in, w);
        if(ref != null)
        {
            return ref;
        }
        in = Context.getInstance().getProject().getProcess().getRequiredInterface();
        ref = getWorkProductRef(in, w);
        return ref;
    }
    
    /**
     * Returns the reference of a work product in an interface
     *  
     * @param in the interface where the reference must be found
     * @param w the work product
     * @return the reference of a work product
     */
    public WorkProductRef getWorkProductRef(ApesProcess.Interface in, WorkProduct w)
    {
        if(in != null)
        {
            for (int i = 0; i < in.modelElementCount(); i++) 
            {
                WorkProductRef temp = (WorkProductRef)in.getModelElement(i);
                if(temp.getReference() == w)
                {
                    return temp;
                }
            }
        }
        return null;
    }
    
    /**
     * Insert the elements in the diagram
     * 
     * @param elements the elements to insert
     * @param parents the parents of each elements
     * @param extras a stored map wich will be returned by the ApesModelEvent
     */
	public void insertInModel(
	        	Object[] elements, 
	        		Object[] parents, 
	        			Map extras)
	{
		if( parents == null || elements == null || elements.length != parents.length)
			throw new IllegalArgumentException("ApesMediator.insert : elements and parents length was different!");
		
		if(Debug.enabled)
		{
			String mess = "[ ";
			for ( int i = 0; i < parents.length; i++ )
			{
				mess+=elements[i]+"/"+parents[i]+", ";
			}
			Debug.print(Debug.MEDIATOR, "\n(ApesMediator) -> insertInModel("+mess+"])");
		}
		
		ApesModelEdit edit =
			createInsertModelEdit(elements, parents, extras);		
		
		if(edit != null)
		{
			saveEdits();				
			if(executeModelEdit(edit, getDependantInsertEdits( elements )))
			{
				Collection extraEdits = addDependantEdits(edit);
				extraEdits.addAll(restoreEdits());
				postEdit(edit, extraEdits);
			}
			else restoreEdits();
   		}
		/****NEWLINE*****/
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "");
	}
	
	/**
	 * Adds edits dependant of another edit
	 * 
     * @param edit the precedent executed edit
     * @return a collection of edits dependant of the precedent edit 
     */
    protected Collection addDependantEdits(ApesEdit edit) 
    {
        Collection edits = insertWorkProductRef(edit, edit.getInserted());
        edits.addAll(changeWorkProductRef(edit, edit.getChanged()));
        edits.addAll(removeWorkProductRef(edit, edit.getRemoved()));
        
        return edits;
    }

    /**
     * @param edit
     * @param edits
     * @param inserted
     */
    protected Collection insertWorkProductRef(ApesEdit edit, Object[] inserted) 
    {
        Vector edits = new Vector();
        
        if(inserted != null && edit.getSource() instanceof ContextDiagram)
        {
            for (int i = 0; i < inserted.length; i++) 
            {
                if(inserted[i] instanceof Link)
                {
                    Link link = (Link)inserted[i];
                    WorkProductRef ref = null;
                    Object parent = null; 
                    if(link.getSource() != null && link.getSource() instanceof WorkProduct)
                    {
                        ref = new WorkProductRef((WorkProduct)link.getSource());
                        parent = Context.getInstance().getProject().getProcess().getRequiredInterface();
                    }
                    else if(link.getTarget() != null && link.getTarget() instanceof WorkProduct)
                    {
                        ref = new WorkProductRef((WorkProduct)link.getTarget());
                        parent = Context.getInstance().getProject().getProcess().getProvidedInterface();
                    }
                    
                    ApesEdit tmpEdit = createInsertModelEdit(new Object[]{ref},new Object[]{parent}, null);
                    if(tmpEdit != null && tmpEdit.execute())
                    {
                        fireModelChanged(null, tmpEdit);
                        tmpEdit.end();
            			edits.add(tmpEdit);
                    }
                }
            }
        }
        return edits;
    }

    /**
     * @param edit
     * @param edits
     * @param inserted
     */
    protected Collection removeWorkProductRef(ApesEdit edit, Object[] removed) 
    {
        Vector edits = new Vector();
        
        if(removed != null && edit.getSource() instanceof ContextDiagram)
        {
            for (int i = 0; i < removed.length; i++) 
            {
                if(removed[i] instanceof Link)
                {
                    Link link = (Link)removed[i];
                    WorkProductRef ref = null;
                    
                    if(link.getSource() != null && link.getSource() instanceof WorkProduct)
                    {
                        ref = getWorkProductRef((WorkProduct)link.getSource());
                    }
                    else if(link.getTarget() != null && link.getTarget() instanceof WorkProduct)
                    {
                        ref = getWorkProductRef((WorkProduct)link.getTarget());
                    }
                    
                    ApesEdit tmpEdit = createRemoveModelEdit(new Object[]{ref}, null);
                    if(tmpEdit != null && tmpEdit.execute())
                    {
                        fireModelChanged(null, tmpEdit);
                        tmpEdit.end();
            			edits.add(tmpEdit);
                    }
                }
            }
        }
        return edits;
    }
    
    
    /**
     * @param edit
     * @param edits
     * @param inserted
     */
    protected Collection changeWorkProductRef(ApesEdit edit, Map change) 
    {
        Vector edits = new Vector();
        
        if(change != null)
        {
            Iterator it = change.entrySet().iterator();
    		while(it.hasNext())
    		{
        		Map.Entry entry = (Map.Entry)it.next();
    			ModelElement element = (ModelElement)entry.getKey();
            
                if(element instanceof WorkProduct)
                {
                    WorkProduct wp = (WorkProduct)element;
                    WorkProductRef ref = null;
                    
                    
                    ref = getWorkProductRef(wp);
                    
                    Map extrasChanges = new HashMap();
                    String value = ConfigManager.getInstance().getProperty("Reference") + wp.getName();
                    
                    extrasChanges.put(ref, value);
                    ApesEdit tmpEdit = createChangeEdit(extrasChanges, null);
                    if(tmpEdit != null && tmpEdit.execute())
                    {
                        fireModelChanged(null, tmpEdit);
                        tmpEdit.end();
            			edits.add(tmpEdit);
                    }
                }
            }
        }
        return edits;
    }
    
    /**
	 * Execute an edit and all extra edits and fire all listeners if the execution returns true
	 * 
	 * @param edit the ApesModelEdit to execute
	 * @param extraEdits the extra edits to execute
	 * @return true if the execution returns true, false otherwise
	 */
	protected boolean executeModelEdit(ApesModelEdit edit, Vector extraEdits)
	{
		if(edit.execute())
		{
			fireModelChanged(null, edit);	            
			for ( int i = 0; i < extraEdits.size(); i++ )
			{
				ApesEdit tmpEdit = (ApesEdit)extraEdits.get(i);
				if(tmpEdit.execute())
				{
					fireModelChanged(tmpEdit.getSource(), tmpEdit);	            
					edit.addEdit((UndoableEdit)extraEdits.get(i));
				}
			}
			return true;
		}
		return false;
	}
	
	/**
	 * Adds elements to the model depending of the type of the elements
	 * 
	 * @param elements
	 * @return a vector of ApesEdit representing the changes
	 */
	protected Vector getDependantInsertEdits( Object[] elements )
	{
		Vector edits = new Vector();
		for ( int i = 0; i < elements.length; i++ )
		{
			if(elements[i] instanceof ApesWorkDefinition)
			{
				ApesWorkDefinition wd = (ApesWorkDefinition)elements[i];
				ApesEdit edit = createInsertModelEdit(
						new Object[]{new ActivityDiagram(mConfig.getProperty("ActivityDiagram")), 
						        new FlowDiagram(mConfig.getProperty("FlowDiagram"))}, 
						new Object[]{wd,wd},
						null);
				edit.end();
				edits.add(edit);
			}	
		}
		return edits;
	}

	/**
	 * Inserts elements in source
	 * 
	 * @param source the source
	 * @param elements the elements to insert
     * @param extras a stored map wich will be returned by the ApesModelEvent
	 */
	public void insertIn(
	        Object source, 
	        	Object[] elements,
	        		Map extras)
	{		
		if(elements == null) return;
	    
	    if(source instanceof SpemDiagram)
	    {
			if(Debug.enabled) 
			{
				String mess = "[ ";
				for ( int i = 0; i < elements.length; i++ )
				{
					mess+=elements[i]+", ";
				}
				Debug.print(Debug.MEDIATOR, "\n(ApesMediator) -> insertInDiagram( "+source+":"+mess+"])");
			}
			
			Object[] parents = getParentsFromDiagram((SpemDiagram)source, elements);
	        if(parents != null)
	        {
	        	ApesModelEdit modelEdit = createInsertModelEdit(elements, parents, null);
	            ApesDiagramEdit diagEdit = createInsertDiagramEdit((SpemDiagram)source,elements, extras);
	            
	            if(diagEdit != null)
	            {
	            	saveEdits();
	            	if(diagEdit.execute())
	            	{
	            		fireModelChanged(diagEdit.getSource(), diagEdit);
		            	if(modelEdit != null)
		            	{
		            		if(executeModelEdit(modelEdit, getDependantInsertEdits(elements)))
		            		{
				            	modelEdit.end();
		            			diagEdit.addEdit(modelEdit);
		            		}
		            	}
		            	
		            	Collection extraEdits = addDependantEdits(diagEdit);						
		            	extraEdits.addAll(restoreEdits());
		            	postEdit(diagEdit, extraEdits);
	            	}	            	
				}
	        }
	    }
	    /****NEWLINE*****/
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "");
	}
	
	/**
	 * Removes elements from the model
	 *  
	 * @param elements the elements to removed
     * @param extras a stored map wich will be returned by the ApesModelEvent
	 */
	public void removeFromModel(Object[] elements, Map extras)
	{
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "\n(ApesMediator) -> removeFromModel ");
		
		//retrieve all elements to be removed
	    elements = getDescendants(elements).toArray();
	    
	    Vector edits = new Vector();	    
	    edits = removeAllLinks( elements );
	    edits.addAll(removeDiagramFromModel( elements ));

	    //execute all edits
	    ApesEdit edit = createRemoveModelEdit(elements, extras);
		if(edit != null)
		{
			saveEdits();				
			Collection extraEdits = addDependantEdits(edit);
			
			for ( int i = 0; i < edits.size(); i++ )
			{
				ApesEdit tmpEdit = (ApesEdit)edits.get(i);
				if(tmpEdit.execute())
				{
					fireModelChanged(tmpEdit.getSource(), tmpEdit);	            
					edit.addEdit((UndoableEdit)edits.get(i));
					extraEdits.addAll(addDependantEdits((ApesEdit)edits.get(i)));
				}
			}
			if(edit.execute())
			{
				fireModelChanged(null, edit);	            
				
				extraEdits.addAll(restoreEdits());
				postEdit(edit, extraEdits);
			}
			else restoreEdits();
   		}
		/****NEWLINE*****/
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "");
	}
	
	/**
	 * Removes diagrams of the elements array
	 * 
	 * @param elements
	 * @return a vector of ApesEdit representing the changes
	 */
	protected Vector removeDiagramFromModel( Object[] elements )
	{
		//remove all elements in diagram to be removed
		Vector edits = new Vector();
	    for ( int i = 0; i < elements.length; i++ )
        {
	    	if(elements[i] instanceof SpemDiagram)
            {
	    		SpemDiagram diagram = (SpemDiagram)elements[i];
	    		ApesEdit edit = createRemoveDiagramEdit(diagram, getAllElements(diagram).toArray(), null);
                if(edit != null)
                {
                	edit.end();
                	edits.add(edit);
                }
            }
        }
	    return edits;
	}

	/**
	 * Removes all links linked to at least one of the elements
	 * 
	 * @param elements
	 * @return a vector of ApesEdit representing the changes
	 */
	protected Vector removeAllLinks( Object[] elements )
	{
		//remove all concerned links from all diagrams
		Vector edits = new Vector();
	    for ( int i = 0; i < mDiagrams.size(); i++ )
		{
			SpemDiagram diagram = (SpemDiagram) mDiagrams.get(i);
			Object[] toRemove = getDependanciesFromDiagram( (SpemDiagram)diagram, elements );
			if(toRemove != null && toRemove.length > 0)
			{
				ApesEdit edit = createRemoveDiagramEdit(diagram, toRemove, null);
				if(edit != null)
				{
					edit.end();
					edits.add(edit);
				}
			}
		}
	    return edits;
	}

	/**
	 * Removes elements from the source passed in parameter
	 * 
	 * @param source the source
	 * @param elements the elements to remove
     * @param extras a stored map wich will be returned by the ApesModelEvent
	 */
	public void removeFrom(Object source, 
            				Object[] elements, 
            					Map extras)
	{
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "\n(ApesMediator) -> removeFrom ");
		
		if(source instanceof SpemDiagram && elements != null)
		{
			SpemDiagram diagram = (SpemDiagram)source;
			ApesDiagramEdit edit = createRemoveDiagramEdit(diagram, getDependanciesFromDiagram( (SpemDiagram)source, elements ), extras);
			if (edit != null) 
			{
			    if(edit.execute())
				{
					saveEdits();
					fireModelChanged(edit.getSource(), edit);
					
					Collection extraEdits = addDependantEdits(edit);
					extraEdits.addAll(restoreEdits());
					postEdit(edit, extraEdits);
				}
			}
		}
		/****NEWLINE*****/
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "");
	}

	/**
	 * Gets all dependant's elements in the diagram
	 * 
	 * @param diagram
	 * @param elements
	 * @return an array of the elements and their direct dependancies
	 */
	protected Object[] getDependanciesFromDiagram( SpemDiagram diagram, Object[] elements )
	{
		Vector tmpElements = new Vector(getLinks(diagram, elements));
		for ( int i = 0; i < elements.length; i++ )
		{
			if(!(elements[i] instanceof Link)) 
			{
				tmpElements.add(elements[i]);
			}
		}
		return tmpElements.toArray();
	}

	/**
	 * Changes attributes of elements in the model
	 * 
	 * @param source the source of the changes
	 * @param change a map representing the changes
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 */
	public void change(Object source,  Map change, Map extras)
	{
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "\n(ApesMediator) -> edit ");
		
		//create an edit represent the editing
		ApesModelEdit edit = createChangeEdit(change, extras);
		if(edit != null)
		{
			if(edit.execute())
			{
				saveEdits();
				edit.mExecuteRedoEditsFirst=false;
				edit.mExecuteUndoEditsFirst=false;
				fireModelChanged(edit.getSource(), edit);
				
				Collection extraEdits = addDependantEdits(edit);
				extraEdits.addAll(restoreEdits());
				//postEdit(edit, restoreEdits());
				postEdit(edit, extraEdits);
			}
		}
	}

	/**
	 * Moves elements in the model or links in a diagram
	 * 
	 * @param moves a map representing the changes
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 */
	public void move(Object source, Map moves, Map extras)
	{
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "\n(ApesMediator) -> move ");
		
		if(moves == null || moves.isEmpty())
			return;
		
		if(source != null && source instanceof SpemDiagram)
		{
		    moveLinkInDiagram((SpemDiagram)source, moves, extras);
		}
		else
		{
			moveInModel(moves, extras);
		}
		
		/****NEWLINE*****/
		if(Debug.enabled) Debug.print(Debug.MEDIATOR, "");
	}
	
	/**
	 * Moves elements in the model
	 * 
	 * @param moves the containing element/new parent pairs
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 */
	protected void moveInModel(Map moves, Map extras) 
	{
		//create the edit which represents the move
		ApesEdit edit = createMoveEdit(moves, extras);
		
		//gets edits for each activity
		//when you move it, you must remove it from its work definition 
		//before insert it in the new work definition
		Collection extraEdits = moveActivitiesInModel(moves,extras);
		
		saveEdits();
			
		//execute the edit
		boolean postEdit = false;
		if(edit != null && edit.execute())
		{
			fireModelChanged(null, edit);
			postEdit = true;
		}
		
		//execute the extra edits
		if(extraEdits != null)
		{
			for (Iterator it = extraEdits.iterator(); it.hasNext();) 
			{
				ApesEdit tmpEdit = (ApesEdit) it.next();
				if(tmpEdit.execute())
				{
					fireModelChanged(null, tmpEdit);
					postEdit = true;
				}
			}
		}

		if(postEdit)
		{
		    //update edit
		    if(edit == null)
		    {
		        Iterator it = extraEdits.iterator();
		        edit = (ApesEdit)it.next();
		        it.remove();
		    }

		    //postEdit
			extraEdits.addAll(restoreEdits());
			postEdit(edit, extraEdits);
		}
		else restoreEdits();
	}

	/**
	 * Creates edits for each moved activities in moves 
	 * 
	 * @param moves the map containing element/new parent pairs
	 * @param extras
	 * @return a stored map wich will be returned by the ApesModelEvent
	 */
	protected Collection moveActivitiesInModel(Map moves, Map extras)
	{
		if(moves == null)
			return null;
		
	    Vector activities = new Vector();
	    Vector newParents = new Vector();
	    
	    Iterator it = moves.entrySet().iterator();
	    while(it.hasNext())
	    {
	        Map.Entry entry = (Map.Entry) it.next();
	        if(entry.getKey() instanceof Activity && entry.getValue() instanceof ApesWorkDefinition)
	        {
	        	boolean duplicateName = false;
	        	ApesWorkDefinition wd = (ApesWorkDefinition)entry.getValue();
	        	Activity a = (Activity)entry.getKey();
	        	//test if there is not already an element with the same name in the work definition
	        	int i = 0;
	        	while(i<wd.subWorkCount() && !duplicateName)
	        	{
	        		if(wd.getSubWork(i).getName().equals(a.getName()))
	        		{
	        			duplicateName = true;
	        		}
	        		i++;
	        	}
	        	
	        	if(!duplicateName)
	        	{
	        		activities.add(entry.getKey());
	        		newParents.add(entry.getValue());
	        	}
	        }
	    }
	    
	    Vector edits = new Vector();
	    ApesEdit edit = createRemoveModelEdit(activities.toArray(),extras);
	    if(edit != null)
	    {
	    	edit.end();
	    	edits.add(edit);
	    	edit = createInsertModelEdit(activities.toArray(), newParents.toArray(), extras);
	    	edit.end();
	    	edits.add(edit);
	    }
	    
        return edits;
	}
	
	/**
	 * Moves source or target of a link in a diagram
	 * 
	 * @param diagram the diagram
	 * @param moves a map containing old link/new link pairs
	 * @param extra a stored map wich will be returned by the ApesModelEvent
	 */
	protected void moveLinkInDiagram(SpemDiagram diagram, Map moves, Map extras) 
	{
		mStorePostEdits = true;
		
		Iterator it = moves.entrySet().iterator();
		while(it.hasNext())
		{
		    Map.Entry entry = (Map.Entry)it.next();
		    Link oldLink = (Link)entry.getKey();
		    Link newLink = (Link)entry.getValue();
		    
		    //checks the data
		    if(oldLink.getSource() instanceof ModelElement && oldLink.getTarget() instanceof ModelElement
		    		&& newLink.getSource() instanceof ModelElement && newLink.getTarget() instanceof ModelElement)
		    {
		    	//removes the old link before tests if the new link can be created
		    	//because dependencies can be found beetween the two links which abort the creation of the new link
		    	removeFrom(diagram, new Object[]{oldLink}, extras);
		    
		    	//tests if the old link has been removed from the diagram
		    	//and tests if the new link can be created
		    	if(!diagram.existsLinkModelElements((ModelElement)oldLink.getSource(), (ModelElement)oldLink.getTarget())
		    			&& diagram.areLinkableModelElements((ModelElement)newLink.getSource(), (ModelElement)newLink.getTarget()))
			    {
			    	insertIn(diagram, new Object[]{newLink}, extras);
			    }
		    	else
		    	{
		    		//backup changes made the removal of the old link
		    		Vector edits = restorePostEdits();
		    		if(edits != null && !edits.isEmpty())
		    		{
		    			for (it = edits.iterator(); it.hasNext();) 
		    			{
							ApesEdit edit = (ApesEdit) it.next();
							if(edit.canUndo())
							{
								edit.undo();
							}
						}
		    		}
		    	}
		    }
		}
		
		Vector edits = restorePostEdits();
		Collections.reverse(edits);
		
		if(edits != null && !edits.isEmpty())
		{
			//create an empty ApesEdit which will contained all edits
			ApesEdit edit = new ApesModelEdit(null,null,null,null,null,null);
			postEdit(edit, edits);
		}
	}

	/**
	 * Compounds each undoable edits in the ApesEdit
	 * Calls the postEdit method of super class if mStorePostEdits is true
	 * 
	 * @param edit
	 * @param undoableEdits
	 */
	protected void postEdit(ApesEdit edit, Collection undoableEdits)
	{
		for (Iterator iter = undoableEdits.iterator(); iter.hasNext();) 
		{
		    edit.addEdit((UndoableEdit)iter.next());
		}    	
		edit.end();
    	if(mStorePostEdits)
    	{
    		mPostEdits.add(edit);
    	}
    	else
    	{
       		postEdit(edit);
    	}
	}
	
	/**
	 * Restores the edits previously save since mStorePostEdits has been changed to true
	 * And affects false in mStorePostEdits
	 * 
	 * @return the vector containing the previously saved edits
	 */
	private Vector restorePostEdits()
	{
		Vector temp = mPostEdits;
		mPostEdits = new Vector();
		mStorePostEdits = false;
		return temp;
	}
	
	/**
	 * Convenience method to call the save method of the undo manager
	 *
	 */
	private void saveEdits()
    {
        Context.getInstance().getUndoManager().save();
    }
    
	/**
	 * Convenience method to call the restore method of the undo manager
	 * 
	 * @return the vector containing the stored edits 
	 */
    private Vector restoreEdits()
    {
    	return Context.getInstance().getUndoManager().restore();
    }
    
	/**
	 * Returns an edit which represents an insert.
	 * 
	 * @param elements the elements to insert
	 * @param parents the parents of each elements
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesModelEdit representing the insert or null
	 */
	protected ApesModelEdit createInsertModelEdit(
	        	Object[] elements,
	        		Object[] parents,
	          			Map extras)
	{
		if(elements == null || elements.length == 0)
			return null;
	    ApesModelEdit edit = 
	        createModelEdit(elements, null, null, parents, extras);
		return edit;
	}

	/**
	 * Returns an edit wich represents the remove
	 * 
	 * @param elements the elements to remove
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesModelEdit representing the remove or null
	 */
	protected ApesModelEdit createRemoveModelEdit(Object[] elements, Map extras) 
	{
		if(elements == null || elements.length == 0)
			return null;
	    ApesModelEdit edit = 
	    	createModelEdit(null, elements, null, getParents(elements), extras);
		return edit;
	}
	
	/**
	 * Creates a model edit
	 * 
	 * @param inserted
	 * @param removed
	 * @param changed
	 * @param parents
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return a ApesModelEdit representing the changes or null
	 */
	protected ApesModelEdit createModelEdit( 
	        	Object[] inserted,
	        		Object[] removed,
	        			Map changed, 
	        				Object[] parents,
	        					Map extras)
	{
	    return new ApesModelEdit( inserted, removed, changed, parents, null, extras);
	}
	
	/**
	 * Returns an edit which represents an insert in a diagram
	 *  
	 * @param source the source
	 * @param elements the elements to insert
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesDiagramEdit representing the changes
	 */
	protected ApesDiagramEdit createInsertDiagramEdit(
							SpemDiagram source,
								Object[] elements,									
									Map extras)
	{
		ApesDiagramEdit edit = 
			createDiagramEdit(source, elements, null, null, extras);
		return edit;
	}
	
	/**
	 * Returns an edit wich represents a remove in a diagram
	 * @param source the source
	 * @param elements the elements to remove
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesDiagramEdit representing the changes
	 */
	protected ApesDiagramEdit createRemoveDiagramEdit(
			SpemDiagram source,
				Object[] elements,									
					Map extras)
	{
		if(elements == null || elements.length == 0) 
			return null;
		ApesDiagramEdit edit = 
			createDiagramEdit(source, null, elements, null, extras);
		return edit;
	}
	
	/**
	 * Creates an ApesDiagramEdit
	 * 
	 * @param source
	 * @param inserted
	 * @param removed
	 * @param changed
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesDiagramEdit representing the change
	 */
	protected ApesDiagramEdit createDiagramEdit(
							SpemDiagram source,
								Object[] inserted,									
									Object[] removed,
										Map changed,
											Map extras)
	{
		return new ApesDiagramEdit(source, inserted, removed,changed, extras);
	}
	
	/**
	 * Returns a change edit representing changes in the model
	 * 
	 * @param change
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesModelEdit representing the changes
	 */
	protected ApesModelEdit createChangeEdit(Map change, Map extras)
	{
		return new ApesModelEdit(null, null, change,null, null, extras);
	}
	
	/**
	 * Returns an ApesModelEdit representing the moves of elements in the model
	 * @param moves
	 * @param extras a stored map wich will be returned by the ApesModelEvent
	 * @return an ApesModelEdit representing the changes
	 */
	protected ApesModelEdit createMoveEdit(Map moves, Map extras)
	{
		return new ApesModelEdit(null, null, null, null, moves, extras);
	}
	
	/**
	 * Inserts elements into the model
	 * 
	 * @param elements the elements to insert
	 * @param parents the parents of each elements
	 * @param diagram a map to store the entry diagram-graph adapter
	 * @return an array of objects to undo the changes
	 */
	protected Object[] handleInsertInModel(Object[] elements, Object[] parents, Map diagram) 
	{
	    if (elements == null || parents == null)
		    return null;
		
	    if(Debug.enabled)
	    {
	    	String mess = "[ ";
	    	for ( int i = 0; i < parents.length; i++ )
			{
				mess+=elements[i]+"/"+parents[i]+", ";
			}
	    	Debug.print(Debug.MEDIATOR, "(ApesMediator) -> handleInsertInModel("+mess+"] )");
	    }
	    		
		Vector result = new Vector();
		for (int i = 0; i < elements.length; i++)
		{
			if(elements[i] instanceof ModelElement && ((ModelElement)elements[i]).getParent() == null
		            && parents.length > i && parents[i] != null && parents[i] instanceof IPackage)
		    {
		        if(((IPackage)parents[i]).addModelElement((ModelElement)elements[i]))
		        {
		        	result.add(elements[i]);
		        }
		    }
		    
		    if(elements[i] instanceof SpemDiagram)
		    {
		    	mDiagrams.add(elements[i]);
		    	if(!diagram.containsKey(elements[i]))
		    	{
		    		diagram.put(elements[i], Context.getInstance().getProject().getGraphModel((SpemDiagram)elements[i]));
		    	}
		    	else
		    	{
		    		Context.getInstance().getProject().setGraphModel((SpemDiagram)elements[i],(SpemGraphAdapter)diagram.get(elements[i]));
		    	}
		    }
		}
		return result.isEmpty() ? null : result.toArray();
	}

	/**
	 * Removes elements from the model
	 * 
	 * @param elements the elements to remove
	 * @param @param diagram a map to store the entry diagram-graph adapter 
	 * @return an array of objects to undo the changes
	 */
	protected Map handleRemoveFromModel(Object[] elements, Map diagram) 
	{
	    Map removed = new HashMap();
		if (elements != null)
		{		    
		    if(Debug.enabled)
		    {
		    	String mess = "[ ";
		    	for ( int i = 0; i < elements.length; i++ )
				{
					mess+=elements[i]+", ";
				}
		    	Debug.print(Debug.MEDIATOR, "(ApesMediator) -> handleRemoveFromModel("+mess+"])");
		    }

		    for (int i = 0; i < elements.length; i++)
	        {
	        	if(elements[i] instanceof ModelElement)
		        {
	        		ModelElement me = (ModelElement)elements[i];
		            if(me.getParent() != null)
		            {
		            	Object parent = me.getParent();
		            	if(me.getParent().removeModelElement(me))
		            		removed.put(me, parent);
		            }
		        }
	        	
	        	if(elements[i] instanceof SpemDiagram)
	        	{
	        		mDiagrams.remove(elements[i]);
	        		if(!diagram.containsKey(elements[i]))
			    	{
	        			diagram.put(elements[i], Context.getInstance().getProject().getGraphModel((SpemDiagram)elements[i]));
			    	}
			    	Context.getInstance().getProject().removeGraphModel((SpemDiagram)elements[i]);
	        	}
	        }    
		}
	    
		return removed.isEmpty() ? null : removed;
	}

	/**
	 * Inserts elements into the source
	 * @param source
	 * @param elements
	 * @return an array of objects to undo the changes
	 */
	protected Object[] handleInsertInDiagram(SpemDiagram source, Object[] elements) 
	{
	    if(elements == null) 
	    	return null;
	    
	    if(Debug.enabled)
	    {
	    	String mess="[ ";
	    	for ( int i = 0; i < elements.length; i++ )
			{
				mess+=elements[i]+", ";
			}
	    	Debug.print(Debug.MEDIATOR, "(ApesMediator) -> handleInsertInDiagram("+source+":"+mess+"])");
	   	}
		
	    Vector inserted = new Vector();
	    for ( int i = 0; i < elements.length; i++ )
		{
    		if(elements[i] instanceof ModelElement)
	    	{
	    		ModelElement me = (ModelElement)elements[i];
	    		if(source.addModelElement(me))
	    		{
	    			inserted.add(me);
	    		}
	    	}
	    	else if(elements[i] instanceof Link)
	    	{
	    		Link link = (Link) elements[i];
	    		if(link.getSource() instanceof ModelElement
	    			&& link.getTarget() instanceof ModelElement
					&& source.createLinkModelElements((ModelElement)link.getSource(), (ModelElement)link.getTarget()))
	    		{
	    			inserted.add(0, link);	    		
	    		}
	    	}		
		}
	    return inserted.isEmpty() ? null : inserted.toArray();
	}

	/**
	 * Removes elements from the source
	 * 
	 * @param source
	 * @param elements
	 * @return an array of objects to undo the changes
	 */
	protected Object[] handleRemoveFromDiagram(SpemDiagram source, Object[] elements) 
	{
	    List removed = new ArrayList();
		if (elements != null)
		{
		    if(Debug.enabled)
		    {
		    	String mess="[ ";
		    	for ( int i = 0; i < elements.length; i++ )
				{
					mess+=elements[i]+", ";
				}
		    	Debug.print(Debug.MEDIATOR, "(ApesMediator) -> handleRemoveFromDiagram( "+source+":"+mess+")");
		    }
		    
		    for (int i = 0; i < elements.length; i++)
		    {
		    	if(elements[i] instanceof ModelElement)
		    	{
		    		ModelElement me = (ModelElement)elements[i];
		    		if(source.removeModelElement(me))
		    		{
		    			removed.add(0, me);
		            }
		    	}
		        else if(elements[i] instanceof Link)
		        {
		        	Link link = (Link)elements[i];
		        	if(link.getSource() instanceof ModelElement
			    			&& link.getTarget() instanceof ModelElement
							&& source.removeLinkModelElements((ModelElement)link.getSource(),(ModelElement)link.getTarget()))
		        	{
		        		removed.add(link);
		        	}
		        }
		    }
		}
		return removed.isEmpty()? null : removed.toArray();
	}

	/**
	 * Changes elements of the model
	 * 
	 * @param changes the map representing the changes
	 * @return a map to undo the changes
	 */
	protected Map handleChange(Map changes) 
	{
	    if(changes == null) return null;

	    if(Debug.enabled)
	    {
	    	Debug.print(Debug.MEDIATOR, "(ApesMediator) -> handleChange( "+changes+" )");
	    }

	    Map undo = new HashMap();
	    Iterator it = changes.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry entry = (Map.Entry)it.next();
			if(entry.getKey() instanceof ModelElement && entry.getValue() instanceof String)
			{
				ModelElement me = (ModelElement)entry.getKey();
				IPackage parent = me.getParent();
				
				//remove the element before rename it
				if(parent != null)
				{
					parent.removeModelElement(me);
				}
				
				//rename the element and add the undo to the map
				undo.put(me, me.getName());
				me.setName((String)entry.getValue());

				//if it's not possible to readd the element (due to duplicated names) rollback to previous state 
				if(parent != null && !parent.addModelElement(me))
				{
					me.setName((String)undo.remove(me));
					parent.addModelElement(me);					
					ErrorManager.getInstance().printKey("errorDuplicatedName");
				}
				
				if(me instanceof SpemDiagram)
				{
					Context.getInstance().graphNameChanged(Context.getInstance().getProject().getGraphModel((SpemDiagram)me),me.getName());
				}
			}
		}
		return undo.isEmpty()? null : undo;	    
	}
	
	/**
	 * Moves elements 
	 * 
	 * @param moves a map representing the move
	 * @return a map to undo the changes
	 */
	protected Map handleMove(Map moves)
	{
	    if(moves == null) return null;

		if(Debug.enabled) 
		{
			Debug.print(Debug.MEDIATOR, "(ApesMediator) -> handleMove( "+moves+" )");
		}
		
	    Map undo = new HashMap();
	    Iterator it = moves.entrySet().iterator();
		while(it.hasNext())
		{
			Map.Entry entry = (Map.Entry)it.next();
			if(entry.getKey() instanceof ModelElement && entry.getValue() instanceof IPackage)
			{
				ModelElement me = (ModelElement)entry.getKey();
				IPackage parent = (IPackage)entry.getValue();
				IPackage oldParent = me.getParent();
				
				if(oldParent != parent)
				{
					oldParent.removeModelElement(me);
					if(parent.addModelElement(me))
					{
						undo.put(me, oldParent);
					}
					else
					{
						oldParent.addModelElement(me);
						ErrorManager.getInstance().printKey("errorMoveElement");
					}
				}
			}
		}
		return undo.isEmpty()? null : undo;
	}
	
	/**
	 * Moves an element in the model
	 * 
	 * @param me the model element to moved
	 * @param parent the new parent of the model element
	 * @param undo the map which can undo the change
	 */
	protected void handleMoveInModel(ModelElement me, IPackage parent, Map undo) 
	{
		IPackage oldParent = me.getParent();
		
		if(oldParent != parent)
		{
			oldParent.removeModelElement(me);
			if(parent.addModelElement(me))
			{
				undo.put(me, oldParent);
			}
			else
			{
				oldParent.addModelElement(me);
				ErrorManager.getInstance().printKey("errorMoveElement");
			}
		}
	}

	/**
	 * Notify all listeners that the model has changed
	 * 
	 * @param source
	 * @param edit the edit representing the change
	 */
	protected void fireModelChanged(
		Object source,
		ApesModelEvent.ApesModelChange edit) 
	{
	    if(Debug.enabled) Debug.print(Debug.MEDIATOR, "(ApesMediator) -> fireModelChanged "+edit);
	    if(source == null) source = this;
	    // Guaranteed to return a non-null array
		Object[] listeners = mListenerList.getListenerList();
		ApesModelEvent e = null;
		// Process the listeners last to first, notifying
		// those that are interested in this event
		for (int i = listeners.length - 2; i >= 0; i -= 2) {
			if (listeners[i] == ApesModelListener.class) {
				// Lazily create the event:
				if (e == null)
					e = new ApesModelEvent(this, edit);
				((ApesModelListener) listeners[i + 1]).modelChanged(e);
			}
		}
	}
	
	static private int msCounter = 0;
	public abstract class ApesEdit extends CompoundEdit implements ApesModelEvent.ApesModelChange
	{
	    private int mId = msCounter++;
	    
	    protected Object[] mInsert, mRemove;
	    protected Map mChange, mExtras;

	    protected boolean mExecuteRedoEditsFirst = true;
	    protected boolean mExecuteUndoEditsFirst = true;
	    
		public ApesEdit(Object[] inserted, 
							Object[] removed,
								Map changed,
									Map extras )
		{
			super();
			mInsert = inserted;
			mRemove = removed;
			mChange = changed;
			mExtras = extras;
		}
		
		public Map getExtras() 
		{
			return mExtras;
		}

		public Map getChanged() 
		{
			return mChange;
		}
		
		public Object[] getInserted() 
		{
			return mRemove;			
		}
		
		public Object[] getRemoved() 
		{
			return mInsert;
		}
		
		public boolean  isSignificant() 
		{
			return true;
		}
		
		public void redo() throws CannotRedoException 
		{
		    if(Debug.enabled) Debug.print(Debug.MEDIATOR, "\n(ApesModelEdit) -> redo "+edits.size());
			if(mExecuteRedoEditsFirst)
			{
				super.redo();
				execute();
			}
			else
			{
				execute();
				super.redo();				
			}
		}

		public void undo() throws CannotUndoException 
		{
		    if(Debug.enabled) Debug.print(Debug.MEDIATOR, "\n(ApesModelEdit) -> undo "+edits.size());
		    if(mExecuteUndoEditsFirst)
			{
		    	super.undo();
				execute();
			}
			else
			{
				execute();
				super.undo();				
			}
		}
		
		protected Object[] getEdits()
		{
			return edits.toArray();
		}   
	       
		public abstract boolean execute();
		
		public String toString()
	    {
			String result = getClass().toString()+"/"+mId+"[ ";
	        if(mRemove != null)
	        	result+=", Inserted/"+mRemove.length;
	        if(mInsert != null)
	        	result+=", Removed/"+mInsert.length;
	        if(mChange != null)
	        	result+=", Changed/"+mChange;
	        result+=", Extras/"+mExtras;
	        return result;
	    }
	}
	
    public class ApesModelEdit extends ApesEdit
	{
	    protected Object[] mParents;
	    protected Map mMove;
	    protected Map mDiagram = new HashMap();
		
		public ApesModelEdit( Object[] inserted, 
		        					Object[] removed,
		        						Map changed,
		        							Object[] parents,
												Map move,
													Map extras )
		{
		    super(inserted, removed, changed, extras);
		    mParents = parents;
		    mMove = move;
		    if(inserted != null)
		    {
		    	mExecuteUndoEditsFirst = true;
		    	mExecuteRedoEditsFirst = false;
		    }
		    else if(removed != null)
		    {
		    	mExecuteUndoEditsFirst = false;
		    	mExecuteRedoEditsFirst = true;
		    }
		}
				
		public Object[] getParents()
		{
		    return mParents;
		}
		
		public Collection getMoved()
		{
			if(mMove != null)
			{
				return mMove.keySet();
			}
			return null;
		}
		
		public Object getOldParent(Object element)
		{
			if(mMove != null)
			{
				return mMove.get(element);
			}
			return null;
		}
		
		public Object getNewParent(Object element)
		{
			if(element instanceof ModelElement)
			{
				return ((ModelElement)element).getParent();
			}
			return null;	
		}
		
		public Object getSource()
		{
			return null;
		}
		
		public boolean execute() 
		{
		    if(Debug.enabled) Debug.print(Debug.MEDIATOR, "(ApesModelEdit) -> execute");
		    
		    Object[] inserted = mInsert;
		    Object[] removed = mRemove;
		    Map changed = mChange;
		    Map moved = mMove;
		    
			mRemove = handleInsertInModel(inserted, mParents, mDiagram);
			Map insert = handleRemoveFromModel(removed, mDiagram);
			mInsert = insert == null ? null : insert.keySet().toArray();
			if(insert != null) mParents = insert.values().toArray();
			mChange = handleChange(changed);
		    mMove = handleMove(moved);
		    
			return mRemove != null || mInsert != null || mChange != null 
				|| (mMove != null && !mMove.isEmpty()) || edits.size()>0;		
		}
       
       public String toString()
       {
           String result = super.toString();
           if(mParents != null)
               result+=", Parents/"+mParents.length;
           return result+"]";
       }
	}

    public class ApesDiagramEdit extends ApesEdit
	{
    	protected SpemDiagram mSource;
    	
    	public ApesDiagramEdit( SpemDiagram source,
    			Object[] inserted, 
					Object[] removed,
						Map changed,
							Map extras )
    	{
    		super(inserted, removed, changed, extras);
    		mSource = source;
    	}
    	
    	public Object getSource() 
		{
			return mSource;
		}
		
        public Object[] getParents()
        {
        	return null;
        }
        
        public Collection getMoved()
        {
        	return null;
        }
        
        public Object getOldParent(Object element)
        {
        	return null;
        }
        
        public Object getNewParent(Object element)
        {
        	return null;
        }
        
    	public boolean execute() 
		{
		    if(Debug.enabled) Debug.print(Debug.MEDIATOR, "(ApesDiagramEdit) -> execute");
		    
		    Object[] inserted = mInsert;
		    Object[] removed = mRemove;
		    Map changed = mChange;
			mRemove = handleInsertInDiagram(mSource, inserted);
			mInsert = handleRemoveFromDiagram(mSource, removed);			
		    mChange = handleChange(changed);
		    
			return mRemove != null || mInsert != null || mChange != null || edits.size()>0;
		}
    	
    	public String toString()
        {
            String result = super.toString();
            if(mSource != null)
                result+=", Source/"+mSource;
            return result+"]";
        }
	}
}
