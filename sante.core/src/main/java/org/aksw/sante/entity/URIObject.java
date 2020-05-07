package org.aksw.sante.entity;

import java.util.List;

public class URIObject extends Resource implements Object {
	/**
	 * 
	 */
	private static final long serialVersionUID = -3745887777931258340L;
	
	public URIObject(String uri, List<Literal> labels) {
		super(uri, labels);
	}
	
	@Override
	public boolean isLiteral() {
		return false;
	}

	@Override
	public void accept(ObjectVisitor visitor) {
		visitor.visit(this);
	}
}
