
package iepp.ui.preferences;

import iepp.Application;

import java.awt.Dimension;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;


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

		DefaultMutableTreeNode generalItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.GENERAL_KEY,Application.getApplication().getTraduction(PanneauDescription.GENERAL_KEY),PreferenceTreeItem.DESC_PANEL));
		
		generalItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauLangue.LANGUAGE_PANEL_KEY,Application.getApplication().getTraduction(PanneauLangue.LANGUAGE_PANEL_KEY),PreferenceTreeItem.LANGUAGE_PANEL)));    
	
		DefaultMutableTreeNode generationItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.GENERATION_KEY,Application.getApplication().getTraduction(PanneauDescription.GENERATION_KEY),PreferenceTreeItem.DESC_PANEL));
		
		generationItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauGeneration.GENERATION_PANEL_KEY,Application.getApplication().getTraduction(PanneauGeneration.GENERATION_PANEL_KEY),PreferenceTreeItem.GENERATION_PANEL)));    
	
		DefaultMutableTreeNode referentielItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.REPOSITORY_KEY,Application.getApplication().getTraduction(PanneauDescription.REPOSITORY_KEY),PreferenceTreeItem.DESC_PANEL));
		
		referentielItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauReferentiel.REPOSITORY_PANEL_KEY,Application.getApplication().getTraduction(PanneauReferentiel.REPOSITORY_PANEL_KEY),PreferenceTreeItem.REPOSITORY_PANEL)));    
	
		
		
		root.add(generalItem);
		root.add(generationItem);
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
				if(panel == PreferenceTreeItem.REPOSITORY_PANEL)
				{
					mPrefDiag.getReferentielPanel().setVisible(true);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				if(panel == PreferenceTreeItem.GENERATION_PANEL)
				{
					mPrefDiag.getGenerationPanel().setVisible(true);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
				}
				if(panel == PreferenceTreeItem.DESC_PANEL)
				{
					mPrefDiag.getDescriptionPanel().setVisible(true);
					mPrefDiag.getLanguagePanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
				}
				if( panel == PreferenceTreeItem.LANGUAGE_PANEL)
				{
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(true);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
				}
				mPrefDiag.setInnerPanel(panel,key);  
			}
		}
	}
}
