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


package org.ipsquad.apes;

import java.util.Locale;
import java.util.Properties;

import org.ipsquad.apes.adapters.ApesTreeNode;
import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.extension.WorkProductRef;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;
import org.ipsquad.apes.ui.ApesFrame;
import org.ipsquad.apes.ui.actions.AboutAction;
import org.ipsquad.apes.ui.actions.AddToModelAction;
import org.ipsquad.apes.ui.actions.BiggerAction;
import org.ipsquad.apes.ui.actions.ChangeColorAction;
import org.ipsquad.apes.ui.actions.InteractWithModelAction;
import org.ipsquad.apes.ui.actions.NewAction;
import org.ipsquad.apes.ui.actions.OpenAction;
import org.ipsquad.apes.ui.actions.PreferencesAction;
import org.ipsquad.apes.ui.actions.PrintDiagramAction;
import org.ipsquad.apes.ui.actions.QuitAction;
import org.ipsquad.apes.ui.actions.RedoAction;
import org.ipsquad.apes.ui.actions.RemoveAction;
import org.ipsquad.apes.ui.actions.RemoveFromModelAction;
import org.ipsquad.apes.ui.actions.SaveAction;
import org.ipsquad.apes.ui.actions.SaveAsAction;
import org.ipsquad.apes.ui.actions.SaveAsJpegAction;
import org.ipsquad.apes.ui.actions.SmallerAction;
import org.ipsquad.apes.ui.actions.TreeRenameAction;
import org.ipsquad.apes.ui.actions.UndoAction;
import org.ipsquad.apes.ui.actions.ValidateAction;
import org.ipsquad.utils.ConfigManager;
import org.ipsquad.utils.ErrorManager;
import org.ipsquad.utils.ResourceManager;

/**
 * APES' main class
 *
 * This class contains the main method of the application.
 *
 * @version $Revision: 1.1 $
 */
public class ApesMain
{
	protected static Properties createDefaultProperties()
	{
		ResourceManager resManager = ResourceManager.getInstance();
		Properties properties = new Properties();
		properties.setProperty("Language",Locale.getDefault().getLanguage());
		properties.setProperty("ErrorPanelTitleyes","true");
		properties.setProperty("ActivityTitlered","0");
		properties.setProperty("ActivityTitlegreen","0");
		properties.setProperty("ActivityTitleblue","0");
		properties.setProperty("ActivityTitlebold","false");
		properties.setProperty("ActivityTitleitalic","false");
		properties.setProperty("RoleTitlered","0");
		properties.setProperty("RoleTitlegreen","0");
		properties.setProperty("RoleTitleblue","0");
		properties.setProperty("RoleTitlebold","false");
		properties.setProperty("RoleTitleitalic","false");
		properties.setProperty("GuardTitlered","0");
		properties.setProperty("GuardTitlegreen","0");
		properties.setProperty("GuardTitleblue","0");
		properties.setProperty("GuardTitlebold","false");
		properties.setProperty("GuardTitleitalic","false");
		properties.setProperty("StateTitlered","0");
		properties.setProperty("StateTitlegreen","0");
		properties.setProperty("StateTitleblue","0");
		properties.setProperty("StateTitlebold","false");
		properties.setProperty("StateTitleitalic","false");
		properties.setProperty("WorkproductTitlered","0");
		properties.setProperty("WorkproductTitlegreen","0");
		properties.setProperty("WorkproductTitleblue","0");
		properties.setProperty("WorkproductTitlebold","false");
		properties.setProperty("WorkproductTitleitalic","false");
		properties.setProperty("WorkproductProvidedTitlered","0");
		properties.setProperty("WorkproductProvidedTitlegreen","0");
		properties.setProperty("WorkproductProvidedTitleblue","0");
		properties.setProperty("WorkproductProvidedTitlebold","false");
		properties.setProperty("WorkproductProvidedTitleitalic","false");
		properties.setProperty("WorkproductRequiredTitlered","0");
		properties.setProperty("WorkproductRequiredTitlegreen","0");
		properties.setProperty("WorkproductRequiredTitleblue","0");
		properties.setProperty("WorkproductRequiredTitlebold","false");
		properties.setProperty("WorkproductRequiredTitleitalic","false");
		properties.setProperty("ToolPresentationTitledefaultPath","");
		properties.setProperty("WorkspaceTitledefaultPath","");


		return properties;
	}
	
	public static void main(String[] args)
	{
		ConfigManager.init(createDefaultProperties());
		ResourceManager.setResourceFile("resources/Apes", new Locale(ConfigManager.getInstance().getProperty("Language")));

		Context context = Context.getInstance();
		
		context.registerAction("New", new NewAction());
		context.registerAction("Open", new OpenAction());
		//context.registerAction("Close", new CloseAction());
		context.registerAction("Save", new SaveAction());
		context.registerAction("SaveAs", new SaveAsAction());
		context.registerAction("Quit", new QuitAction());
		
		context.registerAction("Undo", new UndoAction());
		context.registerAction("Redo", new RedoAction());		

		//context.registerAction("Cut", new CutAction());
		//context.registerAction("Copy", new CopyAction());
		//context.registerAction("Paste", new PasteAction());
		context.registerAction("Remove", new RemoveAction());
		
		context.registerAction("Bigger", new BiggerAction());
		context.registerAction("Smaller", new SmallerAction());
		context.registerAction("Preferences",new PreferencesAction());
		
		context.registerAction("Validate", new ValidateAction());
		//context.registerAction("Generate", new GenerateWebSiteAction());
		
		context.registerAction("ChangeForeground", new ChangeColorAction("changeForeground"));
		context.registerAction("ChangeBackground", new ChangeColorAction("changeBackground"));
		context.registerAction("ChangeItalic", new ChangeColorAction("changeItalic"));
		context.registerAction("ChangeBold", new ChangeColorAction("changeBold"));
		
		context.registerAction("About", new AboutAction());

		context.registerAction("TreeAddPackage", new AddToModelAction("treeAddPackage", new ApesTreeNode(new SPackage(""), true)));
		context.registerAction("TreeAddProcessComponent", new AddToModelAction("treeAddProcessComponent",  new ApesTreeNode(new ProcessComponent(""), true)));
		context.registerAction("TreeAddProvidedInterface", new AddToModelAction("treeAddProvidedInterface",  new ApesTreeNode(new ApesProcess.ProvidedInterface(""), true)));
		context.registerAction("TreeAddRequiredInterface", new AddToModelAction("treeAddRequiredInterface",  new ApesTreeNode(new ApesProcess.RequiredInterface(""), true)));
		context.registerAction("TreeAddActivityDiagram", new AddToModelAction("treeAddActivityDiagram",  new ApesTreeNode(new ActivityDiagram(""), true)));
		context.registerAction("TreeAddFlowDiagram", new AddToModelAction("treeAddFlowDiagram",  new ApesTreeNode(new FlowDiagram(""), true)));
		context.registerAction("TreeAddResponsabilityDiagram", new AddToModelAction("treeAddResponsabilityDiagram",  new ApesTreeNode(new ResponsabilityDiagram(""), true)));
		context.registerAction("TreeAddContextDiagram", new AddToModelAction("treeAddContextDiagram",  new ApesTreeNode(new ContextDiagram(""), true)));
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
		context.registerAction("SaveAsJpeg",new SaveAsJpegAction("saveAsJpeg"));
		context.registerAction("PrintDiagram",new PrintDiagramAction("printDiagram"));

		ApesFrame f = new ApesFrame();
		context.setTopLevelFrame(f);
		
		Project project = new Project();
		
		context.setProject(project);
		
		ErrorManager.getInstance().setOwner(f.getContentPane());
		
		f.show();
	}
}
