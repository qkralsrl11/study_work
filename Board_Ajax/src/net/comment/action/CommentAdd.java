package net.comment.action;

import java.io.IOException;


import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import net.board.action.*;
import net.comment.db.CommentBean;
import net.comment.db.CommentDAO;



public class CommentAdd implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
	
		
		CommentBean comm =new CommentBean();
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		int comment_board_num =Integer.parseInt(request.getParameter("comment_board_num"));
		int comment_re_lev = Integer.parseInt(request.getParameter("comment_re_lev"));
		int comment_re_seq = Integer.parseInt(request.getParameter("comment_re_seq"));

	
		System.out.println("content = " +comm.getContent());

			
				
		comm.setId(id); comm.setContent(content);comm.setComment_board_num(comment_board_num);
		comm.setComment_re_lev(comment_re_lev); comm.setComment_re_seq(comment_re_seq);
		
		
		

				CommentDAO cdao =new CommentDAO();
				
			
				int ok =cdao.insert(comm);
				response.getWriter().print(ok);

		
		
		return null;
	}

}
