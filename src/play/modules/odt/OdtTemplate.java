package play.modules.odt;

import net.sf.jooreports.templates.DocumentTemplate;
import net.sf.jooreports.templates.DocumentTemplateFactory;
import play.Play;
import play.exceptions.PlayException;
import play.exceptions.TemplateCompilationException;
import play.exceptions.TemplateNotFoundException;
import play.templates.Template;
import play.vfs.VirtualFile;

import java.io.IOException;
import java.util.Map;

/**
 * Template ODT étendant les templates Play.
 *
 * @author Benoît Courtine.
 * @since 24/10/2010.
 */
public class OdtTemplate extends Template {

	/**
	 * Factory permettant de créer des templates au format "JodReports" à partir des fichiers physiques.
	 */
	private static final DocumentTemplateFactory DOCUMENT_TEMPLATE_FACTORY = new DocumentTemplateFactory();

	/** Nom du fichier qui sera généré. */
	private String fileName;

	/** Fichier virtuel de localisation du template ODT. */
	private VirtualFile templateFile;

	/** Template ODT au format JodReports pour la fusion. */
	private DocumentTemplate documentTemplate;

	/**
	 * Création du template en trouvant le nom du fichier à partir de son nom.
	 *
	 * @param templateName Nom du template.
	 */
	public OdtTemplate(String templateName) {
		this(getTemplateFile(templateName));
	}

	/**
	 * Création du template à partir du fichier du template ODT.
	 *
	 * @param templateFile Fichier du template ODT.
	 */
	public OdtTemplate(VirtualFile templateFile) {
		this.templateFile = templateFile;
		this.fileName = this.templateFile.getName();
	}

	/**
	 * Méthode de résolution du fichier template ODT à partir du nom du template.
	 *
	 * @param templateName Nom du template ODT.
	 * @return Fichier du template ODT.
	 */
	private static VirtualFile getTemplateFile(String templateName) {
		for (VirtualFile vf : Play.templatesPath) {
			if (vf == null) {
				continue;
			}
			VirtualFile tf = vf.child(templateName);
			if (tf.exists()) {
				return tf;
			}
		}
		// Template non trouvé.
		throw new TemplateNotFoundException(templateName);
	}

	/**
	 * Compilation du template ODT en {@link net.sf.jooreports.templates.DocumentTemplate} prêt à être fusionné.
	 */
	@Override
	public void compile() {
		try {
			this.documentTemplate = DOCUMENT_TEMPLATE_FACTORY.getTemplate(this.templateFile.getRealFile());
		} catch (IOException ioe) {
			StackTraceElement element = PlayException.getInterestingStrackTraceElement(ioe);
			throw new TemplateCompilationException(this, element.getLineNumber(), ioe.getMessage());
		}
	}

	/**
	 * Création du rendu ODT par fusion du template JodReports et des données.
	 *
	 * @param data Données à fusionner dans le template JodReports.
	 * @return Cette méthode ne renvoie rien, le rendu étant obtenu par la levée de {@link play.modules.odt.RenderOdt}.
	 * @throws RenderOdt Rendu ODT.
	 */
	@Override
	public String render(Map<String, Object> data) {
		compile();
		throw new RenderOdt(this.fileName, this.documentTemplate, data);
	}
}
