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
import java.util.HashMap;
import java.util.Vector;

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;


import iepp.application.ageneration.ArbreGeneration;
import iepp.application.ageneration.GenerationManager;
import iepp.application.ageneration.TacheGeneration;
import util.ErrorManager;
import util.SimpleFileFilter;
import util.SmartChooser;
import util.ToolKit;
import iepp.Application;
import iepp.domaine.ComposantProcessus;
import iepp.domaine.DefinitionProcessus;
import iepp.domaine.ElementPresentation;
import iepp.domaine.IdObjetModele;
import iepp.domaine.LienProduits;
import iepp.domaine.PaquetagePresentation;

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
		GenerationManager.getInstance().setListeAGenerer(Application.getApplication().getProjet().getDefProc().getListeAGenerer());
		ArbreGeneration arbre = new ArbreGeneration();
		ArbreGeneration.initialiserMap();
		GenerationManager.recupererProduitsExterieurs();
		GenerationManager.construireArbre(arbre, null);
		File f = new File(GenerationManager.getInstance().getCheminGeneration());
		// permet d'iniatialiser les chemins des élements à exporter
		arbre.initialiserArbre(ToolKit.removeSlashTerminatedPath(f.getAbsolutePath()));
		
		// récupérer la liste des composants du processus
		Vector listeComposant = Application.getApplication().getProjet().getDefProc().getListeComp();
		
		try
		{
			// récupérer la definition de processus
			DefinitionProcessus defProc = Application.getApplication().getProjet().getDefProc();
			
			// créer le fichier d'exportation
			
			// crée un fichier binaire...
			 FileOutputStream outstream = new FileOutputStream( new File( filePath ) );
			OutputStreamWriter data = new OutputStreamWriter(outstream, "UTF-16");	

			data.write("<?iepp version=\""+Application.NUMVERSION+"\"?>\n");
			data.write("<exportExecution>\n");
			
			data.write("\t<proprietes>");
			data.write("\t\t<nomProcessus>");
			data.write(defProc.getNomDefProc());
			data.write("</nomProcessus>\n");
			data.write("\t\t<nomAuteur>");
			data.write(defProc.getAuteur());
			data.write("</nomAuteur>\n");
			data.write("\t\t<emailAuteur>");
			data.write(defProc.getEmailAuteur());
			data.write("</emailAuteur>\n");
			data.write("\t\t<commentaires>");
			data.write(defProc.getCommentaires());
			data.write("</commentaires>\n");
			data.write("\t\t<cheminGeneration>");
			data.write(GenerationManager.getInstance().getCheminGeneration());
			data.write("</cheminGeneration>\n");
			data.write("\t</proprietes>\n");
			
			
			// liste des roles de la définition	
			data.write("\t\t<listeRoles>\n");
			ComposantProcessus courant;
			for (int i = 0; i < listeComposant.size(); i++)
			{
			    courant = (ComposantProcessus)((IdObjetModele)listeComposant.elementAt(i)).getRef();
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
					Integer produit;
					for (int k = 0; k < io.getIDProduit().size(); k++)
					{
					    int prodID = ((Integer)io.getIDProduit().elementAt(k)).intValue();
					    
					    // Recherche de l'ID objet modele du produit courant
					    IdObjetModele prod = null;
					    Vector listeProd = courant.getProduits();
					    for (int l = 0; l < listeProd.size(); l++)
					    {
					        if (((IdObjetModele)listeProd.elementAt(l)).getID() == prodID)
					        {
					            prod = (IdObjetModele)listeProd.elementAt(l);
					        }
					    }
					    
					    // Si le produit est change, on remplace par l'id du produit en sortie
					    if (GenerationManager.getProduitChange(prod) == null)
					    {
					        produit = new Integer(prodID +  (i * 10000));
					    }
					    else
					    {
					        produit = new Integer(GenerationManager.getProduitChange(prod).getID() +  (listeComposant.indexOf(((ComposantProcessus)GenerationManager.getProduitChange(prod).getRef()).getIdComposant()) * 10000));
					    }
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
			     courant = (ComposantProcessus)((IdObjetModele)listeComposant.elementAt(i)).getRef();
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
					Integer produit;
					for (int k = 0; k < listeId.size(); k++)
					{
					    int prodID = ((Integer)listeId.elementAt(k)).intValue();
					    
					    // Recherche de l'ID objet modele du produit courant
					    IdObjetModele prod = null;
					    Vector listeProd = courant.getProduits();
					    for (int l = 0; l < listeProd.size(); l++)
					    {
					        if (((IdObjetModele)listeProd.elementAt(l)).getID() == prodID)
					        {
					            prod = (IdObjetModele)listeProd.elementAt(l);
					        }
					    }
					    
					    // Si le produit est change, on remplace par l'id du produit en sortie
					    if (GenerationManager.getProduitChange(prod) == null)
					    {
					        produit = new Integer(prodID +  (i * 10000));
					    }
					    else
					    {
					        produit = new Integer(GenerationManager.getProduitChange(prod).getID() +  (listeComposant.indexOf(((ComposantProcessus)GenerationManager.getProduitChange(prod).getRef()).getIdComposant()) * 10000));
					    }					    					    
					    data.write("\t\t\t\t\t<prodEntree>");
						data.write(produit.toString());
						data.write("</prodEntree>\n");
					}
					data.write("\t\t\t\t</listeProdEntree>\n");
					
					//produits en sortie
					data.write("\t\t\t\t<listeProdSortie>\n");
					listeId = io.getIDProduitSortie();
					for (int k = 0; k < listeId.size(); k++)
					{
					    int prodID = ((Integer)listeId.elementAt(k)).intValue();
					    
					    // Recherche de l'ID objet modele du produit courant
					    IdObjetModele prod = null;
					    Vector listeProd = courant.getProduits();
					    for (int l = 0; l < listeProd.size(); l++)
					    {
					        if (((IdObjetModele)listeProd.elementAt(l)).getID() == prodID)
					        {
					            prod = (IdObjetModele)listeProd.elementAt(l);
					        }
					    }
					    
					    // Si le produit est change, on remplace par l'id du produit en sortie
					    if (GenerationManager.getProduitChange(prod) == null)
					    {
					        produit = new Integer(prodID +  (i * 10000));
					    }
					    else
					    {
					        produit = new Integer(GenerationManager.getProduitChange(prod).getID() +  (listeComposant.indexOf(((ComposantProcessus)GenerationManager.getProduitChange(prod).getRef()).getIdComposant()) * 10000));
					    }
						data.write("\t\t\t\t\t<prodSortie>");
						data.write(produit.toString());
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

			// Ecriture des produits
			
			data.write("\t\t<listeProduits>\n");
            for (int i = 0; i < listeComposant.size(); i++)
            {
                courant = (ComposantProcessus)((IdObjetModele)listeComposant.elementAt(i)).getRef();
                Vector produits = ((IdObjetModele) listeComposant.elementAt(i)).getProduit();
                for (int j = 0; j < produits.size(); j++)
                {
                    IdObjetModele io = (IdObjetModele) produits.elementAt(j);
                    // On ecrit le produit que s'il ne s'agit pas d'un produit en entree lie vers un autre
                    if (GenerationManager.getProduitChange(io) == null)
                    {
                        data.write("\t\t\t<produit>\n");

                        // id et le nom
                        data.write("\t\t\t\t<id>");
                        data.write((new Integer(io.getID() + (i * 10000))).toString());
                        data.write("</id>\n");
                        data.write("\t\t\t\t<nom>");
                        data.write(io.toString());
                        data.write("</nom>\n");
                        data.write("\t\t\t\t<responsabilite>");
                        data.write(new Integer(io.getIDRole() + (i * 10000)).toString());
                        data.write("</responsabilite>\n");

                        //activités dont le produit est en entrée
                        data.write("\t\t\t\t<listeProdEntree>\n");
                        Vector listeId = io.getIDActiviteEntree();
                        for (int k = 0; k < listeId.size(); k++)
                        {
                            data.write("\t\t\t\t\t<prodEntree>");
                            data.write(new Integer(((Integer) listeId.elementAt(k)).intValue() + (i * 10000)).toString());
                            data.write("</prodEntree>\n");
                        }
                        data.write("\t\t\t\t</listeProdEntree>\n");

                        //activités dont le produit est en sortie
                        data.write("\t\t\t\t<listeProdSortie>\n");
                        listeId = io.getIDActiviteSortie();
                        for (int k = 0; k < listeId.size(); k++)
                        {
                            data.write("\t\t\t\t\t<prodSortie>");
                            data.write(new Integer(((Integer) listeId.elementAt(k)).intValue() + (i * 10000)).toString());
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
                        if (GenerationManager.estProduitExterieur(io) == 0)
                        {
	                        data.write("\t\t\t\t<cheminPage>");
	                        data.write(io.getChemin());
	                        data.write("</cheminPage>\n");
                        }
                        data.write("\t\t\t</produit>\n");
                    }
                }
            }
            data.write("\t\t</listeProduits>\n");

            // liste des définition de travail
            data.write("\t\t<listeDefTravail>\n");
            for (int i = 0; i < listeComposant.size(); i++)
            {
                courant = (ComposantProcessus)((IdObjetModele)listeComposant.elementAt(i)).getRef();
                Vector definition = ((IdObjetModele) listeComposant.elementAt(i)).getDefinitionTravail();
                for (int j = 0; j < definition.size(); j++)
                {
                    IdObjetModele io = (IdObjetModele) definition.elementAt(j);
                    data.write("\t\t\t<definitionTravail>\n");
                    data.write("\t\t\t\t<nom>");
                    data.write(io.toString());
                    data.write("</nom>\n");
                    data.write("\t\t\t\t<listeActivites>\n");
                    for (int k = 0; k < io.getIDActivite().size(); k++)
                    {
                        Integer activite = (new Integer(((Integer) io.getIDActivite().elementAt(k)).intValue() + (i * 10000)));
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
        catch (Throwable t)
        {
            t.printStackTrace();
            ErrorManager.getInstance().display(t);
            return false;
        }

        return true;
    }

}
