/*
 * IEPP: Isi Engineering Process Publisher
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
 * 
 */

package iepp.application.ageneration;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.Enumeration;

import iepp.Application;
import iepp.domaine.*;
import iepp.ui.iedition.dessin.vues.MDComposantProcessus;
import iepp.ui.iedition.dessin.vues.MDProduit;

/**
 * Classe contenant le code html utilisé pour créer les pages principales du site web
 * et pour créer des map area du diagramme d'assemblage des composants
 */
public class CodeHTML 
{

	/**
	 * Retourne le code html de la page index.html
	 * Page découpée en 2 frames
	 * @param nomProcessus nom du processus servant de titre à la page
	 * @return code html de la page
	 */
	public static String getPageAccueil(String nomProcessus)
	{
		return 
		"<html>\n"
		+"<head>\n"
		+ "<title>"		+ nomProcessus
		+ "</title>\n"
		+ "</head>\n"
		+ "<!-- (Please keep all copyright notices.) This frameset document includes the Treeview script. Script found in: http://www.treeview.net Author: Marcelino Alves Martins"
		+ "You may make other changes, see online instructions, but do not change the names of the frames (treeframe and basefrm) -->\n\n"
		+ "<FRAMESET cols=\"250,*\">\n" 
		+"	<frame src=\"applet/arbre.html\" name=\"arbre\">\n"
	    +"	<frame src=\"main.html\" name=\"basefrm\">\n"
		+"</FRAMESET>" 
		+"</HTML>";
	}
	
	
	/**
	 * Retourne le code html de la page main.html
	 * La page contient un titre encadré, le diagramme d'assemblage et des commentaires
	 * @param nomProcessus nom du processus servant de titre à la page
	 * @return code html de la page
	 * @throws IOException
	 */
	public static String getPagePrincipale(String nomProcessus) throws IOException
	{

		String retour = 
			
		"<html><head><title>" + nomProcessus + "</title>"
		+ "<link rel='STYLESHEET' type='text/css' href='styles/" + GenerationManager.getInstance().getFeuilleCss() + "'>"
		+"</head>\n"
		+ "<body><center>\n"
		+ "<table width=\"84%\" align=\"center\">\n"
		+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
		+ "<p align=\"center\">\n"
		+ "<b><div align=\"center\" class=\"titreProcessus\">" + nomProcessus + "</div></b>\n"
		+ "</p></td></tr></table></center><BR><BR> \n" ;
		
		if (!GenerationManager.getInstance().estContenuAvant())
		{
		    retour +=
		    	"<div align=\"center\" class=\"imgdiagramme\">"
				+ construireImageMap()
				+ "<IMG src=\"main.png\" usemap=\"#diag_principal\" >" 
				+ "<p><i>" + Application.getApplication().getTraduction("WEB_DIAG_ASM") + "</i></p></div>\n";
		}
		
		File f = new File(Application.getApplication().getProjet().getDefProc().getFichierContenu());
		if (f.exists())
		{
			FileReader fr = new FileReader(f);
			BufferedReader  br = new BufferedReader(fr);
			
			String ligne ;
			char[] retourChariot = new char[]{Character.LINE_SEPARATOR};
			while ((ligne = br.readLine()) != null)
			{
				retour += ligne + new String(retourChariot);
			}
		}
		else
		{
			System.out.println("Pas de fichier contenu ou le fichier n'existe pas : " + f.getName());
		}

		if (GenerationManager.getInstance().estContenuAvant())
		{
		    retour +=
		    	"<div align=\"center\" class=\"imgdiagramme\">"
				+ construireImageMap()
				+ "<IMG src=\"main.png\" usemap=\"#diag_principal\" >" 
				+ "<p><i>" + Application.getApplication().getTraduction("WEB_DIAG_ASM") + "</i></p></div>\n";
		}
		
		retour += "<hr><div class=\"commentaires\" align=\"justify\">"
		+ Application.getApplication().getProjet().getDefProc().getCommentaires() + "</div>"
		+ "<div align=\"center\" class=\"boutonemail\"><a href=\"mailto:" + Application.getApplication().getProjet().getDefProc().getEmailAuteur() + "?subject=" +  nomProcessus + "\">"
		+ Application.getApplication().getTraduction("WEB_MAIL") + "</A></div>"
		+ "<div align=\"center\" class=\"date\">" + Application.getApplication().getTraduction("WEB_DATE_GEN")
		+ " "+ GenerationManager.getInstance().getDateGeneration() + "</div>"
		+ "</body></html>";
		return retour;
	}
	
	/**
	 * Retourne le code html de la map du diagramme s'assemblage
	 * Construction de la balise Image pour rendre certaines zones cliquables
	 * @return code html de la map area
	 */
	public static String construireImageMap()
	{
		
		String result = "<map name=\"diag_principal\">";

		// recuperer toutes les figures presentes sur le diagrammes
		Enumeration enumer = Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe().getModele().modeleFigures() ;
		// parcours de la liste et verification de l'existence des activites
		while (enumer.hasMoreElements ()) {
			
			Object aux = enumer.nextElement();
			IdObjetModele io = null;
			int x, y, l, h = 0;
			if ( aux instanceof MDComposantProcessus )
			{
				MDComposantProcessus comp = (MDComposantProcessus)aux;
				// récupérer les coordonnées de l'activité dans le diagramme
				// pour faire le carré de la zone cliquable
				x = comp.getX();
				y = comp.getY();
				l = x + comp.getLargeur();
				h = y + comp.getHauteur();
				
				io = comp.getId();
				
				
				// Verifier s'il ne s'agit pas d'un composant vide, auquel cas il ne faut pas creer de zone
				if (!io.estComposantVide())
				{
					result += "<area shape=\"rect\" coords =\"" + x + "," + y + "," + l + "," + h + "\" href=\"" + io.getChemin() + "\">\n";
				}
			}
			else if ( aux instanceof MDProduit )
			{
				MDProduit prod = (MDProduit)aux;
				// récupérer les coordonnées de l'activité dans le diagramme
				// pour faire le carré de la zone cliquable
				x = prod.getX();
				y = prod.getY();
				l = x + prod.getLargeur();
				h = y + prod.getHauteur();
	
				io = prod.getId();
				
				// Verifier qu'il ne s'agit ni d'un produit exterieur, ni d'un produit fusion de deux elements exterieurs (auquel cas le chemin est vide)
				if (!GenerationManager.getListeProduitsExterieurs().contains(io) && ! io.getChemin().equals(""))
				{
					result += "<area shape=\"rect\" coords =\"" + x + "," + y + "," + l + "," + h + "\" href=\"" + io.getChemin() + "\">\n";
				}
			}
		}
		return  result + "</map>\n";
	}
	
	/**
	 * Retourne le code html de la page arbre.html qui contient l'arbre
	 * de navigation du site
	 * @return code html de la page contenant l'arbre
	 */
	public static String getArbre()
	{
		String corps = 
		"<html><head>\n" +		"<style>\n" +		"BODY {background-color: white}\n" +		"TD {font-size: 10pt;\n" +
	 	"font-family: verdana,helvetica;\n" + 
		"text-decoration: none;\n" +
		"white-space:nowrap;}\n" +
		"A  {text-decoration: none;\n" +
		"color: black}\n" +		".specialClass {font-family:garamond; font-size:12pt;color:green;font-weight:bold;text-decoration:underline}\n" +
		"</style>\n" +
		"<script src=\"ua.js\"></script>\n" +
		"<script src=\"ftiens4.js\"></script>\n"+
		"<script src=\"tree.js\"></script>\n"
		+ "<script>\n"
		+ "function op() {}\n"
		+ "</script>\n"
		+ "</head>\n"+
		"<body topmargin=16 marginheight=16>\n"+
		"<div style=\"position:absolute; top:0; left:0; \"><table border=0><tr><td><font size=-2><a style=\"font-size:7pt;text-decoration:none;color:silver\" href=\"http://www.treemenu.net/\" target=_blank>JavaScript Tree Menu</a></font></td></tr></table></div>\n"+
		"<script>initializeDocument()</script>\n"+
		"<noscript>\n"+
		"A tree for site navigation will open here if you enable JavaScript in your browser.\n"+
		"</noscript>\n"+
		"</html>\n";
		return corps ;
	}

	
	/**
	 * Méthode permettant de normaliser le nom d'une page web pour créer des liens
	 * qui seront valides sur tous les systèmes en enlevant les accents, les espaces etc
	 * @param name nom a normaliser
	 * @return la même chaine de caractères mais normalisée
	 */
	public static String normalizeName(String name)
	{
		name=name.replace(' ','_');
		name=name.replace('\'','_');
		name=name.replace('-','_');
		name=name.replace(':','_');
		name=name.replace('\\','_');
		name=name.replace('/','_');
		name=name.replace('*','_');
		name=name.replace('?','_');
		name=name.replace('"','_');
		name=name.replace('<','_');
		name=name.replace('>','_');
		name=name.replace('|','_');
		name=name.replace('é','e');
		name=name.replace('è','e');
		name=name.replace('ù','u');
		name=name.replace('ç','c');
		name=name.replace('à','a');
		name=name.replace('ô','o');
		name=name.replace('+','_');
		name=name.replace('(','_');
		name=name.replace(')','_');
		return name;
	}
}
