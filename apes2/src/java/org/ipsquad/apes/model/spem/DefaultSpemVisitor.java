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


package org.ipsquad.apes.model.spem;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.basic.Guidance;
import org.ipsquad.apes.model.spem.basic.GuidanceKind;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.core.PresentationElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.components.SProcess;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;


/**
 * Default visitor implementation that does nothing.
 * Its only work is to follow the inheritance tree.
 *
 * @version $Revision: 1.3 $
 */
public class DefaultSpemVisitor implements SpemVisitor
{
	/**
	 * Called when visiting an Element
	 *
	 * @param element the visited Element
	 */
	protected void visitElement(Element element) {}
	
	
	/**
	 * Called when visiting a ModelElement
	 * Simply treat it as an Element
	 *
	 * @param element the visited ModelElement
	 */
	protected void visitModelElement(ModelElement element)
	{
		visitElement(element);
	}


	/**
	 * Called when visiting a ModelElement
	 * Simply treat it as an Element
	 *
	 * @param element the visited ModelElement
	 */
	protected void visitPresentationElement(PresentationElement element)
	{
		visitElement(element);
	}

	/*public void visitApesProcess(ApesProcess p)
	{
		visitModelElement(p);
	}*/
	
	/**
	 * Simply treat the Package as a ModelElement
	 *
	 * @param pack the visited package
	 */
	public void visitPackage(IPackage pack)
	{
		if( pack instanceof ModelElement )
		{
			visitModelElement((ModelElement)pack);
		}
	}
	

	/**
	 * Simply treat the ProcessComponent as a Package
	 *
	 * @param component the visited process component
	 */
	public void visitProcessComponent(ProcessComponent component)
	{
		visitPackage(component);
	}

	/**
	 *  Simply treat the interface as a Package
	 * 
	 *  @param i the visited interface
	 */
	public void visitComponentInterface(ApesProcess.Interface i)
	{
		visitPackage(i);
	}
	
	public void visitProvidedInterface(ApesProcess.ProvidedInterface pi)
	{
		visitComponentInterface( pi );
	}
	
	public void visitRequiredInterface(ApesProcess.RequiredInterface ri)
	{
		visitComponentInterface( ri );
	}
	
	/**
	 * Simply treat the Process as a ProcessComponent
	 *
	 * @param process the visited process
	 */
	public void visitProcess(SProcess process)
	{
		visitProcessComponent(process);
	}


	/**
	 * Simply treat the WorkDefinition as a ModelElement
	 *
	 * @param work the visited work definition
	 */
	public void visitWorkDefinition(WorkDefinition work)
	{
		visitModelElement(work);
	}
	
	
	/**
	 * Simply treat the ProcessPerformer as a ModelElement
	 *
	 * @param performer the visited process performer
	 */
	public void visitProcessPerformer(ProcessPerformer performer)
	{
		visitModelElement(performer);
	}


	/**
	 * Simply treat the WorkProduct as a ModelElement
	 *
	 * @param product the visited work product
	 */
	public void visitProduct(WorkProduct product)
	{
		visitModelElement(product);
	}


	/**
	 * Simply treat the ProcessRole as a ProcessPerformer
	 *
	 * @param role the visited process role
	 */
	public void visitRole(ProcessRole role)
	{
		visitProcessPerformer(role);
	}


	/**
	 * Simply treat the Activity as a WorkDefinition
	 *
	 * @param activity the visited activity
	 */
	public void visitActivity(Activity activity)
	{
		visitWorkDefinition(activity);
	}


	/**
	 * Simply treat the ExternalDescription as a PresentationElement
	 *
	 * @param description the visited external description
	 */
	public void visitExternalDescription(ExternalDescription description)
	{
		visitPresentationElement(description);
	}


	/**
	 * Simply treat the Guidance as a ModelElement
	 *
	 * @param guidance the visited guidance
	 */
	public void visitGuidance(Guidance guidance)
	{
		visitModelElement(guidance);
	}


	/**
	 * Simply treat the GuidanceKind as a ModelElement
	 *
	 * @param kind the visited guidance kind
	 */
	public void visitGuidanceKind(GuidanceKind kind)
	{
		visitModelElement(kind);
	}
	
	
	/**
	 * Simply treat the SpemDiagram as a ModelElement
	 *
	 * @param diagram the visited spem diagram
	 */
	protected void visitSpemDiagram(SpemDiagram diagram)
	{
		visitModelElement(diagram);
	}
	
	
	/**
	 * Simply treat the FlowDiagram as a SpemDiagram
	 *
	 * @param diagram the visited flow diagram
	 */
	public void visitFlowDiagram(FlowDiagram diagram)
	{
		visitSpemDiagram(diagram);
	}
	
	/**
	 * Simply treat the ResponsabilityDiagram as a SpemDiagram
	 *
	 * @param diagram the visited class diagram
	 */
	public void visitResponsabilityDiagram(ResponsabilityDiagram diagram)
	{
		visitSpemDiagram(diagram);
	}

	/**
	 * Simply treat the ContextDiagram as a SpemDiagram
	 *
	 * @param diagram the visited class diagram
	 */
	public void visitContextDiagram(ContextDiagram diagram)
	{
		visitSpemDiagram(diagram);
	}

	/**
	 * Simply treat the ActivityDiagram as a SpemDiagram
	 *
	 * @param diagram the visited activity diagram
	 */
	public void visitActivityDiagram(ActivityDiagram diagram)
	{
		visitSpemDiagram(diagram);
	}
	
	/**
	 * Simply treat the WorkDefinitionDiagram as a SpemDiagram
	 *
	 * @param diagram the visited work definition diagram
	 */
	public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram)
	{
		visitSpemDiagram(diagram);
	}
	
	/**
	 * Simply treat the StateMachine as a ModelElement
	 *
	 * @param sm the visited stateMachine
	 */
	public void visitStateMachine(StateMachine sm)
	{
		visitModelElement(sm);
	}
}
