/*
 * Created on 30 sept. 2004
 *
 * TODO To change the template for this generated file go to
 * Window - Preferences - Java - Code Style - Code Templates
 */
package iepp.ui.preferences;


import iepp.Application;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.util.Vector;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.border.TitledBorder;


public class PanneauLangue extends PanneauOption 
{

	private JList mList;
	private String mOldLanguage;
	public static final String LANGUAGE_PANEL_KEY = "LanguageTitle" ;
	
	
	public PanneauLangue(String name)
	{
		mOldLanguage = Application.getApplication().getConfigPropriete("langueCourante");
		boolean trouve = false;
		
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
		makeLabel(" ", gridbag, c);

		c.weightx = 0 ;
		makeLabel(Application.getApplication().getTraduction("Choix_langue"), gridbag, c);
		makeLabel(" ", gridbag, c);

		// liste langues
		Vector listeLangues = Application.getApplication().getLangues();
		mList = new JList(listeLangues);
		gridbag.setConstraints(mList, c);
		mPanel.add(mList);
		
		c.fill = GridBagConstraints.VERTICAL;
		c.weighty = 2.0;     		
		makeLabel(" ", gridbag, c);
		
		this.add(new JLabel("    "),BorderLayout.WEST);
		this.add(mPanel,BorderLayout.CENTER);
		for (int i = 0; i < mList.getModel().getSize() && !trouve;i++)
		{
			if (mOldLanguage.equals(((String)mList.getModel().getElementAt(i))))
			{
				mList.setSelectedIndex(i);
				trouve = true;
			}
		}
		if (!trouve && mList.getModel().getSize()> 0) mList.setSelectedIndex(0);
	}
	
	
	public PanneauOption openPanel(String key)
	{
		this.setName(Application.getApplication().getTraduction(key)) ;
	
		return this ;
	}

	
	public void save ()
	{
		Application.getApplication().setLangueCourante(mList.getSelectedValue().toString());
	}
	
	public boolean hasLanguageChanged()
	{
		boolean result = !mOldLanguage.equals(Application.getApplication().getConfigPropriete("langueCourante"));
		mOldLanguage = Application.getApplication().getConfigPropriete("langueCourante");
		return result;
	}

}