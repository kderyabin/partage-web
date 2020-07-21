<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>
<main class="main-container">
    <div class="content-column">
        <form id="form-item-edit" method="post">
            <c:if test="${!empty errors && !empty errors.generic}">
                <p>
                    <c:forEach items="${ errors.generic }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </p>
            </c:if>
            <div class="input-group-column">
                <label for="title" class="label-inline-block"><fmt:message key="Title"/> <span class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.title}">
                    <c:forEach items="${ errors.title }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="text" name="title" id="title" value="${ model.title }" required maxlength="50">
            </div>
            <div class="input-group-column">
                <label for="amount" class="label-inline-block"><fmt:message key="amount"/> <span
                        class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.amount}">
                    <c:forEach items="${ errors.amount }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="number" name="amount" id="amount" value="${ model.amount }" required min="0" step="0.01">
            </div>

            <div class="input-group-column">
                <label for="date" class="label-inline-block"><fmt:message key="Date"/> <span
                        class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.date}">
                    <c:forEach items="${ errors.date }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="date" name="date" id="date" value="${model.date}">
                <span class="input-info"><fmt:message key="date_help"/></span>
            </div>

            <div class="input-group-column">
                <label for="participant" class="label-inline-block"><fmt:message key="paid_by"/> <span class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.participant}">
                    <c:forEach items="${ errors.participant }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <select name="participant" id="participant">
                    <option value=""><fmt:message key="choose_from_the_list"/></option>
                    <c:forEach items="${participants}" var="participant">
                        <option value="${participant.id}" <c:if test="${ model.participant eq participant.id}"> selected</c:if>>${participant.name}</option>
                    </c:forEach>
                </select>
            </div>

            <button id="btn-form-item-submit" type="submit" class="hidden"><fmt:message key="OK"/></button>
        </form>
    </div>
</main>
<div class="mdc-dialog">
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