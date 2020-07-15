<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<%@ taglib prefix="fmt" uri="http://java.sun.com/jsp/jstl/fmt"%>

<fmt:setLocale value="${ lang }" />
<fmt:setBundle basename="messages" />
<li class="mdc-list-item" tabindex="0">
    <span class="mdc-list-item__ripple"></span>
    <div class="list-condensed-item-content">
        <span class="participants-name">John</span>
        <button type="button" class="mdc-button mdc-button--unelevated list-condensed-button"
                aria-label="Remove John">
            <span class="mdc-button__ripple"></span>
            <span class="material-icons">delete</span>
        </button>
        <input type="hidden" name="participants[id][]" value="">
        <input type="hidden" name="participants[name][]" value="">
    </div>
</li>
