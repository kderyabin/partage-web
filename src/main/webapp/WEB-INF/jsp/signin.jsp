<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="partial/header.jsp" />

<main class="main-container">

    <div class="content-column">

        <h1 class="align-center">Sign in</h1>

        <form method="post">
            <div class="partage-auth">
                <div class="mdc-card partage-auth-form">
				<c:if test="${$isFailedAuth}">
						<div class="input-group-column">
						<p>error</p>
						</div>
				</c:if>
                    <div class="input-group-column">
                        <label for="login">Email</label>
                        <input type="email" id="login" name="login" maxlength="100" required>
                    </div>
                    <div class="input-group-column">
                        <div class="partage-signin-password-label">
                            <label for="pwd">Password</label> <a href="password-reset">Forgot password ?</a>
                        </div>
                        <input type="password" id="pwd" name="pwd">
                    </div>

                    <div class="partage-auth-button-box">
                        <button type="submit" class="mdc-button mdc-button--unelevated button-rounded">
                            <span class="mdc-button__ripple"></span> Sign in
                        </button>
                    </div>
                </div>

                <div class="mdc-card partage-auth-invite">
                    <p>New? <a href="signup">Create an account</a></p>
                </div>
            </div>
        </form>
    </div>
</main>

<jsp:include page="partial/footer.jsp" />