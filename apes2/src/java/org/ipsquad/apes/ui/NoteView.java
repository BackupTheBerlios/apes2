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
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;
import java.util.EventObject;
import java.util.Map;

import javax.swing.AbstractAction;
import javax.swing.AbstractCellEditor;
import javax.swing.BorderFactory;
import javax.swing.JComponent;
import javax.swing.JTextArea;
import javax.swing.KeyStroke;
import javax.swing.UIManager;
import javax.swing.border.Border;

import org.jgraph.JGraph;
import org.jgraph.graph.CellMapper;
import org.jgraph.graph.CellView;
import org.jgraph.graph.CellViewRenderer;
import org.jgraph.graph.DefaultGraphCellEditor;
import org.jgraph.graph.GraphCellEditor;
import org.jgraph.graph.GraphConstants;
import org.jgraph.graph.VertexView;

/**
 * Display a note cell
 *
 * @version $Revision: 1.4 $
 */
 
class NoteView extends VertexView 
{
		static final NoteRenderer renderer = new NoteRenderer();
		static final NoteEditor editor = new NoteEditor();

		public NoteView(Object cell, JGraph graph, CellMapper cm) 
		{
			super(cell, graph, cm);
		}

		public CellViewRenderer getRenderer() 
		{
			return renderer;
		}

		public GraphCellEditor getEditor() 
		{
			return editor;
		}

		static class NoteEditor extends DefaultGraphCellEditor 
		{
			class RealCellEditor extends AbstractCellEditor implements GraphCellEditor 
			{
				JTextArea editorComponent = new JTextArea();
				public RealCellEditor() 
				{
					editorComponent.setBorder(UIManager.getBorder("Tree.editorBorder"));
					editorComponent.setLineWrap(true);
					editorComponent.setWrapStyleWord(true);
					
					//substitute a JTextArea's VK_ENTER action with our own that will stop an edit.
					editorComponent.getInputMap(JComponent.WHEN_FOCUSED).put(KeyStroke.getKeyStroke(KeyEvent.VK_ENTER, 0), "enter");
					editorComponent.getActionMap().put("enter", new AbstractAction(){
						public void actionPerformed(ActionEvent e) 
						{
							stopCellEditing();
						}
					});
				}

				public Component getGraphCellEditorComponent(
						JGraph graph,
						Object value,
						boolean isSelected) 
				{
					editorComponent.setText(value.toString());
					editorComponent.selectAll();
					return editorComponent;
				}

				public Object getCellEditorValue() 
				{
					return editorComponent.getText();
				}

				public boolean stopCellEditing() 
				{
					//set the size of a vertex to that of an editor.
					CellView view = graph.getGraphLayoutCache().getMapping(graph.getEditingCell(), false);
					Map map = view.getAllAttributes();
					Rectangle cellBounds = GraphConstants.getBounds(map);
					Rectangle editingBounds = editorComponent.getBounds();
					GraphConstants.setBounds(map, new Rectangle(cellBounds.x, cellBounds.y, editingBounds.width, editingBounds.height));
					return super.stopCellEditing();
				}

				public boolean shouldSelectCell(EventObject event) 
				{
					editorComponent.requestFocus();
					return super.shouldSelectCell(event);
				}
			}

			public NoteEditor() 
			{
				super();
			}
			/**
			 * Overriding this in order to set the size of an editor to that of an edited view.
			 */
			public Component getGraphCellEditorComponent(
					JGraph graph,
					Object cell,
					boolean isSelected) 
			{

				Component component = super.getGraphCellEditorComponent(graph, cell, isSelected);

				//set the size of an editor to that of a view
				CellView view = graph.getGraphLayoutCache().getMapping(cell, false);
				editingComponent.setBounds(view.getBounds());
				Color background = GraphConstants.getBackground(view.getAllAttributes());
				editingComponent.setBackground((background != null) ? background : graph.getBackground());
				Color foreground = GraphConstants.getForeground(view.getAllAttributes());
				editingComponent.setForeground((foreground != null) ? foreground : graph.getForeground());
				//I have to set a font here instead of in the RealCellEditor.getGraphCellEditorComponent() because
				//I don't know what cell is being edited when in the RealCellEditor.getGraphCellEditorComponent().
				Font font = GraphConstants.getFont(view.getAllAttributes());
				editingComponent.setFont((font != null) ? font : graph.getFont());

				return component;
			}

			protected GraphCellEditor createGraphCellEditor() 
			{
				return new NoteEditor.RealCellEditor();
			}

			/**
			 * Overriting this so that I could modify an eiditor container.
			 * see <http://sourceforge.net/forum/forum.php?thread_id=781479>&forum_id=140880
			 */
			protected Container createContainer() 
			{
				return new NoteEditor.ModifiedEditorContainer();
			}

			class ModifiedEditorContainer extends EditorContainer 
			{
				public void doLayout() 
				{
					super.doLayout();
					//substract 2 pixels that were added to the preferred size of the container for the border.
					Dimension cSize = getSize();
					Dimension dim = editingComponent.getSize();
					editingComponent.setSize(dim.width - 2, dim.height);

					//reset container's size based on a potentially new preferred size of a real editor.
					setSize(cSize.width, getPreferredSize().height);
				}
			}
		}

		static class NoteRenderer extends JTextArea implements CellViewRenderer 
		{
			{
				setLineWrap(true);
				setWrapStyleWord(true);
			}

			public Component getRendererComponent(
					JGraph graph,
					CellView view,
					boolean sel,
					boolean focus,
					boolean preview) 
			{
				setText(view.getCell().toString());

				Map attributes = view.getAllAttributes(); 
				installAttributes(graph, attributes);
				return this;
			}

			protected void installAttributes(JGraph graph, Map attributes) 
			{
				Color foreground = GraphConstants.getForeground(attributes);
				setForeground((foreground != null) ? foreground : graph.getForeground());
				Color background = GraphConstants.getBackground(attributes);
				setBackground((background != null) ? background : graph.getBackground());
				Font font = GraphConstants.getFont(attributes);
				setFont((font != null) ? font : graph.getFont());
				Border border= GraphConstants.getBorder(attributes);
				Color bordercolor = GraphConstants.getBorderColor(attributes);
				if(border != null)
					setBorder(border);
				else if (bordercolor != null) 
				{
					int borderWidth = Math.max(1, Math.round(GraphConstants.getLineWidth(attributes)));
					setBorder(BorderFactory.createLineBorder(bordercolor, borderWidth));
				}
			}
		}
			
	}

	
	

