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


package org.ipsquad.utils;

import java.util.Locale;
import java.util.ResourceBundle;


/**
 * Helper class for application localization.
 * It is implemented as a singleton.
 *
 * @version $Revision: 1.4 $
 */
public class ResourceManager
{
	private static ResourceManager msResourceManager = new ResourceManager();
	private static ResourceBundle msResources = null;

	private ResourceManager() { }
  
	/**
	 * Retrieve the unique instance of ResourceManager
	 *
	 * @return the instance
	 */
	public static ResourceManager getInstance()
	{
		return msResourceManager;
	}

	/**
	 * Set the resource file used in the application
	 *
	 * @param resourceFile the file name minus ".properties"
	 */
	public static void setResourceFile(String resourceFile, Locale locale)
	{
		msResources = ResourceBundle.getBundle(resourceFile, locale);
	}

	/**
	 * Get a string associated to a key
	 *
	 * @param key the key of the string needed
	 */
	public String getString(String key)
	{
		if(msResources!=null)
		{
			return  msResources.getString(key);
		}
		else
		{
			System.err.println("ResourceManager not intialized...");
			return "";
		}
	}
}
