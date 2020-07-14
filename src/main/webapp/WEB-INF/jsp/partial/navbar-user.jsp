<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8" %>
<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<header class="mdc-top-app-bar">
    <nav class="mdc-top-app-bar__row">
        <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-start">
        </section>
        <section class="mdc-top-app-bar__section partage-top-app-bar__section--align-center">
            <span class="mdc-top-app-bar__title">Your boards</span>
        </section>
        <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-end" role="toolbar">
            <section class="mdc-top-app-bar__section mdc-top-app-bar__section--align-start">
            </section>
            <c:if test="${ navbarBtnPticipants }">
                <a href="participants.html" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                   aria-label="Participants">
                    people_outline
                </a>
            </c:if>
            <c:if test="${ navbarBtnBoardAdd }">
                <a href="board-edit.html" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                   aria-label="Add board">add
                </a>
            </c:if>
            <div class="mdc-menu-surface--anchor">
                <button id="settings-btn" class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
                        aria-label="Settings">more_vert
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

        </section>
    </nav>
</header>
