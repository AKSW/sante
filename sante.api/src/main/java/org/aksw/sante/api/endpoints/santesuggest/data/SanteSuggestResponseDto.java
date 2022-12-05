package org.aksw.sante.api.endpoints.santesuggest.data;

import lombok.Getter;
import lombok.Setter;
import org.aksw.sante.api.wrapper.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO that represents a response of a suggestion performed on a SANTé query.
 */
@Getter
public class SanteSuggestResponseDto {

	/**
	 * Parameters of the query.
	 */
	@Setter
	private Query params;

	/**
	 * The list of candidates representing the result.
	 */
	private final List<SanteSuggestCandidate> result;

	/**
	 * Constructs a SanteSuggestResponseDto.
	 */
	public SanteSuggestResponseDto() {
		this.result = new ArrayList<>();
	}

	/**
	 * Adds a suggestion candidate to the list of result.
	 *
	 * @param santeSuggestCandidate a SanteSuggestCandidate that represents a single result
	 */
	public void addCandidateToResult(SanteSuggestCandidate santeSuggestCandidate) {
		this.result.add(santeSuggestCandidate);
	}
}
