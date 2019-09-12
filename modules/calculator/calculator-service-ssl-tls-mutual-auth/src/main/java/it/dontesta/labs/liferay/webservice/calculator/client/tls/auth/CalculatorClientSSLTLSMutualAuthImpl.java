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

package it.dontesta.labs.liferay.webservice.calculator.client.tls.auth;

import com.liferay.portal.configuration.metatype.bnd.util.ConfigurableUtil;
import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.module.framework.ModuleServiceLifecycle;
import com.liferay.portal.kernel.util.Validator;

import it.dontesta.labs.liferay.webservice.calculator.api.Calculator;
import it.dontesta.labs.liferay.webservice.calculator.client.configuration.CalculatorSOAPClientConfiguration;
import it.dontesta.labs.liferay.webservice.calculator.exception.CalculatorOperationException;
import it.dontesta.labs.liferay.webservice.calculator.exception.CalculatorServiceException;

import java.io.IOException;
import java.io.InputStream;

import java.nio.file.Files;
import java.nio.file.Paths;

import java.security.KeyManagementException;
import java.security.KeyStore;
import java.security.KeyStoreException;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import java.security.UnrecoverableKeyException;
import java.security.cert.CertificateException;

import java.util.Map;

import javax.net.ssl.KeyManagerFactory;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSocketFactory;
import javax.net.ssl.TrustManagerFactory;

import javax.xml.ws.BindingProvider;
import javax.xml.ws.WebServiceException;

import org.apache.cxf.configuration.jsse.TLSClientParameters;
import org.apache.cxf.endpoint.Client;
import org.apache.cxf.frontend.ClientProxy;
import org.apache.cxf.transport.http.HTTPConduit;
import org.apache.cxf.transports.http.configuration.HTTPClientPolicy;

import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Modified;
import org.osgi.service.component.annotations.Reference;

import org.tempuri.CalculatorSoap;

/**
 * @author Antonio Musarra
 */
@Component(
	configurationPid = CalculatorSOAPClientConfiguration.PID, immediate = true,
	property = "secure=mutual-auth", service = Calculator.class
)
public class CalculatorClientSSLTLSMutualAuthImpl implements Calculator {

	@Override
	public int add(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().add(number1, number2);
		}
		catch (CalculatorServiceException cse) {
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Override
	public int divide(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().divide(number1, number2);
		}
		catch (CalculatorServiceException cse) {
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Override
	public int multiply(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().multiply(number1, number2);
		}
		catch (CalculatorServiceException cse) {
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Override
	public int subtract(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().subtract(number1, number2);
		}
		catch (CalculatorServiceException cse) {
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Activate
	protected void activate(Map<String, Object> properties) {
		_calculatorSOAPClientConfiguration =
			ConfigurableUtil.createConfigurable(
				CalculatorSOAPClientConfiguration.class, properties);
	}

	@Modified
	protected void modified(Map<String, Object> properties) {
		try {
			if (_log.isInfoEnabled()) {
				_log.info(
					String.format(
						"The configuration %s was modified and re-run the service setup.",
						CalculatorSOAPClientConfiguration.PID));
			}

			activate(properties);

			_getService(true);
		}
		catch (CalculatorServiceException cse) {
			if (_log.isErrorEnabled()) {
				_log.error("Calculator Service setup failed!", cse);
			}
		}
	}

	@Reference(target = ModuleServiceLifecycle.PORTAL_INITIALIZED, unbind = "-")
	protected void setModuleServiceLifecycle(
		ModuleServiceLifecycle moduleServiceLifecycle) {
	}

	private long _getClientConnectionTimeOut() {
		return _calculatorSOAPClientConfiguration.clientConnectionTimeOut();
	}

	private long _getClientReceiveTimeout() {
		return _calculatorSOAPClientConfiguration.clientReceiveTimeout();
	}

	private boolean _getCommonNameCheck() {
		return _calculatorSOAPClientConfiguration.commonNameCheck();
	}

	private boolean _getHostNameVerifier() {
		return _calculatorSOAPClientConfiguration.hostnameVerifier();
	}

	private String _getHTTPSProtocol() {
		String httpsProtocol =
			_calculatorSOAPClientConfiguration.httpsProtocol();

		if (Validator.isNotNull(httpsProtocol)) {
			return httpsProtocol;
		}

		return _HTTPS_DEFAULT_PROTOCOL;
	}

	private InputStream _getKeyStoreFile() throws IOException {
		String keyStoreFile = _calculatorSOAPClientConfiguration.keyStoreFile();

		if (Validator.isNotNull(keyStoreFile)) {
			return Files.newInputStream(Paths.get(keyStoreFile));
		}

		return CalculatorClientSSLTLSMutualAuthImpl.class.getClassLoader(
		).getResourceAsStream(
			_KEYSTORE_FILE_DEFAULT_LOCATION
		);
	}

	private String _getKeyStorePassword() {
		String keyStorePassword =
			_calculatorSOAPClientConfiguration.keyStorePassword();

		if (Validator.isNotNull(keyStorePassword)) {
			return keyStorePassword;
		}

		return _KEYSTORE_DEFAULT_PASSWORD;
	}

	private String _getKeyStoreType() {
		String keyStoreType = _calculatorSOAPClientConfiguration.keyStoreType();

		if (Validator.isNotNull(keyStoreType)) {
			return keyStoreType;
		}

		return _KEYSTORE_DEFAULT_TYPE;
	}

	private CalculatorSoap _getService() throws CalculatorServiceException {
		return _getService(false);
	}

	private CalculatorSoap _getService(boolean renew)
		throws CalculatorServiceException {

		if (Validator.isNull(_calculatorSoap) || renew) {
			try {
				org.tempuri.Calculator calculator =
					new org.tempuri.Calculator();
				_calculatorSoap = calculator.getCalculatorSoap();

				((BindingProvider)_calculatorSoap).getRequestContext(
				).put(
					BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
					_getSOAPEndPointAddress()
				);

				_setUpSecurityClientConnection();
			}
			catch (CertificateException | IOException | KeyManagementException |
				   KeyStoreException | NoSuchAlgorithmException |
				   UnrecoverableKeyException | WebServiceException wse) {

				if (_log.isErrorEnabled()) {
					_log.error("Calculator Service setup failed!", wse);
				}

				throw new CalculatorServiceException(wse.getMessage(), wse);
			}

			return _calculatorSoap;
		}
		else {
			return _calculatorSoap;
		}
	}

	private String _getSOAPEndPointAddress() {
		return _calculatorSOAPClientConfiguration.soapEndPointAddress();
	}

	private SSLSocketFactory _getSSLConnectionSocketFactory()
		throws CertificateException, IOException, KeyManagementException,
			   KeyStoreException, NoSuchAlgorithmException,
			   UnrecoverableKeyException {

		SSLContext sslContext = SSLContext.getInstance(_getHTTPSProtocol());

		// Create and load the truststore

		KeyStore trustStore = KeyStore.getInstance(_getTrustStoreType());

		trustStore.load(
			_getTrustStoreFile(), _getTrustStorePassword().toCharArray());

		// Create and load the keystore

		KeyStore keyStore = KeyStore.getInstance(_getKeyStoreType());

		keyStore.load(_getKeyStoreFile(), _getKeyStorePassword().toCharArray());

		// Create and initialize the truststore manager

		TrustManagerFactory trustManagerFactory =
			TrustManagerFactory.getInstance("SunX509");

		trustManagerFactory.init(trustStore);

		// Create and initialize the keystore manager

		KeyManagerFactory keyManagerFactory = KeyManagerFactory.getInstance(
			KeyManagerFactory.getDefaultAlgorithm());

		keyManagerFactory.init(keyStore, _getKeyStorePassword().toCharArray());

		sslContext.init(
			keyManagerFactory.getKeyManagers(),
			trustManagerFactory.getTrustManagers(), new SecureRandom());

		return sslContext.getSocketFactory();
	}

	private InputStream _getTrustStoreFile() throws IOException {
		String trustStoreFile =
			_calculatorSOAPClientConfiguration.trustStoreFile();

		if (Validator.isNotNull(trustStoreFile)) {
			return Files.newInputStream(Paths.get(trustStoreFile));
		}

		return CalculatorClientSSLTLSMutualAuthImpl.class.getClassLoader(
		).getResourceAsStream(
			_TRUSTSTORE_FILE_DEFAULT_LOCATION
		);
	}

	private String _getTrustStorePassword() {
		String password =
			_calculatorSOAPClientConfiguration.trustStorePassword();

		if (Validator.isNotNull(password)) {
			return password;
		}

		return _TRUSTSTORE_DEFAULT_PASSWORD;
	}

	private String _getTrustStoreType() {
		String keystoreType =
			_calculatorSOAPClientConfiguration.trustStoreType();

		if (Validator.isNotNull(keystoreType)) {
			return keystoreType;
		}

		return _TRUSTSTORE_DEFAULT_TYPE;
	}

	private void _setUpSecurityClientConnection()
		throws CalculatorServiceException, CertificateException, IOException,
			   KeyManagementException, KeyStoreException,
			   NoSuchAlgorithmException, UnrecoverableKeyException {

		if (Validator.isNull(_calculatorSoap)) {
			throw new CalculatorServiceException(
				"SOAP Calculator cannot be null.");
		}

		// Get the underlying http conduit of the client proxy

		Client client = ClientProxy.getClient(_calculatorSoap);

		HTTPConduit http = (HTTPConduit)client.getConduit();

		HTTPClientPolicy httpClientPolicy = new HTTPClientPolicy();

		httpClientPolicy.setConnectionTimeout(_getClientConnectionTimeOut());
		httpClientPolicy.setReceiveTimeout(_getClientReceiveTimeout());
		httpClientPolicy.setAllowChunking(false);

		http.setClient(httpClientPolicy);

		// Set the TLS client parameters

		TLSClientParameters parameters = new TLSClientParameters();

		parameters.setDisableCNCheck(_getCommonNameCheck());

		if (!_getHostNameVerifier()) {
			parameters.setHostnameVerifier((hostName, session) -> true);
		}

		parameters.setSSLSocketFactory(_getSSLConnectionSocketFactory());

		http.setTlsClientParameters(parameters);
	}

	private static final String _HTTPS_DEFAULT_PROTOCOL = "TLSv1.2";

	private static final String _KEYSTORE_DEFAULT_PASSWORD = "secret";

	private static final String _KEYSTORE_DEFAULT_TYPE = "PKCS12";

	private static final String _KEYSTORE_FILE_DEFAULT_LOCATION =
		"META-INF/keystore/tls-client.dontesta.it.p12";

	private static final String _TRUSTSTORE_DEFAULT_PASSWORD = "secret";

	private static final String _TRUSTSTORE_DEFAULT_TYPE = "PKCS12";

	private static final String _TRUSTSTORE_FILE_DEFAULT_LOCATION =
		"META-INF/keystore/client-truststore.p12";

	private CalculatorSoap _calculatorSoap;
	private volatile CalculatorSOAPClientConfiguration
		_calculatorSOAPClientConfiguration;
	private Log _log = LogFactoryUtil.getLog(
		CalculatorClientSSLTLSMutualAuthImpl.class);

}