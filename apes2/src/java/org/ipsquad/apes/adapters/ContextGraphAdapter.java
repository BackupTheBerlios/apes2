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

import java.awt.Rectangle;
import java.util.Arrays;
import java.util.Map;
import java.util.Vector;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.model.extension.Link;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.jgraph.graph.DefaultEdge;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * This adapter allows to display a context diagram in a JGraph
 *
 * @version $Revision: 1.10 $
 */
public class ContextGraphAdapter extends SpemGraphAdapter 
{
	public ContextGraphAdapter( SpemDiagram diagram )
	{
		super( diagram );
		
		mBuilder = new Builder()
		{
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
					edge.setSource(getCellByUserObject(link.getSource(), null, false).getChildAt(0));
					edge.setTarget(getCellByUserObject(link.getTarget(), null, false).getChildAt(0));
					return edge;
				}
				return null;
			}

			public boolean shouldGoInGraph(Object o)
			{
				return ( o instanceof WorkProduct || o instanceof ProcessComponent || o instanceof Link );
			}	

			public void visitProcessComponent(ProcessComponent component) 
			{
				mCreated = new ProcessComponentCell( component );
			}
			
			public void visitProduct(WorkProduct product) 
			{
				mCreated = new WorkProductCell( product );
			}

			public void visitRole(ProcessRole role){ mCreated = null; }

			public void visitActivity(Activity activity){ mCreated = null; }
		
		};

		ApesGraphCell cell = (ApesGraphCell)mBuilder.create(Context.getInstance().getProject().getProcess().getComponent());
		Map apply = GraphConstants.createMap();
		Map attr = cell.getAttributes();
		Rectangle bounds = GraphConstants.getBounds(attr);
		bounds.setLocation(200,100);
		apply.put(cell,attr);
		super.insert(new Object[]{cell},null,null,null,null);		
	}
	
	public void remove(Object[] roots) 
	{
		Vector temp = new Vector( Arrays.asList(roots) );
		
		temp.remove(Context.getInstance().getProject().getProcess().getComponent());
		super.remove( temp.toArray() );
	}
	
	/*protected void inserted( InsertEvent e ) 
	{ 
		if( e.getDiagram() == mDiagram && e.getInserted() == null && (e.getAttributes() == null || !e.getAttributes().containsKey("firstPort") ) )
		{	
			ContextEdge edge = new ContextEdge();
			
			Object component = Context.getInstance().getProject().getProcess().getComponent();
			
			Object source = ( e.getSource() instanceof ProcessComponent ? component : e.getSource() );
			Object target = ( e.getTarget() instanceof ProcessComponent ? component : e.getTarget() );
			
			Port firstPort = null , 
						port = null;
			
			DefaultGraphCell cell = getCellByUserObject( source, null, false );
			if( cell != null )
			{	
				firstPort = (Port) ((ApesGraphCell)cell).getChildAt(0);
			}
			
			cell = getCellByUserObject( target, null, false );
			if( cell != null )
			{	
				port = (Port) ((ApesGraphCell)cell).getChildAt(0);
			}
			
			if( firstPort != null && port != null )
			{	
				ConnectionSet cs = new ConnectionSet();
				edge.setSource( firstPort);
				edge.setTarget( port );
				firstPort.addEdge( edge );
				port.addEdge( edge );
				cs.connect(edge, firstPort, port );
			
				super.insert(new Object[]{ edge },null,cs, null,null);
			}
		}
		/*else
		{
			super.inserted(e);
		}*/
	//}*/
}
