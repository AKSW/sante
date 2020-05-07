package org.aksw.sante.smile.core;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.aksw.sante.entity.Literal;
import org.apache.lucene.analysis.en.EnglishAnalyzer;

import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class StructuredSnippetGeneartor {
	
	private Configuration cfg = null;
	
	public StructuredSnippetGeneartor() throws IOException, URISyntaxException {
		cfg = new Configuration(Configuration.VERSION_2_3_29);
		ClassTemplateLoader ctl = new ClassTemplateLoader(getClass(), "/org/aksw/sante/templates");
        cfg.setTemplateLoader(ctl);
        // Recommended settings for new projects:
        cfg.setDefaultEncoding("UTF-8");
        cfg.setTemplateExceptionHandler(TemplateExceptionHandler.RETHROW_HANDLER);
        cfg.setLogTemplateExceptions(false);
        cfg.setWrapUncheckedExceptions(true);
        cfg.setFallbackOnNullLoopVariable(false);
	}
	
	public String generate(String query, AbstractEntityWrapper e) throws TemplateNotFoundException, 
		MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<Object, Object> root = new HashMap<Object, Object>();
        Template temp;
        
        if(e.getURI().equals("https://github.com/AKSW/sante")) {
        	root.put("entity", e);
	        temp = cfg.getTemplate("about.html");
        }
        else if(query == null || query.isEmpty()) {
			 /* Get the template (uses cache internally) */
			root.put("entity", e);
	        temp = cfg.getTemplate("default-template.html");
		} else {
			EnglishAnalyzer analyzer = new EnglishAnalyzer();
			EntityAnnotator entityAnnotator = new EntityAnnotator();
			System.out.println();
			Set<PropertyObjectAnnotation> annotations = entityAnnotator.annotate(e, query, analyzer);
			AbstractEntityWrapper entitySnippet = e.clone();
			entitySnippet.getProperties().clear();
			for(PropertyObjectAnnotation annotation: annotations) {
				PropertyWrapper pWrapper = annotation.getProperty();
				entitySnippet.getProperties().add(pWrapper);
				Set<Literal> propertyLabels = pWrapper.getLabels();
				for(Literal propertyLabel : propertyLabels) {
					if(toObject(annotation.getResourceLabels()).contains((Object)propertyLabel)) {
						pWrapper.setSnippet("<b>" + propertyLabel.getValue() + "</b>");
					}
				}
				List<ObjectWrapper> objectWrapper = pWrapper.getObjects();
				for(ObjectWrapper objWrapper : objectWrapper) {
					Set<Literal> objectLabels = objWrapper.getLabels();
					for(Literal objectLabel : objectLabels) {
						if(toObject(annotation.getResourceLabels()).contains((Object)objectLabel)) {
							objWrapper.setSnippet("<b>" + objectLabel.getValue() + "</b>");
						}
					}
				}
			}
			root.put("entity", entitySnippet);
			temp = cfg.getTemplate("property-object-template.html");
		}
		try(StringWriter out = new StringWriter();) {
        	temp.process(root, out);
        	String outputString = out.toString();
        	int start = outputString.indexOf("<body>");
        	int end = outputString.lastIndexOf("</body>");
        	return outputString.substring(start + 6, end);
        }
	}
	
	public Set<Object> toObject(Collection<Literal> literals) {
		Set<Object> objectSet = new HashSet<Object>(literals);
		return objectSet;
	}
}
