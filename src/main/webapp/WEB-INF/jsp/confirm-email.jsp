<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="partial/header.jsp"/>
<main class="main-container">
    <div class="content-column">
        <h1 class="align-center">
            <fmt:message key="title.confirm_email"/>
        </h1>
        <div class="partage-auth">

            <div class="mdc-card partage-auth-form">
                <c:if test="${!empty messages}">
                    <c:forEach items="${ messages }" var="msg">
                        <p><fmt:message key="${ msg }"/></p>
                    </c:forEach>
                </c:if>
            </div>
        </div>
    </div>
</main>

<jsp:include page="partial/footer.jsp"/>