<%@ page language="java" contentType="text/html; charset=ISO-8859-1" pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Overview</title>
<link rel="icon" href="/resources/images/favicon.ico?" type="image/x-icon">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
</head>
<script type="text/javascript">
$(document).ready(function(){
	var earningsArray = new Array();
	$('.earnings').each(function(){
		earningsArray.push($(this).html());
	});
	var total = 0;
	for (var i = 0; i < earningsArray.length; i++) {
	    total += earningsArray[i] <<0;
	}
	$('#total').text(total);
});

</script>
</head>
<body>
<div class="topnav">
	<a onclick="location.href='welcome.htm';">Welcome</a>
	<a onclick="location.href='instructions.htm?sessionId=${sessionId}';">Instructions</a>
	<a onclick="location.href='overview.htm?sessionId=${sessionId}';">Overview</a>
	<c:url value="/j_spring_security_logout?sessionId=${sessionId}" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
   <a href="javascript:;" onclick="document.getElementById('logoutForm').submit();" style="float:right">Logout</a>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
	
</div>
<h1 style="margin-left: 50px;">Overview</h1>
<div style="text-align: center; margin-left: -150px;">
	<h3>Please click on session due today to start working on the task</h3>
</div>
<table class="overview">
<thead>
  <tr style="height: 65px;">
    <th style="width: 40%;">Session</th>
    <th style="width: 30%;">Status</th> 
    <th style="width: 40%;">Earnings ($)</th>
  </tr>
  </thead>
  <tbody>
  
  	<c:forEach items="${earningsList}" var="earningsDetails">
	  	<tr style="height: 50px;">
	  	<c:choose>
  		  <c:when test="${earningsDetails.diffInDays == 0 && earningsDetails.sessionStatus eq 'Incomplete'}">
	  	  	<td><a href="transcribe_texts.htm?sessionId=${earningsDetails.sessionNumber}&imageId=${imageId}">Session ${earningsDetails.sessionNumber} 
	  	  	</a><c:if test="${not empty dueToday and dueToday eq 'dueToday'}"><sup class="supClass">Due Today</sup></c:if></td>
	  	  </c:when>
	  	  <c:otherwise>
	  	  	<td>Session ${earningsDetails.sessionNumber}</td>
	  	  </c:otherwise>
	  	</c:choose>
	  	<td>${earningsDetails.sessionStatus}</td>
	  	<td class="earnings">${earningsDetails.earnings}</td>
	  	</tr>
  	</c:forEach>

  <tr>
  <td></td>
  <td></td>
  <td style="height:35px; text-align: left;">Total Earnings: <p style="display:inline;" id="total"></p> $</td>
  </tr>
  </tbody>
</table>
<br>
<c:if test="${sessionScope.count > 1 && empty sessionScope.endTask}">
	<div style="text-align: center;">
		<input class="btn" name="submit" type="button" value="Back to Task" onclick="window.history.go(-1); return false;" style="width: 10%;"/>
	</div>
</c:if>
</body>
</html>
