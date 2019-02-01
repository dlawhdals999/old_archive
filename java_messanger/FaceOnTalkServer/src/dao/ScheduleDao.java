package dao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import dto.ScheduleVO;
import jdbc.DBManager;

public class ScheduleDao {
	// singleton
	private ScheduleDao() {}
	private static ScheduleDao inst = null;
	public static ScheduleDao getInstance() {
		if (inst == null)
			inst = new ScheduleDao();
		return inst;
	}
	// variables
	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;
	
	//methods	
	///////////////////
	//������ ����
	//////////////////
	public int insertSchedule(ScheduleVO schedule) throws SQLException {
	    String query = "insert into schedule (group_id,content,writer,regdate,d_day)"
	    		+ "values(?,?,?,?,?)";
	    int result = 0;
	    try {
	      conn = DBManager.getConnection();
	      pstmt = conn.prepareStatement(query);
	      pstmt.setString(1, schedule.getGroupId());
	      pstmt.setString(2,schedule.getContent());
	      pstmt.setInt(3, schedule.getWriter());
	      pstmt.setTimestamp(4,new Timestamp(schedule.getRegDate().getTime()));
	      pstmt.setTimestamp(5, new Timestamp(schedule.getDate().getTime()));	      
	      result = pstmt.executeUpdate();
	    } catch (SQLException e) {	    	
	      e.printStackTrace();
	      throw new SQLException(e);	      
	    } finally {
	      DBManager.close(conn);
	      DBManager.close(pstmt);
	      DBManager.close(rs);
	    }	    	
	    return result;
	}
	
	///////////////////
	//group_id�� �ش��ϴ� ������ ����Ʈ
	//////////////////	
	public Map<String,ScheduleVO> selectByGroupId(String groupId) throws SQLException {
	    Map<String,ScheduleVO> scheduleMap = new Hashtable<>(); 	    
	    try {
	      conn = DBManager.getConnection();
	      pstmt = conn.prepareStatement("select * from schedule where group_id = ? ");
	      pstmt.setString(1, groupId);	      
	      rs = pstmt.executeQuery();	      
	      SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMdd");
	      while(rs.next()) {
	    	  ScheduleVO schedule = convertSchedule(rs);	    	  
	    	  scheduleMap.put(sdf.format(schedule.getDate()),schedule);
	    	  System.out.println(sdf.format(schedule.getDate()));
	      }	      
	    } catch (SQLException e) {	
	    	e.printStackTrace();
	    	throw new SQLException(e);	      	      
	    } finally {
	      DBManager.close(conn);
	      DBManager.close(pstmt);
	      DBManager.close(rs);
	    }	    		    
	    return scheduleMap;
	}
	
	
	////////////////////
	//group_id && d_day�� �ش��ϴ� ������ ����
	////////////////////
	public int deleteSchedule(String groupId,String date,String nextDate) throws SQLException {
		int result = -1;	        
	    try {
	      conn = DBManager.getConnection();
	      pstmt = conn.prepareStatement("delete from schedule where group_id = ? "
	      		+ "and d_day >= to_date(?,'YYYY-MM-DD') and d_day < to_date(?,'YYYY-MM-DD') ");
	      pstmt.setString(1, groupId);
	      pstmt.setString(2, date);
	      pstmt.setString(3, nextDate);
	      result = pstmt.executeUpdate();           
	    } catch (SQLException e) {	
	    	e.printStackTrace();
	    	throw new SQLException(e);	      	      
	    } finally {
	      DBManager.close(conn);
	      DBManager.close(pstmt);
	      DBManager.close(rs);
	    }	    		    
	    return result;
	}
	
	
	//���Ŀ� ������ ���� �Ҷ� ����ϸ� ��
//	private void update(String groupId,String writer,String content) throws SQLException {
//		Connection connn = null;
//	    PreparedStatement pstmt = null;
//	    ResultSet rs = null;	    	    
//	    try {
//	      connn = DBManager.getConnection();
//	      pstmt = connn.prepareStatement("update schedule set writer = ? , content = ? where group_id");
//	      pstmt.setString(1, groupId);	      
//	      rs = pstmt.executeQuery();	      
//	      while(rs.next()) {
//	    	  scheduleList.add(convertSchedule(rs));
//	      }	      
//	    } catch (SQLException e) {	
//	    	e.printStackTrace();
//	    	throw new SQLException(e);	      	      
//	    } finally {
//	      DBManager.close(conn);
//	      DBManager.close(pstmt);
//	      DBManager.close(rs);
//	    }	    		    
//	    return scheduleList;		
//	}
		
	
	/////////////
	//private methods	
	//ResultSet�� ScheduleVO�� ����
	private ScheduleVO convertSchedule(ResultSet rs) throws SQLException {
		return new ScheduleVO(
				rs.getString("group_id")
				,rs.getInt("writer")
				,rs.getString("content")				
				,toDate(rs.getTimestamp("regdate"))
				,toDate(rs.getTimestamp("d_day"))
				);				
	}
	
	//Timestamp -> Date �ν��Ͻ��� ����
	private Date toDate(Timestamp date) {
		return date == null ? null : new Date(date.getTime());
	}			
}
