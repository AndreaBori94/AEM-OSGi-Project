package it.cgm.osgi.srv.mypost;

import it.cgm.osgi.srv.mypost.query.Query;
import it.cgm.osgi.srv.mypost.result.Result;

import java.io.IOException;
import java.rmi.ServerException;

import javax.jcr.Session;

import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.scr.annotations.sling.SlingServlet;
import org.apache.sling.api.SlingHttpServletRequest;
import org.apache.sling.api.SlingHttpServletResponse;
import org.apache.sling.api.resource.ResourceResolver;
import org.apache.sling.api.resource.ResourceResolverFactory;
import org.apache.sling.api.servlets.SlingAllMethodsServlet;

/**
 * Servizio per creazione di contenuti all'interno della JavaContentRepository
 * è possibile usare un ajax con javascript per consumare questo servizio
 * @author CGM2
 * @version 1.0.0
 * @since 1.0.0
 */
@SlingServlet(paths = "/bin/JCRServiceServlet", methods = "POST", metatype = true)
public class JCRServiceServlet extends SlingAllMethodsServlet {
	/**
	 * Seriale univoco
	 */
	private static final long serialVersionUID = 2598426539166789515L;

	/**
	 * Risorsa ottenuta esternamente da AEM
	 */
	@Reference
	private ResourceResolverFactory resolverFactory;

	/**
	 * Sessione di AEM del JCR utilizzata per modificare e salvare il contenuto
	 */
	private Session session;
	/**
	 * Il resurce resolver utilizzato per ottenere la sessione
	 */
	private ResourceResolver resourceResolver = null;

	
	/**
	 * Ereditato da SlingAllMethodsServlet richiama doPost
	 * @see SlingAllMethodsServlet
	 */
	@Override
	protected void doGet(SlingHttpServletRequest request,
			SlingHttpServletResponse response)
			throws javax.servlet.ServletException, IOException {
		doPost(request, response);
	};

	/**
	 * Ereditato da SlingAllMethodsServlet consuma il servizio
	 */
	@Override
	protected void doPost(SlingHttpServletRequest request,
			SlingHttpServletResponse response) throws ServerException,
			IOException {
		int out = 0;
		try {

			Query q = new Query(request);
			RepositoryManager RM = new RepositoryManager(q, resolverFactory,
					session, resourceResolver);
			Result r = RM.doAction();
			response.getWriter().write(r.render());
		} catch (Exception e) {
			response.getWriter().write(e.getMessage() + " at node " + out);
		}
	}
}