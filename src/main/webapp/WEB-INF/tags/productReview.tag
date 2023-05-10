<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="sort" required="true" %>
<%@ attribute name="order" required="true" %>

<c:if test="${not empty productReview.recentProducts}">
    <h1>Recently reviewed</h1>
</c:if>
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