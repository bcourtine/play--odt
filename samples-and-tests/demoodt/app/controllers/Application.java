package controllers;

import play.modules.odt.OdtController;

/**
 * Contrôleur de démonstration, étendant {@link play.modules.odt.OdtController}.
 */
public class Application extends OdtController {

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