/*
 * POG
 * Copyright (C) 2004 Team POG
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
 */


package POG.interfaceGraphique.fenetre;


import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.Toolkit;
import java.awt.event.ActionEvent;
import java.io.File;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Locale;
import java.util.Vector;

import javax.swing.AbstractAction;
import javax.swing.AbstractListModel;
import javax.swing.Box;
import javax.swing.DefaultListCellRenderer;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTabbedPane;
import javax.swing.JTextField;
import javax.swing.JViewport;
import javax.swing.ListSelectionModel;
import javax.swing.UIManager;
import javax.swing.WindowConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import POG.utile.MyMultiFileFilter;
import POG.utile.PogToolkit;
import POG.utile.propriete.Langues;
import POG.utile.propriete.Preferences;



public class FenetrePreferences

    extends FenetrePOG {

  private JButton boutonAjouterAss;
  private JButton boutonModifierAss;
  private JButton boutonSupprimerAss;

  private JButton boutonAjouterGui;
  private JButton boutonModifierGui;
  private JButton boutonSupprimerGui;

  private JButton boutonAjouterPro;
  private JButton boutonModifierPro;
  private JButton boutonSupprimerPro;


  private JList listeAssociations;
  private JList listeGuides;
  private JList listeProduits;
  private JPanel panneauAssociations;
  private JPanel panneauGuides;
	private JPanel panneauProduits;
  private JPanel panneauLookAndFeel;
  private JPanel panneauLangues;
  private JPanel panneauApplications;
  private JPanel panneauPresentation;
  private JScrollPane panneauListeAssociations;
  private JScrollPane panneauListeGuides;
  private JScrollPane panneauListeProduits;
  private AssociationsListModel modelAssociations ;
  private AssociationsListModel modelGuides;
  private AssociationsListModel modelProduits;
  private JComboBox lafComboBox;
  private JComboBox languesComboBox;
  private JTextField champApplicationApes ;
  private JTextField champPresChemIcones;
  private JTextField champPresChemBiblio;
  private JTextField champPresChemPre;
  private JTextField champPresChemPog;
  private JTextField champPresChemApes;
  
  private JTextField champIcoPack;
  private JTextField champIcoElem;
  
  
  private JCheckBox jcheckuse;
  private Preferences _prefs ;
//  private HashMap _copieAssoc ;
  private FenetrePreferences _this ;

  public FenetrePreferences(FenetrePrincipale fp, Preferences p) throws HeadlessException {
    super(fp);
    this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("preferences"));
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this._this = this ;
    this._prefs = p ;
//    this._copieAssoc = new HashMap(_prefs.getAssociations());

    // Initialisation du modele de la liste
    this.modelAssociations = new AssociationsListModel(_prefs.getAssociations()) ;
	this.modelGuides = new AssociationsListModel(_prefs.get_guide());
	this.modelProduits = new AssociationsListModel(_prefs.get_produit());

    try {
      jbInit();
      this.pack();
      this.setResizable(false);
      PogToolkit.centerWindow(this);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
	JTabbedPane lestab = new JTabbedPane();


// Associations

    panneauAssociations = new JPanel();
    panneauAssociations.setLayout(new BorderLayout());
    panneauAssociations.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("associations")));
    listeAssociations = new JList(this.modelAssociations);
    listeAssociations.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
    listeAssociations.setCellRenderer(new DefaultListCellRenderer()
      {
        public Component getListCellRendererComponent(JList list,
              Object value,
              int index,
              boolean isSelected,
              boolean cellHasFocus)
          {
            Vector tableau = (Vector) value ;
            return super.getListCellRendererComponent(list, tableau.get(0) + " = " + tableau.get(1), index, isSelected, cellHasFocus);
          }
      });
    panneauListeAssociations = new JScrollPane(listeAssociations);
    JViewport vp = panneauListeAssociations.getViewport();
    Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
    vp.setPreferredSize(new Dimension(tailleEcran.width / 4, tailleEcran.height / 5));
    JPanel panneauBoutonsAssociations = new JPanel();

    panneauAssociations.add(panneauListeAssociations, BorderLayout.CENTER);

    JPanel panneauBoutons = new JPanel(new GridLayout(3,1));
   boutonAjouterAss = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter"))
      {
        public void actionPerformed(ActionEvent e)
        {
          modifierAssociation(-1);
        }
      });
    panneauBoutons.add(boutonAjouterAss);

    boutonModifierAss = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier"))
    {
      public void actionPerformed(ActionEvent e)
      {
        modifierAssociation(listeAssociations.getSelectedIndex());
      }
    });
	boutonModifierAss.setEnabled(false);
    panneauBoutons.add(boutonModifierAss);

   boutonSupprimerAss = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimer"))
    {
      public void actionPerformed(ActionEvent e)
      {
        if (!listeAssociations.isSelectionEmpty())
        {
          int index = listeAssociations.getSelectedIndex();
          modelAssociations.remove(index);
          int nbIndex = modelAssociations.getSize();
          if (nbIndex <= 0)
          {
            listeAssociations.clearSelection();
          }
          else
          {
            if (index == nbIndex)
              index--;
            listeAssociations.setSelectedIndex(index);
          }
        }
      }
    });
	boutonSupprimerAss.setEnabled(false);
    panneauBoutons.add(boutonSupprimerAss);

    Box boiteBoutons = Box.createVerticalBox();
    boiteBoutons.add(Box.createVerticalGlue());
    boiteBoutons.add(panneauBoutons);
    boiteBoutons.add(Box.createVerticalGlue());
    panneauAssociations.add(boiteBoutons, BorderLayout.EAST);

    listeAssociations.addListSelectionListener(new ListSelectionListener()
     {
       public void valueChanged(ListSelectionEvent e)
       {
         boutonModifierAss.setEnabled(!listeAssociations.isSelectionEmpty());
         boutonSupprimerAss.setEnabled(!listeAssociations.isSelectionEmpty());
       }
     });

	lestab.addTab("Associations", panneauAssociations);


// Guides

	panneauGuides= new JPanel();
	panneauGuides.setLayout(new BorderLayout());
	panneauGuides.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("associationsguide")));
	listeGuides = new JList(this.modelGuides);
	listeGuides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	listeGuides.setCellRenderer(new DefaultListCellRenderer()
	  {
		public Component getListCellRendererComponent(JList list,
			  Object value,
			  int index,
			  boolean isSelected,
			  boolean cellHasFocus)
		  {
			Vector tableau = (Vector) value ;
			return super.getListCellRendererComponent(list, tableau.get(0) + " = " + tableau.get(1), index, isSelected, cellHasFocus);
		  }
	  });
	panneauListeGuides = new JScrollPane(listeGuides);
	JViewport vp2 = panneauListeGuides.getViewport();
	vp2.setPreferredSize(new Dimension(tailleEcran.width / 4, tailleEcran.height / 5));
	panneauBoutonsAssociations = new JPanel();

	panneauGuides.add(panneauListeGuides, BorderLayout.CENTER);

	panneauBoutons = new JPanel(new GridLayout(3,1));
	boutonAjouterGui = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter"))
	  {
		public void actionPerformed(ActionEvent e)
		{
		  modifierGuide(-1);
		}
	  });
	panneauBoutons.add(boutonAjouterGui);

	boutonModifierGui = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier"))
	{
	  public void actionPerformed(ActionEvent e)
	  {
		modifierGuide(listeGuides.getSelectedIndex());
	  }
	});
	boutonModifierGui.setEnabled(false);
	panneauBoutons.add(boutonModifierGui);

	boutonSupprimerGui = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimer"))
	{
	  public void actionPerformed(ActionEvent e)
	  {
		if (!listeGuides.isSelectionEmpty())
		{
		  int index = listeGuides.getSelectedIndex();
		  modelGuides.remove(index);
		  int nbIndex = modelGuides.getSize();
		  if (nbIndex <= 0)
		  {
			listeGuides.clearSelection();
		  }
		  else
		  {
			if (index == nbIndex)
			  index--;
			listeGuides.setSelectedIndex(index);
		  }
		}
	  }
	});
	boutonSupprimerGui.setEnabled(false);
	panneauBoutons.add(boutonSupprimerGui);

	boiteBoutons = Box.createVerticalBox();
	boiteBoutons.add(Box.createVerticalGlue());
	boiteBoutons.add(panneauBoutons);
	boiteBoutons.add(Box.createVerticalGlue());
	panneauGuides.add(boiteBoutons, BorderLayout.EAST);

	listeGuides.addListSelectionListener(new ListSelectionListener()
	 {
	   public void valueChanged(ListSelectionEvent e)
	   {
		 boutonModifierGui.setEnabled(!listeGuides.isSelectionEmpty());
		 boutonSupprimerGui.setEnabled(!listeGuides.isSelectionEmpty());
	   }
	 });

	lestab.addTab("Guides", panneauGuides);


//	Produits

	 panneauProduits= new JPanel();
	panneauProduits.setLayout(new BorderLayout());
	panneauProduits.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("associationsproduit")));
	listeProduits = new JList(this.modelProduits);
	listeProduits.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
	listeProduits.setCellRenderer(new DefaultListCellRenderer()
	   {
		 public Component getListCellRendererComponent(JList list,
			   Object value,
			   int index,
			   boolean isSelected,
			   boolean cellHasFocus)
		   {
			 Vector tableau = (Vector) value ;
			 return super.getListCellRendererComponent(list, tableau.get(0) + " = " + tableau.get(1), index, isSelected, cellHasFocus);
		   }
	   });
	panneauListeProduits = new JScrollPane(listeProduits);
	 JViewport vp3 = panneauListeProduits.getViewport();
	 vp3.setPreferredSize(new Dimension(tailleEcran.width / 4, tailleEcran.height / 5));
	 panneauBoutonsAssociations = new JPanel();

	panneauProduits.add(panneauListeProduits, BorderLayout.CENTER);

	 panneauBoutons = new JPanel(new GridLayout(3,1));
	 boutonAjouterPro = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter"))
	   {
		 public void actionPerformed(ActionEvent e)
		 {
		   modifierProduit(-1);
		 }
	   });
	 panneauBoutons.add(boutonAjouterPro);

	 boutonModifierPro = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier"))
	 {
	   public void actionPerformed(ActionEvent e)
	   {
		 modifierProduit(listeProduits.getSelectedIndex());
	   }
	 });
	 boutonModifierPro.setEnabled(false);
	 panneauBoutons.add(boutonModifierPro);

	 boutonSupprimerPro = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimer"))
	 {
	   public void actionPerformed(ActionEvent e)
	   {
		 if (!listeProduits.isSelectionEmpty())
		 {
		   int index = listeProduits.getSelectedIndex();
		   modelProduits.remove(index);
		   int nbIndex = modelProduits.getSize();
		   if (nbIndex <= 0)
		   {
			listeProduits.clearSelection();
		   }
		   else
		   {
			 if (index == nbIndex)
			   index--;
			listeProduits.setSelectedIndex(index);
		   }
		 }
	   }
	 });
	 boutonSupprimerPro.setEnabled(false);
	 panneauBoutons.add(boutonSupprimerPro);

	 boiteBoutons = Box.createVerticalBox();
	 boiteBoutons.add(Box.createVerticalGlue());
	 boiteBoutons.add(panneauBoutons);
	 boiteBoutons.add(Box.createVerticalGlue());
	panneauProduits.add(boiteBoutons, BorderLayout.EAST);

	listeProduits.addListSelectionListener(new ListSelectionListener()
	  {
		public void valueChanged(ListSelectionEvent e)
		{
		  boutonModifierPro.setEnabled(!listeProduits.isSelectionEmpty());
		  boutonSupprimerPro.setEnabled(!listeProduits.isSelectionEmpty());
		}
	  });

	 lestab.addTab("Produit", panneauProduits);

	
// Apparences

    panneauLookAndFeel = new JPanel();
    panneauLookAndFeel.setLayout(new BorderLayout());
    panneauLookAndFeel.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("lookandfeel")));
    lafComboBox = new JComboBox (UIManager.getInstalledLookAndFeels());
    lafComboBox.setRenderer(new DefaultListCellRenderer()
      {
        public Component getListCellRendererComponent(JList list,
                                              Object value,
                                              int index,
                                              boolean isSelected,
                                              boolean cellHasFocus)
        {
          return super.getListCellRendererComponent(list,
            ((UIManager.LookAndFeelInfo) value).getName(), index, isSelected, cellHasFocus);
        }
      });
    for (int i = 0 ; i < lafComboBox.getItemCount() ; i++)
    {
      if (((UIManager.LookAndFeelInfo)lafComboBox.getItemAt(i)).getClassName().equals(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_laf()))
      {
        lafComboBox.setSelectedIndex(i);
        break;
      }
    }
    panneauLookAndFeel.add(lafComboBox);

	JPanel papparence = new JPanel();
	papparence.setLayout(new GridLayout(2,1));
	
	papparence.add(panneauLookAndFeel);

    panneauLangues = new JPanel();
    panneauLangues.setLayout(new BorderLayout());
    panneauLangues.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("langues")));
    languesComboBox = new JComboBox (Langues.getLanguesInstallees()) ;
    languesComboBox.setRenderer(new DefaultListCellRenderer()
      {
        public Component getListCellRendererComponent(JList list,
                                              Object value,
                                              int index,
                                              boolean isSelected,
                                              boolean cellHasFocus)
        {
          return super.getListCellRendererComponent(list,
            (value == null) ? "" : ((Locale)value).getDisplayLanguage(), index, isSelected, cellHasFocus);
        }
      });
    for (int i = 0 ; i < languesComboBox.getItemCount() ; i++)
    {
      if (((Locale)languesComboBox.getItemAt(i)).equals(new Locale(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_langue())))
      {
        languesComboBox.setSelectedIndex(i);
        break;
      }
    }
    panneauLangues.add(languesComboBox);

	papparence.add(panneauLangues);
	lestab.addTab("Apparence", papparence);


// Chemins

    panneauApplications = new JPanel();
    panneauApplications.setLayout(new GridLayout(1, 1));
    panneauApplications.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("applications")));

    JPanel panneauApplicationApes = new JPanel(new BorderLayout()) ;
    panneauApplicationApes.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("appliapes")), BorderLayout.WEST) ;
    champApplicationApes = new JTextField(this._prefs.get_pathApes());
    panneauApplicationApes.add(this.champApplicationApes, BorderLayout.CENTER) ;
    panneauApplicationApes.add(new JButton(new AbstractAction("...")
        {
          public void actionPerformed(ActionEvent e)
          {
            File fichier = PogToolkit.chooseFileWithFilter(_this, new MyMultiFileFilter(".jar"), System.getProperty("user.dir"));
            if (fichier != null && fichier.exists())
            {
              champApplicationApes.setText(fichier.getAbsolutePath());
            }
          }
        }), BorderLayout.EAST) ;
    panneauApplications.add(panneauApplicationApes) ;

//	lestab.addTab("Application", panneauApplications);
//    destination.add(panneauApplications);

	JPanel pchemins = new JPanel();
	pchemins.setLayout(new BorderLayout());
	pchemins.add(panneauApplications, BorderLayout.NORTH);

    panneauPresentation = new JPanel();
    panneauPresentation.setLayout(new GridLayout(6, 1));
    panneauPresentation.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("proppresentation")));

    JPanel panneauPresChemIcones = new JPanel(new BorderLayout()) ;
    panneauPresChemIcones.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("cheminicone")), BorderLayout.WEST) ;
    champPresChemIcones = new JTextField(this._prefs.get_pathIconeDefaut());
    panneauPresChemIcones.add(this.champPresChemIcones, BorderLayout.CENTER) ;
    panneauPresChemIcones.add(new JButton(new AbstractAction("...")
        {
          public void actionPerformed(ActionEvent e)
          {
            File fichier = PogToolkit.chooseDirectory(_this, champPresChemIcones.getText());
            if (fichier != null && fichier.exists())
            {
              champPresChemIcones.setText(fichier.getAbsolutePath());
            }
          }
        }), BorderLayout.EAST) ;
    panneauPresentation.add(panneauPresChemIcones) ;

    JPanel panneauPresChemBiblio = new JPanel(new BorderLayout()) ;
    panneauPresChemBiblio.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("nombibliotheque")), BorderLayout.WEST) ;
    champPresChemBiblio = new JTextField(this._prefs.get_pathBiblio());
    panneauPresChemBiblio.add(this.champPresChemBiblio, BorderLayout.CENTER) ;
    panneauPresChemBiblio.add(new JButton(new AbstractAction("...")
        {
          public void actionPerformed(ActionEvent e)
          {
            File fichier = PogToolkit.chooseDirectory(_this, champPresChemBiblio.getText());            
			if (fichier != null && fichier.exists())
			{
			  champPresChemBiblio.setText(fichier.getAbsolutePath());
			}
          }
        }), BorderLayout.EAST) ;
    panneauPresentation.add(panneauPresChemBiblio) ;

    JPanel panneauPresChemPre = new JPanel(new BorderLayout()) ;
    panneauPresChemPre.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("cheminpre")), BorderLayout.WEST) ;
    champPresChemPre = new JTextField(this._prefs.get_pathPre());
    panneauPresChemPre.add(this.champPresChemPre, BorderLayout.CENTER) ;
    panneauPresChemPre.add(new JButton(new AbstractAction("...") {
    	public void actionPerformed(ActionEvent e)
		{
    		File fichier = PogToolkit.chooseDirectory(_this, champPresChemPre.getText());
    		if (fichier != null && fichier.exists())
    		{
    			champPresChemPre.setText(fichier.getAbsolutePath());
    		}
    	}
    }), BorderLayout.EAST) ;
    panneauPresentation.add(panneauPresChemPre) ;
    
    JPanel panneauPresChemPog = new JPanel(new BorderLayout()) ;
    panneauPresChemPog.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("cheminpog")), BorderLayout.WEST) ;
    champPresChemPog = new JTextField(this._prefs.get_pathPog());
    panneauPresChemPog.add(this.champPresChemPog, BorderLayout.CENTER) ;
    panneauPresChemPog.add(new JButton(new AbstractAction("...") {
    	public void actionPerformed(ActionEvent e)
		{
    		File fichier = PogToolkit.chooseDirectory(_this, champPresChemPog.getText());
    		if (fichier != null && fichier.exists())
    		{
    			champPresChemPog.setText(fichier.getAbsolutePath());
    		}
    	}
    }), BorderLayout.EAST) ;
    panneauPresentation.add(panneauPresChemPog) ;

	JPanel panneauPresChemApes = new JPanel(new BorderLayout()) ;
	panneauPresChemApes.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("cheminapes")), BorderLayout.WEST) ;
	champPresChemApes = new JTextField(this._prefs.get_pathModeleApes());
	panneauPresChemApes.add(this.champPresChemApes, BorderLayout.CENTER) ;
	panneauPresChemApes.add(new JButton(new AbstractAction("...") {
		public void actionPerformed(ActionEvent e)
		{
			File fichier = PogToolkit.chooseDirectory(_this, champPresChemApes.getText());
			if (fichier != null && fichier.exists())
			{
				champPresChemApes.setText(fichier.getAbsolutePath());
			}
		}
	}), BorderLayout.EAST) ;
	panneauPresentation.add(panneauPresChemApes) ;
    
    jcheckuse = new JCheckBox(lnkFenetrePrincipale.getLnkLangues().valeurDe("usechemmodsav"));
    jcheckuse.setSelected(_prefs.get_utiliseCheminModele());
	panneauPresentation.add(jcheckuse);

	pchemins.add(panneauPresentation, BorderLayout.SOUTH);
	
  	lestab.addTab("Chemins", pchemins);
    
    
// Icones

   JPanel pico = new JPanel();
   pico.setLayout(new GridLayout(2, 1));
   pico.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("defico")));

   JPanel panneauIcoPack = new JPanel(new BorderLayout()) ;
   panneauIcoPack.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("deficopack")), BorderLayout.WEST) ;
   champIcoPack = new JTextField(this._prefs.get_defIcoPack());
   panneauIcoPack.add(this.champIcoPack, BorderLayout.CENTER) ;
   panneauIcoPack.add(new JButton(new AbstractAction("...")
	   {
		 public void actionPerformed(ActionEvent e)
		 {
			File ff = lnkFenetrePrincipale.iconeChooser(champIcoPack.getText());
			if (ff != null) {
				if (ff.getAbsolutePath().startsWith(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut()))
					champIcoPack.setText(ff.getAbsolutePath().substring(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut().length() + 1));
				else
					champIcoPack.setText(ff.getAbsolutePath());
			}

		 }
	   }), BorderLayout.EAST) ;
	pico.add(panneauIcoPack);
	
	JPanel panneauIcoElem = new JPanel(new BorderLayout()) ;
	panneauIcoElem.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("deficoelem")), BorderLayout.WEST) ;
	champIcoElem = new JTextField(this._prefs.get_defIcoElem());
	panneauIcoElem.add(this.champIcoElem, BorderLayout.CENTER) ;
	panneauIcoElem.add(new JButton(new AbstractAction("...")
		{
		  public void actionPerformed(ActionEvent e)
		  {
			File ff = lnkFenetrePrincipale.iconeChooser(champIcoElem.getText());
			if (ff != null) {
				if (ff.getAbsolutePath().startsWith(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut()))
					champIcoElem.setText(ff.getAbsolutePath().substring(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut().length() + 1));
				else
					champIcoElem.setText(ff.getAbsolutePath());
		  	}
		  }
		}), BorderLayout.EAST) ;
	 pico.add(panneauIcoElem);

	lestab.addTab("Icônes", pico);

	this.getContentPane().add(lestab, BorderLayout.CENTER);
//    this.getContentPane().add(destination, BorderLayout.CENTER);
  }

  public void actionOK() {
    UIManager.LookAndFeelInfo lafi = (UIManager.LookAndFeelInfo)this.lafComboBox.getSelectedItem();
    this._prefs.set_laf(lafi.getClassName());
    this._prefs.setAssociations(modelAssociations.getElements());
	this._prefs.set_guide(modelGuides.getElements());
	this._prefs.set_produit(modelProduits.getElements());
    if (!this._prefs.get_langue().equals(((Locale)this.languesComboBox.getSelectedItem()).getLanguage()))
        PogToolkit.showMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("msgredemarrerlangue"), this.lnkFenetrePrincipale);
    this._prefs.set_langue(((Locale)this.languesComboBox.getSelectedItem()).getLanguage());
    this._prefs.set_pathApes(this.champApplicationApes.getText());
    if (!_prefs.get_pathIconeDefaut().equals(champPresChemIcones.getText())) {
		this._prefs.set_pathIconeDefaut(this.champPresChemIcones.getText());
    	lnkFenetrePrincipale.getLnkPanneauBibliotheque().load();
    }
    this._prefs.set_pathPog(this.champPresChemPog.getText());
	this._prefs.set_pathPre(this.champPresChemPre.getText());
	this._prefs.set_pathModeleApes(this.champPresChemApes.getText());
    this._prefs.set_pathBiblio(this.champPresChemBiblio.getText());
    this._prefs.set_utiliseCheminModele(jcheckuse.isSelected());
	this._prefs.set_defIcoElem(this.champIcoElem.getText());
	this._prefs.set_defIcoPack(this.champIcoPack.getText());
    this._prefs.sauverPrefs();
    FenetrePrincipale.INSTANCE.getLnkMainToolBar().repaint();
    this.dispose();
  }

  public void actionAnnuler() {
    this.dispose();
  }

  private void modifierAssociation(int i) {
    String titre ;
    String ext ;
    String appli;

    JFileChooser fc ;
    if (i == -1)
    {
      titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter") ;
      ext = PogToolkit.askForString(lnkFenetrePrincipale.getLnkLangues().valeurDe("extensionaajouter"), titre) ;
      if (ext == null)
        return;
      fc  = new JFileChooser() ;
    }
    else
    {
      titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier") ;
      Vector v = (Vector)modelAssociations.getElementAt(i);
      ext = v.get(0).toString() ;
      File file = new File(v.get(1).toString());
      fc = new JFileChooser(file) ;
      fc.setSelectedFile(file);
    }
    fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
    fc.setDialogTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("applicationaassociera") + " " + ext);
    int result = fc.showOpenDialog(this);
    if (result == JFileChooser.APPROVE_OPTION)
    {
      modelAssociations.put(ext, fc.getSelectedFile().getAbsolutePath());
    }
  }

  private void modifierGuide(int i) {
	String titre ;
	String ext ;
	String appli;
	String ico;

	if (i == -1)
	{
	  titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter") ;
	  ext = PogToolkit.askForString(lnkFenetrePrincipale.getLnkLangues().valeurDe("nomguide"), titre) ;
	  if (ext == null)
		return;
		ico = null;
	}
	else
	{
	  titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier") ;
	  Vector v = (Vector)modelGuides.getElementAt(i);
	  ext = v.get(0).toString() ;
	  ico = v.get(1).toString();
	}
	File ff = lnkFenetrePrincipale.iconeChooser(ico);
	if (ff != null) {
		if (ff.getAbsolutePath().startsWith(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut()))
	  		modelGuides.put(ext, ff.getAbsolutePath().substring(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut().length() + 1));
	  	else
			modelGuides.put(ext, ff.getAbsolutePath());
	}
  }

  private void modifierProduit(int i) {
	String titre ;
	String ext ;
	String appli;
	String ico;

	if (i == -1)
	{
	  titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter") ;
	  ext = PogToolkit.askForString(lnkFenetrePrincipale.getLnkLangues().valeurDe("nomproduit"), titre) ;
	  if (ext == null)
		return;
		ico = null;
	}
	else
	{
	  titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier") ;
	  Vector v = (Vector)modelProduits.getElementAt(i);
	  ext = v.get(0).toString() ;
	  ico = v.get(1).toString();
	}
	File ff = lnkFenetrePrincipale.iconeChooser(ico);
	if (ff != null) {
		if (ff.getAbsolutePath().startsWith(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut()))
			modelProduits.put(ext, ff.getAbsolutePath().substring(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut().length() + 1));
		else
			modelProduits.put(ext, ff.getAbsolutePath());
	}
  }


  private class AssociationsListModel extends AbstractListModel
  {
    Object [] keys ;
    HashMap _copieAssoc;

	public HashMap getElements() {
		return _copieAssoc;
	}

    public AssociationsListModel(HashMap valeurs)
    {
      super();
      _copieAssoc = valeurs;
      this.majKeys();
    }
    private void majKeys()
    {
      keys = _copieAssoc.keySet().toArray();
      Arrays.sort(keys);
    }
    public int getSize()
    {
      return _copieAssoc.size();
    }

    public Object getElementAt(int index)
    {
      if (_copieAssoc != null)
      {
        Vector result = new Vector(2);
        result.add(0, keys[index]);
        result.add(1, _copieAssoc.get(keys [index]));
        return result;
      }
      return null;
    }

    public void remove(int index)
    {
      _copieAssoc.remove(keys[index]);
      this.fireContentsChanged(this, index, index);
      this.majKeys();
    }

    public void put(Object obj1, Object obj2)
    {
      _copieAssoc.put(obj1, obj2);
      this.fireContentsChanged(this, 0, this.getSize() - 1);
      this.majKeys();
    }
  }
}
