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
package org.ipsquad.apes.ui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Font;
import java.util.HashMap;
import java.util.Map;

import javax.swing.JTextField;
import javax.swing.event.CellEditorListener;
import javax.swing.event.ChangeEvent;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.ui.actions.ChangeBoldAction;
import org.ipsquad.apes.ui.actions.ChangeColorAction;
import org.ipsquad.apes.ui.actions.ChangeItalicAction;
import org.jgraph.JGraph;
import org.jgraph.graph.DefaultGraphCellEditor;
import org.jgraph.graph.GraphCell;
import org.jgraph.graph.GraphConstants;

/**
 * 
 * @version $revision$
 */
public class ApesGraphCellEditor extends DefaultGraphCellEditor 
	implements ChangeColorAction.ColorChangeListener, 
		ChangeItalicAction.ItalicChangeListener, 
		ChangeBoldAction.BoldChangeListener, 
		CellEditorListener
	{
		private ChangeColorAction mActionForeground = (ChangeColorAction)Context.getInstance().getAction("ChangeForeground");
		private ChangeColorAction mActionBackground = (ChangeColorAction)Context.getInstance().getAction("ChangeBackground");
		private ChangeItalicAction mActionItalic = (ChangeItalicAction)Context.getInstance().getAction("ChangeItalic");
		private ChangeBoldAction mActionBold = (ChangeBoldAction)Context.getInstance().getAction("ChangeBold");
		
		private GraphCell mCell;
		
		public ApesGraphCellEditor( GraphCell c )
		{
			super();
			mCell = c;
			
			mActionForeground.addChangeColorListener(this);	
			mActionBackground.addChangeColorListener(this);	
			mActionItalic.addChangeItalicListener(this);
			mActionBold.addChangeBoldListener(this);

			addCellEditorListener(this);
			
			mActionForeground.setEnabled(true);
			mActionBackground.setEnabled(true);
			mActionItalic.setEnabled(true);
			mActionBold.setEnabled(true);
		}
		
		public Component getGraphCellEditorComponent(
				JGraph graph,
				Object value,
				boolean isSelected)
		{
			Component c = super.getGraphCellEditorComponent(graph, mCell, isSelected);
			
			if( editingComponent instanceof JTextField && value instanceof GraphCell )
			{
				Color fc =GraphConstants.getForeground(mCell.getAttributes());
				Color bc =GraphConstants.getBackground(mCell.getAttributes());
				font =GraphConstants.getFont(mCell.getAttributes());
				
				mActionForeground.setColor(fc);
				mActionBackground.setColor(bc);
				
				((JTextField)editingComponent).setForeground(fc);
				((JTextField)editingComponent).setSelectedTextColor(fc);
				((JTextField)editingComponent).setBackground(bc);
				((JTextField)editingComponent).setFont( font );
				((JTextField)editingComponent).selectAll();
			}
			
			return c;
		}

		public void colorChanged(Color c)
		{
			if(editingComponent != null)
			{
				if( c == mActionForeground.getColor() )
				{	
					editingComponent.setForeground(c);
				}
				else
				{
					editingComponent.setBackground(c);
				}
			}
		}
		
		public void italicChanged(boolean newValue) 
		{
			if(editingComponent != null)
			{
				int style = font.getStyle();
				
				if( newValue )
				{
					style += Font.ITALIC;
				}
				else
				{
					style -= Font.ITALIC;
				}
				font = new Font(font.getName(),style, font.getSize());
				editingComponent.setFont(font);
			}
		}
		
		public void boldChanged(boolean newValue) 
		{
			if(editingComponent != null)
			{
				int style = font.getStyle();
				
				if( newValue )
				{
					style += Font.BOLD;
				}
				else
				{
					style -= Font.BOLD;
				}
				font = new Font(font.getName(),style, font.getSize());
				editingComponent.setFont(font);
			}
		}
		
		public void editingCanceled(ChangeEvent e)
		{
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
			mActionItalic.setEnabled(false);
			mActionBold.setEnabled(false);
		}

		public void editingStopped(ChangeEvent e)
		{
			Map map = GraphConstants.cloneMap(mCell.getAttributes());
			boolean hasChanged = false;
			
			if(GraphConstants.getForeground(mCell.getAttributes()) != editingComponent.getForeground())
			{
				GraphConstants.setForeground(map,editingComponent.getForeground());
				hasChanged = true;
			}
			if(GraphConstants.getBackground(mCell.getAttributes()) != editingComponent.getBackground())
			{
				GraphConstants.setBackground(map,editingComponent.getBackground());
				hasChanged = true;
			}
			if(  GraphConstants.getFont(mCell.getAttributes()) != editingComponent.getFont()) 
			{
				GraphConstants.setFont(map,editingComponent.getFont());
				hasChanged = true;
			}
			
			if( hasChanged )
			{	
				Map edit = new HashMap();
				edit.put(mCell,map);
				graph.getGraphLayoutCache().edit(edit,null,null,null);
			}
			
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
			mActionItalic.setEnabled(false);
			mActionBold.setEnabled(false);
		}
}
