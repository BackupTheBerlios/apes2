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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.ipsquad.utils.ConfigManager;
/**
 * @version $Revision: 1.2 $
 */
public class ColorFontPanel extends OptionPanel
{
	private JButton mForegroundColorButton ;
	private JButton mBackgroundColorButton;
	private JCheckBox mBold ;
	private JCheckBox mItalic ;
	private String mPanelkey;
	
	public static final String ACTIVITY_KEY = "ActivityTitle" ;
	public static final String ROLE_KEY = "RoleTitle" ;
	public static final String GUARD_KEY = "GuardTitle" ;
	public static final String STATE_KEY = "StateTitle" ;
	public static final String WORK_PRODUCT_KEY = "WorkproductTitle" ;
	public static final String REQUIRED_INTERFACE_KEY = "WorkproductRequiredTitle" ;
	public static final String PROVIDED_INTERFACE_KEY = "WorkproductProvidedTitle" ;
	
	public  ColorFontPanel(String name)
	{		
		this.mTitleLabel = new JLabel (name) ;
		this.setLayout(new BorderLayout());
		mForegroundColorButton = new JButton("");
		mBackgroundColorButton = new JButton("");
		mPanel = new JPanel() ;
		GridBagLayout gridbag = new GridBagLayout();
		mPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		
		// Title
		c.weightx = 1.0;
		c.weighty = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row			
		this.mTitleLabel = new JLabel (name);
		TitledBorder titleBor = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK));
		titleBor.setTitleJustification(TitledBorder.CENTER);
		mTitleLabel.setBorder(titleBor);
		gridbag.setConstraints(mTitleLabel, c);
		mPanel.add(mTitleLabel);
		
		// linefeed
		makeLabel(" ", gridbag, c);
		
		// Color
		c.fill = GridBagConstraints.VERTICAL;
		c.gridwidth = 3 ;//next-to-last in row
		makeLabel(PreferencesDialog.resMan.getString("ColorFontPanelLibForeground"), gridbag, c);
		ManagerButton manageBut = new ManagerButton();
		mForegroundColorButton.addActionListener(manageBut);
		gridbag.setConstraints(mForegroundColorButton, c);
		mPanel.add(mForegroundColorButton);
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		makeLabel(" ", gridbag, c);
		c.gridwidth = 3 ;//next-to-last in row
		makeLabel(PreferencesDialog.resMan.getString("ColorFontPanelLibBackground"), gridbag, c);
		mBackgroundColorButton.addActionListener(manageBut);
		gridbag.setConstraints(mBackgroundColorButton, c);
		mPanel.add(mBackgroundColorButton);
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		// linefeed
		makeLabel(" ", gridbag, c);
		
		//Style
		c.weighty = 1 ;
		c.gridheight = 1; //end row
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		JPanel style = new JPanel();
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder titleStyle = BorderFactory.createTitledBorder( loweredetched,
		PreferencesDialog.resMan.getString("ColorFontPanelSubTitleStyle"));
		style.setBorder(titleStyle);
		gridbag.setConstraints(style, c);
		mPanel.add(style);
		this.mBold = new JCheckBox (PreferencesDialog.resMan.getString("LibBold"));
		this.mItalic = new JCheckBox (PreferencesDialog.resMan.getString("LibItalic"));
		style.setLayout(new GridLayout(3,1));
		style.add(mBold);
		style.add(mItalic);

		c.weighty = 2 ;
		makeLabel(" ", gridbag, c);
			
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);
	}
	
	public static Color getColor(String key)
	{
		int red = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"red"));
		int green = Integer.parseInt (ConfigManager.getInstance().getProperty(key+"green"));
		int blue = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"blue"));
		Color color = new Color(red,green,blue);
		color = new Color(red,green,blue);
		return color ;
	}
		
	public void setColor (String key)
	{
		int red = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"foregroundred"));
		int green = Integer.parseInt (ConfigManager.getInstance().getProperty(key+"foregroundgreen"));
		int blue = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"foregroundblue"));
		Color color = new Color(red,green,blue);
		this.mForegroundColorButton.setBackground(color);
		red = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"backgroundred"));
		green = Integer.parseInt (ConfigManager.getInstance().getProperty(key+"backgroundgreen"));
		blue = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"backgroundblue"));
		color = new Color(red,green,blue);
		this.mBackgroundColorButton.setBackground(color);
			
	}
		
	public static int getStyle (String key)
	{
		int font ;
		Boolean bold = new Boolean(ConfigManager.getInstance().getProperty(key+"bold"));
		Boolean italic = new Boolean(ConfigManager.getInstance().getProperty(key+"italic"));
		if (bold.booleanValue() == true && italic.booleanValue() == true )
			font = 3 ;
		else
		{
			if (bold.booleanValue() == true)
				font = 1 ;
			else
			{
				if (italic.booleanValue() == true)
					font = 2 ;
				else
					font = 0 ;
			}
		}	
		return font ;
	}
	
	public void setStyle(String key)
	{
		this.mBold.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"bold"))).booleanValue());
		this.mItalic.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"italic"))).booleanValue());
	}
	
	public OptionPanel openPanel(String key)
	{
		this.mPanelkey = key;
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.setColor(key);
		this.setStyle(key) ;
		return this;
	}
	
	public void saveTemp ()
	{
		Color color = this.mForegroundColorButton.getBackground() ;
		String red = (new Integer (color.getRed())).toString() ;
		String green  = (new Integer (color.getGreen())).toString() ;
		String blue  = (new Integer (color.getBlue())).toString() ;
		String name = new String(this.getName()) ;
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"foregroundred", red);
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"foregroundgreen", green);
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"foregroundblue", blue);
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"bold", new Boolean(this.mBold.isSelected()).toString());
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"italic",new Boolean(this.mItalic.isSelected()).toString());
		color = this.mBackgroundColorButton.getBackground() ;
		red = (new Integer (color.getRed())).toString() ;
		green  = (new Integer (color.getGreen())).toString() ;
		blue  = (new Integer (color.getBlue())).toString() ;
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"backgroundred", red);
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"backgroundgreen", green);
		PreferencesDialog.propTemp.setProperty(this.mPanelkey+"backgroundblue", blue);
	}
		
	public void save()
	{
		Color color = this.mForegroundColorButton.getBackground() ;
		String red = (new Integer (color.getRed())).toString() ;
		String green  = (new Integer (color.getGreen())).toString() ;
		String blue  = (new Integer (color.getBlue())).toString() ;
		String name = new String(this.getName()) ;
		ConfigManager.getInstance().setProperty(this.mPanelkey+"foregroundred", red);
		ConfigManager.getInstance().setProperty(this.mPanelkey+"foregroundgreen", green);
		ConfigManager.getInstance().setProperty(this.mPanelkey+"foregroundblue", blue);
		ConfigManager.getInstance().setProperty(this.mPanelkey+"bold", new Boolean(this.mBold.isSelected()).toString());
		ConfigManager.getInstance().setProperty(this.mPanelkey+"italic",new Boolean(this.mItalic.isSelected()).toString());
		color = this.mBackgroundColorButton.getBackground() ;
		red = (new Integer (color.getRed())).toString() ;
		green  = (new Integer (color.getGreen())).toString() ;
		blue  = (new Integer (color.getBlue())).toString() ;
		ConfigManager.getInstance().setProperty(this.mPanelkey+"backgroundred", red);
		ConfigManager.getInstance().setProperty(this.mPanelkey+"backgroundgreen", green);
		ConfigManager.getInstance().setProperty(this.mPanelkey+"backgroundblue", blue);
		try
		{
			ConfigManager.getInstance().save() ;

		}
		catch(IOException ex)
		{
			ex.printStackTrace();
		}

		
	}
	
	private class ManagerButton implements ActionListener
	{
			public void actionPerformed(ActionEvent e)
			{
				Object o = e.getSource () ;
				JButton buttonClik = (JButton) o ;
				Color newColor = JColorChooser.showDialog(ColorFontPanel.this, PreferencesDialog.resMan.getString("LibChangeColor"), buttonClik.getBackground());
				if (newColor != null)
				{
					buttonClik.setBackground(newColor);
				}
			}
	}
}

