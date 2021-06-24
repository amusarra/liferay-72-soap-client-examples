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

package it.dontesta.labs.liferay.web.calculator.portlet.action;

import com.liferay.portal.kernel.portlet.bridges.mvc.BaseMVCActionCommand;
import com.liferay.portal.kernel.portlet.bridges.mvc.MVCActionCommand;
import com.liferay.portal.kernel.servlet.SessionErrors;
import com.liferay.portal.kernel.util.ParamUtil;

import it.dontesta.labs.liferay.web.calculator.constants.CalculatorAppPortletKeys;
import it.dontesta.labs.liferay.web.calculator.constants.CalculatorAppWebKeys;
import it.dontesta.labs.liferay.webservice.calculator.api.Calculator;

import javax.portlet.ActionRequest;
import javax.portlet.ActionResponse;

import javax.xml.ws.WebServiceException;
import javax.xml.ws.soap.SOAPFaultException;

import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * @author Antonio Musarra
 */
@Component(
	immediate = true,
	property = {
		"javax.portlet.name=" + CalculatorAppPortletKeys.CALCULATOR_SOAP_APP,
		"mvc.command.name=/calculator/add-operation"
	},
	service = MVCActionCommand.class
)
public class AddOperationMVCActionCommand extends BaseMVCActionCommand {

	@Override
	protected void doProcessAction(
			ActionRequest actionRequest, ActionResponse actionResponse)
		throws Exception {

		_handleActionCommand(actionRequest);
	}

	private void _handleActionCommand(ActionRequest actionRequest) {
		try {
			int term1 = ParamUtil.getInteger(
				actionRequest, CalculatorAppWebKeys.ADD_OPERATION_TERM_1);

			int term2 = ParamUtil.getInteger(
				actionRequest, CalculatorAppWebKeys.ADD_OPERATION_TERM_2);

			int result = _calculator.add(term1, term2);

			actionRequest.setAttribute(
				CalculatorAppWebKeys.ADD_OPERATION_RESULT, result);
		}
		catch (SOAPFaultException soapfe) {
			SessionErrors.add(actionRequest, soapfe.getClass(), soapfe);
		}
		catch (WebServiceException wse) {
			SessionErrors.add(actionRequest, wse.getClass(), wse);
		}
		catch (Exception e) {
			SessionErrors.add(
				actionRequest, "calculatorOperationError", e.getMessage());
		}
	}

	@Reference(policyOption = ReferencePolicyOption.GREEDY)
	private Calculator _calculator;

}