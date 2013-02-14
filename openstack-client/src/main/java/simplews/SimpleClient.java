package simplews;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.client.WebTarget;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.RuntimeDelegate;

import org.apache.http.Header;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.impl.client.DefaultHttpClient;

public class SimpleClient implements Client {

	final List<Object> components = new ArrayList<Object>();
	final HttpClient httpClient;
	final SimpleConfiguration configuration = new SimpleConfiguration(null);

	static {
		RuntimeDelegate.setInstance(new SimpleRuntimeDelegate());
	}

	public SimpleClient(HttpClient httpClient) {
		this.httpClient = httpClient;
	}

	public SimpleClient() {
		this(new DefaultHttpClient());
	}

	@Override
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client setProperty(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Class<?> componentClass) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Class<?> componentClass, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Class<?> componentClass, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Class<?> componentClass,
			Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Object component) {
		components.add(component);

		configuration.register(component);

		return this;
	}

	@Override
	public Client register(Object component, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Object component, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client register(Object component, Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Client replaceWith(Configuration config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void close() {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget target(String uri) throws IllegalArgumentException,
			NullPointerException {
		URI parsed;
		try {
			parsed = new URI(uri);
		} catch (URISyntaxException e) {
			throw new IllegalArgumentException("Invalid URI: " + uri, e);
		}
		return new SimpleWebTarget(this, this.configuration, parsed, null);
	}

	@Override
	public WebTarget target(URI uri) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget target(UriBuilder uriBuilder) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public WebTarget target(Link link) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder invocation(Link link) throws NullPointerException {
		throw new UnsupportedOperationException();
	}

	public HttpClient getHttpClient() {
		return httpClient;
	}

	public ByteArrayEntity buildEntity(SimpleClientRequest request,
			Entity<?> entity) {
		MediaType mediaType = entity.getMediaType();
		Object o = entity.getEntity();
		Class<?> type = o.getClass();
		Class<?> genericType = type;

		request.httpHeaders.add("Content-Type", mediaType.toString());

		Annotation[] annotations = type.getAnnotations();
		MessageBodyWriter messageBodyWriter = configuration
				.getMessageBodyWriter(type, genericType, annotations, mediaType);

		ByteArrayOutputStream baos = new ByteArrayOutputStream();
		try {
			messageBodyWriter.writeTo(entity.getEntity(), type, genericType,
					annotations, mediaType, request.httpHeaders, baos);
		} catch (IOException e) {
			throw new ClientException("Error writing message body", e);
		}

		return new ByteArrayEntity(baos.toByteArray());
	}

	public <T> T readResponseMessageBody(GenericType<T> genericType,
			MultivaluedMap<String, String> httpHeaders, HttpResponse response) {
		Header contentType = response.getEntity().getContentType();
		MediaType mediaType = null;
		if (contentType != null) {
			mediaType = MediaType.valueOf(contentType.getValue());
		}

		Class<T> clazz = (Class<T>) genericType.getRawType();

		Annotation[] annotations = clazz.getAnnotations();
		MessageBodyReader<T> messageBodyReader = configuration
				.getMessageBodyReader(clazz, genericType.getType(),
						annotations, mediaType);

		InputStream entityStream = null;
		try {
			entityStream = response.getEntity().getContent();
			T t = messageBodyReader.readFrom(clazz, genericType.getType(),
					annotations, mediaType, httpHeaders, entityStream);
			return t;
		} catch (IOException e) {
			throw new ClientException("Error reading response message body", e);
		} finally {
			if (entityStream != null) {
				try {
					entityStream.close();
				} catch (IOException e) {
					throw new ClientException("Error closing response stream",
							e);
				}
			}
		}

	}
}
