package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberDeleteAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		MemberDAO mdao = new MemberDAO();		
		String id = request.getParameter("id");
		response.setContentType("text/html;charset=utf-8");
		PrintWriter out = response.getWriter();
		int result = mdao.delete(id);
		
		
		if(result == 1 ) {
			out.println("<script>");
			out.println("alert('�����Ǿ����ϴ�.');");
			out.println("location.href='memberList.net';");
			out.println("</script>");
		}else {
			out.println("<script>");
			out.println("alert('���������Դϴ�..');");
			out.println("history.back();");
			out.println("</script>");
		}
		
		out.close();
		return null;
		
	}
}
