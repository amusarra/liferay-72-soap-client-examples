/*
 * Copyright (c) 2019 Antonio Musarra's Blog - https://www.dontesta.it
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

package it.dontesta.labs.liferay.webservice.customusers.service.ws.handler;

import com.liferay.portal.kernel.audit.AuditMessage;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.GetterUtil;
import com.liferay.portal.kernel.util.Validator;
import com.liferay.portal.kernel.uuid.PortalUUIDUtil;

import it.dontesta.labs.liferay.webservice.customusers.service.ws.audit.AuditEventKeys;
import it.dontesta.labs.liferay.webservice.customusers.service.ws.audit.AuditEventUtil;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.Serializable;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;

import org.osgi.service.component.annotations.Component;

/**
 * @author Antonio Musarra
 */
@Component(
	immediate = true, property = "audit.log.jax.ws.handler.filters=true",
	service = Handler.class
)
public class AuditLogHandler implements SOAPHandler<SOAPMessageContext> {

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return new HashSet<>();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		_doHandleMessage(context);

		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		_doHandleMessage(context);

		return true;
	}

	private void _doHandleMessage(SOAPMessageContext context) {
		Boolean isOutBound = (Boolean)context.get(
			MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		SOAPMessage soapMessage = context.getMessage();

		String newCorrelationId = PortalUUIDUtil.generate();
		String currentCorrelationId = GetterUtil.getString(
			context.get(_CORRELATION_ID));

		if (Validator.isNull(currentCorrelationId)) {
			currentCorrelationId = newCorrelationId;
			context.put(_CORRELATION_ID, newCorrelationId);
			context.setScope(_CORRELATION_ID, MessageContext.Scope.APPLICATION);
		}

		try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
			AuditMessage auditMessage;
			Map<String, Serializable> additionalInfo = new HashMap<>();

			soapMessage.writeTo(baos);

			if (isOutBound) {
				additionalInfo.put(
					_OUT_BOUND_MESSAGE, new String(baos.toByteArray()));

				auditMessage = AuditEventUtil.getAuditMessage(
					additionalInfo, AuditLogHandler.class.getName(),
					currentCorrelationId,
					AuditEventKeys.SERVICE_SOAP_TRACE_OUTBOUND_EVENT.
						getAuditEventTypeCodeDescription(),
					AuditEventKeys.SERVICE_SOAP_TRACE_OUTBOUND_EVENT.
						getAuditEventTypeCode(),
					AuditEventKeys.SERVICE_SOAP_TRACE_OUTBOUND_EVENT.
						getAuditEventTypeId(),
					AuditEventKeys.SERVICE_SOAP_TRACE_OUTBOUND_EVENT.
						getAuditEventTypeCodeDescription());
			}
			else {
				additionalInfo.put(
					_IN_BOUND_MESSAGE, new String(baos.toByteArray()));

				auditMessage = AuditEventUtil.getAuditMessage(
					additionalInfo, AuditLogHandler.class.getName(),
					currentCorrelationId,
					AuditEventKeys.SERVICE_SOAP_TRACE_INBOUND_EVENT.
						getAuditEventTypeCodeDescription(),
					AuditEventKeys.SERVICE_SOAP_TRACE_INBOUND_EVENT.
						getAuditEventTypeCode(),
					AuditEventKeys.SERVICE_SOAP_TRACE_INBOUND_EVENT.
						getAuditEventTypeId(),
					AuditEventKeys.SERVICE_SOAP_TRACE_INBOUND_EVENT.
						getAuditEventTypeCodeDescription());
			}

			AuditEventUtil.sendAuditMessage(auditMessage);
		}
		catch (IOException | SOAPException e) {
			if (_log.isErrorEnabled()) {
				_log.error(e.getMessage(), e);
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(AuditLogHandler.class);

	private final String _CORRELATION_ID = "correlationId";

	private final String _IN_BOUND_MESSAGE = "inBoundMessage";

	private final String _OUT_BOUND_MESSAGE = "outBoundMessage";

}