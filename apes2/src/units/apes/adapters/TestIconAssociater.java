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


package apes.adapters;

import javax.swing.Icon;

import junit.framework.TestCase;

import org.ipsquad.apes.adapters.IconAssociater;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.IconManager;


public class TestIconAssociater extends TestCase
{
	IconManager im;
	
	public void setUp()
	{
		im = IconManager.getInstance();
	}
	
        public void testPackageIcon()
        {
                Icon expected = im.getIcon("icons/TreePackage.gif");
                Icon result = processThis(new SPackage());
                assertNotNull(expected);
                assertNotNull(result);
        }

        public void testProductIcon()
        {
                Icon expected = im.getIcon("icons/TreeWorkProduct.gif");
		Icon result = processThis(new WorkProduct());
		assertNotNull(expected);
		assertNotNull(result);
        }

        public void testRoleIcon()
        {
		Icon expected = im.getIcon("icons/TreeRole.gif");
		Icon result = processThis(new ProcessRole());
		assertNotNull(expected);
		assertNotNull(result);
        }

        public void testActivityIcon()
        {
		Icon expected = im.getIcon("icons/TreeActivity.gif");
		Icon result = processThis(new Activity());
		assertNotNull(expected);
		assertNotNull(result);
        }
	
	private Icon processThis(Element e)
	{
		IconAssociater ia = new IconAssociater(im);
		e.visit(ia);
		return ia.getIcon();
	}
}
