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
import org.ipsquad.apes.model.extension.WorkProductRef;

/**
 * Interface for visiting the model
 * For more details see the visitor design pattern
 * 
 * @version $Revision: 1.1 $
 */
public interface ModelVisitor extends SpemVisitor {
	public void visitApesProcess(ApesProcess p);
	public void visitWorkProductRef(WorkProductRef ref);
	public void visitDecision( ActivityDiagram.Decision decision );
	public void visitFinalPoint( ActivityDiagram.FinalPoint finalpoint );
	public void visitInitialPoint( ActivityDiagram.InitialPoint initialPoint );
	public void visitSynchro( ActivityDiagram.Synchro synchro );
	public void visitTransition( ActivityDiagram.Transition transition );
}
