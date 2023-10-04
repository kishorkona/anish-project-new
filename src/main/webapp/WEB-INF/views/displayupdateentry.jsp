
<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<%@ taglib uri="http://java.sun.com/jsp/jstl/fmt" prefix="fmt" %>

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
	<h1>UPDATE ENTRY</h1><br/><br/>
	<form action="./processentryupdate" method="post">
		<label>Date: </label><input type="text" name="entrydate" class="formcontrol" value="<fmt:formatDate value="${data.entrydate}" pattern="yyyy-MM-dd"/>"readonly>
		<br/><br/>
		<label>Description: </label>
		<textarea rows="20" cols="50" name="description">${data.description}</textarea>
		<input type="hidden" name="userId" value="${data.userId}">
		<input type="hidden" name="id" value="${recordId}">
		<br/><br/><br/>
		<button type="submit">Update Entry</button>
	</form>
	</div>
</div>
</body>
</html>