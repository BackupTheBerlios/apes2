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


package utils;

import java.io.File;

import junit.framework.TestCase;

import org.ipsquad.utils.SimpleFileFilter;


public class TestSimpleFileFilter extends TestCase
{
	public void testAccept()
	{
		SimpleFileFilter filter = new SimpleFileFilter(new String[]{"a","b","abc"}, "truc");
		
		assertTrue(filter.accept(new File("unfichier.a")));
		assertTrue(filter.accept(new File("unfichier.b")));
		assertTrue(filter.accept(new File("unfichier.abc")));
		assertFalse(filter.accept(new File("unfichier.")));
		assertFalse(filter.accept(new File("unfichier")));
		
		filter = new SimpleFileFilter("abc", "truc");
		
		assertFalse(filter.accept(new File("unfichier.a")));
		assertFalse(filter.accept(new File("unfichier.b")));
		assertTrue(filter.accept(new File("unfichier.abc")));
		assertFalse(filter.accept(new File("unfichier.")));
		assertFalse(filter.accept(new File("unfichier")));
	}

}
