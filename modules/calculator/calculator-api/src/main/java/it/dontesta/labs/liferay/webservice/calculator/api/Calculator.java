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

package it.dontesta.labs.liferay.webservice.calculator.api;

import it.dontesta.labs.liferay.webservice.calculator.exception.CalculatorOperationException;

/**
 * @author Antonio Musarra
 */
public interface Calculator {

	/**
	 * Performs the addition operation between two integers.
	 *
	 * @param number1 First term
	 * @param number2 Second term
	 * @return Returns the result of the operation.
	 * @throws CalculatorOperationException
	 */
	public int add(int number1, int number2)
		throws CalculatorOperationException;

	/**
	 * Performs the division operation between two integers.
	 *
	 * @param number1 First term
	 * @param number2 Second term
	 * @return Returns the result of the operation.
	 * @throws CalculatorOperationException
	 */
	public int divide(int number1, int number2)
		throws CalculatorOperationException;

	/**
	 * Performs the multiplication operation between two integers.
	 *
	 * @param number1 First term
	 * @param number2 Second term
	 * @return Returns the result of the operation.
	 * @throws CalculatorOperationException
	 */
	public int multiply(int number1, int number2)
		throws CalculatorOperationException;

	/**
	 * Performs the subtraction operation between two integers.
	 *
	 * @param number1 First term
	 * @param number2 Second term
	 * @return Returns the result of the operation.
	 * @throws CalculatorOperationException
	 */
	public int subtract(int number1, int number2)
		throws CalculatorOperationException;

}