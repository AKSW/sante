package org.aksw.sante.smile.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet(name = "SearchServlet", urlPatterns = {"/API/search"})
public class SearchServlet extends AbstractServlet {

	
	/**
	 * 
	 */
	private static final long serialVersionUID = -434687490902537643L;
	
	@Autowired
    private SmileParams smileParams;
	
	private static final String QUERY_PARAM = "q";
	private static final String OFFSET_PARAM = "offset";
	private static final String LIMIT_PARAM = "limit";
	private static final String PREFIX_PARAM = "prefix";
	private static final String CLASS_PARAM = "class";
	private static final String CONTENT_PARAM = "content";
	private static final String URI_PARAM = "uri";
	private static final String SCORE_PARAM = "score";
	private static final String INSTANCE_PARAM = "instance";
	
	private static final String CONTENT_TYPE = "application/json";

	@Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		String queryParam = request.getParameter(QUERY_PARAM);
		Integer offsetParam = parseInteger(request.getParameter(OFFSET_PARAM), 0);
		Integer limitParam = parseInteger(request.getParameter(LIMIT_PARAM), 10);
		String prefixParam = request.getParameter(PREFIX_PARAM);
		String classParam = request.getParameter(CLASS_PARAM);
		String contentParam = request.getParameter(CONTENT_PARAM);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(QUERY_PARAM, queryParam);
		params.put(PREFIX_PARAM, prefixParam);
		params.put(CLASS_PARAM, classParam);
		params.put(CONTENT_PARAM, contentParam);
		params.put(OFFSET_PARAM, offsetParam);
		params.put(LIMIT_PARAM, limitParam);	
		Set<String> prefixList = parseSet(prefixParam);
		Set<String> classList = parseSet(classParam);
		Set<String> contentList = parseSet(contentParam);
		boolean score = contentList.contains(SCORE_PARAM);
		 PrintWriter out = response.getWriter();
        File index = new File(smileParams.indexPath);
		Path indexPath = index.toPath();
		try (IndexReader reader = SearchEngine.newReader(indexPath);) {
			SearchEngine searchEngine = new SearchEngine(reader);
			ResultSet<Entity> rs = searchEngine.search(
				queryParam,
				offsetParam,
				limitParam,
				prefixList,
				classList,
				null,
				score);
			out.println("{");
			out.println("\"params\":" + asJson(params) + ",");
			out.println("\"result\":[");
			while(rs.hasNext()) {
				params.clear();
				Entity entity = rs.next();
				params.put(URI_PARAM, entity.getURI());
				if (contentList.contains(INSTANCE_PARAM)) {
					params.put(INSTANCE_PARAM, entity.asTriples());
				}
				if(score) {
					params.put(SCORE_PARAM, Float.toString(rs.score()));
				}
				out.print(asJson(params));
				if(rs.hasNext()) {
					out.println(",");
				}
			}
			out.println("]}");
		} catch (Exception e) {
			out.print(e.getMessage());
		}
    }
}