/*
 * APES is a Process Engineering Software
 * Copyright (C) 2004 IPSquad
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

import java.awt.Color;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.extension.ActivityDiagram.Decision;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.InitialPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.Synchro;
import org.ipsquad.apes.model.extension.ActivityDiagram.Transition;
import org.ipsquad.apes.model.extension.ApesProcess.Interface;
import org.ipsquad.apes.model.extension.ApesProcess.ProvidedInterface;
import org.ipsquad.apes.model.extension.ApesProcess.RequiredInterface;
import org.ipsquad.apes.model.spem.ModelVisitor;
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
import org.ipsquad.apes.ui.ColorFontPanel;

public class ColorAssociater implements ModelVisitor
{
	private Color mForeground = Color.black;
	private Color mBackground = Color.white;
	private int mFont = 0;

	public Color getForeground()
	{
		return mForeground;
	}
	
	public Color getBackground()
	{
		return mBackground;
	}
	
	public int getFont()
	{
		return mFont;
	}
	
	public void visitPackage(IPackage pack)
	{
	}

	public void visitProcessComponent(ProcessComponent component)
	{
	}

	public void visitComponentInterface(Interface i)
	{
	}

	public void visitProvidedInterface(ProvidedInterface pi)
	{	
	}

	public void visitRequiredInterface(RequiredInterface ri)
	{	
	}

	public void visitProcess(SProcess process)
	{	
	}

	public void visitWorkDefinition(WorkDefinition work)
	{	
		mForeground = ColorFontPanel.getColor(ColorFontPanel.WORK_DEF_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.WORK_DEF_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.WORK_DEF_KEY);
	}
	
	public void visitProcessPerformer(ProcessPerformer performer)
	{
	}

	public void visitProduct(WorkProduct product)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.WORK_PRODUCT_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.WORK_PRODUCT_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.WORK_PRODUCT_KEY);
	}

	public void visitRole(ProcessRole role)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.ROLE_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.ROLE_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.ROLE_KEY);
	}

	public void visitActivity(Activity activity)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.ACTIVITY_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.ACTIVITY_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.ACTIVITY_KEY);
	}

	public void visitExternalDescription(ExternalDescription description)
	{
	}

	public void visitGuidance(Guidance guidance)
	{
	}

	public void visitGuidanceKind(GuidanceKind kind)
	{
	}

	public void visitFlowDiagram(FlowDiagram diagram)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.DIAGRAMS_KEY);
	}

	public void visitResponsabilityDiagram(ResponsabilityDiagram diagram)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.DIAGRAMS_KEY);
	}

	public void visitContextDiagram(ContextDiagram diagram)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.DIAGRAMS_KEY);
	}

	public void visitActivityDiagram(ActivityDiagram diagram)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.DIAGRAMS_KEY);
	}
	
	public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.DIAGRAMS_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.DIAGRAMS_KEY);
	}

	public void visitStateMachine(StateMachine sm)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.STATE_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.STATE_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.STATE_KEY);
	}

	public void visitApesProcess(ApesProcess p){	}
	public void visitDecision(Decision decision){ }
	public void visitFinalPoint(FinalPoint finalpoint) {}
	public void visitInitialPoint(InitialPoint initialPoint) { }
	public void visitSynchro(Synchro synchro) { }
	public void visitWorkProductRef(WorkProductRef ref) { }
	
	public void visitTransition(Transition transition)
	{
		mForeground = ColorFontPanel.getColor(ColorFontPanel.GUARD_KEY+"foreground") ;
		mBackground = ColorFontPanel.getColor(ColorFontPanel.GUARD_KEY+"background") ;
		mFont = ColorFontPanel.getStyle(ColorFontPanel.GUARD_KEY);
	}
}
