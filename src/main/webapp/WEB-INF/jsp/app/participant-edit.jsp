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
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </p>
            </c:if>
            <div class="input-group-column">
                <label for="name" class="label-inline-block">
                    <fmt:message key="name"/> <span class="required">*</span>
                </label>
                <c:if test="${!empty errors && !empty errors.name}">
                    <c:forEach items="${ errors.name }" var="errorMsg">
                        <span class="input-error" role="alert"><fmt:message key="${ errorMsg }"/></span>
                    </c:forEach>
                </c:if>
                <input type="text" name="name" id="name" value="${ model.name }"
                       required maxlength="50"
                       aria-invalid="${!empty errors && !empty errors.name ? "true": "false"}">
            </div>

            <button id="btn-form-submit" type="submit" class="hidden"><fmt:message key="OK"/></button>
        </form>
    </div>
</main>
<jsp:include page="../partial/dialog.jsp">
    <jsp:param name="dialogId" value="go-back-dialog"/>
    <jsp:param name="dialogMsg" value="msg.confirm_form_exit"/>
</jsp:include>
<jsp:include page="../partial/footer-user.jsp"/>