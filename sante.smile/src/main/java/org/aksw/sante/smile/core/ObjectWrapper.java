package org.aksw.sante.smile.core;

import java.io.Serializable;

import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.Object;
import org.aksw.sante.entity.ObjectVisitor;
import org.aksw.sante.entity.URIObject;

public class ObjectWrapper extends ResourceWrapper implements Serializable {

	/**
	 * 
	 */
	private static final long serialVersionUID = -3833778580743854124L;
	private Boolean isLiteral = false;
	private Boolean isLangTagged = false;

	public ObjectWrapper(Object object, String defaultLabel, String[] langs) {
		super(object.getURI());
		isLiteral = object.isLiteral();
		object.accept(new ObjectVisitor() {
			@Override
			public void visit(URIObject uriObject) {
				String label = uriObject.getLabel(langs, defaultLabel);
				addLabels(uriObject.getLabels());
				setLabel(label);
				setSnippet(label);
			}
			@Override
			public void visit(Literal literalObject) {
				addLabel(literalObject);
				setLabel(literalObject.getValue());
				String lang = literalObject.getLang();
				isLangTagged = (lang != null && !lang.isEmpty());
				setLang(literalObject.getLang());
				setSnippet(literalObject.getValue());
			}
		});
	}

	public boolean isLiteral() {
		return isLiteral;
	}
	
	public boolean isLangTagged() {
		return isLangTagged;
	}
}
