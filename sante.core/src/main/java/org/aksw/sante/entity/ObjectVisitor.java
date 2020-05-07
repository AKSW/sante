package org.aksw.sante.entity;

public interface ObjectVisitor {
	public void visit(Literal literalObject);
	public void visit(URIObject uriObject);
}
