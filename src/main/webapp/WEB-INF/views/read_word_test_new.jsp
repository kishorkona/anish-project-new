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
	</div>
	<br/><br/>
	<hr/><br/>
	<table style="width:100%;margin:0 auto auto">
		<tr>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
			<td style="width:30%;">
				<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
			</td>
			<td style="width:70%;">
				<table border="1" style="width:90%;margin:0 auto auto">
					<tr><td colspan="2">Test Name:${testName}</td></tr>
					<tr><td colspan="2">&nbsp;&nbsp;&nbsp;&nbsp;</td></tr>
					<tr>
						<td>
							<table border="1" style="width:90%;margin:0 auto auto">
								<tr>
									<th>Redword Name</th>
									<th>Redword Sentence</th>
								</tr>
								<c:forEach items="${subList1}" var="current">
									<tr>
										<td align="center"><c:out value="${current.name}"/></td>
										<td align="center"><c:out value="${current.sentence}"/></td>
										<td align="center"><a href="\anishtestsnew/redwords/doneword/${current.id}/${current.fileNo}">Done</a></td>
									</tr>
								</c:forEach>
							</table>
						</td>
						<td>
							<table border="1" style="width:90%;margin:0 auto auto">
								<tr>
									<th>Redword Name</th>
									<th>Redword Sentence</th>
								</tr>
								<c:forEach items="${subList2}" var="current">
									<tr>
										<td align="center"><c:out value="${current.name}"/></td>
										<td align="center"><c:out value="${current.sentence}"/></td>
										<td align="center"><a href="\anishtestsnew/redwords/doneword/${current.id}/${current.fileNo}">Done</a></td>
									</tr>
								</c:forEach>
							</table>						
						</td>
					</tr>
				</table>
			</td>
			<td>&nbsp;&nbsp;&nbsp;&nbsp;</td>
		</tr>
	</table>
	</br></br>
</body>
</html>