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


package apes;

import java.awt.event.ActionEvent;
import java.util.Locale;

import javax.swing.AbstractAction;

import junit.framework.TestCase;

import org.ipsquad.apes.Context;
import org.ipsquad.utils.ResourceManager;


public class TestContext extends TestCase
{
	public void setUp()
	{
		ResourceManager.setResourceFile("resources/Apes", Locale.getDefault());
		Context.getInstance().setTopLevelFrame(null);
	}
	
	public void testGetInstance()
	{
		Context ctx = Context.getInstance();

		assertEquals(ctx, Context.getInstance());
	}


	public void testRegisterGetAction()
	{
		Context ctx = Context.getInstance();
		
		FakeAction a1 = new FakeAction();
		FakeAction a2 = new FakeAction();
		FakeAction a3 = new FakeAction();
		
		assertEquals(null, ctx.getAction("A1"));
		assertEquals(null, ctx.getAction("A2"));
		assertEquals(null, ctx.getAction("A3"));
		
		ctx.registerAction("A1", a1);
		assertEquals(a1, ctx.getAction("A1"));

		ctx.registerAction("A2", a2);
		assertEquals(a1, ctx.getAction("A1"));
		assertEquals(a2, ctx.getAction("A2"));

		ctx.registerAction("A3", a3);
		assertEquals(a1, ctx.getAction("A1"));
		assertEquals(a2, ctx.getAction("A2"));
		assertEquals(a3, ctx.getAction("A3"));
		
		ctx.registerAction("A3", a1);
		assertEquals(a1, ctx.getAction("A1"));
		assertEquals(a2, ctx.getAction("A2"));
		assertEquals(a1, ctx.getAction("A3"));
		
		ctx.removeAction("A1");
		ctx.removeAction("A2");
		ctx.removeAction("A3");
	}

	public void testGetSetTopLevelFrame()
	{
		Context ctx = Context.getInstance();
		// Too much dependencies in ApesFrame...
		//ApesFrame f = new FakeApesFrame();
		
		assertEquals(null, ctx.getTopLevelFrame());
		//ctx.setTopLevelFrame(f);
		//assertEquals(f, ctx.getTopLevelFrame());
		//ctx.setTopLevelFrame(null);
		//assertEquals(null, ctx.getTopLevelFrame());
	}

	private class FakeAction extends AbstractAction
	{
		public void actionPerformed(ActionEvent e)
		{
		
		}
	}
}
