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

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import iepp.Application;
import iepp.domaine.ElementPresentation;
import iepp.domaine.IdObjetModele;

/**
 * Classe contenant des méthodes communes à tous les éléments à publier qui sont associés
 * à un élément du modèle
 */
public class GElementModele extends GElement 
{
	
	/**
	 * Id de l'élément du modèle auquel est associé l'élément de présentation courant
	 */
	protected IdObjetModele modele;


	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem element de présentation associé à l'élément courant
	 * @param elem2 element de présentation qui suit (dans l'arbre) l'élément de présentation courant
	 * @param listeIdDossier map contenant la liste des dossiers déjà présents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la génération du site
	 */
	public GElementModele(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}

	/**
	 * Retourne le chemin du fichier html à construire à partir de la racine du site
	 * @return chemin du fichier html à construire
	 */
	public String construireNom()
	{
		return (this.modele.getChemin());
	}
	
	/**
	 * Renvoie le chemin du fichier html du composant racine
	 * @return
	 */
	public String getCheminRacine()
	{
		return (this.modele.getCheminRacine());
	}
	
	
	/**
	 * Méthode permettant de traiter les éléments de présentation liés à un élément de modèle
	 */
	public void traiterGeneration() throws IOException
	{
		// récupérer le modèle associé
		if (this.element.getID_Apes() != -1)
		{
			this.modele = this.element.getElementModele();
			// on écrit dans l'arbre
			this.ecrireArbre();
			// on crée le fichier correspondant
			this.creerFichierDescription();
		}
	}
	
	/**
	 * Méthode permettant d'ajouter un lien sur la page vers le composant racine
	 * @param fd
	 * @throws IOException
	 */
	public void ajouterLienRacine(FileWriter fd) throws IOException
	{
		fd.write("<a href=\"../../" + this.getCheminRacine() + "\" class=\"link_home\">" + " Page d'accueil du composant" + "</a>");
		fd.write("<br>");
	}
	
	/**
	 * Méthode permettant d'ajouter le contenu d'un fichier en bas de la page en train
	 * d'être construite 
	 * @param fd lien vers le fichier contenu à construire
	 * @throws IOException
	 */
	public void ajouterContenu(FileWriter fd ) throws IOException
	{
		String contenu = this.element.getContenu();
		if (contenu != null)
		{
			fd.write("<hr></br>");
			// si le contenu est un fichier html
			if (contenu.endsWith(".html") || contenu.endsWith(".htm") || contenu.endsWith(".HTML") || contenu.endsWith(".HTM") || contenu.endsWith(".txt")) 
			{
				// recopier le fichier à la suite de la description
				this.recopierContenu(contenu, fd);
			}
			else
			{
				fd.write(Application.getApplication().getTraduction("WEB_LINK")+ " : " + "<a href=\"../../contenu/" + this.element.getContenu() + "\" target=\"_new\" >" + this.element.getContenu() + "</a>");
				fd.write("<hr></br>");
				this.ajouterMail(fd);
			}
		}
		else
		{
			this.ajouterMail(fd);
		}
		this.ajouterVersionDate(fd);
		fd.write("</BODY></HTML>") ;
		fd.close();
	}
}
