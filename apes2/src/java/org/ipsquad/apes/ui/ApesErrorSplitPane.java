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
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JComponent;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;

import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;

/**
 * Split pane designed for error display
 *
 * @version $Revision: 1.6 $
 */
public class ApesErrorSplitPane extends JSplitPane implements DocumentListener
{
	private JTextArea mArea = new JTextArea();
	private JCheckBox mAutoShow = new JCheckBox(ResourceManager.getInstance().getString("checkboxAutoShow"), true);
	
	public ApesErrorSplitPane(JComponent component)
	{
		super(JSplitPane.VERTICAL_SPLIT);
		
		setOneTouchExpandable(true);
		setResizeWeight(0.7);
		add(component, TOP);
		mArea.setLineWrap(true);
		mArea.setWrapStyleWord(true);
		mArea.setEditable(false);
		mArea.getDocument().addDocumentListener(this);
				
		JPanel bottom_panel = new JPanel();
		bottom_panel.setLayout(new BorderLayout());
		Box button_box = Box.createHorizontalBox();
		
		JButton hide_button = new JButton(ResourceManager.getInstance().getString("buttonHide"));
		hide_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
        			hideErrorArea();
			}
		});
		
		
		JButton clear_button = new JButton(ResourceManager.getInstance().getString("buttonClear"));
		clear_button.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
        			clearErrorArea();
			}
		});
		
		button_box.add(mAutoShow);
		button_box.add(Box.createHorizontalGlue());
		button_box.add(clear_button);
		button_box.add(hide_button);
		
		bottom_panel.add(new JScrollPane(mArea), BorderLayout.CENTER);
		bottom_panel.add(button_box, BorderLayout.SOUTH);
		
		add(bottom_panel, BOTTOM);
		
		ErrorManager.getInstance().setErrorArea(mArea);
	}

	public void hideErrorArea()
	{
		int new_location = getHeight() - getInsets().bottom - getDividerSize();
		int current_location = getDividerLocation();
		
		if(current_location < new_location)
		{
			setDividerLocation(new_location);
			setLastDividerLocation(current_location);
		}
	}
	
	public void showErrorArea()
	{
		int last_location = getLastDividerLocation();
		int current_location = getDividerLocation();
		
		if(current_location == (getHeight() - getInsets().bottom - getDividerSize()) )
		{
			setDividerLocation(last_location);
			setLastDividerLocation(current_location);
		}
	}

	public void clearErrorArea()
	{
		mArea.setText("");
	}
	
	public void insertUpdate(DocumentEvent e)
	{
		if(mAutoShow.isSelected())
		{
			showErrorArea();
		}
	}
	
	public void removeUpdate(DocumentEvent e) {}
	public void changedUpdate(DocumentEvent e) {}
}
