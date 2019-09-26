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

package it.dontesta.labs.liferay.webservice.vat.service.client.gogoshell.util;

import java.io.StringReader;
import java.io.StringWriter;

import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;
import javax.xml.bind.Marshaller;
import javax.xml.bind.Unmarshaller;

/**
 * @author Antonio Musarra
 *
 */
public class JAXBUtil {

	/**
	 * XML serialization of the class passed as input.
	 *
	 * @param sourceClass
	 * @param objectIn
	 * @param formatted
	 * @see JAXBContext
	 * @see Marshaller
	 * @return
	 * @throws JAXBException
	 */
	public static StringWriter getObjectAsXML(
			Class<?> sourceClass, Object objectIn, boolean formatted)
		throws JAXBException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(sourceClass);

			Marshaller jaxbMarshaller = jaxbContext.createMarshaller();

			StringWriter sw = new StringWriter();
			jaxbMarshaller.setProperty(
				Marshaller.JAXB_FORMATTED_OUTPUT, formatted);
			jaxbMarshaller.marshal(objectIn, sw);

			return sw;
		}
		catch (JAXBException jaxbe) {
			throw new JAXBException(
				"Error occurred during marshalling this source object " +
					objectIn.toString() + " of type " + sourceClass.getName());
		}
	}

	/**
	 * Get Object of the type specified from XML passed as input
	 *
	 * @param xmlObject
	 * @param destinationClass
	 * @see JAXBContext
	 * @see Unmarshaller
	 * @return
	 * @throws JAXBException
	 */
	public static Object getObjectFromXMLString(
			String xmlObject, Class<?> destinationClass)
		throws JAXBException {

		try {
			JAXBContext jaxbContext = JAXBContext.newInstance(destinationClass);

			Unmarshaller unmarshaller = jaxbContext.createUnmarshaller();

			StringReader reader = new StringReader(xmlObject);

			return unmarshaller.unmarshal(reader);
		}
		catch (Exception jaxbEx) {
			throw new JAXBException(
				"Error occurred during unmarshalling this source object " +
					xmlObject.toString() + " to destination type " +
						destinationClass.getName());
		}
	}

	private JAXBUtil() {
	}

}