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


/* Generated by Together */
package POG.interfaceGraphique.fenetre;
import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.Frame;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;

import POG.interfaceGraphique.action.ControleurPanneaux;
import POG.interfaceGraphique.action.Systeme;
import POG.interfaceGraphique.utile.arbre.ArbreExplorateur;
import POG.interfaceGraphique.utile.arbre.ArbrePresentation;
import POG.interfaceGraphique.utile.icone.ImageFileView;
import POG.interfaceGraphique.utile.icone.ImageFilter;
import POG.interfaceGraphique.utile.icone.ImagePreview;
import POG.interfaceGraphique.utile.icone.PanneauBibliotheque;
import POG.interfaceGraphique.utile.menu.MainToolBar;
import POG.interfaceGraphique.utile.menu.POGMenu;
import POG.interfaceGraphique.utile.trace.Debug;
import POG.objetMetier.ElementPresentation;
import POG.outil.html.pika.appliTest;
import POG.utile.PogToolkit;
import POG.utile.propriete.Langues;
import POG.utile.propriete.Preferences;

public class FenetrePrincipale
    extends JFrame {

  private JSplitPane jSplitPane1 = new JSplitPane();
  private JSplitPane jSplitPane2 = new JSplitPane();
  private JSplitPane jSplitPane3 = new JSplitPane();
  private JSplitPane jSplitPane4 = new JSplitPane();

  private JPanel _panelArbrePresentation = new JPanel();
  private JPanel _panelExploBiblio = new JPanel();

  private JScrollPane jScrollPane1 = new JScrollPane();
  private JScrollPane jScrollPane2 = new JScrollPane();
  private JScrollPane jScrollPane3 = new JScrollPane();

  private BorderLayout borderLayout1 = new BorderLayout();
  private BorderLayout borderLayout2 = new BorderLayout();
  private BorderLayout borderLayout3 = new BorderLayout();
  private BorderLayout borderLayout5 = new BorderLayout();
  private BorderLayout borderLayout6 = new BorderLayout();
  private BorderLayout borderLayout7 = new BorderLayout();
  private BorderLayout borderLayout8 = new BorderLayout();

  private POGMenu _menu = null;


	// Excecute les traitement en arriere plan
	public static abstract class TheTraitement {

		public abstract void traitement();

		private Thread Thth = new Thread() {
			public void run() {
				traitement();
			}
		};

		public TheTraitement(String name) {
			Thth.setName(name);
			Thth.setDaemon(true);
			Thth.start();
		}
	}

  private static FenetrePrincipale _fenetrePrincipale = null;

  /**
   * @directed
   * @supplierCardinality 1
   * @associates utile.propriete.Preferences
   */
  private Preferences lnkPreferences = null;

  /**
   * @directed
   * @supplierCardinality 1
   * @associates utile.propriete.Langues
   */
  private Langues lnkLangues = null;

  /**
   * @directed
   * @supplierCardinality 1
   * @associates interfaceGraphique.utile.trace.Debug
   */
  private Debug lnkDebug = null;

  /**
   * @directed
   * @supplierCardinality 1*/
  private Systeme lnkSysteme = null;

  /**
   * @directed
   * @supplierCardinality 1
   */
  private FenetreEnregistrerSous lnkFenetreEnregistrerSous = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private FenetreExport lnkFenetreExport = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private FenetreNouvellePresentationSansModele lnkFenetreOuverture = null;

  /**
   * @supplierCardinality 1
   * @directed
   */

  private FenetreNouvellePresentationAvecModele
      lnkFenetreNouvellePresentationAvecModele = null;

  /**
   * @supplierCardinality 1
   * @directed
   */

  private FenetrePreferences lnkFenetrePreferences = null;

  /**
   * @directed
   * @supplierCardinality 1
   */
  private FenetreAPropos lnkFenetreAPropos = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private appliTest lnkappliTest = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private ControleurPanneaux lnkControleurPanneaux = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private ArbrePresentation lnkArbrePresentation = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private ArbreExplorateur lnkArbreExplorateur = null;
  
	private FenetreElementLien lnkFenetreElementLien = null;
	private FenetreChangerIcone lnkFenetreChangerIcone = null;
  
  public JLabel jLabelPathBib = new JLabel();
  
  private MainToolBar lnkMainToolBar = null;

  private JFileChooser lnkJIconChooser = null;

  private String _pathSave = "";
  private String _pathExport = "";
  

  public static FenetrePrincipale INSTANCE;


  public void afficheChangerIcone(ElementPresentation elmt) {
      File file = iconeChooser(null);
      if (file == null)
      	return;
      lnkSysteme.changerIcone(elmt, file);
      getLnkControleurPanneaux().reload();
  }

	public File iconeChooser(String chem) {
		//Show it.
		File dir = new File(lnkPreferences.get_pathIconeDefaut());
		getLnkJIconChooser().setCurrentDirectory(dir);
		if (chem != null)
			getLnkJIconChooser().setSelectedFile(new File(dir.getAbsolutePath() + File.separator + chem));
		int returnVal = getLnkJIconChooser().showDialog(this, getLnkLangues().valeurDe("titreIconChooser"));

		//Process the results.
		if (returnVal == JFileChooser.APPROVE_OPTION) {
		  return getLnkJIconChooser().getSelectedFile();
		}
		return null;
	}


  /**
   * @directed
   * @supplierCardinality 1
   */
  private PanneauBibliotheque lnkPanneauBibliotheque = null;
  public FenetrePrincipale() throws HeadlessException, ClassNotFoundException {
    _fenetrePrincipale = this;
//    new RapportBug();
      INSTANCE = this;
      PogToolkit._PATH_APPLI = System.getProperty("user.dir");
      lnkDebug = new Debug();
      lnkDebug.debogage("Chargement des pr�f�rences...");
      lnkPreferences = Preferences.charger(this);
      lnkDebug.debogage("Chargement des langues...");
      lnkLangues = new Langues(lnkPreferences.get_langue());
      lnkDebug.debogage("Chargement du syst�me :");
      lnkSysteme = new Systeme(lnkPreferences, this);
      lnkDebug.debogage("... Menu ...");
      _menu = new POGMenu(this);
      lnkDebug.debogage("... Arbres ...");
      lnkArbrePresentation = new ArbrePresentation(lnkSysteme);
      lnkArbreExplorateur = new ArbreExplorateur(lnkSysteme);
      lnkPanneauBibliotheque = new PanneauBibliotheque(this);
      lnkDebug.debogage("... ToolBar ...");
      lnkMainToolBar = new MainToolBar(this);
      lnkMainToolBar.setButtonsState(false);
      lnkDebug.debogage("... Controleur ...");
      lnkControleurPanneaux = new ControleurPanneaux(jSplitPane4, _menu.getMenuCentral(), lnkSysteme);
      System.out.println("... Interface Graphique");
      try {
        jbInit();
      }
      catch (Exception ex) {
        ex.printStackTrace();
      }
      PogToolkit.centerWindow(this);
      lnkDebug.clearTexte();
  }
  static Process pp;

  public static void main(String[] args) {
    try {
    	
      // Ne pas enlever les println d'ici...
      DateFormat dt = DateFormat.getTimeInstance();
      System.out.println("Start POG at " + dt.format(new Date()));
      FenetrePrincipale fp;
      String argof = new String("");
      for (int i = 0; i < args.length; i++) {
        argof = argof + args[i] + " ";
        if (args[i].startsWith("user.home="))
          System.setProperty("user.home", args[i].substring(10));
      }
      try {
        fp = new FenetrePrincipale();
      }
      catch (Exception e) {
        boolean yes = (args.length == 0);
        if (!yes)
          yes = !args[args.length - 1].equals("SEC");
        if (yes) {
          pp = Runtime.getRuntime().exec("java -classpath \"" + System.getProperty("java.class.path") + "\"  POG.interfaceGraphique.fenetre.FenetrePrincipale " + argof + " SEC");

		new FenetrePrincipale.TheTraitement("Suivi") {
			public void traitement() {
				InputStream is = pp.getInputStream();
			   OutputStream os = System.out;

				   int c;
				   try {
					while ((c = is.read()) != -1) {
						   os.write(c);
					   }
					} catch (IOException e) {
						e.printStackTrace();
					}
			   }
		};
//		InputStream is = pp.getInputStream();

		 InputStream is = pp.getErrorStream();
		OutputStream os = System.out;

		  Runtime.getRuntime().addShutdownHook(new Thread() {
            public void run() {
              pp.destroy();
            }
          });

			int c;
			while ((c = is.read()) != -1) {
				os.write(c);
			}
        }
        else
          System.out.println("Enable to run POG");
        System.exit(0);
      }
      INSTANCE.show();
      System.out.println("End of Launch at " + dt.format(new Date()));
      System.out.println("Java version : " + System.getProperty("java.version"));

       java.net.URL ip = ClassLoader.getSystemResource("org/ipsquad/apes");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    this.setLocale(java.util.Locale.getDefault());
    this.setState(Frame.NORMAL);
    this.setTitle("POG");

    jSplitPane1.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    jSplitPane1.setDividerSize(10);
    jSplitPane1.setDividerLocation(250);

    jSplitPane2.setOrientation(JSplitPane.HORIZONTAL_SPLIT);
    jSplitPane2.setDividerSize(10);
    jSplitPane2.setDividerLocation(425);

    jSplitPane3.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane3.setDividerSize(10);
    jSplitPane3.setDividerLocation(300);

    jSplitPane4.setOrientation(JSplitPane.VERTICAL_SPLIT);
    jSplitPane4.setDividerSize(10);
    jSplitPane4.setDividerLocation(600);

	_panelExploBiblio.setLayout(borderLayout7);
    JPanel panelBiblio = new JPanel();
	panelBiblio.setLayout(new BorderLayout());
    
    
	JButton btnChangerBib = new JButton();
	btnChangerBib.setToolTipText(lnkLangues.valeurDe("changerbiblio"));
	btnChangerBib.setIcon(lnkPreferences.getIconeDefaut("synchroniser"));
    final FenetrePrincipale fpp = this;
    
    btnChangerBib.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent arg0) {
			if (lnkSysteme.getlnkControleurPresentation().getlnkPresentation() == null)
				return;
			File newBib = PogToolkit.chooseDirectory(fpp, lnkSysteme.getlnkControleurPresentation().getlnkPresentation().lnkBibliotheque.getAbsolutePath());
			lnkSysteme.changerBibliotheque(newBib);
		} 
  	});
	JButton brefr = new JButton();
	brefr.setIcon(lnkPreferences.getIconeDefaut("process_tree_node_process"));

	brefr.addActionListener(new ActionListener() {
	  public void actionPerformed(ActionEvent evt) {
		lnkArbreExplorateur.load();
	  }
	});

	JButton badd = new JButton();
	badd.setToolTipText(lnkLangues.valeurDe("copierfich"));
	badd.setIcon(lnkPreferences.getIconeDefaut("menu_item_add_artefact"));
	
	badd.addActionListener(new ActionListener() {
		private String oldPath;
		
		public void actionPerformed(ActionEvent evt) {
			if (lnkSysteme.getlnkControleurPresentation().getlnkPresentation() == null)
				return;
			if (oldPath == null)
				oldPath = System.getProperty("user.dir");
			File [] tocopy = PogToolkit.chooseMultipleFile(fpp, oldPath);
			if (tocopy == null)
				return;
			if (tocopy.length == 0)
				return;
			for (int i = 0; i < tocopy.length; i++) {
				String desti = lnkSysteme.getlnkControleurPresentation().getlnkPresentation().lnkBibliotheque.getAbsolutePath() + File.separator + tocopy[i].getName();
				boolean copi = true;
				if (new File(desti).exists()) {
					if (PogToolkit.askYesNoQuestion(fpp.getLnkLangues().valeurDe("questouinonremplacerfichier"), false, fpp) != PogToolkit._YES)
						copi = false;
				}
				if (copi)
					PogToolkit.copyFile(tocopy[i].getAbsolutePath(), desti);
			}
			oldPath = tocopy[0].getParent();
			lnkArbreExplorateur.load();
		}
  	});
	jLabelPathBib.setPreferredSize(new Dimension(200,20));
	jLabelPathBib.setMinimumSize(new Dimension(200,20));
	jLabelPathBib.setAutoscrolls(true);
	panelBiblio.add(jLabelPathBib, BorderLayout.NORTH);
	panelBiblio.add(btnChangerBib, BorderLayout.EAST);
	panelBiblio.add(badd, BorderLayout.CENTER);
	panelBiblio.add(brefr, BorderLayout.WEST);
	_panelExploBiblio.add(panelBiblio, BorderLayout.NORTH);
	_panelExploBiblio.add(jScrollPane1, BorderLayout.CENTER);

    jScrollPane1.getViewport().add(lnkArbreExplorateur.get_arbre(), null);

    jSplitPane3.add(_panelExploBiblio, JSplitPane.TOP);
    jSplitPane3.add(lnkPanneauBibliotheque, JSplitPane.BOTTOM);

    jScrollPane3.getViewport().add(lnkDebug.get_texte(), null);
    jSplitPane4.add(jScrollPane3, JSplitPane.BOTTOM);

	_panelArbrePresentation.setLayout(borderLayout6);

    JPanel panelOrganiser = new JPanel();
    JButton bDown = new JButton();
    bDown.setIcon(lnkPreferences.getIconeDefaut("down"));
    bDown.setToolTipText(lnkLangues.valeurDe("descendre"));
    JButton bUp = new JButton();
    bUp.setToolTipText(lnkLangues.valeurDe("monter"));
    bUp.setIcon(lnkPreferences.getIconeDefaut("up"));
    brefr = new JButton();
    brefr.setToolTipText(lnkLangues.valeurDe("reload"));
    brefr.setIcon(lnkPreferences.getIconeDefaut("process_tree_node_process"));
    panelOrganiser.add(brefr);
    panelOrganiser.add(bUp);
    panelOrganiser.add(bDown);

    brefr.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.load();
      }
    });

    bDown.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ElementPresentation e = lnkArbrePresentation.
            getListenerArbrePresentation().getElementPresentationSelectionne();
        if (e == null) {
          return;
        }
        lnkSysteme.descendre(e);
      }
    });

    bUp.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ElementPresentation e = lnkArbrePresentation.
            getListenerArbrePresentation().getElementPresentationSelectionne();
        if (e == null) {
          return;
        }
        lnkSysteme.monter(e);

      }
    });

	_panelArbrePresentation.add(panelOrganiser, BorderLayout.NORTH);
	_panelArbrePresentation.add(jScrollPane2, BorderLayout.CENTER);

    jScrollPane2.getViewport().add(lnkArbrePresentation.get_arbre(), null);

    jSplitPane1.add(jSplitPane2, JSplitPane.RIGHT);
    jSplitPane1.add(_panelArbrePresentation, JSplitPane.LEFT);

    jSplitPane2.add(jSplitPane4, JSplitPane.LEFT);
    jSplitPane2.add(jSplitPane3, JSplitPane.RIGHT);

    lnkControleurPanneaux.loadVide();
    jSplitPane4.add(new JPanel(), JSplitPane.TOP);

    this.setSize(new Dimension(950, 750));
    this.getContentPane().setLayout(borderLayout1);
    this.setJMenuBar(_menu);
    this.getContentPane().add(jSplitPane1, BorderLayout.CENTER);
    this.addWindowListener(new WindowAdapter() {

		public void windowClosing(WindowEvent arg0) {
			lnkSysteme.quitter();
		}
    	
    });
    this.setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
    this.getContentPane().add(lnkMainToolBar, BorderLayout.NORTH);

    setIconImage(lnkPreferences.getIconeDefaut("logo-NB").getImage());
  }

  public Langues getLnkLangues() {
    return lnkLangues;
  }

  public Systeme getLnkSysteme() {
    return lnkSysteme;
  }

  public MainToolBar getLnkMainToolBar() {
    return lnkMainToolBar;
  }

  public ArbreExplorateur getLnkArbreExplorateur() {
    return lnkArbreExplorateur;
  }

  public JFileChooser getLnkJIconChooser() {
  	if (lnkJIconChooser == null) {
		 lnkJIconChooser = new JFileChooser();
		
		//Add a custom file filter and disable the default
		//(Accept All) file filter.
		lnkJIconChooser.addChoosableFileFilter(new ImageFilter());
		lnkJIconChooser.setAcceptAllFileFilterUsed(false);
		
		//Add custom icons for file types.
		lnkJIconChooser.setFileView(new ImageFileView());
		
		//Add the preview pane.
		lnkJIconChooser.setAccessory(new ImagePreview(lnkJIconChooser));
	}
    return lnkJIconChooser;
  }

  public ArbrePresentation getLnkArbrePresentation() {
    return lnkArbrePresentation;
  }

  public POGMenu getLnkMenu() {
    return _menu;
  }

  public ControleurPanneaux getLnkControleurPanneaux() {
    return lnkControleurPanneaux;
  }

  public PanneauBibliotheque getLnkPanneauBibliotheque() {
    return lnkPanneauBibliotheque;
  }

  public FenetreNouvellePresentationSansModele getLnkFenetreOuverture() {
    if (lnkFenetreOuverture == null) {
      lnkFenetreOuverture = new FenetreNouvellePresentationSansModele(this);
    }
    return lnkFenetreOuverture;
  }

  public FenetreEnregistrerSous getLnkFenetreEnregistrerSous() {
    if (lnkFenetreEnregistrerSous == null) {
      lnkFenetreEnregistrerSous = new FenetreEnregistrerSous(this);
    }
    return lnkFenetreEnregistrerSous;
  }

  public FenetreElementLien getLnkFenetreElementLien() {
	if (lnkFenetreElementLien == null) {
		lnkFenetreElementLien = new FenetreElementLien(this);
	}
	return lnkFenetreElementLien;
  }

  public FenetreChangerIcone getLnkFenetreChangerIcone() {
	if (lnkFenetreChangerIcone == null) {
		lnkFenetreChangerIcone = new FenetreChangerIcone(this);
	}
	return lnkFenetreChangerIcone;
  }

  public FenetreNouvellePresentationAvecModele
      getLnkFenetreNouvellePresentationAvecModele() {
    if (lnkFenetreNouvellePresentationAvecModele == null) {
      lnkFenetreNouvellePresentationAvecModele = new
          FenetreNouvellePresentationAvecModele(this);
    }
    return lnkFenetreNouvellePresentationAvecModele;
  }

  public Debug getLnkDebug() {
    return lnkDebug;
  }

  public FenetreExport getLnkFenetreExport() {
    if (lnkFenetreExport == null) {
      lnkFenetreExport = new FenetreExport(this);
    }
    return lnkFenetreExport;
  }

  public void afficherFenetrePreferences()
  {
    this.lnkFenetrePreferences = new FenetrePreferences(this, this.getLnkSysteme().getLnkPreferences());
    this.lnkFenetrePreferences.setVisible(true);
  }

  public FenetreAPropos getLnkFenetreAPropos() {
    if(lnkFenetreAPropos == null)
      lnkFenetreAPropos = new FenetreAPropos(this);
    return lnkFenetreAPropos;
  }

  public appliTest getLnkappliTest() {
    if (lnkappliTest == null) {
      lnkappliTest = new appliTest(this);
    }
    return lnkappliTest;
  }

  public FenetrePrincipale get_fenetrePrincipale() {
    return _fenetrePrincipale;
  }

	public String get_pathSave() {
		return _pathSave;
	}
	
	
	public void set_pathSave(String save) {
		_pathSave = save;
	
	        if(save.equalsIgnoreCase(""))
	           this.setTitle(PogToolkit._APP_NAME);
	        else
	          this.setTitle(PogToolkit._APP_NAME+" : " + save);
	}
//*/

	public String get_pathExport() {
		return _pathExport;
	}
	
	public void set_pathExport(String export) {
		_pathExport = export;
	}
	

public JPanel get_panelArbrePresentation() {
	return _panelArbrePresentation;
}

public JPanel get_panelExploBiblio() {
	return _panelExploBiblio;
}

}
