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

package org.ipsquad.apes.processing;

import java.awt.Color;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.OutputStream;

import javax.swing.JFrame;

import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ContextGraphAdapter;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ResponsabilityGraphAdapter;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkDefinitionGraphAdapter;
import org.ipsquad.apes.ui.ActivityJGraph;
import org.ipsquad.apes.ui.ContextJGraph;
import org.ipsquad.apes.ui.FlowJGraph;
import org.ipsquad.apes.ui.ResponsabilityJGraph;
import org.ipsquad.apes.ui.WorkDefinitionJGraph;
import org.jgraph.JGraph;

import com.sun.image.codec.jpeg.JPEGCodec;
import com.sun.image.codec.jpeg.JPEGEncodeParam;
import com.sun.image.codec.jpeg.JPEGImageEncoder;

/**
 * Save a graph in a jpeg file
 *
 * @version $Revision: 1.4 $
 */
public class SaveJPEG
{
	private String mPath = null;
	private SpemGraphAdapter mAdapter;
	
	/**
	 * Constructor 
	 *
	 * @param path the abolute path to the save file
	 * @param adapter the SpemGraphAdapter containing the diagram to be saved
	 */ 
	public SaveJPEG(String path, SpemGraphAdapter adapter)
	{
		mPath=path;
		mAdapter=adapter;
	}	

	/**
	 * Constructor without path when you want to call directly save(OutputStream). 
	 *
	 * @param adapter the SpemGraphAdapter containing the diagram to be saved
	 */ 
	public SaveJPEG(SpemGraphAdapter adapter)
	{
		mAdapter=adapter;
	}	
	
	/**
	 * Set the path of the jpeg file
	 *
	 * @param path
 	 */
	public void setPath(String path)
	{
		mPath=path;
	}

	/**
	 * Get the path of the jpeg file
	 *
	 * @return the path of the jpeg file
 	 */
	public String getPath()
	{
		return mPath;
	}

	/**
	 * Set the adapter of the diagram to be save
	 *
	 * @param adapter
 	 */
	public void setGraphAdapter(SpemGraphAdapter adapter)
	{
		mAdapter=adapter;
	}

	public SpemGraphAdapter getGraphAdapter()
	{
		return mAdapter;
	}

	/**
	 * Save in jpeg the diagram in the file specified by  mPath variable
	 *
	 */
	public void save() throws IOException 
	{
		File file=new File(mPath);
		File temp=new File(file.getParent());
		
		if(temp.exists() && !temp.isDirectory())
		{
			File rename=new File(temp.getAbsolutePath()+".apesback");
			System.out.println(rename.getName());
			temp.renameTo(rename);
		}
		
		if(!temp.exists())
		{
			temp.mkdir();
		}
		
		OutputStream out = new java.io.FileOutputStream(file);
		
		save(out);
		
		out.close();
	}

	public void save(OutputStream out) throws IOException
	{
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
    	//frame.setVisible(false);
	
		int width = mGraph.getWidth();
		int height = mGraph.getHeight();
		
		if(width!=0 && height!=0)
		{
			//BufferedImage bimg = (java.awt.image.BufferedImage)mGraph.createImage(width,height);
			BufferedImage bimg = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
			Graphics g = bimg.getGraphics();
    			mGraph.paint(g);
			Graphics2D g2 = (Graphics2D)bimg.getGraphics();
			g2.setPaint(Color.white);
		
			JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
      		JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bimg);
      		param.setQuality(0.75f,true);
      		encoder.encode(bimg,param);
		}
	}
};
