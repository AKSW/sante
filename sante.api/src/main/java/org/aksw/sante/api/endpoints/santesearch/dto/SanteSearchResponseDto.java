package org.aksw.sante.api.endpoints.santesearch.dto;

import lombok.Getter;
import lombok.Setter;
import org.aksw.sante.api.wrapper.Query;

import java.util.ArrayList;
import java.util.List;

/**
 * DTO that represents a response to a search performed from a SANTé query.
 */
@Getter
public class SanteSearchResponseDto {

	/**
	 * Parameters of the query.
	 */
	@Setter
	private Query params;

	/**
	 * The list of candidates representing the result.
	 */
	private final List<SanteSearchCandidate> result;

	/**
	 * Constructs a SanteSearchResponseDto.
	 */
	public SanteSearchResponseDto() {
		this.result = new ArrayList<>();
	}

	/**
	 * Adds a search candidate to the list of results.
	 *
	 * @param candidate a SanteSearchCandidate that represents a single result
	 */
	public void addCandidateToResult(SanteSearchCandidate candidate) {
		this.result.add(candidate);
	}
}
