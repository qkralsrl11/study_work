package net.member.action;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import net.member.db.Member;
import net.member.db.MemberDAO;

public class MemberSearchAction implements Action {

	@Override
	public ActionForward execute(HttpServletRequest request, HttpServletResponse response)
			throws ServletException, IOException {
		
		ActionForward forward =new ActionForward();
		MemberDAO mdao = new MemberDAO();
		

		// �α��� ������ �Ķ����page�� ����� . �׷��� �ʱⰪ�� �ʿ��մϴ�.
		int page = 1;
		int limit = 3;// ���������Ĥ� ������ �Խ��� ����Ǽ�
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));

		}
		System.out.println("�Ѿ�� ������ = " + page);
		
		List<Member> memberlist = null;
		// �Ѹ���Ʈ���� �޾ƿɴϴ�.
		int listcount =0;
		int index =-1;//search_field ���� ���� �ʴ� ������ �ʱ�ȭ
		
		String search_word ="";
		//�޴�- ������ -ȸ������ Ŭ���� ���(member_list.net)
		//�Ǵ� �޴�- ������-ȸ������ Ŭ���� ������ Ŭ���� ���(member_list.net?page=2$search_file=-1$serach_word=)
		if(request.getParameter("search_word")==null
					|| request.getParameter("search_word").equals("")) {
			//�� ����Ʈ ���� �޾ƿɴϴ�.
			listcount= mdao.getListCount();
			memberlist=mdao.getList(page,limit);
					
		}else {//�˻��� Ŭ���Ѱ��
			index=Integer.parseInt(request.getParameter("search_field"));
			String[] search_field =new String[] {"id", "name", "age","gender"};
			search_word =request.getParameter("search_word");
			listcount= mdao.getListCount(search_field[index],search_word);
			memberlist =mdao.getList(search_field[index],search_word, page, limit);
			
		}
		
		



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

		request.setAttribute("page", page);// ������������
		request.setAttribute("maxpage", maxpage);// �ִ���������

		// ���� �������� ǥ���� ù������
		request.setAttribute("startpage", startpage);

		// ������������ ǥ���� �������� ��
		request.setAttribute("endpage", endpage);

		request.setAttribute("listcount", listcount); // �ѱ��Ǽ�
		request.setAttribute("totallist", memberlist);
		request.setAttribute("search_field",index);
		request.setAttribute("search_word", search_word);
		
	
		forward.setRedirect(false);

		// �۸�� �������� �̵��ϱ� ���� ��θ� �����մϴ�.

		forward.setPath("member/memberList.jsp");
		return forward;
	}// execute end

}// class end
