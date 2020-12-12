<%@ attribute name="path" required="true" type="java.lang.String" description="Concrete path" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@tag pageEncoding="UTF-8"%>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="text"/>

<c:if test="${not empty requestScope.queryWithLang}">
    <li>
        <a href="${pageContext.request.contextPath}${path}?${requestScope.queryWithLang}en"><fmt:message key="eng"/></a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}${path}?${requestScope.queryWithLang}ru"><fmt:message key="rus"/></a>
    </li>
</c:if>
<c:if test="${empty requestScope.queryWithLang}">
    <li>
        <a href="${pageContext.request.contextPath}${path}?${pageContext.request.queryString}&lang=en"><fmt:message key="eng"/></a>
    </li>
    <li>
        <a href="${pageContext.request.contextPath}${path}?${pageContext.request.queryString}&lang=ru"><fmt:message key="rus"/></a>
    </li>
</c:if>