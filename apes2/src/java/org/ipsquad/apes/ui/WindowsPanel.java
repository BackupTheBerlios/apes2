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
 * @version $Revision: 1.4 $
 */
public class WindowsPanel extends OptionPanel 
{
	private JRadioButton mYesButton ;
	private JRadioButton mNoButton ;
	private String mPanelKey;
	
	public static final String ERROR_PANEL_KEY = "ErrorPanelTitle" ;
	
	public WindowsPanel(String name)
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
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder titleStyle = BorderFactory.createTitledBorder( loweredetched, 
						PreferencesDialog.resMan.getString("ErrorPanelSubTitle"));
		JPanel errorPanel = new JPanel();
		errorPanel.setBorder(titleStyle);
		gridbag.setConstraints(errorPanel, c);
		mPanel.add(errorPanel);
		JLabel label = new JLabel(PreferencesDialog.resMan.getString("ErrorPanelLib"));
		this.mYesButton = new JRadioButton (PreferencesDialog.resMan.getString("LibYes"));
		this.mYesButton.setSelected(true);
		this.mNoButton = new JRadioButton (PreferencesDialog.resMan.getString("LibNo"));
		final ButtonGroup choice = new ButtonGroup();
		choice.add(this.mYesButton);
		choice.add(this.mNoButton);
		errorPanel.setLayout(new GridLayout(4,1));
		errorPanel.add(label);
		errorPanel.add(this.mYesButton);
		errorPanel.add(this.mNoButton);

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
		this.mPanelKey = key;
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.mYesButton.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"yes"))).booleanValue());
		this.mNoButton.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"no"))).booleanValue());
		return this ;
	}
	
	public void saveTemp ()
	{
		PreferencesDialog.propTemp.setProperty(mPanelKey+"yes", new Boolean(this.mYesButton.isSelected()).toString());
		PreferencesDialog.propTemp.setProperty(mPanelKey+"no", new Boolean(this.mYesButton.isSelected()).toString());
	}
	
	public void save ()
	{
		String name = new String(this.getName()) ;
		ConfigManager.getInstance().setProperty(mPanelKey+"yes", new Boolean(this.mYesButton.isSelected()).toString());
		ConfigManager.getInstance().setProperty(mPanelKey+"no", new Boolean(this.mNoButton.isSelected()).toString());
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
