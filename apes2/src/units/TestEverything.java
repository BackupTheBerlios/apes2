/*
 * APES is a Process Engineering Software
 * Copyright (C) 2003-2004 IPSquad
 * team@ipsquad.tuxfamily.org
 *
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
 */


import java.io.File;
import java.util.Vector;

import junit.framework.TestCase;
import junit.framework.TestSuite;


public class TestEverything extends TestSuite
{
	public TestEverything()
	{
		try
		{
			Vector vec = new Vector();
			
			addSuiteClasses("", new File("."), vec);
			
			for(int i=0; i<vec.size(); i++)
			{
				String tmp = (String) vec.get(i);
				
				int index = tmp.indexOf("build"); 
				
				if(  index != -1 )
				{
					tmp = tmp.substring( index+6, tmp.length() );
				}
				
				if( tmp.indexOf("JSX") != -1 )
					continue;
				
				Class clazz = Class.forName(tmp);
				
				if(!clazz.equals(getClass()) && (clazz.getSuperclass().equals(TestCase.class) || clazz.getSuperclass().equals(TestSuite.class)))
				{
					addTestSuite(clazz);
				}
			}
		}
		catch(ClassNotFoundException e)
		{
			e.printStackTrace();
			throw new RuntimeException();
		}
	}
	
	protected static void addSuiteClasses(String packageFragmentRoot, File filePackageFragment, Vector suiteClassCollector)
	{
		File[] files = filePackageFragment.listFiles();
		
		for(int i=0; i<files.length; i++)
		{
			File file = files[i];
			if(file.getName().length()>4 && file.getName().substring(0,4).equals("Test") && file.getName().indexOf(".class")!=-1)
			{
				suiteClassCollector.add(fullyQualifiedClassName(packageFragmentRoot, file.getName()));
			}
			else if(file.isDirectory())
			{
				String extendedPackage;
				
				if(packageFragmentRoot.equals(""))
				{
					extendedPackage = file.getName();
				}
				else
				{
					extendedPackage = packageFragmentRoot + "." + file.getName();
				}
				addSuiteClasses(extendedPackage, file, suiteClassCollector);
			}
		}
	}

	
	protected static String fullyQualifiedClassName(String packageName, String fileName)
	{
		if(packageName.equals(""))
		{
			return fileName.substring(0, fileName.indexOf(".class")); 
		}
		else
		{
			return packageName + "." + fileName.substring(0, fileName.indexOf(".class"));
		}
	}

	
	protected static String slashPackageName(String dotPackageName)
	{
		return dotPackageName.replace('.', '/');
	}

	
	public void testFoo() { }
}
