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

import org.ipsquad.utils.SimpleFileFilter;
import org.ipsquad.utils.SmartChooser;

import iepp.Application ;
import iepp.application.* ;

/**
 * Ajout d'un composant au référentiel.
 */
public class CAjouterComposantRef extends CommandeNonAnnulable
{
	

	/**
	 * Ajoute un composant choisi par l'utilisateur au référentiel.
	 * @see iepp.application.Commande#executer()
	 * @return true si la commande s'est exécutée correctement
	 */
	public boolean executer()
	{
		String cheminComp ;	// Chemin du composant à charger

		// Demander à l'utilisateur de choisir un composant
		JFileChooser chooser = SmartChooser.getChooser();
		chooser.setAcceptAllFileFilterUsed(false);
		chooser.setFileFilter(new SimpleFileFilter
			(new String[]{"pre"}, Application.getApplication().getTraduction("Composants_Publiables"))) ;
		chooser.setFileSelectionMode(JFileChooser.FILES_ONLY);

		// récupère le nom de fichier sélectionné par l'utilisateur
		if(chooser.showOpenDialog(Application.getApplication().getFenetrePrincipale()) != JFileChooser.APPROVE_OPTION )
			return false;

		// Recuperer le référentiel courant et lui demander de charger un composant
		Referentiel ref = Application.getApplication().getReferentiel() ;
		long id = ref.ajouterElement(chooser.getSelectedFile().getAbsolutePath(), ElementReferentiel.COMPOSANT) ;
		if (id == -2)
		{
			JOptionPane.showMessageDialog ( Application.getApplication().getFenetrePrincipale(),
				Application.getApplication().getTraduction("ERR_Non_Composant"),
				Application.getApplication().getTraduction("ERR"),
				JOptionPane.ERROR_MESSAGE );
		}
		// Retourner vrai si ça s'est bien passé
		return (id >= 0) ;
	}

}
