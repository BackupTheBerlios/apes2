
package iepp.ui.preferences;

import iepp.Application;
import iepp.Projet;
import iepp.application.areferentiel.ElementReferentiel;
import iepp.application.areferentiel.Referentiel;
import iepp.domaine.DefinitionProcessus;
import iepp.domaine.IdObjetModele;
import iepp.domaine.PaquetagePresentation;


import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.FlowLayout;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;
import java.io.FileFilter;
import java.util.ArrayList;
import java.util.Vector;

import javax.swing.AbstractListModel;
import javax.swing.BorderFactory;
import javax.swing.DefaultComboBoxModel;
import javax.swing.JButton;
import javax.swing.JFileChooser;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.ListCellRenderer;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import util.IconManager;


public class PanneauDPGeneration extends PanneauOption
{

	private DefinitionProcessus defProc;
	
	private ComposantsListModel ComposantsListModel = new ComposantsListModel();
	private PPListModel PPListModel = new PPListModel();
	
	private JList LS_referentiel = new JList();
	private JList LS_arbre = new JList();
	
	private JButton BP_ajouter = new JButton();
	private JButton BP_retirer = new JButton();
	private JButton BP_monter = new JButton();
	private JButton BP_descendre = new JButton();
	
	private JScrollPane scrollPaneRef = new JScrollPane();
	private JScrollPane scrollPaneArbre = new JScrollPane();
	
	
	public static final String DP_GENERATION_PANEL_KEY = "GenerationTitle";
	
	public PanneauDPGeneration(String name)
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
		
		
		
		this.initPanelArbre(c, gridbag);
		
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

	private void initPanelArbre(GridBagConstraints c, GridBagLayout gridbag)
	{
		GridBagLayout g = new GridBagLayout();
		JPanel p = new JPanel(g);
		GridBagConstraints c2 = new GridBagConstraints();
		p.setBackground(Color.blue);
//		this.LS_referentiel.setPreferredSize(new Dimension(100,100));
		this.LS_referentiel.setBackground(Color.gray);
		this.LS_referentiel.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.LS_referentiel.setModel(this.PPListModel);
		
		this.LS_arbre.setVisibleRowCount(5);
		this.LS_arbre.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		this.LS_arbre.setModel(this.ComposantsListModel);
		this.LS_arbre.addListSelectionListener(new ListSelectionListener()
		{	
			public void valueChanged(ListSelectionEvent e) 
			{
				//System.out.println("le listener est bien ajouté");
				int indexSelect = LS_arbre.getSelectedIndex();
				if((indexSelect != -1) && (indexSelect < ComposantsListModel.getSize()))
				{	
					//System.out.println("bp enable");
					BP_retirer.setEnabled(true);
					
					Object a_verifier = (Object)ComposantsListModel.getListe().elementAt(indexSelect);
					if (a_verifier instanceof IdObjetModele)
					{
						
						//System.out.println("bp disable");
						BP_retirer.setEnabled(false);
					
					}
					
				}
				LS_arbre.setSelectedIndex(indexSelect);
			}
		});

		this.BP_ajouter.setText(Application.getApplication().getTraduction("ajouter_referentiel"));
		this.BP_ajouter.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheDroite.jpg"));
		this.BP_ajouter.setHorizontalTextPosition(SwingConstants.LEFT);
		this.BP_ajouter.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ajouter(e);
			}
		});
		
		p.add(this.BP_ajouter);
		this.BP_retirer.setText(Application.getApplication().getTraduction("retirer_referentiel"));
		this.BP_retirer.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheGauche.jpg"));
		this.BP_retirer.setHorizontalTextPosition(SwingConstants.RIGHT);
		this.BP_retirer.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				retirer(e);
			}
		});

		p.add(this.BP_retirer);
		this.BP_monter.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheHaut.jpg"));
		this.BP_monter.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				monter(e);
			}
		});
		
		p.add(this.BP_monter);
		this.BP_descendre.setIcon(IconManager.getInstance().getIcon(Application.getApplication().getConfigPropriete("dossierIcons") + "flecheBas.jpg"));
		this.BP_descendre.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				descendre(e);
			}
		});
		p.add(this.BP_descendre);
		
		this.scrollPaneRef.setViewportView(this.LS_referentiel);
		this.scrollPaneArbre.setViewportView(this.LS_arbre);
		p.add(this.scrollPaneArbre);
		p.add(this.scrollPaneRef);
		
		c.fill = GridBagConstraints.BOTH;
		c.weighty = 100;
		mPanel.add(p, c);
	}
	
	
	public void save ()
	{
		
	}
	
	private void ajouter(ActionEvent e)
	{
		int indexSelect = this.LS_referentiel.getSelectedIndex();
		
		// TODO 7 = taille de la liste du ref
		if((indexSelect != -1) && (indexSelect < PPListModel.getSize()))
		{
			PPListModel listeRef = (PPListModel)LS_referentiel.getModel();
			Object a_inserer = listeRef.getListe().elementAt(indexSelect);
			
			Referentiel ref = Application.getApplication().getReferentiel() ;
			String nomPaqPre = a_inserer.toString();
			long id= ref.nomPresentationToId(nomPaqPre) ;
			PaquetagePresentation paq = ref.chargerPresentation(id);
			if (paq != null)
			{
				  ComposantsListModel listeComp = (ComposantsListModel)LS_arbre.getModel();
				  listeComp.ajouter(paq);
				  this.LS_referentiel.setSelectedIndex(indexSelect-1);
				  listeRef.enlever(indexSelect);
			}
		}
	}
	private void retirer(ActionEvent e)
	{
			int indexSelect = this.LS_arbre.getSelectedIndex();
		
			// TODO 7 = taille de la liste du ref
			if((indexSelect != -1) && (indexSelect < this.ComposantsListModel.getSize()))
			{
				PPListModel listeRef = (PPListModel)LS_referentiel.getModel();
				ComposantsListModel listeComp = (ComposantsListModel)LS_arbre.getModel();
				Object a_retirer = listeComp.getListe().elementAt(indexSelect);
				if (a_retirer instanceof PaquetagePresentation)
				{
					String cheminFichier = ((PaquetagePresentation)a_retirer).getNomFichier();
					a_retirer = (cheminFichier.substring(cheminFichier.lastIndexOf(File.separator) + 1, cheminFichier.lastIndexOf(".")));
				}
				listeRef.ajouter(a_retirer);
				this.LS_arbre.setSelectedIndex(indexSelect-1);
				listeComp.enlever(indexSelect);
			}
	}
	private void descendre(ActionEvent e)
	{
		int indexSelect = this.LS_arbre.getSelectedIndex();
		if((indexSelect != -1) && (indexSelect < this.ComposantsListModel.getSize()-1))
		{
			this.ComposantsListModel.descendre(indexSelect);
			this.LS_arbre.setSelectedIndex(indexSelect+1);
		}
	}
	private void monter(ActionEvent e)
	{
		int indexSelect = this.LS_arbre.getSelectedIndex();
		if((indexSelect != -1) && (indexSelect > 0))
		{
			this.ComposantsListModel.monter(indexSelect);
			this.LS_arbre.setSelectedIndex(indexSelect-1);
		}
	}
	

	private class ComboStylesModel extends DefaultComboBoxModel
	{
		private ArrayList fichiersStyles = new ArrayList();
		
		public ComboStylesModel()
		{
			File repStyles = new File(Application.getApplication().getConfigPropriete("styles"));
			File[] lesFichiersCss = repStyles.listFiles(new FileFilter()
			{	
				String fileExtension = Application.getApplication().getConfigPropriete("extensionFeuilleStyle").toString();
				public boolean accept(File fich)
				{	
					String nom = fich.getName();
					String extension = nom.substring((nom.lastIndexOf(".")+1));
					return extension.equals(this.fileExtension);
				}
			});
			for(int i = 0 ; i < lesFichiersCss.length ; i++)
			{
				fichiersStyles.add(lesFichiersCss[i]);
			}
		}
			
		public Object getElementAt(int index)
		{
			return (File)fichiersStyles.get(index);
		} 
		
		public int getSize()
		{
			return fichiersStyles.size();
		}
		
	}
	private abstract class ListModel extends AbstractListModel
	{
		public Vector liste = new Vector();

		public Object getElementAt(int index)
		{
			return liste.elementAt(index);
		} 
		
		public int getSize()
		{
			return liste.size();
		}
			
		public void monter(int index)
		{
			Object buffer = getElementAt(index);
			liste.removeElementAt(index);
			liste.add(index-1, buffer);
			fireContentsChanged(this, index-1, index);
		}
			
		public void descendre(int index)
		{
			Object buffer = getElementAt(index);
			liste.removeElementAt(index);
			liste.add(index+1, buffer);
			fireContentsChanged(this, index, index+1);
		}
		
		public void ajouter(Object o)
		{
			liste.add(o);
			fireContentsChanged(this, 0, liste.size());
		}
		
		public void enlever(int i)
		{
			liste.removeElementAt(i);
			fireContentsChanged(this, 0 , liste.size());
		}
		
		public Vector getListe()
		{
			return(this.liste);
		}
	}
	private class ComposantsListModel extends ListModel
	{
		private Vector listeComp = new Vector();
		public ComposantsListModel()
		{
			liste = new Vector();
			for (int i = 0;i<Application.getApplication().getProjet().getDefProc().getListeComp().size();i++)
			{
				liste.add(Application.getApplication().getProjet().getDefProc().getListeComp().elementAt(i)) ;
			}
		}
		public void verifier_supprimer(ListSelectionEvent e,int i)
		{
			
			
		}
		
	}
	private class PPListModel extends ListModel
	{
		private Vector listeComp = new Vector();
		public PPListModel()
		{
			liste = new Vector();
			Vector listePaquetage = Application.getApplication().getReferentiel().getListeNom(ElementReferentiel.PRESENTATION);
			for (int i = 0;i<listePaquetage.size();i++)
			{
				liste.add(listePaquetage.elementAt(i)) ;
			}
		}
	}

	private class ManagerButton implements ActionListener
	{
		public void actionPerformed(ActionEvent e)
		{
			//récupérer l'objet source de l'évènement reçu
			Object source = e.getSource();
			// selon cet objet, réagir en conséquence
		}
	}
}
