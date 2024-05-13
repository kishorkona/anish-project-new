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
				<th>Date</th>
				<th>Book Id</th>
				<th>Topic</th>
			</tr>
			<c:forEach items="${data}" var="cur">
		        <tr>
		        	<td align="center"><c:out value="${cur.date}"/></td>
		        	<td align="center"><c:out value="${cur.bookId}"/></td>
		        	<td align="left"><c:out value="${cur.topic}"/></td>
		        </tr>
			</c:forEach>
		</table>
	</div>
</div>
</body>
</html>