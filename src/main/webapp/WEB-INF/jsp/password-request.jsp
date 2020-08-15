<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="partial/header.jsp" />
<main class="main-container">
    <div class="content-column">
        <h1 class="align-center">
            <fmt:message key="reset_password" />
        </h1>
        <form method="post">
            <div class="partage-auth">
                <div class="mdc-card partage-auth-form">
                    <c:choose>
                        <c:when test="${email_sent}">
                            <p><fmt:message key="reset_pass.confirmation" /></p>
                        </c:when>
                        <c:otherwise>
                            <p><fmt:message key="reset_pass.description" /></p>
                        </c:otherwise>
                    </c:choose>

                    <div class="input-group-column">
                        <label for="login"><fmt:message key="email" /><span class="required">*</span></label>
                        <c:if test="${!empty errors && !empty errors.login}">
                            <c:forEach items="${ errors.login }" var="errorMsg">
                                <span class="input-error" role="alert"><fmt:message key="${ errorMsg }" /></span>
                            </c:forEach>
                        </c:if>
                        <input type="email" id="login" name="login" maxlength="100" value="${ login }" required
                               aria-invalid="${!empty errors && !empty errors.login ? "true": "false"}">
                    </div>
                    <div class="partage-auth-button-box">
                        <button type="submit" class="mdc-button mdc-button--unelevated button-rounded">
                            <span class="mdc-button__ripple"></span>
                            <fmt:message key="button.send_email" />
                        </button>
                    </div>
                </div>

            </div>
        </form>
    </div>
</main>

<jsp:include page="partial/footer.jsp" />