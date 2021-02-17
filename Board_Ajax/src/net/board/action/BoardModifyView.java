package net.board.action;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardModifyView implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		ActionForward forward = new ActionForward();
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();

		// �Ķ���ͷ� ���޹��� �亯�� �� ��ȣ�� num������ �����մϴ�.
		int num = Integer.parseInt(request.getParameter("num"));

		// �۹�ȣ num�� �ش��ϴ� ������ �����ͼ� boarddata ��ü�� �����մϴ�.
		boarddata = boarddao.getDetail(num);
		// �۳��� �ҷ����� �����Ѱ���Դϴ�.
		if (boarddata == null) {
			System.out.println("(����)�󼼺��� ����");
			forward =new ActionForward();
			forward.setRedirect(false); // �ּҺ������jsp�������� ������ �����ݴϴ�.
			request.setAttribute("message", "�Խ��� ���� �󼼺��� �����Դϴ�..");
			forward.setPath("error/error.jsp");
			return forward;
		}
		System.out.println("(����)�󼼺��� ����");
		// ������ �������� �̵��� �� ���� �� ������ �����ֱ� ����
		// boarddata ��ü�� Request��ü�� �����մϴ�.
		request.setAttribute("boarddata", boarddata);

		forward.setRedirect(false);
		// �� �亯 ������ ��� �����մϴ�.
		forward.setPath("board/boardModify.jsp");
		return forward;
	}

}
