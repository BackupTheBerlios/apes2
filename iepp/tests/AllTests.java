package tests;

import junit.framework.Test;
import junit.framework.TestSuite;

/**
 * @author NiBDidi
 *
 * Created on 14 janv. 2004
 * AllTests.java
 */
public class AllTests
{

	public static Test suite()
	{
		TestSuite suite = new TestSuite("Test for tests");
		//$JUnit-BEGIN$
		suite.addTest(new TestSuite(TestIconManager.class));
		suite.addTest(new TestSuite(ApplicationTest.class));

//		suite.addTest(new TestSuite(TestVGestVerification.class));
		suite.addTest(new TestSuite(TestVResultatVerif.class));

		//suite.addTest(new TestSuite(ProjetTest.class));
		//suite.addTest(new TestSuite(DefinitionProcessusTest.class));
		
		// Tests Référentiel
		suite.addTest(new TestSuite(TestReferentiel.class));
		//$JUnit-END$
		return suite;
	}
}
