package controller.schedule;

import java.sql.SQLException;
import java.util.Calendar;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import controller.WebCommandController;
import dao.MemberDao;
import dao.RelationshipDao;
import dao.ScheduleDao;
import dto.ScheduleVO;
import request.Request;
import util.UserGroupParser;

/*
 * ������ ���� ��Ʈ�ѷ� ( �̱���)
 */
public class ModifyScheduleController implements WebCommandController {
	@Override
	public Request execute(Request request) {
//		ScheduleDao scheduleDao = ScheduleDao.getInstance();		
//		Request response = new Request(request.getType());		
//		Map<String,Boolean> errors = new Hashtable<>();
//		response.setAttribute("errors",errors);				
//		try{			
//			String groupId = UserGroupParser.getUserStringSort(request.getParameter("groupid"));
//			String writer = request.getParameter("writer");
//			String content = request.getParameter("content");
//			String date = request.getParameter("date");
//			//scheduleDao.update(groupId,writer,content,date);
//			
//		}catch(SQLException e) {
//			e.printStackTrace();
//			System.out.println("SQL Exception");
//			errors.put("SQLException",Boolean.TRUE);
//		}
//		return response;
		return null;
	}	
}
