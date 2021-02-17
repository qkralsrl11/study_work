package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;


import javax.servlet.ServletException;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import net.member.db.MemberDAO;



public class MemberLoginProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		String id = request.getParameter("id");
		String pass = request.getParameter("pass");
				MemberDAO dao= new MemberDAO();
		int result = dao.isId(id, pass);
		System.out.println("����� " + result);
		
		//�α��μ���
		if (result == 1) {
			HttpSession session =request.getSession();
			session.setAttribute("id", id);
			
			String  IDStore = request.getParameter("remember");
			Cookie cookie = new Cookie("id",id);
			
			//ID�� ����ϱ⸦ üũ�Ѱ��
			if(IDStore != null && IDStore.equals("store")) {
				//cookir.setMaxAge(60*60*24);
				cookie.setMaxAge(2*60);
				//Ŭ���̾�Ʈ�� ���� �����մϴ�
				response.addCookie(cookie);
			}else {
				cookie.setMaxAge(0);
				response.addCookie(cookie);
				
			}
			
			forward.setRedirect(true);
	
			forward.setPath("BoardList.bo");
			return forward;
			

		
		}else {
			String message = "��й�ȣ�� ��ġ���� �ʽ��ϴ�.";
			if(result==-1)
				message= "���̵� �������� �ʽ��ϴ�.";
			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();
			out.println("<script>");
			out.println("alert('"+message+"');");
			out.println("location.href= 'login.net';");
			out.println("</script>");
			out.close();
			return null;
			
		}
	
		
		
		
	 
	   }

 
   
}


