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
 * @version $Revision: 1.5 $
 */
public class DescriptionPanel extends OptionPanel
{
	private JLabel mDescriptionLabel ;
	
	public static final String APPEARANCE_KEY = "AppearanceTitle" ;
	public static final String DEFAULT_PATH_KEY = "DefaultPathTitle" ;
	public static final String WINDOWS_KEY = "WindowsTitle" ;
	
	public DescriptionPanel(String name)
	{
		this.mTitleLabel = new JLabel (name) ;
		this.setLayout(new BorderLayout());
		mPanel = new JPanel() ;
		GridBagLayout gridbag = new GridBagLayout();
		mPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();

		// Title
		c.weightx = 1.0;
		c.weighty = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row			//	title
		this.mTitleLabel = new JLabel (name);
		TitledBorder titleBor = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK));
		titleBor.setTitleJustification(TitledBorder.CENTER);
		mTitleLabel.setBorder(titleBor);
		gridbag.setConstraints(mTitleLabel, c);
		mPanel.add(mTitleLabel);

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
		this.mDescriptionLabel = new JLabel();
		this.setDescription(name);
		gridbag.setConstraints(this.mDescriptionLabel, c);
		mPanel.add(this.mDescriptionLabel);

		//		linefeed 
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;     		
		 c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
   
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);
	}
	
	public OptionPanel openPanel(String key)
	{
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.setDescription (key);
		return this ;
	}
	
	public void setDescription(String key)
	{
		if (key.equals(APPEARANCE_KEY))
		{
			this.mDescriptionLabel.setText(PreferencesDialog.resMan.getString("LibAppearance"));
		}
		if (key.equals(DEFAULT_PATH_KEY))
		{
			this.mDescriptionLabel.setText(PreferencesDialog.resMan.getString("LibDefaultPath"));
		}
		if (key.equals(WINDOWS_KEY))
		{
			this.mDescriptionLabel.setText(PreferencesDialog.resMan.getString("LibWindows"));
		}
	}

}
