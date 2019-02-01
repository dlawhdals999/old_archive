package chat.anonymity;

import java.util.Hashtable;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Vector;

public class Room_Manager {
	//��ü ������!!
	public static Vector<Client_Connection> allUserList = new Vector<Client_Connection>();
	
	/*
	 *  ������!! �̺κ��� ������ 1:1 ��ȭ�� ���� �ʿ�� �Ѵ�.
	 *  ��ȭ������ ���������� �����;� �ϱ� ������ Map(���̵�, ��������) �������� ����Ѵ�.
	 */
	public static Map<String, Client_Connection> connectedUser = new Hashtable<String, Client_Connection>();
	
	/*
	 * �Ʒ��� ���� ����� ���ﶧ ����ϴ� ���̴�.
	 * ����� put(���̸�, �������� ���� ����) �����Ҷ��� remove(���̸�)���� ����Ѵ�.
	 */
	public static Map<String, Room> roomMap = new Hashtable<String, Room>();
	
//	/*
//	 * 1:1��ȭ�� ��û�� �������� Ŀ�ؼ�.
//	 */
//	public static Vector<Client_Connection> user = new Vector<Client_Connection>();
//	
	/*-----------------------------------------------------
	 * 			���� ����� Map�� ���� �ֱ�.
	 *-----------------------------------------------------*/
	public static void makeRoom(String roomName){
		Room newRoom = new Room(roomName);
		roomMap.put(roomName, newRoom);
	}
	/*-----------------------------------------------------
	 * 			���̸��� ����.
	 *-----------------------------------------------------*/
	public static Room getRoom(String roomName){		
		return roomMap.get(roomName);
	}
	/*-----------------------------------------------------
	 * 			���� �����. remove(���̸�)
	 *-----------------------------------------------------*/
	public static void removeRoom(String roomName){
		roomMap.remove(roomName);
	}
	/*-----------------------------------------------------
	 *	�����ϴ� ������ connectedUser(Map)�� put��Ų��. 
	 *-----------------------------------------------------*/
	public static void addallUserList(String username, Client_Connection client){
		connectedUser.put(username, client);
	}
	/*-----------------------------------------------------
	 *	���� ����� connectedUser(Map)���� remove�Ѵ�.
	 *-----------------------------------------------------*/
	public static void removeallUSerList(String username){
		connectedUser.remove(username);
	}
	/*-----------------------------------------------------
	 * 			ID-List�� �������� �κ�.
	 *-----------------------------------------------------*/	
	public static String getIDlist(){
		Set<String> lists = connectedUser.keySet();			//Ű���� �����´�.
		String data = lists.toString();						//String���� ��ȯ
		String list = data.substring(1, data.length()-1);	//[ �� ]�� ����.
		String[] userlists = list.split(", ");				// aaa, bbb �� ������ , �̹Ƿ� �߶󳽴�.
		String sendlist = "";
		for(int i =0; i<userlists.length; i++){
			sendlist += userlists[i]+"/";					//�������� �߰�
		}
		return sendlist;
	}

	/*-----------------------------------------------------
	 * 	�濡 ���� ������ �����Ѵ�.
	 *-----------------------------------------------------*/
	public static String getRoomList(){
		StringBuffer sb = new StringBuffer();
		for(Room room : roomMap.values()){
			sb.append( room.getRoomName() + "/");
		}
		return sb.toString(); 
	}
	/*-----------------------------------------------------
	 * ���̱� ������ �̸��� �Է��ϸ� �ڵ������� ��ȭ�� 
	 * �´� ��������(Ű)�� ������ �ȴ�.	
	 *-----------------------------------------------------*/
	private static Client_Connection findUser(String userName){
		return connectedUser.get(userName);
	}
}
