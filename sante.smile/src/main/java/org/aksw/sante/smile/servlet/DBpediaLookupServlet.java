package org.aksw.sante.smile.servlet;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.aksw.sante.entity.URIObject;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;

import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@WebServlet(name = "DBpediaLookupServlet", urlPatterns = {"/API/dbpedia-lookup"})
public class DBpediaLookupServlet extends AbstractServlet {

    /**
	 * 
	 */
	private static final long serialVersionUID = -434687490902537643L;
	
	private static final String QUERY_PARAM = "QueryString";
	private static final String LIMIT_PARAM = "MaxHits";
	private static final String CLASS_PARAM = "QueryClass";
	private static final String CONTENT_TYPE = "application/json";

	private static final String URI_PARAM = "resource";
	private static final String SCORE_PARAM = "score";
	private static final String LABEL_PARAM = "label";
	private static final String TYPE_NAME_PARAM = "typeName";
	private static final String TYPE_PARAM = "type";
	private static final String COMMENT_PARAM = "comment";
	
	@ApiOperation(httpMethod = "GET", value = "Retrieve entities based on a given query string (when given).", nickname = "dbpedia-lookup")
    @ApiResponses({@ApiResponse(code = 400, message = "Invalid input")})
    @ApiImplicitParams({
            @ApiImplicitParam(name = "QueryString", value = "Query", required = false, dataType = "string", paramType =
                    "query"),
            @ApiImplicitParam(name = "MaxHits", value = "Maximum number of entries", required = false, dataType = "integer", paramType =
                    "query"),
            @ApiImplicitParam(name = "QueryClass", value = "Entity types", required = true, dataType = "string", paramType
                    = "query")})
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
		response.setContentType(CONTENT_TYPE);
		String queryParam = request.getParameter(QUERY_PARAM);
		Integer limitParam = parseInteger(request.getParameter(LIMIT_PARAM), 10);
		String classParam = request.getParameter(CLASS_PARAM);
		Map<String, Object> params = new HashMap<String, Object>();
		params.put(QUERY_PARAM, queryParam);
		params.put(LIMIT_PARAM, limitParam);
		Set<String> classList = parseSet(classParam);
        PrintWriter out = response.getWriter();
        File index = new File(SmileParams.getInstance().indexPath);
        Path indexPath = index.toPath();
        try (IndexReader reader = SearchEngine.newReader(indexPath);) {
			SearchEngine searchEngine = new SearchEngine(reader);
			ResultSet<Entity> rs = searchEngine.search(
				queryParam,
				0,
				limitParam,
				null,
				classList,
				null,
				true);
			out.println("{");
			out.println("\"params\":" + asJson(params) + ",");
			out.println("\"docs\":[");
			while(rs.hasNext()) {
				params.clear();
				Entity entity = rs.next();
				params.put(URI_PARAM, new String [] {entity.getURI()});
				params.put(SCORE_PARAM, new String [] {Float.toString(rs.score())});
				Set<Literal> literalLabels = entity.getLabels();
				List<String> stringlabels = new ArrayList<String>();
				for(Literal literal : literalLabels) {
					stringlabels.add(literal.getValue());
				}
				params.put(LABEL_PARAM, stringlabels);
				List<Property> typeProperties = entity.getProperties("http://www.w3.org/2000/01/rdf-schema#type");
				if(typeProperties != null) {
					List<String> stringTypes = new ArrayList<String>();
					List<String> uriTypes = new ArrayList<String>();
					for(Property typeProperty : typeProperties) {
						Set<Literal> typeLabels = ((URIObject) typeProperty.getObject()).getLabels();
						uriTypes.add(typeProperty.getObject().getURI());
						for(Literal literal : typeLabels) {
							stringTypes.add(literal.getValue());
						}
					}
					params.put(TYPE_NAME_PARAM, stringTypes);
					params.put(TYPE_PARAM, uriTypes);
				}
				List<Property> commentProperties = entity.getProperties("http://www.w3.org/1999/02/22-rdf-syntax-ns#comment");
				if(commentProperties != null) {
					List<String> comments = new ArrayList<String>();
					for(Property commentProperty : commentProperties) {
						comments.add(((LiteralObject)commentProperty.getObject()).getValue());
					}
					params.put(COMMENT_PARAM, comments);
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