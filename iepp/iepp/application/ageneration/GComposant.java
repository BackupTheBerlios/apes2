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
 * Classe permettant de créer une page dont le contenu correspond à un composant
 */
public class GComposant extends GElementModele
{

	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem element de présentation associé au composant courant
	 * @param elem2 element de présentation qui suit (dans l'arbre) l'élément de présentation courant
	 * @param listeIdDossier map contenant la liste des dossiers déjà présents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la génération du site
	 */
	public GComposant(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}
	
	/**
	 * Méthode permettant de créer le contenu de la page associée au composant courant
	 * Affiche selon les options de la génération, des listes ou des diagrammes
	 */
	public void creerFichierDescription() throws IOException
	{
		// création du fichier HTML
		File ficHTML = new File (GenerationManager.getInstance().getCheminGeneration()
								 + "/" + this.modele.getChemin()) ;
		FileWriter fd;
		fd = new FileWriter(ficHTML);
		fd.write("<HTML><head>");
		//TODO modifier le chemin vers la feuille de style
		fd.write("<link rel='STYLESHEET' type='text/css' href='../styles/" + GenerationManager.getInstance().getFeuilleCss() + "'>");
		fd.write("</head>" + "<body><center>\n"
				+ "<table width=\"84%\" align=\"center\">\n"
				+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
				+ "<p align=\"center\" class=\"entete\">\n"
				+ "<b>" + this.modele.toString() + "</b>\n"
				+ "</p></td></tr></table></center><BR><BR>\n");
		
		
		//---------------------SELON OPTION-----------------//
		switch (GenerationManager.getInstance().getTypeComposant())
		{
			case GenerationManager.TYPE_COMP_TABLEAU:
					fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ROLES") + " </div>\n");
					Vector listeRole = this.modele.getRole();
					for (int i = 0; i < listeRole.size(); i++)
					{
						IdObjetModele id = (IdObjetModele) listeRole.elementAt(i);
						fd.write("<div class=\"elementliste\"><a href=\"../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
					}
					fd.write("<br><br><div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PRODUITS") + " </div>\n");
					Vector listeProduits = this.modele.getProduit();
					for (int i = 0; i < listeProduits.size(); i++)
					{
						IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
						fd.write("<div class=\"elementliste\"><a href=\"../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
					}
					fd.write("<br><br><div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_DEFINITIONS") + " </div>\n");
					Vector listeDefinition = this.modele.getDefinitionTravail();
					for (int i = 0; i < listeDefinition.size(); i++)
					{
						IdObjetModele id = (IdObjetModele) listeDefinition.elementAt(i);
						fd.write("<div class=\"elementliste\"><a href=\"../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
					}
					break;
				
			case GenerationManager.TYPE_COMP_RESP:
					Vector liste = this.modele.getListeDiagrammeResponsabilite();
					for (int i = 0; i < liste.size(); i++)
					{
						IdObjetModele id = (IdObjetModele)liste.elementAt(i);
						fd.write("<div align=\"center\" class=\"imgdiagramme\">" + id.getMapImage("../","./diagrammes/") + "</div><br>");
					}
					break;
				
			case GenerationManager.TYPE_COMP_FLOT:
					//diagramme de flot de définition de travail
					IdObjetModele idDiagramme = this.modele.getDiagrammeFlot();
					if (idDiagramme == null)
					{				
						//	ou contexte s'il n'y est pas
						idDiagramme = this.modele.getDiagrammeContexte();
					}
					// on a réussi à avoir au-moins un diagramme
					if (idDiagramme != null)
					{
						fd.write("<div align=\"center\" class=\"imgdiagramme\">" + idDiagramme.getMapImage("../","./diagrammes/") + "</div>");
					}
					break;
		}
		//---------------------FIN OPTION-----------------//
		
		// voir pour les diagrammes

		this.ajouterContenu(fd);
	}
	
}