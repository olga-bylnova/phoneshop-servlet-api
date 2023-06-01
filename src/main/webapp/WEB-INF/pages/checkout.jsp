<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>

<jsp:useBean id="order" type="com.es.phoneshop.model.order.Order" scope="request"/>
<tags:master pageTitle="Checkout Page">
    <c:if test="${not empty param.message}">
        <p class="success">
                ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty errors}">
        <p class="error">
            There was an error placing an order
        </p>
    </c:if>
    <c:if test="${not empty order.items}">
        <form method="post" action="${pageContext.servletContext.contextPath}/checkout">
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
                <tags:orderFormRow name="firstName" order="${order}" label="First name" errors="${errors}"/>
                <tags:orderFormRow name="lastName" order="${order}" label="Last name" errors="${errors}"/>
                <tags:orderFormRow name="phone" order="${order}" label="Phone" errors="${errors}"/>
                <tags:orderFormRow name="deliveryAddress" order="${order}" label="Delivery Address" errors="${errors}"/>
                <tr>
                    <td>Delivery Date<span style="color: red">*</span></td>
                    <td>
                        <c:set var="error" value="${errors['deliveryDate']}"/>
                        <input id="deliveryDate" name="deliveryDate" type="date"
                               placeholder="DD/MM/YYYY"
                               value="${order.deliveryDate.toString()}">
                        <c:if test="${not empty error}">
                            <p class="error">
                                    ${error}
                            </p>
                        </c:if>
                    </td>
                </tr>
                <tr>
                    <td>Payment method<span style="color: red">*</span></td>
                    <td>
                        <c:set var="error" value="${errors['paymentMethod']}"/>
                        <select name="paymentMethod">
                            <option/>
                            <c:forEach var="paymentMethod" items="${paymentMethods}">
                                <option
                                    ${paymentMethod eq order.paymentMethod ? 'selected' : '' }>${paymentMethod}</option>
                            </c:forEach>
                        </select>
                        <c:if test="${not empty error}">
                            <p class="error">
                                    ${error}
                            </p>
                        </c:if>
                    </td>
                </tr>
            </table>
            <p>
                <button>Checkout</button>
            </p>
        </form>
    </c:if>
</tags:master>

<script src="${pageContext.servletContext.contextPath}/scripts/checkout.js" type="text/javascript"></script>