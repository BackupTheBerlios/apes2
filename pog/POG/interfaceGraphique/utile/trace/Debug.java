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



package POG.interfaceGraphique.utile.trace;



import javax.swing.JTextPane;

import javax.swing.text.Style;

import javax.swing.text.StyleContext;

import javax.swing.text.StyleConstants;

import java.awt.Color;



public class Debug {

	public Debug() {
	  _texte.setEditable(false);
	  Style def = StyleContext.getDefaultStyleContext().getStyle(StyleContext.DEFAULT_STYLE);
	  Style normal = _texte.getStyledDocument().addStyle("normal", def);
	  StyleConstants.setFontFamily(def, "SansSerif");
	  Style s = _texte.getStyledDocument().addStyle("rouge", normal);
	  StyleConstants.setForeground(s, Color.RED);
	  s = _texte.getStyledDocument().addStyle("bleu", normal);
	  StyleConstants.setForeground(s, Color.BLUE);
	  s = _texte.getStyledDocument().addStyle("orange", normal);
	  StyleConstants.setForeground(s, Color.ORANGE);
	}


  public JTextPane get_texte() {
    return _texte;
  }

  public void clearTexte() {
    _texte.setText("");
  }

  public void afficher(String message) {
	afficherStyle(message, "normal");
  }

  public void afficher(String [] message) {
    for (int i = 0; i < message.length; i++)
      afficher("id: " + message[i]);
    if (message.length == 0)
      afficher("NON ARRAY");
  }

  public void debogage(String message) {
	afficherStyle(message, "rouge");
	System.out.print(message + System.getProperty("line.separator"));
  }

	private String _patientermess = null;

	public void patienter(String message) {
		try {
			if (_patientermess == null) {
				_texte.getStyledDocument().insertString(_texte.getStyledDocument().getLength(), message, _texte.getStyledDocument().getStyle("orange"));
			}
			else {
				_texte.getStyledDocument().remove(_texte.getStyledDocument().getLength() - _patientermess.length(), _patientermess.length());
				_texte.getStyledDocument().insertString(_texte.getStyledDocument().getLength(), message, _texte.getStyledDocument().getStyle("orange"));
			}
			_patientermess = new String(message);
		} catch (Exception e) {
		  e.printStackTrace();
		}
//		System.out.println("PAT: " + message);
	}


	public void verificationMessage(String message) {
		afficherStyle(message, "bleu");
	}

	private void afficherStyle(String message, String style) {
		if (_patientermess != null)
			message = System.getProperty("line.separator") + message;
		_patientermess = null;
		message = message + System.getProperty("line.separator");
		try {
		  _texte.getStyledDocument().insertString(_texte.getStyledDocument().getLength(), message, _texte.getStyledDocument().getStyle(style));
		} catch (Exception e) {
		  e.printStackTrace();
		}
	}


  	private JTextPane _texte = new JTextPane();

}

