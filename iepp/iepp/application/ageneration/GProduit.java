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

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;

/**
 * Classe permettant de cr�er une page dont le contenu correspond � un produit
 */
public class GProduit extends GElementModele
{

	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� � l'activit� courante
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GProduit(ElementPresentation elem, PrintWriter pwFicTree)
	{
		super(elem,  pwFicTree);
	}
	
	/**
	 * M�thode permettant de cr�er le contenu de la page associ�e � l'activit� courante
	 * Affichage des listes d'activit�s et du r�le responsable du produit
	 */
	public void creerFichierDescription() throws IOException
	{
		// cr�ation du fichier de contenu
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
		
		// affiche les activit�s dont le produit est en entr�e
		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ENTREE_ACT") + " </div>\n");
		Vector listeActivites = this.modele.getActiviteEntree();
		for (int i = 0; i < listeActivites.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeActivites.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}
		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_SORTIE_ACT") + " </div>\n");
		
		listeActivites = this.modele.getActiviteSortie();
		for (int i = 0; i < listeActivites.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeActivites.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}
		
		//affiche le r�le responsable de l'activit�
		 fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ROLE_RESP") + "</div>\n");
		 IdObjetModele id = this.modele.getRoleResponsable();
		 if (id != null)
		 {
			 fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		 }
		
		 this.ajouterDescription(fd);
		this.ajouterContenu(fd);
		this.ajouterMail(fd);
		this.ajouterVersionDate(fd);
		fd.write("</BODY></HTML>") ;
		fd.close();
		
		GenerationManager.nbProduits++;
	}
	
}