package org.aksw.sante.entity;

import java.io.Serializable;

public abstract interface Object extends Serializable {
	public abstract boolean isLiteral();
	public abstract void accept(ObjectVisitor visitor);
	public abstract String getURI();
}