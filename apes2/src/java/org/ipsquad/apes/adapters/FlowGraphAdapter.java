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

import java.util.Map;
import java.util.Vector;

import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.jgraph.graph.ConnectionSet;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphCell;

/**
 * This adapter allows to display a flow diagram in a JGraph
 *
 * @version $Revision: 1.12 $
 */
public class FlowGraphAdapter extends SpemGraphAdapter
{
	private static final int UNKNOWN_ACTION = 0;
	private static final int INSERT_ACTION = 1;
	private static final int REMOVE_ACTION = 2;
	
	/**
	 * mRoleActivityLinkType specifies is used to identify which type of link role -> activity to create (undefined, performer or assistant)
	 */
	protected int mRoleActivityLinkType = FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE;
	protected int mActionType = UNKNOWN_ACTION;
	
	public FlowGraphAdapter(SpemDiagram diagram)
	{
		super(diagram);
		
		mBuilder = new Builder( ) {
			public DefaultGraphCell create( Object o )
			{
				if( o instanceof Element )
				{
					((Element)o).visit( this );
					return mCreated;
				}
				else if( o instanceof Link )
				{
					Link link = (Link)o;
					DefaultEdge edge = new DefaultEdge();
					edge.changeAttributes(getDefaultEdgeAttributes());
					edge.setSource(getCellByUserObject(link.getSource(), null, false).getChildAt(0));
					edge.setTarget(getCellByUserObject(link.getTarget(), null, false).getChildAt(0));
					return edge;
				}
				return null;
			}

			public boolean shouldGoInGraph(Object o)
			{
				if( o instanceof Activity 
						|| o instanceof ProcessRole
						|| o instanceof WorkProduct
						|| o instanceof StateMachine
						|| o instanceof Link)
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

			public void visitActivity(Activity activity) 
			{
				mCreated = new ActivityCell( activity );
			}
			
			public void visitStateMachine( StateMachine sm )
			{
				mCreated = new WorkProductStateCell(sm); 
			}

		};
	}
	
	protected Link createLink(ApesGraphCell source, ApesGraphCell target, Object extra)
	{
		if(mActionType == INSERT_ACTION)
		{
			return new Link(source.getUserObject(), target.getUserObject(), "", new Integer(getRoleActivityLinkType()));
		}
		else if(mActionType == REMOVE_ACTION)
		{
			if(source instanceof ProcessRoleCell
					&& target instanceof ActivityCell)
			{
				if(mDiagram.existsLinkModelElements(
						(ModelElement)source.getUserObject(), 
						(ModelElement)target.getUserObject(), 
						new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)))
				{
					return new Link(source.getUserObject(), target.getUserObject(), "",new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE));	
				}
				else if(mDiagram.existsLinkModelElements(
						(ModelElement)source.getUserObject(), 
						(ModelElement)target.getUserObject(), 
						new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE)))
				{
					return new Link(source.getUserObject(), target.getUserObject(), "",new Integer(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE));
				}
			}
		}

		return super.createLink(source, target, null);
	}
	
	/**
	 * Tries to define the type of the edge.
	 * Updates the variable mRoleActivityLinkType.
	 * If the type of the edge can not be determinate, sets mRoleActivityLinkType to ROLE_ACTIVITY_UNDEFINED_LINK_TYPE
	 * 
	 * @param edge the edge to define its type
	 * @return false if, after update, mRoleActivityLinkType is ROLE_ACTIVITY_UNDEFINED_LINK_TYPE, true otherwise
	 */
	public boolean updateType(DefaultEdge edge)
	{
		if(!(edge instanceof NoteEdge))
		{
			ApesGraphCell source = (ApesGraphCell)getParent(edge.getSource());
			ApesGraphCell target = (ApesGraphCell)getParent(edge.getTarget());
		
			if(source.getUserObject() instanceof ProcessRole
					&& target.getUserObject() instanceof Activity)
			{
				// when the activity already has a performer, the only possibility is to create an assistant
				// otherwise we can't determinate the type of the edge (assistant or performer) 
				
				ProcessRole r = (ProcessRole)source.getUserObject();
				Activity a = (Activity)target.getUserObject();
				
				if(a.getOwner() != null && mDiagram.existsLinkModelElements(a.getOwner(), a, new Integer(FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE)))
				{
					setRoleActivityLinkType(FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE);
				}
				
				return getRoleActivityLinkType() != FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE;
			}
		}	
		return true;
	}
	
	protected void insertElement(Object object, ConnectionSet cs, Vector notes, Vector noteEdges, Vector elements, Map matching) 
	{
		mActionType = INSERT_ACTION;
		
		if(object instanceof DefaultEdge)
		{
			updateType((DefaultEdge)object);
		}
		
		super.insertElement(object,cs,notes,noteEdges,elements,matching);
		
		mActionType = UNKNOWN_ACTION;
	}
	
	protected void removeElement(GraphCell cell, Vector notes, Vector noteEdges, Vector elements, Map matching) 
	{
		mActionType = REMOVE_ACTION;
		
		super.removeElement(cell, notes, noteEdges, elements, matching);
		
		mActionType = UNKNOWN_ACTION;
	}
	
	public void setRoleActivityLinkType(int type)
	{
		mRoleActivityLinkType = type;
	}
	
	public int getRoleActivityLinkType()
	{
		return mRoleActivityLinkType;
	}
}


