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

import java.util.Date;

import org.ipsquad.apes.model.spem.ModelVisitor;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.Debug;

/**
 * 
 * @version $Revision: 1.5 $
 */
public class WorkProductRef extends ModelElement 
{
	private long mTag;
	private WorkProduct mRef;
	
	public WorkProductRef( String name )
	{
		super("");
		mRef = new  WorkProduct();
		mTag = new Date().getTime();
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++WorkProductRef");
	}
	
	public WorkProductRef( WorkProduct w )
	{
		super(ConfigManager.getInstance().getProperty("Reference")+w.getName());
		mRef = w;
		mTag = new Date().getTime();
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> ++WorkProductRef::"+w);
	}
	
	public long getTag()
	{
		return mTag;
	}
	
	public void setParent( IPackage parent )
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkProductRef("+getName()+"::setParent "+parent);
		super.setParent(parent);
		if( parent == null )
		{
			mRef.setReferences( WorkProduct.NO_REFERENCES );
		}
		else if( parent instanceof ApesProcess.ProvidedInterface )
		{
			mRef.setReferences( WorkProduct.REFERENCES_BY_PROVIDED_INTERFACE );
		}
		else
		{
			mRef.setReferences( WorkProduct.REFERENCES_BY_REQUIRED_INTERFACE );
		}
	}
	
	public WorkProduct getReference()
	{
		return mRef;
	}
	
	public void setReference( WorkProduct w )
	{
		if(Debug.enabled) Debug.print(Debug.MODEL, "(M) -> WorkProductRef("+getName()+"::setReference "+w);
		mRef = w;
	}
	
	public void visit(SpemVisitor visitor)
	{
		if( visitor instanceof ModelVisitor )
		{
			((ModelVisitor)visitor).visitWorkProductRef(this);
		}
	}
	
	public Object clone()
	{
		WorkProductRef w = (WorkProductRef)super.clone();
		
		w.mRef = new WorkProduct(w.getName());
		w.setName(ConfigManager.getInstance().getProperty("Reference")+w.mRef.getName() );
		mTag = new Date().getTime()+ Long.parseLong(w.mRef.getName(), 36) ;
		
		return w;
	}
}
