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
import java.awt.GridLayout;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.ipsquad.utils.ConfigManager;

/**
 *
 * @version $Revision: 1.1 $
 */
public class WindowsPanel extends OptionPanel 
{
	private JRadioButton errorPanelYes ;
	private JRadioButton errorPanelNo ;
	private String panelkey;
	public WindowsPanel(String name)
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
		c.gridwidth = GridBagConstraints.REMAINDER;;//next-to-last in row
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder titleStyle = BorderFactory.createTitledBorder( loweredetched, 
						PreferencesDialog.resMan.getString("ErrorPanelSubTitle"));
		JPanel errorPanel = new JPanel();
		errorPanel.setBorder(titleStyle);
		gridbag.setConstraints(errorPanel, c);
		panel.add(errorPanel);
		JLabel label = new JLabel(PreferencesDialog.resMan.getString("ErrorPanelLib"));
		this.errorPanelYes = new JRadioButton (PreferencesDialog.resMan.getString("LibYes"));
		this.errorPanelYes.setSelected(true);
		this.errorPanelNo = new JRadioButton (PreferencesDialog.resMan.getString("LibNo"));
		final ButtonGroup choice = new ButtonGroup();
		choice.add(this.errorPanelYes);
		choice.add(this.errorPanelNo);
		errorPanel.setLayout(new GridLayout(4,1));
		errorPanel.add(label);
		errorPanel.add(this.errorPanelYes);
		errorPanel.add(this.errorPanelNo);

		//		linefeed 
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;     		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
   
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(panel,BorderLayout.CENTER);
	}
	
	public OptionPanel openPanel(String key)
	{
		this.panelkey = key;
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.errorPanelYes.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"yes"))).booleanValue());
		this.errorPanelNo.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"no"))).booleanValue());
		return this ;
	}
	
	public void saveTemp ()
	{
		PreferencesDialog.propTemp.setProperty(panelkey+"yes", new Boolean(this.errorPanelYes.isSelected()).toString());
	}
	
	public void save ()
	{
		String name = new String(this.getName()) ;
		ConfigManager.getInstance().setProperty(panelkey+"yes", new Boolean(this.errorPanelYes.isSelected()).toString());
		try
		{
			ConfigManager.getInstance().save() ;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
}
