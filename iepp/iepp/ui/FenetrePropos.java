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

package iepp.ui;

import java.awt.BorderLayout;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;

import iepp.Application;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;

import util.IconManager;


 
/**
 *
 */
public class FenetrePropos extends JDialog {
	
	private JPanel 		SC_Informations = new JPanel();
	private JPanel 		SC_Titre = new JPanel();
	private JPanel 		SC_Legal = new JPanel();
	private JLabel 		SIC_Icone = new JLabel();
	private JPanel 		SC_TitreInfo = new JPanel();
	private JLabel 		STC_Titre = new JLabel();
	private JLabel 		STC_Version = new JLabel();
	private JPanel 		SC_LegalInfo = new JPanel();
	private JLabel 		STC_Copyright = new JLabel();
	private JTextArea 	STC_Info = new JTextArea();
	private JPanel 		SC_Bouton = new JPanel();
	private JButton 	BP_Fermer = new JButton();
		
	public FenetrePropos(FenetrePrincipale parent)
	{
		//Création
		super(parent, Application.getApplication().getTraduction("Propos"),true);
		this.setDefaultCloseOperation(DISPOSE_ON_CLOSE);
		
		//Création du bouton
		BP_Fermer.setText(Application.getApplication().getTraduction("Fermer"));
		BP_Fermer.setMnemonic('E');
		BP_Fermer.addActionListener(new java.awt.event.ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				dispose();
			}
		});
		// Création et ajout au cadre du bouton
 	    SC_Bouton.setLayout(new FlowLayout());
		SC_Bouton.add(BP_Fermer, null);
		
		//Création du cadre de titre
		SC_Titre.setLayout(new FlowLayout());
		//Creation de l'icône
		SIC_Icone.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "iepp_logo_mini1.jpg"));
		//Création du cadre d'info du titre
		SC_TitreInfo.setLayout(new GridLayout(2,1));
		//Création de l'info	
		STC_Titre.setFont(new java.awt.Font("MS_SansSerif", 0, 12));
		STC_Titre.setText("IEPP : Isi Engineering Process Publisher");
		//Création de la version
		STC_Version.setFont(new java.awt.Font("MS_SansSerif", 0, 12));
		//TODO Changer le numéro de version
		STC_Version.setText("Version " + Application.NUMVESION);
		//Ajout aux panels
		SC_Titre.add(SIC_Icone, null);
		SC_Titre.add(SC_TitreInfo, null);
		SC_TitreInfo.add(STC_Titre, null);
		SC_TitreInfo.add(STC_Version, null);
		
		//Création du cadre d'info
		SC_Informations.setLayout(new BorderLayout());
		//Création du cadre de legal
		SC_Legal.setLayout(new BorderLayout());
		SC_LegalInfo.setLayout(new BorderLayout());
		//Création du copyright
		STC_Copyright.setBorder(new TitledBorder(""));
		STC_Copyright.setHorizontalAlignment(SwingConstants.CENTER);
		STC_Copyright.setHorizontalTextPosition(SwingConstants.CENTER);
		STC_Copyright.setFont(new java.awt.Font("MS_SansSerif", 0, 12));
		STC_Copyright.setText("copyright 2003-2004 Iepp");
		//Création de l'info du legal
		STC_Info.setEnabled(true);
		STC_Info.setEditable(false);
		STC_Info.setText(Application.getApplication().getTraduction("propos_texte")+
						 Application.getApplication().getTraduction("propos_texte2")+
						 Application.getApplication().getTraduction("propos_texte3")+
						 Application.getApplication().getTraduction("propos_texte4"));
		//Ajout aux panels				
		SC_Informations.add(SC_Titre, BorderLayout.NORTH);
		SC_Informations.add(SC_Legal, BorderLayout.CENTER);
		SC_Legal.add(SC_LegalInfo, BorderLayout.NORTH);
		SC_LegalInfo.add(STC_Copyright, BorderLayout.CENTER);
		SC_Legal.add(STC_Info, BorderLayout.CENTER);
		
		//Remplissage
		this.getContentPane().add(SC_Informations,BorderLayout.CENTER);
		this.getContentPane().add(SC_Bouton,BorderLayout.SOUTH);
		
		//Affichage
		this.setResizable(false);
		this.pack();
		Rectangle bounds = parent.getBounds();
		this.setLocation(bounds.x+ (int) bounds.width / 2 - this.getWidth() / 2, bounds.y + bounds.height / 2 - this.getHeight() / 2);
		this.show();		
	}
}
