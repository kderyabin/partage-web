<%@page trimDirectiveWhitespaces="true" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<jsp:include page="partial/header.jsp" />

<form class="form-signin" method="post">
		<img class="" src="assets/img/cda.svg" alt="" width="72" height="72" style="border-radius: 50%;">
		<h1 class="">Please sign in</h1>
	
		${ isFailedAuth == true ? '<div class="alert alert-danger" role="alert">Sorry. Bad credential. Try again.</div>' : ''}
		
		<label for="login" class="sr-only">Name</label> <input name="login" type="text" id="login" class="form-control"
			placeholder="Nom" value="${ login }" required autofocus> 
		<label for="pwd" class="sr-only">Password</label> <input
			name="pwd" type="password" id="pwd" class="form-control" placeholder="Password" required>
		<button class="btn btn-lg btn-primary btn-block" type="submit">Sign in</button>
	</form>

<jsp:include page="partial/footer.jsp" />