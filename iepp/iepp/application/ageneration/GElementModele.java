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
 * Classe contenant des m�thodes communes � tous les �l�ments � publier qui sont associ�s
 * � un �l�ment du mod�le
 */
public class GElementModele extends GElement 
{
	
	/**
	 * Id de l'�l�ment du mod�le auquel est associ� l'�l�ment de pr�sentation courant
	 */
	protected IdObjetModele modele;


	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� � l'�l�ment courant
	 * @param elem2 element de pr�sentation qui suit (dans l'arbre) l'�l�ment de pr�sentation courant
	 * @param listeIdDossier map contenant la liste des dossiers d�j� pr�sents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GElementModele(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}

	/**
	 * Retourne le chemin du fichier html � construire � partir de la racine du site
	 * @return chemin du fichier html � construire
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
	 * M�thode permettant de traiter les �l�ments de pr�sentation li�s � un �l�ment de mod�le
	 */
	public void traiterGeneration() throws IOException
	{
		// r�cup�rer le mod�le associ�
		if (this.element.getID_Apes() != -1)
		{
			this.modele = this.element.getElementModele();
			// on �crit dans l'arbre
			this.ecrireArbre();
			// on cr�e le fichier correspondant
			this.creerFichierDescription();
		}
	}
	
	/**
	 * M�thode permettant d'ajouter un lien sur la page vers le composant racine
	 * @param fd
	 * @throws IOException
	 */
	public void ajouterLienRacine(FileWriter fd) throws IOException
	{
		fd.write("<a href=\"../../" + this.getCheminRacine() + "\" class=\"link_home\">" + " Page d'accueil du composant" + "</a>");
		fd.write("<br>");
	}
	
	/**
	 * M�thode permettant d'ajouter le contenu d'un fichier en bas de la page en train
	 * d'�tre construite 
	 * @param fd lien vers le fichier contenu � construire
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
				// recopier le fichier � la suite de la description
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
