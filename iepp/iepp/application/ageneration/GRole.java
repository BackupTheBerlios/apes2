
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
 * Classe permettant de cr�er une page dont le contenu correspond � un role
 */
public class GRole extends GElementModele
{

	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� � l'activit� courante
	 * @param elem2 element de pr�sentation qui suit (dans l'arbre) l'�l�ment de pr�sentation courant
	 * @param listeIdDossier map contenant la liste des dossiers d�j� pr�sents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GRole(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}
	
	/**
	 * M�thode permettant de cr�er le contenu de la page associ�e � l'activit� courante
	 * Affichage des listes d'activit�s et de produits
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

		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ACTIVITES") + " </div>\n");
		
		// affiche la liste des activit�s dans le role est responsable
		Vector listeActivites = this.modele.getActivite();
		for (int i = 0; i < listeActivites.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeActivites.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"../../" + id.getChemin() + "\">" + id.toString() + "</a></div>\n");
		}

		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PRODUITS") + " </div>\n");
		
		// affiche la liste des produits dans le role est responsable
		Vector listeProduits = this.modele.getProduit();
		for (int i = 0; i < listeProduits.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"../../" + id.getChemin() + "\">" + id.toString() + "</a></div>\n");
		}

		String description = this.element.getDescription();
		if (description != null)
		{
			fd.write("<br><hr><div class=\"description\">" + description + "</div>\n");
		}
		this.ajouterContenu(fd);
	}
	
}