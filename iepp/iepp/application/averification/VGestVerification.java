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

package iepp.application.averification;

import java.util.Vector;

import iepp.Application;
import iepp.Projet;
import iepp.domaine.IdObjetModele;
import iepp.ui.iedition.dessin.rendu.FElement;
import iepp.ui.iedition.dessin.rendu.FProduitFusion;

/**
 * Classe permettant d'effectuer les diff�rentes v�rifications � appliquer sur le
 * processus.
 */
public class VGestVerification
{
	
	/**
	 * V�rifie que le composant n'a pas d�j� �t� charg� depuis le r�f�rentiel
	 * dans la d�finition de processus.
	 * @param idComposant identifiant du composant dans le r�f�rentiel
	 * @return le r�sultat de la v�rification
	 */
	// Utiliser le r�f�rentiel pour savoir si un �l�ment est charge
	//(chercherReference(id) renvoie null si le composant n'est pas charg�)
/*
	public VResultatVerif composantNonCharge (int idComposant)
	{
		// Les composants auront des identifiants dans le r�f�rentiel, ce qui permettra
		// de ne pas les charger plusieurs fois
		return null ;
	}
*/


	/**
	 * Renvoie le nombre de composants portant un nom donn�
	 * @param nom nom du composant � chercher dans la d�finition de processus
	 * @return le nombre de composants portant ce nom
	 */
	protected int nbComposants (String nom)
	{
		// R�cup�rer les composants et initialiser le r�sultat
		Projet projet = Application.getApplication().getProjet() ;
		int nbComposants = 0 ;
		if (projet != null)
		{
			Vector listeComp = projet.getDefProc().getListeComp() ;
			// Regarder si le nom est d�j� pris
			for (int i = 0 ; i < listeComp.size() ; i++)
			{
				// Compter les composants du nom donn�
				IdObjetModele id = (IdObjetModele)listeComp.get(i);
				String nomComposant = id.toString();
				if (nomComposant.equals(nom))
					nbComposants++ ;
			}
		}
		return nbComposants ;
	}

	/**
	 * V�rifie que le nom indiqu� n'est pas d�j� celui d'un composant charg� dans la
	 * d�finition de processus.
	 * @param nom nom du composant
	 * @return le r�sultat de la v�rification
	 */
	public VResultatVerif nomDejaPris (String nom)
	{
		// R�cup�rer les composants et initialiser le r�sultat
		VResultatVerif res = new VResultatVerif ("TITREVERIF_NOM_LIBRE") ;
		if (this.nbComposants (nom) > 0)
		{
			res.ajouterErr (nom+" : "+
				Application.getApplication().getTraduction ("MSGERR_NOM_EXISTANT")) ;
		}
		// Renvoy� le r�sultat cr��
		return res ;
	}
	
	
	/**
	 * V�rifie que chaque composant a un nom unique.
	 * @return le r�sultat de la v�rification
	 */
	public VResultatVerif nomsComposantsUniques ()
	{
		VResultatVerif resVerif ;	// R�sultat � renvoyer
		VResultatVerif res ;		// R�sultat de la v�rification pour 1 composant
		Vector listeComp ;	// Liste des composants de la d�finition de processus
		
		// Parcourir les composants
		resVerif = new VResultatVerif (
			Application.getApplication().getTraduction ("TITREVERIF_NOMS_UNIQUES")) ;
		Projet projet = Application.getApplication().getProjet() ;
		if (projet != null)
		{
			listeComp = projet.getDefProc().getListeComp() ;
			for (int i = 0 ; i < listeComp.size() ; i++)
			{
				// V�rifier que son nom est unique
				IdObjetModele id = (IdObjetModele)listeComp.get(i);
				String nom = id.toString();
				if (this.nbComposants (nom) > 1)
				{
					resVerif.ajouterErr (
						Application.getApplication().getTraduction
							("MSGERR_NOM_EXISTANT") + " : "+nom) ;
				}
			}
		}
		// Renvoyer le r�sultat global
		return resVerif ;
	}

	public VResultatVerif connexionPossible (IdObjetModele source, IdObjetModele destination)
	{
		// Cette m�thode  sera impl�ment�e si la solution de tout v�riifer dans cette
		// classe est retenue
		VResultatVerif resVerif ;	// R�sultat � renvoyer
		
		// v�rifier que l'objet source et l'objet destination sont des produits
		resVerif = new VResultatVerif("Verification connexion");
		if ( !source.estProduit() || !destination.estProduit() )
		{
			resVerif.ajouterErr ("Connexion avec un composant impossible");
		}
		// on a deux produits il ne faut pas que ce soit le m�me produit
		else
		{
			if ( source == destination )
			{
				resVerif.ajouterErr ("Connexion avec un m�me produit impossible");
			}
			// deux produits diff�rents, il faut que leurs composants d'origine ne soient pas les m�mes
			else
			{
				if (source.getRef() == destination.getRef())
				{
					resVerif.ajouterErr ("Connexion entre produits d'un m�me composant");
				}
				// il faut que les produits soient de type diff�rents
				else
				{
					if ((source.estProduitEntree() && destination.estProduitEntree())
						|| (source.estProduitSortie() && destination.estProduitSortie()))
					{
						resVerif.ajouterErr ("Connexion entre produits de m�me type");
					}
					else
					{
						//TODO continuer les v�rifs
						// v�rifier que le lien n'existe pas d�j�
						// ...
					}
				}
			}
		}
		return resVerif ;
	}
	
	public VResultatVerif connexionPossible(FProduitFusion fusion, FElement produit)
	{
		VResultatVerif resVerif ;	// R�sultat � renvoyer
	
		// v�rifier que l'objet source et l'objet destination sont des produits
		resVerif = new VResultatVerif("Verification connexion");
		if (!produit.getModele().getId().estProduit() )
		{
			resVerif.ajouterErr ("Connexion avec un composant impossible");
		}
		// on a deux produits il ne faut pas que ce soit le m�me produit
		else
		{
			if (fusion.isLinkedComponent(produit.getModele().getId().getRef()))
			{
				resVerif.ajouterErr ("Connexion entre produits d'un m�me composant");
			}
			else
			{
				if (produit.getModele().getId().estProduitSortie())
				{
					resVerif.ajouterErr ("Connexion avec un produit en sortie ");
				}
			}
						
		}
		return resVerif ;
	}

	
}
