
package iepp.application;

import java.io.File;

import iepp.Application;
import iepp.application.areferentiel.Referentiel;

import org.ipsquad.utils.ErrorManager;

/**
 * @author SP
 *
 * TODO To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
public class CChargerReferentielDemarrage extends CommandeNonAnnulable {

	
	private File cheminRef;
	/**
	 * 
	 */
	public CChargerReferentielDemarrage(File cheminRef) 
	{
			this.cheminRef = cheminRef;
	}
	
	public boolean executer() 
	{
		// on charge le référentiel
		try
		{
			Referentiel nouveau = new Referentiel(cheminRef);
			Application.getApplication().setReferentiel(nouveau);
		}
		catch(NumberFormatException e)
		{
			e.printStackTrace();
			ErrorManager.getInstance().display("ERR","ERR_Fic_Ref_Corromp"); 
			return false;
		}
		return true;
	}

}
