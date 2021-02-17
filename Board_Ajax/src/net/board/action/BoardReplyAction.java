package net.board.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardReplyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();
		int result =0;

	         boarddata.setBoard_name(request.getParameter("board_name"));
	         boarddata.setBoard_pass(request.getParameter("board_pass"));
	         boarddata.setBoard_subject(request.getParameter("board_subject"));
	         boarddata.setBoard_content(request.getParameter("board_content"));
	         boarddata.setBoard_re_ref(Integer.parseInt(request.getParameter("board_re_ref")));
	         boarddata.setBoard_re_lev(Integer.parseInt(request.getParameter("board_re_lev")));
	         boarddata.setBoard_re_seq(Integer.parseInt(request.getParameter("board_re_seq")));
	 

	         //답변을 DB에 에 저장하기 위한 baorddata객체를 파라미터로 전달하고
	         //DAO의 메서드 boardReply를 호출합니다.
	         
	         result = boarddao.boardReply(boarddata);
	         
	         

			if(result==0) {
				System.out.println("답변 저장 실패");
				forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "답변 저장 실패입니다.");
				forward.setPath("error.error.jsp");
				return forward;
				
			}
			
			//답변 저장이 제대로 된경우
			System.out.println("답변 저장 완료");
			
			
			
			forward.setRedirect(true);
			forward.setPath("BoardDetailAction.bo?num="+result);//이동할 경로를 지정합니다.
			return forward;

	
		
		
		
		
		
		
	}

}

