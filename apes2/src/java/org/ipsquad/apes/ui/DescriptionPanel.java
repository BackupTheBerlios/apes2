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

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

/**
 *
 * @version $Revision: 1.1 $
 */
public class DescriptionPanel extends OptionPanel
{
	private JLabel description ;
	public DescriptionPanel(String name)
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
		//Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		//TitledBorder titleStyle = BorderFactory.createTitledBorder( loweredetched, "Display Error Panel");
		//JPanel errorPanel = new JPanel();
		//errorPanel.setBorder(titleStyle);
		this.description = new JLabel();
		this.setDescription(name);
		gridbag.setConstraints(this.description, c);
		panel.add(this.description);

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
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.setDescription (key);
		return this ;
	}
	
	public void setDescription(String key)
	{
		if (key.equals("AppearanceTitle"))
		{
			this.description.setText(PreferencesDialog.resMan.getString("LibAppearance"));
		}
		if (key.equals("DefaultPathTitle"))
		{
			this.description.setText(PreferencesDialog.resMan.getString("LibDefaultPath"));
		}
		if (key.equals("WindowsTitle"))
		{
			this.description.setText(PreferencesDialog.resMan.getString("LibWindows"));
		}
	}

}
