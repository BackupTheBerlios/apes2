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


package POG.interfaceGraphique.fenetre;

import java.awt.HeadlessException;
import java.io.InputStream;

import javax.swing.JDialog;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;


public class RapportBug extends JDialog {


  JScrollPane jScrollPane1 = new JScrollPane();
  JTextArea jTextArea1 = new JTextArea();


  public RapportBug() throws HeadlessException {
      try {
        jbInit();
        InputStream in = ClassLoader.getSystemResourceAsStream("rapportBug.properties");
        int i;
        String s = new String("");
        while ((i = in.read()) != -1)
          s = s + (char)i;
        jTextArea1.append(s);
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }

    private void jbInit() throws Exception {
//      jScrollPane1.setBounds(13, 18, 343, 262);
      jTextArea1.setText("");
      this.getContentPane().add(jScrollPane1, null);
      jScrollPane1.getViewport().add(jTextArea1, null);
      this.setSize(400, 400);
      this.setVisible(true);
    }
}