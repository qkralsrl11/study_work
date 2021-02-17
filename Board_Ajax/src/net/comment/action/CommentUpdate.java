package net.comment.action;

import java.io.IOException;


import net.board.action.*;
import net.comment.db.CommentBean;
import net.comment.db.CommentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;



public class CommentUpdate implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		CommentBean comm = new CommentBean();
		int num = Integer.parseInt(request.getParameter("num"));
		String content = request.getParameter("content");


		System.out.println("content = " + comm.getContent());

		comm.setNum(num);
		comm.setContent(content);
	

		CommentDAO cdao = new CommentDAO();

		int ok = cdao.update(comm);
		response.getWriter().print(ok);

		return null;
	}

}
