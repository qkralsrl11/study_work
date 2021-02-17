package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.member.db.*;


public class MemberJoinProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
	
		
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
		String name = request.getParameter("name");
		int age = Integer.parseInt(request.getParameter("age"));
		String gender = request.getParameter("gender");
		String email = request.getParameter("email");


	
		

				Member member = new Member();
			member.setId(id);member.setPass(pass);member.setName(name);
			member.setAge(age);member.setEmail(email);member.setGender(gender);
				
		response.setContentType("text/html;charset=utf-8");	
		PrintWriter out = response.getWriter();
				MemberDAO mdao = new MemberDAO();
		
			
				int result=mdao.insert(member);
				out.println("<script>");
				
				if(result==1) {
					out.println("alert('회원가입을 축하드립니다');");
					out.println("location.href='login.net';");
				}else if(result ==-1) {
					out.print("alert('아이디가 중복되었습니다. 다시 입력하세요');");
					//새로고침되어 이전에 입력한 데이터가 나타다지 않습니다.
					//out.printLn("location.href='join.net';");
					out.println("history.back()");//비밀번호를 제외한 다른데이터는 유지되어있습니다.
				}
				out.println("</script>");
				out.close();
				return null;
				
				
		
		}
				
	
			
		}
				
	
			
				
				
				
	


