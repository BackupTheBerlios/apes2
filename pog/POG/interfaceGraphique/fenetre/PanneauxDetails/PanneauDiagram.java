/*
 * Created on 24 mai 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package POG.interfaceGraphique.fenetre.PanneauxDetails;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Rectangle;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.HashMap;
import java.util.Iterator;

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
import org.ipsquad.apes.model.extension.SpemDiagram;
import org.ipsquad.apes.ui.ActivityJGraph;
import org.ipsquad.apes.ui.ContextJGraph;
import org.ipsquad.apes.ui.FlowJGraph;
import org.ipsquad.apes.ui.ResponsabilityJGraph;
import org.ipsquad.apes.ui.WorkDefinitionJGraph;
import org.jgraph.JGraph;

import POG.application.importExport.Apes;
import POG.interfaceGraphique.action.ControleurPanneaux;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.PresentationElementModele;

/**
 * @author c82aber
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class PanneauDiagram extends PanneauDetail {
	private JGraph _graph;

	/**
	 * @param control
	 */
	public PanneauDiagram(ControleurPanneaux control, ElementPresentation idelem) {
		super(control);
		this.removeAll();
		this.setLayout(new BorderLayout());
		JScrollPane js = new JScrollPane();
		SpemGraphAdapter mAdapter = Apes.getDiagramme((SpemDiagram) ((PresentationElementModele)idelem).getLnkModelElement());
		
		if(mAdapter instanceof ContextGraphAdapter) _graph = new ContextJGraph(mAdapter);
		else if(mAdapter instanceof ResponsabilityGraphAdapter) _graph = new ResponsabilityJGraph(mAdapter);
		else if(mAdapter instanceof ActivityGraphAdapter) _graph = new ActivityJGraph(mAdapter);
		else if(mAdapter instanceof FlowGraphAdapter) _graph = new FlowJGraph(mAdapter);
		else if(mAdapter instanceof WorkDefinitionGraphAdapter) _graph = new WorkDefinitionJGraph(mAdapter);

		Object o[] = _graph.getRoots();

		final HashMap rectid = new HashMap();

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
				
				Rectangle rr = new Rectangle((int)_graph.getCellBounds(o[i]).getX(), (int)_graph.getCellBounds(o[i]).getY(), (int)_graph.getCellBounds(o[i]).getWidth(), (int)_graph.getCellBounds(o[i]).getHeight());
				int ID_Apes = ((ApesGraphCell)o[i]).getID();
				rectid.put(rr, new Integer(ID_Apes));
			}
		}
		
		_graph.addMouseListener(new MouseAdapter() {
			public void mouseClicked(MouseEvent arg0) {
				Iterator itk = rectid.keySet().iterator();
				int idapes = -1;
				while (itk.hasNext() && (idapes == -1)) {
					Rectangle rr = (Rectangle)itk.next();
					if(rr.contains(arg0.getX(), arg0.getY())) 
						idapes = ((Integer)rectid.get(rr)).intValue();
				}
				if (idapes != -1)
					lnkControleurPanneaux.getLnkSysteme().lnkFenetrePrincipale.getLnkArbrePresentation().selectNodeContaining(Apes.getElementByID(idapes));
			}
		});

		js.getViewport().add(_graph);
		this.add(js, BorderLayout.CENTER);
		this.setVisible(true);
	}

	public void afficherMenuGuides(Component compo, int x, int y) {
	}

	public void loadElement(ElementPresentation idelem) {
	
	}

}
