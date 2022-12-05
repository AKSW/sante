package org.aksw.sante.api.exception.handler;

import com.fasterxml.jackson.core.JsonParseException;
import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.error.ApiErrorResponse;
import org.aksw.sante.api.exception.error.JsonParseError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

/**
 * Handler for a UnrecognisedPropertyException.
 *
 * @see JsonParseException
 */
@RestControllerAdvice
public class JsonParseExceptionHandler {

	/**
	 * Handles a JsonParseException and returns an appropriate error response.
	 *
	 * @param exception JsonParseException
	 * @return an appropriate error response
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@ExceptionHandler(JsonParseException.class)
	public ApiErrorResponse handleJsonParseException(JsonParseException exception) {
		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.BAD_REQUEST)
						.addMessage("There are issues with the request body")
						.addError(
								new JsonParseError(
										"JSON is malformed and could not be parsed",
										exception.getLocalizedMessage()
								)
						)
						.create()
		);
	}
}
