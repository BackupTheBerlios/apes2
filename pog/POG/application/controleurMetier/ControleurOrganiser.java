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

package POG.application.controleurMetier;

import java.io.File;
import java.util.Hashtable;

import javax.swing.ImageIcon;

import POG.objetMetier.Contenu;
import POG.objetMetier.ElementPresentation;

/**
 * @stereotype control
 */
public class ControleurOrganiser
    extends ControleurSemantique {

  public ControleurOrganiser(ControleurPresentation ctrlpres) {
    super(ctrlpres);
  }

  public void associerContenu(ElementPresentation elemPres, File fichier) {
    Contenu contenu = this.lnkControleurPresentation.getlnkPresentation().
        lnkBibliotheque.getContenu(fichier);
    elemPres.setContenu(contenu);

  }

  public void supprimerContenu(ElementPresentation elem) {
    elem.setContenu(null);
  }

  public void ajouterGuide(String IdElement, String typeGuide) {}

  /**
   * Ajout d'un element de presentation par l'utilisateur dans la racine de
   * l'arbre
   * @param nom Le nom de presentation
   * @return elemt
   */
  public ElementPresentation ajouterElementPre(String nom) {
    String id = this.lnkControleurPresentation.getlnkPresentation().makeId("1");
    ImageIcon icone = lnkControleurPresentation.lnkPreferences.getIconeDefaut(
        "defaut_icon");
    ElementPresentation elmt = new ElementPresentation(id, icone);
    elmt.set_nomPresentation(nom);
    this.lnkControleurPresentation.getlnkPresentation().
        ajouterElementPresentation(elmt);
    return elmt;
  }

  public void modifierElement(ElementPresentation elem, String nom, String desc) {
    elem.set_nomPresentation(nom);
    elem.set_description(desc);
  }

  public void modifierNomDePresentation(ElementPresentation elem, String nom){
    elem.set_nomPresentation(nom);
  }

  public void supprimerElement(ElementPresentation elem) {
    lnkControleurPresentation.getlnkPresentation().removeElementAndUp(elem.
        get_id());
  }

  public Hashtable monter(ElementPresentation elt) {
    if (elt.get_id().equals("1")) {
      return null;
    }
    if ( (elt.get_id().substring(elt.get_id().length() - 2, elt.get_id().length())).equals("-1")) {
      return null;
    }
    String id2 = "";
    String idPere = (this.lnkControleurPresentation.getlnkPresentation().
                     getElementParent(elt.get_id())).get_id();
    String idTerminal = elt.get_id().substring(idPere.length() + 1,
                                               elt.get_id().length());
    int i = Integer.parseInt(idTerminal);
    id2 = idPere + "-" + (i - 1);
    ElementPresentation el2 = this.lnkControleurPresentation.getlnkPresentation().
        getElementPresentation(id2);
    if (el2 == null) {
      return null;
    }
    Hashtable res = this.lnkControleurPresentation.getlnkPresentation().echangerPositions(elt,
        el2);
    return res;
  }

  public Hashtable descendre(ElementPresentation elt) {
    if (elt.get_id().equals("1")) {
      return null;
    }
    String id2 = "";
    String idPere = (this.lnkControleurPresentation.getlnkPresentation().
                     getElementParent(elt.get_id())).get_id();
    String idTerminal = elt.get_id().substring(idPere.length() + 1,
                                               elt.get_id().length());
    int i = Integer.parseInt(idTerminal);
    id2 = idPere + "-" + (i + 1);
    ElementPresentation el2 = this.lnkControleurPresentation.getlnkPresentation().
        getElementPresentation(id2);
    if (el2 == null) {
      return null;
    }
    Hashtable res = this.lnkControleurPresentation.getlnkPresentation().echangerPositions(elt,
     el2);
    return res;
  }

}
