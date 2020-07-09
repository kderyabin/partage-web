<%@ page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="partial/header.jsp"/>
<main class="main-container">
    <div class="content-column">
        <h1 class="align-center">
            ${errorCode}
        </h1>
        <p class="h2 align-center">${errorMsg}</p>
    </div>
</main>

<jsp:include page="partial/footer.jsp"/>