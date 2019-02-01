package chat.anonymity;

import java.io.*;
import java.net.*;

class Listener_Of_Client extends Thread{
	BufferedWriter networkWriter;
	BufferedReader networkReader;
	Socket socket;
	String name;
	static String[] parsingData;
	static ChatRoom startchat;
	Wait_Room waitroom;
	Listener_Of_Client(BufferedWriter networkWriter, BufferedReader networkReader, Socket socket ,Wait_Room waitroom){
		this.networkWriter = networkWriter;
		this.networkReader = networkReader;
		this.socket = socket;
		this.waitroom = waitroom;
	}
	@Override
	public void run() {
		try {
			String line;
			while( (line = networkReader.readLine()) != null ){
				System.out.println("�������� �� �޼��� : " + line);
				parsingData = line.split("/");
	/*==================================================================
	 *				 ���ǿ� ����ؾ��� �޼���
	 * ==================================================================*/
				if( line.startsWith(MsgInfo.MAIN)){		
					Wait_Room.showText.append(parsingData[1]+"\n");
				}
	/*==================================================================
	 *				 line�� USERLIST�� �����ϸ�..
	 * ==================================================================*/					
				else if(line.startsWith(MsgInfo.USERLIST)){		//������ ����
					waitroom.IDlist.removeAll();			//������ IDlist �ʱ�ȭ
					String data = parsingData[1].substring(1, parsingData[1].length()-1);
					String lists = new String();
					// Main, aaa �� ������ , �̹Ƿ� �߶󳽴�.
					String[] roomlists = data.split(", ");
					for(int i = 0; i<roomlists.length; i++){
						lists = roomlists[i];				
						waitroom.IDlist.add(lists);			//������ idlist�� ���
					}
				}
	/*==================================================================
	 *				 line�� ROOMLIST�� �����ϸ� �� ������ ���
	 * ==================================================================*/
				else if(line.startsWith(MsgInfo.ROOMLIST)){	
					waitroom.roomList.removeAll();
					for(int i=1; i<parsingData.length; i++){
						waitroom.roomList.add(parsingData[i]);	//list�� ���
					}
					waitroom.roomList.remove("Main");
				}
	/*==================================================================
	 *				 line�� MAKEROOM���� �����ϸ� �� ����
	 * ==================================================================*/
				else if( line.startsWith(MsgInfo.MAKEROOM)){		
					// [MAKEROOM]/���̸� ����.
					startchat = new ChatRoom(parsingData[1]);
					startchat.showFrame(anonyChatFrame.name);
				}
	/*==================================================================
	 *				 line�� ENTER���� �����ϸ� �濡 ����
	 * ==================================================================*/
				else if( line.startsWith(MsgInfo.ENTER)) {
					// [ENTER]/���̸� �����̹Ƿ�
					startchat = new ChatRoom(parsingData[1]);
					startchat.dispose();
					startchat.showFrame(anonyChatFrame.name);
				}
	/*==================================================================
	 *				 line�� CHATUSER�� �����ϸ� ��ȭ�濡 ����ؾ� ��.
	 * ==================================================================*/			
				else if( line.startsWith(MsgInfo.CHATUSER)){
			// [CHATUSER]/����1/����2/����3/.....����x/�����ȭ��
					startchat.IDlist.removeAll();						//��ȭ�� ID����Ʈ �ʱ�ȭ
					for(int i=1; i<parsingData.length-1; i++){			//����1���� ����x����
//						System.out.print(parsingData[i]+"\t");			//Ȯ�ο�
//						System.out.println();							//Ȯ�ο�
						startchat.IDlist.add(parsingData[i]);			//list�� ��� ����1~����x������
					}
					startchat.IDlist.remove(parsingData[parsingData.length-1]);		//�����ȭ���� ���� ������ ����.
					startchat.IDlist.add("[����]"+parsingData[parsingData.length-1]);	//�׸��� �����ȭ���� ���.
//					System.out.println("����Ʈ�� ��������Ʈ ��� �Ϸ�");	//Ȯ��
				}
	/*==================================================================
	 *				 line�� GOWAIT�� �����ϸ�.. ���Ƿ� ���� ��.
	 * ==================================================================*/
				else if( line.startsWith(MsgInfo.GOWAIT)){
					startchat.setVisible(false);	//��ȭ�� â �����
					waitroom.setVisible(true);		//������� �ٽ� ���̰� ��.
					Wait_Room.showText.setText("");
				
	/*==================================================================
	 *				line�� CHIEF�̸� �ڽ��� �����ڵ带 1 �Ǵ� 0���� �Է��Ѵ�.
	 * ==================================================================*/					
				}else if( line.startsWith(MsgInfo.CHIEF) ){
					startchat.chiefcode = Integer.parseInt(parsingData[1]);
	/*==================================================================
	 *				line�� KICK�̸� ����~! �������Ƿ�.. ���Ƿ�..
	 * ==================================================================*/
				}else if( line.startsWith(MsgInfo.KICK) ){
					startchat.setVisible(false);	//��ȭ�� â �����
					waitroom.setVisible(true);		//������� �ٽ� ���̰� ��.
					Wait_Room.showText.setText("");
				}else if( line.startsWith(MsgInfo.SHOWUSER)){
					Show_All showuser= new Show_All();
					for(int i = 1; i<parsingData.length; i++){
						Show_All.idlist.add(parsingData[i]);
					}
	/*==================================================================
	 *				 �� �ܿ��� �� ä�ù濡 ����ؾ� ��.
	 * ==================================================================*/				
				}else{			
					ChatRoom.showText.append(parsingData[1]+"\n");
				}
			}
			System.out.println("����");
			networkWriter.close();
			try {networkReader.close();} catch (IOException e1) {}
			try {socket.close();} catch (IOException e) {}
		} catch (IOException e) {
			System.out.println("���� ����");
		}
	}
}