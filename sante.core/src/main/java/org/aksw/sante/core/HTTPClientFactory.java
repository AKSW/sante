package org.aksw.sante.core;

import org.apache.http.client.HttpClient;

public interface HTTPClientFactory {
	public HttpClient create();
}
