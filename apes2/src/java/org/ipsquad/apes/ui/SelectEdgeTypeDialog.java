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
import java.awt.Frame;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.Box;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JDialog;
import javax.swing.JLabel;
import javax.swing.JRadioButton;

import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.utils.ResourceManager;


/**
 * This dialog asks the user to specify the type of the link role -> activity to create
 *
 * @version $Revision: 1.1 $
 */
public class SelectEdgeTypeDialog extends JDialog
{
	protected int mSelectedType = FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE;
	protected boolean mIsPersistant;
	
	/**
	 * Creates the dialog
	 * 
	 * @param parent the parent frame
	 */
	public SelectEdgeTypeDialog(Frame parent)
	{
		super(parent, ResourceManager.getInstance().getString("SelectType"), true);
	}
		
	protected void dialogInit() 
	{
		super.dialogInit();
	
		setLocation(getParent().getWidth()/2-getWidth()/2,getParent().getHeight()/2-getHeight()/2);
		getContentPane().setLayout(new BorderLayout());
		setResizable(false);
	
		//creates an vertical box which contains the question 
		Box tmp_upper_box = Box.createVerticalBox();
		
		tmp_upper_box.add(Box.createVerticalStrut(10));
		tmp_upper_box.add(new JLabel(ResourceManager.getInstance().getString("QuestionType")));
		
		//creates elements center on the dialog box
		final JCheckBox checkbox = new JCheckBox(ResourceManager.getInstance().getString("PersistantType"), true);
		checkbox.setEnabled(false);
		checkbox.addActionListener(new ActionListener() 
	    		{
    		public void actionPerformed(ActionEvent e) 
    		{
    			mIsPersistant = !mIsPersistant;
    		}
		});
		
		final JRadioButton performer = new JRadioButton(ResourceManager.getInstance().getString("PerformerType"));
	    performer.setSelected(true);
	    performer.addActionListener(new ActionListener() 
	    		{
		    		public void actionPerformed(ActionEvent e) 
		    		{
		    			checkbox.setEnabled(false);
		    		}
	    		});
	    
	    
	    final JRadioButton assistant = new JRadioButton(ResourceManager.getInstance().getString("AssistantType"));
	    assistant.addActionListener(new ActionListener() 
	    		{
		    		public void actionPerformed(ActionEvent e) 
		    		{
		    			checkbox.setEnabled(true);
		    		}
	    		});
	    
	    ButtonGroup group = new ButtonGroup();
	    group.add(performer);
	    group.add(assistant);
	    
	    //creates a vertical box which contains radio button and checkbox
	    Box tmp_center_box = Box.createVerticalBox();
		tmp_center_box.add(Box.createVerticalGlue());
		tmp_center_box.add(Box.createVerticalStrut(15));
		tmp_center_box.add(performer);
		tmp_center_box.add(assistant);
		tmp_center_box.add(Box.createVerticalStrut(15));
		tmp_center_box.add(checkbox);
		tmp_center_box.add(Box.createVerticalStrut(10));
		
		//creates cancel button
		JButton cancel = new JButton(ResourceManager.getInstance().getString("LibCancel"));
		cancel.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						mSelectedType = FlowDiagram.ROLE_ACTIVITY_UNDEFINED_LINK_TYPE;							
						dispose();
					}
				});
				
		//creates ok button
		JButton validate = new JButton(ResourceManager.getInstance().getString("LibOK"));
		validate.addActionListener(new ActionListener()
				{
					public void actionPerformed(ActionEvent e)
					{
						if(performer.isSelected())
						{
							mSelectedType = FlowDiagram.ROLE_ACTIVITY_PERFORMER_LINK_TYPE;
						}
						else if(assistant.isSelected())
						{
							mSelectedType = FlowDiagram.ROLE_ACTIVITY_ASSISTANT_LINK_TYPE;
						}
						dispose();
					}
				});
		
		//creates an horizontal box which contains ok and cancel
		Box tmp_bottom_box = Box.createHorizontalBox();
		
		tmp_bottom_box.add(Box.createHorizontalGlue());
		tmp_bottom_box.add(validate);
		tmp_bottom_box.add(Box.createHorizontalStrut(10));//,Box.RIGHT_ALIGNMENT);
		tmp_bottom_box.add(cancel);
		//tmp_bottom_box.add(Box.createHorizontalStrut(this.getWidth()-validate.getWidth()-10-cancel.getWidth()-40));
		
		//add all elements to this dialog
		getContentPane().add(tmp_upper_box, BorderLayout.NORTH);
		getContentPane().add(tmp_center_box, BorderLayout.CENTER);
		getContentPane().add(tmp_bottom_box, BorderLayout.SOUTH);
		
	    mIsPersistant = checkbox.isSelected();
	}
	
	/**
	 * Gets the type selected by the user
	 * 
	 * @return the type selected by the user
	 * @see FlowDiagram
	 */
	public int getSelectedType()
	{
		return mSelectedType;
	}
	
	/**
	 * Returns true if the persistant check box has been selected
	 * 
	 * @return true if the persistant check box has been selected, false otherwise
	 */
	public boolean getIsPersistant()
	{
		return mIsPersistant;
	}
}
