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
		private Color mInitForeground = null;
		private Color mCurrentForeground = null;
		
		private Color mInitBackground = null;
		private Color mCurrentBackground = null;
		
		private Font mInitFont = null;
		private Font mCurrentFont = null;
		
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
			
			mInitForeground = GraphConstants.getForeground(((GraphCell)mCell).getAttributes());
			mInitBackground = GraphConstants.getBackground(((GraphCell)mCell).getAttributes());
			mInitFont = mCurrentFont = GraphConstants.getFont(((GraphCell)mCell).getAttributes());
			
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
				Color fc = mCurrentForeground == null ? mInitForeground : mCurrentForeground;
				Color bc = mCurrentBackground == null ? mInitBackground : mCurrentBackground;
				Font font = mCurrentFont == null ? mInitFont : mCurrentFont;
				
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
			if(editingComponent instanceof JTextField)
			{
				if( c == mActionForeground.getColor() )
				{	
					mCurrentForeground = c;
				}
				else
				{
					mCurrentBackground = c;
				}
				
				getGraphCellEditorComponent(graph,mCell,true);
			}
		}
		
		public void italicChanged(boolean newValue) 
		{
			if(editingComponent instanceof JTextField && mCurrentFont != null)
			{
				int style = mCurrentFont.getStyle();
				
				if( newValue )
				{
					style += Font.ITALIC;
				}
				else
				{
					style -= Font.ITALIC;
				}
				
				mCurrentFont = new Font(mCurrentFont.getName(),style,mCurrentFont.getSize());
				
				getGraphCellEditorComponent(graph,mCell,true);
			}
		}
		
		public void boldChanged(boolean newValue) 
		{
			if(editingComponent instanceof JTextField && mCurrentFont != null)
			{
				int style = mCurrentFont.getStyle();
				
				if( newValue )
				{
					style += Font.BOLD;
				}
				else
				{
					style -= Font.BOLD;
				}
				
				mCurrentFont = new Font(mCurrentFont.getName(),style,mCurrentFont.getSize());
				
				getGraphCellEditorComponent(graph,mCell,true);
			}
		}
		
		public void editingCanceled(ChangeEvent e)
		{
			mCurrentForeground = null;
			mCurrentBackground = null;
			mCurrentFont = null;
			
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
			mActionItalic.setEnabled(false);
			mActionBold.setEnabled(false);
		}

		public void editingStopped(ChangeEvent e)
		{
			Map map = GraphConstants.cloneMap(mCell.getAttributes());
			boolean hasChanged = false;
			
			if( mCurrentForeground != null && mInitForeground != mCurrentForeground )
			{
				GraphConstants.setForeground(map,mCurrentForeground);
				hasChanged = true;
			}
			if( mCurrentBackground != null &&  mInitBackground != mCurrentBackground )
			{
				GraphConstants.setBackground(map,mCurrentBackground);
				hasChanged = true;
			}
			if(  mCurrentFont != null && mInitFont.getStyle() != mCurrentFont.getStyle() )
			{
				GraphConstants.setFont(map,mCurrentFont);
				hasChanged = true;
			}
			
			if( hasChanged )
			{	
				Map edit = new HashMap();
				edit.put(mCell,map);
				//getModel().edit(edit,null,null,null);
				graph.getGraphLayoutCache().edit(edit,null,null,null);
			}
			
			mCurrentForeground = null;
			mCurrentBackground = null;
			mCurrentFont = null;
			
			mActionForeground.setEnabled(false);
			mActionBackground.setEnabled(false);
			mActionItalic.setEnabled(false);
			mActionBold.setEnabled(false);
		}
}
