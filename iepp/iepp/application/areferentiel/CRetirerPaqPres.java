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
package iepp.application.areferentiel;

import iepp.Application;
import iepp.application.CommandeNonAnnulable;


import javax.swing.JOptionPane;


/**
 * Retire un paquetage de présentation du référentiel.
 */
public class CRetirerPaqPres extends CommandeNonAnnulable
{
	private long idPres ;	// Identifiant de la présentation


	/**
	 * Construit la commande.
	 * @param idComp identifiant du paquetage de présentation dans le référentiel
	 */
	public CRetirerPaqPres (long idPres)
	{
		this.idPres = idPres ;
	}


	/**
	 * Retire la présentation du référentiel.
	 * @see iepp.application.Commande#executer()
	 * @return true si la commande s'est exécutée correctement
	 */
	public boolean executer()
	{
		String txtMsg ;	// Message à afficher pour demander confirmation
		int typeMsg ;	// Type de la boîte de dialogue (info, avertissement)

		// Demander confirmation à l'utilisateur
		int choice = JOptionPane.showConfirmDialog( Application.getApplication().getFenetrePrincipale(),
		Application.getApplication().getTraduction("BD_SUPP_PRES_REF"),
				 Application.getApplication().getTraduction("Confirmation"),
				 JOptionPane.OK_CANCEL_OPTION, JOptionPane.INFORMATION_MESSAGE );
		if (choice == JOptionPane.OK_OPTION)
		{
			// Demander au référentiel de retirer ce composant
			Referentiel ref = Application.getApplication().getReferentiel() ;
			return (ref.supprimerElement(this.idPres, ElementReferentiel.PRESENTATION)) ;
		}
		// Si on arrive ici, la commande a été annulée
		return false ;
	}
}
