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

package POG.interfaceGraphique.utile.arbre;

import java.awt.Component;
import java.awt.Image;

import javax.swing.ImageIcon;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellEditor;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.TreePath;

import org.ipsquad.apes.model.spem.core.ModelElement;

import POG.interfaceGraphique.action.Systeme;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;
import POG.objetMetier.Presentation;
import POG.objetMetier.PresentationElementModele;

public class ArbrePresentation
    extends Arbre {

  /**
   * Lien vers le listener pour gestion du popup menu
   */
  private ArbrePresentationListener ecouteurClic;

  public ArbrePresentation(Systeme syst) {

//    super();
    lnkSysteme = syst;
    ecouteurClic = new ArbrePresentationListener(this);
    this._arbre.addMouseListener(ecouteurClic);
    IconesRenderer monRenderer = new IconesRenderer(this.lnkSysteme);
    this._arbre.setCellRenderer(monRenderer);
    CellEditor monEditor = new CellEditor(this._arbre, monRenderer);
    this._arbre.setCellEditor(monEditor);
	ArbrePOGListener abpl = new ArbrePOGListener(lnkSysteme) {
		public void valueForPathChanged(TreePath path, Object newValue) {
			Object el = path.getLastPathComponent();
			if (el instanceof ElementPresentation) {
				lnkSysteme.modifierNomDePresentation((ElementPresentation) el, (String) newValue);
			}
			get_arbre().setEditable(false);
		}
	};
	lnkSysteme.ajouterListener(abpl);
	get_arbre().setModel(abpl);
	get_arbre().addTreeSelectionListener(new TreeSelectionListener() {
		public void valueChanged(TreeSelectionEvent arg0) {
			lnkSysteme.lnkFenetrePrincipale.getLnkControleurPanneaux().afficherCentreCorrespondant(arg0.getPath().getLastPathComponent());
		}
		
	});
  }

  public ArbrePresentationListener getListenerArbrePresentation() {
    return this.ecouteurClic;
  }

  /**
   * La racine de l'arbre de presentation
   */

//  DefaultMutableTreeNode racine;

  /**
   * Fonction pour recharger TOUT l'arbre de presentation :
   * N'APPELER QUE AU DEBUT ou apres une synchronisation !!!
   * Ensuite appeler plutot loadElement( ElementPresentation).
   */
/*  public void load() {
    if (lnkSysteme.getlnkControleurPresentation().getlnkPresentation() != null) {
/*      ElementPresentation tete = lnkSysteme.getlnkControleurPresentation().
          getlnkPresentation().getElementPresentation(lnkSysteme.
          getlnkControleurPresentation().getlnkPresentation().getIdRacine());
      racine = new DefaultMutableTreeNode(tete);

      this.setModelWithRenamingListener(racine);
      this.ajouterNoeuds("1");
      
      
      this.get_arbre().setModel(new ArbrePOGListener(lnkSysteme.getlnkControleurPresentation().getlnkPresentation()){

		public void valueForPathChanged(TreePath path, Object newValue) {
			Object el = path.getLastPathComponent();
			if (el instanceof ElementPresentation) {
				lnkSysteme.modifierNomDePresentation((ElementPresentation) el, (String) newValue);
			}
			_arbre.setEditable(false);			
		}
      
      });
    }
    else {
      this.get_arbre().setModel(null);
    }
  }

  /**
   * Ajoute tous les noeuds fils a la racine d'ID idPere
   * @param idPere
   */
/*  private void ajouterNoeuds(String idPere) {
    DefaultMutableTreeNode noeudPere = this.getNoeud(idPere);
    int numFils = 1;
    ElementPresentation el = null;
    boolean isPackFromModel;
    try {
      el = (ElementPresentation)this.getNoeud(idPere).getUserObject();
    }
    catch (Exception e) {
      isPackFromModel = false;
    }
    isPackFromModel = false;
    if (el instanceof PresentationElementModele) {
      if ( ( (PresentationElementModele) el).getLnkModelElement()instanceof
          SPackage) {
        isPackFromModel = true;
      }
    }

    ElementPresentation elt = null;
    DefaultMutableTreeNode fils;
    DefaultMutableTreeNode contenuFils;
    String idSuiv = idPere + "-" + numFils;
    while ( (elt = this.lnkSysteme.getlnkControleurPresentation().
             getlnkPresentation().getElementPresentation(idSuiv)) != null) {
      fils = new DefaultMutableTreeNode(elt);
      noeudPere.add(fils);
      this.setModelAndRefresh(this.racine, null);
      this.ajouterNoeuds(elt.get_id());
      numFils++;
      idSuiv = idPere + "-" + numFils;
    }
    this.setModelAndRefresh(this.racine, null);
  }

  /**
   * Fait setModel tout en redeployant les noeuds anciennement deployes et le
   * noeud anciennement selectionn\uFFFD
   * @param racineCourante
   */
/*  public void setModelAndRefresh(DefaultMutableTreeNode racineCourante,
                                 DefaultMutableTreeNode n) {
    Enumeration enum = _arbre.getExpandedDescendants(this._arbre.getPathForRow(
        0));
    this.setModelWithRenamingListener(racineCourante);
    if (enum != null) {
      while (enum.hasMoreElements()) {
        TreePath path = (TreePath) enum.nextElement();
        this._arbre.expandPath(path);
      }
    }
    if (n != null) {
      TreePath tp = new TreePath(n.getPath());
      this._arbre.setSelectionPath(tp);
    }
  }

   /**
    * Quand un element a subi des modifications (meme suppression) : appeler
    * cette fonction qui met a jour l'arbre.
    * @param element L'element de presentation qui a subi des modifications
    * (ElementPresentation ou PresentationElementModele ou Guide)
    */
/*   public void loadElement(ElementPresentation element) {
     boolean dsPresentation = false;
     boolean dsArbre = false;
     DefaultMutableTreeNode noeudElement;
     this.setModelAndRefresh(this.racine, null);
     String idd = element.get_id();
     ElementPresentation e = this.lnkSysteme.getlnkControleurPresentation().
         getlnkPresentation().
         getElementPresentation(element.get_id());

     if (element.get_id().equals("1")) {
       TreePath tp = new TreePath(this.getNoeud("1"));
       this._arbre.scrollPathToVisible(tp);
       this._arbre.setSelectionPath(tp);
       this._arbre.expandPath(tp);
       return;
     }

     if ( (e != null) && (e == element)) {
       dsPresentation = true;
     }
     String id2 = (element.get_id()).substring(2);
     noeudElement = getNoeud(this.racine, id2);
     if ( (noeudElement != null) &&
         ( (ElementPresentation) (noeudElement.getUserObject()) == element)) {
       dsArbre = true;
     }
     if (dsArbre && dsPresentation) {
       TreePath tp = new TreePath(noeudElement);
       this._arbre.scrollPathToVisible(tp);
       this._arbre.setSelectionPath(tp);
       this._arbre.expandPath(tp);

     }
     else if ( (! (dsArbre)) && dsPresentation) {
       DefaultMutableTreeNode pere = getNoeudPere(element);
       DefaultMutableTreeNode fils = new DefaultMutableTreeNode(element);
       noeudElement = fils;
       if (pere == null) {
         // rien
       }
       pere.add(fils);

       TreePath tp = new TreePath(fils.getPath());
       this._arbre.makeVisible(tp);
       this._arbre.clearSelection();
       this._arbre.scrollPathToVisible(tp);
       this._arbre.setSelectionPath(tp);
       this._arbre.expandPath(tp);
     }
     else { // cas pas dans presentation => suppression
       if (dsArbre) {
         noeudElement.removeAllChildren();
         noeudElement.removeFromParent();
       }
     }
     setModelAndRefresh(racine, noeudElement);
   }

  /**
   * ReLoad quand reorganisation de l'arbre
   * @param map
   */
/*  public void reloadElement(String ancienId1, String nouvelId1,
                            String ancienId2, String nouvelId2) {
    DefaultMutableTreeNode n1 = this.getNoeud(ancienId1);
    DefaultMutableTreeNode n2 = this.getNoeud(ancienId2);
    boolean n1IsExpanded = this._arbre.isExpanded(new TreePath(n1.getPath()));
    boolean n2IsExpanded = this._arbre.isExpanded(new TreePath(n2.getPath()));

    int positionDansPere1;
    int positionDansPere2;

    DefaultMutableTreeNode nPere = this.getNoeudPere( (ElementPresentation) n1.
        getUserObject());
    positionDansPere1 =
        Integer.parseInt(nouvelId1.substring( ( (ElementPresentation) nPere.
                                               getUserObject()).get_id().length() +
                                             1));
    positionDansPere2 =
        Integer.parseInt(nouvelId2.substring( ( (ElementPresentation) nPere.
                                               getUserObject()).get_id().length() +
                                             1));

    positionDansPere1--;
    positionDansPere2--;

    n1.removeFromParent();
    n2.removeFromParent();

    if (positionDansPere1 < positionDansPere2) {
      nPere.insert(n1, positionDansPere1);
      nPere.insert(n2, positionDansPere2);
    }
    else {
      nPere.insert(n2, positionDansPere2);
      nPere.insert(n1, positionDansPere1);
    }

    Enumeration enum = _arbre.getExpandedDescendants(this._arbre.getPathForRow(
        0));
    this.setModelWithRenamingListener(this.racine);
    if (enum != null) {
      while (enum.hasMoreElements()) {
        TreePath path = (TreePath) enum.nextElement();
        this._arbre.expandPath(path);
      }
    }

    if (n1IsExpanded) {
      this._arbre.expandPath(new TreePath(n1.getPath()));
    }
    if (n2IsExpanded) {
      this._arbre.expandPath(new TreePath(n2.getPath()));
    }
    this._arbre.setSelectionPath(new TreePath(n1.getPath()));
  }*/
/*
  private DefaultMutableTreeNode getNoeudPere(ElementPresentation element) {

    DefaultMutableTreeNode pere;
    DefaultMutableTreeNode fils = new DefaultMutableTreeNode(element);
    String id = element.get_id();
    int index = id.length() - 1;
    while ( (java.lang.Character.isDigit( (element.get_id().charAt(index)))) &&
           index >= 0) {
      index--;
    }
    id = id.substring(0, index);
    index = id.length() - 1;
    while ( (java.lang.Character.isDigit( (id.charAt(index)))) &&
           index > 0) {
      index--;
    }
    // si id de type  xxx-xxx (longueur = 2)
    if (index == 0) {
      pere = getNoeud(null, null);
    }
    else {
      pere = getNoeud(racine, id.substring(2));
    }
    return pere;
  }

  /**
   * Retourne le noeud par son identifiant.
   * @param id
   * @return
   *//*
  public DefaultMutableTreeNode getNoeud(String id) {
    if (id == "1") {
      return this.racine;
    }
    return this.getNoeud(racine, id.substring(2));
  }

  /**
   * Attention : appeler avec noeudParent = null si idFils de longueur 2
   * @param idFils L'id RELATIF par rapport au noeud pere.
   *//*
  private DefaultMutableTreeNode getNoeud(DefaultMutableTreeNode
                                          noeudPere, String idFils) {
    if (noeudPere == null) {
      return this.racine;
    }
    int index = 0;
    while ( (java.lang.Character.isDigit( (idFils.charAt(index)))) &&
           index < (idFils.length() - 1)) {
      index++;
    }
    // cas un nombre tout court sans "-"
    if ( (index + 1) == idFils.length()) {
      try {
        int f = noeudPere.getChildCount();
        return (DefaultMutableTreeNode) noeudPere.getChildAt(Integer.parseInt(
            idFils) - 1);
      }

      catch (java.lang.ArrayIndexOutOfBoundsException e) {
        return null;
      }
    }
    else {
      int filsPere = Integer.parseInt(idFils.substring(0, index));
      DefaultMutableTreeNode n;
      try {
        int nh = noeudPere.getChildCount();
        n = (DefaultMutableTreeNode) noeudPere.getChildAt(filsPere - 1);
        nh = noeudPere.getChildCount();
        int j = 2 + 6;
      }
      catch (java.lang.ArrayIndexOutOfBoundsException e) {
        return null;
      }
      if (n != null) {
        return getNoeud(n, idFils.substring(index + 1));
      }
      else {
        return null;
      }
    }
  }

  /**
   * @directed
   */

  public Systeme lnkSysteme;

  /**
   * Selectionne le noeud contenant l'object specifie
   * @param o Object a rechercher dans l'arbre
   */
  public void selectNodeContaining(Object o) {
  	Presentation thepres = lnkSysteme.getlnkControleurPresentation().getlnkPresentation();
	if (o instanceof ModelElement)
		o = thepres.getElementPresentation((ModelElement) o);
	Object [] pth = ArbrePOGListener.thePath(o, thepres);
	if (o instanceof ElementPresentation) {
		TreePath tp = new TreePath(pth);
		this._arbre.scrollPathToVisible(tp);
		this._arbre.setSelectionPath(tp);
		lnkSysteme.lnkFenetrePrincipale.getLnkControleurPanneaux().afficherCentreCorrespondant((ElementPresentation) o);
	}
  }
  	/*
    for (Enumeration e = this.racine.depthFirstEnumeration(); e.hasMoreElements(); ) {
      DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
      Object oNoeud = node.getUserObject();
      if (oNoeud instanceof PresentationElementModele &&
          ( (PresentationElementModele) oNoeud).getLnkModelElement().equals(o)) {
        TreePath tp = new TreePath(node.getPath());
        this._arbre.scrollPathToVisible(tp);
        this._arbre.setSelectionPath(tp);
        this.afficherCentreCorrespondant( (PresentationElementModele) oNoeud);
      }
      else if (oNoeud instanceof Guide &&
               oNoeud.equals(o)) {
        TreePath tp = new TreePath(node.getPath());
        this._arbre.scrollPathToVisible(tp);
        this._arbre.setSelectionPath(tp);
        this.lnkSysteme.lnkFenetrePrincipale.
            getLnkControleurPanneaux().loadCentre("DGuide", (Guide) oNoeud);
      }
    }
  }
*/
  public void selectionnerRacine()
  {
    Object o = lnkSysteme.getlnkControleurPresentation().getlnkPresentation().getElementPresentation(lnkSysteme.getlnkControleurPresentation().getlnkPresentation().getIdRacine());
    if (o != null && this._arbre != null && o instanceof ElementPresentation)
    {
      this._arbre.setSelectionRow(0);
      this.lnkSysteme.lnkFenetrePrincipale.
          getLnkControleurPanneaux().afficherCentreCorrespondant((ElementPresentation)o);
    }
  }


  /**
   * Met editable le noeud pour renomage.
   * @param n Le noeud en cours de renommage.
   */
  public void setNodeEditable(ElementPresentation n) {
    this._arbre.setEditable(true);
    this._arbre.startEditingAtPath(new TreePath(ArbrePOGListener.thePath(n, lnkSysteme.getlnkControleurPresentation().getlnkPresentation())));
  }

  /**
   * Met editable le noeud pour renomage.
   * @param n Le noeud en cours de renommage.
   *//*
  public void setNodeEditable() {
    this._arbre.setEditable(true);
    this._arbre.startEditingAtPath(new TreePath(((DefaultMutableTreeNode)_arbre.getLastSelectedPathComponent()).getPath()));
  }

/*
  public void setModelWithRenamingListener(DefaultMutableTreeNode n) {
    DefaultTreeModel tm = new DefaultTreeModel(n) {
      public void valueForPathChanged(TreePath path, Object newValue) {
        DefaultMutableTreeNode n = (DefaultMutableTreeNode) path.
            getLastPathComponent();
        ElementPresentation elt = (ElementPresentation) n.getUserObject();
        lnkSysteme.modifierNomDePresentation(elt, (String) newValue);
        _arbre.setEditable(false);
      }
      
    };
    this._arbre.setModel(tm);
  }*/

  class IconesRenderer
	  extends DefaultTreeCellRenderer {

	Systeme _systeme;
	public IconesRenderer(Systeme syst) {
	  _systeme = syst;
	}

	public Component getTreeCellRendererComponent(
		JTree tree,
		Object value,
		boolean sel,
		boolean expanded,
		boolean leaf,
		int row,
		boolean hasFocus) {

	  super.getTreeCellRendererComponent(
		  tree, value, sel,
		  expanded, leaf, row,
		  hasFocus);
		  
		Object objet;
		if (value instanceof DefaultMutableTreeNode)
			objet = ((DefaultMutableTreeNode)value).getUserObject();
		else
			objet = value;
		
//	  DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) value;
//	  Object objet = ( (DefaultMutableTreeNode) value).getUserObject();
	  if (objet instanceof Guide || objet instanceof ElementPresentation
		  || objet instanceof PresentationElementModele) {
		ImageIcon icone;

		try {
		  icone = ( (ElementPresentation) objet).get_icone();
		  java.awt.Image img = icone.getImage().getScaledInstance(14, 14,
			  Image.SCALE_SMOOTH);
		  setIcon(new ImageIcon(img));

		}
		catch (Exception e) {
		  icone = lnkSysteme.getLnkPreferences().getIconeDefaut("defaut_icon");
		  setIcon(icone);
		}

	  }
	  return this;
	}
  }

  class CellEditor
	  extends DefaultTreeCellEditor {
	public CellEditor(JTree tree, DefaultTreeCellRenderer renderer) {
	  super(tree, renderer);
	}

	public Component getTreeCellEditorComponent(JTree tree, Object value,
												boolean sel, boolean expanded,
												boolean leaf, int row) {
	  Component c = super.getTreeCellEditorComponent(tree, value, sel, expanded,
		  leaf, row);
		  
		  Object objet;
		  if (value instanceof DefaultMutableTreeNode)
			  objet = ((DefaultMutableTreeNode)value).getUserObject();
		  else
			  objet = value;
//	  DefaultMutableTreeNode noeud = (DefaultMutableTreeNode) value;
//	  Object objet = ( (DefaultMutableTreeNode) value).getUserObject();
	  if (objet instanceof Guide || objet instanceof ElementPresentation
		  || objet instanceof PresentationElementModele) {
		ImageIcon icone;
		try {
		  icone = ( (ElementPresentation) objet).get_icone();
		  java.awt.Image img = icone.getImage().getScaledInstance(14, 14,
			  Image.SCALE_SMOOTH);

		  editingIcon = new ImageIcon(img);
		}
		catch (Exception e) {
		  icone = lnkSysteme.getLnkPreferences().getIconeDefaut("defaut_icon");
		  java.awt.Image img = icone.getImage().getScaledInstance(14, 14,
			  Image.SCALE_SMOOTH);
		  editingIcon = new ImageIcon(img);
		}
	  }
	  return c;
	}

  }

}
