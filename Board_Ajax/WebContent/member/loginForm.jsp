<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<title>Session</title>
<link href="<%=request.getContextPath()%>/css/login.css" type="text/css" rel="stylesheet">
<link rel="stylesheet"
	href="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/css/bootstrap.min.css">
<script
	src="https://ajax.googleapis.com/ajax/libs/jquery/3.5.1/jquery.min.js"></script>
<script
	src="https://cdnjs.cloudflare.com/ajax/libs/popper.js/1.16.0/umd/popper.min.js"></script>
<script
	src="https://maxcdn.bootstrapcdn.com/bootstrap/4.5.2/js/bootstrap.min.js"></script>

</head>
<script>

	$(function(){
		
		$(".join").click(function(){
			location.href="join.net"
		});
		
		
		
		
		
		var id = '${id}';
		if(id){
			$('#id').val(id);
			$("remember").prop("checked",true);
		}
		
					
		})
		
		
		
		
	
	
	
	</script>
<body>
	<form name= "loginform" action='loginProcess.net' method="post">
	<h1>로그인</h1>
		<hr>
		<b>아이디</b> 
		<input type="text" name="id" placeholder="Enter id" id="id" required> 
			<b>Password</b>
			 <input type="text" name="pass" placeholder="Enter password" required>
			 <input type ="checkbox" id="remember" name ="remember" value="store">
			 <span>remember</span>
		<div class="clearfix">
			<button type="submit" class="submitbtn"> 로그인</button>
			<button type="button" class="join">회원가입</button>
		</div>
	</form>
</body>
</html>