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
package org.ipsquad.apes.ui;

import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;

/**
 * 
 * @version $Revision: 1.1 $
 */
public abstract class OptionPanel extends JPanel 
{
	protected JPanel panel ;
	protected JLabel title ;
	
	//protected abstract OptionPanel openPanel();
	
	protected void makeButton(String name, GridBagLayout gridbag,
							  GridBagConstraints c) 
	{
		JButton button = new JButton(name);
		gridbag.setConstraints(button, c);
		panel.add(button);
	}
	protected void makeLabel(String name,
							  GridBagLayout gridbag,
							  GridBagConstraints c) 
	{
		JLabel label = new JLabel(name);
		gridbag.setConstraints(label, c);
		panel.add(label);
	}
	public String getName ()				
	{
			return this.title.getText() ;
	}
	public void setName (String name)				
	{
			this.title.setText(name) ;
	}
}
