/*
 * <b>Date de cr�ation</b> : 23 janv. 2004<p>
 * @author Sylvain Lavalley
 */
package tests;

import junit.framework.TestCase;

/*
import iepp.Application;
import iepp.application.averification.VGestVerification;
import iepp.application.averification.VResultatVerif;
import iepp.domaine.ComposantProcessus;
import iepp.domaine.DefinitionProcessus;

*/
/**
 * <b>Date de cr�ation</b> : 23 janv. 2004<p>
 * @author Sylvain Lavalley
 */

public class TestVGestVerification extends TestCase
{
/*

	public void testNomDejaPris () throws Exception
	{
		VGestVerification verif ;
		VResultatVerif res ;
		DefinitionProcessus defProc ;
		ComposantPubliable comp ;

		// Si aucun composant, le nom n'est pas d�j� pris
		// (on suppose que VResultatVerif est deja teste et que v�rifier le niveau
		// d'erreur suffit)
		Application.creerApplication() ;
		Application.getApplication().lancer() ;
		Application.getApplication().creerProjet() ;
		verif = new VGestVerification() ;
		res = verif.nomDejaPris ("composant") ;
		assertEquals (res.niveauErreur(), VResultatVerif.OK) ;

		// Si un composant, v�rifier que �a marche bien
// Modifier quand on pourra charger de vrais composants (utiliser des composants vide,
// pour simplifier ; on suppose qu'ils sont g�r�s avec les composants pleins
// (� v�rifier)
		defProc = Application.getApplication().getProjet().getDefProc() ; 
		comp = new ComposantPubliable() ;
		comp.setNom ("composant1") ;
		defProc.ajouterComposant(new ComposantProcessus (comp, defProc)) ;
		res = verif.nomDejaPris ("composant") ;
		assertEquals (res.niveauErreur(), VResultatVerif.OK) ;
		res = verif.nomDejaPris ("composant1") ;
		assertTrue (res.niveauErreur() != VResultatVerif.OK) ;
		
		// Et avec 3, aux trois positions (on ne sait pas a priori dans quel
		// ordre ils sont m�moris�s)
		comp = new ComposantPubliable() ;
		comp.setNom ("composant2") ;
		defProc.ajouterComposant(new ComposantProcessus (comp, defProc)) ;
		comp = new ComposantPubliable() ;
		comp.setNom ("composant3") ;
		defProc.ajouterComposant(new ComposantProcessus (comp, defProc)) ;
		res = verif.nomDejaPris ("composant") ;
		assertEquals (res.niveauErreur(), VResultatVerif.OK) ;
		res = verif.nomDejaPris ("composant1") ;
		assertTrue (res.niveauErreur() != VResultatVerif.OK) ;
		res = verif.nomDejaPris ("composant2") ;
		assertTrue (res.niveauErreur() != VResultatVerif.OK) ;
		res = verif.nomDejaPris ("composant3") ;
		assertTrue (res.niveauErreur() != VResultatVerif.OK) ;
	}
*/	
}

