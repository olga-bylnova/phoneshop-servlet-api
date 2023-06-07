<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/productList.css">
<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
    <c:if test="${not empty param.message}">
        <p class="success">
                ${param.message}
        </p>
    </c:if>
    <c:if test="${not empty errors}">
        <p class="error">
            There was an error updating cart
        </p>
    </c:if>
    <form>
        <input name="query" value="${param.query}">
        <button>Search</button>
    </form>
    <table>
        <thead>
        <tr>
            <td>Image</td>
            <td>
                Description
                <tags:sortLink sort="description" order="asc"/>
                <tags:sortLink sort="description" order="desc"/>
            </td>
            <td>Quantity</td>
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
            <td/>
        </tr>
        </thead>
        <c:forEach var="product" items="${products}">
            <tr>
                <td>
                    <img class="product-tile" src="${product.imageUrl}">
                </td>
                <td>
                    <a href="${pageContext.servletContext.contextPath}/products/${product.id}">
                            ${product.description}</a>
                </td>
                <td class="quantity">
                    <form id="addToCart${product.id}" action="${pageContext.servletContext.contextPath}/products" method="post">
                        <fmt:formatNumber value="1" var="quantity"/>
                        <c:set var="error" value="${errors[product.id]}"/>
                        <input name="quantity"
                               value="${not empty error ? param.quantity: 1}"
                               class="quantity"/>
                        <c:if test="${not empty error}">
                            <p class="error">
                                    ${error}
                            </p>
                        </c:if>
                        <input name="productId" value="${product.id}" type="hidden"/>
                    </form>
                </td>
                <td class="price">
                    <a href="#" id="price/${product.id}">
                        <div id="priceHistory">
                            <h1>Price history</h1>
                            <h2>${product.description}</h2>
                            <table>
                                <thead>
                                <tr>
                                    <td>Start date</td>
                                    <td>Price</td>
                                </tr>
                                </thead>
                                <c:forEach var="item" items="${product.priceHistory}">
                                    <tr>
                                        <td><fmt:formatDate value="${item.priceChange}" pattern="yyyy-MM-dd"/>
                                        </td>
                                        <td><fmt:formatNumber value="${item.price}" type="currency"
                                                              currencySymbol="${product.currency.symbol}"/>
                                        </td>
                                    </tr>
                                </c:forEach>
                            </table>
                        </div>
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </a>
                </td>
                <td>
                    <button form="addToCart${product.id}">Add to cart</button>
                </td>
            </tr>
        </c:forEach>
    </table>
    <tags:productReview productReview="${productReview}"/>
</tags:master>

<script src="${pageContext.servletContext.contextPath}/scripts/priceHistory.js" type="text/javascript"></script>