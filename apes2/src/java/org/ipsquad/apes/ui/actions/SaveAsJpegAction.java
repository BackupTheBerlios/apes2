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
import java.io.File;

import javax.swing.AbstractAction;
import javax.swing.JOptionPane;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.processing.SaveJPEG;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.DefaultPathPanel;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.SimpleFileFilter;
import org.ipsquad.utils.SmartChooser;

/**
 * Save the selected diagram in a jpeg file
 *
 * @version $Revision: 1.3 $
 */
public class SaveAsJpegAction extends AbstractAction
{
	protected SimpleFileFilter filter = new SimpleFileFilter(new String[]{"jpeg","jpg"}, "JPEG");

	public SaveAsJpegAction(String label)
	{
		super(ResourceManager.getInstance().getString(label));
	}

	public void actionPerformed(ActionEvent e)
	{
		SmartChooser chooser = SmartChooser.getChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filter);
		chooser.setDirectory(ConfigManager.getInstance().getProperty(DefaultPathPanel.PICTURES_KEY+"defaultPath"));
		
		if (chooser.showSaveDialog(((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane())
		==SmartChooser.APPROVE_OPTION)
		{
			if(chooser.getSelectedFile().isDirectory())
			{
				ErrorManager.getInstance().display("errorTitleSaveProcess", "errorWrongFileName");
				return;
			}

			String selected_file=chooser.getSelectedFile().getAbsolutePath();
			int last_separator= selected_file.lastIndexOf( File.separatorChar );
			int last_point=0;
			if(last_separator!=-1)
				last_point=selected_file.indexOf( '.', last_separator);
			else last_point=selected_file.lastIndexOf( '.');
			File f=null;
			if(last_point==-1)
				f=new File(selected_file+".jpeg");
			else
			{
				String suffixe=selected_file.substring(last_point,selected_file.length());
				if(suffixe.compareTo(".jpeg")!=0 && suffixe.compareTo(".jpg")!=0)
					f=new File(selected_file+".jpeg");
				else f=new File(selected_file);
			}

			if(!filter.accept(f))
			{
				ErrorManager.getInstance().display("errorTitleSaveProcess", "errorWrongFileName");
				return;
			}
			System.out.println(f.getName()+" "+f.getAbsolutePath());
			if(f.exists())
			{
				int choice=JOptionPane.showInternalConfirmDialog(((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane(),
				                                                 ResourceManager.getInstance().getString("msgSaveFileAlreadyExists"),
				                                                 ResourceManager.getInstance().getString("msgTitleSaveConfirm"),
				                                                 JOptionPane.YES_NO_OPTION,
										 JOptionPane.QUESTION_MESSAGE);
				
				if(choice!=JOptionPane.YES_OPTION) return;
			}

			SpemDiagram selected_diagram = (SpemDiagram) ((ApesTreeNode)Context.getInstance().getTopLevelFrame().getTree().getSelectionPath().getLastPathComponent()).getUserObject();
			
			SpemGraphAdapter selected_adapter = (SpemGraphAdapter)Context.getInstance().getProject().getGraphModel(selected_diagram); 
						
			try
			{
				SaveJPEG saveJPEG = new SaveJPEG(f.getAbsolutePath(),selected_adapter);
				saveJPEG.save();
			}
			catch(Throwable t)
			{
				ErrorManager.getInstance().display("errorTitleSaveProcess", "errorOpenProcess");
				return;
			}
		}
	}
};
