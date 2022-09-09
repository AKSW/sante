package org.aksw.sante.api;

import io.swagger.v3.oas.annotations.Operation;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.HashSet;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.apache.lucene.search.Sort;
import org.json.JSONArray;
import org.json.JSONObject;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.sante.lucene.SortFieldRelecanceFactory;

@RestController
public class ReconcileSearchController {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5127732043154933725L;
	
	private static final String RDFS_LABEL = "http://www.w3.org/2000/01/rdf-schema#label";
	
	private static final String SEARCH_PARAM = "search";
	private static final String QUERY_PARAM = "query";
	private static final String LIMIT_PARAM = "limit";
	private static final String CURSOR_PARAM = "cursor";
	private static final String TYPE_PARAM = "type";
	private static final String ID_PARAM = "id";
	private static final String SCORE_PARAM = "score";
	private static final String NAME_PARAM = "name";
	private static final String RESULT_PARAM = "result";
	private static final Set<String> labelingProperties = new HashSet<String>();
	{
		labelingProperties.add(RDFS_LABEL);
	}
	private static final String CONTENT_TYPE = "application/json";

	@Operation(
			method = "GET",
			summary = "Reconcile",
			tags = {"reconcile"}
	)
	@GetMapping(path = "/API/reconcile")
	protected void doGet(
			HttpServletRequest request,
			HttpServletResponse response,
			@RequestParam(name = SEARCH_PARAM, defaultValue = "{ \"q\": { \"query\": \"friend\" }}") String searchParam,
			@RequestParam(name = LIMIT_PARAM, defaultValue = "10") Integer limit,
			@RequestParam(name = CURSOR_PARAM, defaultValue = "0") Integer offset
	) throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		if (searchParam != null) {
			JSONObject searchParams = new JSONObject(searchParam);
			Set<String> queries = searchParams.keySet();
			File index = new File(SmileParams.getInstance().indexPath);
			Path indexPath = index.toPath();
			try(IndexReader reader = SearchEngine.newReader(indexPath);) {			
				SearchEngine searchEngine = new SearchEngine(reader, labelingProperties);
				JSONObject jsonResponse = new JSONObject();
				PrintWriter out = response.getWriter();
				for (String queryId : queries) {
					JSONObject queryObject = searchParams.getJSONObject(queryId);
					JSONObject queryResult = new JSONObject();
					String query = queryObject.getString(QUERY_PARAM);
					Set<String> classes = new HashSet<String>();
					if (queryObject.has(TYPE_PARAM)) {
						String type = queryObject.getString(TYPE_PARAM);
						classes.add(type);
					}
					try {
						SortFieldRelecanceFactory sortFieldRelevanceFactory = new SortFieldRelecanceFactory();
						Sort sort = sortFieldRelevanceFactory.create(reader);
						ResultSet<Entity> rs = searchEngine.search(query, offset, limit, null, classes, null, true, sort);
						JSONArray resultArray = new JSONArray();
						while (rs.hasNext()) {
							JSONObject resultEntry = new JSONObject();
							Entity entity = rs.next();
							resultEntry.put(ID_PARAM, entity.getURI());
							resultEntry.put(NAME_PARAM, entity.getLabel());
							resultEntry.put(SCORE_PARAM, Float.toString(rs.score()));
							resultArray.put(resultEntry);
						}
						queryResult.put(RESULT_PARAM, resultArray);
						jsonResponse.put(queryId, queryResult);
					} catch (Exception e) {
						out.print(e.getMessage());
					}
				}
				out.print(jsonResponse.toString());
			}
		}
	}
}
