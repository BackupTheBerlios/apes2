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

import java.awt.BorderLayout;

import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JProgressBar;

/**
 * Progress Bar
 *
 * 
 *
 * @version $Revision: 1.5 $
 */
public class ProgressBar extends JFrame
{
	private JProgressBar pb;

	public ProgressBar() 
	{
        	super("Progress...");
        
        	pb = new JProgressBar(0,999);

        	JPanel panel = new JPanel();
        	panel.add(pb);

		JPanel contentPane = new JPanel();
        	contentPane.setLayout(new BorderLayout());
        	contentPane.add(panel, BorderLayout.NORTH);
        	setContentPane(contentPane);
    	}

    	/**
     	* The actionPerformed method in this class
     	* is called when the user presses the start button.
    	 */
    	
	
	public JProgressBar getProgressBar()
	{
		return pb;
	}
	
	public void initProgressBar(int val, int max)
	{
		pb.setMaximum(max);
		pb.setValue(val);	
	}
		
};

