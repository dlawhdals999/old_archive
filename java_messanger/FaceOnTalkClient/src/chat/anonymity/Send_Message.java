package chat.anonymity;

import java.io.IOException;


public class Send_Message {
	public static void chattingStart(String consoleData){
		System.out.println(consoleData);
		try{
			if("".equals(consoleData)){
				consoleData = " ";
				anonyChatFrame.sendMsg(MsgInfo.CHAT,consoleData);		//�׳� ���͸� ġ���� ���� ���;� ��.
			}else if("EXIT".equals(consoleData)){		//exit�� ����
				anonyChatFrame.sendMsg(MsgInfo.EXIT,null);
			}else{										//�� �ܴ� ��ȭ�̹Ƿ�..
				anonyChatFrame.sendMsg(MsgInfo.CHAT,consoleData);
			}
		}catch(Exception e1){
			try {anonyChatFrame.networkWriter.close();} catch (IOException e) {}
			try {anonyChatFrame.networkReader.close();} catch (IOException e) {}
			try {anonyChatFrame.socket.close();       } catch (IOException e) {}
		}
	}
}
