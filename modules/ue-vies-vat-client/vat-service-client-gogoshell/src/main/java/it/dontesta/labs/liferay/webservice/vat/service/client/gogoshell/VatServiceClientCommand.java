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

package it.dontesta.labs.liferay.webservice.vat.service.client.gogoshell;

import it.dontesta.labs.liferay.webservice.vat.service.api.VatServiceClient;
import it.dontesta.labs.liferay.webservice.vat.service.exception.VatServiceClientOperationException;
import it.dontesta.labs.liferay.webservice.vat.service.model.VatResponse;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Antonio Musarra
 */
@Component(
	property = {"osgi.command.function=checkVat", "osgi.command.scope=vat"},
	service = Object.class
)
public class VatServiceClientCommand implements VatServiceClient {

	@Override
	public VatResponse checkVat(String countryCode, String vatNumber)
		throws VatServiceClientOperationException {

		return _vatServiceClient.checkVat(countryCode, vatNumber);
	}

	public VatServiceClient getCalculatorService() {
		return _vatServiceClient;
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private volatile VatServiceClient _vatServiceClient;

}