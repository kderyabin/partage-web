<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<c:set var="contextPath" value="${pageContext.servletContext.contextPath}"/>
<footer>
</footer>
<script type="application/javascript" src="${contextPath}/js/mdc.menu.js" defer></script>
<script type="application/javascript" src="${contextPath}/js/common.js" defer></script>
<c:if test="${!empty scripts}">
    <!-- Application scripts loaded per page -->
    <c:forEach items="${scripts}" var="script">
        <script type="application/javascript" src="${contextPath}/js/${script}" defer></script>
    </c:forEach>
</c:if>
</body>
</html>