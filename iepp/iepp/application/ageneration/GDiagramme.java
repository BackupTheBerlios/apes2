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
 
package iepp.application.ageneration;
import iepp.domaine.ComposantProcessus;
import iepp.domaine.ElementPresentation;
import iepp.domaine.IdObjetModele;

import java.io.*;
import java.util.Vector;

import javax.swing.JFrame;
import javax.swing.JScrollPane;

import org.ipsquad.apes.adapters.ActivityCell;
import org.ipsquad.apes.adapters.ActivityGraphAdapter;
import org.ipsquad.apes.adapters.ApesGraphCell;
import org.ipsquad.apes.adapters.ContextGraphAdapter;
import org.ipsquad.apes.adapters.FlowGraphAdapter;
import org.ipsquad.apes.adapters.ProcessComponentCell;
import org.ipsquad.apes.adapters.ProcessRoleCell;
import org.ipsquad.apes.adapters.ResponsabilityGraphAdapter;
import org.ipsquad.apes.adapters.SpemGraphAdapter;
import org.ipsquad.apes.adapters.WorkDefinitionCell;
import org.ipsquad.apes.adapters.WorkDefinitionGraphAdapter;
import org.ipsquad.apes.adapters.WorkProductCell;
import org.ipsquad.apes.adapters.WorkProductStateCell;
import org.ipsquad.apes.ui.ActivityJGraph;
import org.ipsquad.apes.ui.ContextJGraph;
import org.ipsquad.apes.ui.FlowJGraph;
import org.ipsquad.apes.ui.ResponsabilityJGraph;
import org.ipsquad.apes.ui.WorkDefinitionJGraph;
import org.jgraph.JGraph;

import com.sun.image.codec.jpeg.ImageFormatException;

import util.ImageUtil;

/**
 * Classe permettant de cr�er une page dont le contenu correspond � un diagramme
 */
public class GDiagramme extends GElementModele
{

	private ComposantProcessus cp ;
	
	/**
	 * Constructeur du gestionnaire de g�n�ration
	 * @param elem element de pr�sentation associ� au diagramme courant
	 * @param pwFicTree lien vers le fichier tree.js construit durant la g�n�ration du site
	 */
	public GDiagramme(ComposantProcessus cp, ElementPresentation elem, PrintWriter pwFicTree)
	{
		super(elem, pwFicTree);
		this.cp = cp;
	}


	/**
	 * M�thode permettant de traiter les �l�ments de pr�sentation li�s au diagramme
	 */
	public void traiterGeneration(long id) throws IOException
	{
		super.traiterGeneration(id);
		if (this.element.getID_Apes() != -1)
		{
			this.creerFichierImages();
		}
	}
	
	/**
	 * 
	 */
	private void creerFichierImages() throws ImageFormatException, IOException
	{
		FileOutputStream fout = new FileOutputStream(this.cheminParent + File.separator  + "diagramme.png");
		ImageUtil.encoderGrapheImage((SpemGraphAdapter)this.modele.getAdapter(), fout, "png");
	}


	/**
	 * M�thode permettant de cr�er le contenu de la page associ�e au diagramme courant
	 * Affichage du diagramme
	 */
	public void creerFichierDescription() throws IOException
	{
		// cr�ation du fichier de contenu
		File ficHTML = new File (this.cheminAbsolu) ;
		FileWriter fd = new FileWriter(ficHTML);

		fd.write("<HTML><head> <link rel='STYLESHEET' type='text/css' href='" + this.getCheminStyle() + "'>"
					+ "</head>" + "<body><center>\n"
					+ "<table width=\"84%\" align=\"center\">\n"
					+ "<tr><td width=\"100%\" class=\"titrePage\">\n"
					+ "<p align=\"center\" class=\"entete\">\n"
					+ "<b>" + this.element.getNomPresentation() + "</b>\n"
					+ "</p></td></tr></table></center><BR>\n");

		fd.write(getBarreNavigation() + "<br>");
		
		if (GenerationManager.getInstance().estContenuAvant())
		{
		    this.ajouterContenu(fd);
		}
		fd.write("<div align=\"center\" class=\"imgdiagramme\">" + this.getMapDiagramme() + "</div>");
		
		if (!GenerationManager.getInstance().estContenuAvant())
		{
		    this.ajouterContenu(fd);
		}
		
		this.ajouterDescription(fd);
		this.ajouterMail(fd);
		this.ajouterVersionDate(fd);
		fd.write("</BODY></HTML>") ;
		fd.close();

	}
	
	/**
	 * Renvoie sous la forme d'une chaine de caract�re le code associ� � la map du diagramme
	 */
	public String getMapDiagramme()
	{
		SpemGraphAdapter mAdapter = cp.getAdapter(this.modele.getNumRang());
		String mapcode = ("<MAP NAME=\""+ CodeHTML.normalizeName(mAdapter.getName())+"\">\n");

		JGraph mGraph=null;
		if(mAdapter instanceof ContextGraphAdapter) mGraph=new ContextJGraph(mAdapter);
		else if(mAdapter instanceof ResponsabilityGraphAdapter) mGraph=new ResponsabilityJGraph(mAdapter);
		else if(mAdapter instanceof ActivityGraphAdapter) mGraph=new ActivityJGraph(mAdapter);
		else if(mAdapter instanceof FlowGraphAdapter) mGraph=new FlowJGraph(mAdapter);
		else if(mAdapter instanceof WorkDefinitionGraphAdapter) mGraph=new WorkDefinitionJGraph(mAdapter);

		JFrame frame = new JFrame();
		frame.getContentPane().add(new JScrollPane(mGraph));
		frame.pack();
		frame.setVisible(false);

		Vector tmp=new Vector();
		Object o[]=mGraph.getRoots();

		int x1,x2,y1,y2;
		for(int i=0;i<o.length;i++)
		{
			if( o[i] instanceof ActivityCell 
					|| o[i] instanceof WorkProductCell 
					|| o[i] instanceof ProcessRoleCell 
					|| o[i] instanceof WorkProductStateCell
					|| o[i] instanceof ProcessComponentCell 
					|| o[i] instanceof WorkDefinitionCell )
			{
				x1=(int)mGraph.getCellBounds(o[i]).getX();
				x2=x1+(int)mGraph.getCellBounds(o[i]).getWidth();
				y1=(int)mGraph.getCellBounds(o[i]).getY();
				y2=y1+(int)mGraph.getCellBounds(o[i]).getHeight();
				
				// r�cup�rer l'ID de l'�l�ment courant
				int ID_Apes;
				ElementPresentation elem;
				
				IdObjetModele nouvelId = GenerationManager.estProduitChange( this.cp.toString()+ "::" + o[i].toString());
			    if (nouvelId != null)
			    {
			        ID_Apes = nouvelId.getID();
			        elem = ((ComposantProcessus)nouvelId.getRef()).getElementPresentation(ID_Apes);
			    }
				else
				{
				    ID_Apes = ((ApesGraphCell)o[i]).getID();
				    elem = this.cp.getElementPresentation(ID_Apes);
				}
			    
				if ( elem != null )
				{
					IdObjetModele id = elem.getElementModele();
					if (id != null)
					{
						// rajout�: info-bulle contenant la description de l'�l�ment
						String description = "ALT=\"\"";
						if (GenerationManager.getInstance().estInfoBulle())
						{
							if (elem.getDescription() != null)
							{
								description = "ALT=\"" + elem.getDescription() + "\"";
							}
						}
						mapcode += ("<AREA Shape=\"Polygon\" coords = \""+x1 +","+y1+","+x2+","+y1+","+x2+","+y2+","+x1+","+y2+"\" HREF=\""+ this.getLienChemin(id)+ "\" " + description + ">\n");
					}
				}
				else
				{
					// S'il s'agit d'un produit exterieur (sans element de presentation)
					if (o[i] instanceof WorkProductCell)
					{
						// S'occuper du paquetage special
					}
				}
			}
		}

		mapcode += ("</MAP>\n");
		mapcode += ("<IMG SRC=\"diagramme.png\" USEMAP=\"#"+ CodeHTML.normalizeName(mAdapter.getName())+"\">\n");

		return mapcode;
	}
	
	/**
	 * 
	 */
	public void recenser() 
	{
		Integer oldValue = (Integer)ArbreGeneration.mapCompteur.get("nbDiagrammes");
		ArbreGeneration.mapCompteur.put("nbDiagrammes", new Integer(oldValue.intValue() + 1));
		
		oldValue = (Integer)ArbreGeneration.mapCompteur.get("nbPagesTotal");
		ArbreGeneration.mapCompteur.put("nbPagesTotal", new Integer(oldValue.intValue() + 1));
	}
}
