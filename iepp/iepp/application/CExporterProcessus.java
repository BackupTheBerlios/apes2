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
package iepp.application;


import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import iepp.application.ageneration.GenerationManager;
import util.ErrorManager;
import util.SimpleFileFilter;
import util.SmartChooser;
import iepp.Application;
import iepp.domaine.IdObjetModele;

/**
 * 
 */
public class CExporterProcessus extends CommandeNonAnnulable 
{

	protected SimpleFileFilter filter = new SimpleFileFilter("dpe", "Exported Process");
																 
	
	public boolean executer()
	{
		SmartChooser chooser = SmartChooser.getChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(filter);
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		//Ouverture de la boite de dialogue
		 if (chooser.showSaveDialog(Application.getApplication().getFenetrePrincipale()) !=  JFileChooser.APPROVE_OPTION)
		 {
			 return false;
		 }
		 String selected_file=chooser.getSelectedFile().getAbsolutePath();
		 // vérifier qu'il y a bien l'extension
		  if (! chooser.getSelectedFile().getAbsolutePath().endsWith(".DPE")
				 && ! chooser.getSelectedFile().getAbsolutePath().endsWith(".dpe"))
		  {
			 selected_file += ".dpe";
		  }
		 File fic = new File(selected_file);
		 // vérifier si le fichier existe déjà, auquel cas on demande confirmation pour l'écrasement
		 if(fic.exists())
		 {
			 int choice = JOptionPane.showConfirmDialog( Application.getApplication().getFenetrePrincipale(),
														 Application.getApplication().getTraduction("msgConfirmEcrasement") ,
														 Application.getApplication().getTraduction("msgSauvegarde"),
														 JOptionPane.YES_NO_OPTION,
														 JOptionPane.QUESTION_MESSAGE);

			 if(choice!=JOptionPane.YES_OPTION)
			 {
				 return false;
			 }
		 }
		 
		 return ( this.exporter(selected_file));
	}
	
	public boolean exporter(String filePath)
	{
		// récupérer la liste des composants du processus
		Vector listeComposant = Application.getApplication().getProjet().getDefProc().getListeComp();
		
		try
		{
			// créer le fichier d'exportation
			
			// crée un fichier binaire...
			 FileOutputStream outstream = new FileOutputStream( new File( filePath ) );
			OutputStreamWriter data = new OutputStreamWriter(outstream, "UTF-16");	

			data.write("<?iepp version=\""+Application.NUMVERSION+"\"?>\n");
			data.write("<exportExecution>\n");
			data.write("\t<cheminGeneration>");
			data.write(GenerationManager.getInstance().getCheminGeneration());
			data.write("</cheminGeneration>\n");
			
			
			// liste des roles de la définition	
			data.write("\t\t<listeRoles>\n");
			for (int i = 0; i < listeComposant.size(); i++)
			{
				Vector roles = ((IdObjetModele)listeComposant.elementAt(i)).getRole();
				for (int j = 0; j < roles.size(); j++)
				{
					IdObjetModele io = (IdObjetModele)roles.elementAt(j);
					data.write("\t\t\t<role>\n");
					
					// id et le nom
					data.write("\t\t\t\t<id>");
					data.write((new Integer((i * 10000) + io.getID())).toString());
					data.write("</id>\n");
					data.write("\t\t\t\t<nom>");
					data.write(io.toString());
					data.write("</nom>\n");
					
					// liste des participations
					data.write("\t\t\t\t<listeParticipations>\n");
					for (int k = 0; k < io.getIDActivite().size(); k++)
					{
						Integer activite = new Integer(((Integer) io.getIDActivite().elementAt(k)).intValue() +  (i * 10000));
						data.write("\t\t\t\t\t<participation>");
						data.write(activite.toString());
						data.write("</participation>\n");
					}
					data.write("\t\t\t\t</listeParticipations>\n");
					
					// liste des responsabilités
					data.write("\t\t\t\t<listeResponsabilites>\n");
					for (int k = 0; k < io.getIDProduit().size(); k++)
					{
						Integer produit = new Integer(((Integer) io.getIDProduit().elementAt(k)).intValue() +  (i * 10000));
						data.write("\t\t\t\t\t<responsabilite>");
						data.write(produit.toString());
						data.write("</responsabilite>\n");
					}
					data.write("\t\t\t\t</listeResponsabilites>\n");
					data.write("\t\t\t\t<cheminPage>");
					data.write(io.getChemin());
					data.write("</cheminPage>\n");
					data.write("\t\t\t</role>\n");
				}
			}
			data.write("\t\t</listeRoles>\n");
			
			
			// liste des activités de la définition	
			 data.write("\t\t<listeActivites>\n");
			 for (int i = 0; i < listeComposant.size(); i++)
			 {
				 Vector activites = ((IdObjetModele)listeComposant.elementAt(i)).getActivite();
				 for (int j = 0; j < activites.size(); j++)
				 {
					IdObjetModele io = (IdObjetModele)activites.elementAt(j);
					data.write("\t\t\t<activite>\n");
					 
					// id et le nom
					data.write("\t\t\t\t<id>");
					data.write((new Integer(io.getID()+ ( i * 10000))).toString() );
					data.write("</id>\n");
					data.write("\t\t\t\t<nom>");
					data.write(io.toString());
					data.write("</nom>\n");
					data.write("\t\t\t\t<participation>"); 
					data.write(new Integer(io.getIDRole() + ( i * 10000)).toString());
					data.write("</participation>\n");

					// produits en entrée
					data.write("\t\t\t\t<listeProdEntree>\n");
					Vector listeId = io.getIDProduitEntree();
					for (int k = 0; k < listeId.size(); k++)
					{
						data.write("\t\t\t\t\t<prodEntree>");
						data.write(new Integer( ((Integer)listeId.elementAt(k)).intValue() + ( i * 10000)).toString());
						data.write("</prodEntree>\n");
					}
					data.write("\t\t\t\t</listeProdEntree>\n");
					
					//produits en sortie
					data.write("\t\t\t\t<listeProdSortie>\n");
					listeId = io.getIDProduitSortie();
					for (int k = 0; k < listeId.size(); k++)
					{
						data.write("\t\t\t\t\t<prodSortie>");
						data.write(new Integer( ((Integer)listeId.elementAt(k)).intValue() + ( i * 10000)).toString());
						data.write("</prodSortie>\n");
					}
					data.write("\t\t\t\t</listeProdSortie>\n");
					data.write("\t\t\t\t<cheminPage>");
					data.write(io.getChemin());
					data.write("</cheminPage>\n");
					data.write("\t\t\t</activite>\n");
				 }
			 }
			data.write("\t\t</listeActivites>\n");		
			
			
			// liste des produits de la définition
			 data.write("\t\t<listeProduits>\n");
			 for (int i = 0; i < listeComposant.size(); i++)
			 {
				 Vector produits = ((IdObjetModele)listeComposant.elementAt(i)).getProduit();
				 for (int j = 0; j < produits.size(); j++)
				 {
					IdObjetModele io = (IdObjetModele)produits.elementAt(j);
					data.write("\t\t\t<produit>\n");
					 
					// id et le nom
					data.write("\t\t\t\t<id>");
					data.write((new Integer(io.getID() + ( i * 10000))).toString());
					data.write("</id>\n");
					data.write("\t\t\t\t<nom>");
					data.write(io.toString());
					data.write("</nom>\n");
					data.write("\t\t\t\t<responsabilite>"); 
					data.write(new Integer(io.getIDRole() + ( i * 10000)).toString());
					data.write("</responsabilite>\n");

					//activités dont le produit est en entrée
					data.write("\t\t\t\t<listeProdEntree>\n");
					Vector listeId = io.getIDActiviteEntree();
					for (int k = 0; k < listeId.size(); k++)
					{
						data.write("\t\t\t\t\t<prodEntree>");
						data.write(new Integer( ((Integer)listeId.elementAt(k)).intValue() + ( i * 10000)).toString());
						data.write("</prodEntree>\n");
					}
					data.write("\t\t\t\t</listeProdEntree>\n");
					
					//activités dont le produit est en sortie
					data.write("\t\t\t\t<listeProdSortie>\n");
					listeId = io.getIDActiviteSortie();
					for (int k = 0; k < listeId.size(); k++)
					{
						data.write("\t\t\t\t\t<prodSortie>");
						data.write(new Integer( ((Integer)listeId.elementAt(k)).intValue() + ( i * 10000)).toString());
						data.write("</prodSortie>\n");
					}
					data.write("\t\t\t\t</listeProdSortie>\n");
					data.write("\t\t\t\t<listeEtats>\n");
					Vector listeEtat = io.getEtats();
					for (int k = 0; k < listeEtat.size(); k++)
					{
						data.write("\t\t\t\t\t<etat>");
						data.write(listeEtat.elementAt(k).toString());
						data.write("</etat>\n");
					}
					data.write("\t\t\t\t</listeEtats>\n");
					data.write("\t\t\t\t<cheminPage>");
					data.write(io.getChemin());
					data.write("</cheminPage>\n");
					data.write("\t\t\t</produit>\n");
				 }
			 }
			data.write("\t\t</listeProduits>\n");
			
			// liste des définition de travail
			data.write("\t\t<listeDefTravail>\n");
			for (int i = 0; i < listeComposant.size(); i++)
			{
				Vector definition = ((IdObjetModele)listeComposant.elementAt(i)).getDefinitionTravail();
				for (int j = 0; j < definition.size(); j++)
				{
					IdObjetModele io = (IdObjetModele)definition.elementAt(j);
					data.write("\t\t\t<definitionTravail>\n");
					data.write("\t\t\t\t<nom>");
					data.write(io.toString());
					data.write("</nom>\n");
					data.write("\t\t\t\t<listeActivites>\n");
					for (int k = 0; k < io.getIDActivite().size(); k++)
					{
						Integer activite = (new Integer( ((Integer)io.getIDActivite().elementAt(k)).intValue() + ( i * 10000)));
						data.write("\t\t\t\t\t<idAct>");
						data.write(activite.toString());
						data.write("</idAct>\n");
					}
					data.write("\t\t\t\t</listeActivites>\n");
					data.write("\t\t\t</definitionTravail>\n");
				}
			}
			data.write("\t\t</listeDefTravail>\n");
			
			
			data.write("</exportExecution>\n");
			data.close();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			ErrorManager.getInstance().display(t);
			return false;
		}

		return true;
	}
	
}
