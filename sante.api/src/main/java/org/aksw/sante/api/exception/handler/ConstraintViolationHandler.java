package org.aksw.sante.api.exception.handler;

import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.response.ApiErrorResponse;
import org.aksw.sante.api.exception.error.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Handler for a ConstraintViolationException.
 *
 * @see ConstraintViolationException
 */
@RestControllerAdvice
public class ConstraintViolationHandler {

	/**
	 * Handles a ConstraintViolationException and returns an appropriate error response.
	 *
	 * @param exception ConstraintViolationException that is to be handled
	 * @return an appropriate error response
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@org.springframework.web.bind.annotation.ExceptionHandler(ConstraintViolationException.class)
	public ApiErrorResponse handleConstraintViolationException(ConstraintViolationException exception) {
		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.BAD_REQUEST)
						.addMessage("Some constraints were violated")
						.addErrors(
								exception.getConstraintViolations().stream().map(violation ->
										new ValidationError(
												violation.getMessage(), violation.getPropertyPath().toString()
										)
								).collect(Collectors.toList())
						)
						.create()
		);
	}
}
