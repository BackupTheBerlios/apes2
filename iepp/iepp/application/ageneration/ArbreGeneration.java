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

import iepp.domaine.ComposantProcessus;
import iepp.domaine.ElementPresentation;
import iepp.domaine.IdObjetModele;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Vector;

import util.ToolKit;

/**
 * Classe Arbre permettant d'avoir une structure de donnée équivalente à celle de
 * l'arbre de navigation et du site à générer
 */
public class ArbreGeneration 
{
	
	/**
	 * Liste contenant des arbres
	 */
	private Vector listeSousArbres = null;
	
	/**
	 * Noeud parent
	 */
	private ArbreGeneration parent = null;
	
	/**
	 * Element de génération courant = cellule courante
	 */
	private GElement element = null;
	
	
	/**
	 * map permettant d'avoir des statistiques sur les pages
	 */
	public static HashMap mapCompteur = new HashMap();
	
	public static HashMap mapRecap = new HashMap();
	
	
	private static long IDGenere = 1;
	
	
	/**
	 * Constructeur
	 */
	public ArbreGeneration(GElement elem)
	{
		this.element = elem;
		this.listeSousArbres = new Vector();
	}
	
	/**
	 * Constructeur
	 */
	public ArbreGeneration() 
	{
		this.listeSousArbres = new Vector();
	}


	//-----------------------------------------------
	// Setters
	//-----------------------------------------------
	public void ajouterSousArbre(ArbreGeneration sarbre)
	{
		// si la liste n'existe pas, la créer
		if (this.listeSousArbres == null)
		{
			this.listeSousArbres = new Vector();
		}
		this.listeSousArbres.addElement(sarbre);
		sarbre.parent = this;
	}
	
	public void setArbreParent(ArbreGeneration parent)
	{
		this.parent = parent;
	}
	
	public ArbreGeneration getArbreParent()
	{
		return this.parent;
	}
	
	public boolean isRacine()
	{
		return (this.parent == null);
	}
	
	public boolean isFeuille()
	{
		return (this.listeSousArbres.isEmpty());
	}

	public static void initialiserMap()
	{
		mapCompteur.put("nbPagesTotal", new Integer(1));
		mapCompteur.put("nbComposants", new Integer(0));
		mapCompteur.put("nbRoles", new Integer(0));
		mapCompteur.put("nbProduits", new Integer(0));
		mapCompteur.put("nbActivites", new Integer(0));
		mapCompteur.put("nbDefinitionsTravail", new Integer(0));
		mapCompteur.put("nbDiagrammes", new Integer(1));
		mapCompteur.put("nbGuides", new Integer(0));
		mapCompteur.put("nbPaquetagesPresentation", new Integer(0));
		mapCompteur.put("nbPaquetages", new Integer(0));
		mapCompteur.put("nbElementPresentation", new Integer(0));
		
		mapRecap.put("roles", new Vector());
		mapRecap.put("produits", new Vector());
		mapRecap.put("activites", new Vector());
	}

	/**
	 * @param noeud
	 */
	public void ajouterSousArbre(GElement noeud, String id) 
	{
		String[] tab = id.split("-");
		if (tab.length == 1)
		{
			this.ajouterSousArbre(new ArbreGeneration(noeud));
		}
		else
		{
			// récupérer le premier niveau
			if (tab.length > 1)
			{
				// calculer le nouvel id
				String nouvelID = id.substring(id.indexOf("-") + 1);
				ArbreGeneration  a = (ArbreGeneration)(this.listeSousArbres.elementAt(new Integer(tab[0]).intValue() - 1));
				a.ajouterSousArbre(noeud, nouvelID);
			}
		}
	}
	
	/**
	 * Permet d'initialiser tous les éléments de l'arbre et d'associer un chemin à tous
	 * les fichiers à créer
	 */
	public void initialiserArbre(String cheminAbsolu)
	{
		if (!this.isRacine())
		{
			String nouveauChemin = cheminAbsolu + File.separator 
									+ CodeHTML.normalizeName(this.element.toString()) + File.separator  
									+ CodeHTML.normalizeName(this.element.toString()) + "_" + IDGenere + ".html";
			this.element.setChemin(nouveauChemin);
			this.element.setID(IDGenere);
			this.element.setArbre(this);
			this.element.recenser();
			//System.out.println("Chemin après " + this.element.getChemin());
			if (this.element.getElementPresentation().getElementModele() != null)
			{
				File f = new File(GenerationManager.getInstance().getCheminGeneration());
				String cheminRel = "./" + ToolKit.getRelativePathOfAbsolutePath(this.element.getChemin(),
									ToolKit.removeSlashTerminatedPath(f.getAbsolutePath()));

				this.element.getElementPresentation().getElementModele().setChemin(ToolKit.removeSlashTerminatedPath(cheminRel));
				// initialiser les produits interface
				if (this.element.getElementPresentation().getElementModele().estProduit())
				{
					Vector interfaces = (Vector) ((ComposantProcessus)this.element.getElementPresentation().getElementModele().getRef()).getProduitEntree().clone();
					interfaces.addAll(((ComposantProcessus)this.element.getElementPresentation().getElementModele().getRef()).getProduitSortie());
					
					for (int i = 0; i < interfaces.size(); i++)
					{
						IdObjetModele id = (IdObjetModele)interfaces.elementAt(i);
						if (this.element.getElementPresentation().getElementModele().toString().equals(id.toString()))
						{
							id.setChemin(ToolKit.removeSlashTerminatedPath(cheminRel));
						}
					}
				}
				//System.out.println("Chemin relatif : " + cheminRel);
			}
			IDGenere++;
			if (!this.isFeuille())
			{
				for (int i = 0; i < this.listeSousArbres.size(); i++)
				{
					ArbreGeneration a = (ArbreGeneration)this.listeSousArbres.elementAt(i);
					a.initialiserArbre(cheminAbsolu + File.separator + CodeHTML.normalizeName(this.element.toString()));
				}
			}
		}
		else
		{
			// c'est la racine
			for (int i = 0; i < this.listeSousArbres.size(); i++)
			{
				ArbreGeneration a = (ArbreGeneration)this.listeSousArbres.elementAt(i);
				a.initialiserArbre(cheminAbsolu);
			}
		}
	}
	
	/**
	 * Permet de générer le site web à partir de l'arbre construit
	 */
	public void genererSite() throws IOException 
	{
		if (this.isRacine())
		{
			for (int i = 0; i < this.listeSousArbres.size(); i++)
			{
				ArbreGeneration a = (ArbreGeneration)this.listeSousArbres.elementAt(i);
				a.genererSite();
			}
		}
		else
		{
			if (this.parent.isRacine())
			{
				this.element.traiterGeneration(0l);
			}
			else
			{
				this.element.traiterGeneration(this.parent.element.getID());
			}
			
			if (!this.isFeuille())
			{
				for (int i = 0; i < this.listeSousArbres.size(); i++)
				{
					ArbreGeneration a = (ArbreGeneration)this.listeSousArbres.elementAt(i);
					a.genererSite();
				}
			}
		}
	}
	
	
	
	public String toString()
	{
		if (this.isRacine())
		{
			if (this.listeSousArbres.isEmpty())
			{
				return "Arbre Vide";
			}
			return "ARBRE DE GENERATION : \n " + this.listeSousArbres.toString();
		}
		else
		{
			if (this.isFeuille())
			{
				return this.element.toString();
			}
			return "\n" + this.element.toString() + "\t" + this.listeSousArbres;
		}
	}

	/**
	 * @return Returns the element.
	 */
	public GElement getElement() 
	{
		return element;
	}

}
