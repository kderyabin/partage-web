<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<%@ taglib prefix="fn" uri="http://java.sun.com/jsp/jstl/functions" %>

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
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </p>
            </c:if>
            <div class="input-group-column">
                <label for="title" class="label-inline-block">
                    <fmt:message key="Title"/> <span class="required">*</span>
                </label>
                <c:if test="${!empty errors && !empty errors.title}">
                    <c:forEach items="${ errors.title }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="text" name="title" id="title" value="${ fn:escapeXml(model.title) }" required
                       maxlength="50" aria-invalid="${!empty errors && !empty errors.title ? "true": "false"}">
            </div>
            <div class="input-group-column">
                <label for="amount" class="label-inline-block"><fmt:message key="amount"/> <span
                        class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.amount}">
                    <c:forEach items="${ errors.amount }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="number" name="amount" id="amount" value="${ fn:escapeXml(model.amount) }" required
                       min="0" step="0.01" aria-invalid="${!empty errors && !empty errors.amount ? "true": "false"}">
            </div>

            <div class="input-group-column">
                <label for="date" class="label-inline-block"><fmt:message key="Date"/> <span
                        class="required">*</span></label>
                <c:if test="${!empty errors && !empty errors.date}">
                    <c:forEach items="${ errors.date }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="date" name="date" id="date"
                       min="2010-01-01" max="2025-01-01" required
                       value="${fn:escapeXml(model.date)}"
                       aria-invalid="${!empty errors && !empty errors.date ? "true": "false"}">
            </div>

            <div class="input-group-column">
                <label for="participant" class="label-inline-block">
                    <fmt:message key="paid_by"/> <span class="required">*</span>
                </label>
                <c:if test="${!empty errors && !empty errors.participant}">
                    <c:forEach items="${ errors.participant }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <select name="participant" id="participant"
                        aria-invalid="${!empty errors && !empty errors.participant ? "true": "false"}">
                    <option value=""><fmt:message key="choose_from_the_list"/></option>
                    <c:forEach items="${participants}" var="participant">
                        <option value="${participant.id}" ${ model.participant eq participant.id ? "selected" : ""}>
                                ${participant.name}
                        </option>
                    </c:forEach>
                </select>
            </div>
            <c:if test="${ model.id != null}">
                <input type="hidden" id="itemId" name="id" value="${model.id}">
            </c:if>
            <button id="btn-form-item-submit" type="submit" class="hidden"><fmt:message key="OK"/></button>
        </form>
    </div>
</main>
<jsp:include page="../partial/dialog.jsp">
    <jsp:param name="dialogId" value="go-back-dialog"/>
    <jsp:param name="dialogMsg" value="msg.confirm_form_exit"/>
</jsp:include>
<jsp:include page="../partial/dialog.jsp">
    <jsp:param name="dialogId" value="delete-dialog"/>
    <jsp:param name="dialogMsg" value="msg.confirm_delete_item"/>
</jsp:include>
<jsp:include page="../partial/footer-user.jsp"/>