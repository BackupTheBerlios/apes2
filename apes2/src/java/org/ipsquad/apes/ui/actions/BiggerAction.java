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
import java.awt.event.KeyEvent;

import org.ipsquad.apes.ui.GraphFrame;

/**
 * Scale up the currently displayed graph
 *
 * @version $Revision: 1.4 $
 */
public class BiggerAction extends ApesAction
{
	public BiggerAction()
	{
		super("displayBigger", "icons/DisplayBigger.gif", KeyEvent.VK_ADD, Event.CTRL_MASK);
	}

	public void actionPerformed(ActionEvent e)
	{
		GraphFrame g = (GraphFrame)context.getTopLevelFrame().getDesktop().getSelectedFrame();
		
		if(g!=null)
		{
			double scale = g.getGraph().getScale();
			g.getGraph().setScale(scale*1.2);
		}
	}
}
