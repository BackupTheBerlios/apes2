/*
 * APES is a Process Engineering Software
 * Copyright (C) 2004 IPSquad
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

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.Map;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.utils.ErrorManager;

import JSX.ObjOut;

public class SaveProject
{
	private final static String IMAGE_PATH="images";
	
	private ZipOutputStream mZipFile;
	
	public SaveProject(ZipOutputStream zipFile)
	{
		mZipFile = zipFile;
	}
	
	public void save() throws IOException
	{
		Project project = Context.getInstance().getProject();
				
		saveComponent();
		saveInterfaces();
		createImagesDirectory();
		saveImageAndMap();
	}
	
	private static String normalizeName(String name)
	{
		name=name.replace(' ','_');
		name=name.replace('\'','_');
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
	
	public void createImagesDirectory()
	{
		String entryDir = IMAGE_PATH + System.getProperty("file.separator");
		ZipEntry entryZip = new ZipEntry(entryDir);

		try
		{
			mZipFile.putNextEntry(entryZip);
			mZipFile.closeEntry();
		}
		catch(Throwable t)
		{
			t.printStackTrace();
			ErrorManager.getInstance().display("errorTitleSaveProcess", "errorOpenProcess");
		}
	}
	
	private void saveImageAndMap() throws IOException
	{
		ZipEntry entryZip;
		ZipEntry entryDir = null;
		DataOutputStream data = null;
		
		Map diagrams = Context.getInstance().getProject().getDiagramMap();
		SpemGraphAdapter adapter = null;
		SaveJpegWithMap map = null ;
		SaveJPEG image = null;
		
		Iterator it = diagrams.entrySet().iterator();

		while( it.hasNext() )
		{
			Map.Entry entry = (Map.Entry)it.next();

			adapter = (SpemGraphAdapter)entry.getValue();

			entryZip = new ZipEntry(IMAGE_PATH+System.getProperty("file.separator")+normalizeName(adapter.getName())+".jpeg");
			mZipFile.putNextEntry(entryZip);
			data = new DataOutputStream( new BufferedOutputStream(mZipFile) );
			image = new SaveJPEG(adapter);
			image.save(data);
			mZipFile.closeEntry();
			
			entryZip = new ZipEntry(IMAGE_PATH+System.getProperty("file.separator")+normalizeName(adapter.getName())+".map");
			mZipFile.putNextEntry(entryZip);
			data = new DataOutputStream( new BufferedOutputStream(mZipFile) );
			map = new SaveJpegWithMap(adapter);
			data.writeChars(map.createHtmlMap());
			data.flush();
			mZipFile.closeEntry();
		}
	}
	
	private void saveInterfaces() throws IOException
	{
		ZipEntry entryZip;
		entryZip = new ZipEntry( "Interfaces.xml" );
		mZipFile.putNextEntry( entryZip );	
		DataOutputStream data = new DataOutputStream(mZipFile);
		
		data.writeChars("<?apes2 version=\"0.1\"?>\n");
		data.writeChars("<Interfaces>\n");
		data.writeChars("\t<ProvidedInterface>\n");
		
		ApesProcess ap = Context.getInstance().getProject().getProcess();
		ApesProcess.Interface in = ap.getProvidedInterface();
		for( int i = 0; i < in.modelElementCount(); i++ )
		{
			WorkProductRef ref = (WorkProductRef)in.getModelElement(i);
			data.writeChars("\t\t<WorkProductRef tag=\"");
			data.writeChars(new Long(ref.getTag()).toString());
			data.writeChars("\">\n");
			data.writeChars("\t\t\t<WorkProduct name=\"");				
			data.writeChars(ref.getReference().getName()+"\"/>\n");
			data.writeChars("\t\t</WorkProductRef>\n");
		}
		
		data.writeChars("\t</ProvidedInterface>\n");
		
		data.writeChars("\t<RequiredInterface>\n");
		
		in = ap.getRequiredInterface();
		for( int i = 0; i < in.modelElementCount(); i++ )
		{
			WorkProductRef ref = (WorkProductRef)in.getModelElement(i);
			data.writeChars("\t\t<WorkProductRef tag=\"");
			data.writeChars(new Long(ref.getTag()).toString());
			data.writeChars("\">\n");
			data.writeChars("\t\t\t<WorkProduct name=\"");				
			data.writeChars(ref.getReference().getName()+"\"/>\n");
			data.writeChars("\t\t</WorkProductRef>\n");
		}
		
		data.writeChars("\t</RequiredInterface>\n");
		data.writeChars("</Interfaces>\n");
		
		mZipFile.closeEntry();
	}
	
	private void saveComponent() throws IOException
	{
		ZipEntry entryZip = new ZipEntry("Component.xml");
		mZipFile.putNextEntry(entryZip);
		DataOutputStream data = new DataOutputStream( new BufferedOutputStream(mZipFile) );
		ObjOut out = new ObjOut( data );
		
		Vector  v = new Vector();
		Project project = Context.getInstance().getProject();
		
		project.getProcess().getComponent().setParent( null );
		v.add(project.getProcess().getComponent());
		v.add(project.getDiagramMap());
		v.add(((SpemTreeAdapter)Context.getInstance().getTopLevelFrame().getTree().getModel()).getRoot());
		
		out.writeObject(v);
		mZipFile.closeEntry();
		
		project.getProcess().getComponent().setParent( project.getProcess() );
	}
}
