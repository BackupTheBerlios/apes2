/*
 * Created on 28 janv. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.ipsquad.apes.ui;

import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

import org.ipsquad.utils.ResourceManager;

/**
 * @author Administrateur
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class PreferencesTree extends JTree 
{
	private PreferencesDialog mPrefDiag ;
	
	public static ResourceManager resMan = ResourceManager.getInstance();
	
	public PreferencesTree(PreferencesDialog prefDiag)
	{
		this.mPrefDiag = prefDiag ;
		ManagerTree gp = new ManagerTree();
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new PreferencesTreeItem
			("DlgPreferencesTitle",resMan.getString("DlgPreferencesTitle"),0));

		DefaultMutableTreeNode appearanceTree = new DefaultMutableTreeNode(new PreferencesTreeItem
				(DescriptionPanel.APPEARANCE_KEY,resMan.getString(DescriptionPanel.APPEARANCE_KEY),PreferencesTreeItem.DESC_PANEL));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(ColorFontPanel.DIAGRAMS_KEY,resMan.getString(ColorFontPanel.DIAGRAMS_KEY),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(ColorFontPanel.ACTIVITY_KEY,resMan.getString(ColorFontPanel.ACTIVITY_KEY),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(ColorFontPanel.ROLE_KEY,resMan.getString(ColorFontPanel.ROLE_KEY),PreferencesTreeItem.COLOR_PANEL)));
			DefaultMutableTreeNode workProductTree = new DefaultMutableTreeNode(new PreferencesTreeItem(ColorFontPanel.WORK_PRODUCT_KEY,resMan.getString(ColorFontPanel.WORK_PRODUCT_KEY),PreferencesTreeItem.COLOR_PANEL));
					workProductTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
						(ColorFontPanel.STATE_KEY,resMan.getString(ColorFontPanel.STATE_KEY),PreferencesTreeItem.COLOR_PANEL)));	
			appearanceTree.add(workProductTree);
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
					(ColorFontPanel.WORK_DEF_KEY,resMan.getString(ColorFontPanel.WORK_DEF_KEY),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
						(ColorFontPanel.NOTES_KEY,resMan.getString(ColorFontPanel.NOTES_KEY),PreferencesTreeItem.COLOR_PANEL)));
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
						(ColorFontPanel.GUARD_KEY,resMan.getString(ColorFontPanel.GUARD_KEY),PreferencesTreeItem.COLOR_PANEL)));
		DefaultMutableTreeNode defaultTree = new DefaultMutableTreeNode(new PreferencesTreeItem
				(DescriptionPanel.DEFAULT_PATH_KEY,resMan.getString(DescriptionPanel.DEFAULT_PATH_KEY),PreferencesTreeItem.DESC_PANEL));
			defaultTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(DefaultPathPanel.TOOL_PRESENTATION_KEY,resMan.getString(DefaultPathPanel.TOOL_PRESENTATION_KEY),PreferencesTreeItem.PATH_PANEL)));
			defaultTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(DefaultPathPanel.WORKSPACE_KEY,resMan.getString(DefaultPathPanel.WORKSPACE_KEY),PreferencesTreeItem.PATH_PANEL)));	
			defaultTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(DefaultPathPanel.PICTURES_KEY,resMan.getString(DefaultPathPanel.PICTURES_KEY),PreferencesTreeItem.PATH_PANEL)));	
				//defaultTree.setAllowsChildren(false);
		DefaultMutableTreeNode windowsTree = new DefaultMutableTreeNode(new PreferencesTreeItem
					(DescriptionPanel.WINDOWS_KEY,resMan.getString(DescriptionPanel.WINDOWS_KEY),PreferencesTreeItem.DESC_PANEL));
				windowsTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
					(WindowsPanel.ERROR_PANEL_KEY,resMan.getString(WindowsPanel.ERROR_PANEL_KEY),PreferencesTreeItem.ERROR_PANEL)));
				windowsTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
					(LanguagePanel.LANGUAGE_PANEL_KEY,resMan.getString(LanguagePanel.LANGUAGE_PANEL_KEY),PreferencesTreeItem.LANGUAGE_PANEL)));    
				
	
		root.add(appearanceTree);
		root.add(defaultTree);
		root.add(windowsTree);
		this.setRootVisible(false);
		this.addTreeSelectionListener(gp);
		this.setModel (new DefaultTreeModel(root));
		this.setPreferredSize(new Dimension(170,50));
	}
	
	private class ManagerTree implements TreeSelectionListener
	{
		public void valueChanged (TreeSelectionEvent e)
		{
			DefaultMutableTreeNode node = (DefaultMutableTreeNode)getLastSelectedPathComponent();
			if (node != null)
			{
				if (mPrefDiag.getWindowsPanel().isVisible())
				{
					mPrefDiag.getWindowsPanel().save() ;
				}
				else
				{
					if (mPrefDiag.getColorFontPanel().isVisible())
					{
						mPrefDiag.getColorFontPanel().save() ;
					}
					else
					{
						if (mPrefDiag.getDefaultPathPanel().isVisible())
						{
							mPrefDiag.getDefaultPathPanel().save() ;
						}
						else
						{
							if(mPrefDiag.getLanguagePanel().isVisible())
							{
								mPrefDiag.getLanguagePanel().save();
							}
						}
					}
				}
				Object o = e.getSource () ;
				int panel = ((PreferencesTreeItem)node.getUserObject()).getPanel();		
				String key = ((PreferencesTreeItem)node.getUserObject()).getKey();
				if (panel == PreferencesTreeItem.COLOR_PANEL)
				{
					mPrefDiag.getColorFontPanel().setVisible(true);
					mPrefDiag.getDefaultPathPanel().setVisible(false);
					mPrefDiag.getWindowsPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				if(panel == PreferencesTreeItem.PATH_PANEL)
				{
					mPrefDiag.getColorFontPanel().setVisible(false);
					mPrefDiag.getDefaultPathPanel().setVisible(true);
					mPrefDiag.getWindowsPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				if(panel == PreferencesTreeItem.ERROR_PANEL)
				{
					mPrefDiag.getColorFontPanel().setVisible(false);
					mPrefDiag.getDefaultPathPanel().setVisible(false);
					mPrefDiag.getWindowsPanel().setVisible(true);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				if(panel == PreferencesTreeItem.DESC_PANEL)
				{
					mPrefDiag.getColorFontPanel().setVisible(false);
					mPrefDiag.getDefaultPathPanel().setVisible(false);
					mPrefDiag.getWindowsPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(true);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				if( panel == PreferencesTreeItem.LANGUAGE_PANEL)
				{
					mPrefDiag.getColorFontPanel().setVisible(false);
					mPrefDiag.getDefaultPathPanel().setVisible(false);
					mPrefDiag.getWindowsPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(true);
				}
				mPrefDiag.setInnerPanel(panel,key);  
			}
		}
	}
	
}
	
	
