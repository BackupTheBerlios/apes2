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


package POG.utile;

import java.awt.Component;
import java.awt.Container;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Toolkit;
import java.awt.Window;
import java.awt.image.BufferedImage;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.util.Calendar;
import java.util.Enumeration;
import java.util.Stack;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.filechooser.FileFilter;

import POG.interfaceGraphique.fenetre.FenetrePrincipale;

/**
 *  Classe de type "bo\uFFFDte \uFFFD outils" : contient des
 *  m\uFFFDthodes et des attributs statiques d'utilit\uFFFD
 *  diverses et vari\uFFFDes
 */
public class PogToolkit
{
  /** Nom de l'application */
  public static final String _APP_NAME = "POG";

  /**Chemin de l'application*/
  public static String _PATH_APPLI;

  /** Police de l'application */
  public static final Font _APP_FONT = new Font("Tahoma", Font.PLAIN, 11);

  /** Police graphique de l'application */
  public static final Font _APP_GRAPH_FONT = new Font("Tahoma", Font.PLAIN, 10);

  public static final String _APP_EXT = new String(".apes");

  /** Constante indiquant que l'utilisateur a r\uFFFDpondu "Oui" \uFFFD la question pos\uFFFDe */
  public static final int _YES = 0;

  /** Constante indiquant que l'utilisateur a r\uFFFDpondu "Non" \uFFFD la question pos\uFFFDe */
  public static final int _NO = 1;

  /** Constante indiquant que l'utilisateur a r\uFFFDpondu "Annuler" \uFFFD la question pos\uFFFDe */
  public static final int _CANCEL = 2;

  /** FileChooser qui sera utilis\uFFFD pour que l'utilisateur choisisse un fichier */
  private static JFileChooser _FILE_CHOOSER_BIBLI = null;
  private static JFileChooser _FILE_CHOOSER = null;

  public final static String jpeg = ".jpeg";
  public final static String jpg = ".jpg";
  public final static String gif = ".gif";
  public final static String tiff = ".tiff";
  public final static String tif = ".tif";
  public final static String png = ".png";

  static public File chooseFileInBibli(Component fenetre) {
    _FILE_CHOOSER_BIBLI = new JFileChooser(new File(FenetrePrincipale.INSTANCE.
                                                    getLnkSysteme().
                                                    getlnkControleurPresentation().
                                                    getlnkPresentation().
                                                    lnkBibliotheque.
                                                    getAbsolutePath()));
    _FILE_CHOOSER_BIBLI.setFileSelectionMode(JFileChooser.FILES_ONLY);
    _FILE_CHOOSER_BIBLI.setCurrentDirectory(new File(FenetrePrincipale.INSTANCE.getLnkSysteme().getlnkControleurPresentation().
                                                getlnkPresentation().lnkBibliotheque.getAbsolutePath()));
    if (_FILE_CHOOSER_BIBLI.showOpenDialog(fenetre)== JFileChooser.APPROVE_OPTION) {
      return (_FILE_CHOOSER_BIBLI.getSelectedFile());
    }
    return null;
  }

  static public File chooseFileWithFilter(Component fenetre, MyMultiFileFilter filters, String chemin){
  	creerFileChooser(chemin);
    _FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
    _FILE_CHOOSER.addChoosableFileFilter((FileFilter)filters);
    if (_FILE_CHOOSER.showOpenDialog(fenetre)== JFileChooser.APPROVE_OPTION) {
      return (_FILE_CHOOSER.getSelectedFile());
    }
    _FILE_CHOOSER.removeChoosableFileFilter(filters);
    return null;
  }

  static public File [] chooseMultipleFile(Component fenetre, String oldPath) {
    _FILE_CHOOSER = new JFileChooser(new File(oldPath));
    _FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
    _FILE_CHOOSER.setMultiSelectionEnabled(true);
    if (_FILE_CHOOSER.showOpenDialog(fenetre)== JFileChooser.APPROVE_OPTION) {
      return (_FILE_CHOOSER.getSelectedFiles());
    }
    return null;
  }

  static public File chooseFileAPES(Component fenetre, String dossier) {
	creerFileChooser(dossier);
    MyFileFilter fileFilter = new MyFileFilter(PogToolkit._APP_EXT);
    _FILE_CHOOSER.addChoosableFileFilter((FileFilter)fileFilter);
    _FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
    if (_FILE_CHOOSER.showOpenDialog(fenetre)== JFileChooser.APPROVE_OPTION) {
      return (_FILE_CHOOSER.getSelectedFile());
    }
    _FILE_CHOOSER.removeChoosableFileFilter(fileFilter);
    return null;
  }

  static public File chooseFileToSave(Component fenetre, MyMultiFileFilter filters, String nomFile) {
	creerFileChooser(nomFile);
    _FILE_CHOOSER.setFileSelectionMode(JFileChooser.FILES_ONLY);
    _FILE_CHOOSER.addChoosableFileFilter( (FileFilter) filters);
	_FILE_CHOOSER.setSelectedFile(new File(nomFile));
    if (_FILE_CHOOSER.showSaveDialog(fenetre) == JFileChooser.APPROVE_OPTION) {
      return (_FILE_CHOOSER.getSelectedFile());
    }
    _FILE_CHOOSER.removeChoosableFileFilter(filters);
    return null;
}

  static public File chooseDirectory(Component fenetre, String prevDir) {
	creerFileChooser(prevDir);
    _FILE_CHOOSER.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
    if (_FILE_CHOOSER.showOpenDialog(fenetre)== JFileChooser.APPROVE_OPTION) {
      return (_FILE_CHOOSER.getSelectedFile());
    }
    return null;
  }

	private static void creerFileChooser(String fichier) {
		File dopen = new File(fichier);
		boolean ok = dopen.exists();
		if (!ok)
			if (dopen.getParentFile() != null) {
				dopen = dopen.getParentFile();
				ok = dopen.exists();
			}
		if (!ok)
			dopen = new File(System.getProperty("user.dir"));
		_FILE_CHOOSER = new JFileChooser(dopen);
	}

  /** Retourne la largeur de l'\uFFFDcran, en pixel
   *  @return  Largeur de l'\uFFFDcran, en pixel
   */
  public static int getScreenWidth()
  {
    return Toolkit.getDefaultToolkit().getScreenSize().width;
  }


  /** Retourne la hauteur de l'\uFFFDcran, en pixel
   *  @return  Hauteur de l'\uFFFDcran, en pixel
   */
  public static int getScreenHeight()
  {
    return Toolkit.getDefaultToolkit().getScreenSize().height;
  }


  /** Centre la fen\uFFFDtre sp\uFFFDcifi\uFFFDe au milieu de l'\uFFFDcran
   *  @param win  R\uFFFDf\uFFFDrence \uFFFD la fen\uFFFDtre \uFFFD centrer au milieu de l'\uFFFDcran (JFrame ou JDialog)
   */
  public static void centerWindow(Window window)
  {
    Dimension screenSize = Toolkit.getDefaultToolkit().getScreenSize();
    Dimension winSize = window.getSize();

    window.setLocation((screenSize.width - winSize.width) / 2,
                    (screenSize.height - winSize.height) / 2);
  }


  /** Affiche un message d'erreur
   *  @param errMsg  Message d'erreur \uFFFD afficher
   *  @param msgBoxTitle  Titre de la bo\uFFFDte de dialogue qui affichera le message d'erreur
   *  @param parentWindow  Fen\uFFFDtre parent (JFrame ou JDialog qui veut afficher le message) - <I>(peut valoir null)</I>
   */
  public static void showErrorMsg(String errMsg, String msgBoxTitle, Window parentWindow)
  {
    JOptionPane.showMessageDialog(parentWindow, errMsg, msgBoxTitle, JOptionPane.ERROR_MESSAGE);
  }


  /** Affiche un message d'erreur avec comme titre "[titre_application] : Erreur"
   *  @param errMsg  Message d'erreur \uFFFD afficher
   *  @param parentWindow  Fen\uFFFDtre parent (JFrame ou JDialog qui veut afficher le message) - <I>(peut valoir null)</I>
   */
  public static void showErrorMsg(String errMsg, Window parentWindow)
  {
    showErrorMsg(errMsg, _APP_NAME + " : Erreur", parentWindow);
  }

  /** Affiche un message avec comme titre "[titre_application]"
   *  @param msg  Message \uFFFD afficher
   *  @param parentWindow  Fen\uFFFDtre parent (JFrame ou JDialog qui veut afficher le message) - <I>(peut valoir null)</I>
   */
  public static void showMsg(String msg, Window parentWindow)
  {
    JOptionPane.showMessageDialog(parentWindow, msg, _APP_NAME, JOptionPane.INFORMATION_MESSAGE);
  }



  /** Pose une question de type "Oui/Non" ou "Oui/Non/Annuler" \uFFFD l'utilisateur
   *  @param question  Question \uFFFD poser
   *  @param msgBoxTitle  Titre de la bo\uFFFDte de dialogue qui posera la question
   *  @param showCancelButton  <B>true</B> pour afficher un bouton "Annuler", <B>false</B> sinon.
   *  @param parentWindow  Fen\uFFFDtre parent (JFrame ou JDialog qui veut afficher le message) - <I>(peut valoir null)</I>
   *  @return  Une des 3 constantes parmi : OpToolkit._YES, OpToolkit._NO, et OpToolkit._CANCEL, en fonction de la r\uFFFDponse donn\uFFFDe par l'utilisateur
   */
  public static int askYesNoQuestion(String question, String msgBoxTitle, boolean showCancelButton, Window parentWindow)
  {
    int optionType = (showCancelButton) ? JOptionPane.YES_NO_CANCEL_OPTION : JOptionPane.YES_NO_OPTION;

    switch (JOptionPane.showConfirmDialog(parentWindow, question, msgBoxTitle, optionType))
    {
      case JOptionPane.YES_OPTION : return _YES;
      case JOptionPane.NO_OPTION : return _NO;
      case JOptionPane.CANCEL_OPTION : return _CANCEL;
      default : return -1; // \uFFFDa, \uFFFDa devrait pas arriver souvent...
    }
  }


  /** Pose une question de type "Oui/Non" ou "Oui/Non/Annuler" \uFFFD l'utilisateur<BR>
   *  Le titre de la bo\uFFFDte de dialogue sera "[titre_application] : Question"
   *  @param question  Question \uFFFD poser
   *  @param showCancelButton  <B>true</B> pour afficher un bouton "Annuler", <B>false</B> sinon.
   *  @param parentWindow  Fen\uFFFDtre parent (JFrame ou JDialog qui veut afficher le message) - <I>(peut valoir null)</I>
   *  @return  Une des 3 constantes parmi : OpToolkit._YES, OpToolkit._NO, et OpToolkit._CANCEL, en fonction de la r\uFFFDponse donn\uFFFDe par l'utilisateur
   */
  public static int askYesNoQuestion(String question, boolean showCancelButton, Window parentWindow)
  {
    return askYesNoQuestion(question, _APP_NAME + " : Question", showCancelButton, parentWindow);
  }


  /** D\uFFFDcompresse le fichier ZIP sp\uFFFDcifi\uFFFD dans le r\uFFFDpertoire sp\uFFFDcifi\uFFFD
   * @param zipFileName  Fichier ZIP \uFFFD d\uFFFDcompresser
   * @param outputDirectory  R\uFFFDpertoire de destination
   * @param parentWindow  Window qui servira de fen\uFFFDtre parent
   * @param progressBar  JProgressBar qui servira d'indicateur d'avancement, ou <B>null</B> si non utilis\uFFFD. L'avancement en % sera : (avancement / progressFactor) + progressDelta
   * @param stateLabel  JLabel qui servira d'indicateur d'avancement, ou <B>null</B> si non utilis\uFFFD
   * @param progressFactor  Nombre par lequel la valeur d'avancement (en %) sera divis\uFFFDe (ex: 2.0 pour arriver \uFFFD 50% \uFFFD la fin)
   * @param progressDelta  Nombre que l'on ajoutera \uFFFD la valeur d'avancement (en %) (ex: 50 pour commencer \uFFFD 50%)
   * @return  <B>true</B> si tout c'est bien pass\uFFFD, <B>false</B> sinon
   */
  public static boolean extractZipFile(String zipFileName, String outputDirectory, Window parentWindow, JProgressBar progressBar, JLabel stateLabel, float progressFactor, int progressDelta)
  {
    final int BUFFER_SIZE = 2048;

    // Teste l'existance du fichier \uFFFD d\uFFFDcompresser
    if (PogToolkit.fileExists(zipFileName) == false)
    {
      File zipFile = new File(zipFileName);
      PogToolkit.showErrorMsg("Erreur lors de la d\uFFFDcompression des fichiers statiques du site :\n"
                   + "Le fichier \uFFFD d\uFFFDcompresser (\"" + zipFile.getAbsoluteFile() + "\") est introuvable.", "Erreur", parentWindow);
      return false;
    }

    try
    {
      BufferedOutputStream dest = null;
      BufferedInputStream is = null;
      ZipEntry entry;
      ZipFile zipfile = new ZipFile(zipFileName);
      Enumeration e = zipfile.entries();
      String currEntryName = null, stateString = null;
      int nbZipEntries = zipfile.size();
      int cpt = 0, pct = 0;

      outputDirectory = PogToolkit.getSlashTerminatedPath(outputDirectory);

      // D\uFFFDcompresse un par un chaque \uFFFDlement (i.e. fichier ou r\uFFFDpertoire) de l'archive
      while (e.hasMoreElements())
      {
        entry = (ZipEntry) e.nextElement();
        currEntryName = outputDirectory + entry.getName();

        // Si cet \uFFFDl\uFFFDment est un r\uFFFDpertoire, on v\uFFFDrifit qu'il existe, s'il n'existe pas, on le cr\uFFFD\uFFFD
        if (entry.isDirectory())
        {
          if (PogToolkit.fileExists(currEntryName) == false)
          {
            stateString = "[" + (cpt+1) + "/" + nbZipEntries + "] : creating directory \"" + currEntryName + "\"...";
            if (stateLabel != null) stateLabel.setText(stateString);
            if (PogToolkit.createFolder(currEntryName) == null) return false;
          }
        }
        else
        {
          stateString = "[" + (cpt+1) + "/" + nbZipEntries + "] : extracting \"" + currEntryName + "\"...";
          if (stateLabel != null) stateLabel.setText(stateString);
          is = new BufferedInputStream (zipfile.getInputStream(entry));
          int count;
          byte data[] = new byte[BUFFER_SIZE];
          FileOutputStream fos = new FileOutputStream(currEntryName);
          dest = new BufferedOutputStream(fos, BUFFER_SIZE);

          while ((count = is.read(data, 0, BUFFER_SIZE)) != -1)
          {
            dest.write(data, 0, count);
          }

          dest.flush();
          dest.close();
          is.close();
          fos.close();
        }

        cpt++;

        // On met \uFFFD jour la jauge (barre de progression)
        if (progressBar != null)
        {
          pct = (cpt * 100) / nbZipEntries;
          pct = (int) ((pct / progressFactor) + progressDelta);
          progressBar.setValue(pct);
          progressBar.setString(pct + " %");
        }
      }

      zipfile.close();
      return true;
    }
    catch(Exception e)
    {
      e.printStackTrace();
      PogToolkit.showErrorMsg("Erreur lors de la d\uFFFDcompression des fichiers statiques du site :\n" + e.getMessage(),
                   "Erreur", parentWindow);
      return false;
    }
  }


  /** Retourne la valeur bool\uFFFDenne de "l'OS utilis\uFFFD est de la famille Win32"
   *@return  <b>true</b> si l'OS utilis\uFFFD est de la famille Win32, ou <b>false</b> sinon
   */
  public static boolean currOSIsWin32()
  {
    String osName = System.getProperty("os.name").toUpperCase();
    return (osName.indexOf("WINDOW") != -1);
  }


  /** Supprime les "../" (ou "..\") dans le chemin sp\uFFFDcifi\uFFFD.
   *  ex : removeDotDotInPath("progs/alpaga/../data") retournera la cha\uFFFDne "progs/data"
   *@param path   Chemin dans lequel les "../" devront \uFFFDtre supprim\uFFFDs
   *@return  Chemin physiquement identique \uFFFD <b>path</b>, mais ne contenant pas de "../"
   */
  public static String removeDotDotInPath(String path)
  {
    Stack dirs = new Stack();
    Object[] dirsArray;
    int pos = 0, newPos = 0;
    String formattedPath = "", newDir;

    // Remplace tous les "\" par des "/"
    path = path.replace('\\', '/');

    // Rajoute un "/" \uFFFD la fin si c'est pas d\uFFFDj\uFFFD fait
    path = getSlashTerminatedPath(path);

    pos = path.indexOf("/");

    // Si le chemin sp\uFFFDcifi\uFFFD ne contient aucun slash, on ne peut plus rien pour lui....
    if (pos == -1) return (path);

    if (pos == 0)
    {
      formattedPath = "/";
      pos = 1;
    }
    else
      pos = 0;

    while (newPos != -1)
    {
      newPos = path.indexOf("/", pos);
      if (newPos != -1)
      {
        newDir = path.substring(pos, newPos);
        if (newDir.compareTo("..") == 0)
          dirs.pop();
        else
          dirs.push(newDir);
        pos = newPos;
      }
      pos++;
    }

    dirsArray = dirs.toArray();
    for (int i=0 ; i<dirsArray.length ; i++)
      formattedPath += ((i!=0) ? "/" : "") + dirsArray[i].toString();

    return (formattedPath + "/");
  }


  /** Retourne le chemin sp\uFFFDcifi\uFFFD en ajoutant un "/" \uFFFD la fin s'il ne se finissait ni par "/" ni par "\"
   *@param path  Chemin dont on doit retourner une forme normalis\uFFFDe
   *@return  Chemin sp\uFFFDcifi\uFFFD termin\uFFFD par un "/" \uFFFD la fin s'il ne se finissait ni par "/" ni par "\".<BR>
             Retourne "" si <I>path</I> == ""
   */
  public static String getSlashTerminatedPath(String path)
  {
    if (path.equals("")) return "";
    return ( (path.endsWith("/") || path.endsWith("\\")) ? path : (path + "/") );
  }


  /** Cr\uFFFD\uFFFD le r\uFFFDpertoire sp\uFFFDcif\uFFFD
   *@param pathToCreate  R\uFFFDpertoire \uFFFD cr\uFFFDer
   *@return  <b>true</b> si la cr\uFFFDation a r\uFFFDussi avec succ\uFFFDs, ou <b>false</b> sinon
   */
  public static File createFolder(String pathToCreate)
  {
    //A voir : renvoie une chemin egal a null => newDataPath
    File newDataPath = new File(pathToCreate);
    if ((newDataPath != null) && (newDataPath.mkdirs())) return newDataPath;

    //JOptionPane.showMessageDialog(null, "Impossible de creer le repertoire \"" + pathToCreate + "\".");
    else return null;
  }

  /** V\uFFFDrifie que le r\uFFFDpertoire sp\uFFFDcifi\uFFFD existe, et si ce n'est pas le cas, le cr\uFFFD\uFFFD
   *@param folder  R\uFFFDpertoire dont il faut assurer l'existence
   *@return  <B>true</B> si le r\uFFFDpertoire existe ou s'il a \uFFFDt\uFFFD cr\uFFFD\uFFFD avec succ\uFFFDs, ou <B>false</B> sinon
   */
  public static boolean ensureFolderExist(String folder)
  {
    if (folderExists(folder)) return true;

    if (createFolder(folder) == null)
      return false;
    else
      return true;
  }


  /** Retourne le chemin absolu courrant
   *@return  Chemin absolu courrant
   */
  public static String getCurrentAbsoluteDirectory()
  {
    File currDir = new File(".\\");
    String currDirectory = currDir.getAbsolutePath();
    return (currDirectory.substring(0, currDirectory.length()-1));
  }


  /** Retourne le chemin relatif du r\uFFFDpertoire de la donn\uFFFDe point\uFFFD par le chemin absolu sp\uFFFDcifi\uFFFD
   *  Si le r\uFFFDpertoire sp\uFFFDcifi\uFFFD n'est pas un fils de Global.getDataPath() ou si un probl\uFFFDme a \uFFFDt\uFFFD recontr\uFFFD,
   *  le r\uFFFDpertoire sp\uFFFDcifi\uFFFD sera retourn\uFFFD.
   *@param absolutePath  Chemin absolu
   *@param pathRef  Chemin de r\uFFFDf\uFFFDrence \uFFFD partir du quel le chemin relatif sera construit
   *@return  Chemin relatif du r\uFFFDpertoire point\uFFFD par le chemin absolu <i>absolutePath</i> sp\uFFFDcifi\uFFFD
   */
  public static String getRelativePathOfAbsolutePath(String absolutePath, String pathRef)
  {
    String absoluteRefPath = (new File(getSlashTerminatedPath(pathRef))).getAbsolutePath();
    absoluteRefPath = removeDotDotInPath(absoluteRefPath);

    absolutePath = absolutePath.replace('\\', '/');
    absoluteRefPath = absoluteRefPath.replace('\\', '/');

    absolutePath = getSlashTerminatedPath(absolutePath);
    absoluteRefPath = getSlashTerminatedPath(absoluteRefPath);


    // Si la donn\uFFFDe est dans le r\uFFFDpertoire de r\uFFFDf\uFFFDrence, on retourne ce r\uFFFDpertoire
    if (absolutePath.compareTo(absoluteRefPath) == 0) return (getSlashTerminatedPath(pathRef));

    // Si le r\uFFFDpertoire sp\uFFFDcifi\uFFFD n'est pas un fils du r\uFFFDpertoire de r\uFFFDf\uFFFDrence, on abandonne
    // sinon, on retourne le chemin relatif
    if (absolutePath.indexOf(absoluteRefPath) == -1)
      return (absolutePath);
    else
      return (getSlashTerminatedPath(pathRef) + absolutePath.substring(absoluteRefPath.length()));
  }


  /** Retourne la valeur booleenne du test "le fichier dont le nom est pass\uFFFD en param existe"
   *@param fileName  Nom du fichier dont on doit tester l'existence
   *@return  <b>true</b> si le fichier <i>fileName</i> existe, ou <b>false</b> sinon
   */
  public static boolean fileExists(String fileName)
  {
  	File f = new File(fileName);
    return (f.exists() && f.isFile());
  }

  public static void delFile(String fileName){
  	System.gc();
  	System.out.println("DEL: " + fileName);
    File fich = new File(fileName);
    if (fich.exists()) {
        while (! (fich.delete()))
          delFile( (fich.listFiles())[0].getAbsolutePath());
    }
  }


  /** Retourne la valeur booleenne du test "le repertoire dont le nom est pass\uFFFD en param existe"
   *@param folderName  Nom du repertoire dont on doit tester l'existence
   *@return  <b>true</b> si le repertoire <i>folderName</i> existe, ou <b>false</b> sinon
   */
  public static boolean folderExists(String folderName)
  {
    File f = new File(folderName);
    return (f.exists() && f.isDirectory());
  }


  /** Copie un fichier (ascii ou binaire) dans un autre (si le fichier de destination existe, il sera remplac\uFFFD)<BR>
   *  (attention, cette m\uFFFDthode est "faite maison" : elle copie octet par octet le fichier source, donc les performances sont se qu'elles sont...)<BR>
   *  Si les versions du JDK qui succ\uFFFDderont au JDK 1.3 proposent une telle m\uFFFDthode, il serait pr\uFFFDf\uFFFDrable de l'utiliser
   *@param src  Nom du fichier \uFFFD copier
   *@param dest  Nom du fichier dans lequel <i>src</i> doit \uFFFDtre copi\uFFFD
   *@return  <b>true</b> si la copie s'est r\uFFFDalis\uFFFDe sans erreurs, ou <b>false</b> sinon
   */
  public static boolean copyFile(String src, String dest)
  {
    File srcFile = new File(src),
         destFile = new File(dest);

     destFile.getParentFile().mkdirs();

    boolean ret;
    long fileSize;
    int currByte;
    FileOutputStream fos;
    FileInputStream fis;

    if ((srcFile == null) || (destFile == null))
      return (false);

    // Destruction du fichier de destination s'il existe
    if (destFile.exists())
    {
      if (destFile.delete() == false)
      {
        JOptionPane.showInputDialog(null, "Unable to copy file\n" + src + "\nin\n" + dest + "\n\nUnable to delete existing destination file.", _APP_NAME, JOptionPane.ERROR_MESSAGE);
        return (false);
      }
    }

    // Cr\uFFFDation du fichier (vide) de destination
    try
    {
      ret = destFile.createNewFile();
    }
    catch (IOException e)
    {
      ret = false;
    }

    if (ret == false)
    {
      JOptionPane.showInputDialog(null, "Unable to copy file\n" + src + "\nin\n" + dest + "\n\nUnable to create destination file.", _APP_NAME, JOptionPane.ERROR_MESSAGE);
      return (false);
    }

    // Copie du fichier source dans le fichier dest
    try
    {
      fos = new FileOutputStream(destFile);
      fis = new FileInputStream(srcFile);
    }
    catch (FileNotFoundException e)
    {
        JOptionPane.showInputDialog(null,
            "Unexpected error while trying to copy file\n" + src + "\nin\n" +
                                    dest, _APP_NAME, JOptionPane.ERROR_MESSAGE);
        return (false);
    }

      try
      {
        while ((currByte = fis.read()) != -1)
          fos.write(currByte);
      }
      catch (IOException e)
      {
        JOptionPane.showInputDialog(null, "Unexpected error while copying file\n" + src + "\nin\n" + dest, _APP_NAME, JOptionPane.ERROR_MESSAGE);
        return (false);
      }

    try{
      fis.close();
      fos.close();
    }
    catch(Exception e){
      e.printStackTrace();
    }

    return (true);
  }


  /** Renomme un fichier
   *@param src  Nom du fichier \uFFFD renommer
   *@param dest  Nouveau nom du fichier
   *@return  <b>true</b> si le renommage s'est r\uFFFDalis\uFFFD sans erreur, ou <b>false</b> sinon
   */
  public static boolean renameFile(String src, String dest)
  {
    File srcFile = new File(src);
    try
    {
      if (srcFile.renameTo(new File(dest)))
      {
        return (true);
      }
      else
      {
        JOptionPane.showMessageDialog(null,
                                    "Unable to rename file\n" + src + "\nto\n" +
                                    dest, _APP_NAME, JOptionPane.ERROR_MESSAGE);
        return (false);
      }
    }
    catch (Exception e) {
      JOptionPane.showMessageDialog(null, "Unexpected error while renaming file\n" + src + "\nto\n" + dest, _APP_NAME, JOptionPane.ERROR_MESSAGE);
      return (false);
    }
  }


  /** Ex\uFFFDcute une ligne de commande
   *@param commandLine  Ligne de commande \uFFFD ex\uFFFDcuter
   *@param waitForProcess2Finish  Si <b>true</b>, attend que le process ait fini avant de partir en s\uFFFDquence (<B>true</B>=synchrone / <B>false</B>=asynchrone)
   */
  public static void executeCommandLine(String commandLine, boolean waitForProcess2Finish)
  {
    boolean retry = false;

    do
    {
      try
      {
        Process p = Runtime.getRuntime().exec(commandLine);
        if (waitForProcess2Finish)
        {
          p.waitFor();
          p.destroy();
        }
      }
      catch(Exception e)
      {
        if (e instanceof IOException)
          retry = (JOptionPane.showConfirmDialog(null, "IOException error : (specifed program surely doesn't exist...)\n"
                                                + e.getMessage() + "\n\nDo you want to retry ?",
                                                _APP_NAME, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)
                                                 == JOptionPane.YES_OPTION);
        else
          retry = (JOptionPane.showConfirmDialog(null, "Unexpected error :\n" + e.toString()
                                                + "\n\nDo you want to retry ?",
                                                _APP_NAME, JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE)
                                                 == JOptionPane.YES_OPTION);
      }
    }
    while (retry);
  }


  /** Supprimer la sous-cha\uFFFDne sp\uFFFDcifi\uFFFDe dans la cha\uFFFDne sp\uFFFDcifi\uFFFDe
   *@param str  Cha\uFFFDne dans laquelle la sous-cha\uFFFDne <i>strToDel</i> doit \uFFFDtre supprim\uFFFDe
   *@param strToDel  Sous-cha\uFFFDne \uFFFD supprimer de <i>str</i>
   *@return  Cha\uFFFDne <i>str</i> \uFFFD laquelle on aura supprim\uFFFD <i>strToDel</i> (ou retourne <i>str</i> si la sous-cha\uFFFDne <i>strToDel</i> n'a pas \uFFFDt\uFFFD trouv\uFFFDe dans <i>str</i>)
   */
  public static String removeInString(String str, String strToDel)
  {
    int pos = str.indexOf(strToDel);
    if (pos == -1) return (str);
    return (str.substring(0, pos) + str.substring(pos + strToDel.length()));
  }


  /** Demande \uFFFD l'utilisateur de saisir une cha\uFFFDne de caract\uFFFDres (cha\uFFFDne vide non accept\uFFFDe)
   *@param question  Question ou pr\uFFFDcision \uFFFD afficher dans la bo\uFFFDte de JOptionPane
   *@param titre  Titre de la bo\uFFFDte de JOptionPane
   *@return  Cha\uFFFDne saisie ou <b>null</b> si l'utilisateur a annul\uFFFD la saisie
   */
  public static String askForString(String question, String titre)
  {
    String str = "";

    while (str.compareTo("") == 0)
    {
      str = JOptionPane.showInputDialog(null, question, titre, JOptionPane.QUESTION_MESSAGE);
      if (str == null) return null;
      if (str.compareTo("") == 0) JOptionPane.showMessageDialog(null, "Aucune valeur saisie.");
    }
    return (str);
  }


  /** Retourne la date courrante sous la forme jj/mm/aaaa
   *@return  Cha\uFFFDne contenant la date courrante sous la forme jj/mm/aaaa
   */
  public static String getCurrDate()
  {
    int dayOfMonth, monthOfYear, year;
    Calendar date = Calendar.getInstance();
    dayOfMonth =  date.get(Calendar.DAY_OF_MONTH);
    monthOfYear =  date.get(Calendar.MONTH) + 1;
    year =  date.get(Calendar.YEAR);
    return ("" + ((dayOfMonth >= 10) ? ("" + dayOfMonth) : ("0" + dayOfMonth))
               + "/" + ((monthOfYear >= 10) ? ("" + monthOfYear) : ("0" + monthOfYear))
               + "/" + year);
  }


  /** Ajouter un composant \uFFFD un GridBagLayout
   *@param comp  Composant \uFFFD ajouter
   *@param ligne  Ligne \uFFFD laquelle le composant doit \uFFFDtre ajout\uFFFD
   *@param colonne  Colonne \uFFFD laquelle le composant doit \uFFFDtre ajout\uFFFD
   *@param largeur  Largeur (en nb de cellules) que doit occuper le composant
   *@param hauteur  Hauteur (en nb de cellules) que doit occuper le composant
   *@param c  Container auquel on doit ajouter le composant
   *@param gb  Le GridBagLayout de comp
   *@param gbc  Les constraintes de ce GridBagLayout
   */
  public static void addComponentToGridBag(Component comp, int ligne, int colonne, int largeur, int hauteur, Container c, GridBagLayout gb, GridBagConstraints gbc)
  {
    gbc.gridx = colonne;
    gbc.gridy = ligne;
    gbc.gridwidth = largeur;
    gbc.gridheight = hauteur;
    gb.setConstraints(comp, gbc);
    c.add(comp);
  }


  /** Ajouter trois composants \uFFFD un GridBagLayout comme dans un BorderLayout (le composant du milieu est le seul \uFFFD pouvoir se redimensionner)
   *@param comp1  Composant de gauche \uFFFD ajouter
   *@param comp2  Composant du milieu \uFFFD ajouter
   *@param comp3  Composant de droite \uFFFD ajouter
   *@param ligne  Ligne \uFFFD laquelle le composant doit \uFFFDtre ajout\uFFFD
   *@param c  Container auquel on doit ajouter le composant
   *@param gb  Le GridBagLayout de comp
   *@param gbc  Les constraintes de ce GridBagLayout
   */
  public static void add3ComponentsToGridBag(Component comp1, Component comp2, Component comp3, int ligne, Container c, GridBagLayout gb, GridBagConstraints gbc)
  {
    gbc.fill = GridBagConstraints.BOTH;
    addComponentToGridBag(comp1, ligne, 0, 1, 1, c, gb, gbc);
    gbc.weightx = 1000;
    addComponentToGridBag(comp2, ligne, 1, 1, 1, c, gb, gbc);
    gbc.weightx = 0;
    addComponentToGridBag(comp3, ligne, 2, 1, 1, c, gb, gbc);
  }

  public static void extractStream(InputStream is, OutputStream os) {
    int i;
    try {
      while ( (i = is.read()) != -1)
        os.write(i);
    }
    catch (Exception e) {
      e.printStackTrace();
    }
  }

  /**
   * Cree un ImageIcon avec le fichier passe en parametre
   * Si le fichier n'existe pas, un icone vide est retourne
   * @param path Fichier image utilise pour creer l'icone
   * @return L'icone creee a partir du fichier specifie, si il existe
   */
  public static ImageIcon createImageIcon (String path)
  {
    // Creation de l'icone a partir du fichier si il existe
    if (new File (path).exists())
      return (new ImageIcon (path)) ;

    // Creation a partir d'une resource
    URL url = PogToolkit.class.getClassLoader().getResource(path) ;
    if (url != null)
      return (new ImageIcon (url)) ;

    // Creation a paritr d'une image vide
    BufferedImage imgVide = new BufferedImage (1, 1, BufferedImage.TYPE_INT_ARGB) ;
    imgVide.setRGB(0, 0, 0) ;
    return (new ImageIcon (imgVide)) ;
  }
}

class MyFileFilter extends FileFilter{
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

