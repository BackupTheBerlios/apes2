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

import java.awt.event.MouseListener;
import java.awt.event.MouseEvent;
import javax.swing.JPopupMenu;
import javax.swing.JMenuItem;
import javax.swing.AbstractAction;
import javax.swing.JTree;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.TreePath;
import java.awt.event.ActionEvent;
import java.awt.Component;

/**
 * <p>Title: POG</p>
 * <p>Description: Presentation Organisation Generation de composant</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author not attributable
 * @version 1.0
 */

public class ArbreExplorateurListener implements MouseListener {

  private ArbreExplorateur lnkArbreExplorateur;
  private JPopupMenu popup = null;
  private AbstractAction actionEditer ;
  private AbstractAction actionRenommer ;
  private AbstractAction actionSupprimer;
  
  private static DefaultMutableTreeNode _objetCourantSelectionne = null;


  public ArbreExplorateurListener(ArbreExplorateur tree) {
    super();
    this.lnkArbreExplorateur = tree;
    this.initMenuPopUp();
   }

  private void traiterPopup (MouseEvent e)
  {
    if (e.isPopupTrigger())
    {
      /* Selectionner l'element clique par clic droit */
      int x = e.getX();
      int y = e.getY();

      // Recupere l'element sous la souris
      TreePath p = this.lnkArbreExplorateur.get_arbre().getPathForLocation(x, y);
      if (p == null)
        return;

      // Selectionne l'element
      this.lnkArbreExplorateur.get_arbre().setSelectionPath(p);
      ArbreExplorateurListener._objetCourantSelectionne = (DefaultMutableTreeNode) p.getLastPathComponent();
      this.afficherMenu((Component) e.getSource(), x, y, this.lnkArbreExplorateur.isFichier(ArbreExplorateurListener._objetCourantSelectionne));
    }
  }

  public void mouseClicked(MouseEvent e) {
    // Traitement du double clic
    if (e.getButton() == MouseEvent.BUTTON1 && e.getClickCount() == 2)
    {
      // Verifie qu'un element est clique
      TreePath tp = ((JTree) e.getSource()).getSelectionPath() ;
      if (tp == null)
        return ;

      ArbreExplorateurListener._objetCourantSelectionne = (DefaultMutableTreeNode) tp.getLastPathComponent() ;
      if (ArbreExplorateurListener._objetCourantSelectionne.isLeaf() && this.lnkArbreExplorateur.isFichier(ArbreExplorateurListener._objetCourantSelectionne))
      {
        this.lnkArbreExplorateur.editer(ArbreExplorateurListener._objetCourantSelectionne);
      }
    }
  }
  public void mousePressed(MouseEvent e) {
    this.traiterPopup(e);
  }
  public void mouseReleased(MouseEvent e) {
    this.traiterPopup(e);
  }
  public void mouseEntered(MouseEvent e) {
  }
  public void mouseExited(MouseEvent e) {
  }

  /**************************** LES MENUS *************************************/

  public void initMenuPopUp(){
    this.popup = new JPopupMenu();

    this.actionEditer = new AbstractAction (this.lnkArbreExplorateur.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("Editer"))
        {
          public void actionPerformed(ActionEvent e)
          {
            lnkArbreExplorateur.editer(_objetCourantSelectionne);
          }
        } ;
    JMenuItem editer = new JMenuItem(this.actionEditer) ;

    this.actionRenommer = new AbstractAction (this.lnkArbreExplorateur.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("Renommer"))
        {
          public void actionPerformed(ActionEvent e)
          {
            lnkArbreExplorateur.askRenommer(_objetCourantSelectionne);
          }
        } ;
    JMenuItem renommer = new JMenuItem(this.actionRenommer) ;

	this.actionSupprimer = new AbstractAction (this.lnkArbreExplorateur.lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("Supprimer"))
		{
		  public void actionPerformed(ActionEvent e)
		  {
			lnkArbreExplorateur.supprimer(_objetCourantSelectionne);
		  }
		} ;
	JMenuItem supprimer = new JMenuItem(this.actionSupprimer) ;

    this.popup.add(editer);
    this.popup.add(renommer);
	this.popup.add(supprimer);
  }

  public void afficherMenu(Component compo, int x ,int y, boolean estFichier){
    this.actionEditer.setEnabled(estFichier);
    this.actionRenommer.setEnabled(estFichier);
    this.popup.show(compo,x,y);
  }
}