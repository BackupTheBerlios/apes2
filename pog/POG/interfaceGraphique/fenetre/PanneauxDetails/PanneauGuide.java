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
package POG.interfaceGraphique.fenetre.PanneauxDetails;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JScrollPane;

import POG.interfaceGraphique.action.ControleurPanneaux;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;

public class PanneauGuide extends PanneauDetail {
  private JScrollPane jScrollPane1;
  private JComboBox list_type = new JComboBox();

  public PanneauGuide(ControleurPanneaux control) {
    super(control);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
     setList_type((String[]) control.getLnkSysteme().getLnkPreferences().get_guide().keySet().toArray(new String[0]));
  }

  private void jbInit() throws Exception {
    btnajouterguide.setVisible(false);
    jScrollPane1 = new JScrollPane();
    jScrollPane1.setBounds(new Rectangle(11, 265, 250, 64));
    jScrollPane1.setBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(FenetrePrincipale.langue("typeguide")),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
        jScrollPane1.getBorder()));
    this.add(list_type);
    this.add(jScrollPane1, null);
  }

  public void afficherMenuGuides(Component compo, int x, int y) {
  }

  public void loadElement(ElementPresentation elem) {
    _elementCourant = elem;
    setNom_Pres(elem.get_nomPresentation());
    setLabelNomPres(FenetrePrincipale.langue("nomGuide"));
    setDesc(elem.get_description());
    setIcon(elem.get_icone());
    if (elem.getContenu() != null)
      setFichier_associe(elem.getContenu());
    else
      setFichier_associe(null);
    setIcon(elem.get_icone());
	list_type.setSelectedItem(((Guide)elem).getType());
    this.updateUI();

  }

  private void setList_type(String[] list) {
    list_type = new JComboBox(list);
    jScrollPane1.setViewportView(list_type);
	list_type.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			String type = (String)cb.getSelectedItem();
			if (!type.equals(((Guide)_elementCourant).getType()))
				lnkControleurPanneaux.getLnkSysteme().changerTypeGuide(_elementCourant, type);
		}
	});
  }
}

