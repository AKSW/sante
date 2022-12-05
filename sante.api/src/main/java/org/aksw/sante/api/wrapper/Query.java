package org.aksw.sante.api.wrapper;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import org.sante.lucene.Filter;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import java.lang.reflect.Field;
import java.text.MessageFormat;
import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * Query that contains all possible query parameters as well as some reflection logic.
 */
@SuppressWarnings("unused")
@Getter
// no setters needed since Jackson is capable of doing reflective deserialization
public class Query {

	/**
	 * The actual query string.
	 */
	@NotEmpty
	protected String q;

	/**
	 * The offset.
	 */
	@Min(0)
	protected int offset;

	/**
	 * The limit of the amount of results.
	 */
	@Min(1)
	protected int limit;

	/**
	 * A set of strings representing prefixes that the query results are supposed to be reduced by.
	 */
	protected Set<String> prefixes;

	/**
	 * A set of strings representing classes that the query results are supposed to be reduced by.
	 */
	protected Set<String> classes;

	/**
	 * A set of filters that the query results are supposed to be filtered by.
	 */
	protected Set<Filter> filters;

	/**
	 * A set of strings that give additional context about the query and what a result should take into consideration.
	 */
	protected Set<String> content;

	/**
	 * Returns a set that of strings representing the mandatory fields of a query.
	 * They are mandatory because of the way SANTé is currently implemented.
	 *
	 * @return a set of strings
	 */
	@JsonIgnore
	// method instead of constant in order to keep the reflection logic intact
	protected Set<String> getMandatoryFields() {
		return Set.of("q", "offset", "limit");
	}

	/**
	 * Returns a set of strings representing the names of the fields of the current instance that are not-null.
	 *
	 * @return a set of strings
	 */
	@JsonIgnore
	protected Set<String> getDefinedFields() {
		return Arrays
				.stream(this.getClass().getDeclaredFields()).filter(field -> {
					try {
						if (this.getMandatoryFields().contains(field.getName())) {
							return false;
						}
						return field.get(this) != null;
					} catch (IllegalAccessException e) {
						throw new RuntimeException(e);
					}
				})
				.map(Field::getName).collect(Collectors.toSet());
	}

	/**
	 * Checks if a given simple array of strings, representing names of fields,
	 * contains a field name that is not declared for a Query.
	 *
	 * @param fieldNames simple array of strings representing possible field names
	 * @return true or false depending on if the array of fieldNames contains undeclared field names
	 */
	@JsonIgnore
	protected boolean arrayContainsUndeclaredField(String... fieldNames) {
		return Arrays.stream(fieldNames)
				.anyMatch(fieldName -> !Arrays.stream(this.getClass().getDeclaredFields())
						.map(Field::getName)
						.collect(Collectors.toList())
						.contains(fieldName)
				);
	}

	/**
	 * Checks if the content field is set.
	 *
	 * @return true if it is set (i.e. non-null), false otherwise
	 */
	@JsonIgnore
	public boolean hasContent() {
		return this.content != null;
	}

	/**
	 * Checks that, given a simple array of field names, the current instance only has exactly those fields set,
	 * i.e. exactly those specified fields, specified by their name, are non-null.
	 *
	 * @param specifiedFieldNames a simple array of specified field names corresponding to fields
	 *                            that should be non-null
	 * @return true if only the specified fields are set, false otherwise
	 * @throws NoSuchFieldException if any issue arises
	 */
	@JsonIgnore
	public boolean onlySpecifiedOptionalFieldsAreSet(String... specifiedFieldNames) throws NoSuchFieldException {
		if (this.arrayContainsUndeclaredField(specifiedFieldNames)) {
			throw new NoSuchFieldException(MessageFormat.format(
					"Array {0} contains fields that are not allowed. Allowed fields are {1}",
					Arrays.toString(specifiedFieldNames),
					Arrays.stream(this.getClass().getDeclaredFields())
							.map(Field::getName).collect(Collectors.toList()).toString()
			));
		}
		return this.getDefinedFields().equals(Arrays.stream(specifiedFieldNames).collect(Collectors.toSet()));
	}
}
