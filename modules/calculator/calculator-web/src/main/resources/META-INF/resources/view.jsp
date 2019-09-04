<%@ include file="/init.jsp" %>

<liferay-ui:tabs
	cssClass="navbar-no-collapse"
	names="calculatorapp.operation.tab.add,calculatorapp.operation.tab.divide,calculatorapp.operation.tab.multiply,calculatorapp.operation.tab.subtract"
	param="tab"
	refresh="<%= false %>"
	type="tabs nav-tabs-default"
>
	<liferay-ui:section>
		<%@ include file="/section/operation_add.jspf" %>
	</liferay-ui:section>

	<liferay-ui:section>
		<%@ include file="/section/operation_divide.jspf" %>
	</liferay-ui:section>

	<liferay-ui:section>
		<%@ include file="/section/operation_multiply.jspf" %>
	</liferay-ui:section>

	<liferay-ui:section>
		<%@ include file="/section/operation_subtract.jspf" %>
	</liferay-ui:section>
</liferay-ui:tabs>