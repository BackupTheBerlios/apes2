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

import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ContextGraphAdapter;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.ResponsabilityGraphAdapter;
import org.ipsquad.apes.adapters.WorkDefinitionGraphAdapter;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.adapters.WorkProductStateCell;
import org.ipsquad.apes.ui.WorkDefinitionJGraph;
import org.ipsquad.apes.ui.ActivityJGraph;
import org.ipsquad.apes.ui.ContextJGraph;
import org.ipsquad.apes.ui.FlowJGraph;
import org.ipsquad.apes.ui.ResponsabilityJGraph;
import org.jgraph.JGraph;


/**
 * Save a graph in a jpeg file and create a html map
 *
 * @version $Revision: 1.2 $
 */
public class SaveJpegWithMap
{
	private SpemGraphAdapter mAdapter; 
	private Vector mHtmlCode=new Vector();
	
	/**
	 * Constructor 
	 *
	 * @param adapter the SpemGraphAdapter containing the diagram to be saved
	 */ 
	public SaveJpegWithMap(SpemGraphAdapter adapter)
	{
		mAdapter = adapter;
	}	


	
	
	private static String normalizeName(String name)
			{
				name=name.replace(' ','_');
				name=name.replace(':','_');
				name=name.replace('\\','_');
				name=name.replace('/','_');
				name=name.replace('*','_');
				name=name.replace('?','_');
				name=name.replace('"','_');
				name=name.replace('<','_');
				name=name.replace('>','_');
				name=name.replace('|','_');
				name=name.replace('é','e');
				name=name.replace('è','e');
				name=name.replace('ù','u');
				name=name.replace('ç','c');
				name=name.replace('à','a');
				name=name.replace('ô','o');

				return name;
			}

	public String createHtmlMap()
	{
		mHtmlCode.add("<MAP NAME=\""+normalizeName(mAdapter.getName())+"\">\n");

		JGraph mGraph=null;
		
		if(mAdapter instanceof ContextGraphAdapter)
		{
			mGraph=new ContextJGraph(mAdapter);
		}
		else if(mAdapter instanceof ResponsabilityGraphAdapter)
		{
			mGraph=new ResponsabilityJGraph(mAdapter);
		}
		else if(mAdapter instanceof ActivityGraphAdapter)
		{
			mGraph=new ActivityJGraph(mAdapter);
		}
		else if(mAdapter instanceof FlowGraphAdapter)
		{
			mGraph=new FlowJGraph(mAdapter);
		}
		else if(mAdapter instanceof WorkDefinitionGraphAdapter)
		{
			mGraph=new WorkDefinitionJGraph(mAdapter);
		}

		JFrame frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(mGraph));
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.pack();
		frame.setVisible(false);

		Vector tmp=new Vector();
		Object o[]=mGraph.getRoots();
		
		int x1,x2,y1,y2;
		for(int i=0;i<o.length;i++)
		{
			if(o[i] instanceof ActivityCell 
					|| o[i] instanceof WorkProductCell 
					|| o[i] instanceof ProcessRoleCell 
					|| o[i] instanceof WorkProductStateCell
			)
			{
				x1=(int)mGraph.getCellBounds(o[i]).getX();
				x2=x1+(int)mGraph.getCellBounds(o[i]).getWidth();
				y1=(int)mGraph.getCellBounds(o[i]).getY();
				y2=y1+(int)mGraph.getCellBounds(o[i]).getHeight();
				mHtmlCode.add("<AREA Shape=\"Polygon\" coords = \""+x1 +","+y1+","+x2+","+y1+","+x2+","+y2+","+x1+","+y2+"\" HREF=\""+o[i]+"\">\n");
			}
		}

		mHtmlCode.add("</MAP>\n");
		//mHtmlCode.add("<IMG SRC=\""+relativePath(fileDiagram(getGraphAdapter()),fileDiagramImage(getGraphAdapter()))+"\" USEMAP=\"#"+nameDiagramMap(getGraphAdapter())+"\">\n");

		return generateFile();
	}

	private String generateFile()
	{
		String code=new String();
		for(int i=0;i<mHtmlCode.size();i++)
		{
			code+=(String)mHtmlCode.get(i);
		}
		return code;
	}
};
