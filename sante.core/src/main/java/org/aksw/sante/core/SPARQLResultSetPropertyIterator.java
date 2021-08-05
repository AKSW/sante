package org.aksw.sante.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;

public class SPARQLResultSetPropertyIterator extends AbstractResultSetIterator<Property> {
	
	private String query;
	private String endpoint;
	private int limit;
	
	public SPARQLResultSetPropertyIterator(String endpoint, 
			String query,
			String whereClause,
			int limit,
			Set<String> labelingProperties) {
		super(endpoint, whereClause, labelingProperties);
		this.endpoint = endpoint;
		this.query = query;
		this.limit = limit;
	}
	
	public void accept(ResultSetVisitor<Property> visitor) {
		int offset = 0;
		boolean loop = true;
		while (loop) {
			String sparqlQuery = query + " LIMIT " + limit + " OFFSET " + offset;
			Query query = QueryFactory.create(sparqlQuery);
			try(QueryEngineHTTP qexec = new QueryEngineHTTP(endpoint, query)) {
				ResultSet rs = qexec.execSelect();
				while (rs != null && rs.hasNext()) {
					QuerySolution qs = rs.next();
					RDFNode pNode = qs.get("p");
					String pURI = pNode.toString();
					java.util.List<Literal> pLabels = new ArrayList<Literal>();
					List<org.apache.jena.rdf.model.Literal> pLabelLiterals = getLabels(pNode);
					for(org.apache.jena.rdf.model.Literal pLabelLiteral: pLabelLiterals) {
						String pLabel = pLabelLiteral.getString();
						String pLang = pLabelLiteral.getLanguage();						
						Literal propertyLabel = new Literal(pLabel, pLang);
						pLabels.add(propertyLabel);
					}
					RDFNode oNode = qs.get("o");
					String oURI = null;
					String oLabel = null;
					String oLang = null;
					Property p = null;
					if(oNode.isLiteral()) {
						org.apache.jena.rdf.model.Literal oLabelLiteral = oNode.asLiteral();
						oLabel = oLabelLiteral.getString();
						oLang = oLabelLiteral.getLanguage();
						LiteralObject literalObject = new LiteralObject(oLabel, oLang);
						p = new Property(pURI, pLabels, literalObject);
					} else {
						oURI = oNode.toString();
						java.util.List<Literal> oLabels = new ArrayList<Literal>();
						List<org.apache.jena.rdf.model.Literal>  oLabelLiterals = getLabels(oNode);
						for(org.apache.jena.rdf.model.Literal oLabelLiteral : oLabelLiterals) {
							oLabel = oLabelLiteral.getString();
							oLang = oLabelLiteral.getLanguage();
							Literal literal = new Literal(oLabel, oLang);
							oLabels.add(literal);
						}
						p = new Property(pURI, pLabels, oURI, oLabels);
					}	
					visitor.visit(p); // visit property
				}
				int rowNumber = rs.getRowNumber();
				loop = rowNumber == limit;
				offset += limit;
			}
		}
	}
}
