package simplews;

import java.lang.annotation.Annotation;
import java.lang.reflect.Field;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.inject.Provider;
import javax.ws.rs.RuntimeType;
import javax.ws.rs.client.ClientException;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.core.Configuration;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.ext.ContextResolver;
import javax.ws.rs.ext.ExceptionMapper;
import javax.ws.rs.ext.MessageBodyReader;
import javax.ws.rs.ext.MessageBodyWriter;
import javax.ws.rs.ext.Providers;

public class SimpleConfiguration implements FeatureContext, Providers,
		Configuration {

	final Map<Class<?>, ContractProviders<?>> contracts = new HashMap<Class<?>, ContractProviders<?>>();

	final SimpleConfiguration parent;

	public SimpleConfiguration(SimpleConfiguration parent) {
		this.parent = parent;
	}

	class ContractProviders<T> {
		final Class<T> providerClass;
		final ContractProviders<T> parent;

		List<Provider<T>> providers = new ArrayList<Provider<T>>();

		public ContractProviders(Class<T> providerClass,
				ContractProviders<T> parent) {
			this.providerClass = providerClass;
			this.parent = parent;
		}

		public void bindToProvider(Provider<T> provider) {
			providers.add(provider);
		}

		public void bindToClass(final Class<? extends T> providerClass) {
			bindToProvider(new Provider<T>() {

				@Override
				public T get() {
					try {
						T t = providerClass.newInstance();
						injectMembers(t);
						return t;
					} catch (InstantiationException e) {
						throw new IllegalStateException(
								"Error creating instance of " + providerClass,
								e);
					} catch (IllegalAccessException e) {
						throw new IllegalStateException(
								"Error creating instance of " + providerClass,
								e);
					}
				}

			});
		}

		public void bindToInstance(final Object instance) {
			bindToProvider(new Provider<T>() {
				@Override
				public T get() {
					return (T) instance;
				}
			});
		}

		public List<Provider<T>> getProviders() {
			List<Provider<T>> parentProviders = null;
			if (parent != null) {
				parentProviders = parent.getProviders();
			}

			if (providers.isEmpty()) {
				if (parentProviders != null) {
					return parentProviders;
				}
			}

			if (parentProviders == null || parentProviders.isEmpty()) {

				return providers;
			}

			List<Provider<T>> ret = new ArrayList<Provider<T>>();
			ret.addAll(providers);
			ret.addAll(parentProviders);
			return ret;
		}
	}

	void injectMembers(Object o) {
		Class<?> clazz = o.getClass();
		while (clazz != null) {
			for (Field f : clazz.getDeclaredFields()) {
				Context contextAnnotation = f.getAnnotation(Context.class);
				if (contextAnnotation != null) {
					Object v = getInstance(f.getType());
					try {
						f.setAccessible(true);
						f.set(o, v);
					} catch (Exception e) {
						throw new IllegalStateException(
								"Error setting @Context field: " + f, e);
					}
				}
			}
			clazz = clazz.getSuperclass();
		}
	}

	private Object getInstance(Class<?> type) {
		if (type == Providers.class) {
			return this;
		}
		throw new UnsupportedOperationException(
				"@Context of unsupported type: " + type);
	}

	<T> ContractProviders<T> getProviders(Class<T> contract) {
		ContractProviders<T> providers = (ContractProviders<T>) contracts
				.get(contract);
		if (providers == null) {
			ContractProviders<T> parentProviders = null;
			if (parent != null) {
				parentProviders = parent.getProviders(contract);
			}

			providers = new ContractProviders<T>(contract, parentProviders);
			contracts.put(contract, providers);
		}
		return providers;
	}

	@Override
	public Configuration getConfiguration() {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext setProperty(String name, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext register(Class<?> componentClass) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext register(Class<?> componentClass, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext register(Class<?> componentClass,
			Class<?>... contracts) {
		for (Class<?> contract : contracts) {
			getProviders(contract).bindToClass((Class) componentClass);
		}
		return this;
	}

	@Override
	public FeatureContext register(Class<?> componentClass,
			Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext register(Object component) {
		if (component instanceof Feature) {
			((Feature) component).configure(this);
		} else {
			if (component instanceof ContextResolver) {
				getProviders(ContextResolver.class).bindToInstance(component);
			} else if (component instanceof ClientRequestFilter) {
				getProviders(ClientRequestFilter.class).bindToInstance(
						component);
			} else {
				throw new UnsupportedOperationException();
			}
		}
		return this;
	}

	@Override
	public FeatureContext register(Object component, int bindingPriority) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext register(Object component, Class<?>... contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext register(Object component,
			Map<Class<?>, Integer> contracts) {
		throw new UnsupportedOperationException();
	}

	@Override
	public FeatureContext replaceWith(Configuration config) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> MessageBodyReader<T> getMessageBodyReader(Class<T> type,
			Type genericType, Annotation[] annotations, MediaType mediaType) {
		ContractProviders<MessageBodyReader> providers = getProviders(MessageBodyReader.class);
		List<Provider<MessageBodyReader>> bindings = providers.getProviders();
		for (Provider<MessageBodyReader> binding : bindings) {
			MessageBodyReader messageBodyReader = binding.get();

			if (messageBodyReader.isReadable(type, genericType, annotations,
					mediaType)) {
				return messageBodyReader;
			}
		}

		throw new ClientException("Cannot find MessageBodyReader for: " + type);
	}

	@Override
	public <T> MessageBodyWriter<T> getMessageBodyWriter(Class<T> type,
			Type genericType, Annotation[] annotations, MediaType mediaType) {
		ContractProviders<MessageBodyWriter> providers = getProviders(MessageBodyWriter.class);
		List<Provider<MessageBodyWriter>> bindings = providers.getProviders();
		for (Provider<MessageBodyWriter> binding : bindings) {
			MessageBodyWriter messageBodyWriter = binding.get();

			if (messageBodyWriter.isWriteable(type, genericType, annotations,
					mediaType)) {
				return messageBodyWriter;
			}
		}

		throw new ClientException("Cannot find MessageBodyWriter for: " + type);
	}

	@Override
	public <T extends Throwable> ExceptionMapper<T> getExceptionMapper(
			Class<T> type) {
		throw new UnsupportedOperationException();
	}

	@Override
	public <T> ContextResolver<T> getContextResolver(Class<T> contextType,
			MediaType mediaType) {
		ContractProviders<ContextResolver> providers = getProviders(ContextResolver.class);

		for (Provider<ContextResolver> contextResolverProvider : providers
				.getProviders()) {
			ContextResolver contextResolver = contextResolverProvider.get();
			return contextResolver;
		}

		throw new UnsupportedOperationException();
	}

	@Override
	public RuntimeType getRuntimeType() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Map<String, Object> getProperties() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Object getProperty(String name) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Collection<String> getPropertyNames() {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isEnabled(Feature feature) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isEnabled(Class<? extends Feature> featureClass) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isRegistered(Object component) {
		throw new UnsupportedOperationException();

	}

	@Override
	public boolean isRegistered(Class<?> componentClass) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Map<Class<?>, Integer> getContracts(Class<?> componentClass) {
		throw new UnsupportedOperationException();

	}

	@Override
	public Set<Class<?>> getClasses() {
		throw new UnsupportedOperationException();

	}

	@Override
	public Set<Object> getInstances() {
		throw new UnsupportedOperationException();

	}
}
