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

package POG.interfaceGraphique.fenetre.PanneauxDetails;

import java.awt.Component;
import java.awt.Dimension;
import java.awt.Rectangle;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.Transferable;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDragEvent;
import java.awt.dnd.DropTargetDropEvent;
import java.awt.dnd.DropTargetEvent;
import java.awt.dnd.DropTargetListener;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.DefaultListCellRenderer;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.table.AbstractTableModel;

import org.ipsquad.apes.model.spem.core.ModelElement;
import org.ipsquad.apes.model.spem.process.structure.WorkProduct;

import POG.interfaceGraphique.action.ControleurPanneaux;
import POG.interfaceGraphique.fenetre.FenetrePrincipale;
import POG.objetMetier.ElementPresentation;
import POG.objetMetier.Guide;
import POG.objetMetier.PresentationElementModele;
import POG.utile.PogToolkit;

abstract public class PanneauDetail
    extends JPanel implements DropTargetListener{
  private DropTarget _dropTarget = null;
  private JLabel nom_modele = new JLabel();
  private JLabel jLabel9 = new JLabel();
  private JLabel jLabel2 = new JLabel();
  private JLabel jLabel3 = new JLabel();
  private JTextField nom_pres = new JTextField();
  private JLabel fichier_associe = new JLabel();
  private JTextArea desc = new JTextArea();
  private JScrollPane areaScrollPane;
  public JButton jButton1 = new JButton();
  public PanneauDetail _instance;

  /** Traitement de l'\uFFFDvenement "un objet entre dans la zone de Drop"
   * @param event  Evenement DropTargetDragEvent associ\uFFFD \uFFFD l'initialisation du Drop
   */
  public void dragEnter(DropTargetDragEvent event)
  {
    event.acceptDrag(DnDConstants.ACTION_MOVE);
  }

  /** << Impl\uFFFDmentation inutilis\uFFFDe de l'interface DragTargetListener >> */
  public void dropActionChanged(DropTargetDragEvent event) {}

  /** << Impl\uFFFDmentation inutilis\uFFFDe de l'interface DragTargetListener >> */
  public void dragExit(DropTargetEvent event) {}

  /** << Impl\uFFFDmentation inutilis\uFFFDe de l'interface DragTargetListener >> */
  public void dragOver(DropTargetDragEvent event) {}

  /** Traitement de l'\uFFFDvenement "un objet a \uFFFDt\uFFFD Dropp\uFFFD"
   * @param event  Evenement DropTargetDragEvent associ\uFFFD au Drop
   */
  public void drop(DropTargetDropEvent event)
  {
    try {
      Transferable transferable = event.getTransferable();

      if (transferable.isDataFlavorSupported(DataFlavor.stringFlavor)) {
        event.acceptDrop(DnDConstants.ACTION_MOVE);
        String s = (String) transferable.getTransferData(DataFlavor.
            stringFlavor);
        if (s.startsWith("CONT=> ") && ((DropTarget)event.getSource()).getComponent().equals(fichier_associe)){
          lnkControleurPanneaux.getLnkSysteme().associerContenu(_elementCourant,
              new File(s.substring(7)));
          FenetrePrincipale.INSTANCE.getLnkArbreExplorateur().load();
        }
        else if (s.startsWith("IMG=> ") && ((DropTarget)event.getSource()).getComponent().equals(jLabel9)){
          File fichIcon = new File(s.substring(6));
          lnkControleurPanneaux.getLnkSysteme().changerIcone(_elementCourant, fichIcon);
          this.jLabel9.setToolTipText(fichIcon.getAbsolutePath());
          FenetrePrincipale.INSTANCE.getLnkArbreExplorateur().load();
        }
      }
    }
    catch (Exception e) {
      event.rejectDrop();
      e.printStackTrace();
    }
  }

  public PanneauDetail(ControleurPanneaux control) {
    lnkControleurPanneaux = control;
    jbinit();
    jLabel9.setDropTarget(new DropTarget(this, this));
    nom_pres.setDropTarget(new DropTarget(this, this));
    desc.setDropTarget(new DropTarget(this, this));
    fichier_associe.setDropTarget(new DropTarget(this, this));
    _instance = this;
  }

  public void jbinit(){
    this.setPreferredSize(new Dimension(425, 540));
    this.setMinimumSize(new Dimension(425, 540));
    this.setLayout(null);

    //entete du panneau
    nom_modele.setBounds(new Rectangle(70, 10, 350, 40));
    nom_modele.setBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("nomdsmodele")),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
        nom_modele.getBorder()));


    //Label de l'icone
    jLabel9.setBounds(new Rectangle(10, 10, 50, 50));

    //Label introduisant le nom de la presentation
    jLabel2.setBounds(new Rectangle(70, 60, 110, 21));

    nom_pres.setBounds(new Rectangle(190, 60, 229, 24));
    nom_pres.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
          lnkControleurPanneaux.getLnkSysteme().modifierElement(_elementCourant, getNom_Pres(), getDesc());
        lnkControleurPanneaux.getLnkSysteme().lnkFenetrePrincipale.getLnkArbrePresentation().loadElement(_elementCourant);
      }
    });


    jLabel3.setText("Fichier associe :");
    jLabel3.setBounds(new Rectangle(11, 92, 130, 20));

    fichier_associe.setBounds(new Rectangle(110, 92, 180, 21));

    JButton buttonAttach = new JButton("", FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("process_tree_leaf_desc_file"));
    buttonAttach.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkControleurPanneaux.getLnkSysteme().choisirContenu(_elementCourant);
      }
    });
    buttonAttach.setBounds(new Rectangle(300, 93, 30, 21));

    JButton buttonView = new JButton("", FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("view"));
    buttonView.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkControleurPanneaux.getLnkSysteme().lancer_editeur(fichier_associe.getToolTipText());
      }
    });
    buttonView.setBounds(new Rectangle(360, 93, 30, 21));

    JButton buttonDel = new JButton("", FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("deletefile"));
    buttonDel.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        lnkControleurPanneaux.getLnkSysteme().supprimerContenu(_elementCourant);
      }
    });
    buttonDel.setBounds(new Rectangle(390, 93, 30, 21));


    JButton buttonCreate = new JButton("", FenetrePrincipale.INSTANCE.getLnkSysteme().getLnkPreferences().getIconeDefaut("menu_item_new"));
    buttonCreate.addActionListener(new ActionListener() {
      public void actionPerformed(ActionEvent evt) {
        if(fichier_associe.getText().equals(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("aucunfichier")))
          FenetrePrincipale.INSTANCE.getLnkappliTest().nouvelPage(_instance);
        else
          FenetrePrincipale.INSTANCE.getLnkappliTest().ouvrirFile(fichier_associe.getToolTipText());
      }
    });
    buttonCreate.setBounds(new Rectangle(330, 93, 30, 21));

    areaScrollPane = new JScrollPane(desc);
    areaScrollPane.setBounds(new Rectangle(11, 134, 400, 120));
    areaScrollPane.setVerticalScrollBarPolicy(
        JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
    areaScrollPane.setBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createCompoundBorder(
        BorderFactory.createTitledBorder(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("description")),
        BorderFactory.createEmptyBorder(5, 5, 5, 5)),
        areaScrollPane.getBorder()));

    desc.setBounds(new Rectangle(11, 134, 300, 120));
    desc.setWrapStyleWord(true);
    desc.setLineWrap(true);
    desc.addFocusListener(new java.awt.event.FocusAdapter() {
      public void focusLost(FocusEvent e) {
        lnkControleurPanneaux.getLnkSysteme().modifierElement(_elementCourant,
            getNom_Pres(), getDesc());
      }
    });
    desc.addKeyListener(new java.awt.event.KeyAdapter() {
      public void keyReleased(KeyEvent e) {
        if (e.getKeyCode() == KeyEvent.VK_ENTER)
          lnkControleurPanneaux.getLnkSysteme().modifierElement(_elementCourant,
            getNom_Pres(), getDesc());
      }
    });


    jButton1.setBounds(new Rectangle(130, 489, 184, 22));
    jButton1.setText(FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("ajouterguide"));
    jButton1.addActionListener(new ActionListener(){
      public void actionPerformed(ActionEvent evt) {
        afficherMenuGuides(jButton1, jButton1.getParent().getX() + 15, jButton1.getParent().getY());
      }
    });

    this.add(nom_pres, null);
    this.add(nom_modele, null);
    this.add(buttonView, null);
    this.add(buttonCreate, null);
    this.add(buttonAttach, null);
    this.add(fichier_associe, null);
    this.add(areaScrollPane, null);
    this.add(buttonDel, null);
    this.add(jLabel3, null);
    this.add(jLabel2, null);
    this.add(jLabel9, null);
    this.add(jButton1, null);

  }

  public abstract void afficherMenuGuides(Component compo, int x, int y);

  public abstract void loadElement(ElementPresentation idelem);

  public ElementPresentation get_elementCourant() {
    return _elementCourant;
  }

  public void setLabelNomPres(String label){
    jLabel2.setText(label);
  }

  public void setNom_modele(String st) {
    nom_modele.setText(st);
  }

  public void setVisibleNomModele(boolean b){
    nom_modele.setVisible(b);
  }

  public void setNom_Pres(String st) {
    nom_pres.setText(st);
  }

  public String getNom_Pres() {
    return nom_pres.getText();
  }

  public void setDesc(String st) {
     desc.setText(st);
     areaScrollPane.setViewportView(desc);
   }

   public String getDesc() {
   return desc.getText();
 }

 public void supprimerElement(ElementPresentation elt) {
   if (elt.equals(_elementCourant))
     _elementCourant = null;
 }

  public void setIcon(ImageIcon icon){
    jLabel9.setIcon(icon);
  }

  public void setFichier_associe(File fich) {
    if (fich != null){
      fichier_associe.setText(fich.getName());
      fichier_associe.setToolTipText(fich.getAbsolutePath());
    }
    else{
      fichier_associe.setText("<aucun>");
      fichier_associe.setToolTipText("");
    }

  }


  protected ElementPresentation _elementCourant;

  /**
   * @supplierCardinality 1
   * @directed
   */
  protected ControleurPanneaux lnkControleurPanneaux;

  public void actionMenu(ActionEvent evt) {
    if (evt.getActionCommand().equals(lnkControleurPanneaux.getLnkSysteme().
                                      lnkFenetrePrincipale.getLnkLangues().
                                      valeurDe("Ajouter Guide"))) {
      lnkControleurPanneaux.getLnkSysteme().ajouterGuide( (
          PresentationElementModele) _elementCourant, "montype");
    }
    else if (evt.getActionCommand().equals(lnkControleurPanneaux.getLnkSysteme().
                                           lnkFenetrePrincipale.getLnkLangues().
                                           valeurDe("supprimercontenu"))) {
      lnkControleurPanneaux.getLnkSysteme().supprimerContenu(_elementCourant);
    }
    else if (evt.getActionCommand().equals(lnkControleurPanneaux.getLnkSysteme().
                                           lnkFenetrePrincipale.getLnkLangues().
                                           valeurDe("ajoutercontenu"))
             || evt.getActionCommand().equals(lnkControleurPanneaux.getLnkSysteme().
                                           lnkFenetrePrincipale.getLnkLangues().
                                           valeurDe("changerdecontenu"))) {
      File fichier = PogToolkit.chooseFileInBibli(lnkControleurPanneaux.getLnkSysteme().lnkFenetrePrincipale);
      if (fichier == null) {
        return;
      }
      if (fichier.exists()) {
        lnkControleurPanneaux.getLnkSysteme().associerContenu(_elementCourant,
            fichier);
      }
    }
    else if (evt.getActionCommand().equals(lnkControleurPanneaux.getLnkSysteme().
                                           lnkFenetrePrincipale.getLnkLangues().
                                           valeurDe("supprimer"))) {
      lnkControleurPanneaux.getLnkSysteme().supprimerElement(_elementCourant);
      _elementCourant = null;
    }
  }

  /**
   * Cell renderer pour les listes de produits, activites, roles et definition travail
   * <p>Title: POG</p>
   * <p>Description: Presentation Organisation Generation de composant</p>
   * <p>Copyright: Copyright (c) 2003</p>
   * <p>Company: </p>
   * @author not attributable
   * @version 1.0
   */
  protected class ModelElementListCellRenderer
      extends DefaultListCellRenderer {
    public Component getListCellRendererComponent(JList list,
                                                  Object value,
                                                  int index,
                                                  boolean isSelected,
                                                  boolean cellHasFocus) {
      ModelElement me = (ModelElement) value ;
      if (me instanceof WorkProduct && ((WorkProduct)me).getReferences() == WorkProduct.REFERENCES_BY_REQUIRED_INTERFACE)
      {
        return (super.getListCellRendererComponent(list, me.getName() + " (" +
            lnkControleurPanneaux.getLnkSysteme().
            lnkFenetrePrincipale.getLnkLangues().
            valeurDe("requis") + ")", index, isSelected, cellHasFocus)) ;
      }
      else
      {
        return (super.getListCellRendererComponent(list, me.getName(), index,
            isSelected, cellHasFocus)) ;
      }
    }
  }

  protected MouseListener navigationMouseListener = new MouseAdapter() {
    public void mouseClicked(MouseEvent e) {
      if (e.getClickCount() == 2)
      {
        Object src = e.getSource();
        if (src instanceof JList)
        {
          JList list = (JList) src;
          int index = list.locationToIndex(e.getPoint());
          if (index != -1) {
            lnkControleurPanneaux.getLnkSysteme().lnkFenetrePrincipale
                .getLnkArbrePresentation().selectNodeContaining(
                list.getModel().getElementAt(index));
          }
        }
        else if (src instanceof JTable)
        {
          JTable table = (JTable) src;
          if (table.getSelectedRowCount() > 0)
          {
            Guide guide = ((TableGuideModel) table.getModel()).getGuide(table.getSelectedRow());
            lnkControleurPanneaux.getLnkSysteme().lnkFenetrePrincipale
                .getLnkArbrePresentation().selectNodeContaining(guide);
          }
        }
      }
    }
  };

  public class TableGuideModel
      extends AbstractTableModel {
    private Guide[] _guides;

    public TableGuideModel() {
      super();
      this._guides = new Guide[0];
    }

    public TableGuideModel(Object[] t) {
      super();
      if (t instanceof Guide[]) {
        this._guides = (Guide[]) t;
      }
      else {
        this._guides = new Guide[0];
      }
    }

    public int getColumnCount() {
      return 3;
    }

    public Guide getGuide(int rowIndex) {
      return this._guides[rowIndex];
    }

    public int getRowCount() {
      return _guides.length;
    }

    public Object getValueAt(int rowIndex, int columnIndex) {
      switch (columnIndex) {
        case 0:
          return this._guides[rowIndex].getType();
        case 1:
          return this._guides[rowIndex].get_nomPresentation();
        case 2: {
          if (this._guides[rowIndex].getContenu() == null) {
            return "aucun";
          }
          else {
            return this._guides[rowIndex].getContenu().getFile().getName();
          }
        }
      }
      return null;
    }

    private String[] columnNames = {
        FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("type2"),
        FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("nom"),
        FenetrePrincipale.INSTANCE.getLnkLangues().valeurDe("fichier")
    };

    public String getColumnName(int columnIndex) {
      return this.columnNames[columnIndex];
    }

    public Class getColumnClass(int columnIndex) {
      return (String.class);
    }

    public boolean isCellEditable(int rowIndex, int columnIndex) {
      return false;
    }

    public void setValueAt(Object aValue, int rowIndex, int columnIndex) {
      return;
    }
  }
}