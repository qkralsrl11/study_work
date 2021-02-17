package net.member.action;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.oreilly.servlet.MultipartRequest;
import com.oreilly.servlet.multipart.DefaultFileRenamePolicy;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberUpdateProcessAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		String realFolder = "";

		// WebContent 아래에 꼭 폴더를 생성하세요
		String saveFolder = "memberupload";

		int fileSize = 5 * 1024 * 1024; // 업로드할 파일의 최대사이즈입니다. 5mb

		// 실제 저장경로를 지정합니다.
		ServletContext sc = request.getServletContext();
		realFolder = sc.getRealPath(saveFolder);

		System.out.println("realFolder = " + realFolder);

		try {
			MultipartRequest multi = null;
			multi = new MultipartRequest(request, realFolder, fileSize, "utf-8", new DefaultFileRenamePolicy());

			String id = multi.getParameter("id");
			String pass = multi.getParameter("pass");
			String name = multi.getParameter("name");
			int age = Integer.parseInt(multi.getParameter("age"));
			String gender = multi.getParameter("gender");
			String email = multi.getParameter("email");

			Member m = new Member();
			m.setId(id);
			m.setEmail(email);
			m.setGender(gender);
			m.setName(name);
			m.setPass(pass);
			m.setAge(age);

			// 시스템 상에 업로드된 실제 파일명을 얻어 옵니다.
			String memberfile = multi.getFilesystemName("memberfile");
			m.setMemberfile(memberfile);

			// 글 등록 처리를 위해 DAO의 boardInsert() 메서드를 호출합니다.
			// 글 등록 폼에서 입력한 정보가 저장되어 있는 boarddata 개체를 전달합니다.

			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();

			MemberDAO mdao = new MemberDAO();
			int result = mdao.update(m);
			out.println("<script>");

			if (result == 1) {
				out.println("alert('수정되었습니다');");
				out.println("location.href='BoardList.bo';");
			} else {
				out.print("alert('회원정보 수정에 실패했습니다.');");

				out.println("history.back()");// 비밀번호를 제외한 다른데이터는 유지되어있습니다.
			}
			out.println("</script>");
			out.close();
			return null;

		} catch (IOException ex) {
			ActionForward forward = new ActionForward();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "프로필 업로드실패입니다.");
			forward.setRedirect(false);
			return forward;
		} // catch end

	}

}
