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
import java.io.File;

import javax.swing.JFileChooser;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.DefaultPathPanel;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.SimpleFileFilter;


/**
 *
 * @version $Revision: 1.5 $
 */
public class PresentationAction extends ApesAction
{
	public PresentationAction()
	{
		super("toolsPresentation",  "icons/Empty.gif");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		String name = ResourceManager.getInstance().getString("toolsPresentation") ;
		JFileChooser  chooser = new JFileChooser(ConfigManager.getInstance().getProperty(DefaultPathPanel.TOOL_PRESENTATION_KEY+"defaultPath"));
		chooser.setDialogTitle(name);
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new SimpleFileFilter("jar",name));
		int result = chooser.showDialog(((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),ResourceManager.getInstance().getString("fileOpen"));
		if (result == JFileChooser.APPROVE_OPTION )
		{
			try 
			{
				File file = chooser.getSelectedFile();
				String s = System.getProperty("os.name") ; 
                if (s.charAt(0) == 'w' || s.charAt(0) == 'W') 
                { 
                    Runtime.getRuntime().exec("java -jar "+ " \""+ file.getPath() +"\" " ); 
                } 
                else 
                { 
                    Runtime.getRuntime().exec("java -jar "+ file.getPath() ); 
                } 
            } 
			catch (Throwable t) 
			{
				t.printStackTrace() ;
			} 
		}
	}

}

