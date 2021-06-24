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

package it.dontesta.labs.liferay.web.calculator.constants;

import com.liferay.portal.kernel.util.WebKeys;

/**
 * @author Antonio Musarra
 */
public interface CalculatorAppWebKeys extends WebKeys {

	public static final String ACTION_COMMAND_NAME = "ACTION_COMMAND_NAME";

	public static final String ADD_OPERATION_NAME = "add";

	public static final String ADD_OPERATION_RESULT = "addOperationResult";

	public static final String ADD_OPERATION_TERM_1 = "addPperationTerm1";

	public static final String ADD_OPERATION_TERM_2 = "addOperationTerm2";

	public static final String DIVIDE_OPERATION_NAME = "divide";

	public static final String DIVIDE_OPERATION_RESULT =
		"divideOperationResult";

	public static final String DIVIDE_OPERATION_TERM_1 = "divideOperationTerm1";

	public static final String DIVIDE_OPERATION_TERM_2 = "divideOperationTerm2";

	public static final String JSP_PATH = "JSP_PATH";

	public static final String MULTIPLY_OPERATION_NAME = "multiply";

	public static final String MULTIPLY_OPERATION_RESULT =
		"multiplyOperationResult";

	public static final String MULTIPLY_OPERATION_TERM_1 =
		"multiplyOperationTerm1";

	public static final String MULTIPLY_OPERATION_TERM_2 =
		"multiplyOperationTerm2";

	public static final String OPERATION_NAME = "operationName";

	public static final String OPERATION_RESULT = "operationResult";

	public static final String SUBTRACT_OPERATION_NAME = "subtract";

	public static final String SUBTRACT_OPERATION_RESULT =
		"subtractOperationResult";

	public static final String SUBTRACT_OPERATION_TERM_1 =
		"subtractOperationTerm1";

	public static final String SUBTRACT_OPERATION_TERM_2 =
		"subtractOperationTerm2";

}