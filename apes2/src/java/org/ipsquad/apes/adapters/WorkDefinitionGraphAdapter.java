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

import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;

/**
 * This adapter allows to display a work definition diagram in a JGraph
 *
 * @version $Revision: 1.12 $
 */
public class WorkDefinitionGraphAdapter extends SpemGraphAdapter 
{
	public WorkDefinitionGraphAdapter( SpemDiagram diagram )
	{
		super( diagram );
		
		mBuilder = new Builder( ) {
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
					DefaultEdge edge = new DefaultEdge();
					edge.changeAttributes(getDefaultEdgeAttributes());					
					edge.setSource(getCellByUserObject(link.getSource(), null, false).getChildAt(0));
					edge.setTarget(getCellByUserObject(link.getTarget(), null, false).getChildAt(0));
					mCreated = edge;
				}
				else if( o instanceof WorkDefinitionDiagram.Transition )
				{
					WorkDefinitionDiagram.Transition link = (WorkDefinitionDiagram.Transition)o;
					DefaultEdge edge = new DefaultEdge();
					edge.setSource(getCellByUserObject(link.getInputModelElement(), null, false).getChildAt(0));
					edge.setTarget(getCellByUserObject(link.getOutputModelElement(), null, false).getChildAt(0));
					mCreated = edge;
				}
				
				return mCreated;
			}

			public boolean shouldGoInGraph(Object o)
			{
				if( o instanceof WorkProduct || o instanceof ProcessRole || o instanceof WorkDefinition || o instanceof StateMachine || o instanceof Link)
				{
					return true;
				}
				return false;
			}
			
			public void visitProduct(WorkProduct product) 
			{
				mCreated = new WorkProductCell( product );
			}

			public void visitRole(ProcessRole role) 
			{
				mCreated = new ProcessRoleCell( role );
			}
			
			public void visitWorkDefinition(WorkDefinition workDefinition) 
			{
				mCreated = new WorkDefinitionCell( workDefinition );
			}			
			
			public void visitStateMachine( StateMachine sm )
			{
				mCreated = new WorkProductStateCell(sm); 
			}
			
			public void visitActivity(Activity activity) { mCreated = null; }
			
		};
	}
}
