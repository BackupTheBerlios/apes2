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


package org.ipsquad.apes.adapters;
import java.io.File;

import javax.swing.JFileChooser;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.DefaultSpemVisitor;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.ui.ApesFrame;
/**
 * This visitor execute an action corresponding to the visted element.
 *
 * @version $Revision: 1.2 $
 */
public class ActionAssociater extends DefaultSpemVisitor
{
	Context context = Context.getInstance();
	
	protected void visitSpemDiagram(SpemDiagram diagram)
	{
		SpemGraphAdapter model = context.getProject().getGraphModel(diagram);
		context.getTopLevelFrame().openDiagram(model);
	}
	
	
	public void visitExternalDescription(ExternalDescription ed)
	{
		Element selected = (Element)Context.getInstance().getTopLevelFrame().getTree().getSelectionPath().getLastPathComponent();
		ExternalDescription extd = (ExternalDescription) selected;
		JFileChooser chooser = new JFileChooser();
		chooser.setSelectedFile( new File(extd.getContent()));
		if (chooser.showOpenDialog(((ApesFrame)Context.getInstance().getTopLevelFrame()).getContentPane())
		==JFileChooser.APPROVE_OPTION)
		{
			//ExternalDescription extd = (ExternalDescription) selected;
			extd.setName(chooser.getSelectedFile().getName());
			extd.setContent(chooser.getSelectedFile().getAbsolutePath());	
		}
	}
}
