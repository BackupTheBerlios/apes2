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
package org.ipsquad.apes.ui.actions;

import java.awt.Color;
import java.awt.Event;
import java.awt.event.ActionEvent;
import java.util.Vector;

import javax.swing.JColorChooser;

import org.ipsquad.apes.ui.ApesFrame;

/**
 * 
 *
 * @version $Revision: 1.5 $
 *
 */
public class ChangeColorAction extends ApesAction
{
	private Color mColor = Color.black;
	private Vector mListeners = new Vector();
	
	public ChangeColorAction(String name, String icon)
	{
		super(name,"icons/"+icon+".gif", 'C', Event.CTRL_MASK);
		setEnabled(false);
	}
	
	public void addChangeColorListener( ColorChangeListener l )
	{
		if(!mListeners.contains(l))	
		{
			mListeners.add(l);
		}
	}
	
	public void removeChangeColorListner( ColorChangeListener l )
	{
		mListeners.remove(l);
	}
	
	protected void fireColorChange()
	{
		for( int i = 0; i < mListeners.size(); i++ )
		{
			((ColorChangeListener)mListeners.get(i)).colorChanged(mColor);
		}
	}
	
	public void setColor( Color c )
	{
		mColor = c;
	}
	
	public Color getColor()
	{
		return mColor;
	}
	
	public void actionPerformed(ActionEvent e)
	{    
		Color c = JColorChooser.showDialog(((ApesFrame)context.getTopLevelFrame()).getContentPane(),"Color",mColor);
		if( c != null )
		{
			mColor = c;
			fireColorChange();
		}
	}
	
	public interface ColorChangeListener
	{
		public void colorChanged(Color c);
	}
}
