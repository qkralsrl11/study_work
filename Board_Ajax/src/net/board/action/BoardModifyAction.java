package net.board.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardModifyAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();

		String realFolder = "";

		// WebContent �Ʒ��� �� ������ �����ϼ���
		String saveFolder = "boardupload";

		int fileSize = 5 * 1024 * 1024; // ���ε��� ������ �ִ�������Դϴ�. 5mb

		// ���� �����θ� �����մϴ�.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);

		System.out.println("realFolder = " + realFolder);
		boolean result = false;

		try {
			MultipartRequest multi = null;
			multi = new MultipartRequest(request, realFolder, fileSize, "utf-8", new DefaultFileRenamePolicy());

			int num = Integer.parseInt(multi.getParameter("board_num"));
			String pass = multi.getParameter("board_pass");

			boolean usercheck = boarddao.isBoardWriter(num, pass);

			// ��й�ȣ�� �ٸ����
			if (usercheck == false) {
				response.setContentType("text/html;charset=utf-8");
				PrintWriter out = response.getWriter();
				out.println("<script>");
				out.println("alert('비밀번호가다릅니다.');");
				out.println("history.back();");
				out.println("</script>");
				out.close();
				return null;

			}

			// ��й�ȣ�� ��ġ���ϴ� ��� ���������� �����մϴ�.
			// BoardBean ��ü�� �� ��� ������ �Է¹��� �������� �����մϴ�.

			boarddata.setBoard_num(num);
			boarddata.setBoard_subject(multi.getParameter("board_subject"));
			boarddata.setBoard_content(multi.getParameter("board_content"));

			String check = multi.getParameter("check");
			System.out.println("check =" + check);
			if (check != null) {
				boarddata.setBoard_file(check);

			} else {
				// ���ε� �� ������ �ý��� �� ���ε�� ���� ���ϸ��� ���ɴϴ�.
				String filename = multi.getFilesystemName("board_file");
				boarddata.setBoard_file(filename);

			}
			// Dao���� ���� �޼��� ȣ���Ͽ� �����մϴ�.
			result = boarddao.boardModify(boarddata);

			// �ý��� �� ���ε�� ���� ���ϸ��� ��� �ɴϴ�.
			String filename = multi.getFilesystemName("board_file");
			boarddata.setBoard_file(filename);

		

			// ������ �����Ұ��
			if (result == false) {
				System.out.println("수정실패");
				forward = new ActionForward();
				forward.setRedirect(false);
				request.setAttribute("message", "수정실패입니다.");
				forward.setPath("error/error.jsp");
				return forward;
			}
			// ���� ������ ���
			System.out.println("수정성공");

			forward.setRedirect(true);
			// ������ �� ������ �����ֱ� ���� �� ���� ���� ���� �������� �̵��ϱ� ���� ��θ� �����մϴ�.
			forward.setPath("BoardDetailAction.bo?num=" + boarddata.getBoard_num());
			return forward;

		} catch (IOException ex) {
			ex.printStackTrace();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "수정 업로드 실패입니다.");
			forward.setRedirect(false);
			return forward;
		} // catch end

	}

}
