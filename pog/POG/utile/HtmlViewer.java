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

import java.io.File;
import java.io.IOException;
import java.net.URL;

import javax.swing.JEditorPane;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.event.HyperlinkEvent;
import javax.swing.event.HyperlinkListener;
import javax.swing.text.html.HTMLDocument;

import POG.interfaceGraphique.fenetre.FenetrePrincipale;


/**
 * This class is a simple html viewer. It is able to render html files and display them.
 *
 * @author Brad Rippe (brippe@fullcoll.edu)
 * @version 1.0
 */
public class HtmlViewer extends JScrollPane {

	private static String _fichierAide;

  public static void mymain (FenetrePrincipale fp) {
	_fichierAide = POG.utile.propriete.Preferences.REPPREF + "AidePOG/default.htm";
	if (!(new File(_fichierAide).exists())) {
		fp.getLnkDebug().debogage("pasaide");
		return;
	}
    myhtml v = new myhtml();
    v.setTitle("AidePOG");
    v.setVisible(true);
    v.setSize(600, 400);
    HtmlViewer _htmlViewer = new HtmlViewer(v);
    v.getContentPane().add(_htmlViewer);
    v.show();
  }

    /**
     * Constructs a HTMLViewer
     * To construct this in a JFrame simply call "HTMLViewer htmlViewer = new HTMLViewer(this);"
     * @param the main JFrame application component
     */
    public HtmlViewer(URLSetable browser) {
                super(new JEditorPane());
        this.browser = browser;
        editor = (JEditorPane) getViewport().getView();
        editor.setContentType("text/html");
        editor.setEditable(false);
        editor.setEditorKit(new javax.swing.text.html.HTMLEditorKit());

        editor.addHyperlinkListener(new HyperlinkListener() {
            public void hyperlinkUpdate(HyperlinkEvent e) {
                                if (e.getEventType() == HyperlinkEvent.EventType.ACTIVATED) {
                                        JEditorPane pane = (JEditorPane) e.getSource();
                                        setWebPage(e.getURL());
                                }
                        }
        });

        setWebPage("file://" + _fichierAide);
    }

    /**
     * This method set the web page to be displayed.
     * @param url - this uniform resource locator of the new page, this must start with "http://"
     */
    public void setWebPage(String url) {
        try {
                        editor.setPage(new URL(url));
            currentURL = url;
            browser.setURLTextField(currentURL);
        } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,
                                                                  "Error retrieving specified URL",
                                          "Bad URL",
                                          JOptionPane.ERROR_MESSAGE);
                }
    }

    /**
     * This method set the web page to be displayed, this method is for internal use only.
     * @param url - this uniform resource locator of the new page, this must start with "http://"
     */
    public void setWebPage(URL url) {
        try {
                        editor.setPage(url);
            currentURL = "http://" + url.getHost() + url.getPath();
            browser.setURLTextField(currentURL);
        } catch (IOException e) {
                        JOptionPane.showMessageDialog(null,
                                                                  "Error retrieving specified URL",
                                          "Bad URL",
                                          JOptionPane.ERROR_MESSAGE);
                }
    }

    /**
     * Returns the current url.
     */
    public String getCurrentURL() {
                return currentURL;
    }

    private JEditorPane editor;
    private String currentURL = null;
    private String backURL = null;
    private String forwardURL = null;
    private HTMLDocument htmlDoc;
    private URLSetable browser;
}

  class myhtml extends JFrame implements URLSetable {
    String urr;
        public void setURLTextField(String url) {
          urr = url;
        }
  };


/*
import java.io.*;
import java.net.*;
import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.text.*;

public class HtmlViewer
    extends JFrame {
  static Font theTextFont = new Font("Courier", Font.PLAIN, 18);
  JEditorPane theText = null;
  JScrollPane theTextScrollPane = new JScrollPane();
  JButton theCloseButton = new JButton("Close");

  HtmlViewer theCurrentInstance = this;

  public HtmlViewer(
      URL url
      ) {
    this(url, 600, 400);
  }

  public HtmlViewer(
      URL url,
      int width,
      int height
      ) {
    super(url.toString());

    try {
      theText = new JEditorPane(url);
    }
    catch (MalformedURLException e) {
      System.out.println("Malformed URL: " + e);
    }
    catch (IOException e) {
      System.out.println("IOException: " + e);
    }
    // theText.setCaretPosition (0);
    theText.setFont(theTextFont);
    theText.setEditable(false);
    theText.setSize(width, height);

    JPanel thePanel = new JPanel();
    thePanel.add(theCloseButton);

    theTextScrollPane.getViewport().add(theText);
    getContentPane().add(theTextScrollPane, BorderLayout.CENTER);
    getContentPane().add(thePanel, BorderLayout.SOUTH);

    theCloseButton.addActionListener(
        new ActionListener() {
      public void actionPerformed(
          ActionEvent e
          ) {
        theCurrentInstance.dispose();
      }
    }
    );

    pack();
    this.setVisible(true);
    this.show();
  }
}
*/