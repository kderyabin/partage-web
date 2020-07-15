<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="../partial/header.jsp" />
<jsp:include page="../partial/navbar-user.jsp" />
<main class="main-container">
    <form id="form-board-edit" method="post">
        <div class="content-column">
            <div class="input-group-column">
                <label for="name" class="label-inline-block"><fmt:message key="name"/></label>
                <c:if test="${!empty errors && !empty errors.name}">
                    <c:forEach items="${ errors.name }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
                </c:if>
                <input type="text" name="name" id="name" required maxlength="50">
            </div>
            <div class="input-group-column">
                <label for="description"><fmt:message key="description"/></label>
                <c:if test="${!empty errors && !empty errors.description}">
                    <c:forEach items="${ errors.description }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
                </c:if>
                <textarea name="description" id="description" maxlength="255"></textarea>
            </div>
            <div class="input-group-column">
                <label for="currency" class="label-inline-block"><fmt:message key="currency"/></label>
                <select name="currency" id="currency">
                    <c:forEach items="${currencies}" var="currency">
                        <option value="${currency.currencyCode}" <c:if test="${ currency.currencyCode == userCurrency.currencyCode}">selected</c:if>> ${currency.displayName} (${currency.currencyCode})</option>
                    </c:forEach>
                </select>
            </div>
            <h3><fmt:message key="participants"/></h3>


            <div class="input-group-column">
                <ul class="mdc-list list-condensed" id="participants" aria-live="assertive">
                    <c:if test="${ !empty participants}">
                    </c:if>
                </ul>
            </div>

            <div class="input-group-column">
                <label for="person" class="label-inline-block"><fmt:message key="add_new_participant"/></label>
                <c:if test="${!empty errors && !empty errors.person}">
                    <c:forEach items="${ errors.person }" var="errorMsg">
                        <span class="input-error"><fmt:message key="${ errorMsg }" /></span>
                    </c:forEach>
                </c:if>
                <input type="text" name="person" id="person" maxlength="50">
            </div>
            <c:if test="${ !empty persons}">
                <span class="h1 align-center"><fmt:message key="or"/></span>

                <div class="input-group-column">
                    <label for="participant" class="label-inline-block"><fmt:message key="add_registered_participant"/></label>
                    <c:if test="${!empty errors && !empty errors.participant}">
                        <c:forEach items="${ errors.participant }" var="errorMsg">
                            <span class="input-error"><fmt:message key="${ errorMsg }" /></span>
                        </c:forEach>
                    </c:if>
                    <select name="participant" id="participant">
                        <option value=""><fmt:message key="choose_from_the_list"/></option>
                        <c:forEach items="${persons}" var="person">
                            <option value="${person.id}">${person.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>
            <div class="participant-add-button-box">
                <button id="participant-add-button" type="button" class="mdc-button mdc-button--unelevated button-rounded">
                    <span class="mdc-button__ripple"></span>
                        <fmt:message key="button.add_participant"/>
                </button>
            </div>
            <button id="btn-form-board-submit" type="submit" style="visibility: hidden"></button>
        </div>
    </form>
</main>
<template id="participant-item">
    <li class="mdc-list-item" tabindex="0">
        <span class="mdc-list-item__ripple"></span>
        <div class="list-condensed-item-content">
            <span class="participants-name">{{name}}</span>
            <button type="button" class="mdc-button mdc-button--unelevated list-condensed-button"
                    aria-label="<fmt:message key="remove"/> {{name}}">
                <span class="mdc-button__ripple"></span>
                <span class="material-icons">delete</span>
            </button>
        </div>
    </li>
</template>
<div class="mdc-dialog">
    <div class="mdc-dialog__container">
        <div class="mdc-dialog__surface"
             role="alertdialog"
             aria-modal="true"
             aria-labelledby="my-dialog-title"
             aria-describedby="my-dialog-content">
            <div class="mdc-dialog__content" id="my-dialog-content">
                Are you sure you want to continue?
            </div>
            <div class="mdc-dialog__actions">
<%--                <button type="button" class="mdc-button mdc-dialog__button" data-mdc-dialog-action="cancel">--%>
<%--                    <div class="mdc-button__ripple"></div>--%>
<%--                    <span class="mdc-button__label">Cancel</span>--%>
<%--                </button>--%>
                <button type="button" class="mdc-button mdc-dialog__button" data-mdc-dialog-action="accept">
                    <div class="mdc-button__ripple"></div>
                    <span class="mdc-button__label">OK</span>
                </button>
            </div>
        </div>
    </div>
    <div class="mdc-dialog__scrim"></div>
</div>
<jsp:include page="../partial/footer-user.jsp" />