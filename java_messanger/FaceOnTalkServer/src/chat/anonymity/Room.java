package chat.anonymity;

import java.util.Hashtable;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Room {
	/*-----------------------------------------------------
	 *�� �濡 �����ϴ� �������� Ŀ�ؼ��� ���
	 * �̴� �� �濡 ������ �����鳢���� ä���� ���� ��.
	 *-----------------------------------------------------*/
	public Vector<Client_Connection> userList = new Vector<Client_Connection>();
	/*-----------------------------------------------------
	 * ���� ����ų� ��� �濡 ������ ���⿡�� �߰�.
	 * ������ ����� �Ǵ� ��쿡 ���ȴ�. 
	 *-----------------------------------------------------*/	
	public static Map<String, Client_Connection> user_In_room = new Hashtable<String, Client_Connection>();
	/*-----------------------------------------------------
	 * ���ǿ� �ִ� �������� ���� Map�� ����.
	 * ���� �������� �ʰ� �ѹ��� �ϸ� ������ ����� ���̰� �ȴ�. 
	 *-----------------------------------------------------*/
	public static Map<String, Client_Connection> waituser = new Hashtable<String, Client_Connection>();
	/*-----------------------------------------------------
	 * 	������ �̸��� Ŀ�ؼ��� �����ϴ� Map
	 *  �� Map�� �� 1���� ������ ������ �ȴ�.
	 *-----------------------------------------------------*/
	public static Map<String, Client_Connection> roomchief = new Hashtable<String, Client_Connection>(); 
	
	private String roomName;
	static String chief; 				//�����̸�
	public Room(String roomName) {
		this.roomName = roomName;
	}
	/*-----------------------------------------------------
	 * �� �濡 ������ ������ Ŀ�ؼ��� ������.
	 *-----------------------------------------------------*/
	public void addUser(Client_Connection user){
		userList.add(user);
	}
	
	/*-----------------------------------------------------
	 * �� �濡�� ������ �������� Ŀ�ؼ��� ������. 
	 *-----------------------------------------------------*/
	public void removeUser(Client_Connection user){
		userList.remove(user);
	}
	/*-----------------------------------------------------
	 * �� �濡 �ִ� �����鿡�Ը� ������ �����Ѵ�.
	 *-----------------------------------------------------*/
	public void broadCast(String msg){
		for(Client_Connection user : userList){
			user.sendMsg(msg);
		}
	}
	/*-----------------------------------------------------
	 * 		������ �߰��Ѵ�.	(�̸�, ��������)
	 *-----------------------------------------------------*/
	public void addchief(String name, Client_Connection client){
		roomchief.put(name, client);
	}
	/*-----------------------------------------------------
	 * 		������ ������ �Ǹ� ������ �����Ѵ�.
	 *-----------------------------------------------------*/
	public void removechief(String name){
//		System.out.println("���� ���� ���� : " + name);		//Ȯ�ο�.
		roomchief.remove(name);
	}
	/*-----------------------------------------------------
	 *  �� �����ڿ� ������ ���ؼ� ������ Ŀ�ؼǿ��Ը�
	 *  ������ �����Ѵ�.
	 *-----------------------------------------------------*/
	public static void getchief(String username, String roomname){
		if(roomchief.get(chief) == user_In_room.get(username)){		//��
			user_In_room.get(username).sendMsg(MsgInfo.CHIEF+"/"+1);	//����
		}else{
			System.out.println("�������...");							//Ȯ�ο�
		}
	}
	public String getRoomName() {
		return roomName;
	}
	/*-----------------------------------------------------
	 * 	���� ��� ������ Ŀ�ؼ��� ���.
	 *-----------------------------------------------------*/
	public void addWaitUser(String username, Client_Connection client){
		waituser.put(username, client);
	}
	/*-----------------------------------------------------
	 * 	���� ����ų� �濡 �����ϰ� �Ǹ� ���� �������� ����
	 *-----------------------------------------------------*/
	public void removeWaitUser(String username){
		waituser.remove(username);
	}
	/*-----------------------------------------------------
	 * 	���� ����ų�, �濡 �����ϴ� �������� put��.
	 *-----------------------------------------------------*/
	public void addChatUser(String username, Client_Connection client){
		///////////////
		// test code - �� ���� ���� �� ���� ��� ��� ���� �׽�Ʈ
		user_In_room.remove(username, client);
		/////////////////
		
		user_In_room.put(username, client);
	}
	/*-----------------------------------------------------
	 *  �濡�� ������ ��쿡�� ����.
	 *-----------------------------------------------------*/
	public void removeChatUser(String username){
		user_In_room.remove(username);
	}
	
	/* -----------------------------------------------------------------------------
	 * 				**********		�� �� 1		***********
	 * 	ä�� ������ ���ϴ� �κ�.
	 * user_In_room���� �� ���� ������ �̸��� Ŀ�ؼ��� �ִ�. �׷��⿡ Ű�¸� �޾ƿ���
	 * �� ������ ��ȭ���� ������ �� �ִ�. 
	 * --------------------------------------------------------------------------- */
	public void ChatUserList(String roomname){
		Set<String> lists = user_In_room.keySet();					//Ű�� ������.				
		String data = lists.toString();								//String���� ��ȯ.
		String list = data.substring(1, data.length()-1);			//[ ] ����.
		String[] userlists = list.split(", ");						// , �̹Ƿ� �߶󳽴�.
//		System.out.println("�������� �� : " + userlists.length);	//Ȯ�ο�
		if(roomchief.size() == 0){	//roomchief�� ������ �ִٸ� size�� 1�̴�.
			chief = userlists[0];
//			System.out.println("�������� ������ ���� : " + chief);		//Ȯ�ο�
			addchief(userlists[0], user_In_room.get(userlists[0]));		//���� ����!
		}else{
//			System.out.println("������ �̹� �ֽ��ϴ�.");				//Ȯ�ο�
		}
		String sendlist = "";
		for(int i =0; i<userlists.length; i++){
			sendlist += userlists[i]+"/";
		}
//		System.out.println("������ : " + sendlist);					//Ȯ�ο�
		broadCast(MsgInfo.CHATUSER+"/"+sendlist+chief);	 	//�� �濡 �ִ� �������Ը� ����.
		getchief(chief, roomname);
	}
	
	/* ----------------------------------------------------------------------------------------
	 *			***********			�� �� 2		****************** 
	 * 	���� ������ ���ϴ� �κ�.
	 * �׳� Ű�¸� ���ؼ� ���ǿ� �ִ� �����鿡�Ը� �����ϰ� �޴� ������
	 * Ű���� ó���� �Ŀ� ȭ�鿡 ����ϴ� ���.
	 * ---------------------------------------------------------------------------------------- */
	public void WaitUserList(){
		broadCast(MsgInfo.USERLIST+"/"+waituser.keySet());
	}
}
