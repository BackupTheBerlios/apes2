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
package org.ipsquad.apes.ui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;

import org.ipsquad.utils.ConfigManager;

/**
* @version $Revision: 1.1 $
*/
public class DefaultPathPanel extends OptionPanel
{
	private JTextField defaultPath;
	private String panelkey;
	public  DefaultPathPanel (String name)
	{
			this.title = new JLabel (name) ;
			this.setLayout(new BorderLayout());
			panel = new JPanel() ;
			GridBagLayout gridbag = new GridBagLayout();
			panel.setLayout(gridbag);
			GridBagConstraints c = new GridBagConstraints();

			// Title
			c.weightx = 1.0;
			c.weighty = 0 ;
			c.fill = GridBagConstraints.BOTH;
			c.gridwidth = GridBagConstraints.REMAINDER; //end row			//	title
			this.title = new JLabel (name);
			TitledBorder titleBor = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK));
			titleBor.setTitleJustification(TitledBorder.CENTER);
			title.setBorder(titleBor);
			gridbag.setConstraints(title, c);
			panel.add(title);

			// linefeed
			c.weighty = 0;      		
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			makeLabel(" ", gridbag, c);

			c.weighty = 0;
			c.weightx = 0 ;
			c.fill = GridBagConstraints.BOTH;
			//c.gridwidth = ;//next-to-last in row
			//c.gridwidth = GridBagConstraints.RELATIVE;
			c.gridwidth = GridBagConstraints.REMAINDER;
			JLabel label = new JLabel(PreferencesDialog.resMan.getString("DefaultPathEnterLib"));
			gridbag.setConstraints(label, c);
			panel.add(label);
			c.weightx = 3 ;
			c.gridwidth = GridBagConstraints.RELATIVE;
			defaultPath = new JTextField(25);
			panel.add(defaultPath);
			gridbag.setConstraints(defaultPath, c);
			c.weightx = 0 ;
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			JButton browseButton = new JButton(PreferencesDialog.resMan.getString("LibBrowse"));
			browseButton.addActionListener(new ManagerButton());
			gridbag.setConstraints(browseButton, c);
			panel.add(browseButton);			

			c.fill = GridBagConstraints.VERTICAL;
			c.weighty = 2.0; 
			// linefeed     		
			 c.gridwidth = GridBagConstraints.REMAINDER; //end row
			makeLabel(" ", gridbag, c);
           
			this.add(new JLabel("    "),BorderLayout.WEST);
			this.add(panel,BorderLayout.CENTER);
                  


	}
	public OptionPanel openPanel(String key)
	{
		this.panelkey = key;
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.defaultPath.setText(ConfigManager.getInstance().getProperty(key+"defaultPath"));
		return this ;
	}
	
	public void save ()
	{
		String name = new String(this.getName()) ;
		ConfigManager.getInstance().setProperty(this.panelkey+"defaultPath", this.defaultPath.getText());
		try
		{
			ConfigManager.getInstance().save() ;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	
	public void saveTemp ()
	{
	
		PreferencesDialog.propTemp.setProperty(this.panelkey+"defaultPath", this.defaultPath.getText());
	
	}
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			JFileChooser  chooser = new JFileChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			chooser.setDialogTitle(PreferencesDialog.resMan.getString("DefaultPathChooseLib"));
			chooser.setAcceptAllFileFilterUsed(false);
			int result = chooser.showDialog(DefaultPathPanel.this.getParent(),PreferencesDialog.resMan.getString("LibOK"));
			if (result == JFileChooser.APPROVE_OPTION )
			{
				defaultPath.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
	}

}


