/*
 * <b>Date de création</b> : 23 janv. 2004<p>
 * @author Sylvain Lavalley
 */
package tests;

import iepp.application.averification.VResultatVerif;
import junit.framework.TestCase;


/**
 * <b>Date de création</b> : 23 janv. 2004<p>
 * @author Sylvain Lavalley
 */
public class TestVResultatVerif extends TestCase
{

	public void testAjouterErr ()
	{
		// Vérifier que les différentes constantes de niveaux d'erreur sont bien dans
		// le bon ordre (OK < AVERTISSEMENT < ERREUR)
		assertTrue (VResultatVerif.OK < VResultatVerif.AVERTISSEMENT) ;
		assertTrue (VResultatVerif.AVERTISSEMENT < VResultatVerif.ERREUR) ;

		// Au début, aucune erreur et aucun avertissement ; niveau d'erreur = OK
		VResultatVerif res = new VResultatVerif("titre") ;
		assertEquals (res.nbAvert(), 0) ;
		assertEquals (res.nbErr(), 0) ;
		assertEquals (res.niveauErreur(), VResultatVerif.OK) ;
		// Le titre est correct
		assertEquals (res.getTitre(), "titre") ;
		// Les vecteurs de messages sont vides
		assertEquals (res.recupAvert().size(), 0) ;
		assertEquals (res.recupErr().size(), 0) ;

		// Si on ajoute une erreur, nbErr = 1, nbAvert = 0, niveau d'erreur = ERREUR
		res.ajouterErr ("MsgErreur") ;
		assertEquals (res.nbAvert(), 0) ;
		assertEquals (res.nbErr(), 1) ;
		assertEquals (res.niveauErreur(), VResultatVerif.ERREUR) ;
		// Les vecteurs renvoyés contiennent bien les infos
		assertEquals (res.recupAvert().size(), 0) ;
		assertEquals (res.recupErr().size(), 1) ;
		assertEquals ((String)res.recupErr().get(0), "MsgErreur") ;

		// Si on ajoute un avertissement nbErr = 1, nbAvert = 1, niveau d'erreur = ERREUR
		res.ajouterAvert ("MsgAvert") ;
		assertEquals (res.nbAvert(), 1) ;
		assertEquals (res.nbErr(), 1) ;
		assertEquals (res.niveauErreur(), VResultatVerif.ERREUR) ;
		// Les vecteurs renvoyés contiennent bien les infos
		assertEquals (res.recupAvert().size(), 1) ;
		assertEquals (res.recupErr().size(), 1) ;
		assertEquals ((String)res.recupAvert().get(0), "MsgAvert") ;
		assertEquals ((String)res.recupErr().get(0), "MsgErreur") ;
	}
	
	
	public void testAjouterAvert()
	{
		VResultatVerif res = new VResultatVerif("") ;
		// Si on ajoute un avertissement nbErr = 0, nbAvert = 1, niveau d'erreur = AVERTISSEMENT
		res.ajouterAvert ("MsgAvert") ;
		assertEquals (res.nbAvert(), 1) ;
		assertEquals (res.nbErr(), 0) ;
		assertEquals (res.niveauErreur(), VResultatVerif.AVERTISSEMENT) ;
		// Les vecteurs renvoyés contiennent bien les infos
		assertEquals (res.recupAvert().size(), 1) ;
		assertEquals (res.recupErr().size(), 0) ;
		assertEquals ((String)res.recupAvert().get(0), "MsgAvert") ;
		
	}

}
