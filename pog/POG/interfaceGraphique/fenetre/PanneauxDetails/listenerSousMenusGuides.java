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

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;

import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauDetail;
import POG.interfaceGraphique.action.ControleurPanneaux;



/**
 * Classe pour les listeners des guides
 * <p>Title: POG</p>
 * <p>Description: Presentation Organisation Generation de composant</p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */


public class listenerSousMenusGuides implements ActionListener {
  String _typeGuide;
   ControleurPanneaux _cpan;
   PanneauDetail _pan;
   public listenerSousMenusGuides(String typeGuide, PanneauDetail p, ControleurPanneaux cp) {
     this._typeGuide = typeGuide;
     this._cpan = cp;
     this._pan = p;
   }
   public void actionPerformed(ActionEvent evt) {
   //  System.out.println("Ajout d1 guide de type  : " + this._typeGuide + " a l'element " + this._pan.get_elementCourant());
     this._cpan.getLnkSysteme().ajouterGuide(_pan.get_elementCourant(), _typeGuide);
   }
}