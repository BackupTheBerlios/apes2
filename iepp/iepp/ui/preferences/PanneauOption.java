package iepp.ui.preferences;


import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;


public abstract class PanneauOption extends JPanel 
{
	protected JPanel mPanel ;
	protected JLabel mTitleLabel ;

	protected void makeButton(String name, GridBagLayout gridbag,
							  GridBagConstraints c) 
	{
		JButton button = new JButton(name);
		gridbag.setConstraints(button, c);
		mPanel.add(button);
	}
	protected void makeLabel(String name,
							  GridBagLayout gridbag,
							  GridBagConstraints c) 
	{
		JLabel label = new JLabel(name);
		gridbag.setConstraints(label, c);
		mPanel.add(label);
	}
	public String getName ()				
	{
			return this.mTitleLabel.getText() ;
	}
	public void setName (String name)				
	{
			this.mTitleLabel.setText(name) ;
	}
}
