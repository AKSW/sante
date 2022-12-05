package org.aksw.sante.api.exception.handler;

import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.error.ApiErrorResponse;
import org.aksw.sante.api.exception.error.RuntimeError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for a RuntimeException.
 *
 * @see RuntimeException
 */
@RestControllerAdvice
public class RuntimeExceptionHandler {

	/**
	 * Handles a RuntimeExceptions and returns an appropriate error response.
	 *
	 * @param exception RuntimeException
	 * @return an appropriate error response
	 */
	@ResponseStatus(HttpStatus.INTERNAL_SERVER_ERROR)
	@ExceptionHandler(RuntimeException.class)
	public ApiErrorResponse handleRuntimeException(RuntimeException exception) {
		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR)
						.addMessage("There was an issue while processing the request")
						.addError(new RuntimeError(exception.getCause().getLocalizedMessage()))
						.create()
		);
	}
}
