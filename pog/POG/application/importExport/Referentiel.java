/*
 * Created on 13 mai 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package POG.application.importExport;

import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import java.io.File;
import java.util.Vector;

import org.xml.sax.AttributeList;
import org.xml.sax.HandlerBase;
import org.xml.sax.SAXException;

/**
 * @author c82aber
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class Referentiel {

	public Referentiel() {
		try {
			File fileXML = new File(System.getProperty("user.home") + File.separator + ".apes2" + File.separator + "referentiel.xml");
			if (!fileXML.exists())
				return;
			SAXParserFactory factory = SAXParserFactory.newInstance();
			SAXParser saxParser = factory.newSAXParser();
			parseur monpar = new parseur((float)fileXML.length());
			saxParser.parse( fileXML, monpar);
		}
		catch (Exception e) {
//		  d.debogage("Erreur lors du chargement du fichier : " + e.getMessage());
		  e.printStackTrace();
		}
	  }

	private Vector listeComposant = new Vector();

	public class Composant {
		public String cheminpog = "";
		public String cheminpre = "";
		public String synchroniser = "";
	}

	  private class parseur extends HandlerBase {

		private String _curnom = "";
		private Composant _tmpcompo = new Composant();
		private String tmpcontent = "";
		private float lgafaire;
		private float lgfaite;

		public parseur(float lgaf) {
			lgafaire = lgaf;
		}

		public void startElement(String name, AttributeList atts) throws SAXException {
			lgfaite = lgfaite + (float)name.length();
		  	_curnom = _curnom + "." + name;
		}

		public void endElement(String name) throws SAXException {
			lgfaite = lgfaite + (float)name.length();
			if (!tmpcontent.equals("")) {
				inittmpstruct(tmpcontent);
				tmpcontent = "";
			}
			if (_curnom.equals(".referentiel.composant")) {
				listeComposant.add(_tmpcompo);
				_tmpcompo = new Composant();
			}
		  	_curnom = _curnom.substring(0, _curnom.lastIndexOf("."));
		}

		public void characters(char[] charArray, int start, int length) throws SAXException {
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
//			  System.out.println("CHARGE: " + _curnom + " -> " + content);
			if (_curnom == null)
			  _curnom = new String(content);
			else if (_curnom.equals(".referentiel.composant.cheminpre"))
				_tmpcompo.cheminpre = new String(content);
			else if (_curnom.equals(".referentiel.composant.cheminpog"))
				_tmpcompo.cheminpog = new String(content);
			else if (_curnom.equals(".referentiel.composant.synchroniser"))
				_tmpcompo.synchroniser = new String(content);
		}
	  }
	
	/**
	 * @return
	 */
	public Vector getListeComposant() {
		return listeComposant;
	}

}
