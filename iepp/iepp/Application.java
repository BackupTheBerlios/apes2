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
 
 
package iepp;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;
import java.util.Vector;

import org.ipsquad.utils.ErrorManager;

import iepp.application.CChargerReferentielDemarrage;
import iepp.application.areferentiel.Referentiel;
import iepp.ui.FenetreChoixProcessus;
import iepp.ui.FenetrePrincipale;
import iepp.ui.FenetreChoixReferentiel;


/**
 * Classe principale du logiciel, cr�e et affiche la fenetre principale du logiciel
 */
public class Application {

	/**
	 * Num�ro de version de IEPP
	 */
	public static String NUMVERSION = "1.1.6";
	
	/**
	 * Lien vers l'instance courante de l'application
	 */
	private static Application instance = null ;

	/**
	 * Lien vers la fen�tre principale de l'application
	 */
	private FenetrePrincipale fenetrePpale = null ;
	
	/**
	 * Lien vers le projet courant utilis� dans l'application
	 * = null si aucun projet n'existe
	 */
	private Projet projet = null ;

	/**
	 * Lien vers le r�f�rentiel courant
	 * = null si aucun r�f�rentiel n'est ouvert
	 */
	private Referentiel referentiel = null ;


	/**
	 * Lien vers les proprietes de la configuration de l'outil
	 */
	private Properties config = null ;
	
	/**
	 * Contient les cles-valeurs utilis�s par la langue courante
	 */
	private Properties langueCourante ;

	/**
	 * chemin du fichier de propri�t� de l'application 
	 */
	private static final String msConfigDir = ".apes2";
	private static final String filesep = System.getProperty("file.separator");
	private static final String FICPROPRIETE = System.getProperty("user.home") + filesep + msConfigDir + filesep + "iepp.cfg";
	
	/**
	 * chaine de caract�re retourn�e si la cl� dans le fichier langue n'existe pas
	 * ou n'a pas pu �tre r�cup�r�e
	 */
	public static final String CHAINEERREUR = "#ERROR" ;
	
	/**
	 * noms de toutes les langues disponibles dans le r�pertoire de langue
	 */
	private Vector languesDisponibles = null ;
	
	/**
	 * noms de toutes les feuilles de style disponibles
	 */
	private Vector stylesDisponibles = null ;
	
	
	//----------------------------------------------------------------------------------//
	// Methodes																			//
	//----------------------------------------------------------------------------------//
	
	/**
	 * Constructeur uniquement utilis� par la classe Application
	 * Singleton
	 */
	private Application()
	{
		Application.instance = null ;
	}

	/**
	 * Programme Principal
	 * @param args arguments entr�s sur la ligne de commande
	 * @throws Exception Une exception est lanc�e si plusieurs instance de l'application sont en cours
	 */
	public static void main(String[] args) throws Exception
	{
		Application.creerApplication() ;
		Application.getApplication().lancer() ;
	}
	
	
	/**
	 * Cr�ation de l'instance unique de l'application
	 * @throws Exception
	 */
	public static void creerApplication() throws Exception
	{
		// si on essaye de cr�er plusieurs fois l'application
		if (Application.instance != null)
				// on ne peux pas car les fichiers de langue sont charg� apr�s
				throw new Exception ("Une instance de l'application a d�j� �t� cr��e") ;
		Application.instance = new Application() ;
	}

	/**
	 * Construit et affiche la fen�tre principale
	 */
	public void lancer()
	{
		// r�cup�rer la configuration du logiciel
		this.configurerApplication();
		// ainsi que toutes les langues disponibles
		this.recupererLanguesDisponibles(this.getConfigPropriete
									("dossierLangues"),this.getConfigPropriete("extensionLangue"));
		this.recupererStylesDisponibles(this.getConfigPropriete
									("styles"),this.getConfigPropriete("extensionFeuilleStyle"));
		this.chargerLangueCourante();
		// afficher la fenetre principale
		this.fenetrePpale = new FenetrePrincipale() ;
		this.fenetrePpale.rafraichirLangue();
		this.fenetrePpale.show();
		this.chargerReferentiel();

		// initialiser le gestionnaire d'erreur
		ErrorManager.getInstance().setOwner(this.fenetrePpale);
	}
	
	/**
	 * 
	 */
	private void chargerReferentiel() 
	{
		File ref = new File(Application.getApplication().getConfigPropriete("referentiel_demarrage"));
		if (ref.exists() && ref.isFile())
		{
			CChargerReferentielDemarrage c = new CChargerReferentielDemarrage(ref);
			if (c.executer())
			{
				Application.getApplication().getFenetrePrincipale().setTitle(
						Application.getApplication().getConfigPropriete("titre")
						+ " " + Application.getApplication().getReferentiel().getNomReferentiel());
				new FenetreChoixProcessus(this.fenetrePpale);
			}
			else
			{
				// modifier la configuration
				this.setConfigPropriete("referentiel_demarrage", "");
				FenetreChoixReferentiel fcr = new FenetreChoixReferentiel(this.fenetrePpale);
			}
		}
		else
		{
			// modifier la configuration
			this.setConfigPropriete("referentiel_demarrage", "");
			FenetreChoixReferentiel fcr = new FenetreChoixReferentiel(this.fenetrePpale);
		}
	}

	/**
	 * Cr�ation du lien vers le projet courant utilis� dans l'application
	 */
	public void creerProjet()
	{
		this.projet = new Projet() ;
	}

	/**
	 * R�cup�re les propri�t�s de configuration de l'application
	 */
	public void configurerApplication()
	{
		// cr�er le r�pertoire o� sera stock� le .cfg de l'appli s'il n'existe pas
		File cfg_dir = new File(System.getProperty("user.home") + filesep + msConfigDir + filesep);
		if(!cfg_dir.exists())
		{
			cfg_dir.mkdir();
		}
		
		this.config = new Properties() ;
		// charger le fichier dans lequel est stock�e la configuration
		try
		{
			this.config.load( new FileInputStream (Application.FICPROPRIETE));
		}
		catch (FileNotFoundException e)
		{
			// le fichier n'existe pas, on cr�e une configuration par d�fault
			this.creerConfigDefaut();
		}
		catch (IOException e)
		{
			// probl�me de lecture du fichier
			e.printStackTrace();
		}
	}

	/**
	 * M�thode appel�e lorsque le fichier de configuration n'a pas �t� trouv�
	 */
	public void creerConfigDefaut()
	{	
		this.config.put("dossierIcons", "ressources/icons/");
		this.config.put("titre", "IEPP");
		this.config.put("extensionLangue" ,"lng");
		this.config.put("dossierLangues", "ressources/langues/");
		this.config.put("langueCourante", "Francais");
		this.config.put("styles", "ressources/feuillesDeStyle/");
		this.config.put("extensionFeuilleStyle", "css");
		this.config.put("site","ressources/site/");
		this.config.put("couleur_fond_diagrmme", "-1");
		this.config.put("couleur_arbre", "-10053121");
		this.config.put("feuille_style", "Silver");
		this.config.put("repertoire_generation", "./WebSite/");
		this.config.put("chemin_referentiel","./Repository/");
		this.config.put("place_contenu","1");
		this.config.put("info_bulle", "1");
		this.config.put("statistiques", "0");
		this.config.put("recapitulatif", "0");
		
		this.sauvegarderConfig();
	}
	
	/**
	 * M�thode permettant de sauvegarder dans un fichier la configuration de l'outil 
	 */
	public void sauvegarderConfig()
	{
		try 
		{
			this.config.save(new FileOutputStream((Application.FICPROPRIETE)), "IEPP Configuration");
		}
		catch (FileNotFoundException e)
		{
			// proble d'�criture du fichier
			e.printStackTrace();
		}
	}
	
	/**
	 * Permet de charger le fichier de langue qui �tait utilis� lors de la derni�re utilisation
	 * de IEPP
	 */
	public void chargerLangueCourante()
	{
		this.langueCourante = new Properties() ;
		 //	charger le fichier dans lequel est stock�e la langue
		 try
		 {		
			 this.langueCourante.load(new FileInputStream (this.config.getProperty("dossierLangues") 
			+ this.config.getProperty("langueCourante")
			+ "." + this.config.getProperty("extensionLangue")));
		 }
		 catch (FileNotFoundException e)
		 {
			// le fichier n'existe pas, on charge le premier fichier langue trouv� 
			// change la langue et appele directement charger
			if (this.languesDisponibles.size() != 0)
			{
				Application.getApplication().setLangueCourante("" + this.languesDisponibles.elementAt(0));
			}
			else
			{
				// aucune langue disponible
				e.printStackTrace();
			}
		 }
		 catch (IOException e)
		 {
			 // probl�me de lecture du fichier
			 e.printStackTrace();
		 }
	}
	

	/**
	 * Recupere l'ensemble des langues diponibles pour l'application
	 */
	public void recupererLanguesDisponibles(String chemin, String extensionFic)
	{
		this.languesDisponibles = new Vector();
		// parcourir le r�pertoire chemin
		File file = new File(chemin);
		// lister tous les fichiers de ce r�pertoire
		File[] fliste = file.listFiles();
		for (int i = 0 ; i < fliste.length ; i++ )
		{
			// cherche uniquement sur les fichiers
			if (fliste[i].isFile())
			{
				// si le fichier courant correspond bien un fichier.lng, c'est un fichier de langue
			 	 if ( fliste[i].getName().substring(fliste[i].getName().length() - 1 - extensionFic.length()).equals(".".concat(extensionFic)))
			  	 {
			  		// on remplit notre liste de fichiers de langue
					this.languesDisponibles.addElement(fliste[i].getName().substring
								( 0 , fliste[i].getName().length()-1- extensionFic.length()));
			  	 }
			 }
		}
	}

	/**
	 * Recupere l'ensemble des feuilles de style diponibles pour l'application
	 */
	public void recupererStylesDisponibles(String chemin, String extensionFic)
	{
		this.stylesDisponibles = new Vector();
		// parcourir le r�pertoire chemin
		File file = new File(chemin);
		// lister tous les fichiers de ce r�pertoire
		File[] fliste = file.listFiles();
		for (int i = 0 ; i < fliste.length ; i++ )
		{
			// cherche uniquement sur les fichiers
			if (fliste[i].isFile())
			{
				// si le fichier courant correspond bien un fichier.lng, c'est un fichier de langue
			 	 if ( fliste[i].getName().substring(fliste[i].getName().length() - 1 - extensionFic.length()).equals(".".concat(extensionFic)))
			  	 {
			  		// on remplit notre liste de fichiers de langue
			 	 	this.stylesDisponibles.addElement(fliste[i].getName().substring
								( 0 , fliste[i].getName().length()-1- extensionFic.length()));
			  	 }
			 }
		}
	}

	//----------------------------------------------------------------------------------//
	// Getters et Setters																//
	//----------------------------------------------------------------------------------//
	
	/**
	 * R�cup�re l'instance du projet courant
	 * @return le projet courant
	 */
	public Projet getProjet()
	{
		return this.projet ;
	}
	

	/**
	 * <b>Date de cr�ation</b> : 4 mars 2004<p>
	 * R�cup�re l'instance du r�f�rentiel courant.
	 * @author Sylvain Lavalley
	 * @return le r�f�rentiel courant
	 */
	public Referentiel getReferentiel ()
	{
		return this.referentiel ;
	}


	/**
	 * R�cup�re le lien vers la fen�tre principale de l'application
	 * @return la fen�tre principale
	 */
	public FenetrePrincipale getFenetrePrincipale()
	{
		return this.fenetrePpale ;
	}
	
	/**
	 * R�cup�re le lien vers l'application courante
	 * @return
	 */
	public static Application getApplication ()
	{
		return Application.instance ;
	}

	/**
	 * Renvoie la chaine de caract�re associ�e � la cl� s dans le fichier de configuration
	 * @param s cl� dont on veut r�cup�rer la valeur
	 * @return valeur associ�e � la cl� dans le fichier de configuration
	 */
	public String getConfigPropriete(String s)
	{
		if ( this.config == null )
		{
			return "" ;
		}
		String retour = this.config.getProperty(s);
		// si la cl� n'existe pas on renvoie une chaine nulle
		if ( retour == null )
		{
			return "";
		}
		// renvoie la valeur r�cup�r�e
		return retour;
	}
	
	public void setConfigPropriete(String s,String valeur)
	{
		this.config.put(s,valeur);
		this.sauvegarderConfig();
	}
	
	/**
	 * Renvoie la chaine de caract�re associ�e � la cl� s dans le fichier de langue
	 * @param s cl� dont on veut r�cup�rer la traduction
	 * @return valeur associ�e � la cl� dans le fichier de langue
	 */
	public String getTraduction(String s)
	{
		if ( this.langueCourante == null )
		{
			return Application.CHAINEERREUR;
		}
		String retour = this.langueCourante.getProperty(s);
		// si la cl� n'existe pas on renvoie une chaine nulle
		if ( retour == null )
		{
			return Application.CHAINEERREUR;
		}
		// renvoie la valeur r�cup�r�e
		return retour;
	}
	
	/**
	 * Renvoie la liste des langues disponibles r�cup�r�e
	 */
	public Vector getLangues()
	{
		return this.languesDisponibles ;
	}
	
	/**
	 * Renvoie la liste des feuilles de style disponibles r�cup�r�e
	 */
	public Vector getStyles()
	{
		return this.stylesDisponibles ;
	}
	
	/**
	 * Met � jour la langue courante utilis�e dans l'application
	 * Charge le fichier de langues
	 * @param s
	 */
	public void setLangueCourante(String s)
	{
		// modifier la langue utilis�e dans le logiciel pour la r�cup�rer lors de la prochaine utilisation
		this.config.put("langueCourante", s);
		// sauvegarder cette modification
		this.sauvegarderConfig();
		this.chargerLangueCourante();
	}
	
	public void setReferentiel(Referentiel r)
	{
		this.referentiel = r;
		this.fenetrePpale.setReferentielArbre(this.referentiel);
		this.fenetrePpale.majEtat();
	}
	
	//-----------------------------------------------------
	// Indispensables pour les tests
	//-----------------------------------------------------
	
	/**
	 * Indique si la configuration est vide ou pas
	 * @return true, si la configuration est vide
	 */
	public boolean estConfigVide()
	{
		return this.config.isEmpty();
	}
	
	/**
	 * Indique si le fichier de la langue courante est vide ou pas
	 * @return true, si le fichier de la langue courante est vide
	 */
	public boolean estLangueVide()
	{
		return this.langueCourante.isEmpty();
	}
	
	/**
	 * Reinitialise l'instance de l'application courante
	 */
	public static void reinitialiser()
	{
		Application.instance = null ;
	}
	
	public static void setProjet(Projet p)
	{
		instance.projet = p ;
		instance.fenetrePpale.majEtat();
	}
	
	public static void deleteProjet()
	{
		instance.projet = null;
	}
	
	/**
	 * Modifie la fen�tre principale
	 * @param FP, nouvelle fen�tre principale
	 */
	public void setFenetrePpale(FenetrePrincipale FP)
	{
		fenetrePpale = FP;
	}
}
