package org.aksw.sante.api.exception.handler;

import com.fasterxml.jackson.databind.exc.UnrecognizedPropertyException;
import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.error.ApiErrorResponse;
import org.aksw.sante.api.exception.error.JsonDeserializationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for a UnrecognisedPropertyException.
 *
 * @see UnrecognizedPropertyException
 */
@RestControllerAdvice
public class UnrecognisedPropertyExceptionHandler {

	/**
	 * Handles UnrecognisedPropertyException and returns an appropriate error response.
	 *
	 * @param exception UnrecognisedPropertyException
	 * @return an appropriate error response
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(UnrecognizedPropertyException.class)
	public ApiErrorResponse handleUnrecognisedPropertyException(UnrecognizedPropertyException exception) {
		String message = "This field is not allowed. Allowed fields are "
				+ exception.getKnownPropertyIds().toString();

		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.BAD_REQUEST)
						.addMessage("There are issues with the request body")
						.addError(
								new JsonDeserializationError(
										message, exception.getPropertyName()
								)
						)
						.create()
		);
	}
}
