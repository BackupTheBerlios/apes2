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
 * Classe contenant des m�thodes de base (cr�ation des r�pertoires, remplissage du fichier tree.js etc...)
 * communes � tous les �l�ments � publier
 */
public class GElement 
{

	/**
	 * Element de pr�sentation courant � traiter
	 */
	protected ElementPresentation element;
	
	/**
	 * Element de pr�sentation qui suit dans l'arbre l'�l�ment de pr�sentation courant
	 */
	protected ElementPresentation elementSuivant;
	
	/**
	 * Liste des dossiers qui ont d�j� �t� cr��s dans l'arbre
	 */
	protected HashMap idDossier;
	
	/**
	 * Lien vers le fichier tree.js � remplir lors de la g�n�ration
	 */
	protected PrintWriter pwFicTree;
	
	
	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� � l'�l�ment courant
	 * @param elem2 element de pr�sentation qui suit (dans l'arbre) l'�l�ment de pr�sentation courant
	 * @param listeIdDossier map contenant la liste des dossiers d�j� pr�sents dans l'arbre pour le composant publiable / paquetage de publication en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GElement(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		this.element = elem;
		this.elementSuivant = elem2;
		this.idDossier = listeIdDossier;
		this.pwFicTree = pwFicTree;
	}
	
	/**
	 * Traitement commun � tous les �l�ments � g�n�rer
	 * ecriture dans l'arbre et cr�ation du fichier de contenu
	 */
	public void traiterGeneration() throws IOException
	{
		// on �crit dans l'arbre
		this.ecrireArbre();
		// on cr�e le fichier correspondant
		this.creerFichierDescription();
	}



	/**
	 * M�thode permettant de remplir le fichier tree.js pour l'�l�ment de pr�sentation
	 * qui est en train d'�tre trait�. Il faut savoir si c'est une feuille de l'arbre ou
	 * si c'est un dossier. On le sait gr�ce � l'�l�ment suivant et � l'id interne de 
	 * l'�l�ment suivant par rapport � l'id interne de l'�l�ment courant
	 */
	protected void ecrireArbre()
	{
		// si l'�l�ment suivant est null, ou si son Id interne est plus court que
		// celui de l'�l�ment courant, on ajoute une feuille
		if (this.elementSuivant == null)
		{
			pwFicTree.println("docAux = insDoc(" + (String)this.idDossier.get(this.getNiveauSuperieur()) + ", gLnk(\"R\",\" " + this.element.getNomPresentation() + "\", \"../" + this.construireNom() + "\"))");
			if (this.element.getNomIcone() != null)
				pwFicTree.println("docAux.iconSrc = ICONPATH + \"" + this.element.getNomIcone() + "\"");
		}
		else if (this.element.getNiveau() >= this.elementSuivant.getNiveau())
		{
			//System.out.println("Mon niveau : " + this.getNiveauSuperieur() + " : " + (String)this.idDossier.get(this.getNiveauSuperieur()));
			// l'�l�ment courant est le dernier �l�ment pour le niveau courant
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
	 * Renvoie sous forme de chaine de caract�res le niveau sup�rieur
	 * Par exemple si le niveau courant (calcul� selon l'id interne) est 1-1-2,
	 * le niveau sup�rieur est 1-1
	 * Ce niveau est utilis� pour r�cup�rer dans la map des liste des dossiers
	 * l'id du dossier du niveau sup�rieur
	 * @return le niveau sup�rieur de l'�l�ment courant
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
	 * Cr�er le fichier de contenu d'un �l�ment de pr�sentation simple, sans mod�le
	 */
	protected void creerFichierDescription() throws IOException
	{

		File ficHTML = new File (GenerationManager.getInstance().getCheminGeneration() + File.separator + this.construireNom()) ;
		//System.out.println("Fichier  � cr��r : " + ficHTML);
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
			// fichier � la suite du contenu d�j� �crit
			// si le contenu est un fichier html
			if (contenu.endsWith(".html") || contenu.endsWith(".htm") || contenu.endsWith(".HTML") || contenu.endsWith(".HTM") || contenu.endsWith(".txt")) 
			{
				// recopier le fichier � la suite de la description
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
	 * M�thode permettant d'�crire le code html correspondant au bouton mail � chaque bas de page
	 * @param fd lien vers le fichier html de contenu
	 * @throws IOException
	 */
	public void ajouterMail(FileWriter fd) throws IOException
	{
		fd.write("<br><hr>");
		fd.write("<div align=\"center\" class=\"boutonemail\"><a href=\"mailto:" + Application.getApplication().getProjet().getDefProc().getEmailAuteur() + "?subject=" +  this.element.getNomPresentation() + "\">" + Application.getApplication().getTraduction("WEB_MAIL") + "</A></div>");
	}
	
	/**
	 * M�thode permettant d'�crire le code html correspondant � la date de g�n�ration � chaque bas de page
	 * @param fd lien vers le fichier html de contenu
	 * @throws IOException
	 */
	public void ajouterVersionDate(FileWriter fd)throws IOException
	{
		fd.write("<div align=\"center\" class=\"date\">"  + Application.getApplication().getTraduction("WEB_DATE_GEN") + " " + GenerationManager.getInstance().getDateGeneration() + "</div>");
	}
	
	
	/**
	 * M�thode permettant de recopier le contenu associ� � l'�l�ment de pr�sentation
	 * courant. 
	 * @param contenu nom du fichier de contenu associ� � l'�l�ment courant
	 * @param fd lien vers le fichier de contenu courant � remplir
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
