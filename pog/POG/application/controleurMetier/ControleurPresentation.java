/*
 * POG
 * Copyright (C) 2004 Team POG
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
 */


/* Generated by Together */

package POG.application.controleurMetier;

import java.io.File;

import javax.swing.ImageIcon;

import org.ipsquad.apes.model.spem.process.components.ProcessComponent;

import POG.objetMetier.Contenu;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Presentation;
import POG.objetMetier.PresentationElementModele;
import POG.utile.PogToolkit;
import POG.utile.propriete.Preferences;

/**
 * @stereotype control
 */
public class ControleurPresentation {
  /**
   *
   * @directed
   *
   * @supplierCardinality 0..1
   *
   */
  private Presentation lnkPresentation = null;
  public Preferences lnkPreferences = null;

  private File _pathModele;

  public ControleurPresentation(Preferences pref) {
    lnkPreferences = pref;
    //lnkPresentation = new Presentation("toto", System.getProperty("user.home"), new ImageIcon());
  }

  /**
   *
   * @param pathBibli chemin bibliotheque
   * @param nomPres nom presentation
       * @param type Le type de la racine de la presentation (composant ou paquetage)
   */
  public void nouvellePresentation(File pathBibli, String nomPres) {
    //pour SansModele
    _pathModele = null;
    lnkPresentation = new Presentation(nomPres, pathBibli.getAbsolutePath(),
                                       lnkPreferences.
                                       getIconeDefaut(lnkPreferences.get_defIcoPack()));
    lnkPresentation._pathModele = null;
  }

  public void nouvellePresentation(ProcessComponent monproc, String pathBibli,
                                   File pathModele) {
    //pour AvecModele
    _pathModele = pathModele;
    lnkPresentation = new Presentation(monproc.getName(), pathBibli,
                                       lnkPreferences.getIconeDefaut(lnkPreferences.get_defIcoPack()), monproc);
    lnkPresentation._pathModele = this._pathModele.getAbsolutePath();
  }

  public Presentation getlnkPresentation() {
    return lnkPresentation;
  }

  public void majLiensFichiers(String orig, String dest) {
    // Change les liens de tous les contenus concernes
    if (this.lnkPresentation != null) {
      Object[] listeElements = this.lnkPresentation.listeElementPresentation();
      for (int i = 0; i < listeElements.length; i++) {
        Contenu c = ( (ElementPresentation) listeElements[i]).getContenu();
        if (c != null && c.getAbsolutePath().equals(orig))
          c.setFile(new File(dest));
      }
    }

    // Renomme le fichier
    PogToolkit.renameFile(orig, dest);
  }

  public boolean estFichierAffecte(String f)
  {
    if (this.lnkPresentation != null) {
      Object[] listeElements = this.lnkPresentation.listeElementPresentation();
      for (int i = 0; i < listeElements.length; i++) {
        Contenu c = ( (ElementPresentation) listeElements[i]).getContenu();
        if (c != null && c.getAbsolutePath().equals(f))
          return (true);
      }
    }
    return (false);
  }

  /**
   * Supprime toute reference a l'icone specifie (restaure l'icone par defaut de l'element)
   * @param icone Icone que l'on souhaite dereferencer
   */
  public void supprimerLienIcone(ImageIcon icone)
  {
    if (this.lnkPresentation != null) {
      Object[] listeElements = this.lnkPresentation.listeElementPresentation();
      for (int i = 0; i < listeElements.length; i++)
      {
        ElementPresentation courant = (ElementPresentation) listeElements[i] ;
        if (courant.get_icone().getImage().equals(icone.getImage()))
          if (courant instanceof PresentationElementModele)
            courant.set_icone(this.lnkPreferences.getIconeDefaut(((PresentationElementModele)courant).getLnkModelElement()));
          else
            courant.set_icone(this.lnkPreferences.getIconeDefaut(courant));
      }
    }
  }

  public File get_pathModele() {
    return _pathModele;
  }
}