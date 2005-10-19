/*
 * Created on 11 oct. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package POG.interfaceGraphique.fenetre;

import java.awt.Dimension;
import java.awt.HeadlessException;

import javax.swing.DefaultListModel;
import javax.swing.JList;
import javax.swing.JScrollPane;
import javax.swing.ListSelectionModel;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import org.ipsquad.apes.model.extension.SpemDiagram;

import POG.objetMetier.ElementPresentation;
import POG.objetMetier.PresentationElementModele;
import POG.utile.PogToolkit;

/**
 * @author c82aber
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public class FenetreElementLien extends FenetrePOG {

	public FenetreElementLien(FenetrePrincipale fp) throws HeadlessException {
		super(fp);
		jbinit();
		setModal(false);
		PogToolkit.centerWindow(this);
	}

	private JList lstElem;
	private DefaultListModel model;

	private void jbinit() {
		model = new DefaultListModel();
		lstElem = new JList(model);
		lstElem.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
		JScrollPane scroll = new JScrollPane();
		scroll.getViewport().add(lstElem);
		lstElem.addListSelectionListener(new ListSelectionListener() {
			public void valueChanged(ListSelectionEvent arg0) {
				ElementPresentation ep = (ElementPresentation) ((JList)arg0.getSource()).getSelectedValue();
				if (ep instanceof PresentationElementModele)
					lnkFenetrePrincipale.getLnkArbrePresentation().selectNodeContaining(((PresentationElementModele)ep).getLnkModelElement());
				else
					lnkFenetrePrincipale.getLnkArbrePresentation().selectNodeContaining(ep);
			}
		});
		this.getContentPane().add(scroll);
		this.setSize(new Dimension(450, 400));
		this.setTitle(lnkFenetrePrincipale.getLnkLangues().valeurDe("lienelement"));
	}

	void actionOK() {
		super.setVisible(false);
	}

	void actionAnnuler() {
		super.setVisible(false);
	}
	
	public void setVisible(boolean arg0) {
		Object [] elems = lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().listeElementPresentation();
		lstElem.removeAll();
		model.clear();
		for (int i = 0; i < elems.length; i++) {
			ElementPresentation ep = (ElementPresentation) elems[i];
			if (ep instanceof PresentationElementModele)
				if (((PresentationElementModele)ep).getLnkModelElement() instanceof SpemDiagram)
					continue;
			if (ep.getContenu() == null) {
				model.addElement(ep);
			}
		}
		super.setVisible(arg0);
	}

	

}
