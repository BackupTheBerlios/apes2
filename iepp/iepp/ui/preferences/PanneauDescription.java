package iepp.ui.preferences;



import iepp.Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class PanneauDescription extends PanneauOption
{
	private JLabel mDescriptionLabel ;
	
	public static final String GENERAL_KEY = "GeneralTitle" ;
	public static final String GENERATION_KEY = "GenerationTitle" ;
	public static final String REPOSITORY_KEY = "RepositoryTitle" ;
	public static final String DP_KEY = "Propriete_DPTitle" ;
	
	public PanneauDescription(String name)
	{
		this.mTitleLabel = new JLabel (name) ;
		this.setLayout(new BorderLayout());
		mPanel = new JPanel() ;
		GridBagLayout gridbag = new GridBagLayout();
		mPanel.setLayout(gridbag);
		GridBagConstraints c = new GridBagConstraints();

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

		c.weighty = 0;
		c.weightx = 0 ;
		c.fill = GridBagConstraints.BOTH;
		c.gridwidth = GridBagConstraints.REMAINDER;;//next-to-last in row

		
		this.mDescriptionLabel = new JLabel();
		this.setDescription(name);
		gridbag.setConstraints(this.mDescriptionLabel, c);
		mPanel.add(this.mDescriptionLabel);

		//		linefeed 
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
		this.setDescription (key);
		return this ;
	}
	
	public void setDescription(String key)
	{
		if (key.equals(GENERAL_KEY))
		{
			this.mDescriptionLabel.setText(Application.getApplication().getTraduction("GeneralTitleDesc"));
		}
		else if (key.equals(GENERATION_KEY))
		{
			this.mDescriptionLabel.setText(Application.getApplication().getTraduction("GenerationTitleDesc"));
		}
		else if (key.equals(REPOSITORY_KEY))
		{
			this.mDescriptionLabel.setText(Application.getApplication().getTraduction("RepositoryTitleDesc"));
		}
		else if (key.equals(DP_KEY))
		{
			this.mDescriptionLabel.setText(Application.getApplication().getTraduction("DPTitleDesc"));
		}
	}

}
