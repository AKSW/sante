package org.aksw.sante.core;

public interface ResultSetVisitor<T> {
	public boolean visit(T element);
}
