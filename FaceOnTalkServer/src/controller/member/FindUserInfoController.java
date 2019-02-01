package controller.member;

import java.sql.SQLException;
import java.util.Hashtable;
import java.util.Map;

import controller.WebCommandController;
import dao.MemberDao;
import dto.MemberVO;
import request.Request;

/*
 * ģ�� ã�� ��Ʈ�ѷ� Ŭ����
 * (Ŭ���̾�Ʈ���� ��ü������ search type�� �Ǻ��ϰ�, request �ν��Ͻ��� Ÿ���� ����)
 */
public class FindUserInfoController implements WebCommandController {
	@Override
	public Request execute(Request request) {	
		String searchType = request.getParameter("searchtype");		
		if(searchType.equals("email"))
			return searchEmail(request);
		else 
			return searchPassword(request);
	}
	
	private Request searchEmail(Request request) {
		Request response = new Request(request.getType());
		MemberDao memberDao = MemberDao.getInstance();
		Map<String,Boolean> errors = new Hashtable<>(); 
		response.setAttribute("errors",errors);		
		try {
			MemberVO member = memberDao.findId(
					request.getParameter("birth")
					,request.getParameter("phone")
					,request.getParameter("gender"));
			if(member==null) {
				errors.put("notexist",Boolean.TRUE);
			} else {
				member.hideInfoForSearchList();
				String result = "ã���ô� ���̵� \n : "+member.getEmail();
				response.setParameter("result",result);
			}			
		}catch(SQLException e) {
			e.printStackTrace();
			errors.put("SQLException",Boolean.TRUE);
		}		
		return response;		
	}
	
	private Request searchPassword(Request request) {
		Request response = new Request(request.getType());
		MemberDao memberDao = MemberDao.getInstance();
		Map<String,Boolean> errors = new Hashtable<>(); 
		response.setAttribute("errors",errors);		
		try {
			MemberVO member = memberDao.findPassword(
					request.getParameter("email")
					,request.getParameter("phone")
					);					
			if(member==null) {
				errors.put("notexist",Boolean.TRUE);
			} else {
				System.out.println(member.getName());
				response.setAttribute("user",member);
			}			
		}catch(SQLException e) {
			e.printStackTrace();
			errors.put("SQLException",Boolean.TRUE);
		}		
		return response;	
	}
	
}


















