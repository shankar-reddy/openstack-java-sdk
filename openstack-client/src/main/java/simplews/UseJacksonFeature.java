package simplews;

import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;

import org.codehaus.jackson.jaxrs.JacksonJaxbJsonProvider;

public class UseJacksonFeature implements Feature {

	@Override
	public boolean configure(final FeatureContext context) {
		context.register(JacksonJaxbJsonProvider.class,
				MessageBodyReader.class, MessageBodyWriter.class);

		return true;
	}
}
