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

package POG.interfaceGraphique.action;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JSplitPane;
import javax.swing.event.MenuEvent;
import javax.swing.event.MenuListener;

import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauActivite;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauDefinitionTravail;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauDetail;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauDiagram;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauElementPresentation;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauGuide;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauProduit;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauRole;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauVide;
import POG.interfaceGraphique.fenetre.PanneauxDetails.listenerSousMenusGuides;
import POG.objetMetier.ElementPresentation;
//import application.controleurMetier.ControleurOrganiser;

public class ControleurPanneaux {
  /**
   * @supplierCardinality 1
   * @directed
   */
  private PanneauGuide lnkPanneauGuide = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private PanneauRole lnkPanneauRole = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private PanneauElementPresentation lnkPanneauElementPresentation = null;

  private PanneauVide lnkPanneauVide = new PanneauVide();

  /**
   * Represente le panneau courant afficher au centre de la fenetre principale.
   * C'est lui qu'il faut reloader si besoin.
   * @supplierCardinality 1
   * @directed
   */
  private PanneauDetail lnkPanneauDetail = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private PanneauProduit lnkPanneauProduit = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private PanneauActivite lnkPanneauActivite = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private PanneauDefinitionTravail lnkPanneauDefinitionTravail = null;

  private JSplitPane _jsplit;

  private MenuCentral _menu;

  /**
   * @directed
   * @supplierCardinality 1
   */
  private Systeme lnkSysteme;

  public void reload() {
    if (lnkPanneauDetail != null) {
      if (lnkPanneauDetail.get_elementCourant() != null)
        lnkPanneauDetail.loadElement(lnkPanneauDetail.get_elementCourant());
    }
    else{
      loadVide();
    }
  }

  public void loadVide(){
    if (lnkPanneauDetail != null) {
      if (lnkPanneauDetail.isVisible()) {
        lnkPanneauDetail.setVisible(false);
        _jsplit.remove(lnkPanneauDetail);
      }
    }
    if (!lnkPanneauVide.isVisible()) {
      lnkPanneauVide.setVisible(true);
      _jsplit.add(lnkPanneauVide, JSplitPane.TOP);
    }
    _menu._mnu.setVisible(false);
  }

  public ControleurPanneaux(JSplitPane jsp, JMenu mnu, Systeme sys){
    _jsplit = jsp;
    lnkSysteme = sys;
    _menu = new MenuCentral(mnu, this);
    loadVide();
  }

  /*
   * FONCTION INPORTANTE !!!
   * A chaque fois qu'il y a appel de load pour l'arbre de presentation il se
   * peut qu'il y ai besoin de reload le panneau central : APPELEER cette
   * fonction qui s'en occupe.
   */
  public void loadCentre(String fenetre, ElementPresentation idelem){
    if (lnkPanneauVide.isVisible()) {
      lnkPanneauVide.setVisible(false);
      _jsplit.remove(lnkPanneauVide);
    }
    if (lnkPanneauDetail != null) {
      lnkPanneauDetail.setVisible(false);
      _jsplit.remove(lnkPanneauDetail);
    }

    if (fenetre.equals("DActivite")) {
      if (lnkPanneauActivite == null)
        lnkPanneauActivite = new PanneauActivite(this);
      lnkPanneauDetail = lnkPanneauActivite;
      lnkPanneauDetail.setVisibleNomModele(true);
    }
    else if (fenetre.equals("DDefinitionTravail")) {
      if (lnkPanneauDefinitionTravail == null)
        lnkPanneauDefinitionTravail = new PanneauDefinitionTravail(this);
      lnkPanneauDetail = lnkPanneauDefinitionTravail;
      lnkPanneauDetail.setVisibleNomModele(true);
    }
    else if (fenetre.equals("DElementPresentation") || fenetre.equals("DPackage")) {
      if (lnkPanneauElementPresentation == null)
        lnkPanneauElementPresentation = new PanneauElementPresentation(this);
      lnkPanneauDetail = lnkPanneauElementPresentation;
      lnkPanneauDetail.setVisibleNomModele(fenetre.equals("DPackage"));
    }
    else if (fenetre.equals("DGuide")) {
      if (lnkPanneauGuide == null)
        lnkPanneauGuide = new PanneauGuide(this);
      lnkPanneauDetail = lnkPanneauGuide;
      lnkPanneauDetail.setVisibleNomModele(false);
    }
    else if (fenetre.equals("DProduit")) {
      if (lnkPanneauProduit == null)
        lnkPanneauProduit = new PanneauProduit(this);
      lnkPanneauDetail = lnkPanneauProduit;
      lnkPanneauDetail.setVisibleNomModele(true);
    }
    else if (fenetre.equals("DRole")) {
      if (lnkPanneauRole == null)
        lnkPanneauRole = new PanneauRole(this);
      lnkPanneauDetail = lnkPanneauRole;
      lnkPanneauDetail.setVisibleNomModele(true);
    }
	else if (fenetre.equals("DDiagram")) {
	  lnkPanneauDetail = new PanneauDiagram(this, idelem);
	}
    else {
    	loadVide();
    	return;
    }
    _jsplit.add(lnkPanneauDetail, JSplitPane.TOP);
    lnkPanneauDetail.loadElement(idelem);
    _menu.majMenu(fenetre);
    lnkPanneauDetail.setVisible(true);
  }


  public Systeme getLnkSysteme() {
    return lnkSysteme;
  }



  private class MenuCentral implements ActionListener {
    private JMenu _mnu;
    private ControleurPanneaux lnkControleurPanneaux;
    private JMenuItem jMenuAjouterContenu = new JMenuItem();
    private JMenuItem jMenuChangerContenu = new JMenuItem();
    private JMenuItem jMenuSupprimerContenu = new JMenuItem();
    private JMenu jMenuAjouterGuide = new JMenu();
    private JMenuItem jMenuSupprimer = new JMenuItem();
    private String _dernierPanneau = "DPOG";

    public MenuCentral(JMenu mnu, ControleurPanneaux cp) {
      _mnu = mnu;
      mnu.addMenuListener(new MenuListener()
        {
          public void menuSelected(MenuEvent e)
          {
            majContenuMenu() ;
          }
          public void menuDeselected(MenuEvent e) {}
          public void menuCanceled(MenuEvent e) {}
        });
      lnkControleurPanneaux = cp ;
      jMenuAjouterContenu.setText(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("ajoutercontenu"));
      jMenuChangerContenu.setText(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("changerdecontenu"));
      jMenuSupprimerContenu.setText(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimercontenu"));
      jMenuAjouterGuide.setText(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("ajouterguide"));
      jMenuSupprimer.setText(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("supprimer"));

      jMenuAjouterContenu.setVisible(false);
      jMenuChangerContenu.setVisible(false);
      jMenuAjouterGuide.setVisible(false);
      jMenuSupprimerContenu.setVisible(false);
      jMenuSupprimer.setVisible(false);

      jMenuAjouterContenu.addActionListener(this);
      jMenuChangerContenu.addActionListener(this);
      jMenuAjouterGuide.addActionListener(this);
      jMenuSupprimerContenu.addActionListener(this);
      jMenuSupprimer.addActionListener(this);

      _mnu.add(jMenuAjouterContenu);
      _mnu.add(jMenuChangerContenu);
      _mnu.add(jMenuSupprimerContenu);
      _mnu.add(jMenuAjouterGuide);
      _mnu.add(jMenuSupprimer);
    }

    public void majMenu(String panneau) {
      _dernierPanneau = panneau ;
      if (panneau.equals("DPOG") || panneau.equals("DDiagram"))
        _mnu.setVisible(false);
      else {
        _mnu.setVisible(true);
      	_mnu.setText(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe(panneau));
      }
    }

    public void majContenuMenu()
    {
      ElementPresentation ep = lnkPanneauDetail.get_elementCourant();

      jMenuSupprimerContenu.setVisible(true);
      if (ep != null && ep.getContenu() == null)
      {
        jMenuAjouterContenu.setVisible(true);
        jMenuChangerContenu.setVisible(false);
        jMenuSupprimerContenu.setEnabled(false);
      }
      else
      {
        jMenuAjouterContenu.setVisible(false);
        jMenuChangerContenu.setVisible(true);
        jMenuSupprimerContenu.setEnabled(true);
      }

      if (_dernierPanneau.equals("DGuide"))
      {
        jMenuAjouterGuide.setVisible(false);
        jMenuSupprimer.setVisible(true);
      }
      else
      {
        jMenuAjouterGuide.setVisible(true);
        jMenuSupprimer.setVisible(_dernierPanneau.equals("DElementPresentation")
                                  && !ep.get_id().equals(getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().getIdRacine()));
      }

      jMenuAjouterGuide.removeAll();
      if (ep != null)
      {
		Iterator vTypes = this.lnkControleurPanneaux.getLnkSysteme().getLnkControleurGuide().types();
		while (vTypes.hasNext()) {
		  String value = (String) vTypes.next();
          JMenuItem mi = new JMenuItem(value);
          mi.addActionListener(new listenerSousMenusGuides(value, lnkPanneauDetail,
            this.lnkControleurPanneaux));
          jMenuAjouterGuide.add(mi);
        }
      }
    }

    public void actionPerformed(ActionEvent evt) {
      lnkPanneauDetail.actionMenu(evt);
    }
  }

  public void supprimerElement(ElementPresentation elt) {
    lnkPanneauDetail.supprimerElement(elt);
  }



public JSplitPane get_jsplit() {
	return _jsplit;
}

}
