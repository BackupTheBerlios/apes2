package iepp.ui.preferences;

import iepp.Application;
import iepp.Projet;
import iepp.application.ageneration.GenerationManager;
import iepp.application.ageneration.TacheGeneration;
import iepp.domaine.DefinitionProcessus;

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
	private TaskMonitorDialog dialogAvancee = null;
	private TacheGeneration tacheGener;
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

		// linefeed
		c.weighty = 0;      		
		c.gridwidth = GridBagConstraints.REMAINDER; //end row
		makeLabel(" ", gridbag, c);
		
		this.bGenerer = new JButton("Generer");
		this.bGenerer.addActionListener(man);
		c.fill = GridBagConstraints.BOTH;
		mPanel.add(this.bGenerer, c);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;   		
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

	/*
	public void save ()
	{
		
	}
	*/
	
	/**
	 * Vérifie les informations saisies 
	 * @return
	 */
	public boolean verifierDonnees()
	{
		return true;
	}
	
	public void initGeneration()
	{
		// on sauvegarde l'ordre de la liste dans le Generation Manager
		GenerationManager.getInstance().setListeAGenerer(this.defProc.getListeAGenerer());
		// on modifie le chemin de generation
		GenerationManager.getInstance().setCheminGeneration(this.defProc.getRepertoireGeneration());
		//on modifie la couleur des éléments sélectionnés dans l'arbre
		GenerationManager.getInstance().setCouleurSurligne(new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre"))));
		// feuille de style
		GenerationManager.getInstance().setFeuilleCss(Application.getApplication().getConfigPropriete("feuille_style"));
		// contenu
		GenerationManager.getInstance().setPlaceContenu(Application.getApplication().getConfigPropriete("place_contenu"));
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
				PanneauGeneration.this.initGeneration();
				PanneauGeneration.this.tacheGener = new TacheGeneration();
				PanneauGeneration.this.dialogAvancee = new TaskMonitorDialog(Application.getApplication().getFenetrePrincipale(), PanneauGeneration.this.tacheGener);
				PanneauGeneration.this.dialogAvancee.setTitle(Application.getApplication().getTraduction("generation_en_cours"));
				
				PanneauGeneration.this.tacheGener.setTask(dialogAvancee);
				PanneauGeneration.this.dialogAvancee.show();
			
				if(tacheGener.isGenerationReussie())
				{
					 JOptionPane.showMessageDialog(Application.getApplication().getFenetrePrincipale(),Application.getApplication().getTraduction("Generation_ok"),
					 				Application.getApplication().getTraduction("Generation_site_titre"),
									JOptionPane.INFORMATION_MESSAGE);
				}
			}
		}
	}
}
