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

import java.util.HashMap;

import javax.swing.Action;
import javax.swing.JInternalFrame;
import javax.swing.event.UndoableEditEvent;
import javax.swing.tree.TreePath;

import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.utils.ConfigManager;



/**
 * Application Context
 *
 * This class centralize the context of the running application.
 * It is implemented as a singleton.
 *
 * @version $Revision: 1.7 $
 */
public class Context
{
	private static final Context mInstance = new Context();
	private MainFrameInterface mTopLevel;
	private HashMap mActionMap = new HashMap();
	private String mFilePath = "";
	private String mWebSitePath = null;
	private Project mProject;
	
	private ApesUndoManager mUndoManager = new ApesUndoManager()
	{
		public void undoableEditHappened(UndoableEditEvent e)
		{
			super.undoableEditHappened(e);
			updateActions();
    	}
	};
	
	private Context() {};
	/**
	 * Allows to retrieve the Context instance
	 * @return the unique instance of Context
	 */
	public static Context getInstance()
	{
		return mInstance;
	}
	
	public ApesUndoManager getUndoManager()
	{
		return mUndoManager;
	}

	public void updateActions()
	{
		if(Context.getInstance().getAction("Undo")!=null
		&& Context.getInstance().getAction("Redo")!=null
		&& Context.getInstance().getAction("Save")!=null)
		{
			Context.getInstance().getAction("Undo").setEnabled(mUndoManager.canUndo());
			Context.getInstance().getAction("Redo").setEnabled(mUndoManager.canRedo());
			Context.getInstance().getAction("Save").setEnabled(mUndoManager.canUndo() || mFilePath==null);
			/*Context.getInstance().getAction("Undo").setEnabled(false);
			Context.getInstance().getAction("Redo").setEnabled(false);*/
		}
	}

	/**
	 * Set the current project the program
	 *
	 * It automatically close every internal frame and set the new project root
	 *
	 * @param project the new project to edit
	 */
	public void setProject(Project project)
	{
		setProject(project, ConfigManager.getInstance().getProperty("WorkspaceTitledefaultPath"));
	}
	
	/**
	 * Set the current project the program
	 *
	 * It automatically close every internal frame and set the new project root
	 *
	 * @param project the new project to edit
	 * @param filePath the file path or null if there is no file associated to this project
	 */
	public void setProject(Project project, String filePath)
	{
		setFilePath(filePath);
		
		if(mProject!=null)
		{
			mProject.clearListeners();
		}

		ApesMediator.getInstance().clearAll();
		
		JInternalFrame[] frames = mTopLevel.getDesktop().getAllFrames();
		for(int i=0; i<frames.length; i++)
		{
			frames[i].dispose();
		}
		
		SpemTreeAdapter adapter = (SpemTreeAdapter)mTopLevel.getTree().getModel();
		if( ((ApesTreeNode)adapter.getRoot()).getUserObject() != project.getProcess() )
		{
			adapter.setRoot(project.getProcess());
		}
		
		project.setListeners();
		project.initApesMediator();
		
		mProject = project;
		
		if(Context.getInstance().getAction("Remove")!=null
		&& Context.getInstance().getAction("Bigger")!=null
		&& Context.getInstance().getAction("Smaller")!=null)
		{
			Context.getInstance().getAction("Remove").setEnabled(false);
			Context.getInstance().getAction("Bigger").setEnabled(false);
			Context.getInstance().getAction("Smaller").setEnabled(false);
		}
		
		ApesMediator.getInstance().setProcess(project.getProcess());
		
		mUndoManager.discardAllEdits();
		
		updateActions();
		
		ApesTreeNode toSelect = (ApesTreeNode)((ApesTreeNode)getTopLevelFrame().getTree().getModel().getRoot()).getChildAt(0);
		getTopLevelFrame().getTree().setSelectionPath(new TreePath( toSelect.getPath()) );
	}

	
	/**
	 * Retrieve the projet currently opened in the program
	 *
	 * @return the project
	 */
	public Project getProject()
	{
		return mProject;
	}


	/**
	 * Set the current file associated to the project
	 *
	 * @param filePath the file path or null if there is no file
	 */
	public void setFilePath(String filePath)
	{
		mFilePath = filePath;
		mTopLevel.setFilePath(filePath);
	}

	
	/**
	 * Get the current file associated to the project
	 *
	 * @return the current file associated or null if there is no association
	 */
	public String getFilePath()
	{
		return mFilePath;
	}

	/**
	 * Retrieve the top level frame of the GUI
	 * @return the main frame
	 */
	public MainFrameInterface getTopLevelFrame()
	{
		return mTopLevel;
	}
	
	/**
	 * Set the top level frame of the GUI
	 * @param topLevel
	 */
	public void setTopLevelFrame(MainFrameInterface topLevel)
	{
		mTopLevel = topLevel;
	}
	
	/**
	 * Register an action in the context
	 * When an action is registered it can be accessed everywhere thanks to getAction
	 *
	 * @param key the identifier of the action in the context
	 * @param action the action itself
	 */
	public void registerAction(String key, Action action)
	{
		mActionMap.put(key, action);
	}
	
	/**
	 * Retrieve a previously registered action from the context
	 * Useful to enable/disable an action in the whole application
	 *
	 * @param key the identifier of the action you need
	 * @return the action or null if there's nothing registered with the given key
	 */
	public Action getAction(String key)
	{
		return (Action)mActionMap.get(key);
	}
	
}
