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

import javax.swing.AbstractAction;
import javax.swing.tree.TreePath;

import org.ipsquad.apes.Context;
import org.ipsquad.utils.ResourceManager;

/**
 * Rename the currently selected element from the tree
 *
 * @version $Revision: 1.6 $
 */
public class TreeRenameAction extends AbstractAction
{
	public TreeRenameAction(String label)
	{
		super(ResourceManager.getInstance().getString(label));
	}

	public void actionPerformed(ActionEvent e)
	{
		TreePath selected = Context.getInstance().getTopLevelFrame().getTree().getSelectionPath();
		
		
		if(selected!=null)
		{
			Context.getInstance().getTopLevelFrame().getTree().startEditingAtPath(selected);
		}
	}
}
