<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<body>
	<jsp:include page="menu/menu.jsp" />
	<div class="header">
		<div class="first">
		<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75">
		</div>
		<div class="second">Tests App</div>
	</div><br/><br/>
<hr/><br/>

<div class="userhome">
	Welcome <span style="color:rgb(0, 0, 255);padding-left:5px">${user.username}</span><%--jsp expression langauge--%>
	 
	
	<br/><br/><br/><br/>
	<span class="heading" style="color:OrangeRed;padding-left:10px">List of Entries:-${countOfEntries}</span>
	
	<br/><br/><br/>
<table border="1" style="width:90%;margin:0 auto auto">
	<tr>
		<th>Date</th>
		<th colspan="3">Actions</th>
	</tr>
		<c:if test="${entrieslist.size()==0}">
			<tr>
					<td style="font-size:15px;color:green;text-align:center;padding-right:10px" colspan="4">User has not added any diary entries till now!</td>
			</tr>
		</c:if>
		<c:forEach items="${entrieslist}" var="e">
				<tr>
					<td>${e.entrydate}</td>
					<td><b><a href="./viewentry?id=${e.id}">View</a></b></td>
					<td><b><a href="./updateentry?id=${e.id}">Update</a></b></td>
					<td><b><a href="./deleteentry?id=${e.id}">Delete</a></b></td>
				</tr>
		</c:forEach>

</table>	

</div>
</body>
</html>

