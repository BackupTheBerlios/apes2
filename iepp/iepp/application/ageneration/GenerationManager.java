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
 * Classe permettant de garder la configuration de la g�n�ration
 * c'est-�-dire, toutes les options de g�n�ration que l'utilisateur
 * a rentr� dans la bo�te de dialogue de g�n�ration.
 * Pour acc�der � une propri�t�, il faut faire GenerationManager.getInstance().getxxxx()
 * AJOUTER DES COMMENTAIRES JAVADOC ET AUTRES
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
	private static boolean brancheObjet ;
	private static Vector listeAGenerer;
	private static Color couleur_surlign;
	
	// mettre tous les autres attributs en private static

	private static int type_page_composant = 0 ;
	private static int type_page_deftravail = 3 ;

	// options pour les pages diagrammes
	public static final int TYPE_COMP_TABLEAU = 0 ;
	public static final int TYPE_COMP_FLOT = 1 ;
	public static final int TYPE_COMP_RESP = 2 ;
	
	// options pour les pages definitions de travail
	public static final int TYPE_DEFP_TABLEAU = 3 ;
	public static final int TYPE_DEFP_ACT = 4 ;
	public static final int TYPE_DEFP_FLOT = 5 ;

	

	/**
	 * Constructeur du manager de la g�n�ration
	 * Initialise les options avec des valeurs par d�faut
	 */
	private GenerationManager()
	{
		// mettre ici des valeurs par d�faut
		this.setFeuilleCss(Application.getApplication().getConfigPropriete("feuille_style"));
		GenerationManager.couleur_surlign = new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre")));
		GenerationManager.cheminGeneration = Application.getApplication().getConfigPropriete("repertoire_generation");
		GenerationManager.brancheObjet = false;
	}


	
	/**  
	 * Retourne l'instance du manager de la g�n�ration
	 */
	public static GenerationManager getInstance()
	{
		return generation;
	}
	
	//--------------------------------------------------//
	// Setters de propri�t�s							//
	//--------------------------------------------------//
	
	/**
	 * m�thode appel�e lorsque la g�n�ration d�bute, permet
	 * de sauvegarde la date de g�n�ration � afficher sur toutes les
	 * pages du site
	 */
	public void debuterGeneration()
	{
		Date current_date = new Date();
		SimpleDateFormat formatter = new SimpleDateFormat("dd/MM/yyyy");
		GenerationManager.dateGeneration =  "[" + formatter.format(current_date) + "] ";
	}
	
	/**
	 * M�thode permettant d'initialiser le chemin de g�n�ration
	 */
	public void setCheminGeneration(String chemin)
	{
		GenerationManager.cheminGeneration = chemin;
	}
	
	/**
	 * M�thode permettant d'initialiser la feuille de style css
	 */
	public void setFeuilleCss(String nomFeuille)
	{
		GenerationManager.feuilleCss = nomFeuille;
	}
	
	/**
	 * M�thode permettant d'initialiser la couleur des �l�ments surlign�s dans l'arbre du site
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
	// Getters de propri�t�s							//
	//--------------------------------------------------//

	/**
	 * M�thode permettant de r�cup�rer le chemin de g�n�ration
	 */
	public String getCheminGeneration()
	{
		return(GenerationManager.cheminGeneration);
	}
	
	/**
	 * M�thode permettant de r�cup�rer le nom de la feuille de style css
	 */
	public String getFeuilleCss()
	{
		return(GenerationManager.feuilleCss);
	}
	
	/**
	 * M�thode permettant de r�cup�rer la couleur des �l�ments surlign�s dans l'arbre du site
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
	
	public Color getCouleur()
	{
		return GenerationManager.couleur_surlign;
	}
	
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

}
