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

		// 파라미터로 전달받은 답변할 글 번호를 num변수에 저장합니다.
		int num = Integer.parseInt(request.getParameter("num"));

		// 글번호 num에 해당하는 내용을 가져와서 boarddata 객체에 저장합니다.
		boarddata = boarddao.getDetail(num);
		// 글내용 불러오기 실패한경우입니다.
		if (boarddata == null) {
			System.out.println("(수정)상세보기 실패");
			forward =new ActionForward();
			forward.setRedirect(false); // 주소변경없이jsp페이지의 내용을 보여줍니다.
			request.setAttribute("message", "게시판 수정 상세보기 실패입니다..");
			forward.setPath("error/error.jsp");
			return forward;
		}
		System.out.println("(수정)상세보기 성공");
		// 수정폼 페이지로 이동할 때 원본 글 내용을 보여주기 위해
		// boarddata 객체를 Request객체에 저장합니다.
		request.setAttribute("boarddata", boarddata);

		forward.setRedirect(false);
		// 글 답변 페이지 경로 지정합니다.
		forward.setPath("board/boardModify.jsp");
		return forward;
	}

}
