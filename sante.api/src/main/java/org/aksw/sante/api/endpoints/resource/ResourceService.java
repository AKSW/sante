package org.aksw.sante.api.endpoints.resource;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.aksw.sante.entity.Entity;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.RDFReaderI;
import org.apache.jena.rdf.model.RDFWriterI;
import org.sante.lucene.SearchEngine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;

import java.io.InputStream;
import java.io.ByteArrayInputStream;
import java.io.StringWriter;
import java.nio.charset.StandardCharsets;

/**
 * Resource service that performs the resource lookup.
 */
@Service
public class ResourceService {

	/**
	 * The search engine that is needed to get an entity by URI.
	 */
	private final SearchEngine searchEngine;

	/**
	 * The object mapper required to serialize the result.
	 */
	private final ObjectMapper objectMapper;

	/**
	 * Constructs the ResourceService.
	 *
	 * @param searchEngine the search engine to get an entity by URI
	 * @param objectMapper the object mapper to serialize the result
	 */
	@Autowired
	public ResourceService(@Qualifier("without-label") SearchEngine searchEngine, ObjectMapper objectMapper) {
		this.searchEngine = searchEngine;
		this.objectMapper = objectMapper;
	}

	/**
	 * Performs the resource lookup.
	 *
	 * @param uri the uri of the entity
	 * @return an object from a JSON-LD formatted result
	 */
	public Object getResource(String uri) {

		try {

			Entity entity = this.searchEngine.getEntity(uri);

			if (entity == null) {
				// easier than to configure serialization of new Object()
				return "{}";
			}

			String triples = entity.asTriples();

			Model model = ModelFactory.createDefaultModel();
			// use N-Triples since entity.asTriples() will always return N-Triples
			RDFReaderI tripleReader = model.getReader("N-Triples");
			InputStream inputStream = new ByteArrayInputStream(triples.getBytes(StandardCharsets.UTF_8));
			tripleReader.read(model, inputStream, uri);

			// always use JSON-LD since we are inside a REST-API
			RDFWriterI rdfWriter = model.getWriter("JSON-LD");
			StringWriter writer = new StringWriter();
			rdfWriter.write(model, writer, "");
			String object = writer.toString();

			return this.objectMapper.readValue(object, new TypeReference<>() {
			});

		} catch (Exception exception) {
			throw new RuntimeException(exception);
		}
	}
}
