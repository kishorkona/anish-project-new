<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>
<!DOCTYPE html>
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
/* Popup container - can be anything you want */
.popup {
  position: relative;
  display: inline-block;
  cursor: pointer;
  -webkit-user-select: none;
  -moz-user-select: none;
  -ms-user-select: none;
  user-select: none;
}

/* The actual popup */
.popup .popuptext {
  visibility: hidden;
  width: 500px;
  background-color: #555;
  color: #fff;
  text-align: left;
  border-radius: 6px;
  padding: 8px 0;
  position: absolute;
  z-index: 1;
  bottom: 125%;
  left: 50%;
  margin-left: -80px;
}

/* Popup arrow */
.popup .popuptext::after {
  content: "";
  position: absolute;
  top: 100%;
  left: 50%;
  margin-left: -5px;
  border-width: 5px;
  border-style: solid;
  border-color: #555 transparent transparent transparent;
}

/* Toggle this class - hide and show the popup */
.popup .show {
  visibility: visible;
  -webkit-animation: fadeIn 1s;
  animation: fadeIn 1s;
}

/* Add animation (fade in the popup) */
@-webkit-keyframes fadeIn {
  from {opacity: 0;} 
  to {opacity: 1;}
}

@keyframes fadeIn {
  from {opacity: 0;}
  to {opacity:1 ;}
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
	<div>
		<form action="\anishtestsnew/redwords/nextTest/${user_name_key}" method="post">
			<input type="hidden" name="id" value="${data.id}"/>
			<table class="center" style="width:90%">
				<tr>
					<td align="left">
						<b>Word No:</b> ${data.id} &nbsp;&nbsp;&nbsp; 
						<b>Total Words:</b> ${data.totalWords}
					</td>
				</tr>
				<tr><td align="left"><br/></td></tr>
				<tr><td align="left">Word Location:</td></tr>
				<tr><td align="left"><h3>${filePath}</h3></td></tr>
				<c:if test="${data.hasSentence==true}">
					<tr><td align="left"><br/></td></tr>
					<tr><td align="left">Word In Sentence:</td></tr>
					<tr><td align="left"><h3>${filePathSentence}</h3></td></tr>
				</c:if>
				<tr><td align="left"><br/></td></tr>
				<tr>
					<td align="left">
						<input type="text" id="repeat" name="repeat" value="${data.repeat}" required size="10" />
					</td>
				</tr>
				<tr><td align="left"><br/></td></tr>
				<tr><td align="left"><input type="submit" value="Next Word" /></td></tr>
			</table>
		</form>
	</div>
</div>
</body>
</html>