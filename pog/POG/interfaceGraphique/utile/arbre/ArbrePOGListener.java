/*
 * Created on 25 nov. 2004
 *
 * To change the template for this generated file go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
package POG.interfaceGraphique.utile.arbre;

import java.util.Vector;

import javax.swing.JTree;
import javax.swing.event.TreeModelEvent;
import javax.swing.event.TreeModelListener;
import javax.swing.tree.TreeModel;
import javax.swing.tree.TreePath;

import POG.interfaceGraphique.action.POGListener;
import POG.interfaceGraphique.action.Systeme;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Presentation;

/**
 * @author c82aber
 *
 * To change the template for this generated type comment go to
 * Window - Preferences - Java - Code Generation - Code and Comments
 */
public abstract class ArbrePOGListener implements POGListener, TreeModel {

	private Systeme _systeme;
	private Vector _listeners;

	public ArbrePOGListener(Systeme pres) {
		_systeme = pres;
		_listeners = new Vector();
	}

	public void ajouter(ElementPresentation el) {
		TreeModelEvent ev = makeEvent(el);
		for (int i = 0; i < _listeners.size(); i++)
			((TreeModelListener)_listeners.get(i)).treeNodesInserted(ev);
		afficher(el);
	}

	public void supprimer(ElementPresentation el) {
		TreeModelEvent ev = makeEvent(el);
		for (int i = 0; i < _listeners.size(); i++)
			((TreeModelListener)_listeners.get(i)).treeNodesRemoved(ev);
		afficher(thePresentation().getElementParent(el.get_id()));
	}
	
	public void modifier(ElementPresentation el) {
		TreeModelEvent ev = makeEvent(el);
		for (int i = 0; i < _listeners.size(); i++)
			((TreeModelListener)_listeners.get(i)).treeStructureChanged(ev);
		afficher(el);
	}

	public void initialise(ElementPresentation el) {
		TreePath selpt = theArbre().getSelectionPath();
		el = thePresentation().getElementPresentation(thePresentation().getIdRacine());
		TreeModelEvent ev = new TreeModelEvent(el, thePath(el, thePresentation()));
		for (int i = 0; i < _listeners.size(); i++)
			((TreeModelListener)_listeners.get(i)).treeStructureChanged(ev);
		afficher(selpt);
	}
	
	private JTree theArbre() {
		return _systeme.lnkFenetrePrincipale.getLnkArbrePresentation().get_arbre();
	}

	private TreeModelEvent makeEvent(ElementPresentation el) {
		ElementPresentation pere = thePresentation().getElementParent(el.get_id());
		String[] toto = el.get_id().split("-");
		int idel = Integer.parseInt(toto[toto.length - 1]) - 1;
		return new TreeModelEvent(el, thePath(pere, thePresentation()), new int [] {idel}, new Object [] {el});
	}

	private void afficher(ElementPresentation el) {
		TreePath tp = new TreePath(thePath(el, thePresentation()));
		afficher(tp);
	}
	
	private void afficher(TreePath tp) {
		JTree _arbre = theArbre();
		_arbre.expandPath(tp);
		_arbre.makeVisible(tp);
		_arbre.clearSelection();
		_arbre.setSelectionPath(tp);
	}

	private Presentation thePresentation() {
		return _systeme.getlnkControleurPresentation().getlnkPresentation();
	}

	public Object getRoot() {
		if (thePresentation() == null)
			return null;
		return thePresentation().getElementPresentation(thePresentation().getIdRacine());
	}

	public int getChildCount(Object arg0) {
		if (!(arg0 instanceof ElementPresentation))
			return 0;
		int total = 0;
		ElementPresentation el = (ElementPresentation) arg0;
		ElementPresentation[] alls = thePresentation().listeElementPresentation();
		for (int i = 0; i < alls.length; i++)
			if (alls[i].get_id().startsWith(el.get_id()) && (alls[i].get_id().split("-").length == el.get_id().split("-").length + 1))
				total++;
		return total;
	}

	public static Object [] thePath(Object o, Presentation thePresentation) {
		if (!(o instanceof ElementPresentation))
			return null;
		ElementPresentation el = (ElementPresentation) o;
		Vector vect = new Vector();
		while (el != null) {
			vect.insertElementAt(el, 0);
			el = thePresentation.getElementParent(el.get_id());
		}
		return vect.toArray();
	}

	/*private ElementPresentation [] thechilds(ElementPresentation el) {
		ElementPresentation[] alls = _presentation.listeElementPresentation();
		Vector vect = new Vector();
		for (int i = 0; i < alls.length; i++)
			if (alls[i].get_id().startsWith(el.get_id()))
				vect.add(alls);
		ElementPresentation[] ret = (ElementPresentation[]) vect.toArray(new ElementPresentation[0]);
		Arrays.sort(ret, new Comparator() {
			public int compare(Object arg0, Object arg1) {
				return ((ElementPresentation)arg0).get_id().compareTo(((ElementPresentation)arg1).get_id());
			}
		});
		return ret;
	}*/

	public boolean isLeaf(Object arg0) {
		return getChildCount(arg0) == 0;
	}

	public void addTreeModelListener(TreeModelListener arg0) {
		_listeners.add(arg0);
	}

	public void removeTreeModelListener(TreeModelListener arg0) {
		_listeners.remove(arg0);
	}

	public Object getChild(Object arg0, int arg1) {
		if (!(arg0 instanceof ElementPresentation))
			return null;		
		return thePresentation().getElementPresentation(((ElementPresentation)arg0).get_id() + "-" + (arg1 + 1));
	}

	public int getIndexOfChild(Object arg0, Object arg1) {
		if ((arg0 instanceof ElementPresentation) && (arg1 instanceof ElementPresentation)) {
			ElementPresentation elpere = (ElementPresentation) arg0;
			ElementPresentation elfils = (ElementPresentation) arg1;
			String [] idd = elfils.get_id().substring(elpere.get_id().length() + 1).split("-");
			return Integer.parseInt(idd[0]) - 1;
		}
		return 0;
	}

	abstract public void valueForPathChanged(TreePath path, Object arg1);
	
}
