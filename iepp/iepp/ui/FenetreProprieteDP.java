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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.*;

import util.SmartChooser;

import iepp.Application;
import iepp.application.ageneration.GenerationManager;
import iepp.domaine.DefinitionProcessus;
import iepp.domaine.IdObjetModele;

/**
 * FenetreProprieteDP.java
 */
public class FenetreProprieteDP extends FenetrePropriete implements ActionListener
{
	private DefinitionProcessus defProc;
	
	private JButton parcourirGen ;
	
	private JButton ok, annuler ;
	private JTextField sNomDP;
	private JTextField sAuteurDP;
	private JTextField sEmailDP;
	private JTextArea sCommentaireDP;
	private JTextField sRepDest;
	
	public FenetreProprieteDP(IdObjetModele idDefProc)
	{
		this.defProc = (DefinitionProcessus)idDefProc.getRef();
		this.getContentPane().setLayout(new BorderLayout());
		
		// propriétés générales
		JPanel pGen = new JPanel();
		pGen.setBorder(BorderFactory.createTitledBorder( Application.getApplication().getTraduction("Proprietes_Generales_DP")));
		pGen.setLayout(new BorderLayout());
		
		JPanel pGenerales = new JPanel(new GridLayout(4,2,0,15));
		
		JLabel lNomDP = new JLabel(Application.getApplication().getTraduction("Nom_DP"));
		pGenerales.add(lNomDP);

		this.sNomDP = new JTextField(this.defProc.getNomDefProc());
		pGenerales.add(this.sNomDP);
		
		JLabel lAuteurDP = new JLabel(Application.getApplication().getTraduction("Auteur_DP"));
		pGenerales.add(lAuteurDP);
		
		this.sAuteurDP = new JTextField(this.defProc.getAuteur());
		pGenerales.add(this.sAuteurDP);
		
		JLabel lEmailDP = new JLabel(Application.getApplication().getTraduction("E_mail_DP"));
		pGenerales.add(lEmailDP);
		
		this.sEmailDP = new JTextField(this.defProc.getEmailAuteur());
		pGenerales.add(this.sEmailDP);
		
		JLabel lCommentaireDP = new JLabel(Application.getApplication().getTraduction("Commentaire_DP"));
		pGenerales.add(lCommentaireDP);
		pGenerales.add(new JLabel());

		this.sCommentaireDP = new JTextArea(this.defProc.getCommentaires());
		this.sCommentaireDP.setPreferredSize(new Dimension(100,150));
		pGen.add(new JScrollPane(this.sCommentaireDP), BorderLayout.CENTER);
		pGen.add(pGenerales, BorderLayout.NORTH);
		
		
		// propriétés liées à la génération
		JPanel pGeneration = new JPanel(new BorderLayout());
		pGeneration.setBorder(BorderFactory.createTitledBorder( Application.getApplication().getTraduction("Proprietes_Generation_DP")));
		pGeneration.setPreferredSize(new Dimension(250,250));
		
		JPanel pGener = new JPanel(new GridLayout(2,1,10,10));
		
		JLabel lDossierGen = new JLabel(Application.getApplication().getTraduction("Dossier_Generation"));
		pGener.add(lDossierGen);
		pGener.add(new JLabel());
		
		this.sRepDest = new JTextField(GenerationManager.getInstance().getCheminGeneration());
		this.sRepDest.setEditable(false);
		pGener.add(this.sRepDest);
		
		this.parcourirGen = new JButton(Application.getApplication().getTraduction("Parcourir"));
		this.parcourirGen.addActionListener(this);
		pGener.add(this.parcourirGen);
		
		pGeneration.add(pGener);
		
		
		// bouton ok ou annuler
		JPanel pBas = new JPanel();
		this.ok = new JButton(Application.getApplication().getTraduction("OK"));
		this.ok.addActionListener(this);
		this.annuler = new JButton(Application.getApplication().getTraduction("Annuler"));
		this.annuler.addActionListener(this);
		pBas.add(this.ok);
		pBas.add(this.annuler);
		
		this.getContentPane().add(pGen, BorderLayout.NORTH);
		this.getContentPane().add(pGeneration, BorderLayout.CENTER);
		this.getContentPane().add(pBas, BorderLayout.SOUTH);
	}
	
	
	public void actionPerformed( ActionEvent e )
	{
		if (e.getSource() == this.ok)
		{
			if (! this.sNomDP.getText().equals(""))
			{
				this.defProc.setNomDefProc(this.sNomDP.getText());
			}
			if (this.verifierDonnees())
			{
				this.defProc.setAuteur(this.sAuteurDP.getText());
				this.defProc.setEmailAuteur(this.sEmailDP.getText());
				this.defProc.setCommentaires(this.sCommentaireDP.getText());
				this.defProc.setRepertoireGeneration(this.sRepDest.getText());
				GenerationManager.getInstance().setCheminGeneration(this.sRepDest.getText());
				this.dispose();
			}
		}
		else if (e.getSource() == this.parcourirGen)
		{
			JFileChooser chooser = SmartChooser.getChooser();
			chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
			if (chooser.showDialog(this , Application.getApplication().getTraduction("Choisir_Dossier"))
					==JFileChooser.APPROVE_OPTION)
			{
				this.sRepDest.setText(chooser.getSelectedFile().getAbsolutePath());
			}
		}
		else if (e.getSource() == this.annuler)
		{
			this.dispose();
		}
	}
	
	/**
	 * Vérifie les informations saisies : les champs nom, auteur et mail ne doivent pas être vides,
	 * le nom du processus ne doit pas contenir les caractères /\":*<>|?
	 * @return
	 */
	public boolean verifierDonnees(){
		boolean atTrouve = false;
		boolean pointTrouve = false;
		if(! this.sNomDP.getText().equals(""))
		{
			for(int j = 0; j < this.sNomDP.getText().length(); j++)
			{
				char c = this.sNomDP.getText().charAt(j);
				if(c=='/'||c=='\\'||c=='"'||c==':'||c=='*'||c=='<'||c=='>'||c=='|'||c=='?')
				{
					JOptionPane.showMessageDialog(this,Application.getApplication().getTraduction("ERR_Nom_Proc_Incorrect"),Application.getApplication().getTraduction("M_creer_proc_titre"),JOptionPane.WARNING_MESSAGE); 
					return false;
				}
			}
		}
		if(this.sAuteurDP.getText().equals("")){
			JOptionPane.showMessageDialog(this,Application.getApplication().getTraduction("M_nomauteur"),Application.getApplication().getTraduction("M_creer_proc_titre"),JOptionPane.WARNING_MESSAGE);
			return false;
		}
		if(this.sEmailDP.getText().equals("")){
			JOptionPane.showMessageDialog(this,Application.getApplication().getTraduction("M_emailauteur"),Application.getApplication().getTraduction("M_creer_proc_titre"),JOptionPane.WARNING_MESSAGE);
			return false;
		}
		for(int i = 0; i < this.sEmailDP.getText().length(); i++){
			if(this.sEmailDP.getText().charAt(i)== '@')
				atTrouve = true;
			if(this.sEmailDP.getText().charAt(i)=='.')
				pointTrouve = true;
		}
		if(atTrouve == false || pointTrouve == false){
			JOptionPane.showMessageDialog(this,Application.getApplication().getTraduction("M_emailinvalide"),Application.getApplication().getTraduction("M_creer_proc_titre"),JOptionPane.WARNING_MESSAGE);
			return false;
		}
		return true;
	}
}
