package controllers;

import models.Personne;
import play.mvc.Controller;
import play.mvc.Scope;

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
	 * Génération du document de démonstration, contenant des éléments paramétrés provenant de différentes sources :
	 * <ul>
	 * <li>Un titre simple (chaîne de caractères) passé explicitement à la méthode de rendu.</li>
	 * <li>Un objet de utilisateur (contenant un nom, un prénom, et un âge), passé explicitement à la méthode de rendu.</li>
	 * <li>Une fonction, stockée en session.</li>
	 * <li>Un numéro de dossier, passé en paramètre de la requête.</li>
	 * </ul>
	 */
	public static void demo() {
		// Titre.
		String titre = "Document de démonstration";

		// Utilisateur.
		Personne utilisateur = new Personne("Dupont", "Henry", 32);

		// Ajout à la session de l'utilisateur courant la fonction "Administrateur".
		Scope.Session.current().put("fonction", "Administrateur");

		// Récupération du numéro de dossier de la requête, "Inconnu" si non spécifié (pour assurer le bon
		// fonctionnement de la génération du document qui ne permet pas les paramètres non renseignés).
		if (!Scope.Params.current()._contains("numDossier")) {
			Scope.Params.current().put("numDossier", "Inconnu");
		}

		// Génération du document, à partir du titre et de l'utilisateur. Les paramètres de la requête et de la
		// session sont pris en compte automatiquement dans la fusion sans qu'il soit nécessaire de les passer
		// explicitement à la méthode de rendu.
		renderOdt(titre, utilisateur);
	}
}