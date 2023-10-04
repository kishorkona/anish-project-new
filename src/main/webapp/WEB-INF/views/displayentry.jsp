
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<html>
<head>
<title>Save Entry Page</title>
<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
</head>
<body>
<div class="header">
	<div class="first">
	<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75"><br/>
	</div>
	<div class="second">Tests App</div>
</div><br><br><hr/>
Welcome <span style="color:rgb(0, 0, 255);padding-left:5px">${user.username}</span>
<a href="./signout" style="color:red;float:right;padding-right:10px"><b>Singout</b></a>
<div class="bodypart">
	<div class="bodypart1">
	<img src="<c:url value="/images/diary.jpg"/>"></div>
	<div class="bodypart2">
	<h1>VIEW ENTRY</h1><br/><br/>
	<Table>
		<tr><td>Date:<c:out value="${date}"/></td><td></td></tr>
		<tr><td>Description:</td><td><c:out value="${description}"/></td></tr>
	</Table><br/><br/><br/>
	<button style="float:right;background-color:SteelBlue;color:White" onclick="window.location.href='./userhome';">Back to Home</button>
	</div>
</div>
</body>
</html>