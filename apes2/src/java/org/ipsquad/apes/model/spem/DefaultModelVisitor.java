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
package org.ipsquad.apes.model.spem;

import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.extension.ActivityDiagram.Decision;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.InitialPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.Synchro;
import org.ipsquad.apes.model.extension.ActivityDiagram.Transition;

/**
 *
 * @version $Revision: 1.2 $
 */
public class DefaultModelVisitor extends DefaultSpemVisitor implements ModelVisitor 
{
	public void visitDecision(Decision decision) 
	{
		visitModelElement( decision );
	}

	public void visitFinalPoint(FinalPoint finalpoint) 
	{
		visitModelElement( finalpoint );
	}

	public void visitInitialPoint(InitialPoint initialPoint) 
	{
		visitModelElement( initialPoint );
	}

	public void visitSynchro(Synchro synchro) 
	{
		visitModelElement( synchro );
	}

	public void visitTransition(Transition transition) 
	{
	}

	public void visitApesProcess(ApesProcess p)
	{
		visitModelElement(p);
	}
	
	public void visitWorkProductRef(WorkProductRef ref) 
	{
		visitModelElement(ref);
	}
}
