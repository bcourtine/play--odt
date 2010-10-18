package controllers;

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
	 * Génération du document de démonstration.
	 */
	public static void demo() {
		String titre = "Document de démonstration";
		renderOdt(titre);
	}
}