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
import java.awt.HeadlessException;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import POG.utile.MyMultiFileFilter;
import POG.utile.PogToolkit;
import POG.utile.propriete.Preferences;

public class FenetreEnregistrerSous extends FenetrePOG   {

  JPanel jPanel9 = new JPanel();
  BorderLayout borderLayout5 = new BorderLayout();
  JLabel jLabel1 = new JLabel();
  JTextField jTextField1 = new JTextField();
  JButton jButton3 = new JButton();
  boolean _valide = true;
  private FenetreVerification lnkFenetreListBadFiles;

  public FenetreEnregistrerSous(FenetrePrincipale fp) throws HeadlessException {
    super(fp);
    try {
      jbInit();
      PogToolkit.centerWindow(this);
      jTextField1.setText(fp.get_pathSave());
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    jPanel9.setLayout(null);

    jLabel1.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Destination"));
    jLabel1.setBounds(new Rectangle(7, 25, 93, 25));
    jLabel1.setPreferredSize(new Dimension(100, 50));

    jButton3.setBounds(new Rectangle(374, 25, 44, 25));
    jButton3.setPreferredSize(new Dimension(50, 50));
    jButton3.setText("...");
    jButton3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String nomFile = jTextField1.getText();
        File fil = PogToolkit.chooseFileToSave(lnkFenetrePrincipale, new MyMultiFileFilter(".pog"), nomFile);
        if (fil == null)
          return;
        String flm = fil.getAbsolutePath();
        if (!flm.endsWith(".pog"))
          flm = flm + ".pog";
        jTextField1.setText(flm);
      }
    });

    jTextField1.setPreferredSize(new Dimension(300, 50));
    jTextField1.setBounds(new Rectangle(96, 25, 266, 25));

    jPanel9.add(jLabel1, null);
    jPanel9.add(jTextField1, null);
    jPanel9.add(jButton3, null);

    this.getContentPane().add(jPanel9, null);
    this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("EnregistrerSous"));
    this.setSize(new Dimension(450, 125));
  }

  public void actionOK() {
    //Si la sauvegarde s'est bien passee
    int rep = PogToolkit._YES;

    //Si le fichier n'existe pas
    if (!PogToolkit.fileExists(jTextField1.getText())){
      try {
        (new File(jTextField1.getText())).createNewFile();
      }
      catch (Exception e) {
        e.printStackTrace();
        return;
      }
    }
    //Si l'archive existe
    else {
      //On pose la question si oui ou non on ecrase l'ancien xml
      rep = PogToolkit.askYesNoQuestion(this.lnkFenetrePrincipale.getLnkLangues().valeurDe("QuestOuiNonEcraseAncienneSauvegarde"), false, this);
    }

    //Si on a l'autorisation d'ecraser l'ancien xml
    if(rep == PogToolkit._YES){
    	lnkFenetrePrincipale.set_pathSave(jTextField1.getText());
      	lnkFenetrePrincipale.getLnkSysteme().enregistrerPresentation();
        //on modifie le drapeau indiquant que l'on a deja fait
        //une sauvegarde de la Presentation
      }
      this.setVisible(false);
  }

  public void actionAnnuler() {
    this.setVisible(false);
  }
  
	public void setVisible(boolean arg0) {
		String nomficpog = lnkFenetrePrincipale.get_pathSave();
		Preferences pref = lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences();
		if (nomficpog.equals("")) {
			if (pref.get_utiliseCheminModele() && (lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().get_pathModele() != null)) {
				nomficpog = lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().get_pathModele().getAbsolutePath();
				if (nomficpog.indexOf('.') != -1)
					nomficpog = nomficpog.substring(0, nomficpog.lastIndexOf('.'));
				nomficpog = nomficpog + ".pog";
			}
			else {
				nomficpog = pref.get_pathPog() + File.separator + lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().get_nomPresentation() + ".pog";
			}
		}
		jTextField1.setText(nomficpog);
		super.setVisible(arg0);
	}

}
