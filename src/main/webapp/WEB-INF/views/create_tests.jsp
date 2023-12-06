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
	<div class="bodypart1">
   	<img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
	<div class="bodypart2">
		<form action="\anishtestsnew/tests/build" method="post">
			<table class="center" style="width:100%">
				<tr><th>Create Test</th></tr>
				<tr><td>Choose Subject:&nbsp;&nbsp;
					<select name="subject" id="subject">
					  <option value="math">Math</option>
					  <!--
					  <option value="science">Science</option>
					  <option value="social-studies">Social Studies</option>
					  -->
					  <option value="ela">Language Arts</option>
					</select>
				</td></tr>
				<tr><td><br/></td></tr>
				<tr><td>Grade: 
				<select name="grade" id="grade">
					<option value="1">1</option>
					<option value="2">2</option>
				  	<option value="4">4</option>
				  	<option value="5">5</option>
				</select>
				</td></tr>
				<tr><td><br/></td></tr>
				<tr><td align="right"><button type="submit" class="button button5">Create Test</button></td></tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>