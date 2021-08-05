package org.aksw.sante.core;

import java.util.Set;

import org.aksw.sante.entity.Entity;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class SPARQLResultSetEntityIterator extends AbstractResultSetIterator<Entity>{
	
	private int limit;
	private String query;
	private String endpoint;
	
	public SPARQLResultSetEntityIterator(String endpoint, 
			String query, 
			String whereClause,
			int limit,
			Set<String> labelingProperties) {
		super(endpoint, whereClause, labelingProperties);
		this.limit = limit;
		this.endpoint = endpoint;
		this.query = query;
	}
	
	public void accept(ResultSetVisitor<Entity> visitor) {
		int offset = 0;
		boolean loop = true;
		while (loop) {
			String sparqlQuery = query + " LIMIT " + limit + " OFFSET " + offset;
			Query query = QueryFactory.create(sparqlQuery);
			try(QueryEngineHTTP qexec = new QueryEngineHTTP(endpoint, query)) {
				ResultSet rs = qexec.execSelect();
				while (rs != null && rs.hasNext()) {
					QuerySolution qs = rs.next();
					RDFNode sResource = qs.get("s");
					Entity e = getInstance(sResource);
					if(!visitor.visit(e)) {
						return;
					}
				}
				int rowNumber = rs.getRowNumber();
				loop = rowNumber == limit;
				offset += limit;
			}
		}
	}
}
