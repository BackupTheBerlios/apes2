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

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.Iterator;

import javax.swing.BorderFactory;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;

import POG.interfaceGraphique.action.ControleurPanneaux;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;
import POG.objetMetier.PresentationElementModele;

public class PanneauElementPresentation extends PanneauDetail {
  JScrollPane scrollpaneGuides;
  JTable list_guides = new JTable();

  public PanneauElementPresentation(ControleurPanneaux control) {
    super(control);
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    list_guides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

    JPanel jPanel = new JPanel(new BorderLayout());
    jPanel.setBounds(new Rectangle(11, 275, 400, 118));
    list_guides.setModel(new TableGuideModel());
    list_guides.addMouseListener(this.navigationMouseListener);
    list_guides.getColumnModel().getColumn(0).setPreferredWidth(10);
    list_guides.getColumnModel().getColumn(2).setPreferredWidth(10);
    jPanel.add(list_guides.getTableHeader(), BorderLayout.NORTH);
    //jPanel.add(list_guides, BorderLayout.CENTER);
    scrollpaneGuides = new JScrollPane();//list_guides);
    jPanel.add(scrollpaneGuides, BorderLayout.CENTER);
    scrollpaneGuides.setBounds(new Rectangle(11, 370, 400, 250));
    scrollpaneGuides.setBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(FenetrePrincipale.langue("guideassocies")),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
        scrollpaneGuides.getBorder()));
    scrollpaneGuides.setViewportView(list_guides);

    KeyListener listenerJtableGuides = new KeyAdapter() {
    public void keyPressed(KeyEvent e) {
      e.consume();
        if (e.getKeyCode() == KeyEvent.VK_DELETE){
          Guide g = getGuideSelectionne();
          if (g != null)
            lnkControleurPanneaux.getLnkSysteme().supprimerElement(g);
        }
    }
  };

  list_guides.addKeyListener(listenerJtableGuides);
    this.add(jPanel, null);
  }

  Guide getGuideSelectionne(){
    Guide g = ((TableGuideModel)this.list_guides.getModel()).getGuide(
        this.list_guides.getSelectionModel().getMinSelectionIndex());
    return g;
  }

  public void afficherMenuGuides(Component compo, int x, int y) {
    JPopupMenu popup = new JPopupMenu();
    popup.setBackground(java.awt.Color.CYAN);
	Iterator vTypes = this.lnkControleurPanneaux.getLnkSysteme().getLnkControleurGuide().types();
	while (vTypes.hasNext()) {
	  String value = (String) vTypes.next();
      JMenuItem choixEnPlus = new JMenuItem(value);
      listenerSousMenusGuides lis = new listenerSousMenusGuides(value, this, this.lnkControleurPanneaux);
      choixEnPlus.addActionListener(lis);
      popup.add(choixEnPlus);
    }
    popup.show(compo, x, x);
  }

  public void loadElement(ElementPresentation elem) {
    _elementCourant = elem;
    // Cas des paquetage :
    if (elem instanceof PresentationElementModele) 
    	setNom_modele(((PresentationElementModele)elem).getLnkModelElement().getName());
    setLabelNomPres(FenetrePrincipale.langue("nomElemPres"));
    setNom_Pres(elem.get_nomPresentation());
    setDesc(elem.get_description());
    if (elem.getContenu() != null)
      setFichier_associe(elem.getContenu());
    else
      setFichier_associe(null);
    setTable_guide(lnkControleurPanneaux.getLnkSysteme().getGuides(_elementCourant));
    setIcon(elem.get_icone());
    this.updateUI();
  }

  public void setTable_guide(Object[] list) {
    TableGuideModel model = new TableGuideModel(list);
    model.fireTableRowsInserted(0,model.getRowCount()-1);
    list_guides.setModel(model);
    scrollpaneGuides.setViewportView(list_guides);
  }

}
