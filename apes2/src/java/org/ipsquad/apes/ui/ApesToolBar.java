/* *
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

import javax.swing.JToolBar;

import org.ipsquad.apes.Context;

/**
 * Application tool bar
 *
 * @version $Revision: 1.14 $
 */
public class ApesToolBar extends JToolBar
{

	public ApesToolBar()
	{
		super();

		Context context = Context.getInstance();

		setFloatable(false);

		add(context.getAction("New"));
		add(context.getAction("Open"));
		add(context.getAction("Save"));

		addSeparator();
		
		//add(context.getAction("Print"));
		
		//addSeparator();
		
		add(context.getAction("Undo"));
		add(context.getAction("Redo"));

		addSeparator();

		add(context.getAction("Cut"));
		add(context.getAction("Copy"));
		add(context.getAction("Paste"));
		
		addSeparator();
		add(context.getAction("AlignH"));
		add(context.getAction("AlignV"));
		
		addSeparator();
		add(context.getAction("Bigger"));
		add(context.getAction("Smaller"));

		addSeparator();
		
		//add(context.getAction("FitToWindow"));
		//
		//addSeparator();

		add(context.getAction("Validate"));
		//add(context.getAction("Generate"));
		
		addSeparator();
		add(context.getAction("ToolPresentation"));
		addSeparator();
		
		add(context.getAction("ChangeForeground"));
		add(context.getAction("ChangeBackground"));
		add(context.getAction("ChangeItalic"));
		add(context.getAction("ChangeBold"));
		
		addSeparator();
		
		//add(context.getAction("Color"));
	}

}
