<%@ page language="java" contentType="text/html; charset=UTF-8"%>
<%@ taglib prefix="c" uri="http://java.sun.com/jsp/jstl/core"%>

<html>
<head>
<jsp:include page="../board/header.jsp" />
<style>
table caption {
	caption-side: top;
	text-align: center
}

li .current {
	background: #faf7f7;
	color: gray;
}

body>div>table>tbody>tr>td:last-child>a {
	color: red
}

form {
	margin: 0 auto;
	width: 80%;
	text-align: center
}

select {
	color: #495057;
	background-color: #fff;
	background-clip: padding-box;
	border: 1px solid #cdf4da;
	border-radius: .25rem;
	transition: boarder-color .15s ease-in-out, box-shadow .15s ease-in-out;
	outline: none;
}

.container {
	width: 60%
}

td:nth-child(1) {
	width: 33%
}

.input-group {
	margin-bottom: 3em
}
</style>

<%--
1.검색어를 입력한우 다시memberList.net으로 온경우 검ㅁ색필드와 검색어가 나타나도록합니다.

2.검색 필드를 변경하면 검색어 입력창에 placeholder 나타나도록합니다.
예로 아이디를 선택하면 placeholder로 아이디 입력하세요 라고 나타납니다.
예로 이름를 선택하면 placeholder로 이름을 입력하세요 라고 나타납니다.
예로 나이를 선택하면 placeholder로 나이를 입력하세요 라고 나타납니다.
예로 성별를 선택하면 placeholder로 성별을 입력하세요 라고 나타납니다. 

3.검색 버튼 클릭시 다음을 체크합니다.
1) 검색어를 입력하지 않은 경우 "검색어를 입력하세요" 다고 대화상자가 나타나게합니다.
2) 나이는 두자리 숫자가 아닌 경우 "나이는 형식에 맞게 입력하세요(두자리숫자)"라고 대화상자가 나타나게합니다.
3) 성별은 "남" 또는 "여"가 아닌 경우 "남 또는 여를 입력하세요" 라고 대화상자가 나타나게 합니다.

4. 회원목록의 삭제를 클릭한경우
confirm ("정말 살제 하시겠습니다?")를 실행해서 취소를 클릭하면 "memberDelete.net으로 이동하지 않습니다.--%>
<script>
$(function(){

	
	
	//검색 클릭후 응답화면에는 검색시 선택한 필드가 선택되도록 합니다.
	var selectedValue = '${search_field}'
		if(selectedValue != '-1')
			$("#viewcount").val(selectedValue);
	
	//검색 버튼 클릭한 경우
	$("button").click(function(){
		//검색어 공백 유효성 검사합니다.
		if($("input").val()==''){
			alert("검색어를 입력하세요");
			$("input").focus();
			return false;
		}
		word=$(".input-group input").val();
		if(selectedValue==2){
			pattern = /^[0-9]{2}$/;
			if(!pattern.test(word)){
				alert("나이는 형식에 맞게 입력하세여 (두자리 숫자)");
				return false;
			}
		}else if(selectedValue==3){
			if(word !="남" && word !="여"){
				alert("남 또는 여를 입력하세요");
				return false;
			}
		}
	});

	//검색어 입력창에 palceholder 나타나도록합니다.
	
	$("#viewcount").change(function(){
		selectedValue = $(this).val();
		$("input").val();
		message =["아이디", "이름", "나이" ,"남또는여"]
		$("input").attr("placeholder" ,message[selectedValue] + "입력하세요");
		
		
	})// $("#viewcount").change end
	
	//회원 목록의 삭제를 클릭한경우
	
	$("tr > tr:nth-child(3) > a ").click(function(event){
		var answer = confirm("정말 삭제 하시겠습니까?");
		console.log(answer);//취소를 클릭한 경우-false
		if(!answer){//취소를 클릭한경우
				event.preventDefault();//이동하지 않습니다.
		}
	
	}) //삭제 클릭end
	
	
	
	
	
	
	
	
	
	
})




</script>

<title>MVC 게시판</title>
</head>
<body>
	<div class="container">
		<form action="memberList.net" method="post">
			<div class="input-group">


				<select id="viewcount" name="search_field">
					<option value="0" selected>아이디</option>
					<option value="1">이름</option>
					<option value="2">나이</option>
					<option value="3">성별</option>
				</select> <input type="text" name="search_word" class="form-control"
					placeholder="아이디를 입력하세요" value="${search_word}">
				<button type="submit" class="btn btn-info">검색</button>

			</div>
		</form>
		<%--게시글이 있는경우 --%>
		<c:if test="${listcount > 0 }">

			<h3>회원목록</h3>
			<table class="table talbe_striped">
				<thead>
					<tr>
						<th colspan="2">MVC 게시판 -회원정보 list</th>
						<th colspan="2"><font size=3>회원수 : ${listcount}</font></th>
					</tr>
					<tr>
						<th><div>아이디</div></th>
						<th><div>이름</div></th>
						<th><div>삭제</div></th>

					</tr>
				</thead>
				<tbody>
					<c:forEach var="m" items="${totallist }">
						<tr>
							<td><a href="memberInfo.net?id=${m.id }">${m.id }</a></td>
							<td>${m.name}</td>
							<td><a href="memberDelete.net?id=${m.id }">삭제</a></td>
						</tr>
					</c:forEach>





				</tbody>
			</table>


			<div class="center-block">
				<ul class="pagination justify-content-center">
					<c:if test="${page <=1 }">
						<li class="page-item"><a class="page-link gray">이전 &nbsp;</a>
						</li>
					</c:if>
					<c:if test="${page>1 }">
						<li class="page-item"><a
							href="memberList.net?page=${page-1 }&search_field=${search_field}&search_word=${search_word}"
							class="page-link">이전&nbsp;</a></li>
					</c:if>
					<c:forEach var="a" begin="${startpage}" end="${endpage }">
						<c:if test="${a==page }">
							<li class="page-item"><a class="page-link current" href="#">${a}</a></li>
						</c:if>
						<c:if test="${a !=page }">
							<li class="page-item"><a
								href="memberList.net?page=${a}&search_field=${search_field}&search_word=${search_word}"
								class="page-link">${a}</a></li>
						</c:if>
					</c:forEach>
					<c:if test="${page>= maxpage }">
						<li class="page-item"><a class="page-link gray">&nbsp;다음</a>
						</li>
					</c:if>
					<c:if test="${page < maxpage }">
						<li class="page-item"><a
							href="memberList.net?page=${page+1}&search_field=${search_field}&search_word=${search_word}"
							class="page-link">&nbsp;다음</a></li>
					</c:if>

				</ul>

			</div>

		</c:if>
		<%-- <c:if test ="${listcount > 0 }">--%>


		<%--회원이 없는경우 --%>
		<c:if test="${listcount==0 && empty search_word}">
			<font size=5>회원이 없습니다.</font>
		</c:if>

		<c:if test="${listcount==0 && !empty search_word}">
			<font size=5>검색결과가 없습니다.</font>
		</c:if>


	</div>

</body>
</html>