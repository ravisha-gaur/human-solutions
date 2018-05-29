<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>End Task</title>
<link rel="icon" href="/resources/images/favicon.ico?" type="image/x-icon">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>

<script type="text/javascript">
$(document).ready(function (){
	sessionNumber = ${sessionNumber};
	if(sessionNumber == 7){
		$('#session7').css('display','block');
		$('#sessions').css('display','none');
	}
});
</script>
</head>
<body>
<div class="topnav">
	<a onclick="location.href='welcome.htm';">Welcome</a>
	<a onclick="location.href='instructions.htm?sessionId=${sessionId}';">Instructions</a>
	<a onclick="location.href='overview.htm?sessionId=${sessionId}';">Overview</a>
	<c:url value="/system_logout.htm?sessionId=${sessionId}" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
   <a href="javascript:;" onclick="document.getElementById('logoutForm').submit();" style="float:right">Logout</a>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
</div>
<div id="login-box">
<p id="sessions">Thank you for your work! <span class="clear"></span>
You will find new tasks here tomorrow at 00:00 GMT</p>
<p id="session7" style="display: none;">Thank you for your work! We will transfer your earned amount to your account in Upwork.</p>
</div>
</body>
</html>