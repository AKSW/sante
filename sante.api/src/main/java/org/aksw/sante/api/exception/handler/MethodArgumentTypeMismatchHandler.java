package org.aksw.sante.api.exception.handler;

import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.response.ApiErrorResponse;
import org.aksw.sante.api.exception.error.TypeMismatchError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

/**
 * Handler for a MethodArgumentTypeMismatchException.
 *
 * @see MethodArgumentTypeMismatchException
 */
@RestControllerAdvice
public class MethodArgumentTypeMismatchHandler {

	/**
	 * Handles MethodArgumentTypeMismatchException and returns an appropriate error response.
	 *
	 * @param exception MethodArgumentTypeMismatchException that is to be handled
	 * @return          an appropriate error response
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentTypeMismatchException.class)
	public ApiErrorResponse handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException exception) {
		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.BAD_REQUEST)
						.addMessage("Incorrect type (type mismatch) of a parameter")
						.addError(
								new TypeMismatchError(
										exception.getMessage(), exception.getParameter().getParameterName()
								)
						)
						.create()
		);
	}
}
