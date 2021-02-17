package net.comment.action;

import java.io.IOException;
import java.io.PrintWriter;

import net.board.action.*;
import net.comment.db.CommentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommentDelete implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		int num = Integer.parseInt(request.getParameter("num"));
		
	
	
		PrintWriter out = response.getWriter();
		CommentDAO cdao = new CommentDAO();

		int result = cdao.commDelete(num);
		

		out.print(result);

		return null;
		
		
	}

}
