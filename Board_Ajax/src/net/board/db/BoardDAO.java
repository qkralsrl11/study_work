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
			System.out.println("DB���� ����:" + ex);
			return;
		}
	}

	public boolean boardInsert(BoardBean boarddata) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {

			// context.xml�� ���ҽ��� �����س��� (JNDI�� �����س���) jdbc/OracleDB��
			// �����Ͽ� Connection ��ü�� ���ɴϴ�.
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
			System.out.println("getConnection ;insert()");

			String max_sql = "(select nvl(max(board_num),0)+1 from board)";

			String sql = "insert into board " + "(board_num,	board_name,board_pass,	board_subject ,"
					+ "	board_content,	board_file, board_re_ref, " + "	board_re_lev, board_re_seq, board_readcount)"
					+ "	 values (" + max_sql + ", ?, ?, ?," + " 		 ?, ?," + max_sql + "," + "			 ?,?,?)	";

			// ���ο� ���� ����ϴ� �κ��Դϴ�.
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, boarddata.getBoard_name());
			pstmt.setString(2, boarddata.getBoard_pass());
			pstmt.setString(3, boarddata.getBoard_subject());
			pstmt.setString(4, boarddata.getBoard_content());
			pstmt.setString(5, boarddata.getBoard_file());
			// ������ ��� board_re_lev, board_re_seq �ʵ� ���� 0�Դϴ�.
			pstmt.setInt(6, 0); // board_re_lev �ʵ�
			pstmt.setInt(7, 0); // board_re_seq �ʵ�
			pstmt.setInt(8, 0); // board_re_readcount �ʵ�

			result = pstmt.executeUpdate();
			if (result == 1) {
				System.out.println("������ ������ ��� �Ϸ�Ǿ����ϴ�.");
				return true;
			}

			// primarykey�� �������� ��� �߻��ϴ� ����
		} catch (SQLIntegrityConstraintViolationException e) {
			result = -1;
			System.out.println("������̵� �ߺ� ���� �Դϴ�.");

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
					conn.close();// 4�ܰ� Db�������
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
			System.out.println("getListCount()���� :" + se);
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
					conn.close();// 4�ܰ� Db�������
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

		// page: ����
		// limit :�������� ����Ǽ�
		// board_re_ref desc,board_re_seq asc�� ���� ������ ����
		// �������� �´� rnum ���� ��ŭ �������� �������Դϴ�.

		String board_list_sql = "select * from " + "				(select rownum rnum, board_num, board_name,"
				+ "				board_subject , board_content,	board_file,"
				+ "				 board_re_ref, board_re_lev, board_re_seq, "
				+ "				board_readcount,board_date" + "         	from (select * from board"
				+ "						order by board_re_ref desc," + "						board_re_seq asc) "
				+ " 					)" + "where rnum>=? and rnum<=?";

		List<BoardBean> list = new ArrayList<BoardBean>();
		// ���������� 10���� ����� ��� 1������ , 2������, 3������, 4������...
		int startrow = (page - 1) * limit + 1; // �б������ row��ȣ(1 11 21 31..
		int endrow = startrow + limit - 1; // ���� ������ row��ȣ (10 20 30 40...

		try {

			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();
			pstmt = conn.prepareStatement(board_list_sql);
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			// db���� ������ �����͸� VO��ü�� ����ϴ�.
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
			System.out.println("getBoardList()���� :" + se);
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
					conn.close();// 4�ܰ� Db�������
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
			System.out.println("getDetail()���� :" + se);
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
					con.close();// 4�ܰ� Db�������
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

			System.out.println("setReadCountUpdate()���� : " + ex);

		} finally {

			try {
				if (pstmt != null)
					pstmt.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
			try {
				if (con != null)
					con.close();// 4�ܰ� Db�������
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

		// board ���̺��� �۹�ȣ�� ���ϱ����� board_num �ʵ��� �ִ밪�� ���ؿɴϴ�.

		String board_max_sql = "select max(board_num)+1 from board";

		/*
		 * �亯�� �� ���� �� �׷��ȣ�Դϴ�. �亯�� �ް� �Ǹ� �亯 ���� �� ��ȣ�� ���� ���ñ� ��ȣ�� ���� ó���Ǹ鼭 ���� �׷쿡 ���ϰ� �˴ϴ�.
		 * �۸�Ͽ��� �����ٶ� �ϳ��� �׷����� ������ ��µ˴ϴ�.
		 */
		int re_ref = board.getBoard_re_ref();

		/*
		 * ����� ���̸� �ǹ��մϴ�. ������ ���� ������ ��µǎA �ѹ� �鿩���� ó���� �ǰ� ��ۿ� ���� ����� �鿩���Ⱑ �ι� ó���˴ϴ�. ������
		 * ��쿡�� �̰��� 0�̰� ������ ����� 1, ����� ����� 2�� �˴ϴ�.
		 */

		int re_lev = board.getBoard_re_lev();

		// ���� ���� �� �߿��� �ش� ���� ��µǴ� �����Դϴ�.
		int re_seq = board.getBoard_re_seq();

		try {
			con = ds.getConnection();

			// Ʈ������� �̿��ϱ����ؼ� setAutoCommit�� false�� �����մϴ�.

			con.setAutoCommit(false);
			pstmt = con.prepareStatement(board_max_sql);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				num = rs.getInt(1);
			}
			pstmt.close();

			// Board_RE_REF, Board_re_seq ���� Ȯ���Ͽ� ���� �ۿ� �ٸ� ����� ������
			// �ٸ� ��۵��� Board_Re_Seq���� 1��������ŵ�ϴ�.
			// ������� �ٸ� ��ۺ��� �տ� ��µǰ� �ϱ� ���ؼ� �Դϴ�.

			String sql = "update board" + "		set board_re_seq = board_re_seq + 1" + "		where board_re_ref = ?"
					+ "		and board_re_seq > ?";

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, re_ref);
			pstmt.setInt(2, re_seq);
			pstmt.executeUpdate();
			pstmt.close();

			// ����� �亯 ���� board_re_lev, board_re_seq���� �����ۺ��� 1�� ������ŵ�ϴ�.
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
			pstmt.setString(5, "");// �亯���� ������ ���ε� ��Ű�� �ʽ��ϴ�.
			pstmt.setInt(6, re_ref);
			pstmt.setInt(7, re_lev);
			pstmt.setInt(8, re_seq);
			pstmt.setInt(9, 0); // Board_readcount (��ȸ��)�� 0
			if (pstmt.executeUpdate() == 1) {
				con.commit();// commit�մϴ�.

			} else {
				con.rollback();
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardReply()���� : " + ex);
			if (con != null) {
				try {
					con.rollback(); // rollback�մϴ�.
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
					con.close();// 4�ܰ� Db�������
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
			System.out.println("isBoardWriter()���� : " + ex);
			if (con != null) {
				try {
					con.rollback(); // rollback�մϴ�.
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
					con.close();// 4�ܰ� Db�������
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
				System.out.println("����������Ʈ");
				return true;
			}

		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardModify()���� : " + ex);
			if (con != null) {
				try {
					con.rollback(); // rollback�մϴ�.
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
					con.close();// 4�ܰ� Db�������
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
					result_check = true;// ������ �ȵȰ�쿡�� false�� ��ȯ�մϴ�.
			}
		} catch (SQLException ex) {
			ex.printStackTrace();
			System.out.println("boardDelte()���� : " + ex);

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
					con.close();// 4�ܰ� Db�������
				} catch (Exception e) {
					e.printStackTrace();

				}
		}
		return result_check;
		
	}

}
