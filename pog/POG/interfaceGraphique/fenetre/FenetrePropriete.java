package POG.interfaceGraphique.fenetre;

import java.awt.BorderLayout;
import java.awt.Dimension;
import java.awt.GridLayout;
import java.text.DateFormat;
import java.util.Date;

import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;

import POG.objetMetier.Presentation;
import POG.utile.PogToolkit;

public class FenetrePropriete extends FenetrePOG {
	
	private JLabel lblChemin = new JLabel();
	private JTextField _txtChemin = new JTextField();
	private JLabel lblChemExport = new JLabel();
	private JTextField _txtChemExport = new JTextField();
	private JLabel lblExport = new JLabel();
	private JTextField _txtExport = new JTextField();
	private JLabel lblResponsable = new JLabel();
	private JTextField _txtResponsable = new JTextField();
	private JLabel lblVersion = new JLabel();
	private JTextField _txtVersion = new JTextField();
	private JLabel lblDateApes = new JLabel();
	private JTextField _txtDateApes = new JTextField();
	private JLabel lblEmail = new JLabel();
	private JTextField _txtEmail = new JTextField();
	
	private String langue(String toto) {
		return lnkFenetrePrincipale.getLnkLangues().valeurDe(toto);
	}
	
	private void jbInit() throws Exception {
		this.setTitle(langue("propriete"));
		this.setSize(new Dimension(485, 200));

		lblChemin.setText(langue("cheminmodel"));
		lblChemExport.setText(langue("chemexport"));
		lblDateApes.setText(langue("datesauve"));
		lblEmail.setText(langue("emailresp"));
		lblResponsable.setText(langue("auteur"));
		lblExport.setText(langue("dateexport"));
		lblVersion.setText(langue("lblversion"));

		_txtChemin.setEditable(false);
		_txtDateApes.setEditable(false);
		_txtChemExport.setEditable(false);
		_txtExport.setEditable(false);

		JPanel jPanel1 = new JPanel();
		GridLayout grider = new GridLayout();
		grider.setRows(7);
		grider.setColumns(2);
		jPanel1.setLayout(grider);
		jPanel1.add(lblChemin, null);
		jPanel1.add(_txtChemin, null);
		jPanel1.add(lblChemExport, null);
		jPanel1.add(_txtChemExport, null);		
		jPanel1.add(lblDateApes, null);
		jPanel1.add(_txtDateApes, null);
		jPanel1.add(lblExport, null);
		jPanel1.add(_txtExport, null);
		jPanel1.add(lblResponsable, null);
		jPanel1.add(_txtResponsable, null);
		jPanel1.add(lblEmail, null);
		jPanel1.add(_txtEmail, null);
		jPanel1.add(lblVersion, null);
		jPanel1.add(_txtVersion, null);
		
		this.getContentPane().add(jPanel1, BorderLayout.CENTER);
		PogToolkit.centerWindow(this);
	}

  public FenetrePropriete(FenetrePrincipale fp){
		super(fp);
		try {
			jbInit();
			Presentation pres = lnkFenetrePrincipale.getLnkSysteme().getlnkControleurPresentation().getlnkPresentation();
			if (pres.get_pathModele() != null) {
				_txtChemin.setText(pres.get_pathModele().getAbsolutePath());
				_txtDateApes.setText(DateFormat.getDateInstance().format(new Date(pres.get_pathModele().lastModified())));
			}
			if (pres.get_pathExport() != null) {
				_txtChemExport.setText(pres.get_pathExport().getAbsolutePath());
				_txtExport.setText(DateFormat.getDateInstance().format(new Date(pres.get_pathExport().lastModified())));
			}
			_txtEmail.setText(new String(pres.get_email()));
			_txtResponsable.setText(new String(pres.get_auteur()));
			_txtVersion.setText(new String(pres.get_version()));
		}
		catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void actionAnnuler() {
		this.setVisible(false);
	}
	
	public void actionOK() {
		lnkFenetrePrincipale.getLnkSysteme().changerProprietes(_txtResponsable.getText(), _txtEmail.getText(), _txtVersion.getText());
		this.setVisible(false);
	}
	
}
