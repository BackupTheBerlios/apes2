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
 
package iepp.domaine;

import java.util.* ;

import iepp.Application;

/**
 * 
 */
public class DefinitionProcessus extends ObjetModele
{
	/**
	 * Auteur du processus
	 */
	private String auteur ="";

	/**
	 * Commentaires associés au processus
	 */
	private String commentaires ="";
	
	/**
	 * Nom de la définition de processus
	 */
	private String nomDefinition ="";

	/**
	 * Liste des composants qui composent le processus
	 */
	private Vector listeComp ;
	
	/**
	 * Repertoire de destination du site web généré
	 */
	private String repertoireGeneration = "";

	/**
	 * ID de la définition de processus courante
	 */
	private IdObjetModele idDefProc ;
	
	/**
	 * E mail de l'auteur
	 */
	private String emailAuteur ="webmaster@web.fr";
	
	/**
	 * Fichier de contenu pour décrire la def proc
	 */
	private String ficContenu = "";
	
	/**
	 * Liste des éléments à générer
	 */
	private Vector listeAGenerer;
	

	public DefinitionProcessus ()
	{
		this.listeComp = new Vector() ;
		this.listeAGenerer = new Vector();
		this.nomDefinition = Application.getApplication().getTraduction("nomDefinitionDefaut");
		this.idDefProc = new IdObjetModele(this);
		this.repertoireGeneration = new String(Application.getApplication().getConfigPropriete("repertoire_generation"));
	}

	public void ajouterComposant (ComposantProcessus comp)
	{
		if (comp != null)
		{
			this.listeComp.add (comp.getIdComposant()) ;
			// indiquer à tous les observeurs que la définition a change
			this.maj("COMPOSANT_INSERTED");
			// l'ajouter dans la liste à generer
			this.listeAGenerer.add(comp.getIdComposant());
		}
	}
	
	public void retirerComposantTous()
	{
		if (listeComp != null)
		{
			this.listeComp.removeAllElements();
			this.listeAGenerer.removeAllElements();
		}
	}
	
	public void retirerComposant(IdObjetModele compo)
	{
		if (listeComp != null)
		{
			this.maj("COMPOSANT_REMOVED");
			this.listeComp.removeElement(compo);
			this.listeAGenerer.removeElement(compo);
		}
	}
	
	//------------------------------------------------------------------//
	// Getters et setters												//
	//------------------------------------------------------------------//

	public String getNomDefProc()
	{
		return this.nomDefinition ;
	}
	public void setNomDefProc(String nom)
	{
		if (! this.nomDefinition.equals(nom))
		{
			this.nomDefinition = nom ;
			// indiquer à tous les observeurs que la définition a change
			this.maj("CHANGED");
		}
	}
	public void setFicContenu(String fichier)
	{
		if (! this.ficContenu.equals(fichier))
		{
			this.ficContenu = fichier;
			this.maj("CHANGED");
		}
	}
	public String getFichierContenu()
	{
		return this.ficContenu;
	}
	public String getAuteur()
	{
		return this.auteur;
	}
	public String getCommentaires()
	{
		return this.commentaires;
	}
	public void setAuteur(String auteur)
	{
		if (! this.auteur.equals(auteur))
		{
			this.auteur = auteur;
			this.maj("CHANGED");
		}
	}
	public void setCommentaires(String string)
	{
		if (! this.commentaires.equals(string))
		{
			this.commentaires = string;
			this.maj("CHANGED");
		}
	}
	public IdObjetModele getIdDefProc()
	{
		return this.idDefProc;
	}	
	public Vector getListeComp()
	{
		return this.listeComp ;
	}
	public Vector getListeAGenerer()
	{
		return this.listeAGenerer;
	}
	public void setListeAGenerer(Vector liste)
	{
		if (! this.listeAGenerer.equals(liste))
		{
			this.listeAGenerer = liste;
			this.maj("CHANGED");
		}
	}
	
	public String getRepertoireGeneration()
	{
		if ( this.repertoireGeneration.equals(""))
		{
			return (".");
		}
		else
		{
			return this.repertoireGeneration; 
		} 
	}
	public void setRepertoireGeneration(String string)
	{
		if (! this.repertoireGeneration.equals(string))
		{
			this.repertoireGeneration = string;
			this.maj("CHANGED");
		}
	}
	public String toString()
	{
		return this.nomDefinition;
	}
	public void maj(String code)
	{
		this.setChanged();
		this.notifyObservers(code);
	}
	public String getEmailAuteur() {
		return emailAuteur;
	}
	public void setEmailAuteur(String string) 
	{
		if (! this.emailAuteur.equals(string))
		{
			this.emailAuteur = string;
			this.maj("CHANGED");
		}
	}
	
	//--------------------------------------------------------------------------//
	// Implementation de l'interface ObjetModele								//
	//--------------------------------------------------------------------------//

	public String toString(int numrang, int numtype)
	{
		return this.nomDefinition;
	}
	
	public boolean estDefProc(int numrang, int numtype)
	{
		return true ;
	}
		
	/* (non-Javadoc)
	 * @see iepp.domaine.ObjetModele#getNbFils(int)
	 */
	public int getNbFils(int numrang, int numtype)
	{
		return this.listeComp.size();
	}

	/* (non-Javadoc)
	 * @see iepp.domaine.ObjetModele#getFils(int, int, int)
	 */
	public IdObjetModele getFils(int ieme, int numrang, int numtype)
	{
		return ((IdObjetModele)this.listeComp.elementAt(ieme)) ;
	}

	/* (non-Javadoc)
	 * @see iepp.domaine.ObjetModele#setNomElement(java.lang.String, int, int)
	 */
	public void setNomElement(String chaine, int i, int j)
	{
		if (! this.nomDefinition.equals(chaine))
		{
			this.nomDefinition = chaine ;
			this.maj("CHANGED");
		}
	}
	

}
