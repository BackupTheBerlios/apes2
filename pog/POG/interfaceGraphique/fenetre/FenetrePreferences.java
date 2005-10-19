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

public class FenetrePreferences extends FenetrePOG {

  private JPanel panneauAssociations;
  private JPanel panneauGuides;
  private JPanel panneauProduits;
  private JPanel panneauPlantypes;
  
  private JPanel panneauLookAndFeel;
  private JPanel panneauLangues;
  private JPanel panneauApplications;
  private JPanel panneauPresentation;
  
  private AssociationsListModel modelAssociations ;
  private AssociationsListModel modelGuides;
  private AssociationsListModel modelProduits;
  private AssociationsListModel modelPlantypes;
  
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
	this.modelPlantypes = new AssociationsListModel(_prefs.get_plantypes());

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

	panneauAssociations = new MaPreference(lnkFenetrePrincipale, modelAssociations) {
		String getTitleKey() { return "extensionaajouter"; }
		String getTitleValue() { return "applicationaassociera"; }
		boolean isFileChooser() { return true; }
		String getTitleBorder() { return "associations"; }};
	lestab.addTab("Associations", panneauAssociations);

	panneauGuides = new MaPreference(lnkFenetrePrincipale, modelGuides) {
		String getTitleBorder() { return "associationsguide"; }
		String getTitleKey() { return "nomguide"; }
		String getTitleValue() { return null; }
		boolean isFileChooser() { return false; }};
	lestab.addTab("Guides", panneauGuides);

	panneauProduits = new MaPreference(lnkFenetrePrincipale, modelProduits) {
		String getTitleBorder() { return "associationsproduit"; }
		String getTitleKey() { return "nomproduit"; }
		String getTitleValue() { return null; }
		boolean isFileChooser() { return false; }};
	lestab.addTab("Produit", panneauProduits);

	panneauPlantypes = new MaPreference(lnkFenetrePrincipale, modelPlantypes) {
		String getTitleBorder() { return "associationsplantype"; }
		String getTitleKey() { return "classeelement"; }
		String getTitleValue() { return "fichierplantype"; }
		boolean isFileChooser() { return true; }};
	lestab.addTab("Plan Types", panneauPlantypes);

	
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
	this._prefs.set_plantype(modelPlantypes.getElements());
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
}

class AssociationsListModel extends AbstractListModel {
	Object [] keys ;
	HashMap _copieAssoc;
	
	public HashMap getElements() {
		return _copieAssoc;
	}
	
	public AssociationsListModel(HashMap valeurs) {
		super();
		_copieAssoc = valeurs;
		this.majKeys();
	}
	
	private void majKeys() {
		keys = _copieAssoc.keySet().toArray();
		Arrays.sort(keys);
	}
	
	public int getSize() {
		return _copieAssoc.size();
	}
	
	public Object getElementAt(int index) {
		if (_copieAssoc == null)
			return null;
		Vector result = new Vector(2);
		result.add(0, keys[index]);
		result.add(1, _copieAssoc.get(keys [index]));
		return result;
	}
	
	public void remove(int index) {
		_copieAssoc.remove(keys[index]);
		this.fireContentsChanged(this, index, index);
		this.majKeys();
	}
	
	public void put(Object obj1, Object obj2) {
		_copieAssoc.put(obj1, obj2);
		this.fireContentsChanged(this, 0, this.getSize() - 1);
		this.majKeys();
	}
}


abstract class MaPreference extends JPanel {

	public MaPreference(final FenetrePrincipale lnkFenetrePrincipale, final AssociationsListModel themodel) {
		super();
		this.setLayout(new BorderLayout());
		this.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe(getTitleBorder())));
		final JList theliste = new JList(themodel);
		theliste.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		theliste.setCellRenderer(new DefaultListCellRenderer() {
			public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus) {
				Vector tableau = (Vector) value ;
				return super.getListCellRendererComponent(list, tableau.get(0) + " = " + tableau.get(1), index, isSelected, cellHasFocus);
			}
		});
		JScrollPane panneauListe = new JScrollPane(theliste);
		JViewport vp = panneauListe.getViewport();
		Dimension tailleEcran = Toolkit.getDefaultToolkit().getScreenSize();
		vp.setPreferredSize(new Dimension(tailleEcran.width / 4, tailleEcran.height / 5));
		this.add(panneauListe, BorderLayout.CENTER);
		JPanel panneauBoutons = new JPanel(new GridLayout(3,1));
		JButton boutonAjouter = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter")) {
			public void actionPerformed(ActionEvent e) {
				modifier(-1, lnkFenetrePrincipale, themodel);
			}
		});
		panneauBoutons.add(boutonAjouter);
		final JButton boutonModifier = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier")) {
			public void actionPerformed(ActionEvent e) {
				modifier(theliste.getSelectedIndex(), lnkFenetrePrincipale, themodel);
			}
		});
		boutonModifier.setEnabled(false);
		panneauBoutons.add(boutonModifier);
		final JButton boutonSupprimer = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimer")) {
			public void actionPerformed(ActionEvent e) {
				if (!theliste.isSelectionEmpty()) {
					int index = theliste.getSelectedIndex();
					themodel.remove(index);
					int nbIndex = themodel.getSize();
					if (nbIndex <= 0)
						theliste.clearSelection();
					else {
						if (index == nbIndex)
							index--;
						theliste.setSelectedIndex(index);
					}
				}
			}
		});
		boutonSupprimer.setEnabled(false);
		panneauBoutons.add(boutonSupprimer);
		Box boiteBoutons = Box.createVerticalBox();
		boiteBoutons.add(Box.createVerticalGlue());
		boiteBoutons.add(panneauBoutons);
		boiteBoutons.add(Box.createVerticalGlue());
		this.add(boiteBoutons, BorderLayout.EAST);
		theliste.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent e) {
				boutonModifier.setEnabled(!theliste.isSelectionEmpty());
				boutonSupprimer.setEnabled(!theliste.isSelectionEmpty());
			}
		});
	}

	private void modifier(int i, FenetrePrincipale lnkFenetrePrincipale, AssociationsListModel themodel) {
		String titre ;
		String ext ;
		String appli;
		String ico;
		JFileChooser fc = null;
		if (i == -1) {
			titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter") ;
			ext = PogToolkit.askForString(lnkFenetrePrincipale.getLnkLangues().valeurDe(getTitleKey()), titre, "") ;
			if (ext == null)
				return;
			fc  = new JFileChooser();
			ico = null;
		}
		else {
			titre = lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier") ;
			Vector v = (Vector)themodel.getElementAt(i);
			ext = v.get(0).toString();
			ico = v.get(1).toString();
			if (isFileChooser()) {
				File file = new File(ico);
				fc = new JFileChooser(file) ;
				fc.setSelectedFile(file);
			}
		}
		if (isFileChooser()) {
			fc.setFileSelectionMode(JFileChooser.FILES_ONLY);
			fc.setDialogTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe(getTitleValue()) + " " + ext);
			int result = fc.showOpenDialog(this);
			if (result == JFileChooser.APPROVE_OPTION)
				themodel.put(ext, fc.getSelectedFile().getAbsolutePath());
		}
		else {
			File ff = lnkFenetrePrincipale.iconeChooser(ico);
			if (ff != null) {
				if (ff.getAbsolutePath().startsWith(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut()))
					themodel.put(ext, ff.getAbsolutePath().substring(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut().length() + 1));
				else
					themodel.put(ext, ff.getAbsolutePath());
			}
		}
	}
	
	abstract String getTitleBorder();

	abstract String getTitleKey();

	abstract String getTitleValue();

	abstract boolean isFileChooser();

}