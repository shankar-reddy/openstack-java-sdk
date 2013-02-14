package simplews;

import java.net.URI;
import java.net.URISyntaxException;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.utils.URIBuilder;
import org.apache.http.entity.ByteArrayEntity;

public class SimpleWebTarget implements WebTarget {

	private final SimpleClient client;
	private final URI uri;

	final MultivaluedMap<String, String> queryParams;

	SimpleConfiguration configuration;

	public SimpleWebTarget(SimpleClient client,
			SimpleConfiguration parentConfiguration, URI uri,
			MultivaluedMap<String, String> queryParams) {
		this.client = client;
		this.uri = uri;
		this.queryParams = queryParams;

		this.configuration = new SimpleConfiguration(parentConfiguration);
	}

	@Override
	public SimpleConfiguration getConfiguration() {
		return configuration;
	}

	@Override
	public WebTarget setProperty(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Class<?> componentClass) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Class<?> componentClass, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Class<?> componentClass, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Class<?> componentClass,
			Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Object component) {
		configuration.register(component);
		return this;
	}

	@Override
	public WebTarget register(Object component, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Object component, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget register(Object component, Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget replaceWith(Configuration config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public URI getUri() {
		URI withQueryParams = uri;

		if (queryParams != null && !queryParams.isEmpty()) {
			URIBuilder uriBuilder = new URIBuilder(uri);

			for (Entry<String, List<String>> entry : queryParams.entrySet()) {
				String name = entry.getKey();
				for (String value : entry.getValue()) {
					uriBuilder.addParameter(name, value);
				}
			}
			try {
				withQueryParams = uriBuilder.build();
			} catch (URISyntaxException e) {
				throw new IllegalArgumentException("Error building URI", e);
			}
		}

		return withQueryParams;
	}

	@Override
	public UriBuilder getUriBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget path(String path) throws NullPointerException {
		return new SimpleWebTarget(client, configuration, extendUri(path),
				this.queryParams);
	}

	private URI extendUri(String extension) {
		URIBuilder uriBuilder = new URIBuilder(this.uri);
		String path = uri.getPath();

		if (extension.startsWith("/")) {
			if (path.endsWith("/")) {
				extension = extension.substring(1);
			}
		} else {
			if (!path.endsWith("/")) {
				path += "/";
			}
		}
		path += extension;

		uriBuilder.setPath(path);

		try {
			return uriBuilder.build();
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid URI: "
					+ uriBuilder.toString(), e);
		}
	}

	@Override
	public WebTarget resolveTemplate(String name, Object value)
			throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget resolveTemplate(String name, Object value,
			boolean encodeSlashInPath) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget resolveTemplateFromEncoded(String name, Object value)
			throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget resolveTemplates(Map<String, Object> templateValues)
			throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget resolveTemplates(Map<String, Object> templateValues,
			boolean encodeSlashInPath) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget resolveTemplatesFromEncoded(
			Map<String, Object> templateValues) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget matrixParam(String name, Object... values)
			throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget queryParam(String name, Object... values)
			throws NullPointerException {
		MultivaluedMap<String, String> newQueryParams = queryParams != null ? new MultivaluedHashMap<String, String>(
				queryParams) : new MultivaluedHashMap<String, String>();

		if (values.length == 1 && values[0] == null) {
			newQueryParams.remove(name);
		} else {
			for (Object value : values) {
				newQueryParams.add(name, value.toString());
			}
		}

		return new SimpleWebTarget(this.client, this.configuration, this.uri,
				newQueryParams);
	}

	@Override
	public Builder request() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder request(String... acceptedResponseTypes) {
		URI uri = getUri();
		SimpleBuilder builder = new SimpleBuilder(this, uri);
		builder.header("Accept", join(acceptedResponseTypes));
		return builder;
	}

	private String join(String[] values) {
		StringBuilder sb = new StringBuilder();
		for (int i = 0; i < values.length; i++) {
			if (i != 0) {
				sb.append(",");
			}
			sb.append(values[i]);
		}
		return sb.toString();
	}

	@Override
	public Builder request(MediaType... acceptedResponseTypes) {
		throw new UnsupportedOperationException();
	}

	public HttpClient getHttpClient() {
		return client.getHttpClient();
	}

	public ByteArrayEntity buildEntity(SimpleClientRequest request,
			Entity<?> entity) {
		return client.buildEntity(request, entity);
	}

	public <T> T readResponseMessageBody(GenericType<T> responseType,
			MultivaluedMap<String, String> httpHeaders, HttpResponse response) {
		return client.readResponseMessageBody(responseType, httpHeaders,
				response);
	}

}
