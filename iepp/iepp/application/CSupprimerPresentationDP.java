/*
 * IEPP: Isi Engineering Process Publisher
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
 * 
 */

package iepp.application;

import iepp.Application;
import iepp.domaine.PaquetagePresentation;

public class CSupprimerPresentationDP extends CommandeNonAnnulable 
{

	/**
	 * Le paquetage a supprimer de la DP
	 */
	private PaquetagePresentation paquet;
	
	/**
	 * Constructeur avec le paquet � supprimer
	 * @param paquet
	 */
	public CSupprimerPresentationDP(PaquetagePresentation paquet)
	{
		this.paquet = paquet;
	}
	
	public boolean executer() 
	{
		Application.getApplication().getProjet().getDefProc().retirerPresentation(paquet);
		return true;
	}

}
