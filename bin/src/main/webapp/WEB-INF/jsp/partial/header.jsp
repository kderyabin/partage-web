<%@page trimDirectiveWhitespaces="true"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c"%>
<!doctype html>
<html lang="fr">
<head>
<meta charset="utf-8">
<meta name="viewport" content="width=device-width, initial-scale=1">
<title>${ pageTitle }</title>
<link href="https://fonts.googleapis.com/css2?family=Material+Icons&family=Roboto:ital,wght@0,300;0,400;0,700;1,300;1,400;1,700&display=swap" rel="stylesheet">
<link href="css/material-components-web.css" rel="stylesheet">
<!-- <link href="css/product.css" rel="stylesheet"> -->
<!-- <link href="css/main.css" rel="stylesheet"> -->
<body class="mdc-typography">
	<header class="mdc-top-app-bar">
		<nav class="mdc-top-app-bar__row">
			<section
				class="mdc-top-app-bar__section mdc-top-app-bar__section--align-start">
				<button
					class="material-icons mdc-top-app-bar__navigation-icon mdc-icon-button"
					aria-label="Open navigation menu">menu</button>
				<span class="mdc-top-app-bar__title">Page title</span>
			</section>
			<section
				class="mdc-top-app-bar__section mdc-top-app-bar__section--align-end"
				role="toolbar">
				<button
					class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
					aria-label="Favorite">favorite</button>
				<button
					class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
					aria-label="Search">search</button>
				<button
					class="material-icons mdc-top-app-bar__action-item mdc-icon-button"
					aria-label="Options">more_vert</button>
			</section>
		</nav>
	</header>