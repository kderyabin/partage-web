<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<!doctype html>
<html lang="${lang}">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<link rel="icon" type="image/png" href="../images/favicon.ico">
<title>${ title }</title>
<link href="https://fonts.googleapis.com/css2?family=Material+Icons&family=Roboto:ital,wght@0,300;0,400;0,700;1,300;1,400;1,700&display=swap"
          rel="stylesheet">
    <link href="${contextPath}/css/main.css" rel="stylesheet">
    <link href="${contextPath}/css/material-components-web.css" rel="stylesheet">
    <link href="${contextPath}/css/custom.css" rel="stylesheet">
<body class="mdc-typography">