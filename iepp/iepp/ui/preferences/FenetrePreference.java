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

import java.awt.BorderLayout;
import java.awt.Container;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import iepp.Application;
import iepp.ui.FenetrePrincipale;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class FenetrePreference extends JDialog 
{
	/**
	 * Panneau sur lequel s'affiche l'arbre et la partie de droite 
	 * qui change selon l'option choisie
	 */
	private JPanel panneau ;
	
	private PanneauDP panneauDP;
	private PanneauDPGeneration panneauDPGeneration;
	private PanneauDPDescription panneauDPDesc;
	private PanneauDiagramme panneauDiagramme;
	private PanneauReferentiel panneauReferentiel;
	private PanneauDescription panneauDescription;
	private PanneauLangue panneauLangue;
	private PanneauGeneration panneauGeneration;
	
	private JButton apply;
	private JButton ok;
	private JButton cancel; 
	
	private int type_courant;
	
	public static final int TYPE_DP = 0;
	public static final int TYPE_GENERATION = 1;
	public static final int TYPE_PREFERENCES = 2;
	
	
	public FenetrePreference (FenetrePrincipale parent, int type)
	{
		super(parent, true);
		this.setSize(600, 450);
		
		this.type_courant = type;
		
		// gestionnaire de mise en page
		this.getContentPane().setLayout(new BorderLayout());
		
		// créer l'arbre de navigation
		ArbrePreferences tree = new ArbrePreferences(this);
		
		Container panelMarge = getContentPane();
		// créer le panneau qui va changer
		this.panneau = new JPanel() ;
		this.panneau.setLayout(new BorderLayout());
		this.panneau.add(tree,BorderLayout.WEST) ;
		
		// ajouter the buttons ok and cancel and apply
		ManagerButton manageButt = new ManagerButton() ;
		Box bottom = Box.createHorizontalBox() ;
		this.apply = new JButton (Application.getApplication().getTraduction("Appliquer")) ;
		this.apply.addActionListener(manageButt);
		this.ok = new JButton (Application.getApplication().getTraduction("OK")) ;
		this.ok.addActionListener(manageButt);
		this.cancel = new JButton (Application.getApplication().getTraduction("Annuler"));
		this.cancel.addActionListener(manageButt);
		bottom.add(Box.createHorizontalGlue());
		bottom.add(this.apply);
		bottom.add(this.ok);
		bottom.add(this.cancel);
		
		tree.setBorder(BorderFactory.createLoweredBevelBorder());
		this.panneau.add(bottom,BorderLayout.SOUTH);
		//this.panneau.add(this.panneauDescription,BorderLayout.CENTER);

		// create the panel
		panelMarge.add(this.panneau,BorderLayout.CENTER);
		panelMarge.add(new JLabel(" "),BorderLayout.NORTH);
		panelMarge.add(new JLabel("   "),BorderLayout.EAST);
		panelMarge.add(new JLabel("   "),BorderLayout.WEST);
		panelMarge.add(new JLabel(" "),BorderLayout.SOUTH);
		
		this.initPreferencesDialog();
		
		Rectangle bounds = parent.getBounds();
		this.setLocation(bounds.x+ (int) bounds.width / 2 - this.getWidth() / 2,
							 bounds.y + bounds.height / 2 - this.getHeight() / 2);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void initPreferencesDialog ()
	{
		this.panneauDescription = new PanneauDescription(Application.getApplication().getTraduction(PanneauDescription.GENERAL_KEY)) ;
		this.panneauDescription.setVisible(false);
		
		this.panneauLangue = new PanneauLangue(Application.getApplication().getTraduction(PanneauLangue.LANGUAGE_PANEL_KEY)); 
		this.panneauLangue.setVisible(false);
		
		this.panneauGeneration = new PanneauGeneration(Application.getApplication().getTraduction(PanneauGeneration.GENERATION_PANEL_KEY)); 
		this.panneauGeneration.setVisible(false);
		
		this.panneauReferentiel= new PanneauReferentiel(Application.getApplication().getTraduction(PanneauReferentiel.REPOSITORY_PANEL_KEY)); 
		this.panneauReferentiel.setVisible(false);
		
		this.panneauDiagramme= new PanneauDiagramme(Application.getApplication().getTraduction(PanneauDiagramme.DIAGRAM_PANEL_KEY)); 
		this.panneauDiagramme.setVisible(false);
		
		this.panneauDP= new PanneauDP(Application.getApplication().getTraduction(PanneauDP.DP_PANEL_KEY)); 
		this.panneauDP.setVisible(false);
		
		this.panneauDPDesc = new PanneauDPDescription(Application.getApplication().getTraduction(PanneauDPDescription.DP_DESCRIPTION_PANEL_KEY)); 
		this.panneauDPDesc.setVisible(false);
		
		this.panneauDPGeneration = new PanneauDPGeneration(Application.getApplication().getTraduction(PanneauDPGeneration.DP_GENERATION_PANEL_KEY)); 
		this.panneauDPGeneration.setVisible(false);
		
	   
		switch (this.type_courant)
		{
		    case FenetrePreference.TYPE_PREFERENCES:
		        this.panneauDescription.setVisible(true);
		        this.setInnerPanel(PreferenceTreeItem.DESC_PANEL, PanneauDescription.GENERAL_KEY);
		        this.setTitle(Application.getApplication().getTraduction("Preferences"));
						break;
			case FenetrePreference.TYPE_DP:
			    this.panneauDescription.setVisible(true);
			    this.setInnerPanel(PreferenceTreeItem.DESC_PANEL, PanneauDescription.DP_KEY);
			    this.setTitle(Application.getApplication().getTraduction("Proprietes_DP"));
			    break;
			case FenetrePreference.TYPE_GENERATION:
					
						break;
		}
	}
	
	public void setInnerPanel(int panel,String key)
	{
		if(panel == PreferenceTreeItem.DP_GENERATION_PANEL)
			panneau.add(this.panneauDPGeneration.openPanel(key),BorderLayout.CENTER);
	    else if(panel == PreferenceTreeItem.DP_DESCRIPTION_PANEL)
			panneau.add(this.panneauDPDesc.openPanel(key),BorderLayout.CENTER);
	    else if(panel == PreferenceTreeItem.DP_PANEL)
			panneau.add(this.panneauDP.openPanel(key),BorderLayout.CENTER);
	    else if(panel == PreferenceTreeItem.DIAGRAM_PANEL)
			panneau.add(this.panneauDiagramme.openPanel(key),BorderLayout.CENTER);
		else if(panel == PreferenceTreeItem.REPOSITORY_PANEL)
			panneau.add(this.panneauReferentiel.openPanel(key),BorderLayout.CENTER);
		else if(panel == PreferenceTreeItem.GENERATION_PANEL)
			panneau.add(this.panneauGeneration.openPanel(key),BorderLayout.CENTER);
		else if(panel == PreferenceTreeItem.DESC_PANEL)
			panneau.add(this.panneauDescription.openPanel(key),BorderLayout.CENTER);
		else if(panel == PreferenceTreeItem.LANGUAGE_PANEL)   
			panneau.add(this.panneauLangue.openPanel(key),BorderLayout.CENTER);
	}
	
	public PanneauDPGeneration getDPGenPanel()
	{
	    return this.panneauDPGeneration;
	}
	
	public PanneauDPDescription getDPDescPanel()
	{
	    return this.panneauDPDesc;
	}
	
	public PanneauDP getDPPanel()
	{
	    return this.panneauDP;
	}
	
	public PanneauDescription getDescriptionPanel()
	{
		return this.panneauDescription ;
	}
	public PanneauLangue getLanguagePanel()
	{
		return this.panneauLangue;
	}
	public PanneauGeneration getGenerationPanel()
	{
		return this.panneauGeneration;
	}
	public PanneauReferentiel getReferentielPanel()
	{
		return this.panneauReferentiel;
	}
	public PanneauDiagramme getDiagrammePanel()
	{
		return this.panneauDiagramme;
	}
	
	public int getType()
	{
	    return this.type_courant;
	}
	
	private void save()
	{
	    switch (this.type_courant)
		{
		    case FenetrePreference.TYPE_PREFERENCES:
								//sauver tous les paramètres
								this.panneauReferentiel.save();
							 	this.panneauLangue.save();
							 	this.panneauGeneration.save();
							 	if (this.panneauLangue.hasLanguageChanged())
							 	{
							 		Application.getApplication().getFenetrePrincipale().rafraichirLangue();
							 		this.dispose();
							 	}
								break;
			case FenetrePreference.TYPE_DP:
			    				this.panneauDP.save();
								this.panneauDPDesc.save();
								this.panneauDPGeneration.save();
								break;
			case FenetrePreference.TYPE_GENERATION:
			    				this.panneauDP.save();
			    				break;
		}
		
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			 Object source = e.getSource();
			 // selon cet objet, réagir en conséquence
			 if (source == FenetrePreference.this.ok)
			 {
			 	FenetrePreference.this.save();
			 	FenetrePreference.this.dispose();
			 }
			 else if (source == FenetrePreference.this.cancel)
			 {
			 	// enlever la boîte sans rien changer
			 	FenetrePreference.this.dispose();
			 }
			 else if (source == FenetrePreference.this.apply)
			 {
			 	// sauver tous les paramètres
			 	FenetrePreference.this.save();
			 }
		}
	}
}
