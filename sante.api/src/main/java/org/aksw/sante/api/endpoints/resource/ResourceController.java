package org.aksw.sante.api.endpoints.resource;

import org.apache.commons.validator.routines.UrlValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RestController;

import java.net.MalformedURLException;
import java.text.MessageFormat;

/**
 * Controller for the resource endpoint.
 */
@RestController
public class ResourceController implements ResourceOperations {

	/**
	 * The service that performs the resource lookup.
	 */
	private final ResourceService resourceService;

	/**
	 * The UrlValidator that validates an input URI.
	 */
	private final UrlValidator urlValidator;

	/**
	 * Constructs the ResourceController.
	 *
	 * @param resourceService the service that performs the resource lookup
	 * @param urlValidator the UrlValidator to validate input URIs
	 */
	@Autowired
	public ResourceController(ResourceService resourceService, UrlValidator urlValidator) {
		this.resourceService = resourceService;
		this.urlValidator = urlValidator;
	}

	/**
	 * Performs the resource lookup.
	 *
	 * @param uri the uri to perform the lookup for
	 * @return a JSON-LD serialized object
	 */
	@Override
	public Object getResource(String uri) {
		if (!this.urlValidator.isValid(uri)) {
			throw new RuntimeException(
					new MalformedURLException(MessageFormat.format(
							"The URI {0} is malformed", uri
					))
			);
		}
		return this.resourceService.getResource(uri);
	}
}
