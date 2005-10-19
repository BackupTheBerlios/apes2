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
import java.awt.GridLayout;
import java.awt.HeadlessException;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BoxLayout;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
//import java.awt.Image;
//import java.awt.Toolkit;



/**
 * <p>Title: POG</p>
 * <p>Description: Presentation Organisation Generation de composant</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public abstract class FenetrePOG extends JDialog {

  public JButton _jButtonOK = new JButton();
  public JButton _jButtonCancel = new JButton();

  /**
   * @supplierCardinality 1
   * @directed
   */
  protected FenetrePrincipale lnkFenetrePrincipale;

  public FenetrePOG(FenetrePrincipale fp) throws HeadlessException {
    super(fp, true);
    try {
      lnkFenetrePrincipale = fp;
      jbInit();
      setModal(true);
    }
    catch(Exception e) {
      e.printStackTrace();
    }
  }
  private void jbInit() throws Exception {
      this.setResizable(false);
    _jButtonOK.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("OK"));
    _jButtonOK.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          actionOK();
        }
    });

    _jButtonCancel.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Cancel"));
    _jButtonCancel.addActionListener(new ActionListener() {
        public void actionPerformed(ActionEvent e) {
          actionAnnuler();
        }
    });


    GridLayout gridLayout = new GridLayout(1,2);
    gridLayout.setHgap(15);
    JPanel panneauBouton = new JPanel(gridLayout);
    panneauBouton.add(_jButtonOK);
    panneauBouton.add(_jButtonCancel);

    _jButtonCancel.setSelected(false);

    JPanel affichage = new JPanel() ;
    affichage.add(panneauBouton);
    new BoxLayout(affichage, BoxLayout.X_AXIS) ;
    this.getContentPane().add(affichage, BorderLayout.SOUTH);
  }

  abstract void actionOK();
  abstract void actionAnnuler();
}