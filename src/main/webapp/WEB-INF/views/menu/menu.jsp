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
		<a href="\anishtestsnew/">Home</a>
		<div class="dropdown">
			<button class="dropbtn">Create Test<i class="fa fa-caret-down"></i></button>
			<div class="dropdown-content">
				<a href="\anishtestsnew/tests/create">Create IXL Test</a>
				<a href="\anishtestsnew/vocabulory/create">Create Vocabulary</a>
				<a href="\anishtestsnew/redwords/create">Create ReadWords</a>
			</div> 
		</div>
		<div class="dropdown">
			<button class="dropbtn">View Tests<i class="fa fa-caret-down"></i></button>
			<div class="dropdown-content">
				<a href="\anishtestsnew/tests/view">View IXL Tests</a>
				<a href="\anishtestsnew/redwords/takeTest">View RedWords</a>
				<a href="\anishtestsnew/vocabulory/takeTest/anish">View Anish Vocabulary</a>
				<a href="\anishtestsnew/vocabulory/takeTest/ishant">View Ishant Vocabulary</a>
			</div> 
		</div>
		<a href="\anishtestsnew/tests/disabled">Difficult Tests</a>
	</div>
</body>
</html>

