package iepp.ui.preferences;

import iepp.Application;
import iepp.application.ageneration.GenerationManager;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.util.Vector;


import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JColorChooser;
import javax.swing.JComboBox;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.border.TitledBorder;


public class PanneauGeneration extends PanneauOption 
{
	private JTextField mDefaultPath;
	private String mPanelKey;
	private JComboBox mStyles;
	private String mOldStyle;
	private JButton mBackgroundColorButton;
	private JButton browseButton;
	
	public static final String GENERATION_PANEL_KEY = "GenerationTitle";
	
	public PanneauGeneration(String name)
	{
		// lien vers l'ancienne feuille de style utilisée
		this.mOldStyle = Application.getApplication().getConfigPropriete("feuille_style");
		boolean trouve = false;
		
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
		
		// rep generation
		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;
		JLabel label = new JLabel(Application.getApplication().getTraduction("rep_site"));
		gridbag.setConstraints(label, c);
		mPanel.add(label);
		c.weightx = 3 ;
		c.gridwidth = GridBagConstraints.RELATIVE;
		mDefaultPath = new JTextField(25);
		mDefaultPath.setText(Application.getApplication().getConfigPropriete("repertoire_generation"));
		mPanel.add(mDefaultPath);
		gridbag.setConstraints(mDefaultPath, c);
		c.weightx = 0 ;
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		this.browseButton = new JButton(Application.getApplication().getTraduction("Parcourir"));
		browseButton.addActionListener(man);
		gridbag.setConstraints(browseButton, c);
		mPanel.add(browseButton);			

		c.fill = GridBagConstraints.VERTICAL;    		
		makeLabel(" ", gridbag, c);
		
		// Styles
		c.fill = GridBagConstraints.VERTICAL;
		c.gridwidth = 3 ;//next-to-last in row
		makeLabel(Application.getApplication().getTraduction("style_pages"), gridbag, c);
		
		Vector styles = Application.getApplication().getStyles();
		mStyles = new JComboBox(styles);
		for (int i = 0; i < mStyles.getModel().getSize() && !trouve;i++)
		{
			if (mOldStyle.equals(((String)mStyles.getModel().getElementAt(i))))
			{
				mStyles.setSelectedIndex(i);
				trouve = true;
			}
		}
		if (!trouve && mStyles.getModel().getSize()> 0) mStyles.setSelectedIndex(0);
		
		gridbag.setConstraints(mStyles, c);
		mPanel.add(mStyles);
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		makeLabel(" ", gridbag, c);
		
		// couleur des élements dans l'arbre
		mBackgroundColorButton = new JButton("");
		c.gridwidth = 6 ;//next-to-last in row
		makeLabel(Application.getApplication().getTraduction("couleur_surlign"), gridbag, c);
		mBackgroundColorButton.addActionListener(man);
		mBackgroundColorButton.setBackground(new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre"))));
	
		gridbag.setConstraints(mBackgroundColorButton, c);
		mPanel.add(mBackgroundColorButton);
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		makeLabel(" ", gridbag, c);
		
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
		Application.getApplication().setConfigPropriete("repertoire_generation", mDefaultPath.getText());
		if ( mStyles.getSelectedItem().toString() != null)
		{
			Application.getApplication().setConfigPropriete("feuille_style", mStyles.getSelectedItem().toString());
		}
		else
		{
			Application.getApplication().setConfigPropriete("feuille_style","");
		}
		// récupère la couleur choisie dans la bd
		Application.getApplication().setConfigPropriete("couleur_arbre", "" + mBackgroundColorButton.getBackground().getRGB());
		
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			Object source = e.getSource();
			// selon cet objet, réagir en conséquence
			if (source == PanneauGeneration.this.browseButton)
			{
				JFileChooser fileChooser = new JFileChooser(PanneauGeneration.this.mDefaultPath.getText());
				fileChooser.setDialogTitle(Application.getApplication().getTraduction("titre_choix_rep"));
				fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
				int res = fileChooser.showDialog(PanneauGeneration.this, Application.getApplication().getTraduction("OK"));
				if(res == JFileChooser.APPROVE_OPTION)
					PanneauGeneration.this.mDefaultPath.setText(fileChooser.getSelectedFile().getAbsolutePath());
			}
			else if (source == PanneauGeneration.this.mBackgroundColorButton)
			{
				Color couleur_choisie = JColorChooser.showDialog(PanneauGeneration.this,Application.getApplication().getConfigPropriete("choix_couleur"), new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre"))));
				// si l'utilisateur choisit annuler, la bd renvoie null, donc on vérifie le retour
				if (couleur_choisie != null)
				{
					PanneauGeneration.this.mBackgroundColorButton.setBackground(couleur_choisie);
				}
			}
		}
	}
}
