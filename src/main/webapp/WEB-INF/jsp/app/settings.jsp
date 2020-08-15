<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>
<main class="main-container">
    <div class="content-column">
        <form id="form-settings" method="post">

            <div class="input-group-column">
                <label for="language" class="label-inline-block">
                    <fmt:message key="language"/> <span class="required">*</span>
                </label>
                <c:if test="${!empty errors && !empty errors.language}">
                    <c:forEach items="${ errors.language }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <select name="language" id="language"
                        aria-invalid="${!empty errors && !empty errors.language ? "true": "false"}">
                    <c:forEach items="${languages}" var="language">
                        <option value="${language.language}" ${ language.language eq model.language ? "selected" : ""}>
                                ${language.displayName}
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="input-group-column">
                <label for="currency" class="label-inline-block"><fmt:message key="currency"/></label>
                <select name="currency" id="currency">
                    <c:forEach items="${currencies}" var="currency">
                        <option value="${currency.currencyCode}"
                                <c:if test="${ currency.currencyCode == model.currency}">selected</c:if>> ${currency.displayName}
                            (${currency.currencyCode})
                        </option>
                    </c:forEach>
                </select>
            </div>

            <div class="divider"></div>

            <h3><fmt:message key="account"/></h3>

            <div class="input-group-column">
                <label for="name">
                    <fmt:message key="name" /><span class="required">*</span>
                </label>
                <c:if test="${!empty errors && !empty errors.name}">
                    <c:forEach items="${ errors.name }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
                </c:if>
                <input type="text" id="name" name="name" maxlength="100" value="${ model.name }" required
                       aria-invalid="${!empty errors && !empty errors.name ? "true": "false"}">
            </div>

            <div class="input-group-column">
                <label for="login">
                    <fmt:message key="email" /><span class="required">*</span>
                </label>
                <c:if test="${!empty errors && !empty errors.login}">
                    <c:forEach items="${ errors.login }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
                </c:if>
                <input type="email" id="login" name="login" maxlength="100" value="${ model.login }" required
                       aria-invalid="${!empty errors && !empty errors.login ? "true": "false"}">
            </div>

            <div class="input-group-column">
                <a href="reset-password"><fmt:message key="reset_password"/></a>
            </div>

            <button id="btn-form-submit" type="submit" class="hidden"><fmt:message key="OK"/></button>
        </form>
    </div>
</main>
<jsp:include page="../partial/dialog.jsp">
    <jsp:param name="dialogId" value="go-back-dialog"/>
    <jsp:param name="dialogMsg" value="msg.confirm_form_exit"/>
</jsp:include>
<jsp:include page="../partial/footer-user.jsp"/>