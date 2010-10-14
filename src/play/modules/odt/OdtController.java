package play.modules.odt;

import play.mvc.Controller;

/**
 * Contrôleur permettant le rendu de documents ODT. Cette classe étendant {@link play.mvc.Controller}, il est possible
 * de l'étendre directement pour bénéficier des méthodes du Contrôleur standard de Play ainsi que de la méthode
 * addtionnelle {@link #renderOdt(Object...)} permettant d'obtenir un rendu ODT.
 *
 * @author Benoît Courtine.
 * @since 14/10/2010.
 */
public class OdtController extends Controller {

	/**
	 * Délégation du rendu à la classe {@link OdtRenderer}.
	 *
	 * @param args Paramètres de rendering.
	 */
	protected static void renderOdt(Object... args) {
		OdtRenderer.renderOdt(args);
	}
}