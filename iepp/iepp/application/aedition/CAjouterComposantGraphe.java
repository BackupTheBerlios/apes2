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
 
package iepp.application.aedition;

import java.awt.Point;
import java.util.Vector;

import util.ErrorManager;

import iepp.* ;
import iepp.ui.iedition.*;
import iepp.ui.iedition.dessin.rendu.FComposantProcessus;
import iepp.ui.iedition.dessin.rendu.FProduit;
import iepp.ui.iedition.dessin.rendu.liens.FLienInterface;
import iepp.ui.iedition.dessin.vues.MDComposantProcessus;
import iepp.ui.iedition.dessin.vues.MDLienDotted;
import iepp.ui.iedition.dessin.vues.MDProduit;
import iepp.application.CommandeAnnulable;
import iepp.domaine.* ;

/**
 * Commande annulable permettant d'ajouter un composant avec son interface
 * au diagramme. V�rifie si le composant n'est pas d�j� dans le diagramme
 */
public class CAjouterComposantGraphe extends CommandeAnnulable
{
	/**
	 * Id du Composant � rajouter dans le graphe
	 */
	private IdObjetModele composant ;
	
	/**
	 * Endroit o� doit �tre ajout� le composant
	 */
	private Point point = new Point(200, 0);

	/**
	 * Cr�ation de la commande � partir du composant � ajouter
	 * @param comp
	 */
	public CAjouterComposantGraphe (IdObjetModele comp)
	{
		// sauvegarder le composant
		this.composant = comp ;		
	}
	
	/**
	 * Cr�ation de la commande � partir du composant � ajouter
	 * @param comp
	 */
	public CAjouterComposantGraphe (IdObjetModele comp, Point endroitClick)
	{
		// sauvegarder le composant
		this.composant = comp ;	
		this.point = endroitClick ;
	}
	
	/**
	 * Ex�cuter la commande, ajouter au diagramme le composant
	 * et les interfaces en entr�e et sortie
	 * @return true si la commande s'est bien ex�cut�e
	 */
	public boolean executer()
	{
		
		FenetreEdition fenetre = Application.getApplication().getProjet().getFenetreEdition() ;
		// d�selectionner tous les �l�ments
		fenetre.getVueDPGraphe().clearSelection();
		
		// v�rifier que le composant n'est pas d�j� pr�sent dans le diagramme
		if (fenetre.getVueDPGraphe().contient(this.composant) != null)
		{
			ErrorManager.getInstance().display("ERR","ERR_Composant_Present");
			return false;
		}
		
		// Construire la vue associ�� au composant
		MDComposantProcessus mdcomp = new MDComposantProcessus(((IdObjetModele)this.composant));
		mdcomp.setY((int)this.point.getY());
		mdcomp.setX((int)this.point.getX());
		
		FComposantProcessus fcomp = new FComposantProcessus(mdcomp);
		fenetre.getVueDPGraphe().ajouterFigure(fcomp);
		fenetre.getVueDPGraphe().selectionneFigure(fcomp);
		
		// R�cup�ration des produits en entr�e du composant
		Vector prod_entree = ((ComposantProcessus)this.composant.getRef()).getProduitEntree();
		
		for (int i=0; i < prod_entree.size(); i++)
		{
			// Cr�ation de la vue associ�e au produit en entr�e
			MDProduit mprod = new MDProduit((IdObjetModele)prod_entree.elementAt(i));
			mprod.setX((int)this.point.getX() - 100 - mprod.getLargeur());
		    mprod.setY((int)this.point.getY() + (i * (mprod.getHauteur() + 30)) );
			FProduit fprod = new FProduit(mprod);
			fenetre.getVueDPGraphe().ajouterFigure(fprod);
			fenetre.getVueDPGraphe().selectionneFigure(fprod);
			
			// Liaison du produit avec le composant
			CLierInterface c = new CLierInterface(fenetre.getVueDPGraphe(),
												  new FLienInterface(new MDLienDotted()),
												  fprod,
												  fcomp,
												  new Vector());
			c.executer();
		}
		
		
		 //	R�cup�ration des produits en sortie du composant
		 Vector prod_sortie = ((ComposantProcessus)this.composant.getRef()).getProduitSortie();
		
		 for (int i=0; i < prod_sortie.size(); i++)
		 {
			 // Cr�ation de la vue associ�e au produit en sortie
			 MDProduit mprod = new MDProduit((IdObjetModele)prod_sortie.elementAt(i));
			 mprod.setX((int)this.point.getX() + mdcomp.getLargeur() + 100);
		     mprod.setY((int)this.point.getY() + (i * (mprod.getHauteur() + 30)) );
			 FProduit fprod = new FProduit(mprod);
			 fenetre.getVueDPGraphe().ajouterFigure(fprod);
			fenetre.getVueDPGraphe().selectionneFigure(fprod);
			
			 // Liaison du produit avec le composant
			 CLierInterface c = new CLierInterface(fenetre.getVueDPGraphe(),
												   new FLienInterface(new MDLienDotted()),
												   fcomp,
												   fprod,
												   new Vector());
			 c.executer();
		 }
		return (true);
	}
}
