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

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;

import iepp.Application;
import iepp.domaine.ElementPresentation;

/**
 * Classe contenant des méthodes de base (création des répertoires, remplissage du fichier tree.js etc...)
 * communes à tous les éléments à publier
 */
public class GElement 
{

	/**
	 * Element de présentation courant à traiter
	 */
	protected ElementPresentation element;
	
	/**
	 * Element de présentation qui suit dans l'arbre l'élément de présentation courant
	 */
	protected ElementPresentation elementSuivant;
	
	/**
	 * Liste des dossiers qui ont déjà été créés dans l'arbre
	 */
	protected HashMap idDossier;
	
	/**
	 * Lien vers le fichier tree.js à remplir lors de la génération
	 */
	protected PrintWriter pwFicTree;
	
	
	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem element de présentation associé à l'élément courant
	 * @param elem2 element de présentation qui suit (dans l'arbre) l'élément de présentation courant
	 * @param listeIdDossier map contenant la liste des dossiers déjà présents dans l'arbre pour le composant publiable / paquetage de publication en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la génération du site
	 */
	public GElement(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		this.element = elem;
		this.elementSuivant = elem2;
		this.idDossier = listeIdDossier;
		this.pwFicTree = pwFicTree;
	}
	
	/**
	 * Traitement commun à tous les éléments à générer
	 * ecriture dans l'arbre et création du fichier de contenu
	 */
	public void traiterGeneration() throws IOException
	{
		// on écrit dans l'arbre
		this.ecrireArbre();
		// on crée le fichier correspondant
		this.creerFichierDescription();
	}



	/**
	 * Méthode permettant de remplir le fichier tree.js pour l'élément de présentation
	 * qui est en train d'être traité. Il faut savoir si c'est une feuille de l'arbre ou
	 * si c'est un dossier. On le sait grâce à l'élément suivant et à l'id interne de 
	 * l'élément suivant par rapport à l'id interne de l'élément courant
	 */
	protected void ecrireArbre()
	{
		// si l'élément suivant est null, ou si son Id interne est plus court que
		// celui de l'élément courant, on ajoute une feuille
		if (this.elementSuivant == null)
		{
			pwFicTree.println("docAux = insDoc(" + (String)this.idDossier.get(this.getNiveauSuperieur()) + ", gLnk(\"R\",\" " + this.element.getNomPresentation() + "\", \"../" + this.construireNom() + "\"))");
			if (this.element.getNomIcone() != null)
				pwFicTree.println("docAux.iconSrc = ICONPATH + \"" + this.element.getNomIcone() + "\"");
		}
		else if (this.element.getNiveau() >= this.elementSuivant.getNiveau())
		{
			//System.out.println("Mon niveau : " + this.getNiveauSuperieur() + " : " + (String)this.idDossier.get(this.getNiveauSuperieur()));
			// l'élément courant est le dernier élément pour le niveau courant
			pwFicTree.println("docAux = insDoc(" + (String)this.idDossier.get(this.getNiveauSuperieur()) + ", gLnk(\"R\",\" " + this.element.getNomPresentation() + "\", \"../" + this.construireNom()  + "\"))");
			if (this.element.getNomIcone() != null)
				pwFicTree.println("docAux.iconSrc = ICONPATH + \"" + this.element.getNomIcone() + "\"");
		}
		else
		{
			// ajouter dans la map des id
			String nouvelId = "e_" + CodeHTML.normalizeName(this.element.getNomPresentation() + "_" + this.element.getID_Apes());
			this.idDossier.put(this.element.getID_interne(), nouvelId);
			pwFicTree.println( nouvelId + " = insFld(" + (String)this.idDossier.get(this.getNiveauSuperieur()) + ", gFld(\"" + this.element.getNomPresentation() + "\", \"../"+ this.construireNom() + "\"))");
			if (this.element.getNomIcone() != null)
			{
				pwFicTree.println( nouvelId + ".iconSrcClosed = ICONPATH + \"" + this.element.getNomIcone() + "\"");
				pwFicTree.println( nouvelId + ".iconSrc = ICONPATH + \"" + this.element.getNomIcone() + "\"");
			}
		}
	}
	
	/**
	 * Renvoie sous forme de chaine de caractères le niveau supérieur
	 * Par exemple si le niveau courant (calculé selon l'id interne) est 1-1-2,
	 * le niveau supérieur est 1-1
	 * Ce niveau est utilisé pour récupérer dans la map des liste des dossiers
	 * l'id du dossier du niveau supérieur
	 * @return le niveau supérieur de l'élément courant
	 */
	public String getNiveauSuperieur()
	{
		String[] id = this.element.getTableauID();
		String retour = "";
		int i;
		
		if (this.element.getNiveau() == 1) return "";
		if (this.element.getNiveau() == 2) return id[0];  
		for (i = 0 ; i < this.element.getNiveau() - 2; i++)
		{
			retour += (id[i] + "-");
		}
		retour += id[i]; 
		return retour;
	}
	
	/**
	 * Construit le chemin du fichier de contenu courant en partant de la racine du site vers
	 * le fichier html
	 * @return chemin du fichier de contenu html
	 */
	public String construireNom()
	{
		return ( GenerationManager.DESCRIPTION_PATH + "/" + CodeHTML.normalizeName(this.element.getNomPresentation()+  "_" + this.element.getID_interne()) + ".html");
	}
	
	/**
	 * Créer le fichier de contenu d'un élément de présentation simple, sans modèle
	 */
	protected void creerFichierDescription() throws IOException
	{

		File ficHTML = new File (GenerationManager.getInstance().getCheminGeneration() + File.separator + this.construireNom()) ;
		//System.out.println("Fichier  à créér : " + ficHTML);
		FileWriter fd = new FileWriter(ficHTML);
		
		fd.write("<HTML><head> <link rel='STYLESHEET' type='text/css' href='../styles/" + GenerationManager.getInstance().getFeuilleCss() + "'>"
								+ "</head>" + "<body><center>\n"
								+ "<table width=\"84%\" align=\"center\">\n"
								+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
								+ "<p align=\"center\" class=\"entete\">\n"
								+ "<b>" + this.element.getNomPresentation() + "</b>\n"
								+ "</p></td></tr></table></center><BR><BR>\n");
								
		String description = this.element.getDescription();
		if (description != null)
		{
			fd.write("<br><hr><div class=\"description\">" + description + "</div>\n");
		}
				
		String contenu = this.element.getContenu();
		if (contenu != null)
		{
			fd.write("<hr></br>");
			//Selon l'extension du fichier de contenu, recopie ou non le contenu de ce
			// fichier à la suite du contenu déjà écrit
			// si le contenu est un fichier html
			if (contenu.endsWith(".html") || contenu.endsWith(".htm") || contenu.endsWith(".HTML") || contenu.endsWith(".HTM") || contenu.endsWith(".txt")) 
			{
				// recopier le fichier à la suite de la description
				this.recopierContenu(contenu, fd);
			}
			else
			{
				fd.write(Application.getApplication().getTraduction("WEB_LINK")+ " : " + "<a href=\"../contenu/" + this.element.getContenu() + "\" target=\"_new\" >" + this.element.getContenu() + "</a>");
				this.ajouterMail(fd);
				fd.write("</BODY></HTML>") ;
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

	/**
	 * Méthode permettant d'écrire le code html correspondant au bouton mail à chaque bas de page
	 * @param fd lien vers le fichier html de contenu
	 * @throws IOException
	 */
	public void ajouterMail(FileWriter fd) throws IOException
	{
		fd.write("<br><hr>");
		fd.write("<div align=\"center\" class=\"boutonemail\"><a href=\"mailto:" + Application.getApplication().getProjet().getDefProc().getEmailAuteur() + "?subject=" +  this.element.getNomPresentation() + "\">" + Application.getApplication().getTraduction("WEB_MAIL") + "</A></div>");
	}
	
	/**
	 * Méthode permettant d'écrire le code html correspondant à la date de génération à chaque bas de page
	 * @param fd lien vers le fichier html de contenu
	 * @throws IOException
	 */
	public void ajouterVersionDate(FileWriter fd)throws IOException
	{
		fd.write("<div align=\"center\" class=\"date\">"  + Application.getApplication().getTraduction("WEB_DATE_GEN") + " " + GenerationManager.getInstance().getDateGeneration() + "</div>");
	}
	
	
	/**
	 * Méthode permettant de recopier le contenu associé à l'élément de présentation
	 * courant. 
	 * @param contenu nom du fichier de contenu associé à l'élément courant
	 * @param fd lien vers le fichier de contenu courant à remplir
	 */
	protected void recopierContenu(String contenu, FileWriter fd) throws IOException
	{
		File f = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + "contenu" + File.separator + contenu);
		if (f.exists())
		{
			FileReader fr = new FileReader(f);
			BufferedReader  br = new BufferedReader(fr);
			
			String ligne ;
			char[] retourChariot = new char[]{Character.LINE_SEPARATOR};
			while ((ligne = br.readLine()) != null)
			{
				fd.write(ligne);
				fd.write(retourChariot);
			}
			this.ajouterMail(fd);
			br.close();
		}
		else
		{
			//TODO SP: voir si besoinde traiter l'erreur
			System.out.println("le fichier n'existe pas : " + f.getName());
		}
	}
}
