
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
 * Classe permettant de créer une page dont le contenu correspond à un role
 */
public class GRole extends GElementModele
{

	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem element de présentation associé à l'activité courante
	 * @param pwFicTree lien vers le fichier tree.js construit durant la génération du site
	 */
	public GRole(ElementPresentation elem, PrintWriter pwFicTree)
	{
		super(elem, pwFicTree);
	}
	
	/**
	 * Méthode permettant de créer le contenu de la page associée à l'activité courante
	 * Affichage des listes d'activités et de produits
	 */
	public void creerFichierDescription() throws IOException
	{
		// création du fichier de contenu
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
		
		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ACTIVITES") + " </div>\n");
		
		// affiche la liste des activités dans le role est responsable
		Vector listeActivites = this.modele.getActivite();
		for (int i = 0; i < listeActivites.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeActivites.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}

		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PRODUITS") + " </div>\n");
		
		// affiche la liste des produits dans le role est responsable
		Vector listeProduits = this.modele.getProduit();
		for (int i = 0; i < listeProduits.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
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