<%@ tag import="com.es.phoneshop.service.HttpSessionCartService" %>
<%@ tag import="com.es.phoneshop.service.CartService" %>
<%@ tag trimDirectiveWhitespaces="true" %>
<%@ attribute name="pageTitle" required="true" %>

<html>
<head>
    <title>${pageTitle}</title>
    <link href='http://fonts.googleapis.com/css?family=Lobster+Two' rel='stylesheet' type='text/css'>
    <link rel="stylesheet" href="${pageContext.servletContext.contextPath}/styles/main.css">
</head>
<body class="product-list">
<header>
    <a href="${pageContext.servletContext.contextPath}/products">
        <img src="${pageContext.servletContext.contextPath}/images/logo.svg"/>
        PhoneShop
    </a>
    <a href="${pageContext.servletContext.contextPath}/cart">Cart:
<%--        <jsp:include page="/cart/minicart"/>--%>
        <% CartService cartService = HttpSessionCartService.getInstance();
            request.setAttribute("cart", cartService.getCart(request));
        %>
        <jsp:include page="../pages/minicart.jsp"/>
    </a>
</header>
<main>
    <jsp:doBody/>
</main>
<p>(c) Expert-soft</p>
</body>
</html>