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

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import util.ToolKit;

import iepp.Application;
import iepp.domaine.ElementPresentation;
import iepp.domaine.PaquetagePresentation;

/**
 * Classe contenant des méthodes de base (création des répertoires, remplissage du fichier tree.js etc...)
 * communes à tous les éléments à publier
 */
public class GElement 
{

	/**
	 * Arbre courant, permettant d'obtenir des informations sur la position de l'élément courant
	 */
	protected ArbreGeneration arbre;
	
	/**
	 * Element de présentation courant à traiter
	 */
	protected ElementPresentation element;

	/**
	 * Lien vers le fichier tree.js à remplir lors de la génération
	 */
	protected PrintWriter pwFicTree;
	
	/**
	 * Chemin absolu du fichier à creer
	 */
	protected String cheminAbsolu = null;
	protected String cheminParent = null;
	
	/**
	 * ID du gelement
	 */
	protected long ID;
	protected long IDParent;
	
	
	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem
	 * @param pwFicTree2
	 */
	public GElement(ElementPresentation elem, PrintWriter pwFicTree) 
	{
		this.element = elem;
		this.pwFicTree = pwFicTree;
	}

	/**
	 * Traitement commun à tous les éléments à générer
	 * ecriture dans l'arbre et création du fichier de contenu
	 * @param feuille, indique si l'élément courant est une feuille ou non
	 * @param id
	 */
	public void traiterGeneration(long id) throws IOException
	{
		this.IDParent = id;
		// créer le répertoire
		this.creerRep();
		// on écrit dans l'arbre
		this.ecrireArbre();
		// on crée le fichier correspondant
		this.creerFichierDescription();
		GenerationManager.nbPagesTotal++;
	}

	/**
	 * 
	 */
	public void creerRep() 
	{
		File f = new File(this.cheminParent);
		f.mkdirs();
	}

	/**
	 * Retourne l'élément de présentation associé au GNoeud courant
	 */
	public ElementPresentation getElementPresentation()
	{
		return this.element;
	}
	public void setChemin(String chemin)
	{
		this.cheminAbsolu = chemin;
		File f2 = (new File(this.cheminAbsolu)).getParentFile();
		this.cheminParent = ToolKit.removeSlashTerminatedPath(f2.getPath());
	}
	public String getChemin()
	{
		return this.cheminAbsolu;
	}
	public String getCheminParent()
	{
		return this.cheminParent;
	}
	public String getCheminRelatif()
	{
		File f = new File(GenerationManager.getInstance().getCheminGeneration());
		String cheminRel = "./" + ToolKit.getRelativePathOfAbsolutePath(this.cheminAbsolu,
				ToolKit.removeSlashTerminatedPath(f.getAbsolutePath()));
		return ToolKit.removeSlashTerminatedPath(cheminRel);
	}
	
	public String getCheminIcone(String icone)
	{
		String res = "./" + GenerationManager.APPLET_PATH + "/images/" + icone;
		ArbreGeneration aux = this.arbre;
		// on remonte jusqu'à la racine
		while (!aux.isRacine())
		{
			res = "../" + res;
			aux = aux.getArbreParent();
		}
		return res;
	}
	
	/**
	 * Renvoie le chemin relatif de la feuille de style par rapport à l'élément courant
	 */
	public String getCheminStyle()
	{
		String res = "./" + GenerationManager.STYLES_PATH + "/" + GenerationManager.getInstance().getFeuilleCss();
		ArbreGeneration aux = this.arbre;
		// on remonte jusqu'à la racine
		while (!aux.isRacine())
		{
			res = "../" + res;
			aux = aux.getArbreParent();
		}
		return res;
	}
	
	/**
	 * Renvoie le chemin des fichiers de contenu de l'élément courant
	 */
	public String getCheminContenu()
	{
		ArbreGeneration aux = this.arbre;
		// on remonte jusqu'à un composant ou un paquetage de présentation
		while (!aux.isRacine() && (! (aux.getElement() instanceof GPaquetagePresentation)) 
									&& (! (aux.getElement() instanceof GComposantPubliable)))
		{
			aux = aux.getArbreParent();
		}
		if (aux.isRacine())
		{
			return "";
		}
		return ( aux.getElement().getCheminParent() + File.separator + GenerationManager.CONTENU_PATH );
	}
	
	/**
	 * Renvoie le nom du composant ou le nom du paquetage de présentation racine
	 */
	public String getNomRacine()
	{
		ArbreGeneration aux = this.arbre;
		// on remonte jusqu'à un composant ou un paquetage de présentation
		while (!aux.isRacine() && (! (aux.getElement() instanceof GPaquetagePresentation)) 
									&& (! (aux.getElement() instanceof GComposantPubliable)))
		{
			aux = aux.getArbreParent();
		}
		if (aux.isRacine())
		{
			return "";
		}
		return ( aux.getElement().getElementPresentation().getNomPresentation());
	}
	
	public String getBarreNavigation()
	{
		String res = "<img src=\"" + this.getCheminIcone(this.element.getNomIcone()) 
					+ "\" width=\"16\" height=\"16\" border=\"0\"> "  + this.element.getNomPresentation() + "</div>\n";
		if (this.arbre.isRacine())
		{
			return "<div class=\"navigation_barre\"> " + res;
		}
		ArbreGeneration aux = this.arbre.getArbreParent();
		// on remonte jusqu'à la racine
		int niveau = 1;
		while (!aux.isRacine())
		{
			res = aux.getElement().getElementPresentation().getNomPresentation() + "</a>\n >> " + res;
			res = CodeHTML.normalizeName(aux.getElement().getElementPresentation().getNomPresentation()) + "_" + aux.getElement().getID() + ".html\" >" 
			+ "<img src=\"" + this.getCheminIcone(aux.getElement().getElementPresentation().getNomIcone()) + "\" width=\"16\" height=\"16\" border=\"0\"> " + res;
			
			for (int i = 0; i < niveau; i++)
			{
				res = "../" + res;
			}
			res = "<a href=\"" + res;
			aux = aux.getArbreParent();
			niveau++;
		}
		return "<div class=\"navigation_barre\"> " + res;
	}
	
	/**
	 * Renvoie le lien vers le contenu associé à l'élément courant
	 */
	public String getLienContenu()
	{
		String res = "./" + GenerationManager.CONTENU_PATH + "/" + this.element.getContenu();
		ArbreGeneration aux = this.arbre;
		// on remonte jusqu'à un composant ou un paquetage de présentation
		while (!aux.isRacine() && (! (aux.getElement() instanceof GPaquetagePresentation)) 
				&& (! (aux.getElement() instanceof GComposantPubliable)))
		{
			res = "../" + res;
			aux = aux.getArbreParent();
		}
		return ( res );
	}
	
	public String getLienImage()
	{
		String res = "./images" ;
		ArbreGeneration aux = this.arbre;
		// on remonte jusqu'à un composant ou un paquetage de présentation
		while (!aux.isRacine() && (! (aux.getElement() instanceof GPaquetagePresentation)) 
				&& (! (aux.getElement() instanceof GComposantPubliable)))
		{
			res = "../" + res;
			aux = aux.getArbreParent();
		}
		return ( res );
	}
	
	/**
	 * Méthode permettant de remplir le fichier tree.js pour l'élément de présentation
	 * qui est en train d'être traité. Il faut savoir si c'est une feuille de l'arbre ou
	 * si c'est un dossier. On le sait grâce à l'élément suivant et à l'id interne de 
	 * l'élément suivant par rapport à l'id interne de l'élément courant
	 */
	protected void ecrireArbre()
	{
		String noeudParent = null;
		// sous la racine
		if (this.IDParent == 0)
		{
			noeudParent = "foldersTree";
		}
		else
		{
			noeudParent = "p_" + this.IDParent;
		}
		// on ajoute une feuille à l'arbre
		if (this.arbre.isFeuille())
		{
			pwFicTree.println("docAux = insDoc(" + noeudParent + ", gLnk(\"R\",\" " + this.element.getNomPresentation() + "\", \"../" + this.getCheminRelatif() + "\"))");
			if (this.element.getNomIcone() != null)
				pwFicTree.println("docAux.iconSrc = ICONPATH + \"" + this.element.getNomIcone() + "\"");
		}
		else
		{
			// on ajoute un noeud
			pwFicTree.println( "p_" + this.ID + " = insFld(" + noeudParent+ ", gFld(\"" + this.element.getNomPresentation() + "\", \"../"+ this.getCheminRelatif() + "\"))");
			if (this.element.getNomIcone() != null)
			{
				pwFicTree.println(  "p_" + this.ID + ".iconSrcClosed = ICONPATH + \"" + this.element.getNomIcone() + "\"");
				pwFicTree.println(  "p_" + this.ID + ".iconSrc = ICONPATH + \"" + this.element.getNomIcone() + "\"");
			}
		}
	}

	
	/**
	 * Créer le fichier de contenu d'un élément de présentation simple, sans modèle
	 */
	protected void creerFichierDescription() throws IOException
	{
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
		
		this.ajouterDescription(fd);
		this.ajouterContenu(fd);
		this.ajouterMail(fd);
		this.ajouterVersionDate(fd);
		fd.write("</BODY></HTML>") ;
		fd.close();
		
		GenerationManager.nbElementPresentation++;
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
				fd.write(Application.getApplication().getTraduction("WEB_LINK")+ " : " + "<a href=\"" + this.getLienContenu() + "\" target=\"_new\" >" + this.element.getContenu() + "</a>");
			}
		}
	}
	
	/**
	 * Méthode permettant d'écrire le code html correspondant au bouton mail à chaque bas de page
	 * @param fd lien vers le fichier html de contenu
	 * @throws IOException
	 */
	public void ajouterMail(FileWriter fd) throws IOException
	{
		fd.write("<br><hr>");
		fd.write("<div align=\"center\" class=\"boutonemail\"><a href=\"mailto:" 
					+ Application.getApplication().getProjet().getDefProc().getEmailAuteur() 
					+ "?subject=" +  this.getNomRacine() + " : " + this.element.getNomPresentation() + "\">" 
					+ Application.getApplication().getTraduction("WEB_MAIL") + "</A></div>");
	}
	
	/**
	 * Méthode permettant d'écrire le code html correspondant à la date de génération à chaque bas de page
	 * @param fd lien vers le fichier html de contenu
	 * @throws IOException
	 */
	public void ajouterVersionDate(FileWriter fd)throws IOException
	{
		fd.write("<div align=\"center\" class=\"date\">"  
					+ Application.getApplication().getTraduction("WEB_DATE_GEN") 
					+ " " + GenerationManager.getInstance().getDateGeneration() + "</div>");
	}
	
	/**
	 * Méthode permettant d'ajouter la description d'un élément
	 * @param fd
	 * @throws IOException
	 */
	public void ajouterDescription(FileWriter fd) throws IOException
	{
		if (!GenerationManager.getInstance().estInfoBulle())
		{
			String description = this.element.getDescription();
			if (description != null)
			{
				fd.write("<br><div class=\"description\">" + description + "</div>\n");
			}
		}
	}
	
	/**
	 * Méthode permettant de recopier le contenu associé à l'élément de présentation
	 * courant. 
	 * @param contenu nom du fichier de contenu associé à l'élément courant
	 * @param fd lien vers le fichier de contenu courant à remplir
	 */
	protected void recopierContenu(String contenu, FileWriter fd) throws IOException
	{
		String regexp = "src=\"images";
        Pattern modele = Pattern.compile(regexp);
        Matcher correspondance;
        

		File f = new File(this.getCheminContenu() + File.separator + contenu);
		if (f.exists())
		{
			FileReader fr = new FileReader(f);
			BufferedReader  br = new BufferedReader(fr);
			
			String ligne ;
			char[] retourChariot = new char[]{Character.LINE_SEPARATOR};
			while ((ligne = br.readLine()) != null)
			{
				// vérifie si l'on a un lien vers une image
				correspondance = modele.matcher(ligne);
				if (correspondance.find())
				{
					ligne = correspondance.replaceAll("src=\"" + this.getLienImage());
				}
				fd.write(ligne);
				fd.write(retourChariot);
			}
			br.close();
		}
		else
		{
			System.out.println("le fichier n'existe pas : " + f.getPath());
		}
	}
	
	public String toString()
	{
		return this.element.getNomPresentation();
	}
	/**
	 * @return Returns the iD.
	 */
	public long getID() {
		return ID;
	}
	/**
	 * @param id The iD to set.
	 */
	public void setID(long id) {
		ID = id;
	}
	/**
	 * @param arbre The arbre to set.
	 */
	public void setArbre(ArbreGeneration arbre) 
	{
		this.arbre = arbre;
	}
	
	/**
	 * Méthode appelée que pour les paquetages de présentation et les composants
	 * @param paquet
	 */
	public void extraireIconeContenu(PaquetagePresentation paquet) throws FileNotFoundException, IOException
	{
		//this.print(Application.getApplication().getTraduction("extraction_icone"));
		// Créer un flux d'entrée contenant l'archive ZIP à décompresser
		File f = new File(paquet.getNomFichier());
		FileInputStream fin = new FileInputStream(f);

		// Mettre ce flux en mémoire tampon
		BufferedInputStream bis = new BufferedInputStream(fin);

		// Identifier le flux tampon comme flux de compression ZIP
		ZipInputStream zin = new ZipInputStream(bis);
		

		// Définir un objet ZipEntry
		ZipEntry ze = null;

		// Tant que cet objet est différent de nul (tant qu'il y a des fichiers dans l'archive)...
		while ((ze = zin.getNextEntry()) != null)
		{
			 if (ze.toString().startsWith(paquet.getChemin_icone()) 
			 			|| ze.toString().startsWith(paquet.getChemin_contenu())
						|| ze.toString().startsWith(GenerationManager.IMAGES_PATH))
			 {
				 String fichier = ze.toString();
				 fichier = fichier.substring(fichier.indexOf("/") + 1, fichier.length());
				 fichier = fichier.substring(fichier.indexOf("\\") + 1, fichier.length());
				 FileOutputStream fout = null;
				 if (ze.toString().startsWith(paquet.getChemin_icone()))
				 {
				 	/*
					System.out.println(GenerationManager.getInstance().getCheminGeneration() 
																+ File.separator + CGenererSite.APPLET_PATH + File.separator + "images" + File.separator + fichier);
					*/
				 	
					 // Créer un flux de sortie pour le fichier de l'entrée courante
					 fout = new FileOutputStream(GenerationManager.getInstance().getCheminGeneration() 
											 + File.separator + GenerationManager.APPLET_PATH + File.separator + "images" + File.separator + fichier);
				 }
				 else if (ze.toString().startsWith(paquet.getChemin_contenu()))
				 {
					/*
				 	System.out.println(Application.getApplication().getProjet().getDefProc().getRepertoireGeneration() 
					+ "/" + CGenererSite.CONTENU_PATH + "/" + fichier);
					*/
					fout = new FileOutputStream(this.cheminParent + File.separator + GenerationManager.CONTENU_PATH + File.separator + fichier);
				 }
				 // les images
				 else
				 {
					fout = new FileOutputStream(this.cheminParent + File.separator + GenerationManager.IMAGES_PATH + File.separator + fichier);
				 }
				 ToolKit.dezipper(zin, fout);
			 }
		 }
		 // Fermer le flux d'entrée
		 zin.close(); 
	}
	
}
