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

package iepp.application;

import iepp.ui.igeneration.*;
import iepp.application.ageneration.*;
import util.TaskMonitorDialog;
import iepp.Application;

/**
 * 
 */

public class CGenererSite extends CommandeNonAnnulable
{
	private TaskMonitorDialog dialogAvancee = null;
	private TacheGeneration tacheGener;
	public static final String IMAGE_PATH="images";
	public static final String CONTENU_PATH="contenu";
	public static final String APPLET_PATH="applet";
	public static final String DESCRIPTION_PATH="description";
	public static final String STYLES_PATH="styles";
	public static final String EXTERIEUR_PATH="exterieur";
	
	public boolean executer()
	{	
		// ce code devra être associé à la boîte de dialogue
		DialogueGenererSite dialGener = new DialogueGenererSite(Application.getApplication().getFenetrePrincipale());
				
		if (dialGener.getResultat() != DialogueGenererSite.OK_OPTION)
		{
			return false;
		}

		this.tacheGener = new TacheGeneration();
		this.dialogAvancee = new TaskMonitorDialog(Application.getApplication().getFenetrePrincipale(), this.tacheGener);
		this.dialogAvancee.setTitle(Application.getApplication().getTraduction("generation_en_cours"));
		
		this.tacheGener.setTask(dialogAvancee);
		this.dialogAvancee.show();
		
		return tacheGener.isGenerationReussie();
	}
}

