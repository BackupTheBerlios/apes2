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
			//r�cup�rer l'heure de d�but de la g�n�ration
			GenerationManager.getInstance().debuterGeneration();
			
			this.arbre = new ArbreGeneration();
			
			GenerationManager.recupererProduitsExterieurs();
			this.preparerGeneration();
			GenerationManager.construireArbre(this.arbre, pwFicTree);
			File f = new File(GenerationManager.getInstance().getCheminGeneration());
			this.arbre.initialiserArbre(ToolKit.removeSlashTerminatedPath(f.getAbsolutePath()));
			
			//System.out.println(arbre);
			// mettre ici les rajouts de page
			
			
			//Cr�ation des pages contenues dans la page d'accueil
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

	

	private void preparerGeneration() throws Exception
	{
		this.print(Application.getApplication().getTraduction("creation_rep"));
		// Creation du dossier du site
		File rep = new File(GenerationManager.getInstance().getCheminGeneration());
		rep.mkdirs();
			
		/*
		// Cr�ation du dossier contenu
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.CONTENU_PATH );
		rep.mkdirs();
	*/
		
		// Cr�ation du dossier description
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.DESCRIPTION_PATH );
		rep.mkdirs();
		
		// Cr�ation du dossier pour le contenu ext�rieur au processus
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.EXTERIEUR_PATH );
		rep.mkdirs();
			
		// copie des r�pertoires ressource (javascript ...)
		Copie.copieRep(Application.getApplication().getConfigPropriete("site"), GenerationManager.getInstance().getCheminGeneration()+ File.separator + GenerationManager.APPLET_PATH);
	
		//copie du r�pertoire des feuilles de styles
		Copie.copieRep(Application.getApplication().getConfigPropriete("styles"), GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.STYLES_PATH);
	
		//Cr�ation du fichier tree.dat
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
					// r�cup�rer les icones et les contenuts pour chaque paquetage
					//extraireIconeContenu(paquet);
					// on traite la g�n�ration de ce paquetage
					/*
					GPaquetagePresentation paquetCourant = new GPaquetagePresentation (paquet, pwFicTree);
					paquetCourant.traiterGeneration();
					*/
				}
				else
				{
					// composant publiable
					//on recupere l'ID du i�me composant de la definition de Processus
					idComposant = (IdObjetModele)liste.elementAt(i);
					paquet = idComposant.getPaquetagePresentation();
					if (paquet != null && !paquet.getNomFichier().equals(""))
					{
						 this.print(Application.getApplication().getTraduction("traitement_comp") + " " + paquet.getNomPresentation());
						 // r�cup�rer les icones et les contenus pour chaque paquetage
						 //extraireIconeContenu(paquet);
						 /*
						 // on traite la g�n�ration de ce composant
						 GComposantPubliable compCourant = new GComposantPubliable (idComposant, pwFicTree);
						 compCourant.traiterGeneration();
						 */
					}
				}
			}
		
			return true;
		}



		
		
		/**
		 * permet de creer les fichiers HTML corespondant � l'accueil du site
		 */
		public void creerPageAccueil() throws Exception
		{
			
			this.print(Application.getApplication().getTraduction("creation_page_acc"));
			// cr�ation du fichier index.html
			String nom = GenerationManager.getInstance().getCheminGeneration()+ File.separator + "index.html"  ;
			File ficHTML = new File (nom) ;
			FileWriter fd;
		
			fd = new FileWriter(nom);
			String fichierAccueil = CodeHTML.getPageAccueil(Application.getApplication().getProjet().getDefProc().getNomDefProc());
			fd.write(fichierAccueil) ;
			fd.close();
		
			// cr�ation de l'image du diagramme pour la page principale
			this.creerImagePng(Application.getApplication().getProjet().getFenetreEdition().getVueDPGraphe(), GenerationManager.getInstance().getCheminGeneration() + File.separator + "main") ;
			// cr�ation du fichier main.html
			nom =GenerationManager.getInstance().getCheminGeneration()+ File.separator + "main.html"  ;
			ficHTML = new File (nom) ;
		
			fd = new FileWriter(nom);
			String fichierMain = CodeHTML.getPagePrincipale(Application.getApplication().getProjet().getDefProc().getNomDefProc());
			fd.write(fichierMain) ;
			fd.close();
		
			// cr�ation du fichier arbre.html
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
			// cr�ation du fichier tree.dat
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
