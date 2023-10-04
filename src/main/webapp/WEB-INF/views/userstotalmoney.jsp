<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<meta name="viewport" content="width=device-width, initial-scale=1">
	<title>User Home page</title>
	<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
	<link rel="stylesheet" href="<c:url value="/styles/menubar.css"/>">
	<link rel="stylesheet" href="https://cdnjs.cloudflare.com/ajax/libs/font-awesome/4.7.0/css/font-awesome.min.css">
	
</head>


<body>
	<div class="navbar">
	<a href="/home">Home</a>
	<a href="./addentry">Add Entry</a>
		<div class="dropdown">
		<button class="dropbtn">Money Details <i class="fa fa-caret-down"></i></button>
			<div class="dropdown-content">
			<a href="./money">Anish</a>
			<a href="./mymoney">Ishant</a>
			<a href="#">Total Money</a>
			</div>
		<div class="topnav-right">	
	<a href="./signout">Logout</a></div>
		</div> 
</div>
	
	<div class="header">
		<div class="first">
		<img src="<c:url value="/images/money.jpg"/>" width="75" height="75">
		</div>
		<div class="second">MyMoney</div>
	</div><br/><br/>
<hr/><br/>
<div class="bodypart">
	<div class="bodypart1">
	
   <img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
	<div class="bodypart2">
<table border="1" style="width:90%;margin:0 auto auto">
	<tr>
		<th>Userid</th>
		<th>User Name</th>
		<th>Sum</th>
		
	</tr>
		<c:if test="${entrieslist.size()==0}">
			<tr>
					<td style="font-size:15px;color:green;text-align:center;padding-right:10px" colspan="4">No money saved</td>
			</tr>
		</c:if>
</table>
Total amount of money ${toalNumberOfEntriesForUser} the users have!
</div></div>
</body>
</html>
