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


package org.ipsquad.apes.model.extension;

import java.io.Serializable;
import java.util.Vector;

import org.ipsquad.apes.model.spem.ModelVisitor;
import org.ipsquad.apes.model.spem.SpemVisitor;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.utils.ErrorManager;

/**
 * Base class for the activity diagram
 *
 * @version $Revision: 1.1 $
 */
public class ActivityDiagram extends SpemDiagram
{
	private Vector mElements = new Vector();
	private Vector mTransitions = new Vector();
	private boolean mInitialPoint = false;

	public ActivityDiagram()
	{

	}

	public ActivityDiagram(String name)
	{
		super(name);
	}
	
	public void visit(SpemVisitor visitor)
	{
		visitor.visitActivityDiagram(this);
	}
	
	
	
	/**
	 * Add a role to the class diagram
	 *
	 * @param me the model element to associate
	 * @return true if the model element can be added, false otherwise
	 */
	public boolean addModelElement(ModelElement me)
	{
		if( ( me instanceof Activity && ( me.getParent() == null || me.getParent() == getParent() ) ) || me instanceof Decision || me instanceof FinalPoint || me instanceof Synchro)
		{
			if(!containsModelElement(me))
			{
				mElements.add(me);
				return true;
			}
		}
		else if(me instanceof InitialPoint && !mInitialPoint)
		{
			if(!containsModelElement(me))
			{
				mElements.add(me);
				mInitialPoint=true;
				return true;
			}
		}

		return false;
	}


	public boolean canAddModelElement(ModelElement me)
	{
		if( ( me instanceof Activity && ( me.getParent() == null || me.getParent() == getParent() ) )
				|| me instanceof Decision
				|| me instanceof FinalPoint
				|| me instanceof Synchro)
		{
			if(!containsModelElement(me))
			{
				return true;
			}
			else
			{
				ErrorManager.getInstance().printKey("errorElementAlreadyPresent");
				return false;
			}
		}
		
		if(me instanceof InitialPoint)
		{
			if(!mInitialPoint)
			{
				return true;
			}
			else
			{
				ErrorManager.getInstance().printKey("errorInitialPointAlreadyPresent");
				return false;
			}
		}
		
		ErrorManager.getInstance().printKey("errorElementForbidden");
		return false;
	}

	
	/**
	 * Remove a model element from this diagram
	 *
	 * @param e the model element to remove
	 * @return true if the model element can be removed, false otherwise
	 */
	public boolean removeModelElement(ModelElement e)
	{
		if(containsModelElement(e))
		{
			mElements.remove(e);
			if(e instanceof InitialPoint) mInitialPoint=false;
			return true;
		}
		return false;
	}
	
	/**
	 * Check if a model element is in this diagram
	 *
	 * @param e the model element to test
	 * @return true if the model element is in this diagram, false otherwise
	 */
	public boolean containsModelElement(ModelElement e)
	{
		return mElements.contains(e);
	}
	
	
	
	/**
	 * Get a model element by its index
	 *
	 * @param i the model element index
	 * @return the model element or null if the index is invalid
	 */
	public ModelElement getModelElement(int i)
	{
		if(i<0 || i>=mElements.size())
		{
			return null;
		}
		
		return (ModelElement) mElements.get(i);
	}
	
	/**
	 * Get the number of model elements in this diagram
	 *
	 * @return the number of model elements contained in this diagram
	 */
	public int modelElementCount()
	{
		return mElements.size();
	}
	
	/**
	 * Create a link between two model elements
	 *
	 * @param source the model element to be linked in output
	 * @param target the model element to be linked in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean createLinkModelElements(ModelElement source, ModelElement target)
	{
		if(containsModelElement(source))
		{
			if(containsModelElement(target))
			{
				if(areLinkableModelElements(source,target))
				{
					mTransitions.add(new Transition(source,target,""));
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * Remove a link between two model elements
	 *
	 * @param source the model element to be unlinked in output
	 * @param target the model element to be unlinked in input
	 * @return true if the link can be removed, false otherwise
	 */
	public boolean removeLinkModelElements(ModelElement source, ModelElement target)
	{
		if(existsLinkModelElements(source, target))
		{
			mTransitions.remove(getTransition(source,target));
			return true;
		}
		
		return false;		
	}
	
	/**
	 * Test if a link between two model elements can be created
	 *
	 * @param source the model element to be tested in output
	 * @param target the model element to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableModelElements(ModelElement source, ModelElement target)
	{
		if(!containsModelElement(source) || !containsModelElement(target))
		{
			return false;
		}

		if(source instanceof Activity)
		{
			if(target instanceof Activity)
			{
				return areLinkableActivityActivity((Activity)source,(Activity)target);
			}
			if(target instanceof Decision)
			{
				return areLinkableActivityDecision((Activity)source,(Decision)target);
			}
			if(target instanceof FinalPoint)
			{
				return areLinkableModelElementFinalPoint(source,(FinalPoint)target);
			}
			if(target instanceof Synchro)
			{
				return areLinkableModelElementSynchro(source,(Synchro)target);
			}
		}
		if(source instanceof Decision)
		{
			if(target instanceof Activity)
			{
				return areLinkableDecisionActivity((Decision)source,(Activity)target);
			}
			if(target instanceof FinalPoint)
			{
				return areLinkableModelElementFinalPoint(source,(FinalPoint)target);
			}
			if(target instanceof Synchro)
			{
				return areLinkableModelElementSynchro(source,(Synchro)target);
			}
		}	
		if(source instanceof InitialPoint)
		{
			if(!(target instanceof FinalPoint) && !(target instanceof Decision))
			{
				return areLinkableInitialPointModelElement((InitialPoint)source,target);
			}
			if(target instanceof Decision)
			{
				return areLinkableInitialPointDecision((InitialPoint)source,(Decision)target);
			}
		}
		if(source instanceof Synchro)
		{
			if(!(target instanceof InitialPoint) && !(target instanceof Decision))
			{
				return areLinkableSynchroModelElement((Synchro)source,target);
			}
			if(target instanceof Decision)
			{
				return areLinkableSynchroDecision((Synchro)source,(Decision)target);
			}
		}
		
		ErrorManager.getInstance().printKey("errorNotLinkableElements");
		return false;
	}
	
	/**
	 * Test if a link between two activities can be created
	 *
	 * @param a1 the activity to be tested in output
	 * @param a2 the activity to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableActivityActivity(Activity a1, Activity a2)
	{
		for(int i=0;i<mTransitions.size();i++)
		{
			if(existsLinkModelElements(a1,((Transition) mTransitions.get(i)).getOutputModelElement()))
			{
				ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Test if a link between one activity and one decision can be created
	 *
	 * @param a the activity to be tested in output
	 * @param d the decision to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableActivityDecision(Activity a, Decision d)
	{
		for(int i=0;i<mTransitions.size();i++)
		{
			if(existsLinkModelElements(a,((Transition) mTransitions.get(i)).getOutputModelElement()))
			{
				ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
				return false;
			}
		}
		
		return true;
	}
	
	/**
	 * Test if a link between one decision and one activity can be created
	 *
	 * @param d the decision to be tested in output
	 * @param a the activity to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableDecisionActivity(Decision d, Activity a)
	{
		if(!existsLinkModelElements(d,a))
		{
			return true;
		}

		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;
	}
	
	
	/**
	 * Test if a link between one ModelElement and a Synchro can be created
	 *
	 * @param e the modelElement to be tested in output
	 * @param s the synchro to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableModelElementSynchro(ModelElement e,Synchro s)
	{
		if(e instanceof Activity)
		{
			for(int i=0;i<mTransitions.size();i++)
			{
				if(existsLinkModelElements(e,((Transition) mTransitions.get(i)).getOutputModelElement()))
				{
					ErrorManager.getInstance().printKey("errorNotLinkableElements");
					return false;
				}
			}
			return true;
		}
		else
		{
			if(!existsLinkModelElements(e,s))
			{
				return true;
			}
			
			ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
			return false;
		}
	}


	/**
	 * Test if a link between one Synchro and a ModelElement can be created
	 *
	 * @param s the decision to be tested in output
	 * @param e the activity to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableSynchroModelElement(Synchro s,ModelElement e)
	{
		if(!existsLinkModelElements(s,e))
		{
			return true;
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;
	}

	/**
	 * Test if a link between one synchro and one decision can be created
	 *
	 * @param s the synchro to be tested in output
	 * @param d the decision to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableSynchroDecision(Synchro s,Decision d)
	{
		if(!existsLinkModelElements(s,d))
		{
			for(int i=0;i<mTransitions.size();i++)
			{
				if(existsLinkModelElements(((Transition) mTransitions.get(i)).getInputModelElement(),d))
				{
					ErrorManager.getInstance().printKey("errorNotLinkableElements");
					return false;
				}
			}
			return true;
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;

	}

	/**
	 * Test if a link between one initial point and one model element can be created
	 *
	 * @param ip the InitialPoint to be tested in output
	 * @param me the model element to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableInitialPointModelElement(InitialPoint ip, ModelElement me)
	{
		if(!existsLinkModelElements(ip,me))
		{
			for(int i=0;i<mTransitions.size();i++)
			{
				if(existsLinkModelElements(ip,(((Transition) mTransitions.get(i)).getOutputModelElement())))
				{
					ErrorManager.getInstance().printKey("errorNotLinkableElements");
					return false;
				}
			}
			return true;
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;
	}
	
	/**
	 * Test if a link between one initial point and one decision can be created
	 *
	 * @param ip the InitialPoint to be tested in output
	 * @param d the decision to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableInitialPointDecision(InitialPoint ip,Decision d)
	{
		if(!existsLinkModelElements(ip,d))
		{
			for(int i=0;i<mTransitions.size();i++)
			{
				if(existsLinkModelElements(ip,((Transition) mTransitions.get(i)).getOutputModelElement()) || existsLinkModelElements(((Transition) mTransitions.get(i)).getInputModelElement(),d))
				{
					ErrorManager.getInstance().printKey("errorNotLinkableElements");
					return false;
				}
			}
			return true;
		}
		
		ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
		return false;

	}

	/**
	 * Test if a link between one element and one final point can be created
	 *
	 * @param me the ModelElement to be tested in output
	 * @param fp the FinalPoint to be tested in input
	 * @return true if the link can be created, false otherwise
	 */
	public boolean areLinkableModelElementFinalPoint(ModelElement me, FinalPoint fp)
	{
		if(me instanceof Activity)
		{
			for(int i=0;i<mTransitions.size();i++)
			{
				if(existsLinkModelElements(me,((Transition) mTransitions.get(i)).getOutputModelElement()))
				{
					ErrorManager.getInstance().printKey("errorNotLinkableElements");
					return false;
				}
			}
			return true;
		}
		else
		{
			if(!existsLinkModelElements(me,fp))
			{
				return true;
			}
			
			ErrorManager.getInstance().printKey("errorAlreadyLinkedElements");
			return false;
		}
	}
	
	
	/**
	 * Test if a link between two model elements exists
	 *
	 * @param source the model element to be tested in output
	 * @param target the model element to be tested in input
	 * @return true if the link exists, false otherwise
	 */
	public boolean existsLinkModelElements(ModelElement source, ModelElement target)
	{
		if(getTransition(source,target)!=null)
		{
			return true;
		}

		return false;
	}		
	
	/**
	 * Return the link between two given activities
	 * @param source the Activity which start the link
	 * @param target the Activity which end the link
	 * @return the transition between those two activities, null if there is no link between those activities
	 */
	public Transition getTransition(ModelElement source, ModelElement target)
	{
		for(int i=0;i<mTransitions.size();i++)
		{
			if((((Transition)mTransitions.get(i)).getInputModelElement()==source) && ((Transition)mTransitions.get(i)).getOutputModelElement()==target)
			{
				return (Transition)mTransitions.get(i);
			}
		}
		
		return null;
	}


	/**
	 * Get a transition by giving its index
	 * @param i the index of the presentation element to retrieve
	 * @return the transition or null if the index is invalid
	 */
	public Transition getTransition(int i)
	{
		if(i<0 || i>=mTransitions.size())
		{
			return null;
		}

		return (Transition) mTransitions.get(i);
	}


	/**
	 * Get the number of transitions in this diagram
	 *
	 * @return the number of transitions
	 */
	public int getTransitionCount()
	{
		return mTransitions.size();
	}


	
	/**
	 * Allow to set a label on a link
	 * @param source the Activity which start the link
	 * @param target the Activity which end the link
	 * @param label the label to set on the link
	 * @return true if the label has been set, false otherwise
	 */
	public boolean setLinkLabel(ModelElement source, ModelElement target, String label)
	{
		if(existsLinkModelElements(source,target))
		{
			getTransition(source, target).setLabel(label);
			return true;
		}
		
		return false;
	}
	
	/**
	 * Allow to get the label on a link
	 * @param source the Activity which start the link
	 * @param target the Activity which end the link
	 * @return the label on the link, "" if the link doesn't exist
	 */
	public String getLinkLabel(ModelElement source, ModelElement target)
	{
		if(existsLinkModelElements(source,target))
		{
			return getTransition(source, target).getLabel();
		}
		
		return "";
	}

	public Object clone()
	{
		ActivityDiagram d = (ActivityDiagram) super.clone();
		d.mElements = (Vector) mElements.clone();
		d.mTransitions = (Vector) mTransitions.clone();
		d.mInitialPoint = false;
		return d;
	}

	public boolean haveInitialPoint()
	{
		return mInitialPoint;
	}

	/**
	 * Public class which symbolize the link between two model elements (activity or decision)
	 */
	public static class Transition implements Serializable
	{
		private ModelElement mInput;
		private ModelElement mOutput;
		private String mLabel;
		
		public String toString()
		{
			return mLabel;
		}
		public Transition()
		{
		}
		
		public Transition(ModelElement input, ModelElement output, String label)
		{
			mInput=input;
			mOutput=output;
			mLabel=label;
		}
		
		public void setInputModelElement(ModelElement me)
		{
			mInput=me;
		}
		
		public ModelElement getInputModelElement()
		{
			return mInput;
		}
		
		public void setOutputModelElement(ModelElement me)
		{
			mOutput=me;
		}

		public ModelElement getOutputModelElement()
		{
			return mOutput;
		}
		
		public void setLabel(String s)
		{
			mLabel=s;
		}
		
		public String getLabel()
		{
			return mLabel;
		}
		
		public Object clone()
		{
			return new Transition( mInput, mOutput, mLabel );
		}
		
		public void visit(ModelVisitor visitor){ visitor.visitTransition( this ); }
	};

	/**
	 * Public class which symbolize the decision
	 */
	public static class Decision extends ModelElement
	{
		public Decision()
		{
			super("");
		}
		
		public Object clone()
		{
			Decision d = (Decision) super.clone();
			d.setName("");
			return d;
		}
		
		public String toString() { return ""; }
		
		public void visit(SpemVisitor visitor)
		{ 
			if(visitor instanceof ModelVisitor)
			{
				((ModelVisitor)visitor).visitDecision( this );
			}
		}
	};
	
	/**
	 * Public class which symbolize the InitialPoint
	 */
	
	public static class InitialPoint extends ModelElement
	{
		public InitialPoint()
		{
			super("");
		}
		
		public Object clone()
		{	
			InitialPoint d = (InitialPoint) super.clone();
			d.setName("");
			return d;
		}
		
		public String toString() { return "";}
		
		public void visit(SpemVisitor visitor) 
		{
			if(visitor instanceof ModelVisitor)
			{
				((ModelVisitor)visitor).visitInitialPoint( this );
			}
		}
	};

	public static class FinalPoint extends ModelElement
	{
		public FinalPoint()
		{
			super("");
		}
		
		public Object clone()
		{	
			FinalPoint f = (FinalPoint) super.clone();
			f.setName("");
			return f;
		}
		
		public String toString() { return "";}
		
		public void visit(SpemVisitor visitor) 
		{
			if(visitor instanceof ModelVisitor)
			{
				((ModelVisitor)visitor).visitFinalPoint( this );
			}
		}
	};	

	
	/**
	 * Public class which symbolize the fork
	 */
	public static class Synchro extends ModelElement
	{
		public Synchro()
		{
			super("");
		}
		
		public Object clone()
		{
			Synchro s = (Synchro) super.clone();
			s.setName("");
			return s;
		}
		
		public String toString() { return ""; }
		
		public void visit(SpemVisitor visitor) 
		{ 
			if(visitor instanceof ModelVisitor)
			{
				((ModelVisitor)visitor).visitSynchro( this );
			}	
		}
	};	
}
