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


package apes.model.spem;

import java.util.Vector;

import junit.framework.TestCase;

import org.ipsquad.apes.model.extension.ActivityDiagram;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.ContextDiagram;
import org.ipsquad.apes.model.extension.FlowDiagram;
import org.ipsquad.apes.model.extension.WorkDefinitionDiagram;
import org.ipsquad.apes.model.extension.ResponsabilityDiagram;
import org.ipsquad.apes.model.spem.DefaultSpemVisitor;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.basic.ExternalDescription;
import org.ipsquad.apes.model.spem.basic.Guidance;
import org.ipsquad.apes.model.spem.basic.GuidanceKind;
import org.ipsquad.apes.model.spem.core.Element;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.core.PresentationElement;
import org.ipsquad.apes.model.spem.modelmanagement.IPackage;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.components.SProcess;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessPerformer;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;
import org.ipsquad.apes.model.spem.statemachine.StateMachine;



public class TestSpemVisitor extends TestCase
{
	public void testIPackageVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitPackage(IPackage pack) {validity=true;}
		};
		
		processThis(stub, new SPackage(), new String[]{"Package", "ModelElement", "Element"});
	}
	
	public void testProcessComponentVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitProcessComponent(ProcessComponent component) {validity=true;}
		};

		processThis(stub, new ProcessComponent(), new String[]{"ProcessComponent", "Package", "ModelElement", "Element"});
	}
	
	public void testComponentProvidedInterfaceVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitProvidedInterface(ApesProcess.ProvidedInterface i) {validity=true;}
		};

		processThis(stub, new ApesProcess.ProvidedInterface(""), new String[]{"ProvidedInterface", "Interface", "Package", "ModelElement", "Element"});
	}
	
	public void testComponentRequiredInterfaceVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitRequiredInterface(ApesProcess.RequiredInterface i) {validity=true;}
		};

		processThis(stub, new ApesProcess.RequiredInterface(""), new String[]{"RequiredInterface", "Interface", "Package", "ModelElement", "Element"});
	}
	
	public void testSProcessVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitProcess(SProcess process) {validity=true;}
		};

		processThis(stub, new SProcess(), new String[]{"Process", "ProcessComponent", "Package", "ModelElement", "Element"});
	}
	
	public void testWorkDefinitionVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitWorkDefinition(WorkDefinition work) {validity=true;}
		};

		processThis(stub, new WorkDefinition(), new String[]{"WorkDefinition", "ModelElement", "Element"});
	}
	
	public void testProcessPerformerVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitProcessPerformer(ProcessPerformer performer) {validity=true;}
		};

		processThis(stub, new ProcessPerformer(), new String[]{"ProcessPerformer", "ModelElement", "Element"});
	}
	
	public void testWorkProductVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitProduct(WorkProduct product) {validity=true;}
		};

		processThis(stub, new WorkProduct(), new String[]{"Product", "ModelElement", "Element"});
	}
	
	public void testProcessRoleVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitRole(ProcessRole role) {validity=true;}
		};

		processThis(stub, new ProcessRole(), new String[]{"Role", "ProcessPerformer", "ModelElement", "Element"});
	}
	
	public void testActivityVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitActivity(Activity activity) {validity=true;}
		};

		processThis(stub, new Activity(), new String[]{"Activity", "WorkDefinition", "ModelElement", "Element"});
	}
	
	public void testExternalDescriptionVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitExternalDescription(ExternalDescription description) {validity=true;}
		};

		processThis(stub, new ExternalDescription(), new String[]{"ExternalDescription", "PresentationElement", "Element"});
	}
	
	public void testGuidanceVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitGuidance(Guidance guidance) {validity=true;}
		};

		processThis(stub, new Guidance(), new String[]{"Guidance", "ModelElement", "Element"});
	}
	
	public void testGuidanceKindVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitGuidanceKind(GuidanceKind kind) {validity=true;}
		};

		processThis(stub, new GuidanceKind(), new String[]{"GuidanceKind", "ModelElement", "Element"});
	}
	
	public void testFlowDiagramVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitFlowDiagram(FlowDiagram diagram) {validity=true;}
		};

		processThis(stub, new FlowDiagram(), new String[]{"FlowDiagram", "ModelElement", "Element"});
	}
	
	public void testResponsabilityDiagramVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitResponsabilityDiagram(ResponsabilityDiagram diagram) {validity=true;}
		};

		processThis(stub, new ResponsabilityDiagram(), new String[]{"ResponsabilityDiagram", "ModelElement", "Element"});
	}
	
	public void testContextDiagramVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitContextDiagram(ContextDiagram diagram) {validity=true;}
		};

		processThis(stub, new ContextDiagram(), new String[]{"ContextDiagram", "ModelElement", "Element"});
	}
	
	public void testActivityDiagramVisiting()
	{
		SpemVisitorStub stub = new SpemVisitorStub()
		{
			public void visitActivityDiagram(ActivityDiagram diagram) {validity=true;}
		};

		processThis(stub, new ActivityDiagram(), new String[]{"ActivityDiagram", "ModelElement", "Element"});
	}




	private void processThis(SpemVisitorStub stub1, Element e, String[] expected)
	{
		e.visit(stub1);
		assertTrue(stub1.isValid());
		
		DefaultSpemVisitorStub stub2 = new DefaultSpemVisitorStub();
		e.visit(stub2);
		assertTrue(arraysEquals(stub2.getResult().toArray(), expected));
	}

	private boolean arraysEquals(Object[] array1, Object[] array2)
	{
		if(array1.length!=array2.length) return false;
		
		for(int i=0; i<array1.length; i++)
		{
			if(!array1[i].equals(array2[i]))
			{
				return false;
			}
		}
		
		return true;
	}
	
	private class SpemVisitorStub implements SpemVisitor
	{
		protected boolean validity = false;
		
		public boolean isValid()
		{
			return validity;
		}		
		
		public void visitApesProcess(ApesProcess ap){ fail(); }
		public void visitPackage(IPackage pack) { fail(); }
		public void visitProcessComponent(ProcessComponent component) { fail(); }
		public void visitProcess(SProcess process) { fail(); }
		public void visitWorkDefinition(WorkDefinition work) { fail(); }
		public void visitProcessPerformer(ProcessPerformer performer) { fail(); }
		public void visitProduct(WorkProduct product) { fail(); }
		public void visitRole(ProcessRole role) { fail(); }
		public void visitActivity(Activity activity) { fail(); }
		/**********DELPH**********/
		public void visitWorkDefinition(ApesWorkDefinition WorkDefinition) { fail(); }
		
		public void visitExternalDescription(ExternalDescription description) { fail(); }
		public void visitGuidance(Guidance guidance) { fail(); }
		public void visitGuidanceKind(GuidanceKind kind) { fail(); }
		public void visitFlowDiagram(FlowDiagram diagram) { fail(); }
		public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram) { fail(); }
		public void visitResponsabilityDiagram(ResponsabilityDiagram diagram) { fail(); }
		public void visitContextDiagram(ContextDiagram diagram) { fail(); }
		public void visitActivityDiagram(ActivityDiagram diagram) { fail(); }
		public void visitComponentInterface(ApesProcess.Interface i) { fail(); }
		public void visitProvidedInterface(ApesProcess.ProvidedInterface pi) { fail(); }
		public void visitRequiredInterface(ApesProcess.RequiredInterface ri) { fail(); }
		public void visitStateMachine(StateMachine sm) { fail(); }
	}
	
	
	private class DefaultSpemVisitorStub extends DefaultSpemVisitor
	{
		Vector result = new Vector();
		
		public Vector getResult()
		{
			return result;
		}
		
		protected void visitElement(Element element)
		{
			result.add("Element");
			super.visitElement(element);
		}

		protected void visitModelElement(ModelElement element)
		{
			result.add("ModelElement");
			super.visitModelElement(element);
		}

		protected void visitPresentationElement(PresentationElement element)
		{
			result.add("PresentationElement");
			super.visitPresentationElement(element);
		}
		
		public void visitPackage(IPackage pack)
		{
			result.add("Package");
			super.visitPackage(pack);
		}
		
		public void visitProcessComponent(ProcessComponent component)
		{
			result.add("ProcessComponent");
			super.visitProcessComponent(component);
		}
		
		public void visitComponentInterface(ApesProcess.Interface i)
		{
			result.add("Interface");
			super.visitComponentInterface(i);
		}
		
		public void visitProvidedInterface(ApesProcess.ProvidedInterface i)
		{
			result.add("ProvidedInterface");
			super.visitProvidedInterface(i);
		}
		
		public void visitRequiredInterface(ApesProcess.RequiredInterface i)
		{
			result.add("RequiredInterface");
			super.visitRequiredInterface(i);
		}
		
		public void visitProcess(SProcess process)
		{
			result.add("Process");
			super.visitProcess(process);
		}
		
		public void visitWorkDefinition(WorkDefinition work)
		{
			result.add("WorkDefinition");
			
			super.visitWorkDefinition(work);
		}
		
		public void visitProcessPerformer(ProcessPerformer performer)
		{
			result.add("ProcessPerformer");
			super.visitProcessPerformer(performer);
		}
		
		public void visitProduct(WorkProduct product)
		{
			result.add("Product");
			super.visitProduct(product);
		}
		
		public void visitRole(ProcessRole role)
		{
			result.add("Role");
			super.visitRole(role);
		}
		
		public void visitActivity(Activity activity)
		{
			result.add("Activity");
			super.visitActivity(activity);
		}
		
		public void visitExternalDescription(ExternalDescription description)
		{
			result.add("ExternalDescription");
			super.visitExternalDescription(description);
		}
		
		public void visitGuidance(Guidance guidance)
		{
			result.add("Guidance");
			super.visitGuidance(guidance);
		}
		
		public void visitGuidanceKind(GuidanceKind kind)
		{
			result.add("GuidanceKind");
			super.visitGuidanceKind(kind);
		}
		
		public void visitFlowDiagram(FlowDiagram diagram)
		{
			result.add("FlowDiagram");
			super.visitFlowDiagram(diagram);
		}
		
		public void visitResponsabilityDiagram(ResponsabilityDiagram diagram)
		{
			result.add("ResponsabilityDiagram");
			super.visitResponsabilityDiagram(diagram);
		}
		
		public void visitWorkDefinitionDiagram(WorkDefinitionDiagram diagram)
		{
			result.add("WorkDefinitionDiagram");
			super.visitWorkDefinitionDiagram(diagram);
		}
		
		public void visitContextDiagram(ContextDiagram diagram)
		{
			result.add("ContextDiagram");
			super.visitContextDiagram(diagram);
		}
		
		public void visitActivityDiagram(ActivityDiagram diagram)
		{
			result.add("ActivityDiagram");
			super.visitActivityDiagram(diagram);
		}
	}
}
