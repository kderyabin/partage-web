<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>


<main class="main-container">
    <div class="content-column">

        <c:if test="${ !empty model.description}">
        <h2 class="align-center">${ model.description }</h2>
        </c:if>

        <c:if test="${ empty items}">
            <h3 class="text-justify"><fmt:message key="no_data_to_display"/> </h3>
        </c:if>

        <c:if test="${ !empty items}">
        <figure class="highcharts-figure board-balance-chart}">
            <div id="chartContainer" role="graphics-document"></div>
        </figure>
        <c:forEach items="${items}" var="item">
            <a class="list-item" href="item?iid=${item.id}">
                <div class="list-item-content">
                    <span class="list-item-title">${item.title}</span>
                    <span class="list-item-info">${item.person.name}</span>
                </div>
                <div  class="list-item-currency">
                    <span class="list-item-title">${item.amount}&nbsp;${currency}</span>
                    <span class="list-item-info"><fmt:formatDate type="date" dateStyle="long" value="${item.date}"/></span>
                </div>
            </a>
        </c:forEach>
        </c:if>
    </div>
</main>

<script>
    const pieData = [];
<c:if test="${ !empty chartData}">
        <c:forEach items="${chartData}" var="keySet" >pieData.push({name:"<c:out value="${keySet.key}"/>", y: ${keySet.value} });</c:forEach>;
</c:if>
</script>

<jsp:include page="../partial/footer-user.jsp"/>