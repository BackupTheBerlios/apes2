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

import iepp.Application;
import iepp.domaine.ElementPresentation;
import iepp.domaine.IdObjetModele;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

/**
 * Classe permettant de créer une page dont le contenu correspond à une Definition de travail
 */
public class GDefinitionTravail extends GElementModele
{

	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem element de présentation associé à la définition de travail courante
	 * @param pwFicTree lien vers le fichier tree.js construit durant la génération du site
	 */
	public GDefinitionTravail(ElementPresentation elem, PrintWriter pwFicTree)
	{
		super(elem, pwFicTree);
	}

	/**
	 * Méthode permettant de créer le contenu de la page associée à la définition de travail courante
	 * Affichage des listes des listes d'activités
	 */
	public void creerFichierDescription() throws IOException
	{
		//création du fichier de contenu
		File ficHTML = new File (this.cheminAbsolu) ;
		FileWriter fd = new FileWriter(ficHTML);

		fd.write("<HTML><head> <link rel='STYLESHEET' type='text/css' href='" + this.getCheminStyle() + "'>"
					+ "</head>" + "<body><center>\n"
					+ "<table width=\"84%\" align=\"center\">\n"
					+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
					+ "<p align=\"center\" class=\"entete\">\n"
					+ "<b>" + this.element.getNomPresentation() + "</b>\n"
					+ "</p></td></tr></table></center><BR>\n");

		fd.write(getBarreNavigation() + "<br>");

		// tableau
		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ACTIVITES") + " </div>\n");
		Vector listeActivites = this.modele.getActivite();
		for (int i = 0; i < listeActivites.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeActivites.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}
		
		this.ajouterDescription(fd);
		this.ajouterContenu(fd);
		this.ajouterMail(fd);
		this.ajouterVersionDate(fd);
		fd.write("</BODY></HTML>") ;
		fd.close();
	}

}