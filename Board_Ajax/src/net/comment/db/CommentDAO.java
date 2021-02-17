package net.comment.db;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;


import javax.naming.Context;
import javax.naming.InitialContext;
import javax.sql.DataSource;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;



public class CommentDAO {
	private DataSource ds;

	public CommentDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB���� ����:" + ex);
			return;
		}
	}
	//���� �������ϱ�
	public int getListCount(int BOARD_RE_REF) {
		
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		String sql = "select count(*)"
				+ "		from comm where comment_board_num =?";
		try {

		
			con = ds.getConnection();

			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, BOARD_RE_REF);
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
				if (con != null)
					con.close();// 4�ܰ� Db�������
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
		return x;

	}
	public JsonArray getCommentList(int comment_board_num, int state) {

		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		String sort ="asc";
		if(state==2) {
			sort ="desc";
		}
		String sql = "select *  " 
				+ "				from (select b.* , rownum rnum "
				+ "				from( "
				+ "					select num, comm.id, content, reg_date, comment_re_lev, "
				+ "					comment_re_ref, comment_re_seq, member.memberfile "
				+ "			         from comm join member"
				+ "						on comm.id=member.id "
				+ "						where comment_board_num = ? "
				+ "						order by comment_re_ref " + sort + ", "						
				+ "							comment_re_seq asc)b"
				+ "			)";
		
		JsonArray array = new JsonArray();
		
		try {

			con = ds.getConnection();
			pstmt = con.prepareStatement(sql);
			pstmt.setInt(1, comment_board_num);
			rs = pstmt.executeQuery();

			// db���� ������ �����͸� VO��ü�� ����ϴ�.
			while (rs.next()) {
				
				JsonObject object = new JsonObject();
				object.addProperty("num", rs.getInt(1));
				object.addProperty("id", rs.getString(2));
				object.addProperty("content", rs.getString(3));
				object.addProperty("reg_date", rs.getString(4));
				object.addProperty("comment_re_lev", rs.getInt(5));
				object.addProperty("comment_re_ref", rs.getInt(6));
				object.addProperty("comment_re_seq", rs.getInt(7));
				object.addProperty("memberfile", rs.getString(8));
				array.add(object);

				
			}

		} catch (Exception se) {
			System.out.println("getCommentList()���� :" + se);
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
		return array;

	}
	public int insert(CommentBean comm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "insert into comm " 
				+ "	 values ("
				+ "comm_seq.nextval,? ,? ,sysdate ,? ,? ,? ,comm_seq.nextval )";
		try {

			// context.xml�� ���ҽ��� �����س��� (JNDI�� �����س���) jdbc/OracleDB��
			// �����Ͽ� Connection ��ü�� ���ɴϴ�.
		
			conn = ds.getConnection();
	

			

			// PreparedStatemint
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comm.getId());
			pstmt.setString(2, comm.getContent());
			pstmt.setInt(3, comm.getComment_board_num());
			pstmt.setInt(4, comm.getComment_re_lev());
			pstmt.setInt(5, comm.getComment_re_seq());
			
		
			result = pstmt.executeUpdate();

			// primarykey�� �������� ��� �߻��ϴ� ����
	
		} catch (Exception se) {
			System.out.println("insert()���� :" + se);
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
		return result;

	}
	public int update(CommentBean comm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;

		String sql = "update comm set"
				+ 	" content = ?, reg_date =sysdate where num = ? ";
		try {

			// context.xml�� ���ҽ��� �����س��� (JNDI�� �����س���) jdbc/OracleDB��
			// �����Ͽ� Connection ��ü�� ���ɴϴ�.
		
			conn = ds.getConnection();
	

			

			// PreparedStatemint
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comm.getContent());
			pstmt.setInt(2, comm.getNum());
		
			
		
			result = pstmt.executeUpdate();
			if(result==1)
				System.out.println("������ ���� �Ϸ�");

			// primarykey�� �������� ��� �߻��ϴ� ����
	
		} catch (Exception se) {
			System.out.println("update()���� :" + se);
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
		return result;

	}
	public int commDelete(int num) {
		Connection conn = null;
		PreparedStatement pstmt = null;

		int result=0;
		String select_sql = " delete comm where num = ? ";
		try {

			// context.xml�� ���ҽ��� �����س��� (JNDI�� �����س���) jdbc/OracleDB��
			// �����Ͽ� Connection ��ü�� ���ɴϴ�.
		
			conn = ds.getConnection();
	

			
			pstmt = conn.prepareStatement(select_sql);
			pstmt.setInt(1, num);
			result = pstmt.executeUpdate();
			if(result ==1)
				System.out.println("�����Ͱ� ���� �Ǿ����ϴ�.");
		
		
				
			
			
		} catch (Exception se) {
			System.out.println("commDelete()���� :" + se);
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
		return result ;
	
		
		
		
		
		
	}
	public int Reply(CommentBean comm) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		
		String up_sql = "update comm set comment_re_seq = comment_re_seq +1 "
				+ "			where comment_re_ref =? "
				+ "			and comment_re_seq> ? ";
		
		
		String sql = "insert into comm " 
				+ "	 values ("
				+ "comm_seq.nextval,? ,? ,sysdate ,? ,? ,? ,? )";
		try {			
			conn = ds.getConnection();
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement(up_sql.toString());
			pstmt.setInt(1, comm.getComment_re_ref());
			pstmt.setInt(2, comm.getComment_re_seq());
			pstmt.executeUpdate();
			pstmt.close();

			

			// PreparedStatemint
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, comm.getId());
			pstmt.setString(2, comm.getContent());
			pstmt.setInt(3, comm.getComment_board_num());
			pstmt.setInt(4, comm.getComment_re_lev()+1);
			pstmt.setInt(5, comm.getComment_re_seq()+1);
			pstmt.setInt(6, comm.getComment_re_ref());
		
			result = pstmt.executeUpdate();
			if(result ==1) {
				System.out.println("reply ���ԿϷ�");
				conn.commit();
			}

			// primarykey�� �������� ��� �߻��ϴ� ����
	
		} catch (Exception se) {
			System.out.println("reply()���� :" + se);
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
		return result;

	}
}
