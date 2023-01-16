package org.aksw.sante.api.exception;

import lombok.Getter;

/**
 * Exception that can be thrown to indicate any issue that arises while performing a search.
 * This exception is used as a wrapper to be able to handle these issues
 * and report them to the user through the API.
 */
@Getter
public class SearchSuggestException extends Exception {

	public SearchSuggestException() {
		super();
	}
	/**
	 * Constructs a SearchException.
	 *
	 * @param cause throwable that caused the SearchException
	 */
	public SearchSuggestException(Throwable cause) {
		super(cause);
	}
}
