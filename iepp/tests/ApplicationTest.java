package tests;

import junit.framework.TestCase;
import iepp.Application;
import java.util.Vector;

/**
 * @author NiBDidi
 *
 * Created on 15 janv. 2004
 * ApplicationTest.java
 */
public class ApplicationTest extends TestCase {

	/**
	 * Constructor for ApplicationTest.
	 * @param arg0
	 */
	public ApplicationTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		Application.creerApplication();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception 
	{
		super.tearDown();
		Application.reinitialiser();
	}

	public void testCreerApplication()
	{
		Application.reinitialiser();
		// départ, il n'y a pas d'instance de l'application
		assertNull(Application.getApplication());
		// essaye de créer 2 fois une application
		try
		{
			Application.creerApplication();
			assertNotNull(Application.getApplication());
			Application.creerApplication();
			fail("Deux applications tournantes");
		}
		catch(Exception e)
		{
			Application app = Application.getApplication();
			assertEquals(app, Application.getApplication());
		}
	}

	public void testConfigurerApplication()
	{
		Application app = Application.getApplication();
		app.configurerApplication();
		// la configuration ne doit pas être vide, meme si le fichier n'existe pas
		assertFalse(app.estConfigVide());
	}
	
	public void testGetConfigPropriete() 
	{		
		Application app = Application.getApplication();
		app.configurerApplication();
		assertEquals("ressources/langues/", app.getConfigPropriete("dossierLangues"));
		assertNotSame("yoyo", app.getConfigPropriete("dossierLangues"));
		assertEquals("", app.getConfigPropriete("bidon"));
		assertNotSame("",app.getConfigPropriete("langueCourante"));
	}
	
		
	public void testRecupererLanguesDisponibles()
	{
		Application app = Application.getApplication();
		app.configurerApplication();
		Vector langues = app.getLangues();
		assertNull(langues);
		app.recupererLanguesDisponibles(app.getConfigPropriete("dossierLangues"),app.getConfigPropriete("extensionLangue"));
		langues = app.getLangues();
		assertNotNull(langues);
		assertFalse(langues.isEmpty());
		assertTrue(langues.contains("Francais"));
		assertTrue(langues.contains("English"));
	}

		
	public void testChargerLangueCourante()
	{
		Application app = Application.getApplication();
		app.configurerApplication();
		app.chargerLangueCourante();
		assertFalse(app.estLangueVide());
	}

	public void testSetLangueCourante()
	{
		Application app = Application.getApplication();
		app.configurerApplication();
		app.setLangueCourante("Francais");
		assertFalse(app.estLangueVide());
		assertEquals("Francais", app.getConfigPropriete("langueCourante"));
		
		app.recupererLanguesDisponibles(app.getConfigPropriete("dossierLangues"),app.getConfigPropriete("extensionLangue"));
		Vector langues = app.getLangues();
		
		app.setLangueCourante("Bidon");
		if (langues.size() != 0)
		{
			assertFalse(app.estLangueVide());
			assertNotSame("Bidon", app.getConfigPropriete("langueCourante"));
		}
	}
	
	public void testGetTraduction()
	{
		Application app = Application.getApplication();
		app.configurerApplication();
		app.setLangueCourante("Francais");
		assertEquals("Fichier", app.getTraduction("Fichier"));
		assertEquals(Application.CHAINEERREUR, app.getTraduction("Bidon"));
		app.setLangueCourante("English");
		assertEquals("File", app.getTraduction("Fichier"));
		assertEquals(Application.CHAINEERREUR, app.getTraduction("Bidon"));
	}

	public void testGetApplication()
	{
		Application app = Application.getApplication();
		assertEquals(app,Application.getApplication());
		Application.reinitialiser();
		app = Application.getApplication();
		assertEquals(null,Application.getApplication());
	}
}
