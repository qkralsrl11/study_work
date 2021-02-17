package net.board.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.google.gson.Gson;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import net.board.db.BoardBean;
import net.board.db.BoardDAO;

public class BoardListAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		BoardDAO boarddao = new BoardDAO();
		List<BoardBean> boardlist = new ArrayList<BoardBean>();

		// �α��� ������ �Ķ����page�� ����� . �׷��� �ʱⰪ�� �ʿ��մϴ�.
		int page = 1;
		int limit = 10;// ���������Ĥ� ������ �Խ��� ����Ǽ�
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));

		}
		System.out.println("�Ѿ�� ������ = " + page);
		
		//�߰�
		if(request.getParameter("limit") != null) {
			limit =Integer.parseInt(request.getParameter("limit"));
		}
		
		
		System.out.println("�Ѿ�� limit = " + limit);

		// �Ѹ���Ʈ���� �޾ƿɴϴ�.
		int listcount = boarddao.getListCount();

		// ����Ʈ�� �޾ƿɴϴ�.
		boardlist = boarddao.getBoardList(page, limit);

		/*
		 * ���������� =(DB�� ����� �Ѹ���Ʈ�� �� +������������ �����ִ� ����Ʈ�� �� -1)/������������ �����ִ� ����Ʈ�Ǽ�
		 * 
		 * ������� �� �Ф��������� �����ִ� ����Ʈ�� ���� 10���ΰ�� ��1) DB�� ����� �� ����Ʈ�� 0�̸� ������������ 0������ ��2) DB��
		 * ����� �� ����Ʈ�� (1~10)�̸� ������������ 1������ ��3) DB�� ����� �� ����Ʈ�� (11~20)�̸� ������������ 2������ ��4)
		 * DB�� ����� �� ����Ʈ�� (21~30)�̸� ������������ 3������
		 * 
		 */
		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("����������  = " + maxpage);

		/*
		 * startpage : ���� ������ �׷쿡�� �� ó���� ǥ�õ� ������ ���� �ǹ��մϴ�. ([],[],[]��...)������ �������� 30���ϰ��
		 * [1][2][3]...[30]���� ��ǥ���ϱ⿡�� �ʹ����� ������ ���� ������������ 10������ �������� �̵��� ���ְ� ǥ���մϴ�. ��)
		 * ������ �׷쿡 �Ʒ��Ͱ������ [1][2][3][4][5][6][7][8][9][10] ������ �׷��� ���� �������� startpage��
		 * ������ ��������endpage�� ���մϴ�.
		 * 
		 * ���� 1~10�������� ������ ��Ÿ������ ������ �׷��� [1][2][3]...[10]�� ǥ�õǰ� 11~20�������� ������ ��Ÿ������
		 * �������׷��� [11][12][13].....[20]���� ǥ�õ˴ϴ�.
		 * 
		 */

		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("���� �������� ������ ������������  = " + startpage);

		int endpage = startpage + 10 - 1;
		System.out.println("���� �������� ������ �������������� =" + endpage);

	

		/*
		 * ������ �׷��� ������������ ���� �ִ� ������ ���ԴϤ���. ���� ������������ �׷��� [21]~[30]�ΰ�� ���������� (Startpage=
		 * 21)�� ������������(endpage=30)������ �ִ� ������ (maxpage)�� 25��� [21]~[25]������ ǥ�õǵ����մϴ�.
		 * 
		 * 
		 */

		if (endpage > maxpage)
			endpage = maxpage;
		
		String state =request.getParameter("state");
		
		if(state==null) {
		System.out.println("state==null");
		request.setAttribute("page", page);// ������������
		request.setAttribute("maxpage", maxpage);// �ִ���������

		// ���� �������� ǥ���� ù������
		request.setAttribute("startpage", startpage);

		// ������������ ǥ���� �������� ��
		request.setAttribute("endpage", endpage);

		request.setAttribute("listcount", listcount); // �ѱ��Ǽ�

		// �ش��������� �� ����� �����ִ� ����Ʈ
		request.setAttribute("boardlist", boardlist);
		request.setAttribute("limit", limit);
		ActionForward forward = new ActionForward();
		forward.setRedirect(false);

		// �۸�� �������� �̵��ϱ� ���� ��θ� �����մϴ�.

		forward.setPath("board/boardList.jsp");
		return forward;
		
		}else {
			System.out.println("state=ajax");
			
			//������ request�� ��Ҵ� ����JsonObject�� ����ϴ�.
			JsonObject object = new JsonObject();
			object.addProperty("page", page); //{"page": ���� page�� ��} �������� ����
			object.addProperty("maxpage", maxpage);
			object.addProperty("startpage", startpage);
			object.addProperty("endpage", endpage);
			object.addProperty("listcount", listcount);
			object.addProperty("limit", limit);
			
			//JsonObject List ������ ������ �ִ�  addProperty() �������� �ʽ��ϴ�.
			//void com.google.gson.JsonObject.add(String property, jsonElement value)
			//�޼��带 ���ؼ� �����մϴ�.
			//List������ JsonElement�� �ٲپ��־�� object�� ������ ���ֽ��ϴ�.
			//List => JsonElement
			JsonElement je = new Gson().toJsonTree(boardlist);
			System.out.println("boardList="+je.toString());
			object.add("boardlist", je);
			
			response.setContentType("text/html;charset=utf-8");
			response.getWriter().append(object.toString());
			System.out.println(object.toString());
			return null;
				
			
		}//if end
		
		
		
		
		
		
		
	}// execute end

}// class end
