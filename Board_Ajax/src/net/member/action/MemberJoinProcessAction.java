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
					out.println("alert('ȸ�������� ���ϵ帳�ϴ�');");
					out.println("location.href='login.net';");
				}else if(result ==-1) {
					out.print("alert('���̵� �ߺ��Ǿ����ϴ�. �ٽ� �Է��ϼ���');");
					//���ΰ�ħ�Ǿ� ������ �Է��� �����Ͱ� ��Ÿ���� �ʽ��ϴ�.
					//out.printLn("location.href='join.net';");
					out.println("history.back()");//��й�ȣ�� ������ �ٸ������ʹ� �����Ǿ��ֽ��ϴ�.
				}
				out.println("</script>");
				out.close();
				return null;
				
				
		
		}
				
	
			
		}
				
	
			
				
				
				
	


