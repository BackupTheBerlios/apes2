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
import java.io.IOException;

import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.DefaultPathPanel;
import org.ipsquad.apes.ui.PreferencesDialog;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ResourceManager;


/**
 *
 * @version $Revision: 1.1 $
 */
public class PresentationAction extends ApesAction
{
	public PresentationAction()
	{
		super("toolsPresentation",  "icons/Empty.gif");
	}
	
	public void actionPerformed(ActionEvent e)
	{
		JFileChooser  chooser = new JFileChooser(ConfigManager.getInstance().getProperty(DefaultPathPanel.TOOL_PRESENTATION_KEY+"defaultPath"));
		chooser.setDialogTitle(ResourceManager.getInstance().getString("toolsPresentation"));
		chooser.setAcceptAllFileFilterUsed(true);
		chooser.setFileFilter(new ExtensionFileFilter("jar", ".jar"));
		int result = chooser.showDialog(((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),PreferencesDialog.resMan.getString("fileOpen"));
		if (result == JFileChooser.APPROVE_OPTION )
		{
			try 
			{
				File file = chooser.getSelectedFile();
				Process proc = Runtime.getRuntime().exec("java -jar "+ file.getName() );
			} 
			catch (IOException io) 
			{
				io.printStackTrace() ;
			} 
			
		}

	}
	

	public class ExtensionFileFilter extends FileFilter
	{
		private String fileExtension = null;
		private String fileDescription = null;

		public ExtensionFileFilter(String aFileExtension, String aFileDescription)
		{
			this.fileExtension = aFileExtension;
			this.fileDescription = aFileDescription;
		}

		public boolean accept(File f)
		{
			String name = f.getName();
			String extension = name.substring((name.lastIndexOf(".")+1));
			return extension.equals(this.fileExtension);
		}

		public String getDescription()
		{
			return this.fileDescription;
		}

	}
}

