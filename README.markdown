# Liferay 7.2 SOAP Client Examples
[![Antonio Musarra's Blog](https://img.shields.io/badge/maintainer-Antonio_Musarra's_Blog-purple.svg?colorB=6e60cc)](https://www.dontesta.it)
[![Build Status](https://travis-ci.org/amusarra//liferay-72-soap-client-examples.svg?branch=master)](https://travis-ci.org/amusarra/liferay-72-soap-client-examples)
[![Twitter Follow](https://img.shields.io/twitter/follow/antonio_musarra.svg?style=social&label=%40antonio_musarra%20on%20Twitter&style=plastic)](https://twitter.com/antonio_musarra)

At the 2016 Liferay Symposium (Italy) I presented the topic: [How to develop SOAP 
and REST services in JAX-WS and JAX-RS standard on Liferay](https://www.slideshare.net/amusarra/jaxws-e-jaxrs). During the 
presentation I illustrated how to expose both REST (Representational State Transfer) 
and SOAP (Simple Object Access Protocol) services for each application using 
the [Liferay Extender](https://portal.liferay.dev/docs/7-0/tutorials/-/knowledge_base/t/jax-ws-and-jax-rs).

In this project I want to show you how to build a SOAP JAX-WS (Java API for XML 
Web Services) client using the Liferay infrastructure. Let's start with a 
concrete example.

The project was implemented and tested on Liferay 7.2 Community Edition GA1.

The project code is still valid for the 7.0 and 7.1 versions of Liferay. 
Before trying the project on Liferay 7.0, I recommend reading this 
[LPS – Supplied JAX-WS implementation not working](https://issues.liferay.com/browse/LPS-67253)

## 1. Quick Start
Follow the procedure below to try out the project immediately. You need to replace 
or set the variable __$LIFERAY_HOME__ with the installation directory of 
your Liferay instance.

```bash
$ git clone https://github.com/amusarra/liferay-72-soap-client-examples.git
$ cd liferay-72-soap-client-examples/
$ echo "liferay.workspace.home.dir=$LIFERAY_HOME" > gradle-local.properties
$ ./gradlew clean deploy
$ blade sh lb Calculator
```

The list to see are the four bundles just installed against the 
deployment action.

```bash
lb Calculator
START LEVEL 20
   ID|State      |Level|Name
 1080|Active     |   10|Calculator Client API (1.0.0)|1.0.0
 1082|Active     |   10|Calculator Client Gogo Shell Commands (1.0.0)|1.0.0
 1084|Active     |   10|Calculator Web Application (1.0.0)|1.0.0
 1085|Active     |   10|Calculator SOAP Client Implementation (1.0.0)|1.0.0
```

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