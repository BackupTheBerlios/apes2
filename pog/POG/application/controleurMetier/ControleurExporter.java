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
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.util.Vector;

import POG.interfaceGraphique.action.Systeme;
import POG.interfaceGraphique.utile.trace.Debug;
import POG.objetMetier.ElementPresentation;
import POG.utile.PogToolkit;
import POG.utile.ZIP;
/**
 * @stereotype control
 */
public class ControleurExporter extends ControleurSemantique {

  private Systeme lnkSysteme;

  public ControleurExporter(ControleurPresentation ctrlpres, Systeme sys) {
    super(ctrlpres);
    lnkSysteme = sys;
  }

  public boolean exporterPresentation(File destination, Debug lnkDebug) {
    File ff = null;
    try{
        String ancienuserdir = System.getProperty("user.dir");
		int j = 0;
        while ((ff = new File(System.getProperty("user.dir") + File.separator + "tempEXP" + j)).exists())
        	j++;
        ff.mkdirs();

        //Suppression de l'ancienne archive et creation de la nouvelle
        String pathArchive = new String(destination.getAbsolutePath());
        if (PogToolkit.fileExists(pathArchive))
        {
          (new File(pathArchive)).delete();
        }

        //Creation du fichier de sauvegarde
        System.setProperty("user.dir", ff.getAbsolutePath());
        File sauvegarde = new File(ff.getAbsolutePath() + File.separator + "Presentation.xml");
        FileOutputStream sortie = new FileOutputStream(sauvegarde);

        //Creation de la sauvegarde de la presentation
        lnkControleurPresentation.getlnkPresentation().sauver(new OutputStreamWriter(sortie, "UTF-8"), true);

        sortie.close();

        // et duplication des fichiers
        Object [] listFichiers = dupliquerFichiers(ff.getAbsolutePath());


        //Creation de l'archive
        ZIP archive = new ZIP(ff.getAbsolutePath() + File.separator + lnkControleurPresentation.getlnkPresentation().get_nomPresentation().substring(0, lnkControleurPresentation.getlnkPresentation().get_nomPresentation().length()) + ".pre");

		// Duplication du dossier images
		File fima = new File(lnkControleurPresentation.getlnkPresentation().lnkBibliotheque.getAbsolutePath() + File.separator + "images");
		if (fima.exists() && fima.isDirectory()) {
			(new File(ff.getAbsolutePath() + File.separator + "images")).mkdirs();
			File [] sousf = fima.listFiles();
			for (int i = 0; i < sousf.length; i++) {
				lnkDebug.patienter("duplimg", i, sousf.length);
				if (sousf[i].isFile()) {
					PogToolkit.copyFile(sousf[i].getAbsolutePath(), ff.getAbsolutePath() + File.separator + "images" + File.separator + sousf[i].getName());
					archive.ajouteFichier(new String("images" + File.separator + sousf[i].getName()), "Fichier images");
				}
			}
		}

        if (lnkControleurPresentation.get_pathModele() != null) {
          //copie le modele la ou on veut l'exporter
          //lnkDebug.patienter("Copie du mod�le...");
          //PogToolkit.copyFile(lnkControleurPresentation.get_pathModele().getAbsolutePath(), ff.getAbsolutePath() + File.separator + lnkControleurPresentation.getlnkPresentation().get_nomPresentation());

		  lnkDebug.patienter("dezipmo", 0, 0);
          ZIP archive2 = new ZIP(lnkControleurPresentation.get_pathModele().getAbsolutePath());
          Vector vv = archive2.contenu();
          archive2.deziper(ff);

		  lnkDebug.patienter("metmo", 0, 0);
          for (int k = 0; k < vv.size(); k++) {
            File f = new File( (String) ( (Vector) vv.get(k)).get(0));
            if (f.getName().indexOf(".") != -1)
              archive.ajouteFichier((String)((Vector)vv.get(k)).get(0), "Fichier apes");
          }
        }

        //Ajout des fichiers de la presentation dans l'archive
        archive.ajouteFichier(sauvegarde.getName(), "Fichier xml de sauvegarde de la presentation");

        int index;
        String fileTest;
        if (listFichiers != null)
        for (int i = 0; i < listFichiers.length; i++){
            archive.ajouteFichier((String)listFichiers[i], "Fichier de la presentation");
        }
        // et compression
        lnkDebug.patienter("compression", 0, 0);
        archive.ziper();

        PogToolkit.renameFile(archive.getFichier(), destination.getAbsolutePath());

        System.setProperty("user.dir", ancienuserdir);

		lnkDebug.patienter("", 0, 0);

        try {
          PogToolkit.delFile(ff.getAbsolutePath());
        }
        catch (Exception eee) {
          throw new DelException();
        }

        //fin
        return true;
    }
    catch (DelException d) {
      lnkDebug.debogage(lnkSysteme.lnkFenetrePrincipale.getLnkLangues().valeurDe("dossiertemp").replaceFirst("ARG0", ff.getAbsolutePath()));
      return true;
    }
    catch(Exception e){
      e.printStackTrace();
      lnkDebug.debogage(e.getMessage());
      return false;
    }
  }

  private Object[] dupliquerFichiers(String destination){
    Object[] listElmtPres = lnkControleurPresentation.getlnkPresentation().listeElementPresentation();
    Vector listFichiers = new Vector();
    String source;
    String dest;
    int index;

    for(int i = 0; i < listElmtPres.length; i++){
    	lnkSysteme.lnkFenetrePrincipale.getLnkDebug().patienter("duplfich", i, listElmtPres.length);
      if (((ElementPresentation)listElmtPres[i]).getContenu() != null) {
        source = ( (ElementPresentation) listElmtPres[i]).getContenu().getAbsolutePath();
        index = source.lastIndexOf(File.separator);
        dest = destination + File.separator + "Contenu" + File.separator + ((ElementPresentation)listElmtPres[i]).getContenu().get_uri();
        //Si le fichier n'a pas ete duplique, le dupliquer
        if(!PogToolkit.fileExists(dest))
          PogToolkit.copyFile(source, dest);
          //Si le fichier n'est pas present dans la liste des fichiers de la presentation
          //l'inserer
        String str = new String("Contenu" + File.separator + source.substring(index + 1));
        if (!listFichiers.contains(str))
          listFichiers.add(str);
      }
      if (((ElementPresentation) listElmtPres[i]).get_icone() != null) {
        source = ((ElementPresentation) listElmtPres[i]).get_icone().toString();
        index = source.lastIndexOf(File.separator);
        if (index == -1)
          index = source.lastIndexOf("/");
        dest = destination + File.separator + "Icone" + File.separator + source.substring(index + 1);

        if (!PogToolkit.fileExists(source))
          try {
            String str = lnkControleurPresentation.lnkPreferences.getCheminIcon(((ElementPresentation)listElmtPres[i]).get_icone());
            index = str.lastIndexOf(File.separator);
            if (index == -1)
              index = str.lastIndexOf("/");
            String chem = "Icone" + File.separator + str.substring(index + 1);
            dest = destination + File.separator + chem;
            InputStream in = ClassLoader.getSystemResourceAsStream(str);
            if (!PogToolkit.fileExists(dest)) {
              File ff = new File(dest);
              ff.getParentFile().mkdirs();
              ff.createNewFile();
            }
            FileOutputStream fo = new FileOutputStream(dest);
            PogToolkit.extractStream(in, fo);
            in.close();
            fo.close();
            if (!listFichiers.contains(chem))
              listFichiers.add(chem);
              // Attention : la suite n'est pas execute
            continue;
          }
          catch (Exception ee) {
            ee.printStackTrace();
          }

        if(!PogToolkit.fileExists(dest)) {
          PogToolkit.copyFile(source, dest);
        }
        String str = new String("Icone" + File.separator + source.substring(index + 1));
        if (!listFichiers.contains(str))
            listFichiers.add(str);
      }
    }
    return listFichiers.toArray();
  }

  public boolean verifierPresentation(Debug lnkDebug) {
    return lnkControleurPresentation.getlnkPresentation().estValide(lnkDebug);
  }

  class DelException extends Exception {

  }
}