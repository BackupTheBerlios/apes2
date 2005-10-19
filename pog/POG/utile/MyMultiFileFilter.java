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

import javax.swing.filechooser.FileFilter;
import java.io.File;

/**
 * <p>Titre : </p>
 * <p>Description : </p>
 * <p>Copyright: Copyright (c) 2003</p>
 * <p>Company: </p>
 * @author non attribuable
 * @version 1.0
 */

public class MyMultiFileFilter extends FileFilter{
   private java.util.Vector _ext = new java.util.Vector();

   public MyMultiFileFilter(String ext){
     super();
     _ext.add(ext);
   }

   public MyMultiFileFilter(){
     super();
   }

   public void addExt(String ext){
     _ext.add(ext);
   }

   public boolean accept(File fich){
     int nb = _ext.size();
     int i = 0;

     if (fich.isDirectory())
       return true;
     else
       while(i < nb)
         if (fich.getName().endsWith((String)_ext.elementAt(i++)))
           return true;
     return false;
   }

   public String getDescription(){
       return _ext.toString();
   }
 }
