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

import java.awt.event.ActionEvent;
import java.awt.event.KeyEvent;

import javax.swing.JDesktopPane;

import org.ipsquad.apes.ui.GraphFrame;
import org.jgraph.JGraph;

/**
 * Remove the selection from the current graph
 *
 * @version $Revision: 1.4 $
 */
public class RemoveAction extends ApesAction
{
	public RemoveAction()
	{
		super("editRemove", "icons/Empty.gif", KeyEvent.VK_DELETE, 0);
	}

	public void actionPerformed(ActionEvent e)
	{
		JDesktopPane desktop = context.getTopLevelFrame().getDesktop();
		GraphFrame frame = (GraphFrame)desktop.getSelectedFrame();
		JGraph graph = frame.getGraph();
		Object[] cells = graph.getSelectionModel().getSelectionCells();
		
		graph.getModel().remove(cells);
		
	}
}
