
package iepp.ui.preferences;

import iepp.Application;
import iepp.Projet;
import iepp.domaine.DefinitionProcessus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class PanneauDP extends PanneauOption
{
	private JTextField sNomDP;
	private JTextField sAuteurDP;
	private JTextField sEmailDP;
	
	private JButton browseButton3;
	private JTextField mDefaultPaq;
	private JButton browseButton4;
	
	private DefinitionProcessus defProc = null;
	
	public static final String DP_PANEL_KEY = "Propriete_DP";
	
	public PanneauDP(String name)
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

//		 linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);

		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label = new JLabel(Application.getApplication().getTraduction("Nom_DP"));
		gridbag.setConstraints(label, c);
		mPanel.add(label);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		sNomDP = new JTextField(25);
		mPanel.add(sNomDP);
		gridbag.setConstraints(sNomDP, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);		

		//linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		// auteur
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label3 = new JLabel(Application.getApplication().getTraduction("Auteur_DP"));
		gridbag.setConstraints(label3, c);
		mPanel.add(label3);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		sAuteurDP = new JTextField(25);
		mPanel.add(sAuteurDP);
		gridbag.setConstraints(sAuteurDP, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);	
		
		//linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		// email
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label4 = new JLabel(Application.getApplication().getTraduction("E_mail_DP"));
		gridbag.setConstraints(label4, c);
		mPanel.add(label4);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		sEmailDP = new JTextField(25);
		mPanel.add(sEmailDP);
		gridbag.setConstraints(sEmailDP, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);	
		
		// linefeed   
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0; 
		 c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
       
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);   
		
		// initialiser les champs
		if (this.defProc != null)
		{
		    this.sNomDP.setText(this.defProc.getNomDefProc());
		    this.sAuteurDP.setText(this.defProc.getAuteur());
		    this.sEmailDP.setText(this.defProc.getEmailAuteur());
		}
	}
	
	
	public PanneauOption openPanel(String key)
	{
		this.setName(Application.getApplication().getTraduction(key)) ;
		return this ;
	}

	
	public void save ()
	{
	    this.defProc.setNomDefProc(this.sNomDP.getText());
	    this.defProc.setAuteur(this.sAuteurDP.getText());
	    this.defProc.setEmailAuteur(this.sEmailDP.getText());
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
