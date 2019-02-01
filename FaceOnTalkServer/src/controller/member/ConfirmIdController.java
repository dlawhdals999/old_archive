package controller.member;

import java.sql.SQLException;
import java.util.Date;
import java.util.Hashtable;
import java.util.Map;

import controller.WebCommandController;
import dao.MemberDao;
import dto.MemberVO;
import request.Request;


/*
 * ���̵� Ȯ�� ��Ʈ�ѷ� Ŭ����
 * -> ȸ�� ������ �� �ߺ�üũ �� ���
 */
public class ConfirmIdController implements WebCommandController {
	@Override
	public Request execute(Request request) {
		MemberDao memberDao = MemberDao.getInstance();
		String email = request.getParameter("email");	
		
		Request response = new Request(request.getType());		
		Map<String,Boolean> errors = new Hashtable<>();
		response.setAttribute("errors",errors);		
		int existId = -1;
		try{
			existId = memberDao.confirmId(email);			
			if(existId == 1) { //���̵� ����				
				errors.put("duplicatedid",Boolean.TRUE);
			}					
		}catch(SQLException e) {
			e.printStackTrace();			
			errors.put("SQLException",Boolean.TRUE);
		}
		return response;
	}
}
