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

package iepp.ui.igeneration;

import javax.swing.*;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import java.awt.*;
import java.awt.event.*;
import java.util.*;
import iepp.ui.*;
import java.io.*;
import iepp.Application;
import iepp.application.ageneration.GenerationManager;
import iepp.application.areferentiel.ElementReferentiel;
import iepp.application.areferentiel.Referentiel;
import iepp.domaine.IdObjetModele;
import iepp.domaine.PaquetagePresentation;
import util.*;

/**
 * 
 */
public class DialogueGenererSite extends JDialog
{
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	private int resultat;	
	
	private JPanel panel1 = new JPanel();
	private JPanel panelRepSite = new JPanel();
	private JPanel panelArbre = new JPanel();
	private JPanel panelBoutons = new JPanel();
	private JPanel panelStyle = new JPanel();
	private JPanel panelOptionsGen = new JPanel();
	private JPanel panelOptionsComp = new JPanel();
	private JPanel panelOptionsDef = new JPanel();
	
	private JScrollPane scrollPaneRef = new JScrollPane();
	private JScrollPane scrollPaneArbre = new JScrollPane();
	
	private JTextField ES_repSite = new JTextField();
	private JLabel STC_repSite = new JLabel();
	private JLabel STC_style = new JLabel();
	private JLabel STC_couleur_surlign = new JLabel();
	
	private JComboBox LD_styles = new JComboBox();
	
	private JRadioButton BR_comp_tableau;
	private JRadioButton BR_comp_diag_flots;
	private JRadioButton BR_comp_diag_resp;
	
	private JRadioButton BR_def_tableau;
	private JRadioButton BR_def_activites;
	private JRadioButton BR_def_produits;
	
	private ButtonGroup GR_comp = new ButtonGroup();
	private ButtonGroup GR_def = new ButtonGroup();
	
	
	private JList LS_referentiel = new JList();
	private JList LS_arbre = new JList();
	
	private JButton BP_ajouter = new JButton();
	private JButton BP_retirer = new JButton();
	private JButton BP_monter = new JButton();
	private JButton BP_descendre = new JButton();
	private JButton BP_parcourir = new JButton();
	private JButton BP_ok = new JButton();
	private JButton BP_annuler = new JButton();
	private JButton BP_couleur_surlign = new JButton();
	
	private GridBagConstraints constraintsPanel1 = new GridBagConstraints();
	private GridBagConstraints constraintsFtp = new GridBagConstraints();
	private GridBagConstraints constraintsArbre = new GridBagConstraints();
	private GridBagConstraints constraintsStyle = new GridBagConstraints(); 
	
	private GridBagLayout gridBagLayoutPanel1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutFtp = new GridBagLayout();
	private GridBagLayout gridBagLayoutArbre = new GridBagLayout();
	private GridBagLayout gridBagLayoutStyle = new GridBagLayout();
	
	private ComposantsListModel ComposantsListModel = new ComposantsListModel();
	private PPListModel PPListModel = new PPListModel();
	
	
	public DialogueGenererSite(FenetrePrincipale parent)
	{
		super(parent, Application.getApplication().getTraduction("titre_generer_site"), true);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				annuler(e);
			}
		});
	
		this.init();
		this.initPanelRepSite();
		this.initPanelStyle();
		this.initpanelOptionsComp();
		this.initpanelOptionsDef();
		this.initPanelOptionsGen();
		this.initPanelBoutons();
		this.initPanelArbre();
		this.initPanelBackground();
		
		this.getContentPane().add(this.panel1, BorderLayout.CENTER);
		this.getContentPane().add(this.panelBoutons, BorderLayout.SOUTH);
		
		this.pack();
		
		Rectangle bounds = parent.getBounds();
		this.setLocation(bounds.x+ (int) bounds.width / 2 - this.getWidth() / 2,
							 bounds.y + bounds.height / 2 - this.getHeight() / 2);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	
	public int getResultat()
	{
		return this.resultat;
	}
	
	
	public String getRepertoireGeneration()
	{
		return this.ES_repSite.getText();
	}
	
	
	public String getServeurFtp()
	{
		return ES_repSite.getText();
	}
	
	public String getStyle()
	{
		if (LD_styles.getSelectedItem() != null)
			return ((File)LD_styles.getSelectedItem()).getName();
		else
			return ("");
	}
	
	private void init()
	{
		this.ES_repSite.setColumns(30);
		this.ES_repSite.setText(GenerationManager.getInstance().getCheminGeneration());
		
		this.STC_repSite.setLabelFor(STC_repSite);
		this.STC_repSite.setText(Application.getApplication().getTraduction("rep_site"));
	
		this.STC_style.setText(Application.getApplication().getTraduction("style_pages"));
		this.BP_couleur_surlign.setBackground(GenerationManager.getInstance().getCouleur());
		
		this.LD_styles.setModel(new ComboStylesModel());
		this.LD_styles.setRenderer(new FileRenderer());
		if(this.LD_styles.getItemCount() > 0)
		{
			boolean trouvé = false;
			int index = 0;
			for ( index = 0 ; (index  < LD_styles.getItemCount()) && (trouvé == false); index ++)
			{
				File temp = new File(((File)LD_styles.getItemAt(index)).getName());
				trouvé = temp.getName().substring(0, temp.getName().length()-4).equals(GenerationManager.getInstance().getFeuilleCss());
				//System.out.println("Trouvé = "+ trouvé + index + temp.getName().substring(0, temp.getName().length()-4) + " " + Application.getApplication().getConfigPropriete("style_pages"));
			}
			//System.out.println("trouvé ? = " + trouvé +" "+index);
			if ( (trouvé) && (index != 0))
				this.LD_styles.setSelectedIndex(index-1);
			else
				this.LD_styles.setSelectedIndex(0);
		}		
			
		this.STC_couleur_surlign.setText(Application.getApplication().getTraduction("couleur_surlign"));
			
		this.BR_comp_tableau = new JRadioButton(Application.getApplication().getTraduction("comp_tableau"), 
													GenerationManager.getInstance().getTypeComposant() == GenerationManager.TYPE_COMP_TABLEAU);
		this.BR_comp_diag_flots = new JRadioButton(Application.getApplication().getTraduction("comp_diag_flots"),
													GenerationManager.getInstance().getTypeComposant() == GenerationManager.TYPE_COMP_FLOT);
		this.BR_comp_diag_resp = new JRadioButton(Application.getApplication().getTraduction("comp_diag_resp"),
													GenerationManager.getInstance().getTypeComposant() == GenerationManager.TYPE_COMP_RESP);
		
		this.BR_def_tableau = new JRadioButton(Application.getApplication().getTraduction("def_tableau"), 
													GenerationManager.getInstance().getTypeDefTravail() == GenerationManager.TYPE_DEFP_TABLEAU);
		this.BR_def_activites = new JRadioButton(Application.getApplication().getTraduction("def_activites"),
													GenerationManager.getInstance().getTypeDefTravail() == GenerationManager.TYPE_DEFP_ACT);
		this.BR_def_produits = new JRadioButton(Application.getApplication().getTraduction("def_produits"),
													GenerationManager.getInstance().getTypeDefTravail() == GenerationManager.TYPE_DEFP_FLOT);
		
		this.GR_comp.add(BR_comp_tableau);
		this.GR_comp.add(BR_comp_diag_flots);
		this.GR_comp.add(BR_comp_diag_resp);
		
		this.GR_def.add(BR_def_tableau);
		this.GR_def.add(BR_def_activites);
		this.GR_def.add(BR_def_produits);
		
		/*
		this.LS_referentiel.addElement("ref 1");
		
		this.LS_arbre.addElement("comp 1");*/
		
		/*String[] data = {"one", "two", "three", "four", "five", "six", "seven", "height"};*/
 		/*this.LS_referentiel.setListData(data);*/
		this.LS_referentiel.setVisibleRowCount(5);
		this.LS_referentiel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.LS_referentiel.setModel(this.PPListModel);
		
		/*this.LS_arbre.setListData(data);*/
		this.LS_arbre.setVisibleRowCount(5);
		this.LS_arbre.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.LS_arbre.setModel(this.ComposantsListModel);
		this.LS_arbre.addListSelectionListener(new ListSelectionListener()
		{	
			public void valueChanged(ListSelectionEvent e) 
			{
				//System.out.println("le listener est bien ajouté");
				int indexSelect = LS_arbre.getSelectedIndex();
				if((indexSelect != -1) && (indexSelect < ComposantsListModel.getSize()))
				{	
					//System.out.println("bp enable");
					BP_retirer.setEnabled(true);
					
					Object a_verifier = (Object)ComposantsListModel.getListe().elementAt(indexSelect);
					if (a_verifier instanceof IdObjetModele)
					{
						
						//System.out.println("bp disable");
						BP_retirer.setEnabled(false);
					
					}
					
				}
				LS_arbre.setSelectedIndex(indexSelect);
			}
		});
		
		this.scrollPaneRef.setViewportView(this.LS_referentiel);
		this.scrollPaneArbre.setViewportView(this.LS_arbre);
		
		this.BP_ajouter.setText(Application.getApplication().getTraduction("ajouter_referentiel"));
		this.BP_ajouter.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheDroite.jpg"));
		this.BP_ajouter.setHorizontalTextPosition(SwingConstants.LEFT);
		this.BP_ajouter.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ajouter(e);
			}
		});
		
		this.BP_retirer.setText(Application.getApplication().getTraduction("retirer_referentiel"));
		this.BP_retirer.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheGauche.jpg"));
		this.BP_retirer.setHorizontalTextPosition(SwingConstants.RIGHT);
		this.BP_retirer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				retirer(e);
			}
		});

		this.BP_monter.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheHaut.jpg"));
		this.BP_monter.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				monter(e);
			}
		});
		
		this.BP_descendre.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheBas.jpg"));
		this.BP_descendre.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				descendre(e);
			}
		});
		
		this.BP_couleur_surlign.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				choisirCouleur(e);
			}
		});
		
		//this.BP_ok.setMnemonic();
		this.BP_ok.setText(Application.getApplication().getTraduction("generer"));
		this.BP_ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ok(e);
			}
		});
		
		//		this.BP_annuler.setMnemonic();
		this.BP_annuler.setText(Application.getApplication().getTraduction("Annuler"));
		this.BP_annuler.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				annuler(e);
			}
		});

		//		this.BP_parcourir.setMnemonic();
		this.BP_parcourir.setText(Application.getApplication().getTraduction("parcourir"));
		this.BP_parcourir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				parcourir(e);
			}
		});
	}
		
	private void initPanelRepSite()
	{
		this.panelRepSite.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("repertoire_cadre")));
		this.panelRepSite.add(STC_repSite, null);
		this.panelRepSite.add(ES_repSite, null);
		this.panelRepSite.add(BP_parcourir, null);
	}
	
	private void initPanelStyle()
	{
		this.panelStyle.setLayout(this.gridBagLayoutStyle);
		this.panelStyle.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("style_cadre")));
		
		this.constraintsStyle.fill = GridBagConstraints.VERTICAL;

		this.gridBagLayoutStyle.setConstraints(this.STC_style, this.constraintsStyle);
		this.panelStyle.add(this.STC_style);
		
		this.constraintsStyle.insets = new Insets(0, 0, 0, 0);
		this.gridBagLayoutStyle.setConstraints(this.LD_styles, this.constraintsStyle);
		this.panelStyle.add(this.LD_styles);
		

		this.constraintsStyle.insets = new Insets(0, 30, 0, 0);
		this.gridBagLayoutStyle.setConstraints(this.STC_couleur_surlign, this.constraintsStyle);
		this.panelStyle.add(this.STC_couleur_surlign);
		
		this.constraintsStyle.insets = new Insets(0, 0, 0, 0);
		this.gridBagLayoutStyle.setConstraints(this.BP_couleur_surlign, this.constraintsStyle);
		this.panelStyle.add(this.BP_couleur_surlign);
	}
	
	private void initpanelOptionsComp()
	{
		this.panelOptionsComp.setLayout(new GridLayout(3,1));
		this.panelOptionsComp.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("options_comp")));
		this.panelOptionsComp.add(this.BR_comp_tableau);
		this.panelOptionsComp.add(this.BR_comp_diag_flots);
		this.panelOptionsComp.add(this.BR_comp_diag_resp);
	}
	
	private void initpanelOptionsDef()
	{
		this.panelOptionsDef.setLayout(new GridLayout(3,1));
		this.panelOptionsDef.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("options_def")));
		this.panelOptionsDef.add(this.BR_def_tableau);
		this.panelOptionsDef.add(this.BR_def_activites);
		this.panelOptionsDef.add(this.BR_def_produits);
	}
	
	private void initPanelOptionsGen()
	{
		this.panelOptionsGen.setLayout(new GridLayout(1,2));
		this.panelOptionsGen.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("options_gen_cadre")));
		this.panelOptionsGen.add(this.panelOptionsComp);
		this.panelOptionsGen.add(this.panelOptionsDef);
	}
	
	private void initPanelArbre()
	{
		this.panelArbre.setLayout(this.gridBagLayoutArbre);
		this.panelArbre.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("compo_menu_cadre")));
		
		this.constraintsArbre.fill = GridBagConstraints.BOTH;
		this.constraintsArbre.anchor = GridBagConstraints.CENTER;
		//this.constraintsArbre.gridwidth = 1;
		this.constraintsArbre.gridheight = 2;
		this.constraintsArbre.weightx = 1;
		this.constraintsArbre.weighty = 1.0;
		this.gridBagLayoutArbre.setConstraints(this.scrollPaneRef, this.constraintsArbre);
		this.panelArbre.add(this.scrollPaneRef);
		
		this.constraintsArbre.fill = GridBagConstraints.HORIZONTAL;
		this.constraintsArbre.gridheight = 1;
		this.constraintsArbre.weightx = 0.0;
		this.constraintsArbre.weighty = 0.0;
		this.constraintsArbre.anchor = GridBagConstraints.NORTH;
		this.gridBagLayoutArbre.setConstraints(this.BP_ajouter, this.constraintsArbre);
		this.panelArbre.add(this.BP_ajouter);
		
		this.constraintsArbre.gridx = 1; /* pour que les BP "ajouter" et "retirer" soient dans la même colonne */
		this.constraintsArbre.anchor = GridBagConstraints.SOUTH;
		this.gridBagLayoutArbre.setConstraints(this.BP_retirer, this.constraintsArbre);
		this.panelArbre.add(this.BP_retirer);
		
		this.constraintsArbre.fill = GridBagConstraints.HORIZONTAL;
		this.constraintsArbre.gridx = -1; /* on remet le gridx par défaut pour la suite */ 
		this.constraintsArbre.gridheight = 2;
		this.constraintsArbre.weightx = 1;
		this.constraintsArbre.weighty = 1.0;
		this.constraintsArbre.anchor = GridBagConstraints.CENTER;
		this.gridBagLayoutArbre.setConstraints(this.scrollPaneArbre, this.constraintsArbre);
		this.panelArbre.add(this.scrollPaneArbre);
		
		this.constraintsArbre.fill = GridBagConstraints.NONE;
		this.constraintsArbre.weightx = 0;
		this.constraintsArbre.weighty = 0.0;
		this.constraintsArbre.gridheight = 1;
		this.constraintsArbre.gridwidth = GridBagConstraints.REMAINDER;
		this.constraintsArbre.anchor = GridBagConstraints.NORTH;
		this.gridBagLayoutArbre.setConstraints(this.BP_monter, this.constraintsArbre);
		this.panelArbre.add(this.BP_monter);
		
		this.constraintsArbre.anchor = GridBagConstraints.SOUTH;
		this.gridBagLayoutArbre.setConstraints(this.BP_descendre, this.constraintsArbre);
		this.panelArbre.add(this.BP_descendre);
		
	}
	
	private void initPanelBoutons()
	{
		this.panelBoutons.add(this.BP_ok, null);
		this.panelBoutons.add(this.BP_annuler, null);
	}

	private void initPanelBackground()
	{
		this.panel1.setLayout(this.gridBagLayoutPanel1);
		this.constraintsPanel1.fill = GridBagConstraints.HORIZONTAL;
		this.constraintsPanel1.anchor = GridBagConstraints.CENTER;

		this.constraintsPanel1.insets = new Insets(10, 10, 0, 10);
		this.constraintsPanel1.weightx = 1.0;
		this.constraintsPanel1.gridwidth = GridBagConstraints.REMAINDER;
		this.gridBagLayoutPanel1.setConstraints(this.panelRepSite, this.constraintsPanel1);
		this.panel1.add(this.panelRepSite);


		this.gridBagLayoutPanel1.setConstraints(this.panelStyle, this.constraintsPanel1);
		this.panel1.add(this.panelStyle);

		this.gridBagLayoutPanel1.setConstraints(this.panelOptionsGen, this.constraintsPanel1);
		this.panel1.add(this.panelOptionsGen);
		
		this.constraintsPanel1.insets = new Insets(10, 10, 20, 10);
		this.gridBagLayoutPanel1.setConstraints(this.panelArbre, this.constraintsPanel1);
		this.panel1.add(this.panelArbre);
	}


	private void annuler(AWTEvent e)
	{
		this.resultat = CANCEL_OPTION;
		this.dispose();
	}

	private void ok(ActionEvent e)
	{
		// on sauvegarde l'ordre de la liste dans le Generation Manager
		GenerationManager.getInstance().setListeAGenerer(ComposantsListModel.getListe());
		// on modifie le chemin de generation
		GenerationManager.getInstance().setCheminGeneration(this.ES_repSite.getText());
		Application.getApplication().getProjet().getDefProc().setRepertoireGeneration(this.ES_repSite.getText());
		//on modifie la couleur des éléments sélectionnés dans l'arbre
		GenerationManager.getInstance().setCouleurSurligne(this.BP_couleur_surlign.getBackground());
		
		File f1 = new File(LD_styles.getSelectedItem().toString());
		//Application.getApplication().setConfigPropriete("feuille_style", f1.getName().substring(0, f1.getName().length()-4));
		//System.out.println(f1.getName().substring(0, f1.getName().length()-4));
		GenerationManager.getInstance().setFeuilleCss(this.getStyle());
		
		
		
		if (this.BR_comp_tableau.isSelected())
			GenerationManager.getInstance().setTypeComposant(GenerationManager.TYPE_COMP_TABLEAU);
		else if (this.BR_comp_diag_resp.isSelected())
			GenerationManager.getInstance().setTypeComposant(GenerationManager.TYPE_COMP_RESP);
		else if (this.BR_comp_diag_flots.isSelected())
			GenerationManager.getInstance().setTypeComposant(GenerationManager.TYPE_COMP_FLOT);
		
		if (this.BR_def_tableau.isSelected())
			GenerationManager.getInstance().setTypeDefTravail(GenerationManager.TYPE_DEFP_TABLEAU);
		else if (this.BR_def_activites.isSelected())
			GenerationManager.getInstance().setTypeDefTravail(GenerationManager.TYPE_DEFP_ACT);
		else if (this.BR_def_produits.isSelected())
			GenerationManager.getInstance().setTypeDefTravail(GenerationManager.TYPE_DEFP_FLOT);
		
		/*
		System.out.println(" la liste  du Generation Manager est : ");
		for (int i = 0; i < GenerationManager.getInstance().getListeAGenerer().size();i++)
		{
			System.out.println(GenerationManager.getInstance().getListeAGenerer().elementAt(i).toString());
		}
		*/
		this.resultat = OK_OPTION;
		this.dispose();
	}
	
	private void parcourir(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser(this.ES_repSite.getText());
		fileChooser.setDialogTitle(Application.getApplication().getTraduction("titre_choix_rep"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fileChooser.showDialog(this, Application.getApplication().getTraduction("OK"));
		if(res == JFileChooser.APPROVE_OPTION)
			this.ES_repSite.setText(fileChooser.getSelectedFile().getAbsolutePath());
	}
	
	private void ajouter(ActionEvent e)
	{
		int indexSelect = this.LS_referentiel.getSelectedIndex();
		
		// TODO 7 = taille de la liste du ref
		if((indexSelect != -1) && (indexSelect < PPListModel.getSize()))
		{
			PPListModel listeRef = (PPListModel)LS_referentiel.getModel();
			Object a_inserer = listeRef.getListe().elementAt(indexSelect);
			
			Referentiel ref = Application.getApplication().getReferentiel() ;
			String nomPaqPre = a_inserer.toString();
			long id= ref.nomPresentationToId(nomPaqPre) ;
			PaquetagePresentation paq = ref.chargerPresentation(id);
			if (paq != null)
			{
				  ComposantsListModel listeComp = (ComposantsListModel)LS_arbre.getModel();
				  listeComp.ajouter(paq);
				  this.LS_referentiel.setSelectedIndex(indexSelect-1);
				  listeRef.enlever(indexSelect);
			}
		}
	}
	
	private void retirer(ActionEvent e)
	{
			int indexSelect = this.LS_arbre.getSelectedIndex();
		
			// TODO 7 = taille de la liste du ref
			if((indexSelect != -1) && (indexSelect < this.ComposantsListModel.getSize()))
			{
				PPListModel listeRef = (PPListModel)LS_referentiel.getModel();
				ComposantsListModel listeComp = (ComposantsListModel)LS_arbre.getModel();
				Object a_retirer = listeComp.getListe().elementAt(indexSelect);
				if (a_retirer instanceof PaquetagePresentation)
				{
					String cheminFichier = ((PaquetagePresentation)a_retirer).getNomFichier();
					a_retirer = (cheminFichier.substring(cheminFichier.lastIndexOf(File.separator) + 1, cheminFichier.lastIndexOf(".")));
				}
				listeRef.ajouter(a_retirer);
				this.LS_arbre.setSelectedIndex(indexSelect-1);
				listeComp.enlever(indexSelect);
			}
	}
	
	private void descendre(ActionEvent e)
	{
		int indexSelect = this.LS_arbre.getSelectedIndex();
		if((indexSelect != -1) && (indexSelect < this.ComposantsListModel.getSize()-1))
		{
			this.ComposantsListModel.descendre(indexSelect);
			this.LS_arbre.setSelectedIndex(indexSelect+1);
		}
	}
	
	private void monter(ActionEvent e)
	{
		int indexSelect = this.LS_arbre.getSelectedIndex();
		if((indexSelect != -1) && (indexSelect > 0))
		{
			this.ComposantsListModel.monter(indexSelect);
			this.LS_arbre.setSelectedIndex(indexSelect-1);
		}
	}
	
	private void choisirCouleur(ActionEvent e)
	{
		// récupère la couleur choisie dans la bd
		Color couleur_choisie = JColorChooser.showDialog(this,Application.getApplication().getConfigPropriete("choix_couleur"), GenerationManager.getInstance().getCouleur());
		// si l'utilisateur choisit annuler, la bd renvoie null, donc on vérifie le retour
		if (couleur_choisie != null)
		{
			this.BP_couleur_surlign.setBackground(couleur_choisie);
		}
	}
	
	
	private class ComboStylesModel extends DefaultComboBoxModel
	{
		private ArrayList fichiersStyles = new ArrayList();
		
		public ComboStylesModel()
		{
			File repStyles = new File(Application.getApplication().getConfigPropriete("styles"));
			File[] lesFichiersCss = repStyles.listFiles(new FileFilter()
			{	
				String fileExtension = Application.getApplication().getConfigPropriete("extensionFeuilleStyle").toString();
				public boolean accept(File fich)
				{	
					String nom = fich.getName();
					String extension = nom.substring((nom.lastIndexOf(".")+1));
					return extension.equals(this.fileExtension);
				}
			});
			for(int i = 0 ; i < lesFichiersCss.length ; i++)
			{
				fichiersStyles.add(lesFichiersCss[i]);
			}
		}
			
		public Object getElementAt(int index)
		{
			return (File)fichiersStyles.get(index);
		} 
		
		public int getSize()
		{
			return fichiersStyles.size();
		}
		
	}
	
	private class FileRenderer implements ListCellRenderer
	{
		JLabel label = new JLabel();
		public FileRenderer() {
		label.setOpaque(true);
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{	if(value != null)
			{
				File fichier = (File)value;
				label.setText(fichier.getName().substring(0, fichier.getName().length()-4));
			}
		else
			label.setText("");
		label.setBackground(isSelected ? Color.gray : Color.lightGray);
		label.setForeground(isSelected ? Color.white : Color.black);

		return label;
			
		}
	}


	private abstract class ListModel extends AbstractListModel
	{
		public Vector liste = new Vector();

		public Object getElementAt(int index)
		{
			return liste.elementAt(index);
		} 
		
		public int getSize()
		{
			return liste.size();
		}
			
		public void monter(int index)
		{
			Object buffer = getElementAt(index);
			liste.removeElementAt(index);
			liste.add(index-1, buffer);
			fireContentsChanged(this, index-1, index);
		}
			
		public void descendre(int index)
		{
			Object buffer = getElementAt(index);
			liste.removeElementAt(index);
			liste.add(index+1, buffer);
			fireContentsChanged(this, index, index+1);
		}
		
		public void ajouter(Object o)
		{
			liste.add(o);
			fireContentsChanged(this, 0, liste.size());
		}
		
		public void enlever(int i)
		{
			liste.removeElementAt(i);
			fireContentsChanged(this, 0 , liste.size());
		}
		
		public Vector getListe()
		{
			return(this.liste);
		}
	}
	
	private class ComposantsListModel extends ListModel
	{
		private Vector listeComp = new Vector();
		public ComposantsListModel()
		{
			liste = new Vector();
			for (int i = 0;i<Application.getApplication().getProjet().getDefProc().getListeComp().size();i++)
			{
				liste.add(Application.getApplication().getProjet().getDefProc().getListeComp().elementAt(i)) ;
			}
		}
		public void verifier_supprimer(ListSelectionEvent e,int i)
		{
			
			
		}
		
	}
	
	//---------------------------------------------
	// Paquetage de présentation, liste de gauche
	//----------------------------------------------
	private class PPListModel extends ListModel
	{
		private Vector listeComp = new Vector();
		public PPListModel()
		{
			liste = new Vector();
			Vector listePaquetage = Application.getApplication().getReferentiel().getListeNom(ElementReferentiel.PRESENTATION);
			for (int i = 0;i<listePaquetage.size();i++)
			{
				liste.add(listePaquetage.elementAt(i)) ;
			}
		}
	}
}
