
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Save Entry Page</title>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>User Home page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
<div class="navbar">
	
		<div class="topnav-right">	
	<a href="./signout">Logout</a></div>
		</div> 

<div class="header">
	<div class="first">
	<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75"><br/>
	</div>
	<div class="second">Tests App</div>
</div><br><br><hr/><br/>
<div class="userhome">
Welcome <span style="color:rgb(0, 0, 255);padding-left:5px"> ${user.username} </span>
<br/><br/><br/><br/>
<div class="bodypart">
	<div class="bodypart1">
	<img src="<c:url value="/images/diary.jpg"/>"></div>
	<div class="bodypart2">
	<h1>ADD ENTRY</h1><br/><br/>
	<form action="./saveentry" method="post">
		<label>Date: </label><input type="date" size=5 name="entrydate" class="formcontrol"><br/><br/>
		<label>Description: </label>
		<textarea rows="20" cols="50" name="description"></textarea>
		<input type="hidden" name="userId" value="${user.id}">
		<br/><br/><br/>
		<button type="submit">Save Entry</button>
	</form>
	</div>
</div></div>
</body>
</html>