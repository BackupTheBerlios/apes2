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
import iepp.domaine.ComposantProcessus;
import iepp.domaine.ElementPresentation;
import java.io.*;
import java.util.HashMap;

import org.ipsquad.apes.adapters.SpemGraphAdapter;

import com.sun.image.codec.jpeg.ImageFormatException;

import util.ImageUtil;

/**
 * Classe permettant de cr�er une page dont le contenu correspond � un diagramme
 */
public class GDiagramme extends GElementModele
{

	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� au diagramme courant
	 * @param elem2 element de pr�sentation qui suit (dans l'arbre) l'�l�ment de pr�sentation courant
	 * @param listeIdDossier map contenant la liste des dossiers d�j� pr�sents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GDiagramme(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}


	/**
	 * M�thode permettant de traiter les �l�ments de pr�sentation li�s au diagramme
	 */
	public void traiterGeneration() throws IOException
	{
		// r�cup�rer le mod�le associ�
		if (this.element.getID_Apes() != -1)
		{
			this.modele = this.element.getElementModele();
			// on �crit dans l'arbre
			this.ecrireArbre();
			// on g�n�re les fichiers images
			this.creerFichierImages();
			// on cr�e le fichier correspondant
			this.creerFichierDescription();
		}
	}
	
	/**
	 * 
	 */
	private void creerFichierImages() throws ImageFormatException, IOException
	{
		FileOutputStream fout = new FileOutputStream(GenerationManager.getInstance().getCheminGeneration() 
				+ File.separator + CodeHTML.normalizeName(((ComposantProcessus)this.modele.getRef()).toString())
				+ File.separator+ "diagrammes" + File.separator + CodeHTML.normalizeName(this.modele.toString() + this.modele.getID() + ".png"));
		
		ImageUtil.encoderGrapheImage((SpemGraphAdapter)this.modele.getAdapter(), fout, "png");
	}


	/**
	 * M�thode permettant de cr�er le contenu de la page associ�e au diagramme courant
	 * Affichage du diagramme
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
					+ "</p></td></tr></table></center><BR>\n");

		this.ajouterLienRacine(fd);
		
		fd.write("<div align=\"center\" class=\"imgdiagramme\">" + this.modele.getMapImage("../../", "./") + "</div>");
		String description = this.element.getDescription();
		if (description != null)
		{
			fd.write("<br><hr><div class=\"description\">" + description + "</div>\n");
		}
		this.ajouterContenu(fd);
	}
}
