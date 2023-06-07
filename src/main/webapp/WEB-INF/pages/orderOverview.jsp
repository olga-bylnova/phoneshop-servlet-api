<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Order overview Page">
    <c:if test="${not empty order.items}">
        <h1>Order overview</h1>
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="quantity">
                    Quantity
                </td>
                <td class="price">
                    Price
                </td>
            </tr>
            </thead>
            <c:forEach var="item" items="${order.items}" varStatus="status">
                <tr>
                    <td>
                        <img class="product-tile" src="${item.product.imageUrl}">
                    </td>
                    <td>
                        <a href="${pageContext.servletContext.contextPath}/products/${item.product.id}">
                                ${item.product.description}</a>
                    </td>
                    <td class="quantity">
                        <fmt:formatNumber value="${item.quantity}" var="quantity"/>
                            ${item.quantity}
                    </td>
                    <td class="price">
                        <fmt:formatNumber value="${item.product.price}" type="currency"
                                          currencySymbol="${item.product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
            <tr>
                <td/>
                <td/>
                <td>Subtotal:</td>
                <td>${order.subTotal}$</td>
            </tr>
            <tr>
                <td/>
                <td/>
                <td>Delivery cost:</td>
                <td>${order.deliveryCost}$</td>
            </tr>
            <tr>
                <td/>
                <td/>
                <td>Total cost:</td>
                <td>${order.totalCost}$</td>
            </tr>
        </table>
        <h2>Order details</h2>
        <table>
            <tags:orderOverviewRow name="firstName" order="${order}" label="First name"/>
            <tags:orderOverviewRow name="lastName" order="${order}" label="Last name"/>
            <tags:orderOverviewRow name="phone" order="${order}" label="Phone"/>
            <tags:orderOverviewRow name="deliveryAddress" order="${order}" label="Delivery address"/>
            <tr>
                <td>Delivery Date</td>
                <td>
                        ${order.deliveryDate.toString()}
                </td>
            </tr>
            <tags:orderOverviewRow name="paymentMethod" order="${order}" label="Payment method"/>
        </table>
    </c:if>
</tags:master>