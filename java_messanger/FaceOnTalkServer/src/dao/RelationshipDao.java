package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import dto.MemberVO;
import jdbc.DBManager;

/*
 * ģ�� ���� Ŭ����
 */
public class RelationshipDao {
	//singleton
	private RelationshipDao(){}
	private static RelationshipDao inst = null;
	public static RelationshipDao getInstance(){
		if(inst == null)
			inst = new RelationshipDao();
		return inst;
	}
	
	//variables
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	
	/////////////////////
	//�̹� ģ�� ���� Ȯ��
	/////////////////////
	public int isFriends(int from,int to) throws SQLException {
		String query = "select * from relationship where (user_one_id=? and user_two_id=? and status='1') or"
				+ "(user_two_id = ? and user_one_id = ? and status = '1')";				
		try{
			int result = -1;
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement(query);
			pstmt.setInt(1, from);
			pstmt.setInt(2, to);
			pstmt.setInt(3,from);
			pstmt.setInt(4, to);
			rs = pstmt.executeQuery();
			while(rs.next()) {
				result = 1;
			}
			return result;			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally {
			DBManager.close(conn);
			DBManager.close(pstmt);
			DBManager.close(rs);
		}
	}
	
	/////////////////////
	//ģ����û (status 0)
	/////////////////////
	public void addFriendRequest(int from , int to) throws SQLException{
		try{
			conn = DBManager.getConnection();
			pstmt = conn.prepareStatement("insert into relationship (user_one_id,user_two_id,status,action_user_id) "
					+ "values(?,?,?,?)");
			pstmt.setInt(1, from);
			pstmt.setInt(2, to);
			pstmt.setString(3,"0");
			pstmt.setInt(4, from);
			pstmt.executeUpdate();			
		} catch(SQLException e) {
			e.printStackTrace();
			throw new SQLException(e);
		}
		finally {
			DBManager.close(conn);
			DBManager.close(pstmt);
			DBManager.close(rs);
		}		
	}
	
	/////////////////////
	//ģ�� ���� (status 1) ����  (status 2)
	/////////////////////
	public void addFriendResponse(int from,int to,String status) throws SQLException {
		try{
			conn = DBManager.getConnection();		
			conn.setAutoCommit(false);
			pstmt = conn.prepareStatement("update relationship set status=?, action_user_id = ? "
					+ "where user_one_id = ? and user_two_id = ?");		
			pstmt.setString(1, status);			
			pstmt.setInt(2, from);			
			pstmt.setInt(3, to);			
			pstmt.setInt(4, from);
			pstmt.executeUpdate();
			if(status.equals("1")) { //ģ�� ��� �����ϸ�
				MemberDao memberDao = MemberDao.getInstance(); //ģ�� ��� �� ����
				memberDao.increaseFriendCount(from);
				memberDao.increaseFriendCount(to);
			}
			conn.commit();			
		}catch(SQLException e){
			conn.rollback();
			e.printStackTrace();
			throw new SQLException(e);
		}finally{
			DBManager.close(conn);
			DBManager.close(pstmt);
			DBManager.close(rs);
		}
	}	
	
	//ģ�� ���� (�̱��� )
	public void removeFriend(int from,int to) throws SQLException {
		try{
			conn = DBManager.getConnection();		
			conn.setAutoCommit(false); //not auto commit
			pstmt = conn.prepareStatement("delete from relationship where "
					+ "(user_one_id=? and user_two_id=? and status='1') or"
					+ "(user_one_id=? and user_two_id=? and status='1')");					
			pstmt.setInt(1, from);
			pstmt.setInt(2, to);
			pstmt.setInt(3, to);
			pstmt.setInt(4, from);			
			pstmt.executeUpdate();
			MemberDao memberDao = MemberDao.getInstance();
			memberDao.decreaseFriendCount(from);
			memberDao.decreaseFriendCount(to);			
			conn.commit();			
		}catch(SQLException e){
			conn.rollback();
			e.printStackTrace();
			throw new SQLException(e);
		}finally{
			DBManager.close(conn);
			DBManager.close(pstmt);
			DBManager.close(rs);
		}
	}
	
	//ģ�� ���� �����ִ� �޼ҵ�
	public int getFriendCount(int id) throws SQLException {
		try{
			conn = DBManager.getConnection();			
			pstmt = conn.prepareStatement("select count(*) from relationship where user_one_id=? or user_two_id=? and status='1'");
			pstmt.setInt(1, id);
			pstmt.setInt(2, id);
			rs = pstmt.executeQuery();
			int cnt = 0;
			if(rs.next())
				cnt = rs.getInt(1);
			return cnt;			
		}catch(SQLException e){
			e.printStackTrace();
			throw new SQLException(e);
		}finally{
			DBManager.close(conn);
			DBManager.close(pstmt);
			DBManager.close(rs);
		}
	}	
}
