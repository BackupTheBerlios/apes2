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

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;

import org.ipsquad.apes.Context;
import org.ipsquad.apes.Project;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.MonitoredTaskBase;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.TaskMonitorDialog;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import JSX.ObjIn;

/**
 *
 * @version $Revision: 1.1 $
 */
public class LoadProject extends MonitoredTaskBase 
{
	private File mFile = null;
	private TaskMonitorDialog mTask = null;
	private ResourceManager mResource = ResourceManager.getInstance();
	
	public LoadProject(File file)
	{
		mFile = file;
	}
	
	public void setTask( TaskMonitorDialog task )
	{
		mTask = task;
	}
	
	protected Object processingTask() {
		launch();
		return null;
	}
	
	/**
	 * Start loading the project
	 *
	 */
	protected void launch()
	{
		try
		{
			boolean hasComponent = true;
			
			Project p = new Project();
			ApesProcess ap = new ApesProcess("Project");
			
			ZipInputStream zipFile = new ZipInputStream( new FileInputStream(new File(mFile.getAbsolutePath())));
			
			if( !loadComponent( zipFile, p, ap ) )
			{
				hasComponent = false;
			}
			
			loadInterfaces(zipFile, ap);
			
			print(mResource.getString("loadRebuild"));
			
			if( hasComponent )
			{	
				ap.checkInterfaces();
			}
			
			p.setProcess(ap);
			
			Context.getInstance().setProject(p, mFile.getAbsolutePath());
			print(mResource.getString("loadSuccess"));
		}
		catch(Throwable t)
		{
			print(t.getMessage());
			print(mResource.getString("loadFailed"));
			t.printStackTrace();
		}
	}

	/**
	 * Load the component in the process giving in parameter.
	 * 
	 * @param projectZip the zip containing the Component.xml file
	 * @param p the project where to store the diagrams adapters
	 * @param ap the process where to store the component
	 * @return true if successfull, false otherwise
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	protected boolean loadComponent(ZipInputStream projectZip, Project p, ApesProcess ap) throws IOException, ClassNotFoundException
	{
		print(mResource.getString("loadSearchComponent"));
		
		DataInputStream data = findData("Component.xml");
			
			
		if( data != null )
		{
			print(mResource.getString("loadComponent"));
			ObjIn in = new ObjIn(data);
			Vector v = (Vector)in.readObject();
			ap.addModelElement((ProcessComponent)v.get(0));
			p.setDiagramMap((HashMap)v.get(1));
			projectZip.close();
				
			print(mResource.getString("loadComponentSuccess"));
			return true;
		}
		else
		{	
			print(mResource.getString("loadComponentFailed"));
			projectZip.close();
			return false;
		}
	}
	
	/**
	 * Load the interfaces in the process giving in parameter
	 * 
	 * @param projectZip the zip containing the Interfaces.xml file
	 * @param ap the process where to store the interfaces
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	protected void loadInterfaces(ZipInputStream projectZip, ApesProcess ap) throws IOException, SAXException, ParserConfigurationException
	{	
		print(mResource.getString("loadSearchInterfaces"));
		
		DataInputStream data = findData("Interfaces.xml");
			
		if( data != null)
		{
			print(mResource.getString("loadInterfaces"));
			processInterfaces(data, ap);
			print(mResource.getString("loadInterfacesSuccess"));
		}
		else
		{
			//print("failed to find Interfaces.xml");
			projectZip.close();
			throw new IOException(mResource.getString("loadInterfacesFailed"));
		}
	}
	
	/**
	 * Parses the file containing the interfaces of the component.
	 * 
	 * @param data
	 * @param ap the process where you want to put the interfaces
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	public void processInterfaces(DataInputStream data, ApesProcess ap)
	throws IOException, SAXException, ParserConfigurationException
	{
		print(mResource.getString("loadSaxInit"));
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser analyzer = factory.newSAXParser();
		InterfacesHandler handler = new InterfacesHandler();
		print(mResource.getString("loadParsing"));
		analyzer.parse( data, handler );
		
		ApesProcess.ProvidedInterface pi = new ApesProcess.ProvidedInterface("provided");
		ApesProcess.RequiredInterface ri = new ApesProcess.RequiredInterface("required");
		
		Vector names = handler.getProvidedProductNames();
		for( int i = 0; i < names.size(); i++ )
		{
			pi.addModelElement( new WorkProductRef( new WorkProduct((String)names.get(i))));
		}
		names = handler.getRequiredProductNames();
		for( int i = 0; i < names.size(); i++ )
		{
			ri.addModelElement( new WorkProductRef( new WorkProduct((String)names.get(i))));
		}
		
		ap.addModelElement(pi);
		ap.addModelElement(ri);
	}
	
	/**
	 * Search and open the file given by fileName in projectZip.
	 * 
	 * @param projectZip the zip where you want to open a file
	 * @param fileName the file to open
	 * @return the DataInputStream containing the file
	 * @throws IOException
	 * @throws SAXException
	 * @throws ParserConfigurationException
	 */
	private DataInputStream findData(String fileName) throws IOException
	{	
		ZipInputStream zipFile = new ZipInputStream( new FileInputStream(new File(mFile.getAbsolutePath())));
		
		ZipEntry zipEntry = zipFile.getNextEntry();
		
		while( zipEntry != null )
		{
			DataInputStream data = new DataInputStream( new BufferedInputStream(zipFile) );
			
			
			if( zipEntry.getName().equals(fileName) )
			{
				return data;
			}
			else
			{
				zipEntry = zipFile.getNextEntry();
			}
		}
		zipFile.close();
		return null;
	}
	
	/**
	 * Print a new message to the TaskMonitorDialog
	 * 
	 * @param msg
	 */
	private void print( String msg )
	{
		setMessage(msg);
		if( mTask != null )
		{
			mTask.forceRefresh();
		}
	}
	
	private class InterfacesHandler extends DefaultHandler
	{
		private boolean mIsProvidedInterface = true;
		private Vector mProvidedProductNames = new Vector();
		private Vector mRequiredProductNames = new Vector();
		
		public InterfacesHandler()
		{
			super();
		}
		
		public Vector getProvidedProductNames()
		{
			return mProvidedProductNames;
		}
		
		public Vector getRequiredProductNames()
		{
			return mRequiredProductNames;
		}
		
		public void startElement (String uri, String localName,
								  String qName, Attributes attributes) throws SAXException
		{
			if(qName=="RequiredInterface")
			{	
				mIsProvidedInterface = false;
			}
			else if(qName=="WorkProduct")
			{
				if( mIsProvidedInterface )
				{	
					mProvidedProductNames.add(attributes.getValue(0));
				}
				else
				{
					mRequiredProductNames.add(attributes.getValue(0));
				}
			}
		}	
	}
}
