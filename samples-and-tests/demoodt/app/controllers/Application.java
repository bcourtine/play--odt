package controllers;

import models.Personne;
import play.mvc.Controller;
import play.mvc.Scope;

import java.util.ArrayList;
import java.util.List;

import static play.modules.odt.OdtRenderer.*;

/**
 * Contrôleur de démonstration, utilisant l'import statique des méthodes de {@link play.modules.odt.OdtRenderer}.
 */
public class Application extends Controller {

	/**
	 * Page d'accueil : guide de l'applicaion de démonstration.
	 */
    public static void index() {
        render();
    }

	/**
	 * Génération d'un document de démonstration, contenant uniquement une variable simple de type texte.
	 */
	public static void demoSimple() {
		String titre = "Document de démonstration";
		renderOdt(titre);
	}

	/**
	 * Génération d'un document de démonstration, affichant plusieurs propriétés d"un unique objet
	 * {@link models.Personne}.
	 */
	public static void demoObjet() {
		Personne utilisateur = new Personne("Dupont", "Henry", 32);
		renderOdt(utilisateur);
	}

	/**
	 * Génération d'un document de démonstration, affichant une valeur sauvegardée en session.
	 */
	public static void demoSession() {
		Scope.Session.current().put("fonction", "Administrateur");
		renderOdt();
	}

	/**
	 * Génération d'un document de démonstration, affichant une valeur passée en paramètre de la requête HTTP.
	 */
	public static void demoRequete() {
		// Récupération du numéro de dossier de la requête, "Inconnu" si non spécifié (pour assurer le bon
		// fonctionnement de la génération du document qui ne permet pas les paramètres non renseignés).
		if (!Scope.Params.current()._contains("numDossier")) {
			Scope.Params.current().put("numDossier", "Inconnu");
		}
		renderOdt();
	}

	/**
	 * Génération d'un document de démonstration, affichant une liste de prénoms.
	 */
	public static void demoListe() {
		List<String> prenoms = new ArrayList<String>();
		prenoms.add("Thomas");
		prenoms.add("Stéphanie");
		prenoms.add("Jules");
		prenoms.add("Virginie");
		renderOdt(prenoms);
	}
}
