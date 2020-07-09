<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="partial/header.jsp" />
<main class="main-container">
	<div class="content-column">
		<h1 class="align-center">
			<fmt:message key="create_account" />
		</h1>
		<form method="post">
			<div class="partage-auth">
				<div class="mdc-card partage-auth-form">
                    <div class="input-group-column">
						<label for="name"><fmt:message key="name" /><span class="required">*</span> </label>
						<c:if test="${!empty errors && !empty errors.name}">
							<c:forEach items="${ errors.name }" var="errorMsg">
							<span class="input-error"><fmt:message key="${ errorMsg }" /></span>
							</c:forEach>
						</c:if>
						<input type="text" id="name" name="name" maxlength="100" value="${ name }" required>
					</div>
					<div class="input-group-column">
					
						<label for="login"><fmt:message key="email" /><span class="required">*</span> </label>
						<c:if test="${!empty errors && !empty errors.login}">
							<c:forEach items="${ errors.login }" var="errorMsg">
							<span class="input-error"><fmt:message key="${ errorMsg }" /></span> 
							</c:forEach>
						</c:if> 
						<input type="email" id="login" name="login" maxlength="100" value="${ login }" required>
					</div>

					<div class="input-group-column">
						<label for="pwd"><fmt:message key="password" /> <span class="required">*</span></label>
						<c:if test="${!empty errors && !empty errors.pwd}">
							<c:forEach items="${ errors.pwd }" var="errorMsg">
							<span class="input-error"><fmt:message key="${ errorMsg }" /></span> 
							</c:forEach>
						</c:if> 
						<input type="password" id="pwd" name="pwd" minlength="8" maxlength="20" value="${ pwd }" required> 
						<span class="input-info"><fmt:message key="info.password_requirement" /></span>
					</div>
					<div class="input-group-column">
						<label for="confirmPwd"> <fmt:message key="repeat_password" /> <span class="required">*</span></label> 
						<c:if test="${!empty errors && !empty errors.confirmPwd}">
							<c:forEach items="${ errors.confirmPwd }" var="errorMsg">
							<span class="input-error"><fmt:message key="${ errorMsg }" /></span> 
							</c:forEach>
						</c:if> 
						<input type="password" id="confirmPwd" minlength="8" maxlength="20" name="confirmPwd" value="${ confirmPwd }" required>
					</div>
					<div class="partage-auth-button-box">
						<button type="submit" class="mdc-button mdc-button--unelevated button-rounded">
							<span class="mdc-button__ripple"></span>
							<fmt:message key="create_account" />
						</button>
					</div>
				</div>

			</div>
		</form>
	</div>
</main>
<jsp:include page="partial/footer.jsp" />