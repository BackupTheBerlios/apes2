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

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.ActivityDiagram.Decision;
import org.ipsquad.apes.model.extension.ActivityDiagram.FinalPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.InitialPoint;
import org.ipsquad.apes.model.extension.ActivityDiagram.Synchro;
import org.ipsquad.apes.model.extension.ActivityDiagram.Transition;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

/**
 * This adapter allows to display an activity diagram in a JGraph
 *
 * @version $Revision: 1.3 $
 */
public class ActivityGraphAdapter extends SpemGraphAdapter
{
	public ActivityGraphAdapter(SpemDiagram diagram )
	{
		super(diagram);
		mBuilder = new Builder(){
			public Object create( Object o )
			{
				if( o instanceof Element )
				{
					((Element)o).visit( this );
					return mCreated;
				}
				else if( o instanceof ActivityDiagram.Transition )
				{
					((ActivityDiagram.Transition)o).visit( this );
					return mCreated;
				}
				return null;
			}

			public boolean shouldGoInGraph(Object o)
			{
				return (o instanceof Activity
					|| o instanceof ActivityDiagram.Decision
					|| o instanceof ActivityDiagram.FinalPoint
					|| o instanceof ActivityDiagram.InitialPoint
					|| o instanceof ActivityDiagram.Synchro
					|| o instanceof ActivityDiagram.Transition);
			}

			public void visitProduct(WorkProduct product) { mCreated = null; }

			public void visitRole(ProcessRole role){ mCreated = null; }
			
			public void visitActivity(Activity activity) 
			{
				mCreated = new ActivityCell( activity );
			}

			public void visitDecision( Decision decision ) 
			{
				mCreated = new DecisionCell( decision );
			}
			
			public void visitFinalPoint(FinalPoint finalPoint ) 
			{
				mCreated = new FinalPointCell( finalPoint );
			}
			
			public void visitInitialPoint(InitialPoint initialPoint) 
			{
				mCreated = new InitialPointCell( initialPoint );
			}
			
			public void visitSynchro(Synchro synchro) 
			{
				mCreated = new SynchroCell( synchro );
			}
			
			public void visitTransition(Transition transition)
			{
				mCreated = new TransitionEdge( transition );
			}	
			
			
		};
	}
}


