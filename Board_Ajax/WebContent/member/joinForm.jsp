<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<title>Insert title here</title>
<script src="<%=request.getContextPath()%>/js/jquery-3.5.1.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<link href="<%=request.getContextPath()%>/css/join.css" type="text/css" rel="stylesheet">
<script src="<%=request.getContextPath()%>/js/join.js"></script>
<script>






</script>
</head>
<body>
	<form  action="joinProcess.net" name ="joinform" method ="post">
		<h1>회원가입</h1>
		<hr>
		
		<b>아이디</b>
			<input type=text name=id  placeholder="Enter ID" required maxLength ="12"> 
			<span id = "message"></span>
		
		<b>비밀번호</b>	
			<input type=password  name=pass  placeholder="Enter Password" required> 
			
			
		<b>이름</b>	
		<input type="text" name ="name" placeholder="Enter name" maxLength =15 required>
		
		<b>나이</b>	
			<input type="text" name ="age" placeholder="Enter age" maxLength =2 required>
			
		<b>성별</b>
		<div>
			<input type=radio name=gender value="남" checked><span>남자</span> 
			<input type=radio name=gender value="여" ><span>여자</span> 
		</div>
			
		<b>이메일 주소</b>	
			 <input type=text name=email placeholder="Enter Email" required> 
			 <span id = "email_message"></span>
			 

			<div class="clearfix">
			<button type="submit" class ="submitbtn">회원가입</button>
			<button type='reset' class ="cancelbtn">다시작성</button>
			
			</div>


	</form>


</body>
</html>