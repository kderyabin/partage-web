<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="partial/header.jsp" />
<main class="main-container">
    <div class="content-column">
        <h1 class="align-center">
            <fmt:message key="sign_in" />
        </h1>
        <form method="post">
            <div class="partage-auth">
                <div class="mdc-card partage-auth-form">
				<c:if test="${!empty errors && !empty errors.generic}">
                    <c:forEach items="${ errors.generic }" var="errorMsg">
                        <span class="align-center input-error" role="alert"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
				</c:if>
                    <div class="input-group-column">
                        <label for="login">
                            <fmt:message key="email" /><span class="required">*</span>
                        </label>
                        <c:if test="${!empty errors && !empty errors.login}">
                            <c:forEach items="${ errors.login }" var="errorMsg">
                                <span class="input-error" role="alert"><fmt:message key="${ errorMsg }" /></span>
                            </c:forEach>
                        </c:if>
                        <input type="email" id="login" name="login" maxlength="100" value="${ login }" required
                               aria-invalid="${!empty errors && !empty errors.login ? "true": "false"}">
                    </div>
                    <div class="input-group-column">
                        <div class="partage-signin-password-label">
                            <label for="pwd">
                                <fmt:message key="password" /> <span class="required">*</span>
                            </label>
                            <a href="password-reset"><fmt:message key="forgot_password" /></a>
                        </div>
                        <c:if test="${!empty errors && !empty errors.pwd}">
                            <c:forEach items="${ errors.pwd }" var="errorMsg">
                                <span class="input-error" role="alert"><fmt:message key="${ errorMsg }" /></span>
                            </c:forEach>
                        </c:if>
                        <input type="password" id="pwd" name="pwd" minlength="8" maxlength="20" value="${ pwd }"
                               required aria-invalid="${!empty errors && !empty errors.pwd ? "true": "false"}">
                    </div>

                    <div class="partage-auth-button-box">
                        <button type="submit" class="mdc-button mdc-button--unelevated button-rounded">
                            <span class="mdc-button__ripple"></span>
                            <fmt:message key="button.sign_in" />
                        </button>
                    </div>
                </div>

                <div class="mdc-card partage-auth-invite">
                    <p><fmt:message key="not_registered" /> &nbsp;<a href="signup"><fmt:message key="create_an_account" /></a></p>
                </div>
            </div>
        </form>
    </div>
</main>

<jsp:include page="partial/footer.jsp" />