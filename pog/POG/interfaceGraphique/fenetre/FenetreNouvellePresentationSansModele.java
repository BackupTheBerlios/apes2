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
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import POG.utile.PogToolkit;


/**
 * <p>Titre : POG</p>
 * <p>Description : Presentation Organisation Generation</p>
 * <p>Copyright: </p>
 * <p>Company: IUP ISI</p>
 * @author non attribuable
 * @version 1.0
 */

public class FenetreNouvellePresentationSansModele extends FenetrePOG {

  JPanel jPanel1 = new JPanel();
  JPanel jPanel2 = new JPanel(); //nom de la presentation
  JPanel jPanel4 = new JPanel(); //nom de la biblio
  JPanel jPanel5 = new JPanel(); //biblio
  JPanel jPanel6 = new JPanel(); //bouton
  JLabel jLabel1 = new JLabel();
  JButton jButtonP = new JButton();
  JTextField jTextFieldNomPres = new JTextField();
  JLabel jLabel2 = new JLabel();
  JTextField jTextFieldBiblio = new JTextField();
  Box box1 = Box.createVerticalBox();
  Box box2 = Box.createVerticalBox();
  Box box3 = Box.createVerticalBox();
  BorderLayout borderLayout1 = new BorderLayout();
  GridLayout gridLayout1 = new GridLayout();
  public FenetreNouvellePresentationSansModele(FenetrePrincipale fp) {
    super(fp);
    this.setResizable(false);
    try {
      jbInit();
      this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("nouvellepresentationsansmodele"));
      jTextFieldBiblio.setText(this.lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathBiblio());
      PogToolkit.centerWindow(this);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  public void actionOK() {
    if (this.verifParametres())
    {
      lnkFenetrePrincipale.getLnkSysteme().nouvellePresentationSansModele(
          new File(this.jTextFieldBiblio.getText()), this.jTextFieldNomPres.getText());
      this.setVisible(false);
      lnkFenetrePrincipale.getLnkArbrePresentation().selectionnerRacine();
    }
  };

  public void actionAnnuler() {
    this.setVisible(false);
  };

  private boolean verifParametres() {
    if (this.jTextFieldNomPres.getText().length() <= 0)
    {
      PogToolkit.showErrorMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("errmsgnompresentationvide"), this);
      return false;
    }
    else if (!PogToolkit.folderExists(this.jTextFieldBiblio.getText()))
    {
      PogToolkit.showErrorMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("errmsgbibliothequeinvalide"), this);
      return false;
    }
    return true;
  }

  private void jbInit() throws Exception {
    jLabel1.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("NomPresentation"));
    jLabel2.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("NomBibliotheque"));
    jPanel1.setLayout(gridLayout1);
    gridLayout1.setRows(2);
    jTextFieldNomPres.setColumns(30);
    jTextFieldBiblio.setMinimumSize(new Dimension(200,20));
    jTextFieldBiblio.setMaximumSize(new Dimension(200, 20));
    this.getContentPane().add(jPanel1, BorderLayout.CENTER);
    jPanel1.add(jPanel2, null);
    jPanel2.setLayout(new BorderLayout());
    jPanel2.add(jLabel1, BorderLayout.WEST);
    box3.add(Box.createRigidArea(new Dimension(0, 20)));
    box3.add(jTextFieldNomPres);
    box3.add(Box.createRigidArea(new Dimension(0, 20)));
    jPanel2.add(box3, BorderLayout.CENTER);
    jPanel5.setLayout(new BorderLayout());
    jPanel1.add(jPanel5, null);
    jPanel5.add(jLabel2, BorderLayout.WEST);
    box1.add(Box.createRigidArea(new Dimension(0, 20)));
    box1.add(jTextFieldBiblio);
    box1.add(Box.createRigidArea(new Dimension(0, 20)));
    jPanel5.add(box1, BorderLayout.CENTER);
    Dimension dimension = new Dimension(0, 15);
    box2.add(Box.createRigidArea(dimension));
    jButtonP.setText("...");
    jButtonP.addActionListener(new ActionListener() {

      public void actionPerformed(ActionEvent evt) {
        File dir = PogToolkit.chooseDirectory(lnkFenetrePrincipale.getLnkFenetreExport());
        if (dir != null){
          jTextFieldBiblio.setText(dir.getAbsolutePath());
        }
      }
    });

    jButtonP.setPreferredSize(new Dimension(30, 25));
    jPanel6.add(jButtonP);
    box2.add(jPanel6,null);
    box2.add(Box.createRigidArea(dimension));
    jPanel5.add(box2,BorderLayout.EAST);
    this.setSize(new Dimension(400, 175));

  }
}
