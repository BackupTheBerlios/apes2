/*
 * Created on 7 déc. 2003
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package org.ipsquad.apes.model.extension;

import java.util.Vector;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.ErrorManager;

/**
 * Base class for the context diagram
 *
 * @version $Revision: 1.3 $
 */
public class ContextDiagram extends SpemDiagram
{
	private Vector mUnlinkElements = new Vector();
	private Vector mRequiredWorkProduct = new Vector();
	private Vector mProvidedWorkProduct = new Vector();
	
	public ContextDiagram()
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++ContextDiagram");
	}

	public ContextDiagram( String name )
	{
		super(name);
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++ContextDiagram::"+name);
	}

	public void visit(SpemVisitor visitor)
	{
		visitor.visitContextDiagram(this);
	}
	
	public boolean addModelElement(ModelElement me) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ContextDiagram("+getName()+"::addModelElement "+me);
		if( me instanceof WorkProduct )
		{
			return addWorkProduct( (WorkProduct) me );
		}

		return false;
	}


	/**
	 * Add a work product to the context diagram
	 *
	 * @param p the work product to associate
	 * @return true if the work product can be added, false otherwise
	 */
	public boolean addWorkProduct(WorkProduct p)
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ContextDiagram("+getName()+")::addWorkProduct "+p);
		if(!containsModelElement(p))
		{
			mUnlinkElements.add(p);
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
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ContextDiagram("+getName()+")::removeModelElement "+e);
		if(mUnlinkElements.contains(e))
		{
			mUnlinkElements.remove(e);
			return true;
		}
		
		if(mProvidedWorkProduct.contains(e))
		{
			mProvidedWorkProduct.remove(e);
			return true;
		}
		
		if(mRequiredWorkProduct.contains(e))
		{
			mRequiredWorkProduct.remove(e);
			return true;
		}
		
		return false;
	}

	public boolean canAddModelElement(ModelElement me) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ContextDiagram("+getName()+")::canAddModelElement "+me);
		if(me instanceof WorkProduct)
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
		return e instanceof ProcessComponent
					|| mUnlinkElements.contains(e)
					|| mProvidedWorkProduct.contains(e)
					|| mRequiredWorkProduct.contains(e);
	}

	/**
	 * Get a model element by its index
	 *
	 * @param i the model element index
	 * @return the model element or null if the index is invalid
	 */
	public ModelElement getModelElement(int i)
	{
		if( i == 0 )
		{
			return Context.getInstance().getProject().getProcess().getComponent();
		}
		i--;
		if(i>=0 && i<mUnlinkElements.size())
		{
			return (ModelElement)mUnlinkElements.get(i);
		}
		i = i - mUnlinkElements.size();
		if(i>=0 && i<mProvidedWorkProduct.size())
		{
			return (ModelElement)mProvidedWorkProduct.get(i);	
		}
		i = i - mProvidedWorkProduct.size();
		if(i>=0 && i<mRequiredWorkProduct.size())
		{
			return (ModelElement)mRequiredWorkProduct.get(i);	
		}
		
		return null;
	}

	/**
	 * Get the number of model elements in this diagram
	 *
	 * @return the number of model elements contained in this diagram
	 */
	public int modelElementCount() 
	{
		return mUnlinkElements.size()+mProvidedWorkProduct.size()+mRequiredWorkProduct.size()+1;
	}

	/**
	 * Create a link between two model elements
	 *
	 * @param source the model element to be linked in output
	 * @param target the model element to be linked in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean createLinkModelElements( ModelElement source, ModelElement target) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ContextDiagram("+getName()+")::createLinkModelElements "+source+" "+target);
		if( (source instanceof WorkProduct && target instanceof ProcessComponent) )
		{
			if(containsModelElement(source))
			{
				if(areLinkableModelElements(source,target))
				{
					mRequiredWorkProduct.add(source);
					mUnlinkElements.remove(source);
					return true;
				}
			}
		}
		else if( (source instanceof ProcessComponent && target instanceof WorkProduct) )
		{
			if(containsModelElement(target))
			{
				if(areLinkableModelElements(source,target))
				{
					mProvidedWorkProduct.add(target);
					mUnlinkElements.remove(target);
					return true;
				}
			}
		}
		return false;
	}
	
	
	public boolean removeLinkModelElements( ModelElement source, ModelElement target) 
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ContextDiagram("+getName()+")::removeLinkModelElements "+source+" "+target);
		if(existsLinkModelElements(source, target))
		{
			if( (source instanceof ProcessComponent && target instanceof WorkProduct) )
			{
				mUnlinkElements.add(target);
				mProvidedWorkProduct.remove(target);
				return true;
			}
			else if( (source instanceof WorkProduct && target instanceof ProcessComponent) )
			{
				mUnlinkElements.add(source);
				mRequiredWorkProduct.remove(source);
				return true;
			}
		}
	
		return false;
	}	
	
	public boolean areLinkableModelElements( ModelElement source, ModelElement target ) 
	{
		if( source instanceof ProcessComponent && target instanceof WorkProduct )
		{
			return mUnlinkElements.contains(target);
		}
		if( source instanceof WorkProduct && target instanceof ProcessComponent )
		{
			return mUnlinkElements.contains(source);
		}
		
		return false;
	}
	
	public boolean existsLinkModelElements( ModelElement source, ModelElement target ) 
	{
		if( source instanceof ProcessComponent && target instanceof WorkProduct )
		{
			return mProvidedWorkProduct.contains(target);
		}
		if( source instanceof WorkProduct && target instanceof ProcessComponent )
		{
			return mRequiredWorkProduct.contains(source);
		}
		
		return false;
	}

	public Object clone()
	{
		ContextDiagram c = (ContextDiagram) super.clone();
		c.mUnlinkElements = new Vector();
		c.mProvidedWorkProduct = new Vector();
		c.mRequiredWorkProduct = new Vector();
		
		return c;
	}
}
