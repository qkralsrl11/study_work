package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardReplyView implements Action {

	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		
		//�Ķ���ͷ� ���޹��� �亯�� �� ��ȣ�� num������ �����մϴ�.
		int num =Integer.parseInt(request.getParameter("num"));
		
		//�۹�ȣ num�� �ش��ϴ� ������ �����ͼ� boarddata ��ü�� �����մϴ�.
		boarddata=boarddao.getDetail(num);
		
		//�۳����� ���� ���
		if(boarddata==null) {
			System.out.println("���� �������� �ʽ��ϴ�.");
			forward.setRedirect(false); // �ּҺ������jsp�������� ������ �����ݴϴ�.
			request.setAttribute("message", "���� �������� �ʽ��ϴ�.");
			forward.setPath("error/error.jsp");
			return forward;
		}
		System.out.println("�亯������ �̵��Ϸ�");
		//�亯�� �������� �̵��� �� ���� �� ������ �����ֱ� ����
		//boarddata ��ü�� Request��ü�� �����մϴ�.
		request.setAttribute("boarddata", boarddata);
		
		forward.setRedirect(false);
		//�� �亯 ������ ��� �����մϴ�.
		forward.setPath("board/boardReply.jsp");
		return forward;
		
		
		
		
		
		
		

	}

}
