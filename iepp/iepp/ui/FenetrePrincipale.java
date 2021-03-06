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

import iepp.Application;
import iepp.Projet;
import iepp.application.CCreerReferentiel;
import iepp.application.CExporterProcessus;
import iepp.application.CFermerProjet;
import iepp.application.CGenererSite;
import iepp.application.CNouveauProjet;
import iepp.application.COuvrirDP;
import iepp.application.COuvrirReferentiel;
import iepp.application.aedition.CImprimerDiagramme;
import iepp.application.aedition.CZoomer;
import iepp.application.areferentiel.Referentiel;
import iepp.application.averification.CVerification;
import iepp.domaine.DefinitionProcessus;
import iepp.ui.ireferentiel.VueReferentielArbre;
import iepp.ui.iverification.PanneauVerification;
import iepp.ui.preferences.FenetrePreference;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.InputEvent;
import java.awt.event.KeyEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.util.Vector;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTabbedPane;
import javax.swing.JToolBar;
import javax.swing.KeyStroke;
import javax.swing.SwingConstants;

import util.IconManager;
import util.TaskMonitorDialog;

/**
 * Classe permettant de cr�er et d'afficher la fen�tre principale de l'outil
 */
public class FenetrePrincipale extends JFrame implements ActionListener
{
		/**
		 * place des onglets dans le TabbedPane
		 */ 
		public static final int INDEXTABREFERENTIEL = 1 ;
		public static final int INDEXTABPROCESSUS = 0 ;
		
		/**
		 * Arbre de la d�finition de processus
		 */
		private VueDPArbre vueDPArbre;


		/**
		 * Arbre du r�f�rentiel
		 */
		private VueReferentielArbre vueRefArbre ;
		
		/**
		 * Composant permettant de s�parer la fen�tre en 2 parties verticales
		 */
		private JSplitPane frame_split;
		private PanneauVerification panneauVerif ;
		
		
		// El�ments graphiques � afficher
		
		private JMenuBar barre_menu ;
		private JMenu fichier ;
				private JMenuItem nouveau_proc ;
				private JMenuItem nouveau_referentiel ;
				private JMenuItem ouvrir_referentiel ;
				private JMenuItem ouvrir_proc ;
				private JMenuItem fermer ;
				private JMenuItem sauver ;
				private JMenuItem imprimer ;
				private JMenuItem quitter ;
				
		private JMenu affichage ;
				private JMenuItem zoomin ;
				private JMenuItem zoomout ;
				private JMenuItem propriete ;
				
		private JMenu outil ;
				private JMenuItem verifier ;
				private JMenuItem exporter ;
				private JMenuItem generer ;
				
		private JMenu aide;
				private JMenuItem propos;
		
		private JToolBar barre_outil ;
				private JButton bnouveau_proc ;
				private JButton bnouveau_referentiel ;
				private JButton bouvrir_referentiel ;
				private JButton bouvrir_proc ;
				private JButton bsauver ;
				private JButton bfermer ;
				private JButton bverifier ;
				private JButton bgenerer ;
				private JButton bexporter ;
				private JButton bimprimer ;
				private JButton bzoomin ;
				private JButton bzoomout ;
				private JButton bpreferences ;	
				
			
		/**
		 * Panneau dans lequel s'affiche le diagramme
		 */
		private JPanel panneauGenerique;
		
		/**
		 * Onglets pour afficher l'arbre et le r�f�rentiel
		 */
		private JTabbedPane tabbedPane;
	
	
	/**
	 * Constructeur de la fenetre principale de l'outil
	 */
	public FenetrePrincipale()
	{	
		
		// appel au constructeur de JFrame
		super(Application.getApplication().getConfigPropriete("titre"));
		// Lorsqu'on essaye de fermer l'appli, ne rien faire c'est l'adapter qui se charge de 
		// fermer l'appli correctement
		this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
		// ecouteur de la fen�tre
		this.addWindowListener(new FenetreListener());
		// cr�ation du menu
		this.creerMenu();
		// rajouter des raccourcis clavier aux items du menu
		this.ajouterRaccourcis();
		// cr�ation de la barre d'outil
		this.creerBarreOutil();
		// cr�aton d'un arbre vide
		this.vueDPArbre = new VueDPArbre();
		// Cr�ation d'un arbre repr�sentant le r�f�rentiel
		this.vueRefArbre = new VueReferentielArbre () ;
		// taille de la fen�tre en fonction du syst�me d'accueil
		Rectangle r = this.getGraphicsConfiguration().getBounds();
		this.setBounds(r.x+10, r.y+10, r.width*5/6, r.height*5/6);
		Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
		Dimension frameSize = this.getSize();
		this.setLocation( (screenSize.width - frameSize.width) / 2,
										 (screenSize.height - frameSize.height) / 2);
		// placer les �l�ments au bon endroit dans la fen�tre 
		this.positionnerElements();
		// permettre de r�cup�rer les �v�nements utilisateur
		this.ajouterEcouteurs();
		this.majEtat();
		//this.panneauVerif.setDividerLocation(r.height*5/6);
	}
	
	/**
	 * Methode permettant de cr�er les �l�ments du menu et de les ajouter
	 */
	private void creerMenu()
	{
		// cr�ation de la barre de menu
		this.barre_menu = new JMenuBar();
		// positionner la barre de menu
		this.setJMenuBar(barre_menu);
				
		// Fichier
		this.fichier = new JMenu();
		this.barre_menu.add(this.fichier);
		this.nouveau_proc = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileNew.gif"));
		this.fichier.add(this.nouveau_proc);
		this.nouveau_referentiel = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "folder_new.png"));
		this.fichier.add(this.nouveau_referentiel);
		this.ouvrir_referentiel = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileOpen.gif"));
		this.fichier.add(this.ouvrir_referentiel);
		this.ouvrir_proc = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "fileopen.png"));
		this.fichier.add(this.ouvrir_proc);
		this.sauver = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileSave.gif"));
		this.fichier.add(this.sauver);
		this.fermer = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "fileclose.png"));
		this.fichier.add(this.fermer);
		this.fichier.addSeparator();
		
		this.imprimer = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FilePrint.gif"));
		this.fichier.add(this.imprimer);
		this.fichier.addSeparator();

		this.quitter = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileQuit.gif"));
		this.fichier.add(this.quitter);
		
		// affichage
		this.affichage = new JMenu();
		this.barre_menu.add(this.affichage);
		this.zoomin = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "zoomin.png"));
		this.affichage.add(this.zoomin);
		this.zoomout = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "zoomout.png"));
		this.affichage.add(this.zoomout);
		this.affichage.addSeparator();
		this.propriete = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "configure.png"));
		this.affichage.add(this.propriete);
		
		 // Outils
		this.outil = new JMenu();
		this.barre_menu.add(outil);
		this.verifier = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ToolsValidate.gif"));
		this.outil.add(this.verifier);
		this.exporter = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ark_extract.png"));
		this.outil.add(this.exporter);
		this.generer = new JMenuItem(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ToolsGenerate.gif"));
		this.outil.add(this.generer);
		 
		 //Aide
		 this.aide = new JMenu();
		 this.barre_menu.add(Box.createHorizontalGlue());
		 this.barre_menu.add(this.aide);
		 this.propos = new JMenuItem();
		 this.aide.add(this.propos);
	}
	
	/**
	 * M�thode permettant d'ajouter des raccourcis � certains items du menu
	 */
	private void ajouterRaccourcis()
	{
		this.nouveau_referentiel.setAccelerator((KeyStroke.getKeyStroke(KeyEvent.VK_N, InputEvent.CTRL_MASK)));
		this.ouvrir_referentiel.setAccelerator((KeyStroke.getKeyStroke(KeyEvent.VK_O, InputEvent.CTRL_MASK)));
		this.fermer.setAccelerator((KeyStroke.getKeyStroke(KeyEvent.VK_F, InputEvent.CTRL_MASK)));
		this.sauver.setAccelerator((KeyStroke.getKeyStroke(KeyEvent.VK_S, InputEvent.CTRL_MASK)));
		this.generer.setAccelerator((KeyStroke.getKeyStroke(KeyEvent.VK_G, InputEvent.CTRL_MASK)));
		this.propos.setAccelerator((KeyStroke.getKeyStroke(KeyEvent.VK_H, InputEvent.CTRL_MASK)));
	}
	
	
	/**
	 * M�thode permettant de cr�er la barre d'outil contenant des boutons raccourcis
	 */
	private void creerBarreOutil()
	{
		// cr�er un barre d'outil vide
		this.barre_outil = new JToolBar();
		// cr�er et rajouter les boutons � la barre
		this.bnouveau_proc = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileNew.gif"));
		this.barre_outil.add(this.bnouveau_proc);
		this.bnouveau_referentiel = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "folder_new.png"));
		this.barre_outil.add(this.bnouveau_referentiel);
		this.bouvrir_referentiel = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileOpen.gif"));
		this.barre_outil.add(this.bouvrir_referentiel);
		this.bouvrir_proc = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "fileopen.png"));
		this.barre_outil.add(this.bouvrir_proc);
		this.bsauver = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FileSave.gif"));
		this.barre_outil.add(this.bsauver);
		this.bimprimer = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "FilePrint.gif"));
		this.barre_outil.add(this.bimprimer);
		this.bfermer = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "fileclose.png"));
		this.barre_outil.add(this.bfermer);
		this.barre_outil.addSeparator();
		this.barre_outil.addSeparator();
		
		this.bverifier = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ToolsValidate.gif"));
		this.barre_outil.add(this.bverifier);
		this.bexporter = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ark_extract.png"));
		this.barre_outil.add(this.bexporter);
		this.bgenerer = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "ToolsGenerate.gif"));
		this.barre_outil.add(this.bgenerer);		
		this.barre_outil.addSeparator();
		this.barre_outil.addSeparator();
		
		this.bzoomin = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "zoomin.png"));
		this.barre_outil.add(this.bzoomin);
		this.bzoomout = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "zoomout.png"));
		this.barre_outil.add(this.bzoomout);
		
		this.barre_outil.addSeparator();
		this.barre_outil.addSeparator();
		this.bpreferences = new JButton(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "configure.png"));
		this.barre_outil.add(this.bpreferences);
			
		this.barre_outil.setFloatable(false);
	}
	
	/**
	 * M�thode permettant de positionner correctement tous les �l�ments cr��s
	 */
	private void positionnerElements()
	{
		// cr�er le panneau contenant le diagramme
		this.panneauGenerique = new JPanel();
		this.panneauVerif = new PanneauVerification(this.panneauGenerique);
		// division de la fenetre principale
		this.frame_split = new JSplitPane();
		this.frame_split.setOneTouchExpandable(true);
		this.frame_split.setDividerLocation(220);
		// cr�ation d'onglets
		this.tabbedPane = new JTabbedPane();
		this.tabbedPane.setTabPlacement(SwingConstants.BOTTOM);
		this.tabbedPane.addTab(Application.getApplication().getTraduction("Processus"), IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "TreeExternalDescription.gif"), this.vueDPArbre , Application.getApplication().getTraduction("Processus_courant"));
		this.tabbedPane.addTab(Application.getApplication().getTraduction("Referentiel"), IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "edittrash.png"), this.vueRefArbre , Application.getApplication().getTraduction("Referentiel"));

		// positionner les onglets et le panneau g�n�rique
		this.frame_split.add(new JScrollPane(this.tabbedPane), JSplitPane.LEFT);
		this.frame_split.add(this.panneauVerif, JSplitPane.RIGHT);
				
	    //this.frame_split.add(this.panneauGenerique, JSplitPane.RIGHT);
		// mettre en place la barre d'outils
		this.getContentPane().add(this.barre_outil, BorderLayout.NORTH);
		this.getContentPane().add(this.frame_split, BorderLayout.CENTER);
	}
	
	/**
	 * M�thode permettant d'�couter les �v�nements sur les menus, boutons etc... pour pouvoir
	 * les prendre en compte
	 */
	private void ajouterEcouteurs()
	{
		this.nouveau_proc.addActionListener(this) ;
		this.nouveau_referentiel.addActionListener(this) ;
		this.ouvrir_referentiel.addActionListener(this) ;
		this.ouvrir_proc.addActionListener(this) ;
		this.sauver.addActionListener(this) ;
		this.fermer.addActionListener(this) ;
		this.quitter.addActionListener(this) ; 

		this.zoomin.addActionListener(this);
		this.zoomout.addActionListener(this);
		this.propriete.addActionListener(this) ;
		
		this.verifier.addActionListener(this) ;
		this.exporter.addActionListener(this) ;
		this.generer.addActionListener(this) ;

		this.bnouveau_proc.addActionListener(this) ;
		this.bnouveau_referentiel.addActionListener(this) ;
		this.bouvrir_referentiel.addActionListener(this) ;
		this.bouvrir_proc.addActionListener(this) ;
		this.bsauver.addActionListener(this) ;
		this.bimprimer.addActionListener(this);
		this.bfermer.addActionListener(this) ;
		this.bexporter.addActionListener(this) ;
		this.bverifier.addActionListener(this) ;
		this.bgenerer.addActionListener(this) ;
		this.bzoomin.addActionListener(this) ;
		this.bzoomout.addActionListener(this) ;
		this.bpreferences.addActionListener(this) ;
		this.propos.addActionListener(this) ;
	}
	

	 //-----------------------------------------------------------------------------//
	 // Gestion des vues															//
	 //-----------------------------------------------------------------------------//
	  
	/**
	 * Une fois la vue cr��e (l'arbre) on l'affiche sur la partie gauche de la fenetre principale
	 * @param vue, arbre � afficher sur la partie gauche
	 */
	public void enregistrerVueArbre(VueDPArbre vue)
	{
		// garder un lien vers l'arbre
		this.vueDPArbre = vue ;
		// rafraichir l'arbre dans la fenetre principale
		this.tabbedPane.setComponentAt(FenetrePrincipale.INDEXTABPROCESSUS, this.vueDPArbre);
	}
	
	/**
	 * R�cup�rer la vue arbre
	 * @return arbre de la d�finition de processus courante
	 */
	public VueDPArbre getVueDPArbre()
	{
		return this.vueDPArbre ;
	}

	
	public void setReferentielArbre(Referentiel ref)
	{
		this.vueRefArbre = new VueReferentielArbre(ref);
		this.tabbedPane.setComponentAt(FenetrePrincipale.INDEXTABREFERENTIEL, this.vueRefArbre);
	}
	
	/**
	 * Affiche sur la partie de droite le panneau correspondant � l'action courante
	 * par exemple affiche le panneau correspondant � l'�dition du processus
	 * @param p, panneau � afficher sur la droite de la fenetre
	 */
	public void setPanneauGenerique (JPanel p)
	{
		// garder un lien vers le panneau � afficher
		this.panneauGenerique = p;
		this.panneauVerif = new PanneauVerification(this.panneauGenerique);
		// met en place le panneau
		this.frame_split.setRightComponent(this.panneauVerif);
		// remet la division de la fen�tre comme au d�part
		this.frame_split.setDividerLocation(220);
	}
	
	//------------------------------------------------------------------------------//
	// Gestion des �v�nements														//
	//------------------------------------------------------------------------------//
	
	/**
	 *  Met � jour les labels de la fenetre principale lors d'un changement de langue
	 */
	 public void rafraichirLangue()
	 {
	 	// rafraichit tous les menus et sous menus
		this.fichier.setText( Application.getApplication().getTraduction("Fichier"));
		this.nouveau_proc.setText( Application.getApplication().getTraduction("Nouveau")) ;
		this.nouveau_referentiel.setText( Application.getApplication().getTraduction("Nouveau_referentiel")) ;
		this.ouvrir_proc.setText( Application.getApplication().getTraduction("Ouvrir_proc")) ;
		this.ouvrir_referentiel.setText( Application.getApplication().getTraduction("Ouvrir_ref")) ;
		this.sauver.setText( Application.getApplication().getTraduction("Sauver")) ;
		this.fermer.setText( Application.getApplication().getTraduction("Fermer")) ;
		this.imprimer.setText(Application.getApplication().getTraduction("Imprimer"));
		this.quitter.setText( Application.getApplication().getTraduction("Quitter")) ;
		this.affichage.setText(Application.getApplication().getTraduction("Affichage"));
		this.zoomin.setText(Application.getApplication().getTraduction("Agrandir"));
		this.zoomout.setText(Application.getApplication().getTraduction("Reduire"));
		this.propriete.setText( Application.getApplication().getTraduction("Preferences")) ;
		this.outil.setText( Application.getApplication().getTraduction("Outils"));
		this.verifier.setText( Application.getApplication().getTraduction("Valider_le_projet"));
		this.exporter.setText( Application.getApplication().getTraduction("Exporter"));
		this.generer.setText( Application.getApplication().getTraduction("Generer_le_site_Web")) ;
		this.aide.setText( Application.getApplication().getTraduction("Aide")) ;
		this.propos.setText( Application.getApplication().getTraduction("Propos")) ;
				
		//raffraichit les onglets du JTabbedPane
		//this.tabbedPane.setTitleAt(FenetrePrincipale.INDEXTABPROCESSUS,Application.getApplication().getTraduction("Processus"));
		this.tabbedPane.setToolTipTextAt(FenetrePrincipale.INDEXTABPROCESSUS,Application.getApplication().getTraduction("Processus_courant"));
		this.tabbedPane.setToolTipTextAt(FenetrePrincipale.INDEXTABREFERENTIEL,Application.getApplication().getTraduction("Referentiel"));
	 
	 	// rafraichit tous les tooltip des boutons
	 	this.bnouveau_proc.setToolTipText(Application.getApplication().getTraduction("Nouveau"));
		this.bouvrir_proc.setToolTipText(Application.getApplication().getTraduction("Ouvrir_proc"));
	 	this.bouvrir_referentiel.setToolTipText(Application.getApplication().getTraduction("Ouvrir_ref"));
		this.bnouveau_referentiel.setToolTipText(Application.getApplication().getTraduction("Nouveau_referentiel"));
		this.bsauver.setToolTipText(Application.getApplication().getTraduction("Sauver"));
		this.bimprimer.setToolTipText(Application.getApplication().getTraduction("Imprimer"));
		this.bfermer.setToolTipText(Application.getApplication().getTraduction("Fermer"));
		this.bgenerer.setToolTipText(Application.getApplication().getTraduction("Generer_le_site_Web"));
		this.bexporter.setToolTipText( Application.getApplication().getTraduction("Exporter"));
		this.bzoomin.setToolTipText( Application.getApplication().getTraduction("Agrandir"));
		this.bzoomout.setToolTipText( Application.getApplication().getTraduction("Reduire"));
		this.bpreferences.setToolTipText(Application.getApplication().getTraduction("Preferences"));
		this.bverifier.setToolTipText(Application.getApplication().getTraduction("Valider_le_projet"));
		
		if (Application.getApplication().getProjet() != null )
		{
			Application.getApplication().getProjet().rafraichirLangue();
		}
		
		this.panneauVerif.rafraichirLangue();
	 }
	
	public void majEtat()
	{
		Projet projet = Application.getApplication().getProjet();
		boolean projetOuvert = (projet != null) ;
		
		Referentiel ref = Application.getApplication().getReferentiel();
		boolean refOuvert = (ref != null);
		
		this.bsauver.setEnabled(projetOuvert);
		this.sauver.setEnabled(projetOuvert);
		if (projet != null)
		{
			this.bsauver.setEnabled(projet.estModifie());
			this.sauver.setEnabled(projet.estModifie());
		}
		
		this.zoomin.setEnabled(projetOuvert);
		this.zoomout.setEnabled(projetOuvert);
		this.bzoomin.setEnabled(projetOuvert);
		this.bzoomout.setEnabled(projetOuvert);
		this.bimprimer.setEnabled(projetOuvert);
		this.imprimer.setEnabled(projetOuvert);
		this.fermer.setEnabled(projetOuvert);
		this.bfermer.setEnabled(projetOuvert);
		this.bgenerer.setEnabled(projetOuvert);
		this.generer.setEnabled(projetOuvert);
		this.bverifier.setEnabled(projetOuvert);
		this.bexporter.setEnabled(projetOuvert);
		this.verifier.setEnabled(projetOuvert);
		this.exporter.setEnabled(projetOuvert);
		this.ouvrir_proc.setEnabled(refOuvert);
		this.bouvrir_proc.setEnabled(refOuvert);
		this.nouveau_proc.setEnabled(refOuvert);
		this.bnouveau_proc.setEnabled(refOuvert);
	}
	 
	 //	gestion des �v�nements (boutons et menus)
	 public void actionPerformed (ActionEvent e)
	 {
	 		// r�cup�rer l'objet source de l'�v�nement re�u
			 Object source = e.getSource();
			 // selon cet objet, r�agir en cons�quence
			 if (source == quitter)
			 {
				System.exit(0);
			 }
			 // on peut lancer une m�me action � partir de sources diff�rentes
			 else if ((source == this.nouveau_proc )||(source == this.bnouveau_proc))
			 {
					// cr�ation d'un nouveau projet
					 CNouveauProjet c = new CNouveauProjet() ;
					 if (c.executer()== false)
				     {
				     	new CFermerProjet().executer();
				     }
					 this.majEtat();
			 }
			 else if ((source == this.ouvrir_proc )||(source == this.bouvrir_proc))
			 {
					// afficher la liste des DP disponibles dans le r�f�rentiel
			 		COuvrirDP c = new COuvrirDP() ;
					c.executer() ;
					this.majEtat();

			 }
			 else if ((source == this.nouveau_referentiel )||(source == this.bnouveau_referentiel))
			 {
					// cr�ation d'un nouveau r�f�rentiel
			 		 CCreerReferentiel c = new CCreerReferentiel() ;
					 if (c.executer())
					 {
					 	Application.getApplication().getFenetrePrincipale().setTitle(
								Application.getApplication().getConfigPropriete("titre")
								+ " " + Application.getApplication().getReferentiel().getNomReferentiel());
					 }
					 this.majEtat();
			 }
			 
			 // fermer
			 else if ((source == this.fermer)||(source == this.bfermer))
			 {
			 		// fermeture du projet ouvert
			 		CFermerProjet c = new CFermerProjet(true);
			 		if (c.executer())
			 		{
			 			Application.getApplication().getFenetrePrincipale().setTitle(
								Application.getApplication().getConfigPropriete("titre")
								+ " " + Application.getApplication().getReferentiel().getNomReferentiel());
			 		}
			 		this.majEtat();	
			 }
			 else if ((source == this.ouvrir_referentiel )||(source == this.bouvrir_referentiel))
			 {
					 COuvrirReferentiel c = new COuvrirReferentiel();
					 if (c.executer())
					 {
					 	Application.getApplication().getFenetrePrincipale().setTitle(
								Application.getApplication().getConfigPropriete("titre")
								+ " " + Application.getApplication().getReferentiel().getNomReferentiel());
					 }
				 	 this.majEtat();
			 }
			 else if ((source == this.sauver )||(source == this.bsauver))
			 {
			 	Referentiel ref = Application.getApplication().getReferentiel() ;
				ref.sauverDefProc (Application.getApplication().getProjet().getDefProc()) ;
				this.majEtat();
			 }	
			 else  if ((source == this.imprimer )||(source == this.bimprimer))
			 {
				   CImprimerDiagramme c = new CImprimerDiagramme(Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe());
				   c.executer();
			 }
			 //Bouton V�rification
			 else if ((source == this.bverifier)||(source == this.verifier))
			 {
				 //new FenetreVerification() ;
				 CVerification c = new CVerification();
				 c.executer();
			 }
			 // exporter
			else if  ((source == this.bexporter)||(source == this.exporter))
			{
				CExporterProcessus c = new CExporterProcessus();
				c.executer();
			}
			// zoom in
			else if  ((source == this.bzoomin) || (source == this.zoomin))
			{
				CZoomer c = new CZoomer(1.25);
				if (c.executer())
				{
					Application.getApplication().getProjet().setModified(true);
				}
			}
			// zoom out
			else if  ((source == this.bzoomout) || (source == this.zoomout))
			{
				CZoomer c = new CZoomer(0.8);
				if (c.executer())
				{
					Application.getApplication().getProjet().setModified(true);
				}
			}

			 // g�n�ration bo�te dialogue
			 else if ((source == this.generer ))
			 {
			 	JDialog dialogueGen = new FenetrePreference(this, FenetrePreference.TYPE_GENERATION);		 	
			 }
			 // g�n�ration rapide
			 else if ((source == this.bgenerer))
			 {
				CGenererSite c = new CGenererSite(Application.getApplication().getProjet().getDefProc()) ;
				if (c.executer())
				{
					 JOptionPane.showMessageDialog(this,Application.getApplication().getTraduction("Generation_ok"),
					 				Application.getApplication().getTraduction("Generation_site_titre"),
									JOptionPane.INFORMATION_MESSAGE);
				}
			 }
			 
			else if ((source == this.propriete ) || source == this.bpreferences)
			{
				JDialog dialogueProp = new FenetrePreference(this, FenetrePreference.TYPE_PREFERENCES);
			}
			else if (source == this.propos)
			{
				new FenetrePropos(this);
			}
	  }
	  
	  public void afficherPanneauVerification(Vector entree, Vector sortie, Vector composant)
	  {
	  	this.panneauVerif.showErrorArea();
	  	this.panneauVerif.setListeErreurs(entree, sortie, composant);
	  }
	  
	  public PanneauVerification getPanneauVerif()
	  {
	  	return this.panneauVerif;
	  }
	 
	private class FenetreListener extends WindowAdapter
	{
		/**
		 * 
		 */
		public void windowClosing(WindowEvent e)
		{
			if ( Application.getApplication().getProjet() != null)
			{
				if (Application.getApplication().getProjet().estModifie())
				{
					  int choice = JOptionPane.showConfirmDialog(
				  			 Application.getApplication().getFenetrePrincipale(),
							 Application.getApplication().getTraduction("BD_SAUV_AVAT_FERMER"),
							 Application.getApplication().getTraduction("Confirmation"),
							 JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE);
		
					  // si "oui" l'utilisateur accepte, on demande la sauvegarde
					  if( choice == JOptionPane.YES_OPTION )
					  {
					  		//sauver la dp courante
					  		Referentiel ref = Application.getApplication().getReferentiel() ;
					  		ref.sauverDefProc (Application.getApplication().getProjet().getDefProc()) ;
					  		Application.getApplication().getProjet().setModified(false);
					  }
					  if ( choice == JOptionPane.CANCEL_OPTION)
					  {
					  	return;
					  }
				}
				CFermerProjet c = new CFermerProjet();
				c.executer();
			}
			System.exit(0);
		}
	}
}
