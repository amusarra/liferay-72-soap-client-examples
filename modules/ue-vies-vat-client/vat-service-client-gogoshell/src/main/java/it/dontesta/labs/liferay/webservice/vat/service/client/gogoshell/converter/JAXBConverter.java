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

package it.dontesta.labs.liferay.webservice.vat.service.client.gogoshell.converter;

import it.dontesta.labs.liferay.webservice.vat.service.client.gogoshell.util.JAXBUtil;

import javax.xml.bind.JAXBException;

import it.dontesta.labs.liferay.webservice.vat.service.model.VatResponse;
import org.apache.felix.service.command.Converter;

import org.osgi.service.component.annotations.Component;

/**
 * @author Antonio Musarra
 */
@Component(immediate = true, service = Converter.class)
public class JAXBConverter implements Converter {

	@Override
	public Object convert(Class<?> desiredType, Object in) throws Exception {
		return null;
	}

	@Override
	public CharSequence format(Object target, int level, Converter escape)
		throws Exception {

		if (target instanceof VatResponse) {
			return _toXML(target);
		}
		else {
			return null;
		}
	}

	private CharSequence _toXML(Object target) throws JAXBException {
		return JAXBUtil.getObjectAsXML(
			target.getClass(), target, true
		).getBuffer();
	}

}