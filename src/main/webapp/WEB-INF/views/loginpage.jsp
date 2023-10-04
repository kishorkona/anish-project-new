<%@ taglib uri="http://java.sun.com/jsp/jstl/core" prefix="c" %>

<html>
<head>
<title>Tests App HomePage</title>
<link rel="stylesheet" href="<c:url value="/styles/style.css"/>">
</head>
<body>
<div class="header">
<div class="first">
<img src="<c:url value="/images/tests.jpg"/>" width="75" height="75"></br>
</div>
<div class="second">Tests App</div>
</div>
<br>
<br>
<hr/>

<div class="bodypart">
<div class="bodypart1">
<img src="<c:url value="/images/diary.jpg"/>">
</div>
<div class="bodypart2">
<h1>LOGIN HERE</h1><br/><br/>
<form action="./authenticate" method="POST">
<label>username</label><input type="text" name="username" class="formcontrol"><br/><br/>
<label>password</label><input  type="password" name="password" class="formcontrol"><br/><br/></br>
<button type="submit">LOGIN</button>

</form>
<br/><br/>

New User? <a href="./register">Register</a> here
</div>


</div>


</body>
</html>