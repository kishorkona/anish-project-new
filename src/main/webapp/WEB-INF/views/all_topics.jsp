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
		<table border="1" style="width:90%;margin:0 auto auto">
			<tr><th align="center">Anish</th></tr>
			<tr><td><a href="\anishtestsnew/tests/view/anish">View IXL Tests</a></td></tr>
			<tr><td><a href="\anishtestsnew/jei/anish/english">Topics Covered In JEI English</a></td></tr>
			<tr><td><a href="\anishtestsnew/jei/anish/math">Topics Covered In JEI Math</a></td></tr>
		</table>
		<br><br>
		<table border="1" style="width:90%;margin:0 auto auto">
			<tr><th align="center">Ishant</th></tr>
			<tr><td><a href="\anishtestsnew/tests/view/ishant">View IXL Tests</a></td></tr>
			<tr><td><a href="\anishtestsnew/redwords/takeTest">View RedWords</a></td></tr>
			<tr><td><a href="\anishtestsnew/vocabulory/takeTest/ishant">View Ishant Vocabulary</a></td></tr>
			<tr><td><a href="\anishtestsnew/jei/ishant/english">Topics Covered In JEI English</a></td></tr>
		</table>
	</div>
</div>
</body>
</html>