<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="name" required="true" %>
<%@ attribute name="order" required="true" type="com.es.phoneshop.model.order.Order" %>
<%@ attribute name="label" required="true" %>
<%@ attribute name="errors" required="true" type="java.util.Map" %>

<tr>
    <td>${label}<span style="color: red">*</span></td>
    <td>
        <c:set var="error" value="${errors[name]}"/>
        <input name="${name}"
               value="${not empty error ? param[name] : order[name]}">
        <c:if test="${not empty error}">
            <p class="error">
                    ${error}
            </p>
        </c:if>
    </td>
</tr>