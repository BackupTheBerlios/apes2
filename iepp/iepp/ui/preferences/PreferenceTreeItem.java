
package iepp.ui.preferences;

public class PreferenceTreeItem 
{
	public static final int COLOR_PANEL = 1;
	public static final int PATH_PANEL = 2;
	public static final int ERROR_PANEL = 3;
	public static final int DESC_PANEL = 4;
	public static final int LANGUAGE_PANEL = 5;
	
	
	private String mKey;
	private String mValue;
	private int mPanel;
	
	public PreferenceTreeItem(String key, String val,int panel)
	{
		this.mKey = key;
		this.mValue = val;
		this.mPanel = panel;
	}
	public String getKey()
	{
		return (this.mKey);
	}
	public int getPanel()
	{
		return (this.mPanel);
	}
	public String toString()
	{
		return (this.mValue);
	}
}
