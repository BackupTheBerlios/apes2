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
 * @version $Revision: 1.1 $
 */
public class ColorFontPanel extends OptionPanel
{
	private JButton colorButton ;
	// a voir si on peut faire avec un button group
	//private ButtonGroup choiceStyle ; 
	private JCheckBox bold ;
	private JCheckBox italic ;
	private String panelkey;
	
	public  ColorFontPanel(String name)
	{		
			this.title = new JLabel (name) ;
			this.setLayout(new BorderLayout());
			colorButton = new JButton("");
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
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			makeLabel(" ", gridbag, c);
			
			
			// Color
			c.fill = GridBagConstraints.VERTICAL;
			c.gridwidth = 3 ;//next-to-last in row
			makeLabel(PreferencesDialog.resMan.getString("ColorFontPanelLib"), gridbag, c);
			ManagerButton manageBut = new ManagerButton();
			colorButton.addActionListener(manageBut);
			colorButton.setBackground(Color.BLUE);
			gridbag.setConstraints(colorButton, c);
			panel.add(colorButton);
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			makeLabel(" ", gridbag, c);
			
			
			// linefeed
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			makeLabel(" ", gridbag, c);
			
			//Style
			c.weightx = 1;
			c.weighty = 1 ;
			c.gridheight = 1; //end row
			c.fill = GridBagConstraints.BOTH;
			c.anchor = GridBagConstraints.WEST;
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			JPanel style = new JPanel();
			Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
			TitledBorder titleStyle = BorderFactory.createTitledBorder( loweredetched,
			PreferencesDialog.resMan.getString("ColorFontPanelSubTitleStyle"));
			style.setBorder(titleStyle);
			gridbag.setConstraints(style, c);
			panel.add(style);
			this.bold = new JCheckBox (PreferencesDialog.resMan.getString("LibBold"));
			this.italic = new JCheckBox (PreferencesDialog.resMan.getString("LibItalic"));
			style.setLayout(new GridLayout(3,1));
			style.add(bold);
			style.add(italic);

			c.weighty = 2 ;
			c.gridwidth = GridBagConstraints.REMAINDER; //end row
			makeLabel(" ", gridbag, c);
				
			this.add(new JLabel("    "),BorderLayout.WEST);
			this.add(panel,BorderLayout.CENTER);
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
		int red = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"red"));
		int green = Integer.parseInt (ConfigManager.getInstance().getProperty(key+"green"));
		int blue = Integer.parseInt(ConfigManager.getInstance().getProperty(key+"blue"));
		Color color = new Color(red,green,blue);
		this.colorButton.setBackground(color);
			
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
		this.bold.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"bold"))).booleanValue());
		this.italic.setSelected((new Boolean(ConfigManager.getInstance().getProperty(key+"italic"))).booleanValue());
	}
	
	public OptionPanel openPanel(String key)
	{
		this.panelkey = key;
		this.setName(PreferencesDialog.resMan.getString(key)) ;
		this.setColor(key);
		this.setStyle(key) ;
		return this;
	}
	
	public void saveTemp ()
	{
		Color color = this.colorButton.getBackground() ;
		String red = (new Integer (color.getRed())).toString() ;
		String green  = (new Integer (color.getGreen())).toString() ;
		String blue  = (new Integer (color.getBlue())).toString() ;
		String name = new String(this.getName()) ;
		PreferencesDialog.propTemp.setProperty(this.panelkey+"red", red);
		PreferencesDialog.propTemp.setProperty(this.panelkey+"green", green);
		PreferencesDialog.propTemp.setProperty(this.panelkey+"blue", blue);
		PreferencesDialog.propTemp.setProperty(this.panelkey+"bold", new Boolean(this.bold.isSelected()).toString());
		PreferencesDialog.propTemp.setProperty(this.panelkey+"italic",new Boolean(this.italic.isSelected()).toString());
	}
		
	public void save()
	{
		Color color = this.colorButton.getBackground() ;
		String red = (new Integer (color.getRed())).toString() ;
		String green  = (new Integer (color.getGreen())).toString() ;
		String blue  = (new Integer (color.getBlue())).toString() ;
		String name = new String(this.getName()) ;
		ConfigManager.getInstance().setProperty(this.panelkey+"red", red);
		ConfigManager.getInstance().setProperty(this.panelkey+"green", green);
		ConfigManager.getInstance().setProperty(this.panelkey+"blue", blue);
		ConfigManager.getInstance().setProperty(this.panelkey+"bold", new Boolean(this.bold.isSelected()).toString());
		ConfigManager.getInstance().setProperty(this.panelkey+"italic",new Boolean(this.italic.isSelected()).toString());
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
				Color newColor = JColorChooser.showDialog(ColorFontPanel.this, PreferencesDialog.resMan.getString("ColorFontPanelLib"), buttonClik.getBackground());
				if (newColor != null)
				{
					buttonClik.setBackground(newColor);
				}
			}
	}
}

