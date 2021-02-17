package net.board.action;

import java.io.IOException;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;

import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;;



public class BoardAddAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		BoardDAO boarddao = new BoardDAO();
		BoardBean boarddata = new BoardBean();
		ActionForward forward = new ActionForward();
		
		String realFolder="";
		
		//WebContent �Ʒ��� �� ������ �����ϼ���
		String saveFolder = "boardupload";
		
		int fileSize = 5*1024*1024; //���ε��� ������ �ִ�������Դϴ�. 5mb
		
		//���� �����θ� �����մϴ�.
		ServletContext sc = request.getServletContext();
		realFolder= sc.getRealPath(saveFolder);
		
		System.out.println("realFolder = "+ realFolder);
		boolean result =false;
		
		try {
			MultipartRequest multi= null;
			multi =new MultipartRequest(request,
					realFolder,
					fileSize,
					"utf-8",
					new DefaultFileRenamePolicy());
			
			  // BoardBean ��ü�� �� ��� ������ �Է� ���� �������� �����մϴ�.
	         boarddata.setBoard_name(multi.getParameter("board_name"));
	         boarddata.setBoard_pass(multi.getParameter("board_pass"));
	         boarddata.setBoard_subject(multi.getParameter("board_subject"));
	         boarddata.setBoard_content(multi.getParameter("board_content"));

	         // �ý��� �� ���ε�� ���� ���ϸ��� ��� �ɴϴ�.
	         String filename = multi.getFilesystemName("board_file");
	         boarddata.setBoard_file(filename);

	         // �� ��� ó���� ���� DAO�� boardInsert() �޼��带 ȣ���մϴ�.
	         // �� ��� ������ �Է��� ������ ����Ǿ� �ִ� boarddata ��ü�� �����մϴ�.
	         result = boarddao.boardInsert(boarddata);


			
			
			
			if(result==false) {
				System.out.println("�Խ��� ��Ͻ���");
				forward.setPath("error.error.jsp");
				request.setAttribute("message", "�Խ��� ��� �����Դϴ�.");
				forward.setRedirect(false);
				return forward;
				
			}
			System.out.println("�Խ��� ��ϿϷ�");
			
			
			//�۵���� �Ϸ�Ǹ� �� ����� �����ֱ�����  "BoardList.bo"���̵��մϴ�.
			//Redirect���θ� true�� �����մϴ�.
			forward.setRedirect(true);
			forward.setPath("BoardList.bo");//�̵��� ��θ� �����մϴ�.
			return forward;

		}catch(IOException ex) {
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "�Խ��� ���ε�����Դϴ�.");
			forward.setRedirect(false);
			return forward;
		}//catch end
		
		
		
		
		
		
	}

}
