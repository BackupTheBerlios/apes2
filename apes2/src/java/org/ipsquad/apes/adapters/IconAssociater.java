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


package org.ipsquad.apes.adapters;

import javax.swing.Icon;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.DefaultModelVisitor;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.components.SProcess;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.utils.IconManager;


/**
 * This visitor associate an element with an Icon.
 *
 * @version $Revision: 1.3 $
 */
public class IconAssociater extends DefaultModelVisitor
{
	Icon mResult = null;
	IconManager mManager;
	
	/**
	 * @param manager the IconManager
	 */
	public IconAssociater(IconManager manager)
	{
		mManager = manager;
	}
	
	/**
	 * Retrieve the icon associated to the element during the visit
	 */
	public Icon getIcon()
	{
		Icon result = mResult;
		
		mResult = null;
		
		return result;
	}
	
	protected void visitElement(Element element)
	{
		mResult = mManager.getIcon("icons/TreeUnknown.gif");
	}
	
	public void visitApesProcess(ApesProcess p) 
	{
		mResult = mManager.getIcon("icons/TreeProject.gif");
	}

	public void visitPackage(IPackage pack)
	{
		mResult = mManager.getIcon("icons/TreePackage.gif");
	}
	
	public void visitProcessComponent( ProcessComponent component )
	{
		mResult = mManager.getIcon("icons/TreeComponent.gif");
	}
	
	public void visitComponentInterface( ApesProcess.Interface i )
	{
	}
	
	public void visitProvidedInterface( ApesProcess.ProvidedInterface ri )
	{
		mResult = mManager.getIcon("icons/TreeProvidedInterface.gif");
	}
	
	public void visitRequiredInterface( ApesProcess.RequiredInterface ri )
	{
		mResult = mManager.getIcon("icons/TreeRequiredInterface.gif");
	}
	
	public void visitProcess( SProcess p )
	{
		mResult = mManager.getIcon("icons/TreePackage.gif");
	}
	
	public void visitWorkDefinition( WorkDefinition w )
	{
		mResult = mManager.getIcon("icons/TreeWorkDefinition.gif");
	}
	
	public void visitProduct(WorkProduct product)
	{
		mResult = mManager.getIcon("icons/TreeWorkProduct.gif");
	}

	public void visitRole(ProcessRole role)
	{
		mResult = mManager.getIcon("icons/TreeRole.gif");
	}

	public void visitActivity(Activity activity)
	{
		mResult = mManager.getIcon("icons/TreeActivity.gif");
	}

	public void visitExternalDescription(ExternalDescription description)
	{
		mResult = mManager.getIcon("icons/TreeExternalDescription.gif");
	}

	public void visitFlowDiagram(FlowDiagram diagram)
	{
		mResult = mManager.getIcon("icons/TreeClassDiagram.gif");
	}
	
	public void visitActivityDiagram(ActivityDiagram diagram)
	{
		mResult = mManager.getIcon("icons/TreeActivityDiagram.gif");
	}
	
	public void visitResponsabilityDiagram(ResponsabilityDiagram diagram)
	{
		mResult = mManager.getIcon("icons/TreeResponsabilityDiagram.gif");
	}

	public void visitContextDiagram(ContextDiagram diagram)
	{
		mResult = mManager.getIcon("icons/TreeCtxDiag.gif");
	}

	public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram)
	{
		mResult = mManager.getIcon("icons/TreeWorkDefFlow.gif");
	}
	
	public void visitWorkProductRef(WorkProductRef ref)
	{
		mResult = mManager.getIcon("icons/TreeWorkProduct.gif");
	}
	
	public void visitStateMachine(StateMachine s)
	{
		mResult = mManager.getIcon("icons/TreeState.gif");
	}
}
