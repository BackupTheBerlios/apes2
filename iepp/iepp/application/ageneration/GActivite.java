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
 * Classe permettant de cr�er une page dont le contenu correspond � une Activit�
 */
public class GActivite extends GElementModele
{

	
	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� � l'activit� courante
	 * @param elem2 element de pr�sentation qui suit (dans l'arbre) l'�l�ment de pr�sentation courant
	 * @param listeIdDossier map contenant la liste des dossiers d�j� pr�sents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GActivite(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}
	
	
	/**
	 * M�thode permettant de cr�er le contenu de la page associ�e � l'activit� courante
	 * Affichage des listes de produits et du r�le responsable de l'activit�
	 */
	public void creerFichierDescription() throws IOException
	{
		// cr�ation du fichier de contenu
		File ficHTML = new File (GenerationManager.getInstance().getCheminGeneration() + File.separator + this.construireNom()) ;
		FileWriter fd = new FileWriter(ficHTML);

		fd.write("<HTML><head> <link rel='STYLESHEET' type='text/css' href='../../styles/" + GenerationManager.getInstance().getFeuilleCss() + "'>"
								+ "</head>" + "<body><center>\n"
								+ "<table width=\"84%\" align=\"center\">\n"
								+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
								+ "<p align=\"center\" class=\"entete\">\n"
								+ "<b>" + this.element.getNomPresentation() + "</b>\n"
								+ "</p></td></tr></table></center><BR><BR>\n");

		this.ajouterLienRacine(fd);
		// lien vers la page de la d�finition de travail
		// TODO mettre la langue
		IdObjetModele def = this.modele.getLaDefinitionTravail();
		if (def != null)
		{
			fd.write("<br>");
			fd.write("<a href=\"../../" + def.getChemin() + "\" >" + " Page d'accueil de la d�finition de travail" + "</a>");
			fd.write("<br>");
		}
		
		// affiche les produits en entr�e / sortie
		fd.write("<div class=\"titreliste\">"+ Application.getApplication().getTraduction("WEB_PROD_ENTREE") +"</div>\n");
		// affiche la liste des produits entr�e de l'activite
		Vector listeProduits = this.modele.getProduitEntree();
		for (int i = 0; i < listeProduits.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"../../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}

		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PROD_SORTIE") + "</div>\n");
		 // affiche la liste des produits entr�e de l'activite
		listeProduits = this.modele.getProduitSortie();
		for (int i = 0; i < listeProduits.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"../../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}
		
		// affiche le r�le responsable de l'activit�
		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ROLE_RESP") + "</div>\n");
		IdObjetModele id = this.modele.getRoleResponsable();
		if (id != null)
		{
			fd.write("<div class=\"elementliste\"><a href=\"../../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}
		
		String description = this.element.getDescription();
		if (description != null)
		{
			fd.write("<br><hr><div class=\"description\">" + description + "</div>\n");
		}
		this.ajouterContenu(fd);
	}
}
