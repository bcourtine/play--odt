package models;

/**
 * Classe représentant une personne pour tester la génération de documents utilisant des objets complexes.
 *
 * @author Benoît Courtine
 * @since 19/10/2010
 */
public class Personne {

	/** Nom de la personne. */
	public String nom;

	/** Prénom de la personne. */
	public String prenom;

	/** Age de la personne. */
	public int age;

	/**
	 * Création d'une nouvelle personne.
	 *
	 * @param nom Nom de la personne.
	 * @param prenom Prénom de la personne.
	 * @param age Age de la personne
	 */
	public Personne(String nom, String prenom, int age){
		this.nom = nom;
		this.prenom = prenom;
		this.age = age;
	}

	/**
	 * Surcharge de {@link Object#toString()} pour obtenir une description pertinente.
	 *
	 * @return Chaine contenant le nom, le prénom et l'âge de la personne.
	 */
	@Override
	public String toString() {
		return "Nom : " + this.nom + " - Prénom : " + this.prenom + " - Age : " + Integer.toString(this.age);
	}
}
