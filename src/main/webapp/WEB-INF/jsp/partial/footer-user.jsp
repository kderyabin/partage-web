<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>
<footer>
    <div class="mdc-snackbar">
        <div class="mdc-snackbar__surface">
            <div id="snackbar-msg" class="mdc-snackbar__label"
                 role="status"
                 aria-live="polite">
            </div>
            <div class="mdc-snackbar__actions">
                <button type="button" class="mdc-button mdc-snackbar__action">
                    <div class="mdc-button__ripple"></div>
                    <span class="mdc-button__label"><fmt:message key="OK"/></span>
                </button>
            </div>
        </div>
    </div>
</footer>
<c:if test="${ notification != null}">
<script type="application/javascript">
    const notifications = { display: "${notification.display}", i18n : {}};
    <c:if test="${! empty notification.i18n}">
        <c:forEach items="${notification.i18n}" var="entyMap" >
            notifications.i18n["${entyMap.key}"] = "<c:out value="${entyMap.value}"/>" ;
        </c:forEach>
    </c:if>
</script>
</c:if>
<script type="application/javascript" src="${contextPath}/js/mdc.menu.js" defer></script>
<script type="application/javascript" src="${contextPath}/js/mdc.snackbar.min.js" defer></script>
<script type="application/javascript" src="${contextPath}/js/common.js" defer></script>
<c:if test="${!empty scripts}">
    <!-- Application scripts loaded per page -->
    <c:forEach items="${scripts}" var="script">
        <script type="application/javascript" src="${contextPath}/js/${script}" defer></script>
    </c:forEach>
</c:if>
</body>
</html>