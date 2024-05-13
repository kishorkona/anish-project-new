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
		<img src="<c:url value="/images/tests.jpg"/>" width="50" height="75">
		</div>
		<div class="second">Tests App</div>
	</div><br/><br/>
<hr/><br/>
<div class="bodypart">
	<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
	<div class="bodypart2">
		<c:if test="${error!=null}">${error}</c:if>
		<table border="1" style="width:100%;margin:0 auto auto">
			<tr>
				<th>Tests Id</th>
				<th>Tests Name</th>
				<th>Question To Complete</th>	
				<th>Complex Questions</th>
				<th>Completed Questions</th>
				<th>Pratice Questions</th>
			</tr>
			<c:forEach items="${data}" var="current">
		        <tr>
		        	<td align="center">${current.questionId}</td>
		        	<td align="left"><c:out value="${current.questionName}"/></td>
		        	<td align="center">
		        		<a href="\anishtestsnew/tests/currentQuestions/${current.questionId}">${current.totalCurrentQuestions}</a>
		        	</td>
		        	<td align="center">
		        		<a href="\anishtestsnew/tests/complexQuestions/${current.questionId}">${current.totalComplexQuestions}</a>
		        	</td>
		        	<td align="center">
		        		<a href="\anishtestsnew/tests/completedQuestions/${current.questionId}">${current.totalCompletedQuestions}</a>
		        	</td>
		        	<td align="center">
		        		<a href="\anishtestsnew/tests/practiceQuestions/${current.questionId}">${current.totalPracticeQuestions}</a>
		        	</td>
		        </tr>
			</c:forEach>
		</table>
	</div>
</div>
</body>
</html>