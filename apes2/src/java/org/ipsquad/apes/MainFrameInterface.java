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


package org.ipsquad.apes;

import javax.swing.JDesktopPane;
import javax.swing.JTree;

import org.ipsquad.apes.ui.GraphFrame;
import org.ipsquad.apes.ui.ToolPalette;
import org.jgraph.graph.GraphModel;

/**
 * Interface required to be the main frame of the application
 *
 * @version $Revision: 1.5 $
 */
public interface MainFrameInterface
{
	public void setFilePath(String filePath);
	public JTree getTree();
	public GraphFrame getGraphFrame( GraphModel model );
	public JDesktopPane getDesktop();
	public void setToolPalette(ToolPalette toolPalette);
	public ToolPalette getToolPalette();
	public void openDiagram(GraphModel model);
	public void deleteDiagram(GraphModel model);
	public void setDefaultToolPalette();
}
