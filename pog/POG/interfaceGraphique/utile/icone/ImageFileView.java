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


package POG.interfaceGraphique.utile.icone;

import java.io.File;
import javax.swing.*;
import javax.swing.filechooser.*;

import POG.utile.PogToolkit;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;

/* ImageFileView.java is a 1.4 example used by FileChooserDemo2.java. */
public class ImageFileView extends FileView {
    ImageIcon jpgIcon = FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("jpgIcon");
    ImageIcon gifIcon = FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("gifIcon");
    ImageIcon tiffIcon = FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("tiffIcon");
    ImageIcon pngIcon = FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("pngIcon");

    public String getName(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getDescription(File f) {
        return null; //let the L&F FileView figure this out
    }

    public Boolean isTraversable(File f) {
        return null; //let the L&F FileView figure this out
    }

    public String getTypeDescription(File f) {

        String type = null;

        if (f != null) {
            if (f.getName().endsWith(PogToolkit.jpeg) ||
                f.getName().endsWith(PogToolkit.jpg)) {
                type = "JPEG Image";
            } else if (f.getName().endsWith(PogToolkit.gif)){
                type = "GIF Image";
            } else if (f.getName().endsWith(PogToolkit.tiff) ||
                       f.getName().endsWith(PogToolkit.tif)) {
                type = "TIFF Image";
            } else if (f.getName().endsWith(PogToolkit.png)){
                type = "PNG Image";
            }
        }
        return type;
    }

    public Icon getIcon(File f) {
        Icon icon = null;

        if (f != null) {
            if (f.getName().endsWith(PogToolkit.jpeg) ||
                f.getName().endsWith(PogToolkit.jpg)) {
                icon = jpgIcon;
            } else if (f.getName().endsWith(PogToolkit.gif)) {
                icon = gifIcon;
            } else if (f.getName().endsWith(PogToolkit.tiff) ||
                       f.getName().endsWith(PogToolkit.tif)) {
                icon = tiffIcon;
            } else if (f.getName().endsWith(PogToolkit.png)) {
                icon = pngIcon;
            }
        }
        return icon;
    }
}
