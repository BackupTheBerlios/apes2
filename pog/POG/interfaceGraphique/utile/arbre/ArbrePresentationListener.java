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


package POG.interfaceGraphique.utile.arbre;

import java.awt.Component;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;
import javax.swing.tree.DefaultMutableTreeNode;

import org.ipsquad.apes.model.spem.modelmanagement.SPackage;

import POG.objetMetier.Contenu;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;
import POG.objetMetier.PresentationElementModele;

/**
 * <p>Title: POG</p>
 * <p>Description: Presentation Organisation Generation de composant</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ArbrePresentationListener
    implements MouseListener {

  static ArbrePresentation lnkArbrePresentation;

  protected static Object _objetCourantSelectionne = null;
  protected static DefaultMutableTreeNode _noeudCourantSelectionne = null;

  public ArbrePresentationListener(ArbrePresentation tree) {
    super();
    ArbrePresentationListener.lnkArbrePresentation = tree;
  }

  public ElementPresentation getElementPresentationSelectionne() {
    if (ArbrePresentationListener.lnkArbrePresentation.get_arbre().isSelectionEmpty()) {
      return null;
    }
    DefaultMutableTreeNode noeud = (DefaultMutableTreeNode)ArbrePresentationListener.
        lnkArbrePresentation._arbre.getLastSelectedPathComponent();
    Object objet = noeud.getUserObject();
    return (ElementPresentation) objet;
  }

  /**
   * Les cas :
   *    - menu racine arbre
   *    - menu element de prersentation
   *    - menu element modele presentation
   *    - menu guide
   *    - menu nom fichier contenu
   * @param e
   */
  public void mouseClicked(MouseEvent e) {
    int x = e.getX();
    int y = e.getY();
    int row = ArbrePresentationListener.lnkArbrePresentation.get_arbre().getRowForLocation(x, y);
    if (row != -1) {
      ArbrePresentationListener.lnkArbrePresentation.get_arbre().setSelectionRow(row);
    }
    else {
      return;
    }

    DefaultMutableTreeNode noeud = (DefaultMutableTreeNode)ArbrePresentationListener.
        lnkArbrePresentation._arbre.getLastSelectedPathComponent();
    ArbrePresentationListener._noeudCourantSelectionne = noeud;

    Object objet = noeud.getUserObject();
    ArbrePresentationListener._objetCourantSelectionne = objet;

    int aff = 0;

    //cas contenu
    if (objet instanceof Contenu) {
      if (e.getButton() == MouseEvent.BUTTON3) {
        aff = 1;
      }
      ArbrePresentationListener.lnkArbrePresentation.get_arbre().setSelectionRow(row - 1);
      ArbrePresentationListener._noeudCourantSelectionne = (DefaultMutableTreeNode)ArbrePresentationListener.
          lnkArbrePresentation._arbre.getLastSelectedPathComponent();
      ArbrePresentationListener._objetCourantSelectionne = _noeudCourantSelectionne.getUserObject();
      objet = _objetCourantSelectionne;
      noeud = _noeudCourantSelectionne;
    }

    // cas guide
    if (objet instanceof Guide) {
      lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.
          getLnkControleurPanneaux().loadCentre("DGuide",
                                                (Guide)
                                                _objetCourantSelectionne);
      if (e.getButton() == MouseEvent.BUTTON3) {
        afficherMenuGuide_ElementPresentationSimple( (Component) e.getSource(),
            x, y);
      }
    }
    else if (objet instanceof PresentationElementModele) {
      // cas racine ou un paquetage du modele
      if (e.getButton() == MouseEvent.BUTTON3) {

        PresentationElementModele element = (PresentationElementModele) objet;
        if (element.get_id() ==
            ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.getlnkControleurPresentation().
            getlnkPresentation().getIdRacine() ||
            element.getLnkModelElement()instanceof SPackage) {
          afficherMenuRacineComposant_Paquetage( (Component) e.getSource(), x,
                                                y);
          // cas role, produit, activite, definitin de travail
        }
        else {
          try {
            this.afficherMenuRPDA( (Component) e.getSource(), x, y);
          }
          catch (Exception ex) {
            // Rien : cas element de modele sans menu specifique.
          }
        }
      }
      else {
        ArbrePresentationListener.lnkArbrePresentation.afficherCentreCorrespondant( (
            PresentationElementModele) objet);
      }
    }
    // cas paquetage de presentation
    else if (objet instanceof ElementPresentation) {
      if ( ( (ElementPresentation)ArbrePresentationListener._objetCourantSelectionne).get_id() ==
          ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.getlnkControleurPresentation().
          getlnkPresentation().getIdRacine()) {
        if (e.getButton() == MouseEvent.BUTTON3) {
          afficherMenuRacinePaquetage( (Component) e.getSource(), x, y);
        }
        else {
          lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.
              getLnkControleurPanneaux().loadCentre("DElementPresentation",
              (ElementPresentation)
              _objetCourantSelectionne);
        }
      }
      else {
        if (e.getButton() == MouseEvent.BUTTON3) {
          afficherMenuGuide_ElementPresentationSimple( (Component) e.getSource(),
              x, y);
        }
        lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.
            getLnkControleurPanneaux().loadCentre("DElementPresentation",
                                                  (ElementPresentation)
                                                  _objetCourantSelectionne);
      }
    }
    else { // rien
    }
    if (aff == 1) {
      ArbrePresentationListener.lnkArbrePresentation.get_arbre().setSelectionRow(row);
      ArbrePresentationListener._noeudCourantSelectionne = (DefaultMutableTreeNode)ArbrePresentationListener.
          lnkArbrePresentation._arbre.getLastSelectedPathComponent();
      ArbrePresentationListener._objetCourantSelectionne = _noeudCourantSelectionne.getUserObject();
    }
  }

  public void mousePressed(MouseEvent e) {

    /**@todo Implement this java.awt.event.MouseListener method*/
    // throw new java.lang.UnsupportedOperationException("Method mousePressed() not yet implemented.");
  }

  public void mouseReleased(MouseEvent e) {

    /**@todo Implement this java.awt.event.MouseListener method*/
    // throw new java.lang.UnsupportedOperationException("Method mouseReleased() not yet implemented.");
  }

  public void mouseEntered(MouseEvent e) {

    /**@todo Implement this java.awt.event.MouseListener method*/
    //throw new java.lang.UnsupportedOperationException("Method mouseEntered() not yet implemented.");
  }

  public void mouseExited(MouseEvent e) {

    /**@todo Implement this java.awt.event.MouseListener method*/
    //  throw new java.lang.UnsupportedOperationException("Method mouseExited() not yet implemented.");
  }

  public void afficherMenuGuide_ElementPresentationSimple(Component compo,
      int x, int y) {
    boolean contenuPresent =
        ( ( (ElementPresentation)ArbrePresentationListener._objetCourantSelectionne).getContenu() != null);
    JPopupMenu popup = initMenuGuide_ElementPresentationSimple(contenuPresent);
    popup.show(compo, x, y);
  }

  public void afficherMenuRPDA(Component compo, int x, int y) {
    JPopupMenu popup = initMenuRPDA();
    popup.show(compo, x, y);
  }

  public void afficherMenuRacinePaquetage(Component compo, int x, int y) {
    JPopupMenu popup = initMenuRacinePaquetage();
    popup.show(compo, x, y);

  }

  public void afficherMenuRacineComposant_Paquetage(Component compo, int x,
      int y) {
    JPopupMenu popup = initMenuRacineComposant_Paquetage();
    popup.show(compo, x, y);

  }

      /****************************************************************************/
      /**************************** LES MENUS *************************************/
      /****************************************************************************/

  /**
   * Le Menu pour un guide ou pour un element de presentation simple
   * (cas paquetage de presentation)
   */
  public JPopupMenu initMenuGuide_ElementPresentationSimple(boolean
      contenuPresent) {
    JPopupMenu popup = new JPopupMenu();

    JMenuItem choixOuvrir = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                          lnkFenetrePrincipale.getLnkLangues().
                                          valeurDe("Ouvrir"));

    JMenuItem choixSupprimer = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                             lnkFenetrePrincipale.getLnkLangues().
                                             valeurDe("Supprimer"));

    JMenuItem choixContenu;
    if (!contenuPresent) {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("AjouterContenu"));
    }
    else {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("ChangerDeContenu"));
    }

    JMenuItem choixIcone = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                         lnkFenetrePrincipale.getLnkLangues().
                                         valeurDe("ChangerIcone"));

    JMenuItem choixRenommer = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                            lnkFenetrePrincipale.getLnkLangues().
                                            valeurDe("Renommer"));

    JMenu choixGuide = new JMenu(lnkArbrePresentation.lnkSysteme.
                                 lnkFenetrePrincipale.getLnkLangues().
                                 valeurDe("AjouterGuide"));

        /* Creer autants de sous-menus que de guides possibles pour l'element courant*/

    if (! (ArbrePresentationListener._objetCourantSelectionne instanceof Guide)) {
      String typeElement = "ElementDePresentation";
      Vector vTypes = ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
          getLnkControleurGuide().
          type(typeElement);

      Enumeration enum = vTypes.elements();

      while (enum.hasMoreElements()) {
        String value = (String) enum.nextElement();
        JMenuItem choixEnPlus = new JMenuItem(value);
        choixEnPlus.addActionListener(new listenerSousMenusGuides(value));
        choixGuide.add(choixEnPlus);
      }

    }

    choixOuvrir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            lnkFenetrePrincipale.getLnkControleurPanneaux().loadCentre(
            "DElementPresentation",
            (ElementPresentation) ArbrePresentationListener.
            _objetCourantSelectionne);
      }
    });

    choixSupprimer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            supprimerElement(
            (ElementPresentation) ArbrePresentationListener.
            _objetCourantSelectionne);
      }
    });

    choixContenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            choisirContenu(
            (ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixIcone.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            lnkFenetrePrincipale.afficheChangerIcone( (
            ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixRenommer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.setNodeEditable(_noeudCourantSelectionne);
      }
    });

    popup.add(choixOuvrir);
    popup.addSeparator();
    popup.add(choixRenommer);
    popup.add(choixIcone);
    popup.addSeparator();
    if (! (ArbrePresentationListener._objetCourantSelectionne instanceof Guide)) {
      popup.add(choixGuide);
    }
    popup.add(choixContenu);
    popup.addSeparator();
    popup.addSeparator();
    popup.add(choixSupprimer);
    return popup;
  }

  /**
   * Le Menu pour (4 elements provenant d'un modele) :
   *    - R : role
   *    - P : produit
   *    - D : definition de travail
   *    - A : activite
   */
  public JPopupMenu initMenuRPDA() {
    PresentationElementModele element = (PresentationElementModele)
        _objetCourantSelectionne;
    JPopupMenu popup = new JPopupMenu();

    JMenuItem choixOuvrir = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                          lnkFenetrePrincipale.getLnkLangues().
                                          valeurDe("Ouvrir"));
    JMenuItem choixIcone = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                         lnkFenetrePrincipale.getLnkLangues().
                                         valeurDe("ChangerIcone"));
    JMenuItem choixRenommer = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                            lnkFenetrePrincipale.getLnkLangues().
                                            valeurDe("Renommer"));

    JMenu choixGuide = new JMenu(lnkArbrePresentation.lnkSysteme.
                                 lnkFenetrePrincipale.getLnkLangues().
                                 valeurDe("AjouterGuide"));

        /* Creer autants de sous-menus que de guides possibles pour l'element courant*/

    String typeElement = element.getStringType();
    Vector vTypes = ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.getLnkControleurGuide().
        type(typeElement);

    Enumeration enum = vTypes.elements();

    while (enum.hasMoreElements()) {
      String value = (String) enum.nextElement();
      JMenuItem choixEnPlus = new JMenuItem(value);
      choixEnPlus.addActionListener(new listenerSousMenusGuides(value));
      choixGuide.add(choixEnPlus);
    }

    JMenuItem choixContenu = null;

    if (element.getContenu() == null) {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("AjouterContenu"));
    }
    else {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("changerdecontenu"));
    }
    choixContenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            choisirContenu(
            (ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixOuvrir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.afficherCentreCorrespondant( (
            PresentationElementModele) _objetCourantSelectionne);
      }
    });

    choixIcone.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            lnkFenetrePrincipale.afficheChangerIcone( (
            ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixRenommer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.setNodeEditable(_noeudCourantSelectionne);
      }
    });

    popup.add(choixOuvrir);
    popup.addSeparator();
    popup.add(choixRenommer);
    popup.add(choixIcone);
    popup.addSeparator();
    popup.add(choixGuide);
    popup.add(choixContenu);
    return popup;
  }

  /**
   * Le Menu pour la racine si c'est un paquetage de presentation
   */
  public JPopupMenu initMenuRacinePaquetage() {
    JPopupMenu popup = new JPopupMenu();
    ElementPresentation element = (ElementPresentation)
        _objetCourantSelectionne;

    JMenuItem choixOuvrir = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                          lnkFenetrePrincipale.getLnkLangues().
                                          valeurDe("Ouvrir"));
    JMenuItem choixIcone = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                         lnkFenetrePrincipale.getLnkLangues().
                                         valeurDe("ChangerIcone"));
    JMenuItem choixRenommer = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                            lnkFenetrePrincipale.getLnkLangues().
                                            valeurDe("Renommer"));

    JMenuItem choixContenu = null;

    if (element.getContenu() == null) {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("AjouterContenu"));
    }
    else {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("changerdecontenu"));
    }
    choixContenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            choisirContenu(
            (ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    JMenuItem choixAjouterElementPre = new JMenuItem(lnkArbrePresentation.
        lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe(
        "AjouterElementPresentation"));

    choixOuvrir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.
            getLnkControleurPanneaux().loadCentre("DElementPresentation",
                                                  (ElementPresentation)
                                                  _objetCourantSelectionne);
      }
    });

    choixIcone.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            lnkFenetrePrincipale.afficheChangerIcone( (
            ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixRenommer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.setNodeEditable(_noeudCourantSelectionne);
      }
    });

    choixAjouterElementPre.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            ajouterElementPre();
      }
    });

    popup.add(choixOuvrir);
    popup.addSeparator();
    popup.add(choixRenommer);
    popup.add(choixIcone);
    popup.addSeparator();
    popup.add(choixContenu);
    popup.addSeparator();
    popup.add(choixAjouterElementPre);
    return popup;
  }

  /**
   * Le Menu pour la racine si un composant et pour un paquetage du composant
   */
  public JPopupMenu initMenuRacineComposant_Paquetage() {
    JPopupMenu popup = new JPopupMenu();
    PresentationElementModele element = (PresentationElementModele)
        _objetCourantSelectionne;

    JMenuItem choixOuvrir = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                          lnkFenetrePrincipale.getLnkLangues().
                                          valeurDe("Ouvrir"));
    JMenuItem choixIcone = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                         lnkFenetrePrincipale.getLnkLangues().
                                         valeurDe("ChangerIcone"));
    JMenuItem choixRenommer = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                            lnkFenetrePrincipale.getLnkLangues().
                                            valeurDe("Renommer"));

    JMenuItem choixContenu = null;

    if (element.getContenu() == null) {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("AjouterContenu"));
    }
    else {
      choixContenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.
                                   lnkFenetrePrincipale.getLnkLangues().
                                   valeurDe("changerdecontenu"));
    }
    choixContenu.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            choisirContenu(
            (ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixOuvrir.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.afficherCentreCorrespondant( (
            PresentationElementModele) _objetCourantSelectionne);
      }
    });

    choixIcone.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
            lnkFenetrePrincipale.afficheChangerIcone( (
            ElementPresentation) ArbrePresentationListener.
            _noeudCourantSelectionne.getUserObject());
      }
    });

    choixRenommer.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkArbrePresentation.setNodeEditable(_noeudCourantSelectionne);
      }
    });

    popup.add(choixOuvrir);
    popup.addSeparator();
    popup.add(choixRenommer);
    popup.add(choixIcone);
    popup.addSeparator();
    popup.add(choixContenu);

    return popup;
  }
}

/**
 * Classe pour les listeners des guides
 * <p>Title: POG</p>
 * <p>Description: Presentation Organisation Generation de composant</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */
class listenerSousMenusGuides
    implements ActionListener {
  String _typeGuide;

  public listenerSousMenusGuides(String typeGuide) {
    this._typeGuide = typeGuide;
  }

  public void actionPerformed(ActionEvent evt) {
    ArbrePresentationListener.lnkArbrePresentation.lnkSysteme.
        ajouterGuide( (ElementPresentation) ArbrePresentationListener.
                     _objetCourantSelectionne, _typeGuide);
  }

}
