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
import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;

import POG.utile.PogToolkit;

/** Classe g?rant la fen?tre "A propos de..."
 */
public class FenetreChargement extends JDialog
{
   JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
   JPanel centerPanel = new JPanel(new GridLayout(10, 1));
   FenetrePrincipale lnkFenetrePrincipale;

  public FenetreChargement(FenetrePrincipale fp) {
    super(fp, "POG", false);
    lnkFenetrePrincipale = fp;
    jbinit();
    JButton  _okButton = new JButton("OK");
    // Boutton par d?faut : bouton "OK" (=> touche ENTER = bouton OK)
     _okButton.setDefaultCapable(true);
     getRootPane().setDefaultButton(_okButton);

     _okButton.addActionListener(new ActionListener() {
       public void actionPerformed(ActionEvent evt) {
         dispose();
       }
     });
     southPanel.add(_okButton);
  }

private void jbinit() {

    JLabel lbl0 = new JLabel(PogToolkit._APP_NAME, SwingConstants.CENTER);
    JLabel lbl1 = new JLabel("Presentation Organisation", SwingConstants.CENTER);
    JLabel lbl2 = new JLabel("Generation", SwingConstants.CENTER);

    lbl0.setForeground(Color.red);

    centerPanel.add(lbl0);
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(lbl1);
    centerPanel.add(lbl2);
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel("IUP ISI, Octobre 2003 ~ Mars 2004", SwingConstants.CENTER));
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel("par ISI2A et ISI3A", SwingConstants.CENTER));

    getContentPane().setLayout(new BorderLayout(4, 4));
    getContentPane().add(southPanel, BorderLayout.SOUTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    getContentPane().add(new JLabel(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("logo-NB")), BorderLayout.WEST);

    setResizable(false);
    pack();
    PogToolkit.centerWindow(this);

  }

}
