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

import java.awt.Color;
import java.awt.Font;
import java.awt.Rectangle;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Document;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.ui.ColorFontPanel;
import org.jgraph.graph.DefaultGraphCell;
import org.jgraph.graph.DefaultPort;
import org.jgraph.graph.GraphConstants;

/**
 *
 * @version $Revision: 1.5 $
 */
public class NoteCell extends DefaultGraphCell
{
	public Map changeAttributes(Map change)
	{
		Document doc = (Document)userObject;
		
		Map undo = GraphConstants.applyMap(change,attributes);
		
		Object value = GraphConstants.getValue(change); 
		
		if( value != null && !value.toString().equals(doc.toString()) )
		{
			
			System.out.println("cahngeText");
			try{
			doc.remove(0,doc.getLength());
			doc.insertString(0,value.toString(),null);
			userObject=doc;
			}catch(Throwable t){t.printStackTrace();}
		}
		
		return undo;
	}

	public NoteCell() 
	{
		super(new DefaultStyledDocument());
		init();
	}
	
	public NoteCell(Document userObject)
	{
		super(userObject);
		init();
	}
	
	protected void init()
	{
		add(new DefaultPort());
		((Document)userObject).addUndoableEditListener(Context.getInstance().getUndoManager());
		// Create a Map that holds the attributes for the Vertex
		Map map = GraphConstants.createMap();
		// Add a Bounds Attribute to the Map
		GraphConstants.setBounds(map, new Rectangle(50, 50, 130, 32));
		// Even though it is opaque, set it to transparent so that renderer's super.paint() won't paint background.
		GraphConstants.setOpaque(map, false);
		//resizable cells.
		GraphConstants.setSizeable(map, true);
		//outline it with a border.
		GraphConstants.setBorder(map, BorderFactory.createLineBorder(Color.BLACK, 2));
		GraphConstants.setForeground(map,ColorFontPanel.getColor(ColorFontPanel.NOTES_KEY+"foreground"));
		GraphConstants.setBackground(map,ColorFontPanel.getColor(ColorFontPanel.NOTES_KEY+"background"));
		GraphConstants.setFont(map,new Font((String)map.get(GraphConstants.EDITABLE),ColorFontPanel.getStyle(ColorFontPanel.NOTES_KEY),13));
		changeAttributes(map);
	}
	
	public Object clone()
	{
		NoteCell c = (NoteCell) super.clone();
		c.userObject = new DefaultStyledDocument();
		init();
		return c;
	}
	
	public String toString()
	{
		if( userObject instanceof Document )
		{
			try
			{
				return ((Document)userObject).getText(0,((Document)userObject).getLength());
			}
			catch(Throwable t)
			{
				t.printStackTrace();
			}
		}
		return "";
	}
}
