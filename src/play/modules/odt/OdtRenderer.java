package play.modules.odt;

import play.classloading.enhancers.LocalvariablesNamesEnhancer;
import play.mvc.Http;
import play.mvc.Scope;
import play.templates.Template;

import java.util.Map;

/**
 * La méthode de rendering {@link #renderOdt(Object...)} peut être appelée après import statique de cette classe dans
 * un contrôleur quelconque.
 *
 * @author Benoît Courtine.
 * @since 14/10/2010.
 */
public class OdtRenderer {

	/**
	 * Render the corresponding template
	 *
	 * @param args The template data
	 */
	public static void renderOdt(Object... args) {
		String templateName;
		if (args.length > 0 && args[0] instanceof String && LocalvariablesNamesEnhancer.LocalVariablesNamesTracer.getAllLocalVariableNames(args[0]).isEmpty()) {
			templateName = args[0].toString();
		} else {
			templateName = getOdtTemplateName();
		}

		renderTemplateOdt(templateName, args);
	}

	/**
	 * Renvoie le nom du template ODT à utiliser.
	 *
	 * @return Nom du template ODT à utiliser.
	 */
	protected static String getOdtTemplateName() {
		final Http.Request request = Http.Request.current();
		String templateName = request.action.replace(".", "/") + ".odt";
		if (templateName.startsWith("@")) {
			templateName = templateName.substring(1);
			if (!templateName.contains(".")) {
				templateName = request.controller + "." + templateName;
			}
			templateName = templateName.replace(".", "/") + ".odt";
		}
		return templateName;
	}

	/**
	 * Création du template ODT, récupération des paramètres à passer au template, et génération du rendu final.
	 *
	 * @param templateName Nom du template à utiliser.
	 * @param args Données à passer en paramètre du template.
	 */

	protected static void renderTemplateOdt(String templateName, Object... args) {
		Template odtTemplate = new OdtTemplate(templateName);
		Map<String, Object> templateBinding = getTemplateParams(args);
		odtTemplate.render(templateBinding);
	}

	/**
	 * Création et enrichissement des paramètres permettant le rendu ODT.
	 * <p/>
	 * Le paramètre "args" est bien utilisé dans cette méthode, par l'injection de code effectuée par
	 * {@link play.classloading.enhancers.LocalvariablesNamesEnhancer}.
	 *
	 * @param args Données passées en paramètre de la méthode de rendering.
	 * @return Map des données pour la fusion avec le template ODT.
	 */
	@SuppressWarnings("unused")
	protected static Map<String, Object> getTemplateParams(Object... args) {
		// Template datas
		Map<String, Object> templateParams = Scope.RenderArgs.current().data;
		// Ajout des paramètres de la méthode.
		templateParams.putAll(LocalvariablesNamesEnhancer.LocalVariablesNamesTracer.getLocalVariables());
		// Ajout des données de session à la Map des informations permettant la création du rendu final.
		templateParams.put("session", Scope.Session.current().all());
		// Ajout des paramètres d'appel à la Map des informations permettant la création du rendu final.
		templateParams.put("params", Scope.Params.current().all());

		return templateParams;
	}
}
