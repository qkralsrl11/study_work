<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>
<!DOCTYPE html>
<html>
<head>

<title>Insert title here</title>
<script src="<%=request.getContextPath()%>/js/jquery-3.5.1.js"></script>
<script src="http://dmaps.daum.net/map_js_init/postcode.v2.js"></script>
<link href="<%=request.getContextPath()%>/css/join.css" type="text/css" rel="stylesheet">

<jsp:include page="../board/header.jsp" />
<script>

</script>
<style>
h3{
	text-align:center; 
}


input[type=file]{
display:none;
}
</style>
</head>
<body>
	<form  action="updateProcess.net" name ="joinform" method ="post" enctype="multipart/form-data">
		<h1>회원 정보 수정</h1>
		<hr>
		
		<b>아이디</b>
			<input type=text name=id required maxLength ="12" value="${id}" readonly> 
			<span id = "message"></span>
		
		<b>비밀번호</b>	
			<input type=password  name=pass   value="${memberinfo.pass}" required> 
			
			
		<b>이름</b>	
		<input type="text" name ="name" placeholder="Enter name" maxLength =15 value="${memberinfo.name}" required>
		
		<b>나이</b>	
			<input type="text" name ="age" placeholder="Enter age" maxLength =2 value="${memberinfo.age}" required>
			
		<b>성별</b>
		<div>
			<input type=radio name=gender value="남" ><span>남자</span> 
			<input type=radio name=gender value="여" ><span>여자</span> 
		</div>
			
		<b>이메일 주소</b>	
			 <input type=text name=email placeholder="Enter Email" value="${memberinfo.email}" required> 
			 <span id = "email_message"></span>
			 
		<b>프로필 사진</b>
		<label>
			<img src ="image/attach.png" width="10px">
			<span id="filename">${memberinfo.memberfile}</span>
			<span id ="showImage">
			 <c:if test='${empty memberinfo.memberfile}'>
			 	<c:set var='src' value='image/profile.png'/>
			 </c:if>
			 
			  <c:if test='${!empty memberinfo.memberfile}'>
			 	<c:set var='src' value='${"memberupload/"}${memberinfo.memberfile}'/>
			 </c:if>
			 <img src ="${src}" width="20px" alt="profile">
			 </span>
			 <input type="file" name="memberfile" accept="image/*">
		</label>
			<div class="clearfix">
			<button type="submit" class ="submitbtn">수정</button>
			<button type='reset' class ="cancelbtn">취소</button>
			
			</div>


	</form>
	<script>
	$("input[value='${memberinfo.gender}']").prop('checked',true);
	
	$(".cancelbtn").click(function(){
		history.back();
	})
	//처음 화면 로드시 보여줄 이메일은 이미 체크 완료된것이므로 기본 checkemail= true 입니다.
	var checkemail=true;
	$("input:eq(6)").on('keyup',
			function(){
		$("#email_message").empty();
		//[A-Za-z0-9_]와 통일한 것이 \w
		//+는 1회이상 반복을 의미합니다..{1, }와 동일합니다.
		//\w+는 [A-Za-z0-9_]을 1개이상 사용하라는 의미입니다.
		var pattern = /^\w+@\w+[.]\w{3}$/;
		var email = $("input:eq(6)").val();
		if (!pattern.test(email)) {
			$("#email_message").css('color', 'red').html("이메일 형식이 맞지않습니다.")
			checkemail = false;
		} else {
			$("#email_message").css('color', 'green').html("이메일 형식에 맞습니다.");
			checkemail = true;

		}
	}) // email keyup 이벤트 처리 끝
		
	
	$('input[type=file]').change(function(event){
		var inputfile =$(this).val().split('\\');
		var filename=inputfile[inputfile.length -1];
		var pattern = /(gif|jpg|jpeg|png)$/i;
		if(pattern.test(filename)){
			$('#filename').text(filename);//inputfile.length -1 =2
			
			var reader =new FileReader();//파일을 일기위한 객체생성
			//DataURL형식으로 파일을 읽어옵니다.
			//읽어온 결과는reader객체의result속성에 저장됩니다.
			//event.target.files[0] :선택한 그림의 파일객체에서 첫번째 객체를 가져옵니다.
		reader.readAsDataURL(event.target.files[0])
		reader.onload = function(event){//읽기에 성공했을때 실행되는 이벤트 핸들러
				$("#showImage").html('<img width="20px" src="' + event.target.result + '">');
		
				
			};
		}else{
			alser('확장자는 gif, jpg, jpeg, png 가능합니다.');
		}
	
	
	})
	
	

	
	
	</script>


</body>
</html>