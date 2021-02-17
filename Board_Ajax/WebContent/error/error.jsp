<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<!DOCTYPE html>
<html>
<head>

<title>Insert title here</title>
<style>
body{
width:500px; margin:3em auto
}
div{
color:orange;
font-size:30px;
}

span{font-size:1.5em; color:#5d5de2}

</style>

</head>
<body>
<img src = "<%=request.getContextPath()%>/image/error.png" width="100px"><br>
<div>죄송합니다
${message}</div> <br>
<span>서비스 이용에 불편을 드려 죄송합니다.</span>






</body>
</html>