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

import javax.swing.JOptionPane;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.DefaultPathPanel;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ResourceManager;


/**
 *
 * @version $Revision: 1.14 $
 */
public class PresentationAction extends ApesAction
{
	public PresentationAction()
	{
		super("toolsPresentation",  "icons/Presentation.gif");
		if ( ConfigManager.getInstance().getProperty(DefaultPathPanel.TOOL_PRESENTATION_KEY+"defaultPath").equals(""))
		{
			setEnabled(false);
		}
		else
		{
			setEnabled(true);
		}
	}
	
	public void actionPerformed(ActionEvent e)
	{
			try 
			{
				int choice=JOptionPane.showConfirmDialog(
				((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),
				ResourceManager.getInstance().getString("msgPresentationSave"),
				ResourceManager.getInstance().getString("toolsPresentation"),
				JOptionPane.OK_CANCEL_OPTION, JOptionPane.WARNING_MESSAGE);

				if(choice==JOptionPane.YES_OPTION)
				{
					if(((ProjectManagementAction)context.getAction("Save")).saveProject())
					{
						String path = ConfigManager.getInstance().getProperty(DefaultPathPanel.TOOL_PRESENTATION_KEY+"defaultPath");
						String s = System.getProperty("os.name") ; 
						if (s.charAt(0) == 'w' || s.charAt(0) == 'W') 
						{ 
							Runtime.getRuntime().exec("java -jar "+ " \""+ path +"\" " + context.getFilePath() ); 
						} 
						else 
						{ 
							Runtime.getRuntime().exec("java -jar "+ path +" "+ context.getFilePath()); 
						}
					}
				}
            } 
			catch (Throwable t) 
			{
				t.printStackTrace() ;
			} 
	}

}

