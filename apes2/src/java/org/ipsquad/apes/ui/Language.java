/*
 * Created on 29 janv. 2004
 *
 * To change the template for this generated file go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
package org.ipsquad.apes.ui;

import java.util.Locale;


/**
 * @author Scaravetti
 *
 * To change the template for this generated type comment go to
 * Window&gt;Preferences&gt;Java&gt;Code Generation&gt;Code and Comments
 */
public class Language
{
	private String mKey;
	private String mLanguageName;
	
	public Language (Locale locale, String name)
	{
		mKey = locale.getLanguage();
		mLanguageName = name; 
	}
	public String toString()
	{
		return mLanguageName;
	}
	public String getKey()
	{
		return mKey;	
	}
}
