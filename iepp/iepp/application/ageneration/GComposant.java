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
		
		
		// tableau
		fd.write("<div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_ROLES") + " </div>\n");
		Vector listeRole = this.modele.getRole();
		for (int i = 0; i < listeRole.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeRole.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}
		fd.write("<br><br><div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_PRODUITS") + " </div>\n");
		Vector listeProduits = this.modele.getProduit();
		
		// Separer les produits en deux tableaux
		fd.write("<TABLE border=\"0\" width=\"100%\">");
		fd.write("<TR><TH>Produits en entr&eacute;e</TH><TH>Produits internes</TH><TH>Produits en sortie</TH></TR>");
		
		String entree = "";
		String internes = "";
		String sortie = "";
		String ajout;
		
		boolean trouve;
		boolean in;
		
		// Recuperer la liste des produit exterieurs a ne pas lier
		Vector externe = GenerationManager.getListeProduitsExterieurs();
		
		for (int i = 0; i < listeProduits.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeProduits.elementAt(i);
			// Construire les chaines
			ajout = "<div class=\"elementliste\"><a href=\"../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n";
			
			trouve = false;
			in = false;
			
			// Chercher les produits exterieurs (sans liens)
			for (int j = 0; j < externe.size() && !trouve ; j++)
			{
			    if (externe.elementAt(j).toString().equals(id.toString()) && (((IdObjetModele)externe.elementAt(j)).getRef() == id.getRef()))
			    {
			        // Le produit est exterieur, il ne faut pas mettre de lien
			        trouve = true;
			        ajout = "<div class=\"elementliste\"> " + id.toString() + "</div>\n";
			        if (((IdObjetModele)externe.elementAt(j)).estProduitEntree())
			        {
			            in = true;
			        }
			    }
			}
			// S'ils ne sont pas exterieurs, ils n'ont peut etre pas de presentation mais sont des produits en entree lies
			HashMap listeProduitsChanges = GenerationManager.getListeProduitsChanges();
			if (listeProduitsChanges.containsKey(id.getRef().toString() +"::"+ id.toString()))
			{
			    trouve = true;
			    in = true;
			    ajout = "<div class=\"elementliste\"><a href=\"../" + ((IdObjetModele)listeProduitsChanges.get(id.getRef().toString() + "::"+ id.toString())).getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n";
			}
			
			// ou des produits en sortie qui ont une presentation
			Vector listeProduitsSortie = GenerationManager.getListeProduitsSortie();
			for (int j = 0; j < listeProduitsSortie.size() && ! trouve; j++)
			{
			    if (listeProduitsSortie.elementAt(j).toString().equals(id.getRef().toString() + "::"+ id.toString()))
			    {
			        trouve = true;
			    }
			}
			
			if (trouve)
			{
			    if (in)
			    {
			        entree += ajout;
			    }
			    else
			    {
				    sortie += ajout;
				} 
			}
			else
			{
			    // ou enfin des produits internes
			    internes += ajout;
			}
		}
		// Ecrire le tableau les liens
		fd.write("<TR><TD valign=top>"+ entree +"</TD><TD valign=top>"+ internes +"</TD><TD valign=top>"+ sortie +"</TD></TR>");
		fd.write("</TABLE>");
		
		fd.write("<br><br><div class=\"titreliste\">" + Application.getApplication().getTraduction("WEB_DEFINITIONS") + " </div>\n");
		Vector listeDefinition = this.modele.getDefinitionTravail();
		for (int i = 0; i < listeDefinition.size(); i++)
		{
			IdObjetModele id = (IdObjetModele) listeDefinition.elementAt(i);
			fd.write("<div class=\"elementliste\"><a href=\"../" + id.getChemin() + "\" target=\"_new\" >" + id.toString() + "</a></div>\n");
		}

		// voir pour les diagrammes

		this.ajouterContenu(fd);
	}
	
}