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

import javax.swing.JPopupMenu;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.DefaultModelVisitor;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

/**
 * This visitor associates an element with a poup menu.
 *
 * @version $Revision: 1.2 $
 */
public class PopupMenuAssociater extends DefaultModelVisitor
{
	private static final Context msContext = Context.getInstance();
	private JPopupMenu mResult = new JPopupMenu();
	
	/**
	 * Retrieve the icon associated to the element during the visit
	 */
	public JPopupMenu getMenu()
	{
		JPopupMenu result = mResult;
		
		mResult = new JPopupMenu();
		
		return result;
	}
	
	protected void visitElement(Element element)
	{
		if(!(element instanceof WorkProductRef) && !(element instanceof ApesProcess))
		{
			mResult.addSeparator();
			mResult.add(msContext.getAction("TreeRename"));
		}

		if(!(element instanceof ProcessComponent 
				|| element instanceof ApesProcess 
				|| element instanceof ContextDiagram
				|| element instanceof WorkProductRef
				|| element instanceof ApesProcess.ProvidedInterface
				|| element instanceof ApesProcess.RequiredInterface
				|| element instanceof ActivityDiagram
				|| element instanceof FlowDiagram))
		{
			mResult.addSeparator();
			mResult.add(msContext.getAction("TreeRemove"));
		}
	}
	
	public void visitApesProcess(ApesProcess p)
	{
		/*mResult.add(msContext.getAction("TreeAddProcessComponent"));
		mResult.add(msContext.getAction("TreeAddProvidedInterface"));
		mResult.add(msContext.getAction("TreeAddRequiredInterface"));
		mResult.addSeparator();*/
		
		super.visitApesProcess(p);
	}
	
	public void visitPackage(IPackage pack)
	{
		mResult.addSeparator();
		
		if( !( pack instanceof ApesProcess.Interface ) )
		{
			mResult.add(msContext.getAction("TreeAddPackage"));
			mResult.add(msContext.getAction("TreeAddWorkDefinition"));
			mResult.add(msContext.getAction("TreeAddRole"));
			mResult.add(msContext.getAction("TreeAddWorkProduct"));
			mResult.addSeparator();
			mResult.add(msContext.getAction("TreeAddResponsabilityDiagram"));
		}
		
		super.visitPackage(pack);
	}
	
	public void visitProduct(WorkProduct product)
	{
		mResult.add(msContext.getAction("TreeAddWorkProductState"));
		
		super.visitProduct(product);
	}
	
	public void visitRole(ProcessRole role)
	{
		super.visitRole(role);
	}

	public void visitActivity(Activity activity)
	{
		super.visitActivity(activity);
	}
		
	public void visitWorkDefinition(WorkDefinition w)
	{
		if( w instanceof ApesWorkDefinition )
		{
			ApesWorkDefinition aw = (ApesWorkDefinition) w;
			mResult.add(msContext.getAction("TreeAddActivity"));
			
			/*if( aw.canAddActivityDiagram() )
			{
				mResult.add(msContext.getAction("TreeAddActivityDiagram"));
			}
			if( aw.canAddFlowDiagram() )
			{
				mResult.add(msContext.getAction("TreeAddFlowDiagram"));
			}*/
		}
		
		super.visitWorkDefinition(w);
	}
	
	public void visitSpemDiagram(SpemDiagram diagram)
	{
		mResult.add(msContext.getAction("TreeOpenDiagram"));
		mResult.add(msContext.getAction("SaveAsJpeg"));
		if(diagram.modelElementCount()!= 0)
		{
			mResult.addSeparator();
			mResult.add(msContext.getAction("PrintDiagram"));
		}
		super.visitSpemDiagram(diagram);		
	}	
}
