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
//Titre :       Zip
//Version :     1.0
//Copyright :   Copyright (c) 2002
//Auteur :       JHelp
//Soci\uFFFDt\uFFFD :     GG
//Description : Ziper et d\uFFFDziper

import java.util.zip.ZipFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import java.util.zip.ZipInputStream;
import java.util.Vector;
import java.util.Enumeration;
import java.util.Date;
import java.util.Calendar;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;

/**
*   Classe g\uFFFDrant les fichiers ZIP
*/
public class ZIP
{
     //Nom du fichier
     private String fichier;
     //Liste des fichiers contenus dans le zip
     private Vector liste=new Vector();
     //S\uFFFDparateur de fichier, selon le syt\uFFFDme d'exploitaion utilis\uFFFD. Ainsi le code est valide partout
     private static final char separ=File.separatorChar;
     /**
        *    Le rang o\uFFFD se trouve le nom
        */
     public static final int placeNom=0;
     /**
        *    Le rang o\uFFFD se trouve la taille
        */
     public static final int placeTaille=1;
     /**
        *    Le rang o\uFFFD se trouve la taille de compression
        */
     public static final int placeCompression=2;
     /**
        *    Le rang o\uFFFD se trouve la date
        */
     public static final int placeDate=3;
     /**
        *    Le rang o\uFFFD se trouve le commentaire
        */
     public static final int placeCommentaire=4;

     public ZIP(String fichier) throws Exception
     {
            this.fichier = fichier;
            File f = new File(fichier);
            //Si le fichier existe, on r\uFFFDcup\uFFFDre le nom et les attributs des fichiers compr\uFFFDss\uFFFDs dans le zip
            if(f.exists())
            {
                 //C'est un fichier zip
                 ZipFile fic = new ZipFile(fichier);
                 //R\uFFFDcup\uFFFDration des fichiers
                 Enumeration enum=fic.entries();
                 while(enum.hasMoreElements())
                    liste.addElement(enum.nextElement());
                 fic.close();
            }
     }
     /**
        *    Change le nom du fichier zip, avec un chemin complet
        */
     public void setNom(String nom)
     {
            fichier=nom;
     }
     /**
        *    Donne un vecteur titre. Peut servir pour l'ent^te d'un JTable
        */
     public static Vector getTitre()
     {
            Vector titre=new Vector();
            titre.addElement("Nom");
            titre.addElement("Taille");
            titre.addElement("Compression");
            titre.addElement("Date");
            titre.addElement("Commentaire");
            return titre;
     }
     /**
        *    Vecteur de vecteur contenant les donn\uFFFDees <BR>
        *    Pour r\uFFFDcup\uFFFDrer la ligne l, colonnne c, faire : <BR>
        *    Vector donnes=zip.contenu();   <BR>
        *    ... <BR>
        *    Vector lig=(Vector)donnees.get(l); <BR>
        *    String valeur=(String)lig.get(c);
        */
     public Vector contenu()
     {
            int nb=liste.size();
            Vector donnees=new Vector();
            for(int i=0;i<nb;i++)
            {
                 Vector ligne=new Vector();
                 ZipEntry entre=(ZipEntry)liste.elementAt(i);
                 ligne.addElement(entre.getName());
                 ligne.addElement(""+entre.getSize());
                 long j=entre.getCompressedSize();
                 if(j>=0)
                    ligne.addElement(""+j);
                 else
                    ligne.addElement("");
                 Date date=new Date(entre.getTime());
                 Calendar calendrier=Calendar.getInstance();
                 calendrier.setTime(date);
                 ligne.addElement(
                    calendrier.get(Calendar.HOUR_OF_DAY)+"h"+
                    calendrier.get(Calendar.MINUTE)+"m"+
                    calendrier.get(Calendar.SECOND)+"s le "+
                    calendrier.get(Calendar.DAY_OF_MONTH)+"/"+
                    calendrier.get(Calendar.MONTH)+"/"+
                    calendrier.get(Calendar.YEAR));
                 ligne.addElement(entre.getComment());
                 donnees.addElement(ligne);
            }
            return donnees;
     }
     /**
        *    Donne une chaine repr\uFFFDsentant la caract\uFFFDristique du fichier <BR>
        *    @param place Rang de la carat\uFFFDristique. <BR>
        *    @param numero Num\uFFFDro du fichier contenu dans le zip <BR>
        *    Exemple : <BR>
        *    Pour avoir la taille de compression du fichier num\uFFFDro 5 faire : <BR>
        *    String taille=zip.get(ZIP.placeCompression,5);
        */
     public String get(int place,int numero)
     {
            //R\uFFFDcup\uFFFDration des caract\uFFFDristiques du fichier
            ZipEntry entre=(ZipEntry)liste.elementAt(numero);
            //Selon la caract\uFFFDristique demand\uFFFDe, retourner la valeur corespondante
            switch(place)
            {
                 //Le commentaire
                 case placeCommentaire :
                    return entre.getComment();
                 //La taille de compression
                 case placeCompression :
                    long j=entre.getCompressedSize();
                    if(j>=0)
                         return ""+j;
                    return "";
                 //La date du fichier
                 case placeDate :
                    //Transformation de la date exprim\uFFFDe en milliseconde, en date plus courante
                    Date date=new Date(entre.getTime());
                    //Mise en place d'un calendrier \uFFFD partir de la date
                    Calendar calendrier=Calendar.getInstance();
                    calendrier.setTime(date);
                    //Renvoie des donn\uFFFDes
                    return calendrier.get(Calendar.HOUR_OF_DAY)+"h"+
                         calendrier.get(Calendar.MINUTE)+"m"+
                         calendrier.get(Calendar.SECOND)+"s le "+
                         calendrier.get(Calendar.DAY_OF_MONTH)+"/"+
                         calendrier.get(Calendar.MONTH)+"/"+
                         calendrier.get(Calendar.YEAR);
                 //Le nom du fichier
                 case placeNom :
                    return entre.getName();
                 //La taille r\uFFFDelle du fichier
                 case placeTaille :
                    return ""+entre.getSize();
            }
            //Renvoie la cha\uFFFDne vide si la caract\uFFFDristique n'existe pas
            return "";
     }

     /**
        *    Ajoute un fichier au fichier zip avec un commentaire
        */
       public void ajouteFichier(File f, String fichier, String commentaire)     {
         ZipEntry entre;
         //Transforme le fichier en entr\uFFFDe du zip
         entre = new ZipEntry(fichier);
         entre.setSize(f.length());
         entre.setTime(f.lastModified());
         if(commentaire!=null)
           entre.setComment(commentaire);
           //Ajoute l'\uFFFDntr\uFFFDe
         liste.addElement(entre);
       }

     /**
        *    Retire le fichier du zip
        */
     public void retire(File fichier)
     {
            //Cacul le nom de l'entr\uFFFDe, en majuscule, corespondante au fichier
            String s=fichier.getAbsolutePath().substring(3).replace(separ,'/').toUpperCase();
            //Chercher le rang de l'\uFFFDntr\uFFFDe corespondante
            int rang=-1;
            int nb=liste.size();
            for(int i=0;(i<nb)&&(rang<0);i++)
            {
                 ZipEntry entre=(ZipEntry)liste.elementAt(i);
                 if(entre.getName().toUpperCase().equals(s))
                    rang=i;
            }
            //Si l'\uFFFDntr\uFFFDe existe, la retir\uFFFDe
            if(rang>=0)
                 liste.removeElementAt(rang);
     }
     /**
        *    @return Le nom du fichier
        */
     public int getNombreFichier()
     {
            return liste.size();
     }
     /**
        *    Retire l'element num\uFFFDro numero
        */
     public void retire(int numero)
     {
            liste.removeElementAt(numero);
     }
     /**
        *    Zip les fichiers dans le nom du zip correspondant. Met \uFFFD jour les changements.
        */
     public void ziper() throws Exception
     {
            //Si le fichier n'existe pas, le cr\uFFFDer
            File f=new File(fichier);
            if(!f.exists()){
              f.createNewFile();
            }
            //Ouverture du fichier zip en \uFFFDcriture
            ZipOutputStream zos=new ZipOutputStream(new FileOutputStream(fichier));
            //Pour chaque entr\uFFFDe du zip
            int nb=liste.size();
            int index;
            for(int i=0;i<nb;i++)
            {
                 //R\uFFFDcup\uFFFDrer l'\uFFFDntr\uFFFDe courante
                 ZipEntry entre=(ZipEntry)liste.elementAt(i);
                 //R\uFFFDcup\uFFFDrer le nom de l'entr\uFFFDe
                 String nom=entre.getName();
                 //Ajouter l'entr\uFFFDe au fichier physique zip
                 zos.putNextEntry(entre);
                 //Ouvrir l'entr\uFFFDe en lecture
                 if ((new File(f.getParent()+File.separator+nom)).isDirectory())
                   continue;
                 FileInputStream fis=new FileInputStream(f.getParent()+File.separator+nom);
                 //Ziper l'entr\uFFFDe dans le fichier zip
                 byte[] tab=new byte[4096];
                 int lu=-1;
                 do
                 {
                    lu=fis.read(tab);
                    if(lu>0)
                         zos.write(tab,0,lu);
                 }while(lu>0);
                 //Fermer l'entr\uFFFDe
                 fis.close();
            }
            //Force \uFFFD finir le zipage, si jamais il reste des bits non trait\uFFFDs
            zos.flush();
            //Ferme le fichier zip
            zos.close();
     }
     /**
        *    D\uFFFDzipe le fichier dans le dossier en param\uFFFDtre. Respect du nom des dossiers et sous-dossiers
        */
     public void deziper(File dossier) throws Exception
     {
            //Ouverture du fichier zip en lecture
            ZipInputStream zis=new ZipInputStream(new FileInputStream(fichier));
            //R\uFFFDcup\uFFFDre les entr\uFFFDes \uFFFDffectivement zip\uFFFDes dans le zip
            ZipEntry ze=zis.getNextEntry();
            //Tant qu'il y a des entr\uFFFDes
            while(ze!=null)
            {
                 //Cr\uFFFDe le dossier contenant le fichier une fois    d\uFFFDziper, si il n'existe pas
                 //ze contient le dossier images
                 String filen = ze.toString();
                 filen = filen.replace('/', File.separatorChar);
                 filen = filen.replace('\\', File.separatorChar);
                 File f=new File(dossier.getAbsolutePath()+separ+filen);
                 int v =f.getName().indexOf(".");
                 if (v == -1) {
                   ze = zis.getNextEntry();
                   continue;
                 }
                 f.getParentFile().mkdirs();
                 //Ouvre l'entr\uFFFDe le fichier \uFFFD d\uFFFDziper en \uFFFDcriture, le cr\uFFFDe s'il n'existe pas
                 FileOutputStream fos=new FileOutputStream(f);
                 //D\uFFFDzipe le fichier
                 int lu=-1;
                 byte[] tampon=new byte[4096];
                 do
                 {
                    lu=zis.read(tampon);
                    if(lu>0)
                         fos.write(tampon,0,lu);
                 } while(lu>0);
                 //Finir proprement
                 fos.flush();
                 //Fermer le fichier \uFFFD d\uFFFDziper
                 fos.close();
                 //Passer au suivant
                 ze=zis.getNextEntry();
            }
            //Fermer le fichier zip
            zis.close();
     }
     /**
        *    Modifie le commentaire du fichier num\uFFFDro numero
        */
     public void setCommentaire(int numero,String commentaire)
     {
            ZipEntry entre=(ZipEntry)liste.elementAt(numero);
            entre.setComment(commentaire);
     }
  public String getFichier() {
    return fichier;
  }
}


