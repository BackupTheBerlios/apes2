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
 * appliTest.java
 *
 * Created on 10 avril 2002, 17:47
 */
package POG.outil.html.pika;

import java.io.File;
import java.io.FileWriter;
import java.io.BufferedWriter;
import java.io.FileReader;
import java.io.BufferedReader;
import java.awt.event.KeyEvent;
import javax.swing.JFileChooser;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.interfaceGraphique.fenetre.PanneauxDetails.PanneauDetail;

/**
 *
 * @author  Administrateur
 */
public class appliTest extends javax.swing.JFrame {
  FenetrePrincipale _fp = null;
  PanneauDetail _panel = null;
    /** Creates new form appliTest */
    public appliTest(FenetrePrincipale fp) {
      try {
        _fp = fp;
        jbInit();
        feuille.setVisible(false);
        //initialisation de l'icone de l'application
      }
      catch(Exception e) {
        e.printStackTrace();
      }
    }
    private void jbInit() {
        BarreMenu = new javax.swing.JMenuBar();
        MenuFichier = new javax.swing.JMenu();
        MenuNouveau = new javax.swing.JMenuItem();
        MenuOuvrir = new javax.swing.JMenuItem();
        MenuSave = new javax.swing.JMenuItem();
        MenuSaveSous = new javax.swing.JMenuItem();
        MenuApercu = new javax.swing.JMenuItem();
        quitter = new javax.swing.JMenuItem();
        MenuAbout = new javax.swing.JMenuItem();
        MenuEdition = new javax.swing.JMenu();
        couper = new javax.swing.JMenuItem();
        copier = new javax.swing.JMenuItem();
        coller = new javax.swing.JMenuItem();
        supprimer = new javax.swing.JMenuItem();
        MenuSelectionner = new javax.swing.JMenuItem();
        jPanel1 = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        feuille = new javax.swing.JTextArea();
        ToolBarPanel = new javax.swing.JPanel();
        ToolBarFichier = new javax.swing.JToolBar();
        nouveaubouton = new javax.swing.JButton();
        ouvrirbouton = new javax.swing.JButton();
        savebouton = new javax.swing.JButton();
        apercubouton = new javax.swing.JButton();
        ToolBarEdition = new javax.swing.JToolBar();
        couperbouton = new javax.swing.JButton();
        copierbouton = new javax.swing.JButton();
        collerbouton = new javax.swing.JButton();
        MenuFichier.setMnemonic(KeyEvent.VK_F);
        MenuFichier.setText("Fichier");
        MenuNouveau.setMnemonic(KeyEvent.VK_N);
        MenuNouveau.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        MenuNouveau.setText("Nouveau");
        MenuNouveau.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileNew"));
        MenuNouveau.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuNouveauActionPerformed(evt);
            }
        });
        MenuFichier.add(MenuNouveau);
        MenuOuvrir.setMnemonic(KeyEvent.VK_O);
        MenuOuvrir.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_O, java.awt.event.InputEvent.CTRL_MASK));
        MenuOuvrir.setText("Ouvrir");
        MenuOuvrir.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileOpen"));
        MenuOuvrir.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MenuOuvrir.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuOuvrirActionPerformed(evt);
            }
        });
        MenuFichier.add(MenuOuvrir);
        MenuSave.setMnemonic(KeyEvent.VK_E);
        MenuSave.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_E, java.awt.event.InputEvent.CTRL_MASK));
        MenuSave.setText("Enregistrer");
        MenuSave.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileSave"));
        MenuSave.setHorizontalAlignment(javax.swing.SwingConstants.LEFT);
        MenuSave.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSaveActionPerformed(evt);
            }
        });
        MenuFichier.add(MenuSave);
        MenuSaveSous.setMnemonic(KeyEvent.VK_S);
        MenuSaveSous.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        MenuSaveSous.setText("Enregistrer Sous");
        MenuSaveSous.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileSaveAs"));
        MenuSaveSous.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSaveSousActionPerformed(evt);
            }
        });
        MenuFichier.add(MenuSaveSous);
        MenuApercu.setMnemonic(KeyEvent.VK_A);
        MenuApercu.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_A, java.awt.event.InputEvent.CTRL_MASK));
        MenuApercu.setText("Apercu HTML");
        MenuApercu.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("view"));
        MenuApercu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuApercuActionPerformed(evt);
            }
        });
        MenuFichier.add(MenuApercu);
        quitter.setMnemonic(KeyEvent.VK_Q);
        quitter.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_Q, java.awt.event.InputEvent.CTRL_MASK));
        quitter.setText("Quitter");
        quitter.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileQuit"));
        quitter.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitterActionPerformed(evt);
            }
        });
        MenuFichier.add(quitter);
        MenuAbout.setMnemonic(KeyEvent.VK_B);
        MenuAbout.setText("A propos de...");
        MenuAbout.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_about"));
        MenuAbout.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuAboutActionPerformed(evt);
            }
        });
        MenuFichier.add(MenuAbout);
        BarreMenu.add(MenuFichier);
        MenuEdition.setMnemonic(KeyEvent.VK_E);
        MenuEdition.setText("Edition");
        couper.setMnemonic(KeyEvent.VK_U);
        couper.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_X, java.awt.event.InputEvent.CTRL_MASK));
        couper.setText("Couper");
        couper.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditCut"));
        couper.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                couperActionPerformed(evt);
            }
        });
        MenuEdition.add(couper);
        copier.setMnemonic(KeyEvent.VK_C);
        copier.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        copier.setText("Copier");
        copier.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditCopy"));
        copier.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copierActionPerformed(evt);
            }
        });
        MenuEdition.add(copier);
        coller.setMnemonic(KeyEvent.VK_L);
        coller.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_V, java.awt.event.InputEvent.CTRL_MASK));
        coller.setText("Coller");
        coller.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditPaste"));
        coller.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collerActionPerformed(evt);
            }
        });
        MenuEdition.add(coller);
        supprimer.setMnemonic(KeyEvent.VK_S);
        supprimer.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_DELETE, 0));
        supprimer.setText("Supprimer");
        supprimer.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("deletefile"));
        supprimer.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                supprimerActionPerformed(evt);
            }
        });
        MenuEdition.add(supprimer);
        MenuSelectionner.setMnemonic(KeyEvent.VK_T);
        MenuSelectionner.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_T, java.awt.event.InputEvent.CTRL_MASK));
        MenuSelectionner.setText("Selectionner tout");
        MenuSelectionner.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("Empty"));
        MenuSelectionner.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                MenuSelectionnerActionPerformed(evt);
            }
        });
        MenuEdition.add(MenuSelectionner);
        BarreMenu.add(MenuEdition);
        setTitle("Pikachu HTML Editeur");
        addWindowListener(new java.awt.event.WindowAdapter() {
            public void windowClosing(java.awt.event.WindowEvent evt) {
                exitForm(evt);
            }
        });
        jPanel1.setLayout(new java.awt.BorderLayout());
        jPanel1.setPreferredSize(new java.awt.Dimension(300, 200));
        feuille.setFont(new java.awt.Font("Verdana", 1, 12));
        feuille.addKeyListener(new java.awt.event.KeyAdapter() {
            public void keyTyped(java.awt.event.KeyEvent evt) {
                feuilleKeyTyped(evt);
            }
        });
        jScrollPane1.setViewportView(feuille);
        jPanel1.add(jScrollPane1, java.awt.BorderLayout.CENTER);
        ToolBarPanel.setLayout(new java.awt.BorderLayout());
        nouveaubouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileNew"));
        nouveaubouton.setToolTipText("Nouveau document");
        nouveaubouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                nouveauboutonActionPerformed(evt);
            }
        });
        ToolBarFichier.add(nouveaubouton);
        ouvrirbouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileOpen"));
        ouvrirbouton.setToolTipText("Ouvrir un fichier");
        ouvrirbouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                ouvrirboutonActionPerformed(evt);
            }
        });
        ToolBarFichier.add(ouvrirbouton);
        savebouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("FileSave"));
        savebouton.setToolTipText("Enregistrer");
        savebouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveboutonActionPerformed(evt);
            }
        });
        ToolBarFichier.add(savebouton);
        apercubouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("view"));
        apercubouton.setPreferredSize(savebouton.getPreferredSize());
        apercubouton.setToolTipText("Apercu HTML");
        apercubouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                apercuboutonActionPerformed(evt);
            }
        });
        ToolBarFichier.add(apercubouton);
        ToolBarPanel.add(ToolBarFichier, java.awt.BorderLayout.WEST);
        couperbouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditCut"));
        couperbouton.setToolTipText("Couper");
        couperbouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                couperboutonActionPerformed(evt);
            }
        });
        ToolBarEdition.add(couperbouton);
        copierbouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditCopy"));
        copierbouton.setToolTipText("Copier");
        copierbouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                copierboutonActionPerformed(evt);
            }
        });
        ToolBarEdition.add(copierbouton);
        collerbouton.setIcon(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("EditPaste"));
        collerbouton.setToolTipText("Coller");
        collerbouton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                collerboutonActionPerformed(evt);
            }
        });
        ToolBarEdition.add(collerbouton);
        ToolBarPanel.add(ToolBarEdition, java.awt.BorderLayout.CENTER);
        jPanel1.add(ToolBarPanel, java.awt.BorderLayout.NORTH);
        getContentPane().add(jPanel1, java.awt.BorderLayout.CENTER);
        setJMenuBar(BarreMenu);
        pack();
        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();
        setSize(new java.awt.Dimension(500, 420));
        setLocation((screenSize.width-500)/2,(screenSize.height-420)/2);
        setIconImage(FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("pika").getImage());
        this.setDefaultCloseOperation(DO_NOTHING_ON_CLOSE);
    }

    private void MenuAboutActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuAboutActionPerformed
        // Add your handling code here:
        DialogueAbout unDial=new DialogueAbout(this,true);
        unDial.show();
    }//GEN-LAST:event_MenuAboutActionPerformed

    private void apercuboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_apercuboutonActionPerformed
        // Add your handling code here:
        DialogueApercu unDialogue=new DialogueApercu(this, true,feuille.getText());
        unDialogue.show();
    }//GEN-LAST:event_apercuboutonActionPerformed



    private void MenuApercuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuApercuActionPerformed
        // Add your handling code here:
        DialogueApercu unDialogue=new DialogueApercu(this, true,feuille.getText());
        unDialogue.show();
    }//GEN-LAST:event_MenuApercuActionPerformed

    private void MenuSelectionnerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSelectionnerActionPerformed
        // Add your handling code here:
        feuille.requestFocus();
        feuille.selectAll();
    }//GEN-LAST:event_MenuSelectionnerActionPerformed

    private void supprimerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_supprimerActionPerformed
        // Add your handling code here:
        feuille.replaceSelection("");
    }//GEN-LAST:event_supprimerActionPerformed

    private void couperActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_couperActionPerformed
        // Add your handling code here:
        feuille.cut();
    }//GEN-LAST:event_couperActionPerformed

    private void couperboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_couperboutonActionPerformed
        // Add your handling code here:
        feuille.cut();
    }//GEN-LAST:event_couperboutonActionPerformed

    private void collerActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collerActionPerformed
        // Add your handling code here:
        feuille.paste();
    }//GEN-LAST:event_collerActionPerformed

    private void copierActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copierActionPerformed
        // Add your handling code here:
        int debut=feuille.getSelectionStart();
        int fin=feuille.getSelectionEnd();
        feuille.copy();
        feuille.select(debut,fin);
    }//GEN-LAST:event_copierActionPerformed

    private void collerboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_collerboutonActionPerformed
        // Add your handling code here:
        feuille.paste();
    }//GEN-LAST:event_collerboutonActionPerformed

    private void copierboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_copierboutonActionPerformed
        // Add your handling code here:
        int debut=feuille.getSelectionStart();
        int fin=feuille.getSelectionEnd();
        feuille.copy();
        feuille.requestFocus();
        feuille.select(debut,fin);
    }//GEN-LAST:event_copierboutonActionPerformed



    private void MenuSaveSousActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveSousActionPerformed
        // Add your handling code here:
        enregistrer2();
    }//GEN-LAST:event_MenuSaveSousActionPerformed

    private void feuilleKeyTyped(java.awt.event.KeyEvent evt) {//GEN-FIRST:event_feuilleKeyTyped
        // Add your handling code here:
        //on signale que le document vient d'\uFFFDtre modifi\uFFFD
        if (!(getTitle().endsWith("*")))
        {
            stateFeuille=true;
            setTitle(getTitle()+"*");
        }

    }//GEN-LAST:event_feuilleKeyTyped

    private void MenuSaveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuSaveActionPerformed

        // Add your handling code here:
        if (stateFeuille==true)
        {
            if (fichier!=null)
            {
                try
                {
                    FileWriter unFileWriter=new FileWriter(fichier);
                    BufferedWriter unBuffer=new BufferedWriter(unFileWriter);
                    feuille.write(unBuffer);
                    unBuffer.close();
                    String uneString="";
                    setTitle(String.copyValueOf(getTitle().toCharArray(),0,getTitle().length()-1));
                    stateFeuille=false;
                }
                catch (Exception ex)
                {
                    System.out.println(ex.toString());
                }
            }
            else
            {
                enregistrer();
            }
        }
    }//GEN-LAST:event_MenuSaveActionPerformed

    private void saveboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveboutonActionPerformed
        // Add your handling code here:
        if (stateFeuille==true)
        {
            if (fichier!=null)
            {
                try
                {
                    FileWriter unFileWriter=new FileWriter(fichier);
                    BufferedWriter unBuffer=new BufferedWriter(unFileWriter);
                    feuille.write(unBuffer);
                    unBuffer.close();
                    String uneString="";
                    setTitle(String.copyValueOf(getTitle().toCharArray(),0,getTitle().length()-1));
                    stateFeuille=false;
                }
                catch (Exception ex)
                {
                    System.out.println(ex.toString());
                }
            }
            else
            {
                enregistrer();
            }
        }

    }//GEN-LAST:event_saveboutonActionPerformed

    private void ouvrirboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_ouvrirboutonActionPerformed
        // Add your handling code here:
        ouvrir();
    }//GEN-LAST:event_ouvrirboutonActionPerformed

    private void MenuOuvrirActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuOuvrirActionPerformed
        // Add your handling code here:
        ouvrir();
    }//GEN-LAST:event_MenuOuvrirActionPerformed

    private void MenuNouveauActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_MenuNouveauActionPerformed
        // Add your handling code here:
        NouveauDocument();
    }//GEN-LAST:event_MenuNouveauActionPerformed

    private void nouveauboutonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_nouveauboutonActionPerformed
        // Add your handling code here:
        NouveauDocument();
    }//GEN-LAST:event_nouveauboutonActionPerformed

    private void quitterActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitterActionPerformed
        // Add your handling code here:
        quitter();
    }//GEN-LAST:event_quitterActionPerformed

    /** Exit the Application */
    private void exitForm(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_exitForm
        quitter();
    }//GEN-LAST:event_exitForm

  public void nouvelPage(PanneauDetail panel){
    fichier = null;
    _panel = panel;
    this.setVisible(true);
    this.show();
    NouveauDocument();
  }

  public void lancement(){
    fichier = null;
    _panel = null;;
	this.setVisible(true);
	this.show();
  }

    public void NouveauDocument() {
        //on v\uFFFDrifie s'il faut sauvegarder le fichier en cours
        if (stateFeuille==true)
        {
        DialogueBox unDialogue=new DialogueBox(this,true);
        unDialogue.show();
        if (unDialogue.getReturnStatus()==DialogueBox.RET_OK)
        {
            if (fichier!=null)
            {
                try
                {
                    FileWriter unFileWriter=new FileWriter(fichier);
                    BufferedWriter unBuffer=new BufferedWriter(unFileWriter);
                    feuille.write(unBuffer);
                    unBuffer.close();
                    String uneString="";
                    setTitle(String.copyValueOf(getTitle().toCharArray(),0,getTitle().length()-1));
                    stateFeuille=false;
                }
                catch (Exception ex)
                {
                    System.out.println(ex.toString());
                }
            }
            else
            {
                enregistrer();
            }
        }
        if ((unDialogue.getReturnStatus()==DialogueBox.RET_CANCEL)||(stateCancel==true))
        {
            stateCancel=false;
        }
        else{
          //Cr\uFFFDation d'un nouveau document
          feuille.setVisible(true);
          fichier = null;
          feuille.setText("");
          setTitle("nouveau_document");
        }
        }
        else{
            feuille.setVisible(true);
            fichier=null;
            feuille.setText("");
            setTitle("nouveau_document");
        }

    }



    private void ouvrir() {
        feuille.setText("");
        File uneFile;
        JFileChooser unJFileChooser=new JFileChooser();
        unJFileChooser.setDialogType(JFileChooser.OPEN_DIALOG);
        unJFileChooser.setFileFilter(new OuvrirFiltre());
        if (unJFileChooser.showOpenDialog(null)==JFileChooser.APPROVE_OPTION)
        {
            uneFile=unJFileChooser.getSelectedFile();
            fichier=uneFile;
            try
            {
                FileReader fluxfichier=new FileReader(uneFile);
                BufferedReader fluxtampon=new BufferedReader(fluxfichier);
                String uneString=fluxtampon.readLine();
                String resultat="";
                while (uneString!=null)
                {
                    resultat=resultat+uneString+"\n";
                    uneString=fluxtampon.readLine();
                }
                feuille.setText(resultat);
                setTitle(uneFile.getName());
                feuille.setVisible(true);
                fluxtampon.close();
            }
            catch (Exception ex)
            {
                System.out.println(ex.toString());
            }
        }
    }


    public void ouvrirFile(String filename){
      int indexPt = filename.lastIndexOf('.');

      if (indexPt == -1)
        return;

      String ext = filename.substring(indexPt + 1, filename.length());

      if (!ext.equalsIgnoreCase("html") && !ext.equalsIgnoreCase("htm"))
        return;

      this.setVisible(true);
      File uneFile = new File(filename);
      fichier=uneFile;
      try
      {
        FileReader fluxfichier = new FileReader(uneFile);
        BufferedReader fluxtampon = new BufferedReader(fluxfichier);
        String uneString = fluxtampon.readLine();
        String resultat = "";
        while (uneString != null) {
          resultat = resultat + uneString + "\n";
          uneString = fluxtampon.readLine();
        }
        feuille.setText(resultat);
        setTitle(uneFile.getName());
        feuille.setVisible(true);
        fluxtampon.close();
      }
      catch (Exception ex) {
        System.out.println(ex.toString());
      }
    }



    private void enregistrer() {
        File uneFile;
        JFileChooser unJFileChooser=new JFileChooser();
        unJFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        unJFileChooser.setFileFilter(new OuvrirFiltre());
        File dir;
        if (FenetrePrincipale.INSTANCE.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() != null)
          dir = new File(FenetrePrincipale.INSTANCE.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().lnkBibliotheque.getAbsolutePath());
        else
          dir = new File(System.getProperty("user.dir"));
        unJFileChooser.setCurrentDirectory(dir);

        if (unJFileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
        {
            uneFile=VerifieFichier(unJFileChooser.getSelectedFile());
            fichier=uneFile;
            try
            {
                FileWriter unFileWriter=new FileWriter(uneFile);
                BufferedWriter unBuffer=new BufferedWriter(unFileWriter);
                feuille.write(unBuffer);
                unBuffer.close();
                if (getTitle().toLowerCase().trim().equals("nouveau_document*"))
                {
                    setTitle(fichier.getName());
                    stateFeuille=false;
                }
                else
                {
                    String uneString="";
                    setTitle(String.copyValueOf(getTitle().toCharArray(),0,getTitle().length()-1));
                    stateFeuille=false;
                }
                if(_panel != null){
					_fp.getLnkSysteme().associerContenu(_panel.get_elementCourant(), fichier.toURI());
                  	_panel.setFichier_associe(_panel.get_elementCourant().getContenu());                  
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex.toString());
            }
        }
        else
        {
            stateCancel=true;
        }
        _fp.getLnkArbreExplorateur().load();


    }



    private void quitter() {
        // on demande d'enregistrer uniquement s'il y a eu des modifications
        if (stateFeuille==true)
        {
        DialogueBox unDialogue=new DialogueBox(this,true);
        unDialogue.show();
        if (unDialogue.getReturnStatus()==DialogueBox.RET_OK)
        {
            //on enregistre en tenant compte des cas fichier connu ou non
            if (fichier!=null)
            {
                try
                {
                    FileWriter unFileWriter=new FileWriter(fichier);
                    BufferedWriter unBuffer=new BufferedWriter(unFileWriter);
                    feuille.write(unBuffer);
                    unBuffer.close();
                    String uneString="";
                    setTitle(String.copyValueOf(getTitle().toCharArray(),0,getTitle().length()-1));
                    stateFeuille=false;
                }
                catch (Exception ex)
                {
                    System.out.println(ex.toString());
                }
            }
            // On v\uFFFDrifie que l'utilisateur n'a pas annul\uFFFD
            if (stateCancel==false)
            {
                //System.exit(0);
                this.dispose();
            }
            else
            {
                stateCancel=false;
            }
        }
        else
        {
            if( unDialogue.getReturnStatus()==DialogueBox.RET_NON)
            {
                //System.exit(0);
                this.dispose();
            }
        }
        }
        else
        {
            //System.exit(0);
            this.dispose();
        }
    }

    private void enregistrer2() {

        File uneFile;
        JFileChooser unJFileChooser=new JFileChooser();
        unJFileChooser.setDialogType(JFileChooser.SAVE_DIALOG);
        unJFileChooser.setFileFilter(new OuvrirFiltre());

        if (fichier!=null)
        {
            unJFileChooser.setSelectedFile(fichier);
        }
        else{
          File dir;
          if (FenetrePrincipale.INSTANCE.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation() != null)
            dir = new File(FenetrePrincipale.INSTANCE.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().lnkBibliotheque.getAbsolutePath());
          else
            dir = new File(System.getProperty("user.dir"));
          unJFileChooser.setCurrentDirectory(dir);
        }

        if (unJFileChooser.showSaveDialog(null)==JFileChooser.APPROVE_OPTION)
        {
            uneFile=VerifieFichier(unJFileChooser.getSelectedFile());
            fichier=uneFile;
            try
            {
                FileWriter unFileWriter=new FileWriter(uneFile);
                BufferedWriter unBuffer=new BufferedWriter(unFileWriter);
                feuille.write(unBuffer);
                unBuffer.close();
                setTitle(uneFile.getName());
                stateFeuille=false;
                if(_panel != null){
                  _fp.getLnkSysteme().associerContenu(_panel.get_elementCourant(), fichier.toURI());
				  _panel.setFichier_associe(_panel.get_elementCourant().getContenu());
                }
            }
            catch (Exception ex)
            {
                System.out.println(ex.toString());
            }
        }
        else
        {
            stateCancel=true;
        }
        _fp.getLnkArbreExplorateur().load();
    }



    public File VerifieFichier(File unFichier) {
        //on v\uFFFDrifie si le fichier poss\uFFFDde une extension correcte
        File uneFile;
        String uneString=unFichier.getPath();
        if (!((uneString.toLowerCase().endsWith("htm"))||(uneString.toLowerCase().endsWith("html"))))
        {
            uneString=uneString+".htm";
            uneFile=new File(uneString);
        }
        else
        {
            uneFile=unFichier;
        }
        return uneFile;
    }

    private boolean stateFeuille = false;
    private boolean stateCancel = false;
    private File fichier = null;
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuBar BarreMenu;
    private javax.swing.JMenu MenuFichier;
    private javax.swing.JMenuItem MenuNouveau;
    private javax.swing.JMenuItem MenuOuvrir;
    private javax.swing.JMenuItem MenuSave;
    private javax.swing.JMenuItem MenuSaveSous;
    private javax.swing.JMenuItem MenuApercu;
    private javax.swing.JMenuItem quitter;
    private javax.swing.JMenuItem MenuAbout;
    private javax.swing.JMenu MenuEdition;
    private javax.swing.JMenuItem couper;
    private javax.swing.JMenuItem copier;
    private javax.swing.JMenuItem coller;
    private javax.swing.JMenuItem supprimer;
    private javax.swing.JMenuItem MenuSelectionner;
    private javax.swing.JPanel jPanel1;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JTextArea feuille;
    private javax.swing.JPanel ToolBarPanel;
    private javax.swing.JToolBar ToolBarFichier;
    private javax.swing.JButton nouveaubouton;
    private javax.swing.JButton ouvrirbouton;
    private javax.swing.JButton savebouton;
    private javax.swing.JButton apercubouton;
    private javax.swing.JToolBar ToolBarEdition;
    private javax.swing.JButton couperbouton;
    private javax.swing.JButton copierbouton;
    private javax.swing.JButton collerbouton;
    // End of variables declaration//GEN-END:variables
}

