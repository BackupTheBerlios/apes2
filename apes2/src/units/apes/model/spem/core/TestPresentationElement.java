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
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.core.PresentationElement;

public class TestPresentationElement extends TestCase
{
	private PresentationElement createPresentationElement()
	{
		return new PresentationElement()
		{
			public void visit(SpemVisitor visitor) {}
		};	
	}

	private ModelElement createModelElement()
	{
		return new ModelElement()
		{
			public void visit(SpemVisitor visitor) {}
		};	
	}

	public void testSubject()
	{
		PresentationElement pe = createPresentationElement();
		ModelElement me = createModelElement();

		assertNull(pe.getSubject());
		pe.setSubject(me);
		assertEquals(me, pe.getSubject());
		pe.setSubject(null);
		assertNull(pe.getSubject());
	}
}
