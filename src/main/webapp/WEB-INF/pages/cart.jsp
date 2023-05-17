<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="cart" type="com.es.phoneshop.model.cart.Cart" scope="request"/>
<tags:master pageTitle="Cart Page">
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
    <c:if test="${not empty cart.items}">
        <form method="post">
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
                <c:forEach var="item" items="${cart.items}">
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
                            <input name="quantity" value="${quantity}" class="quantity"/>
                            <input name="productId" value="${item.product.id}" type="hidden"/>
                        </td>
                        <td class="price">
                            <fmt:formatNumber value="${item.product.price}" type="currency"
                                              currencySymbol="${item.product.currency.symbol}"/>
                        </td>
                    </tr>
                </c:forEach>
            </table>
            <p>
                <button>Update</button>
            </p>
        </form>
    </c:if>
</tags:master>