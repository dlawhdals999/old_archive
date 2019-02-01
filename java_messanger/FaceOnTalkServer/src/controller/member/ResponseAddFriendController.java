package controller.member;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import controller.WebCommandController;
import dao.MemberDao;
import dao.RelationshipDao;
import request.Request;

/*
 * A -> B ģ����û
 * B -> A ģ�� ������ B�� ������ type="responseadd"����.
 * ģ�� ���� ��Ʈ�ѷ� Ŭ����
 */
public class ResponseAddFriendController implements WebCommandController{
	@Override
	public Request execute(Request request) {
		RelationshipDao relationDao = RelationshipDao.getInstance();
		MemberDao memberDao = MemberDao.getInstance();		
		
		int from = Integer.parseInt(request.getParameter("from"));
		int to = Integer.parseInt(request.getParameter("to"));
		
		Request response = new Request(request.getType());		
		Map<String,Boolean> errors = new Hashtable<>();
		response.setAttribute("errors",errors);				
		try {
			if((memberDao.confirmId(to))==-1) {				
				errors.put("invalidToId",Boolean.TRUE);
				return response;
			}			
			relationDao.addFriendResponse(from,to,request.getParameter("status"));									
		}catch(SQLException e) {
			errors.put("SQLException",Boolean.TRUE);
		}
		return response;		
	}
}
