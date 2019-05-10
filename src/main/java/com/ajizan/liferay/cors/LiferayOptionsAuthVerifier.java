package com.ajizan.liferay.cors;

import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.security.auth.AccessControlContext;
import com.liferay.portal.kernel.security.auth.AuthException;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifier;
import com.liferay.portal.kernel.security.auth.verifier.AuthVerifierResult;
import com.liferay.portal.kernel.service.PortalService;
import com.liferay.portal.kernel.service.UserLocalService;
import com.liferay.portal.kernel.util.PortalUtil;

import java.util.Properties;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@Component(immediate = true, property = "auth.verifier.LiferayOptionsAuthVerifier.urls.includes=/*", service = AuthVerifier.class)
public class LiferayOptionsAuthVerifier implements AuthVerifier {

	@Override
	public String getAuthType() {
		return "LiferayOptionsAuth";
	}

	@Override
	public AuthVerifierResult verify(AccessControlContext accessControlContext, Properties properties)
			throws AuthException {
		AuthVerifierResult authVerifierResult = new AuthVerifierResult();

		if (_log.isDebugEnabled()) {
			_log.debug("pathInfo: " + accessControlContext.getRequest().getPathInfo());
			_log.debug("method: " + accessControlContext.getRequest().getMethod());
			_log.debug("contextPath: " + accessControlContext.getRequest().getContextPath());
		}

		String method = accessControlContext.getRequest().getMethod();
		if ("OPTIONS".equals(method)) {
			try {
				long companyId = PortalUtil.getDefaultCompanyId();
				User defaultUser = _userLocalService.getDefaultUser(companyId);
				authVerifierResult.setUserId(defaultUser.getUserId());
				authVerifierResult.setState(AuthVerifierResult.State.SUCCESS);
			} catch (PortalException e) {
				_log.error("An error occurred when trying to retreive default user", e);
			}
		}

		return authVerifierResult;
	}

	private static Log _log = LogFactoryUtil.getLog(LiferayOptionsAuthVerifier.class);

	@Reference
	private UserLocalService _userLocalService;

	@Reference
	private PortalService _portalService;

}
