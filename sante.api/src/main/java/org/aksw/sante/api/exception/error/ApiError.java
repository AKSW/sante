package org.aksw.sante.api.exception.error;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Getter;
import org.springframework.http.HttpStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;

@AllArgsConstructor
@Getter
public class ApiError {
	private HttpStatus httpStatus;
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "dd-MM-yyyy hh:mm:ss")
	private LocalDateTime timestamp;
	private String message;
	private Collection<ReportableError> errors;

	private ApiError() {
		this.timestamp = LocalDateTime.now();
	}

	private ApiError(ErrorResponseBuilder builder) {
		this();
		this.httpStatus = builder.httpStatus;
		this.message = builder.message;
		this.errors = builder.errors;
	}

	public static class ErrorResponseBuilder {
		private HttpStatus httpStatus;
		private String message;
		private final Collection<ReportableError> errors;

		private ErrorResponseBuilder() {
			this.errors = new ArrayList<>();
		}

		private ErrorResponseBuilder(HttpStatus httpStatus) {
			this();
			this.httpStatus = httpStatus;
		}

		public static ErrorResponseBuilder newErrorResponse(HttpStatus httpStatus) {
			return new ErrorResponseBuilder(httpStatus);
		}

		public ErrorResponseBuilder addMessage(String message) {
			this.message = message;
			return this;
		}

		public ErrorResponseBuilder addError(ReportableError error) {
			this.errors.add(error);
			return this;
		}

		public ErrorResponseBuilder addErrors(Collection<ReportableError> errors) {
			this.errors.addAll(errors);
			return this;
		}

		public ApiError create() {
			return new ApiError(this);
		}
	}
}
