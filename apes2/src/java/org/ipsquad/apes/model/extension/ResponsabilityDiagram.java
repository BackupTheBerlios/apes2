/*
 * Created on 7 déc. 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.ipsquad.apes.model.extension;

import java.util.Vector;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.ErrorManager;

/**
 * Base class for the responsability diagram
 *
 * @version $Revision: 1.9 $
 */
public class ResponsabilityDiagram extends SpemDiagram {

	static int mId = 0;
	Vector mElements = new Vector();

	public ResponsabilityDiagram()
	{
		super();
		setName(ConfigManager.getInstance().getProperty("ResponsabilityDiagram")+mId);
		mId++;
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++ResponsabilityDiagram::"+getName());
	}

	public ResponsabilityDiagram( String name )
	{
		super(name);
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++ResponsabilityDiagram::"+name);
	}

	public boolean addModelElement(ModelElement me) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::addModelElement "+me);
		if( me instanceof ProcessRole )
		{
			return addProcessRole( (ProcessRole) me );
		}
		else if( me instanceof WorkProduct )
		{
			return addWorkProduct( (WorkProduct) me );
		}

		return false;
	}

	/**
	 * Add a role to the class diagram
	 *
	 * @param r the process role to associate
	 * @return true if the process role can be added, false otherwise
	 */
	public boolean addProcessRole(ProcessRole r)
	{
		if(!containsModelElement(r))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::addProcessRole "+r);
			mElements.add(r);
			return true;
		}
		ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
		return false;
	}

	/**
	 * Add a work product to the class diagram
	 *
	 * @param p the work product to associate
	 * @return true if the work product can be added, false otherwise
	 */
	public boolean addWorkProduct(WorkProduct p)
	{
		if(!containsModelElement(p))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::addWorkProduct "+p);
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
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::removeModelElement "+e);
			mElements.remove(e);
			return true;
		}

		return false;
	}

	public boolean canAddModelElement(ModelElement me) 
	{
		if( me instanceof ProcessRole || me instanceof WorkProduct)
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

	public boolean createLinkModelElements( ModelElement source, ModelElement target, Object extras) 
	{
		if( source instanceof ProcessRole && target instanceof WorkProduct )
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::removeModelElement "+source+" "+target);
			return createLinkProcessRoleWorkProduct( (ProcessRole) source, (WorkProduct) target );
		}
		
		ErrorManager.getInstance().printKey("errorNotLinkableElements");
		return false;
	}

	/**
	 * Create a link between a process role and a work product
	 *
	 * @param r the Process Role to be linked
	 * @param w the Work Product to be linked
	 * @return true if the link can be created, false otherwise
	 */
	public boolean createLinkProcessRoleWorkProduct(ProcessRole r, WorkProduct w)
	{
		if (areLinkableProcessRoleWorkProduct(r, w))
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::createLinkProcessRoleWorkProduct "+r+" "+w);
			r.addResponsibility(w);
			w.setResponsible(r);
			return true;
		}
		
		return false;
	}
	
	public boolean removeLinkModelElements( ModelElement source, ModelElement target, Object extras) 
	{
		if( source instanceof ProcessRole && target instanceof WorkProduct )
		{
			if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::removeLinkModelElements "+source+" "+target);
			return removeLinkProcessRoleWorkProduct( (ProcessRole) source, (WorkProduct) target );
		}

		return false;
	}

	/**
	 * Remove a link between a process role and an work product
	 *
	 * @param r the Process Role to be unlinked
	 * @param w the WorkProduct to be unlinked
	 * @return true if the link can be removed, false otherwise
	 */
	public boolean removeLinkProcessRoleWorkProduct(ProcessRole r, WorkProduct w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (r.removeResponsibility(w))
			{
				if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ResponsabilityDiagram("+getName()+")::removeLinkProcessRoleWorkProduct "+r+" "+w);
				w.setResponsible(null);
				return true;
			}
		}
		
		return false;
	}
	
	public boolean areLinkableModelElements( ModelElement source, ModelElement target, Object extras) 
	{
		if( source instanceof ProcessRole && target instanceof WorkProduct )
		{
			return areLinkableProcessRoleWorkProduct( (ProcessRole) source, (WorkProduct) target );
		}
		
		ErrorManager.getInstance().printKey("errorNotLinkableElements");
		return false;
	}

	/**
	 * Test if a link between a process role and a work product can be created
	 *
	 * @param r the Process Role to be tested
	 * @param w the Work Product to be tested
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableProcessRoleWorkProduct(ProcessRole r, WorkProduct w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (!r.containsResponsibility(w) && w.getResponsible()==null)
			{
				return true;
			}
			
			if(r.containsResponsibility(w) && w.getResponsible() == r)
			{
				ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
				return false;			    
			}
		}
		
		ErrorManager.getInstance().printKey("errorNotLinkableElements");
		return false;
	}
	
	public boolean existsLinkModelElements( ModelElement source, ModelElement target, Object extras) 
	{
		if( source instanceof ProcessRole && target instanceof WorkProduct )
		{
			return existsLinkProcessRoleWorkProduct( (ProcessRole) source, (WorkProduct) target );
		}
		
		return false;
	}

	/**
	 * Test if a link between a process role and a work product exists
	 *
	 * @param r the Process Role to be tested
	 * @param w the Work Product to be tested
	 * @return true if the link exists, false otherwise
	 */
	public boolean existsLinkProcessRoleWorkProduct(ProcessRole r, WorkProduct w)
	{
		if (containsModelElement(r) && containsModelElement(w))
		{
			if (r.containsResponsibility(w) && w.getResponsible()==r)
			{
				return true;
			}
		}
		return false;
	}
	
	public void visit(SpemVisitor visitor) 
	{
		visitor.visitResponsabilityDiagram( this );
	}

	public Object clone()
	{
		ResponsabilityDiagram d = (ResponsabilityDiagram) super.clone();
		d.mElements = (Vector) mElements.clone();
		d.setName(ConfigManager.getInstance().getProperty("ResponsabilityDiagram")+mId);
		mId++;
		return d;
	}
}
