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
import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.IOException;
import java.util.Enumeration;
import java.util.Properties;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.ipsquad.apes.Context;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ResourceManager;

/**
 * 
 * @version $Revision: 1.1 $
 */
public class PreferencesDialog extends JDialog 
{
	private JTree tree ;
	private ColorFontPanel ColorFontOption ;
	private DefaultPathPanel DefaultOption ;
	private WindowsPanel windowsOption ;
	private DescriptionPanel description ;
	private JPanel innerPanel ;
	public static ResourceManager resMan = ResourceManager.getInstance();
	public static Properties propTemp ;
	public PreferencesDialog()
	{
		super((ApesFrame)Context.getInstance().getTopLevelFrame(),resMan.getString("DlgPreferencesTitle"),true) ;
		this.getContentPane().setLayout(new BorderLayout());
		// create the tree
		tree = new JTree();
		ManagerTree gp = new ManagerTree();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new PreferencesTreeItem
			("DlgPreferencesTitle",resMan.getString("DlgPreferencesTitle"),0));
			
		DefaultMutableTreeNode appearanceTree = new DefaultMutableTreeNode(new PreferencesTreeItem
				("AppearanceTitle",resMan.getString("AppearanceTitle"),PreferencesTreeItem.DESC_PANEL));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				("ActivityTitle",resMan.getString("ActivityTitle"),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				("RoleTitle",resMan.getString("RoleTitle"),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				("GuardTitle",resMan.getString("GuardTitle"),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				("StateTitle",resMan.getString("StateTitle"),PreferencesTreeItem.COLOR_PANEL)));	
			DefaultMutableTreeNode workProductTree = new DefaultMutableTreeNode(new PreferencesTreeItem("WorkproductTitle",resMan.getString("WorkproductTitle"),PreferencesTreeItem.COLOR_PANEL));
						workProductTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
							("WorkproductProvidedTitle",resMan.getString("WorkproductProvidedTitle"),PreferencesTreeItem.COLOR_PANEL)));	
						workProductTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
							("WorkproductRequiredTitle",resMan.getString("WorkproductRequiredTitle"),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(workProductTree);
		DefaultMutableTreeNode defaultTree = new DefaultMutableTreeNode(new PreferencesTreeItem
				("DefaultPathTitle",resMan.getString("DefaultPathTitle"),PreferencesTreeItem.DESC_PANEL));
			defaultTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				("ToolPresentationTitle",resMan.getString("ToolPresentationTitle"),PreferencesTreeItem.PATH_PANEL)));
			defaultTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				("WorkspaceTitle",resMan.getString("WorkspaceTitle"),PreferencesTreeItem.PATH_PANEL)));	
			
				
				//defaultTree.setAllowsChildren(false);
		DefaultMutableTreeNode windowsTree = new DefaultMutableTreeNode(new PreferencesTreeItem
					("WindowsTitle",resMan.getString("WindowsTitle"),PreferencesTreeItem.DESC_PANEL));
				windowsTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
					("ErrorPanelTitle",resMan.getString("ErrorPanelTitle"),PreferencesTreeItem.ERROR_PANEL)));
				
		
		root.add(appearanceTree);
		root.add(defaultTree);
		root.add(windowsTree);
		tree.setRootVisible(false);
		tree.addTreeSelectionListener(gp);
		tree.setModel (new DefaultTreeModel(root));
		tree.setPreferredSize(new Dimension(170,500));
		
		
		propTemp = new Properties() ;
		Enumeration enum = ConfigManager.getInstance().getProperties().propertyNames();
		while ( enum.hasMoreElements())
		{
			String key = (String) enum.nextElement();
			String value = ConfigManager.getInstance().getProperty(key) ;
			propTemp.setProperty(key,value) ;
						
		}
		
		Container panelMarge = getContentPane();
		this.innerPanel = new JPanel() ;
		innerPanel.setLayout(new BorderLayout());
		innerPanel.add(tree,BorderLayout.WEST) ;
		// add the buttons ok and cancel
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

		innerPanel.add(bottom,BorderLayout.SOUTH);
		innerPanel.add(description,BorderLayout.CENTER);
		// create the panel
		panelMarge.add(innerPanel,BorderLayout.CENTER);
		panelMarge.add(new JLabel(" "),BorderLayout.NORTH);
		panelMarge.add(new JLabel("   "),BorderLayout.EAST);
		panelMarge.add(new JLabel("   "),BorderLayout.WEST);
		panelMarge.add(new JLabel(" "),BorderLayout.SOUTH);
	}
	
	public void setInnerPanel(int panel,String key)
	{
		if (panel == PreferencesTreeItem.COLOR_PANEL)
		{
			innerPanel.add(ColorFontOption.openPanel(key),BorderLayout.CENTER);
		}
		if(panel == PreferencesTreeItem.PATH_PANEL)
		{
			innerPanel.add(DefaultOption.openPanel(key),BorderLayout.CENTER);
		}
		if(panel == PreferencesTreeItem.ERROR_PANEL)
		{
			innerPanel.add(windowsOption.openPanel(key),BorderLayout.CENTER);
		}
		if(panel == PreferencesTreeItem.DESC_PANEL)
		{
			innerPanel.add(description.openPanel(key),BorderLayout.CENTER);
		}
		
	}

	private void cancelSave()
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
	
	private class ManagerTree implements TreeSelectionListener
	{
			public ManagerTree()
			{
				PreferencesDialog.this.description = new DescriptionPanel(resMan.getString("AppearanceTitle")) ;
				PreferencesDialog.this.description.openPanel("AppearanceTitle");
				PreferencesDialog.this.ColorFontOption = new ColorFontPanel(resMan.getString("ActivityTitle"));
				PreferencesDialog.this.DefaultOption = new DefaultPathPanel(resMan.getString("ToolPresentationTitle"));
				PreferencesDialog.this.windowsOption = new WindowsPanel(resMan.getString("ErrorPanelTitle"));
				PreferencesDialog.this.ColorFontOption.setVisible(true);
				PreferencesDialog.this.DefaultOption.setVisible(true);
			}
			public void valueChanged (TreeSelectionEvent e)
			{
				if (PreferencesDialog.this.windowsOption.isVisible())
				{
						PreferencesDialog.this.windowsOption.save() ;
				}
				else
				{
					if (PreferencesDialog.this.ColorFontOption.isVisible())
					{
						PreferencesDialog.this.ColorFontOption.save() ;
					}
					else
					{
						if (PreferencesDialog.this.DefaultOption.isVisible())
						{
							PreferencesDialog.this.DefaultOption.save() ;
						}
					}
				}
				Object o = e.getSource () ;
				DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();
				int panel = ((PreferencesTreeItem)node.getUserObject()).getPanel();		
				String key = ((PreferencesTreeItem)node.getUserObject()).getKey();
				if (panel == PreferencesTreeItem.COLOR_PANEL)
				{
					PreferencesDialog.this.ColorFontOption.setVisible(true);
					PreferencesDialog.this.DefaultOption.setVisible(false);
					PreferencesDialog.this.windowsOption.setVisible(false);
					PreferencesDialog.this.description.setVisible(false);
				}
				if(panel == PreferencesTreeItem.PATH_PANEL)
				{
					PreferencesDialog.this.ColorFontOption.setVisible(false);
					PreferencesDialog.this.DefaultOption.setVisible(true);
					PreferencesDialog.this.windowsOption.setVisible(false);
					PreferencesDialog.this.description.setVisible(false);
				}
				if(panel == PreferencesTreeItem.ERROR_PANEL)
				{
					PreferencesDialog.this.ColorFontOption.setVisible(false);
					PreferencesDialog.this.DefaultOption.setVisible(false);
					PreferencesDialog.this.windowsOption.setVisible(true);
					PreferencesDialog.this.description.setVisible(false);
				}
				if(panel == PreferencesTreeItem.DESC_PANEL)
				{
					PreferencesDialog.this.ColorFontOption.setVisible(false);
					PreferencesDialog.this.DefaultOption.setVisible(false);
					PreferencesDialog.this.windowsOption.setVisible(false);
					PreferencesDialog.this.description.setVisible(true);
				}
			
				PreferencesDialog.this.setInnerPanel(panel,key);
			}		
	}
	
	private class ManagerButton implements ActionListener
	{
		private ManagerButton ()
		{
			
		}
		public void actionPerformed(ActionEvent e)
		{
			Object o = e.getSource () ;
			JButton buttonClik = (JButton) o ;
			if (buttonClik.getText().equals(resMan.getString("LibOK")) || buttonClik.getText().equals(resMan.getString("LibApply")))
			{
				if (PreferencesDialog.this.windowsOption.isVisible())
				{
						PreferencesDialog.this.windowsOption.save() ;
						PreferencesDialog.this.windowsOption.saveTemp() ;
				}
				else
				{
					if (PreferencesDialog.this.ColorFontOption.isVisible())
					{
						PreferencesDialog.this.ColorFontOption.save() ;
						PreferencesDialog.this.ColorFontOption.saveTemp() ;
					}
					else
					{
						if (PreferencesDialog.this.DefaultOption.isVisible())
						{
							PreferencesDialog.this.DefaultOption.save() ;
							PreferencesDialog.this.DefaultOption.saveTemp() ;
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
				if (buttonClik.getText().equals(resMan.getString("LibCancel")))
				{	
					PreferencesDialog.this.cancelSave() ;
					PreferencesDialog.this.dispose();
				}
			}
		}
	}
}

