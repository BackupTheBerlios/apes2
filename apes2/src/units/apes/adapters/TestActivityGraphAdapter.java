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


package apes.adapters;

import java.util.Hashtable;
import java.util.Map;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import junit.framework.TestCase;

import org.ipsquad.apes.ApesMain;
import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.frontend.ApesMediator;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.ipsquad.utils.ConfigManager;
import org.jgraph.graph.GraphModel;

public class TestActivityGraphAdapter extends TestCase
{
	static SpemTreeAdapter model;
	static ActivityDiagram diagram1;
	static ActivityGraphAdapter adapter1;
	static ActivityDiagram diagram2;
	static ActivityGraphAdapter adapter2;
	static ApesGraphCell ac;
	
	
	//public TestSpemGraphAdapter()
	static {
		ConfigManager.init(ApesMain.createDefaultProperties());
		
		Context context = Context.getInstance();
		
		model = new SpemTreeAdapter();
		diagram1 = new ActivityDiagram();
		diagram2 = new ActivityDiagram();
		adapter1 = new ActivityGraphAdapter(diagram1);
		adapter2 = new ActivityGraphAdapter(diagram2);

		context.setTopLevelFrame(new MainFrameInterface()
		{
			private JTree tree = new JTree(model);
			
			public JTree getTree() { return tree; }
			public JDesktopPane getDesktop() { return new JDesktopPane(); }
			public void setToolPalette(ToolPalette toolPalette) { fail(); }
			public ToolPalette getToolPalette() { fail(); return null; }
			public void openDiagram(GraphModel model) { fail(); }
			public void deleteDiagram(GraphModel model) { fail(); }
			public void setDefaultToolPalette() { fail(); }
			public void setFilePath(String filePath) { }
			public GraphFrame getGraphFrame(GraphModel model) { fail(); return null; }
			
		});

		context.setProject(new Project());
		ApesMediator.getInstance().addApesMediatorListener( adapter1 );
		ApesMediator.getInstance().registerDiagram( diagram1 );
		ApesMediator.getInstance().addApesMediatorListener( adapter2 );
		ApesMediator.getInstance().registerDiagram( diagram2 );
		
		context.getProject().getProcess().getComponent().addModelElement(diagram1);
		context.getProject().getProcess().getComponent().addModelElement(diagram2);
		
		//model.addModelElementToPackage(diagram1, (SPackage)model.getRoot());
		//model.addModelElementToPackage(diagram2, (SPackage)model.getRoot());
		
	}
	
	
	public void testInsertInTwoDiagram()
	{
		assertTrue(ActivityGraphAdapter.getRoots(adapter1).length==0);
		assertTrue(ActivityGraphAdapter.getRoots(adapter2).length==0);
		
		Activity a = new Activity();
		
		ac = adapter1.associateGraphCell(a);
		Map attr = ac.getAttributes();
		Hashtable attributes = new Hashtable();
		attributes.put(ac, attr);

		adapter1.insert(new Object[]{ac}, attributes, null, null, null);
		assertTrue(ActivityGraphAdapter.getRoots(adapter1).length==1);



		ac = adapter2.associateGraphCell(a);
		attr = ac.getAttributes();
		attributes = new Hashtable();
		attributes.put(ac, attr);

		adapter2.insert(new Object[]{ac}, attributes, null, null, null);
		assertTrue(ActivityGraphAdapter.getRoots(adapter2).length==1);
		
	}

}
