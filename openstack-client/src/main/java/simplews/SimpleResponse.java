package simplews;

import java.io.IOException;
import java.lang.annotation.Annotation;
import java.net.URI;
import java.util.Date;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

import javax.ws.rs.MessageProcessingException;
import javax.ws.rs.core.EntityTag;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.Link;
import javax.ws.rs.core.Link.Builder;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.NewCookie;
import javax.ws.rs.core.Response;

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;

public class SimpleResponse extends Response {

	private final HttpResponse response;
	private final HttpEntity responseEntity;

	public SimpleResponse(HttpResponse response) {
		this.response = response;
		this.responseEntity = response.getEntity();
	}

	@Override
	public int getStatus() {
		return response.getStatusLine().getStatusCode();
	}

	@Override
	public StatusType getStatusInfo() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Object getEntity() throws IllegalStateException {
		if (responseEntity == null)
			return null;
		try {
			return responseEntity.getContent();
		} catch (IOException e) {
			throw new IllegalStateException("Error reading response body", e);
		}
	}

	@Override
	public <T> T readEntity(Class<T> entityType)
			throws MessageProcessingException, IllegalStateException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T readEntity(GenericType<T> entityType)
			throws MessageProcessingException, IllegalStateException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T readEntity(Class<T> entityType, Annotation[] annotations)
			throws MessageProcessingException, IllegalStateException {
		throw new UnsupportedOperationException();

	}

	@Override
	public <T> T readEntity(GenericType<T> entityType, Annotation[] annotations)
			throws MessageProcessingException, IllegalStateException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean hasEntity() throws IllegalStateException {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean bufferEntity() throws MessageProcessingException,
			IllegalStateException {
		throw new UnsupportedOperationException();

	}

	@Override
	public void close() throws MessageProcessingException {
		throw new UnsupportedOperationException();

	}

	@Override
	public MediaType getMediaType() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Locale getLanguage() {
		throw new UnsupportedOperationException();

	}

	@Override
	public int getLength() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Set<String> getAllowedMethods() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Map<String, NewCookie> getCookies() {
		throw new UnsupportedOperationException();

	}

	@Override
	public EntityTag getEntityTag() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getDate() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Date getLastModified() {
		throw new UnsupportedOperationException();

	}

	@Override
	public URI getLocation() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Set<Link> getLinks() {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean hasLink(String relation) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Link getLink(String relation) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Builder getLinkBuilder(String relation) {
		throw new UnsupportedOperationException();

	}

	@Override
	public MultivaluedMap<String, Object> getMetadata() {
		throw new UnsupportedOperationException();

	}

	@Override
	public MultivaluedMap<String, String> getStringHeaders() {
		throw new UnsupportedOperationException();

	}

	@Override
	public String getHeaderString(String name) {
		throw new UnsupportedOperationException();

	}

}
