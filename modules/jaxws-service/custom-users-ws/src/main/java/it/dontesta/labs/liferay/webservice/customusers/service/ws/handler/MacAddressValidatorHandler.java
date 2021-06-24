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

package it.dontesta.labs.liferay.webservice.customusers.service.ws.handler;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.ArrayUtil;
import com.liferay.portal.kernel.util.Validator;

import it.dontesta.labs.liferay.webservice.customusers.service.ws.handler.configuration.MacAddressValidatorHandlerConfiguration;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;

import javax.xml.namespace.QName;
import javax.xml.soap.Node;
import javax.xml.soap.SOAPBody;
import javax.xml.soap.SOAPConstants;
import javax.xml.soap.SOAPEnvelope;
import javax.xml.soap.SOAPException;
import javax.xml.soap.SOAPFault;
import javax.xml.soap.SOAPHeader;
import javax.xml.soap.SOAPMessage;
import javax.xml.ws.handler.Handler;
import javax.xml.ws.handler.MessageContext;
import javax.xml.ws.handler.soap.SOAPHandler;
import javax.xml.ws.handler.soap.SOAPMessageContext;
import javax.xml.ws.soap.SOAPFaultException;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = MacAddressValidatorHandlerConfiguration.PID,
	immediate = true,
	property = "mac.address.validator.jax.ws.handler.filters=true",
	service = Handler.class
)
public class MacAddressValidatorHandler
	implements SOAPHandler<SOAPMessageContext> {

	@Override
	public void close(MessageContext context) {
	}

	@Override
	public Set<QName> getHeaders() {
		return new HashSet<>();
	}

	@Override
	public boolean handleFault(SOAPMessageContext context) {
		return true;
	}

	@Override
	public boolean handleMessage(SOAPMessageContext context) {
		_doValidateMacAddress(context);

		return true;
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_macAddressValidatorHandlerConfiguration =
			ConfigurableUtil.createConfigurable(
				MacAddressValidatorHandlerConfiguration.class, properties);
	}

	@Modified
	protected void modified(Map<String, Object> properties) {
		if (_log.isInfoEnabled()) {
			_log.info(
				String.format(
					"The configuration %s was modified.",
					MacAddressValidatorHandlerConfiguration.PID));
		}

		activate(properties);
	}

	private void _doValidateMacAddress(SOAPMessageContext context) {
		Boolean isOutBound = (Boolean)context.get(
			MessageContext.MESSAGE_OUTBOUND_PROPERTY);

		if (!isOutBound) {
			try {
				SOAPMessage soapMessage = context.getMessage();

				SOAPEnvelope soapEnv = soapMessage.getSOAPPart(
				).getEnvelope();

				SOAPHeader soapHeader = soapEnv.getHeader();

				if (Validator.isNull(soapHeader)) {
					soapHeader = soapEnv.addHeader();

					_generateSOAPErrorMessage(soapMessage, "No SOAP header.");
				}

				//Get client mac address from SOAP header
				Iterator<?> headers = soapHeader.extractHeaderElements(
					SOAPConstants.URI_SOAP_ACTOR_NEXT);

				if (Validator.isNull(headers) || !headers.hasNext()) {
					_generateSOAPErrorMessage(
						soapMessage, "No header block for next actor.");
				}

				Node macNode = (Node)headers.next();

				String macValue =
					Validator.isNull(macNode) ? null : macNode.getValue();

				if (Validator.isNull(macValue)) {
					_generateSOAPErrorMessage(
						soapMessage, "No MAC Address in header block.");
				}

				if (!ArrayUtil.contains(
						_macAddressValidatorHandlerConfiguration.
							macAddressWhiteList(),
						macValue, true)) {

					_generateSOAPErrorMessage(
						soapMessage, "Invalid MAC Address, access is denied.");
				}
			}
			catch (SOAPException soape) {
				if (_log.isErrorEnabled()) {
					_log.error(soape.getMessage(), soape);
				}
				_generateSOAPErrorMessage(
					context.getMessage(), soape.getMessage());
			}
		}
	}

	private void _generateSOAPErrorMessage(
		SOAPMessage soapMessage, String reason) {

		try {
			SOAPBody soapBody = soapMessage.getSOAPPart(
			).getEnvelope(
			).getBody();

			SOAPFault soapFault = soapBody.addFault();

			soapFault.setFaultString(reason);

			throw new SOAPFaultException(soapFault);
		}
		catch (SOAPException soape) {
			if (_log.isErrorEnabled()) {
				_log.error(soape.getMessage(), soape);
			}
		}
	}

	private static Log _log = LogFactoryUtil.getLog(
		MacAddressValidatorHandler.class);

	private volatile MacAddressValidatorHandlerConfiguration
		_macAddressValidatorHandlerConfiguration;

}