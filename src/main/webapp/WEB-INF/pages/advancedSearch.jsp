<%@ page contentType="text/html;charset=UTF-8" %>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="tags" tagdir="/WEB-INF/tags" %>
<link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">

<tags:master pageTitle="Advanced search">
    <h2>Advanced search</h2>
    <c:if test="${not empty errors}">
        <div class="error">
            Incorrect input data
        </div>
    </c:if>
    <form method="post" action="${pageContext.servletContext.contextPath}/search">
        <table>
            <tbody>
            <tr>
                <td>Description</td>
                <td><input class="quantity"
                           name="description"
                           value="${param.description}"></td>
                <td><select name="searchCriteria">
                    <c:forEach var="searchCriteria" items="${searchCriteria}">
                        <option ${searchCriteria eq param.searchCriteria ? 'selected' : '' }>
                                ${searchCriteria}
                        </option>
                    </c:forEach>
                </select>
                </td>
            </tr>
            <tr>
                <td>Min price</td>
                <td><input class="quantity"
                           name="minPrice"
                           value="${param.minPrice}">
                    <c:if test="${not empty errors['minPrice']}">
                        <p class="error">
                                ${errors['minPrice']}
                        </p>
                    </c:if>
                </td>
                <td></td>
            </tr>
            <tr>
                <td>Max price</td>
                <td><input name="maxPrice"
                           class="quantity"
                           value="${param.maxPrice}">
                    <c:if test="${not empty errors['maxPrice']}">
                        <p class="error">
                                ${errors['maxPrice']}
                        </p>
                    </c:if>
                </td>
                <td></td>
            </tr>
            </tbody>
        </table>
        <button>Search</button>
    </form>
    <c:if test="${not empty products}">
        <table>
            <thead>
            <tr>
                <td>Image</td>
                <td>
                    Description
                </td>
                <td class="price">
                    Price
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
                        <fmt:formatNumber value="${product.price}" type="currency"
                                          currencySymbol="${product.currency.symbol}"/>
                    </td>
                </tr>
            </c:forEach>
        </table>
    </c:if>
</tags:master>