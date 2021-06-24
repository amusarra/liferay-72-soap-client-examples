/*
 * Copyright (c) 2009-present Antonio Musarra's Blog - https://www.dontesta.it
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy of
 * this software and associated documentation files (the "Software"), to deal in the
 * Software without restriction, including without limitation the rights to use, copy,
 * modify, merge, publish, distribute, sublicense, and/or sell copies of the Software,
 * and to permit persons to whom the Software is furnished to do so, subject to the
 * following conditions:
 *
 * The above copyright notice and this permission notice shall be included in all
 * copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND,
 * EXPRESS OR IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF
 * MERCHANTABILITY, FITNESS FOR A PARTICULAR PURPOSE AND NON INFRINGEMENT.
 * IN NO EVENT SHALL THE AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY
 * CLAIM, DAMAGES OR OTHER LIABILITY, WHETHER IN AN ACTION OF CONTRACT,
 * TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN CONNECTION WITH THE
 * SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.
 */

package it.dontesta.labs.liferay.webservice.customusers.service.ws.audit;

import com.liferay.portal.kernel.audit.AuditException;
import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.audit.AuditRouterUtil;
import com.liferay.portal.kernel.exception.PortalException;
import com.liferay.portal.kernel.json.JSONException;
import com.liferay.portal.kernel.json.JSONFactoryUtil;
import com.liferay.portal.kernel.json.JSONObject;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.model.User;
import com.liferay.portal.kernel.service.ServiceContext;
import com.liferay.portal.kernel.service.ServiceContextThreadLocal;
import com.liferay.portal.kernel.service.UserLocalServiceUtil;
import com.liferay.portal.kernel.util.PortalUtil;
import com.liferay.portal.kernel.util.Validator;

import java.io.Serializable;

import java.util.Map;

/**
 * @author Antonio Musarra
 */
public class AuditEventUtil {

	public static AuditMessage getAuditMessage(
		Map<String, Serializable> additionalInfo, String className,
		String classPK, String messageReason, String auditEventType,
		String auditEventCode, String auditEventDescription) {

		User user = null;

		try {
			ServiceContext serviceContext =
				ServiceContextThreadLocal.getServiceContext();

			if (Validator.isNotNull(serviceContext)) {
				long userId = serviceContext.getUserId();

				if (Validator.isNotNull(userId)) {
					user = UserLocalServiceUtil.fetchUser(userId);
				}
			}

			if (Validator.isNull(user)) {
				user = UserLocalServiceUtil.getDefaultUser(
					PortalUtil.getDefaultCompanyId());
			}
		}
		catch (PortalException pe) {
			if (_log.isWarnEnabled()) {
				_log.warn("Strangely, the user could not be found");
			}
		}

		additionalInfo.put(_AUDIT_EVENT_TYPE, auditEventType);
		additionalInfo.put(_AUDIT_EVENT_CODE, auditEventCode);
		additionalInfo.put(_AUDIT_EVENT_DESCRIPTION, auditEventDescription);

		String additionalInfoAsJson = JSONFactoryUtil.looseSerialize(
			additionalInfo);

		JSONObject additionalInfoJSON = null;

		try {
			additionalInfoJSON = JSONFactoryUtil.createJSONObject(
				additionalInfoAsJson);
		}
		catch (JSONException jsone) {
			if (_log.isWarnEnabled()) {
				_log.warn("Error in the JSON deserialization phase");
			}

			additionalInfoJSON = JSONFactoryUtil.createJSONObject();
		}

		AuditMessage auditMessage = new AuditMessage(
			auditEventType, user.getCompanyId(), user.getUserId(),
			user.getFullName(), className, String.valueOf(classPK),
			messageReason, additionalInfoJSON);

		return auditMessage;
	}

	/**
	 * Send the audit message
	 *
	 * @param auditMessage The Audit Message
	 */
	public static void sendAuditMessage(AuditMessage auditMessage) {
		try {
			AuditRouterUtil.getAuditRouter(
			).route(
				auditMessage
			);
		}
		catch (AuditException ae) {
			if (_log.isErrorEnabled()) {
				_log.error(
					"There were errors during the audit message routing", ae);
			}
		}
	}

	private static final String _AUDIT_EVENT_CODE = "auditEventCode";

	private static final String _AUDIT_EVENT_DESCRIPTION =
		"auditEventDescription";

	private static final String _AUDIT_EVENT_TYPE = "auditEventType";

	private static Log _log = LogFactoryUtil.getLog(AuditEventUtil.class);

}