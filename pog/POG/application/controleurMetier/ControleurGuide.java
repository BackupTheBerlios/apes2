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


package POG.application.controleurMetier;

import java.util.HashMap;
import java.util.Iterator;

import javax.swing.ImageIcon;

import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;

public class ControleurGuide {

  private HashMap _lienguideicone;

  /**
   * @directed
   * @supplierCardinality 0..1
   */
//  private Guide lnkGuide;

  public ControleurGuide(HashMap guideicone) {
  		_lienguideicone = guideicone;
   }

  public Iterator types() {
  	return _lienguideicone.keySet().iterator();
  }

  public Guide ajouterGuide(ElementPresentation elt, String id, ImageIcon ico, String type)
  {
    Guide g = new Guide(id, ico, type);
    elt.ajouterGuide(g);
    return g;
  }

  public void supprimerGuide(ElementPresentation el, Guide gu){
    el.supprimerGuide(gu);
  }
  
  public String icone(String type) {
  	return (String) _lienguideicone.get(type);
  }

}
