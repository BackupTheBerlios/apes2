
package iepp.ui.preferences;

import iepp.Application;

import java.awt.Dimension;
import java.util.Enumeration;

import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreeNode;
import javax.swing.tree.TreePath;


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
	
		generalItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDiagramme.DIAGRAM_PANEL_KEY,Application.getApplication().getTraduction(PanneauDiagramme.DIAGRAM_PANEL_KEY),PreferenceTreeItem.DIAGRAM_PANEL)));    
	
		DefaultMutableTreeNode referentielItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.REPOSITORY_KEY,Application.getApplication().getTraduction(PanneauDescription.REPOSITORY_KEY),PreferenceTreeItem.DESC_PANEL));
		
		referentielItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauReferentiel.REPOSITORY_PANEL_KEY,Application.getApplication().getTraduction(PanneauReferentiel.REPOSITORY_PANEL_KEY),PreferenceTreeItem.REPOSITORY_PANEL)));    
	
		DefaultMutableTreeNode DPItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.DP_KEY,Application.getApplication().getTraduction(PanneauDescription.DP_KEY),PreferenceTreeItem.DESC_PANEL));
		
		DPItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDP.DP_PANEL_KEY,Application.getApplication().getTraduction(PanneauDP.DP_PANEL_KEY),PreferenceTreeItem.DP_PANEL)));    
		
		DPItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDPDescription.DP_DESCRIPTION_PANEL_KEY,Application.getApplication().getTraduction(PanneauDPDescription.DP_DESCRIPTION_PANEL_KEY),PreferenceTreeItem.DP_DESCRIPTION_PANEL)));    
	
		DefaultMutableTreeNode generationItem = new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDescription.GENERATION_KEY,Application.getApplication().getTraduction(PanneauDescription.GENERATION_KEY),PreferenceTreeItem.DESC_PANEL));
		
		generationItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauGenerationOption.GENERATION_OPTION_PANEL_KEY,Application.getApplication().getTraduction(PanneauGenerationOption.GENERATION_OPTION_PANEL_KEY),PreferenceTreeItem.GENERATION_OPTION_PANEL_KEY)));    
	
		generationItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauDPGeneration.DP_GENERATION_PANEL_KEY,Application.getApplication().getTraduction(PanneauDPGeneration.DP_GENERATION_PANEL_KEY),PreferenceTreeItem.DP_GENERATION_PANEL)));    
	
		generationItem.add(new DefaultMutableTreeNode(new PreferenceTreeItem
				(PanneauGeneration.GENERATION_PANEL_KEY,Application.getApplication().getTraduction(PanneauGeneration.GENERATION_PANEL_KEY),PreferenceTreeItem.GENERATION_PANEL)));    
	
		switch (this.mPrefDiag.getType())
		{
		    case FenetrePreference.TYPE_PREFERENCES:
						        root.add(generalItem);
								root.add(referentielItem);
								break;
			case FenetrePreference.TYPE_DP:
			    				root.add(DPItem);
								root.add(generationItem);		
								break;
			case FenetrePreference.TYPE_GENERATION:
								root.add(DPItem);
								root.add(generationItem);	
			    				break;
		}
		
		this.setRootVisible(false);
		this.addTreeSelectionListener(gp);
		this.setModel (new DefaultTreeModel(root));
		this.setPreferredSize(new Dimension(170,50));
		this.expandAll(this, true);
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

				if(panel == PreferenceTreeItem.GENERATION_OPTION_PANEL_KEY)
				{
					mPrefDiag.getGenOptionPanel().setVisible(true);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.DP_GENERATION_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(true);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.DP_DESCRIPTION_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(true);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.DIAGRAM_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(true);
					mPrefDiag.getReferentielPanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.DP_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(true);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.REPOSITORY_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(true);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.GENERATION_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(true);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
				}
				else if(panel == PreferenceTreeItem.DESC_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(true);
					mPrefDiag.getLanguagePanel().setVisible(false);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
				}
				else if( panel == PreferenceTreeItem.LANGUAGE_PANEL)
				{
					mPrefDiag.getGenOptionPanel().setVisible(false);
					mPrefDiag.getDPGenPanel().setVisible(false);
				    mPrefDiag.getDPDescPanel().setVisible(false);
				    mPrefDiag.getDPPanel().setVisible(false);
				    mPrefDiag.getDiagrammePanel().setVisible(false);
					mPrefDiag.getDescriptionPanel().setVisible(false);
					mPrefDiag.getLanguagePanel().setVisible(true);
					mPrefDiag.getGenerationPanel().setVisible(false);
					mPrefDiag.getReferentielPanel().setVisible(false);
				}
				mPrefDiag.setInnerPanel(panel,key);  
			}
		}
	}

	//	 If expand is true, expands all nodes in the tree.
    // Otherwise, collapses all nodes in the tree.
    public void expandAll(JTree tree, boolean expand) {
        TreeNode root = (TreeNode)tree.getModel().getRoot();
    
        // Traverse tree from root
        expandAll(tree, new TreePath(root), expand);
    }
    
    private void expandAll(JTree tree, TreePath parent, boolean expand) {
        // Traverse children
        TreeNode node = (TreeNode)parent.getLastPathComponent();
        if (node.getChildCount() >= 0) {
            for (Enumeration e=node.children(); e.hasMoreElements(); ) {
                TreeNode n = (TreeNode)e.nextElement();
                TreePath path = parent.pathByAddingChild(n);
                expandAll(tree, path, expand);
            }
        }
    
        // Expansion or collapse must be done bottom-up
        if (expand) {
            tree.expandPath(parent);
        } else {
            tree.collapsePath(parent);
        }
    }
}
