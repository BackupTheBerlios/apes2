package iepp.ui.preferences;

import iepp.Application;
import iepp.application.ageneration.GenerationManager;

import java.awt.BorderLayout;
import java.awt.Color;
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
import javax.swing.JPanel;
import javax.swing.JRadioButton;
import javax.swing.JTextField;
import javax.swing.border.Border;
import javax.swing.border.EtchedBorder;
import javax.swing.border.TitledBorder;

import org.ipsquad.apes.ui.PreferencesDialog;


public class PanneauGeneration extends PanneauOption 
{
	private JTextField mDefaultPath;
	private JComboBox mStyles;
	private String mOldStyle;
	private JButton mBackgroundColorButton;
	private JButton browseButton;
	private JRadioButton mAvant;
	private JRadioButton mApres;
	
	
	
	public static final String GENERATION_PANEL_KEY = "GenerationTitle";
	
	public PanneauGeneration(String name)
	{
		// lien vers l'ancienne feuille de style utilis�e
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
		
		// couleur des �lements dans l'arbre
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
		
		//Style
		c.weighty = 1 ;
		//c.gridheight = 1; //end row
		c.fill = GridBagConstraints.BOTH;
		c.anchor = GridBagConstraints.WEST;
		JPanel style = new JPanel();
		Border loweredetched = BorderFactory.createEtchedBorder(EtchedBorder.LOWERED);
		TitledBorder titleStyle = BorderFactory.createTitledBorder( loweredetched,Application.getApplication().getTraduction("PlaceContenu"));
		
		style.setBorder(titleStyle);
		gridbag.setConstraints(style, c);
		mPanel.add(style);
		ButtonGroup groupe_contenu_html = new ButtonGroup();
		this.mAvant = new JRadioButton(Application.getApplication().getTraduction("ContenuAvant"),
				Application.getApplication().getConfigPropriete("place_contenu").equals(GenerationManager.AVANT_CONTENU));
		this.mApres = new JRadioButton(Application.getApplication().getTraduction("ContenuApres"),
		        Application.getApplication().getConfigPropriete("place_contenu").equals(GenerationManager.APRES_CONTENU));
		groupe_contenu_html.add(this.mAvant);
		groupe_contenu_html.add(this.mApres);
		
		style.setLayout(new GridLayout(3,1));
		style.add(mAvant);
		style.add(mApres);

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
		// r�cup�re la couleur choisie dans la bd
		Application.getApplication().setConfigPropriete("couleur_arbre", "" + mBackgroundColorButton.getBackground().getRGB());

		if (this.mAvant.isSelected())
		    Application.getApplication().setConfigPropriete("place_contenu", GenerationManager.AVANT_CONTENU);
		else if (this.mApres.isSelected())
		    Application.getApplication().setConfigPropriete("place_contenu", GenerationManager.APRES_CONTENU);
	}
	
	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//r�cup�rer l'objet source de l'�v�nement re�u
			Object source = e.getSource();
			// selon cet objet, r�agir en cons�quence
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
				// si l'utilisateur choisit annuler, la bd renvoie null, donc on v�rifie le retour
				if (couleur_choisie != null)
				{
					PanneauGeneration.this.mBackgroundColorButton.setBackground(couleur_choisie);
				}
			}
		}
	}
}
