<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<!doctype html>
<html lang="${lang}">
<head>
    <meta charset="utf-8">
    <meta name="viewport" content="width=device-width, initial-scale=1">
    <link rel="icon" type="image/png" href="${contextPath}/images/favicon.ico">
    <title><fmt:message key="window.title"/></title>
    <link href="https://fonts.googleapis.com/css2?family=Material+Icons&family=Roboto:ital,wght@0,300;0,400;0,700;1,300;1,400;1,700&display=swap"
          rel="stylesheet">
    <c:if test="${!empty stylesheetsExt}">
        <!-- External stylesheets URLs injected for a page -->
        <c:forEach items="${stylesheetsExt}" var="stylesheet">
            <link href="${stylesheet}" rel="stylesheet">
        </c:forEach>
    </c:if>
    <link href="${contextPath}/css/styles.css" rel="stylesheet">
    <c:if test="${!empty stylesheets}">
        <!-- Application stylesheets URLs per page -->
        <c:forEach items="${stylesheets}" var="stylesheet">
            <link href="${contextPath}/css/${stylesheet}" rel="stylesheet">
        </c:forEach>
    </c:if>
</head>
<body class="mdc-typography">
