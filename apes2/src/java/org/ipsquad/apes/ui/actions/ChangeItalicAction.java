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

import java.awt.Event;
import java.awt.event.ActionEvent;
import java.util.Vector;

/**
 * 
 *
 * @version $Revision: 1.6 $
 *
 */
public class ChangeItalicAction extends ApesAction
{
	private boolean mIsItalic;
	private Vector mListeners = new Vector();
	
	public ChangeItalicAction(String name)
	{
		super(name, "icons/Italic.gif", 'I', Event.CTRL_MASK);
		setEnabled(false);
	}
	
	public void addChangeItalicListener( ItalicChangeListener l )
	{
		if(!mListeners.contains(l))	
		{
			mListeners.add(l);
		}
	}
	
	public void removeChangeItalicListner( ItalicChangeListener l )	
	{
		mListeners.remove(l);
	}
	
	protected void fireItalicChange()
	{
		for( int i = 0; i < mListeners.size(); i++ )
		{
			((ItalicChangeListener)mListeners.get(i)).italicChanged(mIsItalic);
		}
	}
	
	public void actionPerformed(ActionEvent e) 
	{
		mIsItalic = ! mIsItalic;
		fireItalicChange();
	}
	
	public void setItalic( boolean isItalic )
	{
		mIsItalic = isItalic;
	}
	
	public interface ItalicChangeListener
	{
		public void italicChanged(boolean newValue);
	}
}
