<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="productReview" required="true" type="com.es.phoneshop.model.product.ProductReview" %>

<c:if test="${not empty productReview.recentProducts}">
    <h1>Recently reviewed</h1>
    <div class="productReview">
        <c:forEach var="recentProduct" items="${productReview.recentProducts}">
            <div class="recentProduct">
                <img class="product-tile" src="${recentProduct.imageUrl}">
                <a href="${pageContext.servletContext.contextPath}/products/${recentProduct.id}">
                        ${recentProduct.description}</a>
                <fmt:formatNumber value="${recentProduct.price}" type="currency"
                                  currencySymbol="${recentProduct.currency.symbol}"/>
            </div>
        </c:forEach>
    </div>
</c:if>