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
 
package iepp.application.ageneration;

import iepp.domaine.ElementPresentation;

import java.io.PrintWriter;
import java.util.HashMap;



/**
 * Classe permettant de créer une page dont le contenu correspond à un guide
 */
public class GGuide extends GElement
{

	/**
	 * Constructeur du gestionnaire de génération
	 * @param elem element de présentation associé à l'activité courante
	 * @param elem2 element de présentation qui suit (dans l'arbre) l'élément de présentation courant
	 * @param listeIdDossier map contenant la liste des dossiers déjà présents dans l'arbre pour le composant publiable en cours de publication
	 * @param pwFicTree lien vers le fichier tree.js construit durant la génération du site
	 */
	public GGuide(ElementPresentation elem, ElementPresentation elem2, HashMap listeIdDossier, PrintWriter pwFicTree)
	{
		super(elem, elem2, listeIdDossier, pwFicTree);
	}
	
	
}