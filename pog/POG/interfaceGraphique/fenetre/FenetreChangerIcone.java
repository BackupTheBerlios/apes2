/*
 * Created on 19 oct. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package POG.interfaceGraphique.fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.Box;
import javax.swing.JButton;
import javax.swing.JComboBox;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import POG.objetMetier.PresentationElementModele;
import POG.utile.PogToolkit;

/**
 * @author c82aber
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FenetreChangerIcone extends FenetrePOG {

	  JPanel jPanel1 = new JPanel();
	  JPanel jPanel2 = new JPanel(); //nom de la presentation
	  JPanel jPanel4 = new JPanel(); //nom de la biblio
	  JPanel jPanel5 = new JPanel(); //biblio
	  JPanel jPanel6 = new JPanel(); //bouton
	  JLabel jLabel1 = new JLabel();
	  JButton jButtonP = new JButton();
	  JComboBox lestypes;
//	  JTextField jTextFieldNomPres = new JTextField();
	  JLabel jLabel2 = new JLabel();
	  JTextField jTextFieldChemIco = new JTextField();
	  Box box1 = Box.createVerticalBox();
	  Box box2 = Box.createVerticalBox();
	  Box box3 = Box.createVerticalBox();
	  BorderLayout borderLayout1 = new BorderLayout();
	  GridLayout gridLayout1 = new GridLayout();
	  private String [] _lestypes;
	  
	  public FenetreChangerIcone(FenetrePrincipale fp) {
		super(fp);
		this.setResizable(false);
		try {
			HashMap types = lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_produit();
			_lestypes = new String[types.size()];
			int i = 0;
			for(Iterator it = types.entrySet().iterator(); it.hasNext();)
			{
			  Map.Entry entry = (Map.Entry)it.next();
			  _lestypes[i++] = (String)entry.getKey();
			}
		  jbInit();
		  this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("changertoutesicones"));
		  jTextFieldChemIco.setText(this.lnkFenetrePrincipale.getLnkSysteme().getLnkPreferences().get_pathIconeDefaut());
		  PogToolkit.centerWindow(this);
		}
		catch (Exception e) {
		  e.printStackTrace();
		}
	  }

	  public void actionOK() {
		  super.setVisible(false);
		  if (!new File(jTextFieldChemIco.getText()).exists())
		  	return;
		  Object[] toto = lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().listeElementPresentation();
		  for (int i = 0; i < toto.length; i++) {
		  	if (toto[i] instanceof PresentationElementModele) {
				PresentationElementModele ele = (PresentationElementModele) toto[i];
				if (ele.get_typeProduit().equals(lestypes.getSelectedItem()))
					lnkFenetrePrincipale.getLnkSysteme().changerIcone(ele, new File(jTextFieldChemIco.getText()));
		  	}
		  }
	  };

	  public void actionAnnuler() {
		super.setVisible(false);
	  };

	  private void jbInit() throws Exception {
		jLabel1.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("typeelement"));
		jLabel2.setText(lnkFenetrePrincipale.getLnkLangues().valeurDe("chemico"));
		jPanel1.setLayout(gridLayout1);
		gridLayout1.setRows(2);
		lestypes = new JComboBox(_lestypes);
		jTextFieldChemIco.setMinimumSize(new Dimension(200,20));
		jTextFieldChemIco.setMaximumSize(new Dimension(200, 20));
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		jPanel1.add(jPanel2, null);
		jPanel2.setLayout(new BorderLayout());
		jPanel2.add(jLabel1, BorderLayout.WEST);
		box3.add(Box.createRigidArea(new Dimension(0, 20)));
		box3.add(lestypes);
		box3.add(Box.createRigidArea(new Dimension(0, 20)));
		jPanel2.add(box3, BorderLayout.CENTER);
		jPanel5.setLayout(new BorderLayout());
		jPanel1.add(jPanel5, null);
		jPanel5.add(jLabel2, BorderLayout.WEST);
		box1.add(Box.createRigidArea(new Dimension(0, 20)));
		box1.add(jTextFieldChemIco);
		box1.add(Box.createRigidArea(new Dimension(0, 20)));
		jPanel5.add(box1, BorderLayout.CENTER);
		Dimension dimension = new Dimension(0, 15);
		box2.add(Box.createRigidArea(dimension));
		jButtonP.setText("...");
		jButtonP.addActionListener(new ActionListener() {

		  public void actionPerformed(ActionEvent evt) {
			File ico = lnkFenetrePrincipale.iconeChooser(jTextFieldChemIco.getText());
			if (ico != null)
				jTextFieldChemIco.setText(ico.getAbsolutePath());
		  }
		});

		jButtonP.setPreferredSize(new Dimension(30, 25));
		jPanel6.add(jButtonP);
		box2.add(jPanel6,null);
		box2.add(Box.createRigidArea(dimension));
		jPanel5.add(box2,BorderLayout.EAST);
		this.setSize(new Dimension(400, 175));

	  }
	}

