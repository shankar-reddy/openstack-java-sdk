package simplews;

import javax.ws.rs.core.Application;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response.ResponseBuilder;
import javax.ws.rs.core.UriBuilder;
import javax.ws.rs.core.Variant.VariantListBuilder;
import javax.ws.rs.ext.RuntimeDelegate;

public class SimpleRuntimeDelegate extends RuntimeDelegate {

	@Override
	public UriBuilder createUriBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public ResponseBuilder createResponseBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public VariantListBuilder createVariantListBuilder() {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> T createEndpoint(Application application, Class<T> endpointType)
			throws IllegalArgumentException, UnsupportedOperationException {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> HeaderDelegate<T> createHeaderDelegate(Class<T> type)
			throws IllegalArgumentException {
		if (type == MediaType.class) {
			return (HeaderDelegate<T>) new MediaTypeHeaderDelegate();
		}
		throw new UnsupportedOperationException(
				"Unsupported HeaderDelegate requested: " + type);
	}
}
