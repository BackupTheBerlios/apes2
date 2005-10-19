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


/*
 * OuvrirFiltre.java
 *
 * Created on 11 avril 2002, 17:38
 */

package POG.outil.html.pika;
import java.io.IOException;


/**
 *
 * @author  Administrateur
 * @version
 */
public class OuvrirFiltre extends javax.swing.filechooser.FileFilter {

    /** Creates new OuvrirFiltre */
    public OuvrirFiltre() {
        super();
    }

    public boolean accept(java.io.File fichier) {
        if (fichier.isDirectory()) {
		return true;
	}
	else
	{
		String uneString=fichier.getName();
		char[] a=uneString.toCharArray();
		StringBuffer extension=new StringBuffer("");
                StringBuffer extension2=new StringBuffer("");		String extensionString = "";
			extension.append(a[a.length-3]);

			extension.append(a[a.length-2]);

			extension.append(a[a.length-1]);

			extensionString=extension.toString();

					extension2.append(a[a.length-4]);

					extension2.append(a[a.length-3]);

			extension2.append(a[a.length-2]);

			extension2.append(a[a.length-1]);

                String extensionString2=extension2.toString();
		if (extensionString.trim().equalsIgnoreCase("HTM"))
		{
			return true;
		}
		else
		{
                    if (extensionString2.trim().equalsIgnoreCase("HTML"))
                    {
			return true;
                    }
                    else
                    {
			return false;
                    }
		}
	}
    }

    public String getDescription() {
        return "Fichiers HTML (*.HTM, *.HTML)";
    }

}
