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
	<div>
		<c:if test="${error!=null}">${error}</c:if>
		<table border="1" style="width:90%;margin:0 auto auto">
			<tr>
				<th>Test Id</th>
				<th>Test Name</th>
				<th>Subject</th>
				<th>Grade</th>
				<th>Section</th>	
				<th>Section Name</th>
				<th>Url</th>
			</tr>
			<c:forEach items="${data}" var="current">
		        <tr>
		        	<td align="center"><c:out value="${current.questionId}"/></td>
		        	<td align="center"><c:out value="${current.questionName}"/></td>
		        	<td align="center"><c:out value="${current.subject}"/></td>
		        	<td align="center"><c:out value="${current.grade}"/></td>
		        	<td align="left"><c:out value="${current.sectionId}"/>.<c:out value="${current.subSectionId}"/></td>
		        	<td align="left"><c:out value="${current.sectionName}"/></td>
		        	<td align="left"><a href="${current.url}" target="_blank"><b>${current.url}</b></a></td>
		        </tr>
			</c:forEach>
		</table>
	</div>
</div>
</body>
</html>