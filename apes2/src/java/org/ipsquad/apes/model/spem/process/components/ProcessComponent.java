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

package org.ipsquad.apes.model.spem.process.components;

import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ResourceManager;

/**
 * @version $Revision: 1.6 $
 */
public class ProcessComponent extends SPackage
{
	private String mVersion = ResourceManager.getInstance().getString("Version");
	private long mValidateTime = 0;
	
	public ProcessComponent()
	{
	}

	public ProcessComponent(String name)
	{
		super(name);
	}
	
	public boolean addModelElement(ModelElement e)
	{
		if(!containsModelElement(e) && e.getParent()==null)
		{
			if(e instanceof ProcessRole || (e instanceof WorkDefinition && !(e instanceof Activity))
					|| e instanceof WorkProduct || e instanceof ResponsabilityDiagram
					|| e instanceof WorkDefinitionDiagram || e instanceof ContextDiagram
					|| e instanceof SPackage )
			{
				mOwnedElement.add(e);
				e.setParent(this);
				return true;
			}
		}
		return false;
	}
	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitProcessComponent(this);
	}
	
	public Object clone()
	{
		ProcessComponent pc = (ProcessComponent)super.clone();
		
		return pc;
	}

	public void setValidate( long validateTime )
	{
		mValidateTime = validateTime;
	}
	
	public long getValidate()
	{
		return mValidateTime;
	}
	
	public void updateVersion()
	{
		mVersion = ResourceManager.getInstance().getString("Version");
	}
	
	public String getVersion()
	{
		return mVersion;
	}
};
