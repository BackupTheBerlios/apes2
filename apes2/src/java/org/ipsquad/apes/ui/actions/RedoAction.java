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

package org.ipsquad.apes.ui.actions;

import java.awt.Event;
import java.awt.event.ActionEvent;

import javax.swing.JInternalFrame;

import org.ipsquad.apes.ui.GraphFrame;
import org.jgraph.JGraph;

/**
 * Redo the last undone modification
 *
 * @version $Revision: 1.6 $
 */
public class RedoAction extends ApesAction
{
	public RedoAction()
	{
		super("editRedo", "icons/EditRedo.gif", 'Z', Event.CTRL_MASK|Event.SHIFT_MASK);
	}

	public void actionPerformed(ActionEvent e)
	{
		try {
			context.getUndoManager().redo(null);
			context.updateActions();
			JInternalFrame[] frames = context.getTopLevelFrame().getDesktop().getAllFrames();
			for(int i=0; i<frames.length; i++)
			{
				if(frames[i] instanceof GraphFrame)
				{
					JGraph graph = ((GraphFrame)frames[i]).getGraph();
					graph.setModel(graph.getModel());
				}
			}
		} 
		catch (Exception ex) 
		{
			ex.printStackTrace();
		} 
	}
}
