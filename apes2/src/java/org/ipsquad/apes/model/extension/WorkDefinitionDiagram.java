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

package org.ipsquad.apes.model.extension;

import java.io.Serializable;
import java.util.Vector;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ErrorManager;

/**
 * Base class for the work definition diagram
 *
 * @version $Revision: 1.5 $
 */
public class WorkDefinitionDiagram extends SpemDiagram {

	private Vector mElements = new Vector();
	private Vector mTransitions = new Vector();
	
	public WorkDefinitionDiagram()
	{

	}

	public WorkDefinitionDiagram( String name )
	{
		super(name);
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
	
	
	public Transition getTransition(int i)
	{
		if(i<0 || i>=mTransitions.size())
		{
			return null;
		}

		return (Transition) mTransitions.get(i);
	}


	public int getTransitionCount()
	{
		return mTransitions.size();
	}
	

	public boolean addModelElement(ModelElement me) 
	{
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
			mElements.add(p);
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
			mElements.remove(e);
			return true;
		}

		return false;
	}

	public boolean canAddModelElement(ModelElement me) 
	{
		if( me instanceof ProcessRole || me instanceof WorkProduct || me instanceof WorkDefinition)
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
		
		ErrorManager.getInstance().printKey("errorElementForbidden");
		
		return false;
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
				r.addFeature(w);
				w.setOwner(r);
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
					mTransitions.add(new Transition(wd,wp));
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean removeLinkModelElements( ModelElement source, ModelElement target) 
	{
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
				w.setOwner(null);
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
			mTransitions.remove(getTransition(wd,wp));
			return true;
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
			if (!r.containsFeature(w))
			{
				return true;
			}
		}

		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
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
		if(!existsLinkModelElements(wp,wd))
		{
			return true;
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
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
		if(!existsLinkModelElements(wd,wp))
		{
			return true;
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
	
	
}