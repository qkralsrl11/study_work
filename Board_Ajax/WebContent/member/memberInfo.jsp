<%@ page language="java" contentType="text/html; charset=utf-8"
	pageEncoding="utf-8"%>
<jsp:include page="../board/header.jsp" />
<html>
<head>
<meta charset="utf-8">
<title>Insert title here</title>
<style>
tr >td:nth-child(odd){font-weight: bold}
td {text-align:center}
th {text-align:center}
.container{width:50%}

</style>

</head>
<body>
	<div class="container">
	
			
		<table class="table table-bordered">
			<tr>
				<th>아이디</th>
				<td>${memberinfo.id}</td>
			</tr>

			<tr>
				<th>비밀번호</th>
				<td>${memberinfo.pass}</td>
			</tr>


			<tr>
				<th>이름</th>
				<td>${memberinfo.name}</td>
			</tr>


			<tr>
				<th>나이</th>
				<td>${memberinfo.age}</td>
			</tr>


			<tr>
				<th>성별</th>
				<td>${memberinfo.gender}</td>
			</tr>

			<tr>
				<th>이메일주소</th>
				<td>${memberinfo.email}</td>
			</tr>
			
			<tr>
			
			<td colspan=2><a href="memberList.net">리스트로돌아가기</a></td>
		</tr>
		</table>
		</div>


</body>
</html>