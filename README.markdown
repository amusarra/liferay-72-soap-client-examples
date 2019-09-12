

# Liferay 7.2 SOAP Client Examples

[![Antonio Musarra's Blog](https://img.shields.io/badge/maintainer-Antonio_Musarra's_Blog-purple.svg?colorB=6e60cc)](https://www.dontesta.it)
[![Build Status](https://travis-ci.org/amusarra//liferay-72-soap-client-examples.svg?branch=master)](https://travis-ci.org/amusarra/liferay-72-soap-client-examples)
[![Twitter Follow](https://img.shields.io/twitter/follow/antonio_musarra.svg?style=social&label=%40antonio_musarra%20on%20Twitter&style=plastic)](https://twitter.com/antonio_musarra)

At the 2016 Liferay Symposium (Italy) I presented the topic: [How to develop SOAP 
and REST services in JAX-WS and JAX-RS standard on Liferay](https://www.slideshare.net/amusarra/jaxws-e-jaxrs). During the presentation I illustrated how to expose both REST (Representational State Transfer) and SOAP (Simple Object Access Protocol) services for each application using the [Liferay Extender](https://portal.liferay.dev/docs/7-0/tutorials/-/knowledge_base/t/jax-ws-and-jax-rs).

In this project I want to show you how to build a SOAP JAX-WS (Java API for XML Web Services) client using the Liferay infrastructure. Let's start with a concrete example. The project was implemented and tested on **Liferay 7.2 Community Edition GA1**.

The project code is still valid for the 7.0 and 7.1 versions of Liferay. Before trying the project on Liferay 7.0, I recommend reading this [LPS – Supplied JAX-WS implementation not working](https://issues.liferay.com/browse/LPS-67253)

Let’s consider the SOAP Calculator service whose [WSDL](http://www.dneonline.com/calculator.asmx) (Web Services Description Language) descriptor is available at the following address http://www.dneonline.com/calculator.asmx which exposes the four arithmetic operations between two integers.



## 1. Quick Start
Follow the procedure below to try out the project immediately. You need to replace or set the variable __$LIFERAY_HOME__ with the installation directory of your Liferay instance.



```bash
$ git clone https://github.com/amusarra/liferay-72-soap-client-examples.git
$ cd liferay-72-soap-client-examples/
$ echo "liferay.workspace.home.dir=$LIFERAY_HOME" > gradle-local.properties
$ ./gradlew clean deploy
$ blade sh lb Calculator
```

Console 1 - Clone, build and debloy



The list to see are the six bundles just installed against the deployment action.

```bash
g! lb it.dontesta.labs.liferay
START LEVEL 20
   ID|State      |Level|Name
   72|Resolved   |   10|Liferay Portal Remote SOAP Extender Impl Fragment (1.0.0)|1.0.0
   73|Active     |   10|Calculator Web Application (1.0.0)|1.0.0
   75|Active     |   10|Calculator Client API (1.0.0)|1.0.0
   77|Active     |   10|Calculator SOAP Client Implementation (1.0.0)|1.0.0
   78|Active     |   10|Calculator Client Gogo Shell Commands (1.0.0)|1.0.0
 1086|Active     |   10|Calculator SOAP Client SSL/TLS Mutual Auth Impl (1.0.0)|1.0.0

```

Console 2 - Check status of the Calculator bundle



For more information refer to article [How to implement a SOAP client using JAX-WS Liferay infrastructure](http://bit.ly/2lA0ukT) 
published on [Antonio Musarra's Blog](https://www.dontesta.it).



## 2. How to implement a SOAP client that supports Mutual Authentication
There are situations where access to SOAP services can be protected through a mutual authentication mechanism over SSL/TLS.**In this situation, how do you build the client**?

Here I don't want to bore you in technical details about the mutual authentication mechanism, so I recommend reading my last article [Apache HTTP 2.4: How to Build a Docker Image for SSL/TLS Mutual Authentication](https://dzone.com/articles/apache-http-24-how-to-build-a-docker-image-for-ssl) published on DZone Portal.

We recall that Liferay uses the [Apache CXF framework](https://cxf.apache.org/) (version 3.2.5 on Liferay 7.2 GA1). When using an **https URL**, Apache CXF will, by default, use the certs and keystores that are part of the JDK. I have in fact configured the keystore and truststore at the system level and the mutual authentication works correctly.



```bash
-Djavax.net.ssl.trustStore=
-Djavax.net.ssl.trustStorePassword=
-Djavax.net.ssl.trustStoreType=
-Djavax.net.ssl.keyStore=
-Djavax.net.ssl.keyStorePassword=
-Djavax.net.ssl.keyStoreType=
```

Console 3 - System settings for configure truststore and keystore



For more information about Java security implementation, I recommend reading the [Java Secure Socket Extension (JSSE) Reference Guide](https://docs.oracle.com/javase/8/docs/technotes/guides/security/jsse/JSSERefGuide.html#InstallationAndCustomization) and [Java Cryptography Architecture
Standard Algorithm Name Documentation for JDK 8](https://docs.oracle.com/javase/8/docs/technotes/guides/security/StandardNames.html#KeyStore)

For many HTTPs applications, that is enough and no configuration is necessary. However, when using custom client certificates or self signed server certificates or similar, you may need to specifically configure in the keystores and trust managers and such to establish the SSL connection.

Also we can't use the **SSLContext** setting via **JAX-WS API** because ignored by Apache CXF and now deprecated.



```java
((BindingProvider)_calculatorSoap).getRequestContext().put(
		"com.sun.xml.internal.ws.transport.https.client.SSLSocketFactory",
  	_getSSLConnectionSocketFactory()
);
```

Source Code 1 - Setting SSLSocketFactory



We can't even use *HttpsURLConnection.setDefaultSSLSocketFactory(_getSSLConnectionSocketFactory())* because ignored by Apache CXF unless I configure the *useHttpsURLConnectionDefaultSslSocketFactory* property. Unfortunately, the **Liferay SOAP Extender** does not allow to act on the configuration of Apache CXF in depth.

In order to achieve our goal we need to be able to use Apache CXF directly. Therefore it is necessary create an [OSGi Fragment](https://osgi.org/specification/osgi.core/7.0.0/framework.module.html#framework.module.fragmenthost) to export some Apache CXF packages. The fragment must be made on the module **Liferay Portal Remote SOAP Extender Implementation**

The module [liferay-portal-remote-soap-extender-impl-fragment](https://github.com/amusarra/liferay-72-soap-client-examples/tree/master/modules/liferay-hooks/liferay-portal-remote-soap-extender-impl-fragment) implements the fragment above. Following the source of the bnd.bnd where it is evident the fragment and the Apache CXF exported package.



```bash
Bundle-Name: Liferay Portal Remote SOAP Extender Implementation Fragment
Bundle-SymbolicName: it.dontesta.labs.liferay.portal.remote.soap.extender.impl
Bundle-Version: 1.0.0
Fragment-Host: com.liferay.portal.remote.soap.extender.impl;bundle-version="3.0.6"

Export-Package: \
	org.apache.cxf.frontend;version="3.2.5"
```

Source Code 2 - The bnd.bnd of the OSGi Fragment Liferay Portal Remote SOAP Extender Implementation



At this point we can use Apache CXF to build our SOAP client that is able to perform mutual authentication over SSL / TLS. Following is the base code of the [calculator-service-ssl-tls-mutual-auth](https://github.com/amusarra/liferay-72-soap-client-examples/tree/master/modules/calculator/calculator-service-ssl-tls-mutual-auth) module that implements the SOAP client with the support of mutual authentication.

The example implemented refers to the SOAP Calculator service whose [WSDL](http://www.dneonline.com/calculator.asmx) (Web Services Description Language) descriptor is available at the following address http://www.dneonline.com/calculator.asmx which exposes the four arithmetic operations between two integers.



1. The first code block performs the SOAP endpoint setup
2. The second block of code sets the proxy client setup, the http policy and finally the TLS connection properties
3. The third code block sets the SSL context, in particular it performs the KeyStore and TrustStore setup



```java
private CalculatorSoap _getService(boolean renew)
	throws CalculatorServiceException {

	try {
		org.tempuri.Calculator calculator =
			new org.tempuri.Calculator();
		_calculatorSoap = calculator.getCalculatorSoap();

		((BindingProvider)_calculatorSoap).getRequestContext(
		).put(
			BindingProvider.ENDPOINT_ADDRESS_PROPERTY,
			_getSOAPEndPointAddress()
		);

		...
		
	}
	catch (CertificateException | IOException | KeyManagementException |
		   KeyStoreException | NoSuchAlgorithmException |
		   UnrecoverableKeyException | WebServiceException wse) {

		if (_log.isErrorEnabled()) {
			_log.error(wse.getMessage(), wse);
		}

		throw new CalculatorServiceException(wse.getMessage(), wse);
	}

}
```

Source Code 3 - Core implementation of the _getService() method of the service CalculatorClientSSLTLSMutualAuthImpl



```java
private void _setUpSecurityClientConnection()
	throws CalculatorServiceException, CertificateException, IOException,
		   KeyManagementException, KeyStoreException,
		   NoSuchAlgorithmException, UnrecoverableKeyException {

    ...

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
```

Source Code 4 - Core implementation of the _setUpSecurityClientConnection() method of the service CalculatorClientSSLTLSMutualAuthImpl



```java
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
```

Source Code 5 - Core implementation of the _getSSLConnectionSocketFactory() method of the service CalculatorClientSSLTLSMutualAuthImpl



To start a mutual authentication process we need two keystores, one that contains public certificates (**truststore**) and one that contains private certificates (**keystore**). This module contains within it both the keystore and truststore that can be used with the SOAP test service that I have prepared.

In the structure below we can see the keystore (tls-client.dontesta.it.p12) and the truststore (client-truststore.p12). Both are in the standard [PKCS#12](https://en.wikipedia.org/wiki/PKCS_12) format. If you are curious about how these certificates were generated read [How The Certificates Were Generated](https://dzone.com/articles/apache-http-24-how-to-build-a-docker-image-for-ssl).



```bash
├── calculator-service-ssl-tls-mutual-auth
   └── src
       └── main
           └── resources
               ├── META-INF
                  ├── keystore
                  │   ├── client-truststore.p12
                  │   └── tls-client.dontesta.it.p12
                  └── wsdl
                      └── calculator.wsdl
```

Console 4 - Resources of the calculator-service-ssl-tls-mutual-auth module



I also wanted to implement the configuration bonus for this SOAP client. The figures show a part of the module configuration. The following parameters can be configured:



1. TrustStore and KeyStore Type
2. Path of the TrustStore and KeyStore
3. Password of the TrustStore and KeyStore
4. HTTPS Protocol
5. SOAP EndPoint Address
6. Check CN (Common Name)
7. Check Hostname
8. Connection Timeout (ms)
9. Receive Timeout (ms)



![ConfigurationSOAPExternalService_1](docs/images/ConfigurationSOAPExternalService_1.png)

Figure 1 - System Settings configuration for Calculator Service



![ConfigurationSOAPExternalService_2](docs/images/ConfigurationSOAPExternalService_2.png)

Figure 2 - Configuration detail of the Calculator Service



## 3. How to run a client test

To test our Calculator service SOAP client, we need the service to be protected by the mutual authentication mechanism. To facilitate the task we could take advantage of the [Apache HTTP 2.4 - Docker image for SSL / TLS Mutual Authentication](https://github.com/amusarra/docker-apache-ssl-tls-mutual-authentication).



```bash
$ docker run -i -t -d \
	-p 10443:10443 \
	-e API_BASE_PATH='/secure/ws' \
	-e API_BACKEND_BASE_URL='http://www.dneonline.com' \
	-e APACHE_PROXY_PRESERVE_HOST=Off \
	--name=apache-ssl-tls-mutual-authentication \
	amusarra/apache-ssl-tls-mutual-authentication:1.2.3
```

Console 5 - Start of the SOAP Calculator service protected by the mutual authentication mechanism.



The new WSDL of the service will be available at the URL https://localhost:10443/secure/ws/calculator.asmx?WSDL. To avoid the **SSL_ERROR_BAD_CERT_DOMAIN** error from the browser, the following line must be added to your hosts file.



```bash
##
# Mutual authentication service via Apache HTTPD
##
127.0.0.1       tls-auth.dontesta.it
```

Source Code 6 - File /etc/hosts



After adding entry on hosts file, the new WSDL of the service will be available at the URL https://tls-auth.dontesta.it:10443/secure/ws/calculator.asmx?WSDL 

The two figures below show the process of mutual authentication to access the WSDL via browser. In this case the client certificate was installed on the browser.



![MutualAuthenticationViaBrowser_1](docs/images/MutualAuthenticationViaBrowser_1.png)

Figure 3 - Mutual Authentication request for the WSDL resource



![MutualAuthenticationViaBrowser_2](docs/images/MutualAuthenticationViaBrowser_2.png)

Figure 4 - WSDL document of the Calculator SOAP Service



Now that the SOAP service is active, we can test our new client. I remind you that we have two SOAP clients for the same service: 

1. The client that directly accesses the service without any security mechanism (modulo **calculator-service**)
2. The client that instead uses the mutual authentication mechanism (modulo **calculator-service-ssl-tls-mutual-auth**).

We must therefore stop the first service, in this way both the web module and the Gogo Shell module, will use the SOAP client in mutual authentication.



```bash
g! stop 77
g! lb Calculator
START LEVEL 20
   ID|State      |Level|Name
   73|Active     |   10|Calculator Web Application (1.0.0)|1.0.0
   75|Active     |   10|Calculator Client API (1.0.0)|1.0.0
   77|Resolved   |   10|Calculator SOAP Client Implementation (1.0.0)|1.0.0
   78|Active     |   10|Calculator Client Gogo Shell Commands (1.0.0)|1.0.0
 1086|Active     |   10|Calculator SOAP Client SSL/TLS Mutual Auth Imp (1.0.0)|1.0.0
```

Console 6 - Stop the bundle Calculator SOAP Client Implementation



If we wanted to make sure that both the Gogo Shell commands and the MVC portlet were linked to the new service we could check with the command scr: info ${component name}. As you can see, the linked service is the correct one: **it.dontesta.labs.liferay.webservice.calculator.client.tls**



```bash
g! scr:info it.dontesta.labs.liferay.webservice.calculator.gogoshell.CalculatorCommand
Component Description: it.dontesta.labs.liferay.webservice.calculator.gogoshell.CalculatorCommand
=================================================================================================
Class:         it.dontesta.labs.liferay.webservice.calculator.gogoshell.CalculatorCommand
Bundle:        78 (it.dontesta.labs.liferay.webservice.calculator.gogoshell:1.0.0)
Enabled:       true
Immediate:     false
Services:      [java.lang.Object]
Scope:         singleton
Config PID(s): [it.dontesta.labs.liferay.webservice.calculator.gogoshell.CalculatorCommand], Policy: optional
Base Props:    (2 entries)
  osgi.command.function<String[]> = [add, divide, multiply, subtract]
  osgi.command.scope<String> = calculator

Component Configuration Id: 4892
--------------------------------
State:        ACTIVE
Service:      10978 [java.lang.Object]
    Used by bundle 936 (com.liferay.portal.remote.cxf.common:5.0.5)
Config Props: (4 entries)
  component.id<Long> = 4892
  component.name<String> = it.dontesta.labs.liferay.webservice.calculator.gogoshell.CalculatorCommand
  osgi.command.function<String[]> = [add, divide, multiply, subtract]
  osgi.command.scope<String> = calculator
References:   (total 1)
  - _calculator: it.dontesta.labs.liferay.webservice.calculator.api.Calculator SATISFIED 1..1 dynamic+greedy
    target=(*) scope=bundle (1 binding):
    * Bound to [10973] from bundle 1086 (it.dontesta.labs.liferay.webservice.calculator.client.tls:1.0.0)
```

Console 7 - src:info for checking the bound service for Gogo Shell Command



```bash
g! scr:info it.dontesta.labs.liferay.web.calculator.portlet.action.AddOperationMVCActionCommand
Component Description: it.dontesta.labs.liferay.web.calculator.portlet.action.AddOperationMVCActionCommand
==========================================================================================================
Class:         it.dontesta.labs.liferay.web.calculator.portlet.action.AddOperationMVCActionCommand
Bundle:        73 (it.dontesta.labs.liferay.web.calculator:1.0.0)
Enabled:       true
Immediate:     true
Services:      [com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand]
Scope:         singleton
Config PID(s): [it.dontesta.labs.liferay.web.calculator.portlet.action.AddOperationMVCActionCommand], Policy: optional
Base Props:    (2 entries)
  javax.portlet.name<String> = it_dontesta_labs_liferay_web_calculator_CalculatorAppPortlet
  mvc.command.name<String> = /calculator/add-operation

Component Configuration Id: 4887
--------------------------------
State:        ACTIVE
Service:      10974 [com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand]
    Used by bundle 0 (org.eclipse.osgi:3.13.0.LIFERAY-PATCHED-5)
Config Props: (4 entries)
  component.id<Long> = 4887
  component.name<String> = it.dontesta.labs.liferay.web.calculator.portlet.action.AddOperationMVCActionCommand
  javax.portlet.name<String> = it_dontesta_labs_liferay_web_calculator_CalculatorAppPortlet
  mvc.command.name<String> = /calculator/add-operation
References:   (total 1)
  - _calculator: it.dontesta.labs.liferay.webservice.calculator.api.Calculator SATISFIED 1..1 static+greedy
    target=(*) scope=bundle (1 binding):
    * Bound to [10973] from bundle 1086 (it.dontesta.labs.liferay.webservice.calculator.client.tls:1.0.0)
```

Console 8 - src:info for checking the bound service for AddOperationMVCActionCommand



For more information refer to article [How to implement a SOAP client using JAX-WS Liferay infrastructure](http://bit.ly/2lA0ukT) 
published on [Antonio Musarra's Blog](https://www.dontesta.it).



## License

MIT License

Copyright 2019 Antonio Musarra's Blog - https://www.dontesta.it

Permission is hereby granted, free of charge, to any person obtaining a copy
of this software and associated documentation files (the "Software"), to deal
in the Software without restriction, including without limitation the rights
to use, copy, modify, merge, publish, distribute, sublicense, and/or sell copies
of the Software, and to permit persons to whom the Software is furnished to do so,
subject to the following conditions:

The above copyright notice and this permission notice shall be included in all
copies or substantial portions of the Software.

THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER LIABILITY,
WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM, OUT OF OR IN
CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE SOFTWARE.