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


/**
 * Save the current project
 *
 * @version $Revision: 1.8 $
 */
public class SaveAction extends ProjectManagementAction
{
	public SaveAction()
	{
		super("fileSave", "icons/FileSave.gif", 'S', Event.CTRL_MASK);
	}

	
	public void actionPerformed(ActionEvent e)
	{
		/*boolean save = true;
		long time =  Context.getInstance().getProject().getProcess().getComponent().getValidate();
		//if( time == 0 || time < Context.getInstance().getUndoManager().getLastActionTime())
		//{
			int choice=JOptionPane.showInternalConfirmDialog(

					((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),
					ResourceManager.getInstance().getString("msgSaveWithoutValidateConfirm"),
					ResourceManager.getInstance().getString("msgTitleSaveConfirm"),
					JOptionPane.YES_NO_OPTION, JOptionPane.INFORMATION_MESSAGE);

			if(choice!=JOptionPane.YES_OPTION)
			{
				save = false;
			}
		//}
		
		if(save)*/
		{
			saveProject();
		}
	}
}
