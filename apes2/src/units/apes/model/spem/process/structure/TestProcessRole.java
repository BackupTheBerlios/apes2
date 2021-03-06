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


package apes.model.spem.process.structure;

import junit.framework.TestCase;

import org.ipsquad.apes.model.spem.process.structure.ProcessRole;

public class TestProcessRole extends TestCase
{
	ProcessRole proRole = new ProcessRole("process_role");
	
	public void testProcessRole() {
		assertTrue(proRole.getName()=="process_role");
		assertEquals(0, proRole.behaviorCount());
		assertEquals(0, proRole.getParticipationCount());
		assertEquals(0, proRole.getResponsibilityCount());
	}
}
