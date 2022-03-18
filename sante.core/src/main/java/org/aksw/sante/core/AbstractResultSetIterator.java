package org.aksw.sante.core;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.apache.commons.lang3.StringUtils;
import org.apache.jena.query.ParameterizedSparqlString;
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
	private HTTPClientFactory clientFactory = null;
	
	public AbstractResultSetIterator(String endpoint, 
			String whereClause, 
			Set<String> labelingProperties) {
		this(endpoint, whereClause, labelingProperties, null);
	}
	
	public AbstractResultSetIterator(String endpoint, 
			String whereClause, 
			Set<String> labelingProperties,
			HTTPClientFactory clientFactory) {
		this.endpoint = endpoint;
		this.whereClause = whereClause;
		this.labelingProperties = labelingProperties;
		this.clientFactory = clientFactory;
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
	
	protected QueryEngineHTTP getQueryEngine(String endpoint, Query query, HTTPClientFactory clientFactory) {
		QueryEngineHTTP qexec = null;
		if(clientFactory == null) {
			qexec = new QueryEngineHTTP(endpoint, query);
		} else {
			qexec = new QueryEngineHTTP(endpoint, query, clientFactory.create());
		}
		return qexec;
	}
	
	public long getSize(String queryString) {
		String parsedQuery = "SELECT ((COUNT(*)) AS ?count) { " + queryString + " }";
		try {
			Query query = QueryFactory.create(parsedQuery);
			try(QueryEngineHTTP qexec = getQueryEngine(endpoint, query, clientFactory)) {
				ResultSet rs = qexec.execSelect();
				while (rs != null && rs.hasNext()) {
					QuerySolution qs = rs.next();
					int size = qs.getLiteral("count").getInt();
					return size;
				}
			}
		} catch(Exception e) {
			logger.warn("Error retrieving query size.", e);
		}
		return 0;
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
			try(QueryEngineHTTP qexec = getQueryEngine(endpoint, query, clientFactory)) {
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
		try(QueryEngineHTTP qexec = getQueryEngine(endpoint, query, clientFactory)) {
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
	
	public Entity getCachedInstance(RDFNode node) {
		String uri = node.toString();
		String uriQueryVariable = getQueryVariale(node);
		Entity e = new Entity(uri, labelingProperties);
		String parsedWhereClause = whereClause.replace("?s", uriQueryVariable);
		String parsedQuery = INSTANCE_QUERY + parsedWhereClause;
		Query query = QueryFactory.create(parsedQuery);
		List<PropObj> propObjs = new ArrayList<PropObj>();
		Set<String> resources = new HashSet<String>();
		try(QueryEngineHTTP qexec = getQueryEngine(endpoint, query, clientFactory)) { // get all prop / objects
			ResultSet rs = qexec.execSelect();
			while (rs != null && rs.hasNext()) {
				QuerySolution qs = rs.next();
				RDFNode pNode = qs.get("p");
				RDFNode oNode = qs.get("o");
				PropObj propObj = new PropObj();
				propObj.obj = oNode;
				propObj.prop = pNode;
				propObjs.add(propObj);
				resources.add(pNode.toString());
				if(!oNode.isLiteral()) {
					resources.add(oNode.toString());
				}
			}
		}
		Map<String, List<org.apache.jena.rdf.model.Literal>> resourceLabels = getResourceLabelsQuery(resources);
		for(PropObj propObj : propObjs) {
			RDFNode pNode = propObj.prop;
			String pURI = pNode.toString();
			java.util.List<Literal> pLabels = new ArrayList<Literal>();
			if(resourceLabels.containsKey(pURI)) {
				List<org.apache.jena.rdf.model.Literal> pLabelLiterals = resourceLabels.get(pURI);
				for(org.apache.jena.rdf.model.Literal pLabelLiteral: pLabelLiterals) {
					String pLabel = pLabelLiteral.getString();
					String pLang = pLabelLiteral.getLanguage();
					Literal propertyLabel = new Literal(pLabel, pLang);
					pLabels.add(propertyLabel);
				}
			}
			RDFNode oNode = propObj.obj;
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
				if(resourceLabels.containsKey(oURI)) {
					List<org.apache.jena.rdf.model.Literal>  oLabelLiterals = resourceLabels.get(oURI);
					for(org.apache.jena.rdf.model.Literal oLabelLiteral : oLabelLiterals) {
						oLabel = oLabelLiteral.getString();
						oLang = oLabelLiteral.getLanguage();
						Literal literal = new Literal(oLabel, oLang);
						oLabels.add(literal);
					}
				}
				p = new Property(pURI, pLabels, oURI, oLabels);
			}				
			e.addProperty(p);
		}
		return e;
	}

	private Map<String, List<org.apache.jena.rdf.model.Literal>> getResourceLabelsQuery(Set<String> resources) {
		Map<String, List<org.apache.jena.rdf.model.Literal>> labels = new HashMap<String, List<org.apache.jena.rdf.model.Literal>>();
		if(resources.size() == 0) {
			return labels;
		}
		String parsedQuery  = "Select ?s ?l where { ?s <http://www.w3.org/2000/01/rdf-schema#label> ?l filter(";
		for(int i = 0; i < resources.size(); i++) {
			parsedQuery += "?s = ?resource" + i;
			if(i < resources.size() - 1) {
				parsedQuery += " || ";
			}
		}
		parsedQuery += ")}";
		ParameterizedSparqlString pss = new ParameterizedSparqlString(parsedQuery);
		int j = 0;
		for(String resource : resources) {
			pss.setIri("resource" + j, resource);
			j++;
		}
		Query query = null;
		try {
			query = QueryFactory.create(pss.toString());
			try(QueryEngineHTTP qexec = getQueryEngine(endpoint, query, clientFactory)) { // get all prop / objects
				ResultSet rs = qexec.execSelect();
				while (rs != null && rs.hasNext()) {
					QuerySolution qs = rs.next();
					RDFNode sNode = qs.get("s");
					String sURI = sNode.toString();
					org.apache.jena.rdf.model.Literal literal = qs.getLiteral("l");
					List<org.apache.jena.rdf.model.Literal> literals = null;
					if(labels.containsKey(sURI)) {
						literals = labels.get(sURI);
					} else {
						literals = new ArrayList<org.apache.jena.rdf.model.Literal>();
						labels.put(sURI, literals);
					}
					literals.add(literal);
				}
			}
		} catch (Exception e) {
			logger.error("Error retrieving label list.", e);
		}
		return labels;
	}

	protected String getQueryVariale(RDFNode node) {
		String nodeURL = node.toString();
		org.apache.commons.validator.routines.UrlValidator urlValidator = new org.apache.commons.validator.routines.UrlValidator();
		if(urlValidator.isValid(nodeURL)) {
			URL url;
			try {
				url = new URL(nodeURL);
				nodeURL = url.toString();
			} catch (MalformedURLException e) {
				logger.error("Error intantiating URL: " + nodeURL, e);
			}
		}
		return "<" + nodeURL + ">";
	}
	
	public abstract void accept(ResultSetVisitor<T> visitor);
	
	private class PropObj {
		RDFNode prop;
		RDFNode obj;
	}
}
