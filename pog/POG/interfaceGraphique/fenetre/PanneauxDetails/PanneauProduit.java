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
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.BorderFactory;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;

import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;

import POG.interfaceGraphique.action.ControleurPanneaux;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;
import POG.objetMetier.PresentationElementModele;


public class PanneauProduit extends PanneauDetail {
  JLabel jLabel5 = new JLabel();
  JLabel jLabel51 = new JLabel();
  JLabel jLabel52 = new JLabel();
  JScrollPane scrollpaneGuides;
  JScrollPane jScrollPane51 = new JScrollPane();
  JScrollPane jScrollPane52 = new JScrollPane();
  JList list_inactivite = new JList();
  JList list_outactivite = new JList();
  JLabel jLabel7 = new JLabel();
  JTable list_guides = new JTable();
  JComboBox typeProduit;
  private String [] lestypes;

  public PanneauProduit(ControleurPanneaux control) {
    super(control);
    HashMap types = lnkControleurPanneaux.getLnkSysteme().getLnkPreferences().get_produit();
    lestypes = new String[types.size()];
    int i = 0;
	for(Iterator it = types.entrySet().iterator(); it.hasNext();)
	{
	  Map.Entry entry = (Map.Entry)it.next();
	  lestypes[i++] = (String)entry.getKey();
	}
    try {
      jbInit();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void jbInit() throws Exception {
    list_guides.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

	typeProduit = new JComboBox(lestypes);
	typeProduit.addActionListener(new ActionListener() {
		public void actionPerformed(ActionEvent e) {
			JComboBox cb = (JComboBox)e.getSource();
			String type = (String)cb.getSelectedItem();
			if (!type.equals(((PresentationElementModele)_elementCourant).get_typeProduit()))
				lnkControleurPanneaux.getLnkSysteme().changerTypeProduit(_elementCourant, type);
		}
	});
	typeProduit.setBounds(new Rectangle(12, 262, 195, 55));
	typeProduit.setBorder(
			BorderFactory.createCompoundBorder(
			BorderFactory.createCompoundBorder(
			BorderFactory.createTitledBorder("Type Produit"),
			BorderFactory.createEmptyBorder(5, 5, 5, 5)),
			typeProduit.getBorder()));
	
    jLabel5.setText("Produit");
    jLabel5.setBounds(new Rectangle(155, 322, 125, 20));
    jLabel51.setText("en sortie des activites :");
    jLabel51.setBounds(new Rectangle(12, 340, 195, 20));
    jLabel52.setText("en entree des activites :");
    jLabel52.setBounds(new Rectangle(217, 340, 195, 20));
    jScrollPane51 = new JScrollPane();
    jScrollPane51.setBounds(new Rectangle(12, 360, 195, 55));
    jScrollPane52 = new JScrollPane();
    jScrollPane52.setBounds(new Rectangle(217, 360, 195, 55));

    list_inactivite.setCellRenderer(new ModelElementListCellRenderer());
    list_inactivite.addMouseListener(this.navigationMouseListener);
    list_outactivite.setCellRenderer(new ModelElementListCellRenderer());
    list_outactivite.addMouseListener(this.navigationMouseListener);

    JPanel jPanel = new JPanel(new BorderLayout());
    jPanel.setBounds(new Rectangle(11, 430, 400, 118));
    list_guides.setModel(new TableGuideModel ());
    list_guides.addMouseListener(this.navigationMouseListener);
    list_guides.getColumnModel().getColumn(0).setPreferredWidth(10);
    list_guides.getColumnModel().getColumn(2).setPreferredWidth(10);
    jPanel.add(list_guides.getTableHeader(), BorderLayout.NORTH);
    scrollpaneGuides = new JScrollPane(list_guides);
    jPanel.add(scrollpaneGuides, BorderLayout.CENTER);
    scrollpaneGuides.setBounds(new Rectangle(11, 430, 400, 250));
    scrollpaneGuides.setBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder("Guides associes"),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
        scrollpaneGuides.getBorder()));

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

	// redef
	btnajouterguide.setBounds(new Rectangle(130, 550, 184, 22));
	this.setPreferredSize(new Dimension(425, 580));
	this.setMinimumSize(new Dimension(425, 580));


	this.add(typeProduit, null);
    this.add(jLabel5, null);
    this.add(jLabel51, null);
    this.add(jLabel52, null);
    this.add(jScrollPane51, null);
    this.add(jScrollPane52, null);
    this.add(jPanel, null);
    this.add(jLabel7, null);
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
    setNom_Pres(elem.get_nomPresentation());
    setLabelNomPres(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("nomProduit"));
    setDesc(elem.get_description());
    if(elem instanceof PresentationElementModele){
      PresentationElementModele presElemMod = (PresentationElementModele) elem;
      if (presElemMod.getLnkModelElement()instanceof ModelElement){
        ModelElement modElem = (ModelElement)presElemMod.getLnkModelElement();
        this.setNom_modele(modElem.getName());
      }
      else{
        ProcessComponent procComp = (ProcessComponent)presElemMod.getLnkModelElement();
        this.setNom_modele(procComp.getName());
      }
	  typeProduit.setSelectedItem(presElemMod.get_typeProduit());
    }
    else{
      this.setNom_modele("");
    }
    if (elem.getContenu() != null)
      setFichier_associe(elem.getContenu());
    else
      setFichier_associe(null);
    setList_inActivite(lnkControleurPanneaux.getLnkSysteme().getList_Element(_elementCourant, "in"));
    setList_outActivite(lnkControleurPanneaux.getLnkSysteme().getList_Element(_elementCourant, "out"));
    setTable_guide(lnkControleurPanneaux.getLnkSysteme().getGuides(_elementCourant));
    setIcon(elem.get_icone());
    this.updateUI();
  }


  public void setList_inActivite(ListModel list) {
    list_inactivite.setModel(list);
    jScrollPane51.setViewportView(list_inactivite);
  }

  public void setList_outActivite(ListModel list) {
    list_outactivite.setModel(list);
    jScrollPane52.setViewportView(list_outactivite);
  }

  public void setTable_guide(Object[] list) {
    list_guides.setModel(new TableGuideModel (list));
  }

  class ajoutguideActionListener implements ActionListener {
    public void actionPerformed(ActionEvent e) {
    }
  }

}
