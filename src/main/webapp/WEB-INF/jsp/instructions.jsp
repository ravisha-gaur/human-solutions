<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Welcome</title>
<link rel="icon" href="/resources/images/favicon.ico?" type="image/x-icon">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
<script type="text/javascript">
$(document).ready(function () {
	$('#backToTask').click( function () {
	  window.location = document.referrer;
	});
});
</script>
</head>
<body>
<div class="topnav">
  <a onclick="location.href='welcome.htm';">Welcome</a>
  <c:choose>
	<c:when test="${sessionScope.count > 1 && empty sessionScope.endTask}">
		<div><a onclick="window.history.go(-1); return false;">Back to Task</a></div>
	</c:when>
	<c:otherwise>
		<div><a> Instructions</a></div>
	</c:otherwise>
</c:choose>
<c:url value="/system_logout.htm?sessionId=${sessionId}" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
   <a href="javascript:;" onclick="document.getElementById('logoutForm').submit();" style="float:right">Logout</a>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
</div>
<div id="login-box" style="width: 850px;">
<h1>Instructions</h1>
<p>
1.	For each image, you will first need to determine whether it is readable or not.<br>
2.	It is possible that some of the images are not readable. <u>Reporting those as unreadable is acceptable and will NOT reduce your payment.</u><br>
3.	If you report the image as readable, you will need to transcribe the text. <br>
4.	Please transcribe all words as running text including the title and notes. Do NOT preserve the stanzas as you transcribe.<Br>
5.	Do NOT transcribe page numbers.<br>
6.	Use the following rules for non-standard characters:<br>
	<span style="margin-left: 30px;">o	Transcribe ä as ae, Ä as Ae </span><br>
	<span style="margin-left: 30px;">o	Transcribe ö as oe, Ö as Oe </span><br>
	<span style="margin-left: 30px;">o	Transcribe ü as ue, Ü as Ue </span><br>
	<span style="margin-left: 30px;">o	Transcribe ß as ss </span><br>
7.	Use a single "-" for both hyphens and dashes. <br>
8.	If you cannot read some characters, replace them with an underscore _.<br>
9.	Press "Submit" after you finished transcribing.<br><br>
If you need to review the information, you can click on "Instructions" in the top menu. 
<br>

</p>
<br>
<div class="clear"></div>
<div class="clear"></div>
<div style="text-align: center;">
<c:choose>
	<c:when test="${sessionScope.count > 1 && empty sessionScope.endTask}">
		<input id="backToTask" class="btn" name="submit" type="button" value="Back to Task" /> <!-- onclick="window.history.go(-1); return false;" -->
	</c:when>
	<c:otherwise>
		<input class="btn" name="submit" type="button" value="Proceed" onclick="location.href='overview.htm?sessionId=${sessionId}';" />
	</c:otherwise>
</c:choose>
</div>
</div>

</body>
</html>