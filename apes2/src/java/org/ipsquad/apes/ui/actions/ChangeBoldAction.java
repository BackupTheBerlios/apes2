/*
 * APES is a Process Engineering Software
 * Copyright (C) 2004 IPSquad
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

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * 
 *
 * @version $Revision: 1.1 $
 *
 */
public class ChangeBoldAction extends ApesAction
{
	private boolean mIsBold;
	private Vector mListeners = new Vector();
	
	public ChangeBoldAction(String name)
	{
		super(name, "icons/FileNew.gif", 'I', Event.CTRL_MASK);
		setEnabled(false);
	}
	
	public void addChangeBoldListener( BoldChangeListener l )
	{
		if(!mListeners.contains(l))	
		{
			mListeners.add(l);
		}
	}
	
	public void removeChangeBoldListner( BoldChangeListener l )	
	{
		mListeners.remove(l);
	}
	
	protected void fireBoldChange()
	{
		for( int i = 0; i < mListeners.size(); i++ )
		{
			((BoldChangeListener)mListeners.get(i)).boldChanged(mIsBold);
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		mIsBold = ! mIsBold;
		fireBoldChange();
	}
	
	public void setBold( boolean isBold )
	{
		mIsBold = isBold;
	}
	
	public interface BoldChangeListener
	{
		public void boldChanged(boolean newValue);
	}
}
