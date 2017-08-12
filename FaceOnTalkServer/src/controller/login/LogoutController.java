package controller.login;

import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.sql.SQLException;
import java.util.List;

import chat.ClientHandler;
import dao.MemberDao;
import dto.MemberVO;
import request.Request;

/*
 * �α׾ƿ� ��Ʈ�ѷ� Ŭ���� 
 * �α� �ƿ� �� ��� ģ���鿡�� ģ�� �α׾ƿ� �ߴٰ� ó������
 */
public class LogoutController {
	public void execute(MemberVO user) {
		//���� ģ���鿡�� �α׾ƿ��ߴٰ� notice
		Request notice = new Request("friendlogout");
		notice.setAttribute("logoutuser",user);
		MemberDao memberDao = MemberDao.getInstance();
		LoginUserManager manager = LoginUserManager.getInstance();		
		try{
			int id = user.getId();
			List<MemberVO> friendsList = memberDao.getFriendsList(id,user.getFriendCount());			
			for(int i=0;i<friendsList.size();i++) {
				MemberVO friends = friendsList.get(i);						
				ClientHandler client = manager.getUser(friends.getId());
				if(client!=null) {
					client.send(notice);					
				}			
			}									
		}catch(SQLException ignored) {
			
		}		
	}
}		
		
