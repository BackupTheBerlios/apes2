/*
 * Created on 30 sept. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package iepp.ui.preferences;

import iepp.Application;

import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;

/**
 * @author SP
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class ArbrePreferences extends JTree 
{
	/**
	 * Lien vers la boîte de dialogue contenant l'arbre
	 */
	private FenetrePreference mPrefDiag ;

	
	public ArbrePreferences(FenetrePreference prefDiag)
	{
		this.mPrefDiag = prefDiag ;
		
		// gestionnaire d'évènement sur l'arbre
		ManagerTree gp = new ManagerTree();
		
		DefaultMutableTreeNode root = new DefaultMutableTreeNode(new PreferenceTreeItem(
				Application.getApplication().getTraduction("Preferences"),
				Application.getApplication().getTraduction("Preferences"),0));

		DefaultMutableTreeNode referentielItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.GENERAL_KEY,Application.getApplication().getTraduction(PanneauDescription.GENERAL_KEY),PreferenceTreeItem.DESC_PANEL));
		
		referentielItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauLangue.LANGUAGE_PANEL_KEY,Application.getApplication().getTraduction(PanneauLangue.LANGUAGE_PANEL_KEY),PreferenceTreeItem.LANGUAGE_PANEL)));    
	
		/*
			referentielItem.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(ColorFontPanel.DIAGRAMS_KEY,resMan.getString(ColorFontPanel.DIAGRAMS_KEY),PreferencesTreeItem.COLOR_PANEL)));
			
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(ColorFontPanel.ACTIVITY_KEY,resMan.getString(ColorFontPanel.ACTIVITY_KEY),PreferencesTreeItem.COLOR_PANEL)));
			
			appearanceTree.add(new DefaultMutableTreeNode(new PreferencesTreeItem
				(ColorFontPanel.ROLE_KEY,resMan.getString(ColorFontPanel.ROLE_KEY),PreferencesTreeItem.COLOR_PANEL)));
			
			
		
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
		*/
		
		root.add(referentielItem);
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
				Object o = e.getSource () ;
				int panel = ((PreferenceTreeItem)node.getUserObject()).getPanel();		
				String key = ((PreferenceTreeItem)node.getUserObject()).getKey();
				/*
				if (panel == PreferenceTreeItem.COLOR_PANEL)
				{
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				
				if(panel == PreferenceTreeItem.PATH_PANEL)
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
				*/
				if(panel == PreferenceTreeItem.DESC_PANEL)
				{
					mPrefDiag.getDescriptionPanel().setVisible(true);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				if( panel == PreferenceTreeItem.LANGUAGE_PANEL)
				{
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(true);
				}
				mPrefDiag.setInnerPanel(panel,key);  
			}
		}
	}
}
