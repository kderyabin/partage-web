<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="../partial/header.jsp" />
<jsp:include page="../partial/navbar-user.jsp" />
<main class="main-container">
    <form method="post">
        <div class="content-column">
            <div class="input-group-column">
                <label for="name" class="label-inline-block"><fmt:message key="name"/></label>
                <input type="text" name="name" id="name">
            </div>
            <div class="input-group-column">
                <label for="description"><fmt:message key="description"/></label>
                <textarea name="description" id="description"></textarea>
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
            <c:if test="${ !empty participants}">

            <div class="input-group-column">
                <ul class="mdc-list list-condensed" id="participants" aria-live="assertive">
                    <li class="mdc-list-item" tabindex="0">
                        <span class="mdc-list-item__ripple"></span>
                        <div class="list-condensed-item-content">
                            John
                            <button type="button" class="mdc-button mdc-button--unelevated list-condensed-button"
                                    aria-label="Remove John">
                                <span class="mdc-button__ripple"></span>
                                <span class="material-icons">delete</span>
                            </button>
                        </div>
                    </li>
                    <li class="mdc-list-item" tabindex="0">
                        <span class="mdc-list-item__ripple"></span>
                        <div class="list-condensed-item-content">
                            Anna
                            <button type="button" class="mdc-button mdc-button--unelevated list-condensed-button"
                                    aria-label="Remove John">
                                <span class="mdc-button__ripple"></span>
                                <span class="material-icons">delete</span>
                            </button>
                        </div>
                    </li>
                </ul>
            </div>
            </c:if>
            <div class="input-group-column">
                <label for="person" class="label-inline-block"><fmt:message key="add_new_participant"/></label>
                <input type="text" name="person" id="person" maxlength="50">
            </div>
            <c:if test="${ !empty persons}">
                <span class="h1 align-center"><fmt:message key="or"/></span>

                <div class="input-group-column">
                    <label for="participant" class="label-inline-block"><fmt:message key="add_registered_participant"/></label>
                    <select name="participant" id="participant">
                        <option value=""><fmt:message key="choose_from_the_list"/></option>
                        <c:forEach items="${persons}" var="person">
                            <option value="${person.id}">${person.name}</option>
                        </c:forEach>
                    </select>
                </div>
            </c:if>
            <div class="participant-add-button-box">
                <button type="button" class="mdc-button mdc-button--unelevated button-rounded">
                    <span class="mdc-button__ripple"></span>
                        <fmt:message key="button.add_participant"/>
                </button>
            </div>

        </div>
    </form>
</main>

<jsp:include page="../partial/footer-user.jsp" />