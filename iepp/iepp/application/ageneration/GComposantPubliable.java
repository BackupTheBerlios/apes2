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


import iepp.domaine.ElementPresentation;
import iepp.domaine.Guide;
import iepp.domaine.IdObjetModele;
import iepp.domaine.PaquetagePresentation;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Vector;


/**
 * Classe permettant de gérer la génération de tout un composant publiable
 */
public class GComposantPubliable  
{	
	
	protected final String DossierActivites = "activites";
	protected final String DossierProduits = "produits";
	protected final String DossierRoles = "roles";
	protected final String DossierProdEntree = "Produits_en_entree";
	protected final String DossierProdSortie = "Produits_en_sortie";
	protected final String DossierDiag = "diagrammes";
	protected final String DossierDiagAct = "Diagrammes_activites";
	protected final String DossierDiagClasse = "Diagrammes_classes";
	protected final String DossierDefinition = "definitions_travail";
	protected final String DossierPaquetages = "paquetages";
	
	/**
	 * Lien vers le fichier tree.js à construire durant la génération
	 */
	private PrintWriter pwFicTree ;
	
	/**
	 * Paquetage de présentation associé au composant
	 */
	private PaquetagePresentation paquetage ;
	
	/**
	 * Liste des dossiers déjà construits dans l'arbre de navigation
	 */
	private HashMap idDossier ;
	
	/**
	 * Id du composant que l'on publie
	 */
	private IdObjetModele composant ;


	
	/**
	 * Constructeur du gestionnaire de publication du composant publiable
	 * @param idComposant id du composant (modèle) à générer
	 * @param pwFicTree lien vers le fichier tree.js à remplir
	 */
	public GComposantPubliable (IdObjetModele idComposant, PrintWriter pwFicTree)
	{
		this.composant = idComposant;
		this.pwFicTree = pwFicTree ;
		this.paquetage = this.composant.getPaquetagePresentation();
		this.paquetage.trierElement();
		this.idDossier = new HashMap();
		this.idDossier.put("", "foldersTree");
	}

	/**
	 * permet de traiter la génération d'un élément ou d'un guide
	 */	
	public void traiterGeneration() throws IOException
	{
		// création des répertoires indispensables pour le stockage des fichiers
		this.creerRep();
		
		// récupérer la liste des élément de présentation du composant publiable
		Vector liste = this.paquetage.getListeElement();
		if (liste.size() > 0)
		{
			int i;
			ElementPresentation elem;
			for (i = 0; i < liste.size() - 1; i++)
			{
				elem = (ElementPresentation)liste.elementAt(i);
				ElementPresentation elem2 = (ElementPresentation)liste.elementAt(i + 1);
				this.traiterElement(elem, elem2);
			}
			elem = (ElementPresentation)liste.elementAt(i);
			this.traiterElement(elem, null);
		}	
	}


	/**
	 * Construit les gestionnaires de publication associés au type des éléments à traiter
	 * @param elem element de présentation qu'il faut traiter en premier
	 * @param elem2 élément de présentation à traiter ensuite
	 */
	private void traiterElement(ElementPresentation elem, ElementPresentation elem2) throws IOException
	{
		// selon le type de l'élément de présentation
		if (elem instanceof Guide)
		{
			GGuide guide = new GGuide(elem, elem2, this.idDossier, this.pwFicTree);
			guide.traiterGeneration();		
		}
		// on vérifie que l'on ait bien un modèle associé
		else if(elem.getElementModele() != null)
		{
			// c'est un élément normal il faut récupérer le type du modèle associé
			IdObjetModele id = elem.getElementModele();
			if (id.estActivite())
			{
				// Gactivite 
				GActivite activite = new GActivite(elem, elem2, this.idDossier, this.pwFicTree ); 
				activite.traiterGeneration();
			}
			else if (id.estComposant())
			{
				// produit
				GComposant compo = new GComposant(elem, elem2, this.idDossier, this.pwFicTree ); 
				compo.traiterGeneration();
			}
			else if (id.estProduit())
			{
				// produit
				GProduit produit = new GProduit(elem, elem2, this.idDossier, this.pwFicTree ); 
				produit.traiterGeneration();
			}
			else if (id.estDefinitionTravail())
			{
				// deftravail
				GDefinitionTravail defTrav = new GDefinitionTravail(elem, elem2, this.idDossier, this.pwFicTree ); 
				defTrav.traiterGeneration();
			}
			else if (id.estDiagramme())
			{
				// diagramme
				GDiagramme diag = new GDiagramme(elem, elem2, this.idDossier, this.pwFicTree ); 
				diag.traiterGeneration();	
			}
			else if (id.estRole())
			{
				// role
				GRole role = new GRole(elem, elem2, this.idDossier, this.pwFicTree ); 
				role.traiterGeneration();
			}
			else if (id.estPaquetage())
			{
				GPaquetage gelem = new GPaquetage(elem, elem2, this.idDossier, this.pwFicTree ); 
				gelem.traiterGeneration();
			}
			else
			{
				GElementModele gelem = new GElementModele(elem, elem2, this.idDossier, this.pwFicTree ); 
				gelem.traiterGeneration();
			}
		}
	}
		
	/**
	 * Méthode permettant de créer tous les répertoires utiles
	 * pour stocker tous le contenu lié à un composant publiable
	 */
	public void creerRep()
	{
		File repComp = new File(this.getChemin());
		repComp.mkdirs();
	
		File repProd = new File(this.getChemin() + File.separator + DossierProduits);
		repProd.mkdirs();
	
		File repActivite = new File(this.getChemin() + File.separator + DossierActivites);
		repActivite.mkdirs();
	
		File repRoles = new File(this.getChemin() + File.separator + DossierRoles);
		repRoles.mkdirs();
	
		File repDiag = new File(this.getChemin()+ File.separator + DossierDiag);
		repDiag.mkdirs();
	
		File repDef = new File(this.getChemin()+ File.separator + DossierDefinition);
		repDef.mkdirs();
		
		File repPaq = new File(this.getChemin()+ File.separator + DossierPaquetages);
		repPaq.mkdirs();
	}
	
	/**
	 * Méthode permettant de construire le nom du répertoire associé au composant publiable
	 * courant
	 * @return chemin à partir de la racine du site répertoire associé au composant publiable
	 */
	public String getChemin()
	{
		return (GenerationManager.getInstance().getCheminGeneration()
					+ "/" + CodeHTML.normalizeName(this.composant.toString()));
	}

}




