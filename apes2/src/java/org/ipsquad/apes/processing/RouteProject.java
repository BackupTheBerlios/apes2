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


package org.ipsquad.apes.processing;

import javax.swing.ProgressMonitor;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.utils.MonitoredTaskBase;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.TaskMonitorDialog;

/**
 * Route Project
 *
 * This class allow to glance through a project
 *
 * @version $Revision: 1.3 $
 */
public class RouteProject extends MonitoredTaskBase
{
	private Project mProject;
	private RoutedSpemVisitor mVisitor;
	private ProgressMonitor mMonitor = null;
	private boolean mMonitored=false;
	private int mCount=1;
	private TaskMonitorDialog mTask = null;
	
	public RouteProject()
	{
	}

	public RouteProject(RoutedSpemVisitor v)
	{
		mProject=(Project)Context.getInstance().getProject();
		mVisitor=v;
	}
	
	public void setVisitor(RoutedSpemVisitor v)
	{
		mVisitor=v;
	}
	
	public void setTask( TaskMonitorDialog task )
	{
		mTask = task;
	}

	protected void launch()
	{
		RoutedSpemVisitor visitorTemp=mVisitor;
		mVisitor=new ModelElementCountVisitor();

		mVisitor.routingBegin();
		ApesProcess p=mProject.getProcess();
		visit(p);
		mVisitor.routingEnd();
		
		setLengthOfTask(((ModelElementCountVisitor)mVisitor).getModelElementCount());
		
		mMonitored = true;
		mVisitor = visitorTemp;
		
		mVisitor.routingBegin();
		p=mProject.getProcess();
		visit(p);
		mVisitor.routingEnd();
		
		if(((ValidateVisitor)mVisitor).getHasErrors())
		{
			print(ResourceManager.getInstance().getString("errorValidate"));
			mProject.getProcess().getComponent().setValidate(true);
		}
		else
		{
			print(ResourceManager.getInstance().getString("msgValidate"));
		}
	}

	protected void visit(Object o)
	{
		if(mMonitored) 
		{
			setCurrent(mCount);
			print(o.toString());
			
			mCount++;
		}
		
		if(o instanceof IPackage)
		{
			((IPackage)o).visit(mVisitor);
			for(int i=0;i<((IPackage)o).modelElementCount();i++)
			{
				visit(((IPackage)o).getModelElement(i));
			}
		}
		else if(o instanceof ModelElement)
		{
			((ModelElement)o).visit(mVisitor);
		}
	}

	private void print( String msg )
	{
		setMessage(msg);
		if( mTask != null )
		{
			mTask.forceRefresh();
		}
	}
	
	protected Object processingTask()
	{
		launch();
		return null;
	}
};
