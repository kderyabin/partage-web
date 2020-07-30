<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt" %>

<fmt:setLocale value="${ lang }"/>
<fmt:setBundle basename="messages"/>

<jsp:include page="../partial/header.jsp"/>
<jsp:include page="../partial/navbar-user.jsp"/>

<main class="main-container">
    <div class="content-column">
        <h2 class="align-center"><fmt:message key="expenses"/></h2>
        <c:if test="${ isEmpty}">
            <h3 class="align-center"><fmt:message key="no_data_to_display"/></h3>
        </c:if>

        <c:if test="${ !isEmpty}">
            <figure class="highcharts-figure board-balance-chart">
                <div id="chartContainer"></div>
            </figure>

            <h3 class="align-center"><fmt:message key="balance"/></h3>
            <p class="h5"><fmt:message key="currency"/>: ${currency}</p>

            <table class="balance-table">
                <thead>
                <tr>
                    <th><fmt:message key="participant"/></th>
                    <th><fmt:message key="expenses"/></th>
                    <th><fmt:message key="average"/></th>
                    <th><fmt:message key="balance"/></th>
                </tr>
                </thead>
                <tfoot></tfoot>
                <tbody>
                <c:forEach items="${balances}" var="boardPersonTotal">
                    <tr>
                        <td>${boardPersonTotal.person.name}</td>
                        <td>${boardPersonTotal.total}</td>
                        <td>${boardPersonTotal.boardAverage}</td>
                        <td>${boardPersonTotal.balance}</td>
                    </tr>
                </c:forEach>
                </tbody>
            </table>

            <c:if test="${!empty refundments}">
                <h3 class="align-center"><fmt:message key="refundment"/></h3>
                <p class="h5"><fmt:message key="currency"/>: ${currency}</p>

                <table class="balance-table">
                    <thead>
                    <tr>
                        <th><fmt:message key="debtor"/></th>
                        <th><fmt:message key="creditor"/></th>
                        <th><fmt:message key="amount"/></th>
                    </tr>
                    </thead>
                    <tfoot></tfoot>
                    <tbody>
                    <c:forEach items="${refundments}" var="refundment">
                        <tr>
                            <td>${ refundment.debtor }</td>
                            <td>${ refundment.creditor }</td>
                            <td>${ refundment.amount }</td>
                        </tr>
                    </c:forEach>
                    </tbody>
                </table>
            </c:if>
        </c:if>
    </div>
</main>

<script>
    const chartData = {
        categories: [], series: [{
            name: "<fmt:message key="overpaid"/>",
            data: []
        }, {
            name: "<fmt:message key="debt"/>",
            data: []
        }, {
            name: "<fmt:message key="paid"/>",
            data: []
        }]
    };
    <c:if test="${ !empty chartData}">
    <c:forEach items="${chartData}" var="personStats" >
    chartData.categories.push("${ personStats.key }");
    chartData.series[0].data.push(${ personStats.value.overpaid });
    chartData.series[1].data.push(${ personStats.value.debt });
    chartData.series[2].data.push(${ personStats.value.paid });
    </c:forEach>;
    </c:if>
</script>

<jsp:include page="../partial/footer-user.jsp"/>