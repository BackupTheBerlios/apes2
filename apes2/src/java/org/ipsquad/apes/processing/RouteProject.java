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

import java.awt.Component;

import javax.swing.ProgressMonitor;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.SwingWorker;

/**
 * Route Project
 *
 * This class allow to glance through a project
 *
 * @version $Revision: 1.1 $
 */
public class RouteProject
{
	private Project mProject;
	private RoutedSpemVisitor mVisitor;
	private ProgressMonitor mMonitor = null;
	private boolean mMonitored=false;
	private int mCount=1;
	
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
	
	public void launch(Component parent)
	{
		//mProgressBar.getProgressBar().setString("Initialization...");
       		//mProgressBar.getProgressBar().setStringPainted(true);

		RoutedSpemVisitor visitorTemp=mVisitor;
		mVisitor=new ModelElementCountVisitor();

		launch();

		//System.out.println(((ModelElementCountVisitor)mVisitor).getModelElementCount());
		//mProgressBar.initProgressBar(0,((ModelElementCountVisitor)mVisitor).getModelElementCount());
		mMonitor = new ProgressMonitor(parent, ResourceManager.getInstance().getString("toolsGenerate"), "",
		                               0, ((ModelElementCountVisitor)mVisitor).getModelElementCount());

		mMonitor.setMillisToDecideToPopup(0);
		mMonitor.setMillisToPopup(0);
		
		mVisitor=visitorTemp;
		mMonitored=true;
		
		final SwingWorker worker = new SwingWorker()
		{
			public Object construct()
			{
                		launch();
				return null;
			}
		};
        	worker.start();
		//launch();
	}

	public void launch()
	{
		mVisitor.routingBegin();
		
		ApesProcess p=mProject.getProcess();

		visit(p);
		/*for(int i=0;i<p.modelElementCount();i++)
			visit(p.getModelElement(i));
		*/
		mVisitor.routingEnd();
	}

	public void visit(Object o)
	{
		if(mMonitored) 
		{
			//System.out.println(cpt);
			//mProgressBar.getProgressBar().setString(null);
			//mProgressBar.getProgressBar().setValue(cpt);
			mMonitor.setProgress(mCount);
			mMonitor.setNote(o.toString());
			
			mCount++;
		}
		
		if(o instanceof SPackage)
		{
			((SPackage)o).visit(mVisitor);
			for(int i=0;i<((SPackage)o).modelElementCount();i++)
			{
				visit(((SPackage)o).getModelElement(i));
			}
		}
		else if(o instanceof ModelElement)
		{
			((ModelElement)o).visit(mVisitor);
			/*for(int j=0;j<((ModelElement)o).presentationCount();j++)
			{
				((ModelElement)o).getPresentation(j).visit(mVisitor);
			}*/
		}
	}
};
