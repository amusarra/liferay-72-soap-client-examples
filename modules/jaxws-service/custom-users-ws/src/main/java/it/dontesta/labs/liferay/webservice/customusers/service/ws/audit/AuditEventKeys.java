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

package it.dontesta.labs.liferay.webservice.customusers.service.ws.audit;

/**
 * @author Antonio Musarra
 */
public enum AuditEventKeys {

	SERVICE_SOAP_TRACE_INBOUND_EVENT(
		"SERVICE_SOAP_TRACE_INBOUND_EVENT_0000", "SERVICE_SOAP_TRACE_EVENT",
		"Trace SOAP Inbound Message"),
	SERVICE_SOAP_TRACE_OUTBOUND_EVENT(
		"SERVICE_SOAP_TRACE_OUTBOUND_EVENT_0000", "SERVICE_SOAP_TRACE_EVENT",
		"Trace SOAP Outbound Message");

	/**
	 * @return the Audit Event Type Code
	 */
	public String getAuditEventTypeCode() {
		return auditEventTypeCode;
	}

	/**
	 * @return the the Audit Event Type Code Description
	 */
	public String getAuditEventTypeCodeDescription() {
		return auditEventTypeCodeDescription;
	}

	/**
	 * @return the Audit Event Type Id
	 */
	public String getAuditEventTypeId() {
		return auditEventTypeId;
	}

	AuditEventKeys(
		final String auditEventTypeId, final String auditEventTypeCode,
		final String auditEventTypeCodeDescription) {

		this.auditEventTypeId = auditEventTypeId;
		this.auditEventTypeCode = auditEventTypeCode;
		this.auditEventTypeCodeDescription = auditEventTypeCodeDescription;
	}

	private String auditEventTypeCode;
	private String auditEventTypeCodeDescription;
	private String auditEventTypeId;

}