package org.aksw.sante.core;

import java.util.ArrayList;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.apache.jena.query.ARQ;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.jena.sys.JenaSystem;

public class SPARQLResultSetEntityIterator {
	
	private String query;
	private String endpoint;
	private String graph;
	private int limit;
	
	public SPARQLResultSetEntityIterator(String endpoint, String query, String graph, int limit) {
		this.endpoint = endpoint;
		this.query = query;
		this.graph = graph;
		this.limit = limit;
		JenaSystem.init(); // Jena needs to be initialized before being used
		ARQ.init();
	}
	
	public void accept(ResultSetVisitor<Entity> visitor) {
		String lastSURI = null;
		int offset = 0;
		boolean loop = true;
		Entity e = null;
		while (loop) {
			String sparqlQuery = query + " order by ?s LIMIT " + limit + " OFFSET " + offset + "";
			Query query = QueryFactory.create(sparqlQuery, graph);
			try(QueryEngineHTTP qexec = new QueryEngineHTTP(endpoint, query)) {
				ResultSet rs = qexec.execSelect();
				while (rs != null && rs.hasNext()) {
					QuerySolution qs = rs.next();
					String sURI = qs.get("s").toString();
					String pURI = qs.get("p").toString();
					java.util.List<Literal> pLabels = new ArrayList<Literal>();
					org.apache.jena.rdf.model.Literal pLabelLiteral = qs.getLiteral("plabel");
					if(pLabelLiteral != null) {
						String pLabel = pLabelLiteral.getString();
						String plang = pLabelLiteral.getLanguage();						
						Literal propertyLabel = new Literal(pLabel, plang);
						pLabels.add(propertyLabel);
					}
					RDFNode oNode = qs.get("o");
					String oURI = null;
					String oLabel = null;
					String olang = null;
					org.apache.jena.rdf.model.Literal oLabelLiteral = null;
					Property p = null;
					if(oNode.isLiteral()) {
						oLabelLiteral = oNode.asLiteral();
						oLabel = oLabelLiteral.getString();
						olang = oLabelLiteral.getLanguage();
						LiteralObject literalObject = new LiteralObject(oLabel, olang);
						p = new Property(pURI, pLabels, literalObject);
					} else {
						oURI = oNode.asResource().getURI();
						java.util.List<Literal> oLabels = new ArrayList<Literal>();
						oLabelLiteral = qs.getLiteral("olabel");
						if(oLabelLiteral != null) {
							oLabel = oLabelLiteral.getString();
							olang = oLabelLiteral.getLanguage();
							Literal literal = new Literal(oLabel, olang);
							oLabels.add(literal);
						}
						p = new Property(pURI, pLabels, oURI, oLabels);
					}
					if(!sURI.equals(lastSURI)) {
						if(e != null) {
							if(!visitor.visit(e)) {
								return;
							}
						}
						e = new Entity(sURI);
						lastSURI = sURI;
					}				
					e.addProperty(p);
				}
				int rowNumber = rs.getRowNumber();
				loop = rowNumber == limit;
				offset += limit;
			}
		}
		if(e != null) {
			visitor.visit(e); // last entry
		}
	}
}
