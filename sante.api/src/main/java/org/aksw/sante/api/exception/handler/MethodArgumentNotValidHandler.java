package org.aksw.sante.api.exception.handler;

import org.aksw.sante.api.exception.error.ApiError;
import org.aksw.sante.api.exception.error.ApiErrorResponse;
import org.aksw.sante.api.exception.error.ValidationError;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.stream.Collectors;

/**
 * Handler for a MethodArgumentNotValidException.
 *
 * @see MethodArgumentNotValidException
 */
@RestControllerAdvice
public class MethodArgumentNotValidHandler {


	/**
	 * Handles a MethodArgumentNotValidException and returns an appropriate error response.
	 *
	 * @param exception MethodArgumentNotValidException that is to be handled
	 * @return an appropriate error response
	 */
	@ResponseStatus(HttpStatus.BAD_REQUEST)
	@org.springframework.web.bind.annotation.ExceptionHandler(MethodArgumentNotValidException.class)
	public ApiErrorResponse handleMethodArgumentNotValidException(MethodArgumentNotValidException exception) {
		return new ApiErrorResponse(
				ApiError.ErrorResponseBuilder
						.newErrorResponse(HttpStatus.BAD_REQUEST)
						.addMessage("Some arguments are not valid")
						.addErrors(
								exception.getFieldErrors().stream().map(error ->
										new ValidationError(
												error.getDefaultMessage(), error.getField()
										)
								).collect(Collectors.toList())
						)
						.create()
		);
	}
}
