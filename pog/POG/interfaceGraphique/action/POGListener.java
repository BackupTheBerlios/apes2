/*
 * Created on 18 nov. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package POG.interfaceGraphique.action;

import POG.objetMetier.ElementPresentation;

/**
 * @author c82aber
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public interface POGListener {

	public static final String AJOUTER = "ajouter";
	public static final String SUPPRIMER = "supprimer";
	public static final String MODIFIER = "modifier";
	public static final String INITIALISE = "initialise";
	
	// Doivent toutes avoir la même signature (invoke dans la classe Systeme)
	
	public void ajouter(ElementPresentation el);
	public void supprimer(ElementPresentation el);
	public void modifier(ElementPresentation el);
	public void initialise(ElementPresentation el);

}
