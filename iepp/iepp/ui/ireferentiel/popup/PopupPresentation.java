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
package iepp.ui.ireferentiel.popup;

import iepp.Application;
import iepp.application.areferentiel.CRetirerPaqPres;

import javax.swing.* ;
import java.awt.event.* ;


/**
 * Menu contextuel associ� aux paquetages de pr�sentation dans l'arbre du r�f�rentiel.
 */
public class PopupPresentation extends JPopupMenu implements ActionListener
{
	private long idPres ;	// Identifiant du paquetage de pr�sentation concern�
	// El�ments de menu
	private JMenuItem retirerPresentation ;	// Retrait du r�f�rentiel

	/**
	 * Constructeur.
	 * @param idPres identifiant dans le r�f�rentiel du paquetage de pr�sentation associ� au menu.
	 */
	public PopupPresentation (long idPres)
	{
		// Enregistrer les param�tres
		this.idPres = idPres ;
		// Cr�er les �l�ments
		this.retirerPresentation = new JMenuItem (Application.getApplication().getTraduction("Retirer_Pres_Ref")) ;
		// Ajouter les �l�ments au menu
		this.add (this.retirerPresentation) ;
		// Ecouter les �v�nements
		this.retirerPresentation.addActionListener (this) ;
	}


	/** Clics sur les �l�ments du menu.
	 * @see java.awt.event.ActionListener#actionPerformed(java.awt.event.ActionEvent)
	 */
	public void actionPerformed (ActionEvent e)
	{
		Object source = e.getSource() ;
		// Ajouter un composant au r�f�rentiel
		if (source == this.retirerPresentation)
			(new CRetirerPaqPres(this.idPres)).executer() ;
	}
}
