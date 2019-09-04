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

package it.dontesta.labs.liferay.webservice.calculator.gogoshell;

import it.dontesta.labs.liferay.webservice.calculator.api.Calculator;
import it.dontesta.labs.liferay.webservice.calculator.exception.CalculatorOperationException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Antonio Musarra
 */
@Component(
	property = {
		"osgi.command.function=add",
		"osgi.command.function=divide",
		"osgi.command.function=multiply",
		"osgi.command.function=subtract",
		"osgi.command.scope=calculator"
	},
	service = Object.class
)
public class CalculatorCommand implements Calculator {

	public int add(int number1, int number2)
		throws CalculatorOperationException {

		return getCalculatorService().add(number1, number2);
	}

	public int divide(int number1, int number2)
		throws CalculatorOperationException {

		return getCalculatorService().divide(number1, number2);
	}

	public Calculator getCalculatorService() {
		return _calculator;
	}

	public int multiply(int number1, int number2)
		throws CalculatorOperationException {

		return getCalculatorService().multiply(number1, number2);
	}

	public int subtract(int number1, int number2)
		throws CalculatorOperationException {

		return getCalculatorService().subtract(number1, number2);
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private volatile Calculator _calculator;

}