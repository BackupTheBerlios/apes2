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

import javax.swing.Action;
import javax.swing.Box;
import javax.swing.JMenu;
import javax.swing.JMenuBar;
import javax.swing.JMenuItem;
import javax.swing.KeyStroke;

import org.ipsquad.apes.Context;
import org.ipsquad.utils.ResourceManager;

/**
 * Application menu bar
 *
 * @version $Revision: 1.6 $
 */
public class ApesMenuBar extends JMenuBar
{
	private int mNextInsert=0;

	public ApesMenuBar()
	{
		Context context = Context.getInstance();
		JMenu menu = getMenu(ResourceManager.getInstance().getString("menuFile"));
		
		menu.add(context.getAction("New"));
		menu.add(context.getAction("Open"));
		menu.addSeparator();
		menu.add(context.getAction("Save"));
		menu.add(context.getAction("SaveAs"));
		menu.addSeparator();
		/*menu.add(context.getAction("Close"));
		menu.addSeparator();*/
		/*menu.add(context.getAction("Print"));
		menu.addSeparator();*/
		menu.add(context.getAction("Quit"));

		menu = getMenu(ResourceManager.getInstance().getString("menuEdit"));
		menu.add(context.getAction("Undo"));
		menu.add(context.getAction("Redo"));
		menu.addSeparator();
		menu.add(context.getAction("Cut"));
		menu.add(context.getAction("Copy"));
		menu.add(context.getAction("Paste"));
		menu.addSeparator();
		menu.add(context.getAction("Remove"));

		menu = getMenu(ResourceManager.getInstance().getString("menuDisplay"));
		menu.add(context.getAction("Bigger"));
		menu.add(context.getAction("Smaller"));
		menu.addSeparator();
		menu.add(context.getAction("Preferences"));

		menu = getMenu(ResourceManager.getInstance().getString("menuTools"));
		menu.add(context.getAction("Validate"));
		
		menu.addSeparator();
		menu.add(context.getAction("ToolPresentation"));
		//menu.add(context.getAction("Generate"));

		//menu.add(context.getAction("Color"));
		
		menu = getMenu("?");
		menu.add(context.getAction("About"));
	}


	/**
	 * Retrieve (or create) a first level menu of the menubar
	 * It automatically find a menmonic for this menu
	 * The "?" menu is automatically rigth justified
	 */
	public JMenu getMenu(String menuTitle)
	{
		JMenu menu = null;
		int index = 0;

		for(int n=0; n<getMenuCount(); n++)
		{
			// Append the option to the end of the Options menu
			menu = this.getMenu(n);
			if(menu != null)
			{
				// Find a mnemonic that will fit
				while(index < menuTitle.length() && menu.getMnemonic() == menuTitle.charAt(index))
				{
					index++;
				}

				if(menu.getText().compareTo(menuTitle) == 0)
				{
					return menu;
				}
			}
		}

		// Create the new menu and set the mnemonic
		menu = new ApesMenu(menuTitle);
		if(index < menuTitle.length())
		{
			menu.setMnemonic(menuTitle.charAt(index));
		}

		int insert_at = this.getMenuCount() - mNextInsert;
		super.add(menu, insert_at);

		// Insert the glue, update the insert point
		if(menuTitle.toLowerCase().equals("?"))
		{
			super.add(Box.createHorizontalGlue(), insert_at);
			mNextInsert -= 2;
		}

		return menu;
	}



	protected class ApesMenu extends JMenu
	{

		public ApesMenu(String title)
		{
			super(title);
		}

		/**
		 * Add an action and set the accelerator value for the new menu entry
		 */
		public JMenuItem add(Action action)
		{
			JMenuItem item = new JMenuItem(action);
			item.setAccelerator((KeyStroke)action.getValue(Action.ACCELERATOR_KEY));

			return super.add(item);
		}

	}

}
