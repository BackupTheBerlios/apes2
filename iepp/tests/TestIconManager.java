package tests;


import util.IconManager;
import junit.framework.TestCase;



public class TestIconManager extends TestCase
{
	public void testGetInstance()
	{
		IconManager im = IconManager.getInstance();

		assertEquals(im, IconManager.getInstance());
	}

	public void testGetIcon()
	{
		IconManager im = IconManager.getInstance();

		assertNotNull(im.getIcon("ressources/icons/Empty.gif"));
		
		try
		{
			im.getIcon("imadcdges/Empty.gif");
			fail();
		}
		catch(RuntimeException e)
		{
		
		}
	}
}
