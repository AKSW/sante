package org.aksw.sante.api.wrapper.ruleengine.rules;

import org.aksw.sante.api.wrapper.Query;

import java.io.Serializable;

/**
 * Abstract QueryRule that exists to implement shared methods only once.
 *
 * @param <T> type that represents a single result of an executed query rule
 */
public abstract class AbstractQueryRule<T extends Serializable> implements QueryRule<T> {

	/**
	 * Returns a simple array of strings that indicate which optional fields of a query should be set (exclusively).
	 *
	 * @return a simple array of strings
	 */
	protected abstract String[] getSpecifiedOptionalFields();

	/**
	 * Checks if the rule applies for the given query.
	 *
	 * @param query a query with parameters
	 * @return true or false depending on if the rule actually applies
	 */
	@Override
	public boolean queryRuleApplies(Query query) {
		try {
			return query.onlySpecifiedOptionalFieldsAreSet(this.getSpecifiedOptionalFields());
		} catch (NoSuchFieldException exception) {
			throw new RuntimeException(exception);
		}
	}
}
