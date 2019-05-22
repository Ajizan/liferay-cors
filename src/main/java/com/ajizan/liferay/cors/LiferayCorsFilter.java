package com.ajizan.liferay.cors;

import com.ajizan.liferay.cors.configuration.LiferayCORSConfiguration;
import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;

import java.io.IOException;
import java.util.Map;

import javax.ws.rs.container.ContainerRequestContext;
import javax.ws.rs.container.ContainerResponseContext;
import javax.ws.rs.container.ContainerResponseFilter;
import javax.ws.rs.core.MultivaluedMap;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

@Component(property = { "osgi.jaxrs.extension=true",
		"osgi.jaxrs.name=Liferay.Filter.CORS" }, configurationPid = "com.ajizan.liferay.cors.configuration.LiferayCORSConfiguration", service = ContainerResponseFilter.class)
public class LiferayCorsFilter implements ContainerResponseFilter {

	private Log _log = LogFactoryUtil.getLog(getClass());

	@Override
	public void filter(ContainerRequestContext requestContext, ContainerResponseContext responseContext)
			throws IOException {

		String method = requestContext.getMethod();

		boolean applyOptions = _liferayCORSConfiguration.applyOptions();
		if (applyOptions && !"OPTIONS".equals(method)) {
			return;
		}

		String allowOrigin = _liferayCORSConfiguration.getOrigin();
		String allowHeaders = String.join(",", _liferayCORSConfiguration.getHeaders());
		String allowMethods = String.join(",", _liferayCORSConfiguration.getMethods());
		int allowMaxAge = _liferayCORSConfiguration.getMaxAge();

		if (_log.isDebugEnabled()) {
			_log.debug("Writing CORS headers. Access-Control-Allow-Origin=" + allowOrigin
					+ ", Access-Control-Allow-Headers=" + allowHeaders + ", Access-Control-Allow-Methods="
					+ allowMethods + ", Access-Control-Max-Age=" + allowMaxAge);
		}

		MultivaluedMap<String, Object> headers = responseContext.getHeaders();
		headers.add("Access-Control-Allow-Origin", allowOrigin);
		headers.add("Access-Control-Allow-Headers", allowHeaders);
		headers.add("Access-Control-Allow-Methods", allowMethods);
		headers.add("Access-Control-Max-Age", allowMaxAge);

	}

	@Activate
	@Modified
	protected void activate(Map<String, Object> properties) {
		_liferayCORSConfiguration = ConfigurableUtil.createConfigurable(LiferayCORSConfiguration.class, properties);
	}

	private volatile LiferayCORSConfiguration _liferayCORSConfiguration;

}
