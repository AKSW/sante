package org.aksw.sante.smile.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFReader;
import org.apache.jena.rdf.model.RDFWriter;
import org.apache.log4j.Logger;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.SearchEngine;

@WebServlet(name = "ResourceServlet", urlPatterns = {"/API/resource"})
public class ResourceServlet extends AbstractServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -434687490902537643L;
	
	private final static Logger logger = Logger.getLogger(ResourceServlet.class);
	
	private static final String URI_PARAM = "uri";
	private static final String FORMAT_PARAM = "format";
	private static final String CONTENT_TYPE = "text/plain";
	private static final String DEFAULT_READ_FORMAT = "N-Triples";
	private static final String DEFAULT_WRITE_FORMAT = "JSON-LD";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		String uriParam = request.getParameter(URI_PARAM);
		String formatParam = request.getParameter(FORMAT_PARAM);
		PrintWriter out = response.getWriter();
		File index = new File(SmileParams.getInstance().indexPath);
		Path indexPath = index.toPath();
        try (IndexReader reader = SearchEngine.newReader(indexPath);) {
        	SearchEngine searchEngine = new SearchEngine(reader);
        	Entity entity = searchEngine.getEntity(uriParam);
        	if(entity == null) {
        		response.sendError(400);
        		out.print("Entity not found: Make sure the given URI is properly encoded.");
        		return;
        	}
			String triples = entity.asTriples();
			Model model = ModelFactory.createDefaultModel();
			RDFReader tripleReader = model.getReader(DEFAULT_READ_FORMAT);
			InputStream is = new ByteArrayInputStream(triples.getBytes(StandardCharsets.UTF_8));
			tripleReader.read(model, is, uriParam);
			RDFWriter rdfWriter = null;
			if(formatParam == null || formatParam.isEmpty()) {
				rdfWriter = model.getWriter(DEFAULT_WRITE_FORMAT);
			} else {
				rdfWriter = model.getWriter(formatParam);
			}
			rdfWriter.write(model, out, "");
		} catch (Exception e) {
			logger.error("Error retrieving resource: " + uriParam, e);
			response.sendError(500);
			out.print("Ups, something went wrong.");
		}
    }
}