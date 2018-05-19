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
	
	var tasksNotAvailable = "${tasksNotAvailable}"
	
	var treatmentMsgType = "${treatmentMsgType}"
		
	var sessionNumber = ${sessionNumber};
	if(sessionNumber == 1){
		$("#session2").css('display','none');
		if(treatmentMsgType === "Baseline"){
			$("#videoBtn").css('display','block');
			$("#myBtn").css('display','none');
		}
		else {
			$("#videoBtn").css('display','none');
			$("#myBtn").css('display','block');
		}
	}
	 else if(sessionNumber == 4){
		$("#session1").css('display','none');
		$("#login-box").css('width',400);
		if(treatmentMsgType === "Baseline" || treatmentMsgType === "Msg_once"){
			$("#videoBtn").css('display','block');
			$("#myBtn").css('display','none');
		}
		else {
			$("#videoBtn").css('display','none');
			$("#myBtn").css('display','block');
		}
	}
	else{
		$("#session1").css('display','none');
		$("#login-box").css('width',400);
		if(treatmentMsgType === "Baseline" || treatmentMsgType === "Msg_once" || treatmentMsgType === "Msg_low"){
			$("#videoBtn").css('display','block');
			$("#myBtn").css('display','none');
		}
		else {
			$("#videoBtn").css('display','none');
			$("#myBtn").css('display','block');
		}
	}
	
	$('#myBtn').click(function() {
		 $("#myModal").css("display","block");
		 $("#popUpMsg").css("display","block");
	});
	
	
	$(".close").click(function() {
		 $("#myModal").css("display","none");
	});
	
	if(tasksNotAvailable == "true") {
		$("#session1").css('display','none');
		$("#session2").css('display','none');
		$("#videoBtn").css('display','none');
		$("#myBtn").css('display','none');
		$("#tasksNotAvailable").css('display','block');
	}
});

</script>
</head>
<body>
<div class="topnav">
	<c:url value="/j_spring_security_logout?sessionId=${sessionId}" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
   <a href="javascript:;" onclick="document.getElementById('logoutForm').submit();" style="float:right">Logout</a>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
	
</div>
<div id="login-box" style="width: 850px;">
<p id="session1">
Thank you for accepting to work for us!<br><br>
In the following week, you are asked to transcribe short texts which have been scanned from German documents. 
You will be provided with a batch of XX scanned images at the beginning of each day (0:00 GMT). 
You are asked to complete the work of each day by 20:00 GMT. We expect each session to last about one hour.
<br><br>You will be provided with the instructions on the next screen. Please read them carefully. 
You will be able to review the instructions at any time.
<br><br>You will receive $35 for completing the work, i.e., $5 for each completed batch. 
The payment will be released at the end of the week upon completing all seven days work on time. 
It is possible that some of the images are too blurry to be readable. Reporting those as unreadable is acceptable and 
will not affect your payment.
</p>
<p id="session2">
Thank you for your work in yesterday's session. So far you have earned $ ${earnings} for your work.
Please complete the work until 20:00 GMT.
If you need to review the instructions, please <a href="instructions.htm?sessionId=${sessionId}">click here. </a>
</p>
<p id="tasksNotAvailable" style="display: none;">
Thank you for logging in. At the moment, there are no tasks available for you. 
Please login after 0.00GMT to find new tasks.
</p>
<br>
<div class="clear"></div>
<div style="text-align: center;">
	<input class="btn" name="submit" type="button" value="Proceed" id="myBtn" style="margin-left: 270px;"/>
	<input class="btn" name="submit" type="button" value="Proceed" id="videoBtn" onclick="location.href='instructions.htm?sessionId=${sessionId}';" style="margin-left: 125px; display:none;width: 20%;"/>
</div>
</div>
<%-- <div id="myModal" class="modal">

  <!-- Modal content -->
   <div class="modal-content">
    <span class="close">&times;</span>
    <div class="clear"></div>
    <p id=popUpMsg style="display:none;">${treatmentMsg}</p><br>
  	<input class="btn" style="width: 20%; margin-left: 220px; position: relative;" name="Continue" type="button" value="Continue" onclick="location.href='instructions.htm?sessionId=${sessionId}';" />
  </div> 

</div> --%>

  <div class="modal fade" id="myModal" role="dialog">
    <div class="modal-dialog">
    
      <!-- Modal content-->
      <div class="modal-content">
        <!-- <div class="modal-header"> -->
          <span class="close">&times;</span>
          <!-- <h4 class="modal-title">Modal Header</h4> -->
        <!-- </div> -->
        <div class="modal-body">
        <p id=popUpMsg style="display:none;">${treatmentMsg}</p><br>
          <!-- <p>Some text in the modal.</p> -->
        </div>
        <div class="modal-footer">
        	<input class="btn btn-default" style="text-align:center; position: relative; left: 30%;" name="Continue" type="button" value="Continue" onclick="location.href='instructions.htm?sessionId=${sessionId}';" />
          <!-- <button type="button" class="btn btn-default" data-dismiss="modal">Close</button> -->
        </div>
      </div>
      
    </div>
  </div>
</body>
</html>