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

package org.ipsquad.apes.ui.actions;

import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.ActionEvent;
import java.awt.print.PageFormat;
import java.awt.print.Printable;
import java.awt.print.PrinterException;
import java.awt.print.PrinterJob;

import javax.swing.AbstractAction;
import javax.swing.JFrame;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.ContextGraphAdapter;
import org.ipsquad.apes.adapters.WorkDefinitionGraphAdapter;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ResponsabilityGraphAdapter;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.ui.ActivityJGraph;
import org.ipsquad.apes.ui.ContextJGraph;
import org.ipsquad.apes.ui.FlowJGraph;
import org.ipsquad.apes.ui.WorkDefinitionJGraph;
import org.ipsquad.apes.ui.ResponsabilityJGraph;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;
import org.jgraph.JGraph;



/**
 * Print the current graph displayed
 *
 * @version $Revision: 1.2 $
 */

public class PrintDiagramAction extends AbstractAction implements Printable
{
	private SpemGraphAdapter mAdapter = null;
	
	public PrintDiagramAction(String label)
	{
		super(ResourceManager.getInstance().getString(label));
	}

	public int print( Graphics g, PageFormat pf, int pi){
		if( pi >= 1 )
		{
			return NO_SUCH_PAGE;
		}
		
		Graphics2D g2 = (Graphics2D)g;
		
		JGraph mGraph=null;
	
		if(mAdapter instanceof FlowGraphAdapter)
		{
			mGraph=new FlowJGraph(mAdapter);
		}
		else if(mAdapter instanceof ActivityGraphAdapter)
		{
			mGraph=new ActivityJGraph(mAdapter);
		}
		else if(mAdapter instanceof ResponsabilityGraphAdapter)
		{
			mGraph=new ResponsabilityJGraph(mAdapter);
		}
		else if(mAdapter instanceof ContextGraphAdapter)
		{
			mGraph=new ContextJGraph(mAdapter);
		}
		else if(mAdapter instanceof WorkDefinitionGraphAdapter)
		{
			mGraph=new WorkDefinitionJGraph(mAdapter);
		}
		
		JFrame frame = new JFrame();
		frame.getContentPane().add(mGraph);
		frame.pack();
		
		int width = mGraph.getWidth();
		int height = mGraph.getHeight();
		
		g2.translate(pf.getImageableX(),
				pf.getImageableY());

		double dx = 1;
		double dy = 1;
		
		if( mGraph.getWidth() > pf.getWidth() ) 
		{
			dx = (double)(pf.getImageableWidth() / mGraph.getWidth() );
		}
		
		if( mGraph.getHeight() > pf.getHeight() )
		{
			dy = (double)(pf.getImageableHeight() / mGraph.getHeight() );
		}
		
		mGraph.setScale( dx>dy ? dy : dx);
		mGraph.setAntiAliased(true);
		mGraph.paint(g2);
		
		return PAGE_EXISTS;
	}

	
	
	public void actionPerformed(ActionEvent e)
	{
		SpemDiagram selected_diagram = (SpemDiagram)((ApesTreeNode)Context.getInstance().getTopLevelFrame().getTree().getSelectionPath().getLastPathComponent()).getUserObject();
		
		mAdapter = (SpemGraphAdapter)Context.getInstance().getProject().getGraphModel(selected_diagram);
		
		PrinterJob printJob = PrinterJob.getPrinterJob();
		printJob.setPrintable( this );

		if (printJob.printDialog())
		{
			try
			{
				printJob.print();
			}
			catch (PrinterException exception)
			{
				exception.printStackTrace();
				ErrorManager.getInstance().display("errorPrintDiagram", "errorPrintDiagram");
				return;
			}
		}
		
	}
}
