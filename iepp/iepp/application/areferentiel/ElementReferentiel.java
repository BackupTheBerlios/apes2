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

import javax.swing.tree.DefaultMutableTreeNode;


/**
 * Classe utilisée pour remplir l'arbre du référentiel
 * Chaque objet de celle-ci contiendra le nom de l'élément ainsi que son ID
 */
public class ElementReferentiel extends DefaultMutableTreeNode
{
	
	private String nomElement;
	private long idElement;
	private String chemin;
	private int type;
	
	// Constantes représentant les types possibles de l'élément
	public static final int REFERENTIEL = -1;
	public static final int PAQ_COMP = 0;	// Paquetage de composants
	public static final int PAQ_DP = 1;		// Paquetage de DP
	public static final int PAQ_PRESENTATION = 2; // Paquetage de présentation
	public static final int COMPOSANT = 3;
	
	// Le type COMPOSANT_VIDE ne sert qu'à l'ajout du composant vide, ce composant vide est ensuite traité comme un composant
	public static final int COMPOSANT_VIDE = 4;
	public static final int DP = 5;
	public static final int PRESENTATION = 6;
	
	public ElementReferentiel(String nom, long id, String cheminElt, int typeElt)
	{
		nomElement = nom;
		idElement = id;
		chemin = cheminElt;
		type = typeElt;
	}
	
	public String getNomElement()
	{ return nomElement; }
	
	public long getIdElement()
	{ return idElement; }
		
	public String getChemin()
	{ return chemin; }
	
	public int getType()
	{ return type; }
		
	public String toString()
	{ return nomElement; }

} 
		