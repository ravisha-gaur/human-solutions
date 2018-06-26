<%@ page language="java" contentType="text/html; charset=ISO-8859-1"
	pageEncoding="ISO-8859-1"%>
<%@taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<%@ taglib prefix="form" uri="http://www.springframework.org/tags/form"%>
<%@page session="true"%>
<!DOCTYPE html PUBLIC "-//W3C//DTD HTML 4.01 Transitional//EN" "http://www.w3.org/TR/html4/loose.dtd">
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1">
<title>Transcribe Text</title>
<link rel="icon" href="/resources/images/favicon.ico?" type="image/x-icon">
<link href="<c:url value="/resources/css/bootstrap.min.css" />" rel="stylesheet">
<link href="<c:url value="/resources/css/style.css" />" rel="stylesheet">
<script src="<c:url value="/resources/js/jquery.1.10.2.min.js" />"></script>
<script src="<c:url value="/resources/js/bootstrap.min.js" />"></script>

<script>
function saveTranscribedText() {
	var text = $('#transcribedText').val();
	var selectedRadioButton = $('input[name=readable]:checked').val();
	if(typeof(selectedRadioButton) === "undefined") {
		$('#selectAnOption').show();
		return false;
	}
	if(selectedRadioButton == 'yes'){
		if(text.trim() === '' || text.trim() === null) {
			$('#emptyTextArea').show();
			return false;
		}
		else{
			$("#taskForm").attr("action", "transcribe_texts.htm?sessionId="+${sessionId}+"&imageId="+${imageId});
			$('#taskForm').submit();
		} 
	}
	 else {
		$("#taskForm").attr("action", "transcribe_texts.htm?sessionId="+${sessionId}+"&imageId="+${imageId});
		$('#taskForm').submit();
	}
}
</script>
<script type="text/javascript">
var resetW;
var resetH;
var currentZoomW;
var currentZoomH;
var currentZoom = 1.0;

function zoomIn(){
	currentZoomW = currentZoomW + 15;
	currentZoomH = currentZoomH + 20;
	currentImgZoomW = currentImgZoomW + 15;
	currentImgZoomH = currentImgZoomH + 20;
	$('#login-box').css('width', currentZoomW);
	$('#login-box').css('height', currentZoomH);
	$('#test').css('width', currentImgZoomW);
	$('#test').css('height', currentImgZoomH);
}

function zoomOut(){
	currentZoomW = currentZoomW - 15;
	currentZoomH = currentZoomH - 20;
	currentImgZoomW = currentImgZoomW - 15;
	currentImgZoomH = currentImgZoomH - 20;
	$('#login-box').css('width', currentZoomW);
	$('#login-box').css('height', currentZoomH);
	$('#test').css('width', currentImgZoomW);
	$('#test').css('height', currentImgZoomH);
}

function resetZoom(){
	currentZoomW = resetW;
	currentZoomH = resetH;
	
	currentImgZoomW = resetImgW;
	currentImgZoomH = resetImgH;
	$('#login-box').css('width', resetW);
	$('#login-box').css('height', resetH);
	$('#test').css('width', resetImgW);
	$('#test').css('height', resetImgH);
}

$(document).ready(function () {
	
	resetW = 480;
	resetH = 670;
	
	resetImgW = $('#test').width();
	resetImgH = $('#test').height();

	currentZoomW = resetW;
	currentZoomH = resetH;
	
	currentImgZoomW = resetImgW;
	currentImgZoomH = resetImgH;
	
	var clicked;
	var prevClicked;
	
    $('#zoomIn').click(
        function () {
        	zoomIn();
    });
    $('#zoomOut').click(
        function () {
        	zoomOut();
    });
    $('#zoomReset').click(
        function () {
        	resetZoom();
    });
    
    $('input:radio[name="readable"]').change(
    	    function(){
    	        if ($(this).is(':checked') && $(this).val() == 'yes') {
    	            $('#transcribeDiv').show();
    	            $('#selectAnOption').hide();
    	            $('html, body').animate({scrollTop:$('#transcribeDiv').position().top}, 'slow');
    	        }
    	        if ($(this).is(':checked') && $(this).val() == 'no') {
    	            $('#transcribeDiv').hide();
    	            $('#selectAnOption').hide();
    	            $('html, body').animate({scrollTop:$('#myBtn').position().top}, 'slow');
    	        }
    	    });
	});
	
</script>
<style type="text/css">
#test {
	width: 355px;
	height: 530px;
	box-shadow: 0 3px 5px #888;
}
</style>
</head>
<body>
<div class="topnav">
<a onclick="location.href='welcome.htm';">Welcome</a>
<a onclick="location.href='instructions.htm?sessionId=${sessionId}';">Instructions</a>
<a onclick="location.href='overview.htm?imageId=${imageId}&sessionId=${sessionId}';">Overview</a>
<c:url value="/system_logout.htm?sessionId=${sessionId}" var="logoutUrl" />
	<form action="${logoutUrl}" method="post" id="logoutForm">
   <a href="javascript:;" onclick="document.getElementById('logoutForm').submit();" style="float:right">Logout</a>
  <input type="hidden" name="${_csrf.parameterName}" value="${_csrf.token}" />
	</form>
</div>
<div class="parent">
<div class="wrapper">
	<div class="content" id="login-box" style="width: 480px; height: 670px;">
		<c:url var="imgUrl" value="/display_image.htm?imageId=${imageId}" />
		<img src="${imgUrl}" id="test">
		<div class="clear"></div>
		<br><br>
		<div style="text-align: center;">
			<c:url var="zoomInImgUrl" value="/resources/images/zoom-in_icon.png" />
			<img src="${zoomInImgUrl}" alt="Zoom In" id="zoomIn" title="Zoom In" height="30" width="30" hspace="15">
			<c:url var="zoomOutImgUrl" value="/resources/images/zoom-out_icon.png" />
			<img src="${zoomOutImgUrl}" alt="Zoom Out" id="zoomOut" title="Zoom Out" height="30" width="30" hspace="15">
			<c:url var="zoomResetImgUrl" value="/resources/images/zoom-reset_icon.png" />
			<img src="${zoomResetImgUrl}" alt="Zoom Reset" id="zoomReset" title="Zoom Reset" height="30" width="30" hspace="15">
		</div>
	</div>

	<div class="clear"></div>
	
	<div class="container" style="margin:0 auto; width:200px;">
		  <div class="progress">
		    <div class="progress-bar" role="progressbar" aria-valuenow="70" aria-valuemin="0" aria-valuemax="100" style="width:${progressPercent}%">
		      ${sessionImageIdsSize}/30
		    </div>
		  </div>
	   </div>
	   
	<form:form modelAttribute="taskDetails" method="post" id="taskForm">
	<input type="hidden"  name="${_csrf.parameterName}" value="${_csrf.token}"/>
		<div class="" id="readableImageDiv" >
		  <p style="display: inline;">Is the image readable? </p>
		  <form:radiobutton path="readable" value="yes" /> Yes
		  <form:radiobutton path="readable" value="no" /> No
		  <br><br>
		  <p id='selectAnOption' class="errorMsg" style="display:none;">Please select an option.</p>
		</div>
		<div id="transcribeDiv" style="display: none;height:285px;">
			<div class="center">
				<p>Please transcribe the above text below :</p>
			</div>
			<div class="clear"></div>
			
				<div class="center">
					<form:textarea path="transcribedText" id="transcribedText" style="width: 487px; height: 185px;" />
				</div>
				<br><br>
			<p id='emptyTextArea' class="errorMsg center" style="display:none;">Please enter the text above.</p>
						
		</div>
		
		<div style="text-align: center;">
			<form:input path="" class="btn" name="submit" type="submit" value="Submit" id="myBtn" onclick="return saveTranscribedText()" />
		</div>
	</form:form>
</div>
</div>
</body>
</html>