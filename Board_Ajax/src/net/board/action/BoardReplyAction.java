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
	 

	         //�亯�� DB�� �� �����ϱ� ���� baorddata��ü�� �Ķ���ͷ� �����ϰ�
	         //DAO�� �޼��� boardReply�� ȣ���մϴ�.
	         
	         result = boarddao.boardReply(boarddata);
	         
	         

			if(result==0) {
				System.out.println("�亯 ���� ����");
				forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "�亯 ���� �����Դϴ�.");
				forward.setPath("error.error.jsp");
				return forward;
				
			}
			
			//�亯 ������ ����� �Ȱ��
			System.out.println("�亯 ���� �Ϸ�");
			
			
			
			forward.setRedirect(true);
			forward.setPath("BoardDetailAction.bo?num="+result);//�̵��� ��θ� �����մϴ�.
			return forward;

	
		
		
		
		
		
		
	}

}

