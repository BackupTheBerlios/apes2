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

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.utils.ResourceManager;

/**
 * Call the correponding action to an element of the model
 *
 * @version $Revision: 1.4 $
 */
public class InteractWithModelAction extends AbstractAction
{
	public InteractWithModelAction(String label)
	{
		super(ResourceManager.getInstance().getString(label));
	}

	public void actionPerformed(ActionEvent e)
	{
		ApesTreeNode selected = (ApesTreeNode) Context.getInstance().getTopLevelFrame().getTree().getSelectionPath().getLastPathComponent();
		SpemTreeAdapter model = (SpemTreeAdapter) Context.getInstance().getTopLevelFrame().getTree().getModel();
		
		model.elementAction(selected);
	}
}
