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
package org.ipsquad.apes.model.frontend;

import java.io.Serializable;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Vector;

import javax.swing.undo.AbstractUndoableEdit;
import javax.swing.undo.UndoableEditSupport;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Identity;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;

/**
 * 
 * @version $Revision: 1.1 $
 */
public class ApesMediator extends UndoableEditSupport implements Serializable
{
	private static final ApesMediator mInstance = new ApesMediator();
	
	private Vector mListeners = new Vector();
	private Vector mDiagrams = new Vector();
	
	private ApesMediator() { }
	
	public static ApesMediator getInstance()
	{
		return mInstance;
	}
	
	public void setDiagram( Vector diagrams )
	{
		mDiagrams = diagrams;
	}
	
	public void registerDiagram( SpemDiagram diagram )
	{
		if( !mDiagrams.contains(diagram) )
		{
			mDiagrams.add(diagram);
		}
	}
	
	public void setProcess(ApesProcess ap)
	{
		if( ap.getComponent() != null || ap.getProvidedInterface() != null || ap.getRequiredInterface() != null )
		{
			if(ap.getComponent() == null)
			{
				ModelElement me = new ProcessComponent("Component");
				
				ap.addModelElement(me);
				fireModelUpdated(new InsertEvent(me,ap,null));

				me = new ContextDiagram("Context Diagram");
				Context.getInstance().getProject().getGraphModel((SpemDiagram)me);
				
				ap.getComponent().addModelElement(me);
				fireModelUpdated(new InsertEvent(me,ap.getComponent(),null));
			
				loadProvidedInterface(ap);
				loadRequiredInterface(ap);
			}
			
			loadProcess(ap);
		}
		else
		{
			initNewProcess(ap);
		}
	}
	
	private void loadProvidedInterface(ApesProcess ap)
	{
		WorkProductRef ref = null;
		ModelElement me = null;
		ProcessComponent c = ap.getComponent();
		
		if( ap.getProvidedInterface() == null )
		{	
			me = new ApesProcess.ProvidedInterface("provided");
			ap.addModelElement(me);
			fireModelUpdated(new InsertEvent(me,ap,null));
		}
		else
		{
			SpemDiagram diag = (SpemDiagram)ap.getComponent().getModelElement(0);
			Map link = new HashMap();
			
			int i = ap.getProvidedInterface().modelElementCount()-1;
			int index = 0;
			while( i >= 0 )
			{
				ref = (WorkProductRef)ap.getProvidedInterface().getModelElement(index);
				me = ref.getReference();
				ap.getProvidedInterface().removeModelElement(ref);
				update(createInsertCommandToSpemDiagram(diag,me,null));
				update(createInsertCommandToSpemDiagram(diag,c,me,null));
				--i;
			}
		}
	}
	
	private void loadRequiredInterface(ApesProcess ap)
	{
		WorkProductRef ref = null;
		ModelElement me = null;
		ProcessComponent c = ap.getComponent();
		
		if( ap.getRequiredInterface() == null )
		{	
			me = new ApesProcess.RequiredInterface("required");
			ap.addModelElement(me);
			fireModelUpdated(new InsertEvent(me,ap,null));
		}
		else
		{
			SpemDiagram diag = (SpemDiagram)ap.getComponent().getModelElement(0);
			Map link = new HashMap();
			
			int i = ap.getRequiredInterface().modelElementCount()-1;
			int index = 0;
			while( i >= 0 )
			{
				ref = (WorkProductRef)ap.getRequiredInterface().getModelElement(index);
				me = ref.getReference();
				ap.getRequiredInterface().removeModelElement(ref);
				update(createInsertCommandToSpemDiagram(diag,me,null));
				update(createInsertCommandToSpemDiagram(diag,me,c,null));
				--i;
			}
		}
	}
	
	private void initNewProcess( ApesProcess ap )
	{
		ModelElement me = new ProcessComponent("Component");
		
		ap.addModelElement(me);
		fireModelUpdated(new InsertEvent(me,ap,null));

		me = new ContextDiagram("Context Diagram");
		ap.getComponent().addModelElement(me);
		fireModelUpdated(new InsertEvent(me,ap.getComponent(),null));
		Context.getInstance().getProject().getGraphModel((SpemDiagram)me);
		
		me = new ApesProcess.ProvidedInterface("provided");
		ap.addModelElement(me);
		fireModelUpdated(new InsertEvent(me,ap,null));
		
		me = new ApesProcess.RequiredInterface("required");
		ap.addModelElement(me);
		fireModelUpdated(new InsertEvent(me,ap,null));		
	}
	
	private void loadProcess( IPackage parent )
	{
		for( int i = 0; i < parent.modelElementCount(); i++ )
		{	
			fireModelUpdated(new InsertEvent(parent.getModelElement(i),parent,null));
			if( parent.getModelElement(i) instanceof IPackage )
			{
				loadProcess((IPackage)parent.getModelElement(i));
			}
		}
	}
	
	/**
	 * Adds a listener for the Mediator event posted after the model changes
	 * 
	 * @param l the listener to add
	 * @return true if the listener can be added, false otherwise
	 */
	public boolean addApesMediatorListener( Listener l )
	{
		if( !mListeners.contains( l ) )
		{
			mListeners.add( l );
			return true;
		}
		return false;
	}
	
	/**
	 * Removes a listener previously added with addApesMediatorListener
	 * 
	 * @param l the listener to add
	 * @return true if the listener can be added, false otherwise
	 */
	public boolean removeApesMediatorListener( Listener l )
	{
		return mListeners.remove( l );
	}

	public void clearListeners()
	{
		mListeners.clear();
	}
	
	public void clearAll()
	{
		mListeners.clear();
		mDiagrams.clear();
	}
	
	protected void fireModelUpdated( Event e )
	{
		for( int i = 0; i < mListeners.size(); i++ )
		{
			((Listener) mListeners.get( i )).updated( e );
		}
	}
	
	/**
	 * Return a command that represents an insert
	 * 
	 * @param to_insert the object to insert in the model
	 * @param parent the parent of the inserted object
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents an insert
	 */
	public Command createInsertCommand( Object to_insert, Object parent, Map attr )
	{
		return new InsertCommand( null, to_insert, null, null, parent, attr );
	}
	
	/**
	 * Return a command that represents an insert
	 * 
	 * @param diagram the diagram containing the object to insert
	 * @param to_insert the object to insert
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents an insert
	 */
	public Command createInsertCommandToSpemDiagram( SpemDiagram diagram, Object to_insert, Map attr )
	{
		return new InsertCommand( diagram, to_insert, null, null, null, attr );
	}
	
	/**
	 * Return a command that represents an insert of a link
	 * 
	 * @param diagram  the diagram containing the object to insert
	 * @param source the source of the transition
	 * @param target the target of the transition
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents an insert
	 */
	public Command createInsertCommandToSpemDiagram( SpemDiagram diagram, Object source, Object target, Map attr )
	{
		return new InsertCommand( diagram, null, source, target, null, attr );
	}
	
	/**
	 * Return a command that represents a remove
	 * 
	 * @param to_remove the objects to remove from the model
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents a remove
	 */
	public Command createRemoveCommand( Object[] to_remove, Map attr )
	{
		return new RemoveCommand( null, to_remove, null, null, attr );
	}
	
	/**
	 * Return a command that represents a remove
	 * 
	 * @param diagram the diagram containing the objects to remove
	 * @param to_remove the objects to remove
	 * @param sources the sources of the links to remove
	 * @param targets the targets of the links to remove
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents a remove
	 */
	public Command createRemoveCommand( SpemDiagram diagram, Object[] to_remove, Object[] sources, Object[] targets, Map attr )
	{
		return new RemoveCommand( diagram, to_remove, sources, targets, attr );
	}
	
	/**
	 * Return a command that represents a change
	 *  
	 * @param to_change the object which change
	 * @param newValue the new value of the object
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents a remove
	 */
	public Command createChangeCommand( Object to_change, String newValue, Map attr )
	{
		return new ChangeCommand( to_change, newValue, attr );
	}
	
	/**
	 * Return a command that represents a move
	 * 
	 * @param to_move the object to move
	 * @param newParent the new parent of the object
	 * @param attr a map containing informations passing by the object which calls the ApesMediator.
	 * @return a command that represents a remove
	 */
	public Command createMoveCommand( Object to_move, Object newParent, Map attr )
	{
		return new MoveCommand( to_move, newParent, attr );
	}
	
	/**
	 * Call this function when you want to modify the model
	 * 
	 * @param c the command representing a change to do in the model
	 */
	public void update( Command c )
	{
		if( c instanceof InsertCommand )
		{
			insert( (InsertCommand) c,false );
		}
		else if( c instanceof RemoveCommand )
		{
			remove( (RemoveCommand) c,false );
		}
		else if( c instanceof MoveCommand )
		{
			move( (MoveCommand) c,false );
		}
		else if( c instanceof ChangeCommand )
		{
			change( (ChangeCommand) c,false );
		}
	}
	
	protected InsertEvent insertModelElementToDiagram( SpemDiagram diagram, ModelElement me, Map attr )
	{
		boolean isAlreadyExist = me.getParent() == null ? false : true;

		if( diagram.addModelElement( me ) )
		{
			if( me.getParent() == null )
			{
				if( diagram instanceof FlowDiagram && ( me instanceof WorkProduct || me instanceof ProcessRole ) )
				{
					((ModelElement)diagram.getParent()).getParent().addModelElement( me );
				}
				else
				{
					diagram.getParent().addModelElement( me );
				}
			}

			return new InsertEvent( diagram, me, me.getParent(), isAlreadyExist, attr );
		}

		return null;
	}
	
	protected void insertModelElementToIPackage( ModelElement me, IPackage p, Map attr, Vector events )
	{
		if( !p.containsModelElement( me ) && me.getParent() == null )
		{
			p.addModelElement(me);
			events.add( new InsertEvent( me, p, attr ) );
		}
	}
	
	protected void insertElementToModel( Object parent, Element e, Map attr, Vector events )
	{
		if( ( e instanceof Activity
				|| e instanceof FlowDiagram
				|| e instanceof ActivityDiagram )
			&& !( parent instanceof ApesWorkDefinition ) ) 
		{
			return;
		}
		
		if( parent instanceof IPackage && e instanceof ModelElement )
		{
			insertModelElementToIPackage((ModelElement)e, (IPackage)parent, attr, events);
		}
	}
	
	protected void insertLinkToDiagram( SpemDiagram diagram, ModelElement source, ModelElement target, 
											   Map attr, Vector events )
	{
		if( diagram.areLinkableModelElements(source, target))
		{	
			diagram.createLinkModelElements(source, target);
			
			if( diagram instanceof ActivityDiagram )
			{
				events.add( new InsertEvent( diagram, ((ActivityDiagram)diagram).getTransition(source, target), null, true, attr ) );
				return;
			}
			else if( diagram instanceof ContextDiagram )
			{
				WorkProductRef ref;
				ApesProcess ap = Context.getInstance().getProject().getProcess();
				if( source instanceof WorkProduct )
				{
					ref = new WorkProductRef((WorkProduct)source);
					ap.getRequiredInterface().addModelElement(ref);
					events.add( new InsertEvent( ref, ref.getParent(), null ) );
				}
				else
				{
					ref = new WorkProductRef((WorkProduct)target);
					ap.getProvidedInterface().addModelElement(ref);
					events.add( new InsertEvent( ref, ref.getParent(), null ) );				
				}
			}
			
			Map link = new HashMap();
			link.put( source, target );
			events.add( new InsertEvent( diagram, link, attr ) );
		}
	}
	
	protected void insertModelElementToDiagram( SpemDiagram diagram, ModelElement me, Map attr, Vector events )
	{
		boolean isAlreadyExist = me.getParent() == null ? false : true;

		if( diagram.addModelElement( me ) )
		{
			if( me.getParent() == null )
			{
				if( diagram instanceof FlowDiagram && ( me instanceof WorkProduct || me instanceof ProcessRole ) )
				{
					((ModelElement)diagram.getParent()).getParent().addModelElement( me );
				}
				else
				{
					diagram.getParent().addModelElement( me );
				}
			}

			events.add(new InsertEvent( diagram, me, me.getParent(), isAlreadyExist, attr ));
		}
	}
	
	protected InsertEvent insert( SpemDiagram diagram, Object element, 
								  Object source, Object target, Object parent, Map attr, 
								  Vector events )
	{
		//System.out.println("ApesMediator::insert "+diagram+" "+element+" "+parent);
		if( element != null )
		{
			// add an element in a diagram
			if( diagram != null  && element instanceof ModelElement )
			{
				insertModelElementToDiagram( diagram, (ModelElement)element, attr, events );
			}
			// add a modelElement in a package
			else if( element instanceof Element )
			{
				insertElementToModel( parent, (Element) element, attr, events );				
			}
		}
		// add a transition beetween two elements in a diagram
		if( source != null && target != null 
				&& source instanceof ModelElement && target instanceof ModelElement )
		{
			insertLinkToDiagram( diagram, (ModelElement)source, (ModelElement)target, attr, events );
		}
		
		return null;
	}
	
	/**
	 * Insert new element or link into the model according to the InsertCommand.
	 * Emit an insertEvent if the model changed.
	 * 
	 * @param c the command which contains the element or link to insert
	 */
	protected void insert( InsertCommand c, boolean linkedEvent )
	{
		//System.out.println("Mediator::insert "+c);
		Vector events = new Vector();
		insert( c.getDiagram(), c.getElement(), c.getSource(), c.getTarget(), c.getParent(), c.getAttributes(), events );
		
		if( events.size() > 0 )
		{
			for( int i = events.size()-1; i >= 0; i-- )
			{	
				Context.getInstance().getUndoManager().save();
				
				InsertEvent event = (InsertEvent) events.get(i);
				fireModelUpdated( event );
				InsertedUndo edit = new InsertedUndo( event.getDiagram(), event.getInserted(), event.getSource(), event.getTarget(), (ModelElement)event.getParent(), Context.getInstance().getUndoManager().restore() );
				edit.setIsChained( i == events.size()-1 ? linkedEvent : true );
				postEdit( edit );
			}
		}
	}
	
	/**
	 * Move an element of the model according to the MoveCommand.
	 * Emit a MoveEvent if the model changed.
	 * 
	 * @param c the command which contains the element to move
	 */
	protected void move( MoveCommand c, boolean chainedEdit )
	{
		//System.out.println("Mediator::move "+c);
		Context.getInstance().getUndoManager().save();
			
		MoveEvent e = move( c.getElement(), c.getNewParent(), c.getAttributes() );
		if( e != null )
		{
			fireModelUpdated( e );
		 	
			MovedUndo edit = new MovedUndo( (ModelElement)c.getElement(), (IPackage)e.getOldParent(), (IPackage)e.getNewParent(), Context.getInstance().getUndoManager().restore());
			edit.setIsChained(chainedEdit);
				
			postEdit( edit );
		}
		else
		{
			Context.getInstance().getUndoManager().restore();
		}
	}

	protected MoveEvent move( Object element, Object newParent, Map attr )
	{
		if( element instanceof Activity || element instanceof FlowDiagram || element instanceof ActivityDiagram )
		{
			return null;
		}
		
		ModelElement me = null;
		int oldIndex = -1, newIndex = -1;
		IPackage p_new = null , p_old = null;
		
		// move ModelElement into another Package
		if ( element instanceof ModelElement  
			&& ((ModelElement) element).getParent() instanceof IPackage
			&& newParent instanceof IPackage )
		{
			if( element instanceof ApesProcess.Interface
				|| element instanceof WorkProductRef
				|| newParent instanceof ApesProcess
				|| newParent instanceof ApesProcess.Interface )
			{
				ErrorManager.getInstance().println( ResourceManager.getInstance().getString("errorMoveElement"));
				return null;
			}
			
			me = (ModelElement) element;
			p_new = (IPackage) newParent;
			p_old = me.getParent();
					
			if( ( p_new instanceof SPackage && p_old instanceof ApesWorkDefinition )
				|| ( p_new instanceof ApesWorkDefinition && (p_old instanceof SPackage || p_old instanceof ApesProcess.Interface)) ) 
			{
				return null;
			}
			
			oldIndex = getIndexOfChild( p_old , me );
			
			p_old.removeModelElement(me);
			p_new.addModelElement(me);
			
			newIndex = getIndexOfChild( p_new , me );
			
			return new MoveEvent( me, p_old, oldIndex, p_new, newIndex, attr );
		}
		
		return null;
	}
	
	/**
	 * Call this function when a diagram is destroy an you want to delete the links it contains
	 * 
	 * @param diagram
	 * @param sources
	 * @param targets
	 */
	public void removeDiagramLinksAfterDeleted( SpemDiagram diagram, Object[] sources, Object[] targets )
	{
		for( int i = 0; i < sources.length; i++ )
		{
			diagram.removeLinkModelElements( (ModelElement) sources[i], (ModelElement) targets[i] );
		}
		
		postEdit( new RemovedUndo( diagram, null, sources, targets, null, null ) );
	}
	
	/**
	 * Remove elements or links from the model according to the RemoveCommand.
	 * Emit a removeEvent if the model changed.
	 * 
	 * @param c the command which contains the elements or links to remove
	 */
	
	protected boolean removeLinksFromDiagram( SpemDiagram diagram, ModelElement source, ModelElement target, Vector events )
	{
		if( diagram.existsLinkModelElements( source, target ) )
		{
			diagram.removeLinkModelElements( source, target );
			
			if( diagram instanceof ContextDiagram )
			{
				Map parent = new HashMap();
				Map index = new HashMap();
				ApesProcess ap = Context.getInstance().getProject().getProcess();
				WorkProductRef toRemove;

				if( source instanceof WorkProduct )
				{
					toRemove = ap.getRequiredInterface().getWorkProductRef((WorkProduct)source);
					if( toRemove != null )
					{	
						parent.put(toRemove, toRemove.getParent());
						index.put(toRemove, new Integer(getIndexOfChild(toRemove.getParent(),toRemove)));
						toRemove.getParent().removeModelElement(toRemove);
						events.add(new RemoveEvent( new Object[]{toRemove}, null,null,parent,index,null));
					}
				}
				else
				{
					toRemove = ap.getProvidedInterface().getWorkProductRef((WorkProduct)target);
					if( toRemove != null )
					{	
						parent.put(toRemove, toRemove.getParent());
						index.put(toRemove, new Integer(getIndexOfChild(toRemove.getParent(),toRemove)));
						toRemove.getParent().removeModelElement(toRemove);
						events.add(new RemoveEvent( new Object[]{toRemove}, null,null,parent,index,null));				}
					}
			}
			return true;
		}
		return false;
	}
	
	protected void removeFromDiagram( SpemDiagram diagram, Object[] elements, Object[] sources, Object[] targets, Map attr, Vector events )
	{
		HashSet removeElements = new HashSet();
		Vector removedSources = new Vector(),
				removedTargets = new Vector();

		if( sources != null )
		{
			for( int i = 0; i < sources.length; i++ )
			{
				if( removeLinksFromDiagram( diagram, (ModelElement) sources[i], (ModelElement) targets[i], events ) )
				{
					removedSources.add(sources[i]);
					removedTargets.add(targets[i]);
				}
			}
		}
		
		if( elements != null )
		{
			for( int i = 0; i < elements.length; i++ )
			{
				if( elements[i] instanceof ModelElement )
				{
					ModelElement me = (ModelElement) elements[i];
					
					if( diagram.containsModelElement( me ) )
					{
						for( int u = 0; u < diagram.modelElementCount(); u++ )
						{
							if( removeLinksFromDiagram( diagram, me, diagram.getModelElement( u ), events ) )
							{
								removedSources.add(me);
								removedTargets.add(diagram.getModelElement( u ));
							}
							if( removeLinksFromDiagram( diagram, diagram.getModelElement( u ) , me, events ) )
							{
								removedSources.add(diagram.getModelElement( u ));
								removedTargets.add(me);
							}	
						}
						
						if( diagram.removeModelElement( me ) )
						{	
							removeElements.add(me);
						}
					}
				}
			}
		}

		if( removeElements.size() > 0 || removedSources.size() > 0 )
		{	
			events.add(new RemoveEvent( diagram, removeElements.toArray(), removedSources.toArray(), removedTargets.toArray(), attr ) );
		}
	}
	
	protected void removeElementFromDiagrams( Element element, Vector events )
	{
		for( int i = 0; i < mDiagrams.size(); i++ )
		{
			removeFromDiagram( (SpemDiagram) mDiagrams.get(i), new Object[]{element}, null, null, null, events );
		}
	}
	
	protected boolean removeElementFromModel( Element element, Map parent, Map index )
	{
		ModelElement me;
		
		if( element instanceof ModelElement )
		{
			me = (ModelElement) element;
			
			if( me.getParent() != null )
			{
				parent.put( me, me.getParent() );
				index.put( me, new Integer(getIndexOfChild( me.getParent() , me)));
				me.getParent().removeModelElement( me );
				
				if( me instanceof SpemDiagram )
				{
					SpemDiagram diagram = (SpemDiagram) me;
					parent.put("Diagram"+((Element)diagram).getID(), Context.getInstance().getProject().getGraphModel( diagram ));
					Context.getInstance().getProject().removeGraphModel( diagram );
					mDiagrams.remove( diagram );
				}		
				return true;
			}
		}
		
		return false;
	}
	
	protected void removeElementsFromModel( Object[] elements, Map attr, Vector events )
	{
		ModelElement me = null;
		Map parents = new HashMap(),
				indices = new HashMap();
		Vector removedElements = new Vector();
		
		for( int i = 0; i < elements.length; i++ )
		{	
			if( elements[i] instanceof IPackage )
			{
				IPackage pack = (IPackage) elements[i];
				
				if( pack.modelElementCount() > 0 )
				{	
					Vector temp = new Vector();
					for( int j = 0; j < pack.modelElementCount(); j++ )
					{
						temp.add(pack.getModelElement(j));
					}
					
					removeElementsFromModel( temp.toArray(), null, events );
				}
			}
			
			if( elements[i] instanceof Element && !( elements[i] instanceof WorkProductRef ) )
			{
				if( removeElementFromModel( (Element)elements[i], parents, indices ) )
				{
					removeElementFromDiagrams( (Element)elements[i], events);
					removedElements.add(elements[i]);				
				}
			}
		}
		
		if( removedElements.size() > 0 )
		{
			events.add(new RemoveEvent(removedElements.toArray(), null, null, parents, indices, attr ));
		}		
	}
	
	protected void remove( SpemDiagram diagram, Object[] elements,  Object[] sources, Object[] targets, Map attr, Vector events )
	{
		if( diagram == null )
		{
			removeElementsFromModel( elements, attr, events );
		}
		else
		{
			removeFromDiagram( diagram, elements, sources, targets, attr, events );
		}
	}
	
	protected void remove( RemoveCommand c, boolean linkedEvent )
	{
		//System.out.println("Mediator::remove "+c);	
		Vector events = new Vector();
		
		remove( c.getDiagram(), c.getElements(), c.getSources(), c.getTargets(), c.getAttributes(), events );
		
		if( events != null )
		{
			RemoveEvent e;
			for( int i = 0; i < events.size(); i++ )
			{	
				Context.getInstance().getUndoManager().save();
				
				e = (RemoveEvent) events.get(i);
				
				fireModelUpdated( e );
			
				RemovedUndo edit = new RemovedUndo( e.getDiagram(), e.getElements(), e.getSources(), e.getTargets(), e.getParents(),  Context.getInstance().getUndoManager().restore() );
				edit.setIsChained( i == 0 ? linkedEvent : true );
							
				postEdit( edit );
			}
		}
	}
	
	/**
	 * Change the name of an element or link into the model according to the ChangeCommand.
	 * Emit an ChangeEvent if the model changed.
	 * 
	 * @param c the command which contains the element or link to rename
	 */
	protected void change( ChangeCommand c, boolean chainedEdit )
	{
		//System.out.println("mediator edit "+c);
		Vector events = new Vector();
		change( c.getElement(), c.getNewValue(), c.getAttributes(), events );
		
		ChangeEvent event = null;
		for( int i = events.size()-1; i >= 0; i-- )
		{
			event = (ChangeEvent)events.get(i);
			
			Context.getInstance().getUndoManager().save();
			
			fireModelUpdated( event );
			
			ChangedUndo edit = new ChangedUndo( event.getElement(), event.getOldValue(), event.getNewValue(), Context.getInstance().getUndoManager().restore() );
			edit.setIsChained( i == events.size()-1 ? chainedEdit : true );
			
			postEdit( edit );
		}
	}
	
	protected void change( Object to_change, String newValue, Map attr, Vector events )
	{
		if( to_change instanceof WorkProductRef )
		{
			return;
		}

		if( to_change instanceof Element && newValue != null && !newValue.equals("") )
		{
			Element element = (Element) to_change;
			
			if( isDuplicatedName( element, newValue ) )
			{
				ErrorManager.getInstance().println( ResourceManager.getInstance().getString("errorDuplicatedName"));
				return;
			}
			else
			{
				if( element instanceof WorkProduct )
				{		
					if(((WorkProduct)element).getReferences() != WorkProduct.NO_REFERENCES )
					{
						changeWorkProductName((WorkProduct)element, newValue );
					}
					
					changeWorkProductState((WorkProduct)element, events);
				}

				String oldName = element.getName();
				element.setName( newValue );
				
				events.add( new ChangeEvent( element, oldName, newValue, attr ) );
			}
		}
		else if( to_change instanceof ActivityDiagram.Transition )
		{
			ActivityDiagram.Transition t = (ActivityDiagram.Transition)to_change;
			if( ! t.getLabel().equals( newValue ) )
			{
				String oldName = t.getLabel();
				t.setLabel( newValue );
				
				events.add( new ChangeEvent( t, oldName, newValue, attr ) );
			}
		}
	}
	
	protected void changeWorkProductState( WorkProduct w, Vector events )
	{
		for(int i = 0; i < w.behaviorCount(); i++ )
		{
			events.add(new ChangeEvent(w.getBehavior(i), w.getBehavior(i).getName(), w.getBehavior(i).getName(), null) );
		}
	}
	
	protected void changeWorkProductName( WorkProduct w, String newValue )
	{
		String reference = ResourceManager.getInstance().getString("reference");
		ApesProcess ap = Context.getInstance().getProject().getProcess();
		
		WorkProductRef ref = ap.getProvidedInterface().getWorkProductRef(w);
		
		if( ref != null )
		{
			ref.setName(reference+newValue);
		}
		else
		{
			ref = ap.getRequiredInterface().getWorkProductRef(w);
			ref.setName(reference+newValue);
		}
	}
	
	/**
	 * Find the index of a child
	 *
	 * @param parent the container
	 * @param child the child to evaluate
	 * @return the index of the child or -1 if child is not in parent
	 */
	public int getIndexOfChild( Object parent, Object child )
	{
		if(parent instanceof ApesProcess.Interface && child instanceof WorkProductRef )
		{
			ApesProcess.Interface in = (ApesProcess.Interface)parent;
			WorkProductRef ref = (WorkProductRef)child;
			
			for( int i = 0; i < in.modelElementCount(); i++ )
			{
				if( ref.getReference() == ((WorkProductRef)in.getModelElement(i)).getReference() )
				{
					return i;
				}
			}
		}
		
		if(parent instanceof IPackage)
		{
			IPackage p = (IPackage) parent;

			for(int i=0; i<p.modelElementCount(); i++)
			{
				if(p.getModelElement(i)==child)
				{
					return i;
				}
			}
		}
		
		return -1;
	}

	/**
	 * Check if element can have the new name passing in parameter
	 * 
	 * @param e the element which wants to change its name
	 * @param value the new name
	 * @return true if the new name is valid, false otherwise
	 */
	protected boolean isDuplicatedName(Element e, String value)
	{
		if(e instanceof ModelElement)
		{
			ModelElement me = (ModelElement) e;
			IPackage p = me.getParent();

			if( p == null )
			{
				return false;
			}
			
			for(int i=0; i<p.modelElementCount(); i++)
			{
				if(p.getModelElement(i).getName().equals(value))
				{
					return true;
				}
			}
		}
		
		return false;
	}
	
	/**
	 * Get a child of a container by giving its index
	 *
	 * @param parent the container
	 * @param index the index of the child
	 * @return the chil or null if it does not exists
	 */
	public Object getChild(Object parent, int index)
	{
		if(parent instanceof IPackage)
		{
			IPackage p = (IPackage) parent;

			return p.getModelElement(index);
		}

		return null;
	}
	
	/**
	 * Get the number of child of a container
	 *
	 * @param parent the container
	 * @return the number of child
	 */
	public int getChildCount(Object parent)
	{
		if(parent instanceof IPackage)
		{
			IPackage p = (IPackage) parent;

			return p.modelElementCount();
		}

		return 0;
	}
	
	public Object findByID( int id )
	{
		ApesProcess ap = Context.getInstance().getProject().getProcess();
		return findByID( id, ap );
	}
	
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
	 * Defines the interface for an object that listens to changes in a ApesMediator
	 */
	public static interface Listener
	{
		public void updated( Event e );
	}
	
	protected abstract class Command implements Serializable
	{
		private Map mAttributes;
		
		public Command( Map attr )
		{
			mAttributes = attr;
		}
	
		public Map getAttributes() { return mAttributes; }
	}
	
	protected class InsertCommand extends Command
	{
		private SpemDiagram mDiagram;
		private Object mElement;
		private Object mSource, mTarget;
		private Object mParent;
		
		protected InsertCommand( SpemDiagram diagram, Object element, Object source, Object target, Object parent, Map attr )
		{	
			super( attr );
			mDiagram = diagram;
			mElement = element;
			mSource= source;
			mTarget = target;
			mParent = parent;
		}
		
		public SpemDiagram getDiagram() { return mDiagram; }
		public Object getElement() { return mElement;	}
		public Object getSource() { return mSource; }
		public Object getTarget() { return mTarget; }
		public Object getParent() { return mParent;	}
		
		public String toString()
		{
			String result = "element "+mElement;
			result += " parent "+ mParent;
			return result;
		}
	}
	
	protected class MoveCommand extends Command
	{
		private Object mElement;
		private Object mNewParent;
		
		protected MoveCommand( Object element, Object newParent, Map attr )
		{	
			super( attr );
			mElement = element;
			mNewParent = newParent;
		}
		
		public Object getElement() { return mElement; }
		public Object getNewParent() {	return mNewParent; }

		public String toString()
		{
			String result = "element "+mElement;
			result += " newparent "+ mNewParent;
			return result;
		}
	}
	
	protected class RemoveCommand extends Command
	{
		private Object[] mElements;
		private Object[] mSources, mTargets;
		private SpemDiagram mDiagram;
		
		protected RemoveCommand( SpemDiagram diagram, Object[] elements, Object[] sources, Object[] targets, Map attr )
		{
			super( attr );
			mDiagram = diagram;
			mElements = elements;
			mSources = sources;
			mTargets = targets;
		}
		
		public SpemDiagram getDiagram(){ return mDiagram; }
		public Object[] getElements(){ return mElements; }
		public Object[] getSources() { return mSources; }
		public Object[] getTargets() { return mTargets; }
		public Object getSource(int i)
		{
			if( i >=0 && i < mSources.length )
			{
				return mSources[i];
			}
			return null;
		}
		public Object getTarget(int i)
		{
			if( i >=0 && i < mTargets.length )
			{
				return mTargets[i];
			}
			return null;
		}
		public String toString()
		{
			String result = "concerned ";
			for( int i = 0; i < mElements.length; i++ )
				result+=mElements[i]+" , ";
			result += " diagram "+ mDiagram;
			return result;
		}
	}
	
	protected class ChangeCommand extends Command
	{		
		private Object mElement;
		private String mNewValue = "";
		
		public String toString()
		{
			return "element "+mElement+" newValue "+mNewValue;
		}
		
		protected ChangeCommand( Object element, String newValue, Map attr )
		{
			super( attr );
			mElement = element;
			mNewValue = (String) newValue;
		}
		
		public Object getElement() {	return mElement; }
		public String getNewValue() { return mNewValue; }
	}
	
	public class UndoableEdit extends AbstractUndoableEdit
	{
		private boolean mIsChained = false;
		private Vector mExtraEdits = new Vector();
	
		protected UndoableEdit( Vector extraEdits )
		{
			if( extraEdits != null )
			{
				mExtraEdits = extraEdits;
			}	
		}
		
		public void setIsChained( boolean isChained )
		{
			mIsChained = isChained;
		}
		
		public boolean getIsChained()
		{
			return mIsChained;
		}
		
		public void setExtraEdits( Vector extraEdits )
		{
			mExtraEdits = extraEdits;
		}
		
		public void addExtraEdits( Vector extraEdits )
		{
			mExtraEdits.addAll( extraEdits );
		}
		
		protected Vector getExtraEdits()
		{
			return mExtraEdits;
		}
		
		public void undoExtraEdits()
		{
			for( int i = 0; i < mExtraEdits.size(); i++ )
			{
				((AbstractUndoableEdit)mExtraEdits.get(i)).undo();
			}
		}
		
		public void redoExtraEdits()
		{
			for( int i = 0; i < mExtraEdits.size(); i++ )
			{
				((AbstractUndoableEdit)mExtraEdits.get(i)).redo();
			}
		}
	}
	
	protected class InsertedUndo extends UndoableEdit
	{
		private SpemDiagram mDiagram;
		private Element mElement = null;
		private Object mSource, mTarget;
		private ModelElement mParent;
		private SpemGraphAdapter mAdapter;
		
		public String toString()
		{
			return "insertUndo diagram "+mDiagram+" element "+mElement+
						" source "+mSource+" target "+mTarget+" parent "+mParent;
		}
		
		public InsertedUndo( SpemDiagram diagram, Object element, Object source, Object target, ModelElement parent, Vector extraEdits )
		{
			super( extraEdits );
			mDiagram = diagram;
			if( element instanceof Element )
			{	
				mElement = (Element) element;
			}
			mSource = source;
			mTarget = target;
			
			if( mElement instanceof SpemDiagram )
			{
				mAdapter = Context.getInstance().getProject().getGraphModel( (SpemDiagram)mElement);
			}
			mParent = parent;
		}
		
		public void undo()
		{
			super.undo();
			super.undoExtraEdits();
			
			remove( mDiagram, 
							mElement != null ? new Object[]{ mElement } : null, 
							mSource != null ? new Object[]{ mSource } : null, 
							mTarget != null ? new Object[]{ mTarget } : null,
							null, new Vector() );
		}
		
		public void redo()
		{
			super.redo();
			
			insert( mDiagram, mElement, mSource, mTarget, mParent, null, new Vector() );
			
			if( mElement instanceof SpemDiagram )
			{
				Context.getInstance().getProject().setGraphModel( (SpemDiagram)mElement, mAdapter );
			}
			
			super.redoExtraEdits();
		}
	}
	
	protected class RemovedUndo extends UndoableEdit
	{
		private SpemDiagram mDiagram;
		private Object[] mRemovedElements;
		private Object[] mSources, mTargets;
		private Map mParents;
		
		public RemovedUndo( SpemDiagram diagram, Object[] removedElements, Object[] sources, Object[] targets, Map parents, Vector extraEdits )
		{
			super( extraEdits );
			mRemovedElements = removedElements;
			mDiagram = diagram;
			mParents = ( parents != null ? parents : new HashMap() );
			mSources = sources;
			mTargets = targets;
		}
		
		public String toString()
		{
			String result = "RemovedUndo : diag "+mDiagram+" elements ";
			if( mRemovedElements != null )
			for( int i = 0;i<mRemovedElements.length; i++ )
				result+=mRemovedElements[i]+" ";
			result+=" \n Sources targets : ";
			if( mSources != null )
			for( int i = 0;i<mSources.length; i++ )
			{	
				result+=mSources[i]+" "+mTargets[i]+"\n";
			}
			result+="parents "+mParents;
			return result;
		}
		
		public void undo()
		{
			super.undo();
			
			for( int i = 0; i < mRemovedElements.length; i++ )
			{
				insert( mDiagram, mRemovedElements[i], null, null, mParents.get(mRemovedElements[i]),	null, new Vector() );
					
				if( mRemovedElements[i] instanceof SpemDiagram )
				{
					Context.getInstance().getProject().setGraphModel( (SpemDiagram)mRemovedElements[i], (SpemGraphAdapter)mParents.get("Diagram"+((Element)mRemovedElements[i]).getID()));
					for( int j = 0; j < ((SpemDiagram)mRemovedElements[i]).modelElementCount(); j++ )
						((SpemDiagram)mRemovedElements[i]).getModelElement(j);
				}
			}
			
			if( mSources != null )
			{
				for( int i = 0; i < mSources.length; i++ )
				{
					insert( mDiagram, null, mSources[i], mTargets[i], null,	null, new Vector() );
				}
			}
			
			super.undoExtraEdits();
		}
		
		public void redo()
		{
			super.redo();
			super.redoExtraEdits();
			
			remove( mDiagram, mRemovedElements, mSources, mTargets, null, new Vector() );
		}
	}
	
	protected class MovedUndo extends UndoableEdit
	{
		private IPackage mOldParent;
		private IPackage mNewParent;
		private ModelElement mElement;
		
		public String toString()
		{
			return "element "+mElement+" oldParent "+mOldParent+" newParent "+mNewParent;
		}
		
		public MovedUndo( ModelElement element, IPackage oldParent, IPackage newParent, Vector extraEdits )
		{
			super( extraEdits );
			mElement = element;
			mOldParent = oldParent;
			mNewParent = newParent;
		}
		
		public void undo()
		{
			super.undo();
			//System.out.println("undoMove "+this);
			
			int index = getIndexOfChild( mNewParent, mElement );
			
			super.undoExtraEdits();
			
			mNewParent.removeModelElement( mElement );
			mOldParent.addModelElement( mElement );
		}
		
		public void redo()
		{
			super.redo();
			super.redoExtraEdits();
			
			mOldParent.removeModelElement( mElement );
			mNewParent.addModelElement( mElement );
		}
	}
	
	protected class ChangedUndo extends UndoableEdit
	{
		private Object mElement;
		private String mOldValue;
		private String mNewValue;;
		
		public String toString()
		{
			return "element "+mElement+" oldValue "+mOldValue+" newValue "+mNewValue;
		}
		
		public ChangedUndo( Object element, String oldValue, String newValue, Vector extraEdits )
		{
			super( extraEdits );
			mElement = element;
			mOldValue = oldValue;
			mNewValue = newValue;
		}
		
		protected boolean changeRef( ApesProcess.Interface in, String name )
		{
			ApesProcess ap = Context.getInstance().getProject().getProcess();
			WorkProduct w = (WorkProduct) mElement;
			
			WorkProductRef ref = in.getWorkProductRef(w);
				
			if( ref != null )
			{
				ref.setName(ResourceManager.getInstance().getString("reference")+name);
			}
			
			return false;
		}
		
		public void undo()
		{
			super.undo();
			//System.out.println("undoChanged oldval "+mNewValue+" newval "+mOldValue);
			if( mElement instanceof WorkProduct && ((WorkProduct)mElement).getReferences() != WorkProduct.NO_REFERENCES )
			{
				if( ! changeRef(Context.getInstance().getProject().getProcess().getProvidedInterface(), mOldValue) )
				{	
					changeRef(Context.getInstance().getProject().getProcess().getRequiredInterface(), mOldValue);
				}
			}
			
			if( mElement instanceof Element )
			{
				((Element)mElement).setName( mOldValue );
			}
			else if( mElement instanceof ActivityDiagram.Transition )
			{
				((ActivityDiagram.Transition)mElement).setLabel( mOldValue );
			}

			super.undoExtraEdits();
		}
		
		public void redo()
		{
			super.redo();
			//System.out.println("redoChanged oldval "+mOldValue+" newval "+mNewValue);
			if( mElement instanceof WorkProduct )
			{
				if( ! changeRef(Context.getInstance().getProject().getProcess().getProvidedInterface(), mNewValue) )
				{	
					changeRef(Context.getInstance().getProject().getProcess().getRequiredInterface(), mNewValue);
				}
			}
			
			if( mElement instanceof Element )
			{
				((Element)mElement).setName( mNewValue );
			}
			else if( mElement instanceof ActivityDiagram.Transition )
			{
				((ActivityDiagram.Transition)mElement).setLabel( mNewValue );
			}
		
			super.redoExtraEdits();
		}
	}
}
