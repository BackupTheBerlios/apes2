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

package org.ipsquad.apes.ui.actions;

import java.awt.event.ActionEvent;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.processing.RouteProject;
import org.ipsquad.apes.processing.ValidateVisitor;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.utils.TaskMonitorDialog;

/**
 * Validate the current project
 *
 * @version $Revision: 1.3 $
 */
public class ValidateAction extends ApesAction
{
	
	public ValidateAction()
	{
		super("toolsValidate", "icons/ToolsValidate.gif");
	}

	public void actionPerformed(ActionEvent e)
	{
		ValidateVisitor vv = new ValidateVisitor();
				
		RouteProject mMonitor = new RouteProject(vv);
		
		ApesFrame parent = (ApesFrame)Context.getInstance().getTopLevelFrame();
		
		TaskMonitorDialog mTask = new TaskMonitorDialog(parent,mMonitor);
		mTask.setName("Validate");
		mTask.setBounds(parent.getWidth()/2-mTask.getWidth()/2,parent.getHeight()/2-mTask.getHeight()/2,300,300);
		
		mMonitor.setTask(mTask);
		
		mTask.show();
	}
}
