<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="../partial/header.jsp" />
<jsp:include page="../partial/navbar-user.jsp" />

<main class="main-container">
    <div class="starter-box">
        <h1 class="starter-label">
            <fmt:message key="start"></fmt:message>
        </h1>
        <button type="submit" class="mdc-button mdc-button--unelevated button-rounded starter-button">
            <span class="mdc-button__ripple"></span>
            <fmt:message key="add_board"></fmt:message>
        </button>
        <img class="starter-image" src="${contextPath}/images/team-1721x1160.png" alt="Collaborating">
    </div>
</main>
<jsp:include page="../partial/footer-user.jsp" />