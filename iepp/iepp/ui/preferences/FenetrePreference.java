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
	
	private PanneauDescription panneauDescription;
	private PanneauLangue panneauLangue;
	
	private JButton apply;
	private JButton ok;
	private JButton cancel; 
	
	
	public FenetrePreference (FenetrePrincipale parent)
	{
		super(parent, Application.getApplication().getTraduction("Preferences"), true);
		this.setSize(600, 450);
		// gestionnaire de mise en page
		this.getContentPane().setLayout(new BorderLayout());
		
		// cr�er l'arbre de navigation
		ArbrePreferences tree = new ArbrePreferences(this);
		this.initPreferencesDialog();
		
		Container panelMarge = getContentPane();
		// cr�er le panneau qui va changer
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
		this.panneau.add(this.panneauDescription,BorderLayout.CENTER);
		
		// create the panel
		panelMarge.add(this.panneau,BorderLayout.CENTER);
		panelMarge.add(new JLabel(" "),BorderLayout.NORTH);
		panelMarge.add(new JLabel("   "),BorderLayout.EAST);
		panelMarge.add(new JLabel("   "),BorderLayout.WEST);
		panelMarge.add(new JLabel(" "),BorderLayout.SOUTH);
		
		
		Rectangle bounds = parent.getBounds();
		this.setLocation(bounds.x+ (int) bounds.width / 2 - this.getWidth() / 2,
							 bounds.y + bounds.height / 2 - this.getHeight() / 2);
		this.setResizable(false);
		this.setVisible(true);
	}
	
	public void initPreferencesDialog ()
	{
		this.panneauDescription = new PanneauDescription(Application.getApplication().getTraduction(PanneauDescription.GENERAL_KEY)) ;
		this.panneauDescription.openPanel(PanneauDescription.GENERAL_KEY);
		this.panneauDescription.setVisible(true);
		
		this.panneauLangue = new PanneauLangue(Application.getApplication().getTraduction(PanneauLangue.LANGUAGE_PANEL_KEY)); 
		this.panneauDescription.setVisible(false);
	}
	
	public void setInnerPanel(int panel,String key)
	{
		/*
		if (panel == PreferenceTreeItem.COLOR_PANEL)
			panneau.add(mColorFontOption.openPanel(key),BorderLayout.CENTER);
		if(panel == PreferenceTreeItem.PATH_PANEL)
			panneau.add(mDefaultOption.openPanel(key),BorderLayout.CENTER);
		if(panel == PreferenceTreeItem.ERROR_PANEL)
			panneau.add(mWindowsOption.openPanel(key),BorderLayout.CENTER);
		*/
		if(panel == PreferenceTreeItem.DESC_PANEL)
			panneau.add(this.panneauDescription.openPanel(key),BorderLayout.CENTER);		
		if(panel == PreferenceTreeItem.LANGUAGE_PANEL)                                 						
			panneau.add(this.panneauLangue.openPanel(key),BorderLayout.CENTER);
	}
	
	public PanneauDescription getDescriptionPanel()
	{
		return this.panneauDescription ;
	}
	public PanneauLangue getLanguagePanel()
	{
		return this.panneauLangue;
	}
	
	/**
	 * 
	 */
	private void rafraichirLangue() 
	{
		Application.getApplication().getFenetrePrincipale().rafraichirLangue();
		this.panneauDescription.rafraichirLangue();
		this.panneauLangue.rafraichirLangue();
	}
	
	private void save()
	{
		// sauver tous les param�tres
	 	this.panneauLangue.save();
	 	if (this.panneauLangue.hasLanguageChanged())
	 	{
	 		this.rafraichirLangue();
	 	}
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//r�cup�rer l'objet source de l'�v�nement re�u
			 Object source = e.getSource();
			 // selon cet objet, r�agir en cons�quence
			 if (source == FenetrePreference.this.ok)
			 {
			 	FenetrePreference.this.save();
			 	FenetrePreference.this.dispose();
			 }
			 else if (source == FenetrePreference.this.cancel)
			 {
			 	// enlever la bo�te sans rien changer
			 	FenetrePreference.this.dispose();
			 }
			 else if (source == FenetrePreference.this.apply)
			 {
			 	// sauver tous les param�tres
			 	FenetrePreference.this.save();
			 }
		}
	}
}
