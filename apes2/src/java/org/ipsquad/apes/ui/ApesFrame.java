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


package org.ipsquad.apes.ui;

import java.awt.BorderLayout;
import java.awt.Rectangle;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;

import javax.swing.JDesktopPane;
import javax.swing.JFrame;
import javax.swing.JInternalFrame;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JToolBar;
import javax.swing.JTree;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.MainFrameInterface;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ContextGraphAdapter;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ResponsabilityGraphAdapter;
import org.ipsquad.apes.adapters.WorkDefinitionGraphAdapter;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.Debug;
import org.ipsquad.utils.ResourceManager;
import org.jgraph.graph.GraphModel;


/**
 * Main frame for the GUI 
 *
 * @version $Revision: 1.6 $
 */
public class ApesFrame extends JFrame implements MainFrameInterface
{
	private JDesktopPane mDesktop = new JDesktopPane();
	private JTree mTree;
	private String mFilePath;
	private JPanel mPanel = new JPanel();
	private ApesErrorSplitPane mErrorPane = new ApesErrorSplitPane(mPanel);
	private ToolPalette mToolPalette;
	private final ToolPalette mDefaultToolPalette = new ToolPalette();

	public ApesFrame()
	{
		super(ResourceManager.getInstance().getString("Title"));
		mTree = new ApesTree();
		init();
	}

	public ApesFrame(JTree tree)
	{
		super(ResourceManager.getInstance().getString("Title"));
		mTree = tree;
		init();	
	}

	private void init()
	{
		addWindowListener(new ApesFrameListener());

		mDefaultToolPalette.setEnabled(false);
		mDefaultToolPalette.getDefaultButton().setEnabled(false);
		mToolPalette = mDefaultToolPalette;
		mToolPalette.setOrientation(JToolBar.VERTICAL);
		
		mPanel.setLayout(new BorderLayout());
		mPanel.add(mDesktop, BorderLayout.CENTER);
		mPanel.add(mToolPalette, BorderLayout.WEST);

		JSplitPane frame_split = new JSplitPane();
		frame_split.setOneTouchExpandable(true);
		//frame_split.setDividerLocation(150);
		frame_split.setDividerLocation(200);		
		frame_split.add(new JScrollPane(mTree), JSplitPane.LEFT);
		frame_split.add(mErrorPane, JSplitPane.RIGHT);
		setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);

		setJMenuBar(new ApesMenuBar());
		getContentPane().add(new ApesToolBar(), BorderLayout.NORTH);
		getContentPane().add(frame_split, BorderLayout.CENTER);
		
		Rectangle r = getGraphicsConfiguration().getBounds();
		setBounds(r.x+10, r.y+10, r.width*5/6, r.height*5/6);
		if(ConfigManager.getInstance().getProperty("ErrorPanelTitleyes").equals("false"))
		{
			mErrorPane.setDividerLocation(r.height*5/6);
		}
	}

/// @name Accessors
//@{
	public String getFilePath()
	{
		return mFilePath;
	}

	public void setFilePath(String filePath)
	{
		mFilePath = filePath;
		if(filePath == null || filePath.equals(""))
		{
			setTitle(ResourceManager.getInstance().getString("Title"));
		}
		else
		{
			setTitle(ResourceManager.getInstance().getString("Title")
			+" - "+filePath);
		}
	}
	
	public JTree getTree()
	{
		return mTree;
	}

	public JDesktopPane getDesktop()
	{
		return mDesktop;
	}
	
	public void setToolPalette(ToolPalette toolPalette)
	{
		mPanel.remove(mToolPalette);
		toolPalette.setOrientation(JToolBar.VERTICAL);
		mPanel.add(toolPalette, BorderLayout.WEST);
		mToolPalette = toolPalette;
		mPanel.repaint();
	}

	public ToolPalette getToolPalette()
	{
		return mToolPalette;
	}


//@}

	public GraphFrame getGraphFrame( GraphModel model )
	{
		JInternalFrame[] tab = mDesktop.getAllFrames();

		for(int i=0;i<tab.length;i++)
		{
			GraphFrame g = (GraphFrame)tab[i];

			if(model==g.getGraphModel())
			{
				return g;
			}
		}
		return null;
	}
	
	public void openDiagram(GraphModel model)
	{
		JInternalFrame[] tab = mDesktop.getAllFrames();

		for(int i=0;i<tab.length;i++)
		{
			GraphFrame g = (GraphFrame)tab[i];

			if(model==g.getGraphModel())
			{
				try
				{
					if(mDesktop.getSelectedFrame()!=null)
					{
						mDesktop.getSelectedFrame().setSelected(false);
						mDesktop.setSelectedFrame(null);
					}

					g.setIcon(false);
					g.show();
					g.toFront();
					g.setSelected(true);
					mDesktop.setSelectedFrame(g);
				}
				catch(Throwable t)
				{
					if(Debug.enabled) t.printStackTrace();
				}
				return;
			}
		}

		GraphFrame w;
		
		if(model instanceof FlowGraphAdapter)
		{
			FlowGraphAdapter adapter = (FlowGraphAdapter) model;
			w = new FlowGraphFrame(adapter);
		}
		else if(model instanceof ResponsabilityGraphAdapter)
		{
			ResponsabilityGraphAdapter adapter = (ResponsabilityGraphAdapter) model;
			w = new ResponsabilityGraphFrame(adapter);
		}
		else if(model instanceof ActivityGraphAdapter)
		{
			ActivityGraphAdapter adapter = (ActivityGraphAdapter) model;
			w = new ActivityGraphFrame(adapter);
		}
		else if(model instanceof ContextGraphAdapter)
		{
			ContextGraphAdapter adapter = (ContextGraphAdapter) model;
			w = new ContextGraphFrame(adapter);
		}
		else if(model instanceof WorkDefinitionGraphAdapter)
		{
			WorkDefinitionGraphAdapter adapter = (WorkDefinitionGraphAdapter) model;
			w = new WorkDefinitionGraphFrame(adapter);
		}
		else
		{
			return;
		}
		
		Rectangle bounds = mDesktop.getGraphicsConfiguration().getBounds();
		w.setBounds(bounds.x+10,bounds.y+10,bounds.width/3,bounds.height/3);
		mDesktop.add(w);
		w.show();
	}
	
	public void deleteDiagram(GraphModel model)
	{
		JInternalFrame[] tab = mDesktop.getAllFrames();

		for(int i=0;i<tab.length;i++)
		{
			GraphFrame g = (GraphFrame)tab[i];

			if(model==g.getGraphModel())
			{
				g.dispose();
				return;
			}
		}
	}

	/**
	 * Force the displayed toolpalette to the default toolpalette
	 */
	public void setDefaultToolPalette()
	{
		setToolPalette(mDefaultToolPalette);
	}


	private class ApesFrameListener extends WindowAdapter
	{
		/**
		 * Allows to intercept the close event to call the QuitAction
		 */
		public void windowClosing(WindowEvent e)
		{
			Context.getInstance().getAction("Quit").actionPerformed(null);
		}
	}
}
