<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>
<main class="main-container">
    <div class="content-column">
        <form id="form-participant-edit" method="post">
            <c:if test="${!empty errors && !empty errors.generic}">
                <p>
                    <c:forEach items="${ errors.generic }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </p>
            </c:if>
            <div class="input-group-column">
                <label for="name" class="label-inline-block"><fmt:message key="name"/> <span class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.name}">
                    <c:forEach items="${ errors.name }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="text" name="name" id="name" value="${ model.name }" required maxlength="50">
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