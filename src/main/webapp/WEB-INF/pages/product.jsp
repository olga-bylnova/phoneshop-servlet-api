<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/productList.css">

<jsp:useBean id="product" type="com.es.phoneshop.model.product.Product" scope="request"/>
<tags:master pageTitle="Product Details Page">
    <p>Cart: ${cart}</p>

    <c:if test="${not empty message}">
        <p class="success">
                ${message}
        </p>
    </c:if>
    <c:if test="${not empty error}">
        <p class="error">
            There was an error adding to cart
        <br/>
            ${error}
        </p>
    </c:if>
    <p>${product.description}</p>
    <form method="post">
        <table>
            <tr>
                <td>Image</td>
                <td>
                    <img src="${product.imageUrl}">
                </td>
            </tr>
            <tr>
                <td>Code</td>
                <td>${product.code}</td>
            </tr>
            <tr>
                <td>Stock</td>
                <td>${product.stock}</td>
            </tr>
            <tr>
                <td>Price</td>
                <td class="price">
                    <fmt:formatNumber value="${product.price}" type="currency"
                                      currencySymbol="${product.currency.symbol}"/>
                </td>
            </tr>
            <tr>
                <td>Quantity</td>
                <td>
                    <input class="quantity" name="quantity"
                           value="${not empty error ? param.quantity : 1}">
                </td>
            </tr>
        </table>
        <button>Add to cart</button>
    </form>
    <tags:productReview productReview="${productReview}"/>
</tags:master>