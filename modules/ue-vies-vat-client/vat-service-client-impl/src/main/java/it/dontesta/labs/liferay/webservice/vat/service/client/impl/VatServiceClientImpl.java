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

package it.dontesta.labs.liferay.webservice.vat.service.client.impl;

import com.liferay.portal.kernel.util.Validator;

import eu.europa.ec.taxud.vies.services.checkvat.CheckVatPortType;
import eu.europa.ec.taxud.vies.services.checkvat.CheckVatService;

import it.dontesta.labs.liferay.webservice.vat.service.api.VatServiceClient;
import it.dontesta.labs.liferay.webservice.vat.service.exception.VatServiceClientOperationException;
import it.dontesta.labs.liferay.webservice.vat.service.exception.VatServiceException;
import it.dontesta.labs.liferay.webservice.vat.service.model.VatResponse;

import javax.xml.datatype.XMLGregorianCalendar;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;

import org.osgi.service.component.annotations.Component;

/**
 * @author Antonio Musarra
 */
@Component(
	immediate = true, property = "secure=off", service = VatServiceClient.class
)
public class VatServiceClientImpl implements VatServiceClient {

	@Override
	public VatResponse checkVat(String countryCode, String vatNumber)
		throws VatServiceClientOperationException {

		try {
			Holder<String> countryCodeHolder = new Holder<>();
			Holder<String> vatNumberHolder = new Holder<>();
			Holder<XMLGregorianCalendar> requestDaterHolder = new Holder<>();
			Holder<Boolean> validHolder = new Holder<>();
			Holder<String> businessNameHolder = new Holder<>();
			Holder<String> businessAddressHolder = new Holder<>();

			countryCodeHolder.value = countryCode;
			vatNumberHolder.value = vatNumber;

			_getService().checkVat(
				countryCodeHolder, vatNumberHolder, requestDaterHolder,
				validHolder, businessNameHolder, businessAddressHolder);

			VatResponse vatResponse = new VatResponse();

			vatResponse.setCountryCode(countryCodeHolder.value);
			vatResponse.setVatNumber(vatNumberHolder.value);
			vatResponse.setBusinessAddress(
				businessAddressHolder.value.replaceAll(
					"\\r\\n|\\r|\\n", " "
				).trim());
			vatResponse.setBusinessName(businessNameHolder.value);
			vatResponse.setValid(validHolder.value);
			vatResponse.setRequestDate(
				requestDaterHolder.value.toGregorianCalendar(
				).getTime());

			return vatResponse;
		}
		catch (VatServiceException vse) {
			throw new VatServiceClientOperationException(vse.getMessage(), vse);
		}
	}

	private CheckVatPortType _getService() throws VatServiceException {
		if (Validator.isNull(_checkVatPortType)) {
			try {
				CheckVatService checkVatService = new CheckVatService();

				_checkVatPortType = checkVatService.getCheckVatPort();
			}
			catch (WebServiceException wse) {
				throw new VatServiceException(wse.getMessage(), wse);
			}

			return _checkVatPortType;
		}
		else {
			return _checkVatPortType;
		}
	}

	private CheckVatPortType _checkVatPortType;

}