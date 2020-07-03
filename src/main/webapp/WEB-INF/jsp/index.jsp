<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="partial/header.jsp" />

<main class="main-container">

    <div class="content-column">

        <h1 class="align-center">Sign in</h1>

        <form method="post">
            <div class="partage-auth">
                <div class="mdc-card partage-auth-form">

                    <div class="input-group-column">
                        <label for="email">Email</label>
                        <input type="email" id="email" name="email" maxlength="100" required>
                    </div>
                    <div class="input-group-column">
                        <div class="partage-signin-password-label">
                            <label for="pass">Password</label> <a href="password-reset.html">Forgot password ?</a>
                        </div>
                        <input type="password" id="pass" name="pass">
                    </div>

                    <div class="partage-auth-button-box">
                        <button type="submit" class="mdc-button mdc-button--unelevated button-rounded">
                            <span class="mdc-button__ripple"></span> Sign in
                        </button>
                    </div>
                </div>

                <div class="mdc-card partage-auth-invite">
                    <p>New? <a href="subscribe.html">Create an account</a></p>
                </div>
            </div>
        </form>
    </div>
</main>

<jsp:include page="partial/footer.jsp" />