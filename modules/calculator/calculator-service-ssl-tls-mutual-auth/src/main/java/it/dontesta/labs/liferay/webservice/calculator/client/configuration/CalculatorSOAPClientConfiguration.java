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

package it.dontesta.labs.liferay.webservice.calculator.client.configuration;

import aQute.bnd.annotation.metatype.Meta;

import com.liferay.portal.configuration.metatype.annotations.ExtendedObjectClassDefinition;

/**
 * @author Antonio Musarra
 */
@ExtendedObjectClassDefinition(
	category = "external-services-calculator",
	scope = ExtendedObjectClassDefinition.Scope.SYSTEM
)
@Meta.OCD(
	id = "it.dontesta.labs.liferay.webservice.calculator.client.configuration.CalculatorSOAPClientConfiguration",
	localization = "content/Language",
	name = "calculator-soap-client-configuration-name"
)
public interface CalculatorSOAPClientConfiguration {

	String PID =
		"it.dontesta.labs.liferay.webservice.calculator.client.configuration.CalculatorSOAPClientConfiguration";

	@Meta.AD(
		deflt = "PKCS12", description = "trust-store-description",
		name = "trust-store-name",
		optionLabels = {
			"JKS (Java Keystore)",
			"PKCS (Public-Key Cryptography Standards) #12"
		},
		optionValues = {"JKS", "PKCS12"}, required = false
	)
	public String trustStoreType();

	@Meta.AD(
		deflt = "PKCS12", description = "key-store-description",
		name = "key-store-name",
		optionLabels = {
			"JKS (Java Keystore)",
			"PKCS (Public-Key Cryptography Standards) #12"
		},
		optionValues = {"JKS", "PKCS12"}, required = false
	)
	public String keyStoreType();

	@Meta.AD(
		deflt = "", description = "trust-store-file-description",
		name = "trust-store-file-name", required = false
	)
	public String trustStoreFile();

	@Meta.AD(
		deflt = "", description = "key-store-file-description",
		name = "key-store-file-name", required = false
	)
	public String keyStoreFile();

	@Meta.AD(
		deflt = "", description = "trust-store-password-description",
		name = "trust-store-password-name", required = false,
		type = Meta.Type.Password
	)
	public String trustStorePassword();

	@Meta.AD(
		deflt = "", description = "key-store-password-description",
		name = "key-store-password-name", required = false,
		type = Meta.Type.Password
	)
	public String keyStorePassword();

	@Meta.AD(
		deflt = "TLSv1.2", description = "https-protocol-description",
		name = "https-protocol-name",
		optionLabels = {
			"Transport Layer Security (TLS) 1.1",
			"Transport Layer Security (TLS) 1.2",
			"Transport Layer Security (TLS) 1.3"
		},
		optionValues = {"TLSv1.1", "TLSv1.2", "TLSv1.3"}, required = false
	)
	public String httpsProtocol();

	@Meta.AD(
		deflt = "https://tls-auth.dontesta.it:10443/secure/ws/calculator.asmx",
		description = "soap-endpoint-address-description",
		name = "soap-endpoint-address-name", required = false
	)
	public String soapEndPointAddress();

	@Meta.AD(
		deflt = "true", description = "common-name-check-description",
		name = "common-name-check-name", required = false
	)
	public boolean commonNameCheck();

	@Meta.AD(
		deflt = "true", description = "hostname-verifier-description",
		name = "hostname-verifier-name", required = false
	)
	public boolean hostnameVerifier();

	@Meta.AD(
		deflt = "60000", description = "client-connection-timeout-description",
		name = "client-connection-timeout-name", required = false
	)
	public long clientConnectionTimeOut();

	@Meta.AD(
		deflt = "60000", description = "client-receive-timeout-description",
		name = "client-receive-timeout-name", required = false
	)
	public long clientReceiveTimeout();

}