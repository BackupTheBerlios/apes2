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
public class FenetreAPropos extends JDialog implements ActionListener
{
  /** Bouton OK */
  private JButton _okButton = null;

  /**
   * @directed
   * @supplierCardinality 1
   */
  private FenetrePrincipale lnkFenetrePrincipale;

  /** Constructeur par d?faut
   * @param mainWindow  R?f?rence vers la fen?tre parente (fen?tre principale)
   */
  public FenetreAPropos(FenetrePrincipale lnkFenetrePrincipale)
  {
    super(lnkFenetrePrincipale, lnkFenetrePrincipale.getLnkLangues().valeurDe("titreAPropos"), false);

    _okButton = new JButton(lnkFenetrePrincipale.getLnkLangues().valeurDe("OK"));

    JPanel southPanel = new JPanel(new FlowLayout(FlowLayout.CENTER));
    JPanel centerPanel = new JPanel(new GridLayout(10, 1));
    JLabel lbl0 = new JLabel(PogToolkit._APP_NAME, SwingConstants.CENTER);
    JLabel lbl1 = new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("ligne1APropos"), SwingConstants.CENTER);
    JLabel lbl2 = new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("ligne2APropos"), SwingConstants.CENTER);

    // Boutton par d?faut : bouton "OK" (=> touche ENTER = bouton OK)
    _okButton.setDefaultCapable(true);
    getRootPane().setDefaultButton(_okButton);

    _okButton.addActionListener(this);
    lbl0.setForeground(Color.red);

    southPanel.add(_okButton);

    centerPanel.setLayout(new GridLayout(14, 1));
    centerPanel.add(lbl0);
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(lbl1);
    centerPanel.add(lbl2);
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("dateAPropos"), SwingConstants.CENTER));
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel(lnkFenetrePrincipale.getLnkLangues().valeurDe("groupeAPropos"), SwingConstants.CENTER));
    centerPanel.add(new JLabel(" ", SwingConstants.CENTER));
    centerPanel.add(new JLabel("Ce programme est un logiciel libre.", SwingConstants.CENTER));
    centerPanel.add(new JLabel("Vous pouvez le redistribuer et/ou le modifier", SwingConstants.CENTER));
    centerPanel.add(new JLabel("selon les termes de la GNU General Public License", SwingConstants.CENTER));
    centerPanel.add(new JLabel("telle que publiée par la Free Software Foundation,", SwingConstants.CENTER));
    centerPanel.add(new JLabel("à la version 2 de la license, ou ultérieur.", SwingConstants.CENTER));

    getContentPane().setLayout(new BorderLayout(4, 4));
    getContentPane().add(southPanel, BorderLayout.SOUTH);
    getContentPane().add(centerPanel, BorderLayout.CENTER);
    getContentPane().add(new JLabel(lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getIconeDefaut("logo-NB")), BorderLayout.WEST);

    //this.setSize(new Dimension(500, 500));
    setResizable(false);
    pack();
    PogToolkit.centerWindow(this);
    setVisible(true);
  }

  public void actionPerformed(ActionEvent e)
  {
    if (e.getSource() == _okButton)
      dispose();
  }

}
