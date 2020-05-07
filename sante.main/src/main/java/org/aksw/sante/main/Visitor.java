package org.aksw.sante.main;

public interface Visitor <T> {
	public boolean visit(T object);
}
