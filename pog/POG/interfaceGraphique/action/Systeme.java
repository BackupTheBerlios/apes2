/*
 * POG
 * Copyright (C) 2004 Team POG
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
 */


/* Generated by Together */
package POG.interfaceGraphique.action;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStreamWriter;
import java.net.URL;
import java.net.URLDecoder;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Stack;
import java.util.Vector;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;

import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.Activity;
import org.ipsquad.apes.model.spem.process.structure.ProcessRole;
import org.ipsquad.apes.model.spem.process.structure.WorkDefinition;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

import POG.application.controleurMetier.ControleurExporter;
import POG.application.controleurMetier.ControleurGuide;
import POG.application.controleurMetier.ControleurOrganiser;
import POG.application.controleurMetier.ControleurPresentation;
import POG.application.controleurMetier.ControleurPresenter;
import POG.application.importExport.Apes;
import POG.application.importExport.Importer;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.interfaceGraphique.fenetre.Patientez;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;
import POG.objetMetier.PresentationElementModele;
import POG.utile.PogToolkit;
import POG.utile.propriete.Preferences;

public class Systeme {
  /**
   * @directed
   * @supplierCardinality 1
   */
  private Preferences lnkPreferences;

  /**
   * Represente le nombre d'elements de presentations quin'ont pas changes
   * de nom. Ils ont celui par defaut : "ElementPresentation_<nbre>" avec
   * nbre le nombre de dessous.
   * TOUT NOUVEL ELEMENT PREND UN NOM DE CE TYPE
   * Creation d'un element avec ce nom => ++
   * Changement nom ne pas oublier => -- ( pas utile et chiant a g\uFFFDrer)
   */
  private int _numeroNouvelElement = 1;



  /**
   * le --.
   */
  public void moinsNbreNouveauxElements() {
    _numeroNouvelElement--;
  }

  /**
   * le ++.
   */
  public void plusNbreNouveauxElements() {
    _numeroNouvelElement++;
  }

  /**
   * @directed
   * @supplierCardinality 1
   */
  private ControleurPresenter lnkControleurPresenter;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private ControleurOrganiser lnkControleurOrganiser;

  /**
   * @supplierCardinality 1
   * @directed
   */
  private ControleurPresentation lnkControleurPresentation;

  /**
   * @directed
   * @supplierCardinality 1
   */
  private ControleurExporter lnkControleurExporter;

  /**
   * @supplierCardinality 1
   * @directed
   */
  public FenetrePrincipale lnkFenetrePrincipale;

  /**
   * @directed
   * @supplierCardinality 1
   */
  private ControleurGuide lnkControleurGuide;

  private Patientez lnkPatientez;

  // On va mettre ici l'ensemble des variables (TAG) permettant de gérer la sauvegarde, cohérences, ...
  private boolean _save = true;
  private boolean _coherent = true;
  private long _modele;

  public Systeme(Preferences pref, FenetrePrincipale fenetrePrin) {
    lnkPreferences = pref;
    lnkControleurPresentation = new ControleurPresentation(lnkPreferences);
    lnkControleurGuide = new ControleurGuide(lnkPreferences.get_guide());
    lnkControleurExporter = new ControleurExporter(lnkControleurPresentation, this);
    lnkControleurOrganiser = new ControleurOrganiser(lnkControleurPresentation);
    lnkControleurPresenter = new ControleurPresenter(lnkControleurPresentation);
    lnkFenetrePrincipale = fenetrePrin;
    //lnkPatientez = new Patientez(lnkFenetrePrincipale);
  }

	private void _invaliderTAG() {
		if (_save)
			lnkFenetrePrincipale.getLnkMainToolBar()._btnSave.setEnabled(true);
//		if (_coherent)
//			lnkFenetrePrincipale.getLnkMainToolBar()._btnCheckSPEMs.setEnabled(true);
		_save = false;
		_coherent = false;
	}

	private void _verifierSauve(boolean flagenr) {
		if (!_save && flagenr) {
			int rep = PogToolkit.askYesNoQuestion(lnkFenetrePrincipale.getLnkLangues().valeurDe("questsauve"), false, lnkFenetrePrincipale);
			if (rep == PogToolkit._YES)
				enregistrerPresentation();
			_save = true;
			_coherent = false;
		}
		lnkFenetrePrincipale.getLnkMainToolBar()._btnSave.setEnabled(!_save);
	}

	private void _verifcohe() {
		if (!_coherent) {
			_coherent = (_modele == lnkControleurPresentation.get_pathModele().lastModified());
			if (!_coherent) {
				lnkFenetrePrincipale.getLnkDebug().verificationMessage("messverifsync");
				return;
			}
			_coherent = lnkControleurExporter.verifierPresentation(lnkFenetrePrincipale.getLnkDebug());
			lnkFenetrePrincipale.getLnkDebug().patienter("", 0, 0);
//			lnkFenetrePrincipale.getLnkMainToolBar()._btnCheckSPEMs.setEnabled(!_coherent);
		}
	}


  public void nouvellePresentationSansModele(File pathBibli, String nomPres) {
    _verifierSauve(true);

    lnkFenetrePrincipale.jLabelPathBib.setText(pathBibli.getAbsolutePath());

    this._numeroNouvelElement = 1;
    lnkFenetrePrincipale.set_pathSave("");
    lnkControleurPresentation.nouvellePresentation(pathBibli, nomPres); //, ElementPresentation.POG_RACINE_PAQUETAGE);
    lnkFenetrePrincipale.getLnkArbrePresentation().load();
    lnkFenetrePrincipale.getLnkArbreExplorateur().load();
    lnkFenetrePrincipale.getLnkPanneauBibliotheque().load();
    lnkFenetrePrincipale.getLnkControleurPanneaux().loadVide();
    System.setProperty("user.dir", pathBibli.getAbsolutePath());
	_invaliderTAG();
  }

  public void ouvrirPresentation(final String pathOuv) {
	_verifierSauve(true);

        new FenetrePrincipale.TheTraitement("ouverture") {
		public void traitement() {
    		new Importer(new File(pathOuv), lnkControleurPresenter, lnkFenetrePrincipale.getLnkDebug());
			lnkFenetrePrincipale.set_pathSave(pathOuv);
    		lnkFenetrePrincipale.getLnkArbreExplorateur().load();
    		lnkFenetrePrincipale.getLnkArbrePresentation().load();
            lnkFenetrePrincipale.jLabelPathBib.setText(lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().lnkBibliotheque.getAbsolutePath());
		}
	};
  }

  public void enregistrerPresentation() {
    if (!lnkFenetrePrincipale.get_pathSave().equalsIgnoreCase("")){
      try {
        File pathsave = new File(lnkFenetrePrincipale.get_pathSave());
        lnkControleurPresentation.getlnkPresentation().sauver(new OutputStreamWriter(new FileOutputStream(pathsave), "UTF-8"), false);
        _save = true;
      }
      catch (Exception e) {
        e.printStackTrace();
        _save = false;
      }

      _verifierSauve(false);

      if (_save)
        PogToolkit.showMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("successauvegarde"), lnkFenetrePrincipale);
      else
        PogToolkit.showMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("erreursauvegarde"), lnkFenetrePrincipale);
    }
  }

  public void verifiePresentation() {
	new FenetrePrincipale.TheTraitement("Exportation") {
	   public void traitement() {
			_verifcohe();
	    	if (_coherent)
				lnkFenetrePrincipale.getLnkDebug().verificationMessage("messverifcoh");
	    	else
				lnkFenetrePrincipale.getLnkDebug().verificationMessage("messverifncoh");
	   }
	};
  }

  public void exporterPresentation(final String cheminDest) {
	new FenetrePrincipale.TheTraitement("Exportation") {
	   public void traitement() {
			File file = new File(cheminDest);
			_verifcohe();
			boolean ok = _coherent;
			if (_coherent)
				ok = lnkControleurExporter.exporterPresentation(file, lnkFenetrePrincipale.getLnkDebug());
			if (ok)
			 PogToolkit.showMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("SuccesExportation"), lnkFenetrePrincipale);
		   else
			 PogToolkit.showErrorMsg(lnkFenetrePrincipale.getLnkLangues().valeurDe("ErreurExportation"), lnkFenetrePrincipale);
	   }
   };
  }

  public void ajouterGuide(ElementPresentation elt, String typeGuide) {
	_invaliderTAG();
    String nomDefaut = "no_name_" + _numeroNouvelElement;
    ImageIcon icon = this.lnkPreferences.getIconeDefaut("defaut_icon");

    if (typeGuide == "Concept"){
      icon = this.lnkPreferences.getIconeDefaut("GuideConcept");
    }
    else if (typeGuide == "Article"){
      icon = this.lnkPreferences.getIconeDefaut("GuideArticle");
    }
    else if (typeGuide == "Technique"){
      icon = this.lnkPreferences.getIconeDefaut("GuideTechnique");
    }
    else if (typeGuide == "Guide de redaction"){
      icon = this.lnkPreferences.getIconeDefaut("GuideRedaction");
    }
    else if (typeGuide == "Liste de controles"){
      icon = this.lnkPreferences.getIconeDefaut("GuideListeControle");
    }
    else if (typeGuide == "Plan Type"){
      icon = this.lnkPreferences.getIconeDefaut("GuidePlanType");
    }
    else if (typeGuide == "Exemple"){
      icon = this.lnkPreferences.getIconeDefaut("GuideExemple");
    }
    else if (typeGuide == "Guide Outil"){
      icon = this.lnkPreferences.getIconeDefaut("GuideOutil");
    }

    _numeroNouvelElement++;
    Guide g = lnkControleurGuide.ajouterGuide(elt,
                                              this.lnkControleurPresentation.getlnkPresentation().makeId(elt.get_id()),
                                              icon, typeGuide);
    g.set_nomPresentation(nomDefaut);
    this.lnkControleurPresentation.getlnkPresentation().
        ajouterElementPresentation(g);
    g.set_description(typeGuide);
    refreshAll(g);
    lnkFenetrePrincipale.getLnkControleurPanneaux().loadCentre("DGuide", g);
  }

  /**
       * Ajoute un contenu (suppression de l'ancien si existant) et appel de load de
   * cette element pour mise \uFFFD jour de l'arbre.
   * @param elemPres elem
   * @param file file
   */
  public void associerContenu(ElementPresentation elemPres, File file) {
    _invaliderTAG();
    this.lnkControleurOrganiser.associerContenu(elemPres, file);
    refreshAll(elemPres);
  }

  public void choisirContenu(ElementPresentation elem){
    File fichier = PogToolkit.chooseFileInBibli(FenetrePrincipale.INSTANCE);
    if (fichier == null) {
      return;
    }
    if (!fichier.exists()) {
      PogToolkit.showErrorMsg(
          lnkFenetrePrincipale.getLnkLangues().valeurDe("fichierInexistant"),
          lnkFenetrePrincipale);
    }
    else {
      associerContenu(elem, fichier);
    }

  }

  public void extraireIcone() {
    if (lnkControleurPresentation.getlnkPresentation() == null)
      return;
    try {
      _extractFromRessources("org/ipsquad/apes");
      _extractFromRessources("POG");
    }
    catch (Exception e) {
      e.printStackTrace();
    }
    lnkFenetrePrincipale.getLnkArbreExplorateur().load();
  }

  private void _extractFromRessources(String str) throws Exception {
    String pathIco = lnkPreferences.getPathIcones();
    (new File(pathIco)).mkdir();
    URL ip = ClassLoader.getSystemResource(str);

    if (ip.getFile().lastIndexOf("!") == -1) {
      File fl = new File(URLDecoder.decode(ip.getFile()));
      Stack pile = new Stack();
      pile.push(fl.listFiles());
      Vector vgif = new Vector();
      while (!pile.isEmpty()) {
        File [] lst = (File [])pile.pop();
        for (int i = 0; i < lst.length; i++) {
          if (lst[i].isDirectory())
            pile.push(lst[i].listFiles());
          else if (lst[i].getName().endsWith(".gif"))
            vgif.add(lst[i]);
        }
      }
      for (int i = 0; i < vgif.size(); i++)
        PogToolkit.copyFile(((File)vgif.get(i)).getAbsolutePath(), pathIco + ((File)vgif.get(i)).getName());
    }
    else {
      String fi = new File(ip.getFile().substring(5, ip.getFile().lastIndexOf("!"))).getAbsolutePath();
      fi = URLDecoder.decode(fi);
      JarFile jf = new JarFile(fi);
      Enumeration en = jf.entries();
      while (en.hasMoreElements()) {
        JarEntry ob = (JarEntry) en.nextElement();
        if (!ob.isDirectory() && ob.getName().endsWith(".gif")) {
          InputStream is = jf.getInputStream(ob);
          String strf = ob.getName().substring(ob.getName().lastIndexOf("/"));
          File fis = new File(pathIco + strf);
          fis.getParentFile().mkdirs();
          if (fis.createNewFile()) {
            FileOutputStream fos = new FileOutputStream(fis);
            PogToolkit.extractStream(is, fos);
          }
        }
      }
    }
  }

  /**
   * Fonction accessible par l'utilisateur : ajout au noeud racine
   */
  public void ajouterElementPre() {
    try {
      this.lnkControleurPresentation.getlnkPresentation().
          getElementPresentation("1");
    }
    catch (Exception e) {
      return;
    }
	_invaliderTAG();
    String nomDefaut = "no_name_" + _numeroNouvelElement;
    _numeroNouvelElement++;
    ElementPresentation element = lnkControleurOrganiser.ajouterElementPre(
        nomDefaut);
    /*
     A Faire plus tard ...
     */
//    this.lnkFenetrePrincipale.lnkControleurPanneaux.loadCentre("DRole");
    //this.lnkFenetrePrincipale.lnkArbreExplorateur.load();
    this.lnkFenetrePrincipale.getLnkArbrePresentation().loadElement(element); //ajouterNoeud(element);
    this.lnkFenetrePrincipale.getLnkControleurPanneaux().loadCentre("DElementPresentation", element);
    this.lnkFenetrePrincipale.getLnkArbrePresentation().setNodeEditable();
  }

  public void supprimerContenu(ElementPresentation elem) {
	_invaliderTAG();
    lnkControleurOrganiser.supprimerContenu(elem);
    refreshAll(elem);
  }

  public void modifierElement(ElementPresentation elem, String nom, String desc) {
	_invaliderTAG();
    lnkControleurOrganiser.modifierElement(elem, nom, desc);
    refreshAll(elem);
  }

  public void modifierNomDePresentation(ElementPresentation elem, String nom){
	_invaliderTAG();
    lnkControleurOrganiser.modifierNomDePresentation(elem, nom);
    refreshAll(elem);
  }

  public ControleurPresentation getlnkControleurPresentation() {
    return lnkControleurPresentation;
  }

  public void changerIcone(ElementPresentation elemt, File file) {
	_invaliderTAG();
    elemt.set_icone(lnkPreferences.getIconeDefaut(file.getAbsolutePath()));
	refreshAll(elemt);
  }

  public Preferences getLnkPreferences() {
    return this.lnkPreferences;
  }

  public ControleurPresenter getLnkControleurPresenter() {
    return this.lnkControleurPresenter;
  }

  public void nouvellePresentationAvecModele(final String pathBibli,
                                             final String pathModele) {
    _verifierSauve(true);
    lnkFenetrePrincipale.jLabelPathBib.setText(pathBibli);
    new FenetrePrincipale.TheTraitement("NewModele") {
      public void traitement() {
        try {
          _numeroNouvelElement = 1;
          File f = new File(pathModele);
          lnkFenetrePrincipale.set_pathSave("");

          String nomPres = f.getName().substring(0, f.getName().length() - 5);

          if (lnkControleurPresenter.nouvellePresentationAvecModele(f, nomPres,
              new File(pathBibli)) == false)
            PogToolkit.showErrorMsg(lnkFenetrePrincipale.getLnkLangues().
                                    valeurDe("errmsgmodeleinvalide"),
                                    lnkFenetrePrincipale);
          else {
            lnkFenetrePrincipale.getLnkArbrePresentation().load();
            lnkFenetrePrincipale.getLnkArbreExplorateur().load();
            lnkFenetrePrincipale.getLnkPanneauBibliotheque().load();
            _modele = getlnkControleurPresentation().get_pathModele().
                lastModified();
            System.setProperty("user.dir", pathBibli);
            _invaliderTAG();
          }
        }
        catch (Exception e) {
          e.printStackTrace();
        }
        
      }
    };
  }

  public void majLiensFichiers(String orig, String dest) {
    if (!orig.equals(dest)) {
		_invaliderTAG();
      this.lnkControleurPresentation.majLiensFichiers(orig, dest);
    }
  }

  public boolean estFichierAffecte(String f)
  {
    return this.lnkControleurPresentation.estFichierAffecte(f);
  }

  /**
   * Supprime toute reference a l'icone specifie (restaure l'icone par defaut de l'element)
   * @param icone Icone que l'on souhaite dereferencer
   */
  public void supprimerLienIcone(ImageIcon icone)
  {
	_invaliderTAG();
    this.lnkControleurPresentation.supprimerLienIcone(icone);
  }

  public void lancer_editeur(String p0) {
    int indexPt = p0.lastIndexOf('.');

    //Si extension inexistante dans p0
    if (indexPt == -1) {
      lnkFenetrePrincipale.getLnkDebug().afficher("manqueext");
      return;
    }

    //Sinon
    String ext = p0.substring(indexPt + 1, p0.length());
    String editeur = lnkPreferences.get_editeur(ext);
    if (editeur == null)
    	return;
	if (editeur.equals("PIKA"))
		FenetrePrincipale.INSTANCE.getLnkappliTest().ouvrirFile(p0);
	else {
		String[] par = new String[] { new File(editeur).getAbsolutePath(), p0};
  		try {
    		Process p = Runtime.getRuntime().exec(par);
  		}
  		catch (Exception e) {}
  	}
  }

  public ControleurOrganiser getLnkControleurOrganiser() {
    return lnkControleurOrganiser;
  }

  public ControleurGuide getLnkControleurGuide() {
    return lnkControleurGuide;
  }

  public void supprimerElement(ElementPresentation elem) {
	_invaliderTAG();
    lnkControleurOrganiser.supprimerElement(elem);
    if (elem instanceof Guide) {
      ElementPresentation elPere = this.lnkControleurPresentation.
          getlnkPresentation().getElementParent(elem.get_id());
      elPere.supprimerGuide( (Guide) elem);
    }
    lnkFenetrePrincipale.getLnkControleurPanneaux().supprimerElement(elem);
    refreshAll(elem);
    lnkFenetrePrincipale.getLnkControleurPanneaux().loadVide();
  }

  private void refreshAll(ElementPresentation elem) {
    lnkFenetrePrincipale.getLnkArbrePresentation().loadElement(elem);
    lnkFenetrePrincipale.getLnkControleurPanneaux().reload();
  }

  /*En fonction du type de l'element et du type reclame( in ou out), cree la
   liste des elements en entre ou sortie pour l'element concerne*/
  public DefaultListModel getList_Element(ElementPresentation elem,
                                          String inout) {
    /*String res [] = {"b","o","n"};
         return res;*/
    ModelElement m = (ModelElement) ( (PresentationElementModele) elem).
        getLnkModelElement();
    DefaultListModel res = new DefaultListModel();
    if (m instanceof Activity) {
      if (inout.equals("in")) {
        for (int i = 0; i < ( (Activity) m).getInputCount(); i++) {
          res.add(i, ( (Activity) m).getInput(i));
        }
      }
      else if (inout.equals("out")) {
        for (int i = 0; i < ( (Activity) m).getOutputCount(); i++) {
          res.add(i, ( (Activity) m).getOutput(i));
        }
      }
    }
    else if (m instanceof WorkProduct) {
      if (inout.equals("in")) {
        for (int i = 0; i < ( (WorkProduct) m).getInputCount(); i++) {
          res.add(i, ( (WorkProduct) m).getInput(i));
        }
      }
      else if (inout.equals("out")) {
        for (int i = 0; i < ( (WorkProduct) m).getOutputCount(); i++) {
          res.add(i, ( (WorkProduct) m).getOutput(i));
        }
      }
    }
    else if (m instanceof WorkDefinition) {
      for (int i = 0; i < ( (WorkDefinition) m).subWorkCount(); i++) {
        res.add(i, ( (WorkDefinition) m).getSubWork(i));
      }
    }
    else if (m instanceof ProcessRole) {
      for (int i = 0; i < ( (ProcessRole) m).getFeatureCount(); i++) {
        res.add(i, ( (ProcessRole) m).getFeature(i));
      }
    }
    return res;
  }

  public Guide[] getGuides(ElementPresentation elem) {
    /*if (elem instanceof PresentationElementModele)
         {*/
    Vector vg = elem.getGuides();
    Guide[] tab = new Guide[vg.size()];
    for (int i = 0; i < vg.size(); i++) {
      tab[i] = (Guide) vg.elementAt(i);
    }
    return tab;
    //}
    /*si on doit recuperer les guides d'autres types d'elements,
        mettre le traitement a la suite*/
    //return new Object[0];
  }

  public void synchroniserApes()
  {
  	if (_modele == getlnkControleurPresentation().get_pathModele().lastModified())
  		return;

	_invaliderTAG();

	new FenetrePrincipale.TheTraitement("Synchro") {
		public void traitement() {
			Apes.loadModel(lnkControleurPresentation.get_pathModele());
			getLnkControleurPresenter().synchroniserApes(true);
			lnkFenetrePrincipale.getLnkDebug().patienter("", 0, 0);
			lnkFenetrePrincipale.getLnkArbrePresentation().load();
			_modele = lnkControleurPresentation.get_pathModele().lastModified();
		}
	};
  }

  public ControleurExporter getLnkControleurExporter() {
    return lnkControleurExporter;
  }

  public void monter(ElementPresentation elt) {
    _invaliderTAG();
	String ancienId = elt.get_id();
    Hashtable res = this.lnkControleurOrganiser.monter(elt);
    if (res != null) { // Impossibilite de monter si deja en 1ere position (cas null)
      this.lnkFenetrePrincipale.getLnkArbrePresentation().reloadElement(ancienId, elt.get_id(), elt.get_id(), ancienId);
    }
  }

  public void descendre(ElementPresentation elt) {
    _invaliderTAG();
	String ancienId = elt.get_id();
    Hashtable res = this.lnkControleurOrganiser.descendre(elt);
    if (res != null) { // Impossibilite de descendre si deja en derniere position (cas null)
      this.lnkFenetrePrincipale.getLnkArbrePresentation().reloadElement(ancienId, elt.get_id(), elt.get_id(), ancienId);
    }
  }

	public void quitter() {
		_verifierSauve(true);
		System.exit(0);
	}

	/**
	 * Change la bibliothèque
	 * @param newBib Nouvelle bibliothèque
	 */
	public void changerBibliotheque(File newBib) {
		_invaliderTAG();
		lnkControleurPresentation.getlnkPresentation().setBibliotheque(newBib.getAbsolutePath());
		HashMap allmap = new HashMap();
		Vector sscontenu = new Vector();
		Stack stff = new Stack();
		stff.push(newBib);
		while (!stff.isEmpty()) {
			File ff = (File)stff.pop();
			if (ff.isFile())
				allmap.put(ff.getName(), ff);
			else {
				File [] ssff = ff.listFiles();
				for (int i = 0; i < ssff.length; i++)
					stff.push(ssff[i]);
			}
		}
		Object [] elem = lnkControleurPresentation.getlnkPresentation().listeElementPresentation();
		for (int i = 0; i < elem.length; i++) {
			ElementPresentation ep = (ElementPresentation)elem[i];
			if (ep.getContenu() == null)
				continue;
			if (allmap.containsKey(ep.getContenu().getFile().getName()))
				lnkControleurOrganiser.associerContenu(ep, (File)allmap.get(ep.getContenu().getFile().getName()));
			else {
				sscontenu.add("\t" + ep.get_nomPresentation());
				lnkControleurOrganiser.supprimerContenu(ep);
			}
		}
		if (sscontenu.size() > 0) {
			lnkFenetrePrincipale.getLnkDebug().afficher("perdulien");
			lnkFenetrePrincipale.getLnkDebug().afficher(sscontenu.toArray());
		}
		lnkFenetrePrincipale.getLnkArbreExplorateur().load();
		lnkFenetrePrincipale.jLabelPathBib.setText(lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation().lnkBibliotheque.getAbsolutePath());
	}
}