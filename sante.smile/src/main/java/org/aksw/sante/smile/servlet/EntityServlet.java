package org.aksw.sante.smile.servlet;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintWriter;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

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
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.SearchEngine;

@WebServlet(name = "EntityServlet", urlPatterns = {"/API/entity"})
public class EntityServlet extends AbstractServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -434687490902537643L;
	
	private static final String URI_PARAM = "uri";
	private static final String FORMAT_PARAM = "format";
	private static final String CONTENT_TYPE = "text/plain";
	private static final String DEFAULT_FORMAT = "N-TRIPLE";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		String uriParam = request.getParameter(URI_PARAM);
		String formatParam = request.getParameter(FORMAT_PARAM);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(URI_PARAM, uriParam);
		params.put(FORMAT_PARAM, formatParam);
		PrintWriter out = response.getWriter();
		File index = new File(SmileParams.getInstance().indexPath);
		Path indexPath = index.toPath();
        try (IndexReader reader = SearchEngine.newReader(indexPath);) {
        	SearchEngine searchEngine = new SearchEngine(reader);
        	Entity entity = searchEngine.getEntity(uriParam);
			String triples = entity.asTriples();
			Model model = ModelFactory.createDefaultModel();
			if(formatParam == null) {
				out.println(triples);
			} else {
				RDFReader tripleReader = model.getReader(DEFAULT_FORMAT);
				InputStream is = new ByteArrayInputStream(triples.getBytes(StandardCharsets.UTF_8));
				tripleReader.read(model, is, uriParam);
				RDFWriter rdfWriter = model.getWriter(formatParam);
				rdfWriter.write(model, out, "");
			}
		} catch (Exception e) {
			out.print(e.getMessage());
		}
    }
}