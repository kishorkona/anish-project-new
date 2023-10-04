<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<style>
table, th, td {
  border:1px solid black;
  border-collapse: collapse;
}
tr {
	height: 25px;
}
table.center {
  margin-left: auto; 
  margin-right: auto;
}
</style>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>User  Money Home page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
</head>
<body>
	<jsp:include page="menu/menu.jsp" />
	<div class="header">
		<div class="first">
		<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75">
		</div>
		<div class="second">Tests App</div>
	</div><br/><br/>
<hr/><br/>
<div class="bodypart">
	<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
	<div class="bodypart2">
		<table class="center" style="width:100%">
			<tr>
				<td>User : </td>
				<td>Grade - ${gradeId}</td>
			</tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr>
				<td><b>Questions Left:</b>  ${totalQuestions}</td>
				<td>${data.code} : ${subjectName} : ${detailId}</td>
			</tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr><td colspan="2"><a href="${data.url}" target="_blank"><b>${data.url}</b></a></td></tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr><td  colspan="2"align="right">
				<!-- 
				<button type="submit" onclick="myFunction('\anishtests/tests/nextQuestion/${testDate}/${data.id}')" 
				class="button button5">Next Question</button>
				-->
				<a href="\anishtests/tests/nextQuestion/${data.testDate}/${data.id}"><b>Next Question</b></a>
			</td></tr>
		</table>
	</div>
</div>
</body>
</html>