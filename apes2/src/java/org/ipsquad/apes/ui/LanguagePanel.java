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
import java.io.IOException;
import java.util.Locale;

import javax.swing.BorderFactory;
import javax.swing.DefaultListModel;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;

import org.ipsquad.utils.ConfigManager;

/**
 * 
 * @version $Revision: 1.2 $
 */
public class LanguagePanel extends OptionPanel {
	
	private JList mList;
	private String mOldLanguage;
	public static final String LANGUAGE_PANEL_KEY = "LanguageTitle" ;
	
	public LanguagePanel(String name)
	{
		mOldLanguage = ConfigManager.getInstance().getProperty("Language");
		boolean trouve = false;
		
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
		makeLabel(" ", gridbag, c);

		c.weightx = 0 ;
		makeLabel(PreferencesDialog.resMan.getString("LibChooseLanguage"), gridbag, c);
		makeLabel(" ", gridbag, c);

		DefaultListModel listModel = new DefaultListModel();
		listModel.add(0,new Language(Locale.FRANCE,"Français"));
		listModel.add(1,new Language(Locale.ENGLISH,"English"));
		mList = new JList(listModel);
		gridbag.setConstraints(mList, c);
		mPanel.add(mList);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;     		
		makeLabel(" ", gridbag, c);
		
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);
		for (int i = 0; i < mList.getModel().getSize() && !trouve;i++)
		{
			if (mOldLanguage.equals(((Language)mList.getModel().getElementAt(i)).getKey()))
			{
				mList.setSelectedIndex(i);
				trouve = true;
			}
		}
	}
	public OptionPanel openPanel(String key)
	{
		this.setName(PreferencesDialog.resMan.getString(key)) ;
	
		return this ;
	}
	public void saveTemp ()
	{
		PreferencesDialog.propTemp.setProperty("Language",((Language)mList.getSelectedValue()).getKey());
	}
	public void save ()
	{
		ConfigManager.getInstance().setProperty("Language",((Language)mList.getSelectedValue()).getKey());
		try
		{
			ConfigManager.getInstance().save() ;
		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}
	}
	public boolean hasLanguageChanged()
	{
		return(!mOldLanguage.equals(ConfigManager.getInstance().getProperty("Language")));
	}
}
