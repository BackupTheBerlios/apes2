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


package POG.application.importExport;

import java.io.BufferedInputStream;
import java.io.DataInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Stack;
import java.util.Vector;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.model.extension.ApesProcess;
import org.ipsquad.apes.model.extension.ApesWorkDefinition;
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.modelmanagement.SPackage;
import org.ipsquad.apes.model.spem.process.components.ProcessComponent;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;

import JSX.ObjIn;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;

public class Apes {

  private static File mFile;
  private static Vector _lstElement;
  private static HashMap _hdiag;

  public static ProcessComponent loadModel(File nomfich) {
	FenetrePrincipale.INSTANCE.getLnkDebug().patienter("chargeapes", 0, 0);
    mFile = nomfich;
    ApesProcess ap = new ApesProcess("Project");
    try {
    	FileInputStream fis = new FileInputStream(mFile);
      ZipInputStream zipFile = new ZipInputStream(fis);
      loadComponent(zipFile, ap);
	  zipFile.close();
	  fis.close();
    }
    catch (Throwable t) {
      t.printStackTrace();
    }
    ProcessComponent pp = ap.getComponent();
    _lstElement = new Vector();
    _lstElement.add(pp);
    Stack st = new Stack();
    for (int i = 0; i < pp.modelElementCount(); i++) {
      st.push(pp.getModelElement(i));
      _lstElement.add(pp.getModelElement(i));
    }
    while (!st.isEmpty()) {
      ModelElement mm = (ModelElement)st.pop();
      if (mm instanceof SPackage) {
        SPackage sp = (SPackage)mm;
        for (int i = 0; i < sp.modelElementCount(); i++)
          st.push(sp.getModelElement(i));
      }
      else if (mm instanceof WorkDefinition) {
        WorkDefinition wd = (WorkDefinition)mm;
        if (!((ApesWorkDefinition)(wd)).canAddActivityDiagram())
          _lstElement.add(((ApesWorkDefinition)(wd)).getActivityDiagram());
        if (!((ApesWorkDefinition)(wd)).canAddFlowDiagram())
          _lstElement.add(((ApesWorkDefinition)(wd)).getFlowDiagram());
        for (int j = 0; j < wd.subWorkCount(); j++)
          _lstElement.add(wd.getSubWork(j));
      }
      else if (!_lstElement.contains(mm))
        _lstElement.add(mm);
//      else
  //    	System.out.println("toBreak: " + mm.getName() + " " + mm.getID());
    }
	
    return ap.getComponent();
  }

  /**
   * Load the component in the process giving in parameter.
   *
   * @param projectZip the zip containing the Component.xml file
   * @param ap the process where to store the component
   * @return true if successfull, false otherwise
   * @throws IOException
   * @throws ClassNotFoundException
   */
  private static boolean loadComponent(ZipInputStream projectZip, ApesProcess ap) throws IOException,
      ClassNotFoundException {

    DataInputStream data = findData("Component.xml");

    if (data != null) {
      ObjIn in = new ObjIn(data);
      Vector v = (Vector) in.readObject();
      ap.addModelElement( (ProcessComponent) v.get(0));
	  _hdiag = (HashMap)v.get(1);
      projectZip.close();

      return true;
    }
    else {
      projectZip.close();
      return false;
    }
  }

  /**
   * Search and open the file given by fileName in projectZip.
   *
   * @param fileName the file to open
   * @return the DataInputStream containing the file
   * @throws IOException
   * @throws SAXException
   * @throws ParserConfigurationException
   */
  private static DataInputStream findData(String fileName) throws IOException {
    ZipInputStream zipFile = new ZipInputStream(new FileInputStream(new File(
        mFile.getAbsolutePath())));

    ZipEntry zipEntry = zipFile.getNextEntry();
	BufferedInputStream tmpbuf = null;
    while (zipEntry != null) {
		tmpbuf = new BufferedInputStream(zipFile);
      DataInputStream data = new DataInputStream(tmpbuf);

      if (zipEntry.getName().equals(fileName)) {
        return data;
      }
      else {
        zipEntry = zipFile.getNextEntry();
      }
    }
    if (tmpbuf != null)
    	tmpbuf.close();
    zipFile.close();
    return null;
  }


  public static ModelElement getElementByID(int id) {
    for (int i = 0; i < _lstElement.size(); i++)
      if (((ModelElement)_lstElement.get(i)).getID() == id)
        return (ModelElement)_lstElement.get(i);
    return null;
  }

  public static Vector getListeElementApes() {
    return _lstElement;
  }

	public static SpemGraphAdapter getDiagramme(SpemDiagram sp) {
		return (SpemGraphAdapter)_hdiag.get(sp);
	}

}