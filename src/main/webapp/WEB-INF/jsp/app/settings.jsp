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
                <label for="language" class="label-inline-block"><fmt:message key="language"/> <span class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.language}">
                    <c:forEach items="${ errors.language }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <select name="language" id="language">
                    <c:forEach items="${languages}" var="language">
                        <option value="${language.language}" <c:if test="${ language.language eq model.language}"> selected</c:if>>${language.displayName}</option>
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

                <label for="login"><fmt:message key="email" /><span class="required">*</span> </label>
                <c:if test="${!empty errors && !empty errors.login}">
                    <c:forEach items="${ errors.login }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
                </c:if>
                <input type="email" id="login" name="login" maxlength="100" value="${ model.login }" required>
            </div>

            <div class="input-group-column">
                <a href="reset-password"><fmt:message key="reset_password"/></a>
            </div>

            <button id="btn-form-submit" type="submit" class="hidden"><fmt:message key="OK"/></button>
        </form>
    </div>
</main>
<div class="mdc-dialog" id="go-back-dialog">
    <div class="mdc-dialog__container">
        <div class="mdc-dialog__surface"
             role="alertdialog"
             aria-modal="true"
             aria-labelledby="my-dialog-title"
             aria-describedby="my-dialog-content">
            <div class="mdc-dialog__content" id="my-dialog-content">
                <fmt:message key="msg.confirm_form_exit"/>
            </div>
            <div class="mdc-dialog__actions">
                <button type="button" class="mdc-button mdc-dialog__button" data-mdc-dialog-action="cancel">
                    <div class="mdc-button__ripple"></div>
                    <span class="mdc-button__label"><fmt:message key="Cancel"/></span>
                </button>
                <button type="button" class="mdc-button mdc-dialog__button" data-mdc-dialog-action="accept">
                    <div class="mdc-button__ripple"></div>
                    <span class="mdc-button__label"><fmt:message key="OK"/></span>
                </button>
            </div>
        </div>
    </div>
    <div class="mdc-dialog__scrim"></div>
</div>
<jsp:include page="../partial/footer-user.jsp"/>