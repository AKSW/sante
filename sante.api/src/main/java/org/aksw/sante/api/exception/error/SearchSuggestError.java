package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * An error that indicates an issue that came up during search or suggestion.
 *
 * @see ReportableError
 */
@Getter
public class SearchSuggestError extends ReportableError {

	/**
	 * Constructs a SearchError and adds the required information.
	 *
	 * @param message information about the issue
	 */
	public SearchSuggestError(String message) {
		super(message);
	}
}
