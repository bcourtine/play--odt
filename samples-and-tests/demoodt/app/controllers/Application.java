package controllers;

import models.Personne;
import play.mvc.Controller;

import static play.modules.odt.OdtRenderer.*;

/**
 * Contrôleur de démonstration, utilisant l'import statique des méthodes de {@link play.modules.odt.OdtRenderer}.
 */
public class Application extends Controller {

	/**
	 * Page d'accueil par défaut
	 */
    public static void index() {
        render();
    }

	/**
	 * Génération du document de démonstration, avec un titre et un utilisateur (possédant un nom et un prénom).
	 */
	public static void demo() {
		// Titre.
		String titre = "Document de démonstration";

		// Utilisateur.
		Personne utilisateur = new Personne("Dupont", "Henry");

		// Génération du document.
		renderOdt(titre, utilisateur);
	}
}