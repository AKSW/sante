package org.aksw.sante.api;

import io.swagger.v3.oas.annotations.Operation;
import org.json.JSONObject;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;

@RestController
public class SearchController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -434687490902537643L;
	
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

	@Operation(
			method = "GET",
			summary = "search",
			tags = {"search"}
	)
	@GetMapping(path = "/API/search")
    protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(name = QUERY_PARAM, defaultValue = "friend") String queryParam,
			@RequestParam(name = OFFSET_PARAM, defaultValue = "0") Integer offsetParam,
			@RequestParam(name = LIMIT_PARAM, defaultValue = "10") Integer limitParam,
			@RequestParam(name = PREFIX_PARAM, required = false) String prefixParam,
			@RequestParam(name = CLASS_PARAM, required = false) String classParam,
			@RequestParam(name = CONTENT_PARAM, required = false) String contentParam
	) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
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
        File index = new File(SmileParams.getInstance().indexPath);
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
	protected Set<String> parseSet(String array) {
		if(array != null) {
			return new HashSet<String>(parseList(array));
		}
		return new HashSet<String>();
	}	
	protected String asJson(Map<String, Object> params) {
		JSONObject object = new JSONObject();
		for(Map.Entry<String, Object> entry: params.entrySet()) {
			if(entry.getValue() != null) {
				object.put(entry.getKey(), entry.getValue());
			}
		}
		return object.toString();
	}	
	protected List<String> parseList(String array) {
		if(array != null) {
			return Arrays.asList(array.split(","));
		}
		return new ArrayList<String>();
	}

}