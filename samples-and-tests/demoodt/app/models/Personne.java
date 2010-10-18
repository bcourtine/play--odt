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

	/**
	 * Création d'une nouvelle personne.
	 *
	 * @param nom Nom de la personne.
	 * @param prenom Prénom de la personne.
	 */
	public Personne(String nom, String prenom){
		this.nom = nom;
		this.prenom = prenom;
	}

	/**
	 * Surcharge de {@link Object#toString()} pour obtenir une description pertinente.
	 *
	 * @return Chaine contenant le nom et le prénom de la personne.
	 */
	@Override
	public String toString() {
		return "Nom : " + this.nom + " - Prénom : " + this.prenom;
	}
}
