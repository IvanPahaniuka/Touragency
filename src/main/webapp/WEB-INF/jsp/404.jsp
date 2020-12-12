<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
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
    <title><fmt:message key="title_404"/></title>
</head>

<body>
<div class="paper container">
    <div class="row flex-center">
        <div class="col-fill col">
            <div class="text-center">
                <h1><p><fmt:message key="touragency"/></p></h1>
                <h2>~~~</h2>
                <h3><p><fmt:message key="error_404"/></p></h3>
                <h4><p><fmt:message key="error_404_message"/></p></h4>
            </div>
        </div>
    </div>
</div>
</body>

</html>
