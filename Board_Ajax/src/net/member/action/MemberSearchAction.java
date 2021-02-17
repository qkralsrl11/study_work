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
		

		// 로그인 성공시 파라미터page가 없어요 . 그래서 초기값이 필요합니다.
		int page = 1;
		int limit = 3;// 한페이지ㅔㅇ 보여줄 게시판 목록의수
		if (request.getParameter("page") != null) {
			page = Integer.parseInt(request.getParameter("page"));

		}
		System.out.println("넘어온 페이지 = " + page);
		
		List<Member> memberlist = null;
		// 총리스트수를 받아옵니다.
		int listcount =0;
		int index =-1;//search_field 존재 하지 않는 값으로 초기화
		
		String search_word ="";
		//메뉴- 관리자 -회원정보 클릭한 경우(member_list.net)
		//또는 메뉴- 관리자-회원정보 클릭후 페이지 클릭한 경우(member_list.net?page=2$search_file=-1$serach_word=)
		if(request.getParameter("search_word")==null
					|| request.getParameter("search_word").equals("")) {
			//총 리스트 수를 받아옵니다.
			listcount= mdao.getListCount();
			memberlist=mdao.getList(page,limit);
					
		}else {//검색을 클릭한경우
			index=Integer.parseInt(request.getParameter("search_field"));
			String[] search_field =new String[] {"id", "name", "age","gender"};
			search_word =request.getParameter("search_word");
			listcount= mdao.getListCount(search_field[index],search_word);
			memberlist =mdao.getList(search_field[index],search_word, page, limit);
			
		}
		
		



		/*
		 * 총페이지수 =(DB에 저장된 총리스트의 수 +한페이지에서 보여주는 리스트의 수 -1)/한페이지에서 보여주는 리스트의수
		 * 
		 * 예를들어 한 ㅠㅔ이지에서 보여주는 리스트의 수가 10개인경우 예1) DB에 저장된 총 리스트가 0이면 총페이지수는 0페이지 예2) DB에
		 * 저장된 총 리스트가 (1~10)이면 총페이지수는 1페이지 예3) DB에 저장된 총 리스트가 (11~20)이면 총페이지수는 2페이지 예4)
		 * DB에 저장된 총 리스트가 (21~30)이면 총페이지수는 3페이지
		 * 
		 */
		int maxpage = (listcount + limit - 1) / limit;
		System.out.println("총페이지수  = " + maxpage);

		/*
		 * startpage : 현재 페이지 그룹에서 맨 처음에 표시될 페이지 수를 의미합니다. ([],[],[]등...)보여줄 페이지가 30개일경우
		 * [1][2][3]...[30]까지 다표기하기에는 너무많기 때문에 보통 한페이지에는 10테이지 정도까지 이동할 수있게 표시합니다. 예)
		 * 페이지 그룹에 아래와같은경우 [1][2][3][4][5][6][7][8][9][10] 페이지 그룹의 시작 페이지는 startpage에
		 * 마지막 페이지는endpage에 구합니다.
		 * 
		 * 예로 1~10페이지의 내용을 나타낼때는 페이지 그룹은 [1][2][3]...[10]로 표시되고 11~20페이지의 내용을 나타낼떄는
		 * 페이지그룹은 [11][12][13].....[20]까지 표시됩니다.
		 * 
		 */

		int startpage = ((page - 1) / 10) * 10 + 1;
		System.out.println("현재 페이지에 보여줄 시작페이지수  = " + startpage);

		int endpage = startpage + 10 - 1;
		System.out.println("현재 페이지에 보여줄 마지막페이지수 =" + endpage);

	

		/*
		 * 마지막 그룹의 마지막페이지 값은 최대 페이지 값입니ㅏㄷ. 예로 마지막페이지 그룹이 [21]~[30]인경우 시작페이지 (Startpage=
		 * 21)와 마지막페이지(endpage=30)이지만 최대 페이지 (maxpage)가 25라면 [21]~[25]까지만 표시되도록합니다.
		 * 
		 * 
		 */

		if (endpage > maxpage)
			endpage = maxpage;

		request.setAttribute("page", page);// 현재페이지수
		request.setAttribute("maxpage", maxpage);// 최대페이지수

		// 현재 페이지에 표시할 첫페이지
		request.setAttribute("startpage", startpage);

		// 현재페이지에 표시할 끝페이지 수
		request.setAttribute("endpage", endpage);

		request.setAttribute("listcount", listcount); // 총글의수
		request.setAttribute("totallist", memberlist);
		request.setAttribute("search_field",index);
		request.setAttribute("search_word", search_word);
		
	
		forward.setRedirect(false);

		// 글목록 페이지로 이동하기 위한 경로를 설정합니다.

		forward.setPath("member/memberList.jsp");
		return forward;
	}// execute end

}// class end
