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


package POG.interfaceGraphique.fenetre.PanneauxDetails;

import javax.swing.JPanel;
import javax.swing.JLabel;
import java.awt.Dimension;

import POG.utile.propriete.Preferences;

public class PanneauVide extends JPanel {

  JLabel jLabel1 = new JLabel();

  public PanneauVide() {
    super();
    this.setLayout(null);
//    jLabel1.setText("");
    jLabel1.setIcon(Preferences.MyInstance.getIconeDefaut("logo-NB"));
    jLabel1.setBounds(103, 113, 150, 70);

    this.setPreferredSize(new Dimension(425, 540));
    this.setMinimumSize(new Dimension(200, 200));
    this.add(jLabel1);
    this.setVisible(false);
  }

}