package org.aksw.sante.api;

import java.io.File;
import java.io.IOException;
import java.io.PrintWriter;
import java.nio.file.Path;
import java.util.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.aksw.sante.entity.URIObject;
import org.aksw.sante.smile.core.SmileParams;
import org.apache.lucene.index.IndexReader;
import org.json.JSONObject;
import org.sante.lucene.ResultSet;
import org.sante.lucene.SearchEngine;
import org.springframework.boot.WebApplicationType;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

@RestController
public class DBpediaLookupController {
    @Operation(
            summary = "Retrieve entities",
            tags = {"dbpedia-lookup"},
            responses = {
                    @ApiResponse(responseCode = "400", description = "Invalid input")
            }
    )
    @GetMapping(
            path = "/API/dbpedia-lookup",
            produces = MediaType.APPLICATION_JSON_VALUE)
    @ResponseBody
    protected void lookupDbPedia(
            HttpServletResponse response,
            @RequestParam(name = ParameterConstants.QUERY_PARAM, required = false) String queryParam,
            @RequestParam(name = ParameterConstants.LIMIT_PARAM, defaultValue = "10") Integer limitParam,
            @RequestParam(name = ParameterConstants.CLASS_PARAM, required = false) String classParam
    ) throws IOException {
        response.setContentType(ParameterConstants.CONTENT_TYPE);
        Map<String, Object> params = new HashMap<String, Object>();
        params.put(ParameterConstants.QUERY_PARAM, queryParam);
        params.put(ParameterConstants.LIMIT_PARAM, limitParam);
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
                params.put(ParameterConstants.URI_PARAM, new String [] {entity.getURI()});
                params.put(ParameterConstants.SCORE_PARAM, new String [] {Float.toString(rs.score())});
                Set<Literal> literalLabels = entity.getLabels();
                List<String> stringlabels = new ArrayList<String>();
                for(Literal literal : literalLabels) {
                    stringlabels.add(literal.getValue());
                }
                params.put(ParameterConstants.LABEL_PARAM, stringlabels);
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
                    params.put(ParameterConstants.TYPE_NAME_PARAM, stringTypes);
                    params.put(ParameterConstants.TYPE_PARAM, uriTypes);
                }
                List<Property> commentProperties = entity.getProperties("http://www.w3.org/1999/02/22-rdf-syntax-ns#comment");
                if(commentProperties != null) {
                    List<String> comments = new ArrayList<String>();
                    for(Property commentProperty : commentProperties) {
                        comments.add(((LiteralObject)commentProperty.getObject()).getValue());
                    }
                    params.put(ParameterConstants.COMMENT_PARAM, comments);
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

    private List<String> parseList(String array) {
        if(array != null) {
            return Arrays.asList(array.split(","));
        }
        return new ArrayList<String>();
    }

    private Set<String> parseSet(String array) {
        if(array != null) {
            return new HashSet<String>(parseList(array));
        }
        return new HashSet<String>();
    }

    private Integer parseInteger(String integer, Integer defaultValue) {
        if(integer == null) {
            return defaultValue;
        }
        return Integer.parseInt(integer);
    }

    private String asJson(Map<String, Object> params) {
        JSONObject object = new JSONObject();
        for(Map.Entry<String, Object> entry: params.entrySet()) {
            if(entry.getValue() != null) {
                object.put(entry.getKey(), entry.getValue());
            }
        }
        return object.toString();
    }
}
