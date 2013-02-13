package simplews;

import java.io.OutputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Type;
import java.net.URI;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;

import javax.ws.rs.client.Client;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.http.client.methods.HttpUriRequest;

public class SimpleClientRequest implements ClientRequestContext {
	MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<String, Object>();

	Entity<?> entity;
	HttpUriRequest httpClientRequest;

	@Override
	public Object getProperty(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Collection<String> getPropertyNames() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setProperty(String name, Object object) {
		throw new UnsupportedOperationException();

	}

	@Override
	public void removeProperty(String name) {
		throw new UnsupportedOperationException();

	}

	@Override
	public URI getUri() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setUri(URI uri) {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getMethod() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setMethod(String method) {
		throw new UnsupportedOperationException();

	}

	@Override
	public MultivaluedMap<String, Object> getHeaders() {
		return this.httpHeaders;
	}

	@Override
	public MultivaluedMap<String, String> getStringHeaders() {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getHeaderString(String name) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Date getDate() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Locale getLanguage() {
		throw new UnsupportedOperationException();
	}

	@Override
	public MediaType getMediaType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<MediaType> getAcceptableMediaTypes() {
		throw new UnsupportedOperationException();
	}

	@Override
	public List<Locale> getAcceptableLanguages() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Map<String, Cookie> getCookies() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean hasEntity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getEntity() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Class<?> getEntityClass() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Type getEntityType() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEntity(Object entity, Annotation[] annotations,
			MediaType mediaType) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Annotation[] getEntityAnnotations() {
		throw new UnsupportedOperationException();
	}

	@Override
	public OutputStream getEntityStream() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setEntityStream(OutputStream outputStream) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Client getClient() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public void abortWith(Response response) {
		throw new UnsupportedOperationException();

	}

	void prepareHttpClientRequest() {
		for (Entry<String, List<Object>> entry : httpHeaders.entrySet()) {
			String name = entry.getKey();
			for (Object v : entry.getValue()) {
				String value = v.toString();
				httpClientRequest.addHeader(name, value);
			}
		}
	}
}
