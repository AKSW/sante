package org.aksw.sante.api.exception.handler;

import org.aksw.sante.api.exception.SearchSuggestException;
import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.response.ApiErrorResponse;
import org.aksw.sante.api.exception.error.SearchSuggestError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for a SearchSuggestException.
 *
 * @see SearchSuggestException
 */
@RestControllerAdvice
public class SearchSuggestExceptionHandler {

	/**
	 * Handles a SearchSuggestException and returns an appropriate error response.
	 *
	 * @param exception SearchSuggestException
	 * @return an appropriate error response
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(SearchSuggestException.class)
	public ApiErrorResponse handleSearchSuggestException(SearchSuggestException exception) {
		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR)
						.addMessage("There was an issue while processing the request")
						.addError(new SearchSuggestError(exception.getCause().getLocalizedMessage()))
						.create()
		);
	}
}
