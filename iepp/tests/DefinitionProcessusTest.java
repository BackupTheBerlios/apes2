package tests;

import iepp.Application;
import iepp.domaine.*;

import junit.framework.TestCase;

/**
 * @author NiBDidi
 *
 * Created on 15 janv. 2004
 * DefinitionProcessusTest.java
 */
public class DefinitionProcessusTest extends TestCase {

	/**
	 * Constructor for DefinitionProcessusTest.
	 * @param arg0
	 */
	public DefinitionProcessusTest(String arg0) {
		super(arg0);
	}

	/*
	 * @see TestCase#setUp()
	 */
	protected void setUp() throws Exception 
	{
		super.setUp();
		Application.creerApplication();
		Application.getApplication().configurerApplication();
		Application.getApplication().chargerLangueCourante();
	}

	/*
	 * @see TestCase#tearDown()
	 */
	protected void tearDown() throws Exception
	{
		super.tearDown();
		Application.reinitialiser();
	}

	public void testDefinitionProcessus()
	{
		DefinitionProcessus defProc = new DefinitionProcessus();
		assertNotNull(defProc.getIdDefProc());
		assertNotNull(defProc.getListeComp());
		assertNotNull(defProc.getNomDefProc());
	}

	public void testAjouterComposant()
	{
		DefinitionProcessus defProc = new DefinitionProcessus();
		assertTrue(defProc.getListeComp().isEmpty());
		defProc.ajouterComposant(null);
		assertTrue(defProc.getListeComp().isEmpty());
	}

	public void testGetNomDefProc() {}

	public void testSetNomDefProc() {}

	public void testGetIdDefProc()
	{
		DefinitionProcessus defProc = new DefinitionProcessus();
		assertNotNull(defProc.getIdDefProc());
		assertEquals(defProc, defProc.getIdDefProc().getRef());
	}

	public void testGetListeComp() {}

	/*
	 * Test pour String toString()
	 */
	public void testToString() {}

	public void testMaj() {}

	/*
	 * Test pour String toString(int, int)
	 */
	public void testToStringintint() {}

	public void testEstDefProc() {}

	public void testGetNbFils() {}

	public void testEstComposant() {}

	public void testEstProduit() {}

	public void testEstInterfaceRequise() {}

	public void testEstInterfaceFournie() {}

	public void testGetFils() {}

	public void testSetNomElement() {}

}
