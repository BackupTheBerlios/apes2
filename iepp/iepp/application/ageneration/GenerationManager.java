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

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Vector;
import java.awt.*;

/**
 * Classe permettant de garder la configuration de la génération
 * c'est-à-dire, toutes les options de génération que l'utilisateur
 * a rentré dans la boîte de dialogue de génération.
 * Pour accéder à une propriété, il faut faire GenerationManager.getInstance().getxxxx()
 */
public class GenerationManager 
{

	/**
	 * Instance unique de la classe GenerationManager
	 */
	private static GenerationManager generation = new GenerationManager();
	private static String cheminGeneration ;
	private static String feuilleCss ;
	private static String dateGeneration ;
	private static Vector listeAGenerer;
	private static Color couleur_surlign;
	private static Vector listeProduitsExterieurs;
	private static String place_contenu;
	
	// mettre tous les autres attributs en private static

	private static int type_page_composant = 0 ;
	private static int type_page_deftravail = 3 ;
	
	public static final String AVANT_CONTENU = "0";
	public static final String APRES_CONTENU = "1";
	
	public static final String CONTENU_PATH = "contenu";
	public static final String DESCRIPTION_PATH = "description";
	public static final String EXTERIEUR_PATH = "exterieur";
	public static final String APPLET_PATH = "applet";
	public static final String STYLES_PATH = "styles";
	
    

	

	/**
	 * Constructeur du manager de la génération
	 * Initialise les options avec des valeurs par défaut
	 */
	private GenerationManager()
	{
		// mettre ici des valeurs par défaut
		this.setFeuilleCss(Application.getApplication().getConfigPropriete("feuille_style"));
		GenerationManager.couleur_surlign = new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre")));
		GenerationManager.cheminGeneration = Application.getApplication().getConfigPropriete("repertoire_generation");
		GenerationManager.place_contenu = Application.getApplication().getConfigPropriete("place_contenu");
	}


	
	/**  
	 * Retourne l'instance du manager de la génération
	 */
	public static GenerationManager getInstance()
	{
		return generation;
	}
	
	//--------------------------------------------------//
	// Setters de propriétés							//
	//--------------------------------------------------//
	
	/**
	 * méthode appelée lorsque la génération débute, permet
	 * de sauvegarde la date de génération à afficher sur toutes les
	 * pages du site
	 */
	public void debuterGeneration()
	{
		Date current_date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		GenerationManager.dateGeneration =  "[" + formatter.format(current_date) + "] ";
		
		//this.setFeuilleCss(Application.getApplication().getConfigPropriete("feuille_style"));
		GenerationManager.couleur_surlign = new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre")));
		GenerationManager.place_contenu = Application.getApplication().getConfigPropriete("place_contenu");
	}
	
	/**
	 * Méthode permettant d'initialiser le chemin de génération
	 */
	public void setCheminGeneration(String chemin)
	{
		if (!chemin.equals(""))
		{
			GenerationManager.cheminGeneration = chemin;
		}
		else
		{
			GenerationManager.cheminGeneration = "./";
		}
	}
	
	/**
	 * Méthode permettant d'initialiser la feuille de style css
	 */
	public void setFeuilleCss(String nomFeuille)
	{
		GenerationManager.feuilleCss = nomFeuille;
	}
	
	/**
	 * Méthode permettant d'initialiser la couleur des éléments surlignés dans l'arbre du site
	 */
	public void setCouleurSurligne(Color couleur)
	{
		GenerationManager.couleur_surlign = couleur;
	}
	
	public void setListeAGenerer(Vector liste)
	{
		GenerationManager.listeAGenerer = liste ;
	}
	
	public void setTypeComposant(int type)
	{
		GenerationManager.type_page_composant = type;
	}

	public void setTypeDefTravail(int type)
	{
		GenerationManager.type_page_deftravail = type;
	}
	
	//--------------------------------------------------//
	// Getters de propriétés							//
	//--------------------------------------------------//

	/**
	 * Méthode permettant de récupérer le chemin de génération
	 */
	public String getCheminGeneration()
	{
		return(GenerationManager.cheminGeneration);
	}
	
	/**
	 * Méthode permettant de récupérer le nom de la feuille de style css
	 */
	public String getFeuilleCss()
	{
		return(GenerationManager.feuilleCss + "." + Application.getApplication().getConfigPropriete("extensionFeuilleStyle"));
	}
	
	/**
	 * Méthode permettant de récupérer la couleur des éléments surlignés dans l'arbre du site
	 */
	
	public String getCouleurSurligne()
	{
		String result = "#";
		int r = GenerationManager.couleur_surlign.getRed();
		String aux = Integer.toHexString(r);
		if (aux.length() == 1)
		{
			result += '0';
		}
		result += aux;

		int g = GenerationManager.couleur_surlign.getGreen();
		aux = Integer.toHexString(g);
		if (aux.length() == 1)
		{
			result += '0';
		}
		result += aux;
		
		int b = GenerationManager.couleur_surlign.getBlue();
		aux = Integer.toHexString(b);
		if (aux.length() == 1)
		{
			result += '0';
		}
		result += aux;
		return result;
	}
	
	public void setPlaceContenu( String place )
	{
		GenerationManager.place_contenu = place;
	}
	/**
	 * Indique si le contenu doit être placé avant ou après le diagramme
	 * @return
	 */
	public boolean estContenuAvant()
	{
	    return (GenerationManager.place_contenu.equals(GenerationManager.AVANT_CONTENU));
	}
	
	/**
	 * Renvoie la couleur de surlignement dans l'arbre de navigation
	 */
	public Color getCouleur()
	{
		return GenerationManager.couleur_surlign;
	}
	
	/**
	 * Renvoie la liste à générer (A remplacer par l'arbre)
	 * @return
	 */
	public Vector getListeAGenerer()
	{
		return(GenerationManager.listeAGenerer);
	}
	
	public int getTypeComposant()
	{
		return (GenerationManager.type_page_composant);
	}

	public int getTypeDefTravail()
	{
		return (GenerationManager.type_page_deftravail);
	}

	
	/**
	 * @return
	 */
	public String getDateGeneration()
	{
		return dateGeneration;
	}



    /**
     * Remplie la liste des produits exterieurs (n'ayant pas de presentation)
     * @param listeProduitsExterieurs Liste des produits exterieurs
     */
    public static void setListeProduitsExterieurs(Vector listeProduitsExterieurs)
    {
        GenerationManager.listeProduitsExterieurs=listeProduitsExterieurs;
    }
    
    /**
     * Recupere la liste des produits exterieurs (n'ayant pas de presentation)
     */
    public static Vector getListeProduitsExterieurs()
    {
        return (GenerationManager.listeProduitsExterieurs);
    }

}
