package net.board.action;

import java.io.IOException;

import javax.servlet.RequestDispatcher;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.comment.action.CommentAdd;
import net.comment.action.CommentDelete;
import net.comment.action.CommentList;
import net.comment.action.CommentReply;
import net.comment.action.CommentUpdate;


@WebServlet("*.bo")
public class BoardFrontController extends javax.servlet.http.HttpServlet {
	private static final long serialVersionUID = 1L;

	private void doProcess(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {

		/*
		 * ��û�� ��üURL�߿��� ��Ʈ ��ȣ �������� ������ ���ڿ����� ��ȯ�˴ϴ�. ��)
		 * http://localhost:8088/Jspproject/login.net�ΰ�� "JspProject/login.net"��ȯ�˴ϴ�.
		 * 
		 * 
		 */
		String RequestURI = request.getRequestURI();
		System.out.println("RequestURI = " + RequestURI);

		// getContextPath(): ���ؽ�Ʈ ��ΰ� ��ȯ�˴ϴ�.
		// contextPath�� "/JspProject"�� ��ȯ�˴ϴ�.

		String contextPath = request.getContextPath();
		System.out.println("contextPath = " + contextPath);

		// RequestURI���� ���ؽ�Ʈ ��� ���� ���� �ε��� ��ġ�� ���ں��ͤ�
		// ������ ��ġ ���ڱ��� �����մϴ�.
		// command�� "/login.net"�� ��ȯ�մϴ�.
		String command = RequestURI.substring(contextPath.length());
		System.out.println("command = " + command);

		// �ʱ�ȭ
		ActionForward forward = null;
		Action action = null;

		switch (command) {
		case "/BoardWrite.bo":
			action = new BoardWriteAction();
			break;
		case "/BoardList.bo":
			action = new BoardListAction();
			break;
		case "/BoardAddAction.bo":
			action = new BoardAddAction();
			break;

		case "/BoardReplyView.bo":
			action = new BoardReplyView();
			break;
		case "/BoardReplyAction.bo":
			action = new BoardReplyAction();
			break;
		case "/BoardDeleteAction.bo":
			action = new BoardDeleteAction();
			break;
		case "/BoardFileDown.bo":
			action = new BoardFileDownAction();
			break;
		case "/BoardModifyView.bo":
			action = new BoardModifyView();
			break;
		case "/BoardModifyAction.bo":
			action = new BoardModifyAction();
			break;
		case "/BoardDetailAction.bo" :
			action = new BoardDetailAction();
			break;
		case "/CommentAdd.bo" :
			action = new CommentAdd();
			break;
		case "/CommentList.bo" :
			action = new CommentList();
			break;
		case "/CommentDelete.bo" :
			action = new CommentDelete();
			break;
		case "/CommentUpdate.bo" :
			action = new CommentUpdate();
			break;
		case "/CommentReply.bo" :
			action = new CommentReply();
			break;
	
			
			
			

			
		}// switch end
		forward = action.execute(request, response);

		if (forward != null) {
			if (forward.isRedirect()) { // �����̷�Ʈ �˴ϴ�.
				response.sendRedirect(forward.getPath());
			} else {// �������˴ϴ�.
				RequestDispatcher dispatcher = request.getRequestDispatcher(forward.getPath());
				dispatcher.forward(request, response);

			}
		}

	}

	// doProcess(request,response)�޼��带 �����Ͽ� ��û�� get����̵�
	// Post����̵� ���۵Ǿ� ���� ���� �޼��忡�� ��û�� ó���� ���ֵ��� ó���Ҽ� �ֵ��� �Ͽ����ϴ�.

	protected void doGet(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		doProcess(request, response);
	}
	
	
	protected void doPost(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		 request.setCharacterEncoding("UTF-8");
		doProcess(request, response);
	}

}
