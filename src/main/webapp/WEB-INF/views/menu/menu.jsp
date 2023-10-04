<%@page import="com.anish.view.SessionView"%>
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>User Home page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
	<div class="navbar">
		<a href="\anishtests/home">Home</a>
		<a href="./addentry">Add Entry</a>
		<div class="dropdown">
			<button class="dropbtn">Money Details <i class="fa fa-caret-down"></i></button>
			<div class="dropdown-content">
				<a href="\anishtests/money"><%= ((SessionView) session.getAttribute("user")).getUsername()%></a>
				<a href="\anishtests/ourmoney">Total Money</a>
			</div> 
		</div>
		<a href="\anishtests/tests/create">Create Test</a>
		<a href="\anishtests/tests/view">View Tests</a>
		<a href="./signout">Logout</a>
	</div>
</body>
</html>

