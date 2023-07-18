package org.aksw.sante.smile.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.sante.smile.core.SmileParams;
import org.sante.lucene.FuzzyQuerySuggester;
import org.sante.lucene.ResultSet;
import org.sante.lucene.Suggestion;
import org.springframework.beans.factory.annotation.Autowired;

@WebServlet(name = "SuggestServlet", urlPatterns = {"/API/suggest"})
public class SuggestServlet extends AbstractServlet {

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
	private static final String SCORE_PARAM = "score";
	private static final String HIGHLIGHT_PARAM = "highlight";
	private static final String SUGGESTION_PARAM = "suggestion";
	private static final String SEPARATOR_PARAM = ",";
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
		boolean highlight = contentList.contains(HIGHLIGHT_PARAM);
		PrintWriter out = response.getWriter();
        File index = new File(smileParams.indexPath);
        try (FuzzyQuerySuggester suggester = new FuzzyQuerySuggester(index);) {
			ResultSet<Suggestion> rs = suggester.suggest(queryParam, 
					offsetParam,
					limitParam,
					prefixList,
					classList,
					true,
					score,
					false);
			out.println("{");
			out.println("\"params\":" + asJson(params) + SEPARATOR_PARAM);
			out.println("\"result\":[");
			while(rs.hasNext()) {
				params.clear();
				Suggestion suggestion = rs.next();
				if(highlight) {
					params.put(SUGGESTION_PARAM, rs.highlight(suggestion.getValue()));
				} else {
					params.put(SUGGESTION_PARAM, suggestion.getValue());
				}
				if(score) {
					params.put(SCORE_PARAM, Float.toString(rs.score()));
				}
				out.print(asJson(params));
				if(rs.hasNext()) {
					out.println(SEPARATOR_PARAM);
				}
			}
			out.println("]}");
		} catch (Exception e) {
			out.print(e.getMessage());
		}
    }
}