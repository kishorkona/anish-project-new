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
h1 {
  font-size: 30px;
}
</style>
<head>
<script> 
localStorage.setItem('questionTime', new Date()); 
var q_Time = countDownDate = new Date(localStorage.getItem('questionTime'));

var countDownDate = localStorage.getItem('startDate'); 
if (countDownDate) { 
    countDownDate = new Date(countDownDate); 
} else { 
    countDownDate = new Date(); 
    localStorage.setItem('startDate', countDownDate); 
} 

// Update the count down every 1 second 
var x = setInterval(function() { 
    // Get todays date and time 
    var now = new Date().getTime(); 
 
    // Find the distance between now an the count down date 
    var distance = now - countDownDate.getTime(); 
 
    // Time calculations for days, hours, minutes and seconds 
    //var days = Math.floor(distance / (1000 * 60 * 60 * 24)); 
    var hours = Math.floor((distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var minutes = Math.floor((distance % (1000 * 60 * 60)) / (1000 * 60)); 
    var seconds = Math.floor((distance % (1000 * 60)) / 1000); 
 
    // Output the result in an element with id="demo" 
    document.getElementById("demo").innerHTML = hours + "h " + minutes + "m " + seconds + "s "; 
    
	// Setting Question Time
    var q_now = new Date().getTime(); 
    var q_distance = q_now - q_Time.getTime(); 
    var q_hours = Math.floor((q_distance % (1000 * 60 * 60 * 24)) / (1000 * 60 * 60)); 
    var q_minutes = Math.floor((q_distance % (1000 * 60 * 60)) / (1000 * 60)); 
    var q_seconds = Math.floor((q_distance % (1000 * 60)) / 1000);  
    document.getElementById("questionTime").innerHTML = q_hours + "h " + q_minutes + "m " + q_seconds + "s ";
     
}, 1000); 
</script>
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
	<div class="bodypart1"><img src="<c:url value="/images/users.jpg"/>" width="500" height="500"></div>
	<div class="bodypart2">
		<table class="center" style="width:100%">
			<tr>
				<td><b>Subject:</b> ${data.subject}</td>
				<td><b>Grade:</b> ${data.grade}&nbsp;&nbsp; <b>Question No:</b>${data.id}</td>
			</tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr>
				<td><b>Questions Left:</b>  ${data.totalCurrentQuestions}</td>
				<td><b>Section:</b> ${data.sectionId}.${data.subSectionId} &nbsp;&nbsp;&nbsp; <b>Section Name:</b> ${data.sectionName}</td>
			</tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr><td colspan="2"><b>TestId:</b>&nbsp;${data.questionId}&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;&nbsp;<b>Test Name:</b>&nbsp;${data.questionName}</td></tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr><td colspan="2"><a href="${data.url}" target="_blank"><b>${data.url}</b></a></td></tr>
			<tr><td colspan="2"><br/></td></tr>
			<tr>
				<td  colspan="2"align="right">
					<a href="\anishtestsnew/tests/nextQuestion/${data.questionId}/${data.sectionId}/${data.subSectionId}/${user_name_key}"><b>Next Question</b></a>
				</td>
			</tr>
		</table>
		<br/>
		<table class="center" style="width:100%">
			<tr>
				<td align="left">Question Time:<h1><p id="questionTime"></p></h1></td>
				<td align="right">Test Time:<h1><p id="demo"></p></h1></td>
			</tr>
		</table>
	</div>
</div>
</body>
</html>