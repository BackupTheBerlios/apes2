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
package POG.interfaceGraphique.utile.menu;


import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.IOException;

import javax.swing.filechooser.FileFilter;

import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.utile.MyMultiFileFilter;
import POG.utile.PogToolkit;

public class MenuListener implements ActionListener {

  POGMenu pgm;

  public MenuListener(POGMenu p) {
    pgm = p;
  }


 public void actionPerformed(ActionEvent evt) {

   if (evt.getActionCommand().equals(getLangue("Ouvrir"))) {
     MyMultiFileFilter fd = new MyMultiFileFilter(".pog");
     fd.addExt(".pre");
     File pres = PogToolkit.chooseFileWithFilter(pgm.lnkFenetrePrincipale, fd);
     if (pres == null || !(pres.getName().endsWith(".pog") || pres.getName().endsWith(".pre")))
     {
       if (pres == null)
       {
         return;
       }
       pgm.lnkFenetrePrincipale.getLnkDebug().debogage("ouvtype");
       return;
     }

     pgm.lnkFenetrePrincipale.getLnkSysteme().ouvrirPresentation(pres.toString());
//     pgm.lnkFenetrePrincipale.getLnkArbrePresentation().selectionnerRacine();
   }
   else if (evt.getActionCommand().equals(getLangue("Enregistrer"))){
     if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() != null){
       if (this.pgm.lnkFenetrePrincipale.get_pathSave().equalsIgnoreCase(""))
         this.pgm.lnkFenetrePrincipale.getLnkFenetreEnregistrerSous().setVisible(true);
       else
         pgm.lnkFenetrePrincipale.getLnkSysteme().enregistrerPresentation();
     }
     else
       pgm.lnkFenetrePrincipale.getLnkDebug().debogage("presvide");
   }else if (evt.getActionCommand().equals(getLangue("enregistrersous"))){
     if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() !=null)
       pgm.lnkFenetrePrincipale.getLnkFenetreEnregistrerSous().setVisible(true);
     else
       pgm.lnkFenetrePrincipale.getLnkDebug().debogage("presvide");
   } else if (evt.getActionCommand().equals(getLangue("apes")))   {
     	new FenetrePrincipale.TheTraitement("APES") {
			public void traitement() {
				String argvApes = new String("");
				if ((pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() != null)
					&& (pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().get_pathModele() != null))
				argvApes = pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().get_pathModele().getPath();
				String str = pgm.lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().getPathApes();
				str = "java -jar \"" + str + "\" \"" + argvApes + "\"";
				System.out.println(str);
				try {
					final Process pp = Runtime.getRuntime().exec(str);
					new FenetrePrincipale.TheTraitement("SuiviAPES") {
						public void traitement() {
							int c;
							try {
								while ((c = pp.getInputStream().read()) != -1)
									System.out.write(c);
							} catch (IOException e) {
								e.printStackTrace();
							}
						}
					};
					int c;
					while ((c = pp.getErrorStream().read()) != -1)
						System.out.write(c);
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
     	};     	
   }//*/
/*   else if (evt.getActionCommand().equals(getLangue("ExporterIEPP")))
     Exporter.versIepp(pgm.lnkFenetrePrincipale.getLnkSysteme().getLnkControleurExporter(),pgm.lnkFenetrePrincipale.getLnkDebug());*/
   else if (evt.getActionCommand().equals(getLangue("Quitter")))
     pgm.lnkFenetrePrincipale.getLnkSysteme().quitter();
   else if (evt.getActionCommand().equals(getLangue("Preferences")))
     pgm.lnkFenetrePrincipale.afficherFenetrePreferences();
   else if (evt.getActionCommand().equals(getLangue("viderlog")))
     pgm.lnkFenetrePrincipale.getLnkDebug().clearTexte();
   else if (evt.getActionCommand().equals(getLangue("Exporter")))
   {
     if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() !=null)
     {
       if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().lnkProcessComponent == null
          || this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().lnkProcessComponent.getValidate() != 0
          || PogToolkit.askYesNoQuestion(getLangue("apesmodelenonvaliderquestioncontinuer"), false, this.pgm.lnkFenetrePrincipale) == PogToolkit._YES)
       {
         pgm.lnkFenetrePrincipale.getLnkFenetreExport().setVisible(true);
       }
     }
     else
       pgm.lnkFenetrePrincipale.getLnkDebug().debogage("presvide");
   }
   else if (evt.getActionCommand().equals(getLangue("EditeurHTML")))
     pgm.lnkFenetrePrincipale.getLnkappliTest().lancement();
   else if (evt.getActionCommand().equals(getLangue("Nouveau")))
     pgm.lnkFenetrePrincipale.getLnkFenetreOuverture().setVisible(true);
   else if (evt.getActionCommand().equals(getLangue("SansModele"))){
     pgm.lnkFenetrePrincipale.getLnkFenetreOuverture().setVisible(true);
   }
   else if (evt.getActionCommand().equals(getLangue("AvecModele"))) {
     pgm.lnkFenetrePrincipale.getLnkFenetreNouvellePresentationAvecModele().setVisible(true);
   }
   else if (evt.getActionCommand().equals(getLangue("apropos")))
      pgm.lnkFenetrePrincipale.getLnkFenetreAPropos().setVisible(true);
   else if (evt.getActionCommand().equals(getLangue("AjouterElementPresentation")))
     pgm.lnkFenetrePrincipale.getLnkSysteme().ajouterElementPre();
   else if (evt.getActionCommand().equals(getLangue("Synchroniser")))
   {
     if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() !=null)
       pgm.lnkFenetrePrincipale.getLnkArbreExplorateur().load();
     else
       pgm.lnkFenetrePrincipale.getLnkDebug().debogage("presvide");
   }
   else if (evt.getActionCommand().equals(getLangue("syncapes")))
     {
       if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().get_pathModele() == null)
       {
         this.pgm.lnkFenetrePrincipale.getLnkDebug().afficher("presnonlier");
       }
       else {
         pgm.lnkFenetrePrincipale.getLnkSysteme().synchroniserApes();
       }
     }
  else if (evt.getActionCommand().equals(getLangue("VerifierCoherence")))
  {
    if (this.pgm.lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() != null)
      pgm.lnkFenetrePrincipale.getLnkSysteme().verifiePresentation();
    else
       pgm.lnkFenetrePrincipale.getLnkDebug().debogage("presvide");
  }
   else if (evt.getActionCommand().equals(getLangue("ManuelUtilisation")))
     POG.utile.HtmlViewer.mymain(pgm.lnkFenetrePrincipale);
   else if (evt.getActionCommand().equals(getLangue("extraire")))
     pgm.lnkFenetrePrincipale.getLnkSysteme().extraireIcone();
   else
     pgm.lnkFenetrePrincipale.getLnkDebug().afficher(evt.getActionCommand() + " : Non pris en compte...");


 }

 private class MyFileFilter extends FileFilter{
   private String _ext = null;
   public MyFileFilter(String ext){
     super();
     _ext = ext;
   }

   public boolean accept(File fich){
     if (fich.isDirectory())
       return true;
     else
       return fich.getName().endsWith(_ext);
   }
   public String getDescription(){
       return _ext;
   }
 }

	private String getLangue(String cle) {
 		return pgm.lnkFenetrePrincipale.getLnkLangues().valeurDe(cle);
	}

}