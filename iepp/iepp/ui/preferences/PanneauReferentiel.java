
package iepp.ui.preferences;

import iepp.Application;

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

import util.SimpleFileFilter;


public class PanneauReferentiel extends PanneauOption 
{
	private JTextField mDefaultPath;
	private JButton browseButton;
	private JTextField mDefaultRef;
	private JButton browseButton2;
	
	public static final String REPOSITORY_PANEL_KEY = "RepositoryTitle";
	
	private SimpleFileFilter filter = new SimpleFileFilter("ref", "Référentiel") ;
	
	public PanneauReferentiel(String name)
	{

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
		
		// rep référentiel
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label = new JLabel(Application.getApplication().getTraduction("CheminReferentiel"));
		gridbag.setConstraints(label, c);
		mPanel.add(label);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		mDefaultPath = new JTextField(25);
		mDefaultPath.setText(Application.getApplication().getConfigPropriete("chemin_referentiel"));
		mPanel.add(mDefaultPath);
		gridbag.setConstraints(mDefaultPath, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		this.browseButton = new JButton(Application.getApplication().getTraduction("Parcourir"));
		browseButton.addActionListener(man);
		gridbag.setConstraints(browseButton, c);
		mPanel.add(browseButton);		
		
		//linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		
		// referentiel par défaut
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label2 = new JLabel(Application.getApplication().getTraduction("ReferentielDefaut"));
		gridbag.setConstraints(label2, c);
		mPanel.add(label2);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		mDefaultRef = new JTextField(25);
		mDefaultRef.setText(Application.getApplication().getConfigPropriete("referentiel_demarrage"));
		mPanel.add(mDefaultRef);
		gridbag.setConstraints(mDefaultRef, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		this.browseButton2 = new JButton(Application.getApplication().getTraduction("Parcourir"));
		browseButton2.addActionListener(man);
		gridbag.setConstraints(browseButton2, c);
		mPanel.add(browseButton2);		
		

		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0; 
		// linefeed     		
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

	
	public void save ()
	{
		Application.getApplication().setConfigPropriete("chemin_referentiel", mDefaultPath.getText());
		Application.getApplication().setConfigPropriete("referentiel_demarrage", mDefaultRef.getText());
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			Object source = e.getSource();
			// selon cet objet, réagir en conséquence
			if (source == PanneauReferentiel.this.browseButton)
			{
				JFileChooser fileChooser = new JFileChooser(PanneauReferentiel.this.mDefaultPath.getText());
				fileChooser.setDialogTitle(Application.getApplication().getTraduction("titre_choix_rep"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fileChooser.showDialog(PanneauReferentiel.this, Application.getApplication().getTraduction("OK"));
				if(res == JFileChooser.APPROVE_OPTION)
					PanneauReferentiel.this.mDefaultPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
			if (source == PanneauReferentiel.this.browseButton2)
			{
				JFileChooser fileChooser;
				if (PanneauReferentiel.this.mDefaultPath.getText() != "")
					fileChooser = new JFileChooser(PanneauReferentiel.this.mDefaultPath.getText());
				else
					fileChooser = new JFileChooser(Application.getApplication().getConfigPropriete("chemin_referentiel"));
				
				fileChooser.setAcceptAllFileFilterUsed(false);
				fileChooser.setFileFilter(filter);
				fileChooser.setFileSelectionMode(JFileChooser.FILES_ONLY);
				fileChooser.setDialogTitle(Application.getApplication().getTraduction("titre_choix_rep"));
				int res = fileChooser.showDialog(PanneauReferentiel.this, Application.getApplication().getTraduction("OK"));
				if(res == JFileChooser.APPROVE_OPTION)
					PanneauReferentiel.this.mDefaultRef.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
		}
	}
}
