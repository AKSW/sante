package org.aksw.sante.smile.core;

import java.io.IOException;
import java.io.StringWriter;
import java.net.URISyntaxException;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.aksw.sante.entity.Literal;
import org.aksw.sante.smile.view.AbstractSuggestionView;

import freemarker.cache.ClassTemplateLoader;
import freemarker.core.ParseException;
import freemarker.template.Configuration;
import freemarker.template.MalformedTemplateNameException;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.TemplateExceptionHandler;
import freemarker.template.TemplateNotFoundException;

public class ItemAutoCompleteSnippetGeneartor {
	
	private Configuration cfg = null;
	
	public ItemAutoCompleteSnippetGeneartor() throws IOException, URISyntaxException {
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
	
	public String generate(AbstractSuggestionView suggestionItem) throws TemplateNotFoundException, 
		MalformedTemplateNameException, ParseException, IOException, TemplateException {
		Map<Object, Object> root = new HashMap<Object, Object>();
        Template temp;
		root.put("suggestion", suggestionItem);
	    temp = cfg.getTemplate("autocomplete-item.html");
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
