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


package org.ipsquad.apes;

import java.util.Vector;

import javax.swing.undo.UndoableEdit;

import org.ipsquad.apes.model.frontend.ApesMediator;
import org.jgraph.graph.GraphUndoManager;

public class ApesUndoManager extends GraphUndoManager
{
	private boolean mBegin = false;
	private Vector action = new Vector();
	private long mLastActionTime = System.currentTimeMillis();
	
	public synchronized boolean addEdit(UndoableEdit anEdit)
	{
		mLastActionTime = System.currentTimeMillis();
		
		if(mBegin)
		{
			action.add(anEdit);
			return true;
		}
		return super.addEdit(anEdit);
	}

	public synchronized void save()
	{
		mBegin = true;
	}
	
	public synchronized Vector restore()
	{
		mBegin = false;
		Vector temp = action;
		action = new Vector();
		return temp;
	}
	
	public void undo( Object undo )
	{
		mLastActionTime = System.currentTimeMillis();
		
		UndoableEdit edit = editToBeUndone(undo);
		if(canUndo(edit))
		{
			super.undo(edit);
			if( edit instanceof ApesMediator.UndoableEdit && ((ApesMediator.UndoableEdit)edit).getIsChained())
			{
				edit =  nextEditToBeUndone(edit);
				undo(edit);
			}
		}
	}
	
	public void redo( Object redo )
	{
		mLastActionTime = System.currentTimeMillis();
		
		UndoableEdit edit = editToBeRedone(redo);
		if(canRedo(redo))
		{
			super.redo(edit);
			edit = nextEditToBeRedone(edit);
			if( edit != null && edit instanceof ApesMediator.UndoableEdit && ((ApesMediator.UndoableEdit)edit).getIsChained())
			{
				redo(edit);
			}
		}
	}
	
	public long getLastActionTime()
	{
		return mLastActionTime;
	}
}
