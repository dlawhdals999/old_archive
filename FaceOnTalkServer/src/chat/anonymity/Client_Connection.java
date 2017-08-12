package chat.anonymity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;

/*
 * copyrigth@changcoding
 */
public class Client_Connection implements Runnable{
	private Socket socket;
	private PrintWriter networkWriter;
	private BufferedReader networkReader;
	private String name;
	String myRoomName;
	private String roomName;
	Room room;
	
	public Client_Connection(Socket socket) {
		this.socket = socket;
		try {
			networkWriter = new PrintWriter(socket.getOutputStream());
			networkReader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMsg(String msg){
		networkWriter.print(msg + "\n");
		networkWriter.flush();
	}
/*==================================================================
 *			NEW������ waitRoom���� ���� �ȴ�.
 *			���⼭�� Main ��ȭ���� �ּҸ� �����ͼ� �۾��� �ϰ� �ȴ�.
 * ==================================================================*/
	private void waitRoom(String username){
		Room_Manager.addallUserList(name, this);								//��ü ������ �ڱ� �ڽ� �߰�!! (���� ������!);
		Room waitroom = Room_Manager.getRoom("Main");							//Main�� �ּҸ� ������
		waitroom.addUser(this);													//Room�� �ڱ� �ڽ��� �߰�
//		System.out.println("���ǿ����� userlist : " + waitroom.userList.size());
		waitroom.addWaitUser(username, this);									//Room�� ��� ������ �ڱ� �ڽ��� �߰�
		waitroom.broadCast(MsgInfo.MAIN+"/"+name + " �Բ��� " +waitroom.getRoomName()+ "�� �����ϼ̽��ϴ�.");
		waitroom.WaitUserList();												//���ǿ� ��� ���� ���� ����
		waitroom.broadCast(MsgInfo.ROOMLIST+"/"+Room_Manager.getRoomList());		//���ǿ� ��ȭ�� ��� ����
	}
/*==================================================================
 *			MAKEROOM������ makeRoom���� ���� �ȴ�.
 *		���⼭�� Main������ �۾��� ������� �ϴ� �濡 ���� �۾��� �Ѵ�.
 * ==================================================================*/
	private void makeRoom(String roomName, String username){
		Room waitroom = Room_Manager.getRoom("Main");		//������ �ּҸ� ������
		waitroom.removeUser(this);							//Main���� �ڱ� �ڽ� ����.
//		System.out.println("�� ����⿡���� userlist : " + waitroom.userList.size());
		waitroom.removeWaitUser(username);					//���� ä�� ������Ͽ��� �ڱ� �ڽ� ����
		waitroom.WaitUserList();							//���ǿ�  ��� ���� ���� ����
		waitroom.broadCast(MsgInfo.ROOMLIST+"/"+Room_Manager.getRoomList());			//��ȭ�� ����Ʈ ����
		Room_Manager.makeRoom(roomName);						//RoomManager�� �� ����
//		System.out.println(name + "�Բ��� " + roomName +"���� �����մϴ�.");		//���� Ȯ�ο�.
		this.sendMsg(MsgInfo.MAKEROOM+"/"+roomName);		//�ڱ� �ڽſ��� �� ������ ���� ����
		room = Room_Manager.getRoom(roomName);				//���� ���� �ּҸ� ������
		
//		///////////
//		// test code
//		room.removeChatUser(username);							//�������� ä�� ���� ��Ͽ��� �ڱ� �ڽ� ����.
//		room.removechief(name);
//		///////////////

		room.addUser(this);									//���� ���� ���� ��Ͽ� �ڱ� �ڽ� ���
		room.addChatUser(username, this);					//���� ���� ä�� ���� ��Ͽ� �ڱ� �ڽ� ���
		room.ChatUserList(roomName);						//���� �濡 ä�� ����(�ڱ� �ڽ�) ����Ʈ ����
		room.broadCast(roomName+"/"+"["+name+"]" + " �Բ��� "+"["+room.getRoomName()+"]"+ " ��ȭ�濡 �����ϼ̽��ϴ�.");
/*		if(username.equals(Room.chief)){
			this.sendMsg("������ ������ ��ٸ��� �ֽ��ϴ�.,,....");
			//room.broadCast("");
		}else {
			room.broadCast(roomName+"/"+"["+Room.chief+"]"+"���� �����Դϴ�.");
		}*/
		room.broadCast(roomName+"/"+"["+Room.chief+"]"+"���� �����Դϴ�.");
	}
	/*==================================================================
	 *		ENTERROOMM���� enterRoom���� ���� �ȴ�.
	 *		���⼭�� ���ǿ����� �۾��� �����Ϸ��� �ϴ� �濡 ���� �۾��� �Ѵ�.
	 * ==================================================================*/
	private void enterRoom(String roomName){
		Room waitroom = Room_Manager.getRoom("Main");		//������ �ּҸ� ������
		waitroom.removeUser(this);							//������ ���� ��Ͽ��� �ڱ� �ڽ� ����
//		System.out.println("���� ���������� uesrlist ������ : " + waitroom.userList.size());		//Ȯ�ο�
		waitroom.removeWaitUser(name);						//���� ��� �������� �ڱ� �ڽ� ����.
		waitroom.WaitUserList();							//���ǿ� ��� ���� ���� ����.
		
//		
//		/////////////
//		// test code
//		room.removeChatUser(name);							//�������� ä�� ���� ��Ͽ��� �ڱ� �ڽ� ����.
//		room.removechief(name);
//		///////////////
//		
		
		this.sendMsg(MsgInfo.ENTER+"/"+roomName);			//�ڱ� �ڽſ��� ���� ����
		room = Room_Manager.getRoom(roomName);				//�����Ϸ��� ���� �ּҸ� ������
		room.addUser(this);									//�����ϴ� ���� ���� ��Ͽ� �ڱ� �ڽ� ���
		room.addChatUser(name, this);						//�����ϴ� ���� ä�� ���� ��Ͽ� �ڱ� �ڽ� ���		
		
		room.broadCast(roomName+"/"+"["+Room.chief+"]"+"���� �����Դϴ�.");
		room.broadCast(roomName+"/"+"["+name+"]" + " �Բ��� "+"["+room.getRoomName()+"]"+ " ��ȭ�濡 �����ϼ̽��ϴ�.");
		room.ChatUserList(roomName);						//������ �����鿡�� ���� ���� ä�� ���� ����Ʈ ����
	}
	/*==================================================================
	 *			GOWAIT������ goWaitRoom���� ���� �ȴ�.
	 *		���⼭�� ���� ���� �۾��� ����(Main�� �۾��� �ϰ� �ȴ�.)
	 * ==================================================================*/	
	private void goWaitRoom(String username, String preRoom){
		room = Room_Manager.roomMap.get(preRoom);			//���� ���� �ּҸ� ������.
		room.removeChatUser(name);							//�������� ä�� ���� ��Ͽ��� �ڱ� �ڽ� ����.
		room.removechief(name);								//���� �̾��ٸ� ���� ����.
		//��ȭ���� ����ڰ� 0�̶�� ���� ���������̱� ������ �ʿ��� �����ؾ� �Ѵ�.
		if(Room.user_In_room.size() == 0){
			Room_Manager.removeRoom(preRoom);				//�ʿ��� �� ����
			System.out.println("�� ���� �Ϸ�");				//���� Ȯ�ο�
		}else{
			room.broadCast(preRoom+"/"+"["+name+"]"+"�Բ��� [����]�� �����̽��ϴ�.");
			room.removeUser(this);							//���� �濡�� ���� ��Ͽ��� �ڱ� �ڽ� ����
			room.ChatUserList(preRoom);						//���� ���� ä�� ���� ��� ����.
		}
		this.sendMsg(MsgInfo.GOWAIT+"/"+ name);				//�ڱ� �ڽſ��� ������ ����
		Room waitroom = Room_Manager.roomMap.get("Main");
		waitroom.addUser(this);								//���� ������ �ڱ� �ڽ��� ���
		waitroom.addWaitUser(username, this);				//���� ��� ���� ��Ͽ� �ڱ� �ڽ� ���.
		waitroom.broadCast("Main"+"/"+name + " �Բ��� " +"[����]�� ���ƿ��̽��ϴ�.");
//		waitroom.broadCast("Main"+"/"+name + " �Բ��� " +waitroom.getRoomName()+ "�� �����̽��ϴ�.");
		waitroom.WaitUserList();							//���� ��� ���� ��� ����
		waitroom.broadCast(MsgInfo.ROOMLIST+"/"+Room_Manager.getRoomList());		//��ȭ�� ����Ʈ ����
	}
	
	/*==================================================================
	 *				KICK������ kick()���� ���� �ȴ�.
	 *		���� ���ϴ� ������ client�ּҸ� �����ͼ� �۾��� �Ѵ�.
	 * ==================================================================*/			
	private void kick(String target, String roomname){
		Client_Connection kicktarget = Room.user_In_room.get(target);		//���� ������ clinet�ּ� ��������
		kicktarget.goWaitRoom(target, roomname);		//���� ������ clinet�ּҸ� �̿��� goWaitRoomȣ��
	}
	/*==================================================================
	 *				ALLUSER������ allUser()���� ���� �ȴ�.
	 *		������ �� �ִ� ��� clinet�� �ּҿ� list�� �����Ѵ�.
	 * ==================================================================*/		
	private void allUser(Client_Connection client){
		//String idlist = Room_Manager.getIDlist();
		//System.out.println(idlist);			//Ȯ�ο�
		//this.sendMsg(MsgInfo.SELUSER+"/"+idlist);

		/////////
		// test cod - ���� ������ ���
		String waituserlist = Room_Manager.getRoomList();
		System.out.println(waituserlist);
		this.sendMsg(MsgInfo.SELUSER+"/"+waituserlist);
		//////////
		
		
	}
	/*==================================================================
	 *				SHOWUSER������ showUser()���� ���� �ȴ�.
	 *		��� ������ list�� ��û�� clinet�� �ּҿ� list�� �����Ѵ�.
	 * ==================================================================*/		
	private void showUser(Client_Connection client){
//		String idlist = Room_Manager.getIDlist();
//		System.out.println(idlist);			//Ȯ�ο�
//		this.sendMsg(MsgInfo.SHOWUSER+"/"+idlist);
		
		/////////////
		// test code - ���� ������ ���
		String waituserlist = Room_Manager.getRoomList();
		System.out.println(waituserlist);
		this.sendMsg(MsgInfo.SHOWUSER+"/"+waituserlist);
		//////////////
		
	}
	
	@Override
	public void run() {
		observeClientMessage();
	}

	private void observeClientMessage(){
		try{
			String msg;
			myRoomName = "Main"; 
			while( (msg = networkReader.readLine()) != null){
//				System.out.println(name + " ���Լ� �� ���� " + msg);
				String parsingData[] = msg.split("/");
	/*==================================================================
	 *			N	E	W       -------->>		waitRoom() �޼ҵ��..
	 * ==================================================================*/
				if(MsgInfo.NEW.equals( parsingData[0] )){
					this.name = parsingData[1];
					waitRoom(name);
	/*==================================================================
	 *			E	X	I	T   -------->>		break�� while�� �����Ŵ.
	 * ==================================================================*/
				}else if(MsgInfo.EXIT.equals( parsingData[0] )){
					Room_Manager.removeallUSerList(name);		//��ü �������� �ڱ� �ڽ� ���.
					room = Room_Manager.getRoom("Main");		
					room.removeWaitUser(name);					//��� �������� �ڱ� �ڽ� ����.
					room.removeUser(this);						//Room�� ��ϵ� �ڱ� �ڽ� ����.
//					System.out.println(name + " ����õ�");		//Ȯ�ο�
					room.broadCast(myRoomName+"/"+name + " �Բ��� �����ϼ̽��ϴ�.");
					room.WaitUserList();						//��ȭ�� ���� ����Ʈ ����.
					break;
	/*==================================================================
	 *						C	H	A	T
	 * ==================================================================*/				
				}else if(MsgInfo.CHAT.equals( parsingData[0] )){
					// [CHAT]/���̸�/����!!
					//���� �ڽ��� �ִ� �� �̸��� ã�Ƽ� ��ȭ�� �ְ� ����.
					myRoomName = parsingData[1];		//���� �� ����
					String sendtext = "";
					try{
						sendtext = parsingData[2];		//�����̸� Exception �߻�!!
					}catch(ArrayIndexOutOfBoundsException arr){
						sendtext=" ";					//��ĭ���� ��ü�Ѵ�.
					}
//					System.out.println("���� �ִ� �� : " + myRoomName);		//Ȯ�ο�
					room = Room_Manager.roomMap.get(myRoomName);
					room.broadCast(myRoomName+"/"+name + " : " + sendtext);
	/*==================================================================
	 *				MAKEROOM	-------->>		makeRoom()�޼ҵ��..
	 * ==================================================================*/
				}else if(MsgInfo.MAKEROOM.equals( parsingData[0])){
					Room_Manager.makeRoom(parsingData[1]);
					roomName = parsingData[1];
					myRoomName = roomName;
					makeRoom(roomName, name);
		/*==================================================================
		 *			ENTER		-------->>		enterRoom()�޼ҵ��.		
		 * ==================================================================*/
				}else if(MsgInfo.ENTER.equals( parsingData[0])){
					roomName = parsingData[1];
					enterRoom(roomName);
	/*==================================================================
	 *			GOWAIT		-------->>		goWaitRoom()�޼ҵ��..
	 * ==================================================================*/
				}else if(MsgInfo.GOWAIT.equals( parsingData[0]) ){
					//[GOWAIT]/��ȭ��/���� ��ȭ�� �������� ������ ��.
					this.name = parsingData[1];			//�ڽ��� ��ȭ���� name���� ����.
					myRoomName = "Main";				//��ȭ���� Main���� ����.
//					System.out.println("GoWait���� ��ȭ��� ��ȭ�� : " + "Main" + " : " + name);
					goWaitRoom(name, parsingData[2]);
				
	/*==================================================================
	 *					KCIK ���� -------> kick()�޼����
	 * ==================================================================*/					
				}else if(MsgInfo.KICK.equals( parsingData[0]) ){
					String target = parsingData[1];
					String roomname = parsingData[2];
					kick(target, roomname);
	/*==================================================================
	 *					SELUSER -------> selUser()�޼����
	 *						������ ���� â���� ����
	 * ==================================================================*/						
				}else if(MsgInfo.SELUSER.equals( parsingData[0]) ){
					allUser(this);
	/*==================================================================
	 *					SHOWUSER -------> showUser()�޼����
	 *						��� ������ â���� ����
	 * ==================================================================*/				
				}else if(MsgInfo.SHOWUSER.equals( parsingData[0]) ){
					showUser(this);
				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}finally{
			exitClient();
		}
	}
	/* ----------------------------------------------------------------------------------------
	 * 							EXIT ��� ó���� �� �κ��� ��µ�.
	 * ----------------------------------------------------------------------------------------	*/	
	private void exitClient(){
		room = Room_Manager.getRoom("Main");			//������ �ּҸ� ������
		System.out.println("��������" + room.userList.size());		//Ȯ�ο�
			/* �� �� �� �� */
		networkWriter.close();
		try {networkReader.close();} catch (IOException e1) {}
		try {socket.close();} catch (IOException e) {}
	}
}