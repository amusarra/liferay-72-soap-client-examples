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

package it.dontesta.labs.liferay.webservice.vat.service.model;

import java.util.Date;

import javax.xml.bind.annotation.XmlRootElement;

/**
 * @author Antonio Musarra
 *
 */
@XmlRootElement
public class VatResponse {

	/**
	 * @return the address
	 */
	public String getBusinessAddress() {
		return _businessAddress;
	}

	/**
	 * @return the name of business
	 */
	public String getBusinessName() {
		return _businessName;
	}

	/**
	 * @return the country code
	 */
	public String getCountryCode() {
		return _countryCode;
	}

	/**
	 * @return the request date
	 */
	public Date getRequestDate() {
		return _requestDate;
	}

	/**
	 * @return the vat number
	 */
	public String getVatNumber() {
		return _vatNumber;
	}

	/**
	 * @return the vat number is valid
	 */
	public boolean getValid() {
		return _valid;
	}

	/**
	 * @param businessAddress the address to set
	 */
	public void setBusinessAddress(String businessAddress) {
		_businessAddress = businessAddress;
	}

	/**
	 * @param businessName the business name to set
	 */
	public void setBusinessName(String businessName) {
		_businessName = businessName;
	}

	/**
	 * @param countryCode the email to set
	 */
	public void setCountryCode(String countryCode) {
		_countryCode = countryCode;
	}

	/**
	 * @param requestDate the request date to set
	 */
	public void setRequestDate(Date requestDate) {
		_requestDate = requestDate;
	}

	/**
	 * @param valid the valid to set
	 */
	public void setValid(boolean valid) {
		_valid = valid;
	}

	/**
	 * @param vatNumber the vat number to set
	 */
	public void setVatNumber(String vatNumber) {
		_vatNumber = vatNumber;
	}

	private String _businessAddress;
	private String _businessName;
	private String _countryCode;
	private Date _requestDate;
	private boolean _valid;
	private String _vatNumber;

}