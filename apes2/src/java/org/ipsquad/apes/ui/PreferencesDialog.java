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
import java.awt.Container;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Locale;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JInternalFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;

/**
 * 
 * @version $Revision: 1.4 $
 */
public class PreferencesDialog extends JDialog 
{
	private ColorFontPanel mColorFontOption ;
	private DefaultPathPanel mDefaultOption ;
	private WindowsPanel mWindowsOption ;
	private DescriptionPanel mDescriptionOption ;
	private JPanel mInnerPanel ;
	private LanguagePanel mLanguagePanel;
	
	public static ResourceManager resMan = ResourceManager.getInstance();
	public static Properties propTemp ;
	
	public PreferencesDialog()
	{
		super((ApesFrame)Context.getInstance().getTopLevelFrame(),resMan.getString("DlgPreferencesTitle"),true) ;
		this.getContentPane().setLayout(new BorderLayout());
		// create the tree
		PreferencesTree tree = new PreferencesTree(this);
		this.initPreferencesDialog();
		Container panelMarge = getContentPane();
		this.mInnerPanel = new JPanel() ;
		mInnerPanel.setLayout(new BorderLayout());
		mInnerPanel.add(tree,BorderLayout.WEST) ;
		// add the buttons ok and cancel and apply
		ManagerButton manageButt = new ManagerButton() ;
		Box bottom = Box.createHorizontalBox() ;
		JButton apply = new JButton (resMan.getString("LibApply")) ;
		apply.addActionListener(manageButt);
		JButton ok = new JButton (resMan.getString("LibOK")) ;
		ok.addActionListener(manageButt);
		JButton cancel = new JButton (resMan.getString("LibCancel"));
		cancel.addActionListener(manageButt);
		bottom.add(Box.createHorizontalGlue());
		bottom.add(apply);
		bottom.add(ok);
		bottom.add(cancel);
		tree.setBorder(BorderFactory.createLoweredBevelBorder());
		mInnerPanel.add(bottom,BorderLayout.SOUTH);
		mInnerPanel.add(mDescriptionOption,BorderLayout.CENTER);
		// create the panel
		panelMarge.add(mInnerPanel,BorderLayout.CENTER);
		panelMarge.add(new JLabel(" "),BorderLayout.NORTH);
		panelMarge.add(new JLabel("   "),BorderLayout.EAST);
		panelMarge.add(new JLabel("   "),BorderLayout.WEST);
		panelMarge.add(new JLabel(" "),BorderLayout.SOUTH);
	}
	
	public void initPreferencesDialog ()
	{
		this.copyProperties();
		PreferencesDialog.this.mDescriptionOption = new DescriptionPanel(resMan.getString(DescriptionPanel.APPEARANCE_KEY)) ;
		PreferencesDialog.this.mDescriptionOption.openPanel(DescriptionPanel.APPEARANCE_KEY);
		PreferencesDialog.this.mColorFontOption = new ColorFontPanel(resMan.getString(ColorFontPanel.ACTIVITY_KEY));
		PreferencesDialog.this.mDefaultOption = new DefaultPathPanel(resMan.getString(DefaultPathPanel.TOOL_PRESENTATION_KEY));
		PreferencesDialog.this.mWindowsOption = new WindowsPanel(resMan.getString(WindowsPanel.ERROR_PANEL_KEY));
		PreferencesDialog.this.mLanguagePanel = new LanguagePanel(resMan.getString(LanguagePanel.LANGUAGE_PANEL_KEY)); 
		PreferencesDialog.this.mColorFontOption.setVisible(false);
		PreferencesDialog.this.mWindowsOption.setVisible(false);
		PreferencesDialog.this.mDefaultOption.setVisible(false);
		PreferencesDialog.this.mLanguagePanel.setVisible(false);
		PreferencesDialog.this.mDescriptionOption.setVisible(true);
	}
	
	public ColorFontPanel getColorFontPanel()
	{
		return this.mColorFontOption ;
	}

	public DefaultPathPanel getDefaultPathPanel()
	{
		return this.mDefaultOption ;
	}

	public WindowsPanel getWindowsPanel()
	{
		return this.mWindowsOption ;
	}

	public DescriptionPanel getDescriptionPanel()
	{
		return this.mDescriptionOption ;
	}
	public LanguagePanel getLanguagePanel()
	{
		return this.mLanguagePanel ;
	}
	
	public void setInnerPanel(int panel,String key)
	{
		if (panel == PreferencesTreeItem.COLOR_PANEL)
			mInnerPanel.add(mColorFontOption.openPanel(key),BorderLayout.CENTER);
		if(panel == PreferencesTreeItem.PATH_PANEL)
			mInnerPanel.add(mDefaultOption.openPanel(key),BorderLayout.CENTER);
		if(panel == PreferencesTreeItem.ERROR_PANEL)
			mInnerPanel.add(mWindowsOption.openPanel(key),BorderLayout.CENTER);
		if(panel == PreferencesTreeItem.DESC_PANEL)
			mInnerPanel.add(mDescriptionOption.openPanel(key),BorderLayout.CENTER);		
		if(panel == PreferencesTreeItem.LANGUAGE_PANEL)                                 						
			mInnerPanel.add(mLanguagePanel.openPanel(key),BorderLayout.CENTER);
	}
	
	private void copyProperties()
	{
		propTemp = new Properties() ;
		Enumeration enum = ConfigManager.getInstance().getProperties().propertyNames();
		while ( enum.hasMoreElements())
		{
			String key = (String) enum.nextElement();
			String value = ConfigManager.getInstance().getProperty(key) ;
			propTemp.setProperty(key,value) ;
				
		}
	}
	private void changeLanguage()
	{
		ApesFrame mainFrame = (ApesFrame)Context.getInstance().getTopLevelFrame(); 
		JInternalFrame[] frames = mainFrame.getDesktop().getAllFrames();
		JInternalFrame frameSelected = mainFrame.getDesktop().getSelectedFrame();
		JTree tree = mainFrame.getTree();
		Locale locale = new Locale(ConfigManager.getInstance().getProperty("Language"));
		mainFrame.dispose();
		ResourceManager.setResourceFile("resources/Apes", locale);
		Locale.setDefault(locale);
		Context context = Context.getInstance();

		ApesMain.initActions(context);
		ApesFrame f = new ApesFrame(tree);
		context.setTopLevelFrame(f);
		ErrorManager.getInstance().setOwner(f.getContentPane());
		f.setFilePath(context.getFilePath());
		f.show();
		for(int i = 0; i< frames.length;i++)
		{		
			if(!frames[i].equals(frameSelected))
			{
				f.openDiagram(((GraphFrame)frames[i]).getGraphModel());
				f.getDesktop().getAllFrames()[0].setBounds(frames[i].getBounds());
			}
		}
		if(frameSelected != null)
		{
			f.openDiagram(((GraphFrame)frameSelected).getGraphModel());
			f.getDesktop().getAllFrames()[0].setBounds(frameSelected.getBounds());
		}
	}   
	
	public void cancelSave()
	{
		Properties prop = ConfigManager.getInstance().getProperties();
		Enumeration enum = prop.propertyNames();
		while ( enum.hasMoreElements())
		{
			String key = (String) enum.nextElement();
			String value = propTemp.getProperty(key) ;
			String cfgValue = ConfigManager.getInstance().getProperty(key);
			if( ! cfgValue.equals(value))
			{
				ConfigManager.getInstance().setProperty(key,value);
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
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			Object o = e.getSource () ;
			JButton buttonClik = (JButton) o ;
			if (buttonClik.getText().equals(resMan.getString("LibOK")) || buttonClik.getText().equals(resMan.getString("LibApply")))
			{
				if (PreferencesDialog.this.mWindowsOption.isVisible())
				{
						PreferencesDialog.this.mWindowsOption.save() ;
						PreferencesDialog.this.mWindowsOption.saveTemp() ;
				}
				else
				{
					if (PreferencesDialog.this.mColorFontOption.isVisible())
					{
						PreferencesDialog.this.mColorFontOption.save() ;
						PreferencesDialog.this.mColorFontOption.saveTemp() ;
					}
					else
					{
						if (PreferencesDialog.this.mDefaultOption.isVisible())
						{
							PreferencesDialog.this.mDefaultOption.save() ;
							PreferencesDialog.this.mDefaultOption.saveTemp() ;
						}
						else
						{
							if(PreferencesDialog.this.mLanguagePanel.isVisible())
							{
								PreferencesDialog.this.mLanguagePanel.save();
								PreferencesDialog.this.mLanguagePanel.saveTemp();
							}
						}
					}
				}
				if (buttonClik.getText().equals(resMan.getString("LibOK")))
				{
					PreferencesDialog.this.dispose();
				}
			}
			else
			{
				if (buttonClik.getText().equals(resMan.getString("LibCancel")));
				{	
					PreferencesDialog.this.cancelSave() ;
					PreferencesDialog.this.dispose();		
				}
			}
			if (mLanguagePanel.hasLanguageChanged())
			{
				changeLanguage();
			}
		}
	}
}

