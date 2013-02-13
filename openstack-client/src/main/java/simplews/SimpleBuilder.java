package simplews;

import java.io.IOException;
import java.net.URI;
import java.util.Locale;
import java.util.Map;

import javax.inject.Provider;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.client.AsyncInvoker;
import javax.ws.rs.client.ClientException;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.client.Invocation;
import javax.ws.rs.client.Invocation.Builder;
import javax.ws.rs.core.CacheControl;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Cookie;
import javax.ws.rs.core.GenericType;
import javax.ws.rs.core.MultivaluedHashMap;
import javax.ws.rs.core.MultivaluedMap;
import javax.ws.rs.core.Response;

import org.apache.http.Header;
import org.apache.http.HttpEntityEnclosingRequest;
import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.entity.ByteArrayEntity;
import org.apache.http.util.EntityUtils;

import simplews.SimpleConfiguration.ContractProviders;

public class SimpleBuilder implements Builder {

	MultivaluedMap<String, Object> httpHeaders = new MultivaluedHashMap<String, Object>();

	private final SimpleWebTarget target;
	private final URI uri;

	public SimpleBuilder(SimpleWebTarget target, URI uri) {
		this.target = target;
		this.uri = uri;
	}

	@Override
	public Response get() throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T get(Class<T> responseType) throws ClientException,
			WebApplicationException {
		HttpGet get = new HttpGet(getUri());
		GenericType<T> genericType = new GenericType<T>(responseType);
		return doAction(null, genericType, get);
	}

	@Override
	public <T> T get(GenericType<T> responseType) throws ClientException,
			WebApplicationException {
		HttpGet get = new HttpGet(getUri());
		return doAction(null, responseType, get);
	}

	@Override
	public Response put(Entity<?> entity) throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T put(Entity<?> entity, Class<T> responseType)
			throws ClientException, WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T put(Entity<?> entity, GenericType<T> responseType)
			throws ClientException, WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response post(Entity<?> entity) throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T post(Entity<?> entity, Class<T> responseType)
			throws ClientException, WebApplicationException {
		HttpPost post = new HttpPost(getUri());

		GenericType<T> genericType = new GenericType<T>(responseType);

		return doAction(entity, genericType, post);
	}

	<T> T doAction(Entity<?> entity, GenericType<T> responseType,
			HttpUriRequest httpClientRequest) {
		SimpleClientRequest request = new SimpleClientRequest();
		request.httpClientRequest = httpClientRequest;
		request.httpHeaders.putAll(this.httpHeaders);

		HttpClient client = getHttpClient();
		HttpResponse response = null;

		try {
			if (entity != null) {
				ByteArrayEntity httpClientEntity = buildEntity(request, entity);
				((HttpEntityEnclosingRequest) request.httpClientRequest)
						.setEntity(httpClientEntity);
			}

			ContractProviders<ClientRequestFilter> filterProviders = getProviders(ClientRequestFilter.class);

			for (Provider<ClientRequestFilter> filterProvider : filterProviders
					.getProviders()) {
				ClientRequestFilter clientRequestFilter = filterProvider.get();
				clientRequestFilter.filter(request);
			}

			request.prepareHttpClientRequest();

			response = client.execute(request.httpClientRequest);

			MultivaluedMap<String, String> responseHeaders = getResponseHeaderMap(response);

			return processResponse(responseType, responseHeaders, response);
		} catch (IOException e) {
			throw new ClientException("Error during HTTP request", e);
		} finally {
			if (response != null) {
				EntityUtils.consumeQuietly(response.getEntity());
			}
		}
	}

	private <T> ContractProviders<T> getProviders(Class<T> contract) {
		return target.getConfiguration().getProviders(contract);
	}

	private MultivaluedMap<String, String> getResponseHeaderMap(
			HttpResponse response) {
		MultivaluedMap<String, String> responseHeaders = new MultivaluedHashMap<String, String>();
		for (Header header : response.getAllHeaders()) {
			responseHeaders.add(header.getName(), header.getValue());
		}
		return responseHeaders;
	}

	@Override
	public <T> T post(Entity<?> entity, GenericType<T> responseType)
			throws ClientException, WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response delete() throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T delete(Class<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T delete(GenericType<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response head() throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response options() throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T options(Class<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T options(GenericType<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response trace() throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T trace(Class<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T trace(GenericType<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response method(String name) throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T method(String name, Class<T> responseType)
			throws ClientException, WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T method(String name, GenericType<T> responseType)
			throws ClientException, WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Response method(String name, Entity<?> entity)
			throws ClientException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T method(String name, Entity<?> entity, Class<T> responseType)
			throws ClientException, WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T method(String name, Entity<?> entity,
			GenericType<T> responseType) throws ClientException,
			WebApplicationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder setProperty(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Class<?> componentClass) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Class<?> componentClass, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Class<?> componentClass, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Class<?> componentClass,
			Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Object component) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Object component, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Object component, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder register(Object component, Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder replaceWith(Configuration config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Invocation build(String method) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Invocation build(String method, Entity<?> entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Invocation buildGet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Invocation buildDelete() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Invocation buildPost(Entity<?> entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Invocation buildPut(Entity<?> entity) {
		throw new UnsupportedOperationException();
	}

	@Override
	public AsyncInvoker async() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder acceptLanguage(Locale... locales) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder acceptLanguage(String... locales) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder cookie(Cookie cookie) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder cookie(String name, String value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder cacheControl(CacheControl cacheControl) {
		throw new UnsupportedOperationException();
	}

	@Override
	public Builder header(String name, Object value) {
		this.httpHeaders.add(name, value);
		return this;
	}

	@Override
	public Builder headers(MultivaluedMap<String, Object> headers) {
		throw new UnsupportedOperationException();
	}

	private HttpClient getHttpClient() {
		return target.getHttpClient();
	}

	private ByteArrayEntity buildEntity(SimpleClientRequest request,
			Entity<?> entity) {
		return target.buildEntity(request, entity);
	}

	private URI getUri() {
		return uri;
	}

	private <T> T processResponse(GenericType<T> responseType,
			MultivaluedMap<String, String> responseHeaders,
			HttpResponse response) {
		T t = target.readResponseMessageBody(responseType, responseHeaders,
				response);
		return t;
	}

}
