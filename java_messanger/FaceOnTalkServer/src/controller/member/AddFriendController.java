package controller.member;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import controller.WebCommandController;
import dao.MemberDao;
import dao.RelationshipDao;
import request.Request;


/*
 * ģ�� ��û ��Ʈ�ѷ�
 * A->B ģ����û �ϸ� , A�� ������ ���� �� �����
 */
public class AddFriendController implements WebCommandController {
	@Override
	public Request execute(Request request) {
		RelationshipDao relationDao = RelationshipDao.getInstance();
		MemberDao memberDao = MemberDao.getInstance();	
				
		int from = Integer.parseInt(request.getParameter("from"));
		int to = Integer.parseInt(request.getParameter("to"));
		
		Request response = new Request(request.getType());		
		Map<String,Boolean> errors = new Hashtable<>();
		response.setAttribute("errors",errors);				
		try{
			if((memberDao.confirmId(to))==-1) { //�������� �ʴ� ���̵�
				errors.put("invalidResponseId",Boolean.TRUE);
				return response;
			} else if(relationDao.isFriends(from, to)>0) { //�̹� ģ��
				errors.put("alreadyfriend",Boolean.TRUE);
				return response;
			}			
			relationDao.addFriendRequest(from, to);	
		}catch(SQLException e) {			
			errors.put("SQLException",Boolean.TRUE);
		}
		return response;	
	}
}
