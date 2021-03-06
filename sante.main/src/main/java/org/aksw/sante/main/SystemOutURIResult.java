package org.aksw.sante.main;

import org.aksw.sante.entity.Entity;
import org.sante.lucene.EntityVisitor;

public class SystemOutURIResult implements EntityVisitor {
	@Override
	public boolean visit(Entity entity, float score) {
		System.out.println(entity.getURI());
		return true;
	}
}
