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

import java.io.File;
import java.io.OutputStreamWriter;
import java.net.URI;
import java.util.Enumeration;
import java.util.Vector;

import javax.swing.ImageIcon;

import POG.application.importExport.Importer;
import POG.application.sauvegarde.Sauvegarde;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.interfaceGraphique.utile.trace.Debug;
import POG.utile.propriete.Preferences;

/**
 * @stereotype entity
 */
public class ElementPresentation implements Sauvegarde {

  protected ImageIcon _icone;
  protected String _nomPresentation = null;
  protected String _id;
  protected String _description = new String("");

//  public FenetrePrincipale lnkFenetrePrincipale;
//  public Bibliotheque lnkBibliotheque;

  public Guide _chargement_guide;


  /**
   * @link aggregationByValue
   *@associates objetMetier.Contenu
   * @supplierCardinality 0..1
   */
  protected Contenu lnkContenu = null;

  /**
   * @supplierCardinality 0..*
   * @directed
   * @associates objetMetier.Guide
   */
  protected Vector lnkGuide;

  public Vector getGuides()
  {
//    System.out.println("Guide pour: " + _id + " " + _nomPresentation + " nb: " + lnkGuide.size());
    return lnkGuide;
  }


  public String toString(){
    return this._nomPresentation;
  }

	/*
	 * UNIQUEMENT POUR LES TABLEAUX
	 */
  public ElementPresentation() {
  	
  }

  public ElementPresentation(String id, ImageIcon icone) {
    _id = id;
    _icone = icone;
    _nomPresentation = new String("Element de presentation ");
    _nomPresentation = _nomPresentation.concat(_id);
    lnkGuide = new Vector();
  }

  public ImageIcon get_icone() { return _icone; }

  public String get_nomPresentation() {
    return _nomPresentation;
  }

  public String get_description() {
    return _description;
  }

  public String get_id() {
    return _id;
  }

  public void set_id(String newId){
    this._id = newId;
  }

  public Contenu getContenu(){return lnkContenu;}

  public void set_description(String _description) {
    this._description = _description;
  }

  public void set_icone(ImageIcon _icone) {
    this._icone = _icone;
  }

  public void set_nomPresentation(String _nomPresentation) {
    this._nomPresentation = _nomPresentation;
  }

  public void setContenu(Contenu contenu){lnkContenu = contenu;}

  //public void setGuide(Guide guide){lnkGuide = guide;}

  public void ajouterGuide(Guide nouveauGuide){
    this.lnkGuide.add(nouveauGuide);
  }

  public void supprimerGuide(Guide ancienGuide){
    this.lnkGuide.remove(ancienGuide);
  }


  public void sauver(OutputStreamWriter out, boolean FlagExporter) {
    String filename = new String();
    int index;
    try {

      //pas de balise identificateur_externe si pas de modele en entree
      out.write("<identificateur_externe>");
      if (this instanceof PresentationElementModele) {
        out.write( ( (PresentationElementModele)this).getIdentificateurExterne());
      }
      out.write("</identificateur_externe>\n");

      out.write("<identificateur_interne>");
      out.write(_id);
      out.write("</identificateur_interne>\n");

      out.write("<nom_presentation>");
      out.write(_nomPresentation);
      out.write("</nom_presentation>\n");

      out.write("<icone>");
      filename = Preferences.MyInstance.getCheminIcon(get_icone());
      if (FlagExporter) {
        index = filename.lastIndexOf(File.separator);
        if (index == -1)
          index = filename.lastIndexOf("/");
        //A revoir : / en fin de chemin
        filename = filename.substring(index + 1);
      }
      out.write(filename);
      out.write("</icone>\n");

      out.write("<contenu>");
      if (lnkContenu != null)
        if (FlagExporter)
          out.write(this.lnkContenu.get_uri());
        else
          out.write(this.lnkContenu.getRelativeToBiblioPath());
      out.write("</contenu>\n");

      out.write("<description>");
      out.write(_description);
      out.write("</description>\n");

	  if ((this instanceof PresentationElementModele) && !FlagExporter) {
		PresentationElementModele toto = (PresentationElementModele)this;
		out.write("<nominmodele>");
		out.write(toto.get_nominmodel());
		out.write("</nominmodele>\n");
		out.write("<placearbre>");
		out.write(toto.get_placearbre());
		out.write("</placearbre>\n");
	  }

      if (lnkGuide.size() != 0) {
        Enumeration enum = this.lnkGuide.elements();
        while (enum.hasMoreElements()) {
          out.write("<guide>");
          ( (Guide) enum.nextElement()).sauver(out, FlagExporter);
          out.write("</guide>\n");
        }
      }
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  private class tmp_charger_guide {
    public String identificateur_interne = "";
    public String nom_presentation = "";
    public String type = "";
    public String icone = "";
    public String contenu = "";
    public String description = "";
  }

  private tmp_charger_guide _tmp_charger_guide = new tmp_charger_guide();

  public void charger(String cle, String valeur) {
    if (cle.equals(".exportation_presentation.element.guide.identificateur_interne")) {
      _tmp_charger_guide.identificateur_interne = new String(valeur);
    }
    else if (cle.equals(".exportation_presentation.element.guide.nom_presentation"))
      _tmp_charger_guide.nom_presentation = new String(valeur);
    else if (cle.equals(".exportation_presentation.element.guide.type"))
      _tmp_charger_guide.type = new String(valeur);
    else if (cle.equals(".exportation_presentation.element.guide.icone"))
		if (valeur.startsWith("POG")) {
			int index = valeur.lastIndexOf(File.separator);
			if (index == -1)
			  index = valeur.lastIndexOf("/");
			try {
				String n = valeur.substring(index);
				_tmp_charger_guide.icone = new String(n.substring(1, n.length() - 4));
			}
			catch (Exception e) {
				_tmp_charger_guide.icone = "";
			}
		  }
		  else
    	  	_tmp_charger_guide.icone = new String(Importer.pathicon + valeur);
    else if (cle.equals(".exportation_presentation.element.guide.contenu"))
      _tmp_charger_guide.contenu = new String(Importer.pathcontenu + valeur);
    else if (cle.equals(".exportation_presentation.element.guide.description")) {
      _tmp_charger_guide.description = new String(valeur);
      //ajout de l'element du guide a la presentation
    }
  }

  public Guide finirChargement(String valeur, Bibliotheque lnkBibliotheque) {
    if (valeur.equals(".exportation_presentation.element.guide")) {
      _tmp_charger_guide.contenu = _tmp_charger_guide.contenu.replace('/', File.separatorChar);
      _tmp_charger_guide.contenu = _tmp_charger_guide.contenu.replace('\\', File.separatorChar);
      _tmp_charger_guide.icone = _tmp_charger_guide.icone.replace('/', File.separatorChar);
      _tmp_charger_guide.icone = _tmp_charger_guide.icone.replace('\\', File.separatorChar);

      _chargement_guide = new Guide(_tmp_charger_guide.identificateur_interne, Preferences.MyInstance.getIconeDefaut(_tmp_charger_guide.icone), _tmp_charger_guide.type);
      _chargement_guide.set_nomPresentation(_tmp_charger_guide.nom_presentation);
	  _chargement_guide.set_description(_tmp_charger_guide.description);
	  if (!_tmp_charger_guide.contenu.equals("")) {
		try {
			URI urr = new URI(_tmp_charger_guide.contenu.replaceAll("\\\\", "/"));
			if (urr.getScheme() == null)
				throw new Exception();
			_chargement_guide.setContenu(new Contenu(urr, lnkBibliotheque.getAbsolutePath()));
		} catch (Exception e) {
			File toto = new File(_tmp_charger_guide.contenu);
			if (!toto.exists())
				toto = new File(lnkBibliotheque.getAbsolutePath() + File.separator + _tmp_charger_guide.contenu);
			if (toto.exists())
				_chargement_guide.setContenu(new Contenu(toto.toURI(), lnkBibliotheque.getAbsolutePath()));
			else
				FenetrePrincipale.INSTANCE.getLnkDebug().debogage(FenetrePrincipale.langue("attachefich").replaceFirst("ARG0", _tmp_charger_guide.contenu).replaceFirst("ARG1", _tmp_charger_guide.nom_presentation));
		}
	  }

      
      ajouterGuide(_chargement_guide);
      _tmp_charger_guide = new tmp_charger_guide();
      return _chargement_guide;
    }
    //pas de else car les guides sont au dernier niveau
    return null;
  }

  public boolean estValide(Debug lnkDebug){
  	boolean ok = true;
  	
    //verifier que chaque element a un fichier ou une description associe
    if (this.get_description().equals("")) {
      //verifier que cet element a un fichier associe
      if (this.getContenu() == null){
      	String mess = FenetrePrincipale.langue("messverifdesc");
      	mess = mess.replaceFirst("ARG0", this.get_nomPresentation());
        lnkDebug.verificationMessage(mess);
        ok = false;
      }
      else {
        //s'il a un fichier associe, verifier que le lien existe
		if (this.getContenu().isFile()) {
			if (!new File(this.getContenu().getAbsolutePath()).exists()) {
				String mess = FenetrePrincipale.langue("messverifrep");
				mess = mess.replaceFirst("ARG0", this.get_nomPresentation());
				mess = mess.replaceFirst("ARG1", this.lnkContenu.getAbsolutePath());
				lnkDebug.verificationMessage(mess);
				ok = false;
			}
        }
      }
    }
    if (this instanceof Guide)
      //verifier que chaque guide a un contenu
       ok = (ok && ((Guide)this).estValide(lnkDebug));
    else if (this instanceof PresentationElementModele)
		ok = (ok && ((PresentationElementModele)this).estValide(lnkDebug));

    return ok;
  }
}
