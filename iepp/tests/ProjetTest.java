package tests;

import iepp.Projet;
import iepp.Application;
import junit.framework.TestCase;

/**
 * @author NiBDidi
 *
 * Created on 15 janv. 2004
 * ProjetTest.java
 */
public class ProjetTest extends TestCase {

	/**
	 * Constructor for ProjetTest.
	 * @param arg0
	 */
	public ProjetTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception
	{
		super.setUp();
		Application.creerApplication();
		Application.getApplication().lancer();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception {
		super.tearDown();
		Application.reinitialiser();
	}

	public void testProjet()
	{
		Projet p = new Projet() ;
		assertNotNull(p.getDefProc());
		assertNotNull(p.getFenetreEdition());
	}
}
