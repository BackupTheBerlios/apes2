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
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.basic.Guidance;
import org.ipsquad.apes.model.spem.basic.GuidanceKind;
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
 * Interface for visiting SPEM models
 * For more details see the visitor design pattern
 *
 * @version $Revision: 1.3 $
 */
public class CompositeVisitor implements RoutedSpemVisitor
{
	Vector mVisitors=new Vector();
        
	/**
	 * Called when you want to add a visitor
	 *
	 * @param v the visitor
	 */
	public void addVisitor(RoutedSpemVisitor v)
	{
		if (!containsVisitor(v))
			mVisitors.add(v);
        }
	
	/**
	 * Called when you want to remove a visitor
	 *
	 * @param v the visitor
	 */
	public void removeVisitor(RoutedSpemVisitor v)
	{
		if (containsVisitor(v))
			mVisitors.remove(v);
	}
		
	/**
	*
	* Called when you want to know if the visitor is contained
	*
	* @param v the visitor  
	*/
	public boolean containsVisitor(RoutedSpemVisitor v)
	{
		return mVisitors.contains(v);
	}
	
	/**
	 * Called when the visited element is a package
	 *
	 * @param pack the visited package
	 */
	public void visitPackage(IPackage pack)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitPackage(pack);
	}
	
	/**
	 * Called when the visited element is a process component
	 *
	 * @param component the visited process component
	 */
	public void visitProcessComponent(ProcessComponent component)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitProcessComponent(component);
	}

	/**
	 * Called when the visited element is an interface of process component
	 *
	 * @param i the visited interface 
	 */
	public void visitComponentInterface( ApesProcess.Interface i ) 
	{	
		for( int j = 0; j < mVisitors.size(); j++ )
		{
			((SpemVisitor)mVisitors.get(j)).visitComponentInterface(i);
		}
	}
	
	public void visitProvidedInterface( ApesProcess.ProvidedInterface pi ) 
	{	
		for( int j = 0; j < mVisitors.size(); j++ )
		{
			((SpemVisitor)mVisitors.get(j)).visitProvidedInterface(pi);
		}
	}
	
	public void visitRequiredInterface( ApesProcess.RequiredInterface ri ) 
	{	
		for( int j = 0; j < mVisitors.size(); j++ )
		{
			((SpemVisitor)mVisitors.get(j)).visitRequiredInterface(ri);
		}
	}
	
	/**
	 * Called when the visited element is a process
	 *
	 * @param process the visited process
	 */
	public void visitProcess(SProcess process)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitProcess(process);
	}

	/**
	 * Called when the visited element is a work definition
	 *
	 * @param work the visited work definition
	 */
	public void visitWorkDefinition(WorkDefinition work)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitWorkDefinition(work);
	}

	/**
	 * Called when the visited element is a process performer
	 *
	 * @param performer the visited process performer
	 */
	public void visitProcessPerformer(ProcessPerformer performer)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitProcessPerformer(performer);
	}

	/**
	 * Called when the visited element is a work product
	 *
	 * @param product the visited work product
	 */
	public void visitProduct(WorkProduct product)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitProduct(product);
	}
	

	/**
	 * Called when the visited element is a process role
	 *
	 * @param role the visited process role
	 */
	public void visitRole(ProcessRole role)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitRole(role);
	}
	
	/**
	 * Called when the visited element is an activity
	 *
	 * @param activity the visited activity
	 */
	
	public void visitActivity(Activity activity)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitActivity(activity);
	}

	/**
	 * Called when the visited element is an external description
	 *
	 * @param description the visited external description
	 */
	public void visitExternalDescription(ExternalDescription description)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitExternalDescription(description);
	}

	/**
	 * Called when the visited element is a guidance
	 *
	 * @param guidance the visited guidance
	 */
	public void visitGuidance(Guidance guidance)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitGuidance(guidance);
	}	


	/**
	 * Called when the visited element is a guidance kind
	 *
	 * @param kind the visited guidance kind
	 */
	public void visitGuidanceKind(GuidanceKind kind)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitGuidanceKind(kind);	
	}
	
	
	/**
	 * Called when the visited element is a flow diagram
	 *
	 * @param diagram the visited flow diagram
	 */
	public void visitFlowDiagram(FlowDiagram diagram)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitFlowDiagram(diagram);
	}		
			
	/**
	 * Called when the visited element is a responsability diagram
	 *
	 * @param diagram the visited responsability diagram
	 */
	public void visitResponsabilityDiagram(ResponsabilityDiagram diagram)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitResponsabilityDiagram(diagram);
	}	

	/**
	 * Called when the visited element is a responsability diagram
	 *
	 * @param diagram the visited responsability diagram
	 */
	public void visitContextDiagram(ContextDiagram diagram)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitContextDiagram(diagram);
	}	

	/**
	 * Called when the visited element is an activity diagram
	 *
	 * @param diagram the visited activity diagram
	 */
	public void visitActivityDiagram(ActivityDiagram diagram)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitActivityDiagram(diagram);
	}		
	
	
	/**
	 * Called when the visited element is a work definition diagram
	 *
	 * @param diagram the visited responsability diagram
	 */
	public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitWorkDefinitionDiagram(diagram);
	}	

	public void visitStateMachine(StateMachine sm)
	{
		for (int i=0;i<mVisitors.size();i++)
			((SpemVisitor)mVisitors.get(i)).visitStateMachine(sm);
	}
	
	public void routingBegin()
	{
		for (int i=0;i<mVisitors.size();i++)
			((RoutedSpemVisitor)mVisitors.get(i)).routingBegin();
	}

	public void routingEnd()
	{
		for (int i=0;i<mVisitors.size();i++)
			((RoutedSpemVisitor)mVisitors.get(i)).routingEnd();
	}

};
	
