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

package POG.interfaceGraphique.utile.menu;

import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;

import POG.interfaceGraphique.fenetre.FenetrePrincipale;

public class POGMenu extends JMenuBar {

  /**
   * @directed
   */

  FenetrePrincipale lnkFenetrePrincipale;

  /**
   * @directed
   */
  MenuListener lnkMenuListener;

  private JMenu jMenu1 = new JMenu();
  private JMenu jMenu2 = new JMenu();
  private JMenu jMenu3 = new JMenu();
  private JMenu jMenu4 = new JMenu();
  private JMenu jMenu6 = new JMenu();
  private JMenu jMenu8 = new JMenu();
  private JMenuItem jMenuNouveau = new JMenu();
  private JMenuItem jMenuNouveauSansModele = new JMenuItem();
  private JMenuItem jMenuNouveauAvecModele = new JMenuItem();
  private JMenuItem jMenuOuvrir = new JMenuItem();
  private JMenuItem jMenuEnregistrerSous = new JMenuItem();
  private JMenuItem jMenuEnregistrer = new JMenuItem();
  private JMenuItem jMenuApes = new JMenuItem();
  private JMenuItem jMenusyncApes = new JMenuItem();
  private JMenuItem jMenuQuitter = new JMenuItem();
  private JMenuItem jMenuUndo = new JMenuItem();
  private JMenuItem jMenuRedo = new JMenuItem();
  private JMenuItem jMenuAjouterElementPre = new JMenuItem();
  private JMenuItem jMenuSynchroniser = new JMenuItem();
  private JMenuItem jMenuExtraire = new JMenuItem();
  private JMenuItem jMenuLien = new JMenuItem();
  private JMenuItem jMenuchangeico = new JMenuItem();
  private JMenuItem jMenuExporter = new JMenuItem();
  private JMenuItem jMenuExporterIEPP = new JMenuItem();
  private JMenuItem jMenuVerifCoherence = new JMenuItem();
  private JMenuItem jMenuEditHTML = new JMenuItem();
  private JMenuItem jMenuEditIcone = new JMenuItem();
  private JMenuItem jMenuExplo = new JMenuItem();
  private JMenuItem jMenuViderLog = new JMenuItem();
  private JMenuItem jMenuPrefs = new JMenuItem();
  private JMenuItem jMenuManuUtil = new JMenuItem();
  private JMenuItem jMenuAPropos = new JMenuItem();

  public POGMenu(FenetrePrincipale fp) {
    super();
    lnkFenetrePrincipale = fp;
    lnkMenuListener = new MenuListener(this);
    jbInit();
  }

  private void jbInit(){
    jMenu1.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Fichier"));

    jMenuNouveau.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Nouveau"));
    jMenuNouveau.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileNew"));

    jMenuNouveauSansModele.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("SansModele"));
    jMenuNouveauSansModele.addActionListener(lnkMenuListener);
    jMenuNouveauSansModele.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileNew"));

    jMenuNouveauAvecModele.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("AvecModele"));
    jMenuNouveauAvecModele.addActionListener(lnkMenuListener);
    jMenuNouveauAvecModele.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileNew"));

    jMenuOuvrir.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Ouvrir"));
    jMenuOuvrir.addActionListener(lnkMenuListener);
    jMenuOuvrir.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileOpen"));

    jMenuEnregistrer.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Enregistrer"));
    jMenuEnregistrer.addActionListener(lnkMenuListener);
    jMenuEnregistrer.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileSave"));

    jMenuEnregistrerSous.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("enregistrersous"));
    jMenuEnregistrerSous.addActionListener(lnkMenuListener);
    jMenuEnregistrerSous.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileSaveAs"));

    jMenuApes.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("apes"));
    jMenuApes.addActionListener(lnkMenuListener);
    jMenuApes.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("Empty"));//*/


    jMenusyncApes.setText(this.lnkFenetrePrincipale.getLnkLangues().valeurDe("syncapes"));
    jMenusyncApes.addActionListener(lnkMenuListener);
    jMenusyncApes.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("synchroniser"));

    jMenuQuitter.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Quitter"));
    jMenuQuitter.addActionListener(lnkMenuListener);
    jMenuQuitter.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileQuit"));


    jMenu2.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Edition"));

    /*jMenuUndo.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Undo"));
    jMenuUndo.addActionListener(lnkMenuListener);
    jMenuUndo.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditUndo"));

    jMenuRedo.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Redo"));
    jMenuRedo.addActionListener(lnkMenuListener);
    jMenuRedo.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditRedo"));*/

    jMenuAjouterElementPre.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("AjouterElementPresentation"));
    jMenuAjouterElementPre.addActionListener(lnkMenuListener);
    jMenuAjouterElementPre.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_add_artefact"));

	jMenuLien.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("lienfic"));
	jMenuLien.addActionListener(lnkMenuListener);

	jMenuchangeico.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("changertoutesicones"));
	jMenuchangeico.addActionListener(lnkMenuListener);

    jMenuSynchroniser.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("synchroniser"));
    jMenuSynchroniser.addActionListener(lnkMenuListener);
    jMenuSynchroniser.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("process_tree_node_process"));

    jMenuExtraire.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("extraire"));
    jMenuExtraire.addActionListener(lnkMenuListener);
    jMenuExtraire.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("extractIcon"));


    jMenuViderLog.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("viderlog"));
    jMenuViderLog.addActionListener(lnkMenuListener);
    jMenuViderLog.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("Empty"));


    jMenuPrefs.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Preferences"));
    jMenuPrefs.addActionListener(lnkMenuListener);
    jMenuPrefs.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_properties"));


    jMenu4.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Exportation"));

    jMenuExporter.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Exporter"));
    jMenuExporter.addActionListener(lnkMenuListener);
    jMenuExporter.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("ToolsGenerate"));

    /*jMenuExporterIEPP.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("ExporterIEPP"));
    jMenuExporterIEPP.addActionListener(lnkMenuListener);
    jMenuExporterIEPP.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("Empty"));*/

    jMenuVerifCoherence.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("VerifierCoherence"));
    jMenuVerifCoherence.addActionListener(lnkMenuListener);
    jMenuVerifCoherence.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("ToolsValidate"));


    jMenu6.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Outils"));

    jMenuEditHTML.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("EditeurHTML"));
    jMenuEditHTML.addActionListener(lnkMenuListener);
    jMenuEditHTML.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("pika"));

/*    jMenuEditIcone.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("EditeurIcone"));
    jMenuEditIcone.addActionListener(lnkMenuListener);
    jMenuEditIcone.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("JDrawIcon"));
*/

    jMenu3.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Aides"));

    jMenuManuUtil.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("ManuelUtilisation"));
    jMenuManuUtil.addActionListener(lnkMenuListener);
    jMenuManuUtil.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_help"));

    jMenuAPropos.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("apropos"));
    jMenuAPropos.addActionListener(lnkMenuListener);
    jMenuAPropos.setIcon(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_about"));


    this.add(jMenu1);
    this.add(jMenu2);
    this.add(jMenu4);
    this.add(jMenu6);
    this.add(jMenu8);
    this.add(jMenu3);
    jMenu8.setVisible(false);
    jMenu1.add(jMenuNouveau);
    jMenuNouveau.add(jMenuNouveauSansModele);
    jMenuNouveau.add(jMenuNouveauAvecModele);
    jMenu1.add(jMenuOuvrir);
    jMenu1.add(jMenuEnregistrer);
    jMenu1.add(jMenuEnregistrerSous);
    jMenu1.addSeparator();
    jMenu1.add(jMenuApes);
    jMenu1.add(jMenusyncApes);
    jMenu1.add(jMenuQuitter);

    //jMenu2.add(jMenuUndo);
    //jMenu2.add(jMenuRedo);
    //jMenu2.addSeparator();
    jMenu2.add(jMenuAjouterElementPre);
    jMenu2.add(jMenuSynchroniser);
    jMenu2.add(jMenuExtraire);
	jMenu2.add(jMenuLien);
	jMenu2.add(jMenuchangeico);
    jMenu2.addSeparator();
    jMenu2.add(jMenuViderLog);
    jMenu2.addSeparator();
    jMenu2.add(jMenuPrefs);

    jMenu3.add(jMenuManuUtil);
    jMenu3.add(jMenuAPropos);

    jMenu4.add(jMenuExporter);
    //jMenu4.add(jMenuExporterIEPP);
    jMenu4.add(jMenuVerifCoherence);

    jMenu6.add(jMenuEditHTML);
  //  jMenu6.add(jMenuEditIcone);

    this.setBorder(null);
    this.setBorderPainted(true);
  }

  public JMenu getMenuCentral() {
    return jMenu8;
  }

}
