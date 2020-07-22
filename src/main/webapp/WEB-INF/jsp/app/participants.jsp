<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>

<main class="main-container">
    <div class="content-column">

        <c:if test="${ !empty participants}">
            <c:forEach items="${ participants }" var="participant">
                <a class="list-item" href="edit/${participant.id}">
                    <div class="list-item-content">
                        <span class="list-item-title">${participant.name}</span>
                    </div>
                    <button type="button" class="mdc-button mdc-button--unelevated list-item-action"
                            aria-label="<fmt:message key="remove_board"/> <c:out value="${participant.name}" />"
                            data-delete-btn="${participant.id}">
                        <span class="mdc-button__ripple"></span>
                        <span class="material-icons" aria-hidden="true">delete</span>
                    </button>
                </a>
            </c:forEach>
        </c:if>
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
                <fmt:message key="msg.confirm_delete_participant"/>
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