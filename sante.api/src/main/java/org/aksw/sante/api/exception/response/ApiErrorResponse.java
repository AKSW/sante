package org.aksw.sante.api.exception.response;

import lombok.Getter;
import org.aksw.sante.api.exception.error.ApiError;

/**
 * Response object for an ApiError.
 */
@Getter
public class ApiErrorResponse {
	/**
	 * ApiError that is to be returned with the response.
	 */
	private final ApiError apiError;

	/**
	 * Constructs the ApiErrorResponse.
	 *
	 * @param apiError ApiError that is to be returned with the response
	 */
	public ApiErrorResponse(ApiError apiError) {
		this.apiError = apiError;
	}
}
