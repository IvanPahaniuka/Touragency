<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
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
    <title><fmt:message key="title_orders"/></title>
</head>

<body>
<header>
    <nav class="border fixed split-nav">
        <div class="nav-brand">
            <h3><a href="catalog"><fmt:message key="touragency"/></a></h3>
        </div>
        <div class="collapsible">
            <input id="collapsible1" type="checkbox" name="collapsible1">
            <button>
                <label for="collapsible1">
                    <div class="bar1"></div>
                    <div class="bar2"></div>
                    <div class="bar3"></div>
                    <div class="bar4"></div>
                </label>
            </button>
            <div class="collapsible-body">
                <ul class="inline">
                    <li><a href="orders"><fmt:message key="orders"/></a></li>
                    <custom:lang path="/touragency/orders"/>
                    <li>
                        <form action="${pageContext.request.contextPath}/touragency/sign-out">
                            <button class="btn-small btn-danger-outline">
                                <fmt:message key="sign_out"/>
                            </button>
                        </form>
                    </li>
                </ul>
            </div>
        </div>
    </nav>
</header>

<main>
    <div class="paper container">
        <div class="row flex-center">
            <div class="col-fill col">
                <div class="text-center">
                    <h1><p><fmt:message key="orders"/></p></h1>
                    <h2>~~~</h2>
                </div>

                <c:if test="${not empty requestScope.errorMessage}">
                    <div class="row flex-spaces">
                        <input class="alert-state" id="alert" type="checkbox">
                        <div class="alert alert-danger dismissible">
                                ${requestScope.errorMessage}
                            <label class="btn-close" for="alert">X</label>
                        </div>
                    </div>
                </c:if>

                <div class="row flex-spaces tabs">
                    <input id="tab1" type="radio" name="tabs" checked>
                    <label for="tab1"><fmt:message key="processing"/></label>

                    <input id="tab2" type="radio" name="tabs">
                    <label for="tab2"><fmt:message key="active"/></label>

                    <div class="content" id="content1">
                        <c:if test="${not empty requestScope.noProcessing}">
                            <div class="row flex-center">
                                <h4><p>${requestScope.noProcessing}</p></h4>
                            </div>
                        </c:if>

                        <c:forEach var="order" items="${requestScope.orders}">
                            <c:if test="${order.status == 'PROCESSING'}">
                                <form action="${pageContext.request.contextPath}/touragency/orders" method="get">
                                    <div class="card margin-bottom" style="width: 40rem;">
                                        <div class="card-body">
                                            <h4 class="card-title"><p>${order.tour.title}</p></h4>
                                            <h5 class="card-subtitle"><p>${order.tour.author}</p></h5>
                                            <c:if test="${sessionScope.user.role eq 'USER'}">
                                                <input type="hidden" name="action" value="cancel">
                                                <input type="hidden" name="tourId" value="${order.tour.id}">
                                                <button class="btn-danger-outline">
                                                    <fmt:message key="cancel_order"/>
                                                </button>
                                            </c:if>
                                            <c:if test="${sessionScope.user.role eq 'TOURAGENCY'}">
                                                <p class="card-text">
                                                    <fmt:message key="requests"/> ${order.user.firstName} "${order.user.login}" ${order.user.lastName}
                                                </p>
                                                <input type="hidden" name="userLogin" value="${order.user.login}">
                                                <input type="hidden" name="tourId" value="${order.tour.id}">
                                                <button name="action" value="cancel" class="btn-danger-outline">
                                                    <fmt:message key="cancel_order"/>
                                                </button>
                                                <button name="action" value="book_ticket" class="btn-primary-outline">
                                                    <fmt:message key="give_book_ticket"/>
                                                </button>
                                                <button name="action" value="buy_ticket" class="btn-secondary-outline">
                                                    <fmt:message key="give_buy_ticket"/>
                                                </button>
                                            </c:if>
                                        </div>
                                    </div>
                                </form>
                            </c:if>
                        </c:forEach>
                    </div>

                    <div class="content" id="content2">
                        <c:if test="${not empty requestScope.noActive}">
                            <div class="row flex-center">
                                <h4><p>${requestScope.noActive}</p></h4>
                            </div>
                        </c:if>

                        <c:forEach var="order" items="${requestScope.orders}">
                            <c:if test="${order.status != 'PROCESSING'}">
                                <form action="${pageContext.request.contextPath}/touragency/orders" method="get">
                                    <div class="card margin-bottom" style="width: 40rem;">
                                        <div class="card-body">
                                            <h4 class="card-title"><p>
                                                    ${order.tour.title}
                                                <c:if test="${order.status eq 'BOOKTICKET'}">
                                                    <span class="badge"><fmt:message key="book_ticket"/></span>
                                                </c:if>
                                                <c:if test="${order.status eq 'BUYTICKET'}">
                                                    <span class="badge secondary"><fmt:message key="buy_ticket"/></span>
                                                </c:if>
                                            </p></h4>
                                            <h5 class="card-subtitle"><p>${order.tour.author}</p></h5>
                                            <c:if test="${sessionScope.user.role eq 'USER'}">
                                                <input type="hidden" name="action" value="giveback">
                                                <input type="hidden" name="tourId" value="${order.tour.id}">
                                                <button class="btn-success-outline">
                                                    <fmt:message key="return_tour"/>
                                                </button>
                                            </c:if>
                                            <c:if test="${sessionScope.user.role eq 'TOURAGENCY'}">
                                                <p class="card-text">
                                                        ${order.user.firstName} "${order.user.login}" ${order.user.lastName} <fmt:message key="has_on_hand"/>
                                                </p>
                                            </c:if>
                                        </div>
                                    </div>
                                </form>
                            </c:if>
                        </c:forEach>
                    </div>
                </div>
            </div>
        </div>
    </div>
</main>
</body>

</html>

