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
import java.util.Vector;


/**
 * Classe permettant de créer une page dont le contenu correspond à une Activité
 */
public class GActivite extends GElementModele
{
	
	/**
	 * @param elem
	 * @param writer
	 */
	public GActivite(ElementPresentation elem, PrintWriter writer) 
	{	
		super(elem,writer);
	}


	/**
	 * Méthode permettant de créer le contenu de la page associée à l'activité courante
	 * Affichage des listes de produits et du rôle responsable de l'activité
	 */
	public void creerFichierDescription() throws IOException
	{
		// création du fichier de contenu
		File ficHTML = new File (this.cheminAbsolu) ;
		//System.out.println("Fichier  à créér : " + ficHTML);
		FileWriter fd = new FileWriter(ficHTML);

		fd.write("<HTML><head> <link rel='STYLESHEET' type='text/css' href='" + this.getCheminStyle() + "'>"
								+ "</head>" + "<body><center>\n"
								+ "<table width=\"84%\" align=\"center\">\n"
								+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
								+ "<p align=\"center\" class=\"entete\">\n"
								+ "<b>" + this.element.getNomPresentation() + "</b>\n"
								+ "</p></td></tr></table></center><BR>\n");

		fd.write(getBarreNavigation() + "<br>");
		
		// affiche les produits en entrée / sortie
        fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PROD_ENTREE") + "</div>\n");
        // affiche la liste des produits entrée de l'activite
        Vector listeProduits = this.modele.getProduitEntree();
        for (int i = 0; i < listeProduits.size(); i++)
        {
            IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
            if (GenerationManager.estProduitExterieur(id) == 0)
            {
                if (GenerationManager.getProduitChange(id) == null)
                {
                    fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
                }
                else
                {
                    fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(GenerationManager.getProduitChange(id)) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
                }
            }
            else
            {
                fd.write("<div class=\"elementliste\">" + id.toString() + "</div>\n");
            }
        }

        fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PROD_SORTIE") + "</div>\n");
        // affiche la liste des produits sortie de l'activite
        listeProduits = this.modele.getProduitSortie();
        for (int i = 0; i < listeProduits.size(); i++)
        {
            IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
            if (GenerationManager.estProduitExterieur(id) == 0)
            {
                if (GenerationManager.getProduitChange(id) == null)
                {
                    fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(id) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
                }
                else
                {
                    fd.write("<div class=\"elementliste\"><a href=\"" + this.getLienChemin(GenerationManager.getProduitChange(id)) + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
                }
            }
            else
            {
                fd.write("<div class=\"elementliste\">" + id.toString() + "</div>\n");
            }
        }
		
		// affiche le rôle responsable de l'activité
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
	}
	
	/**
	 * 
	 */
	public void recenser() 
	{
		Integer oldValue = (Integer)ArbreGeneration.mapCompteur.get("nbActivites");
		ArbreGeneration.mapCompteur.put("nbActivites", new Integer(oldValue.intValue() + 1));
		
		oldValue = (Integer)ArbreGeneration.mapCompteur.get("nbPagesTotal");
		ArbreGeneration.mapCompteur.put("nbPagesTotal", new Integer(oldValue.intValue() + 1));
		
		Vector oldVecteur = (Vector)ArbreGeneration.mapRecap.get("activites");
		oldVecteur.addElement(this);
	}
}
