/*
 * IEPP: Isi Engineering Process Publisher
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
 * 
 */
package iepp.ui;

import javax.swing.*;
import java.awt.*;
import java.awt.event.*;
import java.util.*;
import java.io.*;
import iepp.Application;
import iepp.application.ageneration.GenerationManager;

/**
 * 
 */
public class FenetrePreferences extends JDialog
{
	public static final int OK_OPTION = 0;
	public static final int CANCEL_OPTION = 1;
	private int resultat;	
	
	private JPanel panel1 = new JPanel();
	private JPanel panelRepSite = new JPanel();
	private JPanel panelBoutons = new JPanel();
	private JPanel panelStyle = new JPanel();
	private JPanel panelCouleurFond = new JPanel();
	private JPanel panelLangues = new JPanel();
	
	private JTextField ES_repSite = new JTextField();
	private JLabel STC_repSite = new JLabel();
	private JLabel STC_style = new JLabel();
	private JLabel STC_couleur_surlign = new JLabel();
	private JLabel STC_couleur_fond = new JLabel();
	private JLabel STC_Langues = new JLabel();
	
	private JComboBox LD_styles = new JComboBox();
	private JComboBox LD_langues = new JComboBox();
	
	private JButton BP_parcourir = new JButton();
	private JButton BP_ok = new JButton();
	private JButton BP_annuler = new JButton();
	private JButton BP_couleur_surlign = new JButton();
	private JButton BP_diagram = new JButton();
	
	private GridBagConstraints constraintsPanel1 = new GridBagConstraints();
	private GridBagConstraints constraintsCouleurFond = new GridBagConstraints();
	private GridBagConstraints constraintsLangues = new GridBagConstraints(); 
	private GridBagConstraints constraintsStyle = new GridBagConstraints(); 
	
	private GridBagLayout gridBagLayoutPanel1 = new GridBagLayout();
	private GridBagLayout gridBagLayoutCouleurFond = new GridBagLayout();
	private GridBagLayout gridBagLayoutLangues = new GridBagLayout();
	private GridBagLayout gridBagLayoutStyle = new GridBagLayout();
	
	public FenetrePreferences(FenetrePrincipale parent, String nom)
	{
		super(parent, Application.getApplication().getTraduction("Preferences"), true);
		this.addWindowListener(new WindowAdapter()
		{
			public void windowClosing(WindowEvent e)
			{
				annuler(e);
			}
		});
	
		this.init();
		this.initPanelRepSite();
		this.initPanelStyle();
		this.initPanelCouleurFond();
		this.initPanelLangues();
		this.initPanelBoutons();
		this.initPanelBackground();
		
		this.getContentPane().add(this.panel1, BorderLayout.CENTER);
		this.getContentPane().add(this.panelBoutons, BorderLayout.SOUTH);
		
		this.pack();
		
		Rectangle bounds = parent.getBounds();
		this.setLocation(bounds.x+ (int) bounds.width / 2 - this.getWidth() / 2,
							 bounds.y + bounds.height / 2 - this.getHeight() / 2);
		this.setResizable(false);
		this.setVisible(true);
		
	}
	
	
	public int getResultat()
	{
		return this.resultat;
	}
	
	
	public String getRepertoireGeneration()
	{
		return this.ES_repSite.getText();
	}
	
	public String getStyle()
	{
		return ((File)LD_styles.getSelectedItem()).getName();
	}
	
	private void init()
	{
		this.ES_repSite.setColumns(30);
		this.ES_repSite.setText(Application.getApplication().getConfigPropriete("repertoire_generation"));
		
		this.STC_repSite.setLabelFor(STC_repSite);
		this.STC_repSite.setText(Application.getApplication().getTraduction("rep_site"));
	
		this.STC_style.setText(Application.getApplication().getTraduction("style_pages"));
		this.BP_couleur_surlign.setBackground(new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_arbre"))));
		this.BP_diagram.setBackground(new Color(Integer.parseInt(Application.getApplication().getConfigPropriete("couleur_fond_diagrmme"))));
		this.LD_styles.setModel(new ComboStylesModel());
		this.LD_styles.setRenderer(new FileStyleRenderer());
		if(this.LD_styles.getItemCount() > 0)
		{
			boolean trouve = false;
			int index = 0;
			for ( index = 0 ; (index  < LD_styles.getItemCount()) && (trouve == false); index ++)
			{
				File temp = new File(((File)LD_styles.getItemAt(index)).getName());
				trouve = temp.getName().substring(0, temp.getName().length()-4).equals(Application.getApplication().getConfigPropriete("feuille_style"));
				//System.out.println("Trouvé = "+ trouvé + index + temp.getName().substring(0, temp.getName().length()-4) + " " + Application.getApplication().getConfigPropriete("style_pages"));
			}
			if ( (trouve) && (index != 0))
				this.LD_styles.setSelectedIndex(index-1);
			else
				this.LD_styles.setSelectedIndex(0);
		}		
		this.LD_langues.setModel(new ComboLangueModel());
		this.LD_langues.setRenderer(new FileRenderer());
		if(this.LD_langues.getItemCount() > 0)
		{
			boolean trouve = false;
			int index = 0;
			for ( index = 0 ; (index  < LD_langues.getItemCount()) && (trouve == false); index ++)
			{
				File temp = new File((String)LD_langues.getItemAt(index));
				//trouve = temp.getName().substring(0, temp.getName().length()-4).equals(Application.getApplication().getConfigPropriete("langueCourante"));
				trouve = temp.getName().equals(Application.getApplication().getConfigPropriete("langueCourante"));
				//System.out.println(temp.getName()+" "+Application.getApplication().getConfigPropriete("langueCourante"));
				//System.out.println("Trouvé = "+ trouvé + index + temp.getName().substring(0, temp.getName().length()-4) + " " + Application.getApplication().getConfigPropriete("style_pages"));
			}
			if ( (trouve) && (index != 0))
				this.LD_langues.setSelectedIndex(index-1);
			else
				this.LD_langues.setSelectedIndex(0);
		}		
		
			
		this.STC_couleur_surlign.setText(Application.getApplication().getTraduction("couleur_surlign"));
		
		this.BP_couleur_surlign.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				choisirCouleur(e);
			}
		});
		
		this.BP_diagram.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				choisirCouleurFond(e);
			}
		});
		
		this.STC_couleur_fond.setText(Application.getApplication().getTraduction("Choix_couleur_fond"));
		this.STC_Langues.setText(Application.getApplication().getTraduction("Choix_langue"));
		
		//this.BP_ok.setMnemonic();
		this.BP_ok.setText(Application.getApplication().getTraduction("OK"));
		this.BP_ok.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				ok(e);
			}
		});
		
//		this.BP_annuler.setMnemonic();
		this.BP_annuler.setText(Application.getApplication().getTraduction("Annuler"));
		this.BP_annuler.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				annuler(e);
			}
		});

//		this.BP_parcourir.setMnemonic();
		this.BP_parcourir.setText(Application.getApplication().getTraduction("parcourir"));
		this.BP_parcourir.addActionListener(new ActionListener()
		{
			public void actionPerformed(ActionEvent e)
			{
				parcourir(e);
			}
		});
	}
		
	private void initPanelRepSite()
	{
		this.panelRepSite.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("repertoire_cadre")));
		this.panelRepSite.add(STC_repSite, null);
		this.panelRepSite.add(ES_repSite, null);
		this.panelRepSite.add(BP_parcourir, null);
	}
	
	private void initPanelStyle()
	{
		//this.panelStyle.setLayout(this.gridBagLayoutStyle);
		this.panelStyle.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("style_cadre")));
		this.panelStyle.add(this.STC_style, null);
		this.panelStyle.add(this.LD_styles, null);
		this.panelStyle.add(this.STC_couleur_surlign, null);
		this.panelStyle.add(this.BP_couleur_surlign, null);

	}
	
	private void initPanelCouleurFond()
	{
		this.panelCouleurFond.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("couleur_fond_diagramme")));
		this.panelCouleurFond.add(this.STC_couleur_fond, null);
		this.panelCouleurFond.add(this.BP_diagram, null);

	}
	
	private void initPanelLangues()
	{
		this.panelLangues.setBorder(BorderFactory.createTitledBorder(Application.getApplication().getTraduction("Langues")));
		this.panelLangues.add(this.STC_Langues);
		this.panelLangues.add(this.LD_langues, null);

	}
	
	
	private void initPanelBoutons()
	{
		this.panelBoutons.add(this.BP_ok, null);
		this.panelBoutons.add(this.BP_annuler, null);
	}

	private void initPanelBackground()
	{
		this.panel1.setLayout(this.gridBagLayoutPanel1);
		this.constraintsPanel1.fill = GridBagConstraints.HORIZONTAL;
		this.constraintsPanel1.anchor = GridBagConstraints.CENTER;

		this.constraintsPanel1.insets = new Insets(10, 10, 0, 10);
		this.constraintsPanel1.weightx = 1.0;
		this.constraintsPanel1.gridwidth = GridBagConstraints.REMAINDER;
		this.gridBagLayoutPanel1.setConstraints(this.panelRepSite, this.constraintsPanel1);
		this.panel1.add(this.panelRepSite);

		this.gridBagLayoutPanel1.setConstraints(this.panelStyle, this.constraintsPanel1);
		this.panel1.add(this.panelStyle);

		this.gridBagLayoutPanel1.setConstraints(this.panelCouleurFond, this.constraintsPanel1);
		this.panel1.add(this.panelCouleurFond);
		
		this.gridBagLayoutPanel1.setConstraints(this.panelLangues, this.constraintsPanel1);
		this.panel1.add(this.panelLangues);
	}


	private void annuler(AWTEvent e)
	{
		this.resultat = CANCEL_OPTION;
		this.dispose();
	}

	private void ok(ActionEvent e)
	{
		this.dispose();
		Application.getApplication().setConfigPropriete("couleur_fond_diagrmme", "" + BP_diagram.getBackground().getRGB());
		
		Application.getApplication().setLangueCourante(LD_langues.getSelectedItem().toString());
		
		
		Application.getApplication().setConfigPropriete("couleur_arbre", "" + BP_couleur_surlign.getBackground().getRGB());
		
		if ( LD_styles.getSelectedItem().toString() != null)
		{
			File f1 = new File(LD_styles.getSelectedItem().toString());
			Application.getApplication().setConfigPropriete("feuille_style", f1.getName().substring(0, f1.getName().length()-4));
		}
		else
		{
			Application.getApplication().setConfigPropriete("feuille_style","");
		}
		String s = new String(ES_repSite.getText());
		Application.getApplication().setConfigPropriete("repertoire_generation", s);
		Application.getApplication().getFenetrePrincipale().rafraichirLangue();
		
		this.resultat = OK_OPTION;
		this.dispose();
	}
	
	private void parcourir(ActionEvent e)
	{
		JFileChooser fileChooser = new JFileChooser(this.ES_repSite.getText());
		fileChooser.setDialogTitle(Application.getApplication().getTraduction("titre_choix_rep"));
		fileChooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
		int res = fileChooser.showDialog(this, Application.getApplication().getTraduction("OK"));
		if(res == JFileChooser.APPROVE_OPTION)
		this.ES_repSite.setText(fileChooser.getSelectedFile().getAbsolutePath());
	}
	
	
	private void choisirCouleur(ActionEvent e)
	{
		// récupère la couleur choisie dans la bd
		Color couleur_choisie = JColorChooser.showDialog(this,Application.getApplication().getConfigPropriete("choix_couleur"), GenerationManager.getInstance().getCouleur());
		// si l'utilisateur choisit annuler, la bd renvoie null, donc on vérifie le retour
		if (couleur_choisie != null)
		{
			this.BP_couleur_surlign.setBackground(couleur_choisie);
		}
	}
	
	private void choisirCouleurFond(ActionEvent e)
	{
		this.BP_diagram.setBackground(JColorChooser.showDialog(this,Application.getApplication().getConfigPropriete("choix_couleur_fond"), Color.blue));
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
					//System.out.println(fich.getName());
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
	
	
	private class ComboLangueModel extends DefaultComboBoxModel
		{
			private ArrayList fichiersLangue = new ArrayList();
		
			public ComboLangueModel()
			{
				Vector listeLangues = Application.getApplication().getLangues();
				for(int i = 0 ; i < listeLangues.size() ; i++)
				{
					fichiersLangue.add(listeLangues.elementAt(i));
				}
			}
			
			public Object getElementAt(int index)
			{
				return fichiersLangue.get(index);
			} 
		
			public int getSize()
			{
				return fichiersLangue.size();
			}
		
		}
	
	private class FileRenderer implements ListCellRenderer
	{
		JLabel label = new JLabel();
		public FileRenderer() {
		label.setOpaque(true);
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{	if(value != null)
			{
				label.setText(value.toString());
			}
			else
				label.setText("");
			label.setBackground(isSelected ? Color.gray : Color.lightGray);
			label.setForeground(isSelected ? Color.white : Color.black);
	
		return label;
			
		}
	}
	
	private class FileStyleRenderer implements ListCellRenderer
	{
		JLabel label = new JLabel();
		public FileStyleRenderer() {
		label.setOpaque(true);
		}
		public Component getListCellRendererComponent(JList list, Object value, int index, boolean isSelected, boolean cellHasFocus)
		{	if(value != null)
			{
				label.setText(((File)value).getName().substring(0,((File)value).getName().length() - Application.getApplication().getConfigPropriete("extensionFeuilleStyle").length() - 1));
			}
			else
				label.setText("");
			label.setBackground(isSelected ? Color.gray : Color.lightGray);
			label.setForeground(isSelected ? Color.white : Color.black);
	
		return label;
			
		}
	}
}