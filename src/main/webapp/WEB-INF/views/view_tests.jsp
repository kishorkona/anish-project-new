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
		<c:if test="${error!=null}">${error}</c:if>
		<table border="1" style="width:90%;margin:0 auto auto">
			<tr>
				<th>Tests Date</th>
				<th>Grade</th>
				<th>Question To Complete</th>	
			</tr>
			<c:forEach items="${data}" var="current">
		        <tr>
		        	<td><a href="\anishtests/tests/getTestLink/${current.tradeDate}">${current.tradeDate}</a></td>
		        	<td align="center"><c:out value="${current.grade}"/></td>
		        	<td align="center"><c:out value="${current.noOfRecords}"/></td>
		        </tr>
			</c:forEach>
		</table>
	</div>
</div>
</body>
</html>