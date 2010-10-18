package play.modules.odt;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;
import play.Play;
import play.classloading.enhancers.LocalvariablesNamesEnhancer;
import play.data.validation.Validation;
import play.exceptions.PlayException;
import play.exceptions.TemplateNotFoundException;
import play.exceptions.UnexpectedException;
import play.mvc.Http;
import play.mvc.Scope;
import play.vfs.VirtualFile;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
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
	 * Factory permettant de créer des templates au format "JodReports".
	 */
	private static final DocumentTemplateFactory DOCUMENT_TEMPLATE_FACTORY = new DocumentTemplateFactory();

	/**
	 * Render the corresponding template
	 *
	 * @param args The template data
	 */
	public static void renderOdt(Object... args) {
		String templateName = null;
		if (args.length > 0 && args[0] instanceof String && LocalvariablesNamesEnhancer.LocalVariablesNamesTracer.getAllLocalVariableNames(args[0]).isEmpty()) {
			templateName = args[0].toString();
		} else {
			templateName = templateOdt();
		}

		renderTemplateOdt(templateName, args);
	}

	/**
	 * Renvoie le nom du template ODT à utiliser.
	 *
	 * @return Nom du template ODT à utiliser.
	 */
	protected static String templateOdt() {
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
	 * Les différents arguments passés en paramètre de la méthode de rendering sont ajoutés à la {@link java.util.Map}
	 * des paramètres qui seront fusionnés dans le document final.
	 * <p/>
	 * A partir de la version 1.1, cette méthode stocke correctement dans le binding un niveau d'attributs pour les
	 * objets complexes (permettant ainsi de passer directement ces objets à la méthode de rendu).
	 *
	 * @param templateName Nom du template à utiliser.
	 * @param args Données passées en paramètre de la méthode de rendering.
	 */
	protected static void renderTemplateOdt(String templateName, Object... args) {
		// Template datas
		Map<String, Object> templateBinding = new HashMap<String, Object>();
		for (Object o : args) {
			templateBinding.putAll(LocalvariablesNamesEnhancer.LocalVariablesNamesTracer.getLocalVariables());
		}
		renderTemplateOdt(templateName, templateBinding);
	}

	/**
	 * Ajout de données à la {@link java.util.Map} des paramètres de rendu du document ODT. Une fois ces derniers
	 * paramètres complétés, le template est résolu à partir de son nom, et le rendu est réellement effectué.
	 *
	 * @param templateName Nom du template ODT à utiliser.
	 * @param args Paramètres de la méthode de rendering.
	 */
	protected static void renderTemplateOdt(String templateName, Map<String, Object> args) {
		Scope.RenderArgs templateBinding = Scope.RenderArgs.current();
		templateBinding.data.putAll(args);
		// Ajout de données à la Map des informations permettant la création de rendu final.
		templateBinding.put("session", Scope.Session.current());
		templateBinding.put("request", Http.Request.current());
		templateBinding.put("flash", Scope.Flash.current());
		templateBinding.put("params", Scope.Params.current());
		templateBinding.put("errors", Validation.errors());
		try {
			// Chargement du template ODT à partir du fichier physique résolu depuis le nom du template.
			DocumentTemplate template = odtTemplateLoad(templateName);

			// fileName est tronqué après le dernier "/" pour obtenir le nom du fichier généré.
			String fileName = templateName.substring(templateName.lastIndexOf("/") + 1);

			// Rendu ODT par fusion du template et des données.
			throw new RenderOdt(fileName, template, templateBinding.data);
		} catch (TemplateNotFoundException ex) {
			if (ex.isSourceAvailable()) {
				throw ex;
			}
			StackTraceElement element = PlayException.getInterestingStrackTraceElement(ex);
			if (element != null) {
				throw new TemplateNotFoundException(templateName, Play.classes.getApplicationClass(element.getClassName()), element.getLineNumber());
			} else {
				throw ex;
			}
		}
	}

	/**
	 * Chargement du template ODT à partir du fichier physique nommé "templateName". Ce fichier est cherché dans les
	 * répertoires des templates de Play.
	 *
	 * @param templateName Nom du fichier servant de template pour la génération.
	 * @return template chargé par la librairie JodReports.
	 * @throws TemplateNotFoundException Fichier template ODT non trouvé.
	 */
	private static DocumentTemplate odtTemplateLoad(String templateName) {
		for (VirtualFile vf : Play.templatesPath) {
			if (vf == null) {
				continue;
			}
			VirtualFile tf = vf.child(templateName);
			if (tf.exists()) {
				try {
					return DOCUMENT_TEMPLATE_FACTORY.getTemplate(tf.getRealFile());
				} catch (IOException ioe) {
					throw new UnexpectedException(ioe);
				}
			}
		}
		// Template non trouvé.
		throw new TemplateNotFoundException(templateName);
	}
}