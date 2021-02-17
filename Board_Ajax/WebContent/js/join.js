$(document).ready(function() {

	var checkid = false;
	var checkemail = false;
	$("form").submit(function() {
		if (!$.isNumeric($("input[name ='age']").val())) {

			alert("나이는 숫자로 입력하세요")
			$("input[name ='age']").val(' ').focus();
			return false;

		}

		if (!checkid) {
			alert("사용가능한 id로 입력하세요.")
			$("input:eq(0)").val('').focus();
			return false;
		}

		if (!checkemail) {
			alert("email형식을 확인하세요.")
			$("input:eq(6)").val('').focus();
			return false;
		}

	})// submit

	$("input:eq(6)").on('keyup', function() {
		$("#email_message").empty();
		// [A-Za-z0-9_]와 동일한것이 \w
		// +는 1회이상 반복을 의미합니다. {1,}와 동일합니다.
		// \w+는 [A-Za-z0-9_]를 1개이상 사용하라는 의미입니다.
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

	$("input:eq(0)").on('keyup', function() {
		checkid = true;
		$("#message").empty();// 처음에 pattern에 적합하지 않은 경우 메시지 출력후 적합한테이터
		// [A-Za-z0-9_]의 의미가 \w로 표현
		var pattern = /^\w{5,12}$/;
		var id = $("input:eq(0)").val();
		if (!pattern.test(id)) {
			$("#message").css('color', 'red').html("영문자 숫자_로 5_12자 가능합니다.")
			checkid = false;
			return;
		}
		$.ajax({
			url :"idcheck.net",
			data :{"id" : id},
			success :function(resp){
				if(resp ==-1){//db에 해당 id가 없는경우
					$("#message").css('color', 'green').html('사용 가능한 아이디 입니다.');
					checkid=true;
					
				}else{//db에 해당 id가 있는경우
					$("#message").css('color', 'red').html('사용 중인 아이디입니다.');
					checkid=false;
				}
			}
		})
		
		
		
		
		

	}) // id keyup end

}) // ready
