
package iepp.ui.preferences;

import iepp.Application;
import iepp.Projet;
import iepp.domaine.DefinitionProcessus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class PanneauDPDescription extends PanneauOption
{
	private JTextArea sCommentaireDP;
	private JTextField sContenuDesc;
	
	private DefinitionProcessus defProc;
	private JButton browseButton;
	
	public static final String DP_DESCRIPTION_PANEL_KEY = "Propriete_DP_Desc";
	
	public PanneauDPDescription(String name)
	{
	    Projet p = Application.getApplication().getProjet();
	    if (p != null)
	    {
	        this.defProc = Application.getApplication().getProjet().getDefProc();
	    }
		this.mTitleLabel = new JLabel (name) ;
		this.setLayout(new BorderLayout());
		mPanel = new JPanel() ;
		GridBagLayout gridbag = new GridBagLayout();
		mPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();
		ManagerButton man = new ManagerButton();

		// Title
		c.weightx = 1.0;
		c.weighty = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row			//	title
		this.mTitleLabel = new JLabel (name);
		TitledBorder titleBor = BorderFactory.createTitledBorder(BorderFactory.createLineBorder(Color.BLACK));
		titleBor.setTitleJustification(TitledBorder.CENTER);
		mTitleLabel.setBorder(titleBor);
		gridbag.setConstraints(mTitleLabel, c);
		mPanel.add(mTitleLabel);

		// linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		// fichier contenu
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label = new JLabel(Application.getApplication().getTraduction("ContenuDP"));
		gridbag.setConstraints(label, c);
		mPanel.add(label);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		sContenuDesc = new JTextField(25);
		sContenuDesc.setText(Application.getApplication().getConfigPropriete("chemin_referentiel"));
		mPanel.add(sContenuDesc);
		gridbag.setConstraints(sContenuDesc, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		this.browseButton = new JButton(Application.getApplication().getTraduction("Parcourir"));
		browseButton.addActionListener(man);
		gridbag.setConstraints(browseButton, c);
		mPanel.add(browseButton);		
		
//		 linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);

//		 fichier contenu
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label2 = new JLabel(Application.getApplication().getTraduction("Commentaire_DP"));
		gridbag.setConstraints(label2, c);
		mPanel.add(label2);

		c.weightx = 4 ;
		c.weighty = 6;
		c.gridwidth = GridBagConstraints.REMAINDER;
		sCommentaireDP = new JTextArea(100,100);
		JScrollPane sp = new JScrollPane(sCommentaireDP);
		mPanel.add(sp);
		gridbag.setConstraints(sp, c);
		
		
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;   		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);   
		
		// initialiser les champs
		if (this.defProc != null)
		{
		    sCommentaireDP.setText(this.defProc.getCommentaires());
		}
	}
	
	
	public PanneauOption openPanel(String key)
	{
		this.setName(Application.getApplication().getTraduction(key)) ;
		return this ;
	}

	
	public void save ()
	{
	    this.defProc.setNomDefProc(this.sCommentaireDP.getText());
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			Object source = e.getSource();
			// selon cet objet, réagir en conséquence
			/*
			if (source == PanneauDP.this.browseButton4)
			{
				JFileChooser fileChooser = new JFileChooser(PanneauDP.this.mDefaultComp.getText());
				fileChooser.setDialogTitle(Application.getApplication().getTraduction("titre_choix_rep"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fileChooser.showDialog(PanneauDP.this, Application.getApplication().getTraduction("OK"));
				if(res == JFileChooser.APPROVE_OPTION)
				    PanneauDP.this.mDefaultPaq.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
			*/
		}
	}
}
