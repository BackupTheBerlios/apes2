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
package org.ipsquad.apes.ui;

/**
 * 
 * @version $Revision: 1.3 $
 */
public class PreferencesTreeItem 
{
	public static final int COLOR_PANEL = 1;
	public static final int PATH_PANEL = 2;
	public static final int ERROR_PANEL = 3;
	public static final int DESC_PANEL = 4;
	public static final int LANGUAGE_PANEL = 5;
	
	private String mKey;
	private String mValue;
	private int mPanel;
	
	public PreferencesTreeItem(String key, String val,int panel)
	{
		this.mKey = key;
		this.mValue = val;
		this.mPanel = panel;
	}
	public String getKey()
	{
		return (this.mKey);
	}
	public int getPanel()
	{
		return (this.mPanel);
	}
	public String toString()
	{
		return (this.mValue);
	}
}
