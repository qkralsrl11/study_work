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
		
		//WebContent 아래에 꼭 폴더를 생성하세요
		String saveFolder = "boardupload";
		
		int fileSize = 5*1024*1024; //업로드할 파일의 최대사이즈입니다. 5mb
		
		//실제 저장경로를 지정합니다.
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
			
			  // BoardBean 개체에 글 등록 폼에서 입력 받은 정보들을 저장합니다.
	         boarddata.setBoard_name(multi.getParameter("board_name"));
	         boarddata.setBoard_pass(multi.getParameter("board_pass"));
	         boarddata.setBoard_subject(multi.getParameter("board_subject"));
	         boarddata.setBoard_content(multi.getParameter("board_content"));

	         // 시스템 상에 업로드된 실제 파일명을 얻어 옵니다.
	         String filename = multi.getFilesystemName("board_file");
	         boarddata.setBoard_file(filename);

	         // 글 등록 처리를 위해 DAO의 boardInsert() 메서드를 호출합니다.
	         // 글 등록 폼에서 입력한 정보가 저장되어 있는 boarddata 개체를 전달합니다.
	         result = boarddao.boardInsert(boarddata);


			
			
			
			if(result==false) {
				System.out.println("게시판 등록실패");
				forward.setPath("error.error.jsp");
				request.setAttribute("message", "게시판 등록 실패입니다.");
				forward.setRedirect(false);
				return forward;
				
			}
			System.out.println("게시판 등록완료");
			
			
			//글등록이 완료되면 글 목록을 보여주기위해  "BoardList.bo"로이동합니다.
			//Redirect여부를 true로 설정합니다.
			forward.setRedirect(true);
			forward.setPath("BoardList.bo");//이동할 경로를 지정합니다.
			return forward;

		}catch(IOException ex) {
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "게시판 업로드실패입니다.");
			forward.setRedirect(false);
			return forward;
		}//catch end
		
		
		
		
		
		
	}

}
