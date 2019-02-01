package chat;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.sql.SQLException;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import controller.ChatCommandController;
import controller.login.LoginController;
import controller.login.LoginUserManager;
import controller.login.LogoutController;
import controller.member.AddFriendController;
import controller.member.RemoveFriendController;
import controller.member.ResponseAddFriendController;
import dao.MemberDao;
import dto.MemberVO;
import request.Request;
import util.UserGroupParser;

public class ClientHandler {	
	private Socket socket;	
	private MemberVO loginUser;	
	private LoginUserManager manager = LoginUserManager.getInstance();	
	
	public ClientHandler(Socket socket) {
		this.socket = socket;
		ChatServer.executorService.submit(() -> {
			receive();
		});		
	}
	
	public Socket getSocket() {
		return socket;
	}
			
	public void receive() {
		while(true) {			 
			try {
				ObjectInputStream oi = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				Request request = (Request)oi.readObject();
				
				ChatCommandController controller = null;				
				String type = request.getType();	
				System.out.println(type);
				
				////////////////////////////////////////////
				//�α��� ��û
				///////////////////////////////////////////
				if (type.equals("login")) { // �α��� ��û
					System.out.println("�α��� ��û �ؼ�");
					Request response = new LoginController().execute(request, this);
					loginUser = (MemberVO)response.getAttribute("loginuser");					
					@SuppressWarnings("unchecked")
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");
					if (errors.isEmpty()) { // �α��� ����
						System.out.println("�α��� ����");
						loginUser = (MemberVO) response.getAttribute("loginuser");
						
						// ģ���鿡�� �α��� �ߴٰ� �Ѹ�
						List<MemberVO> friendsList = (List<MemberVO>) response.getAttribute("onlist");
						int[] friends = new int[friendsList.size()];
						for(int i=0;i<friends.length;i++) {
							friends[i] = friendsList.get(i).getId();
						}						
						Request notice = new Request("friendlogin");
						notice.setAttribute("loginuser", loginUser);
						broadCastForGroup(notice,friends,false);						
					}	
					send(response);
				}

				////////////////////////////////////////////
				//�޽��� ��û
				///////////////////////////////////////////
				else if (type.equals("message")) { 
					System.out.println("�޽��� ��û �ؼ�");
					/*
					 * DB �� message���� ������ ��� ó��
					 * �ڵ� Request response = new
					 * MessageController().execute(request);
					 * 
					 * @SuppressWarnings("unchecked") Map<String,Boolean> errors =
					 * (Map<String,Boolean>)response.getAttribute("errors");
					 * if(errors.isEmpty()) { //���� ���� int[] users =
					 * UserGroupParser.getUserInteger(request.getParameter("groupid"
					 * )); for(int i=0;i<users.length;i++) { //�����ڿ��� ����
					 * ClientHandler client =
					 * manager.getUser(Integer.valueOf(users[i])); client.response =
					 * response; SelectionKey key =
					 * client.socketChannel.keyFor(selector);
					 * key.interestOps(SelectionKey.OP_WRITE); } } else { //���� ����
					 * 
					 * }
					 */////////////////////////////////////
					String groupId = request.getParameter("groupid"); //�׷���̵� -> ��Ʈ�迭��
					int senderId = Integer.parseInt(request.getParameter("sender")); //������� ���̵�
					String name = manager.getUser(senderId).getUser().getName(); //���� ��� �̸�				 
					String content = name + " > " + request.getParameter("content"); //���� ��� �̸� > ä�� ����
					request.setParameter("content", content); //request�� ���
					broadCastForGroup(request,groupId,false);				
				}				
				///////////////////////////////////////////
				//
				//////////////////////////////////////////
				
				////////////////////////////////////////////
				//���� ���� ��û
				///////////////////////////////////////////
				else if (type.equals("file_req")) { // ���� ���� (������ ����� ���� ��û)
					String groupId = request.getParameter("groupid");				
					broadCastForGroup(request, groupId, false);
				} else if (type.equals("file_res")) { // ���� ���� (�޴� ����� ���� ����)

				}

				////////////////////////////////////////////
				// ���� ��û 
				///////////////////////////////////////////
				else if (type.equals("remote_req")) { // ���� (���� �ϴ� ����� ���� ��û)
					System.out.println("���� ��û �ؼ�");
					//������ �Ľ�
					String groupId = request.getParameter("groupid");
					System.out.println("group id : "+groupId);
					String from = request.getParameter("from");
					String to = request.getParameter("to");
					String port = request.getParameter("port");				
					String ip  = request.getParameter("ip");
					
//					System.out.println("��û ip : "+ip);
//					System.out.println("��û port"+port);
					
					//ä�ù濡 ���� ����
					Request notice = new Request("notice");
					String content = "\t"+manager.getUser(from).getUser().getName()+"���� ������ ��û�Ͽ����ϴ�.";
					notice.setParameter("groupid",groupId);
					notice.setParameter("content",content);					
					broadCastForGroup(notice, groupId, true);
					
					if (manager.isLogin(to)) {//�޴� ������� ���� �޽��� ������						
						ClientHandler client = manager.getUser(to);
						Request response = new Request(request.getType());	
						response.setParameter("groupid",groupId);
						response.setParameter("from", from);
						response.setParameter("to", to);
						response.setParameter("port", port);
						response.setParameter("ip", ip);						
						client.send(response); //to���� ������						
					} else {
						Request response = new Request("remote_res");
						Map<String,Boolean> errors = new Hashtable<>();
						errors.put("logout",Boolean.TRUE);
						response.setParameter("logoutuser",from);
						response.setAttribute("errors",errors);	
						send(response); //���� ��û�� ������� ������
					}
				} else if (type.equals("remote_res")) { // ����( ���� �޴� ����� ���� ����(����) )
					String groupId = request.getParameter("groupid");
					String from = request.getParameter("from");
					String to = request.getParameter("to");	
					String content = null;
					
					if(request.getParameter("denied").equals("true")) 						
						content =  "\t"+manager.getUser(from).getUser().getName()+"���� ������ �����Ͽ����ϴ�.";						
					else 
						content =  "\t"+manager.getUser(from).getUser().getName()+"���� ������ �����Ͽ����ϴ�.";
																		
					//ä��â�� ǥ���ϱ�
					Request notice = new Request("notice");						
					notice.setParameter("groupid",groupId);
					notice.setParameter("content",content);					
					broadCastForGroup(notice, groupId, true);					
				}

				////////////////////////////////////////////
				// ģ�� ���
				///////////////////////////////////////////
				else if (type.equals("addfriend")) {
					System.out.println("ģ������ؼ�");
					Request response = new AddFriendController().execute(request);
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");
					if (errors.isEmpty()) { // ģ�� ��� ���� �����ϸ�
						// ��û�� ����� �������̸� �޽��� ����
						int from = Integer.parseInt(request.getParameter("from"));
						int to = Integer.parseInt(request.getParameter("to"));
						if (manager.isLogin(to)) { //ģ�� ��û �� ����� �α��� ���̸�
							ClientHandler client = manager.getUser(to);
							Request res = new Request("addfriend_req");
							/////// ��û�� ��� ������ ��Ƽ� ����
							try{
								MemberVO fromUser = MemberDao.getInstance().selectById(from);
								fromUser.hideForFriendsList();
								res.setAttribute("from", fromUser);
								client.send(res);		
							}catch(SQLException e){}											
						}
					}
					send(response);
				}
				////////////////////////////////////////////
				// ģ����û�°� ����
				///////////////////////////////////////////
				else if (type.equals("responseadd")) {
					System.out.println("ģ�� ���� �ؼ�");
					Request response = new ResponseAddFriendController().execute(request);
					int from = Integer.parseInt(request.getParameter("from"));
					int to = Integer.parseInt(request.getParameter("to"));
					if (manager.isLogin(to)) {
						ClientHandler client = manager.getUser(to);
						Request res = new Request("addfriend_res");
						/////// ��û�� ��� ������ ��Ƽ� ����
						try {
							MemberVO fromUser = MemberDao.getInstance().selectById(from);
							fromUser.hideForFriendsList();
							res.setAttribute("from", fromUser);
							client.send(res);
						} catch (SQLException ignored) {
						}
					}
					send(response);
				}

				////////////////////////////////////////////
				// ģ������
				///////////////////////////////////////////
				else if (type.equals("remove")) {
					System.out.println("ģ������ �ؼ�");
					Request response = new RemoveFriendController().execute(request);
					int from = Integer.parseInt(request.getParameter("from"));
					int to = Integer.parseInt(request.getParameter("to"));
					if (manager.isLogin(to)) {
						ClientHandler client = manager.getUser(to);
						Request cRes = new Request("removefriend_req");						
						/////// ��û�� ��� ������ ��Ƽ� ����
						try {
							MemberVO fromUser = MemberDao.getInstance().selectById(from);
							fromUser.hideForFriendsList();
							cRes.setAttribute("from", fromUser);
							client.send(cRes);							
						} catch (SQLException ignored) {
						}
					}
					send(response);
				}
				
				////////////////////////////////////////////
				// �α��� üũ
				///////////////////////////////////////////
				else if(type.equals("checklogin")) {
					Request response = new Request(request.getType());
					MemberVO checkUser = (MemberVO) request.getAttribute("checkuser");
					response.setAttribute("checkuser",checkUser);
					String result = null;
					if(manager.isLogin(checkUser.getId())) {
						result = "true";
					} else {
						result = "false";
					}
					response.setParameter("result",result);
					send(response);
				}				
				
			} catch(IOException e) { //��������
				new LogoutController().execute(loginUser); //�α׾ƿ� ó��
				manager.removeUser(loginUser.getId());
				break;
			} catch(ClassNotFoundException e) {
				
			}		
		}
	}
	
	//������ ó���� response�� Ŭ���̾�Ʈ���� ����
	public void send(Request response) {
		ChatServer.executorService.submit( () -> {
			try{								
				ObjectOutputStream oos=new ObjectOutputStream(new BufferedOutputStream(socket.getOutputStream()));
				oos.writeObject(response);
				oos.flush();						
			} catch(Exception e) {
				try{					
					manager.removeUser(loginUser.getId());
					socket.close();
				}catch(IOException e2){}
				
			}
		});
	}
	
	//�׷���̵�(String)�� �Ű�����
	private void broadCastForGroup(Request response,String groupId,boolean includeUser) {
			int[] users = UserGroupParser.getUserInteger(groupId);
			broadCastForGroup(response,users,includeUser);		
	}	
	
	//�׷���̵�(int[])�� �Ű����� -> ģ���鿡�� Request �ν��Ͻ� ����
	private void broadCastForGroup(Request response,int[] users,boolean includeUser) {		
			int admin = loginUser.getId();
			for (int i = 0; i < users.length; i++) {			
				ClientHandler client = manager.getUser(users[i]);
				if (client != null && admin!=users[i]) {				
					client.send(response);								
				}
			}		
			if(includeUser) {
				send(response);
			}
	}
	
	public void closeSocket() {
		if(socket!=null && socket.isClosed())
			try{socket.close();}catch(IOException e){}
	}

	public MemberVO getUser() {
		return loginUser;
	}	
}