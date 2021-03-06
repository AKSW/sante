package org.aksw.sante.main;

import org.aksw.sante.entity.Entity;
import org.aksw.sante.entity.Literal;
import org.aksw.sante.entity.LiteralObject;
import org.aksw.sante.entity.Property;
import org.aksw.sante.entity.URIObject;
import org.sante.lucene.EntityVisitor;

public class SystemOutTriplesResult implements EntityVisitor {
	@Override
	public boolean visit(Entity entity, float score) {
		for(Property p: entity.getAllProperties()) {
			for(Literal pLabel : p.getLabels()) {
				if(p.getObject().isLiteral()) {
					LiteralObject obj = (LiteralObject)p.getObject();
					String oLabel = obj.getValue();
					System.out.println("<" + entity.getURI() + ">\t\"" 
							+ pLabel.getValue() 
							+ "\"<" + p.getURI() 
							+ ">\t\"" + oLabel + "\"");
				} else {
					URIObject obj = (URIObject)p.getObject();
					for(Literal oLabel : obj.getLabels()) {
						System.out.println("<" + entity.getURI() + ">\t\"" 
								+ pLabel.getValue() 
								+ "\"<" + p.getURI() 
								+ ">\t\"" + oLabel.getValue() + 
								"\"<" + obj.getURI() + ">");
					}
				}									
			}
		}
		return true;
	}
}
