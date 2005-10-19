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


public class FenetreExport extends FenetrePOG  {

  JPanel jPanel9 = new JPanel();

  JLabel jLabel1 = new JLabel();
  JTextField jTextField1 = new JTextField();

  JButton jButton3 = new JButton();
  boolean _valide = true;


  /**
   * @directed
   * @supplierCardinality 1
   */
  private FenetreVerification lnkFenetreListBadFiles;

  public FenetreExport(FenetrePrincipale fp) throws HeadlessException {
    super(fp);

    try {
      jbInit();
      PogToolkit.centerWindow(this);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }



  private void jbInit() throws Exception {
    jPanel9.setLayout(null);

    jLabel1.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("Destination"));
    jLabel1.setBounds(new Rectangle(7, 25, 93, 25));

    jButton3.setBounds(new Rectangle(374, 25, 44, 25));
    jButton3.setPreferredSize(new Dimension(50, 50));
    jButton3.setText("...");
    jButton3.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        String nomFile = jTextField1.getText();
        File dir = PogToolkit.chooseFileToSave(lnkFenetrePrincipale.getLnkFenetreExport(), new MyMultiFileFilter(".pre"), nomFile);
        if (dir != null){
          if (!dir.getName().endsWith(".pre"))
            jTextField1.setText(dir.getAbsolutePath()+".pre");
          else
            jTextField1.setText(dir.getAbsolutePath());
        }
      }
    });

    jLabel1.setPreferredSize(new Dimension(100, 50));
    jTextField1.setPreferredSize(new Dimension(300, 50));
    jTextField1.setBounds(new Rectangle(96, 25, 266, 25));

    jPanel9.add(jLabel1, null);
    jPanel9.add(jTextField1, null);
    jPanel9.add(jButton3, null);

    this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("ExporterPresentation"));

    this.getContentPane().add(jPanel9, null);

    this.setSize(new Dimension(450, 125));
  }

  public void actionOK() {
    //Si l'exportation c'est bien passee
    int rep = PogToolkit._YES;

    //Si l'archive existe
    if (PogToolkit.fileExists(jTextField1.getText()))
      //On pose la question si oui ou non on ecras l'ancienne archive
      rep = PogToolkit.askYesNoQuestion(this.lnkFenetrePrincipale.getLnkLangues().valeurDe("QuestOuiNonEcraseAncienneArch"), false, this);
	if (rep == PogToolkit._NO)
		return;
	lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().set_pathExport(new File(jTextField1.getText()));
	super.setVisible(false);
	lnkFenetrePrincipale.getLnkSysteme().exporterPresentation();
  }

  public void actionAnnuler() {
    this.setVisible(false);
  }

	public void setVisible(boolean arg0) {
		File pathexp = lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().get_pathExport();
		String chemaff = "";
		if (pathexp == null) {
			String nomficsave = lnkFenetrePrincipale.get_pathSave();
			if (!nomficsave.equals(""))
				nomficsave = nomficsave.substring(nomficsave.lastIndexOf(File.separator) + 1, nomficsave.lastIndexOf("."));
			if (!nomficsave.equals(""))
				chemaff = lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathPre() + File.separator + nomficsave + ".pre";
			else
				chemaff = lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathPre() + File.separator + lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().get_nomPresentation() + ".pre";
		}
		else
			chemaff = pathexp.getAbsolutePath();
		jTextField1.setText(chemaff);
		super.setVisible(arg0);
	}

}
