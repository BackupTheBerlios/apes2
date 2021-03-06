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

 * DialogueAbout.java

 *

 * Created on 13 avril 2002, 18:55

 */



package POG.outil.html.pika;



/**

 *

 * @author  Administrateur

 */

public class DialogueAbout extends javax.swing.JDialog {

    /** A return status code - returned if Cancel button has been pressed */

    public static final int RET_CANCEL = 0;

    /** A return status code - returned if OK button has been pressed */

    public static final int RET_OK = 1;



    /** Creates new form DialogueAbout */

    public DialogueAbout(java.awt.Frame parent, boolean modal) {

        super(parent, modal);

        initComponents();

        jTextArea1.setText("Ce programme est un logiciel libre, vous pouvez le redistribuer et/ou le modifier selon les termes de la GNU General Public License telle que publi�e par la Free Software Foundation � la version 2 de la license ou ult�rieur.");

    }



    /** @return the return status of this dialog - one of RET_OK or RET_CANCEL */

    public int getReturnStatus() {

        return returnStatus;

    }



    /** This method is called from within the constructor to

     * initialize the form.

     * WARNING: Do NOT modify this code. The content of this method is

     * always regenerated by the Form Editor.

     */

    private void initComponents() {//GEN-BEGIN:initComponents

        buttonPanel = new javax.swing.JPanel();

        okButton = new javax.swing.JButton();

        jLabel1 = new javax.swing.JLabel();

        jTextArea1 = new javax.swing.JTextArea();



        setTitle("A propos de...");

        addWindowListener(new java.awt.event.WindowAdapter() {

            public void windowClosing(java.awt.event.WindowEvent evt) {

                closeDialog(evt);

            }

        });



        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.CENTER, 5, 10));



        okButton.setText("OK");

        okButton.addActionListener(new java.awt.event.ActionListener() {

            public void actionPerformed(java.awt.event.ActionEvent evt) {

                okButtonActionPerformed(evt);

            }

        });



        buttonPanel.add(okButton);



        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);
        getContentPane().add(jLabel1, java.awt.BorderLayout.WEST);


        jTextArea1.setLineWrap(true);

        jTextArea1.setEditable(false);

        jTextArea1.setRows(3);

        getContentPane().add(jTextArea1, java.awt.BorderLayout.CENTER);



        pack();

        java.awt.Dimension screenSize = java.awt.Toolkit.getDefaultToolkit().getScreenSize();

        setSize(new java.awt.Dimension(350, 230));

        setLocation((screenSize.width-350)/2,(screenSize.height-230)/2);

    }//GEN-END:initComponents



    private void okButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_okButtonActionPerformed

        doClose(RET_OK);

    }//GEN-LAST:event_okButtonActionPerformed



    /** Closes the dialog */

    private void closeDialog(java.awt.event.WindowEvent evt) {//GEN-FIRST:event_closeDialog

        doClose(RET_CANCEL);

    }//GEN-LAST:event_closeDialog



    private void doClose(int retStatus) {

        returnStatus = retStatus;

        setVisible(false);

        dispose();

    }





    // Variables declaration - do not modify//GEN-BEGIN:variables

    private javax.swing.JPanel buttonPanel;

    private javax.swing.JButton okButton;

    private javax.swing.JLabel jLabel1;

    private javax.swing.JTextArea jTextArea1;

    // End of variables declaration//GEN-END:variables



    private int returnStatus = RET_CANCEL;

}

