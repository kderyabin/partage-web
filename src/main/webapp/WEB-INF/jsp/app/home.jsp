<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />

<jsp:include page="../partial/header.jsp" />
<main class="main-container">
    <div class="content-column">
        <h1 class="align-center">
            <fmt:message key="create_account" />
        </h1>
        <p>
            Creating your space... ${ userId }
        </p>
    </div>
</main>
<jsp:include page="../partial/footer.jsp" />