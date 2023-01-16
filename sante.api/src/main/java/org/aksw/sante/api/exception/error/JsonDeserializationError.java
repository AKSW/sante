package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * An error that indicates an issue with the deserialization of a JSON string into an object.
 *
 * @see ReportableError
 */
@Getter
public class JsonDeserializationError extends ReportableError {

	/**
	 * Field that can not be properly deserialized.
	 */
	private final String field;

	/**
	 * Constructs a JsonDeserializationError and adds the required information.
	 *
	 * @param message   detailed information about the issue
	 * @param jsonField field in the JSON string that has an issue
	 */
	public JsonDeserializationError(String message, String jsonField) {
		super(message);
		this.field = jsonField;
	}
}
