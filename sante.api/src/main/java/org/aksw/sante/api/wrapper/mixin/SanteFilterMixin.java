package org.aksw.sante.api.wrapper.mixin;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonSubTypes.Type;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.fasterxml.jackson.annotation.JsonTypeInfo.Id;
import org.sante.lucene.PatternFilter;
import org.sante.lucene.TriplePatternFilter;
import org.sante.lucene.URIFilter;

// polymorphic deserialization could also be achieved via DEDUCTION
// see https://www.baeldung.com/jackson-deduction-based-polymorphism

/**
 * Jackson mixin that exists only for annotation purposes.
 */
@JsonTypeInfo(
		use = Id.NAME,
		property = "type")
@JsonSubTypes(
		{
				@Type(
						value = TriplePatternFilter.class,
						name = "triplePatternFilter"
				),
				@Type(
						value = PatternFilter.class,
						name = "patternFilter"
				),
				@Type(
						value = URIFilter.class,
						name = "uriFilter"
				)
		}
)
public interface SanteFilterMixin {
}
