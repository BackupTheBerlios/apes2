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

import javax.swing.JFileChooser;
import javax.swing.JOptionPane;

import util.SimpleFileFilter;
import util.SmartChooser;

import iepp.Application;
import iepp.application.CommandeNonAnnulable;

/**
 * Ajout d'un paquetage de pr�sentation au r�f�rentiel.
 */
public class CAjouterPaqPresRef extends CommandeNonAnnulable
{

	/**
	 * Ajoute un paquetage de pr�sentation choisi par l'utilisateur au r�f�rentiel.
	 * @see iepp.application.Commande#executer()
	 * @return true si la commande s'est ex�cut�e correctement
	 */
	public boolean executer()
	{
		String cheminComp ;	// Chemin du paquetage � charger

		// Demander � l'utilisateur de choisir un paquetage
		JFileChooser chooser = new JFileChooser(Application.getApplication().getConfigPropriete("rep_paquetage"));
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new SimpleFileFilter
			(new String[]{"pre"}, Application.getApplication().getTraduction("Presentations"))) ;
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
		// r�cup�re le nom de fichier s�lectionn� par l'utilisateur
		if(chooser.showOpenDialog(Application.getApplication().getFenetrePrincipale()) != JFileChooser.APPROVE_OPTION )
			return false;

		// Recuperer le r�f�rentiel courantet lui demander de charger un composant
		Referentiel ref = Application.getApplication().getReferentiel() ;

		long id = ref.ajouterElement(chooser.getSelectedFile().getAbsolutePath(), ElementReferentiel.PRESENTATION) ;
		if (id == -2)
		{
			JOptionPane.showMessageDialog ( Application.getApplication().getFenetrePrincipale(),
				Application.getApplication().getTraduction("ERR_Non_Presentation"),
				Application.getApplication().getTraduction("ERR"),
				JOptionPane.ERROR_MESSAGE );
		}
		if (id == -3)
		{
			JOptionPane.showMessageDialog ( Application.getApplication().getFenetrePrincipale(),
				Application.getApplication().getTraduction("ERR_Presentation_Deja"),
				Application.getApplication().getTraduction("ERR"),
				JOptionPane.ERROR_MESSAGE );
		}
		// Retourner vrai si �a s'est bien pass�
		return (id >= 0) ;
	}

}
