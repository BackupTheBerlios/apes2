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
 * Classe permettant d'effectuer les différentes vérifications à appliquer sur le
 * processus.
 */
public class VGestVerification
{
	
	/**
	 * Vérifie que le composant n'a pas déjà été chargé depuis le référentiel
	 * dans la définition de processus.
	 * @param idComposant identifiant du composant dans le référentiel
	 * @return le résultat de la vérification
	 */
	// Utiliser le référentiel pour savoir si un élément est charge
	//(chercherReference(id) renvoie null si le composant n'est pas chargé)
/*
	public VResultatVerif composantNonCharge (int idComposant)
	{
		// Les composants auront des identifiants dans le référentiel, ce qui permettra
		// de ne pas les charger plusieurs fois
		return null ;
	}
*/


	/**
	 * Renvoie le nombre de composants portant un nom donné
	 * @param nom nom du composant à chercher dans la définition de processus
	 * @return le nombre de composants portant ce nom
	 */
	protected int nbComposants (String nom)
	{
		// Récupérer les composants et initialiser le résultat
		Projet projet = Application.getApplication().getProjet() ;
		int nbComposants = 0 ;
		if (projet != null)
		{
			Vector listeComp = projet.getDefProc().getListeComp() ;
			// Regarder si le nom est déjà pris
			for (int i = 0 ; i < listeComp.size() ; i++)
			{
				// Compter les composants du nom donné
				IdObjetModele id = (IdObjetModele)listeComp.get(i);
				String nomComposant = id.toString();
				if (nomComposant.equals(nom))
					nbComposants++ ;
			}
		}
		return nbComposants ;
	}

	/**
	 * Vérifie que le nom indiqué n'est pas déjà celui d'un composant chargé dans la
	 * définition de processus.
	 * @param nom nom du composant
	 * @return le résultat de la vérification
	 */
	public VResultatVerif nomDejaPris (String nom)
	{
		// Récupérer les composants et initialiser le résultat
		VResultatVerif res = new VResultatVerif ("TITREVERIF_NOM_LIBRE") ;
		if (this.nbComposants (nom) > 0)
		{
			res.ajouterErr (nom+" : "+
				Application.getApplication().getTraduction ("MSGERR_NOM_EXISTANT")) ;
		}
		// Renvoyé le résultat créé
		return res ;
	}
	
	
	/**
	 * Vérifie que chaque composant a un nom unique.
	 * @return le résultat de la vérification
	 */
	public VResultatVerif nomsComposantsUniques ()
	{
		VResultatVerif resVerif ;	// Résultat à renvoyer
		VResultatVerif res ;		// Résultat de la vérification pour 1 composant
		Vector listeComp ;	// Liste des composants de la définition de processus
		
		// Parcourir les composants
		resVerif = new VResultatVerif (
			Application.getApplication().getTraduction ("TITREVERIF_NOMS_UNIQUES")) ;
		Projet projet = Application.getApplication().getProjet() ;
		if (projet != null)
		{
			listeComp = projet.getDefProc().getListeComp() ;
			for (int i = 0 ; i < listeComp.size() ; i++)
			{
				// Vérifier que son nom est unique
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
		// Renvoyer le résultat global
		return resVerif ;
	}

	public VResultatVerif connexionPossible (IdObjetModele source, IdObjetModele destination)
	{
		// Cette méthode  sera implémentée si la solution de tout vériifer dans cette
		// classe est retenue
		VResultatVerif resVerif ;	// Résultat à renvoyer
		
		// vérifier que l'objet source et l'objet destination sont des produits
		resVerif = new VResultatVerif("Verification connexion");
		if ( !source.estProduit() || !destination.estProduit() )
		{
			resVerif.ajouterErr ("Connexion avec un composant impossible");
		}
		// on a deux produits il ne faut pas que ce soit le même produit
		else
		{
			if ( source == destination )
			{
				resVerif.ajouterErr ("Connexion avec un même produit impossible");
			}
			// deux produits différents, il faut que leurs composants d'origine ne soient pas les mêmes
			else
			{
				if (source.getRef() == destination.getRef())
				{
					resVerif.ajouterErr ("Connexion entre produits d'un même composant");
				}
				// il faut que les produits soient de type différents
				else
				{
					if ((source.estProduitEntree() && destination.estProduitEntree())
						|| (source.estProduitSortie() && destination.estProduitSortie()))
					{
						resVerif.ajouterErr ("Connexion entre produits de même type");
					}
					else
					{
						//TODO continuer les vérifs
						// vérifier que le lien n'existe pas déjà
						// ...
					}
				}
			}
		}
		return resVerif ;
	}
	
	public VResultatVerif connexionPossible(FProduitFusion fusion, FElement produit)
	{
		VResultatVerif resVerif ;	// Résultat à renvoyer
	
		// vérifier que l'objet source et l'objet destination sont des produits
		resVerif = new VResultatVerif("Verification connexion");
		if (!produit.getModele().getId().estProduit() )
		{
			resVerif.ajouterErr ("Connexion avec un composant impossible");
		}
		// on a deux produits il ne faut pas que ce soit le même produit
		else
		{
			if (fusion.isLinkedComponent(produit.getModele().getId().getRef()))
			{
				resVerif.ajouterErr ("Connexion entre produits d'un même composant");
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
