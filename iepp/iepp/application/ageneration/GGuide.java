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
 * Classe permettant de cr�er une page dont le contenu correspond � un guide
 */
public class GGuide extends GElement
{

	/**
	 * @param elem element de pr�sentation associ� � l'activit� courante
	 * @param writer lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GGuide(ElementPresentation elem, PrintWriter writer) 
	{
		super(elem, writer);
	}
	
	
}