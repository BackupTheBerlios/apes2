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
import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.adapters.SpemTreeAdapter;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.MonitoredTaskBase;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.TaskMonitorDialog;
import org.xml.sax.Attributes;
import org.xml.sax.SAXException;
import org.xml.sax.helpers.DefaultHandler;

import JSX.ObjIn;

/**
 *
 * @version $Revision: 1.14 $
 */
public class LoadProject extends MonitoredTaskBase 
{
	private File mFile = null;
	private TaskMonitorDialog mTask = null;
	private ResourceManager mResource = ResourceManager.getInstance();
	private ErrorManager mError = ErrorManager.getInstance();
	
	private Project mProject = new Project();
	
	public LoadProject(File file)
	{
		mFile = file;
	}
	
	public void setTask( TaskMonitorDialog task )
	{
		mTask = task;
	}
	
	protected Object processingTask() 
	{
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
			
			ZipInputStream zipFile = new ZipInputStream( new FileInputStream(new File(mFile.getAbsolutePath())));
			
			if( !loadComponent( zipFile ) )
			{
				loadInterfaces(zipFile, mProject.getProcess());
				print(mResource.getString("loadRebuild"));
			}
			else
			{	
				print(mResource.getString("loadRebuild"));
				mProject.getProcess().buildInterfaces();
			}
			
			Context.getInstance().setProject(mProject, mFile.getAbsolutePath());
			print(mResource.getString("loadSuccess"));
			mCloseOnFinish = true;
			
		}
		catch(Throwable t)
		{
			print(t.getMessage());
			print(mResource.getString("loadFailed"));
			t.printStackTrace();
		}
		mProject = null;
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
	protected boolean loadComponent(ZipInputStream projectZip) throws IOException, ClassNotFoundException
	{
		print(mResource.getString("loadSearchComponent"));
		
		DataInputStream data = findData("Component.xml");
			
		SpemTreeAdapter adapter = (SpemTreeAdapter)Context.getInstance().getTopLevelFrame().getTree().getModel(); 	

		if( data != null )
		{
			print(mResource.getString("loadComponent"));
			ObjIn in = new ObjIn(data);
			Vector v = (Vector)in.readObject();

			if( v.size() == 4 )
			{	
				adapter.setRoot((ApesTreeNode)v.get(2));
				mProject.setProcess((ApesProcess)((ApesTreeNode)adapter.getRoot()).getUserObject());
				mProject.getProcess().addModelElement((ProcessComponent)v.get(0));
				mProject.setDiagramMap((HashMap)v.get(1));
				
				Activity ref = (Activity)v.get(3);
				int nb = new Integer(ref.getName().substring(6)).intValue();
				Element.setNoNameCounter(nb);	
				Element.setNoID(ref.getID());
				projectZip.close();
		
				checkVersion();
				
				print(mResource.getString("loadComponentSuccess"));
				return true;
			}
		}

		print(mResource.getString("loadComponentFailed"));
		mError.printKey("loadComponentFailed");
		projectZip.close();
		return false;
	}

	protected void checkVersion() 
	{
		boolean needUpdate = false;
		ProcessComponent component = mProject.getProcess().getComponent();
		
		if(component.getVersion() != null)
		{
			int comp = mResource.getString("Version").compareTo(component.getVersion()); 
			if(comp < 0)
			{
				print(mResource.getString("warningNewerVersion"));
				mError.printKey("warningNewerVersion");
				mError.println("\t"+mResource.getString("warningCurrentVersion")+" -> "+mResource.getString("Version"));
				mError.println("\t"+mResource.getString("warningFoundVersion")+" -> "+component.getVersion());
			}
			else if(comp > 0)
			{
				print(mResource.getString("warningOlderVersion"));
				mError.printKey("warningOlderVersion");
				mError.println("\t"+mResource.getString("warningCurrentVersion")+" -> "+mResource.getString("Version"));
				mError.println("\t"+mResource.getString("warningFoundVersion")+" -> "+component.getVersion());
				needUpdate = true;
			}
		}
		else
		{
			print(mResource.getString("warningOlderVersion"));	
			mError.printKey("warningOlderVersion");
			mError.println("\t"+mResource.getString("warningCurrentVersion")+" -> "+mResource.getString("Version"));
			mError.println("\t"+mResource.getString("warningFoundVersion")+" -> < 2.5.1");
			needUpdate = true;
		}

		if(needUpdate)
		{
			mError.printKey("updatingToCurrentVersion");
			updateToCurrentVersion(component);
		}
		//always update the component version
		component.updateVersion();
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
	protected void processInterfaces(DataInputStream data, ApesProcess ap)
	throws IOException, SAXException, ParserConfigurationException
	{
		print(mResource.getString("loadSaxInit"));
		SAXParserFactory factory = SAXParserFactory.newInstance();
		SAXParser analyzer = factory.newSAXParser();
		InterfacesHandler handler = new InterfacesHandler();
		print(mResource.getString("loadParsing"));
		analyzer.parse( data, handler );
		
		ApesProcess.ProvidedInterface pi = new ApesProcess.ProvidedInterface(ConfigManager.getInstance().getProperty("Provided"));
		ApesProcess.RequiredInterface ri = new ApesProcess.RequiredInterface(ConfigManager.getInstance().getProperty("Required"));
		
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
	protected void print( String msg )
	{
		setMessage(msg);
		if( mTask != null )
		{
			mTask.forceRefresh();
		}
	}
	
	/**
	 * Recursives method to update all elements of a component
	 * 
	 * @param element the element to update
	 */
	protected void updateToCurrentVersion(Element element)
	{
		if(element instanceof IPackage)
		{
			IPackage p = (IPackage)element;
		
			for (int i = 0; i < p.modelElementCount(); i++) 
			{
				updateToCurrentVersion(p.getModelElement(i));
			}
		}
		element.updateToCurrentVersion();
	}
	
	protected class InterfacesHandler extends DefaultHandler
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
