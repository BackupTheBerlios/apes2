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
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import POG.utile.PogToolkit;

/**
 * @stereotype boundary
 */


public class FenetreNouvellePresentationAvecModele extends FenetrePOG {

  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel(); //nom de la presentation
  JPanel jPanel5 = new JPanel(); //biblio

  JLabel jLabel1 = new JLabel();
  JButton jButtonP = new JButton();
  JButton jButtonP2 = new JButton();
  JTextField jTextFieldNomModele = new JTextField();
  JLabel jLabel2 = new JLabel();
  JTextField jTextFieldBiblio = new JTextField();
  BorderLayout borderLayout1 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();

  public FenetreNouvellePresentationAvecModele(FenetrePrincipale fp) {
    super(fp);
    try {
      jbInit();
      this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("nouvellepresentationavecmodele"));
      PogToolkit.centerWindow(this);
    }
    catch (Exception e) {
      e.printStackTrace();
    }

  }

  public boolean verifParametres() {

    if (!PogToolkit.fileExists(this.jTextFieldNomModele.getText())) {
      PogToolkit.showErrorMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe(
          "errmsgmodeleinvalide"), this);
      return false;
    }
    else if (!PogToolkit.folderExists(this.jTextFieldBiblio.getText())) {
      PogToolkit.showErrorMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe(
          "errmsgbibliothequeinvalide"), this);
      return false;
    }
    return true;
  }

  public void actionAnnuler() {
    this.setVisible(false);
  }

  public void actionOK() {
    this.setVisible(false);
    if (this.verifParametres()) {
      lnkFenetrePrincipale.getLnkSysteme().nouvellePresentationAvecModele(this.jTextFieldBiblio.getText(), this.jTextFieldNomModele.getText());
// Traitement par thread => erreurs
//      lnkFenetrePrincipale.getLnkArbrePresentation().selectionnerRacine();
    }
  }

  private void jbInit() throws Exception {

    jLabel1.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("cheminmodel"));
    jLabel2.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("nombibliotheque"));

    jTextFieldBiblio.setPreferredSize(new Dimension(200, 20));
    jTextFieldBiblio.setText(this.lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathBiblio());

    jTextFieldNomModele.setPreferredSize(new Dimension(200, 20));

    jButtonP.setText("...");
    jButtonP.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent evt) {
        File dir = PogToolkit.chooseDirectory(lnkFenetrePrincipale);
        if (dir != null) {
          jTextFieldBiblio.setText(dir.getAbsolutePath());
        }
      }
    });

    jButtonP2.setText("...");
    jButtonP2.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent evt) {
        File mod = PogToolkit.chooseFileAPES(lnkFenetrePrincipale);
        if (mod != null) {
          jTextFieldNomModele.setText(mod.getAbsolutePath());
        }
      }
    });

    jButtonP.setPreferredSize(new Dimension(30, 25));
    jButtonP2.setPreferredSize(new Dimension(30, 25));

    gridLayout1.setRows(5);
    jPanel1.setLayout(gridLayout1);

    jPanel2.setLayout(new BorderLayout());

    jPanel2.add(jLabel1, BorderLayout.WEST);
    jPanel2.add(jTextFieldNomModele, BorderLayout.CENTER);
    jPanel2.add(jButtonP2, BorderLayout.EAST);

    jPanel5.setLayout(new BorderLayout());

    jPanel5.add(jLabel2, BorderLayout.WEST);
    jPanel5.add(jTextFieldBiblio, BorderLayout.CENTER);
    jPanel5.add(jButtonP, BorderLayout.EAST);

    jPanel1.add(new JPanel(), null);
    jPanel1.add(jPanel2, null);
    jPanel1.add(new JPanel(), null);
    jPanel1.add(jPanel5, null);
    jPanel1.add(new JPanel(), null);

    jPanel1.setSize(new Dimension(450, 175));
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    this.pack();
  }
}