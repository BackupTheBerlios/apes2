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

package org.ipsquad.apes.model.spem.basic;

import java.io.File;

import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.PresentationElement;

/**
 * Describe presentation elements that are external to the model
 *
 * @version $Revision: 1.2 $
 */
public class ExternalDescription extends PresentationElement
{
	private String mContent;
	private String mMedium;
	private String mLanguage;
	
	public ExternalDescription(String name)
	{
		super(name);
	}

	public ExternalDescription()
	{
	
	}

	public void visit(SpemVisitor visitor)
	{
		visitor.visitExternalDescription(this);
	}
	
	public void setContent(String path)
	{
		mContent=path;
		if(mContent.lastIndexOf(File.separatorChar) != -1)
		{
			setName(mContent.substring(mContent.lastIndexOf(File.separatorChar)+1,mContent.length()));
		}
		else
		{
			setName(mContent);
		}
		
	}
	
	public String getContent()
	{
		return mContent;
	}
	
}
