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

import javax.swing.JOptionPane;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.utils.ResourceManager;
/**
 * Create a new project in the application
 *
 * @version $Revision: 1.4 $
 */
public class NewAction extends ProjectManagementAction
{
	public NewAction()
	{
		super("fileNew", "icons/FileNew.gif", 'N', Event.CTRL_MASK);
	}

	public void actionPerformed(ActionEvent e)
	{    
		if(!context.getAction("Undo").isEnabled())
		{
			Project newProject = new Project();
			context.setProject(newProject);
			context.setFilePath(null);
		}
		else
		{
			//Test if a project is currently opened
			if(context.getProject()!=null)
			{
				//Display the dialogBox for a new process
				int choice=JOptionPane.showInternalConfirmDialog(
				   ((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),
				   ResourceManager.getInstance().getString("msgNewProcessConfirm"),
				   ResourceManager.getInstance().getString("msgTitleNewProcess"),
				   JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if(choice==JOptionPane.YES_OPTION)
				{
					if(!saveProject()) return;
				}

				if(choice!=JOptionPane.CANCEL_OPTION)
				{
					Project newProject=new Project();
					context.setProject(newProject);
					context.setFilePath(null);
				}
			}
			else
			{
				Project newProject=new Project();
				context.setProject(newProject);
				context.setFilePath(null);
			}
		}
	}
}
