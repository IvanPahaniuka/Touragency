<%@ page language="java" contentType="text/html; charset=utf-8"
         pageEncoding="utf-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="custom" tagdir="/WEB-INF/tags"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>
<!DOCTYPE html>
<html>

<fmt:setLocale value="${sessionScope.lang}"/>
<fmt:setBundle basename="text"/>

<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <meta http-equiv="X-UA-Compatible" content="ie=edge">
    <link rel="stylesheet" href="https://unpkg.com/papercss@1.8.1/dist/paper.min.css">
    <title><fmt:message key="title_catalog"/></title>
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
                    <custom:lang path="/touragency/catalog"/>
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
                <form>
                    <div class="text-center">
                        <h1><p><fmt:message key="catalog"/></p></h1>
                        <h2>~~~</h2>
                    </div>

                    <form action="${pageContext.request.contextPath}/touragency/catalog" method="get">
                        <c:if test="${sessionScope.user.role eq 'USER'}">
                            <div class="row flex-center form-group">
                                <input type="text" placeholder="Search" name="search">
                            </div>
                        </c:if>
                        <c:if test="${sessionScope.user.role eq 'TOURAGENCY'}">
                            <div class="row flex-center">
                                <button name="action" value="add">
                                    <fmt:message key="add_tour"/>
                                </button>
                            </div>
                        </c:if>
                    </form>

                    <c:if test="${not empty requestScope.errorMessage}">
                        <div class="row flex-spaces">
                            <input class="alert-state" id="alert" type="checkbox">
                            <div class="alert alert-danger dismissible">
                                    ${requestScope.errorMessage}
                                <label class="btn-close" for="alert">X</label>
                            </div>
                        </div>
                    </c:if>

                    <c:if test="${not empty requestScope.noElements}">
                        <div class="row flex-center">
                            <h4>${requestScope.noElements}</h4>
                        </div>
                    </c:if>

                    <div class="row flex-spaces">
                        <c:forEach var="tour" items="${requestScope.tours}">
                            <c:if test="${sessionScope.user.role eq 'USER'}">
                                <form action="${pageContext.request.contextPath}/touragency/orders" method="get">
                            </c:if>
                            <c:if test="${sessionScope.user.role eq 'TOURAGENCY'}">
                                <form action="${pageContext.request.contextPath}/touragency/catalog" method="get">
                            </c:if>
                                <div class="card margin-bottom" style="width: 40rem;">
                                    <div class="card-body">
                                        <h4 class="card-title"><p>${tour.title}</p></h4>
                                        <h5 class="card-subtitle"><p>${tour.author}</p></h5>
                                        <input type="hidden" name="tourId" value="${tour.id}">
                                        <c:if test="${sessionScope.user.role eq 'USER'}">
                                            <c:if test="${tour.count gt 0}">
                                                <button><fmt:message key="to_order"/></button>
                                            </c:if>
                                            <c:if test="${tour.count eq 0}">
                                                <label class="paper-btn" for="modal-1">
                                                    <fmt:message key="to_order"/>
                                                </label>
                                                <input class="modal-state" id="modal-1" type="checkbox">
                                                <div class="modal">
                                                    <label class="modal-bg" for="modal-1"></label>
                                                    <div class="modal-body">
                                                        <label class="btn-close" for="modal-1">X</label>
                                                        <h4 class="modal-title"><fmt:message key="oops"/></h4>
                                                        <p class="modal-text"><fmt:message key="oops_message"/></p>
                                                        <label for="modal-1" class="paper-btn">
                                                            <fmt:message key="ok"/>
                                                        </label>
                                                    </div>
                                                </div>
                                            </c:if>
                                        </c:if>
                                        <c:if test="${sessionScope.user.role eq 'TOURAGENCY'}">
                                            <button class="btn-danger-outline" name="action" value="delete">
                                                <fmt:message key="delete_tour"/>
                                            </button>
                                            <button name="action" value="edit">
                                                <fmt:message key="edit_tour"/>
                                            </button>
                                        </c:if>
                                    </div>
                                </div>
                            </form>
                        </c:forEach>
                    </div>
                </form>
            </div>
        </div>
    </div>
</main>
</body>

</html>
