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
import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.processing.LoadProject;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.SimpleFileFilter;
import org.ipsquad.utils.SmartChooser;
import org.ipsquad.utils.TaskMonitorDialog;

/**
 * Open a new project in the application
 *
 * @version $Revision: 1.4 $
 */
public class OpenAction extends ProjectManagementAction
{
	private SimpleFileFilter filter = new SimpleFileFilter(ResourceManager.getInstance().getString("apesFileExtension"),
	                                                       ResourceManager.getInstance().getString("apesFileType"));

	private LoadProject mMonitor = null;
	private TaskMonitorDialog mTask = null;
	private File mFile = null;
	private ResourceManager mResource = ResourceManager.getInstance();
	
	public OpenAction()
	{
		super("fileOpen", "icons/FileOpen.gif", 'O', Event.CTRL_MASK);
	}

	private void openProject()
	{
		try
		{
			SmartChooser chooser = SmartChooser.getChooser();
			chooser.setAcceptAllFileFilterUsed(false);
			chooser.setFileFilter(filter);
			chooser.setDirectory(ConfigManager.getInstance().getProperty("WorkspaceTitledefaultPath"));
			
			if(chooser.showOpenDialog(((ApesFrame)context.getTopLevelFrame()).getContentPane())==JFileChooser.APPROVE_OPTION)
			{
				if(!filter.accept(chooser.getSelectedFile()) || chooser.getSelectedFile().isDirectory())
				{
					ErrorManager.getInstance().display("errorTitleOpenProcess", "errorWrongFileName");
					return;
				}
				
				mFile = chooser.getSelectedFile();
				
				mMonitor = new LoadProject(mFile);
				
				ApesFrame parent = (ApesFrame)Context.getInstance().getTopLevelFrame();
				
				mTask = new TaskMonitorDialog(parent,mMonitor);
				mTask.setName("Loading");
				mTask.setLocation(parent.getWidth()/2-mTask.getWidth()/2,parent.getHeight()/2-mTask.getHeight()/2);
				
				mMonitor.setTask(mTask);
				
				mTask.show();
			}
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			ErrorManager.getInstance().display("errorTitleOpenProcess", "errorOpenProcess");
		}
	}
	
	
	public void actionPerformed(ActionEvent e)
	{
		if(!context.getAction("Undo").isEnabled())
		{
			openProject();
		}
		else
		{
			//Test if a project is currently opened
			if(context.getProject()!=null)
			{
				//Display the dialogBox for a new process
				int choice=JOptionPane.showInternalConfirmDialog(
				   ((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),
				   ResourceManager.getInstance().getString("msgOpenProcessConfirm"),
				   ResourceManager.getInstance().getString("msgTitleOpenProcess"),
				   JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);

				if(choice==JOptionPane.YES_OPTION)
				{
					if(!saveProject()) return;
				}

				if(choice!=JOptionPane.CANCEL_OPTION)
				{
					openProject();
				}
			}
			else
			{
				openProject();
			}
		}
	}
}
