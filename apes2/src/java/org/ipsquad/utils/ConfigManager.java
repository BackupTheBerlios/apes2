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

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

/**
 *
 * @version $Revision: 1.2 $
 */
public class ConfigManager
{
	private static final String msConfigDir = ".apes2";
	private static final String msSuffix = "cfg";
	private static final String mModuleName = "apes2";
	private static ConfigManager mConfigManager = null;
	
	private Properties mProperties = null;

	{
		String home = System.getProperty("user.home");
		String filesep = System.getProperty("file.separator");

		File cfg_dir = new File(home+filesep+msConfigDir+filesep);
		if(!cfg_dir.exists())
		{
			cfg_dir.mkdir();
		}
	}
	
	private ConfigManager(Properties properties )
	{
		mProperties = properties;
	}
	
	/**
	 * Initialize the ConfigManager instance 
	 * If the user has now personal settings then it is initialized
	 * using the default values.
	 * 
	 */
	public static void init( Properties defaults)
	{
		if( mConfigManager == null )
		{	
			Properties properties = new Properties(defaults);
			mConfigManager = new ConfigManager(properties);
			
			try
			{
				String filesep = System.getProperty("file.separator");
				String home = System.getProperty("user.home");
				
				File cfg = new File(home+filesep+msConfigDir+filesep
						+mModuleName+"."+msSuffix);

				properties.load(new FileInputStream(cfg));
			}
			catch(Exception e)
			{
				mConfigManager = new ConfigManager(defaults);
			}
		}
	}


	/**  
	 * Retrieve the ConfigManager instance
	 * 
	 * @return ConfigManager the configManager 
	 */
	public static ConfigManager getInstance()
	{
		return mConfigManager;
	}
	
	/**
	 * Save the properties of this ConfigManager. In the user's home directory.
	 * 
	 * @throws IOException
	 */
	public void save() throws IOException
	{
		String filesep = System.getProperty("file.separator");
		String home = System.getProperty("user.home");
		
		File cfg = new File(home+filesep+msConfigDir+filesep
				+mModuleName+"."+msSuffix);

		mProperties.store(new FileOutputStream(cfg), " "+mModuleName);
	}

	/**
	 * Retrieve the properties attached to this ConfigManager
	 * 
	 * @return Properties the properties
	 */
	public Properties getProperties()
	{
		return mProperties;
	}

	/**
	 * Set a value associated to a key
	 *
	 * @param key the key of the string needed
	 * @param value the value of the string needed
	 */
	public void setProperty(String key, String value)
	{
		mProperties.setProperty(key, value);
	}


	/**
	 * Get a string associated to a key
	 *
	 * @param key the key of the string needed
	 */
	public String getProperty(String key)
	{
		return mProperties.getProperty(key);
	}

	/**
	 * Get a boolean associated to a key
	 *
	 * @param key the key of the string needed
	 */
	public boolean getPropertyBoolean(String key)
	{
		return Boolean.valueOf(mProperties.getProperty(key)).booleanValue();
	}

	/**
	 * Get an integer associated to a key
	 *
	 * @param key the key of the string needed
	 */
	public int getPropertyInteger(String key)
	{
		return Integer.parseInt(mProperties.getProperty(key));
	}

	/**
	 * Get a double associated to a key
	 *
	 * @param key the key of the string needed
	 */
	public double getPropertyDouble(String key)
	{
		return Double.parseDouble(mProperties.getProperty(key));
	}
}
