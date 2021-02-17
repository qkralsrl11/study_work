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
			System.out.println("DB연결 실패:" + ex);
			return;
		}
	}

	public int insert(Member member) {
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
		return result;

	}

	public int isId(String id, String pass) {
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;// 아이디가 존재하지 않는 경우
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
					result = 1;// 아이디와 비밀번호가 일치하는 경우
				} else {
					result = 0;// 아이디는 일치하고 비밀번호가 일치하지 않는경우
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
					conn.close();// 4단계 Db연결끊기
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
					con.close();// 4단계 Db연결끊기
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

			// primarykey를 위반했을 경우 발생하는 에러

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

	public List<Member> getList(int page, int limit) {
		List<Member> list = new ArrayList<Member>();
		Connection conn = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;

		// page: 페잊
		// limit :페이지당 목록의수
		// board_re_ref desc,board_re_seq asc에 의해 정렬한 것을
		// 조건절에 맞는 rnum 범위 만큼 가져오는 쿼리문입니다.

		// 한페이지당 10개씩 목록인 경우 1페이지 , 2페이지, 3페이지, 4페이지...

		try {

			conn = ds.getConnection();

			String sql = "select * from " 
					+ "				(select b.* , rownum rnum "
					+ "				from( select * from member "
					+ "						where id != 'admin'"
					+ "						order by id) b " + "						)"
					+ "where rnum>=? and rnum<=?";

			pstmt = conn.prepareStatement(sql);
			int startrow = (page - 1) * limit + 1; // 읽기시작할 row번호(1 11 21 31..
			int endrow = startrow + limit - 1; // 읽을 마지막 row번호 (10 20 30 40...
			pstmt.setInt(1, startrow);
			pstmt.setInt(2, endrow);
			rs = pstmt.executeQuery();

			// db에서 가져온 데이터를 VO객체에 담습니다.
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
			System.out.println("getMemberList()에서 :" + se);
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
			System.out.println("getListCount(2)에서 :" + se);
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
		
			int startrow = (page - 1) * limit + 1; // 읽기시작할 row번호(1 11 21 31..
			int endrow = startrow + limit - 1; // 읽을 마지막 row번호 (10 20 30 40...
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
			System.out.println("getListCount(4)에서 :" + se);
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
		System.out.println("boardDelte()에러 : " + ex);

	} finally {

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
		
		
	
	
}

	
		
		
		

	public int isId(String id) {
		Connection con = null;
		PreparedStatement pstmt = null;
		ResultSet rs = null;
		int result = -1;// 아이디가 존재하지 않는 경우
		try {
			con = ds.getConnection();
			String select_sql = "select id from member where id= ? ";
			pstmt = con.prepareStatement(select_sql);
			pstmt.setString(1, id);
			rs = pstmt.executeQuery();

			if (rs.next()) {
				result=0; //db에 해당id가 있습니다.
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
					con.close();// 4단계 Db연결끊기
			} catch (Exception e) {
				System.out.println(e.getMessage());

			}
		}
		return result;

	}
}