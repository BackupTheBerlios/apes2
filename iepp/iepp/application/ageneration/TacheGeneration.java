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

import java.io.*;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import iepp.domaine.*;
import iepp.ui.iedition.VueDPGraphe;

import iepp.Application;
import util.*;
import util.ImageUtil;
import util.MonitoredTaskBase;
import util.ErrorManager;


public class TacheGeneration extends MonitoredTaskBase {

	private PrintWriter pwFicTree ;
	private TaskMonitorDialog mTask = null;
	private boolean generationReussie = false;
	private ArbreGeneration arbre = null;
	
	protected Object processingTask()
	{
		this.genererSite();
		return null;
	}
	public boolean isGenerationReussie()
	{
		return this.generationReussie;
	}
	
	private void genererSite()
	{
		try
		{
			//récupérer l'heure de début de la génération
			GenerationManager.getInstance().debuterGeneration();
			
			this.arbre = new ArbreGeneration();
			
			this.recupererProduitsExterieurs();
			this.preparerGeneration();
			
			this.construireArbre();
			File f = new File(GenerationManager.getInstance().getCheminGeneration());
			this.arbre.initialiserArbre(ToolKit.removeSlashTerminatedPath(f.getAbsolutePath()));
			
			//System.out.println(arbre);
			// mettre ici les rajouts de page
			
			
			//Création des pages contenues dans la page d'accueil
			this.creerPageAccueil();
			this.arbre.genererSite();
			
			/*
			this.creerArbreApes();
			*/
			// fermeture du fichier tree.dat
			this.pwFicTree.close();
			
			this.generationReussie = true;
		}
		catch(Throwable t)
		{
			this.generationReussie = false;
			t.printStackTrace();
			this.traiterErreur();
			ErrorManager.getInstance().displayError(t.getMessage());
		
		}
	}

	/**
	 * 
	 */
	private void construireArbre() 
	{
		this.print("Construction arbre en mémoire");
		Vector liste = GenerationManager.getInstance().getListeAGenerer();
		PaquetagePresentation paquet ;
		IdObjetModele idComposant ;

		for (int i = 0; i < liste.size(); i++)
		{
			if (liste.elementAt(i) instanceof PaquetagePresentation)
			{
				paquet = (PaquetagePresentation)liste.elementAt(i);
				this.construireArbrePaquetage(this.arbre, paquet);
			}
			else
			{
				// composant publiable
				//on recupere l'ID du ième composant de la definition de Processus
				idComposant = (IdObjetModele)liste.elementAt(i);
				paquet = idComposant.getPaquetagePresentation();
				if (paquet != null && !paquet.getNomFichier().equals(""))
				{
					this.construireArbreComposant(this.arbre, paquet);
				}
			}
		}
	}
	

	private void preparerGeneration() throws Exception
	{
		this.print(Application.getApplication().getTraduction("creation_rep"));
		// Creation du dossier du site
		File rep = new File(GenerationManager.getInstance().getCheminGeneration());
		rep.mkdirs();
			
		/*
		// Création du dossier contenu
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.CONTENU_PATH );
		rep.mkdirs();
	*/
		
		// Création du dossier description
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.DESCRIPTION_PATH );
		rep.mkdirs();
		
		// Création du dossier pour le contenu extérieur au processus
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.EXTERIEUR_PATH );
		rep.mkdirs();
			
		// copie des répertoires ressource (javascript ...)
		Copie.copieRep(Application.getApplication().getConfigPropriete("site"), GenerationManager.getInstance().getCheminGeneration()+ File.separator + GenerationManager.APPLET_PATH);
	
		//copie du répertoire des feuilles de styles
		Copie.copieRep(Application.getApplication().getConfigPropriete("styles"), GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.STYLES_PATH);
	
		//Création du fichier tree.dat
		System.out.println(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.APPLET_PATH);
		this.creerFicTree(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.APPLET_PATH);	
	}


		private boolean creerArbreApes() throws FileNotFoundException, IOException
		{
			this.print(Application.getApplication().getTraduction("creation_pages"));
			Vector liste = GenerationManager.getInstance().getListeAGenerer();
			PaquetagePresentation paquet ;
			IdObjetModele idComposant ;

			for (int i = 0; i < liste.size(); i++)
			{
				if (liste.elementAt(i) instanceof PaquetagePresentation)
				{
					paquet = (PaquetagePresentation)liste.elementAt(i);
					this.print(Application.getApplication().getTraduction("traitement_paquetage") + " " + paquet.getNomPresentation());
					paquet.trierElement();
					// récupérer les icones et les contenuts pour chaque paquetage
					//extraireIconeContenu(paquet);
					// on traite la génération de ce paquetage
					/*
					GPaquetagePresentation paquetCourant = new GPaquetagePresentation (paquet, pwFicTree);
					paquetCourant.traiterGeneration();
					*/
				}
				else
				{
					// composant publiable
					//on recupere l'ID du ième composant de la definition de Processus
					idComposant = (IdObjetModele)liste.elementAt(i);
					paquet = idComposant.getPaquetagePresentation();
					if (paquet != null && !paquet.getNomFichier().equals(""))
					{
						 this.print(Application.getApplication().getTraduction("traitement_comp") + " " + paquet.getNomPresentation());
						 // récupérer les icones et les contenus pour chaque paquetage
						 //extraireIconeContenu(paquet);
						 /*
						 // on traite la génération de ce composant
						 GComposantPubliable compCourant = new GComposantPubliable (idComposant, pwFicTree);
						 compCourant.traiterGeneration();
						 */
					}
				}
			}
		
			return true;
		}



		/**
		 * récupérer la liste des produits en entrée et vérifier s'ils sont extérieurs au processus
		 * @param idComposant
		 */
		public void recupererProduitsExterieurs()
		{
			//liste des produits extérieurs: les produits en entrée qui sont en sortie d'aucun composant
			Vector listeProduitsExterieurs = new Vector();
			
			// Liste des produits en entree lies avec d'autres
			HashMap listeProduitsChanges = new HashMap();
			
			// Liste des produits en sortie de composants
			Vector listeProduitsSortie = new Vector();
			
			// SP rajouter les produits exétieurs, la langue
			this.print("Recuperation produits extérieurs");
			Vector liste = GenerationManager.getInstance().getListeAGenerer();
			PaquetagePresentation paquet ;
			IdObjetModele idComposant ;
			for (int i = 0; i < liste.size(); i++)
			{
				if (liste.elementAt(i) instanceof IdObjetModele)
				{
					// composant publiable
					//on recupere l'ID du ième composant de la definition de Processus
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
			System.out.println(listeProduitsExterieurs);
		}
		
		/**
		 * permet de creer les fichiers HTML corespondant à l'accueil du site
		 */
		public void creerPageAccueil() throws Exception
		{
			
			this.print(Application.getApplication().getTraduction("creation_page_acc"));
			// création du fichier index.html
			String nom = GenerationManager.getInstance().getCheminGeneration()+ File.separator + "index.html"  ;
			File ficHTML = new File (nom) ;
			FileWriter fd;
		
			fd = new FileWriter(nom);
			String fichierAccueil = CodeHTML.getPageAccueil(Application.getApplication().getProjet().getDefProc().getNomDefProc());
			fd.write(fichierAccueil) ;
			fd.close();
		
			// création de l'image du diagramme pour la page principale
			this.creerImagePng(Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe(), GenerationManager.getInstance().getCheminGeneration() + File.separator + "main") ;
			// création du fichier main.html
			nom =GenerationManager.getInstance().getCheminGeneration()+ File.separator + "main.html"  ;
			ficHTML = new File (nom) ;
		
			fd = new FileWriter(nom);
			String fichierMain = CodeHTML.getPagePrincipale(Application.getApplication().getProjet().getDefProc().getNomDefProc());
			fd.write(fichierMain) ;
			fd.close();
		
			// création du fichier arbre.html
			nom = GenerationManager.getInstance().getCheminGeneration()+ File.separator + GenerationManager.APPLET_PATH + File.separator + "arbre.html"  ;
			ficHTML = new File (nom) ;
		
			fd = new FileWriter(nom);
			String fichierArbre = CodeHTML.getArbre();
			fd.write(fichierArbre) ;
			fd.close();
		}
	
		public void creerFicTree(String cheminGen) throws Exception
		{
			this.print(Application.getApplication().getTraduction("creation_menu"));
			// création du fichier tree.dat
			pwFicTree =new PrintWriter(new BufferedWriter(new FileWriter(cheminGen + File.separator + "tree.js")), true);
			pwFicTree.println("USETEXTLINKS = 1");
			pwFicTree.println("STARTALLOPEN = 0");
			pwFicTree.println("HIGHLIGHT = 1");
			pwFicTree.println("HIGHLIGHT_BG = '" + GenerationManager.getInstance().getCouleurSurligne() + "'");
			pwFicTree.println("ICONPATH = 'images/'");
			pwFicTree.println("foldersTree = gFld(\"<i>" + Application.getApplication().getProjet().getDefProc().toString() + "</i>\", \"../main.html\")");
		}
	
		public void creerImagePng (VueDPGraphe diagramme, String nom)
		{
			try 
			{
				FileOutputStream fout = new FileOutputStream(new File(nom + ".png"));
				ImageUtil.encoderImage(diagramme, fout, "png");
			} 	
			catch(Exception e)
			{
				e.printStackTrace();
			}
		}
	

		

		/**
		 * Construire l'arbre correspondant au paquetage de présentation
		 */
		public void construireArbrePaquetage(ArbreGeneration arbre, PaquetagePresentation paquetage) 
		{
			Vector liste ; // liste en cours de traitement
			int i;
			ArbreGeneration nouvelArbre = null;
			
			paquetage.trierElement();
			liste = paquetage.getListeElement();
			
			// le premier élément est la racine du paquetage
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
		 * @param paquet
		 */
		public void construireArbreComposant(ArbreGeneration generation, PaquetagePresentation paquetage) 
		{
			Vector liste ; // liste en cours de traitement
			int i;
			ArbreGeneration nouvelArbre = null;
			
			// trier
			paquetage.trierElement();
			liste = paquetage.getListeElement();
			
			// le premier élément est la racine du composant
			if (liste.size() >= 1)
			{
				ElementPresentation elem = (ElementPresentation)liste.elementAt(0);
				GElement noeud = this.getGenerateurCorrepondant(elem);
				nouvelArbre = new ArbreGeneration(noeud);
				arbre.ajouterSousArbre(nouvelArbre);
			}
				
			for (i = 0; i < liste.size() ; i++)
			{
				ElementPresentation elem = (ElementPresentation)liste.elementAt(i);
				GElement noeud = this.getGenerateurCorrepondant(elem);
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
		 * Construit les gestionnaires de publication associés au type des éléments à traiter
		 * @param elem element de présentation qu'il faut traiter
		 */
		public GElement getGenerateurCorrepondant(ElementPresentation elem) 
		{
			// selon le type de l'élément de présentation
			if (elem instanceof Guide)
			{
				GGuide guide = new GGuide(elem,this.pwFicTree);
				return guide;		
			}
			
			// on vérifie que l'on ait bien un modèle associé
			if(elem.getElementModele() != null)
			{
				// c'est un élément normal il faut récupérer le type du modèle associé
				IdObjetModele id = elem.getElementModele();
				
				if (id.estComposant())
				{
					// composant
					GComposantPubliable compo = new GComposantPubliable(id, elem, this.pwFicTree ); 
					return compo;
				}
				
				if (id.estActivite())
				{
					// activite 
					GActivite activite = new GActivite(elem, this.pwFicTree ); 
					return activite;
				}
				
				if (id.estProduit())
				{
					// produit
					GProduit produit = new GProduit(elem, this.pwFicTree ); 
					return produit;
				}
				
				if (id.estDefinitionTravail())
				{
					// deftravail
					GDefinitionTravail defTrav = new GDefinitionTravail(elem, this.pwFicTree ); 
					return defTrav;
				}
				
				if (id.estDiagramme())
				{
					// diagramme
					GDiagramme diag = new GDiagramme((ComposantProcessus)id.getRef(), elem, this.pwFicTree ); 
					return diag;	
				}
				
				if (id.estRole())
				{
					// role
					GRole role = new GRole(elem, this.pwFicTree ); 
					return role;
				}
				
				if (id.estPaquetage())
				{
					GPaquetage gelem = new GPaquetage(elem, this.pwFicTree ); 
					return gelem;
				}
			
				else
				{
					GElementModele gelem = new GElementModele(elem, this.pwFicTree ); 
					return gelem;
				}
			}
			return null;
		}
		
		//-------------------------------------------
		// Extends MonitoredTaskBase
		//-------------------------------------------
		public void setTask(TaskMonitorDialog task)
		{
			this.mTask = task;
		}
		
		private void print( String msg )
		{
			this.setMessage(msg);
			if(this.mTask != null )
			{
				this.mTask.forceRefresh();
			}
		}
}
