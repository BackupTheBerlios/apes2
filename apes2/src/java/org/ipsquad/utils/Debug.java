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

import java.io.PrintStream;

/**
 * Debug facility class
 * 
 * Here is two example of utilization :
 *
 * <code>if(Debug.enabled) Debug.print("message");</code>
 *
 * <code>if(Debug.enabled) Debug.check(a & b | c, "message");</code>
 *
 * Compilers automatically optimize the code if Debug.enabled is set to false
 *
 * @version $Revision: 1.7 $
 */
public class Debug
{
	/// Debug enabled flag
	public static final boolean enabled = true;

	public static final int ALL = 0x00001111;
	public static final int MODEL = 0x00000001;
	public static final int ADAPTER = 0x00000010;
	public static final int VIEW = 0x00000100;
	public static final int MEDIATOR = 0x00001000;
	
	private static int mMask = ALL;//MODEL | MEDIATOR;
	private static PrintStream msErrorOut = System.err;

	public void setMask(int mask)
	{
	    mMask = mask;
	}
	
	/**
	 * Display a debug string
	 *
	 * @param s string to display
	 */
	public static void print(int level, String s)
	{
	    if((level & mMask) == level)
	        msErrorOut.println(s);
	}
	
	/**
	 * Check a condition
	 * 
	 * @exception IllegalArgumentException throwed if the condition is false
	 * @param condition the condition to evaluate
	 * @param message the message contained by the exception
	 */
	public static void check(boolean condition, String message)
	{
		if(!condition)
		{
			throw new IllegalArgumentException(message);
		}
	}
	
	/**
	 * Check a condition
	 *
	 * @param condition
	 */
	public static void check(boolean condition)
	{
		check(condition, "An assertion failed!");
	}
}
