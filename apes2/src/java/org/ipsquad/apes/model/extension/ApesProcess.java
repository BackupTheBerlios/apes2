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

import java.util.Vector;

import org.ipsquad.apes.model.spem.ModelVisitor;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ResourceManager;

/**
 * 
 * @version $Revision: 1.1 $
 */
public class ApesProcess extends ModelElement implements IPackage 
{
	private ProcessComponent mComponent = null;
	private ProvidedInterface mProvidedInterface = null;
	private RequiredInterface mRequiredInterface = null;
	
	public ApesProcess()
	{
		super("");
		init();
	}
	
	public ApesProcess(String name)
	{
		super(name);
		//init();
	}
	
	public void init()
	{
		ProcessComponent c = new ProcessComponent("Component");
		addModelElement( c );
		c.addModelElement( new ContextDiagram("Context Diagram") );
		addModelElement(new ProvidedInterface("provided"));
		addModelElement(new RequiredInterface("required"));
	}
	
	public void visit(SpemVisitor visitor) 
	{
		if( visitor instanceof ModelVisitor )
		{	
			((ModelVisitor)visitor).visitApesProcess(this);
		}
	}
	
	public boolean addModelElement(ModelElement e)
	{
		if(e == null)
		{
			return false;
		}
		
		if( e instanceof ProcessComponent && e.getParent() == null )
		{
			mComponent = (ProcessComponent) e;
			mComponent.setParent(this);
			return true;
		}

		if( e instanceof ProvidedInterface && mProvidedInterface == null )
		{
			mProvidedInterface = (ProvidedInterface) e;
			mProvidedInterface.setParent(this);
			return true;
		}

		if( e instanceof RequiredInterface )
		{
			mRequiredInterface = (RequiredInterface) e;
			mRequiredInterface.setParent(this);
			return true;
		}

		return false;
	}

	public boolean removeModelElement( ModelElement me )
	{
		if( me instanceof ProcessComponent )
		{
			if( mComponent == (ProcessComponent) me )
			{
				mComponent.setParent( null );
				mComponent = null;
				return true;
			}
		}

		if( me == mProvidedInterface )
		{
			mProvidedInterface.setParent( null );
			mProvidedInterface = null;
			return true;
		}
		else if( me == mRequiredInterface )
		{
			mRequiredInterface.setParent(null);
			mRequiredInterface = null;
			return true;
		}

		return false;
	}

	public boolean containsModelElement( ModelElement me )
	{
		if( me instanceof ProcessComponent )
		{
			return mComponent == (ProcessComponent) me;
		}

		if( me instanceof ProvidedInterface )
		{
			return mProvidedInterface == me;
		}

		if( me instanceof RequiredInterface )
		{
			return mRequiredInterface == me;
		}

		return false;
	}
	
	public ModelElement getModelElement(int i) 
	{
		int index = i;
		
		if( index < 0 )
		{
			return null;
		}
		
		if( mComponent != null && index == 0)
		{
				return mComponent;
		}
		else
		{
			--index;
		}
		
		if( mProvidedInterface != null && index == 0 )
		{
			return mProvidedInterface;
		}
		else
		{
			--index;
		}
		
		if( mRequiredInterface != null && index == 0  )
		{
			return mRequiredInterface;
		}
		
		return null;
	}
	
	public ProcessComponent getComponent()
	{
		return mComponent;
	}
	
	/**
	 * use to save the project
	 * 
	 */
	public void setProvidedInterfaces( ProvidedInterface pi )
	{
		mProvidedInterface = pi;
	}
	
	public void setRequiredInterfaces( RequiredInterface ri )
	{
		mRequiredInterface = ri;
	}
	
	public RequiredInterface getRequiredInterface( )
	{
		return mRequiredInterface;
	}

	public ProvidedInterface getProvidedInterface( )
	{
		return mProvidedInterface;
	}

	public int modelElementCount() 
	{
		int count = 0;
		
		if( mComponent != null ) 
		{
			++count;
		}
		if( mProvidedInterface != null )
		{
			++count;
		}
		if( mRequiredInterface != null )
		{
			++count;
		}
		return count;
	}	
	
	public void checkInterfaces()
	{
		if( mComponent == null )
		{
			return;
		}
		
		checkInterfaces( mComponent );
	}
	
	protected void checkInterfaces( IPackage p )
	{
		for( int i = 0; i < p.modelElementCount(); i++)
		{
			ModelElement me = p.getModelElement(i); 
			if( me instanceof SPackage )
			{
				checkInterfaces((SPackage)me);
			}
			else if( me instanceof WorkProduct )
			{
				if( mProvidedInterface == null || !checkInterface( mProvidedInterface, (WorkProduct)me) )
				{	
					if( mRequiredInterface != null )
					{
						checkInterface( mRequiredInterface, (WorkProduct)me);
					}
				}
			}
		}
	}
	
	protected boolean checkInterface( Interface in, WorkProduct w)
	{
		for( int j = 0; j < in.modelElementCount(); j++ )
		{
			if( ((WorkProductRef)in.getModelElement(j)).getReference().equals(w) )
			{
				((WorkProductRef)in.getModelElement(j)).setReference(w);
				return true;
			}
		}
		return false;
	}
	
	public static abstract class Interface extends ModelElement implements IPackage
	{
		private Vector mWorkProductRefs = new Vector();
		
		protected Interface()
		{
		}

		protected Interface( String name )
		{
			super("");
			setName( name );
		}

		public void setName( String name )
		{
			if( name.indexOf( ResourceManager.getInstance().getString("interface") ) == 0 )
			{
				super.setName( name );
			}
			else
			{
				super.setName(ResourceManager.getInstance().getString("interface")+name);
			}
		}

		public void visit(SpemVisitor visitor)
		{
			visitor.visitComponentInterface(this);
		}

		public Object clone()
		{
			Interface in = (Interface) super.clone();

			return in;
		}
		
		public boolean addModelElement(ModelElement e) 
		{
			if( e instanceof WorkProductRef )
			{
				if( ! mWorkProductRefs.contains(e) )
				{
					for( int i = 0; i < mWorkProductRefs.size(); i++ )
					{
						if( ((WorkProductRef)mWorkProductRefs.get(i)).getReference() == ((WorkProductRef)e).getReference() )
						{
							return false;
						}
					}
					
					mWorkProductRefs.add(e);
					e.setParent(this);
					return true;
				}
			}
			return false;
		}

		public boolean containsModelElement(ModelElement e) 
		{
			if( e instanceof WorkProductRef )
			{
				return mWorkProductRefs.contains(e);
			}
			return false;
		}
		
		public WorkProductRef getWorkProductRef( WorkProduct w )
		{
			if( mWorkProductRefs.size() == 0 )
			{
				return null;
			}
			
			int i = 0;
			while( i < mWorkProductRefs.size() && i < mWorkProductRefs.size() && !((WorkProductRef)mWorkProductRefs.get(i)).getReference().equals(w) )
			{
				i++;
			}
		
			if( i < mWorkProductRefs.size() )
			{
				return (WorkProductRef)mWorkProductRefs.get(i);
			}
			return null;
		}
		
		public ModelElement getModelElement(int i) 
		{
			if( i < 0 || i >= mWorkProductRefs.size() )
			{
				return null;
			}
			return (ModelElement)mWorkProductRefs.get(i);
		}

		public int modelElementCount() 
		{
			return mWorkProductRefs.size();
		}

		public boolean removeModelElement(ModelElement e) 
		{
			if( e instanceof WorkProductRef )
			{
				if( mWorkProductRefs.contains(e) )
				{
					mWorkProductRefs.remove(e);
					e.setParent(null);
					return true;
				}
			}
				
			return false;
		}

	}
	
	public static class ProvidedInterface extends Interface
	{
		public ProvidedInterface( String name )
		{
			super( name );
		}
		
		public void visit(SpemVisitor visitor)
		{
			visitor.visitProvidedInterface(this);
		}
	}

	public static class RequiredInterface extends Interface
	{
		public RequiredInterface( String name )
		{
			super( name );
		}

		public void visit(SpemVisitor visitor)
		{
			visitor.visitRequiredInterface(this);
		}
	}
}
