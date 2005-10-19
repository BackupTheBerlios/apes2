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

package POG.objetMetier;

import java.io.File;
import java.net.URI;

/**
 * @stereotype entity
 */

public class Contenu {

	private File _file;
	private URI _urr;
	private String _pathBiblio;

	public Contenu(URI urr, String pathB) {
		setURI(urr);
		_pathBiblio = pathB;
	}

	public String getAbsolutePath() {
		if (_file != null)
			return _file.getAbsolutePath();
		else
			return _urr.toString().replaceAll("\\\\", "/");
	}

	public String get_uri() {
		if (_file != null)
			return _file.getName();
		else
			return _urr.toString().replaceAll("\\\\", "/");
	}

	public String getRelativeToBiblioPath() {
		if (_file == null)
			return _urr.toString().replaceAll("\\\\", "/");
		String abso = getAbsolutePath();
		if (!abso.startsWith(_pathBiblio))
			return abso;
		return abso.substring(_pathBiblio.length() + 1);
	}

	public boolean isFile() {
		return _file != null;
	}
	
	public void setURI(URI urr) {
		_urr = urr;
		if (urr.getScheme().equals("file"))
			_file = new File(urr.getPath());
		else
			_file = null;
	}
}
