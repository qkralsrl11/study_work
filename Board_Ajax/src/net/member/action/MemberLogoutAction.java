package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

public class MemberLogoutAction implements Action {

	
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		HttpSession session = request.getSession();
		session.invalidate();
	//"id"의 속성을 제거합니다.

			PrintWriter out =response.getWriter();
			out.println("<script>");
			out.println("alert('Success Logout');");
			out.println("location.href='login.net';");
			out.println("</script>");
			
			
			
			
			
		
		return null;
	}

}