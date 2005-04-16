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
import java.util.Iterator;

import javax.swing.JMenu;
import javax.swing.JMenuItem;
import javax.swing.JPopupMenu;

import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.core.ModelElement;

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

  private ArbrePresentation lnkArbrePresentation;

  protected static ElementPresentation _objetCourantSelectionne = null;
//  protected static DefaultMutableTreeNode _noeudCourantSelectionne = null;

  public ArbrePresentationListener (ArbrePresentation tree) {
    super();
    lnkArbrePresentation = tree;
    initMenus();
  }

  public ElementPresentation getElementPresentationSelectionne() {
    if (lnkArbrePresentation.get_arbre().isSelectionEmpty()) {
      return null;
    }
    return (ElementPresentation) lnkArbrePresentation._arbre.getLastSelectedPathComponent();
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
    int row = lnkArbrePresentation.get_arbre().getRowForLocation(e.getX(), e.getY());
    if (row == -1) 
      return;
      
	lnkArbrePresentation.get_arbre().setSelectionRow(row);

 /*   DefaultMutableTreeNode noeud = (DefaultMutableTreeNode)lnkArbrePresentation._arbre.getLastSelectedPathComponent();
    _noeudCourantSelectionne = noeud;

    Object objet = noeud.getUserObject();
    _objetCourantSelectionne = objet;
*/
	_objetCourantSelectionne = (ElementPresentation) lnkArbrePresentation._arbre.getLastSelectedPathComponent();

	if (e.getButton() == MouseEvent.BUTTON3)
		afficherPopup(e);
	
//	lnkArbrePresentation.afficherCentreCorrespondant(_objetCourantSelectionne);
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

      /****************************************************************************/
      /**************************** LES MENUS *************************************/
      /****************************************************************************/

	private JPopupMenu _leclickdroit = new JPopupMenu();
	private JMenuItem _mnuajouterelem;
	private JMenuItem _mnusupprimer;
	private JMenuItem _mnucontenu;
	private	JMenuItem _mnuicone;
	private JMenuItem _mnurenommer;
	private JMenu _mnuguide;

	
	private void initMenus() {
		_mnuajouterelem = new JMenuItem(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("AjouterElementPresentation"));
		_mnusupprimer = new JMenuItem(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("Supprimer"));
		_mnucontenu = new JMenuItem(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("AjouterContenu"));
		_mnuicone = new JMenuItem(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("ChangerIcone"));
		_mnurenommer = new JMenuItem(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("Renommer"));
		_mnuguide = new JMenu(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("AjouterGuide"));

		_mnuajouterelem.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				lnkArbrePresentation.lnkSysteme.ajouterElementPre();
  			}
		});

		_mnusupprimer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				lnkArbrePresentation.lnkSysteme.supprimerElement((ElementPresentation)_objetCourantSelectionne);
			}
		});

		_mnucontenu.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				lnkArbrePresentation.lnkSysteme.choisirContenu((ElementPresentation)_objetCourantSelectionne);
			}
		});

		_mnuicone.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.afficheChangerIcone((ElementPresentation)_objetCourantSelectionne);
			}
		});

		_mnurenommer.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent evt) {
				lnkArbrePresentation.setNodeEditable(_objetCourantSelectionne);
			}
		});

		_leclickdroit.add(_mnuajouterelem);
		_leclickdroit.add(_mnusupprimer);
		_leclickdroit.add(_mnucontenu);
		_leclickdroit.add(_mnuicone);
		_leclickdroit.add(_mnurenommer);
		_leclickdroit.add(_mnuguide);
	}
	
	private void rempliPopupGuide(String typeg) {
		_mnuguide.removeAll();
		Iterator vTypes =  lnkArbrePresentation.lnkSysteme.getLnkControleurGuide().types();
		while (vTypes.hasNext()) {
		  String value = (String) vTypes.next();
			JMenuItem choixEnPlus = new JMenuItem(value);
			choixEnPlus.addActionListener(new listenerSousMenusGuides(value));
			_mnuguide.add(choixEnPlus);
		}
	}


	private void afficherPopup(MouseEvent e) {
		_mnuajouterelem.setVisible(false);
		_mnusupprimer.setVisible(true);
		_mnucontenu.setVisible(true);
		_mnuicone.setVisible(true);
		_mnurenommer.setVisible(true);
		_mnuguide.setVisible(true);
		_mnucontenu.setText(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("AjouterContenu"));
		if (((ElementPresentation)_objetCourantSelectionne).getContenu() != null)
			_mnucontenu.setText(lnkArbrePresentation.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("changerdecontenu"));
		if (((ElementPresentation)_objetCourantSelectionne).get_id().equals(lnkArbrePresentation.lnkSysteme.getlnkControleurPresentation().getlnkPresentation().getIdRacine())) {
			_mnusupprimer.setVisible(false);
			_mnuajouterelem.setVisible(lnkArbrePresentation.lnkSysteme.getlnkControleurPresentation().get_pathModele() == null);
			rempliPopupGuide("ALL");
		}
		else if (_objetCourantSelectionne instanceof Guide)
			_mnuguide.setVisible(false);
		else if (_objetCourantSelectionne instanceof PresentationElementModele) {
			ModelElement em = ((PresentationElementModele)_objetCourantSelectionne).getLnkModelElement();
			if (em instanceof SpemDiagram) {
				_mnusupprimer.setVisible(false);
				_mnucontenu.setVisible(false);
				_mnuicone.setVisible(false);
				_mnurenommer.setVisible(false);
				_mnuguide.setVisible(false);
				
			}
			else {
				_mnusupprimer.setVisible(false);
				rempliPopupGuide(em.getClass().getName());
			}
		}
		else
			rempliPopupGuide("ALL");
		_leclickdroit.show((Component) e.getSource(), e.getX(), e.getY());
	}

  class listenerSousMenusGuides
	  implements ActionListener {
	String _typeGuide;

	public listenerSousMenusGuides(String typeGuide) {
	  this._typeGuide = typeGuide;
	}

	public void actionPerformed(ActionEvent evt) {
	  lnkArbrePresentation.lnkSysteme.ajouterGuide( (ElementPresentation)_objetCourantSelectionne, _typeGuide);
	}

  }

}

