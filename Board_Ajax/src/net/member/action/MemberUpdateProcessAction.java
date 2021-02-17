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

		// WebContent �Ʒ��� �� ������ �����ϼ���
		String saveFolder = "memberupload";

		int fileSize = 5 * 1024 * 1024; // ���ε��� ������ �ִ�������Դϴ�. 5mb

		// ���� �����θ� �����մϴ�.
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

			// �ý��� �� ���ε�� ���� ���ϸ��� ��� �ɴϴ�.
			String memberfile = multi.getFilesystemName("memberfile");
			m.setMemberfile(memberfile);

			// �� ��� ó���� ���� DAO�� boardInsert() �޼��带 ȣ���մϴ�.
			// �� ��� ������ �Է��� ������ ����Ǿ� �ִ� boarddata ��ü�� �����մϴ�.

			response.setContentType("text/html;charset=utf-8");
			PrintWriter out = response.getWriter();

			MemberDAO mdao = new MemberDAO();
			int result = mdao.update(m);
			out.println("<script>");

			if (result == 1) {
				out.println("alert('�����Ǿ����ϴ�');");
				out.println("location.href='BoardList.bo';");
			} else {
				out.print("alert('ȸ������ ������ �����߽��ϴ�.');");

				out.println("history.back()");// ��й�ȣ�� ������ �ٸ������ʹ� �����Ǿ��ֽ��ϴ�.
			}
			out.println("</script>");
			out.close();
			return null;

		} catch (IOException ex) {
			ActionForward forward = new ActionForward();
			forward.setPath("error/error.jsp");
			request.setAttribute("message", "������ ���ε�����Դϴ�.");
			forward.setRedirect(false);
			return forward;
		} // catch end

	}

}
