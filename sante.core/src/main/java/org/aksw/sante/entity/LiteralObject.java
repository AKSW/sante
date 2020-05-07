package org.aksw.sante.entity;

public class LiteralObject extends Literal implements Object {

	/**
	 * 
	 */
	private static final long serialVersionUID = -6158977320769897923L;

	public LiteralObject(String literal, String lang) {
		super(literal, lang);
	}
	
	public LiteralObject(Literal l) {
		this.setType(l.getType());
		this.setLang(l.getLang());
		this.setValue(l.getValue());
	}

	@Override
	public boolean isLiteral() {
		return true;
	}

	@Override
	public void accept(ObjectVisitor visitor) {
		visitor.visit(this);
	}
}
