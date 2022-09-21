package org.aksw.sante.api.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

/**
 * An error object that can be returned in a REST-response in case of an error.
 * Can be used to give information about any error that may arise.
 */
@Getter
public class ApiError {

	/**
	 * HTTP status that the error will be returned with.
	 */
	private HttpStatus httpStatus;

	/**
	 * Timestamp of the moment the error occurred.
	 */
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private final LocalDateTime timestamp;

	/**
	 * General message that describes the error.
	 * Should not contain any specific details.
	 */
	private String message;

	/**
	 * Collection of all specific errors that occurred.
	 */
	private Collection<ReportableError> errors;

	/**
	 * Constructs an ApiError and sets a timestamp to the current time.
	 * Can not be called externally.
	 */
	private ApiError() {
		this.timestamp = LocalDateTime.now();
	}

	/**
	 * Constructs an ApiError using an appropriate builder.
	 *
	 * @param builder the builder that is used to construct the ApiError
	 */
	private ApiError(ErrorResponseBuilder builder) {
		this();
		this.httpStatus = builder.httpStatus;
		this.message = builder.message;
		this.errors = builder.errors;
	}

	/**
	 * Builder to construct ApiError objects dynamically.
	 */
	public static class ErrorResponseBuilder {
		/**
		 * HTTP status that the error will be returned with.
		 */
		private HttpStatus httpStatus;

		/**
		 * General message that describes the error.
		 * Should not contain any specific details.
		 */
		private String message;

		/**
		 * Collection of all specific errors that occurred.
		 */
		private final Collection<ReportableError> errors;

		/**
		 * Constructs the builder and initialises the collection of specific errors.
		 * Can not be called externally.
		 */
		private ErrorResponseBuilder() {
			this.errors = new ArrayList<>();
		}

		/**
		 * Constructs the builder with a given HTTP status.
		 * Can not be called externally.
		 */
		private ErrorResponseBuilder(HttpStatus httpStatus) {
			this();
			this.httpStatus = httpStatus;
		}

		/**
		 * Wraps the constructor in static method that initiates the building process of an ApiError.
		 *
		 * @param   httpStatus HTTP status that is to be returned with the ApiError
		 * @return  a new ErrorResponseBuilder
		 */
		public static ErrorResponseBuilder newErrorResponse(HttpStatus httpStatus) {
			return new ErrorResponseBuilder(httpStatus);
		}

		/**
		 * Adds a message to the ApiError.
		 *
		 * @param message   error message
		 * @return          this ErrorResponseBuilder
		 */
		public ErrorResponseBuilder addMessage(String message) {
			this.message = message;
			return this;
		}

		/**
		 * Adds a specific error to the list of specific errors of the ApiError.
		 *
		 * @param error specific error
		 * @return      this ErrorResponseBuilder
		 */
		public ErrorResponseBuilder addError(ReportableError error) {
			this.errors.add(error);
			return this;
		}

		/**
		 * Adds multiple specific errors to the list of specific errors of the ApiError.
		 *
		 * @param errors    collection of specific errors
		 * @return          this ErrorResponseBuilder
		 */
		public ErrorResponseBuilder addErrors(Collection<ReportableError> errors) {
			this.errors.addAll(errors);
			return this;
		}

		/**
		 * Creates the ApiError once the building process is done.
		 *
		 * @return a new ApiError
		 */
		public ApiError create() {
			return new ApiError(this);
		}
	}
}
