/*
 * APES is a Process Engineering Software
 * Copyright (C) 2002-2003 IPSquad
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


package apes.model.spem.core;

import junit.framework.TestCase;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.Element;


public class TestElement extends TestCase
{
	private Element createElement(String name)
	{
		return new Element(name)
		{
			public void visit(SpemVisitor visitor) {}
		};
	}
	

	private Element createElement()
	{
		return new Element()
		{
			public void visit(SpemVisitor visitor) {}
		};
	}

	
	public void testWithName()
	{
		Element e = createElement("testNoName");
		
		assertTrue(e.getName().equals("testNoName"));
	}
	
	public void testNoName()
	{
		Element e = createElement();
		String name = e.getName();
		assertTrue(name.substring(0,6).equals("noname"));
		
		int init = Integer.parseInt(name.substring(6,name.length()));
		
		for(int i=0; i<100; i++)
		{
			e = createElement();
			name = e.getName();
			assertTrue(name.substring(0,6).equals("noname"));

			int cnt = Integer.parseInt(name.substring(6,name.length()));
			assertTrue(cnt == (i+init+1) );
		}
		
	}
	
	public void testToString()
	{
		Element e = createElement();
		
		assertTrue(e.getName().equals(e.toString()));
	}
}
