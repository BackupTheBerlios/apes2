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

package org.ipsquad.apes.ui.actions;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.Icon;
import javax.swing.KeyStroke;

import org.ipsquad.apes.Context;
import org.ipsquad.utils.IconManager;
import org.ipsquad.utils.ResourceManager;

/**
 * Base class for the action of the application
 * Provides convenient constructors and a pointer to the context
 *
 * @version $Revision: 1.2 $
 */
public abstract class ApesAction extends AbstractAction
{
	/// The context of the application is accessible here
	protected Context context = Context.getInstance();
	private String mName;
	
	public void refresh()
	{
		putValue(Action.SHORT_DESCRIPTION, ResourceManager.getInstance().getString(mName));
	}
	/**
	 * @param label the resource key for the action label
	 * @param icon the filename for the action icon
	 */
	public ApesAction(String label, String icon)
	{
		super(ResourceManager.getInstance().getString(label),
		     IconManager.getInstance().getIcon(icon)
			);
		mName = label;
		putValue(Action.SHORT_DESCRIPTION, ResourceManager.getInstance().getString(label));
	}

	/**
	 * @param label the resource key for the action label
	 * @param icon the filename for the action icon
	 * @param key a key associated to the action
	 * @param eventMask a mask for the shortcut of this action
	 */
	public ApesAction(String label, String icon, char key, int eventMask)
	{
		this(label, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key, eventMask));
	}
	
	/**
	 * @param label the resource key for the action label
	 * @param icon the filename for the action icon
	 * @param key a key associated to the action
	 * @param eventMask a mask for the shortcut of this action
	 */
	public ApesAction(String label, String icon, int key, int eventMask)
	{
		this(label, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key, eventMask));
	}

	/**
	 * @param label the resource key for the action label
	 * @param icon the filename for the action icon
	 * @param key a key associated to the action
	 * @param eventMask a mask for the shortcut of this action
	 */
	public ApesAction(String label, Icon icon, int key, int eventMask)
	{
		super(label, icon);
		putValue(Action.ACCELERATOR_KEY, KeyStroke.getKeyStroke(key, eventMask));
		putValue(Action.SHORT_DESCRIPTION, ResourceManager.getInstance().getString(label));
	}
}
