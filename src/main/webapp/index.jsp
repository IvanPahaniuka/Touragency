<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<!DOCTYPE html>
<html>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="text"/>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://unpkg.com/papercss@1.8.1/dist/paper.min.css">
    <title><fmt:message key="title_sign_in_or_sign_up"/></title>
</head>

<body>
<div class="paper container">
    <div class="row flex-center">
        <div class="col-fill col">
            <div class="text-center">
                <h1><p><fmt:message key="touragency"/></p></h1>
                <h2>~~~</h2>
                <h3>
                    <a href="${pageContext.request.contextPath}/touragency/sign-in"><fmt:message key="sign_in"/></a>
                    <fmt:message key="or"/>
                    <a href="${pageContext.request.contextPath}/touragency/sign-up"><fmt:message key="sign_up"/></a>
                </h3>
            </div>
        </div>
    </div>
</div>
</body>

</html>
