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

package it.dontesta.labs.liferay.webservice.calculator.client.soap;

import com.liferay.portal.kernel.log.Log;
import com.liferay.portal.kernel.log.LogFactoryUtil;
import com.liferay.portal.kernel.util.Validator;
import it.dontesta.labs.liferay.webservice.calculator.api.Calculator;
import it.dontesta.labs.liferay.webservice.calculator.exception.CalculatorOperationException;
import it.dontesta.labs.liferay.webservice.calculator.exception.CalculatorServiceException;
import org.osgi.service.component.annotations.Component;
import org.tempuri.CalculatorSoap;

import javax.xml.ws.WebServiceException;

/**
 * @author Antonio Musarra
 */
@Component(
	immediate = true, property = "secure=off", service = Calculator.class
)
public class CalculatorClientImpl implements Calculator {

	@Override
	public int add(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().add(number1, number2);
		}
		catch (CalculatorServiceException | WebServiceException cse) {
			if (_log.isErrorEnabled()) {
				_log.error(cse.getMessage(), cse);
			}
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Override
	public int divide(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().divide(number1, number2);
		}
		catch (CalculatorServiceException | WebServiceException cse) {
			if (_log.isErrorEnabled()) {
				_log.error(cse.getMessage(), cse);
			}
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Override
	public int multiply(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().multiply(number1, number2);
		}
		catch (CalculatorServiceException | WebServiceException cse) {
			if (_log.isErrorEnabled()) {
				_log.error(cse.getMessage(), cse);
			}
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	@Override
	public int subtract(int number1, int number2)
		throws CalculatorOperationException {

		try {
			return _getService().subtract(number1, number2);
		}
		catch (CalculatorServiceException | WebServiceException cse) {
			if (_log.isErrorEnabled()) {
				_log.error(cse.getMessage(), cse);
			}
			throw new CalculatorOperationException(cse.getMessage(), cse);
		}
	}

	private CalculatorSoap _getService() throws CalculatorServiceException {
		if (Validator.isNull(_calculatorSoap)) {
			try {
				org.tempuri.Calculator calculator =
					new org.tempuri.Calculator();
				_calculatorSoap = calculator.getCalculatorSoap();
			}
			catch (WebServiceException wse) {
				if (_log.isErrorEnabled()) {
					_log.error(wse.getMessage(), wse);
				}
				throw new CalculatorServiceException(wse.getMessage(), wse);
			}

		}
		return _calculatorSoap;
	}

	private CalculatorSoap _calculatorSoap;

	private Log _log = LogFactoryUtil.getLog(
		CalculatorClientImpl.class);

}