package org.ipsquad.apes.model.frontend.event;

import java.util.EventListener;

public interface ApesModelListener extends EventListener 
{
	void modelChanged(ApesModelEvent e);
}
