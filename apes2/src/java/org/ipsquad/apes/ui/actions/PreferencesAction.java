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

import java.awt.event.ActionEvent;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.PreferencesDialog;


/**
*
* @version $Revision: 1.2 $
*/
public class PreferencesAction extends ApesAction
{
	public PreferencesAction()
	{
		super("displayPreferences", "icons/Empty.gif");
	}
	public void actionPerformed(ActionEvent e)
	{
		PreferencesDialog pref = new PreferencesDialog();
		pref.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				((PreferencesDialog)e.getWindow()).cancelSave();
				((PreferencesDialog)e.getWindow()).dispose();
				
			}
		});
		ApesFrame frame = (ApesFrame)Context.getInstance().getTopLevelFrame();
		pref.setSize(600,450);
		pref.setLocation(frame.getX()+(frame.getWidth()-pref.getWidth())/2,frame.getY()+(frame.getHeight()-pref.getHeight())/2);
		pref.setResizable(false);
		pref.setVisible (true) ;

	}
}

