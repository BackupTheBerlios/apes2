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
package POG.objetMetier;

import java.util.HashMap;
import java.util.List;

import javax.swing.ImageIcon;
import java.io.OutputStreamWriter;
import java.util.Vector;

import POG.application.importExport.Apes;
import POG.utile.propriete.Preferences;
import POG.application.sauvegarde.Sauvegarde;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.PresentationElementModele;
import POG.objetMetier.Guide;
import POG.objetMetier.Contenu;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.interfaceGraphique.utile.trace.Debug;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.core.ModelElement;
import java.io.File;
import java.util.Iterator;
import java.util.Hashtable;
import java.util.Enumeration;
import POG.application.importExport.Importer;

public class Presentation
    implements Sauvegarde {

  public String _pathModele = null;
  private List _idElements = null;
  private String _nomPresentation = new String();
  private String _IdRacine;
  private String _pathBibli = new String();
  private ImageIcon _icone;
//  public FenetrePrincipale lnkFenetrePrincipale;

  public ElementPresentation _chargement_element;

  /**
   * @link aggregationByValue
   *@associates objetMetier.ElementPresentation
   * @supplierCardinality 1..*
   */
  private HashMap lnkElementPresentation = new HashMap();

  private static int NBELM = 0;

  /**
   * @supplierCardinality 1
   * @link aggregationByValue
   */
  public Bibliotheque lnkBibliotheque = null;

  /**
   * @supplierCardinality 1
   * @directed
   */
  public ProcessComponent lnkProcessComponent;
  public Presentation(String name, String path, ImageIcon icone) {
    this.lnkElementPresentation.clear();
    _pathModele = null;
    _idElements = null;
    _nomPresentation = name;
    _pathBibli = path;
    _icone = icone;

    this.lnkBibliotheque = new Bibliotheque(path);
    this._idElements = new Vector();
    ElementPresentation elementRacine = new ElementPresentation(this.makeId("0"),
        icone);
    this._IdRacine = elementRacine.get_id();
    elementRacine.set_nomPresentation(name);
    this.ajouterElementPresentation(elementRacine);
  }

  public Presentation(String name, String path, ImageIcon icone,
                      ProcessComponent ap) {
    this.lnkElementPresentation.clear();
    _idElements = null;
    _pathModele = null;
    _icone = icone;
    _pathBibli = path;
    _nomPresentation = name;
    this.lnkBibliotheque = new Bibliotheque(path);
    this._idElements = new Vector();
    PresentationElementModele elementRacine = new PresentationElementModele(this.
        makeId("0"), icone, ap);
    this._IdRacine = elementRacine.get_id();
//    elementRacine.set_nomPresentation(name);
    this.ajouterElementPresentation(elementRacine);
    lnkProcessComponent = ap;
  }

  public ElementPresentation getElementPresentation(String idElem) {
    return (ElementPresentation) lnkElementPresentation.get(idElem);
  }

  public String getIdRacine() {
    return this._IdRacine;

  }

  public String get_nomPresentation() {
    return _nomPresentation;
  }

  public List get_idElements() {
    return this._idElements;
  }

 public void setModele(ProcessComponent ap)
 {
   this.lnkProcessComponent = ap;
 }
  /**
   * Echange les positions de 2 elements grace aux ids, ainsi que leurs fils
   * par recursivite : element e1 et e2 de MEME niveau.
   * JAMAIS LES 2 !!!
   * @param id1
   * @param id2
   */
  public Hashtable echangerPositions(ElementPresentation e1,
                                     ElementPresentation e2) {
    Hashtable Map1 = this.suppressionRecursive(e1.get_id());
    Hashtable Map2 = this.suppressionRecursive(e2.get_id());
    Hashtable retour = new Hashtable();
    String id1 = e1.get_id();
    String id2 = e2.get_id();
    Map1 = this.setPositions(Map1, id1, id2);
    Map2 = this.setPositions(Map2, id2, id1);

    Enumeration enum = Map1.keys();
    while (enum.hasMoreElements()) {
      this.ajouterElementPresentation( (ElementPresentation) Map1.get( (String)
          enum.nextElement()));
    }
    enum = Map2.keys();
    while (enum.hasMoreElements()) {
      this.ajouterElementPresentation( (ElementPresentation) Map2.get( (String)
          enum.nextElement()));
    }
    retour.putAll(Map1);
    retour.putAll(Map2);
    return retour;
  }

  /**
   * Change les ids des elements de la Map suivant le nouvel identifiant :
   * nouveauIdPere
   * @param map
   * @param ancienIdPere
   * @param nouveauIdPere
   * @return
   */
  private Hashtable setPositions(Hashtable map, String ancienIdPere,
                                 String nouveauIdPere) {
    ElementPresentation elt;
    int newNumber;
    String ancienId;
    String fin = "";
    String ID;
    Hashtable mapRes = new Hashtable();

    int index = ancienIdPere.length() - 1;
    int indexEnd = ancienIdPere.length();
    while ( (java.lang.Character.isDigit( (ancienIdPere.charAt(index)))) &&
           index >= 0) {
      index--;
    }
    newNumber = Integer.parseInt(nouveauIdPere.substring(index + 1));
    int nb = map.size();
    Enumeration enum = map.keys();
    while (enum.hasMoreElements()) {
      String next = (String) enum.nextElement();
          elt = (ElementPresentation) map.remove(next);
      ancienId = elt.get_id();
      ID = (ancienIdPere.substring(0, index + 1) + newNumber);
      try {
        fin = ancienId.substring(indexEnd);
      }
      catch (Exception e) {
      }
      if (fin != "") {
        ID += fin;
      }
      elt.set_id(ID);
      fin = "";
      mapRes.put(elt.get_id(), elt);
    }
    return mapRes;
  }

  public String makeId(String idPere) {
    String id;
    if (idPere == "0") {
      id = "1";
    }
    else {
      int numFils = 1;
      while ( (this.getElementPresentation(idPere + "-" + numFils)) != null) {
        numFils++;
      }
      id = idPere + "-" + numFils;
    }

    //System.out.println("make ID : " + id + " pour pere d'id " + idPere);
    return id;
  }

  /**
   * Suppression d'un element \uFFFD partir d'un id : remontee des autres + SUPPRESSION de l'element...
   * @param id
   * @return L'objet supprim\uFFFD de la liste
   */
  public Hashtable removeElementAndUp(String id) {
    int index = id.length() - 1;
    // System.out.println("Remove elmeent et id de id : " + id);
    while ( (java.lang.Character.isDigit( (id.charAt(index)))) &&
           index >= 0) {
      index--;
    }
    String idPere = id.substring(0, index);
    // numFils = position de de l'element d'id id dans le pere de id
    int numFils = Integer.parseInt(id.substring(index + 1));

    numFils++;
    String idSuiv = idPere + "-" + numFils;

    // Suppression de l'element et des fils recursivement
    Hashtable table = this.suppressionRecursive(id);

    // cas : il existe des freres qui suivent : les remonter, ainis que leurs fils
    String idAvant = id;
    while (this.getElementPresentation(idSuiv) != null) {
      ElementPresentation e = this.getElementPresentation(idSuiv);
      remonterIds(e, 1);
      numFils++;
      idSuiv = idPere + "-" + numFils;
    }
    return table;
  }

  /**
   * Suppresion de l'element d'identifiant id ansi que tout ses fils
   * @param id L'id de l'element
   * @return Une HashMap des elements supprimes
   */
  private Hashtable suppressionRecursive(String id) {
    Hashtable mapSupprimes = new Hashtable();
    ElementPresentation elt = this.supprimerElementPresentation(id);
    mapSupprimes.put(id, elt);
    // Suppression des fils de cet element
    int numFils = 1;
    String idFils = id + "-" + numFils;
    while (this.getElementPresentation(idFils) != null) {
      mapSupprimes.putAll(this.suppressionRecursive(idFils));
      numFils++;
      idFils = id + "-" + numFils;
    }
    return mapSupprimes;
  }

  /**
   * Remonte l'id de l'element passe en paramtere et ses fils recursivement en
   * SUPPRIMANT les elements aux ids au-dessus (ids ecrases)
   * @param elt L'element
   * @param colonne Le numero de colonne dans l'id \uFFFD partir de la droite
   * (fin <=> 1; juste avant : 2; ...)
   */

  private void remonterIds(ElementPresentation eltPere, int colonne) {
    // remonter l'el
    // puis remonter les fils recursivement ////////////////////////////////////////////////
    String idPereAvant = eltPere.get_id();
    int indexEnd; // index de fin du nombre (index non compris)
    int index = idPereAvant.length();
    indexEnd = index;
    // System.out.println("Remove elmeent et id de id : " + id);
    for (int h = 0; h < colonne; h++) {
      indexEnd = index;
      index--;
      while ( (java.lang.Character.isDigit( (idPereAvant.charAt(index))))) {
        index--;
      }
    }
    String newId = idPereAvant.substring(0, index + 1) +
        (Integer.parseInt(idPereAvant.substring(index + 1, indexEnd)) - 1);
    if (! (indexEnd == idPereAvant.length())) {
      newId += idPereAvant.substring(indexEnd, idPereAvant.length());
    }
    eltPere.set_id(newId);
    // Remontee de l'element Pere
    this.supprimerElementPresentation(idPereAvant);
    this.lnkElementPresentation.put(eltPere.get_id(), eltPere);

    int numFils = 1;
    String idFils = idPereAvant + "-" + numFils;
    while (this.getElementPresentation(idFils) != null) {
      ElementPresentation eee = this.getElementPresentation(idPereAvant +
          "-" + numFils);
      remonterIds(eee, colonne + 1);
      numFils++;
      idFils = idPereAvant + "-" + numFils;
    }
  }

  public void ajouterElementPresentation(ElementPresentation elmt) {
    lnkElementPresentation.put(elmt.get_id(), elmt);
    this._idElements.add(elmt.get_id());
  }

  public ElementPresentation getElementParent(String idFils) {
    if (idFils == "1") {
      return null;
    }
    else {
      int index = idFils.length() - 1;
      // System.out.println("Remove elmeent et id de id : " + id);
      while ( (java.lang.Character.isDigit( (idFils.charAt(index)))) &&
             index >= 0) {
        index--;
      }
      String idPere = idFils.substring(0, index);
      return this.getElementPresentation(idPere);
    }
  }

  /**
   * Suppression de la liste de ids et de la liste des Elements de Presentation
   * @param id
   * @return
   */
  public ElementPresentation supprimerElementPresentation(String id) {
    Iterator it = this._idElements.iterator();
    int pos = 0;
    while (it.hasNext() && pos != -1) {
      if ( ( (String) it.next()) == id) {
        this._idElements.remove(pos);
        pos = -2;
      }
      pos++;
    }
    return (ElementPresentation)this.lnkElementPresentation.
        remove(id);
  }

  public void supprimerElementPresentation(ElementPresentation elmt) {
    lnkElementPresentation.remove(elmt.get_id());
    _idElements.remove(elmt.get_id());
  }

  public void sauver(OutputStreamWriter out, boolean FlagExporter) {
    String filename = new String();
    int index;
    try {
      Object[] elmtPres = this.listeElementPresentation();

      out.write("<?xml version=\"1.0\" encoding=\"UTF-8\"?>");
      out.write("<exportation_presentation>");

      out.write("<proprietes>");
      out.write("<nom_presentation>");
      out.write(this._nomPresentation);
      out.write("</nom_presentation>\n");

      //Recuperer le chemin de la bibliotheque
      out.write("<chemin_contenus>");
      if (FlagExporter) {
        out.write("Contenu" + File.separator);
      }
      else {
        out.write(this._pathBibli);

      }
      out.write("</chemin_contenus>\n");

      out.write("<chemin_icones>");
      if (FlagExporter) {
        out.write("Icone" + File.separator);
      }
      else if (this._pathModele != null) {
        out.write("M=> " + this._pathModele);

      }
      out.write("</chemin_icones>\n");

      out.write("</proprietes>\n");

      for (int i = 0; i < elmtPres.length; i++) {
        if (! (elmtPres[i] instanceof Guide)) {
          out.write("<element>");
          ( (ElementPresentation) elmtPres[i]).sauver(out, FlagExporter);
          out.write("</element>\n");
        }
      }

      out.write("</exportation_presentation>\n");
      out.close();
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class tmp_charger_element {
    public String identificateur_externe = "";
    public String identificateur_interne = "";
    public String nom_presentation = "";
    public String icone = "";
    public String contenu = "";
    public String description = "";
  }

  private tmp_charger_element _tmp_charger_element = new tmp_charger_element();

  public void charger(String cle, String valeur) {
    if (cle.equals(".exportation_presentation.element.identificateur_externe")) {
      _tmp_charger_element.identificateur_externe = new String(valeur);
    }
    else if (cle.equals(".exportation_presentation.element.identificateur_interne")) {
      _tmp_charger_element.identificateur_interne = new String(valeur);
    }
    else if (cle.equals(".exportation_presentation.element.nom_presentation")) {
      _tmp_charger_element.nom_presentation = new String(valeur);
    }
    else if (cle.equals(".exportation_presentation.element.icone")) {
      if (valeur.startsWith("POG")) {
        int index = valeur.lastIndexOf(File.separator);
        if (index == -1)
          index = valeur.lastIndexOf("/");
        try {
        	String n = valeur.substring(index);
        	_tmp_charger_element.icone = new String(n.substring(1, n.length() - 4));
        }
        catch (Exception e) {
			_tmp_charger_element.icone = "";
        }
      }
      else
        _tmp_charger_element.icone = new String(Importer.pathicon + valeur);
//      System.out.println("TMPICO: "+ _tmp_charger_element.icone);
    }
    else if (cle.equals(".exportation_presentation.element.contenu")) {
      _tmp_charger_element.contenu = new String(Importer.pathcontenu + valeur);
    }
    else if (cle.equals(".exportation_presentation.element.description")) {
      _tmp_charger_element.description = new String(valeur);
      //ajout de l'element de presentation a la presentation
    }
    else if (cle.startsWith(".exportation_presentation.element.guide")) {
      //_chargement_element a ete cree dans finirChargement
      if (_chargement_element == null) {
        finirChargement("finelement");
        _flagcharge = false;
      }
      _chargement_element.charger(cle, valeur);
    }
  }

  private boolean _flagcharge = true;

  public void finirChargement(String valeur) {
    if (valeur.equals(".exportation_presentation.element.guide")) {
     //on passe la main a ElementPresentation
     ajouterElementPresentation(_chargement_element.finirChargement(valeur, lnkBibliotheque));
   }
   if (valeur.equals(".exportation_presentation.element")) {
     if (!_flagcharge)
       _flagcharge = true;
     else
       finirChargement("finelement");
     _chargement_element = null;
   }
   else if (valeur.equals("finelement")) {
      if (_tmp_charger_element.identificateur_interne.equals("1")) {
        //cas ou l'element est celui correspondant a la presentation
        _chargement_element = getElementPresentation(getIdRacine());
        _chargement_element.set_icone(Preferences.MyInstance.getIconeDefaut(_tmp_charger_element.icone));
        _chargement_element.set_description(_tmp_charger_element.description);
      }
      else {
        _tmp_charger_element.contenu = _tmp_charger_element.contenu.replace('/', File.separatorChar);
        _tmp_charger_element.contenu = _tmp_charger_element.contenu.replace('\\', File.separatorChar);
        _tmp_charger_element.icone = _tmp_charger_element.icone.replace('/', File.separatorChar);
        _tmp_charger_element.icone = _tmp_charger_element.icone.replace('\\', File.separatorChar);
        if (this.lnkProcessComponent != null) {
          ModelElement mm = Apes.getElementByID(Integer.parseInt(_tmp_charger_element.identificateur_externe));
          _chargement_element = new PresentationElementModele(_tmp_charger_element.identificateur_interne, Preferences.MyInstance.getIconeDefaut(_tmp_charger_element.icone), mm);
        }
        else
          _chargement_element = new ElementPresentation(_tmp_charger_element.identificateur_interne, Preferences.MyInstance.getIconeDefaut(_tmp_charger_element.icone));

        ajouterElementPresentation(_chargement_element);
      }
      _chargement_element.set_nomPresentation(_tmp_charger_element.nom_presentation);
      if (!_tmp_charger_element.contenu.equals("")) {
      	File toto = new File(_tmp_charger_element.contenu);
      	if (!toto.exists())
      		toto = new File(lnkBibliotheque.getAbsolutePath() + File.separator + _tmp_charger_element.contenu);
		if (toto.exists())
        	_chargement_element.setContenu(new Contenu(toto, this.lnkBibliotheque.getAbsolutePath()));
        else
        	FenetrePrincipale.INSTANCE.getLnkDebug().debogage("Impossible d'attacher le fichier " + _tmp_charger_element.contenu + " � l'�l�ment " + _tmp_charger_element.nom_presentation);
      }
      _chargement_element.set_description(_tmp_charger_element.description);

      _tmp_charger_element = new tmp_charger_element();

    }
  }

  public Object[] listeElementPresentation() {
    return lnkElementPresentation.values().toArray();
  }

  public boolean estValide(Debug lnkDebug) {
//verifier si le chemin de la biliotheque existe
    File biblio_temp = new File(this.lnkBibliotheque.getAbsolutePath());
    if (!biblio_temp.exists()) {
      lnkDebug.verificationMessage(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("messverifbibli"));
      return false;
    }

    Object [] lstelem = listeElementPresentation();
	boolean ok = true;

    for (int i = 0; i < lstelem.length; i++) {
	  FenetrePrincipale.INSTANCE.getLnkDebug().patienter(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("verifval") + (int)(((float)i / (float)lstelem.length) * 100.0) + " %");
      if (lnkProcessComponent != null)
        if (!(lstelem[i] instanceof Guide))
          if (!(lstelem[i] instanceof PresentationElementModele)) {
			String mess = FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("messverifmod");
			mess = mess.replaceFirst("ARG0", this.get_nomPresentation());
			lnkDebug.verificationMessage(mess);
        	ok = false;
          }

      	ok = (ok && ((ElementPresentation)lstelem[i]).estValide(lnkDebug));
    }
    return ok;
  }
}