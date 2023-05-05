<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>

<jsp:useBean id="products" type="java.util.ArrayList" scope="request"/>
<tags:master pageTitle="Product List">
    <p>
        Welcome to Expert-Soft training!
    </p>
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
            <td class="price">
                Price
                <tags:sortLink sort="price" order="asc"/>
                <tags:sortLink sort="price" order="desc"/>
            </td>
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
            </tr>
        </c:forEach>
    </table>

    <style>
        #priceHistory {
            display: none;
            width: 250px;
            height: 300px;
            text-align: center;
            color: #2b2b2b;
            border: 1px solid gray;
            background-color: white;
            position: fixed;
            top: 0;
            right: 0;
            bottom: 0;
            left: 0;
            margin: auto;
        }
    </style>
</tags:master>

<script src="http://ajax.googleapis.com/ajax/libs/jquery/1.7.1/jquery.min.js" type="text/javascript"></script>
<script>
    $("[id*=price]").hover(function () {
            $(this).find('#priceHistory').css('display', 'block')
        },
        function () {
            $(this).find('#priceHistory').css('display', 'none')
        }
    );
</script>