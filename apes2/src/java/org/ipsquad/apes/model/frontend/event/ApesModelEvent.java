package org.ipsquad.apes.model.frontend.event;

import java.util.Collection;
import java.util.EventObject;
import java.util.Map;

public class ApesModelEvent extends EventObject 
{

	protected ApesModelChange change;

	public ApesModelEvent(Object source, ApesModelChange change) 
	{
		super(source);
		this.change = change;
	}

	public ApesModelChange getChange() 
	{
		return change;
	}

	public static interface ApesModelChange
	{
		public Object getSource();
		public Object[] getInserted();
		public Object[] getRemoved();
		public Map getChanged();
		public Object[] getParents();
		public Collection getMoved();
		public Object getOldParent(Object element);
		public Object getNewParent(Object element);
		public Map getExtras();
	}

	public static interface ExecutableModelChange
	{
	    public void execute();
	}
}

