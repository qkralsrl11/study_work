package net.board.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.util.ArrayList;
import java.util.List;

import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

public class BoardDAO {
	private DataSource ds;

	public BoardDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB연결 실패:" + ex);
			return;
		}
	}

	public boolean boardInsert(BoardBean boarddata) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {

			// context.xml에 리소스를 생성해놓은 (JNDI에 설정해놓은) jdbc/OracleDB를
			// 참조하여 Connection 객체를 얻어옵니다.
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
			System.out.println("getConnection ;insert()");

			String max_sql = "(select nvl(max(board_num),0)+1 from board)";

			String sql = "insert into board " + "(board_num,	board_name,board_pass,	board_subject ,"
					+ "	board_content,	board_file, board_re_ref, " + "	board_re_lev, board_re_seq, board_readcount)"
					+ "	 values (" + max_sql + ", ?, ?, ?," + " 		 ?, ?," + max_sql + "," + "			 ?,?,?)	";

			// 새로운 글을 등록하는 부분입니다.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boarddata.getBoard_name());
			pstmt.setString(2, boarddata.getBoard_pass());
			pstmt.setString(3, boarddata.getBoard_subject());
			pstmt.setString(4, boarddata.getBoard_content());
			pstmt.setString(5, boarddata.getBoard_file());
			// 원문의 경우 board_re_lev, board_re_seq 필드 값은 0입니다.
			pstmt.setInt(6, 0); // board_re_lev 필드
			pstmt.setInt(7, 0); // board_re_seq 필드
			pstmt.setInt(8, 0); // board_re_readcount 필드

			result = pstmt.executeUpdate();
			if (result == 1) {
				System.out.println("데이터 삽입이 모두 완료되었습니다.");
				return true;
			}

			// primarykey를 위반했을 경우 발생하는 에러
		} catch (SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("멤버아이디 중복 에러 입니다.");

		} catch (Exception se) {
			se.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
			try {
				if (conn != null)
					conn.close();// 4단계 Db연결끊기
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
		return false;

	}

	public int getListCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {

			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();

			pstmt = conn.prepareStatement("select count(*) from board");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);

			}

		} catch (Exception se) {
			System.out.println("getListCount()에서 :" + se);
			se.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
			try {
				if (conn != null)
					conn.close();// 4단계 Db연결끊기
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
		return x;

	}

	public List<BoardBean> getBoardList(int page, int limit) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// page: 페잊
		// limit :페이지당 목록의수
		// board_re_ref desc,board_re_seq asc에 의해 정렬한 것을
		// 조건절에 맞는 rnum 범위 만큼 가져오는 쿼리문입니다.

		String board_list_sql = "select * from " + "				(select rownum rnum, board_num, board_name,"
				+ "				board_subject , board_content,	board_file,"
				+ "				 board_re_ref, board_re_lev, board_re_seq, "
				+ "				board_readcount,board_date" + "         	from (select * from board"
				+ "						order by board_re_ref desc," + "						board_re_seq asc) "
				+ " 					)" + "where rnum>=? and rnum<=?";

		List<BoardBean> list = new ArrayList<BoardBean>();
		// 한페이지당 10개씩 목록인 경우 1페이지 , 2페이지, 3페이지, 4페이지...
		int startrow = (page - 1) * limit + 1; // 읽기시작할 row번호(1 11 21 31..
		int endrow = startrow + limit - 1; // 읽을 마지막 row번호 (10 20 30 40...

		try {

			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(board_list_sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			// db에서 가져온 데이터를 VO객체에 담습니다.
			while (rs.next()) {
				BoardBean board = new BoardBean();
				board.setBoard_num(rs.getInt("board_num"));
				board.setBoard_name(rs.getString("board_name"));
				board.setBoard_subject(rs.getString("board_subject"));
				board.setBoard_content(rs.getString("board_content"));
				board.setBoard_file(rs.getString("board_file"));
				board.setBoard_re_ref(rs.getInt("board_re_ref"));
				board.setBoard_re_lev(rs.getInt("board_re_lev"));
				board.setBoard_re_seq(rs.getInt("board_re_seq"));
				board.setBoard_readcount(rs.getInt("board_readcount"));
				board.setBoard_date(rs.getString("board_date"));
				list.add(board);
			}

		} catch (Exception se) {
			System.out.println("getBoardList()에서 :" + se);
			se.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
			try {
				if (conn != null)
					conn.close();// 4단계 Db연결끊기
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
		return list;

	}

	public BoardBean getDetail(int num) {
		BoardBean board = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();
			String select_sql = "select * from board where board_num =?";
			pstmt = con.prepareStatement(select_sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				board = new BoardBean();
				board.setBoard_num(rs.getInt("board_num"));
				board.setBoard_name(rs.getString("board_name"));
				board.setBoard_subject(rs.getString("board_subject"));
				board.setBoard_content(rs.getString("board_content"));
				board.setBoard_file(rs.getString("board_file"));
				board.setBoard_re_ref(rs.getInt("board_re_ref"));
				board.setBoard_re_lev(rs.getInt("board_re_lev"));
				board.setBoard_re_seq(rs.getInt("board_re_seq"));
				board.setBoard_readcount(rs.getInt("board_readcount"));
				board.setBoard_date(rs.getString("board_date"));
			}
		} catch (Exception se) {
			System.out.println("getDetail()에서 :" + se);
			se.printStackTrace();
		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
			try {
				if (con != null)
					con.close();// 4단계 Db연결끊기
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
		return board;

	}

	public void setReadCountUpdate(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;

		String sql = "update board" + "		set board_readcount = board_readcount+1" + "		where board_num=?";

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, num);
			pstmt.executeQuery();

		} catch (SQLException ex) {

			System.out.println("setReadCountUpdate()에러 : " + ex);

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
			try {
				if (con != null)
					con.close();// 4단계 Db연결끊기
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}

	}

	public int boardReply(BoardBean board) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int num = 0;

		// board 테이블의 글번호를 구하기위해 board_num 필드의 최대값을 구해옵니다.

		String board_max_sql = "select max(board_num)+1 from board";

		/*
		 * 답변을 달 원문 글 그룹번호입니다. 답변을 달게 되면 답변 글은 이 번호와 같은 관련글 번호를 갖게 처리되면서 같은 그룹에 속하게 됩니다.
		 * 글목록에서 보여줄때 하나의 그룹으로 묶여서 출력됩니다.
		 */
		int re_ref = board.getBoard_re_ref();

		/*
		 * 답글의 깊이를 의미합니다. 원문에 대한 갑들이 출력되랟 한번 들여쓰기 처리가 되고 답글에 대한 답글은 들여쓰기가 두번 처리됩니다. 원문인
		 * 경우에는 이값이 0이고 원문의 답글은 1, 답글의 답글은 2가 됩니다.
		 */

		int re_lev = board.getBoard_re_lev();

		// 같은 관련 글 중에서 해당 글이 출력되는 순서입니다.
		int re_seq = board.getBoard_re_seq();

		try {
			con = ds.getConnection();

			// 트랜잭션을 이용하기위해서 setAutoCommit을 false로 설정합니다.

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(board_max_sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
			pstmt.close();

			// Board_RE_REF, Board_re_seq 값을 확인하여 원문 글에 다른 답글이 잇으면
			// 다른 답글들의 Board_Re_Seq값을 1씩증가시킵니다.
			// 현재글을 다른 답글보다 앞에 출력되게 하기 위해서 입니다.

			String sql = "update board" + "		set board_re_seq = board_re_seq + 1" + "		where board_re_ref = ?"
					+ "		and board_re_seq > ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
			pstmt.close();

			// 등록할 답변 글의 board_re_lev, board_re_seq값을 원문글보다 1씩 증가시킵니다.
			re_seq = re_seq + 1;
			re_lev = re_lev + 1;

			sql = "insert into board " + "(board_num,	board_name, board_pass,	board_subject ,"
					+ "	board_content,	board_file, board_re_ref, " + "	board_re_lev, board_re_seq, board_readcount)"
					+ "	 values (" + num + "," + " 		?, ?, ?," + " 		?, ?, ?," + "         ?, ?, ?)	";

			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, board.getBoard_name());
			pstmt.setString(2, board.getBoard_pass());
			pstmt.setString(3, board.getBoard_subject());
			pstmt.setString(4, board.getBoard_content());
			pstmt.setString(5, "");// 답변에는 파일을 업로드 시키지 않습니다.
			pstmt.setInt(6, re_ref);
			pstmt.setInt(7, re_lev);
			pstmt.setInt(8, re_seq);
			pstmt.setInt(9, 0); // Board_readcount (조회수)는 0
			if (pstmt.executeUpdate() == 1) {
				con.commit();// commit합니다.

			} else {
				con.rollback();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardReply()에러 : " + ex);
			if (con != null) {
				try {
					con.rollback(); // rollback합니다.
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();

				}
			if (con != null)
				try {
					con.close();// 4단계 Db연결끊기
				} catch (Exception e) {
					e.printStackTrace();

				}
		}
		return num;
	}//

	public boolean isBoardWriter(int num, String pass) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		boolean result = false;
		String board_sql = "select board_pass from board where board_num = ? ";
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(board_sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				if (pass.equals(rs.getString("board_pass"))) {
					result = true;
				}
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("isBoardWriter()에러 : " + ex);
			if (con != null) {
				try {
					con.rollback(); // rollback합니다.
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();

				}
			if (con != null)
				try {
					con.close();// 4단계 Db연결끊기
				} catch (Exception e) {
					e.printStackTrace();

				}
		}
		return result;

	}//

	public boolean boardModify(BoardBean modifyboard) {
		Connection con = null;
		PreparedStatement pstmt = null;
		String sql = "update board " + "	set board_subject = ? ,  board_content =? , board_file= ? "
				+ "	where board_num = ? ";

		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, modifyboard.getBoard_subject());
			pstmt.setString(2, modifyboard.getBoard_content());
			pstmt.setString(3, modifyboard.getBoard_file());
			pstmt.setInt(4, modifyboard.getBoard_num());
			int result = pstmt.executeUpdate();
			if (result == 1) {
				System.out.println("성공업데이트");
				return true;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardModify()에러 : " + ex);
			if (con != null) {
				try {
					con.rollback(); // rollback합니다.
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}

		} finally {

			try {
				pstmt.close();
			} catch (SQLException ex) {
				ex.printStackTrace();
			}

			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();

				}
			if (con != null)
				try {
					con.close();// 4단계 Db연결끊기
				} catch (Exception e) {
					e.printStackTrace();

				}
		}
		return false;

	}//

	public boolean boardDelete(int num) {
		Connection con = null;
		PreparedStatement pstmt = null;
		PreparedStatement pstmt2 = null;
		ResultSet rs = null;
		String select_sql = " select board_re_ref, board_re_lev, board_re_seq "
							+ "				from board "
							+ "  			where board_num = ? ";

		String board_delete_sql = " delete from board " 
				+ "					where board_re_ref = ? "
				+ "					and	  board_re_lev >= ? " 
				+ "					and   board_re_seq >= ? "
				+ "					and	  board_re_seq <= ("
				+ "											nvl((select min(board_re_seq)-1 "
				+ "												from board "
				+ "												where board_re_ref = ? "
				+ "												and	  board_re_lev = ? "
				+ "												and   board_re_seq > ?) , "
				+ "												(select max(board_re_seq) "
				+ "													from board "
				+ "													where board_re_ref = ? )) "
				+ "													)";

		boolean result_check = false;
		try {
			con = ds.getConnection();
			pstmt = con.prepareStatement(select_sql);
			pstmt.setInt(1, num);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				pstmt2 = con.prepareStatement(board_delete_sql);
				pstmt2.setInt(1, rs.getInt("board_re_ref"));
				pstmt2.setInt(2, rs.getInt("board_re_lev"));
				pstmt2.setInt(3, rs.getInt("board_re_seq"));
				pstmt2.setInt(4, rs.getInt("board_re_ref"));
				pstmt2.setInt(5, rs.getInt("board_re_lev"));
				pstmt2.setInt(6, rs.getInt("board_re_seq"));
				pstmt2.setInt(7, rs.getInt("board_re_ref"));

				int count = pstmt2.executeUpdate();

				if (count >= 1)
					result_check = true;// 삭제가 안된경우에는 false를 반환합니다.
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardDelte()에러 : " + ex);

		} finally {
			if (rs != null)
				try {
					rs.close();

				} catch (SQLException ex) {
					ex.printStackTrace();
				}

			if (pstmt != null)
				try {
					pstmt.close();
				} catch (SQLException e) {
					e.printStackTrace();

				}
			if (con != null)
				try {
					con.close();// 4단계 Db연결끊기
				} catch (Exception e) {
					e.printStackTrace();

				}
		}
		return result_check;
		
	}

}
