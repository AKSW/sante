package org.aksw.sante.api.exception.error;

import lombok.Getter;

/**
 * An error that indicates an issue with an improperly formatted JSON string.
 *
 * @see ReportableError
 */
@Getter
public class JsonParseError extends ReportableError {

	/**
	 * Detailed message that gives information about the formatting issue.
	 */
	private final String detailedMessage;

	/**
	 * Constructs a JsonParseError and adds the required information.
	 *
	 * @param message         information about the issue
	 * @param detailedMessage detailed information about the improperly formatted JSON string
	 */
	public JsonParseError(String message, String detailedMessage) {
		super(message);
		this.detailedMessage = detailedMessage;
	}
}
