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
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
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

  private JButton boutonAjouter;
  private JButton boutonModifier;
  private JButton boutonSupprimer;
  private JList listeAssociations;
  private JPanel panneauAssociations;
  private JPanel panneauLookAndFeel;
  private JPanel panneauLangues;
  private JPanel panneauApplications;
  private JPanel panneauPresentation;
  private JScrollPane panneauListeAssociations;
  private AssociationsListModel modelAssociations ;
  private JComboBox lafComboBox;
  private JComboBox languesComboBox;
  private JTextField champApplicationApes ;
  private JTextField champPresChemIcones;
  private JTextField champPresChemBiblio;
  private Preferences _prefs ;
  private HashMap _copieAssoc ;
  private FenetrePreferences _this ;

  public FenetrePreferences(FenetrePrincipale fp, Preferences p) throws HeadlessException {
    super(fp);
    this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("preferences"));
    this.setDefaultCloseOperation(WindowConstants.DISPOSE_ON_CLOSE);
    this._this = this ;
    this._prefs = p ;
    this._copieAssoc = new HashMap(_prefs.getAssociations());

    // Initialisation du modele de la liste
    this.modelAssociations = new AssociationsListModel() ;

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
    Box destination = Box.createVerticalBox();

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
    boutonAjouter = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouter"))
      {
        public void actionPerformed(ActionEvent e)
        {
          modifierAssociation(-1);
        }
      });
    panneauBoutons.add(boutonAjouter);

    boutonModifier = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("modifier"))
    {
      public void actionPerformed(ActionEvent e)
      {
        modifierAssociation(listeAssociations.getSelectedIndex());
      }
    });
    boutonModifier.setEnabled(false);
    panneauBoutons.add(boutonModifier);

    boutonSupprimer = new JButton(new AbstractAction(lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimer"))
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
    boutonSupprimer.setEnabled(false);
    panneauBoutons.add(boutonSupprimer);

    Box boiteBoutons = Box.createVerticalBox();
    boiteBoutons.add(Box.createVerticalGlue());
    boiteBoutons.add(panneauBoutons);
    boiteBoutons.add(Box.createVerticalGlue());
    panneauAssociations.add(boiteBoutons, BorderLayout.EAST);

    listeAssociations.addListSelectionListener(new ListSelectionListener()
     {
       public void valueChanged(ListSelectionEvent e)
       {
         boutonModifier.setEnabled(!listeAssociations.isSelectionEmpty());
         boutonSupprimer.setEnabled(!listeAssociations.isSelectionEmpty());
       }
     });

    destination.add(panneauAssociations);

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

    destination.add(panneauLookAndFeel);

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

    destination.add(panneauLangues);

    panneauApplications = new JPanel();
    panneauApplications.setLayout(new GridLayout(1, 1));
    panneauApplications.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("applications")));

    JPanel panneauApplicationApes = new JPanel(new BorderLayout()) ;
    panneauApplicationApes.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("appliapes")), BorderLayout.WEST) ;
    champApplicationApes = new JTextField(this._prefs.getPathApes());
    panneauApplicationApes.add(this.champApplicationApes, BorderLayout.CENTER) ;
    panneauApplicationApes.add(new JButton(new AbstractAction("...")
        {
          public void actionPerformed(ActionEvent e)
          {
            File fichier = PogToolkit.chooseFileWithFilter(_this, new MyMultiFileFilter(".jar"));
            if (fichier != null && fichier.exists())
            {
              champApplicationApes.setText(fichier.getAbsolutePath());
            }
          }
        }), BorderLayout.EAST) ;
    panneauApplications.add(panneauApplicationApes) ;

    destination.add(panneauApplications);

    panneauPresentation = new JPanel();
    panneauPresentation.setLayout(new GridLayout(2, 1));
    panneauPresentation.setBorder(new TitledBorder(lnkFenetrePrincipale.getLnkLangues().valeurDe("proppresentation")));

    JPanel panneauPresChemIcones = new JPanel(new BorderLayout()) ;
    panneauPresChemIcones.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("cheminicone")), BorderLayout.WEST) ;
    champPresChemIcones = new JTextField(this._prefs.getPathIcones());
    panneauPresChemIcones.add(this.champPresChemIcones, BorderLayout.CENTER) ;
    panneauPresChemIcones.add(new JButton(new AbstractAction("...")
        {
          public void actionPerformed(ActionEvent e)
          {
            File fichier = PogToolkit.chooseDirectory(_this);
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
            File fichier = PogToolkit.chooseDirectory(_this);
            if (fichier != null && fichier.exists())
            {
              champPresChemBiblio.setText(fichier.getAbsolutePath());
            }
          }
        }), BorderLayout.EAST) ;
    panneauPresentation.add(panneauPresChemBiblio) ;

    destination.add(panneauPresentation);

    this.getContentPane().add(destination, BorderLayout.CENTER);
  }

  public void actionOK() {
    UIManager.LookAndFeelInfo lafi = (UIManager.LookAndFeelInfo)this.lafComboBox.getSelectedItem();
    this._prefs.set_laf(lafi.getClassName());
    this._prefs.setAssociations(this._copieAssoc);
    if (!this._prefs.get_langue().equals(((Locale)this.languesComboBox.getSelectedItem()).getLanguage()))
        PogToolkit.showMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("msgredemarrerlangue"), this.lnkFenetrePrincipale);
    this._prefs.set_langue(((Locale)this.languesComboBox.getSelectedItem()).getLanguage());
    this._prefs.setPathApes(this.champApplicationApes.getText());
    this._prefs.setPathIcones(this.champPresChemIcones.getText());
    this._prefs.set_pathBiblio(this.champPresChemBiblio.getText());
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

  private class AssociationsListModel extends AbstractListModel
  {
    Object [] keys ;

    public AssociationsListModel()
    {
      super();
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
