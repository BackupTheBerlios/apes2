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


package org.ipsquad.apes.adapters;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.Link;
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
import org.jgraph.graph.DefaultGraphCell;

/**
 * This adapter allows to display an activity diagram in a JGraph
 *
 * @version $Revision: 1.10 $
 */
public class ActivityGraphAdapter extends SpemGraphAdapter
{
	public ActivityGraphAdapter(SpemDiagram diagram )
	{
		super(diagram);
		mBuilder = new Builder(){
			public DefaultGraphCell create( Object o )
			{
				mCreated = null;
				if( o instanceof Element )
				{
					((Element)o).visit( this );
				}
				else if( o instanceof Link )
				{
					Link link = (Link)o;
					TransitionEdge edge = new TransitionEdge();
					edge.setSource(getCellByUserObject(link.getSource(), null, false).getChildAt(0));
					edge.setTarget(getCellByUserObject(link.getTarget(), null, false).getChildAt(0));
					mCreated = edge;
				}
				else if( o instanceof ActivityDiagram.Transition )
				{
					((ActivityDiagram.Transition)o).visit( this );
				}
				return mCreated;
			}

			public boolean shouldGoInGraph(Object o)
			{
				return (o instanceof Activity
					|| o instanceof ActivityDiagram.Decision
					|| o instanceof ActivityDiagram.FinalPoint
					|| o instanceof ActivityDiagram.InitialPoint
					|| o instanceof ActivityDiagram.Synchro
					|| o instanceof ActivityDiagram.Transition
					|| o instanceof Link);
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
	
//	public DefaultGraphCell getCellByUserObject(Object userObject, Map attr, boolean create)
//    {
//		DefaultGraphCell cell = super.getCellByUserObject(userObject, attr, create);
//		if(cell != null && cell instanceof DefaultEdge && userObject instanceof Link)
//		{
//			DefaultEdge edge = (DefaultEdge)cell;
//			Link link = (Link)userObject;
//			if(create)
//			{
//				if(link.getSource() instanceof ModelElement && link.getTarget() instanceof ModelElement)
//				{
//					cell = new TransitionEdge(new ActivityDiagram.Transition((ModelElement)link.getSource(),(ModelElement)link.getTarget(),""));
//					((TransitionEdge)cell).setSource(edge.getSource());
//					((TransitionEdge)cell).setTarget(edge.getTarget());
//				}
//			}
//			else
//			{
//				for(int i = 0; i < getRootCount(); i++)
//				{
//					if(getRootAt(i) instanceof TransitionEdge)
//					{
//						edge = (TransitionEdge) getRootAt(i);
//						ActivityDiagram.Transition transition = (ActivityDiagram.Transition)edge.getUserObject();
//						if(transition.getInputModelElement() == link.getSource() && transition.getOutputModelElement() == link.getTarget())
//						{
//							cell = edge;
//						}
//					}
//				}
//			}
//		}
//		return cell;
//    }
}


