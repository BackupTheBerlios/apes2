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
			GenerationManager.getInstance().setTache(this);
			this.arbre = new ArbreGeneration();
			
			GenerationManager.recupererProduitsExterieurs();
			this.preparerGeneration();
			GenerationManager.construireArbre(this.arbre, pwFicTree);
			File f = new File(GenerationManager.getInstance().getCheminGeneration());
			this.arbre.initialiserArbre(ToolKit.removeSlashTerminatedPath(f.getAbsolutePath()));
			
			//System.out.println(arbre);
			// mettre ici les rajouts de page
			
			
			//Création des pages contenues dans la page d'accueil
			this.creerPageAccueil();
			this.print(Application.getApplication().getTraduction("creation_pages"));
			this.arbre.genererSite();
			
			// fermeture du fichier tree.dat
			this.pwFicTree.close();
			
			this.generationReussie = true;
			GenerationManager.getInstance().setTache(null);
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

		// Création du dossier pour le contenu extérieur au processus
		rep = new File(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.EXTERIEUR_PATH );
		rep.mkdirs();
			
		// copie des répertoires ressource (javascript ...)
		Copie.copieRep(Application.getApplication().getConfigPropriete("site"), GenerationManager.getInstance().getCheminGeneration()+ File.separator + GenerationManager.APPLET_PATH);
	
		//copie du répertoire des feuilles de styles
		Copie.copieRep(Application.getApplication().getConfigPropriete("styles"), GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.STYLES_PATH);
	
		//Création du fichier tree.dat
		this.creerFicTree(GenerationManager.getInstance().getCheminGeneration() + File.separator + GenerationManager.APPLET_PATH);	
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

		
		//-------------------------------------------
		// Extends MonitoredTaskBase
		//-------------------------------------------
		public void setTask(TaskMonitorDialog task)
		{
			this.mTask = task;
		}
		
		public void print( String msg )
		{
			this.setMessage(msg);
			if(this.mTask != null )
			{
				this.mTask.forceRefresh();
			}
		}
}
