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


package POG.interfaceGraphique.utile.menu;

import java.awt.Color;
import java.awt.Insets;

import javax.swing.JButton;
import javax.swing.JToolBar;

import POG.interfaceGraphique.fenetre.FenetrePrincipale;


/** Classe g\uFFFDrant la toolbar principale de l'application
 */
public class MainToolBar extends JToolBar 
{
  /** R\uFFFDf\uFFFDrence vers la fen\uFFFDtre parente (fen\uFFFDtre principale) */
  private FenetrePrincipale  lnkFenetrePrincipale = null;

  // Boutons de la barre d'outils
  public JButton _btnNew = null;
  public JButton _btnOpen = null;
  public JButton _btnSave = null;
  public JButton _btnCheckSPEMs = null;
  public JButton _btnExport = null;
  public JButton _btnHelp = null;
  public JButton _btnAbout = null;

  public MainToolBar(FenetrePrincipale mainWindow)
  {
    super("Standard", JToolBar.HORIZONTAL);

    final Color hotColor = new Color(209, 237, 235);

    lnkFenetrePrincipale = mainWindow;

    setMargin(new Insets(2, 2, 2, 2));
    setBorderPainted(false);

    _btnNew = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileNew"));
    _btnOpen = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileOpen"));
    _btnSave = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileSave"));
	_btnCheckSPEMs = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("ToolsValidate"));
    _btnExport = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("ToolsGenerate"));
	_btnHelp = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_help"));
    _btnAbout = new JButton(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_about"));
    
    
    _btnNew.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("AvecModele"));
	_btnOpen.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("Ouvrir"));
	_btnSave.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("Enregistrer"));
	_btnCheckSPEMs.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("VerifierCoherence"));
	_btnExport.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("Exporter"));
	_btnHelp.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("ManuelUtilisation"));
	_btnAbout.setActionCommand(lnkFenetrePrincipale.getLnkLangues().valeurDe("apropos"));


    //_btnNew.setToolTipText(MenuFichier._LABEL_NEW);
    _btnNew.setBorder(null);
    _btnNew.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    //_btnOpen.setToolTipText(MenuFichier._LABEL_OPEN);
    _btnOpen.setBorder(null);
    _btnOpen.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    //_btnSave.setToolTipText(MenuFichier._LABEL_SAVE);
    _btnSave.setBorder(null);
    _btnSave.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    //_btnCheckSPEMs.setToolTipText(MenuProcessus._LABEL_CHECK_SPEMS);
    _btnCheckSPEMs.setBorder(null);
    _btnCheckSPEMs.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    //_btnExport.setToolTipText(MenuProcessus._LABEL_EXPORT);
    _btnExport.setBorder(null);
    _btnExport.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    //_btnHelp.setToolTipText(MenuAide._LABEL_HELP);
    _btnHelp.setBorder(null);
    _btnHelp.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    //_btnAbout.setToolTipText(MenuAide._LABEL_ABOUT);
    _btnAbout.setBorder(null);
    _btnAbout.addActionListener(lnkFenetrePrincipale.getLnkMenu().lnkMenuListener);

    add(_btnNew);
    addSeparator();
    add(_btnOpen);
    addSeparator();
    add(_btnSave);
    addSeparator();
    addSeparator();
    add(_btnCheckSPEMs);
    addSeparator();
    add(_btnExport);
    addSeparator();
    addSeparator();
    add(_btnHelp);
    addSeparator();
    add(_btnAbout);
    addSeparator();
  }

  /** Active ou d\uFFFDsactive tous les boutons relatifs au processus courant
   * @param state  <B>true</B> pour activer ces boutons, ou <B>false</B> pour les d\uFFFDsactiver
   */
  public void setButtonsState(boolean state)
  {
    _btnSave.setEnabled(state);
//    _btnCheckSPEMs.setEnabled(state);
//    _btnExport.setEnabled(state);
  }

}
