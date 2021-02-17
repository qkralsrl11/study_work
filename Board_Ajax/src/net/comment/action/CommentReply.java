package net.comment.action;

import java.io.IOException;
import net.board.action.*;
import net.comment.db.CommentBean;
import net.comment.db.CommentDAO;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class CommentReply implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		
		CommentBean comm =new CommentBean();
	
		String id = request.getParameter("id");
		String content = request.getParameter("content");
		int comment_board_num =Integer.parseInt(request.getParameter("comment_board_num"));
		int comment_re_lev = Integer.parseInt(request.getParameter("comment_re_lev"));
		int comment_re_seq = Integer.parseInt(request.getParameter("comment_re_seq"));
		int comment_re_ref = Integer.parseInt(request.getParameter("comment_re_ref"));
	
		System.out.println("content = " +comm.getContent());

			
	
		comm.setId(id); comm.setContent(content);comm.setComment_board_num(comment_board_num);
		comm.setComment_re_lev(comment_re_lev); comm.setComment_re_seq(comment_re_seq);
		comm.setComment_re_ref(comment_re_ref);
		
		
		

				CommentDAO cdao =new CommentDAO();
				
			
				int ok =cdao.Reply(comm);
				response.getWriter().print(ok);

		
		
		return null;
	}
	}


