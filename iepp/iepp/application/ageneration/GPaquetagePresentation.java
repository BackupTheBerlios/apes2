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
import iepp.domaine.PaquetagePresentation;

import java.io.*;
import java.util.HashMap;
import java.util.Vector;

/**
 * Classe permettant de g�rer la publication d'un paquetage de pr�sentation
 */
public class GPaquetagePresentation 
{
	/**
	 * Lien vers le fichier tree.js � remplir lors de la g�n�ration
	 */
	private PrintWriter pwFicTree ;
	
	/**
	 * Paquetage de pr�sentation � publier
	 */
	private PaquetagePresentation paquetage ;
	
	/**
	 * Liste des dossiers qui ont d�j� �t� cr��s dans l'arbre
	 */
	private HashMap idDossier ;
	
	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param paquetage paquetage de pr�sentation � publier
	 * @param pwFicTree lien vers le fichier tree.js ) remplir
	 */
	public GPaquetagePresentation (PaquetagePresentation paquetage , PrintWriter pwFicTree)
	{
		this.pwFicTree = pwFicTree ;
		this.paquetage = paquetage ;
		this.idDossier = new HashMap();
		this.idDossier.put("", "foldersTree");
	}
	
	/**
	 * Permet de traiter la g�n�ration de tous les �l�ments de pr�sentation
	 * contenu dans le paquetage
	 */	
	public void traiterGeneration() throws IOException
	{
		Vector liste ; // liste en cours de traitement
		int i;

		liste = this.paquetage.getListeElement();
		for (i = 0; i < liste.size() - 1; i++)
		{
			ElementPresentation elem = (ElementPresentation)liste.elementAt(i);
			ElementPresentation elem2 = (ElementPresentation)liste.elementAt(i + 1);
			GElement noeud = new GElement(elem, elem2, this.idDossier, pwFicTree);
			noeud.traiterGeneration();
		}
		ElementPresentation elem = (ElementPresentation)liste.elementAt(i);
		GElement noeud = new GElement(elem, null, this.idDossier, pwFicTree);
		noeud.traiterGeneration();
	}
}
