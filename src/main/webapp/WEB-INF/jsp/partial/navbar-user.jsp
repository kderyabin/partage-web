<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<header class="mdc-top-app-bar">
    <nav class="mdc-top-app-bar__row">
        <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-start">
            <c:if test="${ !empty navbarBtnBackLink }">
                <a id="back-btn" href="${navbarBtnBackLink}" class="material-icons mdc-top-app-bar__navigation-icon mdc-icon-button"
                   aria-label="Open navigation menu">arrow_back
                </a>
            </c:if>
        </section>
        <section class="mdc-top-app-bar__section partage-top-app-bar__section--align-center">
            <span class="mdc-top-app-bar__title">${title}</span>
        </section>
        <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-end">
            <c:if test="${ !empty navbarBtnParticipantsLink }">
                <a href="${ navbarBtnParticipantsLink }"
                   class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                   aria-label="<fmt:message key="participants"/>">
                    <span aria-hidden="true">people_outline</span>
                </a>
            </c:if>
            <c:if test="${ !empty navbarBtnBalanceLink }">
                <a href="${ navbarBtnBalanceLink }" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                   aria-label="<fmt:message key="balance"/>">
                    <span aria-hidden="true">equalizer</span>
                </a>
            </c:if>
            <c:if test="${ !empty navbarBtnAddBoardLink }">
                <a href="${ navbarBtnAddBoardLink }" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                   aria-label="<fmt:message key="add_board"/>">
                    <span aria-hidden="true">add</span>
                </a>
            </c:if>
            <c:if test="${ !empty navbarBtnAddItemLink }">
                <a href="${ navbarBtnAddItemLink }" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                   aria-label="<fmt:message key="add_expense"/>">
                    <span aria-hidden="true">add</span>
                </a>
            </c:if>
            <c:if test="${ navbarBtnSave }">
                <button id="navbarBtnSave" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                        aria-label="Save">
                    <span aria-hidden="true">done</span>
                </button>
            </c:if>
<%--            <c:if test="${ ! empty navbarBtnSettings }">--%>
            <div class="mdc-menu-surface--anchor">
                <button id="settings-btn" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                        aria-label="<fmt:message key="settings"/>">
                    <span aria-hidden="true">more_vert</span>
                </button>
                <div class="mdc-menu mdc-menu-surface">
                    <ul class="mdc-list" role="menu" aria-hidden="true" aria-orientation="vertical" tabindex="-1">
                        <li class="mdc-list-item" role="menuitem">
                            <span class="mdc-list-item__ripple"></span>
                            <a href="settings.html" class="mdc-list-item__text">
                                <fmt:message key="settings"></fmt:message>
                            </a>
                        </li>
                        <li role="separator" class="mdc-list-divider"></li>
                        <li class="mdc-list-item" role="menuitem">
                            <span class="mdc-list-item__ripple"></span>
                            <a href="/${lang}/logout" class="mdc-list-item__text">
                                <fmt:message key="sign_out"></fmt:message></a>
                        </li>
                    </ul>
                </div>
            </div>
<%--            </c:if>--%>
        </section>
    </nav>
</header>
