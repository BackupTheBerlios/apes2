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
import iepp.domaine.ComposantProcessus;
import iepp.domaine.ElementPresentation;
import iepp.domaine.Guide;
import iepp.domaine.IdObjetModele;
import iepp.domaine.LienProduits;
import iepp.domaine.PaquetagePresentation;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Vector;
import java.awt.*;
import java.io.FileWriter;
import java.io.PrintWriter;

/**
 * Classe permettant de garder la configuration de la g�n�ration
 * c'est-�-dire, toutes les options de g�n�ration que l'utilisateur
 * a rentr� dans la bo�te de dialogue de g�n�ration.
 * Pour acc�der � une propri�t�, il faut faire GenerationManager.getInstance().getxxxx()
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
	private static HashMap listeProduitsChanges;
    private static Vector listeProduitsSortie;
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
	public static final String IMAGES_PATH = "images";
    
	
    

	

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
		GenerationManager.place_contenu = Application.getApplication().getConfigPropriete("place_contenu");
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
		
		//this.setFeuilleCss(Application.getApplication().getConfigPropriete("feuille_style"));
		GenerationManager.couleur_surlign = new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre")));
		GenerationManager.place_contenu = Application.getApplication().getConfigPropriete("place_contenu");
	}
	
	/**
	 * M�thode permettant d'initialiser le chemin de g�n�ration
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
		return(GenerationManager.feuilleCss + "." + Application.getApplication().getConfigPropriete("extensionFeuilleStyle"));
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
	
	public void setPlaceContenu( String place )
	{
		GenerationManager.place_contenu = place;
	}
	/**
	 * Indique si le contenu doit �tre plac� avant ou apr�s le diagramme
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
	 * Renvoie la liste � g�n�rer (A remplacer par l'arbre)
	 * @return
	 */
	public Vector getListeAGenerer()
	{
		return(GenerationManager.listeAGenerer);
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



    /**
     * Remplie la liste des produits en entree lies
     * @param listeProduitsChanges
     */
    public static void setListeProduitsChanges(HashMap listeProduitsChanges)
    {
        GenerationManager.listeProduitsChanges = listeProduitsChanges;
    }

    /**
     * Recupere la liste des produits en entree lies
     */
    public static HashMap getListeProduitsChanges()
    {
        return listeProduitsChanges;
    }

    /**
     * Remplie la liste des produits en sortie
     * @param listeProduitsSortie
     */
    public static void setListeProduitsSortie(Vector listeProduitsSortie)
    {
        GenerationManager.listeProduitsSortie = listeProduitsSortie;
    }
    
    /**
     * Recupere la liste des produits en sortie
     * @return la liste des produits en sortie
     */
    public static Vector getListeProduitsSortie()
    {
        return listeProduitsSortie;
    }
    
    /**
	 * r�cup�rer la liste des produits en entr�e et v�rifier s'ils sont ext�rieurs au processus
	 * @param idComposant
	 */
	public static void recupererProduitsExterieurs()
	{
		//liste des produits ext�rieurs: les produits en entr�e qui sont en sortie d'aucun composant
		Vector listeProduitsExterieurs = new Vector();
		
		// Liste des produits en entree lies avec d'autres
		HashMap listeProduitsChanges = new HashMap();
		
		// Liste des produits en sortie de composants
		Vector listeProduitsSortie = new Vector();
		
		// TODO SP rajouter les produits ex�tieurs, la langue
		Vector liste = GenerationManager.getInstance().getListeAGenerer();
		PaquetagePresentation paquet ;
		IdObjetModele idComposant ;
		for (int i = 0; i < liste.size(); i++)
		{
			if (liste.elementAt(i) instanceof IdObjetModele)
			{
				// composant publiable
				//on recupere l'ID du i�me composant de la definition de Processus
				idComposant = (IdObjetModele)liste.elementAt(i);
				Vector listeProduits = idComposant.getProduitEntree();
				Vector listeLiens = ((ComposantProcessus)idComposant.getRef()).getLien();
				
				// Ajouter tous les noms des produits en sortie
				Vector sortie = idComposant.getProduitSortie();
				for (int j = 0; j < sortie.size(); j++)
				{
				    listeProduitsSortie.add(idComposant.getRef().toString() + "::" + sortie.elementAt(j).toString());
				}
				
				// Verifier s'il s'agit d'un composant vide, auquel cas il faut verifier les produits en sortie
				if (idComposant.estComposantVide())
				{
				    listeProduits.addAll(idComposant.getProduitSortie());
				}
				for(int j = 0; j < listeProduits.size(); j++)
				{
				 	IdObjetModele idProduit = (IdObjetModele)listeProduits.elementAt(j);
				 	// Si le composant n'a pas de lien, les produits ne peuvent etre lies
				 	if (listeLiens.size() == 0)
				 	{
				 		listeProduitsExterieurs.addElement(idProduit);
				 	}
				 	else
				 	{
					 	for (int k = 0; k < listeLiens.size(); k++)
					 	{
					 		LienProduits lien = (LienProduits)listeLiens.elementAt(k);
					 		if (!lien.contient(idProduit))
					 		{
					 			listeProduitsExterieurs.addElement(idProduit);
					 		}
					 		else
					 		{
					 		    // Si le produit en entree est lie, on le note pour changer son lien vers le produit lie
					 		    IdObjetModele produitCible;
					 		    if (lien.getProduitEntree() == idProduit)
					 		    {
					 		        produitCible = lien.getProduitSortie();
					 		    }
					 		    else
					 		    {
					 		        produitCible = lien.getProduitEntree();
					 		    }
					 		    listeProduitsChanges.put(idProduit.getRef().toString() +"::"+ idProduit.toString(),produitCible);
					 		}
					 	}
				 	}
				}
			}
		}
		GenerationManager.setListeProduitsChanges(listeProduitsChanges);
		GenerationManager.setListeProduitsExterieurs(listeProduitsExterieurs);
		GenerationManager.setListeProduitsSortie(listeProduitsSortie);
		System.out.println("Produits exterieurs " + listeProduitsExterieurs);
	}
	
	/**
	 * 
	 */
	public static void construireArbre(ArbreGeneration arbre, PrintWriter fd) 
	{
		//TODO ajouter u print
		Vector liste = GenerationManager.getInstance().getListeAGenerer();
		PaquetagePresentation paquet ;
		IdObjetModele idComposant ;

		for (int i = 0; i < liste.size(); i++)
		{
			if (liste.elementAt(i) instanceof PaquetagePresentation)
			{
				paquet = (PaquetagePresentation)liste.elementAt(i);
				construireArbrePaquetage(arbre, paquet, fd);
			}
			else
			{
				// composant publiable
				//on recupere l'ID du i�me composant de la definition de Processus
				idComposant = (IdObjetModele)liste.elementAt(i);
				paquet = idComposant.getPaquetagePresentation();
				if (paquet != null && !paquet.getNomFichier().equals(""))
				{
					construireArbreComposant(arbre, paquet, fd);
				}
			}
		}
	}
	
	/**
	 * Construire l'arbre correspondant au paquetage de pr�sentation
	 * @param fd
	 */
	public static void construireArbrePaquetage(ArbreGeneration arbre, PaquetagePresentation paquetage, PrintWriter pwFicTree) 
	{
		Vector liste ; // liste en cours de traitement
		int i;
		ArbreGeneration nouvelArbre = null;
		
		paquetage.trierElement();
		liste = paquetage.getListeElement();
		
		// le premier �l�ment est la racine du paquetage
		if (liste.size() >= 1)
		{
			ElementPresentation elem = (ElementPresentation)liste.elementAt(0);
			GElement noeud = new GPaquetagePresentation(elem, paquetage, pwFicTree);
			nouvelArbre = new ArbreGeneration(noeud);
			arbre.ajouterSousArbre(nouvelArbre);
		}
			
		for (i = 0; i < liste.size() ; i++)
		{
			ElementPresentation elem = (ElementPresentation)liste.elementAt(i);
			GElement noeud = new GElement(elem, pwFicTree);
			if (elem.getNiveau() == 2)
			{
				// rajoute directement au nouvel arbre
				ArbreGeneration n = new ArbreGeneration(noeud);
				nouvelArbre.ajouterSousArbre(n);
			}
			else if (elem.getNiveau() >= 2)
			{
				String nouvelID = elem.getID_interne().substring(elem.getID_interne().indexOf("-") + 1);
				nouvelArbre.ajouterSousArbre(noeud, nouvelID);
			}
		}
	}
	
	/**
	 * @param generation
	 * @param fd
	 * @param paquet
	 */
	public static void construireArbreComposant(ArbreGeneration generation, PaquetagePresentation paquetage, PrintWriter fd) 
	{
		Vector liste ; // liste en cours de traitement
		int i;
		ArbreGeneration nouvelArbre = null;
		
		// trier
		paquetage.trierElement();
		liste = paquetage.getListeElement();
		
		// le premier �l�ment est la racine du composant
		if (liste.size() >= 1)
		{
			ElementPresentation elem = (ElementPresentation)liste.elementAt(0);
			GElement noeud = getGenerateurCorrepondant(elem, fd);
			nouvelArbre = new ArbreGeneration(noeud);
			generation.ajouterSousArbre(nouvelArbre);
		}
			
		for (i = 0; i < liste.size() ; i++)
		{
			ElementPresentation elem = (ElementPresentation)liste.elementAt(i);
			GElement noeud = getGenerateurCorrepondant(elem, fd);
			if (elem.getNiveau() == 2)
			{
				// rajoute directement au nouvel arbre
				ArbreGeneration n = new ArbreGeneration(noeud);
				nouvelArbre.ajouterSousArbre(n);
			}
			else if (elem.getNiveau() >= 2)
			{
				String nouvelID = elem.getID_interne().substring(elem.getID_interne().indexOf("-") + 1);
				nouvelArbre.ajouterSousArbre(noeud, nouvelID);
			}
		}
	}
	
	/**
	 * Construit les gestionnaires de publication associ�s au type des �l�ments � traiter
	 * @param elem element de pr�sentation qu'il faut traiter
	 */
	public static GElement getGenerateurCorrepondant(ElementPresentation elem, PrintWriter pwFicTree) 
	{
		// selon le type de l'�l�ment de pr�sentation
		if (elem instanceof Guide)
		{
			GGuide guide = new GGuide(elem,pwFicTree);
			return guide;		
		}
		
		// on v�rifie que l'on ait bien un mod�le associ�
		if(elem.getElementModele() != null)
		{
			// c'est un �l�ment normal il faut r�cup�rer le type du mod�le associ�
			IdObjetModele id = elem.getElementModele();
			
			if (id.estComposant())
			{
				// composant
				GComposantPubliable compo = new GComposantPubliable(id, elem, pwFicTree ); 
				return compo;
			}
			
			if (id.estActivite())
			{
				// activite 
				GActivite activite = new GActivite(elem, pwFicTree ); 
				return activite;
			}
			
			if (id.estProduit())
			{
				// produit
				GProduit produit = new GProduit(elem, pwFicTree ); 
				return produit;
			}
			
			if (id.estDefinitionTravail())
			{
				// deftravail
				GDefinitionTravail defTrav = new GDefinitionTravail(elem, pwFicTree ); 
				return defTrav;
			}
			
			if (id.estDiagramme())
			{
				// diagramme
				GDiagramme diag = new GDiagramme((ComposantProcessus)id.getRef(), elem, pwFicTree ); 
				return diag;	
			}
			
			if (id.estRole())
			{
				// role
				GRole role = new GRole(elem, pwFicTree ); 
				return role;
			}
			
			if (id.estPaquetage())
			{
				GPaquetage gelem = new GPaquetage(elem, pwFicTree ); 
				return gelem;
			}
		
			else
			{
				GElementModele gelem = new GElementModele(elem, pwFicTree ); 
				return gelem;
			}
		}
		return null;
	}
}
