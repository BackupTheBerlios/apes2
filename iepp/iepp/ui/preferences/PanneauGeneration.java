/*
 * IEPP: Isi Engineering Process Publisher
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
 * 
 */
package iepp.ui.preferences;

import iepp.Application;
import iepp.Projet;
import iepp.application.CGenererSite;
import iepp.application.ageneration.GenerationManager;
import iepp.domaine.DefinitionProcessus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.border.TitledBorder;

import util.IconManager;


public class PanneauGeneration extends PanneauOption 
{
	private DefinitionProcessus defProc;
	private JButton bGenerer;
	private FenetrePreference fenPref;
	private JTextArea mOutput;
	
	public static final String GENERATION_PANEL_KEY = "Generation_GO_Desc";
	
	public PanneauGeneration(String name, FenetrePreference fenPref)
	{
		this.fenPref = fenPref;
		Projet p = Application.getApplication().getProjet();
	    if (p != null)
	    {
	        this.defProc = Application.getApplication().getProjet().getDefProc();
	    }
	    
	
		
		this.mTitleLabel = new JLabel (name) ;
		this.setLayout(new BorderLayout());
		mPanel = new JPanel() ;
		GridBagLayout gridbag = new GridBagLayout();
		mPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		ManagerButton man = new ManagerButton();

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

		//linefeed
		c.weighty = 1;  
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);

		//linefeed
		c.weighty = 1;  
		c.gridx = 0; c.gridy = 2;
		c.fill = GridBagConstraints.BOTH; //end row
		this.mOutput = new JTextArea(13, 25);
		this.mOutput.setAutoscrolls(true);
		this.mOutput.setEditable(false);
		JScrollPane js = new JScrollPane(this.mOutput);
		mPanel.add(js, c);
		
		
		this.bGenerer = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ToolsGenerate.gif"));
		this.bGenerer.setText( Application.getApplication().getTraduction("Generer_le_site_Web")) ;
		this.bGenerer.addActionListener(man);
		c.gridy = 4;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		mPanel.add(this.bGenerer, c);
		
		//linefeed
		c.weighty = 1;  
		c.gridx = 0; c.gridy = 5;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
	
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);  
	}
	
	/**
	 * 
	 */
	public void afficherStatistiques()
	{
		//TODO SP rajouter langue
		this.mOutput.append("Nombre de pages composant : \t\t " + GenerationManager.nbComposants + "\n");
		this.mOutput.append("Nombre de pages rôle : \t\t " + GenerationManager.nbRoles + "\n");
		this.mOutput.append("Nombre de pages produit : \t\t " + GenerationManager.nbProduits + "\n");
		this.mOutput.append("Nombre de pages activité : \t\t " + GenerationManager.nbActivites + "\n");
		this.mOutput.append("Nombre de pages définition travail : \t " + GenerationManager.nbDefinitionsTravail + "\n");
		this.mOutput.append("Nombre de pages diagramme : \t " + GenerationManager.nbDiagrammes + "\n");
		this.mOutput.append("Nombre de pages guide : \t\t " + GenerationManager.nbGuides + "\n");
		this.mOutput.append("Nombre de pages paquetage présentation : \t " + GenerationManager.nbPaquetagesPresentation + "\n");
		this.mOutput.append("Nombre de pages paquetages: \t\t " + GenerationManager.nbPaquetages + "\n");
		this.mOutput.append("Nombre de pages élément présentation : \t " + GenerationManager.nbElementPresentation + "\n");
		this.mOutput.append("Nombre de pages accueil : \t\t 1"+ "\n");
		
		this.mOutput.append("-------------------------------------------------------------------------------\n");
		
		this.mOutput.append("Nombre de pages total : \t\t " + GenerationManager.nbPagesTotal + "\n");
	}
	
	public PanneauOption openPanel(String key)
	{
		this.setName(Application.getApplication().getTraduction(key)) ;
		return this ;
	}

	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			Object source = e.getSource();
			
			
			if (source == PanneauGeneration.this.bGenerer)
			{	
				//sauver les paramètres
				PanneauGeneration.this.fenPref.save();
				CGenererSite c = new CGenererSite(PanneauGeneration.this.defProc) ;
				if (c.executer())
				{
					 // afficher les statistiques
					 PanneauGeneration.this.mOutput.setText("");
					 PanneauGeneration.this.afficherStatistiques();
					 
					 JOptionPane.showMessageDialog(Application.getApplication().getFenetrePrincipale(),Application.getApplication().getTraduction("Generation_ok"),
					 				Application.getApplication().getTraduction("Generation_site_titre"),
									JOptionPane.INFORMATION_MESSAGE);
					 
				}
			}
		}
	}


	
}
