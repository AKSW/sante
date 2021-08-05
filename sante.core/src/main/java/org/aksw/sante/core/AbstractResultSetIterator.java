package org.aksw.sante.core;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.QuerySolution;
import org.apache.jena.query.ResultSet;
import org.apache.jena.rdf.model.RDFNode;
import org.apache.jena.sparql.engine.http.QueryEngineHTTP;
import org.apache.log4j.Logger;

public abstract class AbstractResultSetIterator <T> {
	
	private static Logger logger = Logger.getLogger(AbstractResultSetIterator.class);

	private final static String LABEL_QUERY = "PREFIX  rdfs:  <http://www.w3.org/2000/01/rdf-schema#>  " + 
			"Select distinct ?label where ";
	
	private final static String INSTANCE_QUERY = "PREFIX  rdfs:  <http://www.w3.org/2000/01/rdf-schema#>  " + 
			"Select ?p ?o where ";
	
	private final static String DEFAULT_LABELING_PROPERTY = "http://www.w3.org/2000/01/rdf-schema#label";

	private String endpoint;
	private String whereClause;
	private Set<String> labelingProperties;
	private String pFilter = null;
	
	public AbstractResultSetIterator(String endpoint, 
			String whereClause, 
			Set<String> labelingProperties) {
		this.endpoint = endpoint;
		this.whereClause = whereClause;
		this.labelingProperties = labelingProperties;
		if(labelingProperties == null) {
			pFilter = "?p = <" + DEFAULT_LABELING_PROPERTY + ">";
		} else {
			String[] conjunction = new String [labelingProperties.size()];
			int i = 0;
			for(String labelingProperty : labelingProperties) {
				conjunction[i] = "?p = <" +labelingProperty + ">";
				i++;
			}
			pFilter = StringUtils.join(conjunction, " && ");	
		}
	}
	
	public List<org.apache.jena.rdf.model.Literal> getLabels(RDFNode node) {
		String queryVariable = getQueryVariale(node);
		String parsedWhereClause = whereClause.replace("?s", queryVariable);
		parsedWhereClause = parsedWhereClause.replace("?p", "rdfs:label");
		parsedWhereClause = parsedWhereClause.replace("?o", "?label");
		String parsedQuery = LABEL_QUERY + parsedWhereClause;
		parsedQuery = parsedQuery.replace("?pFilter", pFilter);
		List<org.apache.jena.rdf.model.Literal> literals = new ArrayList<org.apache.jena.rdf.model.Literal>();
		try {
			Query query = QueryFactory.create(parsedQuery);
			try(QueryEngineHTTP qexec = new QueryEngineHTTP(endpoint, query)) {
				ResultSet rs = qexec.execSelect();
				while (rs != null && rs.hasNext()) {
					QuerySolution qs = rs.next();
					org.apache.jena.rdf.model.Literal literal = qs.getLiteral("label");
					literals.add(literal);
				}
			}
		} catch(Exception e) {
			logger.warn("Error retrieving label: " + node.toString(), e);
		}
		return literals;
	}
	
	public Entity getInstance(RDFNode node) {
		String uri = node.toString();
		String uriQueryVariable = getQueryVariale(node);
		Entity e = new Entity(uri, labelingProperties);
		String parsedWhereClause = whereClause.replace("?s", uriQueryVariable);
		String parsedQuery = INSTANCE_QUERY + parsedWhereClause;
		Query query = QueryFactory.create(parsedQuery);
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
				e.addProperty(p);
			}
		}
		return e;
	}

	protected String getQueryVariale(RDFNode node) {
		String nodeURI = node.toString();
		nodeURI = "<" + nodeURI + ">";
		return nodeURI;
	}
	
	public abstract void accept(ResultSetVisitor<T> visitor);
	
}
