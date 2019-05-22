package com.ajizan.liferay.cors.configuration;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

import java.util.List;

import aQute.bnd.annotation.metatype.Meta;

@ExtendedObjectClassDefinition(category = "cors")
@Meta.OCD(id = "com.ajizan.liferay.cors.configuration.LiferayCORSConfiguration", localization = "content/Language", name = "liferay-cors-configuration-name")
public interface LiferayCORSConfiguration {

	@Meta.AD(deflt = "Origin,Content-Type,Accept,Authorization", description = "cors-headers-description", name = "cors-headers", required = false, cardinality = 4)
	public List<String> getHeaders();

	@Meta.AD(deflt = "GET,POST,PUT,DELETE,OPTIONS,HEAD,PATCH", description = "cors-method-description", name = "cors-method", required = false, cardinality = 7)
	public List<String> getMethods();

	@Meta.AD(deflt = "1209500", description = "cors-max-age-description", name = "cors-max-age", required = false)
	public int getMaxAge();

	@Meta.AD(deflt = "*", description = "cors-origin-description", name = "cors-origin", required = false)
	public String getOrigin();

	@Meta.AD(deflt = "true", description = "cors-apply-options-description", name = "cors-apply-options", required = false)
	public boolean applyOptions();

}
