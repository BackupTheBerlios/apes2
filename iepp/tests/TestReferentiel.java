package tests;


import java.io.File;
import iepp.application.areferentiel.ElementReferentiel;
import iepp.application.areferentiel.Referentiel;
import junit.framework.TestCase;


public class TestReferentiel extends TestCase
{
	/*
	public void testCreation()  
	{
		System.out.println("*****\n=>Création et chargement du référentiel");
		Referentiel ref = new Referentiel("RandomAccess");		
		String chaine = new String();
		chaine = ref.getCheminReferentiel();
		System.out.println("Path du referentiel : " + chaine);
		File fic = new File(chaine);	
		
		assertTrue(fic.exists() == true);		
		assertTrue(ref.getLastId() == 0);
		assertTrue(ref.getNomReferentiel().equalsIgnoreCase("RandomAccess"));
 
		System.out.println("\nChargement du référentiel uniquement");
 
		Referentiel ref2 = new Referentiel("RandomAccess");		
		String chaine2 = new String();
		chaine = ref.getCheminReferentiel();
		System.out.println("Path du referentiel : " + chaine);
		File fic2 = new File(chaine);	
		
		assertTrue(fic.exists() == true);		
		assertTrue(ref.getLastId() == 0);
		assertTrue(ref.getNomReferentiel().equalsIgnoreCase("RandomAccess"));			
	}
	*/
	/*
	public void testDefaut()
	{
		System.out.println("*****\n=> Test du référentiel Defaut");
		Referentiel ref = new Referentiel("Defaut");
		
		System.out.println(" - Verification du 1e Element"); 
		System.out.println("NomElt: " + ref.getComposantAt(0).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getComposantAt(0).getChemin());
		System.out.println("Id elt: " + ref.getComposantAt(0).getIdElement());
		System.out.println("N° du type: " + ref.getComposantAt(0).getType());
		assertTrue(ref.getComposantAt(0).getType() == ElementReferentiel.COMPOSANT);
		
		System.out.println(" - Verification du 2e Element"); 
		System.out.println("NomElt: " + ref.getDefProcAt(0).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getDefProcAt(0).getChemin());
		System.out.println("Id elt: " + ref.getDefProcAt(0).getIdElement());
		System.out.println("N° du type: " + ref.getDefProcAt(0).getType());
		assertTrue(ref.getDefProcAt(0).getType() == ElementReferentiel.DP);
		
		System.out.println(" - Verification du 3e Element"); 
		System.out.println("NomElt: " + ref.getComposantAt(1).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getComposantAt(1).getChemin());
		System.out.println("Id elt: " + ref.getComposantAt(1).getIdElement());
		System.out.println("N° du type: " + ref.getComposantAt(1).getType());
		assertTrue(ref.getComposantAt(1).getType() == ElementReferentiel.COMPOSANT);
		
		System.out.println(" - Verification du 4e Element"); 
		System.out.println("NomElt: " + ref.getDefProcAt(1).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getDefProcAt(1).getChemin());
		System.out.println("Id elt: " + ref.getDefProcAt(1).getIdElement());
		System.out.println("N° du type: " + ref.getDefProcAt(1).getType());
		assertTrue(ref.getDefProcAt(1).getType() == ElementReferentiel.DP);		
	}
	*/
	/*
	public void testRaf()
	{
		try
		{
			System.out.println("*****\nTest de lecture sur un RandomAccessFile");
			RandomAccessFile raf = new RandomAccessFile("." + File.separator + "Referentiels" + File.separator + "Defaut" + File.separator + "Defaut.ref", "rw");
			String ligne = raf.readLine();
			System.out.println(ligne);
			ligne = raf.readLine();
			System.out.println(ligne);
			ligne = raf.readLine();
			System.out.println(ligne);
		}
		catch(FileNotFoundException e)
		{
			e.printStackTrace();
		}
		catch(IOException e)
		{
			e.printStackTrace();
		}
		
	}
	*/
	public void testAjoutElements()
	{
		System.out.println("*****\n=>Test de l'ajout d'éléments");
		Referentiel ref = new Referentiel("AjoutElements");	
		ref.ajouterElement("." + File.separator + "composantpubliable.pre", ElementReferentiel.COMPOSANT);
		System.out.println("  => 1e composant ajouté !\n");
		ref.ajouterElement("noname0", ElementReferentiel.COMPOSANT_VIDE);
		System.out.println("  => 2e composant ajouté !\n");
		ref.ajouterElement("." + File.separator + "composantpubliable.pre", ElementReferentiel.PRESENTATION);
		System.out.println("  => 1e elt de présentation ajouté !\n");
		ref.ajouterElement("defProc1", ElementReferentiel.DP);
		System.out.println("  => 1e DP ajouté !\n");
		ref.ajouterElement("." + File.separator + "exempleAubry.pre", ElementReferentiel.PRESENTATION);
		System.out.println("  => 2e elt de présentation ajouté !\n");		

		System.out.println("Ajouts réussis");
		
		System.out.println(" - Verification du 2e composant"); 
		System.out.println("NomElt: " + ref.getComposantAt(1).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getComposantAt(1).getChemin());
		System.out.println("Id elt: " + ref.getComposantAt(1).getIdElement());
		System.out.println("N° du type: " + ref.getComposantAt(1).getType());
		
		System.out.println(" - Verification du 1e DP"); 
		System.out.println("NomElt: " + ref.getDefProcAt(0).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getDefProcAt(0).getChemin());
		System.out.println("Id elt: " + ref.getDefProcAt(0).getIdElement());
		System.out.println("N° du type: " + ref.getDefProcAt(0).getType());
	
		System.out.println(" - Verification du 2e elt de présentation"); 
		System.out.println("NomElt: " + ref.getPresentationAt(1).getNomElement());
		System.out.println("Chemin de l'elt: " + ref.getPresentationAt(1).getChemin());
		System.out.println("Id elt: " + ref.getPresentationAt(1).getIdElement());
		System.out.println("N° du type: " + ref.getPresentationAt(1).getType());
		
		System.out.println("Début de la suppression\n");
		
		/*
		ref.supprimerElement(1, ElementReferentiel.COMPOSANT);
		ref.supprimerElement(2, ElementReferentiel.COMPOSANT);		
		ref.supprimerElement(3, ElementReferentiel.PRESENTATION);
		ref.supprimerElement(4, ElementReferentiel.DP);
		ref.supprimerElement(5, ElementReferentiel.PRESENTATION);
		/*
		ref.supprimerElement(6, ElementReferentiel.COMPOSANT);
		ref.supprimerElement(7, ElementReferentiel.COMPOSANT);		
		ref.supprimerElement(8, ElementReferentiel.PRESENTATION);
		ref.supprimerElement(9, ElementReferentiel.DP);
		ref.supprimerElement(10, ElementReferentiel.PRESENTATION);
		
		ref.supprimerElement(11, ElementReferentiel.COMPOSANT);
		ref.supprimerElement(12, ElementReferentiel.COMPOSANT);		
		ref.supprimerElement(13, ElementReferentiel.PRESENTATION);
		ref.supprimerElement(14, ElementReferentiel.DP);
		ref.supprimerElement(15, ElementReferentiel.PRESENTATION);
		
		ref.supprimerElement(16, ElementReferentiel.COMPOSANT);
		ref.supprimerElement(17, ElementReferentiel.COMPOSANT);		
		ref.supprimerElement(18, ElementReferentiel.PRESENTATION);
		ref.supprimerElement(19, ElementReferentiel.DP);
		ref.supprimerElement(20, ElementReferentiel.PRESENTATION);
		*/
		System.out.println("FIN DE LA SUPPRESSION\n");
	}
}	