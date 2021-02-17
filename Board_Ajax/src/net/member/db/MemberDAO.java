package net.member.db;

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

import net.member.db.Member;

public class MemberDAO {
	private DataSource ds;

	public MemberDAO() {
		try {
			Context init = new InitialContext();
			ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
		} catch (Exception ex) {
			System.out.println("DB���� ����:" + ex);
			return;
		}
	}

	public int insert(Member member) {
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

			String sql = "insert into member " + "	 values (?,?,?,?,?,?)	";

			// PreparedStatemint
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, member.getId());
			pstmt.setString(2, member.getPass());
			pstmt.setString(3, member.getName());
			pstmt.setInt(4, member.getAge());
			pstmt.setString(5, member.getGender());
			pstmt.setString(6, member.getEmail());
			result = pstmt.executeUpdate();

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
		return result;

	}

	public int isId(String id, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;// ���̵� �������� �ʴ� ���
		try {
			Context init = new InitialContext();
			DataSource ds = (DataSource) init.lookup("java:comp/env/jdbc/OracleDB");
			conn = ds.getConnection();

			String select_sql = "select id, passward from member where id= ? ";
			pstmt = conn.prepareStatement(select_sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				if (rs.getString(2).equals(pass)) {
					result = 1;// ���̵�� ��й�ȣ�� ��ġ�ϴ� ���
				} else {
					result = 0;// ���̵�� ��ġ�ϰ� ��й�ȣ�� ��ġ���� �ʴ°��
				}

			}

		} catch (Exception se) {
			se.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
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

	public Member member_info(String id) {
		Member m = null;
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {
			con = ds.getConnection();

			String sql = "select * from member where id = ? ";
			pstmt = con.prepareStatement(sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();
			if (rs.next()) {
				m = new Member();
				m.setId(rs.getString(1));
				m.setPass(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));

			}
		} catch (Exception se) {
			se.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
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
		return m;

	}

	public int update(Member m) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		int result = 0;
		try {
			conn = ds.getConnection();

			String up_sql = "update member" + "		set  passward=?, name=?, age=?, gender=?, email=?, memberfile=?"
					+ "		where id = ?  ";

			// PreparedStatemint
			pstmt = conn.prepareStatement(up_sql);

			pstmt.setString(1, m.getPass());
			pstmt.setString(2, m.getName());
			pstmt.setInt(3, m.getAge());
			pstmt.setString(4, m.getGender());
			pstmt.setString(5, m.getEmail());
			pstmt.setString(6, m.getMemberfile());
			pstmt.setString(7, m.getId());

			result = pstmt.executeUpdate();

			// primarykey�� �������� ��� �߻��ϴ� ����

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
		return result;

	}

	public int getListCount() {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {

			conn = ds.getConnection();

			pstmt = conn.prepareStatement("select count(*) from member where id != 'admin'");
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

	public List<Member> getList(int page, int limit) {
		List<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// page: ����
		// limit :�������� ����Ǽ�
		// board_re_ref desc,board_re_seq asc�� ���� ������ ����
		// �������� �´� rnum ���� ��ŭ �������� �������Դϴ�.

		// ���������� 10���� ����� ��� 1������ , 2������, 3������, 4������...

		try {

			conn = ds.getConnection();

			String sql = "select * from " 
					+ "				(select b.* , rownum rnum "
					+ "				from( select * from member "
					+ "						where id != 'admin'"
					+ "						order by id) b " + "						)"
					+ "where rnum>=? and rnum<=?";

			pstmt = conn.prepareStatement(sql);
			int startrow = (page - 1) * limit + 1; // �б������ row��ȣ(1 11 21 31..
			int endrow = startrow + limit - 1; // ���� ������ row��ȣ (10 20 30 40...
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			// db���� ������ �����͸� VO��ü�� ����ϴ�.
			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPass(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));

				list.add(m);
			}

		} catch (Exception se) {
			System.out.println("getMemberList()���� :" + se);
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

	public int getListCount(String field, String value) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int x = 0;
		try {

			conn = ds.getConnection();
			String sql = "select count(*) from member " 
			+ "		where id != 'admin' " 
					+ "	and " + field + " like ? ";

			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1, "%" + value + "%");
			rs = pstmt.executeQuery();

			if (rs.next()) {
				x = rs.getInt(1);

			}

		} catch (Exception se) {
			System.out.println("getListCount(2)���� :" + se);
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

	public List<Member> getList(String field, String value, int page, int limit) {
		List<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		try {

			conn = ds.getConnection();

			String sql = "select * from " 
					+ "				(select b.* , rownum rnum "
					+ "				from(select * from member " 
					+ "						where id != 'admin'"
					+ "					and " + field + " like ? "
					+ "						order by id) b "
					+ "						) "
					+ " where rnum between ? and ? ";
			System.out.println(sql);
			pstmt = conn.prepareStatement(sql);
			pstmt.setString(1,"%"+value+"%");
		
			int startrow = (page - 1) * limit + 1; // �б������ row��ȣ(1 11 21 31..
			int endrow = startrow + limit - 1; // ���� ������ row��ȣ (10 20 30 40...
			pstmt.setInt(2, startrow);
			pstmt.setInt(3, endrow);
			rs = pstmt.executeQuery();

			while (rs.next()) {
				Member m = new Member();
				m.setId(rs.getString("id"));
				m.setPass(rs.getString(2));
				m.setName(rs.getString(3));
				m.setAge(rs.getInt(4));
				m.setGender(rs.getString(5));
				m.setEmail(rs.getString(6));

				list.add(m);
			}
			
			
			
			
		} catch (Exception se) {
			System.out.println("getListCount(4)���� :" + se);
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

	public int delete(String id) {
	
		
		Connection con = null;
		PreparedStatement pstmt = null;
		int result = 0;
		String delete_sql = " delete from member"
				+ "				where id = ? " ;
		
		try {
			con = ds.getConnection();
		
			
			pstmt = con.prepareStatement(delete_sql);
			pstmt.setString(1, id);
			result = pstmt.executeUpdate();
			
		
		
	}catch (SQLException ex) {
		ex.printStackTrace();
		System.out.println("boardDelte()���� : " + ex);

	} finally {

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
		
		
	
	
}

	
		
		
		

	public int isId(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;// ���̵� �������� �ʴ� ���
		try {
			con = ds.getConnection();
			String select_sql = "select id from member where id= ? ";
			pstmt = con.prepareStatement(select_sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result=0; //db�� �ش�id�� �ֽ��ϴ�.
			}

		} catch (Exception se) {
			se.printStackTrace();
		} finally {
			try {
				if (rs != null)
					rs.close();
			} catch (SQLException e) {
				System.out.println(e.getMessage());

			}
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
		return result;

	}
}