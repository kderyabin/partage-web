<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>

<main class="main-container">
    <div class="content-column">

        <c:if test="${ !empty boards}">
            <c:forEach items="${boards}" var="board">
                <a class="list-item" href="board/${board.id}/details">
                    <div class="list-item-content">
                        <span class="list-item-title">${board.name}</span>
                        <span class="list-item-info"><fmt:message key="updated_on"/> <fmt:formatDate value="${board.update}" type="DATE" dateStyle="LONG" /></span>
                    </div>
                    <button class="mdc-button mdc-button--unelevated list-item-action" aria-label="<fmt:message key="edit_board"/> <c:out value="${board.name}" />"
                            data-link-href="board/${board.id}/edit">
                        <span class="mdc-button__ripple"></span>
                        <span class="material-icons" aria-hidden="true">edit</span>
                    </button>
                    <button type="button" class="mdc-button mdc-button--unelevated list-item-action"
                            aria-label="<fmt:message key="remove_board"/> <c:out value="${board.name}" />"
                            data-delete-btn="${board.id}">
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
                <fmt:message key="msg.confirm_delete_board"/>
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