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


package org.ipsquad.apes.ui;

import java.awt.event.ActionEvent;

import javax.swing.ButtonGroup;
import javax.swing.JToggleButton;
import javax.swing.JToolBar;

import org.ipsquad.apes.ui.tools.DefaultTool;
import org.ipsquad.apes.ui.tools.Tool;
import org.ipsquad.apes.ui.tools.ToolListener;
import org.ipsquad.utils.IconManager;
import org.ipsquad.utils.ResourceManager;
import org.jgraph.JGraph;


/**
 * Base class for the graph editing toolpalettes
 *
 * @version $Revision: 1.1 $
 */
public class ToolPalette extends JToolBar implements ToolListener
{
	private JGraph mGraph;
	private ButtonGroup mButtonGroup = new ButtonGroup();
	private ToolButton mDefaultButton;
	private Tool mCurrentTool = null;


	
	public ToolPalette()
	{
		super();

		setFloatable(false);
		
		mDefaultButton = createToolButton(new DefaultTool(), "icons/PaletteArrow.gif", "paletteArrow");
		add(mDefaultButton);
		addSeparator();
		
		/*mDefaultButton = createToolButton(new DefaultTool(), "icons/PaletteArrow.gif", "paletteArrow");
		add(mDefaultButton);
		addSeparator();*/
	}
	

	/**
	 * Create a new button for the palette
	 * The button is not added to the tool palette it is only created
	 *
	 * @param tool the tool associated to the future button
	 * @param icon the path for the icon file
	 * @param toolTip the tool tip string to display
	 * @return the new tool button
	 */
	protected ToolButton createToolButton(Tool tool, String icon, String toolTip)
	{
		ToolButton button = new ToolButton(tool, icon, toolTip);
		tool.addToolListener(this);
		return button;
	}

	/**
	 * Add a new button to the palette
	 * The button is created and added to the tool palette
	 *
	 * @param tool the tool associated to the future button
	 * @param icon the path for the icon file
	 * @param toolTip the tool tip string to display
	 */
	public void addToolButton(Tool tool, String icon, String toolTip)
	{
		add(createToolButton(tool, icon, toolTip));
	}

	/**
	 * Get the default button of the palette
	 *
	 * @return default button
	 */
	public JToggleButton getDefaultButton()
	{
		return mDefaultButton;
	}

	/**
	 * Install the tool palette for the given graph widget
	 * @param graph
	 */
	public void install(JGraph graph)
	{
		mGraph = graph;
		mButtonGroup.setSelected(mDefaultButton.getModel(), true);
		mDefaultButton.repaint();
		mCurrentTool = mDefaultButton.getTool();
		mCurrentTool.install(mGraph);
	}


	/**
	 * Uninstall the tool palette from its graph
	 */
	public void uninstall()
	{
		if(mCurrentTool!=null)
		{
			mCurrentTool.uninstall(mGraph);
		}
	}


	public void toolStarted(Tool tool)
	{
	
	}
	

	/**
	 * Reset the tool palette to the default tool
	 */
	public void toolFinished(Tool tool)
	{
		if(!tool.isStable())
		{
			tool.uninstall(mGraph);
			mDefaultButton.getTool().install(mGraph);
			mCurrentTool=mDefaultButton.getTool();
			mButtonGroup.setSelected(mDefaultButton.getModel(), true);
			mDefaultButton.repaint();
		}
	}

	
	private class ToolButton extends JToggleButton
	{
		private Tool mTool;
		
		public ToolButton(Tool tool, String icon, String toolTip)
		{
			super(IconManager.getInstance().getIcon(icon));

			setToolTipText(ResourceManager.getInstance().getString(toolTip));
			mTool = tool;

			mButtonGroup.add(this);
		}

		public Tool getTool()
		{
			return mTool;
		}

		protected void fireActionPerformed(ActionEvent e)
		{
			if(mGraph != null)
			{
				if(mCurrentTool!=null)
				{
					mCurrentTool.uninstall(mGraph);
				}

				mTool.install(mGraph);
				mCurrentTool = mTool;
			}

			super.fireActionPerformed(e);
		}
	}

}
