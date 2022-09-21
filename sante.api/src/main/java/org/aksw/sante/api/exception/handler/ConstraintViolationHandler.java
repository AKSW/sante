package org.aksw.sante.api.exception.handler;

import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.error.ApiErrorResponse;
import org.aksw.sante.api.exception.error.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ConstraintViolationException;
import java.util.stream.Collectors;

/**
 * Handler for ConstraintViolationException.
 *
 * @see ConstraintViolationException
 */
@RestControllerAdvice
public class ConstraintViolationHandler {

	/**
	 * Handle ConstraintViolationException and return an appropriate error response.
	 *
	 * @param exception ConstraintViolationException that is to be handled
	 * @return          an appropriate error response
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
												violation.getPropertyPath().toString(),
												violation.getMessage()
										)
								).collect(Collectors.toList())
						)
						.create()
		);
	}
}
