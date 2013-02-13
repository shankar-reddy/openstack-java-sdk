package simplews;

import java.util.Map;

import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.RuntimeDelegate.HeaderDelegate;

public class MediaTypeHeaderDelegate implements HeaderDelegate<MediaType> {

	@Override
	public MediaType fromString(String value) throws IllegalArgumentException {
		int colonIndex = value.indexOf(";");
		if (colonIndex != -1) {
			String parameters = value.substring(colonIndex + 1);
			value = value.substring(0, colonIndex);

			// TODO: Parse the headers
		}

		String[] tokens = value.split("/");
		if (tokens.length != 2) {
			throw new UnsupportedOperationException();
		}

		return new MediaType(tokens[0], tokens[1]);
	}

	@Override
	public String toString(MediaType value) throws IllegalArgumentException {
		String type = value.getType();
		String subtype = value.getSubtype();

		Map<String, String> parameters = value.getParameters();
		if (parameters != null && !parameters.isEmpty()) {
			throw new UnsupportedOperationException();
		}

		return type + "/" + subtype;
	}

}
