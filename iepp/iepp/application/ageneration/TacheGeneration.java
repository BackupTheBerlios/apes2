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
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;
import iepp.domaine.*;
import iepp.ui.iedition.VueDPGraphe;

import iepp.Application;
import util.*;
import util.ImageUtil;
import util.MonitoredTaskBase;
import util.TaskMonitorDialog;
import util.ErrorManager;


public class TacheGeneration extends MonitoredTaskBase {

	private PrintWriter pwFicTree ;
	private TaskMonitorDialog mTask = null;
	private boolean generationReussie = false;
	
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
			
			this.recupererProduitsExterieurs();
			this.preparerGeneration();
			this.creerArbreApes();

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
	private void preparerGeneration() throws Exception
		{
			this.print(Application.getApplication().getTraduction("creation_rep"));
			// Creation du dossier du site
			File rep = new File(GenerationManager.getInstance().getCheminGeneration());
			rep.mkdirs();
				
			// Création du dossier contenu
			rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.CONTENU_PATH );
			rep.mkdirs();
		
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
		
			//Création des pages contenues dans la page d'accueil
			this.creerPageAccueil();
			
			//Création du fichier tree.dat
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
					extraireIconeContenu(paquet);
					// on traite la génération de ce paquetage
					GPaquetagePresentation paquetCourant = new GPaquetagePresentation (paquet, pwFicTree);
					paquetCourant.traiterGeneration();
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
						 extraireIconeContenu(paquet);
						 // on traite la génération de ce composant
						 GComposantPubliable compCourant = new GComposantPubliable (idComposant, pwFicTree);
						 compCourant.traiterGeneration();
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
					// Verifier s'il s'agit d'un composant vide, auquel cas il faut verifier les produits en sortie
					if (idComposant.estComposantVide())
					{
					    listeProduits.addAll(idComposant.getProduitSortie());
					}
					for(int j = 0; j < listeProduits.size(); j++)
					{
					 	IdObjetModele idProduit = (IdObjetModele)listeProduits.elementAt(j);
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
						 	}
					 	}
					}
				}
			}
			GenerationManager.setListeProduitsExterieurs(listeProduitsExterieurs);
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
		 * @param paquet
		 */
		private void extraireIconeContenu(PaquetagePresentation paquet) throws FileNotFoundException, IOException
		{
			this.print(Application.getApplication().getTraduction("extraction_icone"));
			// Créer un flux d'entrée contenant l'archive ZIP à décompresser
			FileInputStream fin = new FileInputStream(paquet.getNomFichier());

			// Mettre ce flux en mémoire tampon
			BufferedInputStream bis = new BufferedInputStream(fin);

			// Identifier le flux tampon comme flux de compression ZIP
			ZipInputStream zin = new ZipInputStream(bis);

			// Définir un objet ZipEntry
			ZipEntry ze = null;

			// Tant que cet objet est différent de nul (tant qu'il y a des fichiers dans l'archive)...
			while ((ze = zin.getNextEntry()) != null)
			{
				 if (ze.toString().startsWith(paquet.getChemin_icone()) || ze.toString().startsWith(paquet.getChemin_contenu()))
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
					 else
					 {
						/*
					 	System.out.println(Application.getApplication().getProjet().getDefProc().getRepertoireGeneration() 
						+ "/" + CGenererSite.CONTENU_PATH + "/" + fichier);
						*/
						fout = new FileOutputStream(GenerationManager.getInstance().getCheminGeneration() 
												+ File.separator + GenerationManager.CONTENU_PATH + File.separator + fichier);
					 }
					 dezipper(zin, fout);
				 }
			 }
			 // Fermer le flux d'entrée
			 zin.close(); 
		}
	
		private void dezipper( ZipInputStream zin, FileOutputStream fout) throws IOException
		{
			//Copier les flux:
			synchronized (zin)
			{
				synchronized (fout)
				{
					  byte[] buffer = new byte[256];
					  while (true)
					  {
						  int bytesRead = zin.read(buffer);
						  if (bytesRead == -1) break;
						  fout.write(buffer, 0, bytesRead);
					  }
				 }
			 } 
			 // Fermer l'entrée et le flux de sortie
			zin.closeEntry();
			fout.close();
		}
}
