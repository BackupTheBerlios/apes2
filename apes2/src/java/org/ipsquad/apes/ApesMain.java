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

import java.io.File;
import java.util.Locale;
import java.util.Properties;

import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.apes.processing.LoadProject;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.actions.AboutAction;
import org.ipsquad.apes.ui.actions.AddToModelAction;
import org.ipsquad.apes.ui.actions.AlignHAction;
import org.ipsquad.apes.ui.actions.AlignVAction;
import org.ipsquad.apes.ui.actions.BiggerAction;
import org.ipsquad.apes.ui.actions.ChangeBoldAction;
import org.ipsquad.apes.ui.actions.ChangeColorAction;
import org.ipsquad.apes.ui.actions.ChangeItalicAction;
import org.ipsquad.apes.ui.actions.CopyAction;
import org.ipsquad.apes.ui.actions.CutAction;
import org.ipsquad.apes.ui.actions.InteractWithModelAction;
import org.ipsquad.apes.ui.actions.NewAction;
import org.ipsquad.apes.ui.actions.OpenAction;
import org.ipsquad.apes.ui.actions.PasteAction;
import org.ipsquad.apes.ui.actions.PreferencesAction;
import org.ipsquad.apes.ui.actions.PresentationAction;
import org.ipsquad.apes.ui.actions.PrintDiagramAction;
import org.ipsquad.apes.ui.actions.QuitAction;
import org.ipsquad.apes.ui.actions.RedoAction;
import org.ipsquad.apes.ui.actions.RemoveAction;
import org.ipsquad.apes.ui.actions.RemoveFromModelAction;
import org.ipsquad.apes.ui.actions.SaveAction;
import org.ipsquad.apes.ui.actions.SaveAsAction;
import org.ipsquad.apes.ui.actions.SaveAsImageAction;
import org.ipsquad.apes.ui.actions.SmallerAction;
import org.ipsquad.apes.ui.actions.TreeRenameAction;
import org.ipsquad.apes.ui.actions.UndoAction;
import org.ipsquad.apes.ui.actions.ValidateAction;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;
import org.ipsquad.utils.TaskMonitorDialog;

/**
 * APES' main class
 *
 * This class contains the main method of the application.
 *
 * @version $Revision: 1.26 $
 */
public class ApesMain
{
	public static Properties createDefaultProperties()
	{
		ResourceManager resManager = ResourceManager.getInstance();
		Properties properties = new Properties();
		properties.setProperty("Project","model");
		properties.setProperty("Component","component");
		properties.setProperty("ContextDiagram","component interfaces");
		properties.setProperty("WorkDefinitionDiagram","flow diagram");
		properties.setProperty("FlowDiagram","flow diagram");
		properties.setProperty("ActivityDiagram","activity diagram");
		properties.setProperty("ResponsabilityDiagram","product responsability");
		properties.setProperty("Interface","interface::");
		properties.setProperty("Reference","ref::");
		properties.setProperty("Provided","provided");
		properties.setProperty("Required","required");
		
		properties.setProperty("DiagramsTitleforegroundred","0");
		properties.setProperty("DiagramsTitleforegroundgreen","0");
		properties.setProperty("DiagramsTitleforegroundblue","0");
		properties.setProperty("DiagramsTitlebackgroundred","255");
		properties.setProperty("DiagramsTitlebackgroundgreen","255");
		properties.setProperty("DiagramsTitlebackgroundblue","255");
		properties.setProperty("DiagramsTitlebold","false");
		properties.setProperty("DiagramsTitleitalic","false");
		properties.setProperty("ActivityTitleforegroundred","0");
		properties.setProperty("ActivityTitleforegroundgreen","0");
		properties.setProperty("ActivityTitleforegroundblue","0");
		properties.setProperty("ActivityTitlebackgroundred","255");
		properties.setProperty("ActivityTitlebackgroundgreen","255");
		properties.setProperty("ActivityTitlebackgroundblue","255");
		properties.setProperty("ActivityTitlebold","false");
		properties.setProperty("ActivityTitleitalic","false");
		properties.setProperty("RoleTitleforegroundred","0");
		properties.setProperty("RoleTitleforegroundgreen","0");
		properties.setProperty("RoleTitleforegroundblue","0");
		properties.setProperty("RoleTitlebackgroundred","255");
		properties.setProperty("RoleTitlebackgroundgreen","255");
		properties.setProperty("RoleTitlebackgroundblue","255");
		properties.setProperty("RoleTitlebold","false");
		properties.setProperty("RoleTitleitalic","false");
		properties.setProperty("WorkproductTitleforegroundred","0");
		properties.setProperty("WorkproductTitleforegroundgreen","0");
		properties.setProperty("WorkproductTitleforegroundblue","0");
		properties.setProperty("WorkproductTitlebackgroundred","255");
		properties.setProperty("WorkproductTitlebackgroundgreen","255");
		properties.setProperty("WorkproductTitlebackgroundblue","255");
		properties.setProperty("WorkproductTitlebold","false");
		properties.setProperty("WorkproductTitleitalic","false");
		properties.setProperty("StateTitleforegroundred","0");
		properties.setProperty("StateTitleforegroundgreen","0");
		properties.setProperty("StateTitleforegroundblue","0");
		properties.setProperty("StateTitlebackgroundred","255");
		properties.setProperty("StateTitlebackgroundgreen","255");
		properties.setProperty("StateTitlebackgroundblue","255");
		properties.setProperty("StateTitlebold","false");
		properties.setProperty("StateTitleitalic","false");
		properties.setProperty("WorkDefinitionTitleforegroundred","0");
		properties.setProperty("WorkDefinitionTitleforegroundgreen","0");
		properties.setProperty("WorkDefinitionTitleforegroundblue","0");
		properties.setProperty("WorkDefinitionTitlebackgroundred","255");
		properties.setProperty("WorkDefinitionTitlebackgroundgreen","255");
		properties.setProperty("WorkDefinitionTitlebackgroundblue","255");
		properties.setProperty("WorkDefinitionTitlebold","false");
		properties.setProperty("WorkDefinitionTitleitalic","false");
		properties.setProperty("GuardTitleforegroundred","0");
		properties.setProperty("GuardTitleforegroundgreen","0");
		properties.setProperty("GuardTitleforegroundblue","0");
		properties.setProperty("GuardTitlebackgroundred","255");
		properties.setProperty("GuardTitlebackgroundgreen","255");
		properties.setProperty("GuardTitlebackgroundblue","255");
		properties.setProperty("GuardTitlebold","false");
		properties.setProperty("GuardTitleitalic","false");
		properties.setProperty("NotesTitleforegroundred","0");
		properties.setProperty("NotesTitleforegroundgreen","0");
		properties.setProperty("NotesTitleforegroundblue","0");
		properties.setProperty("NotesTitlebackgroundred","255");
		properties.setProperty("NotesTitlebackgroundgreen","255");
		properties.setProperty("NotesTitlebackgroundblue","204");
		properties.setProperty("NotesTitlebold","false");
		properties.setProperty("NotesTitleitalic","false");
		properties.setProperty("ToolPresentationTitledefaultPath","");
		properties.setProperty("WorkspaceTitledefaultPath","");
		properties.setProperty("PicturesTitledefaultPath","");
		properties.setProperty("ErrorPanelTitleyes","true");
		properties.setProperty("Language",Locale.getDefault().getLanguage());

		return properties;
	}

	public static void main(String[] args)
	{
		ConfigManager.init(createDefaultProperties());
		Locale locale = new Locale(ConfigManager.getInstance().getProperty("Language"));
		ResourceManager.setResourceFile("resources/Apes",locale);
		Locale.setDefault(locale);

		Context context = Context.getInstance();

		initActions(context);

		final ApesFrame f = new ApesFrame();
		context.setTopLevelFrame(f);
		ErrorManager.getInstance().setOwner(f.getContentPane());
		
	
		Project project = new Project();
		
		context.setProject(project);
		
		f.show();
		
		
		//Open a file given in parameter
		if(args.length>0)
		{
			if(args[0].endsWith("."+ResourceManager.getInstance().getString("apesFileExtension")) ||
					args[0].endsWith("."+ResourceManager.getInstance().getString("apesDosFileExtension")))
			{
			  try
			  {
				
			  	File mFile = new File(args[0]);
				
				LoadProject mMonitor = new LoadProject(mFile);
				
				ApesFrame parent = (ApesFrame)Context.getInstance().getTopLevelFrame();
				
				TaskMonitorDialog mTask = new TaskMonitorDialog(parent,mMonitor);
				mTask.setName(ResourceManager.getInstance().getString("titleLoading"));
				mTask.setLocation(parent.getWidth()/2-mTask.getWidth() / 2,parent.getHeight()/2-mTask.getHeight()/2);
				
				mMonitor.setTask(mTask);
				
				mTask.show();
				mTask.hide();
			 }
			
			 catch(Throwable t)
			 {
			 	t.printStackTrace();
				ErrorManager.getInstance().display("errorTitleOpenProcess", "errorOpenProcess");
		 	 } 
			}
			else
			{
				ErrorManager.getInstance().println(args[0]+" : "+ResourceManager.getInstance().getString("errorOpenFileNameInvalid"));	
			}
		}
	}
	public static void initActions(Context context)
	{
		context.registerAction("New", new NewAction());
		context.registerAction("Open", new OpenAction());
		//context.registerAction("Close", new CloseAction());
		context.registerAction("Save", new SaveAction());
		context.registerAction("SaveAs", new SaveAsAction());
		context.registerAction("Quit", new QuitAction());
		
		context.registerAction("Undo", new UndoAction());
		context.registerAction("Redo", new RedoAction());		

		context.registerAction("Cut", new CutAction());
		context.registerAction("Copy", new CopyAction());
		context.registerAction("Paste", new PasteAction());
		context.registerAction("Remove", new RemoveAction());
		
		context.registerAction("AlignH", new AlignHAction());
		context.registerAction("AlignV", new AlignVAction());
		
		context.registerAction("Bigger", new BiggerAction());
		context.registerAction("Smaller", new SmallerAction());
		context.registerAction("Preferences",new PreferencesAction());
		
		context.registerAction("Validate", new ValidateAction());
		context.registerAction("ToolPresentation", new PresentationAction());
		//context.registerAction("Generate", new GenerateWebSiteAction());
		
		context.registerAction("ChangeForeground", new ChangeColorAction("changeForeground","ChangeForeground"));
		context.registerAction("ChangeBackground", new ChangeColorAction("changeBackground","ChangeBackground"));
		context.registerAction("ChangeItalic", new ChangeItalicAction("changeItalic"));
		context.registerAction("ChangeBold", new ChangeBoldAction("changeBold"));
		
		context.registerAction("About", new AboutAction());

		context.registerAction("TreeAddPackage", new AddToModelAction("treeAddPackage", new ApesTreeNode(new SPackage(""), true)));
		context.registerAction("TreeAddProcessComponent", new AddToModelAction("treeAddProcessComponent",  new ApesTreeNode(new ProcessComponent(""), true)));
		context.registerAction("TreeAddProvidedInterface", new AddToModelAction("treeAddProvidedInterface",  new ApesTreeNode(new ApesProcess.ProvidedInterface(""), true)));
		context.registerAction("TreeAddRequiredInterface", new AddToModelAction("treeAddRequiredInterface",  new ApesTreeNode(new ApesProcess.RequiredInterface(""), true)));
		context.registerAction("TreeAddActivityDiagram", new AddToModelAction("treeAddActivityDiagram",  new ApesTreeNode(new ActivityDiagram(""), true)));
		context.registerAction("TreeAddFlowDiagram", new AddToModelAction("treeAddFlowDiagram",  new ApesTreeNode(new FlowDiagram(""), true)));
		context.registerAction("TreeAddResponsabilityDiagram", new AddToModelAction("treeAddResponsabilityDiagram",  new ApesTreeNode(new ResponsabilityDiagram(""), true)));
		context.registerAction("TreeAddContextDiagram", new AddToModelAction("treeAddContextDiagram",  new ApesTreeNode(new ContextDiagram(""), true)));
		context.registerAction("TreeAddWorkDefinitionDiagram", new AddToModelAction("treeAddWorkDefinitionDiagram",  new ApesTreeNode(new WorkDefinitionDiagram(""), true)));
		context.registerAction("TreeOpenDiagram", new InteractWithModelAction("treeOpenDiagram"));
		context.registerAction("TreeAddRole", new AddToModelAction("treeAddRole",  new ApesTreeNode(new ProcessRole(""), true)));		
		context.registerAction("TreeAddActivity", new AddToModelAction("treeAddActivity",  new ApesTreeNode(new Activity(""), true)));
		context.registerAction("TreeAddWorkProduct", new AddToModelAction("treeAddWorkProduct",  new ApesTreeNode(new WorkProduct(""), true)));
		context.registerAction("TreeAddWorkProductRef", new AddToModelAction("treeAddWorkProductRef",  new ApesTreeNode(new WorkProductRef(""), true)));
		context.registerAction("TreeAddWorkProductState", new AddToModelAction("treeAddWorkProductState", new ApesTreeNode(new StateMachine(""), true)));
		context.registerAction("TreeAddWorkDefinition", new AddToModelAction("treeAddWorkDefinition",  new ApesTreeNode(new ApesWorkDefinition(""), true)));
		context.registerAction("TreeRemove", new RemoveFromModelAction("treeRemove"));
		context.registerAction("TreeRename", new TreeRenameAction("treeRename"));
		//context.registerAction("TreeAddExternalElement", new AddExternalElement("treeAddExternalElement"));
		context.registerAction("SaveAsImage",new SaveAsImageAction("saveAsImage"));
		context.registerAction("PrintDiagram",new PrintDiagramAction("printDiagram"));
	}

}
