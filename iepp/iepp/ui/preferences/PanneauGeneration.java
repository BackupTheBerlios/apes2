package iepp.ui.preferences;

import iepp.Application;
import iepp.Projet;
import iepp.application.CGenererSite;
import iepp.application.ageneration.GenerationManager;
import iepp.application.ageneration.TacheGeneration;
import iepp.domaine.DefinitionProcessus;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.GridLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;


import javax.swing.BorderFactory;
import javax.swing.ButtonGroup;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.ipsquad.apes.ui.PreferencesDialog;

import util.TaskMonitorDialog;


public class PanneauGeneration extends PanneauOption 
{
	private DefinitionProcessus defProc;
	private JButton bGenerer;
	
	public static final String GENERATION_PANEL_KEY = "Generation_GO_Desc";
	
	public PanneauGeneration(String name)
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

		//linefeed
		c.weighty = 1;  
		c.gridx = 0; c.gridy = 1;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		
		//linefeed
		c.weighty = 1;  
		c.gridx = 0; c.gridy = 3;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		this.bGenerer = new JButton("Generer");
		this.bGenerer.addActionListener(man);
		c.gridx = 2; c.gridy = 4;
		c.gridwidth = 1;
		mPanel.add(this.bGenerer, c);
		
		//linefeed
		c.weighty = 1;  
		c.gridx = 0; c.gridy = 5;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
	
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);  
	}
	
	
	public PanneauOption openPanel(String key)
	{
		this.setName(Application.getApplication().getTraduction(key)) ;
		return this ;
	}

	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			Object source = e.getSource();
			
			// sauver les paramètres
			if (source == PanneauGeneration.this.bGenerer)
			{	
				CGenererSite c = new CGenererSite(PanneauGeneration.this.defProc) ;
				if (c.executer())
				{
					 JOptionPane.showMessageDialog(Application.getApplication().getFenetrePrincipale(),Application.getApplication().getTraduction("Generation_ok"),
					 				Application.getApplication().getTraduction("Generation_site_titre"),
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
}
