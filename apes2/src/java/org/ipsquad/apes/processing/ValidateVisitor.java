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


package org.ipsquad.apes.processing;

import java.util.Vector;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.basic.Guidance;
import org.ipsquad.apes.model.spem.basic.GuidanceKind;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.components.SProcess;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;

/**
 *
 * @version $Revision: 1.8 $
 */
public class ValidateVisitor implements RoutedSpemVisitor
{
	private boolean mHasErrors;
	private Vector mProductNames = new Vector();
	
	public boolean getHasErrors()
	{
		return mHasErrors;
	}
	
	public void visitApesProcess(ApesProcess p) { }
	
	/**
	 * Called when the visited element is a package
	 *
	 * @param pack the visited package
	 */
	public void visitPackage(IPackage pack) 
	{ 
		if(pack.modelElementCount()>1)
		{
			Vector temp=new Vector();
			for(int i=0;i<pack.modelElementCount();i++)
			{
				if(mProductNames.contains(pack.getModelElement(i).getName()))
				{	
					ErrorManager.getInstance().println(
							ResourceManager.getInstance().getString("errorValidateDuplicateNameInAPackage")+" : "+((Element)pack).getName()+", duplicate name : "+pack.getModelElement(i).getName());
					mHasErrors = true;	
				}
				else temp.add(pack.getModelElement(i).getName());
			}
		}
	}


	/**
	 * Called when the visited element is a process component
	 *
	 * @param component the visited process component
	 */
	public void visitProcessComponent(ProcessComponent component) {	}

	/**
	 * Called when the visited element is an interface of process component
	 *
	 * @param i the visited interface 
	 */
	public void visitComponentInterface( ApesProcess.Interface i ) {	}
	public void visitProvidedInterface(ApesProcess.ProvidedInterface pi) { }
	public void visitRequiredInterface(ApesProcess.RequiredInterface ri) { }
	
	/**
	 * Called when the visited element is a process
	 *
	 * @param process the visited process
	 */
	public void visitProcess(SProcess process) { visitPackage(process); }


	/**
	 * Called when the visited element is a work definition
	 *
	 * @param work the visited work definition
	 */
	public void visitWorkDefinition(WorkDefinition work) 
	{ 
		if( work.subWorkCount() < 2 )
		{
			ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateWorkDefinition")+" : "+work.getName());
			mHasErrors = true;
		}
	}

	/**
	 * Called when the visited element is a process performer
	 *
	 * @param performer the visited process performer
	 */
	public void visitProcessPerformer(ProcessPerformer performer) {	}


	/**
	 * Called when the visited element is a work product
	 *
	 * @param product the visited work product
	 */
	public void visitProduct(WorkProduct product)
	{
		if( mProductNames.contains(product.getName()) )
		{
			ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateWorkProductDuplicateName")+" : "+product.getName());
			mHasErrors = true;
		}
		else
		{
			mProductNames.add(product.getName());
		}
		
		if( product.getReferences() == WorkProduct.NO_REFERENCES )
		{
			if( product.getResponsible() == null )
			{
				ErrorManager.getInstance().println(
						ResourceManager.getInstance().getString("errorValidateWorkProductWithoutResponsible")+" : "+product.getName());
				mHasErrors = true;
			}
			if( product.getInputCount() == 0 && product.getOutputCount() == 0)
			{	
				ErrorManager.getInstance().println(
						ResourceManager.getInstance().getString("errorValidateWorkProductWithoutActivity")+" : "+product.getName());
				mHasErrors = true;
			}
		}

		if( product.getReferences() == WorkProduct.REFERENCES_BY_PROVIDED_INTERFACE )
		{
			if( product.getResponsible() == null )
			{
				ErrorManager.getInstance().println(
						ResourceManager.getInstance().getString("errorValidateWorkProductWithoutResponsible")+" : "+product.getName());
				mHasErrors = true;
			}
			if( product.getInputCount() == 0 )
			{
				ErrorManager.getInstance().println(
						ResourceManager.getInstance().getString("errorValidateProvidedWorkProductActivity")+" : "+product.getName());
				mHasErrors = true;
			}
		}	

		if( product.getReferences() == WorkProduct.REFERENCES_BY_REQUIRED_INTERFACE )
		{	
			if( product.getResponsible() != null )
			{
				ErrorManager.getInstance().println(
						ResourceManager.getInstance().getString("errorValidateRequiredWorkProductRole")+" : "+product.getName());
				mHasErrors = true;
			}
			if( product.getInputCount() != 0 || product.getOutputCount() == 0 )
			{
				ErrorManager.getInstance().println(
						ResourceManager.getInstance().getString("errorValidateRequiredWorkProductActivity")+" : "+product.getName());
				mHasErrors = true;
			}	
		}
		
		if( product.getInputCount()==0 && product.getOutputCount()==0 && product.getResponsible()==null)
		{
			ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateWorkProductAlone")+" : "+product.getName());
			mHasErrors = true;
		}
	}


	/**
	 * Called when the visited element is a process role
	 *
	 * @param role the visited process role
	 */
	public void visitRole(ProcessRole role) 
	{ 
		if(role.getFeatureCount()==0 && role.getResponsibilityCount()==0)
		{
			ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateProcessRoleAlone")+" : "+role.getName());
			mHasErrors = true;
		}		
	}


	/**
	 * Called when the visited element is an activity
	 *
	 * @param activity the visited activity
	 */
	public void visitActivity(Activity activity)
	{
		if (activity.getOwner()==null)
		{
			ErrorManager.getInstance().println(
			ResourceManager.getInstance().getString("errorValidateActivityWithoutRole")+" : "+activity.getName());
			mHasErrors = true;
		}
		
		if(activity.getInputCount()==0 && activity.getOutputCount()==0 && activity.getOwner()==null)
		{
			ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateActivityAlone")+" : "+activity.getName());
			mHasErrors = true;
		}
	}


	/**
	 * Called when the visited element is an external description
	 *
	 * @param description the visited external description
	 */
	public void visitExternalDescription(ExternalDescription description) { }


	/**
	 * Called when the visited element is a guidance
	 *
	 * @param guidance the visited guidance
	 */
	public void visitGuidance(Guidance guidance) { }


	/**
	 * Called when the visited element is a guidance kind
	 *
	 * @param kind the visited guidance kind
	 */
	public void visitGuidanceKind(GuidanceKind kind) { }

	
	/**
	 * Called when the visited element is a flow diagram
	 *
	 * @param diagram the visited flow diagram
	 */
	public void visitFlowDiagram(FlowDiagram diagram)
	{
		visitSpemDiagram(diagram);
	}
	
	/**
	 * Called when the visited element is a work definition diagram
	 *
	 * @param diagram the visited work definition diagram
	 */
	public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram)
	{
		visitSpemDiagram(diagram);
		
		WorkProduct wp;
		WorkDefinition wd;
		boolean isUsed, isProvided;
		
		for( int i = 0; i < diagram.getTransitionCount(); i++ )
		{
			isUsed = false;
			
			if( diagram.getTransition(i).getInputModelElement() instanceof WorkProduct )
			{
				isProvided = false;
				wp = (WorkProduct)diagram.getTransition(i).getInputModelElement();
				wd = (WorkDefinition)diagram.getTransition(i).getOutputModelElement();
				
				for( int j = 0; j < wd.subWorkCount(); j++ )
				{
					if( wd.getSubWork(j).containsOutputWorkProduct(wp) )
					{	
						isProvided = true;
					}
					if( wd.getSubWork(j).containsInputWorkProduct(wp) )
					{
						isUsed = true;
					}
				}
				if( isProvided && !isUsed )
				{
					ErrorManager.getInstance().println(
							ResourceManager.getInstance().getString("errorValidateRequiredWorkProductProvidedByWorkDefinition")
								+" : "+diagram.getName()+" "+wp.getName()+" "+wd.getName());
						mHasErrors = true;
				}
			}
			else
			{
				wp = (WorkProduct)diagram.getTransition(i).getOutputModelElement();
				wd = (WorkDefinition)diagram.getTransition(i).getInputModelElement();
				
				for( int j = 0; j < wd.subWorkCount(); j++ )
				{
					if( wd.getSubWork(j).containsOutputWorkProduct(wp) )
					{
						isUsed = true;
					}
				}
			}
			
			if( !isUsed )
			{
				ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateWorkProductNotUsedInWorkDefinition")
						+" : "+diagram.getName()+" "+wp.getName()+" "+wd.getName());
				mHasErrors = true;
			}
		}
	}
	
	public void visitResponsabilityDiagram(ResponsabilityDiagram diagram) 
	{ 
		visitSpemDiagram(diagram);
	}

	public void visitContextDiagram(ContextDiagram diagram)
	{ 
		visitSpemDiagram(diagram);
	}

	public void visitSpemDiagram(SpemDiagram diagram)
	{
		if( diagram.modelElementCount() == 0 )
		{
			ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateEmptyDiagram")+" : "+diagram.getName());
			mHasErrors = true;
		}
	}
	/**
	 * Called when the visited element is an activity diagram
	 *
	 * @param diagram the visited activity diagram
	 */
	public void visitActivityDiagram(ActivityDiagram diagram)
	{
		boolean haveFinalPoint = false;
		boolean haveActivity = false;
		
		int nbElement = diagram.modelElementCount();
		ModelElement me;

		int nbTransition = diagram.getTransitionCount();
		ActivityDiagram.Transition tr;

		boolean inputTransitionFind;
		boolean outputTransitionFind;
		int nbDecisionExit;
		int i = 0;
		int j;
		
		if(!diagram.haveInitialPoint())
		{
			ErrorManager.getInstance().println(
			ResourceManager.getInstance().getString("errorValidateNoInitialPoint"));
			mHasErrors = true;
		}
		
		while(i < nbElement)
		{
			me = diagram.getModelElement(i);
			inputTransitionFind = false;
			outputTransitionFind = false;
			nbDecisionExit = 0;
			
			if (me instanceof Activity)
			{
				haveActivity = true;
			}
			else if (me instanceof ActivityDiagram.FinalPoint)
			{
				haveFinalPoint = true;
			}
			
			j = 0;
			
			while(j < nbTransition && (!inputTransitionFind || !outputTransitionFind || nbDecisionExit!=2))
			{
				tr = diagram.getTransition(j);
				if(me == tr.getInputModelElement()) 
				{
					inputTransitionFind = true;
					if (me instanceof ActivityDiagram.Decision)
					{
						nbDecisionExit++;
					}
					else
					{
						nbDecisionExit=2;
					}
				}
				else if(me == tr.getOutputModelElement())
				{
					outputTransitionFind = true;
					if (!(me instanceof ActivityDiagram.Decision))
					{
						nbDecisionExit=2;
					}					
				}
				j++;
			}

			if(!inputTransitionFind && !outputTransitionFind)
			{
				if (me instanceof Activity)
				{	
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateActivityAlone")+" : "+((Activity)me).getName());
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.Decision)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateDecisionAlone"));
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.InitialPoint)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateInitialPointAlone"));
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.FinalPoint)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateFinalPointAlone"));
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.Synchro)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateSynchroAlone"));
					mHasErrors = true;
				}
			}
			else if(!outputTransitionFind)
			{
				if (me instanceof Activity)
				{	
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateActivityNoEntry")+" : "+((Activity)me).getName());
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.Decision)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateDecisionNoEntry"));
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.Synchro)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateSynchroNoEntry"));
					mHasErrors = true;
				}				
			}
			else if(!inputTransitionFind || nbDecisionExit!=2)
			{
				if (me instanceof Activity)
				{	
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateActivityNoExit")+" : "+((Activity)me).getName());
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.Decision)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateDecisionNoExit"));
					mHasErrors = true;
				}
				if (me instanceof ActivityDiagram.Synchro)
				{
					ErrorManager.getInstance().println(
					ResourceManager.getInstance().getString("errorValidateSynchroNoExit"));
					mHasErrors = true;
				}
			}
			i++;
		}
		if (haveActivity == false)
		{
			ErrorManager.getInstance().println(
			ResourceManager.getInstance().getString("errorValidateNoActivity"));
			mHasErrors = true;
		}
		if (haveFinalPoint == false)
		{
			ErrorManager.getInstance().println(
			ResourceManager.getInstance().getString("errorValidateNoFinalPoint"));
			mHasErrors = true;
		}
	}

	public void visitStateMachine(StateMachine sm) { }
	
	public void routingBegin()
	{
		mHasErrors = false;
		ErrorManager.getInstance().println("");
		ErrorManager.getInstance().println(
		ResourceManager.getInstance().getString("errorValidateStart"));
	}

	public void routingEnd() 
	{
		ErrorManager.getInstance().println(
		ResourceManager.getInstance().getString("errorValidateEnd"));
		ErrorManager.getInstance().println("");
	}

};
