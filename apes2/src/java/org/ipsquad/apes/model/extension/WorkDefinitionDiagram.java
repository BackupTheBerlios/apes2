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

package org.ipsquad.apes.model.extension;

import java.io.Serializable;
import java.util.Vector;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.ErrorManager;

/**
 * Base class for the work definition diagram
 *
 * @version $Revision: 1.18 $
 */
public class WorkDefinitionDiagram extends SpemDiagram {

	private Vector mElements = new Vector();
	private Vector mTransitions = new Vector();
	
	public WorkDefinitionDiagram()
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++WorkDefinitionDiagram");
	}

	public WorkDefinitionDiagram( String name )
	{
		super(name);
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++WorkDefinitionDiagram::"+name);
	}
	
	/**
	 * Return the link between a WorkDefinition and a WorkProduct
	 * @param source the WorkDefinition or the WorkProduct which start the link
	 * @param target the  WorkDefinition or the WorkProduct which end the link
	 * @return the transition between those elements, null if there is no link between those elements
	 */
	public Transition getTransition(ModelElement source, ModelElement target)
	{
		for(int i=0;i<mTransitions.size();i++)
		{
			if((((Transition)mTransitions.get(i)).getInputModelElement()==source) && ((Transition)mTransitions.get(i)).getOutputModelElement()==target)
			{
				return (Transition)mTransitions.get(i);
			}
		}
		
		return null;
	}
	
	
	/**
	 * Get a transition by giving its index
	 * @param i the index of the presentation element to retrieve
	 * @return the transition or null if the index is invalid
	 */
	public Transition getTransition(int i)
	{
		if(i<0 || i>=mTransitions.size())
		{
			return null;
		}

		return (Transition) mTransitions.get(i);
	}


	/**
	 * Get the number of transitions in this diagram
	 *
	 * @return the number of transitions
	 */
	public int getTransitionCount()
	{
		return mTransitions.size();
	}
	

	/**
	 * Add a model element to the WorkDefinition diagram
	 *
	 * @param me the model element to associate
	 * @return true if the model element can be added, false otherwise
	 */
	public boolean addModelElement(ModelElement me) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::addModelElement "+me);
		if( me instanceof ProcessRole )
		{
			return addProcessRole( (ProcessRole) me );
		}
		else if( me instanceof WorkProduct )
		{
			return addWorkProduct( (WorkProduct) me );
		}
		else if( me instanceof WorkDefinition )
		{
			return addWorkDefinition( (WorkDefinition) me );
		}
		else if(me instanceof StateMachine)
		{
			return addWorkProductState((StateMachine)me);
		}
		ErrorManager.getInstance().printKey("errorElementForbidden");
		return false;
	}

	/**
	 * Add a role to the work defintion diagram
	 *
	 * @param r the process role to associate
	 * @return true if the process role can be added, false otherwise
	 */
	public boolean addProcessRole(ProcessRole r)
	{
		if(!containsModelElement(r))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::addProcessRole "+r);
			mElements.add(r);
			return true;
		}
		ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
		return false;
	}

	/**
	 * Add a work product to the work definition diagram
	 *
	 * @param p the work product to associate
	 * @return true if the work product can be added, false otherwise
	 */
	public boolean addWorkProduct(WorkProduct p)
	{
		if(!containsModelElement(p))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::addWorkProduct "+p);
			mElements.add(p);
			return true;
		}
		ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
		return false;
	}
	
	/**
	 * Add a work product state to the work definition diagram
	 *
	 * @param s the work product state to associate
	 * @return true if the work product can be added, false otherwise
	 */
	public boolean addWorkProductState(StateMachine sm)
	{
		/*if( containsModelElement(sm.getContext()) )
		{
			ErrorManager.getInstance().printKey("errorStateContextPresentInDiagram");
			return false;
		}*/
		
		if(!containsModelElement(sm))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::addWorkProductState "+sm);
			mElements.add(sm);
			return true;
		}
		
		ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
		return false;
	}
	
	
	
	/**
	 * Add a work definition to the work definition diagram
	 *
	 * @param p the work definition to associate
	 * @return true if the work definition can be added, false otherwise
	 */
	public boolean addWorkDefinition(WorkDefinition p)
	{
		if(!containsModelElement(p))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::addWorkDefinition "+p);
			mElements.add(p);
			return true;
		}
		ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
		return false;
	}

	/**
	 * Remove a model element from this diagram
	 *
	 * @param e the model element to remove
	 * @return true if the model element can be removed, false otherwise
	 */
	public boolean removeModelElement(ModelElement e) 
	{
		if(containsModelElement(e))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeModelElement "+e);
			mElements.remove(e);
			return true;
		}

		return false;
	}

	public boolean canAddModelElement(ModelElement me) 
	{
		if( me instanceof ProcessRole || me instanceof WorkDefinition)
		{
			if(!containsModelElement(me))
			{
				return true;
			}
			else
			{
				ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
				return false;
			}
		}
		
		if( me instanceof WorkProduct )
		{
			return canAddWorkProduct((WorkProduct)me);
		}
		
		if( me instanceof StateMachine )
		{
			return canAddStateMachine((StateMachine)me);
		}
		
		ErrorManager.getInstance().printKey("errorElementForbidden");
		
		return false;
	}
	
	public boolean canAddWorkProduct( WorkProduct p )
	{
		if( !containsModelElement(p) )
		{	
			for( int i = 0; i < p.behaviorCount(); i++ )
			{
				if( mElements.contains(p.getBehavior(i)) )
				{
					ErrorManager.getInstance().printKey("errorStateContextPresentInDiagram");
					return false;
				}
			}
			return true;
		}
		ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
		return false;
	}
	
	public boolean canAddStateMachine( StateMachine sm )
	{
		if( containsModelElement(sm.getContext()) )
		{
			ErrorManager.getInstance().printKey("errorStateContextPresentInDiagram");
			return false;
		}
		return true;
	}
	
	

	/**
	 * Check if a model element is in this diagram
	 *
	 * @param e the model element to test
	 * @return true if the model element is in this diagram, false otherwise
	 */
	public boolean containsModelElement(ModelElement e) 
	{
		return mElements.contains(e);
	}

	/**
	 * Get a model element by its index
	 *
	 * @param i the model element index
	 * @return the model element or null if the index is invalid
	 */
	public ModelElement getModelElement(int i)
	{
		if(i<0 || i>=mElements.size())
		{
			return null;
		}
		
		return (ModelElement) mElements.get(i);
	}

	/**
	 * Get the number of model elements in this diagram
	 *
	 * @return the number of model elements contained in this diagram
	 */
	public int modelElementCount() 
	{
		return mElements.size();
	}

	public boolean createLinkModelElements( ModelElement source, ModelElement target) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::createLinkModelElements "+source+" "+target);
		if( source instanceof ProcessRole && target instanceof WorkDefinition )
		{
			return createLinkProcessRoleWorkDefinition( (ProcessRole) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkProduct && target instanceof WorkDefinition )
		{
			return createLinkWorkProductWorkDefinition( (WorkProduct) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof WorkProduct )
		{
			return createLinkWorkDefinitionWorkProduct( (WorkDefinition) source, (WorkProduct) target );
		}
		
		if( source instanceof StateMachine && target instanceof WorkDefinition )
		{
			return createLinkWorkProductStateWorkDefinition( (StateMachine) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof StateMachine )
		{
			return createLinkWorkDefinitionWorkProductState( (WorkDefinition) source, (StateMachine) target );
		}
		
		ErrorManager.getInstance().printKey("errorNotLinkableElements");
		return false;
	}

	/**
	 * Create a link between a process role and a work definition
	 *
	 * @param r the Process Role to be linked
	 * @param w the Work Definition to be linked
	 * @return true if the link can be created, false otherwise
	 */
	public boolean createLinkProcessRoleWorkDefinition(ProcessRole r, WorkDefinition w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (!r.containsFeature(w) && w.getOwner()==null)
			{
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::createLinkProcessRoleWorkDefinition "+r+" "+w);
				r.addFeature(w);
				w.setOwner(r);
				
				/*for(int i=0; i < w.subWorkCount(); i++)
				{
					Activity a = w.getSubWork(i); 
					if( a.getOwner() == null && !r.containsFeature(a) )
					{	
						r.addFeature(a);
						a.setOwner(r);
					}
				}*/
				return true;
			}
		}

		return false;
	}
	
	/**
	 * Create a link between a work product in input and a work definition
	 *
	 * @param r the Work Product to be linked
	 * @param w the Work Definition to be linked in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean createLinkWorkProductWorkDefinition(WorkProduct wp, WorkDefinition wd)
	{
		if(containsModelElement(wp))
		{
			if(containsModelElement(wd))
			{
				if(areLinkableModelElements(wp,wd))
				{
					if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::createLinkWorkProductWorkDefinition "+wp+" "+wd);
					mTransitions.add(new Transition(wp,wd));
					return true;
				}
			}
		}
		return false;
	}
	
	/**
	 * Create a link between a work product in output and a work definition
	 *
	 * @param r the Work Product to be linked
	 * @param w the Work Definition to be linked in output
	 * @return true if the link can be created, false otherwise
	 */
	public boolean createLinkWorkDefinitionWorkProduct(WorkDefinition wd, WorkProduct wp)
	{
		if(containsModelElement(wd))
		{
			if(containsModelElement(wp))
			{
				if(areLinkableModelElements(wd,wp))
				{
					if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::createLinkWorkDefinitionWorkProduct "+wd+" "+wp);
					mTransitions.add(new Transition(wd,wp));
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean createLinkWorkProductStateWorkDefinition( StateMachine sm, WorkDefinition wd)
	{		
		if( sm.getContext() instanceof WorkProduct 
				&& containsModelElement(sm) 
				&& containsModelElement(wd) )
		{
			WorkProduct w = (WorkProduct)sm.getContext();
			

			if((!existsLinkWorkProductWorkDefinition(w,wd)) && (!existsLinkWorkDefinitionWorkProductState(wd,sm)))

			{	
				mTransitions.add(new Transition(w,wd));
				mTransitions.add(new Transition(sm,wd));
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::createLinkWorkProductStateWorkDefinition "+sm+" "+wd);
				return true;
			}
		}
		return false;
	}
	
	
	public boolean createLinkWorkDefinitionWorkProductState(WorkDefinition wd,  StateMachine sm)
	{
		if( sm.getContext() instanceof WorkProduct 
				&& containsModelElement(sm) 
				&& containsModelElement(wd) )
		{
			WorkProduct w = (WorkProduct)sm.getContext();

			if((!existsLinkWorkDefinitionWorkProduct(wd,w)) && (!existsLinkWorkProductStateWorkDefinition(sm,wd)))

			{
				mTransitions.add(new Transition(wd,w));
				mTransitions.add(new Transition(wd,sm));
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::createLinkWorkDefinitionWorkProductState "+wd+" "+sm);
				return true;
			}
		}
		return false;
	}
	
	
	
	public boolean removeLinkModelElements( ModelElement source, ModelElement target) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeLinkModelElements "+source+" "+target);
		if( source instanceof ProcessRole && target instanceof WorkDefinition )
		{
			return removeLinkProcessRoleWorkDefinition( (ProcessRole) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkProduct && target instanceof WorkDefinition )
		{
			return removeLinkWorkProductWorkDefinition( (WorkProduct) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof WorkProduct )
		{
			return removeLinkWorkDefinitionWorkProduct((WorkDefinition) source, (WorkProduct) target);
		}
		
		if( source instanceof StateMachine && target instanceof WorkDefinition )
		{
			return removeLinkWorkProductStateWorkDefinition( (StateMachine) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof StateMachine )
		{
			return removeLinkWorkDefinitionWorkProductState((WorkDefinition) source, (StateMachine) target);
		}

		return false;
	}

	/**
	 * Remove a link between a process role and an work definition
	 *
	 * @param r the Process Role to be unlinked
	 * @param w the Work Definition to be unlinked
	 * @return true if the link can be removed, false otherwise
	 */
	public boolean removeLinkProcessRoleWorkDefinition(ProcessRole r, WorkDefinition w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (r.removeFeature(w))
			{
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeLinkProcessRoleWorkDefinition "+r+" "+w);
				w.setOwner(null);
				/*for(int i = 0; i < w.subWorkCount(); i++)
				{
					Activity a = w.getSubWork(i);
					if(r.removeFeature(a))
					{
						a.setOwner(null);
					}
				}*/
				return true;
			}
		}
		
		return false;
	}
	
	/**
	 * Remove a link with a work product in input of a work definition
	 *
	 * @param w the work product to be unlinked
	 * @param a the work definition to be unlinked in input
	 * @return true if the link can be removed, false otherwise
	 */
	public boolean removeLinkWorkProductWorkDefinition(WorkProduct wp, WorkDefinition wd)
	{
		if(existsLinkModelElements(wp, wd))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeLinkWorkProductWorkDefinition "+wp+" "+wd);
			mTransitions.remove(getTransition(wp,wd));
			return true;
		}
		
		return false;	
	}
	
	/**
	 * Remove a link with a work product in output of a work definition
	 *
	 * @param w the work product to be unlinked
	 * @param a the work definition to be unlinked in input
	 * @return true if the link can be removed, false otherwise
	 */
	public boolean removeLinkWorkDefinitionWorkProduct(WorkDefinition wd, WorkProduct wp)
	{
		if(existsLinkModelElements(wd, wp))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeLinkWorkDefinitionWorkProduct "+wd+" "+wp);
			mTransitions.remove(getTransition(wd,wp));
			return true;
		}
		
		return false;	
	}
	
	
	
	public boolean removeLinkWorkProductStateWorkDefinition(StateMachine sm, WorkDefinition wd)
	{
		if (sm.getContext() instanceof WorkProduct
				&& containsModelElement(wd) 
				&& containsModelElement(sm))
		{
			WorkProduct w = (WorkProduct)sm.getContext();
			
			if(existsLinkModelElements(w, wd))
			{
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeLinkWorkProductStateWorkDefinition "+sm+" "+wd);
				mTransitions.remove(getTransition(w,wd));
				mTransitions.remove(getTransition(sm,wd));
				return true;
			}
		}
		return false;
	}
	
	
	public boolean removeLinkWorkDefinitionWorkProductState(WorkDefinition wd, StateMachine sm)
	{
		if (sm.getContext() instanceof WorkProduct
				&& containsModelElement(wd) 
				&& containsModelElement(sm))
		{
			WorkProduct w = (WorkProduct)sm.getContext();
			
			if(existsLinkModelElements(wd, w))
			{
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkDefinitionDiagram("+getName()+")::removeLinkWorkDefinitionWorkProductState "+wd+" "+sm);
				mTransitions.remove(getTransition(wd,sm));
				mTransitions.remove(getTransition(wd,w));
				mTransitions.remove(getTransition(wd,sm));
				return true;
			}
		}
		return false;
	}
	
	
	


	public boolean areLinkableModelElements( ModelElement source, ModelElement target) 
	{	
		if( source instanceof ProcessRole && target instanceof WorkDefinition )
		{
			return areLinkableProcessRoleWorkDefinition( (ProcessRole) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkProduct && target instanceof WorkDefinition )
		{
			return areLinkableWorkProductWorkDefinition( (WorkProduct) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof WorkProduct )
		{
			return areLinkableWorkDefinitionWorkProduct((WorkDefinition) source, (WorkProduct) target);
		}
		
		if( source instanceof StateMachine && target instanceof WorkDefinition )
		{
			return areLinkableWorkProductStateWorkDefinition( (StateMachine) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof StateMachine )
		{
			return areLinkableWorkDefinitionWorkProductState((WorkDefinition) source, (StateMachine) target);
		}
		
		return false;
	}

	/**
	 * Test if a link between a process role and a work definition can be created
	 *
	 * @param r the Process Role to be tested
	 * @param a the Work Definition to be tested
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableProcessRoleWorkDefinition(ProcessRole r, WorkDefinition w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (!r.containsFeature(w) && w.getOwner() == null)
			{
				boolean canLink = true;
				for(int i = 0; i < w.subWorkCount(); i++)
				{
					canLink = canLink && w.getSubWork(i).getOwner() != r;
				}
				if(!canLink)
				{
					ErrorManager.getInstance().printKey("errorRoleAlreadyLinkedWithSubWork");
				}
				return canLink;
			}
			
			ErrorManager.getInstance().printKey("errorWorkDefinitionAlreadyHaveRole");
			return false;			
		}

		ErrorManager.getInstance().printKey("errorNotLinkableElements");
		return false;
	}
	
	/**
	 * Test if a link with a work product in input of a work definition can be created
	 *
	 * @param w the work product to be tested
	 * @param a the work definition to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableWorkProductWorkDefinition(WorkProduct wp, WorkDefinition wd)
	{
		if ((containsModelElement(wp)) && (containsModelElement(wd)))
		{	
			if(!existsLinkModelElements(wp,wd))
			{
				return true;
			}
			
			ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
			return false;
		}
		return false;
	}
	
	/**
	 * Test if a link with a work product in output of a work definition can be created
	 *
	 * @param w the work product to be tested
	 * @param a the work definition to be tested in output
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableWorkDefinitionWorkProduct(WorkDefinition wd, WorkProduct wp)
	{
		if ((containsModelElement(wp)) && (containsModelElement(wd)))
		{
			if(!existsLinkModelElements(wd,wp))
			{
				return true;
			}
			
			ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
			return false;
		}
		return false;
	}
	
	
	
	public boolean areLinkableWorkProductStateWorkDefinition(StateMachine sm, WorkDefinition wd)
	{
		if (sm.getContext() instanceof WorkProduct
				&& containsModelElement(wd) 
				&& containsModelElement(sm))
		{
			WorkProduct w = (WorkProduct)sm.getContext();
			
			if((!existsLinkModelElements(w,wd)) && (!existsLinkModelElements(wd,sm)))
			{
				return true;
			}
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;
	}
	
	public boolean areLinkableWorkDefinitionWorkProductState(WorkDefinition wd, StateMachine sm)
	{
		if (sm.getContext() instanceof WorkProduct
				&& containsModelElement(wd) 
				&& containsModelElement(sm))
		{
			WorkProduct w = (WorkProduct)sm.getContext();
			if((!existsLinkModelElements(wd,w)) && (!existsLinkModelElements(sm,wd)))
			{
				return true;
			}
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;
	}
	
	
	
	
	public boolean existsLinkModelElements( ModelElement source, ModelElement target) 
	{
		if( source instanceof ProcessRole && target instanceof WorkDefinition )
		{
			return existsLinkProcessRoleWorkDefinition( (ProcessRole) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkProduct && target instanceof WorkDefinition )
		{
			return existsLinkWorkProductWorkDefinition( (WorkProduct) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof WorkProduct )
		{
			return existsLinkWorkDefinitionWorkProduct((WorkDefinition) source, (WorkProduct) target);
		}
		
		if( source instanceof StateMachine && target instanceof WorkDefinition )
		{
			return existsLinkWorkProductStateWorkDefinition( (StateMachine) source, (WorkDefinition) target );
		}
		
		if( source instanceof WorkDefinition && target instanceof StateMachine )
		{
			return existsLinkWorkDefinitionWorkProductState((WorkDefinition) source, (StateMachine) target);
		}
		
		return false;
	}

	/**
	 * Test if a link between a process role and a work definition exists
	 *
	 * @param r the Process Role to be tested
	 * @param a the Work Definition to be tested
	 * @return true if the link exists, false otherwise
	 */
	public boolean existsLinkProcessRoleWorkDefinition(ProcessRole r, WorkDefinition w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (r.containsFeature(w))
			{
				return true;
			}
		}
		return false;
	}
	
	/**
	 * Test if a link with a work product in input of a work definition exists
	 *
	 * @param w the work product to be tested
	 * @param a the work definition to be tested in input
	 * @return true if the link exists, false otherwise
	 */
	public boolean existsLinkWorkProductWorkDefinition(WorkProduct wp, WorkDefinition wd)
	{
		if(getTransition(wp,wd)!=null)
		{
			return true;
		}

		return false;
	}
	
	/**
	 * Test if a link with a work product in output of a work definition exists
	 *
	 * @param w the work product to be tested
	 * @param a the work definition to be tested in output
	 * @return true if the link exists, false otherwise
	 */
	public boolean existsLinkWorkDefinitionWorkProduct(WorkDefinition wd, WorkProduct wp)
	{
		if(getTransition(wd,wp)!=null)
		{
			return true;
		}

		return false;
	}
	
	
	public boolean existsLinkWorkProductStateWorkDefinition(StateMachine sm, WorkDefinition wd)
	{
		
		if (sm.getContext() instanceof WorkProduct
				&& containsModelElement(wd) 
				&& containsModelElement(sm))
		{
			WorkProduct w = (WorkProduct)sm.getContext(); 
			
			if(getTransition(w,wd)!=null)
			{
				
				if (getTransition(sm,wd)!=null)
				{	

					return true;
				}
			}
		}
		return false;
	}
	
	public boolean existsLinkWorkDefinitionWorkProductState(WorkDefinition wd, StateMachine sm)
	{
		if ( sm.getContext() instanceof WorkProduct
				&& containsModelElement(wd) 
				&& containsModelElement(sm))
		{
			WorkProduct w = (WorkProduct) sm.getContext();
			
			if(getTransition(wd,w)!=null)
			{
				if (getTransition(wd,sm)!=null)
				{	
					return true;
				}
			}
		}
		return false;
	}
	
	
	
	public void visit(SpemVisitor visitor) 
	{
		visitor.visitWorkDefinitionDiagram( this );
	}

	public Object clone()
	{
		WorkDefinitionDiagram d = (WorkDefinitionDiagram) super.clone();
		d.mElements = (Vector) mElements.clone();
		d.mTransitions = (Vector) mTransitions.clone();
		return d;
	}
	
	public static class Transition implements Serializable
	{
		private ModelElement mInput;
		private ModelElement mOutput;
		
		public Transition()
		{
		}
		
		public Transition(ModelElement input, ModelElement output)
		{
			mInput=input;
			mOutput=output;
		}
		
		public void setInputModelElement(ModelElement me)
		{
			mInput=me;
		}
		
		public ModelElement getInputModelElement()
		{
			return mInput;
		}
		
		public void setOutputModelElement(ModelElement me)
		{
			mOutput=me;
		}

		public ModelElement getOutputModelElement()
		{
			return mOutput;
		}
		
		public Object clone()
		{
			return new Transition( mInput, mOutput);
		}
	};
}
