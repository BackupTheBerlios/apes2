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

package POG.application.importExport;

import java.io.File;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

import POG.application.controleurMetier.ControleurPresentation;
import POG.application.controleurMetier.ControleurPresenter;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.interfaceGraphique.utile.trace.Debug;
import POG.objetMetier.Presentation;
import POG.utile.PogToolkit;
import POG.utile.ZIP;

public class Importer{
  public Presentation _chargement_presentation;
  private ControleurPresentation _ctr;

  public static String pathicon = null;
  public static String pathcontenu = null;

  public File ouvrirFichierPre(File fichP) {
    try {
      File doss = new File(fichP.getAbsolutePath().substring(0,
          fichP.getAbsolutePath().length() - 4));
      doss.mkdir();
      ZIP arch = new ZIP(fichP.getAbsolutePath());
      arch.deziper(doss);
      String fichA = doss.getAbsolutePath() + File.separator +
          fichP.getName().substring(0, fichP.getName().length() - 3) + "apes";
//      PogToolkit.copyFile(fichP.getAbsolutePath(), fichA);
      _tmp_charger_presentation.chemin_contenus = doss.getAbsolutePath();
      if (PogToolkit.fileExists(doss.getAbsolutePath() + File.separator +
                                "Component.xml")) {
//      	ZipFile tmpf = new ZipFile(fichA);
        ZIP arch2 = new ZIP(fichA);
        arch2.ajouteFichier(new File(fichP.getAbsolutePath() + File.separator +
                                     "Interfaces.xml"), "", "");
        arch2.ajouteFichier(new File(fichP.getAbsolutePath() + File.separator +
                                     "Component.xml"), "", "");
        arch2.ziper();
        _tmp_charger_presentation.chemin_icones = "M=> " + fichA;
      }
      else
        _tmp_charger_presentation.chemin_icones = doss.getAbsolutePath();
      pathicon = doss.getAbsolutePath() + File.separator;
      pathcontenu = doss.getAbsolutePath() + File.separator;
      return new File(doss.getAbsolutePath() + File.separator +
                      "Presentation.xml");
    }
    catch (Exception e) {
      e.printStackTrace();
      return null;
    }
  }

  public Importer(File fileXML, ControleurPresenter ctr, Debug d) {
    _ctr = ctr.lnkControleurPresentation;
    pathicon = "";
    pathcontenu = "";
    if (fileXML.getName().endsWith(".pre"))
      fileXML = ouvrirFichierPre(fileXML);
    try {
      SAXParserFactory factory = SAXParserFactory.newInstance();
      SAXParser saxParser = factory.newSAXParser();
      parseur monpar = new parseur((float)fileXML.length());
      saxParser.parse( fileXML, monpar);
      ctr.synchroniserApes(true);
	  FenetrePrincipale.INSTANCE.getLnkDebug().patienter("", 0, 0);
    }
    catch (Exception e) {
      d.debogage("Erreur lors du chargement du fichier : " + e.getMessage());
      e.printStackTrace();
    }
  }

  private class tmp_charger_presentation {
    public String nom_presentation = "";
    public String chemin_contenus = ""; // Chemin Biblio
    public String chemin_icones = ""; // Chemin Modele
  }

  private tmp_charger_presentation _tmp_charger_presentation = new tmp_charger_presentation();

  private class parseur extends HandlerBase {

    private String _curnom = "";

    public parseur(float lgaf) {
    	lgafaire = lgaf;
    }

    public void startElement(String name, AttributeList atts) throws
        SAXException {
			lgfaite = lgfaite + (float)name.length();
			FenetrePrincipale.INSTANCE.getLnkDebug().patienter("parse", lgfaite, lgafaire);
      _curnom = _curnom + "." + name;
    }

    public void endElement(String name) throws SAXException {
      try {
      	lgfaite = lgfaite + (float)name.length();
		FenetrePrincipale.INSTANCE.getLnkDebug().patienter("parse", lgfaite, lgafaire);
		if (!tmpcontent.equals("")) {
			inittmpstruct(tmpcontent);
			tmpcontent = "";
		}
      if (_curnom.equals(".exportation_presentation.proprietes")) {

        // Avec modele
        if (_tmp_charger_presentation.chemin_icones.startsWith("M=> ")) {
            String pathmo = _tmp_charger_presentation.chemin_icones.substring(4);
            
            // modele d�plac�
            if (!new File(pathmo).exists()) {
            	String mess = FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("moddepl");
            	mess = mess.replaceFirst("ARG0", pathmo);
            	FenetrePrincipale.INSTANCE.getLnkDebug().debogage(mess);
				File rep = PogToolkit.chooseFileAPES(FenetrePrincipale.INSTANCE);
            	if (rep == null)
            		return;
            	pathmo = rep.getAbsolutePath();
            }
            
            _ctr.nouvellePresentation(Apes.loadModel(new File(pathmo)), _tmp_charger_presentation.chemin_contenus, new File(pathmo));
        }
        // Sans  modele
        else
          _ctr.nouvellePresentation(new File(_tmp_charger_presentation.chemin_contenus), _tmp_charger_presentation.nom_presentation);
        _chargement_presentation = _ctr.getlnkPresentation();
      }
      else if (_curnom.startsWith(".exportation_presentation.element")) {
        //on passe la main a Presentation
        _chargement_presentation.finirChargement(_curnom);
      }
      _curnom = _curnom.substring(0, _curnom.lastIndexOf("."));

      }
      catch (Exception e) {
        e.printStackTrace();
      }
    }

	private String tmpcontent = "";
	private float lgafaire;
	private float lgfaite;

    public void characters(char[] charArray, int start, int length) throws
        SAXException {
        	FenetrePrincipale.INSTANCE.getLnkDebug().patienter("parse", lgfaite, lgafaire);
        	lgfaite = lgfaite + (float)length;
      	if (start + length >= charArray.length) {
			tmpcontent = new String(charArray, start, length);
      		return;
      	}
		String content = new String(charArray, start, length);
      	if (!tmpcontent.equals("")) {
			String strs = new String(charArray, start, length);
			content = tmpcontent + strs;
			tmpcontent = "";
      	}
		inittmpstruct(content);
      }

      private void inittmpstruct(String content) {

//        System.out.println("CHARGE: " + _curnom + " -> " + content);
        if (_curnom == null)
          _curnom = new String(content);
        else if (_curnom.equals(
            ".exportation_presentation.proprietes.nom_presentation"))
          _tmp_charger_presentation.nom_presentation = new String(content);
        else if (_curnom.equals(
            ".exportation_presentation.proprietes.chemin_contenus"))
          if (_tmp_charger_presentation.chemin_contenus.equals(""))
            _tmp_charger_presentation.chemin_contenus = new String(content);
          else
            pathcontenu = pathcontenu + content;
//          _tmp_charger_presentation.chemin_contenus = pathcontenu + content;
        else if (_curnom.equals(
            ".exportation_presentation.proprietes.chemin_icones"))
          if (_tmp_charger_presentation.chemin_icones.equals(""))
            _tmp_charger_presentation.chemin_icones = new String(content);
          else
            pathicon = pathicon + content;
        else if (_curnom.startsWith(".exportation_presentation.element")) {
          //_chargement_presentation a ete cree dans endElement
          _chargement_presentation.charger(_curnom, content);
        }
    }
  }
}