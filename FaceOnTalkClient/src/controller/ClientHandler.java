package controller;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.util.List;
import java.util.Map;

import javax.swing.JFrame;
import javax.swing.JOptionPane;

import dto.MemberVO;
import frame.LoginFrame;
import main.MainFrame;
import noticeframe.FriendsLoginFrame;
import noticeframe.MessageFrame;
import remote.client.RemoteClient;
import request.Request;
import serverinfo.ServerInfo;

public class ClientHandler {
	/////////////////
	//singleton
	/////////////////
	private static ClientHandler inst = null;
	private ClientHandler(){}
	public static ClientHandler getInstance(){
		if(inst == null)
			inst = new ClientHandler();
		return inst;
	}	
	
	////////////////
	// variables
	///////////////
	private Socket socket;
	private MemberVO admin;
	private MessageController messageManager;
	private MemberController friendsManager;
	
	////////////////
	//�ǽð� ���� ó�� ������
	////////////////
	private JFrame loginFrm;	
	private MainFrame mainFrm;
	
	////////////////
	//methods
	////////////////	
	//login
	public void login(String email, String password, JFrame loginFrm) {		
		this.loginFrm = loginFrm;						
		Request request = new Request("login");
		request.setParameter("email", email);
		request.setParameter("password", password);	
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					// connect
					socket = new Socket(ServerInfo.IP,ServerInfo.CHAT_PORT);					
					// �α� ��û
					ObjectOutputStream oos = new ObjectOutputStream(socket.getOutputStream());
					oos.writeObject(request);			
					oos.flush();				
					receive();	
				} catch (Exception e) {
					e.printStackTrace();			
					new MessageFrame("�������� ������ �����ʽ��ϴ�.",false);
				}
			}
		};
		thread.start();		 
	}

	//Server -> User 
	private void receive() {	
		while (true) {
			try {				
				ObjectInputStream ois = new ObjectInputStream(new BufferedInputStream(socket.getInputStream()));
				Request response = (Request) ois.readObject();
				String type = response.getType();
				/////////////////////////
				// ä�� �޽��� ����
				/////////////////////////	
				if (type.equals("message")) {
					messageManager.receiveMessage(response);
				} else if(type.equals("notice")) {
					messageManager.receiveNotice(response);
				}
				/////////////////////////
				// ���� ������ ģ�� �߰� ��û ���� �� ���� �� ����
				/////////////////////////
				else if (type.equals("addfriend")) {
					System.out.println("ģ����û ������ ���� ��");
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");
					if(errors!=null) {
						if (errors.isEmpty()) {
							new MessageFrame("���������� ��û�� ���½��ϴ�.\n",true);						
						} else {
							System.out.println("errors ��Ƽ �ƴ�");
							if (errors.get("invalidResponseId") !=null) {
								new MessageFrame("��ȿ���� ���� ���̵� �Դϴ�.\n",false);							
							} else if (errors.get("alreadyfriend") != null) {
								new MessageFrame("�̹� ģ���Դϴ�.",true);							
							} else if (errors.get("SQLException") != null) {
								new MessageFrame("�������� ������ ���� �ʽ��ϴ�.\n����� �������ּ���",false);							
							}
						}
					}					
				}
				/////////////////////////
				// ���ο� ������� ���� ģ�� ��û ��
				/////////////////////////
				else if (type.equals("addfriend_req")) {
					System.out.println("���ο� ������� ���� ģ�� ��û ��");		
					MemberVO fromUser = (MemberVO) response.getAttribute("from");
					String message = fromUser.getName() + "(" + fromUser.getEmail() + ")������ ���� ģ����û�� �Խ��ϴ�.";
					int result = JOptionPane.showConfirmDialog(null, message);
					if (result == JOptionPane.OK_OPTION) {
						Request request = new Request("responseadd");
						request.setParameter("from", String.valueOf(admin.getId()));
						request.setParameter("to", String.valueOf(fromUser.getId()));
						request.setParameter("status", "1");
						send(request);
						MemberController.getInstance().addFriend(fromUser);
						mainFrm.fillFriendsList();
					}
				}
				
				/////////////////////////
				// ���� ģ�� ��û ������ ���� ��
				/////////////////////////
				else if (type.equals("addfriend_res")) {	
					System.out.println("addfriend_res");
					MemberVO fromUser = (MemberVO) response.getAttribute("from");
					String message = fromUser.getName() + "(" + fromUser.getEmail() + ")\n���� ģ�� �����Ͽ����ϴ�.";
					new MessageFrame(message,true);					
					friendsManager.addFriend(fromUser);
					mainFrm.fillFriendsList();
				}

				/////////////////////////
				// ���� ģ�� ���� ��û ������
				/////////////////////////
				else if (type.equals("responseadd")) {
					System.out.println("ģ�� ��û ��� ���� �� ����");
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");
					if (!errors.isEmpty()) {
						String message = "�������� ������ ���� �ʽ��ϴ�.\n";
						if (errors.get("invalidToId") == Boolean.TRUE) {
							message = "��ȿ���� ���� ���̵� �Դϴ�.\n";
						} else if (errors.get("SQLException")) {
							////
						}
						new MessageFrame(message,false);
					} else { // ���������� ��� ���������� �ƹ��͵� ���� ����
						// empty
					}
				}
				/////////////////////////
				// ģ�� �α���
				/////////////////////////
				else if (type.equals("friendlogin")) {
					MemberVO friend = (MemberVO) response.getAttribute("loginuser");
					friendsManager.addLogOnUser(friend);
					mainFrm.fillFriendsList();
					new FriendsLoginFrame(friend.getName());					
				}

				/////////////////////////
				// ģ�� �α� �ƿ�
				/////////////////////////
				else if (type.equals("friendlogout")) {
					System.out.println("ģ�� �α׾ƿ� ��û ����");
					MemberVO friend = (MemberVO) response.getAttribute("logoutuser");
					friendsManager.removeLogOnUser(friend);
					mainFrm.fillFriendsList();					
				}

				/////////////////////////
				// ģ�� �α��� üũ
				/////////////////////////
				else if (type.equals("checklogin")) {
					System.out.println("�α��� üũ ���� ����");
					MemberVO friend = (MemberVO) response.getAttribute("checkuser");
					String result = response.getParameter("result");
					if (result.equals("true")) {
						System.out.println("�α�����");
						friendsManager.addLogOnUser(friend);
						mainFrm.fillFriendsList();
					}
				}	
				
				//////////////////
				// ���� ��û ����
				//////////////////
				else if(type.equals("remote_req")) {
					System.out.println("���� ��û �ޱ� �ؼ�");
					String groupId = response.getParameter("groupid");
					String from = response.getParameter("from");					
					String to = response.getParameter("to");
					System.out.println(groupId);
					String name = friendsManager.getMember(from).getName();
					int result = JOptionPane.showConfirmDialog(null,name+"���Ƿκ��� ������ �����Ͻðڽ��ϱ�?");
					Request request = new Request("remote_res");
					if (result == JOptionPane.OK_OPTION) {
						request.setParameter("denied","false");
						String ip = response.getParameter("ip");
						String port = response.getParameter("port");
						new RemoteClient(ip,Integer.parseInt(port));						
					} else {						
						request.setParameter("denied","true");						
					}					
					request.setParameter("from",to);
					request.setParameter("to",from);
					request.setParameter("groupid",groupId);
					send(request);
				}
				//////////////////
				// ���� ��û �Ѱ� ���� or �α׾ƿ�
				//////////////////
				else if(type.equals("remote_res")) {
					System.out.println("���� ��û �ؼ�");
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");
					if (errors == null) {
						String isDenied = response.getParameter("denied");
						if(isDenied.equals("true")) {
							String from = response.getParameter("from");
							new MessageFrame(friendsManager.getMember(from).getName()+"���� \n������ �����Ͽ����ϴ�.",false);							
						}
					} else { // �α׾ƿ���
						if(errors.get("logout")!=null) {
							String logoutUser = response.getParameter("logoutuser");	
							new MessageFrame(friendsManager.getMember(logoutUser).getName()+"���� \n���� �α׾ƿ� �����Դϴ�.",false);
						}
					}									
				}
				
//				else if(type.equals("file_req")) {
//					Request request = new Request("file_req");			
//					request.setParameter("groupid",groupId);
//					request.setAttribute("file",selectedFile);						
//					request.setParameter("port", String.valueOf(serverSocket.getLocalPort()));
//					request.setParameter("ip",ip);
//					request.setParameter("from",user);
//					
//					int result = JOptionPane.showConfirmDialog(null, "��������?");
//					if(result == JOptionPane.OK_OPTION) {
//						
//					}
//					
//				} else if(type.equals("file_res")) {
//					
//				}
				
				
				
				//////////////////
				// �α��� ��û�� ���� ����
				/////////////////
				else if(type.equals("login")) { //�α��ο� ���� ����
					System.out.println("�α��ο� ���� ���� ��");									
					Map<String, Boolean> errors = (Map<String, Boolean>) response.getAttribute("errors");
					if (errors.isEmpty()) { // login success
						admin = (MemberVO)response.getAttribute("loginuser");
						friendsManager = MemberController.getInstance();
						friendsManager.initFriendsList(admin
								,response.getParameter("ip")
								,(List<MemberVO>)response.getAttribute("onlist")
								,(List<MemberVO>)response.getAttribute("offlist")
								,(List<MemberVO>) response.getAttribute("pendinglist"));							
						messageManager = MessageController.getInstance();
						loginFrm.dispose();
						mainFrm = new MainFrame(response);
						mainFrm.setVisible(true);						
					} else { //login fail
						if(errors.get("notmatch")!=null) { //��� Ʋ��
							new MessageFrame("ID/PASSWORD�� Ȯ�����ּ���.",false);
							break;
						} else if(errors.get("duplicatedLogin")!=null) { //���� �α���
							int result = JOptionPane.showConfirmDialog(null,"���� �������� ������ �ֽ��ϴ�.�α׾ƿ� ��Ű�ڽ��ϱ�?");							
							if (result == JOptionPane.OK_OPTION) {
														
							} else {						
														
							}									
						}					
					}					
				}				
				else{
					//empty
				}
			} catch (IOException e) {
				e.printStackTrace();									
				////////////////
				//���� ���� ����ٴ� �޽���					
				////////////////
				new MessageFrame("�������� ������ ������ϴ�.",false);
				mainFrm.dispose();
				new LoginFrame("Face On Talk").setVisible(true);
				break;				
			} catch(ClassNotFoundException ignored) {
				ignored.printStackTrace();
			} 
		}						
	}
	
	
	
	//User -> Server 
	public void send(Request request) {
		Thread thread = new Thread() {
			@Override
			public void run() {
				try {
					ObjectOutputStream oos = new ObjectOutputStream(
							new BufferedOutputStream(socket.getOutputStream()));					
					// send to server
					oos.writeObject(request);
					oos.flush();					
				} catch (IOException e) {
					e.printStackTrace();
				}
			}
		};
		thread.start();
	}
	
	public Socket getSocket() {
		return socket;
	}
}
