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

import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Vector;

import javax.swing.ImageIcon;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.Parser;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.ParserFactory;

import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;

public class ControleurGuide {

  private HashMap _lienElementGuide = new HashMap();

  /**
   * @directed
   * @supplierCardinality 0..1
   */
  private Guide lnkGuide;
  private class parseur extends HandlerBase {
    private String _curnom;
    private Vector _currel;

    public void startElement(String name, AttributeList atts) throws SAXException {
      if (name.equals("nom"))
        _curnom = null;
    }

    public void endElement(String name) throws SAXException {
      if (name.equals("guide")) {
        _lienElementGuide.put(_curnom, _currel);
        _currel = new Vector();
      }
    }

    public void characters(char[] charArray, int start, int length) throws SAXException {
      String content = new String(charArray, start, length);
      if (_curnom == null)
        _curnom = new String(content);
      else
        _currel.add(content);
    }
  }

  public ControleurGuide(String fileXML) {
    try {
      Parser parser = ParserFactory.makeParser("org.apache.xerces.parsers.SAXParser");
      parser.setDocumentHandler(new parseur());
      parser.parse(fileXML);
    }
    catch (Exception e) {
      // Type par defaut :
      String nom = new String("DefTrav");
      Vector vect = new Vector();
      vect.add("Concept");
      vect.add("Article");
      vect.add("Technique");
      _lienElementGuide.put(nom, vect);

      nom = new String("Role");
      vect = new Vector();
      vect.add("Concept");
      vect.add("Article");
      _lienElementGuide.put(nom, vect);

      nom = new String("Activite");
      vect = new Vector();
      vect.add("Technique");
      vect.add("Article");
      vect.add("Guide Outil");
      _lienElementGuide.put(nom, vect);

      nom = new String("Produit");
      vect = new Vector();
      vect.add("Guide de redaction");
      vect.add("Liste de controles");
      vect.add("Plan Type");
      vect.add("Exemple");
      _lienElementGuide.put(nom, vect);

      nom = new String("ElementDePresentation");
      vect = new Vector();
      Collection elements = this._lienElementGuide.values() ;
      for (Iterator it = elements.iterator() ; it.hasNext() ; )
      {
        Vector v = (Vector)it.next() ;
        for (Iterator it1 = v.iterator() ; it1.hasNext() ; )
        {
          String s = (String)it1.next();
          if (!vect.contains(s))
            vect.add(s);
        }
      }
      _lienElementGuide.put(nom, vect);
    }
  }

/* cette fonction est-elle utile ? il semble que non :
  public void sauver(OutputStreamWriter out, boolean FlagExporter) {
    try {
      String nom;
      for (Iterator it = _lienElementGuide.keySet().iterator(); it.hasNext(); ) {
        nom = (String) it.next();
        out.write("<guide>" + System.getProperty("line.separator"));
        out.write("\t<nom>" + nom  + "</nom>" +  System.getProperty("line.separator"));
        Object [] vect = ((Vector)_lienElementGuide.get(nom)).toArray();
        for (int i = 0; i < vect.length; i++)
          out.write("\t<lien>" + (String)vect[i]  + "</lien>" +  System.getProperty("line.separator"));
        out.write("</guide>" + System.getProperty("line.separator"));
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }
*/
  public Vector type(String eltModele) {
    return (Vector)_lienElementGuide.get(eltModele);
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
}
